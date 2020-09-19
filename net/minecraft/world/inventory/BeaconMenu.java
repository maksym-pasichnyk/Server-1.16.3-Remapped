/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeaconMenu
/*     */   extends AbstractContainerMenu
/*     */ {
/*  24 */   private final Container beacon = (Container)new SimpleContainer(1)
/*     */     {
/*     */       public boolean canPlaceItem(int debug1, ItemStack debug2) {
/*  27 */         return debug2.getItem().is((Tag)ItemTags.BEACON_PAYMENT_ITEMS);
/*     */       }
/*     */ 
/*     */       
/*     */       public int getMaxStackSize() {
/*  32 */         return 1;
/*     */       }
/*     */     };
/*     */   
/*     */   private final PaymentSlot paymentSlot;
/*     */   private final ContainerLevelAccess access;
/*     */   private final ContainerData beaconData;
/*     */   
/*     */   public BeaconMenu(int debug1, Container debug2) {
/*  41 */     this(debug1, debug2, new SimpleContainerData(3), ContainerLevelAccess.NULL);
/*     */   }
/*     */   
/*     */   public BeaconMenu(int debug1, Container debug2, ContainerData debug3, ContainerLevelAccess debug4) {
/*  45 */     super(MenuType.BEACON, debug1);
/*  46 */     checkContainerDataCount(debug3, 3);
/*  47 */     this.beaconData = debug3;
/*  48 */     this.access = debug4;
/*     */     
/*  50 */     this.paymentSlot = new PaymentSlot(this.beacon, 0, 136, 110);
/*  51 */     addSlot(this.paymentSlot);
/*     */     
/*  53 */     addDataSlots(debug3);
/*     */     
/*  55 */     int debug5 = 36;
/*  56 */     int debug6 = 137;
/*     */     int debug7;
/*  58 */     for (debug7 = 0; debug7 < 3; debug7++) {
/*  59 */       for (int debug8 = 0; debug8 < 9; debug8++) {
/*  60 */         addSlot(new Slot(debug2, debug8 + debug7 * 9 + 9, 36 + debug8 * 18, 137 + debug7 * 18));
/*     */       }
/*     */     } 
/*  63 */     for (debug7 = 0; debug7 < 9; debug7++) {
/*  64 */       addSlot(new Slot(debug2, debug7, 36 + debug7 * 18, 195));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player debug1) {
/*  70 */     super.removed(debug1);
/*  71 */     if (debug1.level.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/*  75 */     ItemStack debug2 = this.paymentSlot.remove(this.paymentSlot.getMaxStackSize());
/*  76 */     if (!debug2.isEmpty()) {
/*  77 */       debug1.drop(debug2, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player debug1) {
/*  83 */     return stillValid(this.access, debug1, Blocks.BEACON);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setData(int debug1, int debug2) {
/*  88 */     super.setData(debug1, debug2);
/*  89 */     broadcastChanges();
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player debug1, int debug2) {
/*  94 */     ItemStack debug3 = ItemStack.EMPTY;
/*  95 */     Slot debug4 = this.slots.get(debug2);
/*  96 */     if (debug4 != null && debug4.hasItem()) {
/*  97 */       ItemStack debug5 = debug4.getItem();
/*  98 */       debug3 = debug5.copy();
/*     */       
/* 100 */       if (debug2 == 0) {
/* 101 */         if (!moveItemStackTo(debug5, 1, 37, true)) {
/* 102 */           return ItemStack.EMPTY;
/*     */         }
/* 104 */         debug4.onQuickCraft(debug5, debug3);
/* 105 */       } else if (!this.paymentSlot.hasItem() && this.paymentSlot.mayPlace(debug5) && debug5.getCount() == 1) {
/* 106 */         if (!moveItemStackTo(debug5, 0, 1, false)) {
/* 107 */           return ItemStack.EMPTY;
/*     */         }
/* 109 */       } else if (debug2 >= 1 && debug2 < 28) {
/* 110 */         if (!moveItemStackTo(debug5, 28, 37, false)) {
/* 111 */           return ItemStack.EMPTY;
/*     */         }
/* 113 */       } else if (debug2 >= 28 && debug2 < 37) {
/* 114 */         if (!moveItemStackTo(debug5, 1, 28, false)) {
/* 115 */           return ItemStack.EMPTY;
/*     */         }
/*     */       }
/* 118 */       else if (!moveItemStackTo(debug5, 1, 37, false)) {
/* 119 */         return ItemStack.EMPTY;
/*     */       } 
/*     */       
/* 122 */       if (debug5.isEmpty()) {
/* 123 */         debug4.set(ItemStack.EMPTY);
/*     */       } else {
/* 125 */         debug4.setChanged();
/*     */       } 
/* 127 */       if (debug5.getCount() == debug3.getCount()) {
/* 128 */         return ItemStack.EMPTY;
/*     */       }
/* 130 */       debug4.onTake(debug1, debug5);
/*     */     } 
/*     */     
/* 133 */     return debug3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateEffects(int debug1, int debug2) {
/* 151 */     if (this.paymentSlot.hasItem()) {
/* 152 */       this.beaconData.set(1, debug1);
/* 153 */       this.beaconData.set(2, debug2);
/* 154 */       this.paymentSlot.remove(1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   class PaymentSlot
/*     */     extends Slot
/*     */   {
/*     */     public PaymentSlot(Container debug2, int debug3, int debug4, int debug5) {
/* 164 */       super(debug2, debug3, debug4, debug5);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean mayPlace(ItemStack debug1) {
/* 169 */       return debug1.getItem().is((Tag)ItemTags.BEACON_PAYMENT_ITEMS);
/*     */     }
/*     */ 
/*     */     
/*     */     public int getMaxStackSize() {
/* 174 */       return 1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\BeaconMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */