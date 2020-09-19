/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.world.item.alchemy.Potion;
/*    */ import net.minecraft.world.item.alchemy.PotionUtils;
/*    */ import net.minecraft.world.item.alchemy.Potions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TippedArrowItem
/*    */   extends ArrowItem
/*    */ {
/*    */   public TippedArrowItem(Item.Properties debug1) {
/* 16 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack getDefaultInstance() {
/* 21 */     return PotionUtils.setPotion(super.getDefaultInstance(), Potions.POISON);
/*    */   }
/*    */ 
/*    */   
/*    */   public void fillItemCategory(CreativeModeTab debug1, NonNullList<ItemStack> debug2) {
/* 26 */     if (allowdedIn(debug1)) {
/* 27 */       for (Potion debug4 : Registry.POTION) {
/* 28 */         if (!debug4.getEffects().isEmpty()) {
/* 29 */           debug2.add(PotionUtils.setPotion(new ItemStack(this), debug4));
/*    */         }
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDescriptionId(ItemStack debug1) {
/* 42 */     return PotionUtils.getPotion(debug1).getName(getDescriptionId() + ".effect.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\TippedArrowItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */