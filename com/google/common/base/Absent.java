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
/*    */ 
/*    */ @GwtCompatible
/*    */ final class Absent<T>
/*    */   extends Optional<T>
/*    */ {
/* 29 */   static final Absent<Object> INSTANCE = new Absent();
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   static <T> Optional<T> withType() {
/* 33 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isPresent() {
/* 40 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public T get() {
/* 45 */     throw new IllegalStateException("Optional.get() cannot be called on an absent value");
/*    */   }
/*    */ 
/*    */   
/*    */   public T or(T defaultValue) {
/* 50 */     return Preconditions.checkNotNull(defaultValue, "use Optional.orNull() instead of Optional.or(null)");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Optional<T> or(Optional<? extends T> secondChoice) {
/* 56 */     return (Optional<T>)Preconditions.<Optional<? extends T>>checkNotNull(secondChoice);
/*    */   }
/*    */ 
/*    */   
/*    */   public T or(Supplier<? extends T> supplier) {
/* 61 */     return Preconditions.checkNotNull(supplier
/* 62 */         .get(), "use Optional.orNull() instead of a Supplier that returns null");
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public T orNull() {
/* 68 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<T> asSet() {
/* 73 */     return Collections.emptySet();
/*    */   }
/*    */ 
/*    */   
/*    */   public <V> Optional<V> transform(Function<? super T, V> function) {
/* 78 */     Preconditions.checkNotNull(function);
/* 79 */     return Optional.absent();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 84 */     return (object == this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 89 */     return 2040732332;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 94 */     return "Optional.absent()";
/*    */   }
/*    */   
/*    */   private Object readResolve() {
/* 98 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\base\Absent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */