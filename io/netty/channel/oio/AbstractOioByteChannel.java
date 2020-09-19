/*     */ package io.netty.channel.oio;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.FileRegion;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.socket.ChannelInputShutdownEvent;
/*     */ import io.netty.channel.socket.ChannelInputShutdownReadComplete;
/*     */ import io.netty.util.internal.StringUtil;
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
/*     */ public abstract class AbstractOioByteChannel
/*     */   extends AbstractOioChannel
/*     */ {
/*  40 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
/*  41 */   private static final String EXPECTED_TYPES = " (expected: " + 
/*  42 */     StringUtil.simpleClassName(ByteBuf.class) + ", " + 
/*  43 */     StringUtil.simpleClassName(FileRegion.class) + ')';
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractOioByteChannel(Channel parent) {
/*  49 */     super(parent);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelMetadata metadata() {
/*  54 */     return METADATA;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean isInputShutdown();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract ChannelFuture shutdownInput();
/*     */ 
/*     */ 
/*     */   
/*     */   private void closeOnRead(ChannelPipeline pipeline) {
/*  70 */     if (isOpen()) {
/*  71 */       if (Boolean.TRUE.equals(config().getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
/*  72 */         shutdownInput();
/*  73 */         pipeline.fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
/*     */       } else {
/*  75 */         unsafe().close(unsafe().voidPromise());
/*     */       } 
/*  77 */       pipeline.fireUserEventTriggered(ChannelInputShutdownReadComplete.INSTANCE);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleReadException(ChannelPipeline pipeline, ByteBuf byteBuf, Throwable cause, boolean close, RecvByteBufAllocator.Handle allocHandle) {
/*  83 */     if (byteBuf != null) {
/*  84 */       if (byteBuf.isReadable()) {
/*  85 */         this.readPending = false;
/*  86 */         pipeline.fireChannelRead(byteBuf);
/*     */       } else {
/*  88 */         byteBuf.release();
/*     */       } 
/*     */     }
/*  91 */     allocHandle.readComplete();
/*  92 */     pipeline.fireChannelReadComplete();
/*  93 */     pipeline.fireExceptionCaught(cause);
/*  94 */     if (close || cause instanceof java.io.IOException) {
/*  95 */       closeOnRead(pipeline);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doRead() {
/* 101 */     ChannelConfig config = config();
/* 102 */     if (isInputShutdown() || !this.readPending) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     this.readPending = false;
/*     */     
/* 111 */     ChannelPipeline pipeline = pipeline();
/* 112 */     ByteBufAllocator allocator = config.getAllocator();
/* 113 */     RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
/* 114 */     allocHandle.reset(config);
/*     */     
/* 116 */     ByteBuf byteBuf = null;
/* 117 */     boolean close = false;
/* 118 */     boolean readData = false;
/*     */     try {
/* 120 */       byteBuf = allocHandle.allocate(allocator);
/*     */       do {
/* 122 */         allocHandle.lastBytesRead(doReadBytes(byteBuf));
/* 123 */         if (allocHandle.lastBytesRead() <= 0) {
/* 124 */           if (!byteBuf.isReadable()) {
/* 125 */             byteBuf.release();
/* 126 */             byteBuf = null;
/* 127 */             close = (allocHandle.lastBytesRead() < 0);
/* 128 */             if (close)
/*     */             {
/* 130 */               this.readPending = false;
/*     */             }
/*     */           } 
/*     */           break;
/*     */         } 
/* 135 */         readData = true;
/*     */ 
/*     */         
/* 138 */         int available = available();
/* 139 */         if (available <= 0) {
/*     */           break;
/*     */         }
/*     */ 
/*     */         
/* 144 */         if (byteBuf.isWritable())
/* 145 */           continue;  int capacity = byteBuf.capacity();
/* 146 */         int maxCapacity = byteBuf.maxCapacity();
/* 147 */         if (capacity == maxCapacity) {
/* 148 */           allocHandle.incMessagesRead(1);
/* 149 */           this.readPending = false;
/* 150 */           pipeline.fireChannelRead(byteBuf);
/* 151 */           byteBuf = allocHandle.allocate(allocator);
/*     */         } else {
/* 153 */           int writerIndex = byteBuf.writerIndex();
/* 154 */           if (writerIndex + available > maxCapacity) {
/* 155 */             byteBuf.capacity(maxCapacity);
/*     */           } else {
/* 157 */             byteBuf.ensureWritable(available);
/*     */           }
/*     */         
/*     */         } 
/* 161 */       } while (allocHandle.continueReading());
/*     */       
/* 163 */       if (byteBuf != null) {
/*     */ 
/*     */         
/* 166 */         if (byteBuf.isReadable()) {
/* 167 */           this.readPending = false;
/* 168 */           pipeline.fireChannelRead(byteBuf);
/*     */         } else {
/* 170 */           byteBuf.release();
/*     */         } 
/* 172 */         byteBuf = null;
/*     */       } 
/*     */       
/* 175 */       if (readData) {
/* 176 */         allocHandle.readComplete();
/* 177 */         pipeline.fireChannelReadComplete();
/*     */       } 
/*     */       
/* 180 */       if (close) {
/* 181 */         closeOnRead(pipeline);
/*     */       }
/* 183 */     } catch (Throwable t) {
/* 184 */       handleReadException(pipeline, byteBuf, t, close, allocHandle);
/*     */     } finally {
/* 186 */       if (this.readPending || config.isAutoRead() || (!readData && isActive()))
/*     */       {
/*     */         
/* 189 */         read();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception {
/*     */     while (true) {
/* 197 */       Object msg = in.current();
/* 198 */       if (msg == null) {
/*     */         break;
/*     */       }
/*     */       
/* 202 */       if (msg instanceof ByteBuf) {
/* 203 */         ByteBuf buf = (ByteBuf)msg;
/* 204 */         int readableBytes = buf.readableBytes();
/* 205 */         while (readableBytes > 0) {
/* 206 */           doWriteBytes(buf);
/* 207 */           int newReadableBytes = buf.readableBytes();
/* 208 */           in.progress((readableBytes - newReadableBytes));
/* 209 */           readableBytes = newReadableBytes;
/*     */         } 
/* 211 */         in.remove(); continue;
/* 212 */       }  if (msg instanceof FileRegion) {
/* 213 */         FileRegion region = (FileRegion)msg;
/* 214 */         long transferred = region.transferred();
/* 215 */         doWriteFileRegion(region);
/* 216 */         in.progress(region.transferred() - transferred);
/* 217 */         in.remove(); continue;
/*     */       } 
/* 219 */       in.remove(new UnsupportedOperationException("unsupported message type: " + 
/* 220 */             StringUtil.simpleClassName(msg)));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Object filterOutboundMessage(Object msg) throws Exception {
/* 227 */     if (msg instanceof ByteBuf || msg instanceof FileRegion) {
/* 228 */       return msg;
/*     */     }
/*     */     
/* 231 */     throw new UnsupportedOperationException("unsupported message type: " + 
/* 232 */         StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
/*     */   }
/*     */   
/*     */   protected abstract int available();
/*     */   
/*     */   protected abstract int doReadBytes(ByteBuf paramByteBuf) throws Exception;
/*     */   
/*     */   protected abstract void doWriteBytes(ByteBuf paramByteBuf) throws Exception;
/*     */   
/*     */   protected abstract void doWriteFileRegion(FileRegion paramFileRegion) throws Exception;
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\oio\AbstractOioByteChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */