/*    */ package net.minecraft.world.entity.animal;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class Salmon extends AbstractSchoolingFish {
/*    */   public Salmon(EntityType<? extends Salmon> debug1, Level debug2) {
/* 13 */     super((EntityType)debug1, debug2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getMaxSchoolSize() {
/* 20 */     return 5;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack getBucketItemStack() {
/* 25 */     return new ItemStack((ItemLike)Items.SALMON_BUCKET);
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getAmbientSound() {
/* 30 */     return SoundEvents.SALMON_AMBIENT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getDeathSound() {
/* 35 */     return SoundEvents.SALMON_DEATH;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 40 */     return SoundEvents.SALMON_HURT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getFlopSound() {
/* 45 */     return SoundEvents.SALMON_FLOP;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\Salmon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */