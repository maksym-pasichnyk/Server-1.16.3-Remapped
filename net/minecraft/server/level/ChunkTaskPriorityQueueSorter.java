/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntConsumer;
/*     */ import java.util.function.IntSupplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.util.thread.ProcessorHandle;
/*     */ import net.minecraft.util.thread.ProcessorMailbox;
/*     */ import net.minecraft.util.thread.StrictQueue;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ChunkTaskPriorityQueueSorter
/*     */   implements AutoCloseable, ChunkHolder.LevelChangeListener
/*     */ {
/*  29 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   private final Map<ProcessorHandle<?>, ChunkTaskPriorityQueue<? extends Function<ProcessorHandle<Unit>, ?>>> queues;
/*     */   private final Set<ProcessorHandle<?>> sleeping;
/*     */   private final ProcessorMailbox<StrictQueue.IntRunnable> mailbox;
/*     */   
/*     */   public ChunkTaskPriorityQueueSorter(List<ProcessorHandle<?>> debug1, Executor debug2, int debug3) {
/*  35 */     this.queues = (Map<ProcessorHandle<?>, ChunkTaskPriorityQueue<? extends Function<ProcessorHandle<Unit>, ?>>>)debug1.stream().collect(Collectors.toMap(Function.identity(), debug1 -> new ChunkTaskPriorityQueue(debug1.name() + "_queue", debug0)));
/*  36 */     this.sleeping = Sets.newHashSet(debug1);
/*  37 */     this.mailbox = new ProcessorMailbox((StrictQueue)new StrictQueue.FixedPriorityQueue(4), debug2, "sorter");
/*     */   }
/*     */   
/*     */   public static final class Message<T> {
/*     */     private final Function<ProcessorHandle<Unit>, T> task;
/*     */     private final long pos;
/*     */     private final IntSupplier level;
/*     */     
/*     */     private Message(Function<ProcessorHandle<Unit>, T> debug1, long debug2, IntSupplier debug4) {
/*  46 */       this.task = debug1;
/*  47 */       this.pos = debug2;
/*  48 */       this.level = debug4;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Message<Runnable> message(Runnable debug0, long debug1, IntSupplier debug3) {
/*  57 */     return new Message<>(debug1 -> (), debug1, debug3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Message<Runnable> message(ChunkHolder debug0, Runnable debug1) {
/*  64 */     return message(debug1, debug0.getPos().toLong(), debug0::getQueueLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class Release
/*     */   {
/*     */     private final Runnable task;
/*     */     
/*     */     private final long pos;
/*     */     
/*     */     private final boolean clearQueue;
/*     */     
/*     */     private Release(Runnable debug1, long debug2, boolean debug4) {
/*  77 */       this.task = debug1;
/*  78 */       this.pos = debug2;
/*  79 */       this.clearQueue = debug4;
/*     */     }
/*     */   }
/*     */   
/*     */   public static Release release(Runnable debug0, long debug1, boolean debug3) {
/*  84 */     return new Release(debug0, debug1, debug3);
/*     */   }
/*     */   
/*     */   public <T> ProcessorHandle<Message<T>> getProcessor(ProcessorHandle<T> debug1, boolean debug2) {
/*  88 */     return this.mailbox.ask(debug3 -> new StrictQueue.IntRunnable(0, ()))
/*     */ 
/*     */ 
/*     */       
/*  92 */       .join();
/*     */   }
/*     */   
/*     */   public ProcessorHandle<Release> getReleaseProcessor(ProcessorHandle<Runnable> debug1) {
/*  96 */     return this.mailbox.ask(debug2 -> new StrictQueue.IntRunnable(0, ()))
/*     */       
/*  98 */       .join();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLevelChange(ChunkPos debug1, IntSupplier debug2, int debug3, IntConsumer debug4) {
/* 103 */     this.mailbox.tell(new StrictQueue.IntRunnable(0, () -> {
/*     */             int debug5 = debug1.getAsInt();
/*     */             this.queues.values().forEach(());
/*     */             debug4.accept(debug3);
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <T> void release(ProcessorHandle<T> debug1, long debug2, Runnable debug4, boolean debug5) {
/* 114 */     this.mailbox.tell(new StrictQueue.IntRunnable(1, () -> {
/*     */             ChunkTaskPriorityQueue<Function<ProcessorHandle<Unit>, T>> debug6 = getQueue(debug1);
/*     */             debug6.release(debug2, debug4);
/*     */             if (this.sleeping.remove(debug1)) {
/*     */               pollTask(debug6, debug1);
/*     */             }
/*     */             debug5.run();
/*     */           }));
/*     */   }
/*     */   
/*     */   private <T> void submit(ProcessorHandle<T> debug1, Function<ProcessorHandle<Unit>, T> debug2, long debug3, IntSupplier debug5, boolean debug6) {
/* 125 */     this.mailbox.tell(new StrictQueue.IntRunnable(2, () -> {
/*     */             ChunkTaskPriorityQueue<Function<ProcessorHandle<Unit>, T>> debug7 = getQueue(debug1);
/*     */             int debug8 = debug2.getAsInt();
/*     */             debug7.submit(Optional.of(debug5), debug3, debug8);
/*     */             if (debug6) {
/*     */               debug7.submit(Optional.empty(), debug3, debug8);
/*     */             }
/*     */             if (this.sleeping.remove(debug1)) {
/*     */               pollTask(debug7, debug1);
/*     */             }
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <T> void pollTask(ChunkTaskPriorityQueue<Function<ProcessorHandle<Unit>, T>> debug1, ProcessorHandle<T> debug2) {
/* 142 */     this.mailbox.tell(new StrictQueue.IntRunnable(3, () -> {
/*     */             Stream<Either<Function<ProcessorHandle<Unit>, T>, Runnable>> debug3 = debug1.pop();
/*     */             if (debug3 == null) {
/*     */               this.sleeping.add(debug2);
/*     */             } else {
/*     */               Util.sequence((List)debug3.map(()).collect(Collectors.toList())).thenAccept(());
/*     */             } 
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <T> ChunkTaskPriorityQueue<Function<ProcessorHandle<Unit>, T>> getQueue(ProcessorHandle<T> debug1) {
/* 157 */     ChunkTaskPriorityQueue<? extends Function<ProcessorHandle<Unit>, ?>> debug2 = this.queues.get(debug1);
/* 158 */     if (debug2 == null) {
/* 159 */       throw (IllegalArgumentException)Util.pauseInIde(new IllegalArgumentException("No queue for: " + debug1));
/*     */     }
/* 161 */     return (ChunkTaskPriorityQueue)debug2;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public String getDebugStatus() {
/* 166 */     return (String)this.queues.entrySet().stream()
/* 167 */       .map(debug0 -> ((ProcessorHandle)debug0.getKey()).name() + "=[" + (String)((ChunkTaskPriorityQueue)debug0.getValue()).getAcquired().stream().map(()).collect(Collectors.joining(",")) + "]")
/* 168 */       .collect(Collectors.joining(",")) + ", s=" + this.sleeping.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 173 */     this.queues.keySet().forEach(ProcessorHandle::close);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\ChunkTaskPriorityQueueSorter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */