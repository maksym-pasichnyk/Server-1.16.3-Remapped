/*    */ package com.google.common.io;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
/*    */ import java.util.Iterator;
/*    */ import javax.annotation.Nullable;
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
/*    */ @GwtIncompatible
/*    */ class MultiReader
/*    */   extends Reader
/*    */ {
/*    */   private final Iterator<? extends CharSource> it;
/*    */   private Reader current;
/*    */   
/*    */   MultiReader(Iterator<? extends CharSource> readers) throws IOException {
/* 36 */     this.it = readers;
/* 37 */     advance();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void advance() throws IOException {
/* 44 */     close();
/* 45 */     if (this.it.hasNext()) {
/* 46 */       this.current = ((CharSource)this.it.next()).openStream();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(@Nullable char[] cbuf, int off, int len) throws IOException {
/* 52 */     if (this.current == null) {
/* 53 */       return -1;
/*    */     }
/* 55 */     int result = this.current.read(cbuf, off, len);
/* 56 */     if (result == -1) {
/* 57 */       advance();
/* 58 */       return read(cbuf, off, len);
/*    */     } 
/* 60 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public long skip(long n) throws IOException {
/* 65 */     Preconditions.checkArgument((n >= 0L), "n is negative");
/* 66 */     if (n > 0L) {
/* 67 */       while (this.current != null) {
/* 68 */         long result = this.current.skip(n);
/* 69 */         if (result > 0L) {
/* 70 */           return result;
/*    */         }
/* 72 */         advance();
/*    */       } 
/*    */     }
/* 75 */     return 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean ready() throws IOException {
/* 80 */     return (this.current != null && this.current.ready());
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 85 */     if (this.current != null)
/*    */       try {
/* 87 */         this.current.close();
/*    */       } finally {
/* 89 */         this.current = null;
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\io\MultiReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */