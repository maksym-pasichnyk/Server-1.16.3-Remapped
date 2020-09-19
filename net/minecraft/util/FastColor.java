/*    */ package net.minecraft.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FastColor
/*    */ {
/*    */   public static class ARGB32
/*    */   {
/*    */     public static int red(int debug0) {
/* 10 */       return debug0 >> 16 & 0xFF;
/*    */     }
/*    */     
/*    */     public static int green(int debug0) {
/* 14 */       return debug0 >> 8 & 0xFF;
/*    */     }
/*    */     
/*    */     public static int blue(int debug0) {
/* 18 */       return debug0 & 0xFF;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\FastColor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */