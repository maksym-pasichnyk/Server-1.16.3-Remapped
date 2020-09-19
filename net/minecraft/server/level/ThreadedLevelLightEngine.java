/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.function.IntSupplier;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.thread.ProcessorHandle;
/*     */ import net.minecraft.util.thread.ProcessorMailbox;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ThreadedLevelLightEngine
/*     */   extends LevelLightEngine
/*     */   implements AutoCloseable
/*     */ {
/*  30 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   private final ProcessorMailbox<Runnable> taskMailbox;
/*  32 */   private final ObjectList<Pair<TaskType, Runnable>> lightTasks = (ObjectList<Pair<TaskType, Runnable>>)new ObjectArrayList();
/*     */   private final ChunkMap chunkMap;
/*     */   private final ProcessorHandle<ChunkTaskPriorityQueueSorter.Message<Runnable>> sorterMailbox;
/*  35 */   private volatile int taskPerBatch = 5;
/*  36 */   private final AtomicBoolean scheduled = new AtomicBoolean();
/*     */   
/*     */   public ThreadedLevelLightEngine(LightChunkGetter debug1, ChunkMap debug2, boolean debug3, ProcessorMailbox<Runnable> debug4, ProcessorHandle<ChunkTaskPriorityQueueSorter.Message<Runnable>> debug5) {
/*  39 */     super(debug1, true, debug3);
/*  40 */     this.chunkMap = debug2;
/*  41 */     this.sorterMailbox = debug5;
/*  42 */     this.taskMailbox = debug4;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {}
/*     */ 
/*     */   
/*     */   public int runUpdates(int debug1, boolean debug2, boolean debug3) {
/*  51 */     throw (UnsupportedOperationException)Util.pauseInIde(new UnsupportedOperationException("Ran authomatically on a different thread!"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockEmissionIncrease(BlockPos debug1, int debug2) {
/*  56 */     throw (UnsupportedOperationException)Util.pauseInIde(new UnsupportedOperationException("Ran authomatically on a different thread!"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkBlock(BlockPos debug1) {
/*  61 */     BlockPos debug2 = debug1.immutable();
/*  62 */     addTask(debug1.getX() >> 4, debug1.getZ() >> 4, TaskType.POST_UPDATE, Util.name(() -> super.checkBlock(debug1), () -> "checkBlock " + debug0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateChunkStatus(ChunkPos debug1) {
/*  67 */     addTask(debug1.x, debug1.z, () -> 0, TaskType.PRE_UPDATE, Util.name(() -> {
/*     */             super.retainData(debug1, false);
/*     */             super.enableLightSources(debug1, false);
/*     */             int debug2;
/*     */             for (debug2 = -1; debug2 < 17; debug2++) {
/*     */               super.queueSectionData(LightLayer.BLOCK, SectionPos.of(debug1, debug2), null, true);
/*     */               super.queueSectionData(LightLayer.SKY, SectionPos.of(debug1, debug2), null, true);
/*     */             } 
/*     */             for (debug2 = 0; debug2 < 16; debug2++) {
/*     */               super.updateSectionStatus(SectionPos.of(debug1, debug2), true);
/*     */             }
/*     */           }() -> "updateChunkStatus " + debug0 + " " + '\001'));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateSectionStatus(SectionPos debug1, boolean debug2) {
/*  86 */     addTask(debug1.x(), debug1.z(), () -> 0, TaskType.PRE_UPDATE, Util.name(() -> super.updateSectionStatus(debug1, debug2), () -> "updateSectionStatus " + debug0 + " " + debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public void enableLightSources(ChunkPos debug1, boolean debug2) {
/*  91 */     addTask(debug1.x, debug1.z, TaskType.PRE_UPDATE, Util.name(() -> super.enableLightSources(debug1, debug2), () -> "enableLight " + debug0 + " " + debug1));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void queueSectionData(LightLayer debug1, SectionPos debug2, @Nullable DataLayer debug3, boolean debug4) {
/*  97 */     addTask(debug2.x(), debug2.z(), () -> 0, TaskType.PRE_UPDATE, Util.name(() -> super.queueSectionData(debug1, debug2, debug3, debug4), () -> "queueData " + debug0));
/*     */   }
/*     */   
/*     */   private void addTask(int debug1, int debug2, TaskType debug3, Runnable debug4) {
/* 101 */     addTask(debug1, debug2, this.chunkMap.getChunkQueueLevel(ChunkPos.asLong(debug1, debug2)), debug3, debug4);
/*     */   }
/*     */   
/*     */   private void addTask(int debug1, int debug2, IntSupplier debug3, TaskType debug4, Runnable debug5) {
/* 105 */     this.sorterMailbox.tell(ChunkTaskPriorityQueueSorter.message(() -> {
/*     */             this.lightTasks.add(Pair.of(debug1, debug2));
/*     */             if (this.lightTasks.size() >= this.taskPerBatch) {
/*     */               runUpdate();
/*     */             }
/* 110 */           }ChunkPos.asLong(debug1, debug2), debug3));
/*     */   }
/*     */ 
/*     */   
/*     */   public void retainData(ChunkPos debug1, boolean debug2) {
/* 115 */     addTask(debug1.x, debug1.z, () -> 0, TaskType.PRE_UPDATE, Util.name(() -> super.retainData(debug1, debug2), () -> "retainData " + debug0));
/*     */   }
/*     */   
/*     */   public CompletableFuture<ChunkAccess> lightChunk(ChunkAccess debug1, boolean debug2) {
/* 119 */     ChunkPos debug3 = debug1.getPos();
/* 120 */     debug1.setLightCorrect(false);
/* 121 */     addTask(debug3.x, debug3.z, TaskType.PRE_UPDATE, Util.name(() -> {
/*     */             LevelChunkSection[] debug4 = debug1.getSections();
/*     */ 
/*     */             
/*     */             for (int debug5 = 0; debug5 < 16; debug5++) {
/*     */               LevelChunkSection debug6 = debug4[debug5];
/*     */               
/*     */               if (!LevelChunkSection.isEmpty(debug6)) {
/*     */                 super.updateSectionStatus(SectionPos.of(debug2, debug5), false);
/*     */               }
/*     */             } 
/*     */             
/*     */             super.enableLightSources(debug2, true);
/*     */             
/*     */             if (!debug3) {
/*     */               debug1.getLights().forEach(());
/*     */             }
/*     */             
/*     */             this.chunkMap.releaseLightTicket(debug2);
/*     */           }() -> "lightChunk " + debug0 + " " + debug1));
/*     */     
/* 142 */     return CompletableFuture.supplyAsync(() -> {
/*     */           debug1.setLightCorrect(true);
/*     */           super.retainData(debug2, false);
/*     */           return debug1;
/*     */         }debug2 -> addTask(debug1.x, debug1.z, TaskType.POST_UPDATE, debug2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void tryScheduleUpdate() {
/* 153 */     if ((!this.lightTasks.isEmpty() || hasLightWork()) && this.scheduled.compareAndSet(false, true)) {
/* 154 */       this.taskMailbox.tell(() -> {
/*     */             runUpdate();
/*     */             this.scheduled.set(false);
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   private void runUpdate() {
/* 162 */     int debug1 = Math.min(this.lightTasks.size(), this.taskPerBatch);
/*     */     
/* 164 */     ObjectListIterator<Pair<TaskType, Runnable>> debug2 = this.lightTasks.iterator();
/*     */     
/* 166 */     int debug3 = 0;
/* 167 */     while (debug2.hasNext() && debug3 < debug1) {
/* 168 */       Pair<TaskType, Runnable> debug4 = (Pair<TaskType, Runnable>)debug2.next();
/* 169 */       if (debug4.getFirst() == TaskType.PRE_UPDATE) {
/* 170 */         ((Runnable)debug4.getSecond()).run();
/*     */       }
/* 172 */       debug3++;
/*     */     } 
/* 174 */     debug2.back(debug3);
/*     */     
/* 176 */     super.runUpdates(2147483647, true, true);
/*     */     
/* 178 */     debug3 = 0;
/* 179 */     while (debug2.hasNext() && debug3 < debug1) {
/* 180 */       Pair<TaskType, Runnable> debug4 = (Pair<TaskType, Runnable>)debug2.next();
/* 181 */       if (debug4.getFirst() == TaskType.POST_UPDATE) {
/* 182 */         ((Runnable)debug4.getSecond()).run();
/*     */       }
/* 184 */       debug2.remove();
/* 185 */       debug3++;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setTaskPerBatch(int debug1) {
/* 190 */     this.taskPerBatch = debug1;
/*     */   }
/*     */   
/*     */   enum TaskType {
/* 194 */     PRE_UPDATE, POST_UPDATE;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\ThreadedLevelLightEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */