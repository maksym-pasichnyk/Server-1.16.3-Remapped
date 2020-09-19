/*    */ package net.minecraft.world.level.block.grower;
/*    */ 
/*    */ import java.util.Random;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.data.worldgen.Features;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class AcaciaTreeGrower
/*    */   extends AbstractTreeGrower
/*    */ {
/*    */   @Nullable
/*    */   protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random debug1, boolean debug2) {
/* 14 */     return Features.ACACIA;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\grower\AcaciaTreeGrower.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */