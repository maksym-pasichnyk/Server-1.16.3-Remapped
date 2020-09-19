/*    */ package net.minecraft.world.entity.projectile;
/*    */ 
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Snowball
/*    */   extends ThrowableItemProjectile
/*    */ {
/*    */   public Snowball(EntityType<? extends Snowball> debug1, Level debug2) {
/* 21 */     super((EntityType)debug1, debug2);
/*    */   }
/*    */   
/*    */   public Snowball(Level debug1, LivingEntity debug2) {
/* 25 */     super(EntityType.SNOWBALL, debug2, debug1);
/*    */   }
/*    */   
/*    */   public Snowball(Level debug1, double debug2, double debug4, double debug6) {
/* 29 */     super(EntityType.SNOWBALL, debug2, debug4, debug6, debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Item getDefaultItem() {
/* 34 */     return Items.SNOWBALL;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onHitEntity(EntityHitResult debug1) {
/* 54 */     super.onHitEntity(debug1);
/* 55 */     Entity debug2 = debug1.getEntity();
/* 56 */     int debug3 = (debug2 instanceof net.minecraft.world.entity.monster.Blaze) ? 3 : 0;
/*    */     
/* 58 */     debug2.hurt(DamageSource.thrown(this, getOwner()), debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHit(HitResult debug1) {
/* 63 */     super.onHit(debug1);
/*    */     
/* 65 */     if (!this.level.isClientSide) {
/* 66 */       this.level.broadcastEntityEvent(this, (byte)3);
/* 67 */       remove();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\Snowball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */