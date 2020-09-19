/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collector;
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
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class MoreCollectors
/*     */ {
/*  46 */   private static final Collector<Object, ?, Optional<Object>> TO_OPTIONAL = Collector.of(ToOptionalState::new, ToOptionalState::add, ToOptionalState::combine, ToOptionalState::getOptional, new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
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
/*     */   public static <T> Collector<T, ?, Optional<T>> toOptional() {
/*  61 */     return (Collector)TO_OPTIONAL;
/*     */   }
/*     */   
/*  64 */   private static final Object NULL_PLACEHOLDER = new Object();
/*     */   
/*     */   static {
/*  67 */     ONLY_ELEMENT = Collector.of(ToOptionalState::new, (state, o) -> state.add((o == null) ? NULL_PLACEHOLDER : o), ToOptionalState::combine, state -> {
/*     */           Object result = state.getElement();
/*     */           return (result == NULL_PLACEHOLDER) ? null : result;
/*     */         }new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Collector<Object, ?, Object> ONLY_ELEMENT;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Collector<T, ?, T> onlyElement() {
/*  84 */     return (Collector)ONLY_ELEMENT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ToOptionalState
/*     */   {
/*     */     static final int MAX_EXTRAS = 4;
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*  98 */     Object element = null; @Nullable
/*  99 */     List<Object> extras = null;
/*     */ 
/*     */ 
/*     */     
/*     */     IllegalArgumentException multiples(boolean overflow) {
/* 104 */       StringBuilder sb = (new StringBuilder()).append("expected one element but was: <").append(this.element);
/* 105 */       for (Object o : this.extras) {
/* 106 */         sb.append(", ").append(o);
/*     */       }
/* 108 */       if (overflow) {
/* 109 */         sb.append(", ...");
/*     */       }
/* 111 */       sb.append('>');
/* 112 */       throw new IllegalArgumentException(sb.toString());
/*     */     }
/*     */     
/*     */     void add(Object o) {
/* 116 */       Preconditions.checkNotNull(o);
/* 117 */       if (this.element == null) {
/* 118 */         this.element = o;
/* 119 */       } else if (this.extras == null) {
/* 120 */         this.extras = new ArrayList(4);
/* 121 */         this.extras.add(o);
/* 122 */       } else if (this.extras.size() < 4) {
/* 123 */         this.extras.add(o);
/*     */       } else {
/* 125 */         throw multiples(true);
/*     */       } 
/*     */     }
/*     */     
/*     */     ToOptionalState combine(ToOptionalState other) {
/* 130 */       if (this.element == null)
/* 131 */         return other; 
/* 132 */       if (other.element == null) {
/* 133 */         return this;
/*     */       }
/* 135 */       if (this.extras == null) {
/* 136 */         this.extras = new ArrayList();
/*     */       }
/* 138 */       this.extras.add(other.element);
/* 139 */       if (other.extras != null) {
/* 140 */         this.extras.addAll(other.extras);
/*     */       }
/* 142 */       if (this.extras.size() > 4) {
/* 143 */         this.extras.subList(4, this.extras.size()).clear();
/* 144 */         throw multiples(true);
/*     */       } 
/* 146 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     Optional<Object> getOptional() {
/* 151 */       if (this.extras == null) {
/* 152 */         return Optional.ofNullable(this.element);
/*     */       }
/* 154 */       throw multiples(false);
/*     */     }
/*     */ 
/*     */     
/*     */     Object getElement() {
/* 159 */       if (this.element == null)
/* 160 */         throw new NoSuchElementException(); 
/* 161 */       if (this.extras == null) {
/* 162 */         return this.element;
/*     */       }
/* 164 */       throw multiples(false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\MoreCollectors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */