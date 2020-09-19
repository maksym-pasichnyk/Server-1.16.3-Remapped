/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.HugeMushroomBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
/*    */ 
/*    */ public class HugeRedMushroomFeature extends AbstractHugeMushroomFeature {
/*    */   public HugeRedMushroomFeature(Codec<HugeMushroomFeatureConfiguration> debug1) {
/* 13 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void makeCap(LevelAccessor debug1, Random debug2, BlockPos debug3, int debug4, BlockPos.MutableBlockPos debug5, HugeMushroomFeatureConfiguration debug6) {
/* 18 */     for (int debug7 = debug4 - 3; debug7 <= debug4; debug7++) {
/* 19 */       int debug8 = (debug7 < debug4) ? debug6.foliageRadius : (debug6.foliageRadius - 1);
/* 20 */       int debug9 = debug6.foliageRadius - 2;
/*    */       
/* 22 */       for (int debug10 = -debug8; debug10 <= debug8; debug10++) {
/* 23 */         for (int debug11 = -debug8; debug11 <= debug8; debug11++) {
/* 24 */           boolean debug12 = (debug10 == -debug8);
/* 25 */           boolean debug13 = (debug10 == debug8);
/* 26 */           boolean debug14 = (debug11 == -debug8);
/* 27 */           boolean debug15 = (debug11 == debug8);
/*    */           
/* 29 */           boolean debug16 = (debug12 || debug13);
/* 30 */           boolean debug17 = (debug14 || debug15);
/*    */           
/* 32 */           if (debug7 >= debug4 || debug16 != debug17) {
/*    */ 
/*    */ 
/*    */             
/* 36 */             debug5.setWithOffset((Vec3i)debug3, debug10, debug7, debug11);
/* 37 */             if (!debug1.getBlockState((BlockPos)debug5).isSolidRender((BlockGetter)debug1, (BlockPos)debug5)) {
/* 38 */               setBlock((LevelWriter)debug1, (BlockPos)debug5, (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)debug6.capProvider.getState(debug2, debug3)
/* 39 */                   .setValue((Property)HugeMushroomBlock.UP, Boolean.valueOf((debug7 >= debug4 - 1))))
/* 40 */                   .setValue((Property)HugeMushroomBlock.WEST, Boolean.valueOf((debug10 < -debug9))))
/* 41 */                   .setValue((Property)HugeMushroomBlock.EAST, Boolean.valueOf((debug10 > debug9))))
/* 42 */                   .setValue((Property)HugeMushroomBlock.NORTH, Boolean.valueOf((debug11 < -debug9))))
/* 43 */                   .setValue((Property)HugeMushroomBlock.SOUTH, Boolean.valueOf((debug11 > debug9))));
/*    */             }
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getTreeRadiusForHeight(int debug1, int debug2, int debug3, int debug4) {
/* 53 */     int debug5 = 0;
/* 54 */     if (debug4 < debug2 && debug4 >= debug2 - 3) {
/* 55 */       debug5 = debug3;
/* 56 */     } else if (debug4 == debug2) {
/* 57 */       debug5 = debug3;
/*    */     } 
/* 59 */     return debug5;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\HugeRedMushroomFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */