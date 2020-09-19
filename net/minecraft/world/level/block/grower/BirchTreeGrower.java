/*    */ package net.minecraft.world.level.block.grower;
/*    */ 
/*    */ import java.util.Random;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.data.worldgen.Features;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class BirchTreeGrower
/*    */   extends AbstractTreeGrower
/*    */ {
/*    */   @Nullable
/*    */   protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random debug1, boolean debug2) {
/* 14 */     return debug2 ? Features.BIRCH_BEES_005 : Features.BIRCH;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\grower\BirchTreeGrower.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */