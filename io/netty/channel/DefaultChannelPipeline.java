/*      */ package io.netty.channel;
/*      */ 
/*      */ import io.netty.util.ReferenceCountUtil;
/*      */ import io.netty.util.ResourceLeakDetector;
/*      */ import io.netty.util.concurrent.EventExecutor;
/*      */ import io.netty.util.concurrent.EventExecutorGroup;
/*      */ import io.netty.util.concurrent.FastThreadLocal;
/*      */ import io.netty.util.internal.ObjectUtil;
/*      */ import io.netty.util.internal.StringUtil;
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import java.net.SocketAddress;
/*      */ import java.util.ArrayList;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.WeakHashMap;
/*      */ import java.util.concurrent.RejectedExecutionException;
/*      */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DefaultChannelPipeline
/*      */   implements ChannelPipeline
/*      */ {
/*   48 */   static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultChannelPipeline.class);
/*      */   
/*   50 */   private static final String HEAD_NAME = generateName0(HeadContext.class);
/*   51 */   private static final String TAIL_NAME = generateName0(TailContext.class);
/*      */   
/*   53 */   private static final FastThreadLocal<Map<Class<?>, String>> nameCaches = new FastThreadLocal<Map<Class<?>, String>>()
/*      */     {
/*      */       protected Map<Class<?>, String> initialValue() throws Exception
/*      */       {
/*   57 */         return new WeakHashMap<Class<?>, String>();
/*      */       }
/*      */     };
/*      */ 
/*      */   
/*   62 */   private static final AtomicReferenceFieldUpdater<DefaultChannelPipeline, MessageSizeEstimator.Handle> ESTIMATOR = AtomicReferenceFieldUpdater.newUpdater(DefaultChannelPipeline.class, MessageSizeEstimator.Handle.class, "estimatorHandle");
/*      */   
/*      */   final AbstractChannelHandlerContext head;
/*      */   
/*      */   final AbstractChannelHandlerContext tail;
/*      */   private final Channel channel;
/*      */   private final ChannelFuture succeededFuture;
/*      */   private final VoidChannelPromise voidPromise;
/*   70 */   private final boolean touch = ResourceLeakDetector.isEnabled();
/*      */ 
/*      */ 
/*      */   
/*      */   private Map<EventExecutorGroup, EventExecutor> childExecutors;
/*      */ 
/*      */ 
/*      */   
/*      */   private volatile MessageSizeEstimator.Handle estimatorHandle;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean firstRegistration = true;
/*      */ 
/*      */   
/*      */   private PendingHandlerCallback pendingHandlerCallbackHead;
/*      */ 
/*      */   
/*      */   private boolean registered;
/*      */ 
/*      */ 
/*      */   
/*      */   protected DefaultChannelPipeline(Channel channel) {
/*   93 */     this.channel = (Channel)ObjectUtil.checkNotNull(channel, "channel");
/*   94 */     this.succeededFuture = new SucceededChannelFuture(channel, null);
/*   95 */     this.voidPromise = new VoidChannelPromise(channel, true);
/*      */     
/*   97 */     this.tail = new TailContext(this);
/*   98 */     this.head = new HeadContext(this);
/*      */     
/*  100 */     this.head.next = this.tail;
/*  101 */     this.tail.prev = this.head;
/*      */   }
/*      */   
/*      */   final MessageSizeEstimator.Handle estimatorHandle() {
/*  105 */     MessageSizeEstimator.Handle handle = this.estimatorHandle;
/*  106 */     if (handle == null) {
/*  107 */       handle = this.channel.config().getMessageSizeEstimator().newHandle();
/*  108 */       if (!ESTIMATOR.compareAndSet(this, null, handle)) {
/*  109 */         handle = this.estimatorHandle;
/*      */       }
/*      */     } 
/*  112 */     return handle;
/*      */   }
/*      */   
/*      */   final Object touch(Object msg, AbstractChannelHandlerContext next) {
/*  116 */     return this.touch ? ReferenceCountUtil.touch(msg, next) : msg;
/*      */   }
/*      */   
/*      */   private AbstractChannelHandlerContext newContext(EventExecutorGroup group, String name, ChannelHandler handler) {
/*  120 */     return new DefaultChannelHandlerContext(this, childExecutor(group), name, handler);
/*      */   }
/*      */   
/*      */   private EventExecutor childExecutor(EventExecutorGroup group) {
/*  124 */     if (group == null) {
/*  125 */       return null;
/*      */     }
/*  127 */     Boolean pinEventExecutor = this.channel.config().<Boolean>getOption(ChannelOption.SINGLE_EVENTEXECUTOR_PER_GROUP);
/*  128 */     if (pinEventExecutor != null && !pinEventExecutor.booleanValue()) {
/*  129 */       return group.next();
/*      */     }
/*  131 */     Map<EventExecutorGroup, EventExecutor> childExecutors = this.childExecutors;
/*  132 */     if (childExecutors == null)
/*      */     {
/*  134 */       childExecutors = this.childExecutors = new IdentityHashMap<EventExecutorGroup, EventExecutor>(4);
/*      */     }
/*      */ 
/*      */     
/*  138 */     EventExecutor childExecutor = childExecutors.get(group);
/*  139 */     if (childExecutor == null) {
/*  140 */       childExecutor = group.next();
/*  141 */       childExecutors.put(group, childExecutor);
/*      */     } 
/*  143 */     return childExecutor;
/*      */   }
/*      */   
/*      */   public final Channel channel() {
/*  147 */     return this.channel;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline addFirst(String name, ChannelHandler handler) {
/*  152 */     return addFirst(null, name, handler);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline addFirst(EventExecutorGroup group, String name, ChannelHandler handler) {
/*      */     final AbstractChannelHandlerContext newCtx;
/*  158 */     synchronized (this) {
/*  159 */       checkMultiplicity(handler);
/*  160 */       name = filterName(name, handler);
/*      */       
/*  162 */       newCtx = newContext(group, name, handler);
/*      */       
/*  164 */       addFirst0(newCtx);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  169 */       if (!this.registered) {
/*  170 */         newCtx.setAddPending();
/*  171 */         callHandlerCallbackLater(newCtx, true);
/*  172 */         return this;
/*      */       } 
/*      */       
/*  175 */       EventExecutor executor = newCtx.executor();
/*  176 */       if (!executor.inEventLoop()) {
/*  177 */         newCtx.setAddPending();
/*  178 */         executor.execute(new Runnable()
/*      */             {
/*      */               public void run() {
/*  181 */                 DefaultChannelPipeline.this.callHandlerAdded0(newCtx);
/*      */               }
/*      */             });
/*  184 */         return this;
/*      */       } 
/*      */     } 
/*  187 */     callHandlerAdded0(newCtx);
/*  188 */     return this;
/*      */   }
/*      */   
/*      */   private void addFirst0(AbstractChannelHandlerContext newCtx) {
/*  192 */     AbstractChannelHandlerContext nextCtx = this.head.next;
/*  193 */     newCtx.prev = this.head;
/*  194 */     newCtx.next = nextCtx;
/*  195 */     this.head.next = newCtx;
/*  196 */     nextCtx.prev = newCtx;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline addLast(String name, ChannelHandler handler) {
/*  201 */     return addLast(null, name, handler);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline addLast(EventExecutorGroup group, String name, ChannelHandler handler) {
/*      */     final AbstractChannelHandlerContext newCtx;
/*  207 */     synchronized (this) {
/*  208 */       checkMultiplicity(handler);
/*      */       
/*  210 */       newCtx = newContext(group, filterName(name, handler), handler);
/*      */       
/*  212 */       addLast0(newCtx);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  217 */       if (!this.registered) {
/*  218 */         newCtx.setAddPending();
/*  219 */         callHandlerCallbackLater(newCtx, true);
/*  220 */         return this;
/*      */       } 
/*      */       
/*  223 */       EventExecutor executor = newCtx.executor();
/*  224 */       if (!executor.inEventLoop()) {
/*  225 */         newCtx.setAddPending();
/*  226 */         executor.execute(new Runnable()
/*      */             {
/*      */               public void run() {
/*  229 */                 DefaultChannelPipeline.this.callHandlerAdded0(newCtx);
/*      */               }
/*      */             });
/*  232 */         return this;
/*      */       } 
/*      */     } 
/*  235 */     callHandlerAdded0(newCtx);
/*  236 */     return this;
/*      */   }
/*      */   
/*      */   private void addLast0(AbstractChannelHandlerContext newCtx) {
/*  240 */     AbstractChannelHandlerContext prev = this.tail.prev;
/*  241 */     newCtx.prev = prev;
/*  242 */     newCtx.next = this.tail;
/*  243 */     prev.next = newCtx;
/*  244 */     this.tail.prev = newCtx;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline addBefore(String baseName, String name, ChannelHandler handler) {
/*  249 */     return addBefore(null, baseName, name, handler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ChannelPipeline addBefore(EventExecutorGroup group, String baseName, String name, ChannelHandler handler) {
/*      */     final AbstractChannelHandlerContext newCtx;
/*  257 */     synchronized (this) {
/*  258 */       checkMultiplicity(handler);
/*  259 */       name = filterName(name, handler);
/*  260 */       AbstractChannelHandlerContext ctx = getContextOrDie(baseName);
/*      */       
/*  262 */       newCtx = newContext(group, name, handler);
/*      */       
/*  264 */       addBefore0(ctx, newCtx);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  269 */       if (!this.registered) {
/*  270 */         newCtx.setAddPending();
/*  271 */         callHandlerCallbackLater(newCtx, true);
/*  272 */         return this;
/*      */       } 
/*      */       
/*  275 */       EventExecutor executor = newCtx.executor();
/*  276 */       if (!executor.inEventLoop()) {
/*  277 */         newCtx.setAddPending();
/*  278 */         executor.execute(new Runnable()
/*      */             {
/*      */               public void run() {
/*  281 */                 DefaultChannelPipeline.this.callHandlerAdded0(newCtx);
/*      */               }
/*      */             });
/*  284 */         return this;
/*      */       } 
/*      */     } 
/*  287 */     callHandlerAdded0(newCtx);
/*  288 */     return this;
/*      */   }
/*      */   
/*      */   private static void addBefore0(AbstractChannelHandlerContext ctx, AbstractChannelHandlerContext newCtx) {
/*  292 */     newCtx.prev = ctx.prev;
/*  293 */     newCtx.next = ctx;
/*  294 */     ctx.prev.next = newCtx;
/*  295 */     ctx.prev = newCtx;
/*      */   }
/*      */   
/*      */   private String filterName(String name, ChannelHandler handler) {
/*  299 */     if (name == null) {
/*  300 */       return generateName(handler);
/*      */     }
/*  302 */     checkDuplicateName(name);
/*  303 */     return name;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline addAfter(String baseName, String name, ChannelHandler handler) {
/*  308 */     return addAfter(null, baseName, name, handler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ChannelPipeline addAfter(EventExecutorGroup group, String baseName, String name, ChannelHandler handler) {
/*      */     final AbstractChannelHandlerContext newCtx;
/*  317 */     synchronized (this) {
/*  318 */       checkMultiplicity(handler);
/*  319 */       name = filterName(name, handler);
/*  320 */       AbstractChannelHandlerContext ctx = getContextOrDie(baseName);
/*      */       
/*  322 */       newCtx = newContext(group, name, handler);
/*      */       
/*  324 */       addAfter0(ctx, newCtx);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  329 */       if (!this.registered) {
/*  330 */         newCtx.setAddPending();
/*  331 */         callHandlerCallbackLater(newCtx, true);
/*  332 */         return this;
/*      */       } 
/*  334 */       EventExecutor executor = newCtx.executor();
/*  335 */       if (!executor.inEventLoop()) {
/*  336 */         newCtx.setAddPending();
/*  337 */         executor.execute(new Runnable()
/*      */             {
/*      */               public void run() {
/*  340 */                 DefaultChannelPipeline.this.callHandlerAdded0(newCtx);
/*      */               }
/*      */             });
/*  343 */         return this;
/*      */       } 
/*      */     } 
/*  346 */     callHandlerAdded0(newCtx);
/*  347 */     return this;
/*      */   }
/*      */   
/*      */   private static void addAfter0(AbstractChannelHandlerContext ctx, AbstractChannelHandlerContext newCtx) {
/*  351 */     newCtx.prev = ctx;
/*  352 */     newCtx.next = ctx.next;
/*  353 */     ctx.next.prev = newCtx;
/*  354 */     ctx.next = newCtx;
/*      */   }
/*      */   
/*      */   public final ChannelPipeline addFirst(ChannelHandler handler) {
/*  358 */     return addFirst((String)null, handler);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline addFirst(ChannelHandler... handlers) {
/*  363 */     return addFirst((EventExecutorGroup)null, handlers);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline addFirst(EventExecutorGroup executor, ChannelHandler... handlers) {
/*  368 */     if (handlers == null) {
/*  369 */       throw new NullPointerException("handlers");
/*      */     }
/*  371 */     if (handlers.length == 0 || handlers[0] == null) {
/*  372 */       return this;
/*      */     }
/*      */     
/*      */     int size;
/*  376 */     for (size = 1; size < handlers.length && 
/*  377 */       handlers[size] != null; size++);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  382 */     for (int i = size - 1; i >= 0; i--) {
/*  383 */       ChannelHandler h = handlers[i];
/*  384 */       addFirst(executor, null, h);
/*      */     } 
/*      */     
/*  387 */     return this;
/*      */   }
/*      */   
/*      */   public final ChannelPipeline addLast(ChannelHandler handler) {
/*  391 */     return addLast((String)null, handler);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline addLast(ChannelHandler... handlers) {
/*  396 */     return addLast((EventExecutorGroup)null, handlers);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline addLast(EventExecutorGroup executor, ChannelHandler... handlers) {
/*  401 */     if (handlers == null) {
/*  402 */       throw new NullPointerException("handlers");
/*      */     }
/*      */     
/*  405 */     for (ChannelHandler h : handlers) {
/*  406 */       if (h == null) {
/*      */         break;
/*      */       }
/*  409 */       addLast(executor, null, h);
/*      */     } 
/*      */     
/*  412 */     return this;
/*      */   }
/*      */   
/*      */   private String generateName(ChannelHandler handler) {
/*  416 */     Map<Class<?>, String> cache = (Map<Class<?>, String>)nameCaches.get();
/*  417 */     Class<?> handlerType = handler.getClass();
/*  418 */     String name = cache.get(handlerType);
/*  419 */     if (name == null) {
/*  420 */       name = generateName0(handlerType);
/*  421 */       cache.put(handlerType, name);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  426 */     if (context0(name) != null) {
/*  427 */       String baseName = name.substring(0, name.length() - 1);
/*  428 */       for (int i = 1;; i++) {
/*  429 */         String newName = baseName + i;
/*  430 */         if (context0(newName) == null) {
/*  431 */           name = newName;
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*  436 */     return name;
/*      */   }
/*      */   
/*      */   private static String generateName0(Class<?> handlerType) {
/*  440 */     return StringUtil.simpleClassName(handlerType) + "#0";
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline remove(ChannelHandler handler) {
/*  445 */     remove(getContextOrDie(handler));
/*  446 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelHandler remove(String name) {
/*  451 */     return remove(getContextOrDie(name)).handler();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final <T extends ChannelHandler> T remove(Class<T> handlerType) {
/*  457 */     return (T)remove(getContextOrDie(handlerType)).handler();
/*      */   }
/*      */   
/*      */   public final <T extends ChannelHandler> T removeIfExists(String name) {
/*  461 */     return removeIfExists(context(name));
/*      */   }
/*      */   
/*      */   public final <T extends ChannelHandler> T removeIfExists(Class<T> handlerType) {
/*  465 */     return removeIfExists(context(handlerType));
/*      */   }
/*      */   
/*      */   public final <T extends ChannelHandler> T removeIfExists(ChannelHandler handler) {
/*  469 */     return removeIfExists(context(handler));
/*      */   }
/*      */ 
/*      */   
/*      */   private <T extends ChannelHandler> T removeIfExists(ChannelHandlerContext ctx) {
/*  474 */     if (ctx == null) {
/*  475 */       return null;
/*      */     }
/*  477 */     return (T)remove((AbstractChannelHandlerContext)ctx).handler();
/*      */   }
/*      */   
/*      */   private AbstractChannelHandlerContext remove(final AbstractChannelHandlerContext ctx) {
/*  481 */     assert ctx != this.head && ctx != this.tail;
/*      */     
/*  483 */     synchronized (this) {
/*  484 */       remove0(ctx);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  489 */       if (!this.registered) {
/*  490 */         callHandlerCallbackLater(ctx, false);
/*  491 */         return ctx;
/*      */       } 
/*      */       
/*  494 */       EventExecutor executor = ctx.executor();
/*  495 */       if (!executor.inEventLoop()) {
/*  496 */         executor.execute(new Runnable()
/*      */             {
/*      */               public void run() {
/*  499 */                 DefaultChannelPipeline.this.callHandlerRemoved0(ctx);
/*      */               }
/*      */             });
/*  502 */         return ctx;
/*      */       } 
/*      */     } 
/*  505 */     callHandlerRemoved0(ctx);
/*  506 */     return ctx;
/*      */   }
/*      */   
/*      */   private static void remove0(AbstractChannelHandlerContext ctx) {
/*  510 */     AbstractChannelHandlerContext prev = ctx.prev;
/*  511 */     AbstractChannelHandlerContext next = ctx.next;
/*  512 */     prev.next = next;
/*  513 */     next.prev = prev;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelHandler removeFirst() {
/*  518 */     if (this.head.next == this.tail) {
/*  519 */       throw new NoSuchElementException();
/*      */     }
/*  521 */     return remove(this.head.next).handler();
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelHandler removeLast() {
/*  526 */     if (this.head.next == this.tail) {
/*  527 */       throw new NoSuchElementException();
/*      */     }
/*  529 */     return remove(this.tail.prev).handler();
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline replace(ChannelHandler oldHandler, String newName, ChannelHandler newHandler) {
/*  534 */     replace(getContextOrDie(oldHandler), newName, newHandler);
/*  535 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelHandler replace(String oldName, String newName, ChannelHandler newHandler) {
/*  540 */     return replace(getContextOrDie(oldName), newName, newHandler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final <T extends ChannelHandler> T replace(Class<T> oldHandlerType, String newName, ChannelHandler newHandler) {
/*  547 */     return (T)replace(getContextOrDie(oldHandlerType), newName, newHandler);
/*      */   }
/*      */   
/*      */   private ChannelHandler replace(final AbstractChannelHandlerContext ctx, String newName, ChannelHandler newHandler) {
/*      */     final AbstractChannelHandlerContext newCtx;
/*  552 */     assert ctx != this.head && ctx != this.tail;
/*      */ 
/*      */     
/*  555 */     synchronized (this) {
/*  556 */       checkMultiplicity(newHandler);
/*  557 */       if (newName == null) {
/*  558 */         newName = generateName(newHandler);
/*      */       } else {
/*  560 */         boolean sameName = ctx.name().equals(newName);
/*  561 */         if (!sameName) {
/*  562 */           checkDuplicateName(newName);
/*      */         }
/*      */       } 
/*      */       
/*  566 */       newCtx = newContext((EventExecutorGroup)ctx.executor, newName, newHandler);
/*      */       
/*  568 */       replace0(ctx, newCtx);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  574 */       if (!this.registered) {
/*  575 */         callHandlerCallbackLater(newCtx, true);
/*  576 */         callHandlerCallbackLater(ctx, false);
/*  577 */         return ctx.handler();
/*      */       } 
/*  579 */       EventExecutor executor = ctx.executor();
/*  580 */       if (!executor.inEventLoop()) {
/*  581 */         executor.execute(new Runnable()
/*      */             {
/*      */ 
/*      */               
/*      */               public void run()
/*      */               {
/*  587 */                 DefaultChannelPipeline.this.callHandlerAdded0(newCtx);
/*  588 */                 DefaultChannelPipeline.this.callHandlerRemoved0(ctx);
/*      */               }
/*      */             });
/*  591 */         return ctx.handler();
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  597 */     callHandlerAdded0(newCtx);
/*  598 */     callHandlerRemoved0(ctx);
/*  599 */     return ctx.handler();
/*      */   }
/*      */   
/*      */   private static void replace0(AbstractChannelHandlerContext oldCtx, AbstractChannelHandlerContext newCtx) {
/*  603 */     AbstractChannelHandlerContext prev = oldCtx.prev;
/*  604 */     AbstractChannelHandlerContext next = oldCtx.next;
/*  605 */     newCtx.prev = prev;
/*  606 */     newCtx.next = next;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  612 */     prev.next = newCtx;
/*  613 */     next.prev = newCtx;
/*      */ 
/*      */     
/*  616 */     oldCtx.prev = newCtx;
/*  617 */     oldCtx.next = newCtx;
/*      */   }
/*      */   
/*      */   private static void checkMultiplicity(ChannelHandler handler) {
/*  621 */     if (handler instanceof ChannelHandlerAdapter) {
/*  622 */       ChannelHandlerAdapter h = (ChannelHandlerAdapter)handler;
/*  623 */       if (!h.isSharable() && h.added) {
/*  624 */         throw new ChannelPipelineException(h
/*  625 */             .getClass().getName() + " is not a @Sharable handler, so can't be added or removed multiple times.");
/*      */       }
/*      */       
/*  628 */       h.added = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void callHandlerAdded0(AbstractChannelHandlerContext ctx) {
/*      */     try {
/*  636 */       ctx.setAddComplete();
/*  637 */       ctx.handler().handlerAdded(ctx);
/*  638 */     } catch (Throwable t) {
/*  639 */       boolean removed = false;
/*      */       try {
/*  641 */         remove0(ctx);
/*      */         try {
/*  643 */           ctx.handler().handlerRemoved(ctx);
/*      */         } finally {
/*  645 */           ctx.setRemoved();
/*      */         } 
/*  647 */         removed = true;
/*  648 */       } catch (Throwable t2) {
/*  649 */         if (logger.isWarnEnabled()) {
/*  650 */           logger.warn("Failed to remove a handler: " + ctx.name(), t2);
/*      */         }
/*      */       } 
/*      */       
/*  654 */       if (removed) {
/*  655 */         fireExceptionCaught(new ChannelPipelineException(ctx
/*  656 */               .handler().getClass().getName() + ".handlerAdded() has thrown an exception; removed.", t));
/*      */       } else {
/*      */         
/*  659 */         fireExceptionCaught(new ChannelPipelineException(ctx
/*  660 */               .handler().getClass().getName() + ".handlerAdded() has thrown an exception; also failed to remove.", t));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void callHandlerRemoved0(AbstractChannelHandlerContext ctx) {
/*      */     try {
/*      */       try {
/*  670 */         ctx.handler().handlerRemoved(ctx);
/*      */       } finally {
/*  672 */         ctx.setRemoved();
/*      */       } 
/*  674 */     } catch (Throwable t) {
/*  675 */       fireExceptionCaught(new ChannelPipelineException(ctx
/*  676 */             .handler().getClass().getName() + ".handlerRemoved() has thrown an exception.", t));
/*      */     } 
/*      */   }
/*      */   
/*      */   final void invokeHandlerAddedIfNeeded() {
/*  681 */     assert this.channel.eventLoop().inEventLoop();
/*  682 */     if (this.firstRegistration) {
/*  683 */       this.firstRegistration = false;
/*      */ 
/*      */       
/*  686 */       callHandlerAddedForAllHandlers();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelHandler first() {
/*  692 */     ChannelHandlerContext first = firstContext();
/*  693 */     if (first == null) {
/*  694 */       return null;
/*      */     }
/*  696 */     return first.handler();
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelHandlerContext firstContext() {
/*  701 */     AbstractChannelHandlerContext first = this.head.next;
/*  702 */     if (first == this.tail) {
/*  703 */       return null;
/*      */     }
/*  705 */     return this.head.next;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelHandler last() {
/*  710 */     AbstractChannelHandlerContext last = this.tail.prev;
/*  711 */     if (last == this.head) {
/*  712 */       return null;
/*      */     }
/*  714 */     return last.handler();
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelHandlerContext lastContext() {
/*  719 */     AbstractChannelHandlerContext last = this.tail.prev;
/*  720 */     if (last == this.head) {
/*  721 */       return null;
/*      */     }
/*  723 */     return last;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelHandler get(String name) {
/*  728 */     ChannelHandlerContext ctx = context(name);
/*  729 */     if (ctx == null) {
/*  730 */       return null;
/*      */     }
/*  732 */     return ctx.handler();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final <T extends ChannelHandler> T get(Class<T> handlerType) {
/*  739 */     ChannelHandlerContext ctx = context(handlerType);
/*  740 */     if (ctx == null) {
/*  741 */       return null;
/*      */     }
/*  743 */     return (T)ctx.handler();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ChannelHandlerContext context(String name) {
/*  749 */     if (name == null) {
/*  750 */       throw new NullPointerException("name");
/*      */     }
/*      */     
/*  753 */     return context0(name);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelHandlerContext context(ChannelHandler handler) {
/*  758 */     if (handler == null) {
/*  759 */       throw new NullPointerException("handler");
/*      */     }
/*      */     
/*  762 */     AbstractChannelHandlerContext ctx = this.head.next;
/*      */     
/*      */     while (true) {
/*  765 */       if (ctx == null) {
/*  766 */         return null;
/*      */       }
/*      */       
/*  769 */       if (ctx.handler() == handler) {
/*  770 */         return ctx;
/*      */       }
/*      */       
/*  773 */       ctx = ctx.next;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelHandlerContext context(Class<? extends ChannelHandler> handlerType) {
/*  779 */     if (handlerType == null) {
/*  780 */       throw new NullPointerException("handlerType");
/*      */     }
/*      */     
/*  783 */     AbstractChannelHandlerContext ctx = this.head.next;
/*      */     while (true) {
/*  785 */       if (ctx == null) {
/*  786 */         return null;
/*      */       }
/*  788 */       if (handlerType.isAssignableFrom(ctx.handler().getClass())) {
/*  789 */         return ctx;
/*      */       }
/*  791 */       ctx = ctx.next;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final List<String> names() {
/*  797 */     List<String> list = new ArrayList<String>();
/*  798 */     AbstractChannelHandlerContext ctx = this.head.next;
/*      */     while (true) {
/*  800 */       if (ctx == null) {
/*  801 */         return list;
/*      */       }
/*  803 */       list.add(ctx.name());
/*  804 */       ctx = ctx.next;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final Map<String, ChannelHandler> toMap() {
/*  810 */     Map<String, ChannelHandler> map = new LinkedHashMap<String, ChannelHandler>();
/*  811 */     AbstractChannelHandlerContext ctx = this.head.next;
/*      */     while (true) {
/*  813 */       if (ctx == this.tail) {
/*  814 */         return map;
/*      */       }
/*  816 */       map.put(ctx.name(), ctx.handler());
/*  817 */       ctx = ctx.next;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final Iterator<Map.Entry<String, ChannelHandler>> iterator() {
/*  823 */     return toMap().entrySet().iterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String toString() {
/*  833 */     StringBuilder buf = (new StringBuilder()).append(StringUtil.simpleClassName(this)).append('{');
/*  834 */     AbstractChannelHandlerContext ctx = this.head.next;
/*      */     
/*  836 */     while (ctx != this.tail) {
/*      */ 
/*      */ 
/*      */       
/*  840 */       buf.append('(')
/*  841 */         .append(ctx.name())
/*  842 */         .append(" = ")
/*  843 */         .append(ctx.handler().getClass().getName())
/*  844 */         .append(')');
/*      */       
/*  846 */       ctx = ctx.next;
/*  847 */       if (ctx == this.tail) {
/*      */         break;
/*      */       }
/*      */       
/*  851 */       buf.append(", ");
/*      */     } 
/*  853 */     buf.append('}');
/*  854 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline fireChannelRegistered() {
/*  859 */     AbstractChannelHandlerContext.invokeChannelRegistered(this.head);
/*  860 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline fireChannelUnregistered() {
/*  865 */     AbstractChannelHandlerContext.invokeChannelUnregistered(this.head);
/*  866 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private synchronized void destroy() {
/*  880 */     destroyUp(this.head.next, false);
/*      */   }
/*      */   
/*      */   private void destroyUp(AbstractChannelHandlerContext ctx, boolean inEventLoop) {
/*  884 */     Thread currentThread = Thread.currentThread();
/*  885 */     AbstractChannelHandlerContext tail = this.tail;
/*      */     while (true) {
/*  887 */       if (ctx == tail) {
/*  888 */         destroyDown(currentThread, tail.prev, inEventLoop);
/*      */         
/*      */         break;
/*      */       } 
/*  892 */       EventExecutor executor = ctx.executor();
/*  893 */       if (!inEventLoop && !executor.inEventLoop(currentThread)) {
/*  894 */         final AbstractChannelHandlerContext finalCtx = ctx;
/*  895 */         executor.execute(new Runnable()
/*      */             {
/*      */               public void run() {
/*  898 */                 DefaultChannelPipeline.this.destroyUp(finalCtx, true);
/*      */               }
/*      */             });
/*      */         
/*      */         break;
/*      */       } 
/*  904 */       ctx = ctx.next;
/*  905 */       inEventLoop = false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void destroyDown(Thread currentThread, AbstractChannelHandlerContext ctx, boolean inEventLoop) {
/*  911 */     AbstractChannelHandlerContext head = this.head;
/*      */     
/*  913 */     while (ctx != head) {
/*      */ 
/*      */ 
/*      */       
/*  917 */       EventExecutor executor = ctx.executor();
/*  918 */       if (inEventLoop || executor.inEventLoop(currentThread)) {
/*  919 */         synchronized (this) {
/*  920 */           remove0(ctx);
/*      */         } 
/*  922 */         callHandlerRemoved0(ctx);
/*      */       } else {
/*  924 */         final AbstractChannelHandlerContext finalCtx = ctx;
/*  925 */         executor.execute(new Runnable()
/*      */             {
/*      */               public void run() {
/*  928 */                 DefaultChannelPipeline.this.destroyDown(Thread.currentThread(), finalCtx, true);
/*      */               }
/*      */             });
/*      */         
/*      */         break;
/*      */       } 
/*  934 */       ctx = ctx.prev;
/*  935 */       inEventLoop = false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline fireChannelActive() {
/*  941 */     AbstractChannelHandlerContext.invokeChannelActive(this.head);
/*  942 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline fireChannelInactive() {
/*  947 */     AbstractChannelHandlerContext.invokeChannelInactive(this.head);
/*  948 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline fireExceptionCaught(Throwable cause) {
/*  953 */     AbstractChannelHandlerContext.invokeExceptionCaught(this.head, cause);
/*  954 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline fireUserEventTriggered(Object event) {
/*  959 */     AbstractChannelHandlerContext.invokeUserEventTriggered(this.head, event);
/*  960 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline fireChannelRead(Object msg) {
/*  965 */     AbstractChannelHandlerContext.invokeChannelRead(this.head, msg);
/*  966 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline fireChannelReadComplete() {
/*  971 */     AbstractChannelHandlerContext.invokeChannelReadComplete(this.head);
/*  972 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline fireChannelWritabilityChanged() {
/*  977 */     AbstractChannelHandlerContext.invokeChannelWritabilityChanged(this.head);
/*  978 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelFuture bind(SocketAddress localAddress) {
/*  983 */     return this.tail.bind(localAddress);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelFuture connect(SocketAddress remoteAddress) {
/*  988 */     return this.tail.connect(remoteAddress);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
/*  993 */     return this.tail.connect(remoteAddress, localAddress);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelFuture disconnect() {
/*  998 */     return this.tail.disconnect();
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelFuture close() {
/* 1003 */     return this.tail.close();
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelFuture deregister() {
/* 1008 */     return this.tail.deregister();
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline flush() {
/* 1013 */     this.tail.flush();
/* 1014 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
/* 1019 */     return this.tail.bind(localAddress, promise);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
/* 1024 */     return this.tail.connect(remoteAddress, promise);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
/* 1030 */     return this.tail.connect(remoteAddress, localAddress, promise);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelFuture disconnect(ChannelPromise promise) {
/* 1035 */     return this.tail.disconnect(promise);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelFuture close(ChannelPromise promise) {
/* 1040 */     return this.tail.close(promise);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelFuture deregister(ChannelPromise promise) {
/* 1045 */     return this.tail.deregister(promise);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPipeline read() {
/* 1050 */     this.tail.read();
/* 1051 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelFuture write(Object msg) {
/* 1056 */     return this.tail.write(msg);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelFuture write(Object msg, ChannelPromise promise) {
/* 1061 */     return this.tail.write(msg, promise);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
/* 1066 */     return this.tail.writeAndFlush(msg, promise);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelFuture writeAndFlush(Object msg) {
/* 1071 */     return this.tail.writeAndFlush(msg);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPromise newPromise() {
/* 1076 */     return new DefaultChannelPromise(this.channel);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelProgressivePromise newProgressivePromise() {
/* 1081 */     return new DefaultChannelProgressivePromise(this.channel);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelFuture newSucceededFuture() {
/* 1086 */     return this.succeededFuture;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelFuture newFailedFuture(Throwable cause) {
/* 1091 */     return new FailedChannelFuture(this.channel, null, cause);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ChannelPromise voidPromise() {
/* 1096 */     return this.voidPromise;
/*      */   }
/*      */   
/*      */   private void checkDuplicateName(String name) {
/* 1100 */     if (context0(name) != null) {
/* 1101 */       throw new IllegalArgumentException("Duplicate handler name: " + name);
/*      */     }
/*      */   }
/*      */   
/*      */   private AbstractChannelHandlerContext context0(String name) {
/* 1106 */     AbstractChannelHandlerContext context = this.head.next;
/* 1107 */     while (context != this.tail) {
/* 1108 */       if (context.name().equals(name)) {
/* 1109 */         return context;
/*      */       }
/* 1111 */       context = context.next;
/*      */     } 
/* 1113 */     return null;
/*      */   }
/*      */   
/*      */   private AbstractChannelHandlerContext getContextOrDie(String name) {
/* 1117 */     AbstractChannelHandlerContext ctx = (AbstractChannelHandlerContext)context(name);
/* 1118 */     if (ctx == null) {
/* 1119 */       throw new NoSuchElementException(name);
/*      */     }
/* 1121 */     return ctx;
/*      */   }
/*      */ 
/*      */   
/*      */   private AbstractChannelHandlerContext getContextOrDie(ChannelHandler handler) {
/* 1126 */     AbstractChannelHandlerContext ctx = (AbstractChannelHandlerContext)context(handler);
/* 1127 */     if (ctx == null) {
/* 1128 */       throw new NoSuchElementException(handler.getClass().getName());
/*      */     }
/* 1130 */     return ctx;
/*      */   }
/*      */ 
/*      */   
/*      */   private AbstractChannelHandlerContext getContextOrDie(Class<? extends ChannelHandler> handlerType) {
/* 1135 */     AbstractChannelHandlerContext ctx = (AbstractChannelHandlerContext)context(handlerType);
/* 1136 */     if (ctx == null) {
/* 1137 */       throw new NoSuchElementException(handlerType.getName());
/*      */     }
/* 1139 */     return ctx;
/*      */   }
/*      */ 
/*      */   
/*      */   private void callHandlerAddedForAllHandlers() {
/*      */     PendingHandlerCallback pendingHandlerCallbackHead;
/* 1145 */     synchronized (this) {
/* 1146 */       assert !this.registered;
/*      */ 
/*      */       
/* 1149 */       this.registered = true;
/*      */       
/* 1151 */       pendingHandlerCallbackHead = this.pendingHandlerCallbackHead;
/*      */       
/* 1153 */       this.pendingHandlerCallbackHead = null;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1159 */     PendingHandlerCallback task = pendingHandlerCallbackHead;
/* 1160 */     while (task != null) {
/* 1161 */       task.execute();
/* 1162 */       task = task.next;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void callHandlerCallbackLater(AbstractChannelHandlerContext ctx, boolean added) {
/* 1167 */     assert !this.registered;
/*      */     
/* 1169 */     PendingHandlerCallback task = added ? new PendingHandlerAddedTask(ctx) : new PendingHandlerRemovedTask(ctx);
/* 1170 */     PendingHandlerCallback pending = this.pendingHandlerCallbackHead;
/* 1171 */     if (pending == null) {
/* 1172 */       this.pendingHandlerCallbackHead = task;
/*      */     } else {
/*      */       
/* 1175 */       while (pending.next != null) {
/* 1176 */         pending = pending.next;
/*      */       }
/* 1178 */       pending.next = task;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onUnhandledInboundException(Throwable cause) {
/*      */     try {
/* 1188 */       logger.warn("An exceptionCaught() event was fired, and it reached at the tail of the pipeline. It usually means the last handler in the pipeline did not handle the exception.", cause);
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/* 1193 */       ReferenceCountUtil.release(cause);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onUnhandledInboundChannelActive() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onUnhandledInboundChannelInactive() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onUnhandledInboundMessage(Object msg) {
/*      */     try {
/* 1218 */       logger.debug("Discarded inbound message {} that reached at the tail of the pipeline. Please check your pipeline configuration.", msg);
/*      */     }
/*      */     finally {
/*      */       
/* 1222 */       ReferenceCountUtil.release(msg);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onUnhandledInboundChannelReadComplete() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onUnhandledInboundUserEventTriggered(Object evt) {
/* 1241 */     ReferenceCountUtil.release(evt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onUnhandledChannelWritabilityChanged() {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void incrementPendingOutboundBytes(long size) {
/* 1253 */     ChannelOutboundBuffer buffer = this.channel.unsafe().outboundBuffer();
/* 1254 */     if (buffer != null) {
/* 1255 */       buffer.incrementPendingOutboundBytes(size);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void decrementPendingOutboundBytes(long size) {
/* 1261 */     ChannelOutboundBuffer buffer = this.channel.unsafe().outboundBuffer();
/* 1262 */     if (buffer != null)
/* 1263 */       buffer.decrementPendingOutboundBytes(size); 
/*      */   }
/*      */   
/*      */   final class TailContext
/*      */     extends AbstractChannelHandlerContext
/*      */     implements ChannelInboundHandler
/*      */   {
/*      */     TailContext(DefaultChannelPipeline pipeline) {
/* 1271 */       super(pipeline, (EventExecutor)null, DefaultChannelPipeline.TAIL_NAME, true, false);
/* 1272 */       setAddComplete();
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelHandler handler() {
/* 1277 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public void channelRegistered(ChannelHandlerContext ctx) throws Exception {}
/*      */ 
/*      */     
/*      */     public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {}
/*      */ 
/*      */     
/*      */     public void channelActive(ChannelHandlerContext ctx) throws Exception {
/* 1288 */       DefaultChannelPipeline.this.onUnhandledInboundChannelActive();
/*      */     }
/*      */ 
/*      */     
/*      */     public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/* 1293 */       DefaultChannelPipeline.this.onUnhandledInboundChannelInactive();
/*      */     }
/*      */ 
/*      */     
/*      */     public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
/* 1298 */       DefaultChannelPipeline.this.onUnhandledChannelWritabilityChanged();
/*      */     }
/*      */ 
/*      */     
/*      */     public void handlerAdded(ChannelHandlerContext ctx) throws Exception {}
/*      */ 
/*      */     
/*      */     public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {}
/*      */ 
/*      */     
/*      */     public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
/* 1309 */       DefaultChannelPipeline.this.onUnhandledInboundUserEventTriggered(evt);
/*      */     }
/*      */ 
/*      */     
/*      */     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/* 1314 */       DefaultChannelPipeline.this.onUnhandledInboundException(cause);
/*      */     }
/*      */ 
/*      */     
/*      */     public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/* 1319 */       DefaultChannelPipeline.this.onUnhandledInboundMessage(msg);
/*      */     }
/*      */ 
/*      */     
/*      */     public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
/* 1324 */       DefaultChannelPipeline.this.onUnhandledInboundChannelReadComplete();
/*      */     }
/*      */   }
/*      */   
/*      */   final class HeadContext
/*      */     extends AbstractChannelHandlerContext
/*      */     implements ChannelOutboundHandler, ChannelInboundHandler {
/*      */     private final Channel.Unsafe unsafe;
/*      */     
/*      */     HeadContext(DefaultChannelPipeline pipeline) {
/* 1334 */       super(pipeline, (EventExecutor)null, DefaultChannelPipeline.HEAD_NAME, false, true);
/* 1335 */       this.unsafe = pipeline.channel().unsafe();
/* 1336 */       setAddComplete();
/*      */     }
/*      */ 
/*      */     
/*      */     public ChannelHandler handler() {
/* 1341 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void handlerAdded(ChannelHandlerContext ctx) throws Exception {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
/* 1358 */       this.unsafe.bind(localAddress, promise);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
/* 1366 */       this.unsafe.connect(remoteAddress, localAddress, promise);
/*      */     }
/*      */ 
/*      */     
/*      */     public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
/* 1371 */       this.unsafe.disconnect(promise);
/*      */     }
/*      */ 
/*      */     
/*      */     public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
/* 1376 */       this.unsafe.close(promise);
/*      */     }
/*      */ 
/*      */     
/*      */     public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
/* 1381 */       this.unsafe.deregister(promise);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(ChannelHandlerContext ctx) {
/* 1386 */       this.unsafe.beginRead();
/*      */     }
/*      */ 
/*      */     
/*      */     public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/* 1391 */       this.unsafe.write(msg, promise);
/*      */     }
/*      */ 
/*      */     
/*      */     public void flush(ChannelHandlerContext ctx) throws Exception {
/* 1396 */       this.unsafe.flush();
/*      */     }
/*      */ 
/*      */     
/*      */     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/* 1401 */       ctx.fireExceptionCaught(cause);
/*      */     }
/*      */ 
/*      */     
/*      */     public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
/* 1406 */       DefaultChannelPipeline.this.invokeHandlerAddedIfNeeded();
/* 1407 */       ctx.fireChannelRegistered();
/*      */     }
/*      */ 
/*      */     
/*      */     public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
/* 1412 */       ctx.fireChannelUnregistered();
/*      */ 
/*      */       
/* 1415 */       if (!DefaultChannelPipeline.this.channel.isOpen()) {
/* 1416 */         DefaultChannelPipeline.this.destroy();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public void channelActive(ChannelHandlerContext ctx) throws Exception {
/* 1422 */       ctx.fireChannelActive();
/*      */       
/* 1424 */       readIfIsAutoRead();
/*      */     }
/*      */ 
/*      */     
/*      */     public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/* 1429 */       ctx.fireChannelInactive();
/*      */     }
/*      */ 
/*      */     
/*      */     public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/* 1434 */       ctx.fireChannelRead(msg);
/*      */     }
/*      */ 
/*      */     
/*      */     public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
/* 1439 */       ctx.fireChannelReadComplete();
/*      */       
/* 1441 */       readIfIsAutoRead();
/*      */     }
/*      */     
/*      */     private void readIfIsAutoRead() {
/* 1445 */       if (DefaultChannelPipeline.this.channel.config().isAutoRead()) {
/* 1446 */         DefaultChannelPipeline.this.channel.read();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
/* 1452 */       ctx.fireUserEventTriggered(evt);
/*      */     }
/*      */ 
/*      */     
/*      */     public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
/* 1457 */       ctx.fireChannelWritabilityChanged();
/*      */     }
/*      */   }
/*      */   
/*      */   private static abstract class PendingHandlerCallback implements Runnable {
/*      */     final AbstractChannelHandlerContext ctx;
/*      */     PendingHandlerCallback next;
/*      */     
/*      */     PendingHandlerCallback(AbstractChannelHandlerContext ctx) {
/* 1466 */       this.ctx = ctx;
/*      */     }
/*      */     
/*      */     abstract void execute();
/*      */   }
/*      */   
/*      */   private final class PendingHandlerAddedTask
/*      */     extends PendingHandlerCallback {
/*      */     PendingHandlerAddedTask(AbstractChannelHandlerContext ctx) {
/* 1475 */       super(ctx);
/*      */     }
/*      */ 
/*      */     
/*      */     public void run() {
/* 1480 */       DefaultChannelPipeline.this.callHandlerAdded0(this.ctx);
/*      */     }
/*      */ 
/*      */     
/*      */     void execute() {
/* 1485 */       EventExecutor executor = this.ctx.executor();
/* 1486 */       if (executor.inEventLoop()) {
/* 1487 */         DefaultChannelPipeline.this.callHandlerAdded0(this.ctx);
/*      */       } else {
/*      */         try {
/* 1490 */           executor.execute(this);
/* 1491 */         } catch (RejectedExecutionException e) {
/* 1492 */           if (DefaultChannelPipeline.logger.isWarnEnabled()) {
/* 1493 */             DefaultChannelPipeline.logger.warn("Can't invoke handlerAdded() as the EventExecutor {} rejected it, removing handler {}.", new Object[] { executor, this.ctx
/*      */                   
/* 1495 */                   .name(), e });
/*      */           }
/* 1497 */           DefaultChannelPipeline.remove0(this.ctx);
/* 1498 */           this.ctx.setRemoved();
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private final class PendingHandlerRemovedTask
/*      */     extends PendingHandlerCallback {
/*      */     PendingHandlerRemovedTask(AbstractChannelHandlerContext ctx) {
/* 1507 */       super(ctx);
/*      */     }
/*      */ 
/*      */     
/*      */     public void run() {
/* 1512 */       DefaultChannelPipeline.this.callHandlerRemoved0(this.ctx);
/*      */     }
/*      */ 
/*      */     
/*      */     void execute() {
/* 1517 */       EventExecutor executor = this.ctx.executor();
/* 1518 */       if (executor.inEventLoop()) {
/* 1519 */         DefaultChannelPipeline.this.callHandlerRemoved0(this.ctx);
/*      */       } else {
/*      */         try {
/* 1522 */           executor.execute(this);
/* 1523 */         } catch (RejectedExecutionException e) {
/* 1524 */           if (DefaultChannelPipeline.logger.isWarnEnabled()) {
/* 1525 */             DefaultChannelPipeline.logger.warn("Can't invoke handlerRemoved() as the EventExecutor {} rejected it, removing handler {}.", new Object[] { executor, this.ctx
/*      */                   
/* 1527 */                   .name(), e });
/*      */           }
/*      */           
/* 1530 */           this.ctx.setRemoved();
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\DefaultChannelPipeline.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */