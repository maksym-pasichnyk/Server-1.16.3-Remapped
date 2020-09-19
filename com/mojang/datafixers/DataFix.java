/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.BitSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import javax.annotation.Nullable;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DataFix
/*     */ {
/*  20 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final Schema outputSchema;
/*     */   private final boolean changesType;
/*     */   @Nullable
/*     */   private TypeRewriteRule rule;
/*     */   
/*     */   public DataFix(Schema outputSchema, boolean changesType) {
/*  28 */     this.outputSchema = outputSchema;
/*  29 */     this.changesType = changesType;
/*     */   }
/*     */   
/*     */   protected <A> TypeRewriteRule fixTypeEverywhere(String name, Type<A> type, Function<DynamicOps<?>, Function<A, A>> function) {
/*  33 */     return fixTypeEverywhere(name, type, type, function, new BitSet());
/*     */   }
/*     */ 
/*     */   
/*     */   protected <A, B> TypeRewriteRule convertUnchecked(String name, Type<A> type, Type<B> newType) {
/*  38 */     return fixTypeEverywhere(name, type, newType, ops -> Function.identity(), new BitSet());
/*     */   }
/*     */   
/*     */   protected TypeRewriteRule writeAndRead(String name, Type<?> type, Type<?> newType) {
/*  42 */     return writeFixAndRead(name, type, newType, Function.identity());
/*     */   }
/*     */   
/*     */   protected <A, B> TypeRewriteRule writeFixAndRead(String name, Type<A> type, Type<B> newType, Function<Dynamic<?>, Dynamic<?>> fix) {
/*  46 */     return fixTypeEverywhere(name, type, newType, ops -> ());
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
/*     */   protected <A, B> TypeRewriteRule fixTypeEverywhere(String name, Type<A> type, Type<B> newType, Function<DynamicOps<?>, Function<A, B>> function) {
/*  60 */     return fixTypeEverywhere(name, type, newType, function, new BitSet());
/*     */   }
/*     */   
/*     */   protected <A, B> TypeRewriteRule fixTypeEverywhere(String name, Type<A> type, Type<B> newType, Function<DynamicOps<?>, Function<A, B>> function, BitSet bitSet) {
/*  64 */     return fixTypeEverywhere(type, RewriteResult.create(View.create(name, type, newType, new NamedFunctionWrapper<>(name, function)), bitSet));
/*     */   }
/*     */   
/*     */   protected <A> TypeRewriteRule fixTypeEverywhereTyped(String name, Type<A> type, Function<Typed<?>, Typed<?>> function) {
/*  68 */     return fixTypeEverywhereTyped(name, type, function, new BitSet());
/*     */   }
/*     */   
/*     */   protected <A> TypeRewriteRule fixTypeEverywhereTyped(String name, Type<A> type, Function<Typed<?>, Typed<?>> function, BitSet bitSet) {
/*  72 */     return fixTypeEverywhereTyped(name, type, type, function, bitSet);
/*     */   }
/*     */   
/*     */   protected <A, B> TypeRewriteRule fixTypeEverywhereTyped(String name, Type<A> type, Type<B> newType, Function<Typed<?>, Typed<?>> function) {
/*  76 */     return fixTypeEverywhereTyped(name, type, newType, function, new BitSet());
/*     */   }
/*     */   
/*     */   protected <A, B> TypeRewriteRule fixTypeEverywhereTyped(String name, Type<A> type, Type<B> newType, Function<Typed<?>, Typed<?>> function, BitSet bitSet) {
/*  80 */     return fixTypeEverywhere(type, checked(name, type, newType, function, bitSet));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <A, B> RewriteResult<A, B> checked(String name, Type<A> type, Type<B> newType, Function<Typed<?>, Typed<?>> function, BitSet bitSet) {
/*  85 */     return RewriteResult.create(View.create(name, type, newType, new NamedFunctionWrapper<>(name, ops -> ())), bitSet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <A, B> TypeRewriteRule fixTypeEverywhere(Type<A> type, RewriteResult<A, B> view) {
/*  95 */     return TypeRewriteRule.checkOnce(TypeRewriteRule.everywhere(TypeRewriteRule.ifSame(type, view), DataFixerUpper.OPTIMIZATION_RULE, true, true), this::onFail);
/*     */   }
/*     */   
/*     */   protected void onFail(Type<?> type) {
/*  99 */     LOGGER.info("Not matched: " + this + " " + type);
/*     */   }
/*     */   
/*     */   public final int getVersionKey() {
/* 103 */     return getOutputSchema().getVersionKey();
/*     */   }
/*     */   
/*     */   public TypeRewriteRule getRule() {
/* 107 */     if (this.rule == null) {
/* 108 */       this.rule = makeRule();
/*     */     }
/* 110 */     return this.rule;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Schema getInputSchema() {
/* 116 */     if (this.changesType) {
/* 117 */       return this.outputSchema.getParent();
/*     */     }
/* 119 */     return getOutputSchema();
/*     */   }
/*     */   
/*     */   protected Schema getOutputSchema() {
/* 123 */     return this.outputSchema;
/*     */   }
/*     */   
/*     */   protected abstract TypeRewriteRule makeRule();
/*     */   
/*     */   private static final class NamedFunctionWrapper<A, B> implements Function<DynamicOps<?>, Function<A, B>> { private final String name;
/*     */     
/*     */     public NamedFunctionWrapper(String name, Function<DynamicOps<?>, Function<A, B>> delegate) {
/* 131 */       this.name = name;
/* 132 */       this.delegate = delegate;
/*     */     }
/*     */     private final Function<DynamicOps<?>, Function<A, B>> delegate;
/*     */     
/*     */     public Function<A, B> apply(DynamicOps<?> ops) {
/* 137 */       return this.delegate.apply(ops);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 142 */       if (this == o) {
/* 143 */         return true;
/*     */       }
/* 145 */       if (o == null || getClass() != o.getClass()) {
/* 146 */         return false;
/*     */       }
/* 148 */       NamedFunctionWrapper<?, ?> that = (NamedFunctionWrapper<?, ?>)o;
/* 149 */       return Objects.equals(this.name, that.name);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 154 */       return Objects.hash(new Object[] { this.name });
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\DataFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */