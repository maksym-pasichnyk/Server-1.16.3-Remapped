/*     */ package org.apache.commons.io.comparator;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
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
/*     */ public class CompositeFileComparator
/*     */   extends AbstractFileComparator
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2224170307287243428L;
/*  48 */   private static final Comparator<?>[] NO_COMPARATORS = (Comparator<?>[])new Comparator[0];
/*     */ 
/*     */ 
/*     */   
/*     */   private final Comparator<File>[] delegates;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeFileComparator(Comparator<File>... delegates) {
/*  58 */     if (delegates == null) {
/*  59 */       this.delegates = (Comparator<File>[])NO_COMPARATORS;
/*     */     } else {
/*  61 */       this.delegates = (Comparator<File>[])new Comparator[delegates.length];
/*  62 */       System.arraycopy(delegates, 0, this.delegates, 0, delegates.length);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeFileComparator(Iterable<Comparator<File>> delegates) {
/*  73 */     if (delegates == null) {
/*  74 */       this.delegates = (Comparator<File>[])NO_COMPARATORS;
/*     */     } else {
/*  76 */       List<Comparator<File>> list = new ArrayList<Comparator<File>>();
/*  77 */       for (Comparator<File> comparator : delegates) {
/*  78 */         list.add(comparator);
/*     */       }
/*  80 */       this.delegates = list.<Comparator<File>>toArray((Comparator<File>[])new Comparator[list.size()]);
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
/*     */   public int compare(File file1, File file2) {
/*  93 */     int result = 0;
/*  94 */     for (Comparator<File> delegate : this.delegates) {
/*  95 */       result = delegate.compare(file1, file2);
/*  96 */       if (result != 0) {
/*     */         break;
/*     */       }
/*     */     } 
/* 100 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 110 */     StringBuilder builder = new StringBuilder();
/* 111 */     builder.append(super.toString());
/* 112 */     builder.append('{');
/* 113 */     for (int i = 0; i < this.delegates.length; i++) {
/* 114 */       if (i > 0) {
/* 115 */         builder.append(',');
/*     */       }
/* 117 */       builder.append(this.delegates[i]);
/*     */     } 
/* 119 */     builder.append('}');
/* 120 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\comparator\CompositeFileComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */