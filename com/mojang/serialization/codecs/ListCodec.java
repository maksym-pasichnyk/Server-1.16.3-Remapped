/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import com.mojang.serialization.ListBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.stream.Stream;
/*    */ import org.apache.commons.lang3.mutable.MutableObject;
/*    */ 
/*    */ public final class ListCodec<A>
/*    */   implements Codec<List<A>>
/*    */ {
/*    */   private final Codec<A> elementCodec;
/*    */   
/*    */   public ListCodec(Codec<A> elementCodec) {
/* 23 */     this.elementCodec = elementCodec;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<T> encode(List<A> input, DynamicOps<T> ops, T prefix) {
/* 28 */     ListBuilder<T> builder = ops.listBuilder();
/*    */     
/* 30 */     for (A a : input) {
/* 31 */       builder.add(this.elementCodec.encodeStart(ops, a));
/*    */     }
/*    */     
/* 34 */     return builder.build(prefix);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<Pair<List<A>, T>> decode(DynamicOps<T> ops, T input) {
/* 39 */     return ops.getList(input).setLifecycle(Lifecycle.stable()).flatMap(stream -> {
/*    */           ImmutableList.Builder<A> read = ImmutableList.builder();
/*    */           Stream.Builder<T> failed = Stream.builder();
/*    */           MutableObject<DataResult<Unit>> result = new MutableObject(DataResult.success(Unit.INSTANCE, Lifecycle.stable()));
/*    */           stream.accept(());
/*    */           ImmutableList<A> elements = read.build();
/*    */           T errors = (T)ops.createList(failed.build());
/*    */           Pair<List<A>, T> pair = Pair.of(elements, errors);
/*    */           return ((DataResult)result.getValue()).map(()).setPartial(pair);
/*    */         });
/*    */   }
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
/*    */   public boolean equals(Object o) {
/* 65 */     if (this == o) {
/* 66 */       return true;
/*    */     }
/* 68 */     if (o == null || getClass() != o.getClass()) {
/* 69 */       return false;
/*    */     }
/* 71 */     ListCodec<?> listCodec = (ListCodec)o;
/* 72 */     return Objects.equals(this.elementCodec, listCodec.elementCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 77 */     return Objects.hash(new Object[] { this.elementCodec });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 82 */     return "ListCodec[" + this.elementCodec + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\codecs\ListCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */