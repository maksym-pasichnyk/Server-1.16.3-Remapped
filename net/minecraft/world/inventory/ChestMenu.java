/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class ChestMenu
/*     */   extends AbstractContainerMenu {
/*     */   private final Container container;
/*     */   private final int containerRows;
/*     */   
/*     */   private ChestMenu(MenuType<?> debug1, int debug2, Inventory debug3, int debug4) {
/*  15 */     this(debug1, debug2, debug3, (Container)new SimpleContainer(9 * debug4), debug4);
/*     */   }
/*     */   
/*     */   public static ChestMenu oneRow(int debug0, Inventory debug1) {
/*  19 */     return new ChestMenu(MenuType.GENERIC_9x1, debug0, debug1, 1);
/*     */   }
/*     */   
/*     */   public static ChestMenu twoRows(int debug0, Inventory debug1) {
/*  23 */     return new ChestMenu(MenuType.GENERIC_9x2, debug0, debug1, 2);
/*     */   }
/*     */   
/*     */   public static ChestMenu threeRows(int debug0, Inventory debug1) {
/*  27 */     return new ChestMenu(MenuType.GENERIC_9x3, debug0, debug1, 3);
/*     */   }
/*     */   
/*     */   public static ChestMenu fourRows(int debug0, Inventory debug1) {
/*  31 */     return new ChestMenu(MenuType.GENERIC_9x4, debug0, debug1, 4);
/*     */   }
/*     */   
/*     */   public static ChestMenu fiveRows(int debug0, Inventory debug1) {
/*  35 */     return new ChestMenu(MenuType.GENERIC_9x5, debug0, debug1, 5);
/*     */   }
/*     */   
/*     */   public static ChestMenu sixRows(int debug0, Inventory debug1) {
/*  39 */     return new ChestMenu(MenuType.GENERIC_9x6, debug0, debug1, 6);
/*     */   }
/*     */   
/*     */   public static ChestMenu threeRows(int debug0, Inventory debug1, Container debug2) {
/*  43 */     return new ChestMenu(MenuType.GENERIC_9x3, debug0, debug1, debug2, 3);
/*     */   }
/*     */   
/*     */   public static ChestMenu sixRows(int debug0, Inventory debug1, Container debug2) {
/*  47 */     return new ChestMenu(MenuType.GENERIC_9x6, debug0, debug1, debug2, 6);
/*     */   }
/*     */   
/*     */   public ChestMenu(MenuType<?> debug1, int debug2, Inventory debug3, Container debug4, int debug5) {
/*  51 */     super(debug1, debug2);
/*  52 */     checkContainerSize(debug4, debug5 * 9);
/*  53 */     this.container = debug4;
/*  54 */     this.containerRows = debug5;
/*  55 */     debug4.startOpen(debug3.player);
/*     */     
/*  57 */     int debug6 = (this.containerRows - 4) * 18;
/*     */     int debug7;
/*  59 */     for (debug7 = 0; debug7 < this.containerRows; debug7++) {
/*  60 */       for (int debug8 = 0; debug8 < 9; debug8++) {
/*  61 */         addSlot(new Slot(debug4, debug8 + debug7 * 9, 8 + debug8 * 18, 18 + debug7 * 18));
/*     */       }
/*     */     } 
/*     */     
/*  65 */     for (debug7 = 0; debug7 < 3; debug7++) {
/*  66 */       for (int debug8 = 0; debug8 < 9; debug8++) {
/*  67 */         addSlot(new Slot((Container)debug3, debug8 + debug7 * 9 + 9, 8 + debug8 * 18, 103 + debug7 * 18 + debug6));
/*     */       }
/*     */     } 
/*  70 */     for (debug7 = 0; debug7 < 9; debug7++) {
/*  71 */       addSlot(new Slot((Container)debug3, debug7, 8 + debug7 * 18, 161 + debug6));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player debug1) {
/*  77 */     return this.container.stillValid(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player debug1, int debug2) {
/*  82 */     ItemStack debug3 = ItemStack.EMPTY;
/*  83 */     Slot debug4 = this.slots.get(debug2);
/*  84 */     if (debug4 != null && debug4.hasItem()) {
/*  85 */       ItemStack debug5 = debug4.getItem();
/*  86 */       debug3 = debug5.copy();
/*     */       
/*  88 */       if (debug2 < this.containerRows * 9) {
/*  89 */         if (!moveItemStackTo(debug5, this.containerRows * 9, this.slots.size(), true)) {
/*  90 */           return ItemStack.EMPTY;
/*     */         }
/*     */       }
/*  93 */       else if (!moveItemStackTo(debug5, 0, this.containerRows * 9, false)) {
/*  94 */         return ItemStack.EMPTY;
/*     */       } 
/*     */       
/*  97 */       if (debug5.isEmpty()) {
/*  98 */         debug4.set(ItemStack.EMPTY);
/*     */       } else {
/* 100 */         debug4.setChanged();
/*     */       } 
/*     */     } 
/* 103 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player debug1) {
/* 108 */     super.removed(debug1);
/* 109 */     this.container.stopOpen(debug1);
/*     */   }
/*     */   
/*     */   public Container getContainer() {
/* 113 */     return this.container;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\ChestMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */