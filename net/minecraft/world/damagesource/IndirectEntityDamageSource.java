/*    */ package net.minecraft.world.damagesource;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class IndirectEntityDamageSource
/*    */   extends EntityDamageSource {
/*    */   private final Entity owner;
/*    */   
/*    */   public IndirectEntityDamageSource(String debug1, Entity debug2, @Nullable Entity debug3) {
/* 15 */     super(debug1, debug2);
/* 16 */     this.owner = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Entity getDirectEntity() {
/* 22 */     return this.entity;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Entity getEntity() {
/* 28 */     return this.owner;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getLocalizedDeathMessage(LivingEntity debug1) {
/* 33 */     Component debug2 = (this.owner == null) ? this.entity.getDisplayName() : this.owner.getDisplayName();
/* 34 */     ItemStack debug3 = (this.owner instanceof LivingEntity) ? ((LivingEntity)this.owner).getMainHandItem() : ItemStack.EMPTY;
/* 35 */     String debug4 = "death.attack." + this.msgId;
/* 36 */     String debug5 = debug4 + ".item";
/*    */     
/* 38 */     if (!debug3.isEmpty() && debug3.hasCustomHoverName()) {
/* 39 */       return (Component)new TranslatableComponent(debug5, new Object[] { debug1.getDisplayName(), debug2, debug3.getDisplayName() });
/*    */     }
/* 41 */     return (Component)new TranslatableComponent(debug4, new Object[] { debug1.getDisplayName(), debug2 });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\damagesource\IndirectEntityDamageSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */