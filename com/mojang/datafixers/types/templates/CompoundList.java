/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.FamilyOptic;
/*     */ import com.mojang.datafixers.OpticParts;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.optics.ListTraversal;
/*     */ import com.mojang.datafixers.optics.Optic;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.optics.profunctors.TraversalP;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.types.families.TypeFamily;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.IntFunction;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ public final class CompoundList
/*     */   implements TypeTemplate
/*     */ {
/*     */   private final TypeTemplate key;
/*     */   private final TypeTemplate element;
/*     */   
/*     */   public CompoundList(TypeTemplate key, TypeTemplate element) {
/*  40 */     this.key = key;
/*  41 */     this.element = element;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  46 */     return Math.max(this.key.size(), this.element.size());
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeFamily apply(TypeFamily family) {
/*  51 */     return index -> DSL.compoundList(this.key.apply(family).apply(index), this.element.apply(family).apply(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> input, Type<A> aType, Type<B> bType) {
/*  56 */     return TypeFamily.familyOptic(i -> {
/*     */           OpticParts<A, B> optic = this.element.<A, B>applyO(input, aType, bType).apply(i);
/*     */           Set<TypeToken<? extends K1>> bounds = Sets.newHashSet(optic.bounds());
/*     */           bounds.add(TraversalP.Mu.TYPE_TOKEN);
/*     */           return new OpticParts(bounds, cap(optic.optic()));
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private <S, T, A, B> Optic<?, ?, ?, A, B> cap(Optic<?, S, T, A, B> concreteOptic) {
/*  67 */     return (new ListTraversal()).compose((Optic)Optics.proj2()).composeUnchecked(concreteOptic);
/*     */   }
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int index, @Nullable String name, Type<FT> type, Type<FR> resultType) {
/*  72 */     return this.element.<FT, FR>findFieldOrType(index, name, type, resultType).mapLeft(element1 -> new CompoundList(this.key, element1));
/*     */   }
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily family, IntFunction<RewriteResult<?, ?>> function) {
/*  77 */     return i -> {
/*     */         RewriteResult<?, ?> f1 = this.key.hmap(family, function).apply(i);
/*     */         RewriteResult<?, ?> f2 = this.element.hmap(family, function).apply(i);
/*     */         return cap(apply(family).apply(i), f1, f2);
/*     */       };
/*     */   }
/*     */   
/*     */   private <L, R> RewriteResult<?, ?> cap(Type<?> type, RewriteResult<L, ?> f1, RewriteResult<R, ?> f2) {
/*  85 */     return ((CompoundListType)type).mergeViews(f1, f2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  90 */     return (obj instanceof CompoundList && Objects.equals(this.element, ((CompoundList)obj).element));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  95 */     return Objects.hash(new Object[] { this.element });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 100 */     return "CompoundList[" + this.element + "]";
/*     */   }
/*     */   
/*     */   public static final class CompoundListType<K, V> extends Type<List<Pair<K, V>>> {
/*     */     protected final Type<K> key;
/*     */     protected final Type<V> element;
/*     */     
/*     */     public CompoundListType(Type<K> key, Type<V> element) {
/* 108 */       this.key = key;
/* 109 */       this.element = element;
/*     */     }
/*     */ 
/*     */     
/*     */     public RewriteResult<List<Pair<K, V>>, ?> all(TypeRewriteRule rule, boolean recurse, boolean checkIndex) {
/* 114 */       return mergeViews(this.key.rewriteOrNop(rule), this.element.rewriteOrNop(rule));
/*     */     }
/*     */     
/*     */     public <K2, V2> RewriteResult<List<Pair<K, V>>, ?> mergeViews(RewriteResult<K, K2> leftView, RewriteResult<V, V2> rightView) {
/* 118 */       RewriteResult<List<Pair<K, V>>, List<Pair<K2, V>>> v1 = fixKeys(this, this.key, this.element, leftView);
/* 119 */       RewriteResult<List<Pair<K2, V>>, List<Pair<K2, V2>>> v2 = fixValues(v1.view().newType(), leftView.view().newType(), this.element, rightView);
/* 120 */       return v2.compose(v1);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<List<Pair<K, V>>, ?>> one(TypeRewriteRule rule) {
/* 125 */       return DataFixUtils.or(rule
/* 126 */           .rewrite(this.key).map(v -> fixKeys(this, this.key, this.element, v)), () -> rule.rewrite(this.element).map(()));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static <K, V, K2> RewriteResult<List<Pair<K, V>>, List<Pair<K2, V>>> fixKeys(Type<List<Pair<K, V>>> type, Type<K> first, Type<V> second, RewriteResult<K, K2> view) {
/* 132 */       return opticView(type, view, TypedOptic.compoundListKeys(first, view.view().newType(), second));
/*     */     }
/*     */     
/*     */     private static <K, V, V2> RewriteResult<List<Pair<K, V>>, List<Pair<K, V2>>> fixValues(Type<List<Pair<K, V>>> type, Type<K> first, Type<V> second, RewriteResult<V, V2> view) {
/* 136 */       return opticView(type, view, TypedOptic.compoundListElements(first, second, view.view().newType()));
/*     */     }
/*     */ 
/*     */     
/*     */     public Type<?> updateMu(RecursiveTypeFamily newFamily) {
/* 141 */       return DSL.compoundList(this.key.updateMu(newFamily), this.element.updateMu(newFamily));
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeTemplate buildTemplate() {
/* 146 */       return new CompoundList(this.key.template(), this.element.template());
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<List<Pair<K, V>>> point(DynamicOps<?> ops) {
/* 151 */       return (Optional)Optional.of(ImmutableList.of());
/*     */     }
/*     */ 
/*     */     
/*     */     public <FT, FR> Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> type, Type<FR> resultType, Type.TypeMatcher<FT, FR> matcher, boolean recurse) {
/* 156 */       Either<TypedOptic<K, ?, FT, FR>, Type.FieldNotFoundException> firstFieldLens = this.key.findType(type, resultType, matcher, recurse);
/* 157 */       return (Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, Type.FieldNotFoundException>)firstFieldLens.map(this::capLeft, r -> {
/*     */             Either<TypedOptic<V, ?, FT, FR>, Type.FieldNotFoundException> secondFieldLens = this.element.findType(type, resultType, matcher, recurse);
/*     */             return secondFieldLens.mapLeft(this::capRight);
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private <FT, K2, FR> Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, Type.FieldNotFoundException> capLeft(TypedOptic<K, K2, FT, FR> optic) {
/* 167 */       return Either.left(TypedOptic.compoundListKeys(optic.sType(), optic.tType(), this.element).compose(optic));
/*     */     }
/*     */     
/*     */     private <FT, V2, FR> TypedOptic<List<Pair<K, V>>, ?, FT, FR> capRight(TypedOptic<V, V2, FT, FR> optic) {
/* 171 */       return TypedOptic.compoundListElements(this.key, optic.sType(), optic.tType()).compose(optic);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Codec<List<Pair<K, V>>> buildCodec() {
/* 176 */       return Codec.compoundList(this.key.codec(), this.element.codec());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 181 */       return "CompoundList[" + this.key + " -> " + this.element + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj, boolean ignoreRecursionPoints, boolean checkIndex) {
/* 186 */       if (!(obj instanceof CompoundListType)) {
/* 187 */         return false;
/*     */       }
/* 189 */       CompoundListType<?, ?> that = (CompoundListType<?, ?>)obj;
/* 190 */       return (this.key.equals(that.key, ignoreRecursionPoints, checkIndex) && this.element.equals(that.element, ignoreRecursionPoints, checkIndex));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 195 */       return Objects.hash(new Object[] { this.key, this.element });
/*     */     }
/*     */     
/*     */     public Type<K> getKey() {
/* 199 */       return this.key;
/*     */     }
/*     */     
/*     */     public Type<V> getElement() {
/* 203 */       return this.element;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\types\templates\CompoundList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */