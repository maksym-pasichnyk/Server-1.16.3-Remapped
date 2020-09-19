/*     */ package org.apache.commons.lang3.concurrent;
/*     */ 
/*     */ import org.apache.commons.lang3.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConstantInitializer<T>
/*     */   implements ConcurrentInitializer<T>
/*     */ {
/*     */   private static final String FMT_TO_STRING = "ConstantInitializer@%d [ object = %s ]";
/*     */   private final T object;
/*     */   
/*     */   public ConstantInitializer(T obj) {
/*  57 */     this.object = obj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final T getObject() {
/*  68 */     return this.object;
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
/*     */   public T get() throws ConcurrentException {
/*  80 */     return getObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  91 */     return (getObject() != null) ? getObject().hashCode() : 0;
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
/*     */   public boolean equals(Object obj) {
/* 106 */     if (this == obj) {
/* 107 */       return true;
/*     */     }
/* 109 */     if (!(obj instanceof ConstantInitializer)) {
/* 110 */       return false;
/*     */     }
/*     */     
/* 113 */     ConstantInitializer<?> c = (ConstantInitializer)obj;
/* 114 */     return ObjectUtils.equals(getObject(), c.getObject());
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
/*     */   public String toString() {
/* 126 */     return String.format("ConstantInitializer@%d [ object = %s ]", new Object[] { Integer.valueOf(System.identityHashCode(this)), 
/* 127 */           String.valueOf(getObject()) });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\concurrent\ConstantInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */