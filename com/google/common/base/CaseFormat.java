/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
/*     */ import javax.annotation.Nullable;
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
/*     */ @GwtCompatible
/*     */ public enum CaseFormat
/*     */ {
/*  35 */   LOWER_HYPHEN(CharMatcher.is('-'), "-")
/*     */   {
/*     */     String normalizeWord(String word) {
/*  38 */       return Ascii.toLowerCase(word);
/*     */     }
/*     */ 
/*     */     
/*     */     String convert(CaseFormat format, String s) {
/*  43 */       if (format == LOWER_UNDERSCORE) {
/*  44 */         return s.replace('-', '_');
/*     */       }
/*  46 */       if (format == UPPER_UNDERSCORE) {
/*  47 */         return Ascii.toUpperCase(s.replace('-', '_'));
/*     */       }
/*  49 */       return super.convert(format, s);
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   LOWER_UNDERSCORE(CharMatcher.is('_'), "_")
/*     */   {
/*     */     String normalizeWord(String word) {
/*  59 */       return Ascii.toLowerCase(word);
/*     */     }
/*     */ 
/*     */     
/*     */     String convert(CaseFormat format, String s) {
/*  64 */       if (format == LOWER_HYPHEN) {
/*  65 */         return s.replace('_', '-');
/*     */       }
/*  67 */       if (format == UPPER_UNDERSCORE) {
/*  68 */         return Ascii.toUpperCase(s);
/*     */       }
/*  70 */       return super.convert(format, s);
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   LOWER_CAMEL(CharMatcher.inRange('A', 'Z'), "")
/*     */   {
/*     */     String normalizeWord(String word) {
/*  80 */       return firstCharOnlyToUpper(word);
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   UPPER_CAMEL(CharMatcher.inRange('A', 'Z'), "")
/*     */   {
/*     */     String normalizeWord(String word) {
/*  90 */       return firstCharOnlyToUpper(word);
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   UPPER_UNDERSCORE(CharMatcher.is('_'), "_")
/*     */   {
/*     */     String normalizeWord(String word) {
/* 100 */       return Ascii.toUpperCase(word);
/*     */     }
/*     */ 
/*     */     
/*     */     String convert(CaseFormat format, String s) {
/* 105 */       if (format == LOWER_HYPHEN) {
/* 106 */         return Ascii.toLowerCase(s.replace('_', '-'));
/*     */       }
/* 108 */       if (format == LOWER_UNDERSCORE) {
/* 109 */         return Ascii.toLowerCase(s);
/*     */       }
/* 111 */       return super.convert(format, s);
/*     */     }
/*     */   };
/*     */   
/*     */   private final CharMatcher wordBoundary;
/*     */   private final String wordSeparator;
/*     */   
/*     */   CaseFormat(CharMatcher wordBoundary, String wordSeparator) {
/* 119 */     this.wordBoundary = wordBoundary;
/* 120 */     this.wordSeparator = wordSeparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String to(CaseFormat format, String str) {
/* 129 */     Preconditions.checkNotNull(format);
/* 130 */     Preconditions.checkNotNull(str);
/* 131 */     return (format == this) ? str : convert(format, str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String convert(CaseFormat format, String s) {
/* 139 */     StringBuilder out = null;
/* 140 */     int i = 0;
/* 141 */     int j = -1;
/* 142 */     while ((j = this.wordBoundary.indexIn(s, ++j)) != -1) {
/* 143 */       if (i == 0) {
/*     */         
/* 145 */         out = new StringBuilder(s.length() + 4 * this.wordSeparator.length());
/* 146 */         out.append(format.normalizeFirstWord(s.substring(i, j)));
/*     */       } else {
/* 148 */         out.append(format.normalizeWord(s.substring(i, j)));
/*     */       } 
/* 150 */       out.append(format.wordSeparator);
/* 151 */       i = j + this.wordSeparator.length();
/*     */     } 
/* 153 */     return (i == 0) ? format
/* 154 */       .normalizeFirstWord(s) : out
/* 155 */       .append(format.normalizeWord(s.substring(i))).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Converter<String, String> converterTo(CaseFormat targetFormat) {
/* 164 */     return new StringConverter(this, targetFormat);
/*     */   }
/*     */   
/*     */   private static final class StringConverter
/*     */     extends Converter<String, String> implements Serializable {
/*     */     private final CaseFormat sourceFormat;
/*     */     private final CaseFormat targetFormat;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     StringConverter(CaseFormat sourceFormat, CaseFormat targetFormat) {
/* 174 */       this.sourceFormat = Preconditions.<CaseFormat>checkNotNull(sourceFormat);
/* 175 */       this.targetFormat = Preconditions.<CaseFormat>checkNotNull(targetFormat);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doForward(String s) {
/* 180 */       return this.sourceFormat.to(this.targetFormat, s);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(String s) {
/* 185 */       return this.targetFormat.to(this.sourceFormat, s);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 190 */       if (object instanceof StringConverter) {
/* 191 */         StringConverter that = (StringConverter)object;
/* 192 */         return (this.sourceFormat.equals(that.sourceFormat) && this.targetFormat.equals(that.targetFormat));
/*     */       } 
/* 194 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 199 */       return this.sourceFormat.hashCode() ^ this.targetFormat.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 204 */       return this.sourceFormat + ".converterTo(" + this.targetFormat + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String normalizeFirstWord(String word) {
/* 213 */     return (this == LOWER_CAMEL) ? Ascii.toLowerCase(word) : normalizeWord(word);
/*     */   }
/*     */   
/*     */   private static String firstCharOnlyToUpper(String word) {
/* 217 */     return word.isEmpty() ? word : (
/*     */       
/* 219 */       Ascii.toUpperCase(word.charAt(0)) + Ascii.toLowerCase(word.substring(1)));
/*     */   }
/*     */   
/*     */   abstract String normalizeWord(String paramString);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\base\CaseFormat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */