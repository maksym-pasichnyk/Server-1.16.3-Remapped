/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.FileRegion;
/*     */ import io.netty.handler.codec.MessageToMessageEncoder;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class HttpObjectEncoder<H extends HttpMessage>
/*     */   extends MessageToMessageEncoder<Object>
/*     */ {
/*     */   static final int CRLF_SHORT = 3338;
/*     */   private static final int ZERO_CRLF_MEDIUM = 3149066;
/*  52 */   private static final byte[] ZERO_CRLF_CRLF = new byte[] { 48, 13, 10, 13, 10 };
/*  53 */   private static final ByteBuf CRLF_BUF = Unpooled.unreleasableBuffer(Unpooled.directBuffer(2).writeByte(13).writeByte(10));
/*  54 */   private static final ByteBuf ZERO_CRLF_CRLF_BUF = Unpooled.unreleasableBuffer(Unpooled.directBuffer(ZERO_CRLF_CRLF.length)
/*  55 */       .writeBytes(ZERO_CRLF_CRLF));
/*     */   
/*     */   private static final float HEADERS_WEIGHT_NEW = 0.2F;
/*     */   
/*     */   private static final float HEADERS_WEIGHT_HISTORICAL = 0.8F;
/*     */   private static final float TRAILERS_WEIGHT_NEW = 0.2F;
/*     */   private static final float TRAILERS_WEIGHT_HISTORICAL = 0.8F;
/*     */   private static final int ST_INIT = 0;
/*     */   private static final int ST_CONTENT_NON_CHUNK = 1;
/*     */   private static final int ST_CONTENT_CHUNK = 2;
/*     */   private static final int ST_CONTENT_ALWAYS_EMPTY = 3;
/*  66 */   private int state = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   private float headersEncodedSizeAccumulator = 256.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   private float trailersEncodedSizeAccumulator = 256.0F;
/*     */ 
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
/*  83 */     ByteBuf buf = null;
/*  84 */     if (msg instanceof HttpMessage) {
/*  85 */       if (this.state != 0) {
/*  86 */         throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
/*     */       }
/*     */ 
/*     */       
/*  90 */       HttpMessage httpMessage = (HttpMessage)msg;
/*     */       
/*  92 */       buf = ctx.alloc().buffer((int)this.headersEncodedSizeAccumulator);
/*     */       
/*  94 */       encodeInitialLine(buf, (H)httpMessage);
/*  95 */       this
/*  96 */         .state = isContentAlwaysEmpty((H)httpMessage) ? 3 : (HttpUtil.isTransferEncodingChunked(httpMessage) ? 2 : 1);
/*     */       
/*  98 */       sanitizeHeadersBeforeEncode((H)httpMessage, (this.state == 3));
/*     */       
/* 100 */       encodeHeaders(httpMessage.headers(), buf);
/* 101 */       ByteBufUtil.writeShortBE(buf, 3338);
/*     */       
/* 103 */       this.headersEncodedSizeAccumulator = 0.2F * padSizeForAccumulation(buf.readableBytes()) + 0.8F * this.headersEncodedSizeAccumulator;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 112 */     if (msg instanceof ByteBuf) {
/* 113 */       ByteBuf potentialEmptyBuf = (ByteBuf)msg;
/* 114 */       if (!potentialEmptyBuf.isReadable()) {
/* 115 */         out.add(potentialEmptyBuf.retain());
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 120 */     if (msg instanceof HttpContent || msg instanceof ByteBuf || msg instanceof FileRegion) {
/* 121 */       long contentLength; switch (this.state) {
/*     */         case 0:
/* 123 */           throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
/*     */         case 1:
/* 125 */           contentLength = contentLength(msg);
/* 126 */           if (contentLength > 0L) {
/* 127 */             if (buf != null && buf.writableBytes() >= contentLength && msg instanceof HttpContent) {
/*     */               
/* 129 */               buf.writeBytes(((HttpContent)msg).content());
/* 130 */               out.add(buf);
/*     */             } else {
/* 132 */               if (buf != null) {
/* 133 */                 out.add(buf);
/*     */               }
/* 135 */               out.add(encodeAndRetain(msg));
/*     */             } 
/*     */             
/* 138 */             if (msg instanceof LastHttpContent) {
/* 139 */               this.state = 0;
/*     */             }
/*     */             break;
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 3:
/* 148 */           if (buf != null) {
/*     */             
/* 150 */             out.add(buf);
/*     */ 
/*     */ 
/*     */             
/*     */             break;
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 159 */           out.add(Unpooled.EMPTY_BUFFER);
/*     */           break;
/*     */ 
/*     */         
/*     */         case 2:
/* 164 */           if (buf != null)
/*     */           {
/* 166 */             out.add(buf);
/*     */           }
/* 168 */           encodeChunkedContent(ctx, msg, contentLength(msg), out);
/*     */           break;
/*     */         
/*     */         default:
/* 172 */           throw new Error();
/*     */       } 
/*     */       
/* 175 */       if (msg instanceof LastHttpContent) {
/* 176 */         this.state = 0;
/*     */       }
/* 178 */     } else if (buf != null) {
/* 179 */       out.add(buf);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void encodeHeaders(HttpHeaders headers, ByteBuf buf) {
/* 187 */     Iterator<Map.Entry<CharSequence, CharSequence>> iter = headers.iteratorCharSequence();
/* 188 */     while (iter.hasNext()) {
/* 189 */       Map.Entry<CharSequence, CharSequence> header = iter.next();
/* 190 */       HttpHeadersEncoder.encoderHeader(header.getKey(), header.getValue(), buf);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void encodeChunkedContent(ChannelHandlerContext ctx, Object msg, long contentLength, List<Object> out) {
/* 195 */     if (contentLength > 0L) {
/* 196 */       String lengthHex = Long.toHexString(contentLength);
/* 197 */       ByteBuf buf = ctx.alloc().buffer(lengthHex.length() + 2);
/* 198 */       buf.writeCharSequence(lengthHex, CharsetUtil.US_ASCII);
/* 199 */       ByteBufUtil.writeShortBE(buf, 3338);
/* 200 */       out.add(buf);
/* 201 */       out.add(encodeAndRetain(msg));
/* 202 */       out.add(CRLF_BUF.duplicate());
/*     */     } 
/*     */     
/* 205 */     if (msg instanceof LastHttpContent) {
/* 206 */       HttpHeaders headers = ((LastHttpContent)msg).trailingHeaders();
/* 207 */       if (headers.isEmpty()) {
/* 208 */         out.add(ZERO_CRLF_CRLF_BUF.duplicate());
/*     */       } else {
/* 210 */         ByteBuf buf = ctx.alloc().buffer((int)this.trailersEncodedSizeAccumulator);
/* 211 */         ByteBufUtil.writeMediumBE(buf, 3149066);
/* 212 */         encodeHeaders(headers, buf);
/* 213 */         ByteBufUtil.writeShortBE(buf, 3338);
/* 214 */         this.trailersEncodedSizeAccumulator = 0.2F * padSizeForAccumulation(buf.readableBytes()) + 0.8F * this.trailersEncodedSizeAccumulator;
/*     */         
/* 216 */         out.add(buf);
/*     */       } 
/* 218 */     } else if (contentLength == 0L) {
/*     */ 
/*     */       
/* 221 */       out.add(encodeAndRetain(msg));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sanitizeHeadersBeforeEncode(H msg, boolean isAlwaysEmpty) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isContentAlwaysEmpty(H msg) {
/* 240 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean acceptOutboundMessage(Object msg) throws Exception {
/* 245 */     return (msg instanceof HttpObject || msg instanceof ByteBuf || msg instanceof FileRegion);
/*     */   }
/*     */   
/*     */   private static Object encodeAndRetain(Object msg) {
/* 249 */     if (msg instanceof ByteBuf) {
/* 250 */       return ((ByteBuf)msg).retain();
/*     */     }
/* 252 */     if (msg instanceof HttpContent) {
/* 253 */       return ((HttpContent)msg).content().retain();
/*     */     }
/* 255 */     if (msg instanceof FileRegion) {
/* 256 */       return ((FileRegion)msg).retain();
/*     */     }
/* 258 */     throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
/*     */   }
/*     */   
/*     */   private static long contentLength(Object msg) {
/* 262 */     if (msg instanceof HttpContent) {
/* 263 */       return ((HttpContent)msg).content().readableBytes();
/*     */     }
/* 265 */     if (msg instanceof ByteBuf) {
/* 266 */       return ((ByteBuf)msg).readableBytes();
/*     */     }
/* 268 */     if (msg instanceof FileRegion) {
/* 269 */       return ((FileRegion)msg).count();
/*     */     }
/* 271 */     throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int padSizeForAccumulation(int readableBytes) {
/* 281 */     return (readableBytes << 2) / 3;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected static void encodeAscii(String s, ByteBuf buf) {
/* 286 */     buf.writeCharSequence(s, CharsetUtil.US_ASCII);
/*     */   }
/*     */   
/*     */   protected abstract void encodeInitialLine(ByteBuf paramByteBuf, H paramH) throws Exception;
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\HttpObjectEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */