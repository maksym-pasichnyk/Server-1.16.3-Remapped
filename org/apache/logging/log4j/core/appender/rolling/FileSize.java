/*    */ package org.apache.logging.log4j.core.appender.rolling;
/*    */ 
/*    */ import java.text.NumberFormat;
/*    */ import java.text.ParseException;
/*    */ import java.util.Locale;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ public final class FileSize
/*    */ {
/* 33 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */ 
/*    */   
/*    */   private static final long KB = 1024L;
/*    */   
/*    */   private static final long MB = 1048576L;
/*    */   
/*    */   private static final long GB = 1073741824L;
/*    */   
/* 42 */   private static final Pattern VALUE_PATTERN = Pattern.compile("([0-9]+([\\.,][0-9]+)?)\\s*(|K|M|G)B?", 2);
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
/*    */   public static long parse(String string, long defaultValue) {
/* 58 */     Matcher matcher = VALUE_PATTERN.matcher(string);
/*    */ 
/*    */     
/* 61 */     if (matcher.matches()) {
/*    */       
/*    */       try {
/* 64 */         long value = NumberFormat.getNumberInstance(Locale.getDefault()).parse(matcher.group(1)).longValue();
/*    */ 
/*    */ 
/*    */         
/* 68 */         String units = matcher.group(3);
/*    */         
/* 70 */         if (units.isEmpty())
/* 71 */           return value; 
/* 72 */         if (units.equalsIgnoreCase("K"))
/* 73 */           return value * 1024L; 
/* 74 */         if (units.equalsIgnoreCase("M"))
/* 75 */           return value * 1048576L; 
/* 76 */         if (units.equalsIgnoreCase("G")) {
/* 77 */           return value * 1073741824L;
/*    */         }
/* 79 */         LOGGER.error("FileSize units not recognized: " + string);
/* 80 */         return defaultValue;
/*    */       }
/* 82 */       catch (ParseException e) {
/* 83 */         LOGGER.error("FileSize unable to parse numeric part: " + string, e);
/* 84 */         return defaultValue;
/*    */       } 
/*    */     }
/* 87 */     LOGGER.error("FileSize unable to parse bytes: " + string);
/* 88 */     return defaultValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rolling\FileSize.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */