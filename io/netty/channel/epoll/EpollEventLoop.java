/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.SelectStrategy;
/*     */ import io.netty.channel.SingleThreadEventLoop;
/*     */ import io.netty.channel.unix.FileDescriptor;
/*     */ import io.netty.channel.unix.IovArray;
/*     */ import io.netty.util.IntSupplier;
/*     */ import io.netty.util.collection.IntObjectHashMap;
/*     */ import io.netty.util.collection.IntObjectMap;
/*     */ import io.netty.util.concurrent.RejectedExecutionHandler;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
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
/*     */ final class EpollEventLoop
/*     */   extends SingleThreadEventLoop
/*     */ {
/*  49 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(EpollEventLoop.class);
/*     */   
/*  51 */   private static final AtomicIntegerFieldUpdater<EpollEventLoop> WAKEN_UP_UPDATER = AtomicIntegerFieldUpdater.newUpdater(EpollEventLoop.class, "wakenUp");
/*     */   
/*     */   private final FileDescriptor epollFd;
/*     */   
/*     */   static {
/*  56 */     Epoll.ensureAvailability();
/*     */   }
/*     */ 
/*     */   
/*     */   private final FileDescriptor eventFd;
/*     */   private final FileDescriptor timerFd;
/*  62 */   private final IntObjectMap<AbstractEpollChannel> channels = (IntObjectMap<AbstractEpollChannel>)new IntObjectHashMap(4096); private final boolean allowGrowing;
/*     */   private final EpollEventArray events;
/*     */   private final SelectStrategy selectStrategy;
/*  65 */   private final IovArray iovArray = new IovArray();
/*     */   
/*  67 */   private final IntSupplier selectNowSupplier = new IntSupplier()
/*     */     {
/*     */       public int get() throws Exception {
/*  70 */         return EpollEventLoop.this.epollWaitNow();
/*     */       }
/*     */     };
/*  73 */   private final Callable<Integer> pendingTasksCallable = new Callable<Integer>()
/*     */     {
/*     */       public Integer call() throws Exception {
/*  76 */         return Integer.valueOf(EpollEventLoop.this.pendingTasks());
/*     */       }
/*     */     };
/*     */   private volatile int wakenUp;
/*  80 */   private volatile int ioRatio = 50;
/*     */ 
/*     */   
/*  83 */   static final long MAX_SCHEDULED_DAYS = TimeUnit.SECONDS.toDays(999999999L);
/*     */ 
/*     */   
/*     */   EpollEventLoop(EventLoopGroup parent, Executor executor, int maxEvents, SelectStrategy strategy, RejectedExecutionHandler rejectedExecutionHandler) {
/*  87 */     super(parent, executor, false, DEFAULT_MAX_PENDING_TASKS, rejectedExecutionHandler);
/*  88 */     this.selectStrategy = (SelectStrategy)ObjectUtil.checkNotNull(strategy, "strategy");
/*  89 */     if (maxEvents == 0) {
/*  90 */       this.allowGrowing = true;
/*  91 */       this.events = new EpollEventArray(4096);
/*     */     } else {
/*  93 */       this.allowGrowing = false;
/*  94 */       this.events = new EpollEventArray(maxEvents);
/*     */     } 
/*  96 */     boolean success = false;
/*  97 */     FileDescriptor epollFd = null;
/*  98 */     FileDescriptor eventFd = null;
/*  99 */     FileDescriptor timerFd = null;
/*     */     try {
/* 101 */       this.epollFd = epollFd = Native.newEpollCreate();
/* 102 */       this.eventFd = eventFd = Native.newEventFd();
/*     */       try {
/* 104 */         Native.epollCtlAdd(epollFd.intValue(), eventFd.intValue(), Native.EPOLLIN);
/* 105 */       } catch (IOException e) {
/* 106 */         throw new IllegalStateException("Unable to add eventFd filedescriptor to epoll", e);
/*     */       } 
/* 108 */       this.timerFd = timerFd = Native.newTimerFd();
/*     */       try {
/* 110 */         Native.epollCtlAdd(epollFd.intValue(), timerFd.intValue(), Native.EPOLLIN | Native.EPOLLET);
/* 111 */       } catch (IOException e) {
/* 112 */         throw new IllegalStateException("Unable to add timerFd filedescriptor to epoll", e);
/*     */       } 
/* 114 */       success = true;
/*     */     } finally {
/* 116 */       if (!success) {
/* 117 */         if (epollFd != null) {
/*     */           try {
/* 119 */             epollFd.close();
/* 120 */           } catch (Exception exception) {}
/*     */         }
/*     */ 
/*     */         
/* 124 */         if (eventFd != null) {
/*     */           try {
/* 126 */             eventFd.close();
/* 127 */           } catch (Exception exception) {}
/*     */         }
/*     */ 
/*     */         
/* 131 */         if (timerFd != null) {
/*     */           try {
/* 133 */             timerFd.close();
/* 134 */           } catch (Exception exception) {}
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   IovArray cleanArray() {
/* 146 */     this.iovArray.clear();
/* 147 */     return this.iovArray;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void wakeup(boolean inEventLoop) {
/* 152 */     if (!inEventLoop && WAKEN_UP_UPDATER.compareAndSet(this, 0, 1))
/*     */     {
/* 154 */       Native.eventFdWrite(this.eventFd.intValue(), 1L);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void add(AbstractEpollChannel ch) throws IOException {
/* 162 */     assert inEventLoop();
/* 163 */     int fd = ch.socket.intValue();
/* 164 */     Native.epollCtlAdd(this.epollFd.intValue(), fd, ch.flags);
/* 165 */     this.channels.put(fd, ch);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void modify(AbstractEpollChannel ch) throws IOException {
/* 172 */     assert inEventLoop();
/* 173 */     Native.epollCtlMod(this.epollFd.intValue(), ch.socket.intValue(), ch.flags);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void remove(AbstractEpollChannel ch) throws IOException {
/* 180 */     assert inEventLoop();
/*     */     
/* 182 */     if (ch.isOpen()) {
/* 183 */       int fd = ch.socket.intValue();
/* 184 */       if (this.channels.remove(fd) != null)
/*     */       {
/*     */         
/* 187 */         Native.epollCtlDel(this.epollFd.intValue(), ch.fd().intValue());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Queue<Runnable> newTaskQueue(int maxPendingTasks) {
/* 195 */     return (maxPendingTasks == Integer.MAX_VALUE) ? PlatformDependent.newMpscQueue() : 
/* 196 */       PlatformDependent.newMpscQueue(maxPendingTasks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int pendingTasks() {
/* 204 */     if (inEventLoop()) {
/* 205 */       return super.pendingTasks();
/*     */     }
/* 207 */     return ((Integer)submit(this.pendingTasksCallable).syncUninterruptibly().getNow()).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIoRatio() {
/* 214 */     return this.ioRatio;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIoRatio(int ioRatio) {
/* 222 */     if (ioRatio <= 0 || ioRatio > 100) {
/* 223 */       throw new IllegalArgumentException("ioRatio: " + ioRatio + " (expected: 0 < ioRatio <= 100)");
/*     */     }
/* 225 */     this.ioRatio = ioRatio;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int epollWait(boolean oldWakeup) throws IOException {
/* 233 */     if (oldWakeup && hasTasks()) {
/* 234 */       return epollWaitNow();
/*     */     }
/*     */     
/* 237 */     long totalDelay = delayNanos(System.nanoTime());
/* 238 */     int delaySeconds = (int)Math.min(totalDelay / 1000000000L, 2147483647L);
/* 239 */     return Native.epollWait(this.epollFd, this.events, this.timerFd, delaySeconds, 
/* 240 */         (int)Math.min(totalDelay - delaySeconds * 1000000000L, 2147483647L));
/*     */   }
/*     */   
/*     */   private int epollWaitNow() throws IOException {
/* 244 */     return Native.epollWait(this.epollFd, this.events, this.timerFd, 0, 0);
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
/*     */     //   17: istore_1
/*     */     //   18: iload_1
/*     */     //   19: lookupswitch default -> 88, -2 -> 44, -1 -> 47
/*     */     //   44: goto -> 0
/*     */     //   47: aload_0
/*     */     //   48: getstatic io/netty/channel/epoll/EpollEventLoop.WAKEN_UP_UPDATER : Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */     //   51: aload_0
/*     */     //   52: iconst_0
/*     */     //   53: invokevirtual getAndSet : (Ljava/lang/Object;I)I
/*     */     //   56: iconst_1
/*     */     //   57: if_icmpne -> 64
/*     */     //   60: iconst_1
/*     */     //   61: goto -> 65
/*     */     //   64: iconst_0
/*     */     //   65: invokespecial epollWait : (Z)I
/*     */     //   68: istore_1
/*     */     //   69: aload_0
/*     */     //   70: getfield wakenUp : I
/*     */     //   73: iconst_1
/*     */     //   74: if_icmpne -> 88
/*     */     //   77: aload_0
/*     */     //   78: getfield eventFd : Lio/netty/channel/unix/FileDescriptor;
/*     */     //   81: invokevirtual intValue : ()I
/*     */     //   84: lconst_1
/*     */     //   85: invokestatic eventFdWrite : (IJ)V
/*     */     //   88: aload_0
/*     */     //   89: getfield ioRatio : I
/*     */     //   92: istore_2
/*     */     //   93: iload_2
/*     */     //   94: bipush #100
/*     */     //   96: if_icmpne -> 131
/*     */     //   99: iload_1
/*     */     //   100: ifle -> 112
/*     */     //   103: aload_0
/*     */     //   104: aload_0
/*     */     //   105: getfield events : Lio/netty/channel/epoll/EpollEventArray;
/*     */     //   108: iload_1
/*     */     //   109: invokespecial processReady : (Lio/netty/channel/epoll/EpollEventArray;I)V
/*     */     //   112: aload_0
/*     */     //   113: invokevirtual runAllTasks : ()Z
/*     */     //   116: pop
/*     */     //   117: goto -> 128
/*     */     //   120: astore_3
/*     */     //   121: aload_0
/*     */     //   122: invokevirtual runAllTasks : ()Z
/*     */     //   125: pop
/*     */     //   126: aload_3
/*     */     //   127: athrow
/*     */     //   128: goto -> 202
/*     */     //   131: invokestatic nanoTime : ()J
/*     */     //   134: lstore_3
/*     */     //   135: iload_1
/*     */     //   136: ifle -> 148
/*     */     //   139: aload_0
/*     */     //   140: aload_0
/*     */     //   141: getfield events : Lio/netty/channel/epoll/EpollEventArray;
/*     */     //   144: iload_1
/*     */     //   145: invokespecial processReady : (Lio/netty/channel/epoll/EpollEventArray;I)V
/*     */     //   148: invokestatic nanoTime : ()J
/*     */     //   151: lload_3
/*     */     //   152: lsub
/*     */     //   153: lstore #5
/*     */     //   155: aload_0
/*     */     //   156: lload #5
/*     */     //   158: bipush #100
/*     */     //   160: iload_2
/*     */     //   161: isub
/*     */     //   162: i2l
/*     */     //   163: lmul
/*     */     //   164: iload_2
/*     */     //   165: i2l
/*     */     //   166: ldiv
/*     */     //   167: invokevirtual runAllTasks : (J)Z
/*     */     //   170: pop
/*     */     //   171: goto -> 202
/*     */     //   174: astore #7
/*     */     //   176: invokestatic nanoTime : ()J
/*     */     //   179: lload_3
/*     */     //   180: lsub
/*     */     //   181: lstore #8
/*     */     //   183: aload_0
/*     */     //   184: lload #8
/*     */     //   186: bipush #100
/*     */     //   188: iload_2
/*     */     //   189: isub
/*     */     //   190: i2l
/*     */     //   191: lmul
/*     */     //   192: iload_2
/*     */     //   193: i2l
/*     */     //   194: ldiv
/*     */     //   195: invokevirtual runAllTasks : (J)Z
/*     */     //   198: pop
/*     */     //   199: aload #7
/*     */     //   201: athrow
/*     */     //   202: aload_0
/*     */     //   203: getfield allowGrowing : Z
/*     */     //   206: ifeq -> 227
/*     */     //   209: iload_1
/*     */     //   210: aload_0
/*     */     //   211: getfield events : Lio/netty/channel/epoll/EpollEventArray;
/*     */     //   214: invokevirtual length : ()I
/*     */     //   217: if_icmpne -> 227
/*     */     //   220: aload_0
/*     */     //   221: getfield events : Lio/netty/channel/epoll/EpollEventArray;
/*     */     //   224: invokevirtual increase : ()V
/*     */     //   227: goto -> 235
/*     */     //   230: astore_1
/*     */     //   231: aload_1
/*     */     //   232: invokestatic handleLoopException : (Ljava/lang/Throwable;)V
/*     */     //   235: aload_0
/*     */     //   236: invokevirtual isShuttingDown : ()Z
/*     */     //   239: ifeq -> 256
/*     */     //   242: aload_0
/*     */     //   243: invokespecial closeAll : ()V
/*     */     //   246: aload_0
/*     */     //   247: invokevirtual confirmShutdown : ()Z
/*     */     //   250: ifeq -> 256
/*     */     //   253: goto -> 267
/*     */     //   256: goto -> 0
/*     */     //   259: astore_1
/*     */     //   260: aload_1
/*     */     //   261: invokestatic handleLoopException : (Ljava/lang/Throwable;)V
/*     */     //   264: goto -> 0
/*     */     //   267: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #251	-> 0
/*     */     //   #252	-> 18
/*     */     //   #254	-> 44
/*     */     //   #256	-> 47
/*     */     //   #286	-> 69
/*     */     //   #287	-> 77
/*     */     //   #293	-> 88
/*     */     //   #294	-> 93
/*     */     //   #296	-> 99
/*     */     //   #297	-> 103
/*     */     //   #301	-> 112
/*     */     //   #302	-> 117
/*     */     //   #301	-> 120
/*     */     //   #302	-> 126
/*     */     //   #304	-> 131
/*     */     //   #307	-> 135
/*     */     //   #308	-> 139
/*     */     //   #312	-> 148
/*     */     //   #313	-> 155
/*     */     //   #314	-> 171
/*     */     //   #312	-> 174
/*     */     //   #313	-> 183
/*     */     //   #314	-> 199
/*     */     //   #316	-> 202
/*     */     //   #318	-> 220
/*     */     //   #322	-> 227
/*     */     //   #320	-> 230
/*     */     //   #321	-> 231
/*     */     //   #325	-> 235
/*     */     //   #326	-> 242
/*     */     //   #327	-> 246
/*     */     //   #328	-> 253
/*     */     //   #333	-> 256
/*     */     //   #331	-> 259
/*     */     //   #332	-> 260
/*     */     //   #333	-> 264
/*     */     //   #335	-> 267
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   155	16	5	ioTime	J
/*     */     //   183	16	8	ioTime	J
/*     */     //   135	67	3	ioStartTime	J
/*     */     //   18	209	1	strategy	I
/*     */     //   93	134	2	ioRatio	I
/*     */     //   231	4	1	t	Ljava/lang/Throwable;
/*     */     //   260	4	1	t	Ljava/lang/Throwable;
/*     */     //   0	268	0	this	Lio/netty/channel/epoll/EpollEventLoop;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	44	230	java/lang/Throwable
/*     */     //   47	227	230	java/lang/Throwable
/*     */     //   99	112	120	finally
/*     */     //   135	148	174	finally
/*     */     //   174	176	174	finally
/*     */     //   235	253	259	java/lang/Throwable
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void handleLoopException(Throwable t) {
/* 338 */     logger.warn("Unexpected exception in the selector loop.", t);
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 343 */       Thread.sleep(1000L);
/* 344 */     } catch (InterruptedException interruptedException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void closeAll() {
/*     */     try {
/* 351 */       epollWaitNow();
/* 352 */     } catch (IOException iOException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 357 */     Collection<AbstractEpollChannel> array = new ArrayList<AbstractEpollChannel>(this.channels.size());
/*     */     
/* 359 */     for (AbstractEpollChannel channel : this.channels.values()) {
/* 360 */       array.add(channel);
/*     */     }
/*     */     
/* 363 */     for (AbstractEpollChannel ch : array) {
/* 364 */       ch.unsafe().close(ch.unsafe().voidPromise());
/*     */     }
/*     */   }
/*     */   
/*     */   private void processReady(EpollEventArray events, int ready) {
/* 369 */     for (int i = 0; i < ready; i++) {
/* 370 */       int fd = events.fd(i);
/* 371 */       if (fd == this.eventFd.intValue()) {
/*     */         
/* 373 */         Native.eventFdRead(fd);
/* 374 */       } else if (fd == this.timerFd.intValue()) {
/*     */         
/* 376 */         Native.timerFdRead(fd);
/*     */       } else {
/* 378 */         long ev = events.events(i);
/*     */         
/* 380 */         AbstractEpollChannel ch = (AbstractEpollChannel)this.channels.get(fd);
/* 381 */         if (ch != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 386 */           AbstractEpollChannel.AbstractEpollUnsafe unsafe = (AbstractEpollChannel.AbstractEpollUnsafe)ch.unsafe();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 396 */           if ((ev & (Native.EPOLLERR | Native.EPOLLOUT)) != 0L)
/*     */           {
/* 398 */             unsafe.epollOutReady();
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 406 */           if ((ev & (Native.EPOLLERR | Native.EPOLLIN)) != 0L)
/*     */           {
/* 408 */             unsafe.epollInReady();
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 414 */           if ((ev & Native.EPOLLRDHUP) != 0L) {
/* 415 */             unsafe.epollRdHupReady();
/*     */           }
/*     */         } else {
/*     */           
/*     */           try {
/* 420 */             Native.epollCtlDel(this.epollFd.intValue(), fd);
/* 421 */           } catch (IOException iOException) {}
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void cleanup() {
/*     */     try {
/*     */       try {
/* 436 */         this.epollFd.close();
/* 437 */       } catch (IOException e) {
/* 438 */         logger.warn("Failed to close the epoll fd.", e);
/*     */       } 
/*     */       try {
/* 441 */         this.eventFd.close();
/* 442 */       } catch (IOException e) {
/* 443 */         logger.warn("Failed to close the event fd.", e);
/*     */       } 
/*     */       try {
/* 446 */         this.timerFd.close();
/* 447 */       } catch (IOException e) {
/* 448 */         logger.warn("Failed to close the timer fd.", e);
/*     */       } 
/*     */     } finally {
/*     */       
/* 452 */       this.iovArray.release();
/* 453 */       this.events.free();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void validateScheduled(long amount, TimeUnit unit) {
/* 459 */     long days = unit.toDays(amount);
/* 460 */     if (days > MAX_SCHEDULED_DAYS)
/* 461 */       throw new IllegalArgumentException("days: " + days + " (expected: < " + MAX_SCHEDULED_DAYS + ')'); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\epoll\EpollEventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */