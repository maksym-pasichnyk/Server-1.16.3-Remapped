/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.FamilyOptic;
/*     */ import com.mojang.datafixers.OpticParts;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.View;
/*     */ import com.mojang.datafixers.functions.Functions;
/*     */ import com.mojang.datafixers.functions.PointFreeRule;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.types.families.TypeFamily;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.BitSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ import org.apache.commons.lang3.ObjectUtils;
/*     */ 
/*     */ public final class RecursivePoint
/*     */   implements TypeTemplate
/*     */ {
/*     */   private final int index;
/*     */   
/*     */   public RecursivePoint(int index) {
/*  35 */     this.index = index;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  40 */     return this.index + 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeFamily apply(TypeFamily family) {
/*  45 */     final Type<?> result = family.apply(this.index);
/*  46 */     return new TypeFamily()
/*     */       {
/*     */         public Type<?> apply(int index) {
/*  49 */           return result;
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
/*     */   public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> input, Type<A> aType, Type<B> bType) {
/*  62 */     return TypeFamily.familyOptic(i -> input.apply(this.index));
/*     */   }
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int index, @Nullable String name, Type<FT> type, Type<FR> resultType) {
/*  67 */     return Either.right(new Type.FieldNotFoundException("Recursion point"));
/*     */   }
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily family, IntFunction<RewriteResult<?, ?>> function) {
/*  72 */     return i -> {
/*     */         RewriteResult<?, ?> result = function.apply(this.index);
/*     */         return cap(family, result);
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public <S, T> RewriteResult<S, T> cap(TypeFamily family, RewriteResult<S, T> result) {
/*  80 */     Type<?> sourceType = family.apply(this.index);
/*  81 */     if (!(sourceType instanceof RecursivePointType)) {
/*  82 */       throw new IllegalArgumentException("Type error: Recursive point template template got a non-recursice type as an input.");
/*     */     }
/*  84 */     if (!Objects.equals(result.view().type(), ((RecursivePointType)sourceType).unfold())) {
/*  85 */       throw new IllegalArgumentException("Type error: hmap function input type");
/*     */     }
/*  87 */     RecursivePointType<S> sType = (RecursivePointType)sourceType;
/*  88 */     RecursivePointType<T> tType = sType.family().buildMuType(result.view().newType(), null);
/*  89 */     BitSet bitSet = (BitSet)ObjectUtils.clone(result.recData());
/*  90 */     bitSet.set(this.index);
/*  91 */     return RewriteResult.create(View.create(sType, tType, result.view().function()), bitSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  96 */     return (obj instanceof RecursivePoint && this.index == ((RecursivePoint)obj).index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 101 */     return Objects.hash(new Object[] { Integer.valueOf(this.index) });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 106 */     return "Id[" + this.index + "]";
/*     */   }
/*     */   
/*     */   public int index() {
/* 110 */     return this.index;
/*     */   }
/*     */   
/*     */   public static final class RecursivePointType<A> extends Type<A> {
/*     */     private final RecursiveTypeFamily family;
/*     */     private final int index;
/*     */     private final Supplier<Type<A>> delegate;
/*     */     @Nullable
/*     */     private volatile Type<A> type;
/*     */     
/*     */     public RecursivePointType(RecursiveTypeFamily family, int index, Supplier<Type<A>> delegate) {
/* 121 */       this.family = family;
/* 122 */       this.index = index;
/* 123 */       this.delegate = delegate;
/*     */     }
/*     */     
/*     */     public RecursiveTypeFamily family() {
/* 127 */       return this.family;
/*     */     }
/*     */     
/*     */     public int index() {
/* 131 */       return this.index;
/*     */     }
/*     */ 
/*     */     
/*     */     public Type<A> unfold() {
/* 136 */       if (this.type == null) {
/* 137 */         this.type = this.delegate.get();
/*     */       }
/* 139 */       return this.type;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Codec<A> buildCodec() {
/* 145 */       return new Codec<A>()
/*     */         {
/*     */           public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/* 148 */             return RecursivePoint.RecursivePointType.this.unfold().codec().decode(ops, input).setLifecycle(Lifecycle.experimental());
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
/* 153 */             return RecursivePoint.RecursivePointType.this.unfold().codec().encode(input, ops, prefix).setLifecycle(Lifecycle.experimental());
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public RewriteResult<A, ?> all(TypeRewriteRule rule, boolean recurse, boolean checkIndex) {
/* 161 */       return unfold().all(rule, recurse, checkIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<A, ?>> one(TypeRewriteRule rule) {
/* 166 */       return unfold().one(rule);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<A, ?>> everywhere(TypeRewriteRule rule, PointFreeRule optimizationRule, boolean recurse, boolean checkIndex) {
/* 171 */       if (recurse) {
/* 172 */         return this.family.everywhere(this.index, rule, optimizationRule).map(view -> view);
/*     */       }
/* 174 */       return Optional.of(RewriteResult.nop(this));
/*     */     }
/*     */ 
/*     */     
/*     */     public Type<?> updateMu(RecursiveTypeFamily newFamily) {
/* 179 */       return newFamily.apply(this.index);
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeTemplate buildTemplate() {
/* 184 */       return DSL.id(this.index);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String name, int index) {
/* 189 */       return unfold().findChoiceType(name, this.index);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findCheckedType(int index) {
/* 194 */       return unfold().findCheckedType(this.index);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findFieldTypeOpt(String name) {
/* 199 */       return unfold().findFieldTypeOpt(name);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<A> point(DynamicOps<?> ops) {
/* 204 */       return unfold().point(ops);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> type, Type<FR> resultType, Type.TypeMatcher<FT, FR> matcher, boolean recurse) {
/* 214 */       return this.family.findType(this.index, type, resultType, matcher, recurse).mapLeft(o -> {
/*     */             if (!Objects.equals(this, o.sType())) {
/*     */               throw new IllegalStateException(":/");
/*     */             }
/*     */             return o;
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
/*     */     private <B, FT, FR> TypedOptic<A, B, FT, FR> wrapOptic(TypedOptic<A, B, FT, FR> optic) {
/* 232 */       return new TypedOptic(optic
/* 233 */           .bounds(), this, optic
/*     */           
/* 235 */           .tType(), optic
/* 236 */           .aType(), optic
/* 237 */           .bType(), optic
/* 238 */           .optic());
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
/*     */     public String toString() {
/* 265 */       return "MuType[" + this.family.name() + "_" + this.index + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj, boolean ignoreRecursionPoints, boolean checkIndex) {
/* 270 */       if (!(obj instanceof RecursivePointType)) {
/* 271 */         return false;
/*     */       }
/* 273 */       RecursivePointType<?> type = (RecursivePointType)obj;
/* 274 */       return ((ignoreRecursionPoints || Objects.equals(this.family, type.family)) && this.index == type.index);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 279 */       return Objects.hash(new Object[] { this.family, Integer.valueOf(this.index) });
/*     */     }
/*     */     
/*     */     public View<A, A> in() {
/* 283 */       return View.create(unfold(), this, Functions.in(this));
/*     */     }
/*     */     
/*     */     public View<A, A> out() {
/* 287 */       return View.create(this, unfold(), Functions.out(this));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\types\templates\RecursivePoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */