/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.npc.ClientSideMerchant;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.trading.Merchant;
/*     */ import net.minecraft.world.item.trading.MerchantOffer;
/*     */ import net.minecraft.world.item.trading.MerchantOffers;
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
/*     */ 
/*     */ public class MerchantMenu
/*     */   extends AbstractContainerMenu
/*     */ {
/*     */   private final Merchant trader;
/*     */   private final MerchantContainer tradeContainer;
/*     */   
/*     */   public MerchantMenu(int debug1, Inventory debug2) {
/*  37 */     this(debug1, debug2, (Merchant)new ClientSideMerchant(debug2.player));
/*     */   }
/*     */   
/*     */   public MerchantMenu(int debug1, Inventory debug2, Merchant debug3) {
/*  41 */     super(MenuType.MERCHANT, debug1);
/*  42 */     this.trader = debug3;
/*     */     
/*  44 */     this.tradeContainer = new MerchantContainer(debug3);
/*  45 */     addSlot(new Slot(this.tradeContainer, 0, 136, 37));
/*  46 */     addSlot(new Slot(this.tradeContainer, 1, 162, 37));
/*  47 */     addSlot(new MerchantResultSlot(debug2.player, debug3, this.tradeContainer, 2, 220, 37));
/*     */     int debug4;
/*  49 */     for (debug4 = 0; debug4 < 3; debug4++) {
/*  50 */       for (int debug5 = 0; debug5 < 9; debug5++) {
/*  51 */         addSlot(new Slot((Container)debug2, debug5 + debug4 * 9 + 9, 108 + debug5 * 18, 84 + debug4 * 18));
/*     */       }
/*     */     } 
/*  54 */     for (debug4 = 0; debug4 < 9; debug4++) {
/*  55 */       addSlot(new Slot((Container)debug2, debug4, 108 + debug4 * 18, 142));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void slotsChanged(Container debug1) {
/*  65 */     this.tradeContainer.updateSellItem();
/*  66 */     super.slotsChanged(debug1);
/*     */   }
/*     */   
/*     */   public void setSelectionHint(int debug1) {
/*  70 */     this.tradeContainer.setSelectionHint(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player debug1) {
/*  75 */     return (this.trader.getTradingPlayer() == debug1);
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
/*     */   public boolean canTakeItemForPickAll(ItemStack debug1, Slot debug2) {
/* 108 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player debug1, int debug2) {
/* 113 */     ItemStack debug3 = ItemStack.EMPTY;
/* 114 */     Slot debug4 = this.slots.get(debug2);
/* 115 */     if (debug4 != null && debug4.hasItem()) {
/* 116 */       ItemStack debug5 = debug4.getItem();
/* 117 */       debug3 = debug5.copy();
/*     */       
/* 119 */       if (debug2 == 2) {
/* 120 */         if (!moveItemStackTo(debug5, 3, 39, true)) {
/* 121 */           return ItemStack.EMPTY;
/*     */         }
/* 123 */         debug4.onQuickCraft(debug5, debug3);
/*     */         
/* 125 */         playTradeSound();
/* 126 */       } else if (debug2 == 0 || debug2 == 1) {
/* 127 */         if (!moveItemStackTo(debug5, 3, 39, false)) {
/* 128 */           return ItemStack.EMPTY;
/*     */         }
/* 130 */       } else if (debug2 >= 3 && debug2 < 30) {
/* 131 */         if (!moveItemStackTo(debug5, 30, 39, false)) {
/* 132 */           return ItemStack.EMPTY;
/*     */         }
/* 134 */       } else if (debug2 >= 30 && debug2 < 39 && 
/* 135 */         !moveItemStackTo(debug5, 3, 30, false)) {
/* 136 */         return ItemStack.EMPTY;
/*     */       } 
/*     */       
/* 139 */       if (debug5.isEmpty()) {
/* 140 */         debug4.set(ItemStack.EMPTY);
/*     */       } else {
/* 142 */         debug4.setChanged();
/*     */       } 
/* 144 */       if (debug5.getCount() == debug3.getCount()) {
/* 145 */         return ItemStack.EMPTY;
/*     */       }
/* 147 */       debug4.onTake(debug1, debug5);
/*     */     } 
/*     */     
/* 150 */     return debug3;
/*     */   }
/*     */   
/*     */   private void playTradeSound() {
/* 154 */     if (!(this.trader.getLevel()).isClientSide) {
/* 155 */       Entity debug1 = (Entity)this.trader;
/* 156 */       this.trader.getLevel().playLocalSound(debug1.getX(), debug1.getY(), debug1.getZ(), this.trader.getNotifyTradeSound(), SoundSource.NEUTRAL, 1.0F, 1.0F, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player debug1) {
/* 162 */     super.removed(debug1);
/* 163 */     this.trader.setTradingPlayer(null);
/*     */     
/* 165 */     if ((this.trader.getLevel()).isClientSide) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 170 */     if (!debug1.isAlive() || (debug1 instanceof ServerPlayer && ((ServerPlayer)debug1).hasDisconnected())) {
/* 171 */       ItemStack debug2 = this.tradeContainer.removeItemNoUpdate(0);
/* 172 */       if (!debug2.isEmpty()) {
/* 173 */         debug1.drop(debug2, false);
/*     */       }
/* 175 */       debug2 = this.tradeContainer.removeItemNoUpdate(1);
/* 176 */       if (!debug2.isEmpty()) {
/* 177 */         debug1.drop(debug2, false);
/*     */       }
/*     */     } else {
/* 180 */       debug1.inventory.placeItemBackInInventory(debug1.level, this.tradeContainer.removeItemNoUpdate(0));
/* 181 */       debug1.inventory.placeItemBackInInventory(debug1.level, this.tradeContainer.removeItemNoUpdate(1));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tryMoveItems(int debug1) {
/* 186 */     if (getOffers().size() <= debug1) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 191 */     ItemStack debug2 = this.tradeContainer.getItem(0);
/* 192 */     if (!debug2.isEmpty()) {
/* 193 */       if (!moveItemStackTo(debug2, 3, 39, true)) {
/*     */         return;
/*     */       }
/*     */       
/* 197 */       this.tradeContainer.setItem(0, debug2);
/*     */     } 
/*     */     
/* 200 */     ItemStack debug3 = this.tradeContainer.getItem(1);
/* 201 */     if (!debug3.isEmpty()) {
/* 202 */       if (!moveItemStackTo(debug3, 3, 39, true)) {
/*     */         return;
/*     */       }
/*     */       
/* 206 */       this.tradeContainer.setItem(1, debug3);
/*     */     } 
/*     */ 
/*     */     
/* 210 */     if (this.tradeContainer.getItem(0).isEmpty() && this.tradeContainer.getItem(1).isEmpty()) {
/* 211 */       ItemStack debug4 = ((MerchantOffer)getOffers().get(debug1)).getCostA();
/* 212 */       moveFromInventoryToPaymentSlot(0, debug4);
/*     */       
/* 214 */       ItemStack debug5 = ((MerchantOffer)getOffers().get(debug1)).getCostB();
/* 215 */       moveFromInventoryToPaymentSlot(1, debug5);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void moveFromInventoryToPaymentSlot(int debug1, ItemStack debug2) {
/* 220 */     if (!debug2.isEmpty()) {
/* 221 */       for (int debug3 = 3; debug3 < 39; debug3++) {
/* 222 */         ItemStack debug4 = ((Slot)this.slots.get(debug3)).getItem();
/* 223 */         if (!debug4.isEmpty() && isSameItem(debug2, debug4)) {
/* 224 */           ItemStack debug5 = this.tradeContainer.getItem(debug1);
/* 225 */           int debug6 = debug5.isEmpty() ? 0 : debug5.getCount();
/* 226 */           int debug7 = Math.min(debug2.getMaxStackSize() - debug6, debug4.getCount());
/*     */           
/* 228 */           ItemStack debug8 = debug4.copy();
/* 229 */           int debug9 = debug6 + debug7;
/*     */           
/* 231 */           debug4.shrink(debug7);
/*     */           
/* 233 */           debug8.setCount(debug9);
/* 234 */           this.tradeContainer.setItem(debug1, debug8);
/*     */           
/* 236 */           if (debug9 >= debug2.getMaxStackSize()) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isSameItem(ItemStack debug1, ItemStack debug2) {
/* 245 */     return (debug1.getItem() == debug2.getItem() && ItemStack.tagMatches(debug1, debug2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MerchantOffers getOffers() {
/* 253 */     return this.trader.getOffers();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\MerchantMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */