/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SystemPropertyUtil
/*     */ {
/*  29 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(SystemPropertyUtil.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean contains(String key) {
/*  36 */     return (get(key) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String get(String key) {
/*  46 */     return get(key, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String get(final String key, String def) {
/*  59 */     if (key == null) {
/*  60 */       throw new NullPointerException("key");
/*     */     }
/*  62 */     if (key.isEmpty()) {
/*  63 */       throw new IllegalArgumentException("key must not be empty.");
/*     */     }
/*     */     
/*  66 */     String value = null;
/*     */     try {
/*  68 */       if (System.getSecurityManager() == null) {
/*  69 */         value = System.getProperty(key);
/*     */       } else {
/*  71 */         value = AccessController.<String>doPrivileged(new PrivilegedAction<String>()
/*     */             {
/*     */               public String run() {
/*  74 */                 return System.getProperty(key);
/*     */               }
/*     */             });
/*     */       } 
/*  78 */     } catch (SecurityException e) {
/*  79 */       logger.warn("Unable to retrieve a system property '{}'; default values will be used.", key, e);
/*     */     } 
/*     */     
/*  82 */     if (value == null) {
/*  83 */       return def;
/*     */     }
/*     */     
/*  86 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean getBoolean(String key, boolean def) {
/*  99 */     String value = get(key);
/* 100 */     if (value == null) {
/* 101 */       return def;
/*     */     }
/*     */     
/* 104 */     value = value.trim().toLowerCase();
/* 105 */     if (value.isEmpty()) {
/* 106 */       return def;
/*     */     }
/*     */     
/* 109 */     if ("true".equals(value) || "yes".equals(value) || "1".equals(value)) {
/* 110 */       return true;
/*     */     }
/*     */     
/* 113 */     if ("false".equals(value) || "no".equals(value) || "0".equals(value)) {
/* 114 */       return false;
/*     */     }
/*     */     
/* 117 */     logger.warn("Unable to parse the boolean system property '{}':{} - using the default value: {}", new Object[] { key, value, 
/*     */           
/* 119 */           Boolean.valueOf(def) });
/*     */ 
/*     */     
/* 122 */     return def;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getInt(String key, int def) {
/* 135 */     String value = get(key);
/* 136 */     if (value == null) {
/* 137 */       return def;
/*     */     }
/*     */     
/* 140 */     value = value.trim();
/*     */     try {
/* 142 */       return Integer.parseInt(value);
/* 143 */     } catch (Exception exception) {
/*     */ 
/*     */ 
/*     */       
/* 147 */       logger.warn("Unable to parse the integer system property '{}':{} - using the default value: {}", new Object[] { key, value, 
/*     */             
/* 149 */             Integer.valueOf(def) });
/*     */ 
/*     */       
/* 152 */       return def;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getLong(String key, long def) {
/* 165 */     String value = get(key);
/* 166 */     if (value == null) {
/* 167 */       return def;
/*     */     }
/*     */     
/* 170 */     value = value.trim();
/*     */     try {
/* 172 */       return Long.parseLong(value);
/* 173 */     } catch (Exception exception) {
/*     */ 
/*     */ 
/*     */       
/* 177 */       logger.warn("Unable to parse the long integer system property '{}':{} - using the default value: {}", new Object[] { key, value, 
/*     */             
/* 179 */             Long.valueOf(def) });
/*     */ 
/*     */       
/* 182 */       return def;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\SystemPropertyUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */