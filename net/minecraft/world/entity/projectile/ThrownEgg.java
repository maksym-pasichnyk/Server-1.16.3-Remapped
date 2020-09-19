/*    */ package net.minecraft.world.entity.projectile;
/*    */ 
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.Chicken;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ public class ThrownEgg
/*    */   extends ThrowableItemProjectile
/*    */ {
/*    */   public ThrownEgg(EntityType<? extends ThrownEgg> debug1, Level debug2) {
/* 18 */     super((EntityType)debug1, debug2);
/*    */   }
/*    */   
/*    */   public ThrownEgg(Level debug1, LivingEntity debug2) {
/* 22 */     super(EntityType.EGG, debug2, debug1);
/*    */   }
/*    */   
/*    */   public ThrownEgg(Level debug1, double debug2, double debug4, double debug6) {
/* 26 */     super(EntityType.EGG, debug2, debug4, debug6, debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onHitEntity(EntityHitResult debug1) {
/* 41 */     super.onHitEntity(debug1);
/* 42 */     debug1.getEntity().hurt(DamageSource.thrown(this, getOwner()), 0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHit(HitResult debug1) {
/* 47 */     super.onHit(debug1);
/*    */     
/* 49 */     if (!this.level.isClientSide) {
/* 50 */       if (this.random.nextInt(8) == 0) {
/* 51 */         int debug2 = 1;
/* 52 */         if (this.random.nextInt(32) == 0) {
/* 53 */           debug2 = 4;
/*    */         }
/* 55 */         for (int debug3 = 0; debug3 < debug2; debug3++) {
/* 56 */           Chicken debug4 = (Chicken)EntityType.CHICKEN.create(this.level);
/* 57 */           debug4.setAge(-24000);
/*    */           
/* 59 */           debug4.moveTo(getX(), getY(), getZ(), this.yRot, 0.0F);
/* 60 */           this.level.addFreshEntity((Entity)debug4);
/*    */         } 
/*    */       } 
/*    */       
/* 64 */       this.level.broadcastEntityEvent(this, (byte)3);
/* 65 */       remove();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected Item getDefaultItem() {
/* 71 */     return Items.EGG;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\ThrownEgg.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */