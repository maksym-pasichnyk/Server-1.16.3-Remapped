/*    */ package net.minecraft.world;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.ListTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ContainerHelper
/*    */ {
/*    */   public static ItemStack removeItem(List<ItemStack> debug0, int debug1, int debug2) {
/* 14 */     if (debug1 < 0 || debug1 >= debug0.size() || ((ItemStack)debug0.get(debug1)).isEmpty() || debug2 <= 0) {
/* 15 */       return ItemStack.EMPTY;
/*    */     }
/*    */     
/* 18 */     return ((ItemStack)debug0.get(debug1)).split(debug2);
/*    */   }
/*    */   
/*    */   public static ItemStack takeItem(List<ItemStack> debug0, int debug1) {
/* 22 */     if (debug1 < 0 || debug1 >= debug0.size()) {
/* 23 */       return ItemStack.EMPTY;
/*    */     }
/*    */     
/* 26 */     return debug0.set(debug1, ItemStack.EMPTY);
/*    */   }
/*    */   
/*    */   public static CompoundTag saveAllItems(CompoundTag debug0, NonNullList<ItemStack> debug1) {
/* 30 */     return saveAllItems(debug0, debug1, true);
/*    */   }
/*    */   
/*    */   public static CompoundTag saveAllItems(CompoundTag debug0, NonNullList<ItemStack> debug1, boolean debug2) {
/* 34 */     ListTag debug3 = new ListTag();
/* 35 */     for (int debug4 = 0; debug4 < debug1.size(); debug4++) {
/* 36 */       ItemStack debug5 = (ItemStack)debug1.get(debug4);
/* 37 */       if (!debug5.isEmpty()) {
/* 38 */         CompoundTag debug6 = new CompoundTag();
/* 39 */         debug6.putByte("Slot", (byte)debug4);
/* 40 */         debug5.save(debug6);
/* 41 */         debug3.add(debug6);
/*    */       } 
/*    */     } 
/* 44 */     if (!debug3.isEmpty() || debug2) {
/* 45 */       debug0.put("Items", (Tag)debug3);
/*    */     }
/* 47 */     return debug0;
/*    */   }
/*    */   
/*    */   public static void loadAllItems(CompoundTag debug0, NonNullList<ItemStack> debug1) {
/* 51 */     ListTag debug2 = debug0.getList("Items", 10);
/* 52 */     for (int debug3 = 0; debug3 < debug2.size(); debug3++) {
/* 53 */       CompoundTag debug4 = debug2.getCompound(debug3);
/* 54 */       int debug5 = debug4.getByte("Slot") & 0xFF;
/* 55 */       if (debug5 >= 0 && debug5 < debug1.size()) {
/* 56 */         debug1.set(debug5, ItemStack.of(debug4));
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public static int clearOrCountMatchingItems(Container debug0, Predicate<ItemStack> debug1, int debug2, boolean debug3) {
/* 62 */     int debug4 = 0;
/* 63 */     for (int debug5 = 0; debug5 < debug0.getContainerSize(); debug5++) {
/* 64 */       ItemStack debug6 = debug0.getItem(debug5);
/* 65 */       int debug7 = clearOrCountMatchingItems(debug6, debug1, debug2 - debug4, debug3);
/* 66 */       if (debug7 > 0 && !debug3 && debug6.isEmpty()) {
/* 67 */         debug0.setItem(debug5, ItemStack.EMPTY);
/*    */       }
/* 69 */       debug4 += debug7;
/*    */     } 
/* 71 */     return debug4;
/*    */   }
/*    */ 
/*    */   
/*    */   public static int clearOrCountMatchingItems(ItemStack debug0, Predicate<ItemStack> debug1, int debug2, boolean debug3) {
/* 76 */     if (debug0.isEmpty() || !debug1.test(debug0)) {
/* 77 */       return 0;
/*    */     }
/*    */     
/* 80 */     if (debug3) {
/* 81 */       return debug0.getCount();
/*    */     }
/*    */     
/* 84 */     int debug4 = (debug2 < 0) ? debug0.getCount() : Math.min(debug2, debug0.getCount());
/* 85 */     debug0.shrink(debug4);
/* 86 */     return debug4;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\ContainerHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */