/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public abstract class AbstractDragonPhaseInstance
/*    */   implements DragonPhaseInstance {
/*    */   protected final EnderDragon dragon;
/*    */   
/*    */   public AbstractDragonPhaseInstance(EnderDragon debug1) {
/* 18 */     this.dragon = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSitting() {
/* 23 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void doClientTick() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void doServerTick() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void onCrystalDestroyed(EndCrystal debug1, BlockPos debug2, DamageSource debug3, @Nullable Player debug4) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void begin() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void end() {}
/*    */ 
/*    */   
/*    */   public float getFlySpeed() {
/* 48 */     return 0.6F;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Vec3 getFlyTargetLocation() {
/* 54 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public float onHurt(DamageSource debug1, float debug2) {
/* 59 */     return debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getTurnSpeed() {
/* 64 */     float debug1 = Mth.sqrt(Entity.getHorizontalDistanceSqr(this.dragon.getDeltaMovement())) + 1.0F;
/* 65 */     float debug2 = Math.min(debug1, 40.0F);
/*    */     
/* 67 */     return 0.7F / debug2 / debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\AbstractDragonPhaseInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */