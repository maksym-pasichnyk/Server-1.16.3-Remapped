/*    */ package net.minecraft.world.entity.animal;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class AbstractGolem
/*    */   extends PathfinderMob {
/*    */   protected AbstractGolem(EntityType<? extends AbstractGolem> debug1, Level debug2) {
/* 13 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean causeFallDamage(float debug1, float debug2) {
/* 18 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected SoundEvent getAmbientSound() {
/* 24 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 30 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected SoundEvent getDeathSound() {
/* 36 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getAmbientSoundInterval() {
/* 41 */     return 120;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean removeWhenFarAway(double debug1) {
/* 46 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\AbstractGolem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */