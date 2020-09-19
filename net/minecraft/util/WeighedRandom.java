/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.Util;
/*    */ 
/*    */ public class WeighedRandom
/*    */ {
/*    */   public static int getTotalWeight(List<? extends WeighedRandomItem> debug0) {
/* 10 */     int debug1 = 0;
/* 11 */     for (int debug2 = 0, debug3 = debug0.size(); debug2 < debug3; debug2++) {
/* 12 */       WeighedRandomItem debug4 = debug0.get(debug2);
/* 13 */       debug1 += debug4.weight;
/*    */     } 
/* 15 */     return debug1;
/*    */   }
/*    */   
/*    */   public static <T extends WeighedRandomItem> T getRandomItem(Random debug0, List<T> debug1, int debug2) {
/* 19 */     if (debug2 <= 0) {
/* 20 */       throw (IllegalArgumentException)Util.pauseInIde(new IllegalArgumentException());
/*    */     }
/*    */     
/* 23 */     int debug3 = debug0.nextInt(debug2);
/* 24 */     return getWeightedItem(debug1, debug3);
/*    */   }
/*    */   
/*    */   public static <T extends WeighedRandomItem> T getWeightedItem(List<T> debug0, int debug1) {
/* 28 */     for (int debug2 = 0, debug3 = debug0.size(); debug2 < debug3; debug2++) {
/* 29 */       WeighedRandomItem weighedRandomItem = (WeighedRandomItem)debug0.get(debug2);
/* 30 */       debug1 -= weighedRandomItem.weight;
/* 31 */       if (debug1 < 0) {
/* 32 */         return (T)weighedRandomItem;
/*    */       }
/*    */     } 
/* 35 */     return null;
/*    */   }
/*    */   
/*    */   public static <T extends WeighedRandomItem> T getRandomItem(Random debug0, List<T> debug1) {
/* 39 */     return getRandomItem(debug0, debug1, getTotalWeight(debug1));
/*    */   }
/*    */   
/*    */   public static class WeighedRandomItem {
/*    */     protected final int weight;
/*    */     
/*    */     public WeighedRandomItem(int debug1) {
/* 46 */       this.weight = debug1;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\WeighedRandom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */