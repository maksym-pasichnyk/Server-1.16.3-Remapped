/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*    */ 
/*    */ public abstract class ChunkSource
/*    */   implements LightChunkGetter, AutoCloseable
/*    */ {
/*    */   @Nullable
/*    */   public LevelChunk getChunk(int debug1, int debug2, boolean debug3) {
/* 16 */     return (LevelChunk)getChunk(debug1, debug2, ChunkStatus.FULL, debug3);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public LevelChunk getChunkNow(int debug1, int debug2) {
/* 21 */     return getChunk(debug1, debug2, false);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public BlockGetter getChunkForLighting(int debug1, int debug2) {
/* 27 */     return getChunk(debug1, debug2, ChunkStatus.EMPTY, false);
/*    */   }
/*    */   
/*    */   public boolean hasChunk(int debug1, int debug2) {
/* 31 */     return (getChunk(debug1, debug2, ChunkStatus.FULL, false) != null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public abstract ChunkAccess getChunk(int paramInt1, int paramInt2, ChunkStatus paramChunkStatus, boolean paramBoolean);
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract String gatherStats();
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() throws IOException {}
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract LevelLightEngine getLightEngine();
/*    */ 
/*    */   
/*    */   public void setSpawnSettings(boolean debug1, boolean debug2) {}
/*    */ 
/*    */   
/*    */   public void updateChunkForced(ChunkPos debug1, boolean debug2) {}
/*    */ 
/*    */   
/*    */   public boolean isEntityTickingChunk(Entity debug1) {
/* 59 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isEntityTickingChunk(ChunkPos debug1) {
/* 63 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isTickingChunk(BlockPos debug1) {
/* 67 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\ChunkSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */