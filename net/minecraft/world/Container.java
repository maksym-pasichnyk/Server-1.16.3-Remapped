/*    */ package net.minecraft.world;
/*    */ 
/*    */ import java.util.Set;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ public interface Container
/*    */   extends Clearable
/*    */ {
/*    */   int getContainerSize();
/*    */   
/*    */   boolean isEmpty();
/*    */   
/*    */   ItemStack getItem(int paramInt);
/*    */   
/*    */   ItemStack removeItem(int paramInt1, int paramInt2);
/*    */   
/*    */   ItemStack removeItemNoUpdate(int paramInt);
/*    */   
/*    */   void setItem(int paramInt, ItemStack paramItemStack);
/*    */   
/*    */   default int getMaxStackSize() {
/* 25 */     return 64;
/*    */   }
/*    */ 
/*    */   
/*    */   void setChanged();
/*    */ 
/*    */   
/*    */   boolean stillValid(Player paramPlayer);
/*    */   
/*    */   default void startOpen(Player debug1) {}
/*    */   
/*    */   default void stopOpen(Player debug1) {}
/*    */   
/*    */   default boolean canPlaceItem(int debug1, ItemStack debug2) {
/* 39 */     return true;
/*    */   }
/*    */   
/*    */   default int countItem(Item debug1) {
/* 43 */     int debug2 = 0;
/* 44 */     for (int debug3 = 0; debug3 < getContainerSize(); debug3++) {
/* 45 */       ItemStack debug4 = getItem(debug3);
/* 46 */       if (debug4.getItem().equals(debug1)) {
/* 47 */         debug2 += debug4.getCount();
/*    */       }
/*    */     } 
/* 50 */     return debug2;
/*    */   }
/*    */   
/*    */   default boolean hasAnyOf(Set<Item> debug1) {
/* 54 */     for (int debug2 = 0; debug2 < getContainerSize(); debug2++) {
/* 55 */       ItemStack debug3 = getItem(debug2);
/* 56 */       if (debug1.contains(debug3.getItem()) && debug3.getCount() > 0) {
/* 57 */         return true;
/*    */       }
/*    */     } 
/* 60 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\Container.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */