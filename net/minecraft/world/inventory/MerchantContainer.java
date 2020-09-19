/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.trading.Merchant;
/*     */ import net.minecraft.world.item.trading.MerchantOffer;
/*     */ import net.minecraft.world.item.trading.MerchantOffers;
/*     */ 
/*     */ public class MerchantContainer implements Container {
/*     */   private final Merchant merchant;
/*  16 */   private final NonNullList<ItemStack> itemStacks = NonNullList.withSize(3, ItemStack.EMPTY);
/*     */   @Nullable
/*     */   private MerchantOffer activeOffer;
/*     */   private int selectionHint;
/*     */   private int futureXp;
/*     */   
/*     */   public MerchantContainer(Merchant debug1) {
/*  23 */     this.merchant = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/*  28 */     return this.itemStacks.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  33 */     for (ItemStack debug2 : this.itemStacks) {
/*  34 */       if (!debug2.isEmpty()) {
/*  35 */         return false;
/*     */       }
/*     */     } 
/*  38 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int debug1) {
/*  43 */     return (ItemStack)this.itemStacks.get(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int debug1, int debug2) {
/*  48 */     ItemStack debug3 = (ItemStack)this.itemStacks.get(debug1);
/*  49 */     if (debug1 == 2 && !debug3.isEmpty()) {
/*  50 */       return ContainerHelper.removeItem((List)this.itemStacks, debug1, debug3.getCount());
/*     */     }
/*     */     
/*  53 */     ItemStack debug4 = ContainerHelper.removeItem((List)this.itemStacks, debug1, debug2);
/*  54 */     if (!debug4.isEmpty() && isPaymentSlot(debug1)) {
/*  55 */       updateSellItem();
/*     */     }
/*  57 */     return debug4;
/*     */   }
/*     */   
/*     */   private boolean isPaymentSlot(int debug1) {
/*  61 */     return (debug1 == 0 || debug1 == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int debug1) {
/*  66 */     return ContainerHelper.takeItem((List)this.itemStacks, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int debug1, ItemStack debug2) {
/*  71 */     this.itemStacks.set(debug1, debug2);
/*  72 */     if (!debug2.isEmpty() && debug2.getCount() > getMaxStackSize()) {
/*  73 */       debug2.setCount(getMaxStackSize());
/*     */     }
/*  75 */     if (isPaymentSlot(debug1)) {
/*  76 */       updateSellItem();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player debug1) {
/*  82 */     return (this.merchant.getTradingPlayer() == debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setChanged() {
/*  87 */     updateSellItem();
/*     */   }
/*     */   public void updateSellItem() {
/*     */     ItemStack debug1, debug2;
/*  91 */     this.activeOffer = null;
/*     */ 
/*     */ 
/*     */     
/*  95 */     if (((ItemStack)this.itemStacks.get(0)).isEmpty()) {
/*  96 */       debug1 = (ItemStack)this.itemStacks.get(1);
/*  97 */       debug2 = ItemStack.EMPTY;
/*     */     } else {
/*  99 */       debug1 = (ItemStack)this.itemStacks.get(0);
/* 100 */       debug2 = (ItemStack)this.itemStacks.get(1);
/*     */     } 
/*     */     
/* 103 */     if (debug1.isEmpty()) {
/* 104 */       setItem(2, ItemStack.EMPTY);
/* 105 */       this.futureXp = 0;
/*     */       
/*     */       return;
/*     */     } 
/* 109 */     MerchantOffers debug3 = this.merchant.getOffers();
/* 110 */     if (!debug3.isEmpty()) {
/* 111 */       MerchantOffer debug4 = debug3.getRecipeFor(debug1, debug2, this.selectionHint);
/* 112 */       if (debug4 == null || debug4.isOutOfStock()) {
/*     */         
/* 114 */         this.activeOffer = debug4;
/* 115 */         debug4 = debug3.getRecipeFor(debug2, debug1, this.selectionHint);
/*     */       } 
/*     */       
/* 118 */       if (debug4 != null && !debug4.isOutOfStock()) {
/* 119 */         this.activeOffer = debug4;
/* 120 */         setItem(2, debug4.assemble());
/* 121 */         this.futureXp = debug4.getXp();
/*     */       } else {
/* 123 */         setItem(2, ItemStack.EMPTY);
/* 124 */         this.futureXp = 0;
/*     */       } 
/*     */     } 
/* 127 */     this.merchant.notifyTradeUpdated(getItem(2));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public MerchantOffer getActiveOffer() {
/* 132 */     return this.activeOffer;
/*     */   }
/*     */   
/*     */   public void setSelectionHint(int debug1) {
/* 136 */     this.selectionHint = debug1;
/* 137 */     updateSellItem();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 142 */     this.itemStacks.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\MerchantContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */