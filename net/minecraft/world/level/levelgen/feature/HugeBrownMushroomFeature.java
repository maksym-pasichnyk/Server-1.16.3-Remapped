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
/*    */ public class HugeBrownMushroomFeature extends AbstractHugeMushroomFeature {
/*    */   public HugeBrownMushroomFeature(Codec<HugeMushroomFeatureConfiguration> debug1) {
/* 13 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void makeCap(LevelAccessor debug1, Random debug2, BlockPos debug3, int debug4, BlockPos.MutableBlockPos debug5, HugeMushroomFeatureConfiguration debug6) {
/* 18 */     int debug7 = debug6.foliageRadius;
/* 19 */     for (int debug8 = -debug7; debug8 <= debug7; debug8++) {
/* 20 */       for (int debug9 = -debug7; debug9 <= debug7; debug9++) {
/* 21 */         boolean debug10 = (debug8 == -debug7);
/* 22 */         boolean debug11 = (debug8 == debug7);
/* 23 */         boolean debug12 = (debug9 == -debug7);
/* 24 */         boolean debug13 = (debug9 == debug7);
/*    */         
/* 26 */         boolean debug14 = (debug10 || debug11);
/* 27 */         boolean debug15 = (debug12 || debug13);
/* 28 */         if (!debug14 || !debug15) {
/*    */ 
/*    */ 
/*    */           
/* 32 */           debug5.setWithOffset((Vec3i)debug3, debug8, debug4, debug9);
/* 33 */           if (!debug1.getBlockState((BlockPos)debug5).isSolidRender((BlockGetter)debug1, (BlockPos)debug5)) {
/* 34 */             boolean debug16 = (debug10 || (debug15 && debug8 == 1 - debug7));
/* 35 */             boolean debug17 = (debug11 || (debug15 && debug8 == debug7 - 1));
/* 36 */             boolean debug18 = (debug12 || (debug14 && debug9 == 1 - debug7));
/* 37 */             boolean debug19 = (debug13 || (debug14 && debug9 == debug7 - 1));
/* 38 */             setBlock((LevelWriter)debug1, (BlockPos)debug5, (BlockState)((BlockState)((BlockState)((BlockState)debug6.capProvider.getState(debug2, debug3)
/* 39 */                 .setValue((Property)HugeMushroomBlock.WEST, Boolean.valueOf(debug16)))
/* 40 */                 .setValue((Property)HugeMushroomBlock.EAST, Boolean.valueOf(debug17)))
/* 41 */                 .setValue((Property)HugeMushroomBlock.NORTH, Boolean.valueOf(debug18)))
/* 42 */                 .setValue((Property)HugeMushroomBlock.SOUTH, Boolean.valueOf(debug19)));
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getTreeRadiusForHeight(int debug1, int debug2, int debug3, int debug4) {
/* 51 */     return (debug4 <= 3) ? 0 : debug3;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\HugeBrownMushroomFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */