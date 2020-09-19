/*    */ package net.minecraft.world.item;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.tags.ItemTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public abstract class ProjectileWeaponItem extends Item {
/*    */   static {
/* 10 */     ARROW_ONLY = (debug0 -> debug0.getItem().is((Tag<Item>)ItemTags.ARROWS));
/* 11 */     ARROW_OR_FIREWORK = ARROW_ONLY.or(debug0 -> (debug0.getItem() == Items.FIREWORK_ROCKET));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Predicate<ItemStack> ARROW_ONLY;
/*    */   public static final Predicate<ItemStack> ARROW_OR_FIREWORK;
/*    */   
/*    */   public ProjectileWeaponItem(Item.Properties debug1) {
/* 19 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Predicate<ItemStack> getSupportedHeldProjectiles() {
/* 24 */     return getAllSupportedProjectiles();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ItemStack getHeldProjectile(LivingEntity debug0, Predicate<ItemStack> debug1) {
/* 31 */     if (debug1.test(debug0.getItemInHand(InteractionHand.OFF_HAND))) {
/* 32 */       return debug0.getItemInHand(InteractionHand.OFF_HAND);
/*    */     }
/* 34 */     if (debug1.test(debug0.getItemInHand(InteractionHand.MAIN_HAND))) {
/* 35 */       return debug0.getItemInHand(InteractionHand.MAIN_HAND);
/*    */     }
/* 37 */     return ItemStack.EMPTY;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getEnchantmentValue() {
/* 42 */     return 1;
/*    */   }
/*    */   
/*    */   public abstract Predicate<ItemStack> getAllSupportedProjectiles();
/*    */   
/*    */   public abstract int getDefaultProjectileRange();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\ProjectileWeaponItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */