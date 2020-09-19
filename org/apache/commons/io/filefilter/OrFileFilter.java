/*     */ package org.apache.commons.io.filefilter;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
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
/*     */ public class OrFileFilter
/*     */   extends AbstractFileFilter
/*     */   implements ConditionalFileFilter, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5767770777065432721L;
/*     */   private final List<IOFileFilter> fileFilters;
/*     */   
/*     */   public OrFileFilter() {
/*  50 */     this.fileFilters = new ArrayList<IOFileFilter>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OrFileFilter(List<IOFileFilter> fileFilters) {
/*  61 */     if (fileFilters == null) {
/*  62 */       this.fileFilters = new ArrayList<IOFileFilter>();
/*     */     } else {
/*  64 */       this.fileFilters = new ArrayList<IOFileFilter>(fileFilters);
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
/*     */   public OrFileFilter(IOFileFilter filter1, IOFileFilter filter2) {
/*  76 */     if (filter1 == null || filter2 == null) {
/*  77 */       throw new IllegalArgumentException("The filters must not be null");
/*     */     }
/*  79 */     this.fileFilters = new ArrayList<IOFileFilter>(2);
/*  80 */     addFileFilter(filter1);
/*  81 */     addFileFilter(filter2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileFilter(IOFileFilter ioFileFilter) {
/*  88 */     this.fileFilters.add(ioFileFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<IOFileFilter> getFileFilters() {
/*  95 */     return Collections.unmodifiableList(this.fileFilters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeFileFilter(IOFileFilter ioFileFilter) {
/* 102 */     return this.fileFilters.remove(ioFileFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFileFilters(List<IOFileFilter> fileFilters) {
/* 109 */     this.fileFilters.clear();
/* 110 */     this.fileFilters.addAll(fileFilters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(File file) {
/* 118 */     for (IOFileFilter fileFilter : this.fileFilters) {
/* 119 */       if (fileFilter.accept(file)) {
/* 120 */         return true;
/*     */       }
/*     */     } 
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(File file, String name) {
/* 131 */     for (IOFileFilter fileFilter : this.fileFilters) {
/* 132 */       if (fileFilter.accept(file, name)) {
/* 133 */         return true;
/*     */       }
/*     */     } 
/* 136 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 146 */     StringBuilder buffer = new StringBuilder();
/* 147 */     buffer.append(super.toString());
/* 148 */     buffer.append("(");
/* 149 */     if (this.fileFilters != null) {
/* 150 */       for (int i = 0; i < this.fileFilters.size(); i++) {
/* 151 */         if (i > 0) {
/* 152 */           buffer.append(",");
/*     */         }
/* 154 */         Object filter = this.fileFilters.get(i);
/* 155 */         buffer.append((filter == null) ? "null" : filter.toString());
/*     */       } 
/*     */     }
/* 158 */     buffer.append(")");
/* 159 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\filefilter\OrFileFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */