/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
/*     */ import net.minecraft.world.entity.animal.horse.AbstractHorse;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ 
/*     */ public class HorseInventoryMenu extends AbstractContainerMenu {
/*     */   private final Container horseContainer;
/*     */   
/*     */   public HorseInventoryMenu(int debug1, Inventory debug2, Container debug3, final AbstractHorse horse) {
/*  16 */     super(null, debug1);
/*  17 */     this.horseContainer = debug3;
/*  18 */     this.horse = horse;
/*  19 */     int debug5 = 3;
/*  20 */     debug3.startOpen(debug2.player);
/*     */     
/*  22 */     int debug6 = -18;
/*     */ 
/*     */     
/*  25 */     addSlot(new Slot(debug3, 0, 8, 18)
/*     */         {
/*     */           public boolean mayPlace(ItemStack debug1) {
/*  28 */             return (debug1.getItem() == Items.SADDLE && !hasItem() && horse.isSaddleable());
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  37 */     addSlot(new Slot(debug3, 1, 8, 36)
/*     */         {
/*     */           public boolean mayPlace(ItemStack debug1) {
/*  40 */             return horse.isArmor(debug1);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public int getMaxStackSize() {
/*  50 */             return 1;
/*     */           }
/*     */         });
/*     */     
/*  54 */     if (horse instanceof AbstractChestedHorse && ((AbstractChestedHorse)horse).hasChest()) {
/*  55 */       for (int i = 0; i < 3; i++) {
/*  56 */         for (int debug8 = 0; debug8 < ((AbstractChestedHorse)horse).getInventoryColumns(); debug8++) {
/*  57 */           addSlot(new Slot(debug3, 2 + debug8 + i * ((AbstractChestedHorse)horse).getInventoryColumns(), 80 + debug8 * 18, 18 + i * 18));
/*     */         }
/*     */       } 
/*     */     }
/*     */     int debug7;
/*  62 */     for (debug7 = 0; debug7 < 3; debug7++) {
/*  63 */       for (int debug8 = 0; debug8 < 9; debug8++) {
/*  64 */         addSlot(new Slot((Container)debug2, debug8 + debug7 * 9 + 9, 8 + debug8 * 18, 102 + debug7 * 18 + -18));
/*     */       }
/*     */     } 
/*  67 */     for (debug7 = 0; debug7 < 9; debug7++)
/*  68 */       addSlot(new Slot((Container)debug2, debug7, 8 + debug7 * 18, 142)); 
/*     */   }
/*     */   
/*     */   private final AbstractHorse horse;
/*     */   
/*     */   public boolean stillValid(Player debug1) {
/*  74 */     return (this.horseContainer.stillValid(debug1) && this.horse.isAlive() && this.horse.distanceTo((Entity)debug1) < 8.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player debug1, int debug2) {
/*  79 */     ItemStack debug3 = ItemStack.EMPTY;
/*  80 */     Slot debug4 = this.slots.get(debug2);
/*  81 */     if (debug4 != null && debug4.hasItem()) {
/*  82 */       ItemStack debug5 = debug4.getItem();
/*  83 */       debug3 = debug5.copy();
/*     */       
/*  85 */       int debug6 = this.horseContainer.getContainerSize();
/*  86 */       if (debug2 < debug6) {
/*  87 */         if (!moveItemStackTo(debug5, debug6, this.slots.size(), true)) {
/*  88 */           return ItemStack.EMPTY;
/*     */         }
/*  90 */       } else if (getSlot(1).mayPlace(debug5) && !getSlot(1).hasItem()) {
/*  91 */         if (!moveItemStackTo(debug5, 1, 2, false)) {
/*  92 */           return ItemStack.EMPTY;
/*     */         }
/*  94 */       } else if (getSlot(0).mayPlace(debug5)) {
/*  95 */         if (!moveItemStackTo(debug5, 0, 1, false)) {
/*  96 */           return ItemStack.EMPTY;
/*     */         }
/*  98 */       } else if (debug6 <= 2 || !moveItemStackTo(debug5, 2, debug6, false)) {
/*  99 */         int debug7 = debug6;
/* 100 */         int debug8 = debug7 + 27;
/* 101 */         int debug9 = debug8;
/* 102 */         int debug10 = debug9 + 9;
/* 103 */         if (debug2 >= debug9 && debug2 < debug10) {
/* 104 */           if (!moveItemStackTo(debug5, debug7, debug8, false)) {
/* 105 */             return ItemStack.EMPTY;
/*     */           }
/* 107 */         } else if (debug2 >= debug7 && debug2 < debug8) {
/* 108 */           if (!moveItemStackTo(debug5, debug9, debug10, false)) {
/* 109 */             return ItemStack.EMPTY;
/*     */           }
/* 111 */         } else if (!moveItemStackTo(debug5, debug9, debug8, false)) {
/* 112 */           return ItemStack.EMPTY;
/*     */         } 
/* 114 */         return ItemStack.EMPTY;
/*     */       } 
/* 116 */       if (debug5.isEmpty()) {
/* 117 */         debug4.set(ItemStack.EMPTY);
/*     */       } else {
/* 119 */         debug4.setChanged();
/*     */       } 
/*     */     } 
/* 122 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player debug1) {
/* 127 */     super.removed(debug1);
/* 128 */     this.horseContainer.stopOpen(debug1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\HorseInventoryMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */