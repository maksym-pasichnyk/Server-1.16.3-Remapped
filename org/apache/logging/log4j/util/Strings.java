/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Strings
/*     */ {
/*     */   public static final String EMPTY = "";
/*  39 */   public static final String LINE_SEPARATOR = PropertiesUtil.getProperties().getStringProperty("line.separator", "\n");
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
/*     */   public static String dquote(String str) {
/*  53 */     return '"' + str + '"';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isBlank(String s) {
/*  64 */     return (s == null || s.trim().isEmpty());
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
/*     */   public static boolean isEmpty(CharSequence cs) {
/*  93 */     return (cs == null || cs.length() == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNotBlank(String s) {
/* 103 */     return !isBlank(s);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNotEmpty(CharSequence cs) {
/* 127 */     return !isEmpty(cs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String quote(String str) {
/* 137 */     return '\'' + str + '\'';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toRootUpperCase(String str) {
/* 147 */     return str.toUpperCase(Locale.ROOT);
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
/*     */   public static String trimToNull(String str) {
/* 175 */     String ts = (str == null) ? null : str.trim();
/* 176 */     return isEmpty(ts) ? null : ts;
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
/*     */   public static String join(Iterable<?> iterable, char separator) {
/* 191 */     if (iterable == null) {
/* 192 */       return null;
/*     */     }
/* 194 */     return join(iterable.iterator(), separator);
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
/*     */   public static String join(Iterator<?> iterator, char separator) {
/* 211 */     if (iterator == null) {
/* 212 */       return null;
/*     */     }
/* 214 */     if (!iterator.hasNext()) {
/* 215 */       return "";
/*     */     }
/* 217 */     Object first = iterator.next();
/* 218 */     if (!iterator.hasNext()) {
/* 219 */       return Objects.toString(first);
/*     */     }
/*     */ 
/*     */     
/* 223 */     StringBuilder buf = new StringBuilder(256);
/* 224 */     if (first != null) {
/* 225 */       buf.append(first);
/*     */     }
/*     */     
/* 228 */     while (iterator.hasNext()) {
/* 229 */       buf.append(separator);
/* 230 */       Object obj = iterator.next();
/* 231 */       if (obj != null) {
/* 232 */         buf.append(obj);
/*     */       }
/*     */     } 
/*     */     
/* 236 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4\\util\Strings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */