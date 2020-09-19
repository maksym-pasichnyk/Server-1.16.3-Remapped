/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.Properties;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.Strings;
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
/*     */ public final class OptionConverter
/*     */ {
/*  33 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private static final String DELIM_START = "${";
/*     */   
/*     */   private static final char DELIM_STOP = '}';
/*     */   
/*     */   private static final int DELIM_START_LEN = 2;
/*     */   
/*     */   private static final int DELIM_STOP_LEN = 1;
/*     */   
/*     */   private static final int ONE_K = 1024;
/*     */ 
/*     */   
/*     */   public static String[] concatenateArrays(String[] l, String[] r) {
/*  48 */     int len = l.length + r.length;
/*  49 */     String[] a = new String[len];
/*     */     
/*  51 */     System.arraycopy(l, 0, a, 0, l.length);
/*  52 */     System.arraycopy(r, 0, a, l.length, r.length);
/*     */     
/*  54 */     return a;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String convertSpecialChars(String s) {
/*  59 */     int len = s.length();
/*  60 */     StringBuilder sbuf = new StringBuilder(len);
/*     */     
/*  62 */     int i = 0;
/*  63 */     while (i < len) {
/*  64 */       char c = s.charAt(i++);
/*  65 */       if (c == '\\') {
/*  66 */         c = s.charAt(i++);
/*  67 */         switch (c) {
/*     */           case 'n':
/*  69 */             c = '\n';
/*     */             break;
/*     */           case 'r':
/*  72 */             c = '\r';
/*     */             break;
/*     */           case 't':
/*  75 */             c = '\t';
/*     */             break;
/*     */           case 'f':
/*  78 */             c = '\f';
/*     */             break;
/*     */           case 'b':
/*  81 */             c = '\b';
/*     */             break;
/*     */           case '"':
/*  84 */             c = '"';
/*     */             break;
/*     */           case '\'':
/*  87 */             c = '\'';
/*     */             break;
/*     */           case '\\':
/*  90 */             c = '\\';
/*     */             break;
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/*  96 */       sbuf.append(c);
/*     */     } 
/*  98 */     return sbuf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object instantiateByKey(Properties props, String key, Class<?> superClass, Object defaultValue) {
/* 105 */     String className = findAndSubst(key, props);
/* 106 */     if (className == null) {
/* 107 */       LOGGER.error("Could not find value for key {}", key);
/* 108 */       return defaultValue;
/*     */     } 
/*     */     
/* 111 */     return instantiateByClassName(className.trim(), superClass, defaultValue);
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
/*     */   public static boolean toBoolean(String value, boolean defaultValue) {
/* 127 */     if (value == null) {
/* 128 */       return defaultValue;
/*     */     }
/* 130 */     String trimmedVal = value.trim();
/* 131 */     if ("true".equalsIgnoreCase(trimmedVal)) {
/* 132 */       return true;
/*     */     }
/* 134 */     if ("false".equalsIgnoreCase(trimmedVal)) {
/* 135 */       return false;
/*     */     }
/* 137 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int toInt(String value, int defaultValue) {
/* 147 */     if (value != null) {
/* 148 */       String s = value.trim();
/*     */       try {
/* 150 */         return Integer.parseInt(s);
/* 151 */       } catch (NumberFormatException e) {
/* 152 */         LOGGER.error("[{}] is not in proper int form.", s, e);
/*     */       } 
/*     */     } 
/* 155 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long toFileSize(String value, long defaultValue) {
/* 165 */     if (value == null) {
/* 166 */       return defaultValue;
/*     */     }
/*     */     
/* 169 */     String str = value.trim().toUpperCase(Locale.ENGLISH);
/* 170 */     long multiplier = 1L;
/*     */     
/*     */     int index;
/* 173 */     if ((index = str.indexOf("KB")) != -1) {
/* 174 */       multiplier = 1024L;
/* 175 */       str = str.substring(0, index);
/* 176 */     } else if ((index = str.indexOf("MB")) != -1) {
/* 177 */       multiplier = 1048576L;
/* 178 */       str = str.substring(0, index);
/* 179 */     } else if ((index = str.indexOf("GB")) != -1) {
/* 180 */       multiplier = 1073741824L;
/* 181 */       str = str.substring(0, index);
/*     */     } 
/*     */     try {
/* 184 */       return Long.parseLong(str) * multiplier;
/* 185 */     } catch (NumberFormatException e) {
/* 186 */       LOGGER.error("[{}] is not in proper int form.", str);
/* 187 */       LOGGER.error("[{}] not in expected format.", value, e);
/*     */       
/* 189 */       return defaultValue;
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
/*     */   public static String findAndSubst(String key, Properties props) {
/* 201 */     String value = props.getProperty(key);
/* 202 */     if (value == null) {
/* 203 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 207 */       return substVars(value, props);
/* 208 */     } catch (IllegalArgumentException e) {
/* 209 */       LOGGER.error("Bad option value [{}].", value, e);
/* 210 */       return value;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object instantiateByClassName(String className, Class<?> superClass, Object defaultValue) {
/* 227 */     if (className != null) {
/*     */       try {
/* 229 */         Class<?> classObj = LoaderUtil.loadClass(className);
/* 230 */         if (!superClass.isAssignableFrom(classObj)) {
/* 231 */           LOGGER.error("A \"{}\" object is not assignable to a \"{}\" variable.", className, superClass.getName());
/*     */           
/* 233 */           LOGGER.error("The class \"{}\" was loaded by [{}] whereas object of type [{}] was loaded by [{}].", superClass.getName(), superClass.getClassLoader(), classObj.getName());
/*     */           
/* 235 */           return defaultValue;
/*     */         } 
/* 237 */         return classObj.newInstance();
/* 238 */       } catch (Exception e) {
/* 239 */         LOGGER.error("Could not instantiate class [{}].", className, e);
/*     */       } 
/*     */     }
/* 242 */     return defaultValue;
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
/*     */   public static String substVars(String val, Properties props) throws IllegalArgumentException {
/* 285 */     StringBuilder sbuf = new StringBuilder();
/*     */     
/* 287 */     int i = 0;
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 292 */       int j = val.indexOf("${", i);
/* 293 */       if (j == -1) {
/*     */         
/* 295 */         if (i == 0) {
/* 296 */           return val;
/*     */         }
/*     */         
/* 299 */         sbuf.append(val.substring(i, val.length()));
/* 300 */         return sbuf.toString();
/*     */       } 
/* 302 */       sbuf.append(val.substring(i, j));
/* 303 */       int k = val.indexOf('}', j);
/* 304 */       if (k == -1) {
/* 305 */         throw new IllegalArgumentException(Strings.dquote(val) + " has no closing brace. Opening brace at position " + j + '.');
/*     */       }
/*     */ 
/*     */       
/* 309 */       j += 2;
/* 310 */       String key = val.substring(j, k);
/*     */       
/* 312 */       String replacement = PropertiesUtil.getProperties().getStringProperty(key, null);
/*     */       
/* 314 */       if (replacement == null && props != null) {
/* 315 */         replacement = props.getProperty(key);
/*     */       }
/*     */       
/* 318 */       if (replacement != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 324 */         String recursiveReplacement = substVars(replacement, props);
/* 325 */         sbuf.append(recursiveReplacement);
/*     */       } 
/* 327 */       i = k + 1;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\OptionConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */