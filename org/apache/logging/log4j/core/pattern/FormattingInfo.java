/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class FormattingInfo
/*     */ {
/*  30 */   private static final char[] SPACES = new char[] { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  35 */   private static final FormattingInfo DEFAULT = new FormattingInfo(false, 0, 2147483647, true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int minLength;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int maxLength;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean leftAlign;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean leftTruncate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FormattingInfo(boolean leftAlign, int minLength, int maxLength, boolean leftTruncate) {
/*  70 */     this.leftAlign = leftAlign;
/*  71 */     this.minLength = minLength;
/*  72 */     this.maxLength = maxLength;
/*  73 */     this.leftTruncate = leftTruncate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FormattingInfo getDefault() {
/*  82 */     return DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLeftAligned() {
/*  91 */     return this.leftAlign;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLeftTruncate() {
/* 100 */     return this.leftTruncate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMinLength() {
/* 109 */     return this.minLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxLength() {
/* 118 */     return this.maxLength;
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
/*     */   public void format(int fieldStart, StringBuilder buffer) {
/* 130 */     int rawLength = buffer.length() - fieldStart;
/*     */     
/* 132 */     if (rawLength > this.maxLength) {
/* 133 */       if (this.leftTruncate) {
/* 134 */         buffer.delete(fieldStart, buffer.length() - this.maxLength);
/*     */       } else {
/* 136 */         buffer.delete(fieldStart + this.maxLength, fieldStart + buffer.length());
/*     */       } 
/* 138 */     } else if (rawLength < this.minLength) {
/* 139 */       if (this.leftAlign) {
/* 140 */         int fieldEnd = buffer.length();
/* 141 */         buffer.setLength(fieldStart + this.minLength);
/*     */         
/* 143 */         for (int i = fieldEnd; i < buffer.length(); i++) {
/* 144 */           buffer.setCharAt(i, ' ');
/*     */         }
/*     */       } else {
/* 147 */         int padLength = this.minLength - rawLength;
/*     */         
/* 149 */         for (; padLength > SPACES.length; padLength -= SPACES.length) {
/* 150 */           buffer.insert(fieldStart, SPACES);
/*     */         }
/*     */         
/* 153 */         buffer.insert(fieldStart, SPACES, 0, padLength);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 165 */     StringBuilder sb = new StringBuilder();
/* 166 */     sb.append(super.toString());
/* 167 */     sb.append("[leftAlign=");
/* 168 */     sb.append(this.leftAlign);
/* 169 */     sb.append(", maxLength=");
/* 170 */     sb.append(this.maxLength);
/* 171 */     sb.append(", minLength=");
/* 172 */     sb.append(this.minLength);
/* 173 */     sb.append(", leftTruncate=");
/* 174 */     sb.append(this.leftTruncate);
/* 175 */     sb.append(']');
/* 176 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\pattern\FormattingInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */