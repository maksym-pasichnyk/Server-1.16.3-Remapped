/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelOutboundHandler;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.FutureListener;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractSniHandler<T>
/*     */   extends ByteToMessageDecoder
/*     */   implements ChannelOutboundHandler
/*     */ {
/*     */   private static final int MAX_SSL_RECORDS = 4;
/*  49 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractSniHandler.class);
/*     */   
/*     */   private boolean handshakeFailed;
/*     */   
/*     */   private boolean suppressRead;
/*     */   private boolean readPending;
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/*  57 */     if (!this.suppressRead && !this.handshakeFailed) {
/*  58 */       int writerIndex = in.writerIndex();
/*     */       
/*     */       try {
/*  61 */         for (int i = 0; i < 4; i++) {
/*  62 */           int len, majorVersion, readerIndex = in.readerIndex();
/*  63 */           int readableBytes = writerIndex - readerIndex;
/*  64 */           if (readableBytes < 5) {
/*     */             return;
/*     */           }
/*     */ 
/*     */           
/*  69 */           int command = in.getUnsignedByte(readerIndex);
/*     */ 
/*     */           
/*  72 */           switch (command) {
/*     */             case 20:
/*     */             case 21:
/*  75 */               len = SslUtils.getEncryptedPacketLength(in, readerIndex);
/*     */ 
/*     */               
/*  78 */               if (len == -2) {
/*  79 */                 this.handshakeFailed = true;
/*     */                 
/*  81 */                 NotSslRecordException e = new NotSslRecordException("not an SSL/TLS record: " + ByteBufUtil.hexDump(in));
/*  82 */                 in.skipBytes(in.readableBytes());
/*  83 */                 ctx.fireUserEventTriggered(new SniCompletionEvent(e));
/*  84 */                 SslUtils.handleHandshakeFailure(ctx, e, true);
/*  85 */                 throw e;
/*     */               } 
/*  87 */               if (len == -1 || writerIndex - readerIndex - 5 < len) {
/*     */                 return;
/*     */               }
/*     */ 
/*     */ 
/*     */               
/*  93 */               in.skipBytes(len);
/*     */               break;
/*     */             case 22:
/*  96 */               majorVersion = in.getUnsignedByte(readerIndex + 1);
/*     */ 
/*     */               
/*  99 */               if (majorVersion == 3) {
/* 100 */                 int packetLength = in.getUnsignedShort(readerIndex + 3) + 5;
/*     */ 
/*     */                 
/* 103 */                 if (readableBytes < packetLength) {
/*     */                   return;
/*     */                 }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 128 */                 int endOffset = readerIndex + packetLength;
/* 129 */                 int offset = readerIndex + 43;
/*     */                 
/* 131 */                 if (endOffset - offset < 6) {
/*     */                   break;
/*     */                 }
/*     */                 
/* 135 */                 int sessionIdLength = in.getUnsignedByte(offset);
/* 136 */                 offset += sessionIdLength + 1;
/*     */                 
/* 138 */                 int cipherSuitesLength = in.getUnsignedShort(offset);
/* 139 */                 offset += cipherSuitesLength + 2;
/*     */                 
/* 141 */                 int compressionMethodLength = in.getUnsignedByte(offset);
/* 142 */                 offset += compressionMethodLength + 1;
/*     */                 
/* 144 */                 int extensionsLength = in.getUnsignedShort(offset);
/* 145 */                 offset += 2;
/* 146 */                 int extensionsLimit = offset + extensionsLength;
/*     */                 
/* 148 */                 if (extensionsLimit > endOffset) {
/*     */                   break;
/*     */                 }
/*     */ 
/*     */ 
/*     */                 
/* 154 */                 while (extensionsLimit - offset >= 4) {
/*     */ 
/*     */ 
/*     */                   
/* 158 */                   int extensionType = in.getUnsignedShort(offset);
/* 159 */                   offset += 2;
/*     */                   
/* 161 */                   int extensionLength = in.getUnsignedShort(offset);
/* 162 */                   offset += 2;
/*     */                   
/* 164 */                   if (extensionsLimit - offset < extensionLength) {
/*     */                     break;
/*     */                   }
/*     */ 
/*     */ 
/*     */                   
/* 170 */                   if (extensionType == 0) {
/* 171 */                     offset += 2;
/* 172 */                     if (extensionsLimit - offset < 3) {
/*     */                       break;
/*     */                     }
/*     */                     
/* 176 */                     int serverNameType = in.getUnsignedByte(offset);
/* 177 */                     offset++;
/*     */                     
/* 179 */                     if (serverNameType == 0) {
/* 180 */                       int serverNameLength = in.getUnsignedShort(offset);
/* 181 */                       offset += 2;
/*     */                       
/* 183 */                       if (extensionsLimit - offset < serverNameLength) {
/*     */                         break;
/*     */                       }
/*     */                       
/* 187 */                       String hostname = in.toString(offset, serverNameLength, CharsetUtil.US_ASCII);
/*     */ 
/*     */                       
/*     */                       try {
/* 191 */                         select(ctx, hostname.toLowerCase(Locale.US));
/* 192 */                       } catch (Throwable t) {
/* 193 */                         PlatformDependent.throwException(t);
/*     */                       } 
/*     */                       
/*     */                       return;
/*     */                     } 
/*     */                     
/*     */                     break;
/*     */                   } 
/*     */                   
/* 202 */                   offset += extensionLength;
/*     */                 } 
/*     */               } 
/*     */               break;
/*     */             
/*     */             default:
/*     */               break;
/*     */           } 
/*     */         } 
/* 211 */       } catch (NotSslRecordException e) {
/*     */         
/* 213 */         throw e;
/* 214 */       } catch (Exception e) {
/*     */         
/* 216 */         if (logger.isDebugEnabled()) {
/* 217 */           logger.debug("Unexpected client hello packet: " + ByteBufUtil.hexDump(in), e);
/*     */         }
/*     */       } 
/*     */       
/* 221 */       select(ctx, null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void select(final ChannelHandlerContext ctx, final String hostname) throws Exception {
/* 226 */     Future<T> future = lookup(ctx, hostname);
/* 227 */     if (future.isDone()) {
/* 228 */       fireSniCompletionEvent(ctx, hostname, future);
/* 229 */       onLookupComplete(ctx, hostname, future);
/*     */     } else {
/* 231 */       this.suppressRead = true;
/* 232 */       future.addListener((GenericFutureListener)new FutureListener<T>()
/*     */           {
/*     */             public void operationComplete(Future<T> future) throws Exception {
/*     */               try {
/* 236 */                 AbstractSniHandler.this.suppressRead = false;
/*     */                 try {
/* 238 */                   AbstractSniHandler.this.fireSniCompletionEvent(ctx, hostname, future);
/* 239 */                   AbstractSniHandler.this.onLookupComplete(ctx, hostname, future);
/* 240 */                 } catch (DecoderException err) {
/* 241 */                   ctx.fireExceptionCaught((Throwable)err);
/* 242 */                 } catch (Exception cause) {
/* 243 */                   ctx.fireExceptionCaught((Throwable)new DecoderException(cause));
/* 244 */                 } catch (Throwable cause) {
/* 245 */                   ctx.fireExceptionCaught(cause);
/*     */                 } 
/*     */               } finally {
/* 248 */                 if (AbstractSniHandler.this.readPending) {
/* 249 */                   AbstractSniHandler.this.readPending = false;
/* 250 */                   ctx.read();
/*     */                 } 
/*     */               } 
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void fireSniCompletionEvent(ChannelHandlerContext ctx, String hostname, Future<T> future) {
/* 259 */     Throwable cause = future.cause();
/* 260 */     if (cause == null) {
/* 261 */       ctx.fireUserEventTriggered(new SniCompletionEvent(hostname));
/*     */     } else {
/* 263 */       ctx.fireUserEventTriggered(new SniCompletionEvent(hostname, cause));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void read(ChannelHandlerContext ctx) throws Exception {
/* 285 */     if (this.suppressRead) {
/* 286 */       this.readPending = true;
/*     */     } else {
/* 288 */       ctx.read();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
/* 294 */     ctx.bind(localAddress, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
/* 300 */     ctx.connect(remoteAddress, localAddress, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
/* 305 */     ctx.disconnect(promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
/* 310 */     ctx.close(promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
/* 315 */     ctx.deregister(promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/* 320 */     ctx.write(msg, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush(ChannelHandlerContext ctx) throws Exception {
/* 325 */     ctx.flush();
/*     */   }
/*     */   
/*     */   protected abstract Future<T> lookup(ChannelHandlerContext paramChannelHandlerContext, String paramString) throws Exception;
/*     */   
/*     */   protected abstract void onLookupComplete(ChannelHandlerContext paramChannelHandlerContext, String paramString, Future<T> paramFuture) throws Exception;
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\AbstractSniHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */