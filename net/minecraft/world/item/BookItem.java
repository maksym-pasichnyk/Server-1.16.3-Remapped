/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ public class BookItem extends Item {
/*    */   public BookItem(Item.Properties debug1) {
/*  5 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEnchantable(ItemStack debug1) {
/* 10 */     return (debug1.getCount() == 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getEnchantmentValue() {
/* 15 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\BookItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */