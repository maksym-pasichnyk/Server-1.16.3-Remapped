/*    */ package net.minecraft.world.entity.projectile;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.syncher.EntityDataAccessor;
/*    */ import net.minecraft.network.syncher.EntityDataSerializers;
/*    */ import net.minecraft.network.syncher.SynchedEntityData;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class ThrowableItemProjectile extends ThrowableProjectile {
/* 15 */   private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(ThrowableItemProjectile.class, EntityDataSerializers.ITEM_STACK);
/*    */   
/*    */   public ThrowableItemProjectile(EntityType<? extends ThrowableItemProjectile> debug1, Level debug2) {
/* 18 */     super((EntityType)debug1, debug2);
/*    */   }
/*    */   
/*    */   public ThrowableItemProjectile(EntityType<? extends ThrowableItemProjectile> debug1, double debug2, double debug4, double debug6, Level debug8) {
/* 22 */     super((EntityType)debug1, debug2, debug4, debug6, debug8);
/*    */   }
/*    */   
/*    */   public ThrowableItemProjectile(EntityType<? extends ThrowableItemProjectile> debug1, LivingEntity debug2, Level debug3) {
/* 26 */     super((EntityType)debug1, debug2, debug3);
/*    */   }
/*    */   
/*    */   public void setItem(ItemStack debug1) {
/* 30 */     if (debug1.getItem() != getDefaultItem() || debug1.hasTag()) {
/* 31 */       getEntityData().set(DATA_ITEM_STACK, Util.make(debug1.copy(), debug0 -> debug0.setCount(1)));
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected ItemStack getItemRaw() {
/* 38 */     return (ItemStack)getEntityData().get(DATA_ITEM_STACK);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack getItem() {
/* 43 */     ItemStack debug1 = getItemRaw();
/* 44 */     return debug1.isEmpty() ? new ItemStack((ItemLike)getDefaultItem()) : debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void defineSynchedData() {
/* 49 */     getEntityData().define(DATA_ITEM_STACK, ItemStack.EMPTY);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 54 */     super.addAdditionalSaveData(debug1);
/* 55 */     ItemStack debug2 = getItemRaw();
/* 56 */     if (!debug2.isEmpty()) {
/* 57 */       debug1.put("Item", (Tag)debug2.save(new CompoundTag()));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 63 */     super.readAdditionalSaveData(debug1);
/* 64 */     ItemStack debug2 = ItemStack.of(debug1.getCompound("Item"));
/* 65 */     setItem(debug2);
/*    */   }
/*    */   
/*    */   protected abstract Item getDefaultItem();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\ThrowableItemProjectile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */