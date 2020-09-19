/*    */ package org.apache.commons.lang3.concurrent;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicReference;
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
/*    */ public abstract class AtomicSafeInitializer<T>
/*    */   implements ConcurrentInitializer<T>
/*    */ {
/* 58 */   private final AtomicReference<AtomicSafeInitializer<T>> factory = new AtomicReference<AtomicSafeInitializer<T>>();
/*    */ 
/*    */ 
/*    */   
/* 62 */   private final AtomicReference<T> reference = new AtomicReference<T>();
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
/*    */   public final T get() throws ConcurrentException {
/*    */     T result;
/* 75 */     while ((result = this.reference.get()) == null) {
/* 76 */       if (this.factory.compareAndSet(null, this)) {
/* 77 */         this.reference.set(initialize());
/*    */       }
/*    */     } 
/*    */     
/* 81 */     return result;
/*    */   }
/*    */   
/*    */   protected abstract T initialize() throws ConcurrentException;
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\concurrent\AtomicSafeInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */