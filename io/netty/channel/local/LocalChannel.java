/*     */ package io.netty.channel.local;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.AbstractChannel;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.DefaultChannelConfig;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.PreferHeapByteBufAllocator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.SingleThreadEventExecutor;
/*     */ import io.netty.util.internal.InternalThreadLocalMap;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.ConnectException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.AlreadyConnectedException;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.ConnectionPendingException;
/*     */ import java.nio.channels.NotYetConnectedException;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
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
/*     */ public class LocalChannel
/*     */   extends AbstractChannel
/*     */ {
/*  52 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(LocalChannel.class);
/*     */ 
/*     */   
/*  55 */   private static final AtomicReferenceFieldUpdater<LocalChannel, Future> FINISH_READ_FUTURE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(LocalChannel.class, Future.class, "finishReadFuture");
/*  56 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
/*     */   private static final int MAX_READER_STACK_DEPTH = 8;
/*  58 */   private static final ClosedChannelException DO_WRITE_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), LocalChannel.class, "doWrite(...)");
/*     */   
/*  60 */   private static final ClosedChannelException DO_CLOSE_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), LocalChannel.class, "doClose()");
/*     */   
/*     */   private enum State {
/*  63 */     OPEN, BOUND, CONNECTED, CLOSED;
/*     */   }
/*  65 */   private final ChannelConfig config = (ChannelConfig)new DefaultChannelConfig((Channel)this);
/*     */   
/*  67 */   final Queue<Object> inboundBuffer = PlatformDependent.newSpscQueue();
/*  68 */   private final Runnable readTask = new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/*  72 */         if (!LocalChannel.this.inboundBuffer.isEmpty()) {
/*  73 */           LocalChannel.this.readInbound();
/*     */         }
/*     */       }
/*     */     };
/*     */   
/*  78 */   private final Runnable shutdownHook = new Runnable()
/*     */     {
/*     */       public void run() {
/*  81 */         LocalChannel.this.unsafe().close(LocalChannel.this.unsafe().voidPromise());
/*     */       }
/*     */     };
/*     */   
/*     */   private volatile State state;
/*     */   private volatile LocalChannel peer;
/*     */   private volatile LocalAddress localAddress;
/*     */   private volatile LocalAddress remoteAddress;
/*     */   private volatile ChannelPromise connectPromise;
/*     */   private volatile boolean readInProgress;
/*     */   private volatile boolean writeInProgress;
/*     */   private volatile Future<?> finishReadFuture;
/*     */   
/*     */   public LocalChannel() {
/*  95 */     super(null);
/*  96 */     config().setAllocator((ByteBufAllocator)new PreferHeapByteBufAllocator(this.config.getAllocator()));
/*     */   }
/*     */   
/*     */   protected LocalChannel(LocalServerChannel parent, LocalChannel peer) {
/* 100 */     super((Channel)parent);
/* 101 */     config().setAllocator((ByteBufAllocator)new PreferHeapByteBufAllocator(this.config.getAllocator()));
/* 102 */     this.peer = peer;
/* 103 */     this.localAddress = parent.localAddress();
/* 104 */     this.remoteAddress = peer.localAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelMetadata metadata() {
/* 109 */     return METADATA;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelConfig config() {
/* 114 */     return this.config;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalServerChannel parent() {
/* 119 */     return (LocalServerChannel)super.parent();
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalAddress localAddress() {
/* 124 */     return (LocalAddress)super.localAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalAddress remoteAddress() {
/* 129 */     return (LocalAddress)super.remoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 134 */     return (this.state != State.CLOSED);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/* 139 */     return (this.state == State.CONNECTED);
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractChannel.AbstractUnsafe newUnsafe() {
/* 144 */     return new LocalUnsafe();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isCompatible(EventLoop loop) {
/* 149 */     return loop instanceof io.netty.channel.SingleThreadEventLoop;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress localAddress0() {
/* 154 */     return this.localAddress;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress remoteAddress0() {
/* 159 */     return this.remoteAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doRegister() throws Exception {
/* 169 */     if (this.peer != null && parent() != null) {
/*     */ 
/*     */       
/* 172 */       final LocalChannel peer = this.peer;
/* 173 */       this.state = State.CONNECTED;
/*     */       
/* 175 */       peer.remoteAddress = (parent() == null) ? null : parent().localAddress();
/* 176 */       peer.state = State.CONNECTED;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 182 */       peer.eventLoop().execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 185 */               ChannelPromise promise = peer.connectPromise;
/*     */ 
/*     */ 
/*     */               
/* 189 */               if (promise != null && promise.trySuccess()) {
/* 190 */                 peer.pipeline().fireChannelActive();
/*     */               }
/*     */             }
/*     */           });
/*     */     } 
/* 195 */     ((SingleThreadEventExecutor)eventLoop()).addShutdownHook(this.shutdownHook);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception {
/* 200 */     this
/* 201 */       .localAddress = LocalChannelRegistry.register((Channel)this, this.localAddress, localAddress);
/*     */     
/* 203 */     this.state = State.BOUND;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDisconnect() throws Exception {
/* 208 */     doClose();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClose() throws Exception {
/* 213 */     final LocalChannel peer = this.peer;
/* 214 */     State oldState = this.state;
/*     */     try {
/* 216 */       if (oldState != State.CLOSED) {
/*     */         
/* 218 */         if (this.localAddress != null) {
/* 219 */           if (parent() == null) {
/* 220 */             LocalChannelRegistry.unregister(this.localAddress);
/*     */           }
/* 222 */           this.localAddress = null;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 227 */         this.state = State.CLOSED;
/*     */ 
/*     */         
/* 230 */         if (this.writeInProgress && peer != null) {
/* 231 */           finishPeerRead(peer);
/*     */         }
/*     */         
/* 234 */         ChannelPromise promise = this.connectPromise;
/* 235 */         if (promise != null) {
/*     */           
/* 237 */           promise.tryFailure(DO_CLOSE_CLOSED_CHANNEL_EXCEPTION);
/* 238 */           this.connectPromise = null;
/*     */         } 
/*     */       } 
/*     */       
/* 242 */       if (peer != null) {
/* 243 */         this.peer = null;
/*     */ 
/*     */ 
/*     */         
/* 247 */         EventLoop peerEventLoop = peer.eventLoop();
/* 248 */         final boolean peerIsActive = peer.isActive();
/*     */         try {
/* 250 */           peerEventLoop.execute(new Runnable()
/*     */               {
/*     */                 public void run() {
/* 253 */                   peer.tryClose(peerIsActive);
/*     */                 }
/*     */               });
/* 256 */         } catch (Throwable cause) {
/* 257 */           logger.warn("Releasing Inbound Queues for channels {}-{} because exception occurred!", new Object[] { this, peer, cause });
/*     */           
/* 259 */           if (peerEventLoop.inEventLoop()) {
/* 260 */             peer.releaseInboundBuffers();
/*     */           }
/*     */           else {
/*     */             
/* 264 */             peer.close();
/*     */           } 
/* 266 */           PlatformDependent.throwException(cause);
/*     */         } 
/*     */       } 
/*     */     } finally {
/*     */       
/* 271 */       if (oldState != null && oldState != State.CLOSED)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 276 */         releaseInboundBuffers();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tryClose(boolean isActive) {
/* 282 */     if (isActive) {
/* 283 */       unsafe().close(unsafe().voidPromise());
/*     */     } else {
/* 285 */       releaseInboundBuffers();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doDeregister() throws Exception {
/* 292 */     ((SingleThreadEventExecutor)eventLoop()).removeShutdownHook(this.shutdownHook);
/*     */   }
/*     */   
/*     */   private void readInbound() {
/* 296 */     RecvByteBufAllocator.Handle handle = unsafe().recvBufAllocHandle();
/* 297 */     handle.reset(config());
/* 298 */     ChannelPipeline pipeline = pipeline();
/*     */     do {
/* 300 */       Object received = this.inboundBuffer.poll();
/* 301 */       if (received == null) {
/*     */         break;
/*     */       }
/* 304 */       pipeline.fireChannelRead(received);
/* 305 */     } while (handle.continueReading());
/*     */     
/* 307 */     pipeline.fireChannelReadComplete();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doBeginRead() throws Exception {
/* 312 */     if (this.readInProgress) {
/*     */       return;
/*     */     }
/*     */     
/* 316 */     Queue<Object> inboundBuffer = this.inboundBuffer;
/* 317 */     if (inboundBuffer.isEmpty()) {
/* 318 */       this.readInProgress = true;
/*     */       
/*     */       return;
/*     */     } 
/* 322 */     InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
/* 323 */     Integer stackDepth = Integer.valueOf(threadLocals.localChannelReaderStackDepth());
/* 324 */     if (stackDepth.intValue() < 8) {
/* 325 */       threadLocals.setLocalChannelReaderStackDepth(stackDepth.intValue() + 1);
/*     */       try {
/* 327 */         readInbound();
/*     */       } finally {
/* 329 */         threadLocals.setLocalChannelReaderStackDepth(stackDepth.intValue());
/*     */       } 
/*     */     } else {
/*     */       try {
/* 333 */         eventLoop().execute(this.readTask);
/* 334 */       } catch (Throwable cause) {
/* 335 */         logger.warn("Closing Local channels {}-{} because exception occurred!", new Object[] { this, this.peer, cause });
/* 336 */         close();
/* 337 */         this.peer.close();
/* 338 */         PlatformDependent.throwException(cause);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception {
/* 345 */     switch (this.state) {
/*     */       case OPEN:
/*     */       case BOUND:
/* 348 */         throw new NotYetConnectedException();
/*     */       case CLOSED:
/* 350 */         throw DO_WRITE_CLOSED_CHANNEL_EXCEPTION;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 355 */     LocalChannel peer = this.peer;
/*     */     
/* 357 */     this.writeInProgress = true;
/*     */     try {
/*     */       while (true) {
/* 360 */         Object msg = in.current();
/* 361 */         if (msg == null) {
/*     */           break;
/*     */         }
/*     */ 
/*     */         
/*     */         try {
/* 367 */           if (peer.state == State.CONNECTED) {
/* 368 */             peer.inboundBuffer.add(ReferenceCountUtil.retain(msg));
/* 369 */             in.remove(); continue;
/*     */           } 
/* 371 */           in.remove(DO_WRITE_CLOSED_CHANNEL_EXCEPTION);
/*     */         }
/* 373 */         catch (Throwable cause) {
/* 374 */           in.remove(cause);
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 383 */       this.writeInProgress = false;
/*     */     } 
/*     */     
/* 386 */     finishPeerRead(peer);
/*     */   }
/*     */ 
/*     */   
/*     */   private void finishPeerRead(LocalChannel peer) {
/* 391 */     if (peer.eventLoop() == eventLoop() && !peer.writeInProgress) {
/* 392 */       finishPeerRead0(peer);
/*     */     } else {
/* 394 */       runFinishPeerReadTask(peer);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void runFinishPeerReadTask(final LocalChannel peer) {
/* 401 */     Runnable finishPeerReadTask = new Runnable()
/*     */       {
/*     */         public void run() {
/* 404 */           LocalChannel.this.finishPeerRead0(peer);
/*     */         }
/*     */       };
/*     */     try {
/* 408 */       if (peer.writeInProgress) {
/* 409 */         peer.finishReadFuture = peer.eventLoop().submit(finishPeerReadTask);
/*     */       } else {
/* 411 */         peer.eventLoop().execute(finishPeerReadTask);
/*     */       } 
/* 413 */     } catch (Throwable cause) {
/* 414 */       logger.warn("Closing Local channels {}-{} because exception occurred!", new Object[] { this, peer, cause });
/* 415 */       close();
/* 416 */       peer.close();
/* 417 */       PlatformDependent.throwException(cause);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void releaseInboundBuffers() {
/* 422 */     assert eventLoop() == null || eventLoop().inEventLoop();
/* 423 */     this.readInProgress = false;
/* 424 */     Queue<Object> inboundBuffer = this.inboundBuffer;
/*     */     Object msg;
/* 426 */     while ((msg = inboundBuffer.poll()) != null) {
/* 427 */       ReferenceCountUtil.release(msg);
/*     */     }
/*     */   }
/*     */   
/*     */   private void finishPeerRead0(LocalChannel peer) {
/* 432 */     Future<?> peerFinishReadFuture = peer.finishReadFuture;
/* 433 */     if (peerFinishReadFuture != null) {
/* 434 */       if (!peerFinishReadFuture.isDone()) {
/* 435 */         runFinishPeerReadTask(peer);
/*     */         
/*     */         return;
/*     */       } 
/* 439 */       FINISH_READ_FUTURE_UPDATER.compareAndSet(peer, peerFinishReadFuture, null);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 444 */     if (peer.readInProgress && !peer.inboundBuffer.isEmpty()) {
/* 445 */       peer.readInProgress = false;
/* 446 */       peer.readInbound();
/*     */     } 
/*     */   }
/*     */   private class LocalUnsafe extends AbstractChannel.AbstractUnsafe { private LocalUnsafe() {
/* 450 */       super(LocalChannel.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
/* 455 */       if (!promise.setUncancellable() || !ensureOpen(promise)) {
/*     */         return;
/*     */       }
/*     */       
/* 459 */       if (LocalChannel.this.state == LocalChannel.State.CONNECTED) {
/* 460 */         Exception cause = new AlreadyConnectedException();
/* 461 */         safeSetFailure(promise, cause);
/* 462 */         LocalChannel.this.pipeline().fireExceptionCaught(cause);
/*     */         
/*     */         return;
/*     */       } 
/* 466 */       if (LocalChannel.this.connectPromise != null) {
/* 467 */         throw new ConnectionPendingException();
/*     */       }
/*     */       
/* 470 */       LocalChannel.this.connectPromise = promise;
/*     */       
/* 472 */       if (LocalChannel.this.state != LocalChannel.State.BOUND)
/*     */       {
/* 474 */         if (localAddress == null) {
/* 475 */           localAddress = new LocalAddress((Channel)LocalChannel.this);
/*     */         }
/*     */       }
/*     */       
/* 479 */       if (localAddress != null) {
/*     */         try {
/* 481 */           LocalChannel.this.doBind(localAddress);
/* 482 */         } catch (Throwable t) {
/* 483 */           safeSetFailure(promise, t);
/* 484 */           close(voidPromise());
/*     */           
/*     */           return;
/*     */         } 
/*     */       }
/* 489 */       Channel boundChannel = LocalChannelRegistry.get(remoteAddress);
/* 490 */       if (!(boundChannel instanceof LocalServerChannel)) {
/* 491 */         Exception cause = new ConnectException("connection refused: " + remoteAddress);
/* 492 */         safeSetFailure(promise, cause);
/* 493 */         close(voidPromise());
/*     */         
/*     */         return;
/*     */       } 
/* 497 */       LocalServerChannel serverChannel = (LocalServerChannel)boundChannel;
/* 498 */       LocalChannel.this.peer = serverChannel.serve(LocalChannel.this);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\local\LocalChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */