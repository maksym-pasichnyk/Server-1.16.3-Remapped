/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongCollection;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ public class ChunkTaskPriorityQueue<T> {
/*  19 */   public static final int PRIORITY_LEVEL_COUNT = ChunkMap.MAX_CHUNK_DISTANCE + 2;
/*  20 */   private final List<Long2ObjectLinkedOpenHashMap<List<Optional<T>>>> taskQueue = (List<Long2ObjectLinkedOpenHashMap<List<Optional<T>>>>)IntStream.range(0, PRIORITY_LEVEL_COUNT).mapToObj(debug0 -> new Long2ObjectLinkedOpenHashMap()).collect(Collectors.toList());
/*  21 */   private volatile int firstQueue = PRIORITY_LEVEL_COUNT;
/*     */   
/*     */   private final String name;
/*     */   
/*  25 */   private final LongSet acquired = (LongSet)new LongOpenHashSet();
/*     */   private final int maxTasks;
/*     */   
/*     */   public ChunkTaskPriorityQueue(String debug1, int debug2) {
/*  29 */     this.name = debug1;
/*  30 */     this.maxTasks = debug2;
/*     */   }
/*     */   
/*     */   protected void resortChunkTasks(int debug1, ChunkPos debug2, int debug3) {
/*  34 */     if (debug1 >= PRIORITY_LEVEL_COUNT) {
/*     */       return;
/*     */     }
/*  37 */     Long2ObjectLinkedOpenHashMap<List<Optional<T>>> debug4 = this.taskQueue.get(debug1);
/*  38 */     List<Optional<T>> debug5 = (List<Optional<T>>)debug4.remove(debug2.toLong());
/*  39 */     if (debug1 == this.firstQueue) {
/*  40 */       while (this.firstQueue < PRIORITY_LEVEL_COUNT && ((Long2ObjectLinkedOpenHashMap)this.taskQueue.get(this.firstQueue)).isEmpty()) {
/*  41 */         this.firstQueue++;
/*     */       }
/*     */     }
/*  44 */     if (debug5 != null && !debug5.isEmpty()) {
/*  45 */       ((List<Optional<T>>)((Long2ObjectLinkedOpenHashMap)this.taskQueue.get(debug3)).computeIfAbsent(debug2.toLong(), debug0 -> Lists.newArrayList())).addAll(debug5);
/*  46 */       this.firstQueue = Math.min(this.firstQueue, debug3);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void submit(Optional<T> debug1, long debug2, int debug4) {
/*  51 */     ((List<Optional<T>>)((Long2ObjectLinkedOpenHashMap)this.taskQueue.get(debug4)).computeIfAbsent(debug2, debug0 -> Lists.newArrayList())).add(debug1);
/*  52 */     this.firstQueue = Math.min(this.firstQueue, debug4);
/*     */   }
/*     */   
/*     */   protected void release(long debug1, boolean debug3) {
/*  56 */     for (Long2ObjectLinkedOpenHashMap<List<Optional<T>>> debug5 : this.taskQueue) {
/*  57 */       List<Optional<T>> debug6 = (List<Optional<T>>)debug5.get(debug1);
/*  58 */       if (debug6 == null) {
/*     */         continue;
/*     */       }
/*  61 */       if (debug3) {
/*  62 */         debug6.clear();
/*     */       } else {
/*  64 */         debug6.removeIf(debug0 -> !debug0.isPresent());
/*     */       } 
/*  66 */       if (debug6.isEmpty()) {
/*  67 */         debug5.remove(debug1);
/*     */       }
/*     */     } 
/*  70 */     while (this.firstQueue < PRIORITY_LEVEL_COUNT && ((Long2ObjectLinkedOpenHashMap)this.taskQueue.get(this.firstQueue)).isEmpty()) {
/*  71 */       this.firstQueue++;
/*     */     }
/*  73 */     this.acquired.remove(debug1);
/*     */   }
/*     */   
/*     */   private Runnable acquire(long debug1) {
/*  77 */     return () -> this.acquired.add(debug1);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Stream<Either<T, Runnable>> pop() {
/*  82 */     if (this.acquired.size() >= this.maxTasks) {
/*  83 */       return null;
/*     */     }
/*  85 */     if (this.firstQueue < PRIORITY_LEVEL_COUNT) {
/*  86 */       int debug1 = this.firstQueue;
/*  87 */       Long2ObjectLinkedOpenHashMap<List<Optional<T>>> debug2 = this.taskQueue.get(debug1);
/*  88 */       long debug3 = debug2.firstLongKey();
/*  89 */       List<Optional<T>> debug5 = (List<Optional<T>>)debug2.removeFirst();
/*  90 */       while (this.firstQueue < PRIORITY_LEVEL_COUNT && ((Long2ObjectLinkedOpenHashMap)this.taskQueue.get(this.firstQueue)).isEmpty()) {
/*  91 */         this.firstQueue++;
/*     */       }
/*  93 */       return debug5.stream().map(debug3 -> (Either)debug3.map(Either::left).orElseGet(()));
/*     */     } 
/*  95 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 100 */     return this.name + " " + this.firstQueue + "...";
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   LongSet getAcquired() {
/* 105 */     return (LongSet)new LongOpenHashSet((LongCollection)this.acquired);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\ChunkTaskPriorityQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */