/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ import com.google.common.collect.Queues;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Queue;
/*    */ import java.util.concurrent.ConcurrentLinkedQueue;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.IntStream;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public interface StrictQueue<T, F> {
/*    */   @Nullable
/*    */   F pop();
/*    */   
/*    */   boolean push(T paramT);
/*    */   
/*    */   boolean isEmpty();
/*    */   
/*    */   public static final class QueueStrictQueue<T>
/*    */     implements StrictQueue<T, T> {
/*    */     public QueueStrictQueue(Queue<T> debug1) {
/* 23 */       this.queue = debug1;
/*    */     }
/*    */     private final Queue<T> queue;
/*    */     
/*    */     @Nullable
/*    */     public T pop() {
/* 29 */       return this.queue.poll();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean push(T debug1) {
/* 34 */       return this.queue.add(debug1);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isEmpty() {
/* 39 */       return this.queue.isEmpty();
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class IntRunnable implements Runnable {
/*    */     private final int priority;
/*    */     private final Runnable task;
/*    */     
/*    */     public IntRunnable(int debug1, Runnable debug2) {
/* 48 */       this.priority = debug1;
/* 49 */       this.task = debug2;
/*    */     }
/*    */ 
/*    */     
/*    */     public void run() {
/* 54 */       this.task.run();
/*    */     }
/*    */     
/*    */     public int getPriority() {
/* 58 */       return this.priority;
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class FixedPriorityQueue implements StrictQueue<IntRunnable, Runnable> {
/*    */     private final List<Queue<Runnable>> queueList;
/*    */     
/*    */     public FixedPriorityQueue(int debug1) {
/* 66 */       this.queueList = (List<Queue<Runnable>>)IntStream.range(0, debug1).mapToObj(debug0 -> Queues.newConcurrentLinkedQueue()).collect(Collectors.toList());
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     @Nullable
/*    */     public Runnable pop() {
/* 73 */       for (Queue<Runnable> debug2 : this.queueList) {
/* 74 */         Runnable debug3 = debug2.poll();
/* 75 */         if (debug3 != null) {
/* 76 */           return debug3;
/*    */         }
/*    */       } 
/* 79 */       return null;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean push(StrictQueue.IntRunnable debug1) {
/* 84 */       int debug2 = debug1.getPriority();
/* 85 */       ((Queue<StrictQueue.IntRunnable>)this.queueList.get(debug2)).add(debug1);
/* 86 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isEmpty() {
/* 91 */       return this.queueList.stream().allMatch(Collection::isEmpty);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\thread\StrictQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */