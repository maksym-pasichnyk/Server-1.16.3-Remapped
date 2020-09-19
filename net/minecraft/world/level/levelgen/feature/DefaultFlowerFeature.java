/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
/*    */ 
/*    */ public class DefaultFlowerFeature extends AbstractFlowerFeature<RandomPatchConfiguration> {
/*    */   public DefaultFlowerFeature(Codec<RandomPatchConfiguration> debug1) {
/* 13 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValid(LevelAccessor debug1, BlockPos debug2, RandomPatchConfiguration debug3) {
/* 18 */     return !debug3.blacklist.contains(debug1.getBlockState(debug2));
/*    */   }
/*    */ 
/*    */   
/*    */   public int getCount(RandomPatchConfiguration debug1) {
/* 23 */     return debug1.tries;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getPos(Random debug1, BlockPos debug2, RandomPatchConfiguration debug3) {
/* 28 */     return debug2.offset(debug1.nextInt(debug3.xspread) - debug1.nextInt(debug3.xspread), debug1.nextInt(debug3.yspread) - debug1.nextInt(debug3.yspread), debug1.nextInt(debug3.zspread) - debug1.nextInt(debug3.zspread));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getRandomFlower(Random debug1, BlockPos debug2, RandomPatchConfiguration debug3) {
/* 33 */     return debug3.stateProvider.getState(debug1, debug2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\DefaultFlowerFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */