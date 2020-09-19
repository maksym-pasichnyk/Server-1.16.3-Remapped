/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.UnaryOperator;
/*     */ 
/*     */ public interface RecordBuilder<T>
/*     */ {
/*     */   DynamicOps<T> ops();
/*     */   
/*     */   RecordBuilder<T> add(T paramT1, T paramT2);
/*     */   
/*     */   RecordBuilder<T> add(T paramT, DataResult<T> paramDataResult);
/*     */   
/*     */   RecordBuilder<T> add(DataResult<T> paramDataResult1, DataResult<T> paramDataResult2);
/*     */   
/*     */   RecordBuilder<T> withErrorsFrom(DataResult<?> paramDataResult);
/*     */   
/*     */   RecordBuilder<T> setLifecycle(Lifecycle paramLifecycle);
/*     */   
/*     */   RecordBuilder<T> mapError(UnaryOperator<String> paramUnaryOperator);
/*     */   
/*     */   DataResult<T> build(T paramT);
/*     */   
/*     */   default DataResult<T> build(DataResult<T> prefix) {
/*  27 */     return prefix.flatMap(this::build);
/*     */   }
/*     */   
/*     */   default RecordBuilder<T> add(String key, T value) {
/*  31 */     return add(ops().createString(key), value);
/*     */   }
/*     */   
/*     */   default RecordBuilder<T> add(String key, DataResult<T> value) {
/*  35 */     return add(ops().createString(key), value);
/*     */   }
/*     */   
/*     */   default <E> RecordBuilder<T> add(String key, E value, Encoder<E> encoder) {
/*  39 */     return add(key, encoder.encodeStart(ops(), value));
/*     */   }
/*     */   
/*     */   public static abstract class AbstractBuilder<T, R> implements RecordBuilder<T> {
/*     */     private final DynamicOps<T> ops;
/*  44 */     protected DataResult<R> builder = DataResult.success(initBuilder(), Lifecycle.stable());
/*     */     
/*     */     protected AbstractBuilder(DynamicOps<T> ops) {
/*  47 */       this.ops = ops;
/*     */     }
/*     */ 
/*     */     
/*     */     public DynamicOps<T> ops() {
/*  52 */       return this.ops;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public DataResult<T> build(T prefix) {
/*  61 */       DataResult<T> result = this.builder.flatMap(b -> build((R)b, (T)prefix));
/*  62 */       this.builder = DataResult.success(initBuilder(), Lifecycle.stable());
/*  63 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> withErrorsFrom(DataResult<?> result) {
/*  68 */       this.builder = this.builder.flatMap(v -> result.map(()));
/*  69 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> setLifecycle(Lifecycle lifecycle) {
/*  74 */       this.builder = this.builder.setLifecycle(lifecycle);
/*  75 */       return this;
/*     */     }
/*     */     protected abstract R initBuilder();
/*     */     protected abstract DataResult<T> build(R param1R, T param1T);
/*     */     public RecordBuilder<T> mapError(UnaryOperator<String> onError) {
/*  80 */       this.builder = this.builder.mapError(onError);
/*  81 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract class AbstractStringBuilder<T, R> extends AbstractBuilder<T, R> {
/*     */     protected AbstractStringBuilder(DynamicOps<T> ops) {
/*  87 */       super(ops);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> add(String key, T value) {
/*  94 */       this.builder = this.builder.map(b -> append(key, (T)value, (R)b));
/*  95 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> add(String key, DataResult<T> value) {
/* 100 */       this.builder = this.builder.apply2stable((b, v) -> append(key, (T)v, (R)b), value);
/* 101 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> add(T key, T value) {
/* 106 */       this.builder = ops().getStringValue(key).flatMap(k -> {
/*     */             add(k, (T)value);
/*     */             return this.builder;
/*     */           });
/* 110 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> add(T key, DataResult<T> value) {
/* 115 */       this.builder = ops().getStringValue(key).flatMap(k -> {
/*     */             add(k, value);
/*     */             return this.builder;
/*     */           });
/* 119 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> add(DataResult<T> key, DataResult<T> value) {
/* 124 */       this.builder = key.flatMap(ops()::getStringValue).flatMap(k -> {
/*     */             add(k, value);
/*     */             return this.builder;
/*     */           });
/* 128 */       return this;
/*     */     }
/*     */     
/*     */     protected abstract R append(String param1String, T param1T, R param1R); }
/*     */   
/*     */   public static abstract class AbstractUniversalBuilder<T, R> extends AbstractBuilder<T, R> { protected AbstractUniversalBuilder(DynamicOps<T> ops) {
/* 134 */       super(ops);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> add(T key, T value) {
/* 141 */       this.builder = this.builder.map(b -> append((T)key, (T)value, (R)b));
/* 142 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> add(T key, DataResult<T> value) {
/* 147 */       this.builder = this.builder.apply2stable((b, v) -> append((T)key, (T)v, (R)b), value);
/* 148 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> add(DataResult<T> key, DataResult<T> value) {
/* 153 */       this.builder = this.builder.ap(key.apply2stable((k, v) -> (), value));
/* 154 */       return this;
/*     */     }
/*     */     
/*     */     protected abstract R append(T param1T1, T param1T2, R param1R); }
/*     */   
/*     */   public static final class MapBuilder<T> extends AbstractUniversalBuilder<T, ImmutableMap.Builder<T, T>> { public MapBuilder(DynamicOps<T> ops) {
/* 160 */       super(ops);
/*     */     }
/*     */ 
/*     */     
/*     */     protected ImmutableMap.Builder<T, T> initBuilder() {
/* 165 */       return ImmutableMap.builder();
/*     */     }
/*     */ 
/*     */     
/*     */     protected ImmutableMap.Builder<T, T> append(T key, T value, ImmutableMap.Builder<T, T> builder) {
/* 170 */       return builder.put(key, value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected DataResult<T> build(ImmutableMap.Builder<T, T> builder, T prefix) {
/* 175 */       return ops().mergeToMap(prefix, (Map<T, T>)builder.build());
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\RecordBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */