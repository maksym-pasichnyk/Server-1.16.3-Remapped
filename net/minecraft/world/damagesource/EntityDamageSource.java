/*    */ package net.minecraft.world.damagesource;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class EntityDamageSource
/*    */   extends DamageSource
/*    */ {
/*    */   @Nullable
/*    */   protected final Entity entity;
/*    */   private boolean isThorns;
/*    */   
/*    */   public EntityDamageSource(String debug1, @Nullable Entity debug2) {
/* 19 */     super(debug1);
/* 20 */     this.entity = debug2;
/*    */   }
/*    */   
/*    */   public EntityDamageSource setThorns() {
/* 24 */     this.isThorns = true;
/* 25 */     return this;
/*    */   }
/*    */   
/*    */   public boolean isThorns() {
/* 29 */     return this.isThorns;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Entity getEntity() {
/* 35 */     return this.entity;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getLocalizedDeathMessage(LivingEntity debug1) {
/* 40 */     ItemStack debug2 = (this.entity instanceof LivingEntity) ? ((LivingEntity)this.entity).getMainHandItem() : ItemStack.EMPTY;
/* 41 */     String debug3 = "death.attack." + this.msgId;
/*    */     
/* 43 */     if (!debug2.isEmpty() && debug2.hasCustomHoverName()) {
/* 44 */       return (Component)new TranslatableComponent(debug3 + ".item", new Object[] { debug1.getDisplayName(), this.entity.getDisplayName(), debug2.getDisplayName() });
/*    */     }
/* 46 */     return (Component)new TranslatableComponent(debug3, new Object[] { debug1.getDisplayName(), this.entity.getDisplayName() });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean scalesWithDifficulty() {
/* 52 */     return (this.entity != null && this.entity instanceof LivingEntity && !(this.entity instanceof net.minecraft.world.entity.player.Player));
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Vec3 getSourcePosition() {
/* 58 */     return (this.entity != null) ? this.entity.position() : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 63 */     return "EntityDamageSource (" + this.entity + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\damagesource\EntityDamageSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */