/*    */ package net.minecraft.world.entity.animal.horse;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.AgableMob;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class Mule
/*    */   extends AbstractChestedHorse {
/*    */   public Mule(EntityType<? extends Mule> debug1, Level debug2) {
/* 15 */     super((EntityType)debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getAmbientSound() {
/* 20 */     super.getAmbientSound();
/* 21 */     return SoundEvents.MULE_AMBIENT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getAngrySound() {
/* 26 */     super.getAngrySound();
/* 27 */     return SoundEvents.MULE_ANGRY;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getDeathSound() {
/* 32 */     super.getDeathSound();
/* 33 */     return SoundEvents.MULE_DEATH;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected SoundEvent getEatingSound() {
/* 39 */     return SoundEvents.MULE_EAT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 44 */     super.getHurtSound(debug1);
/* 45 */     return SoundEvents.MULE_HURT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void playChestEquipsSound() {
/* 50 */     playSound(SoundEvents.MULE_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public AgableMob getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/* 55 */     return (AgableMob)EntityType.MULE.create((Level)debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\horse\Mule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */