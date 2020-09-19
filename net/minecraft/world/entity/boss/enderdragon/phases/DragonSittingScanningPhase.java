/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DragonSittingScanningPhase
/*    */   extends AbstractDragonSittingPhase
/*    */ {
/* 15 */   private static final TargetingConditions CHARGE_TARGETING = (new TargetingConditions()).range(150.0D);
/*    */   
/*    */   private final TargetingConditions scanTargeting;
/*    */   private int scanningTime;
/*    */   
/*    */   public DragonSittingScanningPhase(EnderDragon debug1) {
/* 21 */     super(debug1);
/*    */     
/* 23 */     this.scanTargeting = (new TargetingConditions()).range(20.0D).selector(debug1 -> (Math.abs(debug1.getY() - debug0.getY()) <= 10.0D));
/*    */   }
/*    */ 
/*    */   
/*    */   public void doServerTick() {
/* 28 */     this.scanningTime++;
/* 29 */     Player player = this.dragon.level.getNearestPlayer(this.scanTargeting, (LivingEntity)this.dragon, this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
/*    */     
/* 31 */     if (player != null) {
/* 32 */       if (this.scanningTime > 25) {
/* 33 */         this.dragon.getPhaseManager().setPhase(EnderDragonPhase.SITTING_ATTACKING);
/*    */       } else {
/* 35 */         Vec3 debug2 = (new Vec3(player.getX() - this.dragon.getX(), 0.0D, player.getZ() - this.dragon.getZ())).normalize();
/* 36 */         Vec3 debug3 = (new Vec3(Mth.sin(this.dragon.yRot * 0.017453292F), 0.0D, -Mth.cos(this.dragon.yRot * 0.017453292F))).normalize();
/* 37 */         float debug4 = (float)debug3.dot(debug2);
/* 38 */         float debug5 = (float)(Math.acos(debug4) * 57.2957763671875D) + 0.5F;
/*    */         
/* 40 */         if (debug5 < 0.0F || debug5 > 10.0F) {
/* 41 */           double debug6 = player.getX() - this.dragon.head.getX();
/* 42 */           double debug8 = player.getZ() - this.dragon.head.getZ();
/* 43 */           double debug10 = Mth.clamp(Mth.wrapDegrees(180.0D - Mth.atan2(debug6, debug8) * 57.2957763671875D - this.dragon.yRot), -100.0D, 100.0D);
/*    */           
/* 45 */           this.dragon.yRotA *= 0.8F;
/*    */           
/* 47 */           float debug12 = Mth.sqrt(debug6 * debug6 + debug8 * debug8) + 1.0F;
/* 48 */           float debug13 = debug12;
/* 49 */           if (debug12 > 40.0F) {
/* 50 */             debug12 = 40.0F;
/*    */           }
/* 52 */           this.dragon.yRotA = (float)(this.dragon.yRotA + debug10 * (0.7F / debug12 / debug13));
/* 53 */           this.dragon.yRot += this.dragon.yRotA;
/*    */         } 
/*    */       } 
/* 56 */     } else if (this.scanningTime >= 100) {
/* 57 */       player = this.dragon.level.getNearestPlayer(CHARGE_TARGETING, (LivingEntity)this.dragon, this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
/* 58 */       this.dragon.getPhaseManager().setPhase(EnderDragonPhase.TAKEOFF);
/* 59 */       if (player != null) {
/* 60 */         this.dragon.getPhaseManager().setPhase(EnderDragonPhase.CHARGING_PLAYER);
/* 61 */         ((DragonChargePlayerPhase)this.dragon.getPhaseManager().<DragonChargePlayerPhase>getPhase(EnderDragonPhase.CHARGING_PLAYER)).setTarget(new Vec3(player.getX(), player.getY(), player.getZ()));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin() {
/* 68 */     this.scanningTime = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public EnderDragonPhase<DragonSittingScanningPhase> getPhase() {
/* 73 */     return EnderDragonPhase.SITTING_SCANNING;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonSittingScanningPhase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */