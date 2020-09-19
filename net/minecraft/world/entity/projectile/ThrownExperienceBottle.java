/*    */ package net.minecraft.world.entity.projectile;
/*    */ 
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.ExperienceOrb;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.alchemy.PotionUtils;
/*    */ import net.minecraft.world.item.alchemy.Potions;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ public class ThrownExperienceBottle extends ThrowableItemProjectile {
/*    */   public ThrownExperienceBottle(EntityType<? extends ThrownExperienceBottle> debug1, Level debug2) {
/* 16 */     super((EntityType)debug1, debug2);
/*    */   }
/*    */   
/*    */   public ThrownExperienceBottle(Level debug1, LivingEntity debug2) {
/* 20 */     super(EntityType.EXPERIENCE_BOTTLE, debug2, debug1);
/*    */   }
/*    */   
/*    */   public ThrownExperienceBottle(Level debug1, double debug2, double debug4, double debug6) {
/* 24 */     super(EntityType.EXPERIENCE_BOTTLE, debug2, debug4, debug6, debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Item getDefaultItem() {
/* 29 */     return Items.EXPERIENCE_BOTTLE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getGravity() {
/* 34 */     return 0.07F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHit(HitResult debug1) {
/* 39 */     super.onHit(debug1);
/*    */     
/* 41 */     if (!this.level.isClientSide) {
/* 42 */       this.level.levelEvent(2002, blockPosition(), PotionUtils.getColor(Potions.WATER));
/*    */       
/* 44 */       int debug2 = 3 + this.level.random.nextInt(5) + this.level.random.nextInt(5);
/* 45 */       while (debug2 > 0) {
/* 46 */         int debug3 = ExperienceOrb.getExperienceValue(debug2);
/* 47 */         debug2 -= debug3;
/* 48 */         this.level.addFreshEntity((Entity)new ExperienceOrb(this.level, getX(), getY(), getZ(), debug3));
/*    */       } 
/*    */       
/* 51 */       remove();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\ThrownExperienceBottle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */