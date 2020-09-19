/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class VoidStartPlatformFeature
/*    */   extends Feature<NoneFeatureConfiguration> {
/* 15 */   private static final BlockPos PLATFORM_ORIGIN = new BlockPos(8, 3, 8);
/* 16 */   private static final ChunkPos PLATFORM_ORIGIN_CHUNK = new ChunkPos(PLATFORM_ORIGIN);
/*    */ 
/*    */ 
/*    */   
/*    */   public VoidStartPlatformFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 21 */     super(debug1);
/*    */   }
/*    */   
/*    */   private static int checkerboardDistance(int debug0, int debug1, int debug2, int debug3) {
/* 25 */     return Math.max(Math.abs(debug0 - debug2), Math.abs(debug1 - debug3));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, NoneFeatureConfiguration debug5) {
/* 30 */     ChunkPos debug6 = new ChunkPos(debug4);
/* 31 */     if (checkerboardDistance(debug6.x, debug6.z, PLATFORM_ORIGIN_CHUNK.x, PLATFORM_ORIGIN_CHUNK.z) > 1) {
/* 32 */       return true;
/*    */     }
/*    */     
/* 35 */     BlockPos.MutableBlockPos debug7 = new BlockPos.MutableBlockPos();
/* 36 */     for (int debug8 = debug6.getMinBlockZ(); debug8 <= debug6.getMaxBlockZ(); debug8++) {
/* 37 */       for (int debug9 = debug6.getMinBlockX(); debug9 <= debug6.getMaxBlockX(); debug9++) {
/* 38 */         if (checkerboardDistance(PLATFORM_ORIGIN.getX(), PLATFORM_ORIGIN.getZ(), debug9, debug8) <= 16) {
/*    */ 
/*    */           
/* 41 */           debug7.set(debug9, PLATFORM_ORIGIN.getY(), debug8);
/* 42 */           if (debug7.equals(PLATFORM_ORIGIN)) {
/* 43 */             debug1.setBlock((BlockPos)debug7, Blocks.COBBLESTONE.defaultBlockState(), 2);
/*    */           } else {
/* 45 */             debug1.setBlock((BlockPos)debug7, Blocks.STONE.defaultBlockState(), 2);
/*    */           } 
/*    */         } 
/*    */       } 
/* 49 */     }  return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\VoidStartPlatformFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */