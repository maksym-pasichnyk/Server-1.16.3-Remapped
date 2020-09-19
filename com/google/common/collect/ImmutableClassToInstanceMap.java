/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Primitives;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
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
/*     */ @GwtIncompatible
/*     */ public final class ImmutableClassToInstanceMap<B>
/*     */   extends ForwardingMap<Class<? extends B>, B>
/*     */   implements ClassToInstanceMap<B>, Serializable
/*     */ {
/*  39 */   private static final ImmutableClassToInstanceMap<Object> EMPTY = new ImmutableClassToInstanceMap(
/*  40 */       ImmutableMap.of());
/*     */ 
/*     */ 
/*     */   
/*     */   private final ImmutableMap<Class<? extends B>, B> delegate;
/*     */ 
/*     */ 
/*     */   
/*     */   public static <B> ImmutableClassToInstanceMap<B> of() {
/*  49 */     return (ImmutableClassToInstanceMap)EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <B, T extends B> ImmutableClassToInstanceMap<B> of(Class<T> type, T value) {
/*  58 */     ImmutableMap<Class<? extends B>, B> map = ImmutableMap.of(type, (B)value);
/*  59 */     return new ImmutableClassToInstanceMap<>(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <B> Builder<B> builder() {
/*  67 */     return new Builder<>();
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
/*     */   public static final class Builder<B>
/*     */   {
/*  88 */     private final ImmutableMap.Builder<Class<? extends B>, B> mapBuilder = ImmutableMap.builder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public <T extends B> Builder<B> put(Class<T> key, T value) {
/*  96 */       this.mapBuilder.put(key, (B)value);
/*  97 */       return this;
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
/*     */     public <T extends B> Builder<B> putAll(Map<? extends Class<? extends T>, ? extends T> map) {
/* 110 */       for (Map.Entry<? extends Class<? extends T>, ? extends T> entry : map.entrySet()) {
/* 111 */         Class<? extends T> type = entry.getKey();
/* 112 */         T value = entry.getValue();
/* 113 */         this.mapBuilder.put(type, cast((Class)type, value));
/*     */       } 
/* 115 */       return this;
/*     */     }
/*     */     
/*     */     private static <B, T extends B> T cast(Class<T> type, B value) {
/* 119 */       return Primitives.wrap(type).cast(value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableClassToInstanceMap<B> build() {
/* 129 */       ImmutableMap<Class<? extends B>, B> map = this.mapBuilder.build();
/* 130 */       if (map.isEmpty()) {
/* 131 */         return ImmutableClassToInstanceMap.of();
/*     */       }
/* 133 */       return new ImmutableClassToInstanceMap<>(map);
/*     */     }
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
/*     */   public static <B, S extends B> ImmutableClassToInstanceMap<B> copyOf(Map<? extends Class<? extends S>, ? extends S> map) {
/* 153 */     if (map instanceof ImmutableClassToInstanceMap) {
/*     */ 
/*     */       
/* 156 */       ImmutableClassToInstanceMap<B> cast = (ImmutableClassToInstanceMap)map;
/* 157 */       return cast;
/*     */     } 
/* 159 */     return (new Builder<>()).<S>putAll(map).build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ImmutableClassToInstanceMap(ImmutableMap<Class<? extends B>, B> delegate) {
/* 165 */     this.delegate = delegate;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<Class<? extends B>, B> delegate() {
/* 170 */     return this.delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T extends B> T getInstance(Class<T> type) {
/* 177 */     return (T)this.delegate.get(Preconditions.checkNotNull(type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public <T extends B> T putInstance(Class<T> type, T value) {
/* 190 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 194 */     return isEmpty() ? of() : this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ImmutableClassToInstanceMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */