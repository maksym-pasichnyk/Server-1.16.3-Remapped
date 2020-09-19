/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.FamilyOptic;
/*     */ import com.mojang.datafixers.FunctionType;
/*     */ import com.mojang.datafixers.OpticParts;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.optics.Optic;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.optics.Traversal;
/*     */ import com.mojang.datafixers.optics.profunctors.TraversalP;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.types.families.TypeFamily;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public final class Sum
/*     */   implements TypeTemplate {
/*     */   private final TypeTemplate f;
/*     */   private final TypeTemplate g;
/*     */   
/*     */   public Sum(TypeTemplate f, TypeTemplate g) {
/*  38 */     this.f = f;
/*  39 */     this.g = g;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  44 */     return Math.max(this.f.size(), this.g.size());
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeFamily apply(final TypeFamily family) {
/*  49 */     return new TypeFamily()
/*     */       {
/*     */         public Type<?> apply(int index) {
/*  52 */           return DSL.or(Sum.this.f.apply(family).apply(index), Sum.this.g.apply(family).apply(index));
/*     */         }
/*     */       };
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
/*     */   public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> input, Type<A> aType, Type<B> bType) {
/*  78 */     return TypeFamily.familyOptic(i -> cap(this.f.applyO(input, aType, bType), this.g.applyO(input, aType, bType), i));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <A, B, LS, RS, LT, RT> OpticParts<A, B> cap(final FamilyOptic<A, B> lo, final FamilyOptic<A, B> ro, final int index) {
/*  88 */     final TypeToken<TraversalP.Mu> bound = TraversalP.Mu.TYPE_TOKEN;
/*     */     
/*  90 */     return new OpticParts(
/*  91 */         (Set)ImmutableSet.of(bound), (Optic)new Traversal<Either<LS, RS>, Either<LT, RT>, A, B>()
/*     */         {
/*     */           public <F extends com.mojang.datafixers.kinds.K1> FunctionType<Either<LS, RS>, App<F, Either<LT, RT>>> wander(Applicative<F, ?> applicative, FunctionType<A, App<F, B>> input)
/*     */           {
/*  95 */             return e -> (App)e.map((), ());
/*     */           }
/*     */         });
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
/*     */   public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int index, @Nullable String name, Type<FT> type, Type<FR> resultType) {
/* 114 */     Either<TypeTemplate, Type.FieldNotFoundException> either = this.f.findFieldOrType(index, name, type, resultType);
/* 115 */     return (Either<TypeTemplate, Type.FieldNotFoundException>)either.map(f2 -> Either.left(new Sum(f2, this.g)), r -> this.g.findFieldOrType(index, name, type, resultType).mapLeft(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily family, IntFunction<RewriteResult<?, ?>> function) {
/* 123 */     return i -> {
/*     */         RewriteResult<?, ?> f1 = this.f.hmap(family, function).apply(i);
/*     */         RewriteResult<?, ?> f2 = this.g.hmap(family, function).apply(i);
/*     */         return cap(apply(family).apply(i), f1, f2);
/*     */       };
/*     */   }
/*     */   
/*     */   private <L, R> RewriteResult<?, ?> cap(Type<?> type, RewriteResult<L, ?> f1, RewriteResult<R, ?> f2) {
/* 131 */     return ((SumType)type).mergeViews(f1, f2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 136 */     if (this == obj) {
/* 137 */       return true;
/*     */     }
/* 139 */     if (!(obj instanceof Sum)) {
/* 140 */       return false;
/*     */     }
/* 142 */     Sum that = (Sum)obj;
/* 143 */     return (Objects.equals(this.f, that.f) && Objects.equals(this.g, that.g));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 148 */     return Objects.hash(new Object[] { this.f, this.g });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 153 */     return "(" + this.f + " | " + this.g + ")";
/*     */   }
/*     */   
/*     */   public static final class SumType<F, G> extends Type<Either<F, G>> {
/*     */     protected final Type<F> first;
/*     */     protected final Type<G> second;
/*     */     private int hashCode;
/*     */     
/*     */     public SumType(Type<F> first, Type<G> second) {
/* 162 */       this.first = first;
/* 163 */       this.second = second;
/*     */     }
/*     */ 
/*     */     
/*     */     public RewriteResult<Either<F, G>, ?> all(TypeRewriteRule rule, boolean recurse, boolean checkIndex) {
/* 168 */       return mergeViews(this.first.rewriteOrNop(rule), this.second.rewriteOrNop(rule));
/*     */     }
/*     */     
/*     */     public <F2, G2> RewriteResult<Either<F, G>, ?> mergeViews(RewriteResult<F, F2> leftView, RewriteResult<G, G2> rightView) {
/* 172 */       RewriteResult<Either<F, G>, Either<F2, G>> v1 = fixLeft(this, this.first, this.second, leftView);
/* 173 */       RewriteResult<Either<F2, G>, Either<F2, G2>> v2 = fixRight(v1.view().newType(), leftView.view().newType(), this.second, rightView);
/* 174 */       return v2.compose(v1);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<Either<F, G>, ?>> one(TypeRewriteRule rule) {
/* 179 */       return DataFixUtils.or(rule
/* 180 */           .rewrite(this.first).map(v -> fixLeft(this, this.first, this.second, v)), () -> rule.rewrite(this.second).map(()));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static <F, G, F2> RewriteResult<Either<F, G>, Either<F2, G>> fixLeft(Type<Either<F, G>> type, Type<F> first, Type<G> second, RewriteResult<F, F2> view) {
/* 186 */       return opticView(type, view, TypedOptic.inj1(first, second, view.view().newType()));
/*     */     }
/*     */     
/*     */     private static <F, G, G2> RewriteResult<Either<F, G>, Either<F, G2>> fixRight(Type<Either<F, G>> type, Type<F> first, Type<G> second, RewriteResult<G, G2> view) {
/* 190 */       return opticView(type, view, TypedOptic.inj2(first, second, view.view().newType()));
/*     */     }
/*     */ 
/*     */     
/*     */     public Type<?> updateMu(RecursiveTypeFamily newFamily) {
/* 195 */       return DSL.or(this.first.updateMu(newFamily), this.second.updateMu(newFamily));
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeTemplate buildTemplate() {
/* 200 */       return DSL.or(this.first.template(), this.second.template());
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String name, int index) {
/* 205 */       return DataFixUtils.or(this.first.findChoiceType(name, index), () -> this.second.findChoiceType(name, index));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findCheckedType(int index) {
/* 210 */       return DataFixUtils.or(this.first.findCheckedType(index), () -> this.second.findCheckedType(index));
/*     */     }
/*     */ 
/*     */     
/*     */     protected Codec<Either<F, G>> buildCodec() {
/* 215 */       return Codec.either(this.first.codec(), this.second.codec());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 220 */       return "(" + this.first + " | " + this.second + ")";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj, boolean ignoreRecursionPoints, boolean checkIndex) {
/* 225 */       if (!(obj instanceof SumType)) {
/* 226 */         return false;
/*     */       }
/* 228 */       SumType<?, ?> that = (SumType<?, ?>)obj;
/* 229 */       return (this.first.equals(that.first, ignoreRecursionPoints, checkIndex) && this.second.equals(that.second, ignoreRecursionPoints, checkIndex));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 234 */       if (this.hashCode == 0) {
/* 235 */         this.hashCode = Objects.hash(new Object[] { this.first, this.second });
/*     */       }
/* 237 */       return this.hashCode;
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findFieldTypeOpt(String name) {
/* 242 */       return DataFixUtils.or(this.first.findFieldTypeOpt(name), () -> this.second.findFieldTypeOpt(name));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Optional<Either<F, G>> point(DynamicOps<?> ops) {
/* 248 */       return DataFixUtils.or(this.second.point(ops).map(Either::right), () -> this.first.point(ops).map(Either::left));
/*     */     }
/*     */     
/*     */     private static <A, B, LS, RS, LT, RT> TypedOptic<Either<LS, RS>, Either<LT, RT>, A, B> mergeOptics(final TypedOptic<LS, LT, A, B> lo, final TypedOptic<RS, RT, A, B> ro) {
/* 252 */       final TypeToken<TraversalP.Mu> bound = TraversalP.Mu.TYPE_TOKEN;
/*     */       
/* 254 */       return new TypedOptic(bound, 
/*     */           
/* 256 */           DSL.or(lo.sType(), ro.sType()), 
/* 257 */           DSL.or(lo.tType(), ro.tType()), lo
/* 258 */           .aType(), lo
/* 259 */           .bType(), (Optic)new Traversal<Either<LS, RS>, Either<LT, RT>, A, B>()
/*     */           {
/*     */             public <F extends com.mojang.datafixers.kinds.K1> FunctionType<Either<LS, RS>, App<F, Either<LT, RT>>> wander(Applicative<F, ?> applicative, FunctionType<A, App<F, B>> input)
/*     */             {
/* 263 */               return e -> (App)e.map((), ());
/*     */             }
/*     */           });
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
/*     */     
/*     */     public <FT, FR> Either<TypedOptic<Either<F, G>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> type, Type<FR> resultType, Type.TypeMatcher<FT, FR> matcher, boolean recurse) {
/* 280 */       Either<TypedOptic<F, ?, FT, FR>, Type.FieldNotFoundException> firstOptic = this.first.findType(type, resultType, matcher, recurse);
/* 281 */       Either<TypedOptic<G, ?, FT, FR>, Type.FieldNotFoundException> secondOptic = this.second.findType(type, resultType, matcher, recurse);
/* 282 */       if (firstOptic.left().isPresent() && secondOptic.left().isPresent()) {
/* 283 */         return Either.left(mergeOptics(firstOptic.left().get(), secondOptic.left().get()));
/*     */       }
/* 285 */       if (firstOptic.left().isPresent()) {
/* 286 */         return firstOptic.mapLeft(this::capLeft);
/*     */       }
/* 288 */       return secondOptic.mapLeft(this::capRight);
/*     */     }
/*     */     
/*     */     private <FT, FR, F2> TypedOptic<Either<F, G>, ?, FT, FR> capLeft(TypedOptic<F, F2, FT, FR> optic) {
/* 292 */       return TypedOptic.inj1(optic.sType(), this.second, optic.tType()).compose(optic);
/*     */     }
/*     */     
/*     */     private <FT, FR, G2> TypedOptic<Either<F, G>, ?, FT, FR> capRight(TypedOptic<G, G2, FT, FR> optic) {
/* 296 */       return TypedOptic.inj2(this.first, optic.sType(), optic.tType()).compose(optic);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\types\templates\Sum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */