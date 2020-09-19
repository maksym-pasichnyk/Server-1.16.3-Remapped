/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Slot
/*     */ {
/*     */   private final int slot;
/*     */   public final Container container;
/*     */   public int index;
/*     */   public final int x;
/*     */   public final int y;
/*     */   
/*     */   public Slot(Container debug1, int debug2, int debug3, int debug4) {
/*  20 */     this.container = debug1;
/*  21 */     this.slot = debug2;
/*  22 */     this.x = debug3;
/*  23 */     this.y = debug4;
/*     */   }
/*     */   
/*     */   public void onQuickCraft(ItemStack debug1, ItemStack debug2) {
/*  27 */     int debug3 = debug2.getCount() - debug1.getCount();
/*  28 */     if (debug3 > 0) {
/*  29 */       onQuickCraft(debug2, debug3);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onQuickCraft(ItemStack debug1, int debug2) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onSwapCraft(int debug1) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkTakeAchievements(ItemStack debug1) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack onTake(Player debug1, ItemStack debug2) {
/*  65 */     setChanged();
/*  66 */     return debug2;
/*     */   }
/*     */   
/*     */   public boolean mayPlace(ItemStack debug1) {
/*  70 */     return true;
/*     */   }
/*     */   
/*     */   public ItemStack getItem() {
/*  74 */     return this.container.getItem(this.slot);
/*     */   }
/*     */   
/*     */   public boolean hasItem() {
/*  78 */     return !getItem().isEmpty();
/*     */   }
/*     */   
/*     */   public void set(ItemStack debug1) {
/*  82 */     this.container.setItem(this.slot, debug1);
/*  83 */     setChanged();
/*     */   }
/*     */   
/*     */   public void setChanged() {
/*  87 */     this.container.setChanged();
/*     */   }
/*     */   
/*     */   public int getMaxStackSize() {
/*  91 */     return this.container.getMaxStackSize();
/*     */   }
/*     */   
/*     */   public int getMaxStackSize(ItemStack debug1) {
/*  95 */     return getMaxStackSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack remove(int debug1) {
/* 104 */     return this.container.removeItem(this.slot, debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean mayPickup(Player debug1) {
/* 112 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\Slot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */