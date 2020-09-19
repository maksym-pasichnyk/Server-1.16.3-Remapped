/*    */ package net.minecraft.recipebook;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.item.crafting.ShapedRecipe;
/*    */ 
/*    */ public interface PlaceRecipe<T>
/*    */ {
/*    */   default void placeRecipe(int debug1, int debug2, int debug3, Recipe<?> debug4, Iterator<T> debug5, int debug6) {
/* 11 */     int debug7 = debug1;
/* 12 */     int debug8 = debug2;
/*    */     
/* 14 */     if (debug4 instanceof ShapedRecipe) {
/* 15 */       ShapedRecipe shapedRecipe = (ShapedRecipe)debug4;
/* 16 */       debug7 = shapedRecipe.getWidth();
/* 17 */       debug8 = shapedRecipe.getHeight();
/*    */     } 
/*    */     
/* 20 */     int debug9 = 0;
/* 21 */     for (int debug10 = 0; debug10 < debug2; debug10++) {
/* 22 */       if (debug9 == debug3) {
/* 23 */         debug9++;
/*    */       }
/*    */       
/* 26 */       boolean debug11 = (debug8 < debug2 / 2.0F);
/* 27 */       int debug12 = Mth.floor(debug2 / 2.0F - debug8 / 2.0F);
/*    */       
/* 29 */       if (debug11 && debug12 > debug10) {
/* 30 */         debug9 += debug1;
/* 31 */         debug10++;
/*    */       } 
/*    */       
/* 34 */       for (int debug13 = 0; debug13 < debug1; debug13++) {
/* 35 */         if (!debug5.hasNext()) {
/*    */           return;
/*    */         }
/*    */         
/* 39 */         debug11 = (debug7 < debug1 / 2.0F);
/* 40 */         debug12 = Mth.floor(debug1 / 2.0F - debug7 / 2.0F);
/* 41 */         int debug14 = debug7;
/* 42 */         boolean debug15 = (debug13 < debug7);
/* 43 */         if (debug11) {
/* 44 */           debug14 = debug12 + debug7;
/* 45 */           debug15 = (debug12 <= debug13 && debug13 < debug12 + debug7);
/*    */         } 
/*    */ 
/*    */         
/* 49 */         if (debug15) {
/* 50 */           addItemToSlot(debug5, debug9, debug6, debug10, debug13);
/* 51 */         } else if (debug14 == debug13) {
/* 52 */           debug9 += debug1 - debug13;
/*    */           
/*    */           break;
/*    */         } 
/* 56 */         debug9++;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   void addItemToSlot(Iterator<T> paramIterator, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\recipebook\PlaceRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */