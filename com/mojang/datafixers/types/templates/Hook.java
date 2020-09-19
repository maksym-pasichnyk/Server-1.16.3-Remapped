/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.FamilyOptic;
/*     */ import com.mojang.datafixers.OpticParts;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.functions.Functions;
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
/*     */ import java.util.function.IntFunction;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public final class Hook
/*     */   implements TypeTemplate
/*     */ {
/*     */   private final TypeTemplate element;
/*     */   private final HookFunction preRead;
/*     */   private final HookFunction postWrite;
/*     */   
/*     */   public Hook(TypeTemplate element, HookFunction preRead, HookFunction postWrite) {
/*  32 */     this.element = element;
/*  33 */     this.preRead = preRead;
/*  34 */     this.postWrite = postWrite;
/*     */   }
/*     */   
/*     */   public static interface HookFunction {
/*  38 */     public static final HookFunction IDENTITY = new HookFunction()
/*     */       {
/*     */         public <T> T apply(DynamicOps<T> ops, T value) {
/*  41 */           return value;
/*     */         }
/*     */       };
/*     */ 
/*     */     
/*     */     <T> T apply(DynamicOps<T> param1DynamicOps, T param1T);
/*     */   }
/*     */   
/*     */   public int size() {
/*  50 */     return this.element.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeFamily apply(TypeFamily family) {
/*  55 */     return index -> DSL.hook(this.element.apply(family).apply(index), this.preRead, this.postWrite);
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> input, Type<A> aType, Type<B> bType) {
/*  60 */     return TypeFamily.familyOptic(i -> this.element.<A, B>applyO(input, aType, bType).apply(i));
/*     */   }
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int index, @Nullable String name, Type<FT> type, Type<FR> resultType) {
/*  65 */     return this.element.findFieldOrType(index, name, type, resultType);
/*     */   }
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily family, IntFunction<RewriteResult<?, ?>> function) {
/*  70 */     return index -> {
/*     */         RewriteResult<?, ?> elementResult = this.element.hmap(family, function).apply(index);
/*     */         return cap(family, index, elementResult);
/*     */       };
/*     */   }
/*     */   
/*     */   private <A> RewriteResult<A, ?> cap(TypeFamily family, int index, RewriteResult<A, ?> elementResult) {
/*  77 */     return HookType.fix((HookType<A>)apply(family).apply(index), elementResult);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  82 */     if (this == obj) {
/*  83 */       return true;
/*     */     }
/*  85 */     if (!(obj instanceof Hook)) {
/*  86 */       return false;
/*     */     }
/*  88 */     Hook that = (Hook)obj;
/*  89 */     return (Objects.equals(this.element, that.element) && Objects.equals(this.preRead, that.preRead) && Objects.equals(this.postWrite, that.postWrite));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  94 */     return Objects.hash(new Object[] { this.element, this.preRead, this.postWrite });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  99 */     return "Hook[" + this.element + ", " + this.preRead + ", " + this.postWrite + "]";
/*     */   }
/*     */   
/*     */   public static final class HookType<A> extends Type<A> {
/*     */     private final Type<A> delegate;
/*     */     private final Hook.HookFunction preRead;
/*     */     private final Hook.HookFunction postWrite;
/*     */     
/*     */     public HookType(Type<A> delegate, Hook.HookFunction preRead, Hook.HookFunction postWrite) {
/* 108 */       this.delegate = delegate;
/* 109 */       this.preRead = preRead;
/* 110 */       this.postWrite = postWrite;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Codec<A> buildCodec() {
/* 115 */       return new Codec<A>()
/*     */         {
/*     */           public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/* 118 */             return Hook.HookType.this.delegate.codec().decode(ops, Hook.HookType.this.preRead.apply(ops, input)).setLifecycle(Lifecycle.experimental());
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
/* 123 */             return Hook.HookType.this.delegate.codec().encode(input, ops, prefix).map(v -> Hook.HookType.this.postWrite.apply(ops, v)).setLifecycle(Lifecycle.experimental());
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public RewriteResult<A, ?> all(TypeRewriteRule rule, boolean recurse, boolean checkIndex) {
/* 130 */       return fix(this, this.delegate.rewriteOrNop(rule));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<A, ?>> one(TypeRewriteRule rule) {
/* 135 */       return rule.rewrite(this.delegate).map(view -> fix(this, view));
/*     */     }
/*     */ 
/*     */     
/*     */     public Type<?> updateMu(RecursiveTypeFamily newFamily) {
/* 140 */       return new HookType(this.delegate.updateMu(newFamily), this.preRead, this.postWrite);
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeTemplate buildTemplate() {
/* 145 */       return DSL.hook(this.delegate.template(), this.preRead, this.postWrite);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String name, int index) {
/* 150 */       return this.delegate.findChoiceType(name, index);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findCheckedType(int index) {
/* 155 */       return this.delegate.findCheckedType(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findFieldTypeOpt(String name) {
/* 160 */       return this.delegate.findFieldTypeOpt(name);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<A> point(DynamicOps<?> ops) {
/* 165 */       return this.delegate.point(ops);
/*     */     }
/*     */ 
/*     */     
/*     */     public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> type, Type<FR> resultType, Type.TypeMatcher<FT, FR> matcher, boolean recurse) {
/* 170 */       return this.delegate.findType(type, resultType, matcher, recurse).mapLeft(optic -> wrapOptic(optic, this.preRead, this.postWrite));
/*     */     }
/*     */     
/*     */     public static <A, B> RewriteResult<A, ?> fix(HookType<A> type, RewriteResult<A, B> instance) {
/* 174 */       if (Objects.equals(instance.view().function(), Functions.id())) {
/* 175 */         return RewriteResult.nop(type);
/*     */       }
/* 177 */       return opticView(type, instance, wrapOptic(TypedOptic.adapter(instance.view().type(), instance.view().newType()), type.preRead, type.postWrite));
/*     */     }
/*     */     
/*     */     protected static <A, B, FT, FR> TypedOptic<A, B, FT, FR> wrapOptic(TypedOptic<A, B, FT, FR> optic, Hook.HookFunction preRead, Hook.HookFunction postWrite) {
/* 181 */       return new TypedOptic(optic
/* 182 */           .bounds(), 
/* 183 */           DSL.hook(optic.sType(), preRead, postWrite), 
/* 184 */           DSL.hook(optic.tType(), preRead, postWrite), optic
/* 185 */           .aType(), optic
/* 186 */           .bType(), optic
/* 187 */           .optic());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 193 */       return "HookType[" + this.delegate + ", " + this.preRead + ", " + this.postWrite + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj, boolean ignoreRecursionPoints, boolean checkIndex) {
/* 198 */       if (!(obj instanceof HookType)) {
/* 199 */         return false;
/*     */       }
/* 201 */       HookType<?> type = (HookType)obj;
/* 202 */       return (this.delegate.equals(type.delegate, ignoreRecursionPoints, checkIndex) && Objects.equals(this.preRead, type.preRead) && Objects.equals(this.postWrite, type.postWrite));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 207 */       return Objects.hash(new Object[] { this.delegate, this.preRead, this.postWrite });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\types\templates\Hook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */