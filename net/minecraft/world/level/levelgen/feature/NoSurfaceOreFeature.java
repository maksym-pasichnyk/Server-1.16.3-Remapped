/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NoSurfaceOreFeature
/*    */   extends Feature<OreConfiguration>
/*    */ {
/*    */   NoSurfaceOreFeature(Codec<OreConfiguration> debug1) {
/* 21 */     super(debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, OreConfiguration debug5) {
/* 29 */     int debug6 = debug3.nextInt(debug5.size + 1);
/* 30 */     BlockPos.MutableBlockPos debug7 = new BlockPos.MutableBlockPos();
/*    */     
/* 32 */     for (int debug8 = 0; debug8 < debug6; debug8++) {
/*    */       
/* 34 */       offsetTargetPos(debug7, debug3, debug4, Math.min(debug8, 7));
/*    */       
/* 36 */       if (debug5.target.test(debug1.getBlockState((BlockPos)debug7), debug3) && !isFacingAir((LevelAccessor)debug1, (BlockPos)debug7))
/*    */       {
/* 38 */         debug1.setBlock((BlockPos)debug7, debug5.state, 2);
/*    */       }
/*    */     } 
/* 41 */     return true;
/*    */   }
/*    */   
/*    */   private void offsetTargetPos(BlockPos.MutableBlockPos debug1, Random debug2, BlockPos debug3, int debug4) {
/* 45 */     int debug5 = getRandomPlacementInOneAxisRelativeToOrigin(debug2, debug4);
/* 46 */     int debug6 = getRandomPlacementInOneAxisRelativeToOrigin(debug2, debug4);
/* 47 */     int debug7 = getRandomPlacementInOneAxisRelativeToOrigin(debug2, debug4);
/* 48 */     debug1.setWithOffset((Vec3i)debug3, debug5, debug6, debug7);
/*    */   }
/*    */   
/*    */   private int getRandomPlacementInOneAxisRelativeToOrigin(Random debug1, int debug2) {
/* 52 */     return Math.round((debug1.nextFloat() - debug1.nextFloat()) * debug2);
/*    */   }
/*    */   
/*    */   private boolean isFacingAir(LevelAccessor debug1, BlockPos debug2) {
/* 56 */     BlockPos.MutableBlockPos debug3 = new BlockPos.MutableBlockPos();
/* 57 */     for (Direction debug7 : Direction.values()) {
/* 58 */       debug3.setWithOffset((Vec3i)debug2, debug7);
/* 59 */       if (debug1.getBlockState((BlockPos)debug3).isAir()) {
/* 60 */         return true;
/*    */       }
/*    */     } 
/* 63 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\NoSurfaceOreFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */