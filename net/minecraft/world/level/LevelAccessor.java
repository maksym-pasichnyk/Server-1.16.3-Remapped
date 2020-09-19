/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.Random;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.DifficultyInstance;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.chunk.ChunkSource;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.storage.LevelData;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface LevelAccessor
/*    */   extends CommonLevelAccessor, LevelTimeAccess
/*    */ {
/*    */   default long dayTime() {
/* 27 */     return getLevelData().getDayTime();
/*    */   }
/*    */ 
/*    */   
/*    */   TickList<Block> getBlockTicks();
/*    */ 
/*    */   
/*    */   TickList<Fluid> getLiquidTicks();
/*    */ 
/*    */   
/*    */   LevelData getLevelData();
/*    */   
/*    */   DifficultyInstance getCurrentDifficultyAt(BlockPos paramBlockPos);
/*    */   
/*    */   default Difficulty getDifficulty() {
/* 42 */     return getLevelData().getDifficulty();
/*    */   }
/*    */ 
/*    */   
/*    */   ChunkSource getChunkSource();
/*    */   
/*    */   default boolean hasChunk(int debug1, int debug2) {
/* 49 */     return getChunkSource().hasChunk(debug1, debug2);
/*    */   }
/*    */   
/*    */   Random getRandom();
/*    */   
/*    */   default void blockUpdated(BlockPos debug1, Block debug2) {}
/*    */   
/*    */   void playSound(@Nullable Player paramPlayer, BlockPos paramBlockPos, SoundEvent paramSoundEvent, SoundSource paramSoundSource, float paramFloat1, float paramFloat2);
/*    */   
/*    */   void addParticle(ParticleOptions paramParticleOptions, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6);
/*    */   
/*    */   void levelEvent(@Nullable Player paramPlayer, int paramInt1, BlockPos paramBlockPos, int paramInt2);
/*    */   
/*    */   default int getHeight() {
/* 63 */     return dimensionType().logicalHeight();
/*    */   }
/*    */   
/*    */   default void levelEvent(int debug1, BlockPos debug2, int debug3) {
/* 67 */     levelEvent(null, debug1, debug2, debug3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\LevelAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */