/*    */ package net.minecraft.server.level.progress;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.chunk.ChunkStatus;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class LoggerChunkProgressListener
/*    */   implements ChunkProgressListener
/*    */ {
/* 15 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   private final int maxCount;
/*    */   private int count;
/*    */   private long startTime;
/* 19 */   private long nextTickTime = Long.MAX_VALUE;
/*    */   
/*    */   public LoggerChunkProgressListener(int debug1) {
/* 22 */     int debug2 = debug1 * 2 + 1;
/* 23 */     this.maxCount = debug2 * debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateSpawnPos(ChunkPos debug1) {
/* 28 */     this.nextTickTime = Util.getMillis();
/* 29 */     this.startTime = this.nextTickTime;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onStatusChange(ChunkPos debug1, @Nullable ChunkStatus debug2) {
/* 34 */     if (debug2 == ChunkStatus.FULL) {
/* 35 */       this.count++;
/*    */     }
/* 37 */     int debug3 = getProgress();
/*    */ 
/*    */ 
/*    */     
/* 41 */     if (Util.getMillis() > this.nextTickTime) {
/* 42 */       this.nextTickTime += 500L;
/*    */       
/* 44 */       LOGGER.info((new TranslatableComponent("menu.preparingSpawn", new Object[] { Integer.valueOf(Mth.clamp(debug3, 0, 100)) })).getString());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void stop() {
/* 55 */     LOGGER.info("Time elapsed: {} ms", Long.valueOf(Util.getMillis() - this.startTime));
/* 56 */     this.nextTickTime = Long.MAX_VALUE;
/*    */   }
/*    */   
/*    */   public int getProgress() {
/* 60 */     return Mth.floor(this.count * 100.0F / this.maxCount);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\progress\LoggerChunkProgressListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */