/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.SimpleContainer;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ public class ShulkerBoxMenu
/*    */   extends AbstractContainerMenu
/*    */ {
/*    */   private final Container container;
/*    */   
/*    */   public ShulkerBoxMenu(int debug1, Inventory debug2) {
/* 16 */     this(debug1, debug2, (Container)new SimpleContainer(27));
/*    */   }
/*    */   
/*    */   public ShulkerBoxMenu(int debug1, Inventory debug2, Container debug3) {
/* 20 */     super(MenuType.SHULKER_BOX, debug1);
/* 21 */     checkContainerSize(debug3, 27);
/* 22 */     this.container = debug3;
/* 23 */     debug3.startOpen(debug2.player);
/*    */     
/* 25 */     int debug4 = 3;
/* 26 */     int debug5 = 9;
/*    */     int debug6;
/* 28 */     for (debug6 = 0; debug6 < 3; debug6++) {
/* 29 */       for (int debug7 = 0; debug7 < 9; debug7++) {
/* 30 */         addSlot(new ShulkerBoxSlot(debug3, debug7 + debug6 * 9, 8 + debug7 * 18, 18 + debug6 * 18));
/*    */       }
/*    */     } 
/*    */     
/* 34 */     for (debug6 = 0; debug6 < 3; debug6++) {
/* 35 */       for (int debug7 = 0; debug7 < 9; debug7++) {
/* 36 */         addSlot(new Slot((Container)debug2, debug7 + debug6 * 9 + 9, 8 + debug7 * 18, 84 + debug6 * 18));
/*    */       }
/*    */     } 
/* 39 */     for (debug6 = 0; debug6 < 9; debug6++) {
/* 40 */       addSlot(new Slot((Container)debug2, debug6, 8 + debug6 * 18, 142));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean stillValid(Player debug1) {
/* 46 */     return this.container.stillValid(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack quickMoveStack(Player debug1, int debug2) {
/* 51 */     ItemStack debug3 = ItemStack.EMPTY;
/* 52 */     Slot debug4 = this.slots.get(debug2);
/* 53 */     if (debug4 != null && debug4.hasItem()) {
/* 54 */       ItemStack debug5 = debug4.getItem();
/* 55 */       debug3 = debug5.copy();
/*    */       
/* 57 */       if (debug2 < this.container.getContainerSize()) {
/* 58 */         if (!moveItemStackTo(debug5, this.container.getContainerSize(), this.slots.size(), true)) {
/* 59 */           return ItemStack.EMPTY;
/*    */         }
/*    */       }
/* 62 */       else if (!moveItemStackTo(debug5, 0, this.container.getContainerSize(), false)) {
/* 63 */         return ItemStack.EMPTY;
/*    */       } 
/*    */       
/* 66 */       if (debug5.isEmpty()) {
/* 67 */         debug4.set(ItemStack.EMPTY);
/*    */       } else {
/* 69 */         debug4.setChanged();
/*    */       } 
/*    */     } 
/* 72 */     return debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public void removed(Player debug1) {
/* 77 */     super.removed(debug1);
/* 78 */     this.container.stopOpen(debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\ShulkerBoxMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */