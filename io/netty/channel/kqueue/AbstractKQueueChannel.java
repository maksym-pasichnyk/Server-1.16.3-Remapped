/*     */ package io.netty.channel.kqueue;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.AbstractChannel;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.ConnectTimeoutException;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.socket.ChannelInputShutdownEvent;
/*     */ import io.netty.channel.socket.ChannelInputShutdownReadComplete;
/*     */ import io.netty.channel.socket.SocketChannelConfig;
/*     */ import io.netty.channel.unix.FileDescriptor;
/*     */ import io.netty.channel.unix.UnixChannel;
/*     */ import io.netty.channel.unix.UnixChannelUtil;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.io.IOException;
/*     */ import java.net.ConnectException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.AlreadyConnectedException;
/*     */ import java.nio.channels.ConnectionPendingException;
/*     */ import java.nio.channels.NotYetConnectedException;
/*     */ import java.nio.channels.UnresolvedAddressException;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ abstract class AbstractKQueueChannel
/*     */   extends AbstractChannel
/*     */   implements UnixChannel
/*     */ {
/*  58 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
/*     */   
/*     */   private ChannelPromise connectPromise;
/*     */   
/*     */   private ScheduledFuture<?> connectTimeoutFuture;
/*     */   
/*     */   private SocketAddress requestedRemoteAddress;
/*     */   
/*     */   final BsdSocket socket;
/*     */   
/*     */   private boolean readFilterEnabled = true;
/*     */   
/*     */   private boolean writeFilterEnabled;
/*     */   
/*     */   boolean readReadyRunnablePending;
/*     */   
/*     */   boolean inputClosedSeenErrorOnRead;
/*     */   
/*     */   long jniSelfPtr;
/*     */   
/*     */   protected volatile boolean active;
/*     */   
/*     */   private volatile SocketAddress local;
/*     */   
/*     */   private volatile SocketAddress remote;
/*     */ 
/*     */   
/*     */   AbstractKQueueChannel(Channel parent, BsdSocket fd, boolean active) {
/*  86 */     super(parent);
/*  87 */     this.socket = (BsdSocket)ObjectUtil.checkNotNull(fd, "fd");
/*  88 */     this.active = active;
/*  89 */     if (active) {
/*     */ 
/*     */       
/*  92 */       this.local = fd.localAddress();
/*  93 */       this.remote = fd.remoteAddress();
/*     */     } 
/*     */   }
/*     */   
/*     */   AbstractKQueueChannel(Channel parent, BsdSocket fd, SocketAddress remote) {
/*  98 */     super(parent);
/*  99 */     this.socket = (BsdSocket)ObjectUtil.checkNotNull(fd, "fd");
/* 100 */     this.active = true;
/*     */ 
/*     */     
/* 103 */     this.remote = remote;
/* 104 */     this.local = fd.localAddress();
/*     */   }
/*     */   
/*     */   static boolean isSoErrorZero(BsdSocket fd) {
/*     */     try {
/* 109 */       return (fd.getSoError() == 0);
/* 110 */     } catch (IOException e) {
/* 111 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final FileDescriptor fd() {
/* 117 */     return (FileDescriptor)this.socket;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/* 122 */     return this.active;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelMetadata metadata() {
/* 127 */     return METADATA;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClose() throws Exception {
/* 132 */     this.active = false;
/*     */ 
/*     */     
/* 135 */     this.inputClosedSeenErrorOnRead = true;
/*     */     try {
/* 137 */       if (isRegistered()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 146 */         EventLoop loop = eventLoop();
/* 147 */         if (loop.inEventLoop()) {
/* 148 */           doDeregister();
/*     */         } else {
/* 150 */           loop.execute(new Runnable()
/*     */               {
/*     */                 public void run() {
/*     */                   try {
/* 154 */                     AbstractKQueueChannel.this.doDeregister();
/* 155 */                   } catch (Throwable cause) {
/* 156 */                     AbstractKQueueChannel.this.pipeline().fireExceptionCaught(cause);
/*     */                   } 
/*     */                 }
/*     */               });
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 163 */       this.socket.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDisconnect() throws Exception {
/* 169 */     doClose();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isCompatible(EventLoop loop) {
/* 174 */     return loop instanceof KQueueEventLoop;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 179 */     return this.socket.isOpen();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doDeregister() throws Exception {
/* 185 */     readFilter(false);
/* 186 */     writeFilter(false);
/* 187 */     evSet0(Native.EVFILT_SOCK, Native.EV_DELETE, 0);
/*     */     
/* 189 */     ((KQueueEventLoop)eventLoop()).remove(this);
/*     */ 
/*     */     
/* 192 */     this.readFilterEnabled = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void doBeginRead() throws Exception {
/* 198 */     AbstractKQueueUnsafe unsafe = (AbstractKQueueUnsafe)unsafe();
/* 199 */     unsafe.readPending = true;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 204 */     readFilter(true);
/*     */ 
/*     */ 
/*     */     
/* 208 */     if (unsafe.maybeMoreDataToRead) {
/* 209 */       unsafe.executeReadReadyRunnable((ChannelConfig)config());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doRegister() throws Exception {
/* 218 */     this.readReadyRunnablePending = false;
/*     */     
/* 220 */     if (this.writeFilterEnabled) {
/* 221 */       evSet0(Native.EVFILT_WRITE, Native.EV_ADD_CLEAR_ENABLE);
/*     */     }
/* 223 */     if (this.readFilterEnabled) {
/* 224 */       evSet0(Native.EVFILT_READ, Native.EV_ADD_CLEAR_ENABLE);
/*     */     }
/* 226 */     evSet0(Native.EVFILT_SOCK, Native.EV_ADD, Native.NOTE_RDHUP);
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
/*     */   protected final ByteBuf newDirectBuffer(ByteBuf buf) {
/* 239 */     return newDirectBuffer(buf, buf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ByteBuf newDirectBuffer(Object holder, ByteBuf buf) {
/* 248 */     int readableBytes = buf.readableBytes();
/* 249 */     if (readableBytes == 0) {
/* 250 */       ReferenceCountUtil.release(holder);
/* 251 */       return Unpooled.EMPTY_BUFFER;
/*     */     } 
/*     */     
/* 254 */     ByteBufAllocator alloc = alloc();
/* 255 */     if (alloc.isDirectBufferPooled()) {
/* 256 */       return newDirectBuffer0(holder, buf, alloc, readableBytes);
/*     */     }
/*     */     
/* 259 */     ByteBuf directBuf = ByteBufUtil.threadLocalDirectBuffer();
/* 260 */     if (directBuf == null) {
/* 261 */       return newDirectBuffer0(holder, buf, alloc, readableBytes);
/*     */     }
/*     */     
/* 264 */     directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
/* 265 */     ReferenceCountUtil.safeRelease(holder);
/* 266 */     return directBuf;
/*     */   }
/*     */   
/*     */   private static ByteBuf newDirectBuffer0(Object holder, ByteBuf buf, ByteBufAllocator alloc, int capacity) {
/* 270 */     ByteBuf directBuf = alloc.directBuffer(capacity);
/* 271 */     directBuf.writeBytes(buf, buf.readerIndex(), capacity);
/* 272 */     ReferenceCountUtil.safeRelease(holder);
/* 273 */     return directBuf;
/*     */   }
/*     */   
/*     */   protected static void checkResolvable(InetSocketAddress addr) {
/* 277 */     if (addr.isUnresolved()) {
/* 278 */       throw new UnresolvedAddressException();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int doReadBytes(ByteBuf byteBuf) throws Exception {
/* 286 */     int localReadAmount, writerIndex = byteBuf.writerIndex();
/*     */     
/* 288 */     unsafe().recvBufAllocHandle().attemptedBytesRead(byteBuf.writableBytes());
/* 289 */     if (byteBuf.hasMemoryAddress()) {
/* 290 */       localReadAmount = this.socket.readAddress(byteBuf.memoryAddress(), writerIndex, byteBuf.capacity());
/*     */     } else {
/* 292 */       ByteBuffer buf = byteBuf.internalNioBuffer(writerIndex, byteBuf.writableBytes());
/* 293 */       localReadAmount = this.socket.read(buf, buf.position(), buf.limit());
/*     */     } 
/* 295 */     if (localReadAmount > 0) {
/* 296 */       byteBuf.writerIndex(writerIndex + localReadAmount);
/*     */     }
/* 298 */     return localReadAmount;
/*     */   }
/*     */   
/*     */   protected final int doWriteBytes(ChannelOutboundBuffer in, ByteBuf buf) throws Exception {
/* 302 */     if (buf.hasMemoryAddress()) {
/* 303 */       int localFlushedAmount = this.socket.writeAddress(buf.memoryAddress(), buf.readerIndex(), buf.writerIndex());
/* 304 */       if (localFlushedAmount > 0) {
/* 305 */         in.removeBytes(localFlushedAmount);
/* 306 */         return 1;
/*     */       } 
/*     */     } else {
/*     */       
/* 310 */       ByteBuffer nioBuf = (buf.nioBufferCount() == 1) ? buf.internalNioBuffer(buf.readerIndex(), buf.readableBytes()) : buf.nioBuffer();
/* 311 */       int localFlushedAmount = this.socket.write(nioBuf, nioBuf.position(), nioBuf.limit());
/* 312 */       if (localFlushedAmount > 0) {
/* 313 */         nioBuf.position(nioBuf.position() + localFlushedAmount);
/* 314 */         in.removeBytes(localFlushedAmount);
/* 315 */         return 1;
/*     */       } 
/*     */     } 
/* 318 */     return Integer.MAX_VALUE;
/*     */   }
/*     */   
/*     */   final boolean shouldBreakReadReady(ChannelConfig config) {
/* 322 */     return (this.socket.isInputShutdown() && (this.inputClosedSeenErrorOnRead || !isAllowHalfClosure(config)));
/*     */   }
/*     */   
/*     */   private static boolean isAllowHalfClosure(ChannelConfig config) {
/* 326 */     return (config instanceof SocketChannelConfig && ((SocketChannelConfig)config)
/* 327 */       .isAllowHalfClosure());
/*     */   }
/*     */ 
/*     */   
/*     */   final void clearReadFilter() {
/* 332 */     if (isRegistered()) {
/* 333 */       EventLoop loop = eventLoop();
/* 334 */       final AbstractKQueueUnsafe unsafe = (AbstractKQueueUnsafe)unsafe();
/* 335 */       if (loop.inEventLoop()) {
/* 336 */         unsafe.clearReadFilter0();
/*     */       } else {
/*     */         
/* 339 */         loop.execute(new Runnable()
/*     */             {
/*     */               public void run() {
/* 342 */                 if (!unsafe.readPending && !AbstractKQueueChannel.this.config().isAutoRead())
/*     */                 {
/* 344 */                   unsafe.clearReadFilter0();
/*     */                 }
/*     */               }
/*     */             });
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 352 */       this.readFilterEnabled = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   void readFilter(boolean readFilterEnabled) throws IOException {
/* 357 */     if (this.readFilterEnabled != readFilterEnabled) {
/* 358 */       this.readFilterEnabled = readFilterEnabled;
/* 359 */       evSet(Native.EVFILT_READ, readFilterEnabled ? Native.EV_ADD_CLEAR_ENABLE : Native.EV_DELETE_DISABLE);
/*     */     } 
/*     */   }
/*     */   
/*     */   void writeFilter(boolean writeFilterEnabled) throws IOException {
/* 364 */     if (this.writeFilterEnabled != writeFilterEnabled) {
/* 365 */       this.writeFilterEnabled = writeFilterEnabled;
/* 366 */       evSet(Native.EVFILT_WRITE, writeFilterEnabled ? Native.EV_ADD_CLEAR_ENABLE : Native.EV_DELETE_DISABLE);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void evSet(short filter, short flags) {
/* 371 */     if (isOpen() && isRegistered()) {
/* 372 */       evSet0(filter, flags);
/*     */     }
/*     */   }
/*     */   
/*     */   private void evSet0(short filter, short flags) {
/* 377 */     evSet0(filter, flags, 0);
/*     */   }
/*     */   
/*     */   private void evSet0(short filter, short flags, int fflags) {
/* 381 */     ((KQueueEventLoop)eventLoop()).evSet(this, filter, flags, fflags);
/*     */   } abstract class AbstractKQueueUnsafe extends AbstractChannel.AbstractUnsafe { boolean readPending; boolean maybeMoreDataToRead;
/*     */     AbstractKQueueUnsafe() {
/* 384 */       super(AbstractKQueueChannel.this);
/*     */ 
/*     */ 
/*     */       
/* 388 */       this.readReadyRunnable = new Runnable()
/*     */         {
/*     */           public void run() {
/* 391 */             AbstractKQueueChannel.this.readReadyRunnablePending = false;
/* 392 */             AbstractKQueueChannel.AbstractKQueueUnsafe.this.readReady(AbstractKQueueChannel.AbstractKQueueUnsafe.this.recvBufAllocHandle());
/*     */           }
/*     */         };
/*     */     } private KQueueRecvByteAllocatorHandle allocHandle; private final Runnable readReadyRunnable;
/*     */     final void readReady(long numberBytesPending) {
/* 397 */       KQueueRecvByteAllocatorHandle allocHandle = recvBufAllocHandle();
/* 398 */       allocHandle.numberBytesPending(numberBytesPending);
/* 399 */       readReady(allocHandle);
/*     */     }
/*     */ 
/*     */     
/*     */     final void readReadyBefore() {
/* 404 */       this.maybeMoreDataToRead = false;
/*     */     }
/*     */     final void readReadyFinally(ChannelConfig config) {
/* 407 */       this.maybeMoreDataToRead = this.allocHandle.maybeMoreDataToRead();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 414 */       if (!this.readPending && !config.isAutoRead()) {
/* 415 */         clearReadFilter0();
/* 416 */       } else if (this.readPending && this.maybeMoreDataToRead) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 424 */         executeReadReadyRunnable(config);
/*     */       } 
/*     */     }
/*     */     
/*     */     final boolean failConnectPromise(Throwable cause) {
/* 429 */       if (AbstractKQueueChannel.this.connectPromise != null) {
/*     */ 
/*     */ 
/*     */         
/* 433 */         ChannelPromise connectPromise = AbstractKQueueChannel.this.connectPromise;
/* 434 */         AbstractKQueueChannel.this.connectPromise = null;
/* 435 */         if (connectPromise.tryFailure((cause instanceof ConnectException) ? cause : (new ConnectException("failed to connect"))
/* 436 */             .initCause(cause))) {
/* 437 */           closeIfClosed();
/* 438 */           return true;
/*     */         } 
/*     */       } 
/* 441 */       return false;
/*     */     }
/*     */     
/*     */     final void writeReady() {
/* 445 */       if (AbstractKQueueChannel.this.connectPromise != null) {
/*     */         
/* 447 */         finishConnect();
/* 448 */       } else if (!AbstractKQueueChannel.this.socket.isOutputShutdown()) {
/*     */         
/* 450 */         super.flush0();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void shutdownInput(boolean readEOF) {
/* 463 */       if (readEOF && AbstractKQueueChannel.this.connectPromise != null) {
/* 464 */         finishConnect();
/*     */       }
/* 466 */       if (!AbstractKQueueChannel.this.socket.isInputShutdown()) {
/* 467 */         if (AbstractKQueueChannel.isAllowHalfClosure((ChannelConfig)AbstractKQueueChannel.this.config())) {
/*     */           try {
/* 469 */             AbstractKQueueChannel.this.socket.shutdown(true, false);
/* 470 */           } catch (IOException ignored) {
/*     */ 
/*     */             
/* 473 */             fireEventAndClose(ChannelInputShutdownEvent.INSTANCE);
/*     */             return;
/* 475 */           } catch (NotYetConnectedException notYetConnectedException) {}
/*     */ 
/*     */ 
/*     */           
/* 479 */           AbstractKQueueChannel.this.pipeline().fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
/*     */         } else {
/* 481 */           close(voidPromise());
/*     */         } 
/* 483 */       } else if (!readEOF) {
/* 484 */         AbstractKQueueChannel.this.inputClosedSeenErrorOnRead = true;
/* 485 */         AbstractKQueueChannel.this.pipeline().fireUserEventTriggered(ChannelInputShutdownReadComplete.INSTANCE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     final void readEOF() {
/* 491 */       KQueueRecvByteAllocatorHandle allocHandle = recvBufAllocHandle();
/* 492 */       allocHandle.readEOF();
/*     */       
/* 494 */       if (AbstractKQueueChannel.this.isActive()) {
/*     */ 
/*     */ 
/*     */         
/* 498 */         readReady(allocHandle);
/*     */       } else {
/*     */         
/* 501 */         shutdownInput(true);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public KQueueRecvByteAllocatorHandle recvBufAllocHandle() {
/* 507 */       if (this.allocHandle == null) {
/* 508 */         this
/* 509 */           .allocHandle = new KQueueRecvByteAllocatorHandle((RecvByteBufAllocator.ExtendedHandle)super.recvBufAllocHandle());
/*     */       }
/* 511 */       return this.allocHandle;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected final void flush0() {
/* 519 */       if (!AbstractKQueueChannel.this.writeFilterEnabled) {
/* 520 */         super.flush0();
/*     */       }
/*     */     }
/*     */     
/*     */     final void executeReadReadyRunnable(ChannelConfig config) {
/* 525 */       if (AbstractKQueueChannel.this.readReadyRunnablePending || !AbstractKQueueChannel.this.isActive() || AbstractKQueueChannel.this.shouldBreakReadReady(config)) {
/*     */         return;
/*     */       }
/* 528 */       AbstractKQueueChannel.this.readReadyRunnablePending = true;
/* 529 */       AbstractKQueueChannel.this.eventLoop().execute(this.readReadyRunnable);
/*     */     }
/*     */     
/*     */     protected final void clearReadFilter0() {
/* 533 */       assert AbstractKQueueChannel.this.eventLoop().inEventLoop();
/*     */       try {
/* 535 */         this.readPending = false;
/* 536 */         AbstractKQueueChannel.this.readFilter(false);
/* 537 */       } catch (IOException e) {
/*     */ 
/*     */         
/* 540 */         AbstractKQueueChannel.this.pipeline().fireExceptionCaught(e);
/* 541 */         AbstractKQueueChannel.this.unsafe().close(AbstractKQueueChannel.this.unsafe().voidPromise());
/*     */       } 
/*     */     }
/*     */     
/*     */     private void fireEventAndClose(Object evt) {
/* 546 */       AbstractKQueueChannel.this.pipeline().fireUserEventTriggered(evt);
/* 547 */       close(voidPromise());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void connect(final SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
/* 553 */       if (!promise.setUncancellable() || !ensureOpen(promise)) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/* 558 */         if (AbstractKQueueChannel.this.connectPromise != null) {
/* 559 */           throw new ConnectionPendingException();
/*     */         }
/*     */         
/* 562 */         boolean wasActive = AbstractKQueueChannel.this.isActive();
/* 563 */         if (AbstractKQueueChannel.this.doConnect(remoteAddress, localAddress)) {
/* 564 */           fulfillConnectPromise(promise, wasActive);
/*     */         } else {
/* 566 */           AbstractKQueueChannel.this.connectPromise = promise;
/* 567 */           AbstractKQueueChannel.this.requestedRemoteAddress = remoteAddress;
/*     */ 
/*     */           
/* 570 */           int connectTimeoutMillis = AbstractKQueueChannel.this.config().getConnectTimeoutMillis();
/* 571 */           if (connectTimeoutMillis > 0) {
/* 572 */             AbstractKQueueChannel.this.connectTimeoutFuture = (ScheduledFuture<?>)AbstractKQueueChannel.this.eventLoop().schedule(new Runnable()
/*     */                 {
/*     */                   public void run() {
/* 575 */                     ChannelPromise connectPromise = AbstractKQueueChannel.this.connectPromise;
/* 576 */                     ConnectTimeoutException cause = new ConnectTimeoutException("connection timed out: " + remoteAddress);
/*     */                     
/* 578 */                     if (connectPromise != null && connectPromise.tryFailure((Throwable)cause)) {
/* 579 */                       AbstractKQueueChannel.AbstractKQueueUnsafe.this.close(AbstractKQueueChannel.AbstractKQueueUnsafe.this.voidPromise());
/*     */                     }
/*     */                   }
/*     */                 },  connectTimeoutMillis, TimeUnit.MILLISECONDS);
/*     */           }
/*     */           
/* 585 */           promise.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */               {
/*     */                 public void operationComplete(ChannelFuture future) throws Exception {
/* 588 */                   if (future.isCancelled()) {
/* 589 */                     if (AbstractKQueueChannel.this.connectTimeoutFuture != null) {
/* 590 */                       AbstractKQueueChannel.this.connectTimeoutFuture.cancel(false);
/*     */                     }
/* 592 */                     AbstractKQueueChannel.this.connectPromise = null;
/* 593 */                     AbstractKQueueChannel.AbstractKQueueUnsafe.this.close(AbstractKQueueChannel.AbstractKQueueUnsafe.this.voidPromise());
/*     */                   } 
/*     */                 }
/*     */               });
/*     */         } 
/* 598 */       } catch (Throwable t) {
/* 599 */         closeIfClosed();
/* 600 */         promise.tryFailure(annotateConnectException(t, remoteAddress));
/*     */       } 
/*     */     }
/*     */     
/*     */     private void fulfillConnectPromise(ChannelPromise promise, boolean wasActive) {
/* 605 */       if (promise == null) {
/*     */         return;
/*     */       }
/*     */       
/* 609 */       AbstractKQueueChannel.this.active = true;
/*     */ 
/*     */ 
/*     */       
/* 613 */       boolean active = AbstractKQueueChannel.this.isActive();
/*     */ 
/*     */       
/* 616 */       boolean promiseSet = promise.trySuccess();
/*     */ 
/*     */ 
/*     */       
/* 620 */       if (!wasActive && active) {
/* 621 */         AbstractKQueueChannel.this.pipeline().fireChannelActive();
/*     */       }
/*     */ 
/*     */       
/* 625 */       if (!promiseSet) {
/* 626 */         close(voidPromise());
/*     */       }
/*     */     }
/*     */     
/*     */     private void fulfillConnectPromise(ChannelPromise promise, Throwable cause) {
/* 631 */       if (promise == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 637 */       promise.tryFailure(cause);
/* 638 */       closeIfClosed();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void finishConnect() {
/* 645 */       assert AbstractKQueueChannel.this.eventLoop().inEventLoop();
/*     */       
/* 647 */       boolean connectStillInProgress = false;
/*     */       try {
/* 649 */         boolean wasActive = AbstractKQueueChannel.this.isActive();
/* 650 */         if (!doFinishConnect()) {
/* 651 */           connectStillInProgress = true;
/*     */           return;
/*     */         } 
/* 654 */         fulfillConnectPromise(AbstractKQueueChannel.this.connectPromise, wasActive);
/* 655 */       } catch (Throwable t) {
/* 656 */         fulfillConnectPromise(AbstractKQueueChannel.this.connectPromise, annotateConnectException(t, AbstractKQueueChannel.this.requestedRemoteAddress));
/*     */       } finally {
/* 658 */         if (!connectStillInProgress) {
/*     */ 
/*     */           
/* 661 */           if (AbstractKQueueChannel.this.connectTimeoutFuture != null) {
/* 662 */             AbstractKQueueChannel.this.connectTimeoutFuture.cancel(false);
/*     */           }
/* 664 */           AbstractKQueueChannel.this.connectPromise = null;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean doFinishConnect() throws Exception {
/* 670 */       if (AbstractKQueueChannel.this.socket.finishConnect()) {
/* 671 */         AbstractKQueueChannel.this.writeFilter(false);
/* 672 */         if (AbstractKQueueChannel.this.requestedRemoteAddress instanceof InetSocketAddress) {
/* 673 */           AbstractKQueueChannel.this.remote = UnixChannelUtil.computeRemoteAddr((InetSocketAddress)AbstractKQueueChannel.this.requestedRemoteAddress, AbstractKQueueChannel.this.socket.remoteAddress());
/*     */         }
/* 675 */         AbstractKQueueChannel.this.requestedRemoteAddress = null;
/* 676 */         return true;
/*     */       } 
/* 678 */       AbstractKQueueChannel.this.writeFilter(true);
/* 679 */       return false;
/*     */     }
/*     */     
/*     */     abstract void readReady(KQueueRecvByteAllocatorHandle param1KQueueRecvByteAllocatorHandle); }
/*     */   
/*     */   protected void doBind(SocketAddress local) throws Exception {
/* 685 */     if (local instanceof InetSocketAddress) {
/* 686 */       checkResolvable((InetSocketAddress)local);
/*     */     }
/* 688 */     this.socket.bind(local);
/* 689 */     this.local = this.socket.localAddress();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
/* 696 */     if (localAddress instanceof InetSocketAddress) {
/* 697 */       checkResolvable((InetSocketAddress)localAddress);
/*     */     }
/*     */     
/* 700 */     InetSocketAddress remoteSocketAddr = (remoteAddress instanceof InetSocketAddress) ? (InetSocketAddress)remoteAddress : null;
/*     */     
/* 702 */     if (remoteSocketAddr != null) {
/* 703 */       checkResolvable(remoteSocketAddr);
/*     */     }
/*     */     
/* 706 */     if (this.remote != null)
/*     */     {
/*     */ 
/*     */       
/* 710 */       throw new AlreadyConnectedException();
/*     */     }
/*     */     
/* 713 */     if (localAddress != null) {
/* 714 */       this.socket.bind(localAddress);
/*     */     }
/*     */     
/* 717 */     boolean connected = doConnect0(remoteAddress);
/* 718 */     if (connected) {
/* 719 */       this
/* 720 */         .remote = (remoteSocketAddr == null) ? remoteAddress : UnixChannelUtil.computeRemoteAddr(remoteSocketAddr, this.socket.remoteAddress());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 725 */     this.local = this.socket.localAddress();
/* 726 */     return connected;
/*     */   }
/*     */   
/*     */   private boolean doConnect0(SocketAddress remote) throws Exception {
/* 730 */     boolean success = false;
/*     */     try {
/* 732 */       boolean connected = this.socket.connect(remote);
/* 733 */       if (!connected) {
/* 734 */         writeFilter(true);
/*     */       }
/* 736 */       success = true;
/* 737 */       return connected;
/*     */     } finally {
/* 739 */       if (!success) {
/* 740 */         doClose();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress localAddress0() {
/* 747 */     return this.local;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress remoteAddress0() {
/* 752 */     return this.remote;
/*     */   }
/*     */   
/*     */   protected abstract AbstractKQueueUnsafe newUnsafe();
/*     */   
/*     */   public abstract KQueueChannelConfig config();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\AbstractKQueueChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */