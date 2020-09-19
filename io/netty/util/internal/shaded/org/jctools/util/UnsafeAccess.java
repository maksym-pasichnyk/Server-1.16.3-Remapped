/*    */ package io.netty.util.internal.shaded.org.jctools.util;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Field;
/*    */ import sun.misc.Unsafe;
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
/*    */ public class UnsafeAccess
/*    */ {
/*    */   public static final boolean SUPPORTS_GET_AND_SET;
/*    */   public static final Unsafe UNSAFE;
/*    */   
/*    */   static {
/*    */     Unsafe instance;
/*    */     try {
/* 46 */       Field field = Unsafe.class.getDeclaredField("theUnsafe");
/* 47 */       field.setAccessible(true);
/* 48 */       instance = (Unsafe)field.get(null);
/*    */     }
/* 50 */     catch (Exception ignored) {
/*    */ 
/*    */       
/*    */       try {
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 58 */         Constructor<Unsafe> c = Unsafe.class.getDeclaredConstructor(new Class[0]);
/* 59 */         c.setAccessible(true);
/* 60 */         instance = c.newInstance(new Object[0]);
/*    */       }
/* 62 */       catch (Exception e) {
/*    */         
/* 64 */         SUPPORTS_GET_AND_SET = false;
/* 65 */         throw new RuntimeException(e);
/*    */       } 
/*    */     } 
/*    */     
/* 69 */     boolean getAndSetSupport = false;
/*    */     
/*    */     try {
/* 72 */       Unsafe.class.getMethod("getAndSetObject", new Class[] { Object.class, long.class, Object.class });
/* 73 */       getAndSetSupport = true;
/*    */     }
/* 75 */     catch (Exception exception) {}
/*    */ 
/*    */ 
/*    */     
/* 79 */     UNSAFE = instance;
/* 80 */     SUPPORTS_GET_AND_SET = getAndSetSupport;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctool\\util\UnsafeAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */