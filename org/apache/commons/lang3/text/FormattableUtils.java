/*     */ package org.apache.commons.lang3.text;
/*     */ 
/*     */ import java.util.Formattable;
/*     */ import java.util.Formatter;
/*     */ import org.apache.commons.lang3.ObjectUtils;
/*     */ import org.apache.commons.lang3.Validate;
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
/*     */ public class FormattableUtils
/*     */ {
/*     */   private static final String SIMPLEST_FORMAT = "%s";
/*     */   
/*     */   public static String toString(Formattable formattable) {
/*  65 */     return String.format("%s", new Object[] { formattable });
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
/*     */   public static Formatter append(CharSequence seq, Formatter formatter, int flags, int width, int precision) {
/*  82 */     return append(seq, formatter, flags, width, precision, ' ', null);
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
/*     */   public static Formatter append(CharSequence seq, Formatter formatter, int flags, int width, int precision, char padChar) {
/*  99 */     return append(seq, formatter, flags, width, precision, padChar, null);
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
/*     */   public static Formatter append(CharSequence seq, Formatter formatter, int flags, int width, int precision, CharSequence ellipsis) {
/* 117 */     return append(seq, formatter, flags, width, precision, ' ', ellipsis);
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
/*     */   public static Formatter append(CharSequence seq, Formatter formatter, int flags, int width, int precision, char padChar, CharSequence ellipsis) {
/* 135 */     Validate.isTrue((ellipsis == null || precision < 0 || ellipsis.length() <= precision), "Specified ellipsis '%1$s' exceeds precision of %2$s", new Object[] { ellipsis, 
/* 136 */           Integer.valueOf(precision) });
/* 137 */     StringBuilder buf = new StringBuilder(seq);
/* 138 */     if (precision >= 0 && precision < seq.length()) {
/* 139 */       CharSequence _ellipsis = (CharSequence)ObjectUtils.defaultIfNull(ellipsis, "");
/* 140 */       buf.replace(precision - _ellipsis.length(), seq.length(), _ellipsis.toString());
/*     */     } 
/* 142 */     boolean leftJustify = ((flags & 0x1) == 1);
/* 143 */     for (int i = buf.length(); i < width; i++) {
/* 144 */       buf.insert(leftJustify ? i : 0, padChar);
/*     */     }
/* 146 */     formatter.format(buf.toString(), new Object[0]);
/* 147 */     return formatter;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\text\FormattableUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */