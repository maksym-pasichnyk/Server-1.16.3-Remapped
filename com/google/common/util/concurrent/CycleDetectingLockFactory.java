/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.MapMaker;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.annotation.concurrent.ThreadSafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @CanIgnoreReturnValue
/*     */ @ThreadSafe
/*     */ @GwtIncompatible
/*     */ public class CycleDetectingLockFactory
/*     */ {
/*     */   @Beta
/*     */   public enum Policies
/*     */     implements Policy
/*     */   {
/* 201 */     THROW
/*     */     {
/*     */       public void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException e) {
/* 204 */         throw e;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     WARN
/*     */     {
/*     */       public void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException e) {
/* 216 */         CycleDetectingLockFactory.logger.log(Level.SEVERE, "Detected potential deadlock", e);
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 228 */     DISABLED
/*     */     {
/*     */       public void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException e) {}
/*     */     };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CycleDetectingLockFactory newInstance(Policy policy) {
/* 238 */     return new CycleDetectingLockFactory(policy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReentrantLock newReentrantLock(String lockName) {
/* 245 */     return newReentrantLock(lockName, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReentrantLock newReentrantLock(String lockName, boolean fair) {
/* 253 */     return (this.policy == Policies.DISABLED) ? new ReentrantLock(fair) : new CycleDetectingReentrantLock(new LockGraphNode(lockName), fair);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReentrantReadWriteLock newReentrantReadWriteLock(String lockName) {
/* 262 */     return newReentrantReadWriteLock(lockName, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReentrantReadWriteLock newReentrantReadWriteLock(String lockName, boolean fair) {
/* 271 */     return (this.policy == Policies.DISABLED) ? new ReentrantReadWriteLock(fair) : new CycleDetectingReentrantReadWriteLock(new LockGraphNode(lockName), fair);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 278 */   private static final ConcurrentMap<Class<? extends Enum>, Map<? extends Enum, LockGraphNode>> lockGraphNodesPerType = (new MapMaker()).weakKeys().makeMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Enum<E>> WithExplicitOrdering<E> newInstanceWithExplicitOrdering(Class<E> enumClass, Policy policy) {
/* 287 */     Preconditions.checkNotNull(enumClass);
/* 288 */     Preconditions.checkNotNull(policy);
/*     */     
/* 290 */     Map<E, LockGraphNode> lockGraphNodes = (Map)getOrCreateNodes(enumClass);
/* 291 */     return new WithExplicitOrdering<>(policy, lockGraphNodes);
/*     */   }
/*     */   
/*     */   private static Map<? extends Enum, LockGraphNode> getOrCreateNodes(Class<? extends Enum> clazz) {
/* 295 */     Map<? extends Enum, LockGraphNode> existing = lockGraphNodesPerType.get(clazz);
/* 296 */     if (existing != null) {
/* 297 */       return existing;
/*     */     }
/* 299 */     Map<? extends Enum, LockGraphNode> created = createNodes(clazz);
/* 300 */     existing = lockGraphNodesPerType.putIfAbsent(clazz, created);
/* 301 */     return (Map<? extends Enum, LockGraphNode>)MoreObjects.firstNonNull(existing, created);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static <E extends Enum<E>> Map<E, LockGraphNode> createNodes(Class<E> clazz) {
/* 312 */     EnumMap<E, LockGraphNode> map = Maps.newEnumMap(clazz);
/* 313 */     Enum[] arrayOfEnum = (Enum[])clazz.getEnumConstants();
/* 314 */     int numKeys = arrayOfEnum.length;
/* 315 */     ArrayList<LockGraphNode> nodes = Lists.newArrayListWithCapacity(numKeys);
/*     */     
/* 317 */     for (Enum<?> enum_ : arrayOfEnum) {
/* 318 */       LockGraphNode node = new LockGraphNode(getLockName(enum_));
/* 319 */       nodes.add(node);
/* 320 */       map.put((E)enum_, node);
/*     */     } 
/*     */     int i;
/* 323 */     for (i = 1; i < numKeys; i++) {
/* 324 */       ((LockGraphNode)nodes.get(i)).checkAcquiredLocks(Policies.THROW, nodes.subList(0, i));
/*     */     }
/*     */     
/* 327 */     for (i = 0; i < numKeys - 1; i++) {
/* 328 */       ((LockGraphNode)nodes.get(i)).checkAcquiredLocks(Policies.DISABLED, nodes.subList(i + 1, numKeys));
/*     */     }
/* 330 */     return Collections.unmodifiableMap(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getLockName(Enum<?> rank) {
/* 338 */     return rank.getDeclaringClass().getSimpleName() + "." + rank.name();
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
/*     */   @Beta
/*     */   public static final class WithExplicitOrdering<E extends Enum<E>>
/*     */     extends CycleDetectingLockFactory
/*     */   {
/*     */     private final Map<E, CycleDetectingLockFactory.LockGraphNode> lockGraphNodes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @VisibleForTesting
/*     */     WithExplicitOrdering(CycleDetectingLockFactory.Policy policy, Map<E, CycleDetectingLockFactory.LockGraphNode> lockGraphNodes) {
/* 406 */       super(policy);
/* 407 */       this.lockGraphNodes = lockGraphNodes;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ReentrantLock newReentrantLock(E rank) {
/* 414 */       return newReentrantLock(rank, false);
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
/*     */     public ReentrantLock newReentrantLock(E rank, boolean fair) {
/* 426 */       return (this.policy == CycleDetectingLockFactory.Policies.DISABLED) ? new ReentrantLock(fair) : new CycleDetectingLockFactory.CycleDetectingReentrantLock(this.lockGraphNodes
/*     */           
/* 428 */           .get(rank), fair);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ReentrantReadWriteLock newReentrantReadWriteLock(E rank) {
/* 435 */       return newReentrantReadWriteLock(rank, false);
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
/*     */     public ReentrantReadWriteLock newReentrantReadWriteLock(E rank, boolean fair) {
/* 447 */       return (this.policy == CycleDetectingLockFactory.Policies.DISABLED) ? new ReentrantReadWriteLock(fair) : new CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock(this.lockGraphNodes
/*     */           
/* 449 */           .get(rank), fair);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 455 */   private static final Logger logger = Logger.getLogger(CycleDetectingLockFactory.class.getName());
/*     */   
/*     */   final Policy policy;
/*     */   
/*     */   private CycleDetectingLockFactory(Policy policy) {
/* 460 */     this.policy = (Policy)Preconditions.checkNotNull(policy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 469 */   private static final ThreadLocal<ArrayList<LockGraphNode>> acquiredLocks = new ThreadLocal<ArrayList<LockGraphNode>>()
/*     */     {
/*     */       protected ArrayList<CycleDetectingLockFactory.LockGraphNode> initialValue()
/*     */       {
/* 473 */         return Lists.newArrayListWithCapacity(3);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ExampleStackTrace
/*     */     extends IllegalStateException
/*     */   {
/* 492 */     static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
/*     */ 
/*     */     
/* 495 */     static final Set<String> EXCLUDED_CLASS_NAMES = (Set<String>)ImmutableSet.of(CycleDetectingLockFactory.class
/* 496 */         .getName(), ExampleStackTrace.class
/* 497 */         .getName(), CycleDetectingLockFactory.LockGraphNode.class
/* 498 */         .getName());
/*     */     
/*     */     ExampleStackTrace(CycleDetectingLockFactory.LockGraphNode node1, CycleDetectingLockFactory.LockGraphNode node2) {
/* 501 */       super(node1.getLockName() + " -> " + node2.getLockName());
/* 502 */       StackTraceElement[] origStackTrace = getStackTrace();
/* 503 */       for (int i = 0, n = origStackTrace.length; i < n; i++) {
/* 504 */         if (CycleDetectingLockFactory.WithExplicitOrdering.class.getName().equals(origStackTrace[i].getClassName())) {
/*     */           
/* 506 */           setStackTrace(EMPTY_STACK_TRACE);
/*     */           break;
/*     */         } 
/* 509 */         if (!EXCLUDED_CLASS_NAMES.contains(origStackTrace[i].getClassName())) {
/* 510 */           setStackTrace(Arrays.<StackTraceElement>copyOfRange(origStackTrace, i, n));
/*     */           break;
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
/*     */   @Beta
/*     */   public static final class PotentialDeadlockException
/*     */     extends ExampleStackTrace
/*     */   {
/*     */     private final CycleDetectingLockFactory.ExampleStackTrace conflictingStackTrace;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private PotentialDeadlockException(CycleDetectingLockFactory.LockGraphNode node1, CycleDetectingLockFactory.LockGraphNode node2, CycleDetectingLockFactory.ExampleStackTrace conflictingStackTrace) {
/* 544 */       super(node1, node2);
/* 545 */       this.conflictingStackTrace = conflictingStackTrace;
/* 546 */       initCause(conflictingStackTrace);
/*     */     }
/*     */     
/*     */     public CycleDetectingLockFactory.ExampleStackTrace getConflictingStackTrace() {
/* 550 */       return this.conflictingStackTrace;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getMessage() {
/* 559 */       StringBuilder message = new StringBuilder(super.getMessage());
/* 560 */       for (Throwable t = this.conflictingStackTrace; t != null; t = t.getCause()) {
/* 561 */         message.append(", ").append(t.getMessage());
/*     */       }
/* 563 */       return message.toString();
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
/*     */   private static class LockGraphNode
/*     */   {
/* 591 */     final Map<LockGraphNode, CycleDetectingLockFactory.ExampleStackTrace> allowedPriorLocks = (new MapMaker())
/* 592 */       .weakKeys().makeMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 598 */     final Map<LockGraphNode, CycleDetectingLockFactory.PotentialDeadlockException> disallowedPriorLocks = (new MapMaker())
/* 599 */       .weakKeys().makeMap();
/*     */     
/*     */     final String lockName;
/*     */     
/*     */     LockGraphNode(String lockName) {
/* 604 */       this.lockName = (String)Preconditions.checkNotNull(lockName);
/*     */     }
/*     */     
/*     */     String getLockName() {
/* 608 */       return this.lockName;
/*     */     }
/*     */     
/*     */     void checkAcquiredLocks(CycleDetectingLockFactory.Policy policy, List<LockGraphNode> acquiredLocks) {
/* 612 */       for (int i = 0, size = acquiredLocks.size(); i < size; i++) {
/* 613 */         checkAcquiredLock(policy, acquiredLocks.get(i));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void checkAcquiredLock(CycleDetectingLockFactory.Policy policy, LockGraphNode acquiredLock) {
/* 633 */       Preconditions.checkState((this != acquiredLock), "Attempted to acquire multiple locks with the same rank %s", acquiredLock
/*     */ 
/*     */           
/* 636 */           .getLockName());
/*     */       
/* 638 */       if (this.allowedPriorLocks.containsKey(acquiredLock)) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 644 */       CycleDetectingLockFactory.PotentialDeadlockException previousDeadlockException = this.disallowedPriorLocks.get(acquiredLock);
/* 645 */       if (previousDeadlockException != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 651 */         CycleDetectingLockFactory.PotentialDeadlockException exception = new CycleDetectingLockFactory.PotentialDeadlockException(acquiredLock, this, previousDeadlockException.getConflictingStackTrace());
/* 652 */         policy.handlePotentialDeadlock(exception);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 657 */       Set<LockGraphNode> seen = Sets.newIdentityHashSet();
/* 658 */       CycleDetectingLockFactory.ExampleStackTrace path = acquiredLock.findPathTo(this, seen);
/*     */       
/* 660 */       if (path == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 669 */         this.allowedPriorLocks.put(acquiredLock, new CycleDetectingLockFactory.ExampleStackTrace(acquiredLock, this));
/*     */       }
/*     */       else {
/*     */         
/* 673 */         CycleDetectingLockFactory.PotentialDeadlockException exception = new CycleDetectingLockFactory.PotentialDeadlockException(acquiredLock, this, path);
/*     */         
/* 675 */         this.disallowedPriorLocks.put(acquiredLock, exception);
/* 676 */         policy.handlePotentialDeadlock(exception);
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
/*     */     @Nullable
/*     */     private CycleDetectingLockFactory.ExampleStackTrace findPathTo(LockGraphNode node, Set<LockGraphNode> seen) {
/* 689 */       if (!seen.add(this)) {
/* 690 */         return null;
/*     */       }
/* 692 */       CycleDetectingLockFactory.ExampleStackTrace found = this.allowedPriorLocks.get(node);
/* 693 */       if (found != null) {
/* 694 */         return found;
/*     */       }
/*     */       
/* 697 */       for (Map.Entry<LockGraphNode, CycleDetectingLockFactory.ExampleStackTrace> entry : this.allowedPriorLocks.entrySet()) {
/* 698 */         LockGraphNode preAcquiredLock = entry.getKey();
/* 699 */         found = preAcquiredLock.findPathTo(node, seen);
/* 700 */         if (found != null) {
/*     */ 
/*     */ 
/*     */           
/* 704 */           CycleDetectingLockFactory.ExampleStackTrace path = new CycleDetectingLockFactory.ExampleStackTrace(preAcquiredLock, this);
/* 705 */           path.setStackTrace(((CycleDetectingLockFactory.ExampleStackTrace)entry.getValue()).getStackTrace());
/* 706 */           path.initCause(found);
/* 707 */           return path;
/*     */         } 
/*     */       } 
/* 710 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void aboutToAcquire(CycleDetectingLock lock) {
/* 718 */     if (!lock.isAcquiredByCurrentThread()) {
/* 719 */       ArrayList<LockGraphNode> acquiredLockList = acquiredLocks.get();
/* 720 */       LockGraphNode node = lock.getLockGraphNode();
/* 721 */       node.checkAcquiredLocks(this.policy, acquiredLockList);
/* 722 */       acquiredLockList.add(node);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void lockStateChanged(CycleDetectingLock lock) {
/* 732 */     if (!lock.isAcquiredByCurrentThread()) {
/* 733 */       ArrayList<LockGraphNode> acquiredLockList = acquiredLocks.get();
/* 734 */       LockGraphNode node = lock.getLockGraphNode();
/*     */ 
/*     */       
/* 737 */       for (int i = acquiredLockList.size() - 1; i >= 0; i--) {
/* 738 */         if (acquiredLockList.get(i) == node) {
/* 739 */           acquiredLockList.remove(i);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   final class CycleDetectingReentrantLock
/*     */     extends ReentrantLock implements CycleDetectingLock {
/*     */     private final CycleDetectingLockFactory.LockGraphNode lockGraphNode;
/*     */     
/*     */     private CycleDetectingReentrantLock(CycleDetectingLockFactory.LockGraphNode lockGraphNode, boolean fair) {
/* 751 */       super(fair);
/* 752 */       this.lockGraphNode = (CycleDetectingLockFactory.LockGraphNode)Preconditions.checkNotNull(lockGraphNode);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CycleDetectingLockFactory.LockGraphNode getLockGraphNode() {
/* 759 */       return this.lockGraphNode;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isAcquiredByCurrentThread() {
/* 764 */       return isHeldByCurrentThread();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void lock() {
/* 771 */       CycleDetectingLockFactory.this.aboutToAcquire(this);
/*     */       try {
/* 773 */         super.lock();
/*     */       } finally {
/* 775 */         CycleDetectingLockFactory.lockStateChanged(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void lockInterruptibly() throws InterruptedException {
/* 781 */       CycleDetectingLockFactory.this.aboutToAcquire(this);
/*     */       try {
/* 783 */         super.lockInterruptibly();
/*     */       } finally {
/* 785 */         CycleDetectingLockFactory.lockStateChanged(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryLock() {
/* 791 */       CycleDetectingLockFactory.this.aboutToAcquire(this);
/*     */       try {
/* 793 */         return super.tryLock();
/*     */       } finally {
/* 795 */         CycleDetectingLockFactory.lockStateChanged(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
/* 801 */       CycleDetectingLockFactory.this.aboutToAcquire(this);
/*     */       try {
/* 803 */         return super.tryLock(timeout, unit);
/*     */       } finally {
/* 805 */         CycleDetectingLockFactory.lockStateChanged(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void unlock() {
/*     */       try {
/* 812 */         super.unlock();
/*     */       } finally {
/* 814 */         CycleDetectingLockFactory.lockStateChanged(this);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   final class CycleDetectingReentrantReadWriteLock
/*     */     extends ReentrantReadWriteLock
/*     */     implements CycleDetectingLock
/*     */   {
/*     */     private final CycleDetectingLockFactory.CycleDetectingReentrantReadLock readLock;
/*     */     
/*     */     private final CycleDetectingLockFactory.CycleDetectingReentrantWriteLock writeLock;
/*     */     
/*     */     private final CycleDetectingLockFactory.LockGraphNode lockGraphNode;
/*     */ 
/*     */     
/*     */     private CycleDetectingReentrantReadWriteLock(CycleDetectingLockFactory.LockGraphNode lockGraphNode, boolean fair) {
/* 832 */       super(fair);
/* 833 */       this.readLock = new CycleDetectingLockFactory.CycleDetectingReentrantReadLock(this);
/* 834 */       this.writeLock = new CycleDetectingLockFactory.CycleDetectingReentrantWriteLock(this);
/* 835 */       this.lockGraphNode = (CycleDetectingLockFactory.LockGraphNode)Preconditions.checkNotNull(lockGraphNode);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ReentrantReadWriteLock.ReadLock readLock() {
/* 842 */       return this.readLock;
/*     */     }
/*     */ 
/*     */     
/*     */     public ReentrantReadWriteLock.WriteLock writeLock() {
/* 847 */       return this.writeLock;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CycleDetectingLockFactory.LockGraphNode getLockGraphNode() {
/* 854 */       return this.lockGraphNode;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isAcquiredByCurrentThread() {
/* 859 */       return (isWriteLockedByCurrentThread() || getReadHoldCount() > 0);
/*     */     }
/*     */   }
/*     */   
/*     */   private class CycleDetectingReentrantReadLock
/*     */     extends ReentrantReadWriteLock.ReadLock {
/*     */     @Weak
/*     */     final CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock;
/*     */     
/*     */     CycleDetectingReentrantReadLock(CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock) {
/* 869 */       this.readWriteLock = readWriteLock;
/*     */     }
/*     */ 
/*     */     
/*     */     public void lock() {
/* 874 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 876 */         super.lock();
/*     */       } finally {
/* 878 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void lockInterruptibly() throws InterruptedException {
/* 884 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 886 */         super.lockInterruptibly();
/*     */       } finally {
/* 888 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryLock() {
/* 894 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 896 */         return super.tryLock();
/*     */       } finally {
/* 898 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
/* 904 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 906 */         return super.tryLock(timeout, unit);
/*     */       } finally {
/* 908 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void unlock() {
/*     */       try {
/* 915 */         super.unlock();
/*     */       } finally {
/* 917 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private class CycleDetectingReentrantWriteLock
/*     */     extends ReentrantReadWriteLock.WriteLock {
/*     */     @Weak
/*     */     final CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock;
/*     */     
/*     */     CycleDetectingReentrantWriteLock(CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock) {
/* 928 */       this.readWriteLock = readWriteLock;
/*     */     }
/*     */ 
/*     */     
/*     */     public void lock() {
/* 933 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 935 */         super.lock();
/*     */       } finally {
/* 937 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void lockInterruptibly() throws InterruptedException {
/* 943 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 945 */         super.lockInterruptibly();
/*     */       } finally {
/* 947 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryLock() {
/* 953 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 955 */         return super.tryLock();
/*     */       } finally {
/* 957 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
/* 963 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 965 */         return super.tryLock(timeout, unit);
/*     */       } finally {
/* 967 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void unlock() {
/*     */       try {
/* 974 */         super.unlock();
/*     */       } finally {
/* 976 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static interface CycleDetectingLock {
/*     */     CycleDetectingLockFactory.LockGraphNode getLockGraphNode();
/*     */     
/*     */     boolean isAcquiredByCurrentThread();
/*     */   }
/*     */   
/*     */   @Beta
/*     */   @ThreadSafe
/*     */   public static interface Policy {
/*     */     void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException param1PotentialDeadlockException);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\CycleDetectingLockFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */