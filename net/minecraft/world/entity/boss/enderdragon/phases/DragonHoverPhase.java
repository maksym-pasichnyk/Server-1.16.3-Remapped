/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DragonHoverPhase
/*    */   extends AbstractDragonPhaseInstance {
/*    */   private Vec3 targetLocation;
/*    */   
/*    */   public DragonHoverPhase(EnderDragon debug1) {
/* 12 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void doServerTick() {
/* 17 */     if (this.targetLocation == null) {
/* 18 */       this.targetLocation = this.dragon.position();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSitting() {
/* 24 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin() {
/* 29 */     this.targetLocation = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getFlySpeed() {
/* 34 */     return 1.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Vec3 getFlyTargetLocation() {
/* 40 */     return this.targetLocation;
/*    */   }
/*    */ 
/*    */   
/*    */   public EnderDragonPhase<DragonHoverPhase> getPhase() {
/* 45 */     return EnderDragonPhase.HOVERING;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonHoverPhase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */