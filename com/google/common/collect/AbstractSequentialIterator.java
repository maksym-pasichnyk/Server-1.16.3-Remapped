/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.NoSuchElementException;
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
/*    */ @GwtCompatible
/*    */ public abstract class AbstractSequentialIterator<T>
/*    */   extends UnmodifiableIterator<T>
/*    */ {
/*    */   private T nextOrNull;
/*    */   
/*    */   protected AbstractSequentialIterator(@Nullable T firstOrNull) {
/* 50 */     this.nextOrNull = firstOrNull;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract T computeNext(T paramT);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean hasNext() {
/* 63 */     return (this.nextOrNull != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public final T next() {
/* 68 */     if (!hasNext()) {
/* 69 */       throw new NoSuchElementException();
/*    */     }
/*    */     try {
/* 72 */       return this.nextOrNull;
/*    */     } finally {
/* 74 */       this.nextOrNull = computeNext(this.nextOrNull);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\AbstractSequentialIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */