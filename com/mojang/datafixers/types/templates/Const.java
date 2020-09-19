/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.FamilyOptic;
/*     */ import com.mojang.datafixers.OpticParts;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.optics.Optic;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.optics.profunctors.AffineP;
/*     */ import com.mojang.datafixers.optics.profunctors.Profunctor;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.TypeFamily;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.IntFunction;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public final class Const
/*     */   implements TypeTemplate {
/*     */   private final Type<?> type;
/*     */   
/*     */   public Const(Type<?> type) {
/*  27 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  32 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeFamily apply(TypeFamily family) {
/*  37 */     return new TypeFamily()
/*     */       {
/*     */         public Type<?> apply(int index) {
/*  40 */           return Const.this.type;
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
/*     */   public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> input, Type<A> aType, Type<B> bType) {
/*  52 */     if (Objects.equals(this.type, aType)) {
/*  53 */       return TypeFamily.familyOptic(i -> new OpticParts((Set)ImmutableSet.of(Profunctor.Mu.TYPE_TOKEN), (Optic)Optics.id()));
/*     */     }
/*  55 */     TypedOptic<?, ?, A, B> ignoreOptic = makeIgnoreOptic(this.type, aType, bType);
/*  56 */     return TypeFamily.familyOptic(i -> new OpticParts(ignoreOptic.bounds(), ignoreOptic.optic()));
/*     */   }
/*     */   
/*     */   private <T, A, B> TypedOptic<T, T, A, B> makeIgnoreOptic(Type<T> type, Type<A> aType, Type<B> bType) {
/*  60 */     return new TypedOptic(AffineP.Mu.TYPE_TOKEN, type, type, aType, bType, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  66 */         (Optic)Optics.affine(Either::left, (b, t) -> t));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int index, @Nullable String name, Type<FT> type, Type<FR> resultType) {
/*  72 */     return DSL.fieldFinder(name, type).findType(this.type, resultType, false).mapLeft(field -> new Const(field.tType()));
/*     */   }
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily family, IntFunction<RewriteResult<?, ?>> function) {
/*  77 */     return i -> RewriteResult.nop(this.type);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  82 */     return (obj instanceof Const && Objects.equals(this.type, ((Const)obj).type));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  87 */     return Objects.hash(new Object[] { this.type });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  92 */     return "Const[" + this.type + "]";
/*     */   }
/*     */   
/*     */   public Type<?> type() {
/*  96 */     return this.type;
/*     */   }
/*     */   
/*     */   public static final class PrimitiveType<A> extends Type<A> {
/*     */     private final Codec<A> codec;
/*     */     
/*     */     public PrimitiveType(Codec<A> codec) {
/* 103 */       this.codec = codec;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o, boolean ignoreRecursionPoints, boolean checkIndex) {
/* 108 */       return (this == o);
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeTemplate buildTemplate() {
/* 113 */       return DSL.constType(this);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Codec<A> buildCodec() {
/* 118 */       return this.codec;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 123 */       return this.codec.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\types\templates\Const.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */