/*    */ package io.netty.util.internal.logging;
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
/*    */ public abstract class InternalLoggerFactory
/*    */ {
/*    */   private static volatile InternalLoggerFactory defaultFactory;
/*    */   
/*    */   private static InternalLoggerFactory newDefaultFactory(String name) {
/*    */     InternalLoggerFactory f;
/*    */     try {
/* 42 */       f = new Slf4JLoggerFactory(true);
/* 43 */       f.newInstance(name).debug("Using SLF4J as the default logging framework");
/* 44 */     } catch (Throwable t1) {
/*    */       try {
/* 46 */         f = Log4JLoggerFactory.INSTANCE;
/* 47 */         f.newInstance(name).debug("Using Log4J as the default logging framework");
/* 48 */       } catch (Throwable t2) {
/* 49 */         f = JdkLoggerFactory.INSTANCE;
/* 50 */         f.newInstance(name).debug("Using java.util.logging as the default logging framework");
/*    */       } 
/*    */     } 
/* 53 */     return f;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static InternalLoggerFactory getDefaultFactory() {
/* 61 */     if (defaultFactory == null) {
/* 62 */       defaultFactory = newDefaultFactory(InternalLoggerFactory.class.getName());
/*    */     }
/* 64 */     return defaultFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setDefaultFactory(InternalLoggerFactory defaultFactory) {
/* 71 */     if (defaultFactory == null) {
/* 72 */       throw new NullPointerException("defaultFactory");
/*    */     }
/* 74 */     InternalLoggerFactory.defaultFactory = defaultFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static InternalLogger getInstance(Class<?> clazz) {
/* 81 */     return getInstance(clazz.getName());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static InternalLogger getInstance(String name) {
/* 88 */     return getDefaultFactory().newInstance(name);
/*    */   }
/*    */   
/*    */   protected abstract InternalLogger newInstance(String paramString);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\logging\InternalLoggerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */