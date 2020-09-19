/*    */ package net.minecraft.world.entity.monster;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class Skeleton extends AbstractSkeleton {
/*    */   public Skeleton(EntityType<? extends Skeleton> debug1, Level debug2) {
/* 13 */     super((EntityType)debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getAmbientSound() {
/* 18 */     return SoundEvents.SKELETON_AMBIENT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 23 */     return SoundEvents.SKELETON_HURT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getDeathSound() {
/* 28 */     return SoundEvents.SKELETON_DEATH;
/*    */   }
/*    */ 
/*    */   
/*    */   SoundEvent getStepSound() {
/* 33 */     return SoundEvents.SKELETON_STEP;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void dropCustomDeathLoot(DamageSource debug1, int debug2, boolean debug3) {
/* 38 */     super.dropCustomDeathLoot(debug1, debug2, debug3);
/* 39 */     Entity debug4 = debug1.getEntity();
/* 40 */     if (debug4 instanceof Creeper) {
/* 41 */       Creeper debug5 = (Creeper)debug4;
/* 42 */       if (debug5.canDropMobsSkull()) {
/* 43 */         debug5.increaseDroppedSkulls();
/* 44 */         spawnAtLocation((ItemLike)Items.SKELETON_SKULL);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Skeleton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */