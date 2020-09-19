/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class EntityBasedExplosionDamageCalculator
/*    */   extends ExplosionDamageCalculator {
/*    */   private final Entity source;
/*    */   
/*    */   public EntityBasedExplosionDamageCalculator(Entity debug1) {
/* 14 */     this.source = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Float> getBlockExplosionResistance(Explosion debug1, BlockGetter debug2, BlockPos debug3, BlockState debug4, FluidState debug5) {
/* 19 */     return super.getBlockExplosionResistance(debug1, debug2, debug3, debug4, debug5).map(debug6 -> Float.valueOf(this.source.getBlockExplosionResistance(debug1, debug2, debug3, debug4, debug5, debug6.floatValue())));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldBlockExplode(Explosion debug1, BlockGetter debug2, BlockPos debug3, BlockState debug4, float debug5) {
/* 24 */     return this.source.shouldBlockExplode(debug1, debug2, debug3, debug4, debug5);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\EntityBasedExplosionDamageCalculator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */