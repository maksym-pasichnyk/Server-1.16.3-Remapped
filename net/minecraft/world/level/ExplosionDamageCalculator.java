/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class ExplosionDamageCalculator
/*    */ {
/*    */   public Optional<Float> getBlockExplosionResistance(Explosion debug1, BlockGetter debug2, BlockPos debug3, BlockState debug4, FluidState debug5) {
/* 11 */     if (debug4.isAir() && debug5.isEmpty()) {
/* 12 */       return Optional.empty();
/*    */     }
/* 14 */     return Optional.of(Float.valueOf(Math.max(debug4.getBlock().getExplosionResistance(), debug5.getExplosionResistance())));
/*    */   }
/*    */   
/*    */   public boolean shouldBlockExplode(Explosion debug1, BlockGetter debug2, BlockPos debug3, BlockState debug4, float debug5) {
/* 18 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\ExplosionDamageCalculator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */