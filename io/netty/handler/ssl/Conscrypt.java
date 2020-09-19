/*    */ package io.netty.handler.ssl;
/*    */ 
/*    */ import io.netty.util.internal.PlatformDependent;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import javax.net.ssl.SSLEngine;
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
/*    */ final class Conscrypt
/*    */ {
/* 30 */   private static final Method IS_CONSCRYPT_SSLENGINE = loadIsConscryptEngine();
/*    */   
/*    */   private static Method loadIsConscryptEngine() {
/*    */     try {
/* 34 */       Class<?> conscryptClass = Class.forName("org.conscrypt.Conscrypt", true, ConscryptAlpnSslEngine.class
/* 35 */           .getClassLoader());
/* 36 */       return conscryptClass.getMethod("isConscrypt", new Class[] { SSLEngine.class });
/* 37 */     } catch (Throwable ignore) {
/*    */       
/* 39 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static boolean isAvailable() {
/* 47 */     return (IS_CONSCRYPT_SSLENGINE != null && PlatformDependent.javaVersion() >= 8);
/*    */   }
/*    */   
/*    */   static boolean isEngineSupported(SSLEngine engine) {
/* 51 */     return (isAvailable() && isConscryptEngine(engine));
/*    */   }
/*    */   
/*    */   private static boolean isConscryptEngine(SSLEngine engine) {
/*    */     try {
/* 56 */       return ((Boolean)IS_CONSCRYPT_SSLENGINE.invoke(null, new Object[] { engine })).booleanValue();
/* 57 */     } catch (IllegalAccessException ignore) {
/* 58 */       return false;
/* 59 */     } catch (InvocationTargetException ex) {
/* 60 */       throw new RuntimeException(ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\Conscrypt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */