/*    */ package net.minecraft.world.entity.animal.horse;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.AgableMob;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.animal.Animal;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class Donkey
/*    */   extends AbstractChestedHorse {
/*    */   public Donkey(EntityType<? extends Donkey> debug1, Level debug2) {
/* 16 */     super((EntityType)debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getAmbientSound() {
/* 21 */     super.getAmbientSound();
/* 22 */     return SoundEvents.DONKEY_AMBIENT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getAngrySound() {
/* 27 */     super.getAngrySound();
/* 28 */     return SoundEvents.DONKEY_ANGRY;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getDeathSound() {
/* 33 */     super.getDeathSound();
/* 34 */     return SoundEvents.DONKEY_DEATH;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected SoundEvent getEatingSound() {
/* 40 */     return SoundEvents.DONKEY_EAT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 45 */     super.getHurtSound(debug1);
/* 46 */     return SoundEvents.DONKEY_HURT;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canMate(Animal debug1) {
/* 51 */     if (debug1 == this) {
/* 52 */       return false;
/*    */     }
/*    */     
/* 55 */     if (debug1 instanceof Donkey || debug1 instanceof Horse) {
/* 56 */       return (canParent() && ((AbstractHorse)debug1).canParent());
/*    */     }
/*    */     
/* 59 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public AgableMob getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/* 64 */     EntityType<? extends AbstractHorse> debug3 = (debug2 instanceof Horse) ? EntityType.MULE : EntityType.DONKEY;
/* 65 */     AbstractHorse debug4 = (AbstractHorse)debug3.create((Level)debug1);
/*    */     
/* 67 */     setOffspringAttributes(debug2, debug4);
/*    */     
/* 69 */     return (AgableMob)debug4;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\horse\Donkey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */