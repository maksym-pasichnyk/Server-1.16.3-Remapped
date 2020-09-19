/*    */ package net.minecraft.world.level.block.grower;
/*    */ 
/*    */ import java.util.Random;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.data.worldgen.Features;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class OakTreeGrower
/*    */   extends AbstractTreeGrower
/*    */ {
/*    */   @Nullable
/*    */   protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random debug1, boolean debug2) {
/* 14 */     if (debug1.nextInt(10) == 0) {
/* 15 */       return debug2 ? Features.FANCY_OAK_BEES_005 : Features.FANCY_OAK;
/*    */     }
/* 17 */     return debug2 ? Features.OAK_BEES_005 : Features.OAK;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\grower\OakTreeGrower.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */