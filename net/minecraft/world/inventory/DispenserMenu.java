/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.SimpleContainer;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DispenserMenu
/*    */   extends AbstractContainerMenu
/*    */ {
/*    */   private final Container dispenser;
/*    */   
/*    */   public DispenserMenu(int debug1, Inventory debug2) {
/* 21 */     this(debug1, debug2, (Container)new SimpleContainer(9));
/*    */   }
/*    */   
/*    */   public DispenserMenu(int debug1, Inventory debug2, Container debug3) {
/* 25 */     super(MenuType.GENERIC_3x3, debug1);
/* 26 */     checkContainerSize(debug3, 9);
/* 27 */     this.dispenser = debug3;
/* 28 */     debug3.startOpen(debug2.player);
/*    */     int debug4;
/* 30 */     for (debug4 = 0; debug4 < 3; debug4++) {
/* 31 */       for (int debug5 = 0; debug5 < 3; debug5++) {
/* 32 */         addSlot(new Slot(debug3, debug5 + debug4 * 3, 62 + debug5 * 18, 17 + debug4 * 18));
/*    */       }
/*    */     } 
/*    */     
/* 36 */     for (debug4 = 0; debug4 < 3; debug4++) {
/* 37 */       for (int debug5 = 0; debug5 < 9; debug5++) {
/* 38 */         addSlot(new Slot((Container)debug2, debug5 + debug4 * 9 + 9, 8 + debug5 * 18, 84 + debug4 * 18));
/*    */       }
/*    */     } 
/* 41 */     for (debug4 = 0; debug4 < 9; debug4++) {
/* 42 */       addSlot(new Slot((Container)debug2, debug4, 8 + debug4 * 18, 142));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean stillValid(Player debug1) {
/* 48 */     return this.dispenser.stillValid(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack quickMoveStack(Player debug1, int debug2) {
/* 53 */     ItemStack debug3 = ItemStack.EMPTY;
/* 54 */     Slot debug4 = this.slots.get(debug2);
/* 55 */     if (debug4 != null && debug4.hasItem()) {
/* 56 */       ItemStack debug5 = debug4.getItem();
/* 57 */       debug3 = debug5.copy();
/*    */       
/* 59 */       if (debug2 < 9) {
/* 60 */         if (!moveItemStackTo(debug5, 9, 45, true)) {
/* 61 */           return ItemStack.EMPTY;
/*    */         }
/*    */       }
/* 64 */       else if (!moveItemStackTo(debug5, 0, 9, false)) {
/* 65 */         return ItemStack.EMPTY;
/*    */       } 
/*    */       
/* 68 */       if (debug5.isEmpty()) {
/* 69 */         debug4.set(ItemStack.EMPTY);
/*    */       } else {
/* 71 */         debug4.setChanged();
/*    */       } 
/* 73 */       if (debug5.getCount() == debug3.getCount())
/*    */       {
/* 75 */         return ItemStack.EMPTY;
/*    */       }
/* 77 */       debug4.onTake(debug1, debug5);
/*    */     } 
/*    */     
/* 80 */     return debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public void removed(Player debug1) {
/* 85 */     super.removed(debug1);
/* 86 */     this.dispenser.stopOpen(debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\DispenserMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */