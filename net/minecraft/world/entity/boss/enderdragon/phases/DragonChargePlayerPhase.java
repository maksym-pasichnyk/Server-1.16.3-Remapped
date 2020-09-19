/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class DragonChargePlayerPhase
/*    */   extends AbstractDragonPhaseInstance {
/* 11 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   private Vec3 targetLocation;
/*    */   
/*    */   private int timeSinceCharge;
/*    */   
/*    */   public DragonChargePlayerPhase(EnderDragon debug1) {
/* 18 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void doServerTick() {
/* 23 */     if (this.targetLocation == null) {
/* 24 */       LOGGER.warn("Aborting charge player as no target was set.");
/* 25 */       this.dragon.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
/*    */       
/*    */       return;
/*    */     } 
/* 29 */     if (this.timeSinceCharge > 0 && 
/* 30 */       this.timeSinceCharge++ >= 10) {
/* 31 */       this.dragon.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 36 */     double debug1 = this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
/* 37 */     if (debug1 < 100.0D || debug1 > 22500.0D || this.dragon.horizontalCollision || this.dragon.verticalCollision) {
/* 38 */       this.timeSinceCharge++;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin() {
/* 44 */     this.targetLocation = null;
/* 45 */     this.timeSinceCharge = 0;
/*    */   }
/*    */   
/*    */   public void setTarget(Vec3 debug1) {
/* 49 */     this.targetLocation = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getFlySpeed() {
/* 54 */     return 3.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Vec3 getFlyTargetLocation() {
/* 60 */     return this.targetLocation;
/*    */   }
/*    */ 
/*    */   
/*    */   public EnderDragonPhase<DragonChargePlayerPhase> getPhase() {
/* 65 */     return EnderDragonPhase.CHARGING_PLAYER;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonChargePlayerPhase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */