/*    */ package org.apache.logging.log4j.core.config.plugins.convert;
/*    */ 
/*    */ import java.lang.invoke.MethodHandle;
/*    */ import java.lang.invoke.MethodHandles;
/*    */ import java.lang.invoke.MethodType;
/*    */ import java.sql.Date;
/*    */ import java.sql.Time;
/*    */ import java.sql.Timestamp;
/*    */ import java.util.Arrays;
/*    */ import java.util.Date;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
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
/*    */ public final class DateTypeConverter
/*    */ {
/* 34 */   private static final Map<Class<? extends Date>, MethodHandle> CONSTRUCTORS = new ConcurrentHashMap<>();
/*    */   
/*    */   static {
/* 37 */     MethodHandles.Lookup lookup = MethodHandles.publicLookup();
/* 38 */     for (Class<? extends Date> dateClass : Arrays.<Class<?>[]>asList((Class<?>[][])new Class[] { Date.class, Date.class, Time.class, Timestamp.class })) {
/*    */       
/*    */       try {
/* 41 */         CONSTRUCTORS.put(dateClass, lookup.findConstructor(dateClass, MethodType.methodType(void.class, long.class)));
/*    */       }
/* 43 */       catch (NoSuchMethodException|IllegalAccessException noSuchMethodException) {}
/*    */     } 
/*    */   }
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
/*    */   public static <D extends Date> D fromMillis(long millis, Class<D> type) {
/*    */     try {
/* 60 */       return (D)((MethodHandle)CONSTRUCTORS.get(type)).invoke(millis);
/* 61 */     } catch (Throwable ignored) {
/* 62 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\plugins\convert\DateTypeConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */