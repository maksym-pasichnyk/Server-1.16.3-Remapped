/*    */ package org.apache.commons.io.comparator;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ReverseComparator
/*    */   extends AbstractFileComparator
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -4808255005272229056L;
/*    */   private final Comparator<File> delegate;
/*    */   
/*    */   public ReverseComparator(Comparator<File> delegate) {
/* 41 */     if (delegate == null) {
/* 42 */       throw new IllegalArgumentException("Delegate comparator is missing");
/*    */     }
/* 44 */     this.delegate = delegate;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int compare(File file1, File file2) {
/* 56 */     return this.delegate.compare(file2, file1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 66 */     return super.toString() + "[" + this.delegate.toString() + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\comparator\ReverseComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */