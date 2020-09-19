/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Throwables;
/*      */ import com.google.j2objc.annotations.Weak;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.locks.Condition;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import java.util.function.BooleanSupplier;
/*      */ import javax.annotation.concurrent.GuardedBy;
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
/*      */ @Beta
/*      */ @GwtIncompatible
/*      */ public final class Monitor
/*      */ {
/*      */   private final boolean fair;
/*      */   private final ReentrantLock lock;
/*      */   
/*      */   @Beta
/*      */   public static abstract class Guard
/*      */   {
/*      */     @Weak
/*      */     final Monitor monitor;
/*      */     final Condition condition;
/*      */     @GuardedBy("monitor.lock")
/*  307 */     int waiterCount = 0;
/*      */ 
/*      */     
/*      */     @GuardedBy("monitor.lock")
/*      */     Guard next;
/*      */ 
/*      */     
/*      */     protected Guard(Monitor monitor) {
/*  315 */       this.monitor = (Monitor)Preconditions.checkNotNull(monitor, "monitor");
/*  316 */       this.condition = monitor.lock.newCondition();
/*      */     }
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
/*      */     public abstract boolean isSatisfied();
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
/*      */   @GuardedBy("lock")
/*  341 */   private Guard activeGuards = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Monitor() {
/*  349 */     this(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Monitor(boolean fair) {
/*  359 */     this.fair = fair;
/*  360 */     this.lock = new ReentrantLock(fair);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Guard newGuard(final BooleanSupplier isSatisfied) {
/*  369 */     Preconditions.checkNotNull(isSatisfied, "isSatisfied");
/*  370 */     return new Guard(this)
/*      */       {
/*      */         public boolean isSatisfied() {
/*  373 */           return isSatisfied.getAsBoolean();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enter() {
/*  382 */     this.lock.lock();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enterInterruptibly() throws InterruptedException {
/*  391 */     this.lock.lockInterruptibly();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enter(long time, TimeUnit unit) {
/*  400 */     long timeoutNanos = toSafeNanos(time, unit);
/*  401 */     ReentrantLock lock = this.lock;
/*  402 */     if (!this.fair && lock.tryLock()) {
/*  403 */       return true;
/*      */     }
/*  405 */     boolean interrupted = Thread.interrupted();
/*      */     try {
/*  407 */       long startTime = System.nanoTime();
/*  408 */       long remainingNanos = timeoutNanos; while (true) {
/*      */         try {
/*  410 */           return lock.tryLock(remainingNanos, TimeUnit.NANOSECONDS);
/*  411 */         } catch (InterruptedException interrupt) {
/*  412 */           interrupted = true;
/*  413 */           remainingNanos = remainingNanos(startTime, timeoutNanos);
/*      */         } 
/*      */       } 
/*      */     } finally {
/*  417 */       if (interrupted) {
/*  418 */         Thread.currentThread().interrupt();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterInterruptibly(long time, TimeUnit unit) throws InterruptedException {
/*  430 */     return this.lock.tryLock(time, unit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean tryEnter() {
/*  441 */     return this.lock.tryLock();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enterWhen(Guard guard) throws InterruptedException {
/*  450 */     if (guard.monitor != this) {
/*  451 */       throw new IllegalMonitorStateException();
/*      */     }
/*  453 */     ReentrantLock lock = this.lock;
/*  454 */     boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
/*  455 */     lock.lockInterruptibly();
/*      */     
/*  457 */     boolean satisfied = false;
/*      */     try {
/*  459 */       if (!guard.isSatisfied()) {
/*  460 */         await(guard, signalBeforeWaiting);
/*      */       }
/*  462 */       satisfied = true;
/*      */     } finally {
/*  464 */       if (!satisfied) {
/*  465 */         leave();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enterWhenUninterruptibly(Guard guard) {
/*  474 */     if (guard.monitor != this) {
/*  475 */       throw new IllegalMonitorStateException();
/*      */     }
/*  477 */     ReentrantLock lock = this.lock;
/*  478 */     boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
/*  479 */     lock.lock();
/*      */     
/*  481 */     boolean satisfied = false;
/*      */     try {
/*  483 */       if (!guard.isSatisfied()) {
/*  484 */         awaitUninterruptibly(guard, signalBeforeWaiting);
/*      */       }
/*  486 */       satisfied = true;
/*      */     } finally {
/*  488 */       if (!satisfied) {
/*  489 */         leave();
/*      */       }
/*      */     } 
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
/*      */   public boolean enterWhen(Guard guard, long time, TimeUnit unit) throws InterruptedException {
/*      */     // Byte code:
/*      */     //   0: lload_2
/*      */     //   1: aload #4
/*      */     //   3: invokestatic toSafeNanos : (JLjava/util/concurrent/TimeUnit;)J
/*      */     //   6: lstore #5
/*      */     //   8: aload_1
/*      */     //   9: getfield monitor : Lcom/google/common/util/concurrent/Monitor;
/*      */     //   12: aload_0
/*      */     //   13: if_acmpeq -> 24
/*      */     //   16: new java/lang/IllegalMonitorStateException
/*      */     //   19: dup
/*      */     //   20: invokespecial <init> : ()V
/*      */     //   23: athrow
/*      */     //   24: aload_0
/*      */     //   25: getfield lock : Ljava/util/concurrent/locks/ReentrantLock;
/*      */     //   28: astore #7
/*      */     //   30: aload #7
/*      */     //   32: invokevirtual isHeldByCurrentThread : ()Z
/*      */     //   35: istore #8
/*      */     //   37: lconst_0
/*      */     //   38: lstore #9
/*      */     //   40: aload_0
/*      */     //   41: getfield fair : Z
/*      */     //   44: ifne -> 72
/*      */     //   47: invokestatic interrupted : ()Z
/*      */     //   50: ifeq -> 61
/*      */     //   53: new java/lang/InterruptedException
/*      */     //   56: dup
/*      */     //   57: invokespecial <init> : ()V
/*      */     //   60: athrow
/*      */     //   61: aload #7
/*      */     //   63: invokevirtual tryLock : ()Z
/*      */     //   66: ifeq -> 72
/*      */     //   69: goto -> 92
/*      */     //   72: lload #5
/*      */     //   74: invokestatic initNanoTime : (J)J
/*      */     //   77: lstore #9
/*      */     //   79: aload #7
/*      */     //   81: lload_2
/*      */     //   82: aload #4
/*      */     //   84: invokevirtual tryLock : (JLjava/util/concurrent/TimeUnit;)Z
/*      */     //   87: ifne -> 92
/*      */     //   90: iconst_0
/*      */     //   91: ireturn
/*      */     //   92: iconst_0
/*      */     //   93: istore #11
/*      */     //   95: iconst_1
/*      */     //   96: istore #12
/*      */     //   98: aload_1
/*      */     //   99: invokevirtual isSatisfied : ()Z
/*      */     //   102: ifne -> 134
/*      */     //   105: aload_0
/*      */     //   106: aload_1
/*      */     //   107: lload #9
/*      */     //   109: lconst_0
/*      */     //   110: lcmp
/*      */     //   111: ifne -> 119
/*      */     //   114: lload #5
/*      */     //   116: goto -> 126
/*      */     //   119: lload #9
/*      */     //   121: lload #5
/*      */     //   123: invokestatic remainingNanos : (JJ)J
/*      */     //   126: iload #8
/*      */     //   128: invokespecial awaitNanos : (Lcom/google/common/util/concurrent/Monitor$Guard;JZ)Z
/*      */     //   131: ifeq -> 138
/*      */     //   134: iconst_1
/*      */     //   135: goto -> 139
/*      */     //   138: iconst_0
/*      */     //   139: istore #11
/*      */     //   141: iconst_0
/*      */     //   142: istore #12
/*      */     //   144: iload #11
/*      */     //   146: istore #13
/*      */     //   148: iload #11
/*      */     //   150: ifne -> 185
/*      */     //   153: iload #12
/*      */     //   155: ifeq -> 167
/*      */     //   158: iload #8
/*      */     //   160: ifne -> 167
/*      */     //   163: aload_0
/*      */     //   164: invokespecial signalNextWaiter : ()V
/*      */     //   167: aload #7
/*      */     //   169: invokevirtual unlock : ()V
/*      */     //   172: goto -> 185
/*      */     //   175: astore #14
/*      */     //   177: aload #7
/*      */     //   179: invokevirtual unlock : ()V
/*      */     //   182: aload #14
/*      */     //   184: athrow
/*      */     //   185: iload #13
/*      */     //   187: ireturn
/*      */     //   188: astore #15
/*      */     //   190: iload #11
/*      */     //   192: ifne -> 227
/*      */     //   195: iload #12
/*      */     //   197: ifeq -> 209
/*      */     //   200: iload #8
/*      */     //   202: ifne -> 209
/*      */     //   205: aload_0
/*      */     //   206: invokespecial signalNextWaiter : ()V
/*      */     //   209: aload #7
/*      */     //   211: invokevirtual unlock : ()V
/*      */     //   214: goto -> 227
/*      */     //   217: astore #16
/*      */     //   219: aload #7
/*      */     //   221: invokevirtual unlock : ()V
/*      */     //   224: aload #16
/*      */     //   226: athrow
/*      */     //   227: aload #15
/*      */     //   229: athrow
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #503	-> 0
/*      */     //   #504	-> 8
/*      */     //   #505	-> 16
/*      */     //   #507	-> 24
/*      */     //   #508	-> 30
/*      */     //   #509	-> 37
/*      */     //   #513	-> 40
/*      */     //   #515	-> 47
/*      */     //   #516	-> 53
/*      */     //   #518	-> 61
/*      */     //   #519	-> 69
/*      */     //   #522	-> 72
/*      */     //   #523	-> 79
/*      */     //   #524	-> 90
/*      */     //   #528	-> 92
/*      */     //   #529	-> 95
/*      */     //   #531	-> 98
/*      */     //   #532	-> 99
/*      */     //   #535	-> 123
/*      */     //   #533	-> 128
/*      */     //   #537	-> 141
/*      */     //   #538	-> 144
/*      */     //   #540	-> 148
/*      */     //   #543	-> 153
/*      */     //   #544	-> 163
/*      */     //   #547	-> 167
/*      */     //   #548	-> 172
/*      */     //   #547	-> 175
/*      */     //   #538	-> 185
/*      */     //   #540	-> 188
/*      */     //   #543	-> 195
/*      */     //   #544	-> 205
/*      */     //   #547	-> 209
/*      */     //   #548	-> 214
/*      */     //   #547	-> 217
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	230	0	this	Lcom/google/common/util/concurrent/Monitor;
/*      */     //   0	230	1	guard	Lcom/google/common/util/concurrent/Monitor$Guard;
/*      */     //   0	230	2	time	J
/*      */     //   0	230	4	unit	Ljava/util/concurrent/TimeUnit;
/*      */     //   8	222	5	timeoutNanos	J
/*      */     //   30	200	7	lock	Ljava/util/concurrent/locks/ReentrantLock;
/*      */     //   37	193	8	reentrant	Z
/*      */     //   40	190	9	startTime	J
/*      */     //   95	135	11	satisfied	Z
/*      */     //   98	132	12	threw	Z
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   98	148	188	finally
/*      */     //   153	167	175	finally
/*      */     //   175	177	175	finally
/*      */     //   188	190	188	finally
/*      */     //   195	209	217	finally
/*      */     //   217	219	217	finally
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
/*      */   public boolean enterWhenUninterruptibly(Guard guard, long time, TimeUnit unit) {
/*  560 */     long timeoutNanos = toSafeNanos(time, unit);
/*  561 */     if (guard.monitor != this) {
/*  562 */       throw new IllegalMonitorStateException();
/*      */     }
/*  564 */     ReentrantLock lock = this.lock;
/*  565 */     long startTime = 0L;
/*  566 */     boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
/*  567 */     boolean interrupted = Thread.interrupted();
/*      */     try {
/*  569 */       if (this.fair || !lock.tryLock()) {
/*  570 */         startTime = initNanoTime(timeoutNanos);
/*  571 */         long remainingNanos = timeoutNanos; while (true) {
/*      */           try {
/*  573 */             if (lock.tryLock(remainingNanos, TimeUnit.NANOSECONDS)) {
/*      */               break;
/*      */             }
/*  576 */             return false;
/*      */           }
/*  578 */           catch (InterruptedException interrupt) {
/*  579 */             interrupted = true;
/*  580 */             remainingNanos = remainingNanos(startTime, timeoutNanos);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  585 */       boolean satisfied = false;
/*      */       
/*      */       while (true) {
/*      */         try {
/*  589 */           if (guard.isSatisfied()) {
/*  590 */             satisfied = true;
/*      */           } else {
/*      */             long remainingNanos;
/*  593 */             if (startTime == 0L) {
/*  594 */               startTime = initNanoTime(timeoutNanos);
/*  595 */               remainingNanos = timeoutNanos;
/*      */             } else {
/*  597 */               remainingNanos = remainingNanos(startTime, timeoutNanos);
/*      */             } 
/*  599 */             satisfied = awaitNanos(guard, remainingNanos, signalBeforeWaiting);
/*      */           } 
/*  601 */           return satisfied;
/*  602 */         } catch (InterruptedException interrupt) {
/*  603 */           interrupted = true;
/*      */         
/*      */         }
/*      */         finally {
/*      */           
/*  608 */           if (!satisfied)
/*  609 */             lock.unlock(); 
/*      */         } 
/*      */       } 
/*      */     } finally {
/*  613 */       if (interrupted) {
/*  614 */         Thread.currentThread().interrupt();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterIf(Guard guard) {
/*  626 */     if (guard.monitor != this) {
/*  627 */       throw new IllegalMonitorStateException();
/*      */     }
/*  629 */     ReentrantLock lock = this.lock;
/*  630 */     lock.lock();
/*      */     
/*  632 */     boolean satisfied = false;
/*      */     try {
/*  634 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  636 */       if (!satisfied) {
/*  637 */         lock.unlock();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterIfInterruptibly(Guard guard) throws InterruptedException {
/*  650 */     if (guard.monitor != this) {
/*  651 */       throw new IllegalMonitorStateException();
/*      */     }
/*  653 */     ReentrantLock lock = this.lock;
/*  654 */     lock.lockInterruptibly();
/*      */     
/*  656 */     boolean satisfied = false;
/*      */     try {
/*  658 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  660 */       if (!satisfied) {
/*  661 */         lock.unlock();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterIf(Guard guard, long time, TimeUnit unit) {
/*  673 */     if (guard.monitor != this) {
/*  674 */       throw new IllegalMonitorStateException();
/*      */     }
/*  676 */     if (!enter(time, unit)) {
/*  677 */       return false;
/*      */     }
/*      */     
/*  680 */     boolean satisfied = false;
/*      */     try {
/*  682 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  684 */       if (!satisfied) {
/*  685 */         this.lock.unlock();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterIfInterruptibly(Guard guard, long time, TimeUnit unit) throws InterruptedException {
/*  698 */     if (guard.monitor != this) {
/*  699 */       throw new IllegalMonitorStateException();
/*      */     }
/*  701 */     ReentrantLock lock = this.lock;
/*  702 */     if (!lock.tryLock(time, unit)) {
/*  703 */       return false;
/*      */     }
/*      */     
/*  706 */     boolean satisfied = false;
/*      */     try {
/*  708 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  710 */       if (!satisfied) {
/*  711 */         lock.unlock();
/*      */       }
/*      */     } 
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
/*      */   public boolean tryEnterIf(Guard guard) {
/*  725 */     if (guard.monitor != this) {
/*  726 */       throw new IllegalMonitorStateException();
/*      */     }
/*  728 */     ReentrantLock lock = this.lock;
/*  729 */     if (!lock.tryLock()) {
/*  730 */       return false;
/*      */     }
/*      */     
/*  733 */     boolean satisfied = false;
/*      */     try {
/*  735 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  737 */       if (!satisfied) {
/*  738 */         lock.unlock();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void waitFor(Guard guard) throws InterruptedException {
/*  750 */     if ((((guard.monitor == this) ? 1 : 0) & this.lock.isHeldByCurrentThread()) == 0) {
/*  751 */       throw new IllegalMonitorStateException();
/*      */     }
/*  753 */     if (!guard.isSatisfied()) {
/*  754 */       await(guard, true);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void waitForUninterruptibly(Guard guard) {
/*  763 */     if ((((guard.monitor == this) ? 1 : 0) & this.lock.isHeldByCurrentThread()) == 0) {
/*  764 */       throw new IllegalMonitorStateException();
/*      */     }
/*  766 */     if (!guard.isSatisfied()) {
/*  767 */       awaitUninterruptibly(guard, true);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean waitFor(Guard guard, long time, TimeUnit unit) throws InterruptedException {
/*  779 */     long timeoutNanos = toSafeNanos(time, unit);
/*  780 */     if ((((guard.monitor == this) ? 1 : 0) & this.lock.isHeldByCurrentThread()) == 0) {
/*  781 */       throw new IllegalMonitorStateException();
/*      */     }
/*  783 */     if (guard.isSatisfied()) {
/*  784 */       return true;
/*      */     }
/*  786 */     if (Thread.interrupted()) {
/*  787 */       throw new InterruptedException();
/*      */     }
/*  789 */     return awaitNanos(guard, timeoutNanos, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean waitForUninterruptibly(Guard guard, long time, TimeUnit unit) {
/*  799 */     long timeoutNanos = toSafeNanos(time, unit);
/*  800 */     if ((((guard.monitor == this) ? 1 : 0) & this.lock.isHeldByCurrentThread()) == 0) {
/*  801 */       throw new IllegalMonitorStateException();
/*      */     }
/*  803 */     if (guard.isSatisfied()) {
/*  804 */       return true;
/*      */     }
/*  806 */     boolean signalBeforeWaiting = true;
/*  807 */     long startTime = initNanoTime(timeoutNanos);
/*  808 */     boolean interrupted = Thread.interrupted();
/*      */     try {
/*  810 */       long remainingNanos = timeoutNanos; while (true) {
/*      */         try {
/*  812 */           return awaitNanos(guard, remainingNanos, signalBeforeWaiting);
/*  813 */         } catch (InterruptedException interrupt) {
/*  814 */           interrupted = true;
/*  815 */           if (guard.isSatisfied()) {
/*  816 */             return true;
/*      */           }
/*  818 */           signalBeforeWaiting = false;
/*  819 */           remainingNanos = remainingNanos(startTime, timeoutNanos);
/*      */         } 
/*      */       } 
/*      */     } finally {
/*  823 */       if (interrupted) {
/*  824 */         Thread.currentThread().interrupt();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void leave() {
/*  833 */     ReentrantLock lock = this.lock;
/*      */     
/*      */     try {
/*  836 */       if (lock.getHoldCount() == 1) {
/*  837 */         signalNextWaiter();
/*      */       }
/*      */     } finally {
/*  840 */       lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFair() {
/*  848 */     return this.fair;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOccupied() {
/*  856 */     return this.lock.isLocked();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOccupiedByCurrentThread() {
/*  864 */     return this.lock.isHeldByCurrentThread();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getOccupiedDepth() {
/*  872 */     return this.lock.getHoldCount();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getQueueLength() {
/*  882 */     return this.lock.getQueueLength();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasQueuedThreads() {
/*  892 */     return this.lock.hasQueuedThreads();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasQueuedThread(Thread thread) {
/*  902 */     return this.lock.hasQueuedThread(thread);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasWaiters(Guard guard) {
/*  912 */     return (getWaitQueueLength(guard) > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getWaitQueueLength(Guard guard) {
/*  922 */     if (guard.monitor != this) {
/*  923 */       throw new IllegalMonitorStateException();
/*      */     }
/*  925 */     this.lock.lock();
/*      */     try {
/*  927 */       return guard.waiterCount;
/*      */     } finally {
/*  929 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static long toSafeNanos(long time, TimeUnit unit) {
/*  939 */     long timeoutNanos = unit.toNanos(time);
/*  940 */     return (timeoutNanos <= 0L) ? 0L : ((timeoutNanos > 6917529027641081853L) ? 6917529027641081853L : timeoutNanos);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static long initNanoTime(long timeoutNanos) {
/*  950 */     if (timeoutNanos <= 0L) {
/*  951 */       return 0L;
/*      */     }
/*  953 */     long startTime = System.nanoTime();
/*  954 */     return (startTime == 0L) ? 1L : startTime;
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
/*      */ 
/*      */   
/*      */   private static long remainingNanos(long startTime, long timeoutNanos) {
/*  970 */     return (timeoutNanos <= 0L) ? 0L : (timeoutNanos - System.nanoTime() - startTime);
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
/*      */   @GuardedBy("lock")
/*      */   private void signalNextWaiter() {
/*  999 */     for (Guard guard = this.activeGuards; guard != null; guard = guard.next) {
/* 1000 */       if (isSatisfied(guard)) {
/* 1001 */         guard.condition.signal();
/*      */         break;
/*      */       } 
/*      */     } 
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
/*      */   @GuardedBy("lock")
/*      */   private boolean isSatisfied(Guard guard) {
/*      */     try {
/* 1032 */       return guard.isSatisfied();
/* 1033 */     } catch (Throwable throwable) {
/* 1034 */       signalAllWaiters();
/* 1035 */       throw Throwables.propagate(throwable);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void signalAllWaiters() {
/* 1044 */     for (Guard guard = this.activeGuards; guard != null; guard = guard.next) {
/* 1045 */       guard.condition.signalAll();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void beginWaitingFor(Guard guard) {
/* 1054 */     int waiters = guard.waiterCount++;
/* 1055 */     if (waiters == 0) {
/*      */       
/* 1057 */       guard.next = this.activeGuards;
/* 1058 */       this.activeGuards = guard;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void endWaitingFor(Guard guard) {
/* 1067 */     int waiters = --guard.waiterCount;
/* 1068 */     if (waiters == 0)
/*      */     {
/* 1070 */       for (Guard p = this.activeGuards, pred = null;; pred = p, p = p.next) {
/* 1071 */         if (p == guard) {
/* 1072 */           if (pred == null) {
/* 1073 */             this.activeGuards = p.next;
/*      */           } else {
/* 1075 */             pred.next = p.next;
/*      */           } 
/* 1077 */           p.next = null;
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void await(Guard guard, boolean signalBeforeWaiting) throws InterruptedException {
/* 1092 */     if (signalBeforeWaiting) {
/* 1093 */       signalNextWaiter();
/*      */     }
/* 1095 */     beginWaitingFor(guard);
/*      */     try {
/*      */       do {
/* 1098 */         guard.condition.await();
/* 1099 */       } while (!guard.isSatisfied());
/*      */     } finally {
/* 1101 */       endWaitingFor(guard);
/*      */     } 
/*      */   }
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void awaitUninterruptibly(Guard guard, boolean signalBeforeWaiting) {
/* 1107 */     if (signalBeforeWaiting) {
/* 1108 */       signalNextWaiter();
/*      */     }
/* 1110 */     beginWaitingFor(guard);
/*      */     try {
/*      */       do {
/* 1113 */         guard.condition.awaitUninterruptibly();
/* 1114 */       } while (!guard.isSatisfied());
/*      */     } finally {
/* 1116 */       endWaitingFor(guard);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private boolean awaitNanos(Guard guard, long nanos, boolean signalBeforeWaiting) throws InterruptedException {
/* 1126 */     boolean firstTime = true;
/*      */     
/*      */     try { while (true) {
/* 1129 */         if (nanos <= 0L) {
/* 1130 */           return false;
/*      */         }
/* 1132 */         if (firstTime) {
/* 1133 */           if (signalBeforeWaiting) {
/* 1134 */             signalNextWaiter();
/*      */           }
/* 1136 */           beginWaitingFor(guard);
/* 1137 */           firstTime = false;
/*      */         } 
/* 1139 */         nanos = guard.condition.awaitNanos(nanos);
/* 1140 */         if (guard.isSatisfied())
/* 1141 */           return true; 
/*      */       }  }
/* 1143 */     finally { if (!firstTime)
/* 1144 */         endWaitingFor(guard);  }
/*      */   
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\Monitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */