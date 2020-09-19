/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Ordering;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
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
/*     */ @Beta
/*     */ public final class ElementOrder<T>
/*     */ {
/*     */   private final Type type;
/*     */   @Nullable
/*     */   private final Comparator<T> comparator;
/*     */   
/*     */   public enum Type
/*     */   {
/*  63 */     UNORDERED,
/*  64 */     INSERTION,
/*  65 */     SORTED;
/*     */   }
/*     */   
/*     */   private ElementOrder(Type type, @Nullable Comparator<T> comparator) {
/*  69 */     this.type = (Type)Preconditions.checkNotNull(type);
/*  70 */     this.comparator = comparator;
/*  71 */     Preconditions.checkState((((type == Type.SORTED) ? true : false) == ((comparator != null) ? true : false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <S> ElementOrder<S> unordered() {
/*  76 */     return new ElementOrder<>(Type.UNORDERED, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <S> ElementOrder<S> insertion() {
/*  81 */     return new ElementOrder<>(Type.INSERTION, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <S extends Comparable<? super S>> ElementOrder<S> natural() {
/*  88 */     return new ElementOrder<>(Type.SORTED, (Comparator<S>)Ordering.natural());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <S> ElementOrder<S> sorted(Comparator<S> comparator) {
/*  96 */     return new ElementOrder<>(Type.SORTED, comparator);
/*     */   }
/*     */ 
/*     */   
/*     */   public Type type() {
/* 101 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<T> comparator() {
/* 110 */     if (this.comparator != null) {
/* 111 */       return this.comparator;
/*     */     }
/* 113 */     throw new UnsupportedOperationException("This ordering does not define a comparator.");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 118 */     if (obj == this) {
/* 119 */       return true;
/*     */     }
/* 121 */     if (!(obj instanceof ElementOrder)) {
/* 122 */       return false;
/*     */     }
/*     */     
/* 125 */     ElementOrder<?> other = (ElementOrder)obj;
/* 126 */     return (this.type == other.type && Objects.equal(this.comparator, other.comparator));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 131 */     return Objects.hashCode(new Object[] { this.type, this.comparator });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 136 */     MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this).add("type", this.type);
/* 137 */     if (this.comparator != null) {
/* 138 */       helper.add("comparator", this.comparator);
/*     */     }
/* 140 */     return helper.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   <K extends T, V> Map<K, V> createMap(int expectedSize) {
/* 145 */     switch (this.type) {
/*     */       case UNORDERED:
/* 147 */         return Maps.newHashMapWithExpectedSize(expectedSize);
/*     */       case INSERTION:
/* 149 */         return Maps.newLinkedHashMapWithExpectedSize(expectedSize);
/*     */       case SORTED:
/* 151 */         return Maps.newTreeMap(comparator());
/*     */     } 
/* 153 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   <T1 extends T> ElementOrder<T1> cast() {
/* 159 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\ElementOrder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */