/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ public class TieredItem extends Item {
/*    */   private final Tier tier;
/*    */   
/*    */   public TieredItem(Tier debug1, Item.Properties debug2) {
/*  7 */     super(debug2.defaultDurability(debug1.getUses()));
/*  8 */     this.tier = debug1;
/*    */   }
/*    */   
/*    */   public Tier getTier() {
/* 12 */     return this.tier;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getEnchantmentValue() {
/* 17 */     return this.tier.getEnchantmentValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidRepairItem(ItemStack debug1, ItemStack debug2) {
/* 22 */     return (this.tier.getRepairIngredient().test(debug2) || super.isValidRepairItem(debug1, debug2));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\TieredItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */