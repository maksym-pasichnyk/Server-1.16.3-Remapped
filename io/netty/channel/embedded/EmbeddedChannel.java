/*     */ package io.netty.channel.embedded;
/*     */ 
/*     */ import io.netty.channel.AbstractChannel;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelId;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.DefaultChannelConfig;
/*     */ import io.netty.channel.DefaultChannelPipeline;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.RecyclableArrayList;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Queue;
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
/*     */ public class EmbeddedChannel
/*     */   extends AbstractChannel
/*     */ {
/*  51 */   private static final SocketAddress LOCAL_ADDRESS = new EmbeddedSocketAddress();
/*  52 */   private static final SocketAddress REMOTE_ADDRESS = new EmbeddedSocketAddress();
/*     */   
/*  54 */   private static final ChannelHandler[] EMPTY_HANDLERS = new ChannelHandler[0];
/*  55 */   private enum State { OPEN, ACTIVE, CLOSED; }
/*     */   
/*  57 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(EmbeddedChannel.class);
/*     */   
/*  59 */   private static final ChannelMetadata METADATA_NO_DISCONNECT = new ChannelMetadata(false);
/*  60 */   private static final ChannelMetadata METADATA_DISCONNECT = new ChannelMetadata(true);
/*     */   
/*  62 */   private final EmbeddedEventLoop loop = new EmbeddedEventLoop();
/*  63 */   private final ChannelFutureListener recordExceptionListener = new ChannelFutureListener()
/*     */     {
/*     */       public void operationComplete(ChannelFuture future) throws Exception {
/*  66 */         EmbeddedChannel.this.recordException(future);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   private final ChannelMetadata metadata;
/*     */   
/*     */   private final ChannelConfig config;
/*     */   
/*     */   private Queue<Object> inboundMessages;
/*     */   
/*     */   private Queue<Object> outboundMessages;
/*     */   private Throwable lastException;
/*     */   private State state;
/*     */   
/*     */   public EmbeddedChannel() {
/*  82 */     this(EMPTY_HANDLERS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EmbeddedChannel(ChannelId channelId) {
/*  91 */     this(channelId, EMPTY_HANDLERS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EmbeddedChannel(ChannelHandler... handlers) {
/* 100 */     this(EmbeddedChannelId.INSTANCE, handlers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EmbeddedChannel(boolean hasDisconnect, ChannelHandler... handlers) {
/* 111 */     this(EmbeddedChannelId.INSTANCE, hasDisconnect, handlers);
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
/*     */   public EmbeddedChannel(boolean register, boolean hasDisconnect, ChannelHandler... handlers) {
/* 124 */     this(EmbeddedChannelId.INSTANCE, register, hasDisconnect, handlers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EmbeddedChannel(ChannelId channelId, ChannelHandler... handlers) {
/* 135 */     this(channelId, false, handlers);
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
/*     */   public EmbeddedChannel(ChannelId channelId, boolean hasDisconnect, ChannelHandler... handlers) {
/* 148 */     this(channelId, true, hasDisconnect, handlers);
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
/*     */   public EmbeddedChannel(ChannelId channelId, boolean register, boolean hasDisconnect, ChannelHandler... handlers) {
/* 164 */     super(null, channelId);
/* 165 */     this.metadata = metadata(hasDisconnect);
/* 166 */     this.config = (ChannelConfig)new DefaultChannelConfig((Channel)this);
/* 167 */     setup(register, handlers);
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
/*     */   public EmbeddedChannel(ChannelId channelId, boolean hasDisconnect, ChannelConfig config, ChannelHandler... handlers) {
/* 182 */     super(null, channelId);
/* 183 */     this.metadata = metadata(hasDisconnect);
/* 184 */     this.config = (ChannelConfig)ObjectUtil.checkNotNull(config, "config");
/* 185 */     setup(true, handlers);
/*     */   }
/*     */   
/*     */   private static ChannelMetadata metadata(boolean hasDisconnect) {
/* 189 */     return hasDisconnect ? METADATA_DISCONNECT : METADATA_NO_DISCONNECT;
/*     */   }
/*     */   
/*     */   private void setup(boolean register, ChannelHandler... handlers) {
/* 193 */     ObjectUtil.checkNotNull(handlers, "handlers");
/* 194 */     ChannelPipeline p = pipeline();
/* 195 */     p.addLast(new ChannelHandler[] { (ChannelHandler)new ChannelInitializer<Channel>()
/*     */           {
/*     */             protected void initChannel(Channel ch) throws Exception {
/* 198 */               ChannelPipeline pipeline = ch.pipeline();
/* 199 */               for (ChannelHandler h : handlers) {
/* 200 */                 if (h == null) {
/*     */                   break;
/*     */                 }
/* 203 */                 pipeline.addLast(new ChannelHandler[] { h });
/*     */               } 
/*     */             }
/*     */           } });
/* 207 */     if (register) {
/* 208 */       ChannelFuture future = this.loop.register((Channel)this);
/* 209 */       assert future.isDone();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void register() throws Exception {
/* 217 */     ChannelFuture future = this.loop.register((Channel)this);
/* 218 */     assert future.isDone();
/* 219 */     Throwable cause = future.cause();
/* 220 */     if (cause != null) {
/* 221 */       PlatformDependent.throwException(cause);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected final DefaultChannelPipeline newChannelPipeline() {
/* 227 */     return new EmbeddedChannelPipeline(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelMetadata metadata() {
/* 232 */     return this.metadata;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelConfig config() {
/* 237 */     return this.config;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 242 */     return (this.state != State.CLOSED);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/* 247 */     return (this.state == State.ACTIVE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Queue<Object> inboundMessages() {
/* 254 */     if (this.inboundMessages == null) {
/* 255 */       this.inboundMessages = new ArrayDeque();
/*     */     }
/* 257 */     return this.inboundMessages;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Queue<Object> lastInboundBuffer() {
/* 265 */     return inboundMessages();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Queue<Object> outboundMessages() {
/* 272 */     if (this.outboundMessages == null) {
/* 273 */       this.outboundMessages = new ArrayDeque();
/*     */     }
/* 275 */     return this.outboundMessages;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Queue<Object> lastOutboundBuffer() {
/* 283 */     return outboundMessages();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T readInbound() {
/* 291 */     T message = (T)poll(this.inboundMessages);
/* 292 */     if (message != null) {
/* 293 */       ReferenceCountUtil.touch(message, "Caller of readInbound() will handle the message from this point");
/*     */     }
/* 295 */     return message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T readOutbound() {
/* 303 */     T message = (T)poll(this.outboundMessages);
/* 304 */     if (message != null) {
/* 305 */       ReferenceCountUtil.touch(message, "Caller of readOutbound() will handle the message from this point.");
/*     */     }
/* 307 */     return message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean writeInbound(Object... msgs) {
/* 318 */     ensureOpen();
/* 319 */     if (msgs.length == 0) {
/* 320 */       return isNotEmpty(this.inboundMessages);
/*     */     }
/*     */     
/* 323 */     ChannelPipeline p = pipeline();
/* 324 */     for (Object m : msgs) {
/* 325 */       p.fireChannelRead(m);
/*     */     }
/*     */     
/* 328 */     flushInbound(false, voidPromise());
/* 329 */     return isNotEmpty(this.inboundMessages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeOneInbound(Object msg) {
/* 339 */     return writeOneInbound(msg, newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeOneInbound(Object msg, ChannelPromise promise) {
/* 349 */     if (checkOpen(true)) {
/* 350 */       pipeline().fireChannelRead(msg);
/*     */     }
/* 352 */     return checkException(promise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EmbeddedChannel flushInbound() {
/* 361 */     flushInbound(true, voidPromise());
/* 362 */     return this;
/*     */   }
/*     */   
/*     */   private ChannelFuture flushInbound(boolean recordException, ChannelPromise promise) {
/* 366 */     if (checkOpen(recordException)) {
/* 367 */       pipeline().fireChannelReadComplete();
/* 368 */       runPendingTasks();
/*     */     } 
/*     */     
/* 371 */     return checkException(promise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean writeOutbound(Object... msgs) {
/* 381 */     ensureOpen();
/* 382 */     if (msgs.length == 0) {
/* 383 */       return isNotEmpty(this.outboundMessages);
/*     */     }
/*     */     
/* 386 */     RecyclableArrayList futures = RecyclableArrayList.newInstance(msgs.length);
/*     */     try {
/* 388 */       for (Object m : msgs) {
/* 389 */         if (m == null) {
/*     */           break;
/*     */         }
/* 392 */         futures.add(write(m));
/*     */       } 
/*     */       
/* 395 */       flushOutbound0();
/*     */       
/* 397 */       int size = futures.size();
/* 398 */       for (int i = 0; i < size; i++) {
/* 399 */         ChannelFuture future = (ChannelFuture)futures.get(i);
/* 400 */         if (future.isDone()) {
/* 401 */           recordException(future);
/*     */         } else {
/*     */           
/* 404 */           future.addListener((GenericFutureListener)this.recordExceptionListener);
/*     */         } 
/*     */       } 
/*     */       
/* 408 */       checkException();
/* 409 */       return isNotEmpty(this.outboundMessages);
/*     */     } finally {
/* 411 */       futures.recycle();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeOneOutbound(Object msg) {
/* 422 */     return writeOneOutbound(msg, newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeOneOutbound(Object msg, ChannelPromise promise) {
/* 432 */     if (checkOpen(true)) {
/* 433 */       return write(msg, promise);
/*     */     }
/* 435 */     return checkException(promise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EmbeddedChannel flushOutbound() {
/* 444 */     if (checkOpen(true)) {
/* 445 */       flushOutbound0();
/*     */     }
/* 447 */     checkException(voidPromise());
/* 448 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void flushOutbound0() {
/* 454 */     runPendingTasks();
/*     */     
/* 456 */     flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean finish() {
/* 465 */     return finish(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean finishAndReleaseAll() {
/* 475 */     return finish(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean finish(boolean releaseAll) {
/* 485 */     close();
/*     */     try {
/* 487 */       checkException();
/* 488 */       return (isNotEmpty(this.inboundMessages) || isNotEmpty(this.outboundMessages));
/*     */     } finally {
/* 490 */       if (releaseAll) {
/* 491 */         releaseAll(this.inboundMessages);
/* 492 */         releaseAll(this.outboundMessages);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean releaseInbound() {
/* 502 */     return releaseAll(this.inboundMessages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean releaseOutbound() {
/* 510 */     return releaseAll(this.outboundMessages);
/*     */   }
/*     */   
/*     */   private static boolean releaseAll(Queue<Object> queue) {
/* 514 */     if (isNotEmpty(queue)) {
/*     */       while (true) {
/* 516 */         Object msg = queue.poll();
/* 517 */         if (msg == null) {
/*     */           break;
/*     */         }
/* 520 */         ReferenceCountUtil.release(msg);
/*     */       } 
/* 522 */       return true;
/*     */     } 
/* 524 */     return false;
/*     */   }
/*     */   
/*     */   private void finishPendingTasks(boolean cancel) {
/* 528 */     runPendingTasks();
/* 529 */     if (cancel)
/*     */     {
/* 531 */       this.loop.cancelScheduledTasks();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final ChannelFuture close() {
/* 537 */     return close(newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   public final ChannelFuture disconnect() {
/* 542 */     return disconnect(newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ChannelFuture close(ChannelPromise promise) {
/* 549 */     runPendingTasks();
/* 550 */     ChannelFuture future = super.close(promise);
/*     */ 
/*     */     
/* 553 */     finishPendingTasks(true);
/* 554 */     return future;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ChannelFuture disconnect(ChannelPromise promise) {
/* 559 */     ChannelFuture future = super.disconnect(promise);
/* 560 */     finishPendingTasks(!this.metadata.hasDisconnect());
/* 561 */     return future;
/*     */   }
/*     */   
/*     */   private static boolean isNotEmpty(Queue<Object> queue) {
/* 565 */     return (queue != null && !queue.isEmpty());
/*     */   }
/*     */   
/*     */   private static Object poll(Queue<Object> queue) {
/* 569 */     return (queue != null) ? queue.poll() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void runPendingTasks() {
/*     */     try {
/* 578 */       this.loop.runTasks();
/* 579 */     } catch (Exception e) {
/* 580 */       recordException(e);
/*     */     } 
/*     */     
/*     */     try {
/* 584 */       this.loop.runScheduledTasks();
/* 585 */     } catch (Exception e) {
/* 586 */       recordException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long runScheduledPendingTasks() {
/*     */     try {
/* 597 */       return this.loop.runScheduledTasks();
/* 598 */     } catch (Exception e) {
/* 599 */       recordException(e);
/* 600 */       return this.loop.nextScheduledTask();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void recordException(ChannelFuture future) {
/* 605 */     if (!future.isSuccess()) {
/* 606 */       recordException(future.cause());
/*     */     }
/*     */   }
/*     */   
/*     */   private void recordException(Throwable cause) {
/* 611 */     if (this.lastException == null) {
/* 612 */       this.lastException = cause;
/*     */     } else {
/* 614 */       logger.warn("More than one exception was raised. Will report only the first one and log others.", cause);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ChannelFuture checkException(ChannelPromise promise) {
/* 624 */     Throwable t = this.lastException;
/* 625 */     if (t != null) {
/* 626 */       this.lastException = null;
/*     */       
/* 628 */       if (promise.isVoid()) {
/* 629 */         PlatformDependent.throwException(t);
/*     */       }
/*     */       
/* 632 */       return (ChannelFuture)promise.setFailure(t);
/*     */     } 
/*     */     
/* 635 */     return (ChannelFuture)promise.setSuccess();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkException() {
/* 642 */     checkException(voidPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkOpen(boolean recordException) {
/* 650 */     if (!isOpen()) {
/* 651 */       if (recordException) {
/* 652 */         recordException(new ClosedChannelException());
/*     */       }
/* 654 */       return false;
/*     */     } 
/*     */     
/* 657 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void ensureOpen() {
/* 664 */     if (!checkOpen(true)) {
/* 665 */       checkException();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isCompatible(EventLoop loop) {
/* 671 */     return loop instanceof EmbeddedEventLoop;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress localAddress0() {
/* 676 */     return isActive() ? LOCAL_ADDRESS : null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress remoteAddress0() {
/* 681 */     return isActive() ? REMOTE_ADDRESS : null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doRegister() throws Exception {
/* 686 */     this.state = State.ACTIVE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doDisconnect() throws Exception {
/* 696 */     if (!this.metadata.hasDisconnect()) {
/* 697 */       doClose();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClose() throws Exception {
/* 703 */     this.state = State.CLOSED;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doBeginRead() throws Exception {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractChannel.AbstractUnsafe newUnsafe() {
/* 713 */     return new EmbeddedUnsafe();
/*     */   }
/*     */ 
/*     */   
/*     */   public Channel.Unsafe unsafe() {
/* 718 */     return ((EmbeddedUnsafe)super.unsafe()).wrapped;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception {
/*     */     while (true) {
/* 724 */       Object msg = in.current();
/* 725 */       if (msg == null) {
/*     */         break;
/*     */       }
/*     */       
/* 729 */       ReferenceCountUtil.retain(msg);
/* 730 */       handleOutboundMessage(msg);
/* 731 */       in.remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleOutboundMessage(Object msg) {
/* 741 */     outboundMessages().add(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleInboundMessage(Object msg) {
/* 748 */     inboundMessages().add(msg);
/*     */   }
/*     */   private final class EmbeddedUnsafe extends AbstractChannel.AbstractUnsafe { private EmbeddedUnsafe() {
/* 751 */       super(EmbeddedChannel.this);
/*     */ 
/*     */ 
/*     */       
/* 755 */       this.wrapped = new Channel.Unsafe()
/*     */         {
/*     */           public RecvByteBufAllocator.Handle recvBufAllocHandle() {
/* 758 */             return EmbeddedChannel.EmbeddedUnsafe.this.recvBufAllocHandle();
/*     */           }
/*     */ 
/*     */           
/*     */           public SocketAddress localAddress() {
/* 763 */             return EmbeddedChannel.EmbeddedUnsafe.this.localAddress();
/*     */           }
/*     */ 
/*     */           
/*     */           public SocketAddress remoteAddress() {
/* 768 */             return EmbeddedChannel.EmbeddedUnsafe.this.remoteAddress();
/*     */           }
/*     */ 
/*     */           
/*     */           public void register(EventLoop eventLoop, ChannelPromise promise) {
/* 773 */             EmbeddedChannel.EmbeddedUnsafe.this.register(eventLoop, promise);
/* 774 */             EmbeddedChannel.this.runPendingTasks();
/*     */           }
/*     */ 
/*     */           
/*     */           public void bind(SocketAddress localAddress, ChannelPromise promise) {
/* 779 */             EmbeddedChannel.EmbeddedUnsafe.this.bind(localAddress, promise);
/* 780 */             EmbeddedChannel.this.runPendingTasks();
/*     */           }
/*     */ 
/*     */           
/*     */           public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
/* 785 */             EmbeddedChannel.EmbeddedUnsafe.this.connect(remoteAddress, localAddress, promise);
/* 786 */             EmbeddedChannel.this.runPendingTasks();
/*     */           }
/*     */ 
/*     */           
/*     */           public void disconnect(ChannelPromise promise) {
/* 791 */             EmbeddedChannel.EmbeddedUnsafe.this.disconnect(promise);
/* 792 */             EmbeddedChannel.this.runPendingTasks();
/*     */           }
/*     */ 
/*     */           
/*     */           public void close(ChannelPromise promise) {
/* 797 */             EmbeddedChannel.EmbeddedUnsafe.this.close(promise);
/* 798 */             EmbeddedChannel.this.runPendingTasks();
/*     */           }
/*     */ 
/*     */           
/*     */           public void closeForcibly() {
/* 803 */             EmbeddedChannel.EmbeddedUnsafe.this.closeForcibly();
/* 804 */             EmbeddedChannel.this.runPendingTasks();
/*     */           }
/*     */ 
/*     */           
/*     */           public void deregister(ChannelPromise promise) {
/* 809 */             EmbeddedChannel.EmbeddedUnsafe.this.deregister(promise);
/* 810 */             EmbeddedChannel.this.runPendingTasks();
/*     */           }
/*     */ 
/*     */           
/*     */           public void beginRead() {
/* 815 */             EmbeddedChannel.EmbeddedUnsafe.this.beginRead();
/* 816 */             EmbeddedChannel.this.runPendingTasks();
/*     */           }
/*     */ 
/*     */           
/*     */           public void write(Object msg, ChannelPromise promise) {
/* 821 */             EmbeddedChannel.EmbeddedUnsafe.this.write(msg, promise);
/* 822 */             EmbeddedChannel.this.runPendingTasks();
/*     */           }
/*     */ 
/*     */           
/*     */           public void flush() {
/* 827 */             EmbeddedChannel.EmbeddedUnsafe.this.flush();
/* 828 */             EmbeddedChannel.this.runPendingTasks();
/*     */           }
/*     */ 
/*     */           
/*     */           public ChannelPromise voidPromise() {
/* 833 */             return EmbeddedChannel.EmbeddedUnsafe.this.voidPromise();
/*     */           }
/*     */ 
/*     */           
/*     */           public ChannelOutboundBuffer outboundBuffer() {
/* 838 */             return EmbeddedChannel.EmbeddedUnsafe.this.outboundBuffer();
/*     */           }
/*     */         };
/*     */     }
/*     */     final Channel.Unsafe wrapped;
/*     */     public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
/* 844 */       safeSetSuccess(promise);
/*     */     } }
/*     */ 
/*     */   
/*     */   private final class EmbeddedChannelPipeline extends DefaultChannelPipeline {
/*     */     EmbeddedChannelPipeline(EmbeddedChannel channel) {
/* 850 */       super((Channel)channel);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void onUnhandledInboundException(Throwable cause) {
/* 855 */       EmbeddedChannel.this.recordException(cause);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void onUnhandledInboundMessage(Object msg) {
/* 860 */       EmbeddedChannel.this.handleInboundMessage(msg);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\embedded\EmbeddedChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */