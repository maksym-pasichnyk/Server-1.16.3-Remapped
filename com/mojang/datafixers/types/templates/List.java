/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.FamilyOptic;
/*     */ import com.mojang.datafixers.OpticParts;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.optics.ListTraversal;
/*     */ import com.mojang.datafixers.optics.Optic;
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
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public final class List
/*     */   implements TypeTemplate
/*     */ {
/*     */   private final TypeTemplate element;
/*     */   
/*     */   public List(TypeTemplate element) {
/*  35 */     this.element = element;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  40 */     return this.element.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeFamily apply(final TypeFamily family) {
/*  45 */     return new TypeFamily()
/*     */       {
/*     */         public Type<?> apply(int index) {
/*  48 */           return DSL.list(List.this.element.apply(family).apply(index));
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
/*     */   public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> input, Type<A> aType, Type<B> bType) {
/*  65 */     return TypeFamily.familyOptic(i -> {
/*     */           OpticParts<A, B> pair = this.element.<A, B>applyO(input, aType, bType).apply(i);
/*     */           Set<TypeToken<? extends K1>> bounds = Sets.newHashSet(pair.bounds());
/*     */           bounds.add(TraversalP.Mu.TYPE_TOKEN);
/*     */           return new OpticParts(bounds, cap(pair.optic()));
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private <S, T, A, B> Optic<?, ?, ?, A, B> cap(Optic<?, S, T, A, B> concreteOptic) {
/*  76 */     return (new ListTraversal()).composeUnchecked(concreteOptic);
/*     */   }
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int index, @Nullable String name, Type<FT> type, Type<FR> resultType) {
/*  81 */     return this.element.<FT, FR>findFieldOrType(index, name, type, resultType).mapLeft(List::new);
/*     */   }
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily family, IntFunction<RewriteResult<?, ?>> function) {
/*  86 */     return i -> {
/*     */         RewriteResult<?, ?> view = this.element.hmap(family, function).apply(i);
/*     */         return cap(apply(family).apply(i), view);
/*     */       };
/*     */   }
/*     */   
/*     */   private <E> RewriteResult<?, ?> cap(Type<?> type, RewriteResult<E, ?> view) {
/*  93 */     return ((ListType)type).fix(view);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  98 */     return (obj instanceof List && Objects.equals(this.element, ((List)obj).element));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 103 */     return Objects.hash(new Object[] { this.element });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 108 */     return "List[" + this.element + "]";
/*     */   }
/*     */   
/*     */   public static final class ListType<A> extends Type<java.util.List<A>> {
/*     */     protected final Type<A> element;
/*     */     
/*     */     public ListType(Type<A> element) {
/* 115 */       this.element = element;
/*     */     }
/*     */ 
/*     */     
/*     */     public RewriteResult<java.util.List<A>, ?> all(TypeRewriteRule rule, boolean recurse, boolean checkIndex) {
/* 120 */       RewriteResult<A, ?> view = this.element.rewriteOrNop(rule);
/* 121 */       return fix(view);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<java.util.List<A>, ?>> one(TypeRewriteRule rule) {
/* 126 */       return rule.rewrite(this.element).map(this::fix);
/*     */     }
/*     */ 
/*     */     
/*     */     public Type<?> updateMu(RecursiveTypeFamily newFamily) {
/* 131 */       return DSL.list(this.element.updateMu(newFamily));
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeTemplate buildTemplate() {
/* 136 */       return DSL.list(this.element.template());
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<java.util.List<A>> point(DynamicOps<?> ops) {
/* 141 */       return (Optional)Optional.of(ImmutableList.of());
/*     */     }
/*     */ 
/*     */     
/*     */     public <FT, FR> Either<TypedOptic<java.util.List<A>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> type, Type<FR> resultType, Type.TypeMatcher<FT, FR> matcher, boolean recurse) {
/* 146 */       Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> firstFieldLens = this.element.findType(type, resultType, matcher, recurse);
/* 147 */       return firstFieldLens.mapLeft(this::capLeft);
/*     */     }
/*     */     
/*     */     private <FT, FR, B> TypedOptic<java.util.List<A>, ?, FT, FR> capLeft(TypedOptic<A, B, FT, FR> optic) {
/* 151 */       return TypedOptic.list(optic.sType(), optic.tType()).compose(optic);
/*     */     }
/*     */     
/*     */     public <B> RewriteResult<java.util.List<A>, ?> fix(RewriteResult<A, B> view) {
/* 155 */       return opticView(this, view, TypedOptic.list(this.element, view.view().newType()));
/*     */     }
/*     */ 
/*     */     
/*     */     public Codec<java.util.List<A>> buildCodec() {
/* 160 */       return Codec.list(this.element.codec());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 165 */       return "List[" + this.element + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj, boolean ignoreRecursionPoints, boolean checkIndex) {
/* 170 */       return (obj instanceof ListType && this.element.equals(((ListType)obj).element, ignoreRecursionPoints, checkIndex));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 175 */       return this.element.hashCode();
/*     */     }
/*     */     
/*     */     public Type<A> getElement() {
/* 179 */       return this.element;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\types\templates\List.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */