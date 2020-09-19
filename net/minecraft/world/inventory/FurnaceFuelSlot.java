/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class FurnaceFuelSlot extends Slot {
/*    */   private final AbstractFurnaceMenu menu;
/*    */   
/*    */   public FurnaceFuelSlot(AbstractFurnaceMenu debug1, Container debug2, int debug3, int debug4, int debug5) {
/* 11 */     super(debug2, debug3, debug4, debug5);
/* 12 */     this.menu = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mayPlace(ItemStack debug1) {
/* 17 */     return (this.menu.isFuel(debug1) || isBucket(debug1));
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxStackSize(ItemStack debug1) {
/* 22 */     return isBucket(debug1) ? 1 : super.getMaxStackSize(debug1);
/*    */   }
/*    */   
/*    */   public static boolean isBucket(ItemStack debug0) {
/* 26 */     return (debug0.getItem() == Items.BUCKET);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\FurnaceFuelSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */