/*    */ package com.mojang.serialization;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import java.util.function.UnaryOperator;
/*    */ 
/*    */ 
/*    */ public interface ListBuilder<T>
/*    */ {
/*    */   DynamicOps<T> ops();
/*    */   
/*    */   DataResult<T> build(T paramT);
/*    */   
/*    */   ListBuilder<T> add(T paramT);
/*    */   
/*    */   ListBuilder<T> add(DataResult<T> paramDataResult);
/*    */   
/*    */   ListBuilder<T> withErrorsFrom(DataResult<?> paramDataResult);
/*    */   
/*    */   ListBuilder<T> mapError(UnaryOperator<String> paramUnaryOperator);
/*    */   
/*    */   default DataResult<T> build(DataResult<T> prefix) {
/* 23 */     return prefix.flatMap(this::build);
/*    */   }
/*    */   
/*    */   default <E> ListBuilder<T> add(E value, Encoder<E> encoder) {
/* 27 */     return add(encoder.encodeStart(ops(), value));
/*    */   }
/*    */   
/*    */   default <E> ListBuilder<T> addAll(Iterable<E> values, Encoder<E> encoder) {
/* 31 */     values.forEach(v -> encoder.encode(v, ops(), ops().empty()));
/* 32 */     return this;
/*    */   }
/*    */   
/*    */   public static final class Builder<T> implements ListBuilder<T> {
/*    */     private final DynamicOps<T> ops;
/* 37 */     private DataResult<ImmutableList.Builder<T>> builder = DataResult.success(ImmutableList.builder(), Lifecycle.stable());
/*    */     
/*    */     public Builder(DynamicOps<T> ops) {
/* 40 */       this.ops = ops;
/*    */     }
/*    */ 
/*    */     
/*    */     public DynamicOps<T> ops() {
/* 45 */       return this.ops;
/*    */     }
/*    */ 
/*    */     
/*    */     public ListBuilder<T> add(T value) {
/* 50 */       this.builder = this.builder.map(b -> b.add(value));
/* 51 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public ListBuilder<T> add(DataResult<T> value) {
/* 56 */       this.builder = this.builder.apply2stable(ImmutableList.Builder::add, value);
/* 57 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public ListBuilder<T> withErrorsFrom(DataResult<?> result) {
/* 62 */       this.builder = this.builder.flatMap(r -> result.map(()));
/* 63 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public ListBuilder<T> mapError(UnaryOperator<String> onError) {
/* 68 */       this.builder = this.builder.mapError(onError);
/* 69 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public DataResult<T> build(T prefix) {
/* 74 */       DataResult<T> result = this.builder.flatMap(b -> this.ops.mergeToList((T)prefix, (List<T>)b.build()));
/* 75 */       this.builder = DataResult.success(ImmutableList.builder(), Lifecycle.stable());
/* 76 */       return result;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\ListBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */