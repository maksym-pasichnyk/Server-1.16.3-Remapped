/*    */ package it.unimi.dsi.fastutil.chars;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface CharComparator
/*    */   extends Comparator<Character>
/*    */ {
/*    */   @Deprecated
/*    */   default int compare(Character ok1, Character ok2) {
/* 51 */     return compare(ok1.charValue(), ok2.charValue());
/*    */   }
/*    */   
/*    */   int compare(char paramChar1, char paramChar2);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\chars\CharComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */