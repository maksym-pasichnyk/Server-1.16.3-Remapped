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
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.types.families.TypeFamily;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.IntFunction;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public final class Tag
/*     */   implements TypeTemplate
/*     */ {
/*     */   private final String name;
/*     */   private final TypeTemplate element;
/*     */   
/*     */   public Tag(String name, TypeTemplate element) {
/*  29 */     this.name = name;
/*  30 */     this.element = element;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  35 */     return this.element.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeFamily apply(final TypeFamily family) {
/*  40 */     return new TypeFamily()
/*     */       {
/*     */         public Type<?> apply(int index) {
/*  43 */           return DSL.field(Tag.this.name, Tag.this.element.apply(family).apply(index));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> input, Type<A> aType, Type<B> bType) {
/*  73 */     return TypeFamily.familyOptic(i -> this.element.<A, B>applyO(input, aType, bType).apply(i));
/*     */   }
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int index, @Nullable String name, Type<FT> type, Type<FR> resultType) {
/*  78 */     if (!Objects.equals(name, this.name)) {
/*  79 */       return Either.right(new Type.FieldNotFoundException("Names don't match"));
/*     */     }
/*  81 */     if (this.element instanceof Const) {
/*  82 */       Const c = (Const)this.element;
/*  83 */       if (Objects.equals(type, c.type())) {
/*  84 */         return Either.left(new Tag(name, new Const(resultType)));
/*     */       }
/*  86 */       return Either.right(new Type.FieldNotFoundException("don't match"));
/*     */     } 
/*     */     
/*  89 */     if (Objects.equals(type, resultType)) {
/*  90 */       return Either.left(this);
/*     */     }
/*  92 */     if (type instanceof RecursivePoint.RecursivePointType && this.element instanceof RecursivePoint && (
/*  93 */       (RecursivePoint)this.element).index() == ((RecursivePoint.RecursivePointType)type).index()) {
/*  94 */       if (resultType instanceof RecursivePoint.RecursivePointType) {
/*  95 */         if (((RecursivePoint.RecursivePointType)resultType).index() == ((RecursivePoint)this.element).index()) {
/*  96 */           return Either.left(this);
/*     */         }
/*     */       } else {
/*  99 */         return Either.left(DSL.constType(resultType));
/*     */       } 
/*     */     }
/*     */     
/* 103 */     return Either.right(new Type.FieldNotFoundException("Recursive field"));
/*     */   }
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily family, IntFunction<RewriteResult<?, ?>> function) {
/* 108 */     return this.element.hmap(family, function);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 113 */     if (this == obj) {
/* 114 */       return true;
/*     */     }
/* 116 */     if (!(obj instanceof Tag)) {
/* 117 */       return false;
/*     */     }
/* 119 */     Tag that = (Tag)obj;
/* 120 */     return (Objects.equals(this.name, that.name) && Objects.equals(this.element, that.element));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 125 */     return Objects.hash(new Object[] { this.name, this.element });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 130 */     return "NameTag[" + this.name + ": " + this.element + "]";
/*     */   }
/*     */   
/*     */   public static final class TagType<A> extends Type<A> {
/*     */     protected final String name;
/*     */     protected final Type<A> element;
/*     */     
/*     */     public TagType(String name, Type<A> element) {
/* 138 */       this.name = name;
/* 139 */       this.element = element;
/*     */     }
/*     */ 
/*     */     
/*     */     public RewriteResult<A, ?> all(TypeRewriteRule rule, boolean recurse, boolean checkIndex) {
/* 144 */       RewriteResult<A, ?> elementView = this.element.rewriteOrNop(rule);
/* 145 */       return RewriteResult.create(cap(elementView.view()), elementView.recData());
/*     */     }
/*     */     
/*     */     private <B> View<A, ?> cap(View<A, B> instance) {
/* 149 */       if (Objects.equals(instance.function(), Functions.id())) {
/* 150 */         return View.nopView(this);
/*     */       }
/* 152 */       return View.create(this, DSL.field(this.name, instance.newType()), instance.function());
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<A, ?>> one(TypeRewriteRule rule) {
/* 157 */       Optional<RewriteResult<A, ?>> view = rule.rewrite(this.element);
/* 158 */       return view.map(instance -> RewriteResult.create(cap(instance.view()), instance.recData()));
/*     */     }
/*     */ 
/*     */     
/*     */     public Type<?> updateMu(RecursiveTypeFamily newFamily) {
/* 163 */       return DSL.field(this.name, this.element.updateMu(newFamily));
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeTemplate buildTemplate() {
/* 168 */       return DSL.field(this.name, this.element.template());
/*     */     }
/*     */ 
/*     */     
/*     */     protected Codec<A> buildCodec() {
/* 173 */       return this.element.codec().fieldOf(this.name).codec();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 178 */       return "Tag[\"" + this.name + "\", " + this.element + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o, boolean ignoreRecursionPoints, boolean checkIndex) {
/* 183 */       if (this == o) {
/* 184 */         return true;
/*     */       }
/* 186 */       if (o == null || getClass() != o.getClass()) {
/* 187 */         return false;
/*     */       }
/* 189 */       TagType<?> tagType = (TagType)o;
/* 190 */       return (Objects.equals(this.name, tagType.name) && this.element.equals(tagType.element, ignoreRecursionPoints, checkIndex));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 195 */       return Objects.hash(new Object[] { this.name, this.element });
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findFieldTypeOpt(String name) {
/* 200 */       if (Objects.equals(name, this.name)) {
/* 201 */         return Optional.of(this.element);
/*     */       }
/* 203 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<A> point(DynamicOps<?> ops) {
/* 208 */       return this.element.point(ops);
/*     */     }
/*     */ 
/*     */     
/*     */     public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> type, Type<FR> resultType, Type.TypeMatcher<FT, FR> matcher, boolean recurse) {
/* 213 */       return this.element.findType(type, resultType, matcher, recurse).mapLeft(this::wrapOptic);
/*     */     }
/*     */     
/*     */     private <B, FT, FR> TypedOptic<A, B, FT, FR> wrapOptic(TypedOptic<A, B, FT, FR> optic) {
/* 217 */       return new TypedOptic(optic
/* 218 */           .bounds(), 
/* 219 */           DSL.field(this.name, optic.sType()), 
/* 220 */           DSL.field(this.name, optic.tType()), optic
/* 221 */           .aType(), optic
/* 222 */           .bType(), optic
/* 223 */           .optic());
/*     */     }
/*     */ 
/*     */     
/*     */     public String name() {
/* 228 */       return this.name;
/*     */     }
/*     */     
/*     */     public Type<A> element() {
/* 232 */       return this.element;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\types\templates\Tag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */