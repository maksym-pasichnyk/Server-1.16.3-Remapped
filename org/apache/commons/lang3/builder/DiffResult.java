/*     */ package org.apache.commons.lang3.builder;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DiffResult
/*     */   implements Iterable<Diff<?>>
/*     */ {
/*     */   public static final String OBJECTS_SAME_STRING = "";
/*     */   private static final String DIFFERS_STRING = "differs from";
/*     */   private final List<Diff<?>> diffs;
/*     */   private final Object lhs;
/*     */   private final Object rhs;
/*     */   private final ToStringStyle style;
/*     */   
/*     */   DiffResult(Object lhs, Object rhs, List<Diff<?>> diffs, ToStringStyle style) {
/*  74 */     if (lhs == null) {
/*  75 */       throw new IllegalArgumentException("Left hand object cannot be null");
/*     */     }
/*     */     
/*  78 */     if (rhs == null) {
/*  79 */       throw new IllegalArgumentException("Right hand object cannot be null");
/*     */     }
/*     */     
/*  82 */     if (diffs == null) {
/*  83 */       throw new IllegalArgumentException("List of differences cannot be null");
/*     */     }
/*     */ 
/*     */     
/*  87 */     this.diffs = diffs;
/*  88 */     this.lhs = lhs;
/*  89 */     this.rhs = rhs;
/*     */     
/*  91 */     if (style == null) {
/*  92 */       this.style = ToStringStyle.DEFAULT_STYLE;
/*     */     } else {
/*  94 */       this.style = style;
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
/*     */   public List<Diff<?>> getDiffs() {
/* 107 */     return Collections.unmodifiableList(this.diffs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumberOfDiffs() {
/* 118 */     return this.diffs.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringStyle getToStringStyle() {
/* 129 */     return this.style;
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
/*     */   public String toString() {
/* 165 */     return toString(this.style);
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
/*     */   public String toString(ToStringStyle style) {
/* 180 */     if (this.diffs.size() == 0) {
/* 181 */       return "";
/*     */     }
/*     */     
/* 184 */     ToStringBuilder lhsBuilder = new ToStringBuilder(this.lhs, style);
/* 185 */     ToStringBuilder rhsBuilder = new ToStringBuilder(this.rhs, style);
/*     */     
/* 187 */     for (Diff<?> diff : this.diffs) {
/* 188 */       lhsBuilder.append(diff.getFieldName(), diff.getLeft());
/* 189 */       rhsBuilder.append(diff.getFieldName(), diff.getRight());
/*     */     } 
/*     */     
/* 192 */     return String.format("%s %s %s", new Object[] { lhsBuilder.build(), "differs from", rhsBuilder
/* 193 */           .build() });
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
/*     */   public Iterator<Diff<?>> iterator() {
/* 205 */     return this.diffs.iterator();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\builder\DiffResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */