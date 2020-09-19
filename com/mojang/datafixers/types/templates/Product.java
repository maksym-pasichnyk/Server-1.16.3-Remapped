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
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public final class Product
/*     */   implements TypeTemplate
/*     */ {
/*     */   private final TypeTemplate f;
/*     */   private final TypeTemplate g;
/*     */   
/*     */   public Product(TypeTemplate f, TypeTemplate g) {
/*  40 */     this.f = f;
/*  41 */     this.g = g;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  46 */     return Math.max(this.f.size(), this.g.size());
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeFamily apply(final TypeFamily family) {
/*  51 */     return new TypeFamily()
/*     */       {
/*     */         public Type<?> apply(int index) {
/*  54 */           return DSL.and(Product.this.f.apply(family).apply(index), Product.this.g.apply(family).apply(index));
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
/*     */   private <A, B, LS, RS, LT, RT> OpticParts<A, B> cap(FamilyOptic<A, B> lo, FamilyOptic<A, B> ro, int index) {
/*  88 */     TypeToken<TraversalP.Mu> bound = TraversalP.Mu.TYPE_TOKEN;
/*     */     
/*  90 */     OpticParts<A, B> lp = lo.apply(index);
/*  91 */     OpticParts<A, B> rp = ro.apply(index);
/*     */     
/*  93 */     Optic<? super TraversalP.Mu, ?, ?, A, B> l = (Optic<? super TraversalP.Mu, ?, ?, A, B>)lp.optic().upCast(lp.bounds(), bound).orElseThrow(IllegalArgumentException::new);
/*  94 */     Optic<? super TraversalP.Mu, ?, ?, A, B> r = (Optic<? super TraversalP.Mu, ?, ?, A, B>)rp.optic().upCast(rp.bounds(), bound).orElseThrow(IllegalArgumentException::new);
/*     */     
/*  96 */     final Traversal<LS, LT, A, B> lt = Optics.toTraversal(l);
/*  97 */     final Traversal<RS, RT, A, B> rt = Optics.toTraversal(r);
/*     */     
/*  99 */     return new OpticParts(
/* 100 */         (Set)ImmutableSet.of(bound), (Optic)new Traversal<Pair<LS, RS>, Pair<LT, RT>, A, B>()
/*     */         {
/*     */           public <F extends com.mojang.datafixers.kinds.K1> FunctionType<Pair<LS, RS>, App<F, Pair<LT, RT>>> wander(Applicative<F, ?> applicative, FunctionType<A, App<F, B>> input)
/*     */           {
/* 104 */             return p -> applicative.ap2(applicative.point(Pair::of), (App)lt.wander(applicative, input).apply(p.getFirst()), (App)rt.wander(applicative, input).apply(p.getSecond()));
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int index, @Nullable String name, Type<FT> type, Type<FR> resultType) {
/* 115 */     Either<TypeTemplate, Type.FieldNotFoundException> either = this.f.findFieldOrType(index, name, type, resultType);
/* 116 */     return (Either<TypeTemplate, Type.FieldNotFoundException>)either.map(f2 -> Either.left(new Product(f2, this.g)), r -> this.g.findFieldOrType(index, name, type, resultType).mapLeft(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily family, IntFunction<RewriteResult<?, ?>> function) {
/* 124 */     return i -> {
/*     */         RewriteResult<?, ?> f1 = this.f.hmap(family, function).apply(i);
/*     */         RewriteResult<?, ?> f2 = this.g.hmap(family, function).apply(i);
/*     */         return cap(apply(family).apply(i), f1, f2);
/*     */       };
/*     */   }
/*     */   
/*     */   private <L, R> RewriteResult<?, ?> cap(Type<?> type, RewriteResult<L, ?> f1, RewriteResult<R, ?> f2) {
/* 132 */     return ((ProductType)type).mergeViews(f1, f2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 137 */     if (this == obj) {
/* 138 */       return true;
/*     */     }
/* 140 */     if (!(obj instanceof Product)) {
/* 141 */       return false;
/*     */     }
/* 143 */     Product that = (Product)obj;
/* 144 */     return (Objects.equals(this.f, that.f) && Objects.equals(this.g, that.g));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 149 */     return Objects.hash(new Object[] { this.f, this.g });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 154 */     return "(" + this.f + ", " + this.g + ")";
/*     */   }
/*     */   
/*     */   public static final class ProductType<F, G> extends Type<Pair<F, G>> {
/*     */     protected final Type<F> first;
/*     */     protected final Type<G> second;
/*     */     private int hashCode;
/*     */     
/*     */     public ProductType(Type<F> first, Type<G> second) {
/* 163 */       this.first = first;
/* 164 */       this.second = second;
/*     */     }
/*     */ 
/*     */     
/*     */     public RewriteResult<Pair<F, G>, ?> all(TypeRewriteRule rule, boolean recurse, boolean checkIndex) {
/* 169 */       return mergeViews(this.first.rewriteOrNop(rule), this.second.rewriteOrNop(rule));
/*     */     }
/*     */     
/*     */     public <F2, G2> RewriteResult<Pair<F, G>, ?> mergeViews(RewriteResult<F, F2> leftView, RewriteResult<G, G2> rightView) {
/* 173 */       RewriteResult<Pair<F, G>, Pair<F2, G>> v1 = fixLeft(this, this.first, this.second, leftView);
/* 174 */       RewriteResult<Pair<F2, G>, Pair<F2, G2>> v2 = fixRight(v1.view().newType(), leftView.view().newType(), this.second, rightView);
/* 175 */       return v2.compose(v1);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<Pair<F, G>, ?>> one(TypeRewriteRule rule) {
/* 180 */       return DataFixUtils.or(rule
/* 181 */           .rewrite(this.first).map(v -> fixLeft(this, this.first, this.second, v)), () -> rule.rewrite(this.second).map(()));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static <F, G, F2> RewriteResult<Pair<F, G>, Pair<F2, G>> fixLeft(Type<Pair<F, G>> type, Type<F> first, Type<G> second, RewriteResult<F, F2> view) {
/* 187 */       return opticView(type, view, TypedOptic.proj1(first, second, view.view().newType()));
/*     */     }
/*     */     
/*     */     private static <F, G, G2> RewriteResult<Pair<F, G>, Pair<F, G2>> fixRight(Type<Pair<F, G>> type, Type<F> first, Type<G> second, RewriteResult<G, G2> view) {
/* 191 */       return opticView(type, view, TypedOptic.proj2(first, second, view.view().newType()));
/*     */     }
/*     */ 
/*     */     
/*     */     public Type<?> updateMu(RecursiveTypeFamily newFamily) {
/* 196 */       return DSL.and(this.first.updateMu(newFamily), this.second.updateMu(newFamily));
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeTemplate buildTemplate() {
/* 201 */       return DSL.and(this.first.template(), this.second.template());
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String name, int index) {
/* 206 */       return DataFixUtils.or(this.first.findChoiceType(name, index), () -> this.second.findChoiceType(name, index));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findCheckedType(int index) {
/* 211 */       return DataFixUtils.or(this.first.findCheckedType(index), () -> this.second.findCheckedType(index));
/*     */     }
/*     */ 
/*     */     
/*     */     public Codec<Pair<F, G>> buildCodec() {
/* 216 */       return Codec.pair(this.first.codec(), this.second.codec());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 221 */       return "(" + this.first + ", " + this.second + ")";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj, boolean ignoreRecursionPoints, boolean checkIndex) {
/* 226 */       if (!(obj instanceof ProductType)) {
/* 227 */         return false;
/*     */       }
/* 229 */       ProductType<?, ?> that = (ProductType<?, ?>)obj;
/* 230 */       return (this.first.equals(that.first, ignoreRecursionPoints, checkIndex) && this.second.equals(that.second, ignoreRecursionPoints, checkIndex));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 235 */       if (this.hashCode == 0) {
/* 236 */         this.hashCode = Objects.hash(new Object[] { this.first, this.second });
/*     */       }
/* 238 */       return this.hashCode;
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findFieldTypeOpt(String name) {
/* 243 */       return DataFixUtils.or(this.first.findFieldTypeOpt(name), () -> this.second.findFieldTypeOpt(name));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Pair<F, G>> point(DynamicOps<?> ops) {
/* 248 */       return this.first.point(ops).flatMap(f -> this.second.point(ops).map(()));
/*     */     }
/*     */ 
/*     */     
/*     */     public <FT, FR> Either<TypedOptic<Pair<F, G>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> type, Type<FR> resultType, Type.TypeMatcher<FT, FR> matcher, boolean recurse) {
/* 253 */       Either<TypedOptic<F, ?, FT, FR>, Type.FieldNotFoundException> firstFieldLens = this.first.findType(type, resultType, matcher, recurse);
/* 254 */       return (Either<TypedOptic<Pair<F, G>, ?, FT, FR>, Type.FieldNotFoundException>)firstFieldLens.map(this::capLeft, r -> {
/*     */             Either<TypedOptic<G, ?, FT, FR>, Type.FieldNotFoundException> secondFieldLens = this.second.findType(type, resultType, matcher, recurse);
/*     */             return secondFieldLens.mapLeft(this::capRight);
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private <FT, F2, FR> Either<TypedOptic<Pair<F, G>, ?, FT, FR>, Type.FieldNotFoundException> capLeft(TypedOptic<F, F2, FT, FR> optic) {
/* 264 */       return Either.left(TypedOptic.proj1(optic.sType(), this.second, optic.tType()).compose(optic));
/*     */     }
/*     */     
/*     */     private <FT, G2, FR> TypedOptic<Pair<F, G>, ?, FT, FR> capRight(TypedOptic<G, G2, FT, FR> optic) {
/* 268 */       return TypedOptic.proj2(this.first, optic.sType(), optic.tType()).compose(optic);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\types\templates\Product.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */