/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public abstract class FlyingMob extends Mob {
/*    */   protected FlyingMob(EntityType<? extends FlyingMob> debug1, Level debug2) {
/* 10 */     super((EntityType)debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean causeFallDamage(float debug1, float debug2) {
/* 15 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void checkFallDamage(double debug1, boolean debug3, BlockState debug4, BlockPos debug5) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void travel(Vec3 debug1) {
/* 26 */     if (isInWater()) {
/* 27 */       moveRelative(0.02F, debug1);
/* 28 */       move(MoverType.SELF, getDeltaMovement());
/*    */       
/* 30 */       setDeltaMovement(getDeltaMovement().scale(0.800000011920929D));
/* 31 */     } else if (isInLava()) {
/* 32 */       moveRelative(0.02F, debug1);
/* 33 */       move(MoverType.SELF, getDeltaMovement());
/* 34 */       setDeltaMovement(getDeltaMovement().scale(0.5D));
/*    */     } else {
/* 36 */       float debug2 = 0.91F;
/* 37 */       if (this.onGround) {
/* 38 */         debug2 = this.level.getBlockState(new BlockPos(getX(), getY() - 1.0D, getZ())).getBlock().getFriction() * 0.91F;
/*    */       }
/*    */       
/* 41 */       float debug3 = 0.16277137F / debug2 * debug2 * debug2;
/*    */       
/* 43 */       debug2 = 0.91F;
/* 44 */       if (this.onGround) {
/* 45 */         debug2 = this.level.getBlockState(new BlockPos(getX(), getY() - 1.0D, getZ())).getBlock().getFriction() * 0.91F;
/*    */       }
/*    */       
/* 48 */       moveRelative(this.onGround ? (0.1F * debug3) : 0.02F, debug1);
/* 49 */       move(MoverType.SELF, getDeltaMovement());
/*    */       
/* 51 */       setDeltaMovement(getDeltaMovement().scale(debug2));
/*    */     } 
/* 53 */     calculateEntityAnimation(this, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean onClimbable() {
/* 58 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\FlyingMob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */