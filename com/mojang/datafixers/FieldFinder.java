/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.mojang.datafixers.optics.Optic;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.optics.Proj1;
/*     */ import com.mojang.datafixers.optics.profunctors.Cartesian;
/*     */ import com.mojang.datafixers.optics.profunctors.Profunctor;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.Tag;
/*     */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Objects;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ public final class FieldFinder<FT>
/*     */   implements OpticFinder<FT>
/*     */ {
/*     */   @Nullable
/*     */   private final String name;
/*     */   private final Type<FT> type;
/*     */   
/*     */   public FieldFinder(@Nullable String name, Type<FT> type) {
/*  25 */     this.name = name;
/*  26 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public Type<FT> type() {
/*  31 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findType(Type<A> containerType, Type<FR> resultType, boolean recurse) {
/*  36 */     return containerType.findTypeCached(this.type, resultType, new Matcher<>(this.name, this.type, resultType), recurse);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  41 */     if (this == o) {
/*  42 */       return true;
/*     */     }
/*  44 */     if (!(o instanceof FieldFinder)) {
/*  45 */       return false;
/*     */     }
/*  47 */     FieldFinder<?> that = (FieldFinder)o;
/*  48 */     return (Objects.equals(this.name, that.name) && Objects.equals(this.type, that.type));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  53 */     return Objects.hash(new Object[] { this.name, this.type });
/*     */   }
/*     */   
/*     */   private static final class Matcher<FT, FR> implements Type.TypeMatcher<FT, FR> {
/*     */     private final Type<FR> resultType;
/*     */     @Nullable
/*     */     private final String name;
/*     */     private final Type<FT> type;
/*     */     
/*     */     public Matcher(@Nullable String name, Type<FT> type, Type<FR> resultType) {
/*  63 */       this.resultType = resultType;
/*  64 */       this.name = name;
/*  65 */       this.type = type;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <S> Either<TypedOptic<S, ?, FT, FR>, Type.FieldNotFoundException> match(Type<S> targetType) {
/*  71 */       if (this.name == null && this.type.equals(targetType, true, false)) {
/*  72 */         return Either.left(new TypedOptic<>(Profunctor.Mu.TYPE_TOKEN, targetType, this.resultType, targetType, this.resultType, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*  78 */               (Optic<?, S, FR, S, FR>)Optics.id()));
/*     */       }
/*     */       
/*  81 */       if (targetType instanceof Tag.TagType) {
/*  82 */         Tag.TagType<S> tagType = (Tag.TagType<S>)targetType;
/*  83 */         if (!Objects.equals(tagType.name(), this.name)) {
/*  84 */           return Either.right(new Type.FieldNotFoundException(String.format("Not found: \"%s\" (in type: %s)", new Object[] { this.name, targetType })));
/*     */         }
/*  86 */         if (!Objects.equals(this.type, tagType.element())) {
/*  87 */           return Either.right(new Type.FieldNotFoundException(String.format("Type error for field \"%s\": expected type: %s, actual type: %s)", new Object[] { this.name, this.type, tagType.element() })));
/*     */         }
/*  89 */         return Either.left(new TypedOptic<>(Profunctor.Mu.TYPE_TOKEN, (Type<S>)tagType, 
/*     */ 
/*     */               
/*  92 */               (Type<?>)DSL.field(tagType.name(), this.resultType), this.type, this.resultType, 
/*     */ 
/*     */               
/*  95 */               (Optic<?, S, ?, FT, FR>)Optics.id()));
/*     */       } 
/*     */       
/*  98 */       if (targetType instanceof TaggedChoice.TaggedChoiceType) {
/*  99 */         TaggedChoice.TaggedChoiceType<FT> choiceType = (TaggedChoice.TaggedChoiceType<FT>)targetType;
/* 100 */         if (Objects.equals(this.name, choiceType.getName())) {
/* 101 */           if (!Objects.equals(this.type, choiceType.getKeyType())) {
/* 102 */             return Either.right(new Type.FieldNotFoundException(String.format("Type error for field \"%s\": expected type: %s, actual type: %s)", new Object[] { this.name, this.type, choiceType.getKeyType() })));
/*     */           }
/* 104 */           if (!Objects.equals(this.type, this.resultType)) {
/* 105 */             return Either.right(new Type.FieldNotFoundException("TaggedChoiceType key type change is unsupported."));
/*     */           }
/* 107 */           return Either.left(capChoice((Type<?>)choiceType));
/*     */         } 
/*     */       } 
/* 110 */       return Either.right(new Type.Continue());
/*     */     }
/*     */ 
/*     */     
/*     */     private <V> TypedOptic<Pair<FT, V>, ?, FT, FT> capChoice(Type<?> choiceType) {
/* 115 */       return (TypedOptic)new TypedOptic<>(Cartesian.Mu.TYPE_TOKEN, choiceType, choiceType, this.type, this.type, (Optic<?, ?, ?, FT, FT>)new Proj1());
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
/*     */     public boolean equals(Object o) {
/* 127 */       if (this == o) {
/* 128 */         return true;
/*     */       }
/* 130 */       if (o == null || getClass() != o.getClass()) {
/* 131 */         return false;
/*     */       }
/* 133 */       Matcher<?, ?> matcher = (Matcher<?, ?>)o;
/* 134 */       return (Objects.equals(this.resultType, matcher.resultType) && Objects.equals(this.name, matcher.name) && Objects.equals(this.type, matcher.type));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 139 */       return Objects.hash(new Object[] { this.resultType, this.name, this.type });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\FieldFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */