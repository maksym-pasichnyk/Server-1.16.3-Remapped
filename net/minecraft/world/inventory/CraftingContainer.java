/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.ContainerHelper;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.player.StackedContents;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class CraftingContainer
/*    */   implements Container, StackedContentsCompatible {
/*    */   private final NonNullList<ItemStack> items;
/*    */   private final int width;
/*    */   
/*    */   public CraftingContainer(AbstractContainerMenu debug1, int debug2, int debug3) {
/* 17 */     this.items = NonNullList.withSize(debug2 * debug3, ItemStack.EMPTY);
/* 18 */     this.menu = debug1;
/* 19 */     this.width = debug2;
/* 20 */     this.height = debug3;
/*    */   }
/*    */   private final int height; private final AbstractContainerMenu menu;
/*    */   
/*    */   public int getContainerSize() {
/* 25 */     return this.items.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 30 */     for (ItemStack debug2 : this.items) {
/* 31 */       if (!debug2.isEmpty()) {
/* 32 */         return false;
/*    */       }
/*    */     } 
/* 35 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack getItem(int debug1) {
/* 40 */     if (debug1 >= getContainerSize()) {
/* 41 */       return ItemStack.EMPTY;
/*    */     }
/* 43 */     return (ItemStack)this.items.get(debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack removeItemNoUpdate(int debug1) {
/* 55 */     return ContainerHelper.takeItem((List)this.items, debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack removeItem(int debug1, int debug2) {
/* 60 */     ItemStack debug3 = ContainerHelper.removeItem((List)this.items, debug1, debug2);
/* 61 */     if (!debug3.isEmpty()) {
/* 62 */       this.menu.slotsChanged(this);
/*    */     }
/* 64 */     return debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setItem(int debug1, ItemStack debug2) {
/* 69 */     this.items.set(debug1, debug2);
/* 70 */     this.menu.slotsChanged(this);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setChanged() {}
/*    */ 
/*    */   
/*    */   public boolean stillValid(Player debug1) {
/* 79 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void clearContent() {
/* 84 */     this.items.clear();
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 88 */     return this.height;
/*    */   }
/*    */   
/*    */   public int getWidth() {
/* 92 */     return this.width;
/*    */   }
/*    */ 
/*    */   
/*    */   public void fillStackedContents(StackedContents debug1) {
/* 97 */     for (ItemStack debug3 : this.items)
/* 98 */       debug1.accountSimpleStack(debug3); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\CraftingContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */