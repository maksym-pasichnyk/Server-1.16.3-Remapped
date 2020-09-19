/*     */ package org.apache.commons.lang3.concurrent;
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
/*     */ public abstract class LazyInitializer<T>
/*     */   implements ConcurrentInitializer<T>
/*     */ {
/*     */   private volatile T object;
/*     */   
/*     */   public T get() throws ConcurrentException {
/*  96 */     T result = this.object;
/*     */     
/*  98 */     if (result == null) {
/*  99 */       synchronized (this) {
/* 100 */         result = this.object;
/* 101 */         if (result == null) {
/* 102 */           this.object = result = initialize();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 107 */     return result;
/*     */   }
/*     */   
/*     */   protected abstract T initialize() throws ConcurrentException;
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\concurrent\LazyInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */