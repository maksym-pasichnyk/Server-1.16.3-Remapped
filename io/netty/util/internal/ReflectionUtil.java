/*    */ package io.netty.util.internal;
/*    */ 
/*    */ import java.lang.reflect.AccessibleObject;
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
/*    */ public final class ReflectionUtil
/*    */ {
/*    */   public static Throwable trySetAccessible(AccessibleObject object, boolean checkAccessible) {
/* 30 */     if (checkAccessible && !PlatformDependent0.isExplicitTryReflectionSetAccessible()) {
/* 31 */       return new UnsupportedOperationException("Reflective setAccessible(true) disabled");
/*    */     }
/*    */     try {
/* 34 */       object.setAccessible(true);
/* 35 */       return null;
/* 36 */     } catch (SecurityException e) {
/* 37 */       return e;
/* 38 */     } catch (RuntimeException e) {
/* 39 */       return handleInaccessibleObjectException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static RuntimeException handleInaccessibleObjectException(RuntimeException e) {
/* 47 */     if ("java.lang.reflect.InaccessibleObjectException".equals(e.getClass().getName())) {
/* 48 */       return e;
/*    */     }
/* 50 */     throw e;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\ReflectionUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */