/*     */ package net.minecraft.world;
/*     */ 
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class CompoundContainer implements Container {
/*     */   private final Container container1;
/*     */   private final Container container2;
/*     */   
/*     */   public CompoundContainer(Container debug1, Container debug2) {
/*  11 */     if (debug1 == null) {
/*  12 */       debug1 = debug2;
/*     */     }
/*  14 */     if (debug2 == null) {
/*  15 */       debug2 = debug1;
/*     */     }
/*  17 */     this.container1 = debug1;
/*  18 */     this.container2 = debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/*  23 */     return this.container1.getContainerSize() + this.container2.getContainerSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  28 */     return (this.container1.isEmpty() && this.container2.isEmpty());
/*     */   }
/*     */   
/*     */   public boolean contains(Container debug1) {
/*  32 */     return (this.container1 == debug1 || this.container2 == debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int debug1) {
/*  37 */     if (debug1 >= this.container1.getContainerSize()) {
/*  38 */       return this.container2.getItem(debug1 - this.container1.getContainerSize());
/*     */     }
/*  40 */     return this.container1.getItem(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int debug1, int debug2) {
/*  46 */     if (debug1 >= this.container1.getContainerSize()) {
/*  47 */       return this.container2.removeItem(debug1 - this.container1.getContainerSize(), debug2);
/*     */     }
/*  49 */     return this.container1.removeItem(debug1, debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int debug1) {
/*  55 */     if (debug1 >= this.container1.getContainerSize()) {
/*  56 */       return this.container2.removeItemNoUpdate(debug1 - this.container1.getContainerSize());
/*     */     }
/*  58 */     return this.container1.removeItemNoUpdate(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItem(int debug1, ItemStack debug2) {
/*  64 */     if (debug1 >= this.container1.getContainerSize()) {
/*  65 */       this.container2.setItem(debug1 - this.container1.getContainerSize(), debug2);
/*     */     } else {
/*  67 */       this.container1.setItem(debug1, debug2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxStackSize() {
/*  73 */     return this.container1.getMaxStackSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setChanged() {
/*  78 */     this.container1.setChanged();
/*  79 */     this.container2.setChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player debug1) {
/*  84 */     return (this.container1.stillValid(debug1) && this.container2.stillValid(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public void startOpen(Player debug1) {
/*  89 */     this.container1.startOpen(debug1);
/*  90 */     this.container2.startOpen(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stopOpen(Player debug1) {
/*  95 */     this.container1.stopOpen(debug1);
/*  96 */     this.container2.stopOpen(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceItem(int debug1, ItemStack debug2) {
/* 101 */     if (debug1 >= this.container1.getContainerSize()) {
/* 102 */       return this.container2.canPlaceItem(debug1 - this.container1.getContainerSize(), debug2);
/*     */     }
/* 104 */     return this.container1.canPlaceItem(debug1, debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 110 */     this.container1.clearContent();
/* 111 */     this.container2.clearContent();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\CompoundContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */