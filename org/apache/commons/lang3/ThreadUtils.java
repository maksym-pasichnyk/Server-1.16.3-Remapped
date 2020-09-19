/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ThreadUtils
/*     */ {
/*     */   public static Thread findThreadById(long threadId, ThreadGroup threadGroup) {
/*  53 */     if (threadGroup == null) {
/*  54 */       throw new IllegalArgumentException("The thread group must not be null");
/*     */     }
/*  56 */     Thread thread = findThreadById(threadId);
/*  57 */     if (thread != null && threadGroup.equals(thread.getThreadGroup())) {
/*  58 */       return thread;
/*     */     }
/*  60 */     return null;
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
/*     */   public static Thread findThreadById(long threadId, String threadGroupName) {
/*  78 */     if (threadGroupName == null) {
/*  79 */       throw new IllegalArgumentException("The thread group name must not be null");
/*     */     }
/*  81 */     Thread thread = findThreadById(threadId);
/*  82 */     if (thread != null && thread.getThreadGroup() != null && thread.getThreadGroup().getName().equals(threadGroupName)) {
/*  83 */       return thread;
/*     */     }
/*  85 */     return null;
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
/*     */   public static Collection<Thread> findThreadsByName(String threadName, ThreadGroup threadGroup) {
/* 103 */     return findThreads(threadGroup, false, new NamePredicate(threadName));
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
/*     */   public static Collection<Thread> findThreadsByName(String threadName, String threadGroupName) {
/* 121 */     if (threadName == null) {
/* 122 */       throw new IllegalArgumentException("The thread name must not be null");
/*     */     }
/* 124 */     if (threadGroupName == null) {
/* 125 */       throw new IllegalArgumentException("The thread group name must not be null");
/*     */     }
/*     */     
/* 128 */     Collection<ThreadGroup> threadGroups = findThreadGroups(new NamePredicate(threadGroupName));
/*     */     
/* 130 */     if (threadGroups.isEmpty()) {
/* 131 */       return Collections.emptyList();
/*     */     }
/*     */     
/* 134 */     Collection<Thread> result = new ArrayList<Thread>();
/* 135 */     NamePredicate threadNamePredicate = new NamePredicate(threadName);
/* 136 */     for (ThreadGroup group : threadGroups) {
/* 137 */       result.addAll(findThreads(group, false, threadNamePredicate));
/*     */     }
/* 139 */     return Collections.unmodifiableCollection(result);
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
/*     */   public static Collection<ThreadGroup> findThreadGroupsByName(String threadGroupName) {
/* 155 */     return findThreadGroups(new NamePredicate(threadGroupName));
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
/*     */   public static Collection<ThreadGroup> getAllThreadGroups() {
/* 169 */     return findThreadGroups(ALWAYS_TRUE_PREDICATE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ThreadGroup getSystemThreadGroup() {
/* 180 */     ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
/* 181 */     while (threadGroup.getParent() != null) {
/* 182 */       threadGroup = threadGroup.getParent();
/*     */     }
/* 184 */     return threadGroup;
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
/*     */   public static Collection<Thread> getAllThreads() {
/* 198 */     return findThreads(ALWAYS_TRUE_PREDICATE);
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
/*     */   public static Collection<Thread> findThreadsByName(String threadName) {
/* 214 */     return findThreads(new NamePredicate(threadName));
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
/*     */   public static Thread findThreadById(long threadId) {
/* 230 */     Collection<Thread> result = findThreads(new ThreadIdPredicate(threadId));
/* 231 */     return result.isEmpty() ? null : result.iterator().next();
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
/*     */   
/* 278 */   public static final AlwaysTruePredicate ALWAYS_TRUE_PREDICATE = new AlwaysTruePredicate();
/*     */   
/*     */   public static interface ThreadPredicate {
/*     */     boolean test(Thread param1Thread); }
/*     */   
/*     */   public static interface ThreadGroupPredicate {
/*     */     boolean test(ThreadGroup param1ThreadGroup); }
/*     */   
/*     */   private static final class AlwaysTruePredicate implements ThreadPredicate, ThreadGroupPredicate {
/*     */     private AlwaysTruePredicate() {}
/*     */     
/*     */     public boolean test(ThreadGroup threadGroup) {
/* 290 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(Thread thread) {
/* 295 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class NamePredicate
/*     */     implements ThreadPredicate, ThreadGroupPredicate
/*     */   {
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NamePredicate(String name) {
/* 314 */       if (name == null) {
/* 315 */         throw new IllegalArgumentException("The name must not be null");
/*     */       }
/* 317 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(ThreadGroup threadGroup) {
/* 322 */       return (threadGroup != null && threadGroup.getName().equals(this.name));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(Thread thread) {
/* 327 */       return (thread != null && thread.getName().equals(this.name));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ThreadIdPredicate
/*     */     implements ThreadPredicate
/*     */   {
/*     */     private final long threadId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ThreadIdPredicate(long threadId) {
/* 346 */       if (threadId <= 0L) {
/* 347 */         throw new IllegalArgumentException("The thread id must be greater than zero");
/*     */       }
/* 349 */       this.threadId = threadId;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(Thread thread) {
/* 354 */       return (thread != null && thread.getId() == this.threadId);
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
/*     */   public static Collection<Thread> findThreads(ThreadPredicate predicate) {
/* 371 */     return findThreads(getSystemThreadGroup(), true, predicate);
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
/*     */   public static Collection<ThreadGroup> findThreadGroups(ThreadGroupPredicate predicate) {
/* 386 */     return findThreadGroups(getSystemThreadGroup(), true, predicate);
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
/*     */   public static Collection<Thread> findThreads(ThreadGroup group, boolean recurse, ThreadPredicate predicate) {
/*     */     Thread[] threads;
/* 401 */     if (group == null) {
/* 402 */       throw new IllegalArgumentException("The group must not be null");
/*     */     }
/* 404 */     if (predicate == null) {
/* 405 */       throw new IllegalArgumentException("The predicate must not be null");
/*     */     }
/*     */     
/* 408 */     int count = group.activeCount();
/*     */     
/*     */     do {
/* 411 */       threads = new Thread[count + count / 2 + 1];
/* 412 */       count = group.enumerate(threads, recurse);
/*     */     }
/* 414 */     while (count >= threads.length);
/*     */     
/* 416 */     List<Thread> result = new ArrayList<Thread>(count);
/* 417 */     for (int i = 0; i < count; i++) {
/* 418 */       if (predicate.test(threads[i])) {
/* 419 */         result.add(threads[i]);
/*     */       }
/*     */     } 
/* 422 */     return Collections.unmodifiableCollection(result);
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
/*     */   public static Collection<ThreadGroup> findThreadGroups(ThreadGroup group, boolean recurse, ThreadGroupPredicate predicate) {
/*     */     ThreadGroup[] threadGroups;
/* 437 */     if (group == null) {
/* 438 */       throw new IllegalArgumentException("The group must not be null");
/*     */     }
/* 440 */     if (predicate == null) {
/* 441 */       throw new IllegalArgumentException("The predicate must not be null");
/*     */     }
/*     */     
/* 444 */     int count = group.activeGroupCount();
/*     */     
/*     */     do {
/* 447 */       threadGroups = new ThreadGroup[count + count / 2 + 1];
/* 448 */       count = group.enumerate(threadGroups, recurse);
/*     */     }
/* 450 */     while (count >= threadGroups.length);
/*     */     
/* 452 */     List<ThreadGroup> result = new ArrayList<ThreadGroup>(count);
/* 453 */     for (int i = 0; i < count; i++) {
/* 454 */       if (predicate.test(threadGroups[i])) {
/* 455 */         result.add(threadGroups[i]);
/*     */       }
/*     */     } 
/* 458 */     return Collections.unmodifiableCollection(result);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\ThreadUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */