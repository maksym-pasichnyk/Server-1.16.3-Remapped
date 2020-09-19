/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.FamilyOptic;
/*     */ import com.mojang.datafixers.OpticParts;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
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
/*     */ import com.mojang.serialization.Encoder;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.IntFunction;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public final class Check
/*     */   implements TypeTemplate {
/*     */   private final String name;
/*     */   private final int index;
/*     */   private final TypeTemplate element;
/*     */   
/*     */   public Check(String name, int index, TypeTemplate element) {
/*  32 */     this.name = name;
/*  33 */     this.index = index;
/*  34 */     this.element = element;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  39 */     return Math.max(this.index + 1, this.element.size());
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeFamily apply(final TypeFamily family) {
/*  44 */     return new TypeFamily()
/*     */       {
/*     */         public Type<?> apply(int index) {
/*  47 */           if (index < 0) {
/*  48 */             throw new IndexOutOfBoundsException();
/*     */           }
/*  50 */           return new Check.CheckType(Check.this.name, index, Check.this.index, Check.this.element.apply(family).apply(index));
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
/*     */   public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> input, Type<A> aType, Type<B> bType) {
/*  65 */     return TypeFamily.familyOptic(i -> this.element.<A, B>applyO(input, aType, bType).apply(i));
/*     */   }
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int index, @Nullable String name, Type<FT> type, Type<FR> resultType) {
/*  70 */     if (index == this.index) {
/*  71 */       return this.element.findFieldOrType(index, name, type, resultType);
/*     */     }
/*  73 */     return Either.right(new Type.FieldNotFoundException("Not a matching index"));
/*     */   }
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily family, IntFunction<RewriteResult<?, ?>> function) {
/*  78 */     return index -> {
/*     */         RewriteResult<?, ?> elementResult = this.element.hmap(family, function).apply(index);
/*     */         return cap(family, index, elementResult);
/*     */       };
/*     */   }
/*     */   
/*     */   private <A> RewriteResult<?, ?> cap(TypeFamily family, int index, RewriteResult<A, ?> elementResult) {
/*  85 */     return CheckType.fix((CheckType)apply(family).apply(index), elementResult);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  90 */     if (this == obj) {
/*  91 */       return true;
/*     */     }
/*  93 */     if (!(obj instanceof Check)) {
/*  94 */       return false;
/*     */     }
/*  96 */     Check that = (Check)obj;
/*  97 */     return (Objects.equals(this.name, that.name) && this.index == that.index && Objects.equals(this.element, that.element));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 102 */     return Objects.hash(new Object[] { this.name, Integer.valueOf(this.index), this.element });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 107 */     return "Tag[" + this.name + ", " + this.index + ": " + this.element + "]";
/*     */   }
/*     */   
/*     */   public static final class CheckType<A> extends Type<A> {
/*     */     private final String name;
/*     */     private final int index;
/*     */     private final int expectedIndex;
/*     */     private final Type<A> delegate;
/*     */     
/*     */     public CheckType(String name, int index, int expectedIndex, Type<A> delegate) {
/* 117 */       this.name = name;
/* 118 */       this.index = index;
/* 119 */       this.expectedIndex = expectedIndex;
/* 120 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Codec<A> buildCodec() {
/* 125 */       return Codec.of((Encoder)this.delegate
/* 126 */           .codec(), this::read);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private <T> DataResult<Pair<A, T>> read(DynamicOps<T> ops, T input) {
/* 132 */       if (this.index != this.expectedIndex) {
/* 133 */         return DataResult.error("Index mismatch: " + this.index + " != " + this.expectedIndex);
/*     */       }
/* 135 */       return this.delegate.codec().decode(ops, input);
/*     */     }
/*     */     
/*     */     public static <A, B> RewriteResult<A, ?> fix(CheckType<A> type, RewriteResult<A, B> instance) {
/* 139 */       if (Objects.equals(instance.view().function(), Functions.id())) {
/* 140 */         return RewriteResult.nop(type);
/*     */       }
/* 142 */       return opticView(type, instance, wrapOptic(type, TypedOptic.adapter(instance.view().type(), instance.view().newType())));
/*     */     }
/*     */ 
/*     */     
/*     */     public RewriteResult<A, ?> all(TypeRewriteRule rule, boolean recurse, boolean checkIndex) {
/* 147 */       if (checkIndex && this.index != this.expectedIndex) {
/* 148 */         return RewriteResult.nop(this);
/*     */       }
/* 150 */       return fix(this, this.delegate.rewriteOrNop(rule));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<A, ?>> everywhere(TypeRewriteRule rule, PointFreeRule optimizationRule, boolean recurse, boolean checkIndex) {
/* 155 */       if (checkIndex && this.index != this.expectedIndex) {
/* 156 */         return Optional.empty();
/*     */       }
/* 158 */       return super.everywhere(rule, optimizationRule, recurse, checkIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<A, ?>> one(TypeRewriteRule rule) {
/* 163 */       return rule.rewrite(this.delegate).map(view -> fix(this, view));
/*     */     }
/*     */ 
/*     */     
/*     */     public Type<?> updateMu(RecursiveTypeFamily newFamily) {
/* 168 */       return new CheckType(this.name, this.index, this.expectedIndex, this.delegate.updateMu(newFamily));
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeTemplate buildTemplate() {
/* 173 */       return DSL.check(this.name, this.expectedIndex, this.delegate.template());
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String name, int index) {
/* 178 */       if (index == this.expectedIndex) {
/* 179 */         return this.delegate.findChoiceType(name, index);
/*     */       }
/* 181 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findCheckedType(int index) {
/* 186 */       if (index == this.expectedIndex) {
/* 187 */         return Optional.of(this.delegate);
/*     */       }
/* 189 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findFieldTypeOpt(String name) {
/* 194 */       if (this.index == this.expectedIndex) {
/* 195 */         return this.delegate.findFieldTypeOpt(name);
/*     */       }
/* 197 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<A> point(DynamicOps<?> ops) {
/* 202 */       if (this.index == this.expectedIndex) {
/* 203 */         return this.delegate.point(ops);
/*     */       }
/* 205 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> type, Type<FR> resultType, Type.TypeMatcher<FT, FR> matcher, boolean recurse) {
/* 210 */       if (this.index != this.expectedIndex) {
/* 211 */         return Either.right(new Type.FieldNotFoundException("Incorrect index in CheckType"));
/*     */       }
/* 213 */       return this.delegate.findType(type, resultType, matcher, recurse).mapLeft(optic -> wrapOptic(this, optic));
/*     */     }
/*     */     
/*     */     protected static <A, B, FT, FR> TypedOptic<A, B, FT, FR> wrapOptic(CheckType<A> type, TypedOptic<A, B, FT, FR> optic) {
/* 217 */       return new TypedOptic(optic
/* 218 */           .bounds(), type, new CheckType(type.name, type.index, type.expectedIndex, optic
/*     */             
/* 220 */             .tType()), optic
/* 221 */           .aType(), optic
/* 222 */           .bType(), optic
/* 223 */           .optic());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 229 */       return "TypeTag[" + this.index + "~" + this.expectedIndex + "][" + this.name + ": " + this.delegate + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj, boolean ignoreRecursionPoints, boolean checkIndex) {
/* 234 */       if (!(obj instanceof CheckType)) {
/* 235 */         return false;
/*     */       }
/* 237 */       CheckType<?> type = (CheckType)obj;
/* 238 */       if (this.index == type.index && this.expectedIndex == type.expectedIndex) {
/* 239 */         if (!checkIndex) {
/* 240 */           return true;
/*     */         }
/* 242 */         if (this.delegate.equals(type.delegate, ignoreRecursionPoints, checkIndex)) {
/* 243 */           return true;
/*     */         }
/*     */       } 
/* 246 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 251 */       return Objects.hash(new Object[] { Integer.valueOf(this.index), Integer.valueOf(this.expectedIndex), this.delegate });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\types\templates\Check.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */