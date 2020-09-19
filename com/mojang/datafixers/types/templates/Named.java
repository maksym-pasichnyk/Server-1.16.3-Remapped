/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.FamilyOptic;
/*     */ import com.mojang.datafixers.OpticParts;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.functions.Functions;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.optics.profunctors.Cartesian;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.types.families.TypeFamily;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.IntFunction;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public final class Named
/*     */   implements TypeTemplate {
/*     */   private final String name;
/*     */   private final TypeTemplate element;
/*     */   
/*     */   public Named(String name, TypeTemplate element) {
/*  36 */     this.name = name;
/*  37 */     this.element = element;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  42 */     return this.element.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeFamily apply(TypeFamily family) {
/*  47 */     return index -> DSL.named(this.name, this.element.apply(family).apply(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> input, Type<A> aType, Type<B> bType) {
/*  52 */     return TypeFamily.familyOptic(i -> this.element.<A, B>applyO(input, aType, bType).apply(i));
/*     */   }
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int index, @Nullable String name, Type<FT> type, Type<FR> resultType) {
/*  57 */     return this.element.findFieldOrType(index, name, type, resultType);
/*     */   }
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily family, IntFunction<RewriteResult<?, ?>> function) {
/*  62 */     return index -> {
/*     */         RewriteResult<?, ?> elementResult = this.element.hmap(family, function).apply(index);
/*     */         return cap(family, index, elementResult);
/*     */       };
/*     */   }
/*     */   
/*     */   private <A> RewriteResult<Pair<String, A>, ?> cap(TypeFamily family, int index, RewriteResult<A, ?> elementResult) {
/*  69 */     return NamedType.fix((NamedType<A>)apply(family).apply(index), elementResult);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  74 */     if (this == obj) {
/*  75 */       return true;
/*     */     }
/*  77 */     if (!(obj instanceof Named)) {
/*  78 */       return false;
/*     */     }
/*  80 */     Named that = (Named)obj;
/*  81 */     return (Objects.equals(this.name, that.name) && Objects.equals(this.element, that.element));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  86 */     return Objects.hash(new Object[] { this.name, this.element });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  91 */     return "NamedTypeTag[" + this.name + ": " + this.element + "]";
/*     */   }
/*     */   
/*     */   public static final class NamedType<A> extends Type<Pair<String, A>> {
/*     */     protected final String name;
/*     */     protected final Type<A> element;
/*     */     
/*     */     public NamedType(String name, Type<A> element) {
/*  99 */       this.name = name;
/* 100 */       this.element = element;
/*     */     }
/*     */     
/*     */     public static <A, B> RewriteResult<Pair<String, A>, ?> fix(NamedType<A> type, RewriteResult<A, B> instance) {
/* 104 */       if (Objects.equals(instance.view().function(), Functions.id())) {
/* 105 */         return RewriteResult.nop(type);
/*     */       }
/* 107 */       return opticView(type, instance, wrapOptic(type.name, TypedOptic.adapter(instance.view().type(), instance.view().newType())));
/*     */     }
/*     */ 
/*     */     
/*     */     public RewriteResult<Pair<String, A>, ?> all(TypeRewriteRule rule, boolean recurse, boolean checkIndex) {
/* 112 */       RewriteResult<A, ?> elementView = this.element.rewriteOrNop(rule);
/* 113 */       return fix(this, elementView);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<Pair<String, A>, ?>> one(TypeRewriteRule rule) {
/* 118 */       Optional<RewriteResult<A, ?>> view = rule.rewrite(this.element);
/* 119 */       return view.map(instance -> fix(this, instance));
/*     */     }
/*     */ 
/*     */     
/*     */     public Type<?> updateMu(RecursiveTypeFamily newFamily) {
/* 124 */       return DSL.named(this.name, this.element.updateMu(newFamily));
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeTemplate buildTemplate() {
/* 129 */       return DSL.named(this.name, this.element.template());
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String name, int index) {
/* 134 */       return this.element.findChoiceType(name, index);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findCheckedType(int index) {
/* 139 */       return this.element.findCheckedType(index);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Codec<Pair<String, A>> buildCodec() {
/* 144 */       return new Codec<Pair<String, A>>()
/*     */         {
/*     */           public <T> DataResult<Pair<Pair<String, A>, T>> decode(DynamicOps<T> ops, T input) {
/* 147 */             return Named.NamedType.this.element.codec().decode(ops, input).map(vo -> vo.mapFirst(())).setLifecycle(Lifecycle.experimental());
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> DataResult<T> encode(Pair<String, A> input, DynamicOps<T> ops, T prefix) {
/* 152 */             if (!Objects.equals(input.getFirst(), Named.NamedType.this.name)) {
/* 153 */               return DataResult.error("Named type name doesn't match: expected: " + Named.NamedType.this.name + ", got: " + (String)input.getFirst(), prefix);
/*     */             }
/* 155 */             return Named.NamedType.this.element.codec().encode(input.getSecond(), ops, prefix).setLifecycle(Lifecycle.experimental());
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 162 */       return "NamedType[\"" + this.name + "\", " + this.element + "]";
/*     */     }
/*     */     
/*     */     public String name() {
/* 166 */       return this.name;
/*     */     }
/*     */     
/*     */     public Type<A> element() {
/* 170 */       return this.element;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj, boolean ignoreRecursionPoints, boolean checkIndex) {
/* 175 */       if (this == obj) {
/* 176 */         return true;
/*     */       }
/* 178 */       if (!(obj instanceof NamedType)) {
/* 179 */         return false;
/*     */       }
/* 181 */       NamedType<?> other = (NamedType)obj;
/* 182 */       return (Objects.equals(this.name, other.name) && this.element.equals(other.element, ignoreRecursionPoints, checkIndex));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 187 */       return Objects.hash(new Object[] { this.name, this.element });
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findFieldTypeOpt(String name) {
/* 192 */       return this.element.findFieldTypeOpt(name);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Pair<String, A>> point(DynamicOps<?> ops) {
/* 197 */       return this.element.point(ops).map(value -> Pair.of(this.name, value));
/*     */     }
/*     */ 
/*     */     
/*     */     public <FT, FR> Either<TypedOptic<Pair<String, A>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> type, Type<FR> resultType, Type.TypeMatcher<FT, FR> matcher, boolean recurse) {
/* 202 */       return this.element.findType(type, resultType, matcher, recurse).mapLeft(o -> wrapOptic(this.name, o));
/*     */     }
/*     */     
/*     */     protected static <A, B, FT, FR> TypedOptic<Pair<String, A>, Pair<String, B>, FT, FR> wrapOptic(String name, TypedOptic<A, B, FT, FR> optic) {
/* 206 */       ImmutableSet.Builder<TypeToken<? extends K1>> builder = ImmutableSet.builder();
/* 207 */       builder.addAll(optic.bounds());
/* 208 */       builder.add(Cartesian.Mu.TYPE_TOKEN);
/* 209 */       return new TypedOptic((Set)builder
/* 210 */           .build(), 
/* 211 */           DSL.named(name, optic.sType()), 
/* 212 */           DSL.named(name, optic.tType()), optic
/* 213 */           .aType(), optic
/* 214 */           .bType(), 
/* 215 */           Optics.proj2().composeUnchecked(optic.optic()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\types\templates\Named.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */