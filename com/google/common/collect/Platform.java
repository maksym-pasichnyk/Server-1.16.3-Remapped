/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.lang.reflect.Array;
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
/*    */ @GwtCompatible(emulated = true)
/*    */ final class Platform
/*    */ {
/*    */   static <T> T[] newArray(T[] reference, int length) {
/* 37 */     Class<?> type = reference.getClass().getComponentType();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 42 */     T[] result = (T[])Array.newInstance(type, length);
/* 43 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static MapMaker tryWeakKeys(MapMaker mapMaker) {
/* 53 */     return mapMaker.weakKeys();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\Platform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */