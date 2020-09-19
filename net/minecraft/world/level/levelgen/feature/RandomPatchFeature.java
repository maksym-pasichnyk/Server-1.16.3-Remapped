/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
/*    */ 
/*    */ public class RandomPatchFeature extends Feature<RandomPatchConfiguration> {
/*    */   public RandomPatchFeature(Codec<RandomPatchConfiguration> debug1) {
/* 16 */     super(debug1);
/*    */   }
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, RandomPatchConfiguration debug5) {
/*    */     BlockPos debug7;
/* 21 */     BlockState debug6 = debug5.stateProvider.getState(debug3, debug4);
/*    */     
/* 23 */     if (debug5.project) {
/* 24 */       debug7 = debug1.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, debug4);
/*    */     } else {
/* 26 */       debug7 = debug4;
/*    */     } 
/*    */     
/* 29 */     int debug8 = 0;
/*    */     
/* 31 */     BlockPos.MutableBlockPos debug9 = new BlockPos.MutableBlockPos();
/* 32 */     for (int debug10 = 0; debug10 < debug5.tries; debug10++) {
/* 33 */       debug9.setWithOffset((Vec3i)debug7, debug3.nextInt(debug5.xspread + 1) - debug3.nextInt(debug5.xspread + 1), debug3.nextInt(debug5.yspread + 1) - debug3.nextInt(debug5.yspread + 1), debug3.nextInt(debug5.zspread + 1) - debug3.nextInt(debug5.zspread + 1));
/* 34 */       BlockPos debug11 = debug9.below();
/* 35 */       BlockState debug12 = debug1.getBlockState(debug11);
/* 36 */       if ((debug1.isEmptyBlock((BlockPos)debug9) || (debug5.canReplace && debug1.getBlockState((BlockPos)debug9).getMaterial().isReplaceable())) && debug6
/* 37 */         .canSurvive((LevelReader)debug1, (BlockPos)debug9) && (debug5.whitelist
/* 38 */         .isEmpty() || debug5.whitelist.contains(debug12.getBlock())) && 
/* 39 */         !debug5.blacklist.contains(debug12) && (!debug5.needWater || debug1
/* 40 */         .getFluidState(debug11.west()).is((Tag)FluidTags.WATER) || debug1.getFluidState(debug11.east()).is((Tag)FluidTags.WATER) || debug1.getFluidState(debug11.north()).is((Tag)FluidTags.WATER) || debug1.getFluidState(debug11.south()).is((Tag)FluidTags.WATER))) {
/*    */         
/* 42 */         debug5.blockPlacer.place((LevelAccessor)debug1, (BlockPos)debug9, debug6, debug3);
/* 43 */         debug8++;
/*    */       } 
/*    */     } 
/*    */     
/* 47 */     return (debug8 > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\RandomPatchFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */