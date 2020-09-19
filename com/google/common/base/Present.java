/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collections;
/*    */ import java.util.Set;
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
/*    */ @GwtCompatible
/*    */ final class Present<T>
/*    */   extends Optional<T>
/*    */ {
/*    */   private final T reference;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   Present(T reference) {
/* 32 */     this.reference = reference;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPresent() {
/* 37 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public T get() {
/* 42 */     return this.reference;
/*    */   }
/*    */ 
/*    */   
/*    */   public T or(T defaultValue) {
/* 47 */     Preconditions.checkNotNull(defaultValue, "use Optional.orNull() instead of Optional.or(null)");
/* 48 */     return this.reference;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<T> or(Optional<? extends T> secondChoice) {
/* 53 */     Preconditions.checkNotNull(secondChoice);
/* 54 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public T or(Supplier<? extends T> supplier) {
/* 59 */     Preconditions.checkNotNull(supplier);
/* 60 */     return this.reference;
/*    */   }
/*    */ 
/*    */   
/*    */   public T orNull() {
/* 65 */     return this.reference;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<T> asSet() {
/* 70 */     return Collections.singleton(this.reference);
/*    */   }
/*    */ 
/*    */   
/*    */   public <V> Optional<V> transform(Function<? super T, V> function) {
/* 75 */     return new Present(
/* 76 */         Preconditions.checkNotNull((T)function
/* 77 */           .apply(this.reference), "the Function passed to Optional.transform() must not return null."));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 83 */     if (object instanceof Present) {
/* 84 */       Present<?> other = (Present)object;
/* 85 */       return this.reference.equals(other.reference);
/*    */     } 
/* 87 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 92 */     return 1502476572 + this.reference.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 97 */     return "Optional.of(" + this.reference + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\base\Present.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */