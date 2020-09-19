/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class ShulkerBoxSlot
/*    */   extends Slot {
/*    */   public ShulkerBoxSlot(Container debug1, int debug2, int debug3, int debug4) {
/* 10 */     super(debug1, debug2, debug3, debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mayPlace(ItemStack debug1) {
/* 15 */     return !(Block.byItem(debug1.getItem()) instanceof net.minecraft.world.level.block.ShulkerBoxBlock);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\ShulkerBoxSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */