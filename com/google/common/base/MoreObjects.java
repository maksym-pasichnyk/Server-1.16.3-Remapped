/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Arrays;
/*     */ import javax.annotation.Nullable;
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
/*     */ @GwtCompatible
/*     */ public final class MoreObjects
/*     */ {
/*     */   public static <T> T firstNonNull(@Nullable T first, @Nullable T second) {
/*  56 */     return (first != null) ? first : Preconditions.<T>checkNotNull(second);
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
/*     */   public static ToStringHelper toStringHelper(Object self) {
/* 100 */     return new ToStringHelper(self.getClass().getSimpleName());
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
/*     */   public static ToStringHelper toStringHelper(Class<?> clazz) {
/* 114 */     return new ToStringHelper(clazz.getSimpleName());
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
/*     */   public static ToStringHelper toStringHelper(String className) {
/* 126 */     return new ToStringHelper(className);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class ToStringHelper
/*     */   {
/*     */     private final String className;
/*     */ 
/*     */     
/* 137 */     private final ValueHolder holderHead = new ValueHolder();
/* 138 */     private ValueHolder holderTail = this.holderHead;
/*     */ 
/*     */     
/*     */     private boolean omitNullValues = false;
/*     */ 
/*     */     
/*     */     private ToStringHelper(String className) {
/* 145 */       this.className = Preconditions.<String>checkNotNull(className);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper omitNullValues() {
/* 157 */       this.omitNullValues = true;
/* 158 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, @Nullable Object value) {
/* 168 */       return addHolder(name, value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, boolean value) {
/* 178 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, char value) {
/* 188 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, double value) {
/* 198 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, float value) {
/* 208 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, int value) {
/* 218 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, long value) {
/* 228 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(@Nullable Object value) {
/* 239 */       return addHolder(value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(boolean value) {
/* 252 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(char value) {
/* 265 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(double value) {
/* 278 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(float value) {
/* 291 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(int value) {
/* 304 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(long value) {
/* 317 */       return addHolder(String.valueOf(value));
/*     */     }
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
/*     */     public String toString() {
/* 331 */       boolean omitNullValuesSnapshot = this.omitNullValues;
/* 332 */       String nextSeparator = "";
/* 333 */       StringBuilder builder = (new StringBuilder(32)).append(this.className).append('{');
/* 334 */       ValueHolder valueHolder = this.holderHead.next;
/* 335 */       for (; valueHolder != null; 
/* 336 */         valueHolder = valueHolder.next) {
/* 337 */         Object value = valueHolder.value;
/* 338 */         if (!omitNullValuesSnapshot || value != null) {
/* 339 */           builder.append(nextSeparator);
/* 340 */           nextSeparator = ", ";
/*     */           
/* 342 */           if (valueHolder.name != null) {
/* 343 */             builder.append(valueHolder.name).append('=');
/*     */           }
/* 345 */           if (value != null && value.getClass().isArray()) {
/* 346 */             Object[] objectArray = { value };
/* 347 */             String arrayString = Arrays.deepToString(objectArray);
/* 348 */             builder.append(arrayString, 1, arrayString.length() - 1);
/*     */           } else {
/* 350 */             builder.append(value);
/*     */           } 
/*     */         } 
/*     */       } 
/* 354 */       return builder.append('}').toString();
/*     */     }
/*     */     
/*     */     private ValueHolder addHolder() {
/* 358 */       ValueHolder valueHolder = new ValueHolder();
/* 359 */       this.holderTail = this.holderTail.next = valueHolder;
/* 360 */       return valueHolder;
/*     */     }
/*     */     
/*     */     private ToStringHelper addHolder(@Nullable Object value) {
/* 364 */       ValueHolder valueHolder = addHolder();
/* 365 */       valueHolder.value = value;
/* 366 */       return this;
/*     */     }
/*     */     
/*     */     private ToStringHelper addHolder(String name, @Nullable Object value) {
/* 370 */       ValueHolder valueHolder = addHolder();
/* 371 */       valueHolder.value = value;
/* 372 */       valueHolder.name = Preconditions.<String>checkNotNull(name);
/* 373 */       return this;
/*     */     }
/*     */     
/*     */     private static final class ValueHolder {
/*     */       String name;
/*     */       Object value;
/*     */       ValueHolder next;
/*     */       
/*     */       private ValueHolder() {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\base\MoreObjects.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */