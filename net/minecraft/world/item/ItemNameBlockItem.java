/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class ItemNameBlockItem extends BlockItem {
/*    */   public ItemNameBlockItem(Block debug1, Item.Properties debug2) {
/*  7 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescriptionId() {
/* 12 */     return getOrCreateDescriptionId();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\ItemNameBlockItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */