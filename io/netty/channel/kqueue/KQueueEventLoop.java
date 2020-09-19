/*     */ package io.netty.channel.kqueue;
/*     */ 
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.SelectStrategy;
/*     */ import io.netty.channel.SingleThreadEventLoop;
/*     */ import io.netty.channel.unix.FileDescriptor;
/*     */ import io.netty.channel.unix.IovArray;
/*     */ import io.netty.util.IntSupplier;
/*     */ import io.netty.util.concurrent.RejectedExecutionHandler;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ final class KQueueEventLoop
/*     */   extends SingleThreadEventLoop
/*     */ {
/*  46 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(KQueueEventLoop.class);
/*     */   
/*  48 */   private static final AtomicIntegerFieldUpdater<KQueueEventLoop> WAKEN_UP_UPDATER = AtomicIntegerFieldUpdater.newUpdater(KQueueEventLoop.class, "wakenUp");
/*     */   private static final int KQUEUE_WAKE_UP_IDENT = 0;
/*     */   private final NativeLongArray jniChannelPointers;
/*     */   private final boolean allowGrowing;
/*     */   
/*     */   static {
/*  54 */     KQueue.ensureAvailability();
/*     */   }
/*     */ 
/*     */   
/*     */   private final FileDescriptor kqueueFd;
/*     */   
/*     */   private final KQueueEventArray changeList;
/*     */   private final KQueueEventArray eventList;
/*     */   private final SelectStrategy selectStrategy;
/*  63 */   private final IovArray iovArray = new IovArray();
/*  64 */   private final IntSupplier selectNowSupplier = new IntSupplier()
/*     */     {
/*     */       public int get() throws Exception {
/*  67 */         return KQueueEventLoop.this.kqueueWaitNow();
/*     */       }
/*     */     };
/*  70 */   private final Callable<Integer> pendingTasksCallable = new Callable<Integer>()
/*     */     {
/*     */       public Integer call() throws Exception {
/*  73 */         return Integer.valueOf(KQueueEventLoop.this.pendingTasks());
/*     */       }
/*     */     };
/*     */   
/*     */   private volatile int wakenUp;
/*  78 */   private volatile int ioRatio = 50;
/*     */   
/*     */   static final long MAX_SCHEDULED_DAYS = 1095L;
/*     */ 
/*     */   
/*     */   KQueueEventLoop(EventLoopGroup parent, Executor executor, int maxEvents, SelectStrategy strategy, RejectedExecutionHandler rejectedExecutionHandler) {
/*  84 */     super(parent, executor, false, DEFAULT_MAX_PENDING_TASKS, rejectedExecutionHandler);
/*  85 */     this.selectStrategy = (SelectStrategy)ObjectUtil.checkNotNull(strategy, "strategy");
/*  86 */     this.kqueueFd = Native.newKQueue();
/*  87 */     if (maxEvents == 0) {
/*  88 */       this.allowGrowing = true;
/*  89 */       maxEvents = 4096;
/*     */     } else {
/*  91 */       this.allowGrowing = false;
/*     */     } 
/*  93 */     this.changeList = new KQueueEventArray(maxEvents);
/*  94 */     this.eventList = new KQueueEventArray(maxEvents);
/*  95 */     this.jniChannelPointers = new NativeLongArray(4096);
/*  96 */     int result = Native.keventAddUserEvent(this.kqueueFd.intValue(), 0);
/*  97 */     if (result < 0) {
/*  98 */       cleanup();
/*  99 */       throw new IllegalStateException("kevent failed to add user event with errno: " + -result);
/*     */     } 
/*     */   }
/*     */   
/*     */   void evSet(AbstractKQueueChannel ch, short filter, short flags, int fflags) {
/* 104 */     this.changeList.evSet(ch, filter, flags, fflags);
/*     */   }
/*     */   
/*     */   void remove(AbstractKQueueChannel ch) throws IOException {
/* 108 */     assert inEventLoop();
/* 109 */     if (ch.jniSelfPtr == 0L) {
/*     */       return;
/*     */     }
/*     */     
/* 113 */     this.jniChannelPointers.add(ch.jniSelfPtr);
/* 114 */     ch.jniSelfPtr = 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   IovArray cleanArray() {
/* 121 */     this.iovArray.clear();
/* 122 */     return this.iovArray;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void wakeup(boolean inEventLoop) {
/* 127 */     if (!inEventLoop && WAKEN_UP_UPDATER.compareAndSet(this, 0, 1)) {
/* 128 */       wakeup();
/*     */     }
/*     */   }
/*     */   
/*     */   private void wakeup() {
/* 133 */     Native.keventTriggerUserEvent(this.kqueueFd.intValue(), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int kqueueWait(boolean oldWakeup) throws IOException {
/* 143 */     if (oldWakeup && hasTasks()) {
/* 144 */       return kqueueWaitNow();
/*     */     }
/*     */     
/* 147 */     long totalDelay = delayNanos(System.nanoTime());
/* 148 */     int delaySeconds = (int)Math.min(totalDelay / 1000000000L, 2147483647L);
/* 149 */     return kqueueWait(delaySeconds, (int)Math.min(totalDelay - delaySeconds * 1000000000L, 2147483647L));
/*     */   }
/*     */   
/*     */   private int kqueueWaitNow() throws IOException {
/* 153 */     return kqueueWait(0, 0);
/*     */   }
/*     */   
/*     */   private int kqueueWait(int timeoutSec, int timeoutNs) throws IOException {
/* 157 */     deleteJniChannelPointers();
/* 158 */     int numEvents = Native.keventWait(this.kqueueFd.intValue(), this.changeList, this.eventList, timeoutSec, timeoutNs);
/* 159 */     this.changeList.clear();
/* 160 */     return numEvents;
/*     */   }
/*     */   
/*     */   private void deleteJniChannelPointers() {
/* 164 */     if (!this.jniChannelPointers.isEmpty()) {
/* 165 */       KQueueEventArray.deleteGlobalRefs(this.jniChannelPointers.memoryAddress(), this.jniChannelPointers.memoryAddressEnd());
/* 166 */       this.jniChannelPointers.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processReady(int ready) {
/* 171 */     for (int i = 0; i < ready; i++) {
/* 172 */       short filter = this.eventList.filter(i);
/* 173 */       short flags = this.eventList.flags(i);
/* 174 */       if (filter == Native.EVFILT_USER || (flags & Native.EV_ERROR) != 0) {
/*     */ 
/*     */         
/* 177 */         assert filter != Native.EVFILT_USER || (filter == Native.EVFILT_USER && this.eventList
/* 178 */           .fd(i) == 0);
/*     */       }
/*     */       else {
/*     */         
/* 182 */         AbstractKQueueChannel channel = this.eventList.channel(i);
/* 183 */         if (channel == null) {
/*     */ 
/*     */ 
/*     */           
/* 187 */           logger.warn("events[{}]=[{}, {}] had no channel!", new Object[] { Integer.valueOf(i), Integer.valueOf(this.eventList.fd(i)), Short.valueOf(filter) });
/*     */         }
/*     */         else {
/*     */           
/* 191 */           AbstractKQueueChannel.AbstractKQueueUnsafe unsafe = (AbstractKQueueChannel.AbstractKQueueUnsafe)channel.unsafe();
/*     */ 
/*     */           
/* 194 */           if (filter == Native.EVFILT_WRITE) {
/* 195 */             unsafe.writeReady();
/* 196 */           } else if (filter == Native.EVFILT_READ) {
/*     */             
/* 198 */             unsafe.readReady(this.eventList.data(i));
/* 199 */           } else if (filter == Native.EVFILT_SOCK && (this.eventList.fflags(i) & Native.NOTE_RDHUP) != 0) {
/* 200 */             unsafe.readEOF();
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 206 */           if ((flags & Native.EV_EOF) != 0) {
/* 207 */             unsafe.readEOF();
/*     */           }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */     //   19: lookupswitch default -> 81, -2 -> 44, -1 -> 47
/*     */     //   44: goto -> 0
/*     */     //   47: aload_0
/*     */     //   48: getstatic io/netty/channel/kqueue/KQueueEventLoop.WAKEN_UP_UPDATER : Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */     //   51: aload_0
/*     */     //   52: iconst_0
/*     */     //   53: invokevirtual getAndSet : (Ljava/lang/Object;I)I
/*     */     //   56: iconst_1
/*     */     //   57: if_icmpne -> 64
/*     */     //   60: iconst_1
/*     */     //   61: goto -> 65
/*     */     //   64: iconst_0
/*     */     //   65: invokespecial kqueueWait : (Z)I
/*     */     //   68: istore_1
/*     */     //   69: aload_0
/*     */     //   70: getfield wakenUp : I
/*     */     //   73: iconst_1
/*     */     //   74: if_icmpne -> 81
/*     */     //   77: aload_0
/*     */     //   78: invokespecial wakeup : ()V
/*     */     //   81: aload_0
/*     */     //   82: getfield ioRatio : I
/*     */     //   85: istore_2
/*     */     //   86: iload_2
/*     */     //   87: bipush #100
/*     */     //   89: if_icmpne -> 120
/*     */     //   92: iload_1
/*     */     //   93: ifle -> 101
/*     */     //   96: aload_0
/*     */     //   97: iload_1
/*     */     //   98: invokespecial processReady : (I)V
/*     */     //   101: aload_0
/*     */     //   102: invokevirtual runAllTasks : ()Z
/*     */     //   105: pop
/*     */     //   106: goto -> 117
/*     */     //   109: astore_3
/*     */     //   110: aload_0
/*     */     //   111: invokevirtual runAllTasks : ()Z
/*     */     //   114: pop
/*     */     //   115: aload_3
/*     */     //   116: athrow
/*     */     //   117: goto -> 187
/*     */     //   120: invokestatic nanoTime : ()J
/*     */     //   123: lstore_3
/*     */     //   124: iload_1
/*     */     //   125: ifle -> 133
/*     */     //   128: aload_0
/*     */     //   129: iload_1
/*     */     //   130: invokespecial processReady : (I)V
/*     */     //   133: invokestatic nanoTime : ()J
/*     */     //   136: lload_3
/*     */     //   137: lsub
/*     */     //   138: lstore #5
/*     */     //   140: aload_0
/*     */     //   141: lload #5
/*     */     //   143: bipush #100
/*     */     //   145: iload_2
/*     */     //   146: isub
/*     */     //   147: i2l
/*     */     //   148: lmul
/*     */     //   149: iload_2
/*     */     //   150: i2l
/*     */     //   151: ldiv
/*     */     //   152: invokevirtual runAllTasks : (J)Z
/*     */     //   155: pop
/*     */     //   156: goto -> 187
/*     */     //   159: astore #7
/*     */     //   161: invokestatic nanoTime : ()J
/*     */     //   164: lload_3
/*     */     //   165: lsub
/*     */     //   166: lstore #8
/*     */     //   168: aload_0
/*     */     //   169: lload #8
/*     */     //   171: bipush #100
/*     */     //   173: iload_2
/*     */     //   174: isub
/*     */     //   175: i2l
/*     */     //   176: lmul
/*     */     //   177: iload_2
/*     */     //   178: i2l
/*     */     //   179: ldiv
/*     */     //   180: invokevirtual runAllTasks : (J)Z
/*     */     //   183: pop
/*     */     //   184: aload #7
/*     */     //   186: athrow
/*     */     //   187: aload_0
/*     */     //   188: getfield allowGrowing : Z
/*     */     //   191: ifeq -> 213
/*     */     //   194: iload_1
/*     */     //   195: aload_0
/*     */     //   196: getfield eventList : Lio/netty/channel/kqueue/KQueueEventArray;
/*     */     //   199: invokevirtual capacity : ()I
/*     */     //   202: if_icmpne -> 213
/*     */     //   205: aload_0
/*     */     //   206: getfield eventList : Lio/netty/channel/kqueue/KQueueEventArray;
/*     */     //   209: iconst_0
/*     */     //   210: invokevirtual realloc : (Z)V
/*     */     //   213: goto -> 221
/*     */     //   216: astore_1
/*     */     //   217: aload_1
/*     */     //   218: invokestatic handleLoopException : (Ljava/lang/Throwable;)V
/*     */     //   221: aload_0
/*     */     //   222: invokevirtual isShuttingDown : ()Z
/*     */     //   225: ifeq -> 242
/*     */     //   228: aload_0
/*     */     //   229: invokespecial closeAll : ()V
/*     */     //   232: aload_0
/*     */     //   233: invokevirtual confirmShutdown : ()Z
/*     */     //   236: ifeq -> 242
/*     */     //   239: goto -> 253
/*     */     //   242: goto -> 0
/*     */     //   245: astore_1
/*     */     //   246: aload_1
/*     */     //   247: invokestatic handleLoopException : (Ljava/lang/Throwable;)V
/*     */     //   250: goto -> 0
/*     */     //   253: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #216	-> 0
/*     */     //   #217	-> 18
/*     */     //   #219	-> 44
/*     */     //   #221	-> 47
/*     */     //   #251	-> 69
/*     */     //   #252	-> 77
/*     */     //   #258	-> 81
/*     */     //   #259	-> 86
/*     */     //   #261	-> 92
/*     */     //   #262	-> 96
/*     */     //   #265	-> 101
/*     */     //   #266	-> 106
/*     */     //   #265	-> 109
/*     */     //   #266	-> 115
/*     */     //   #268	-> 120
/*     */     //   #271	-> 124
/*     */     //   #272	-> 128
/*     */     //   #275	-> 133
/*     */     //   #276	-> 140
/*     */     //   #277	-> 156
/*     */     //   #275	-> 159
/*     */     //   #276	-> 168
/*     */     //   #277	-> 184
/*     */     //   #279	-> 187
/*     */     //   #281	-> 205
/*     */     //   #285	-> 213
/*     */     //   #283	-> 216
/*     */     //   #284	-> 217
/*     */     //   #288	-> 221
/*     */     //   #289	-> 228
/*     */     //   #290	-> 232
/*     */     //   #291	-> 239
/*     */     //   #296	-> 242
/*     */     //   #294	-> 245
/*     */     //   #295	-> 246
/*     */     //   #296	-> 250
/*     */     //   #298	-> 253
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   140	16	5	ioTime	J
/*     */     //   168	16	8	ioTime	J
/*     */     //   124	63	3	ioStartTime	J
/*     */     //   18	195	1	strategy	I
/*     */     //   86	127	2	ioRatio	I
/*     */     //   217	4	1	t	Ljava/lang/Throwable;
/*     */     //   246	4	1	t	Ljava/lang/Throwable;
/*     */     //   0	254	0	this	Lio/netty/channel/kqueue/KQueueEventLoop;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	44	216	java/lang/Throwable
/*     */     //   47	213	216	java/lang/Throwable
/*     */     //   92	101	109	finally
/*     */     //   124	133	159	finally
/*     */     //   159	161	159	finally
/*     */     //   221	239	245	java/lang/Throwable
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
/*     */   protected Queue<Runnable> newTaskQueue(int maxPendingTasks) {
/* 303 */     return (maxPendingTasks == Integer.MAX_VALUE) ? PlatformDependent.newMpscQueue() : 
/* 304 */       PlatformDependent.newMpscQueue(maxPendingTasks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int pendingTasks() {
/* 312 */     return inEventLoop() ? super.pendingTasks() : ((Integer)submit(this.pendingTasksCallable).syncUninterruptibly().getNow()).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIoRatio() {
/* 319 */     return this.ioRatio;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIoRatio(int ioRatio) {
/* 327 */     if (ioRatio <= 0 || ioRatio > 100) {
/* 328 */       throw new IllegalArgumentException("ioRatio: " + ioRatio + " (expected: 0 < ioRatio <= 100)");
/*     */     }
/* 330 */     this.ioRatio = ioRatio;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void cleanup() {
/*     */     try {
/*     */       try {
/* 337 */         this.kqueueFd.close();
/* 338 */       } catch (IOException e) {
/* 339 */         logger.warn("Failed to close the kqueue fd.", e);
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 346 */       deleteJniChannelPointers();
/* 347 */       this.jniChannelPointers.free();
/*     */       
/* 349 */       this.changeList.free();
/* 350 */       this.eventList.free();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void closeAll() {
/*     */     try {
/* 356 */       kqueueWaitNow();
/* 357 */     } catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void handleLoopException(Throwable t) {
/* 363 */     logger.warn("Unexpected exception in the selector loop.", t);
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 368 */       Thread.sleep(1000L);
/* 369 */     } catch (InterruptedException interruptedException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validateScheduled(long amount, TimeUnit unit) {
/* 376 */     long days = unit.toDays(amount);
/* 377 */     if (days > 1095L)
/* 378 */       throw new IllegalArgumentException("days: " + days + " (expected: < " + 1095L + ')'); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\KQueueEventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */