/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class NamedChoiceFinder<FT>
/*     */   implements OpticFinder<FT>
/*     */ {
/*     */   private final String name;
/*     */   private final Type<FT> type;
/*     */   
/*     */   public NamedChoiceFinder(String name, Type<FT> type) {
/*  19 */     this.name = name;
/*  20 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public Type<FT> type() {
/*  25 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findType(Type<A> containerType, Type<FR> resultType, boolean recurse) {
/*  30 */     return containerType.findTypeCached(this.type, resultType, new Matcher<>(this.name, this.type, resultType), recurse);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  35 */     if (this == o) {
/*  36 */       return true;
/*     */     }
/*  38 */     if (!(o instanceof NamedChoiceFinder)) {
/*  39 */       return false;
/*     */     }
/*  41 */     NamedChoiceFinder<?> that = (NamedChoiceFinder)o;
/*  42 */     return (Objects.equals(this.name, that.name) && Objects.equals(this.type, that.type));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  47 */     return Objects.hash(new Object[] { this.name, this.type });
/*     */   }
/*     */   
/*     */   private static class Matcher<FT, FR> implements Type.TypeMatcher<FT, FR> {
/*     */     private final Type<FR> resultType;
/*     */     private final String name;
/*     */     private final Type<FT> type;
/*     */     
/*     */     public Matcher(String name, Type<FT> type, Type<FR> resultType) {
/*  56 */       this.resultType = resultType;
/*  57 */       this.name = name;
/*  58 */       this.type = type;
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
/*     */     public <S> Either<TypedOptic<S, ?, FT, FR>, Type.FieldNotFoundException> match(Type<S> targetType) {
/*  74 */       if (targetType instanceof TaggedChoice.TaggedChoiceType) {
/*  75 */         TaggedChoice.TaggedChoiceType<?> choiceType = (TaggedChoice.TaggedChoiceType)targetType;
/*  76 */         Type<?> elementType = (Type)choiceType.types().get(this.name);
/*  77 */         if (elementType != null) {
/*  78 */           if (!Objects.equals(this.type, elementType)) {
/*  79 */             return Either.right(new Type.FieldNotFoundException(String.format("Type error for choice type \"%s\": expected type: %s, actual type: %s)", new Object[] { this.name, targetType, elementType })));
/*     */           }
/*  81 */           return Either.left(TypedOptic.tagged(choiceType, this.name, this.type, this.resultType));
/*     */         } 
/*  83 */         return Either.right(new Type.Continue());
/*     */       } 
/*  85 */       if (targetType instanceof com.mojang.datafixers.types.templates.Tag.TagType) {
/*  86 */         return Either.right(new Type.FieldNotFoundException("in tag"));
/*     */       }
/*  88 */       return Either.right(new Type.Continue());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/*  93 */       if (this == o) {
/*  94 */         return true;
/*     */       }
/*  96 */       if (o == null || getClass() != o.getClass()) {
/*  97 */         return false;
/*     */       }
/*  99 */       Matcher<?, ?> matcher = (Matcher<?, ?>)o;
/* 100 */       return (Objects.equals(this.resultType, matcher.resultType) && Objects.equals(this.name, matcher.name) && Objects.equals(this.type, matcher.type));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 105 */       return Objects.hash(new Object[] { this.resultType, this.name, this.type });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\NamedChoiceFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */