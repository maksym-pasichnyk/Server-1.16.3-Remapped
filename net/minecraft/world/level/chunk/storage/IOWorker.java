/*     */ package net.minecraft.world.level.chunk.storage;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionException;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.util.thread.ProcessorHandle;
/*     */ import net.minecraft.util.thread.ProcessorMailbox;
/*     */ import net.minecraft.util.thread.StrictQueue;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IOWorker
/*     */   implements AutoCloseable
/*     */ {
/*  40 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   enum Priority {
/*  43 */     HIGH, LOW;
/*     */   }
/*     */   
/*     */   static class PendingStore {
/*     */     private CompoundTag data;
/*  48 */     private final CompletableFuture<Void> result = new CompletableFuture<>();
/*     */     
/*     */     public PendingStore(CompoundTag debug1) {
/*  51 */       this.data = debug1;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  56 */   private final AtomicBoolean shutdownRequested = new AtomicBoolean();
/*     */   
/*     */   private final ProcessorMailbox<StrictQueue.IntRunnable> mailbox;
/*     */   
/*     */   private final RegionFileStorage storage;
/*  61 */   private final Map<ChunkPos, PendingStore> pendingWrites = Maps.newLinkedHashMap();
/*     */   
/*     */   protected IOWorker(File debug1, boolean debug2, String debug3) {
/*  64 */     this.storage = new RegionFileStorage(debug1, debug2);
/*  65 */     this.mailbox = new ProcessorMailbox((StrictQueue)new StrictQueue.FixedPriorityQueue((Priority.values()).length), Util.ioPool(), "IOWorker-" + debug3);
/*     */   }
/*     */   
/*     */   public CompletableFuture<Void> store(ChunkPos debug1, CompoundTag debug2) {
/*  69 */     return submitTask(() -> {
/*     */           PendingStore debug3 = this.pendingWrites.computeIfAbsent(debug1, ());
/*     */           debug3.data = debug2;
/*     */           return Either.left(debug3.result);
/*  73 */         }).thenCompose(Function.identity());
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public CompoundTag load(ChunkPos debug1) throws IOException {
/*  79 */     CompletableFuture<CompoundTag> debug2 = submitTask(() -> {
/*     */           PendingStore debug2 = this.pendingWrites.get(debug1);
/*     */           if (debug2 != null) {
/*     */             return Either.left(debug2.data);
/*     */           }
/*     */           try {
/*     */             CompoundTag debug3 = this.storage.read(debug1);
/*     */             return Either.left(debug3);
/*  87 */           } catch (Exception debug3) {
/*     */             LOGGER.warn("Failed to read chunk {}", debug1, debug3);
/*     */             
/*     */             return Either.right(debug3);
/*     */           } 
/*     */         });
/*     */     
/*     */     try {
/*  95 */       return debug2.join();
/*  96 */     } catch (CompletionException debug3) {
/*  97 */       if (debug3.getCause() instanceof IOException) {
/*  98 */         throw (IOException)debug3.getCause();
/*     */       }
/* 100 */       throw debug3;
/*     */     } 
/*     */   }
/*     */   
/*     */   public CompletableFuture<Void> synchronize() {
/* 105 */     CompletableFuture<Void> debug1 = submitTask(() -> Either.left(CompletableFuture.allOf((CompletableFuture<?>[])this.pendingWrites.values().stream().map(()).toArray(())))).thenCompose(Function.identity());
/* 106 */     return debug1.thenCompose(debug1 -> submitTask(()));
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
/*     */   private <T> CompletableFuture<T> submitTask(Supplier<Either<T, Exception>> debug1) {
/* 118 */     return this.mailbox.askEither(debug2 -> new StrictQueue.IntRunnable(Priority.HIGH.ordinal(), ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void storePendingChunk() {
/* 127 */     Iterator<Map.Entry<ChunkPos, PendingStore>> debug1 = this.pendingWrites.entrySet().iterator();
/* 128 */     if (!debug1.hasNext()) {
/*     */       return;
/*     */     }
/*     */     
/* 132 */     Map.Entry<ChunkPos, PendingStore> debug2 = debug1.next();
/* 133 */     debug1.remove();
/* 134 */     runStore(debug2.getKey(), debug2.getValue());
/* 135 */     tellStorePending();
/*     */   }
/*     */   
/*     */   private void tellStorePending() {
/* 139 */     this.mailbox.tell(new StrictQueue.IntRunnable(Priority.LOW.ordinal(), this::storePendingChunk));
/*     */   }
/*     */   
/*     */   private void runStore(ChunkPos debug1, PendingStore debug2) {
/*     */     try {
/* 144 */       this.storage.write(debug1, debug2.data);
/* 145 */       debug2.result.complete(null);
/* 146 */     } catch (Exception debug3) {
/* 147 */       LOGGER.error("Failed to store chunk {}", debug1, debug3);
/* 148 */       debug2.result.completeExceptionally(debug3);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 154 */     if (!this.shutdownRequested.compareAndSet(false, true)) {
/*     */       return;
/*     */     }
/*     */     
/* 158 */     CompletableFuture<Unit> debug1 = this.mailbox.ask(debug0 -> new StrictQueue.IntRunnable(Priority.HIGH.ordinal(), ()));
/*     */     try {
/* 160 */       debug1.join();
/* 161 */     } catch (CompletionException debug2) {
/* 162 */       if (debug2.getCause() instanceof IOException) {
/* 163 */         throw (IOException)debug2.getCause();
/*     */       }
/* 165 */       throw debug2;
/*     */     } 
/*     */     
/* 168 */     this.mailbox.close();
/* 169 */     this.pendingWrites.forEach(this::runStore);
/* 170 */     this.pendingWrites.clear();
/*     */     try {
/* 172 */       this.storage.close();
/* 173 */     } catch (Exception debug2) {
/* 174 */       LOGGER.error("Failed to close storage", debug2);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\storage\IOWorker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */