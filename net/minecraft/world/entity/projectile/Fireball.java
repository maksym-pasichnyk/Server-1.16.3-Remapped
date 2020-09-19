/*    */ package net.minecraft.world.entity.projectile;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.syncher.EntityDataAccessor;
/*    */ import net.minecraft.network.syncher.EntityDataSerializers;
/*    */ import net.minecraft.network.syncher.SynchedEntityData;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class Fireball extends AbstractHurtingProjectile {
/* 15 */   private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(Fireball.class, EntityDataSerializers.ITEM_STACK);
/*    */   
/*    */   public Fireball(EntityType<? extends Fireball> debug1, Level debug2) {
/* 18 */     super((EntityType)debug1, debug2);
/*    */   }
/*    */   
/*    */   public Fireball(EntityType<? extends Fireball> debug1, double debug2, double debug4, double debug6, double debug8, double debug10, double debug12, Level debug14) {
/* 22 */     super((EntityType)debug1, debug2, debug4, debug6, debug8, debug10, debug12, debug14);
/*    */   }
/*    */   
/*    */   public Fireball(EntityType<? extends Fireball> debug1, LivingEntity debug2, double debug3, double debug5, double debug7, Level debug9) {
/* 26 */     super((EntityType)debug1, debug2, debug3, debug5, debug7, debug9);
/*    */   }
/*    */   
/*    */   public void setItem(ItemStack debug1) {
/* 30 */     if (debug1.getItem() != Items.FIRE_CHARGE || debug1.hasTag()) {
/* 31 */       getEntityData().set(DATA_ITEM_STACK, Util.make(debug1.copy(), debug0 -> debug0.setCount(1)));
/*    */     }
/*    */   }
/*    */   
/*    */   protected ItemStack getItemRaw() {
/* 36 */     return (ItemStack)getEntityData().get(DATA_ITEM_STACK);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void defineSynchedData() {
/* 47 */     getEntityData().define(DATA_ITEM_STACK, ItemStack.EMPTY);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 52 */     super.addAdditionalSaveData(debug1);
/* 53 */     ItemStack debug2 = getItemRaw();
/* 54 */     if (!debug2.isEmpty()) {
/* 55 */       debug1.put("Item", (Tag)debug2.save(new CompoundTag()));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 61 */     super.readAdditionalSaveData(debug1);
/* 62 */     ItemStack debug2 = ItemStack.of(debug1.getCompound("Item"));
/* 63 */     setItem(debug2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\Fireball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */