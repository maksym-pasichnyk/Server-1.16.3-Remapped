/*     */ package io.netty.channel.nio;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.AbstractChannel;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.ConnectTimeoutException;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.CancelledKeyException;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.ConnectionPendingException;
/*     */ import java.nio.channels.SelectableChannel;
/*     */ import java.nio.channels.SelectionKey;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractNioChannel
/*     */   extends AbstractChannel
/*     */ {
/*  52 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractNioChannel.class);
/*     */   
/*  54 */   private static final ClosedChannelException DO_CLOSE_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractNioChannel.class, "doClose()");
/*     */   
/*     */   private final SelectableChannel ch;
/*     */   protected final int readInterestOp;
/*     */   volatile SelectionKey selectionKey;
/*     */   boolean readPending;
/*     */   
/*  61 */   private final Runnable clearReadPendingRunnable = new Runnable()
/*     */     {
/*     */       public void run() {
/*  64 */         AbstractNioChannel.this.clearReadPending0();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ChannelPromise connectPromise;
/*     */ 
/*     */ 
/*     */   
/*     */   private ScheduledFuture<?> connectTimeoutFuture;
/*     */ 
/*     */ 
/*     */   
/*     */   private SocketAddress requestedRemoteAddress;
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractNioChannel(Channel parent, SelectableChannel ch, int readInterestOp) {
/*  84 */     super(parent);
/*  85 */     this.ch = ch;
/*  86 */     this.readInterestOp = readInterestOp;
/*     */     try {
/*  88 */       ch.configureBlocking(false);
/*  89 */     } catch (IOException e) {
/*     */       try {
/*  91 */         ch.close();
/*  92 */       } catch (IOException e2) {
/*  93 */         if (logger.isWarnEnabled()) {
/*  94 */           logger.warn("Failed to close a partially initialized socket.", e2);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*  99 */       throw new ChannelException("Failed to enter non-blocking mode.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 105 */     return this.ch.isOpen();
/*     */   }
/*     */ 
/*     */   
/*     */   public NioUnsafe unsafe() {
/* 110 */     return (NioUnsafe)super.unsafe();
/*     */   }
/*     */   
/*     */   protected SelectableChannel javaChannel() {
/* 114 */     return this.ch;
/*     */   }
/*     */ 
/*     */   
/*     */   public NioEventLoop eventLoop() {
/* 119 */     return (NioEventLoop)super.eventLoop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SelectionKey selectionKey() {
/* 126 */     assert this.selectionKey != null;
/* 127 */     return this.selectionKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected boolean isReadPending() {
/* 136 */     return this.readPending;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected void setReadPending(final boolean readPending) {
/* 145 */     if (isRegistered()) {
/* 146 */       NioEventLoop nioEventLoop = eventLoop();
/* 147 */       if (nioEventLoop.inEventLoop()) {
/* 148 */         setReadPending0(readPending);
/*     */       } else {
/* 150 */         nioEventLoop.execute(new Runnable()
/*     */             {
/*     */               public void run() {
/* 153 */                 AbstractNioChannel.this.setReadPending0(readPending);
/*     */               }
/*     */             });
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 161 */       this.readPending = readPending;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void clearReadPending() {
/* 169 */     if (isRegistered()) {
/* 170 */       NioEventLoop nioEventLoop = eventLoop();
/* 171 */       if (nioEventLoop.inEventLoop()) {
/* 172 */         clearReadPending0();
/*     */       } else {
/* 174 */         nioEventLoop.execute(this.clearReadPendingRunnable);
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 180 */       this.readPending = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setReadPending0(boolean readPending) {
/* 185 */     this.readPending = readPending;
/* 186 */     if (!readPending) {
/* 187 */       ((AbstractNioUnsafe)unsafe()).removeReadOp();
/*     */     }
/*     */   }
/*     */   
/*     */   private void clearReadPending0() {
/* 192 */     this.readPending = false;
/* 193 */     ((AbstractNioUnsafe)unsafe()).removeReadOp();
/*     */   }
/*     */ 
/*     */   
/*     */   public static interface NioUnsafe
/*     */     extends Channel.Unsafe
/*     */   {
/*     */     SelectableChannel ch();
/*     */ 
/*     */     
/*     */     void finishConnect();
/*     */ 
/*     */     
/*     */     void read();
/*     */ 
/*     */     
/*     */     void forceFlush();
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract class AbstractNioUnsafe
/*     */     extends AbstractChannel.AbstractUnsafe
/*     */     implements NioUnsafe
/*     */   {
/*     */     protected AbstractNioUnsafe() {
/* 218 */       super(AbstractNioChannel.this);
/*     */     }
/*     */     protected final void removeReadOp() {
/* 221 */       SelectionKey key = AbstractNioChannel.this.selectionKey();
/*     */ 
/*     */ 
/*     */       
/* 225 */       if (!key.isValid()) {
/*     */         return;
/*     */       }
/* 228 */       int interestOps = key.interestOps();
/* 229 */       if ((interestOps & AbstractNioChannel.this.readInterestOp) != 0)
/*     */       {
/* 231 */         key.interestOps(interestOps & (AbstractNioChannel.this.readInterestOp ^ 0xFFFFFFFF));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public final SelectableChannel ch() {
/* 237 */       return AbstractNioChannel.this.javaChannel();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final void connect(final SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
/* 243 */       if (!promise.setUncancellable() || !ensureOpen(promise)) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/* 248 */         if (AbstractNioChannel.this.connectPromise != null)
/*     */         {
/* 250 */           throw new ConnectionPendingException();
/*     */         }
/*     */         
/* 253 */         boolean wasActive = AbstractNioChannel.this.isActive();
/* 254 */         if (AbstractNioChannel.this.doConnect(remoteAddress, localAddress)) {
/* 255 */           fulfillConnectPromise(promise, wasActive);
/*     */         } else {
/* 257 */           AbstractNioChannel.this.connectPromise = promise;
/* 258 */           AbstractNioChannel.this.requestedRemoteAddress = remoteAddress;
/*     */ 
/*     */           
/* 261 */           int connectTimeoutMillis = AbstractNioChannel.this.config().getConnectTimeoutMillis();
/* 262 */           if (connectTimeoutMillis > 0) {
/* 263 */             AbstractNioChannel.this.connectTimeoutFuture = (ScheduledFuture<?>)AbstractNioChannel.this.eventLoop().schedule(new Runnable()
/*     */                 {
/*     */                   public void run() {
/* 266 */                     ChannelPromise connectPromise = AbstractNioChannel.this.connectPromise;
/* 267 */                     ConnectTimeoutException cause = new ConnectTimeoutException("connection timed out: " + remoteAddress);
/*     */                     
/* 269 */                     if (connectPromise != null && connectPromise.tryFailure((Throwable)cause)) {
/* 270 */                       AbstractNioChannel.AbstractNioUnsafe.this.close(AbstractNioChannel.AbstractNioUnsafe.this.voidPromise());
/*     */                     }
/*     */                   }
/*     */                 },  connectTimeoutMillis, TimeUnit.MILLISECONDS);
/*     */           }
/*     */           
/* 276 */           promise.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */               {
/*     */                 public void operationComplete(ChannelFuture future) throws Exception {
/* 279 */                   if (future.isCancelled()) {
/* 280 */                     if (AbstractNioChannel.this.connectTimeoutFuture != null) {
/* 281 */                       AbstractNioChannel.this.connectTimeoutFuture.cancel(false);
/*     */                     }
/* 283 */                     AbstractNioChannel.this.connectPromise = null;
/* 284 */                     AbstractNioChannel.AbstractNioUnsafe.this.close(AbstractNioChannel.AbstractNioUnsafe.this.voidPromise());
/*     */                   } 
/*     */                 }
/*     */               });
/*     */         } 
/* 289 */       } catch (Throwable t) {
/* 290 */         promise.tryFailure(annotateConnectException(t, remoteAddress));
/* 291 */         closeIfClosed();
/*     */       } 
/*     */     }
/*     */     
/*     */     private void fulfillConnectPromise(ChannelPromise promise, boolean wasActive) {
/* 296 */       if (promise == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 303 */       boolean active = AbstractNioChannel.this.isActive();
/*     */ 
/*     */       
/* 306 */       boolean promiseSet = promise.trySuccess();
/*     */ 
/*     */ 
/*     */       
/* 310 */       if (!wasActive && active) {
/* 311 */         AbstractNioChannel.this.pipeline().fireChannelActive();
/*     */       }
/*     */ 
/*     */       
/* 315 */       if (!promiseSet) {
/* 316 */         close(voidPromise());
/*     */       }
/*     */     }
/*     */     
/*     */     private void fulfillConnectPromise(ChannelPromise promise, Throwable cause) {
/* 321 */       if (promise == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 327 */       promise.tryFailure(cause);
/* 328 */       closeIfClosed();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final void finishConnect() {
/* 336 */       assert AbstractNioChannel.this.eventLoop().inEventLoop();
/*     */       
/*     */       try {
/* 339 */         boolean wasActive = AbstractNioChannel.this.isActive();
/* 340 */         AbstractNioChannel.this.doFinishConnect();
/* 341 */         fulfillConnectPromise(AbstractNioChannel.this.connectPromise, wasActive);
/* 342 */       } catch (Throwable t) {
/* 343 */         fulfillConnectPromise(AbstractNioChannel.this.connectPromise, annotateConnectException(t, AbstractNioChannel.this.requestedRemoteAddress));
/*     */       }
/*     */       finally {
/*     */         
/* 347 */         if (AbstractNioChannel.this.connectTimeoutFuture != null) {
/* 348 */           AbstractNioChannel.this.connectTimeoutFuture.cancel(false);
/*     */         }
/* 350 */         AbstractNioChannel.this.connectPromise = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected final void flush0() {
/* 359 */       if (!isFlushPending()) {
/* 360 */         super.flush0();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final void forceFlush() {
/* 367 */       super.flush0();
/*     */     }
/*     */     
/*     */     private boolean isFlushPending() {
/* 371 */       SelectionKey selectionKey = AbstractNioChannel.this.selectionKey();
/* 372 */       return (selectionKey.isValid() && (selectionKey.interestOps() & 0x4) != 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isCompatible(EventLoop loop) {
/* 378 */     return loop instanceof NioEventLoop;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doRegister() throws Exception {
/* 383 */     boolean selected = false;
/*     */     while (true) {
/*     */       try {
/* 386 */         this.selectionKey = javaChannel().register(eventLoop().unwrappedSelector(), 0, this);
/*     */         return;
/* 388 */       } catch (CancelledKeyException e) {
/* 389 */         if (!selected) {
/*     */ 
/*     */           
/* 392 */           eventLoop().selectNow();
/* 393 */           selected = true; continue;
/*     */         }  break;
/*     */       } 
/*     */     } 
/* 397 */     throw e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doDeregister() throws Exception {
/* 405 */     eventLoop().cancel(selectionKey());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doBeginRead() throws Exception {
/* 411 */     SelectionKey selectionKey = this.selectionKey;
/* 412 */     if (!selectionKey.isValid()) {
/*     */       return;
/*     */     }
/*     */     
/* 416 */     this.readPending = true;
/*     */     
/* 418 */     int interestOps = selectionKey.interestOps();
/* 419 */     if ((interestOps & this.readInterestOp) == 0) {
/* 420 */       selectionKey.interestOps(interestOps | this.readInterestOp);
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
/*     */   protected final ByteBuf newDirectBuffer(ByteBuf buf) {
/* 440 */     int readableBytes = buf.readableBytes();
/* 441 */     if (readableBytes == 0) {
/* 442 */       ReferenceCountUtil.safeRelease(buf);
/* 443 */       return Unpooled.EMPTY_BUFFER;
/*     */     } 
/*     */     
/* 446 */     ByteBufAllocator alloc = alloc();
/* 447 */     if (alloc.isDirectBufferPooled()) {
/* 448 */       ByteBuf byteBuf = alloc.directBuffer(readableBytes);
/* 449 */       byteBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
/* 450 */       ReferenceCountUtil.safeRelease(buf);
/* 451 */       return byteBuf;
/*     */     } 
/*     */     
/* 454 */     ByteBuf directBuf = ByteBufUtil.threadLocalDirectBuffer();
/* 455 */     if (directBuf != null) {
/* 456 */       directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
/* 457 */       ReferenceCountUtil.safeRelease(buf);
/* 458 */       return directBuf;
/*     */     } 
/*     */ 
/*     */     
/* 462 */     return buf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ByteBuf newDirectBuffer(ReferenceCounted holder, ByteBuf buf) {
/* 472 */     int readableBytes = buf.readableBytes();
/* 473 */     if (readableBytes == 0) {
/* 474 */       ReferenceCountUtil.safeRelease(holder);
/* 475 */       return Unpooled.EMPTY_BUFFER;
/*     */     } 
/*     */     
/* 478 */     ByteBufAllocator alloc = alloc();
/* 479 */     if (alloc.isDirectBufferPooled()) {
/* 480 */       ByteBuf byteBuf = alloc.directBuffer(readableBytes);
/* 481 */       byteBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
/* 482 */       ReferenceCountUtil.safeRelease(holder);
/* 483 */       return byteBuf;
/*     */     } 
/*     */     
/* 486 */     ByteBuf directBuf = ByteBufUtil.threadLocalDirectBuffer();
/* 487 */     if (directBuf != null) {
/* 488 */       directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
/* 489 */       ReferenceCountUtil.safeRelease(holder);
/* 490 */       return directBuf;
/*     */     } 
/*     */ 
/*     */     
/* 494 */     if (holder != buf) {
/*     */       
/* 496 */       buf.retain();
/* 497 */       ReferenceCountUtil.safeRelease(holder);
/*     */     } 
/*     */     
/* 500 */     return buf;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClose() throws Exception {
/* 505 */     ChannelPromise promise = this.connectPromise;
/* 506 */     if (promise != null) {
/*     */       
/* 508 */       promise.tryFailure(DO_CLOSE_CLOSED_CHANNEL_EXCEPTION);
/* 509 */       this.connectPromise = null;
/*     */     } 
/*     */     
/* 512 */     ScheduledFuture<?> future = this.connectTimeoutFuture;
/* 513 */     if (future != null) {
/* 514 */       future.cancel(false);
/* 515 */       this.connectTimeoutFuture = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract boolean doConnect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2) throws Exception;
/*     */   
/*     */   protected abstract void doFinishConnect() throws Exception;
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\nio\AbstractNioChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */