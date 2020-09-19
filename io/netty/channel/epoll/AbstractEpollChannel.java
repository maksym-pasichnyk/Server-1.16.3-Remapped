/*     */ package io.netty.channel.epoll;
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
/*     */ import io.netty.channel.unix.Socket;
/*     */ import io.netty.channel.unix.UnixChannel;
/*     */ import io.netty.channel.unix.UnixChannelUtil;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.AlreadyConnectedException;
/*     */ import java.nio.channels.ClosedChannelException;
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
/*     */ abstract class AbstractEpollChannel
/*     */   extends AbstractChannel
/*     */   implements UnixChannel
/*     */ {
/*  60 */   private static final ClosedChannelException DO_CLOSE_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractEpollChannel.class, "doClose()");
/*     */   
/*  62 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
/*     */   
/*     */   private final int readFlag;
/*     */   
/*     */   final LinuxSocket socket;
/*     */   
/*     */   private ChannelPromise connectPromise;
/*     */   
/*     */   private ScheduledFuture<?> connectTimeoutFuture;
/*     */   
/*     */   private SocketAddress requestedRemoteAddress;
/*     */   
/*     */   private volatile SocketAddress local;
/*     */   private volatile SocketAddress remote;
/*  76 */   protected int flags = Native.EPOLLET;
/*     */   
/*     */   boolean inputClosedSeenErrorOnRead;
/*     */   boolean epollInReadyRunnablePending;
/*     */   protected volatile boolean active;
/*     */   
/*     */   AbstractEpollChannel(LinuxSocket fd, int flag) {
/*  83 */     this((Channel)null, fd, flag, false);
/*     */   }
/*     */   
/*     */   AbstractEpollChannel(Channel parent, LinuxSocket fd, int flag, boolean active) {
/*  87 */     super(parent);
/*  88 */     this.socket = (LinuxSocket)ObjectUtil.checkNotNull(fd, "fd");
/*  89 */     this.readFlag = flag;
/*  90 */     this.flags |= flag;
/*  91 */     this.active = active;
/*  92 */     if (active) {
/*     */ 
/*     */       
/*  95 */       this.local = fd.localAddress();
/*  96 */       this.remote = fd.remoteAddress();
/*     */     } 
/*     */   }
/*     */   
/*     */   AbstractEpollChannel(Channel parent, LinuxSocket fd, int flag, SocketAddress remote) {
/* 101 */     super(parent);
/* 102 */     this.socket = (LinuxSocket)ObjectUtil.checkNotNull(fd, "fd");
/* 103 */     this.readFlag = flag;
/* 104 */     this.flags |= flag;
/* 105 */     this.active = true;
/*     */ 
/*     */     
/* 108 */     this.remote = remote;
/* 109 */     this.local = fd.localAddress();
/*     */   }
/*     */   
/*     */   static boolean isSoErrorZero(Socket fd) {
/*     */     try {
/* 114 */       return (fd.getSoError() == 0);
/* 115 */     } catch (IOException e) {
/* 116 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   void setFlag(int flag) throws IOException {
/* 121 */     if (!isFlagSet(flag)) {
/* 122 */       this.flags |= flag;
/* 123 */       modifyEvents();
/*     */     } 
/*     */   }
/*     */   
/*     */   void clearFlag(int flag) throws IOException {
/* 128 */     if (isFlagSet(flag)) {
/* 129 */       this.flags &= flag ^ 0xFFFFFFFF;
/* 130 */       modifyEvents();
/*     */     } 
/*     */   }
/*     */   
/*     */   boolean isFlagSet(int flag) {
/* 135 */     return ((this.flags & flag) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public final FileDescriptor fd() {
/* 140 */     return (FileDescriptor)this.socket;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/* 148 */     return this.active;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelMetadata metadata() {
/* 153 */     return METADATA;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClose() throws Exception {
/* 158 */     this.active = false;
/*     */ 
/*     */     
/* 161 */     this.inputClosedSeenErrorOnRead = true;
/*     */     try {
/* 163 */       ChannelPromise promise = this.connectPromise;
/* 164 */       if (promise != null) {
/*     */         
/* 166 */         promise.tryFailure(DO_CLOSE_CLOSED_CHANNEL_EXCEPTION);
/* 167 */         this.connectPromise = null;
/*     */       } 
/*     */       
/* 170 */       ScheduledFuture<?> future = this.connectTimeoutFuture;
/* 171 */       if (future != null) {
/* 172 */         future.cancel(false);
/* 173 */         this.connectTimeoutFuture = null;
/*     */       } 
/*     */       
/* 176 */       if (isRegistered()) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 181 */         EventLoop loop = eventLoop();
/* 182 */         if (loop.inEventLoop()) {
/* 183 */           doDeregister();
/*     */         } else {
/* 185 */           loop.execute(new Runnable()
/*     */               {
/*     */                 public void run() {
/*     */                   try {
/* 189 */                     AbstractEpollChannel.this.doDeregister();
/* 190 */                   } catch (Throwable cause) {
/* 191 */                     AbstractEpollChannel.this.pipeline().fireExceptionCaught(cause);
/*     */                   } 
/*     */                 }
/*     */               });
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 198 */       this.socket.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDisconnect() throws Exception {
/* 204 */     doClose();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isCompatible(EventLoop loop) {
/* 209 */     return loop instanceof EpollEventLoop;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 214 */     return this.socket.isOpen();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDeregister() throws Exception {
/* 219 */     ((EpollEventLoop)eventLoop()).remove(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void doBeginRead() throws Exception {
/* 225 */     AbstractEpollUnsafe unsafe = (AbstractEpollUnsafe)unsafe();
/* 226 */     unsafe.readPending = true;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 231 */     setFlag(this.readFlag);
/*     */ 
/*     */ 
/*     */     
/* 235 */     if (unsafe.maybeMoreDataToRead) {
/* 236 */       unsafe.executeEpollInReadyRunnable((ChannelConfig)config());
/*     */     }
/*     */   }
/*     */   
/*     */   final boolean shouldBreakEpollInReady(ChannelConfig config) {
/* 241 */     return (this.socket.isInputShutdown() && (this.inputClosedSeenErrorOnRead || !isAllowHalfClosure(config)));
/*     */   }
/*     */   
/*     */   private static boolean isAllowHalfClosure(ChannelConfig config) {
/* 245 */     return (config instanceof SocketChannelConfig && ((SocketChannelConfig)config)
/* 246 */       .isAllowHalfClosure());
/*     */   }
/*     */ 
/*     */   
/*     */   final void clearEpollIn() {
/* 251 */     if (isRegistered()) {
/* 252 */       EventLoop loop = eventLoop();
/* 253 */       final AbstractEpollUnsafe unsafe = (AbstractEpollUnsafe)unsafe();
/* 254 */       if (loop.inEventLoop()) {
/* 255 */         unsafe.clearEpollIn0();
/*     */       } else {
/*     */         
/* 258 */         loop.execute(new Runnable()
/*     */             {
/*     */               public void run() {
/* 261 */                 if (!unsafe.readPending && !AbstractEpollChannel.this.config().isAutoRead())
/*     */                 {
/* 263 */                   unsafe.clearEpollIn0();
/*     */                 }
/*     */               }
/*     */             });
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 271 */       this.flags &= this.readFlag ^ 0xFFFFFFFF;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void modifyEvents() throws IOException {
/* 276 */     if (isOpen() && isRegistered()) {
/* 277 */       ((EpollEventLoop)eventLoop()).modify(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doRegister() throws Exception {
/* 286 */     this.epollInReadyRunnablePending = false;
/* 287 */     ((EpollEventLoop)eventLoop()).add(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ByteBuf newDirectBuffer(ByteBuf buf) {
/* 297 */     return newDirectBuffer(buf, buf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ByteBuf newDirectBuffer(Object holder, ByteBuf buf) {
/* 306 */     int readableBytes = buf.readableBytes();
/* 307 */     if (readableBytes == 0) {
/* 308 */       ReferenceCountUtil.release(holder);
/* 309 */       return Unpooled.EMPTY_BUFFER;
/*     */     } 
/*     */     
/* 312 */     ByteBufAllocator alloc = alloc();
/* 313 */     if (alloc.isDirectBufferPooled()) {
/* 314 */       return newDirectBuffer0(holder, buf, alloc, readableBytes);
/*     */     }
/*     */     
/* 317 */     ByteBuf directBuf = ByteBufUtil.threadLocalDirectBuffer();
/* 318 */     if (directBuf == null) {
/* 319 */       return newDirectBuffer0(holder, buf, alloc, readableBytes);
/*     */     }
/*     */     
/* 322 */     directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
/* 323 */     ReferenceCountUtil.safeRelease(holder);
/* 324 */     return directBuf;
/*     */   }
/*     */   
/*     */   private static ByteBuf newDirectBuffer0(Object holder, ByteBuf buf, ByteBufAllocator alloc, int capacity) {
/* 328 */     ByteBuf directBuf = alloc.directBuffer(capacity);
/* 329 */     directBuf.writeBytes(buf, buf.readerIndex(), capacity);
/* 330 */     ReferenceCountUtil.safeRelease(holder);
/* 331 */     return directBuf;
/*     */   }
/*     */   
/*     */   protected static void checkResolvable(InetSocketAddress addr) {
/* 335 */     if (addr.isUnresolved()) {
/* 336 */       throw new UnresolvedAddressException();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int doReadBytes(ByteBuf byteBuf) throws Exception {
/* 344 */     int localReadAmount, writerIndex = byteBuf.writerIndex();
/*     */     
/* 346 */     unsafe().recvBufAllocHandle().attemptedBytesRead(byteBuf.writableBytes());
/* 347 */     if (byteBuf.hasMemoryAddress()) {
/* 348 */       localReadAmount = this.socket.readAddress(byteBuf.memoryAddress(), writerIndex, byteBuf.capacity());
/*     */     } else {
/* 350 */       ByteBuffer buf = byteBuf.internalNioBuffer(writerIndex, byteBuf.writableBytes());
/* 351 */       localReadAmount = this.socket.read(buf, buf.position(), buf.limit());
/*     */     } 
/* 353 */     if (localReadAmount > 0) {
/* 354 */       byteBuf.writerIndex(writerIndex + localReadAmount);
/*     */     }
/* 356 */     return localReadAmount;
/*     */   }
/*     */   
/*     */   protected final int doWriteBytes(ChannelOutboundBuffer in, ByteBuf buf) throws Exception {
/* 360 */     if (buf.hasMemoryAddress()) {
/* 361 */       int localFlushedAmount = this.socket.writeAddress(buf.memoryAddress(), buf.readerIndex(), buf.writerIndex());
/* 362 */       if (localFlushedAmount > 0) {
/* 363 */         in.removeBytes(localFlushedAmount);
/* 364 */         return 1;
/*     */       } 
/*     */     } else {
/*     */       
/* 368 */       ByteBuffer nioBuf = (buf.nioBufferCount() == 1) ? buf.internalNioBuffer(buf.readerIndex(), buf.readableBytes()) : buf.nioBuffer();
/* 369 */       int localFlushedAmount = this.socket.write(nioBuf, nioBuf.position(), nioBuf.limit());
/* 370 */       if (localFlushedAmount > 0) {
/* 371 */         nioBuf.position(nioBuf.position() + localFlushedAmount);
/* 372 */         in.removeBytes(localFlushedAmount);
/* 373 */         return 1;
/*     */       } 
/*     */     } 
/* 376 */     return Integer.MAX_VALUE;
/*     */   } protected abstract class AbstractEpollUnsafe extends AbstractChannel.AbstractUnsafe { boolean readPending;
/*     */     protected AbstractEpollUnsafe() {
/* 379 */       super(AbstractEpollChannel.this);
/*     */ 
/*     */ 
/*     */       
/* 383 */       this.epollInReadyRunnable = new Runnable()
/*     */         {
/*     */           public void run() {
/* 386 */             AbstractEpollChannel.this.epollInReadyRunnablePending = false;
/* 387 */             AbstractEpollChannel.AbstractEpollUnsafe.this.epollInReady();
/*     */           }
/*     */         };
/*     */     }
/*     */     boolean maybeMoreDataToRead;
/*     */     private EpollRecvByteAllocatorHandle allocHandle;
/*     */     private final Runnable epollInReadyRunnable;
/*     */     
/*     */     final void epollInBefore() {
/* 396 */       this.maybeMoreDataToRead = false;
/*     */     }
/*     */     final void epollInFinally(ChannelConfig config) {
/* 399 */       this.maybeMoreDataToRead = (this.allocHandle.isEdgeTriggered() && this.allocHandle.maybeMoreDataToRead());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 406 */       if (!this.readPending && !config.isAutoRead()) {
/* 407 */         AbstractEpollChannel.this.clearEpollIn();
/* 408 */       } else if (this.readPending && this.maybeMoreDataToRead) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 416 */         executeEpollInReadyRunnable(config);
/*     */       } 
/*     */     }
/*     */     
/*     */     final void executeEpollInReadyRunnable(ChannelConfig config) {
/* 421 */       if (AbstractEpollChannel.this.epollInReadyRunnablePending || !AbstractEpollChannel.this.isActive() || AbstractEpollChannel.this.shouldBreakEpollInReady(config)) {
/*     */         return;
/*     */       }
/* 424 */       AbstractEpollChannel.this.epollInReadyRunnablePending = true;
/* 425 */       AbstractEpollChannel.this.eventLoop().execute(this.epollInReadyRunnable);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final void epollRdHupReady() {
/* 433 */       recvBufAllocHandle().receivedRdHup();
/*     */       
/* 435 */       if (AbstractEpollChannel.this.isActive()) {
/*     */ 
/*     */ 
/*     */         
/* 439 */         epollInReady();
/*     */       } else {
/*     */         
/* 442 */         shutdownInput(true);
/*     */       } 
/*     */ 
/*     */       
/* 446 */       clearEpollRdHup();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void clearEpollRdHup() {
/*     */       try {
/* 454 */         AbstractEpollChannel.this.clearFlag(Native.EPOLLRDHUP);
/* 455 */       } catch (IOException e) {
/* 456 */         AbstractEpollChannel.this.pipeline().fireExceptionCaught(e);
/* 457 */         close(voidPromise());
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void shutdownInput(boolean rdHup) {
/* 465 */       if (!AbstractEpollChannel.this.socket.isInputShutdown()) {
/* 466 */         if (AbstractEpollChannel.isAllowHalfClosure((ChannelConfig)AbstractEpollChannel.this.config())) {
/*     */           try {
/* 468 */             AbstractEpollChannel.this.socket.shutdown(true, false);
/* 469 */           } catch (IOException ignored) {
/*     */ 
/*     */             
/* 472 */             fireEventAndClose(ChannelInputShutdownEvent.INSTANCE);
/*     */             return;
/* 474 */           } catch (NotYetConnectedException notYetConnectedException) {}
/*     */ 
/*     */ 
/*     */           
/* 478 */           AbstractEpollChannel.this.clearEpollIn();
/* 479 */           AbstractEpollChannel.this.pipeline().fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
/*     */         } else {
/* 481 */           close(voidPromise());
/*     */         } 
/* 483 */       } else if (!rdHup) {
/* 484 */         AbstractEpollChannel.this.inputClosedSeenErrorOnRead = true;
/* 485 */         AbstractEpollChannel.this.pipeline().fireUserEventTriggered(ChannelInputShutdownReadComplete.INSTANCE);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void fireEventAndClose(Object evt) {
/* 490 */       AbstractEpollChannel.this.pipeline().fireUserEventTriggered(evt);
/* 491 */       close(voidPromise());
/*     */     }
/*     */ 
/*     */     
/*     */     public EpollRecvByteAllocatorHandle recvBufAllocHandle() {
/* 496 */       if (this.allocHandle == null) {
/* 497 */         this.allocHandle = newEpollHandle((RecvByteBufAllocator.ExtendedHandle)super.recvBufAllocHandle());
/*     */       }
/* 499 */       return this.allocHandle;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     EpollRecvByteAllocatorHandle newEpollHandle(RecvByteBufAllocator.ExtendedHandle handle) {
/* 507 */       return new EpollRecvByteAllocatorHandle(handle);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected final void flush0() {
/* 515 */       if (!AbstractEpollChannel.this.isFlagSet(Native.EPOLLOUT)) {
/* 516 */         super.flush0();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final void epollOutReady() {
/* 524 */       if (AbstractEpollChannel.this.connectPromise != null) {
/*     */         
/* 526 */         finishConnect();
/* 527 */       } else if (!AbstractEpollChannel.this.socket.isOutputShutdown()) {
/*     */         
/* 529 */         super.flush0();
/*     */       } 
/*     */     }
/*     */     
/*     */     protected final void clearEpollIn0() {
/* 534 */       assert AbstractEpollChannel.this.eventLoop().inEventLoop();
/*     */       try {
/* 536 */         this.readPending = false;
/* 537 */         AbstractEpollChannel.this.clearFlag(AbstractEpollChannel.this.readFlag);
/* 538 */       } catch (IOException e) {
/*     */ 
/*     */         
/* 541 */         AbstractEpollChannel.this.pipeline().fireExceptionCaught(e);
/* 542 */         AbstractEpollChannel.this.unsafe().close(AbstractEpollChannel.this.unsafe().voidPromise());
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void connect(final SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
/* 549 */       if (!promise.setUncancellable() || !ensureOpen(promise)) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/* 554 */         if (AbstractEpollChannel.this.connectPromise != null) {
/* 555 */           throw new ConnectionPendingException();
/*     */         }
/*     */         
/* 558 */         boolean wasActive = AbstractEpollChannel.this.isActive();
/* 559 */         if (AbstractEpollChannel.this.doConnect(remoteAddress, localAddress)) {
/* 560 */           fulfillConnectPromise(promise, wasActive);
/*     */         } else {
/* 562 */           AbstractEpollChannel.this.connectPromise = promise;
/* 563 */           AbstractEpollChannel.this.requestedRemoteAddress = remoteAddress;
/*     */ 
/*     */           
/* 566 */           int connectTimeoutMillis = AbstractEpollChannel.this.config().getConnectTimeoutMillis();
/* 567 */           if (connectTimeoutMillis > 0) {
/* 568 */             AbstractEpollChannel.this.connectTimeoutFuture = (ScheduledFuture<?>)AbstractEpollChannel.this.eventLoop().schedule(new Runnable()
/*     */                 {
/*     */                   public void run() {
/* 571 */                     ChannelPromise connectPromise = AbstractEpollChannel.this.connectPromise;
/* 572 */                     ConnectTimeoutException cause = new ConnectTimeoutException("connection timed out: " + remoteAddress);
/*     */                     
/* 574 */                     if (connectPromise != null && connectPromise.tryFailure((Throwable)cause)) {
/* 575 */                       AbstractEpollChannel.AbstractEpollUnsafe.this.close(AbstractEpollChannel.AbstractEpollUnsafe.this.voidPromise());
/*     */                     }
/*     */                   }
/*     */                 },  connectTimeoutMillis, TimeUnit.MILLISECONDS);
/*     */           }
/*     */           
/* 581 */           promise.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */               {
/*     */                 public void operationComplete(ChannelFuture future) throws Exception {
/* 584 */                   if (future.isCancelled()) {
/* 585 */                     if (AbstractEpollChannel.this.connectTimeoutFuture != null) {
/* 586 */                       AbstractEpollChannel.this.connectTimeoutFuture.cancel(false);
/*     */                     }
/* 588 */                     AbstractEpollChannel.this.connectPromise = null;
/* 589 */                     AbstractEpollChannel.AbstractEpollUnsafe.this.close(AbstractEpollChannel.AbstractEpollUnsafe.this.voidPromise());
/*     */                   } 
/*     */                 }
/*     */               });
/*     */         } 
/* 594 */       } catch (Throwable t) {
/* 595 */         closeIfClosed();
/* 596 */         promise.tryFailure(annotateConnectException(t, remoteAddress));
/*     */       } 
/*     */     }
/*     */     
/*     */     private void fulfillConnectPromise(ChannelPromise promise, boolean wasActive) {
/* 601 */       if (promise == null) {
/*     */         return;
/*     */       }
/*     */       
/* 605 */       AbstractEpollChannel.this.active = true;
/*     */ 
/*     */ 
/*     */       
/* 609 */       boolean active = AbstractEpollChannel.this.isActive();
/*     */ 
/*     */       
/* 612 */       boolean promiseSet = promise.trySuccess();
/*     */ 
/*     */ 
/*     */       
/* 616 */       if (!wasActive && active) {
/* 617 */         AbstractEpollChannel.this.pipeline().fireChannelActive();
/*     */       }
/*     */ 
/*     */       
/* 621 */       if (!promiseSet) {
/* 622 */         close(voidPromise());
/*     */       }
/*     */     }
/*     */     
/*     */     private void fulfillConnectPromise(ChannelPromise promise, Throwable cause) {
/* 627 */       if (promise == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 633 */       promise.tryFailure(cause);
/* 634 */       closeIfClosed();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void finishConnect() {
/* 641 */       assert AbstractEpollChannel.this.eventLoop().inEventLoop();
/*     */       
/* 643 */       boolean connectStillInProgress = false;
/*     */       try {
/* 645 */         boolean wasActive = AbstractEpollChannel.this.isActive();
/* 646 */         if (!doFinishConnect()) {
/* 647 */           connectStillInProgress = true;
/*     */           return;
/*     */         } 
/* 650 */         fulfillConnectPromise(AbstractEpollChannel.this.connectPromise, wasActive);
/* 651 */       } catch (Throwable t) {
/* 652 */         fulfillConnectPromise(AbstractEpollChannel.this.connectPromise, annotateConnectException(t, AbstractEpollChannel.this.requestedRemoteAddress));
/*     */       } finally {
/* 654 */         if (!connectStillInProgress) {
/*     */ 
/*     */           
/* 657 */           if (AbstractEpollChannel.this.connectTimeoutFuture != null) {
/* 658 */             AbstractEpollChannel.this.connectTimeoutFuture.cancel(false);
/*     */           }
/* 660 */           AbstractEpollChannel.this.connectPromise = null;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean doFinishConnect() throws Exception {
/* 669 */       if (AbstractEpollChannel.this.socket.finishConnect()) {
/* 670 */         AbstractEpollChannel.this.clearFlag(Native.EPOLLOUT);
/* 671 */         if (AbstractEpollChannel.this.requestedRemoteAddress instanceof InetSocketAddress) {
/* 672 */           AbstractEpollChannel.this.remote = UnixChannelUtil.computeRemoteAddr((InetSocketAddress)AbstractEpollChannel.this.requestedRemoteAddress, AbstractEpollChannel.this.socket.remoteAddress());
/*     */         }
/* 674 */         AbstractEpollChannel.this.requestedRemoteAddress = null;
/*     */         
/* 676 */         return true;
/*     */       } 
/* 678 */       AbstractEpollChannel.this.setFlag(Native.EPOLLOUT);
/* 679 */       return false;
/*     */     }
/*     */     
/*     */     abstract void epollInReady(); }
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
/* 734 */         setFlag(Native.EPOLLOUT);
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
/*     */   public abstract EpollChannelConfig config();
/*     */   
/*     */   protected abstract AbstractEpollUnsafe newUnsafe();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\epoll\AbstractEpollChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */