/*     */ package com.mojang.datafixers.types;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.FieldFinder;
/*     */ import com.mojang.datafixers.FunctionType;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.View;
/*     */ import com.mojang.datafixers.functions.Functions;
/*     */ import com.mojang.datafixers.functions.PointFreeRule;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.optics.Optic;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ import org.apache.commons.lang3.tuple.Triple;
/*     */ 
/*     */ public abstract class Type<A> implements App<Type.Mu, A> {
/*  39 */   private static final Map<Triple<Type<?>, TypeRewriteRule, PointFreeRule>, CompletableFuture<Optional<? extends RewriteResult<?, ?>>>> PENDING_REWRITE_CACHE = Maps.newConcurrentMap();
/*  40 */   private static final Map<Triple<Type<?>, TypeRewriteRule, PointFreeRule>, Optional<? extends RewriteResult<?, ?>>> REWRITE_CACHE = Maps.newConcurrentMap();
/*     */   @Nullable
/*     */   private TypeTemplate template;
/*     */   
/*     */   public static <A> Type<A> unbox(App<Mu, A> box) {
/*  45 */     return (Type)box;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Codec<A> codec;
/*     */   
/*     */   public static class Mu
/*     */     implements K1 {}
/*     */   
/*     */   public RewriteResult<A, ?> rewriteOrNop(TypeRewriteRule rule) {
/*  55 */     return (RewriteResult<A, ?>)DataFixUtils.orElseGet(rule.rewrite(this), () -> RewriteResult.nop(this));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <S, T, A, B> RewriteResult<S, T> opticView(Type<S> type, RewriteResult<A, B> view, TypedOptic<S, T, A, B> optic) {
/*  60 */     if (Objects.equals(view.view().function(), Functions.id())) {
/*  61 */       return RewriteResult.nop(type);
/*     */     }
/*     */     
/*  64 */     return RewriteResult.create(View.create(optic
/*  65 */           .sType(), optic
/*  66 */           .tType(), 
/*  67 */           Functions.app(
/*  68 */             Functions.profunctorTransformer((Optic)optic.upCast(FunctionType.Instance.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)), view
/*  69 */             .view().function(), 
/*  70 */             DSL.func(optic.aType(), view.view().newType()))), view
/*  71 */         .recData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RewriteResult<A, ?> all(TypeRewriteRule rule, boolean recurse, boolean checkIndex) {
/*  79 */     return RewriteResult.nop(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<RewriteResult<A, ?>> one(TypeRewriteRule rule) {
/*  86 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public Optional<RewriteResult<A, ?>> everywhere(TypeRewriteRule rule, PointFreeRule optimizationRule, boolean recurse, boolean checkIndex) {
/*  90 */     TypeRewriteRule rule2 = TypeRewriteRule.seq(TypeRewriteRule.orElse(rule, TypeRewriteRule::nop), TypeRewriteRule.all(TypeRewriteRule.everywhere(rule, optimizationRule, recurse, checkIndex), recurse, checkIndex));
/*  91 */     return rewrite(rule2, optimizationRule);
/*     */   }
/*     */   
/*     */   public Type<?> updateMu(RecursiveTypeFamily newFamily) {
/*  95 */     return this;
/*     */   }
/*     */   
/*     */   public TypeTemplate template() {
/*  99 */     if (this.template == null) {
/* 100 */       this.template = buildTemplate();
/*     */     }
/* 102 */     return this.template;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String name, int index) {
/* 108 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public Optional<Type<?>> findCheckedType(int index) {
/* 112 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public final <T> DataResult<Pair<A, Dynamic<T>>> read(Dynamic<T> input) {
/* 116 */     return codec().decode(input.getOps(), input.getValue()).map(v -> v.mapSecond(()));
/*     */   }
/*     */   
/*     */   public final Codec<A> codec() {
/* 120 */     if (this.codec == null) {
/* 121 */       this.codec = buildCodec();
/*     */     }
/* 123 */     return this.codec;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> DataResult<T> write(DynamicOps<T> ops, A value) {
/* 129 */     return codec().encode(value, ops, ops.empty());
/*     */   }
/*     */   
/*     */   public final <T> DataResult<Dynamic<T>> writeDynamic(DynamicOps<T> ops, A value) {
/* 133 */     return write(ops, value).map(result -> new Dynamic(ops, result));
/*     */   }
/*     */   
/*     */   public <T> DataResult<Pair<Typed<A>, T>> readTyped(Dynamic<T> input) {
/* 137 */     return readTyped(input.getOps(), (T)input.getValue());
/*     */   }
/*     */   
/*     */   public <T> DataResult<Pair<Typed<A>, T>> readTyped(DynamicOps<T> ops, T input) {
/* 141 */     return codec().decode(ops, input).map(vo -> vo.mapFirst(()));
/*     */   }
/*     */   
/*     */   public <T> DataResult<Pair<Optional<?>, T>> read(DynamicOps<T> ops, TypeRewriteRule rule, PointFreeRule fRule, T input) {
/* 145 */     return codec().decode(ops, input).map(vo -> vo.mapFirst(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> DataResult<T> readAndWrite(DynamicOps<T> ops, Type<?> expectedType, TypeRewriteRule rule, PointFreeRule fRule, T input) {
/* 152 */     Optional<RewriteResult<A, ?>> rewriteResult = rewrite(rule, fRule);
/* 153 */     if (!rewriteResult.isPresent()) {
/* 154 */       return DataResult.error("Could not build a rewrite rule: " + rule + " " + fRule, input);
/*     */     }
/* 156 */     View<A, ?> view = ((RewriteResult)rewriteResult.get()).view();
/*     */     
/* 158 */     return codec().decode(ops, input).flatMap(pair -> capWrite(ops, expectedType, pair.getSecond(), (A)pair.getFirst(), view));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private <T, B> DataResult<T> capWrite(DynamicOps<T> ops, Type<?> expectedType, T rest, A value, View<A, B> f) {
/* 164 */     if (!expectedType.equals(f.newType(), true, true)) {
/* 165 */       return DataResult.error("Rewritten type doesn't match");
/*     */     }
/* 167 */     return f.newType().codec().encode(((Function)f.function().evalCached().apply(ops)).apply(value), ops, rest);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<RewriteResult<A, ?>> rewrite(TypeRewriteRule rule, PointFreeRule fRule) {
/* 172 */     Triple<Type<?>, TypeRewriteRule, PointFreeRule> key = Triple.of(this, rule, fRule);
/*     */ 
/*     */ 
/*     */     
/* 176 */     Optional<? extends RewriteResult<?, ?>> rewrite = REWRITE_CACHE.get(key);
/* 177 */     if (rewrite != null) {
/* 178 */       return (Optional)rewrite;
/*     */     }
/*     */     
/* 181 */     MutableObject<CompletableFuture<Optional<? extends RewriteResult<?, ?>>>> ref = new MutableObject();
/*     */     
/* 183 */     CompletableFuture<Optional<? extends RewriteResult<?, ?>>> pending = PENDING_REWRITE_CACHE.computeIfAbsent(key, k -> {
/*     */           CompletableFuture<Optional<? extends RewriteResult<?, ?>>> value = new CompletableFuture<>();
/*     */           
/*     */           ref.setValue(value);
/*     */           return value;
/*     */         });
/* 189 */     if (ref.getValue() != null) {
/* 190 */       Optional<RewriteResult<A, ?>> result = rule.rewrite(this).flatMap(r -> r.view().rewrite(fRule).map(()));
/* 191 */       REWRITE_CACHE.put(key, result);
/* 192 */       pending.complete(result);
/* 193 */       PENDING_REWRITE_CACHE.remove(key);
/* 194 */       return result;
/*     */     } 
/* 196 */     return (Optional<RewriteResult<A, ?>>)pending.join();
/*     */   }
/*     */   
/*     */   public <FT, FR> Type<?> getSetType(OpticFinder<FT> optic, Type<FR> newType) {
/* 200 */     return ((TypedOptic)optic.findType(this, newType, false).orThrow()).tType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<Type<?>> findFieldTypeOpt(String name) {
/* 209 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public Type<?> findFieldType(String name) {
/* 213 */     return findFieldTypeOpt(name).<Throwable>orElseThrow(() -> new IllegalArgumentException("Field not found: " + name));
/*     */   }
/*     */   
/*     */   public OpticFinder<?> findField(String name) {
/* 217 */     return (OpticFinder<?>)new FieldFinder(name, findFieldType(name));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<A> point(DynamicOps<?> ops) {
/* 225 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public Optional<Typed<A>> pointTyped(DynamicOps<?> ops) {
/* 229 */     return point(ops).map(value -> new Typed(this, ops, value));
/*     */   }
/*     */   
/*     */   public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException> findTypeCached(Type<FT> type, Type<FR> resultType, TypeMatcher<FT, FR> matcher, boolean recurse) {
/* 233 */     return findType(type, resultType, matcher, recurse);
/*     */   }
/*     */   
/*     */   public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException> findType(Type<FT> type, Type<FR> resultType, TypeMatcher<FT, FR> matcher, boolean recurse) {
/* 237 */     return (Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException>)matcher.<S>match(this).map(Either::left, r -> (r instanceof Continue) ? findTypeInChildren(type, resultType, matcher, recurse) : Either.right(r));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException> findTypeInChildren(Type<FT> type, Type<FR> resultType, TypeMatcher<FT, FR> matcher, boolean recurse) {
/* 246 */     return Either.right(new FieldNotFoundException("No more children"));
/*     */   }
/*     */   
/*     */   public OpticFinder<A> finder() {
/* 250 */     return DSL.typeFinder(this);
/*     */   }
/*     */   
/*     */   public <B> Optional<A> ifSame(Typed<B> value) {
/* 254 */     return ifSame(value.getType(), value.getValue());
/*     */   }
/*     */   public abstract TypeTemplate buildTemplate();
/*     */   
/*     */   public <B> Optional<A> ifSame(Type<B> type, B value) {
/* 259 */     if (equals(type, true, true)) {
/* 260 */       return Optional.of((A)value);
/*     */     }
/* 262 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public <B> Optional<RewriteResult<A, ?>> ifSame(Type<B> type, RewriteResult<B, ?> value) {
/* 267 */     if (equals(type, true, true)) {
/* 268 */       return Optional.of(value);
/*     */     }
/* 270 */     return Optional.empty();
/*     */   }
/*     */   protected abstract Codec<A> buildCodec();
/*     */   
/*     */   public final boolean equals(Object o) {
/* 275 */     if (this == o) {
/* 276 */       return true;
/*     */     }
/* 278 */     return equals(o, false, true);
/*     */   }
/*     */   
/*     */   public abstract boolean equals(Object paramObject, boolean paramBoolean1, boolean paramBoolean2);
/*     */   
/*     */   public static interface TypeMatcher<FT, FR> {
/*     */     <S> Either<TypedOptic<S, ?, FT, FR>, Type.FieldNotFoundException> match(Type<S> param1Type); }
/*     */   
/*     */   public static abstract class TypeError { public TypeError(String message) {
/* 287 */       this.message = message;
/*     */     }
/*     */     private final String message;
/*     */     
/*     */     public String toString() {
/* 292 */       return this.message;
/*     */     } }
/*     */ 
/*     */   
/*     */   public static class FieldNotFoundException extends TypeError {
/*     */     public FieldNotFoundException(String message) {
/* 298 */       super(message);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Continue extends FieldNotFoundException {
/*     */     public Continue() {
/* 304 */       super("Continue");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\types\Type.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */