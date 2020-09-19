/*     */ package io.netty.channel.kqueue;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.AbstractChannel;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.DefaultFileRegion;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.FileRegion;
/*     */ import io.netty.channel.socket.DuplexChannel;
/*     */ import io.netty.channel.unix.FileDescriptor;
/*     */ import io.netty.channel.unix.IovArray;
/*     */ import io.netty.channel.unix.SocketWritableByteChannel;
/*     */ import io.netty.channel.unix.UnixChannelUtil;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.util.concurrent.Executor;
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
/*     */ public abstract class AbstractKQueueStreamChannel
/*     */   extends AbstractKQueueChannel
/*     */   implements DuplexChannel
/*     */ {
/*  53 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractKQueueStreamChannel.class);
/*  54 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);
/*  55 */   private static final String EXPECTED_TYPES = " (expected: " + 
/*  56 */     StringUtil.simpleClassName(ByteBuf.class) + ", " + 
/*  57 */     StringUtil.simpleClassName(DefaultFileRegion.class) + ')';
/*     */   
/*  59 */   private final Runnable flushTask = new Runnable()
/*     */     {
/*     */       
/*     */       public void run()
/*     */       {
/*  64 */         ((AbstractKQueueChannel.AbstractKQueueUnsafe)AbstractKQueueStreamChannel.this.unsafe()).flush0();
/*     */       }
/*     */     };
/*     */   private WritableByteChannel byteChannel;
/*     */   AbstractKQueueStreamChannel(Channel parent, BsdSocket fd, boolean active) {
/*  69 */     super(parent, fd, active);
/*     */   }
/*     */   
/*     */   AbstractKQueueStreamChannel(Channel parent, BsdSocket fd, SocketAddress remote) {
/*  73 */     super(parent, fd, remote);
/*     */   }
/*     */   
/*     */   AbstractKQueueStreamChannel(BsdSocket fd) {
/*  77 */     this((Channel)null, fd, isSoErrorZero(fd));
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractKQueueChannel.AbstractKQueueUnsafe newUnsafe() {
/*  82 */     return new KQueueStreamUnsafe();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelMetadata metadata() {
/*  87 */     return METADATA;
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
/*     */   private int writeBytes(ChannelOutboundBuffer in, ByteBuf buf) throws Exception {
/* 105 */     int readableBytes = buf.readableBytes();
/* 106 */     if (readableBytes == 0) {
/* 107 */       in.remove();
/* 108 */       return 0;
/*     */     } 
/*     */     
/* 111 */     if (buf.hasMemoryAddress() || buf.nioBufferCount() == 1) {
/* 112 */       return doWriteBytes(in, buf);
/*     */     }
/* 114 */     ByteBuffer[] nioBuffers = buf.nioBuffers();
/* 115 */     return writeBytesMultiple(in, nioBuffers, nioBuffers.length, readableBytes, 
/* 116 */         config().getMaxBytesPerGatheringWrite());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void adjustMaxBytesPerGatheringWrite(long attempted, long written, long oldMaxBytesPerGatheringWrite) {
/* 124 */     if (attempted == written) {
/* 125 */       if (attempted << 1L > oldMaxBytesPerGatheringWrite) {
/* 126 */         config().setMaxBytesPerGatheringWrite(attempted << 1L);
/*     */       }
/* 128 */     } else if (attempted > 4096L && written < attempted >>> 1L) {
/* 129 */       config().setMaxBytesPerGatheringWrite(attempted >>> 1L);
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
/*     */   private int writeBytesMultiple(ChannelOutboundBuffer in, IovArray array) throws IOException {
/* 149 */     long expectedWrittenBytes = array.size();
/* 150 */     assert expectedWrittenBytes != 0L;
/* 151 */     int cnt = array.count();
/* 152 */     assert cnt != 0;
/*     */     
/* 154 */     long localWrittenBytes = this.socket.writevAddresses(array.memoryAddress(0), cnt);
/* 155 */     if (localWrittenBytes > 0L) {
/* 156 */       adjustMaxBytesPerGatheringWrite(expectedWrittenBytes, localWrittenBytes, array.maxBytes());
/* 157 */       in.removeBytes(localWrittenBytes);
/* 158 */       return 1;
/*     */     } 
/* 160 */     return Integer.MAX_VALUE;
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
/*     */ 
/*     */ 
/*     */   
/*     */   private int writeBytesMultiple(ChannelOutboundBuffer in, ByteBuffer[] nioBuffers, int nioBufferCnt, long expectedWrittenBytes, long maxBytesPerGatheringWrite) throws IOException {
/* 184 */     assert expectedWrittenBytes != 0L;
/* 185 */     if (expectedWrittenBytes > maxBytesPerGatheringWrite) {
/* 186 */       expectedWrittenBytes = maxBytesPerGatheringWrite;
/*     */     }
/*     */     
/* 189 */     long localWrittenBytes = this.socket.writev(nioBuffers, 0, nioBufferCnt, expectedWrittenBytes);
/* 190 */     if (localWrittenBytes > 0L) {
/* 191 */       adjustMaxBytesPerGatheringWrite(expectedWrittenBytes, localWrittenBytes, maxBytesPerGatheringWrite);
/* 192 */       in.removeBytes(localWrittenBytes);
/* 193 */       return 1;
/*     */     } 
/* 195 */     return Integer.MAX_VALUE;
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
/*     */   private int writeDefaultFileRegion(ChannelOutboundBuffer in, DefaultFileRegion region) throws Exception {
/* 213 */     long regionCount = region.count();
/* 214 */     if (region.transferred() >= regionCount) {
/* 215 */       in.remove();
/* 216 */       return 0;
/*     */     } 
/*     */     
/* 219 */     long offset = region.transferred();
/* 220 */     long flushedAmount = this.socket.sendFile(region, region.position(), offset, regionCount - offset);
/* 221 */     if (flushedAmount > 0L) {
/* 222 */       in.progress(flushedAmount);
/* 223 */       if (region.transferred() >= regionCount) {
/* 224 */         in.remove();
/*     */       }
/* 226 */       return 1;
/*     */     } 
/* 228 */     return Integer.MAX_VALUE;
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
/*     */   private int writeFileRegion(ChannelOutboundBuffer in, FileRegion region) throws Exception {
/* 246 */     if (region.transferred() >= region.count()) {
/* 247 */       in.remove();
/* 248 */       return 0;
/*     */     } 
/*     */     
/* 251 */     if (this.byteChannel == null) {
/* 252 */       this.byteChannel = (WritableByteChannel)new KQueueSocketWritableByteChannel();
/*     */     }
/* 254 */     long flushedAmount = region.transferTo(this.byteChannel, region.transferred());
/* 255 */     if (flushedAmount > 0L) {
/* 256 */       in.progress(flushedAmount);
/* 257 */       if (region.transferred() >= region.count()) {
/* 258 */         in.remove();
/*     */       }
/* 260 */       return 1;
/*     */     } 
/* 262 */     return Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception {
/* 267 */     int writeSpinCount = config().getWriteSpinCount();
/*     */     do {
/* 269 */       int msgCount = in.size();
/*     */       
/* 271 */       if (msgCount > 1 && in.current() instanceof ByteBuf)
/* 272 */       { writeSpinCount -= doWriteMultiple(in); }
/* 273 */       else { if (msgCount == 0) {
/*     */           
/* 275 */           writeFilter(false);
/*     */           
/*     */           return;
/*     */         } 
/* 279 */         writeSpinCount -= doWriteSingle(in);
/*     */          }
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 285 */     while (writeSpinCount > 0);
/*     */     
/* 287 */     if (writeSpinCount == 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 292 */       writeFilter(false);
/*     */ 
/*     */       
/* 295 */       eventLoop().execute(this.flushTask);
/*     */     }
/*     */     else {
/*     */       
/* 299 */       writeFilter(true);
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
/*     */   protected int doWriteSingle(ChannelOutboundBuffer in) throws Exception {
/* 319 */     Object msg = in.current();
/* 320 */     if (msg instanceof ByteBuf)
/* 321 */       return writeBytes(in, (ByteBuf)msg); 
/* 322 */     if (msg instanceof DefaultFileRegion)
/* 323 */       return writeDefaultFileRegion(in, (DefaultFileRegion)msg); 
/* 324 */     if (msg instanceof FileRegion) {
/* 325 */       return writeFileRegion(in, (FileRegion)msg);
/*     */     }
/*     */     
/* 328 */     throw new Error();
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
/*     */   private int doWriteMultiple(ChannelOutboundBuffer in) throws Exception {
/* 347 */     long maxBytesPerGatheringWrite = config().getMaxBytesPerGatheringWrite();
/* 348 */     if (PlatformDependent.hasUnsafe()) {
/* 349 */       IovArray array = ((KQueueEventLoop)eventLoop()).cleanArray();
/* 350 */       array.maxBytes(maxBytesPerGatheringWrite);
/* 351 */       in.forEachFlushedMessage((ChannelOutboundBuffer.MessageProcessor)array);
/*     */       
/* 353 */       if (array.count() >= 1)
/*     */       {
/* 355 */         return writeBytesMultiple(in, array);
/*     */       }
/*     */     } else {
/* 358 */       ByteBuffer[] buffers = in.nioBuffers();
/* 359 */       int cnt = in.nioBufferCount();
/* 360 */       if (cnt >= 1)
/*     */       {
/* 362 */         return writeBytesMultiple(in, buffers, cnt, in.nioBufferSize(), maxBytesPerGatheringWrite);
/*     */       }
/*     */     } 
/*     */     
/* 366 */     in.removeBytes(0L);
/* 367 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object filterOutboundMessage(Object msg) {
/* 372 */     if (msg instanceof ByteBuf) {
/* 373 */       ByteBuf buf = (ByteBuf)msg;
/* 374 */       return UnixChannelUtil.isBufferCopyNeededForWrite(buf) ? newDirectBuffer(buf) : buf;
/*     */     } 
/*     */     
/* 377 */     if (msg instanceof FileRegion) {
/* 378 */       return msg;
/*     */     }
/*     */     
/* 381 */     throw new UnsupportedOperationException("unsupported message type: " + 
/* 382 */         StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void doShutdownOutput() throws Exception {
/* 388 */     this.socket.shutdown(false, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOutputShutdown() {
/* 393 */     return this.socket.isOutputShutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInputShutdown() {
/* 398 */     return this.socket.isInputShutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShutdown() {
/* 403 */     return this.socket.isShutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture shutdownOutput() {
/* 408 */     return shutdownOutput(newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture shutdownOutput(final ChannelPromise promise) {
/* 413 */     EventLoop loop = eventLoop();
/* 414 */     if (loop.inEventLoop()) {
/* 415 */       ((AbstractChannel.AbstractUnsafe)unsafe()).shutdownOutput(promise);
/*     */     } else {
/* 417 */       loop.execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 420 */               ((AbstractChannel.AbstractUnsafe)AbstractKQueueStreamChannel.this.unsafe()).shutdownOutput(promise);
/*     */             }
/*     */           });
/*     */     } 
/* 424 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture shutdownInput() {
/* 429 */     return shutdownInput(newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture shutdownInput(final ChannelPromise promise) {
/* 434 */     EventLoop loop = eventLoop();
/* 435 */     if (loop.inEventLoop()) {
/* 436 */       shutdownInput0(promise);
/*     */     } else {
/* 438 */       loop.execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 441 */               AbstractKQueueStreamChannel.this.shutdownInput0(promise);
/*     */             }
/*     */           });
/*     */     } 
/* 445 */     return (ChannelFuture)promise;
/*     */   }
/*     */   
/*     */   private void shutdownInput0(ChannelPromise promise) {
/*     */     try {
/* 450 */       this.socket.shutdown(true, false);
/* 451 */     } catch (Throwable cause) {
/* 452 */       promise.setFailure(cause);
/*     */       return;
/*     */     } 
/* 455 */     promise.setSuccess();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture shutdown() {
/* 460 */     return shutdown(newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture shutdown(final ChannelPromise promise) {
/* 465 */     ChannelFuture shutdownOutputFuture = shutdownOutput();
/* 466 */     if (shutdownOutputFuture.isDone()) {
/* 467 */       shutdownOutputDone(shutdownOutputFuture, promise);
/*     */     } else {
/* 469 */       shutdownOutputFuture.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture shutdownOutputFuture) throws Exception {
/* 472 */               AbstractKQueueStreamChannel.this.shutdownOutputDone(shutdownOutputFuture, promise);
/*     */             }
/*     */           });
/*     */     } 
/* 476 */     return (ChannelFuture)promise;
/*     */   }
/*     */   
/*     */   private void shutdownOutputDone(final ChannelFuture shutdownOutputFuture, final ChannelPromise promise) {
/* 480 */     ChannelFuture shutdownInputFuture = shutdownInput();
/* 481 */     if (shutdownInputFuture.isDone()) {
/* 482 */       shutdownDone(shutdownOutputFuture, shutdownInputFuture, promise);
/*     */     } else {
/* 484 */       shutdownInputFuture.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture shutdownInputFuture) throws Exception {
/* 487 */               AbstractKQueueStreamChannel.shutdownDone(shutdownOutputFuture, shutdownInputFuture, promise);
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void shutdownDone(ChannelFuture shutdownOutputFuture, ChannelFuture shutdownInputFuture, ChannelPromise promise) {
/* 496 */     Throwable shutdownOutputCause = shutdownOutputFuture.cause();
/* 497 */     Throwable shutdownInputCause = shutdownInputFuture.cause();
/* 498 */     if (shutdownOutputCause != null) {
/* 499 */       if (shutdownInputCause != null) {
/* 500 */         logger.debug("Exception suppressed because a previous exception occurred.", shutdownInputCause);
/*     */       }
/*     */       
/* 503 */       promise.setFailure(shutdownOutputCause);
/* 504 */     } else if (shutdownInputCause != null) {
/* 505 */       promise.setFailure(shutdownInputCause);
/*     */     } else {
/* 507 */       promise.setSuccess();
/*     */     } 
/*     */   }
/*     */   
/*     */   class KQueueStreamUnsafe
/*     */     extends AbstractKQueueChannel.AbstractKQueueUnsafe
/*     */   {
/*     */     protected Executor prepareToClose() {
/* 515 */       return super.prepareToClose();
/*     */     }
/*     */ 
/*     */     
/*     */     void readReady(KQueueRecvByteAllocatorHandle allocHandle) {
/* 520 */       KQueueChannelConfig kQueueChannelConfig = AbstractKQueueStreamChannel.this.config();
/* 521 */       if (AbstractKQueueStreamChannel.this.shouldBreakReadReady((ChannelConfig)kQueueChannelConfig)) {
/* 522 */         clearReadFilter0();
/*     */         return;
/*     */       } 
/* 525 */       ChannelPipeline pipeline = AbstractKQueueStreamChannel.this.pipeline();
/* 526 */       ByteBufAllocator allocator = kQueueChannelConfig.getAllocator();
/* 527 */       allocHandle.reset((ChannelConfig)kQueueChannelConfig);
/* 528 */       readReadyBefore();
/*     */       
/* 530 */       ByteBuf byteBuf = null;
/* 531 */       boolean close = false;
/*     */ 
/*     */       
/*     */       try {
/*     */         do {
/* 536 */           byteBuf = allocHandle.allocate(allocator);
/* 537 */           allocHandle.lastBytesRead(AbstractKQueueStreamChannel.this.doReadBytes(byteBuf));
/* 538 */           if (allocHandle.lastBytesRead() <= 0) {
/*     */             
/* 540 */             byteBuf.release();
/* 541 */             byteBuf = null;
/* 542 */             close = (allocHandle.lastBytesRead() < 0);
/* 543 */             if (close)
/*     */             {
/* 545 */               this.readPending = false;
/*     */             }
/*     */             break;
/*     */           } 
/* 549 */           allocHandle.incMessagesRead(1);
/* 550 */           this.readPending = false;
/* 551 */           pipeline.fireChannelRead(byteBuf);
/* 552 */           byteBuf = null;
/*     */           
/* 554 */           if (AbstractKQueueStreamChannel.this.shouldBreakReadReady((ChannelConfig)kQueueChannelConfig))
/*     */           {
/*     */             break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/* 568 */         while (allocHandle.continueReading());
/*     */         
/* 570 */         allocHandle.readComplete();
/* 571 */         pipeline.fireChannelReadComplete();
/*     */         
/* 573 */         if (close) {
/* 574 */           shutdownInput(false);
/*     */         }
/* 576 */       } catch (Throwable t) {
/* 577 */         handleReadException(pipeline, byteBuf, t, close, allocHandle);
/*     */       } finally {
/* 579 */         readReadyFinally((ChannelConfig)kQueueChannelConfig);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private void handleReadException(ChannelPipeline pipeline, ByteBuf byteBuf, Throwable cause, boolean close, KQueueRecvByteAllocatorHandle allocHandle) {
/* 585 */       if (byteBuf != null) {
/* 586 */         if (byteBuf.isReadable()) {
/* 587 */           this.readPending = false;
/* 588 */           pipeline.fireChannelRead(byteBuf);
/*     */         } else {
/* 590 */           byteBuf.release();
/*     */         } 
/*     */       }
/* 593 */       if (!failConnectPromise(cause)) {
/* 594 */         allocHandle.readComplete();
/* 595 */         pipeline.fireChannelReadComplete();
/* 596 */         pipeline.fireExceptionCaught(cause);
/* 597 */         if (close || cause instanceof IOException)
/* 598 */           shutdownInput(false); 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private final class KQueueSocketWritableByteChannel
/*     */     extends SocketWritableByteChannel {
/*     */     KQueueSocketWritableByteChannel() {
/* 606 */       super((FileDescriptor)AbstractKQueueStreamChannel.this.socket);
/*     */     }
/*     */ 
/*     */     
/*     */     protected ByteBufAllocator alloc() {
/* 611 */       return AbstractKQueueStreamChannel.this.alloc();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\AbstractKQueueStreamChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */