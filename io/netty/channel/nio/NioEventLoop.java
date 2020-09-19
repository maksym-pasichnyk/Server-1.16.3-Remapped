/*     */ package io.netty.channel.nio;
/*     */ 
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.EventLoopException;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.SelectStrategy;
/*     */ import io.netty.channel.SingleThreadEventLoop;
/*     */ import io.netty.util.IntSupplier;
/*     */ import io.netty.util.concurrent.RejectedExecutionHandler;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.ReflectionUtil;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.nio.channels.CancelledKeyException;
/*     */ import java.nio.channels.SelectableChannel;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ public final class NioEventLoop
/*     */   extends SingleThreadEventLoop
/*     */ {
/*  58 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NioEventLoop.class);
/*     */ 
/*     */   
/*     */   private static final int CLEANUP_INTERVAL = 256;
/*     */   
/*  63 */   private static final boolean DISABLE_KEYSET_OPTIMIZATION = SystemPropertyUtil.getBoolean("io.netty.noKeySetOptimization", false);
/*     */   
/*     */   private static final int MIN_PREMATURE_SELECTOR_RETURNS = 3;
/*     */   private static final int SELECTOR_AUTO_REBUILD_THRESHOLD;
/*     */   
/*  68 */   private final IntSupplier selectNowSupplier = new IntSupplier()
/*     */     {
/*     */       public int get() throws Exception {
/*  71 */         return NioEventLoop.this.selectNow();
/*     */       }
/*     */     };
/*  74 */   private final Callable<Integer> pendingTasksCallable = new Callable<Integer>()
/*     */     {
/*     */       public Integer call() throws Exception {
/*  77 */         return Integer.valueOf(NioEventLoop.this.pendingTasks());
/*     */       }
/*     */     };
/*     */   static final long MAX_SCHEDULED_DAYS = 1095L;
/*     */   private Selector selector;
/*     */   private Selector unwrappedSelector;
/*     */   private SelectedSelectionKeySet selectedKeys;
/*     */   private final SelectorProvider provider;
/*     */   
/*     */   static {
/*  87 */     String key = "sun.nio.ch.bugLevel";
/*  88 */     String buglevel = SystemPropertyUtil.get("sun.nio.ch.bugLevel");
/*  89 */     if (buglevel == null) {
/*     */       try {
/*  91 */         AccessController.doPrivileged(new PrivilegedAction<Void>()
/*     */             {
/*     */               public Void run() {
/*  94 */                 System.setProperty("sun.nio.ch.bugLevel", "");
/*  95 */                 return null;
/*     */               }
/*     */             });
/*  98 */       } catch (SecurityException e) {
/*  99 */         logger.debug("Unable to get/set System Property: sun.nio.ch.bugLevel", e);
/*     */       } 
/*     */     }
/*     */     
/* 103 */     int selectorAutoRebuildThreshold = SystemPropertyUtil.getInt("io.netty.selectorAutoRebuildThreshold", 512);
/* 104 */     if (selectorAutoRebuildThreshold < 3) {
/* 105 */       selectorAutoRebuildThreshold = 0;
/*     */     }
/*     */     
/* 108 */     SELECTOR_AUTO_REBUILD_THRESHOLD = selectorAutoRebuildThreshold;
/*     */     
/* 110 */     if (logger.isDebugEnabled()) {
/* 111 */       logger.debug("-Dio.netty.noKeySetOptimization: {}", Boolean.valueOf(DISABLE_KEYSET_OPTIMIZATION));
/* 112 */       logger.debug("-Dio.netty.selectorAutoRebuildThreshold: {}", Integer.valueOf(SELECTOR_AUTO_REBUILD_THRESHOLD));
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
/* 133 */   private final AtomicBoolean wakenUp = new AtomicBoolean();
/*     */   
/*     */   private final SelectStrategy selectStrategy;
/*     */   
/* 137 */   private volatile int ioRatio = 50;
/*     */   
/*     */   private int cancelledKeys;
/*     */   private boolean needsToSelectAgain;
/*     */   
/*     */   NioEventLoop(NioEventLoopGroup parent, Executor executor, SelectorProvider selectorProvider, SelectStrategy strategy, RejectedExecutionHandler rejectedExecutionHandler) {
/* 143 */     super((EventLoopGroup)parent, executor, false, DEFAULT_MAX_PENDING_TASKS, rejectedExecutionHandler);
/* 144 */     if (selectorProvider == null) {
/* 145 */       throw new NullPointerException("selectorProvider");
/*     */     }
/* 147 */     if (strategy == null) {
/* 148 */       throw new NullPointerException("selectStrategy");
/*     */     }
/* 150 */     this.provider = selectorProvider;
/* 151 */     SelectorTuple selectorTuple = openSelector();
/* 152 */     this.selector = selectorTuple.selector;
/* 153 */     this.unwrappedSelector = selectorTuple.unwrappedSelector;
/* 154 */     this.selectStrategy = strategy;
/*     */   }
/*     */   
/*     */   private static final class SelectorTuple {
/*     */     final Selector unwrappedSelector;
/*     */     final Selector selector;
/*     */     
/*     */     SelectorTuple(Selector unwrappedSelector) {
/* 162 */       this.unwrappedSelector = unwrappedSelector;
/* 163 */       this.selector = unwrappedSelector;
/*     */     }
/*     */     
/*     */     SelectorTuple(Selector unwrappedSelector, Selector selector) {
/* 167 */       this.unwrappedSelector = unwrappedSelector;
/* 168 */       this.selector = selector;
/*     */     }
/*     */   }
/*     */   
/*     */   private SelectorTuple openSelector() {
/*     */     final Selector unwrappedSelector;
/*     */     try {
/* 175 */       unwrappedSelector = this.provider.openSelector();
/* 176 */     } catch (IOException e) {
/* 177 */       throw new ChannelException("failed to open a new selector", e);
/*     */     } 
/*     */     
/* 180 */     if (DISABLE_KEYSET_OPTIMIZATION) {
/* 181 */       return new SelectorTuple(unwrappedSelector);
/*     */     }
/*     */     
/* 184 */     final SelectedSelectionKeySet selectedKeySet = new SelectedSelectionKeySet();
/*     */     
/* 186 */     Object maybeSelectorImplClass = AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Object run() {
/*     */             try {
/* 190 */               return Class.forName("sun.nio.ch.SelectorImpl", false, 
/*     */ 
/*     */                   
/* 193 */                   PlatformDependent.getSystemClassLoader());
/* 194 */             } catch (Throwable cause) {
/* 195 */               return cause;
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 200 */     if (!(maybeSelectorImplClass instanceof Class) || 
/*     */       
/* 202 */       !((Class)maybeSelectorImplClass).isAssignableFrom(unwrappedSelector.getClass())) {
/* 203 */       if (maybeSelectorImplClass instanceof Throwable) {
/* 204 */         Throwable t = (Throwable)maybeSelectorImplClass;
/* 205 */         logger.trace("failed to instrument a special java.util.Set into: {}", unwrappedSelector, t);
/*     */       } 
/* 207 */       return new SelectorTuple(unwrappedSelector);
/*     */     } 
/*     */     
/* 210 */     final Class<?> selectorImplClass = (Class)maybeSelectorImplClass;
/*     */     
/* 212 */     Object maybeException = AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Object run() {
/*     */             try {
/* 216 */               Field selectedKeysField = selectorImplClass.getDeclaredField("selectedKeys");
/* 217 */               Field publicSelectedKeysField = selectorImplClass.getDeclaredField("publicSelectedKeys");
/*     */               
/* 219 */               Throwable cause = ReflectionUtil.trySetAccessible(selectedKeysField, true);
/* 220 */               if (cause != null) {
/* 221 */                 return cause;
/*     */               }
/* 223 */               cause = ReflectionUtil.trySetAccessible(publicSelectedKeysField, true);
/* 224 */               if (cause != null) {
/* 225 */                 return cause;
/*     */               }
/*     */               
/* 228 */               selectedKeysField.set(unwrappedSelector, selectedKeySet);
/* 229 */               publicSelectedKeysField.set(unwrappedSelector, selectedKeySet);
/* 230 */               return null;
/* 231 */             } catch (NoSuchFieldException e) {
/* 232 */               return e;
/* 233 */             } catch (IllegalAccessException e) {
/* 234 */               return e;
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 239 */     if (maybeException instanceof Exception) {
/* 240 */       this.selectedKeys = null;
/* 241 */       Exception e = (Exception)maybeException;
/* 242 */       logger.trace("failed to instrument a special java.util.Set into: {}", unwrappedSelector, e);
/* 243 */       return new SelectorTuple(unwrappedSelector);
/*     */     } 
/* 245 */     this.selectedKeys = selectedKeySet;
/* 246 */     logger.trace("instrumented a special java.util.Set into: {}", unwrappedSelector);
/* 247 */     return new SelectorTuple(unwrappedSelector, new SelectedSelectionKeySetSelector(unwrappedSelector, selectedKeySet));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SelectorProvider selectorProvider() {
/* 255 */     return this.provider;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Queue<Runnable> newTaskQueue(int maxPendingTasks) {
/* 261 */     return (maxPendingTasks == Integer.MAX_VALUE) ? PlatformDependent.newMpscQueue() : 
/* 262 */       PlatformDependent.newMpscQueue(maxPendingTasks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int pendingTasks() {
/* 270 */     if (inEventLoop()) {
/* 271 */       return super.pendingTasks();
/*     */     }
/* 273 */     return ((Integer)submit(this.pendingTasksCallable).syncUninterruptibly().getNow()).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void register(SelectableChannel ch, int interestOps, NioTask<?> task) {
/* 283 */     if (ch == null) {
/* 284 */       throw new NullPointerException("ch");
/*     */     }
/* 286 */     if (interestOps == 0) {
/* 287 */       throw new IllegalArgumentException("interestOps must be non-zero.");
/*     */     }
/* 289 */     if ((interestOps & (ch.validOps() ^ 0xFFFFFFFF)) != 0) {
/* 290 */       throw new IllegalArgumentException("invalid interestOps: " + interestOps + "(validOps: " + ch
/* 291 */           .validOps() + ')');
/*     */     }
/* 293 */     if (task == null) {
/* 294 */       throw new NullPointerException("task");
/*     */     }
/*     */     
/* 297 */     if (isShutdown()) {
/* 298 */       throw new IllegalStateException("event loop shut down");
/*     */     }
/*     */     
/*     */     try {
/* 302 */       ch.register(this.selector, interestOps, task);
/* 303 */     } catch (Exception e) {
/* 304 */       throw new EventLoopException("failed to register a channel", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIoRatio() {
/* 312 */     return this.ioRatio;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIoRatio(int ioRatio) {
/* 320 */     if (ioRatio <= 0 || ioRatio > 100) {
/* 321 */       throw new IllegalArgumentException("ioRatio: " + ioRatio + " (expected: 0 < ioRatio <= 100)");
/*     */     }
/* 323 */     this.ioRatio = ioRatio;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rebuildSelector() {
/* 331 */     if (!inEventLoop()) {
/* 332 */       execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 335 */               NioEventLoop.this.rebuildSelector0();
/*     */             }
/*     */           });
/*     */       return;
/*     */     } 
/* 340 */     rebuildSelector0();
/*     */   }
/*     */   private void rebuildSelector0() {
/*     */     SelectorTuple newSelectorTuple;
/* 344 */     Selector oldSelector = this.selector;
/*     */ 
/*     */     
/* 347 */     if (oldSelector == null) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/* 352 */       newSelectorTuple = openSelector();
/* 353 */     } catch (Exception e) {
/* 354 */       logger.warn("Failed to create a new Selector.", e);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 359 */     int nChannels = 0;
/* 360 */     for (SelectionKey key : oldSelector.keys()) {
/* 361 */       Object a = key.attachment();
/*     */       try {
/* 363 */         if (!key.isValid() || key.channel().keyFor(newSelectorTuple.unwrappedSelector) != null) {
/*     */           continue;
/*     */         }
/*     */         
/* 367 */         int interestOps = key.interestOps();
/* 368 */         key.cancel();
/* 369 */         SelectionKey newKey = key.channel().register(newSelectorTuple.unwrappedSelector, interestOps, a);
/* 370 */         if (a instanceof AbstractNioChannel)
/*     */         {
/* 372 */           ((AbstractNioChannel)a).selectionKey = newKey;
/*     */         }
/* 374 */         nChannels++;
/* 375 */       } catch (Exception e) {
/* 376 */         logger.warn("Failed to re-register a Channel to the new Selector.", e);
/* 377 */         if (a instanceof AbstractNioChannel) {
/* 378 */           AbstractNioChannel ch = (AbstractNioChannel)a;
/* 379 */           ch.unsafe().close(ch.unsafe().voidPromise());
/*     */           continue;
/*     */         } 
/* 382 */         NioTask<SelectableChannel> task = (NioTask<SelectableChannel>)a;
/* 383 */         invokeChannelUnregistered(task, key, e);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 388 */     this.selector = newSelectorTuple.selector;
/* 389 */     this.unwrappedSelector = newSelectorTuple.unwrappedSelector;
/*     */ 
/*     */     
/*     */     try {
/* 393 */       oldSelector.close();
/* 394 */     } catch (Throwable t) {
/* 395 */       if (logger.isWarnEnabled()) {
/* 396 */         logger.warn("Failed to close the old Selector.", t);
/*     */       }
/*     */     } 
/*     */     
/* 400 */     logger.info("Migrated " + nChannels + " channel(s) to the new Selector.");
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
/*     */   protected void run() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield selectStrategy : Lio/netty/channel/SelectStrategy;
/*     */     //   4: aload_0
/*     */     //   5: getfield selectNowSupplier : Lio/netty/util/IntSupplier;
/*     */     //   8: aload_0
/*     */     //   9: invokevirtual hasTasks : ()Z
/*     */     //   12: invokeinterface calculateStrategy : (Lio/netty/util/IntSupplier;Z)I
/*     */     //   17: lookupswitch default -> 77, -2 -> 44, -1 -> 47
/*     */     //   44: goto -> 0
/*     */     //   47: aload_0
/*     */     //   48: aload_0
/*     */     //   49: getfield wakenUp : Ljava/util/concurrent/atomic/AtomicBoolean;
/*     */     //   52: iconst_0
/*     */     //   53: invokevirtual getAndSet : (Z)Z
/*     */     //   56: invokespecial select : (Z)V
/*     */     //   59: aload_0
/*     */     //   60: getfield wakenUp : Ljava/util/concurrent/atomic/AtomicBoolean;
/*     */     //   63: invokevirtual get : ()Z
/*     */     //   66: ifeq -> 77
/*     */     //   69: aload_0
/*     */     //   70: getfield selector : Ljava/nio/channels/Selector;
/*     */     //   73: invokevirtual wakeup : ()Ljava/nio/channels/Selector;
/*     */     //   76: pop
/*     */     //   77: aload_0
/*     */     //   78: iconst_0
/*     */     //   79: putfield cancelledKeys : I
/*     */     //   82: aload_0
/*     */     //   83: iconst_0
/*     */     //   84: putfield needsToSelectAgain : Z
/*     */     //   87: aload_0
/*     */     //   88: getfield ioRatio : I
/*     */     //   91: istore_1
/*     */     //   92: iload_1
/*     */     //   93: bipush #100
/*     */     //   95: if_icmpne -> 121
/*     */     //   98: aload_0
/*     */     //   99: invokespecial processSelectedKeys : ()V
/*     */     //   102: aload_0
/*     */     //   103: invokevirtual runAllTasks : ()Z
/*     */     //   106: pop
/*     */     //   107: goto -> 118
/*     */     //   110: astore_2
/*     */     //   111: aload_0
/*     */     //   112: invokevirtual runAllTasks : ()Z
/*     */     //   115: pop
/*     */     //   116: aload_2
/*     */     //   117: athrow
/*     */     //   118: goto -> 183
/*     */     //   121: invokestatic nanoTime : ()J
/*     */     //   124: lstore_2
/*     */     //   125: aload_0
/*     */     //   126: invokespecial processSelectedKeys : ()V
/*     */     //   129: invokestatic nanoTime : ()J
/*     */     //   132: lload_2
/*     */     //   133: lsub
/*     */     //   134: lstore #4
/*     */     //   136: aload_0
/*     */     //   137: lload #4
/*     */     //   139: bipush #100
/*     */     //   141: iload_1
/*     */     //   142: isub
/*     */     //   143: i2l
/*     */     //   144: lmul
/*     */     //   145: iload_1
/*     */     //   146: i2l
/*     */     //   147: ldiv
/*     */     //   148: invokevirtual runAllTasks : (J)Z
/*     */     //   151: pop
/*     */     //   152: goto -> 183
/*     */     //   155: astore #6
/*     */     //   157: invokestatic nanoTime : ()J
/*     */     //   160: lload_2
/*     */     //   161: lsub
/*     */     //   162: lstore #7
/*     */     //   164: aload_0
/*     */     //   165: lload #7
/*     */     //   167: bipush #100
/*     */     //   169: iload_1
/*     */     //   170: isub
/*     */     //   171: i2l
/*     */     //   172: lmul
/*     */     //   173: iload_1
/*     */     //   174: i2l
/*     */     //   175: ldiv
/*     */     //   176: invokevirtual runAllTasks : (J)Z
/*     */     //   179: pop
/*     */     //   180: aload #6
/*     */     //   182: athrow
/*     */     //   183: goto -> 191
/*     */     //   186: astore_1
/*     */     //   187: aload_1
/*     */     //   188: invokestatic handleLoopException : (Ljava/lang/Throwable;)V
/*     */     //   191: aload_0
/*     */     //   192: invokevirtual isShuttingDown : ()Z
/*     */     //   195: ifeq -> 210
/*     */     //   198: aload_0
/*     */     //   199: invokespecial closeAll : ()V
/*     */     //   202: aload_0
/*     */     //   203: invokevirtual confirmShutdown : ()Z
/*     */     //   206: ifeq -> 210
/*     */     //   209: return
/*     */     //   210: goto -> 0
/*     */     //   213: astore_1
/*     */     //   214: aload_1
/*     */     //   215: invokestatic handleLoopException : (Ljava/lang/Throwable;)V
/*     */     //   218: goto -> 0
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #407	-> 0
/*     */     //   #409	-> 44
/*     */     //   #411	-> 47
/*     */     //   #441	-> 59
/*     */     //   #442	-> 69
/*     */     //   #448	-> 77
/*     */     //   #449	-> 82
/*     */     //   #450	-> 87
/*     */     //   #451	-> 92
/*     */     //   #453	-> 98
/*     */     //   #456	-> 102
/*     */     //   #457	-> 107
/*     */     //   #456	-> 110
/*     */     //   #457	-> 116
/*     */     //   #459	-> 121
/*     */     //   #461	-> 125
/*     */     //   #464	-> 129
/*     */     //   #465	-> 136
/*     */     //   #466	-> 152
/*     */     //   #464	-> 155
/*     */     //   #465	-> 164
/*     */     //   #466	-> 180
/*     */     //   #470	-> 183
/*     */     //   #468	-> 186
/*     */     //   #469	-> 187
/*     */     //   #473	-> 191
/*     */     //   #474	-> 198
/*     */     //   #475	-> 202
/*     */     //   #476	-> 209
/*     */     //   #481	-> 210
/*     */     //   #479	-> 213
/*     */     //   #480	-> 214
/*     */     //   #481	-> 218
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   136	16	4	ioTime	J
/*     */     //   164	16	7	ioTime	J
/*     */     //   125	58	2	ioStartTime	J
/*     */     //   92	91	1	ioRatio	I
/*     */     //   187	4	1	t	Ljava/lang/Throwable;
/*     */     //   214	4	1	t	Ljava/lang/Throwable;
/*     */     //   0	221	0	this	Lio/netty/channel/nio/NioEventLoop;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	44	186	java/lang/Throwable
/*     */     //   47	183	186	java/lang/Throwable
/*     */     //   98	102	110	finally
/*     */     //   125	129	155	finally
/*     */     //   155	157	155	finally
/*     */     //   191	209	213	java/lang/Throwable
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
/*     */   private static void handleLoopException(Throwable t) {
/* 486 */     logger.warn("Unexpected exception in the selector loop.", t);
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 491 */       Thread.sleep(1000L);
/* 492 */     } catch (InterruptedException interruptedException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void processSelectedKeys() {
/* 498 */     if (this.selectedKeys != null) {
/* 499 */       processSelectedKeysOptimized();
/*     */     } else {
/* 501 */       processSelectedKeysPlain(this.selector.selectedKeys());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void cleanup() {
/*     */     try {
/* 508 */       this.selector.close();
/* 509 */     } catch (IOException e) {
/* 510 */       logger.warn("Failed to close a selector.", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   void cancel(SelectionKey key) {
/* 515 */     key.cancel();
/* 516 */     this.cancelledKeys++;
/* 517 */     if (this.cancelledKeys >= 256) {
/* 518 */       this.cancelledKeys = 0;
/* 519 */       this.needsToSelectAgain = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Runnable pollTask() {
/* 525 */     Runnable task = super.pollTask();
/* 526 */     if (this.needsToSelectAgain) {
/* 527 */       selectAgain();
/*     */     }
/* 529 */     return task;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processSelectedKeysPlain(Set<SelectionKey> selectedKeys) {
/* 536 */     if (selectedKeys.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 540 */     Iterator<SelectionKey> i = selectedKeys.iterator();
/*     */     while (true) {
/* 542 */       SelectionKey k = i.next();
/* 543 */       Object a = k.attachment();
/* 544 */       i.remove();
/*     */       
/* 546 */       if (a instanceof AbstractNioChannel) {
/* 547 */         processSelectedKey(k, (AbstractNioChannel)a);
/*     */       } else {
/*     */         
/* 550 */         NioTask<SelectableChannel> task = (NioTask<SelectableChannel>)a;
/* 551 */         processSelectedKey(k, task);
/*     */       } 
/*     */       
/* 554 */       if (!i.hasNext()) {
/*     */         break;
/*     */       }
/*     */       
/* 558 */       if (this.needsToSelectAgain) {
/* 559 */         selectAgain();
/* 560 */         selectedKeys = this.selector.selectedKeys();
/*     */ 
/*     */         
/* 563 */         if (selectedKeys.isEmpty()) {
/*     */           break;
/*     */         }
/* 566 */         i = selectedKeys.iterator();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void processSelectedKeysOptimized() {
/* 573 */     for (int i = 0; i < this.selectedKeys.size; i++) {
/* 574 */       SelectionKey k = this.selectedKeys.keys[i];
/*     */ 
/*     */       
/* 577 */       this.selectedKeys.keys[i] = null;
/*     */       
/* 579 */       Object a = k.attachment();
/*     */       
/* 581 */       if (a instanceof AbstractNioChannel) {
/* 582 */         processSelectedKey(k, (AbstractNioChannel)a);
/*     */       } else {
/*     */         
/* 585 */         NioTask<SelectableChannel> task = (NioTask<SelectableChannel>)a;
/* 586 */         processSelectedKey(k, task);
/*     */       } 
/*     */       
/* 589 */       if (this.needsToSelectAgain) {
/*     */ 
/*     */         
/* 592 */         this.selectedKeys.reset(i + 1);
/*     */         
/* 594 */         selectAgain();
/* 595 */         i = -1;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processSelectedKey(SelectionKey k, AbstractNioChannel ch) {
/* 601 */     AbstractNioChannel.NioUnsafe unsafe = ch.unsafe();
/* 602 */     if (!k.isValid()) {
/*     */       NioEventLoop nioEventLoop;
/*     */       try {
/* 605 */         nioEventLoop = ch.eventLoop();
/* 606 */       } catch (Throwable ignored) {
/*     */         return;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 616 */       if (nioEventLoop != this || nioEventLoop == null) {
/*     */         return;
/*     */       }
/*     */       
/* 620 */       unsafe.close(unsafe.voidPromise());
/*     */       
/*     */       return;
/*     */     } 
/*     */     try {
/* 625 */       int readyOps = k.readyOps();
/*     */ 
/*     */       
/* 628 */       if ((readyOps & 0x8) != 0) {
/*     */ 
/*     */         
/* 631 */         int ops = k.interestOps();
/* 632 */         ops &= 0xFFFFFFF7;
/* 633 */         k.interestOps(ops);
/*     */         
/* 635 */         unsafe.finishConnect();
/*     */       } 
/*     */ 
/*     */       
/* 639 */       if ((readyOps & 0x4) != 0)
/*     */       {
/* 641 */         ch.unsafe().forceFlush();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 646 */       if ((readyOps & 0x11) != 0 || readyOps == 0) {
/* 647 */         unsafe.read();
/*     */       }
/* 649 */     } catch (CancelledKeyException ignored) {
/* 650 */       EventLoop eventLoop; unsafe.close(unsafe.voidPromise());
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void processSelectedKey(SelectionKey k, NioTask<SelectableChannel> task) {
/* 655 */     int state = 0;
/*     */     try {
/* 657 */       task.channelReady(k.channel(), k);
/* 658 */       state = 1;
/* 659 */     } catch (Exception e) {
/* 660 */       k.cancel();
/* 661 */       invokeChannelUnregistered(task, k, e);
/* 662 */       state = 2;
/*     */     } finally {
/* 664 */       switch (state) {
/*     */         case 0:
/* 666 */           k.cancel();
/* 667 */           invokeChannelUnregistered(task, k, (Throwable)null);
/*     */           break;
/*     */         case 1:
/* 670 */           if (!k.isValid()) {
/* 671 */             invokeChannelUnregistered(task, k, (Throwable)null);
/*     */           }
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void closeAll() {
/* 679 */     selectAgain();
/* 680 */     Set<SelectionKey> keys = this.selector.keys();
/* 681 */     Collection<AbstractNioChannel> channels = new ArrayList<AbstractNioChannel>(keys.size());
/* 682 */     for (SelectionKey k : keys) {
/* 683 */       Object a = k.attachment();
/* 684 */       if (a instanceof AbstractNioChannel) {
/* 685 */         channels.add((AbstractNioChannel)a); continue;
/*     */       } 
/* 687 */       k.cancel();
/*     */       
/* 689 */       NioTask<SelectableChannel> task = (NioTask<SelectableChannel>)a;
/* 690 */       invokeChannelUnregistered(task, k, (Throwable)null);
/*     */     } 
/*     */ 
/*     */     
/* 694 */     for (AbstractNioChannel ch : channels) {
/* 695 */       ch.unsafe().close(ch.unsafe().voidPromise());
/*     */     }
/*     */   }
/*     */   
/*     */   private static void invokeChannelUnregistered(NioTask<SelectableChannel> task, SelectionKey k, Throwable cause) {
/*     */     try {
/* 701 */       task.channelUnregistered(k.channel(), cause);
/* 702 */     } catch (Exception e) {
/* 703 */       logger.warn("Unexpected exception while running NioTask.channelUnregistered()", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void wakeup(boolean inEventLoop) {
/* 709 */     if (!inEventLoop && this.wakenUp.compareAndSet(false, true)) {
/* 710 */       this.selector.wakeup();
/*     */     }
/*     */   }
/*     */   
/*     */   Selector unwrappedSelector() {
/* 715 */     return this.unwrappedSelector;
/*     */   }
/*     */   
/*     */   int selectNow() throws IOException {
/*     */     try {
/* 720 */       return this.selector.selectNow();
/*     */     } finally {
/*     */       
/* 723 */       if (this.wakenUp.get()) {
/* 724 */         this.selector.wakeup();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void select(boolean oldWakenUp) throws IOException {
/* 730 */     Selector selector = this.selector;
/*     */     try {
/* 732 */       int selectCnt = 0;
/* 733 */       long currentTimeNanos = System.nanoTime();
/* 734 */       long selectDeadLineNanos = currentTimeNanos + delayNanos(currentTimeNanos);
/*     */       
/*     */       while (true) {
/* 737 */         long timeoutMillis = (selectDeadLineNanos - currentTimeNanos + 500000L) / 1000000L;
/* 738 */         if (timeoutMillis <= 0L) {
/* 739 */           if (selectCnt == 0) {
/* 740 */             selector.selectNow();
/* 741 */             selectCnt = 1;
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */ 
/*     */         
/* 750 */         if (hasTasks() && this.wakenUp.compareAndSet(false, true)) {
/* 751 */           selector.selectNow();
/* 752 */           selectCnt = 1;
/*     */           
/*     */           break;
/*     */         } 
/* 756 */         int selectedKeys = selector.select(timeoutMillis);
/* 757 */         selectCnt++;
/*     */         
/* 759 */         if (selectedKeys != 0 || oldWakenUp || this.wakenUp.get() || hasTasks() || hasScheduledTasks()) {
/*     */           break;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 766 */         if (Thread.interrupted()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 772 */           if (logger.isDebugEnabled()) {
/* 773 */             logger.debug("Selector.select() returned prematurely because Thread.currentThread().interrupt() was called. Use NioEventLoop.shutdownGracefully() to shutdown the NioEventLoop.");
/*     */           }
/*     */ 
/*     */           
/* 777 */           selectCnt = 1;
/*     */           
/*     */           break;
/*     */         } 
/* 781 */         long time = System.nanoTime();
/* 782 */         if (time - TimeUnit.MILLISECONDS.toNanos(timeoutMillis) >= currentTimeNanos) {
/*     */           
/* 784 */           selectCnt = 1;
/* 785 */         } else if (SELECTOR_AUTO_REBUILD_THRESHOLD > 0 && selectCnt >= SELECTOR_AUTO_REBUILD_THRESHOLD) {
/*     */ 
/*     */ 
/*     */           
/* 789 */           logger.warn("Selector.select() returned prematurely {} times in a row; rebuilding Selector {}.", 
/*     */               
/* 791 */               Integer.valueOf(selectCnt), selector);
/*     */           
/* 793 */           rebuildSelector();
/* 794 */           selector = this.selector;
/*     */ 
/*     */           
/* 797 */           selector.selectNow();
/* 798 */           selectCnt = 1;
/*     */           
/*     */           break;
/*     */         } 
/* 802 */         currentTimeNanos = time;
/*     */       } 
/*     */       
/* 805 */       if (selectCnt > 3 && 
/* 806 */         logger.isDebugEnabled()) {
/* 807 */         logger.debug("Selector.select() returned prematurely {} times in a row for Selector {}.", 
/* 808 */             Integer.valueOf(selectCnt - 1), selector);
/*     */       }
/*     */     }
/* 811 */     catch (CancelledKeyException e) {
/* 812 */       if (logger.isDebugEnabled()) {
/* 813 */         logger.debug(CancelledKeyException.class.getSimpleName() + " raised by a Selector {} - JDK bug?", selector, e);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void selectAgain() {
/* 821 */     this.needsToSelectAgain = false;
/*     */     try {
/* 823 */       this.selector.selectNow();
/* 824 */     } catch (Throwable t) {
/* 825 */       logger.warn("Failed to update SelectionKeys.", t);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void validateScheduled(long amount, TimeUnit unit) {
/* 831 */     long days = unit.toDays(amount);
/* 832 */     if (days > 1095L)
/* 833 */       throw new IllegalArgumentException("days: " + days + " (expected: < " + 1095L + ')'); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\nio\NioEventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */