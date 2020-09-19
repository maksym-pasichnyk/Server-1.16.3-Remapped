/*     */ package org.apache.commons.lang3.time;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.Format;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class FormatCache<F extends Format>
/*     */ {
/*     */   static final int NONE = -1;
/*  40 */   private final ConcurrentMap<MultipartKey, F> cInstanceCache = new ConcurrentHashMap<MultipartKey, F>(7);
/*     */ 
/*     */   
/*  43 */   private static final ConcurrentMap<MultipartKey, String> cDateTimeInstanceCache = new ConcurrentHashMap<MultipartKey, String>(7);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public F getInstance() {
/*  53 */     return getDateTimeInstance(3, 3, TimeZone.getDefault(), Locale.getDefault());
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
/*     */ 
/*     */ 
/*     */   
/*     */   public F getInstance(String pattern, TimeZone timeZone, Locale locale) {
/*  69 */     if (pattern == null) {
/*  70 */       throw new NullPointerException("pattern must not be null");
/*     */     }
/*  72 */     if (timeZone == null) {
/*  73 */       timeZone = TimeZone.getDefault();
/*     */     }
/*  75 */     if (locale == null) {
/*  76 */       locale = Locale.getDefault();
/*     */     }
/*  78 */     MultipartKey key = new MultipartKey(new Object[] { pattern, timeZone, locale });
/*  79 */     Format format = (Format)this.cInstanceCache.get(key);
/*  80 */     if (format == null) {
/*  81 */       format = (Format)createInstance(pattern, timeZone, locale);
/*  82 */       Format format1 = (Format)this.cInstanceCache.putIfAbsent(key, (F)format);
/*  83 */       if (format1 != null)
/*     */       {
/*     */         
/*  86 */         format = format1;
/*     */       }
/*     */     } 
/*  89 */     return (F)format;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract F createInstance(String paramString, TimeZone paramTimeZone, Locale paramLocale);
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
/*     */   private F getDateTimeInstance(Integer dateStyle, Integer timeStyle, TimeZone timeZone, Locale locale) {
/* 120 */     if (locale == null) {
/* 121 */       locale = Locale.getDefault();
/*     */     }
/* 123 */     String pattern = getPatternForStyle(dateStyle, timeStyle, locale);
/* 124 */     return getInstance(pattern, timeZone, locale);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   F getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale) {
/* 142 */     return getDateTimeInstance(Integer.valueOf(dateStyle), Integer.valueOf(timeStyle), timeZone, locale);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   F getDateInstance(int dateStyle, TimeZone timeZone, Locale locale) {
/* 159 */     return getDateTimeInstance(Integer.valueOf(dateStyle), (Integer)null, timeZone, locale);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   F getTimeInstance(int timeStyle, TimeZone timeZone, Locale locale) {
/* 176 */     return getDateTimeInstance((Integer)null, Integer.valueOf(timeStyle), timeZone, locale);
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
/*     */   
/*     */   static String getPatternForStyle(Integer dateStyle, Integer timeStyle, Locale locale) {
/* 190 */     MultipartKey key = new MultipartKey(new Object[] { dateStyle, timeStyle, locale });
/*     */     
/* 192 */     String pattern = cDateTimeInstanceCache.get(key);
/* 193 */     if (pattern == null) {
/*     */       try {
/*     */         DateFormat formatter;
/* 196 */         if (dateStyle == null) {
/* 197 */           formatter = DateFormat.getTimeInstance(timeStyle.intValue(), locale);
/*     */         }
/* 199 */         else if (timeStyle == null) {
/* 200 */           formatter = DateFormat.getDateInstance(dateStyle.intValue(), locale);
/*     */         } else {
/*     */           
/* 203 */           formatter = DateFormat.getDateTimeInstance(dateStyle.intValue(), timeStyle.intValue(), locale);
/*     */         } 
/* 205 */         pattern = ((SimpleDateFormat)formatter).toPattern();
/* 206 */         String previous = cDateTimeInstanceCache.putIfAbsent(key, pattern);
/* 207 */         if (previous != null)
/*     */         {
/*     */ 
/*     */           
/* 211 */           pattern = previous;
/*     */         }
/* 213 */       } catch (ClassCastException ex) {
/* 214 */         throw new IllegalArgumentException("No date time pattern for locale: " + locale);
/*     */       } 
/*     */     }
/* 217 */     return pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MultipartKey
/*     */   {
/*     */     private final Object[] keys;
/*     */ 
/*     */     
/*     */     private int hashCode;
/*     */ 
/*     */ 
/*     */     
/*     */     public MultipartKey(Object... keys) {
/* 233 */       this.keys = keys;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 244 */       return Arrays.equals(this.keys, ((MultipartKey)obj).keys);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 252 */       if (this.hashCode == 0) {
/* 253 */         int rc = 0;
/* 254 */         for (Object key : this.keys) {
/* 255 */           if (key != null) {
/* 256 */             rc = rc * 7 + key.hashCode();
/*     */           }
/*     */         } 
/* 259 */         this.hashCode = rc;
/*     */       } 
/* 261 */       return this.hashCode;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\time\FormatCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */