/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
/*    */ 
/*    */ public abstract class AbstractHugeMushroomFeature extends Feature<HugeMushroomFeatureConfiguration> {
/*    */   public AbstractHugeMushroomFeature(Codec<HugeMushroomFeatureConfiguration> debug1) {
/* 19 */     super(debug1);
/*    */   }
/*    */   
/*    */   protected void placeTrunk(LevelAccessor debug1, Random debug2, BlockPos debug3, HugeMushroomFeatureConfiguration debug4, int debug5, BlockPos.MutableBlockPos debug6) {
/* 23 */     for (int debug7 = 0; debug7 < debug5; debug7++) {
/* 24 */       debug6.set((Vec3i)debug3).move(Direction.UP, debug7);
/* 25 */       if (!debug1.getBlockState((BlockPos)debug6).isSolidRender((BlockGetter)debug1, (BlockPos)debug6)) {
/* 26 */         setBlock((LevelWriter)debug1, (BlockPos)debug6, debug4.stemProvider.getState(debug2, debug3));
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   protected int getTreeHeight(Random debug1) {
/* 32 */     int debug2 = debug1.nextInt(3) + 4;
/* 33 */     if (debug1.nextInt(12) == 0) {
/* 34 */       debug2 *= 2;
/*    */     }
/* 36 */     return debug2;
/*    */   }
/*    */   
/*    */   protected boolean isValidPosition(LevelAccessor debug1, BlockPos debug2, int debug3, BlockPos.MutableBlockPos debug4, HugeMushroomFeatureConfiguration debug5) {
/* 40 */     int debug6 = debug2.getY();
/* 41 */     if (debug6 < 1 || debug6 + debug3 + 1 >= 256) {
/* 42 */       return false;
/*    */     }
/*    */     
/* 45 */     Block debug7 = debug1.getBlockState(debug2.below()).getBlock();
/* 46 */     if (!isDirt(debug7) && !debug7.is((Tag)BlockTags.MUSHROOM_GROW_BLOCK)) {
/* 47 */       return false;
/*    */     }
/*    */     
/* 50 */     for (int debug8 = 0; debug8 <= debug3; debug8++) {
/* 51 */       int debug9 = getTreeRadiusForHeight(-1, -1, debug5.foliageRadius, debug8);
/* 52 */       for (int debug10 = -debug9; debug10 <= debug9; debug10++) {
/* 53 */         for (int debug11 = -debug9; debug11 <= debug9; debug11++) {
/* 54 */           BlockState debug12 = debug1.getBlockState((BlockPos)debug4.setWithOffset((Vec3i)debug2, debug10, debug8, debug11));
/* 55 */           if (!debug12.isAir() && !debug12.is((Tag)BlockTags.LEAVES)) {
/* 56 */             return false;
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/* 61 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, HugeMushroomFeatureConfiguration debug5) {
/* 66 */     int debug6 = getTreeHeight(debug3);
/*    */     
/* 68 */     BlockPos.MutableBlockPos debug7 = new BlockPos.MutableBlockPos();
/* 69 */     if (!isValidPosition((LevelAccessor)debug1, debug4, debug6, debug7, debug5)) {
/* 70 */       return false;
/*    */     }
/*    */     
/* 73 */     makeCap((LevelAccessor)debug1, debug3, debug4, debug6, debug7, debug5);
/* 74 */     placeTrunk((LevelAccessor)debug1, debug3, debug4, debug5, debug6, debug7);
/* 75 */     return true;
/*    */   }
/*    */   
/*    */   protected abstract int getTreeRadiusForHeight(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*    */   
/*    */   protected abstract void makeCap(LevelAccessor paramLevelAccessor, Random paramRandom, BlockPos paramBlockPos, int paramInt, BlockPos.MutableBlockPos paramMutableBlockPos, HugeMushroomFeatureConfiguration paramHugeMushroomFeatureConfiguration);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\AbstractHugeMushroomFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */