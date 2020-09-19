/*     */ package org.apache.commons.lang3.builder;
/*     */ 
/*     */ import org.apache.commons.lang3.ClassUtils;
/*     */ import org.apache.commons.lang3.SystemUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultilineRecursiveToStringStyle
/*     */   extends RecursiveToStringStyle
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  74 */   private int indent = 2;
/*     */ 
/*     */   
/*  77 */   private int spaces = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultilineRecursiveToStringStyle() {
/*  84 */     resetIndent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void resetIndent() {
/*  92 */     setArrayStart("{" + SystemUtils.LINE_SEPARATOR + spacer(this.spaces));
/*  93 */     setArraySeparator("," + SystemUtils.LINE_SEPARATOR + spacer(this.spaces));
/*  94 */     setArrayEnd(SystemUtils.LINE_SEPARATOR + spacer(this.spaces - this.indent) + "}");
/*     */     
/*  96 */     setContentStart("[" + SystemUtils.LINE_SEPARATOR + spacer(this.spaces));
/*  97 */     setFieldSeparator("," + SystemUtils.LINE_SEPARATOR + spacer(this.spaces));
/*  98 */     setContentEnd(SystemUtils.LINE_SEPARATOR + spacer(this.spaces - this.indent) + "]");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private StringBuilder spacer(int spaces) {
/* 108 */     StringBuilder sb = new StringBuilder();
/* 109 */     for (int i = 0; i < spaces; i++) {
/* 110 */       sb.append(" ");
/*     */     }
/* 112 */     return sb;
/*     */   }
/*     */ 
/*     */   
/*     */   public void appendDetail(StringBuffer buffer, String fieldName, Object value) {
/* 117 */     if (!ClassUtils.isPrimitiveWrapper(value.getClass()) && !String.class.equals(value.getClass()) && 
/* 118 */       accept(value.getClass())) {
/* 119 */       this.spaces += this.indent;
/* 120 */       resetIndent();
/* 121 */       buffer.append(ReflectionToStringBuilder.toString(value, this));
/* 122 */       this.spaces -= this.indent;
/* 123 */       resetIndent();
/*     */     } else {
/* 125 */       super.appendDetail(buffer, fieldName, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void appendDetail(StringBuffer buffer, String fieldName, Object[] array) {
/* 131 */     this.spaces += this.indent;
/* 132 */     resetIndent();
/* 133 */     super.appendDetail(buffer, fieldName, array);
/* 134 */     this.spaces -= this.indent;
/* 135 */     resetIndent();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void reflectionAppendArrayDetail(StringBuffer buffer, String fieldName, Object array) {
/* 140 */     this.spaces += this.indent;
/* 141 */     resetIndent();
/* 142 */     super.appendDetail(buffer, fieldName, array);
/* 143 */     this.spaces -= this.indent;
/* 144 */     resetIndent();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void appendDetail(StringBuffer buffer, String fieldName, long[] array) {
/* 149 */     this.spaces += this.indent;
/* 150 */     resetIndent();
/* 151 */     super.appendDetail(buffer, fieldName, array);
/* 152 */     this.spaces -= this.indent;
/* 153 */     resetIndent();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void appendDetail(StringBuffer buffer, String fieldName, int[] array) {
/* 158 */     this.spaces += this.indent;
/* 159 */     resetIndent();
/* 160 */     super.appendDetail(buffer, fieldName, array);
/* 161 */     this.spaces -= this.indent;
/* 162 */     resetIndent();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void appendDetail(StringBuffer buffer, String fieldName, short[] array) {
/* 167 */     this.spaces += this.indent;
/* 168 */     resetIndent();
/* 169 */     super.appendDetail(buffer, fieldName, array);
/* 170 */     this.spaces -= this.indent;
/* 171 */     resetIndent();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void appendDetail(StringBuffer buffer, String fieldName, byte[] array) {
/* 176 */     this.spaces += this.indent;
/* 177 */     resetIndent();
/* 178 */     super.appendDetail(buffer, fieldName, array);
/* 179 */     this.spaces -= this.indent;
/* 180 */     resetIndent();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void appendDetail(StringBuffer buffer, String fieldName, char[] array) {
/* 185 */     this.spaces += this.indent;
/* 186 */     resetIndent();
/* 187 */     super.appendDetail(buffer, fieldName, array);
/* 188 */     this.spaces -= this.indent;
/* 189 */     resetIndent();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void appendDetail(StringBuffer buffer, String fieldName, double[] array) {
/* 194 */     this.spaces += this.indent;
/* 195 */     resetIndent();
/* 196 */     super.appendDetail(buffer, fieldName, array);
/* 197 */     this.spaces -= this.indent;
/* 198 */     resetIndent();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void appendDetail(StringBuffer buffer, String fieldName, float[] array) {
/* 203 */     this.spaces += this.indent;
/* 204 */     resetIndent();
/* 205 */     super.appendDetail(buffer, fieldName, array);
/* 206 */     this.spaces -= this.indent;
/* 207 */     resetIndent();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void appendDetail(StringBuffer buffer, String fieldName, boolean[] array) {
/* 212 */     this.spaces += this.indent;
/* 213 */     resetIndent();
/* 214 */     super.appendDetail(buffer, fieldName, array);
/* 215 */     this.spaces -= this.indent;
/* 216 */     resetIndent();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\builder\MultilineRecursiveToStringStyle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */