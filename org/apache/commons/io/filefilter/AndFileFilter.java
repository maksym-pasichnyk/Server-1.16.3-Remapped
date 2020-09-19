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
/*     */ 
/*     */ 
/*     */ public class AndFileFilter
/*     */   extends AbstractFileFilter
/*     */   implements ConditionalFileFilter, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 7215974688563965257L;
/*     */   private final List<IOFileFilter> fileFilters;
/*     */   
/*     */   public AndFileFilter() {
/*  52 */     this.fileFilters = new ArrayList<IOFileFilter>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AndFileFilter(List<IOFileFilter> fileFilters) {
/*  63 */     if (fileFilters == null) {
/*  64 */       this.fileFilters = new ArrayList<IOFileFilter>();
/*     */     } else {
/*  66 */       this.fileFilters = new ArrayList<IOFileFilter>(fileFilters);
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
/*     */   public AndFileFilter(IOFileFilter filter1, IOFileFilter filter2) {
/*  78 */     if (filter1 == null || filter2 == null) {
/*  79 */       throw new IllegalArgumentException("The filters must not be null");
/*     */     }
/*  81 */     this.fileFilters = new ArrayList<IOFileFilter>(2);
/*  82 */     addFileFilter(filter1);
/*  83 */     addFileFilter(filter2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileFilter(IOFileFilter ioFileFilter) {
/*  90 */     this.fileFilters.add(ioFileFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<IOFileFilter> getFileFilters() {
/*  97 */     return Collections.unmodifiableList(this.fileFilters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeFileFilter(IOFileFilter ioFileFilter) {
/* 104 */     return this.fileFilters.remove(ioFileFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFileFilters(List<IOFileFilter> fileFilters) {
/* 111 */     this.fileFilters.clear();
/* 112 */     this.fileFilters.addAll(fileFilters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(File file) {
/* 120 */     if (this.fileFilters.isEmpty()) {
/* 121 */       return false;
/*     */     }
/* 123 */     for (IOFileFilter fileFilter : this.fileFilters) {
/* 124 */       if (!fileFilter.accept(file)) {
/* 125 */         return false;
/*     */       }
/*     */     } 
/* 128 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(File file, String name) {
/* 136 */     if (this.fileFilters.isEmpty()) {
/* 137 */       return false;
/*     */     }
/* 139 */     for (IOFileFilter fileFilter : this.fileFilters) {
/* 140 */       if (!fileFilter.accept(file, name)) {
/* 141 */         return false;
/*     */       }
/*     */     } 
/* 144 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 154 */     StringBuilder buffer = new StringBuilder();
/* 155 */     buffer.append(super.toString());
/* 156 */     buffer.append("(");
/* 157 */     if (this.fileFilters != null) {
/* 158 */       for (int i = 0; i < this.fileFilters.size(); i++) {
/* 159 */         if (i > 0) {
/* 160 */           buffer.append(",");
/*     */         }
/* 162 */         Object filter = this.fileFilters.get(i);
/* 163 */         buffer.append((filter == null) ? "null" : filter.toString());
/*     */       } 
/*     */     }
/* 166 */     buffer.append(")");
/* 167 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\filefilter\AndFileFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */