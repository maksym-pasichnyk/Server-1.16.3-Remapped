/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class SlimeBlock extends HalfTransparentBlock {
/*    */   public SlimeBlock(BlockBehaviour.Properties debug1) {
/* 12 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void fallOn(Level debug1, BlockPos debug2, Entity debug3, float debug4) {
/* 17 */     if (debug3.isSuppressingBounce()) {
/* 18 */       super.fallOn(debug1, debug2, debug3, debug4);
/*    */     } else {
/*    */       
/* 21 */       debug3.causeFallDamage(debug4, 0.0F);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateEntityAfterFallOn(BlockGetter debug1, Entity debug2) {
/* 27 */     if (debug2.isSuppressingBounce()) {
/* 28 */       super.updateEntityAfterFallOn(debug1, debug2);
/*    */     } else {
/* 30 */       bounceUp(debug2);
/*    */     } 
/*    */   }
/*    */   
/*    */   private void bounceUp(Entity debug1) {
/* 35 */     Vec3 debug2 = debug1.getDeltaMovement();
/* 36 */     if (debug2.y < 0.0D) {
/*    */       
/* 38 */       double debug3 = (debug1 instanceof net.minecraft.world.entity.LivingEntity) ? 1.0D : 0.8D;
/* 39 */       debug1.setDeltaMovement(debug2.x, -debug2.y * debug3, debug2.z);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void stepOn(Level debug1, BlockPos debug2, Entity debug3) {
/* 49 */     double debug4 = Math.abs((debug3.getDeltaMovement()).y);
/* 50 */     if (debug4 < 0.1D && !debug3.isSteppingCarefully()) {
/* 51 */       double debug6 = 0.4D + debug4 * 0.2D;
/* 52 */       debug3.setDeltaMovement(debug3.getDeltaMovement().multiply(debug6, 1.0D, debug6));
/*    */     } 
/* 54 */     super.stepOn(debug1, debug2, debug3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\SlimeBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */