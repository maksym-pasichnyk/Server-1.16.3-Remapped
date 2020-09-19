/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.SimpleContainer;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ public class HopperMenu
/*    */   extends AbstractContainerMenu
/*    */ {
/*    */   private final Container hopper;
/*    */   
/*    */   public HopperMenu(int debug1, Inventory debug2) {
/* 16 */     this(debug1, debug2, (Container)new SimpleContainer(5));
/*    */   }
/*    */   
/*    */   public HopperMenu(int debug1, Inventory debug2, Container debug3) {
/* 20 */     super(MenuType.HOPPER, debug1);
/* 21 */     this.hopper = debug3;
/* 22 */     checkContainerSize(debug3, 5);
/*    */     
/* 24 */     debug3.startOpen(debug2.player);
/* 25 */     int debug4 = 51;
/*    */     int debug5;
/* 27 */     for (debug5 = 0; debug5 < 5; debug5++) {
/* 28 */       addSlot(new Slot(debug3, debug5, 44 + debug5 * 18, 20));
/*    */     }
/*    */     
/* 31 */     for (debug5 = 0; debug5 < 3; debug5++) {
/* 32 */       for (int debug6 = 0; debug6 < 9; debug6++) {
/* 33 */         addSlot(new Slot((Container)debug2, debug6 + debug5 * 9 + 9, 8 + debug6 * 18, debug5 * 18 + 51));
/*    */       }
/*    */     } 
/* 36 */     for (debug5 = 0; debug5 < 9; debug5++) {
/* 37 */       addSlot(new Slot((Container)debug2, debug5, 8 + debug5 * 18, 109));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean stillValid(Player debug1) {
/* 43 */     return this.hopper.stillValid(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack quickMoveStack(Player debug1, int debug2) {
/* 48 */     ItemStack debug3 = ItemStack.EMPTY;
/* 49 */     Slot debug4 = this.slots.get(debug2);
/* 50 */     if (debug4 != null && debug4.hasItem()) {
/* 51 */       ItemStack debug5 = debug4.getItem();
/* 52 */       debug3 = debug5.copy();
/*    */       
/* 54 */       if (debug2 < this.hopper.getContainerSize()) {
/* 55 */         if (!moveItemStackTo(debug5, this.hopper.getContainerSize(), this.slots.size(), true)) {
/* 56 */           return ItemStack.EMPTY;
/*    */         }
/*    */       }
/* 59 */       else if (!moveItemStackTo(debug5, 0, this.hopper.getContainerSize(), false)) {
/* 60 */         return ItemStack.EMPTY;
/*    */       } 
/*    */       
/* 63 */       if (debug5.isEmpty()) {
/* 64 */         debug4.set(ItemStack.EMPTY);
/*    */       } else {
/* 66 */         debug4.setChanged();
/*    */       } 
/*    */     } 
/* 69 */     return debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public void removed(Player debug1) {
/* 74 */     super.removed(debug1);
/* 75 */     this.hopper.stopOpen(debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\HopperMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */