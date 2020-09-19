/*     */ package com.mojang.datafixers.types.families;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.FamilyOptic;
/*     */ import com.mojang.datafixers.OpticParts;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.View;
/*     */ import com.mojang.datafixers.functions.Functions;
/*     */ import com.mojang.datafixers.functions.PointFree;
/*     */ import com.mojang.datafixers.functions.PointFreeRule;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.RecursivePoint;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RecursiveTypeFamily
/*     */   implements TypeFamily
/*     */ {
/*     */   private final String name;
/*     */   private final TypeTemplate template;
/*     */   private final int size;
/*  38 */   private final Int2ObjectMap<RecursivePoint.RecursivePointType<?>> types = Int2ObjectMaps.synchronize((Int2ObjectMap)new Int2ObjectOpenHashMap());
/*     */   private final int hashCode;
/*     */   
/*     */   public RecursiveTypeFamily(String name, TypeTemplate template) {
/*  42 */     this.name = name;
/*  43 */     this.template = template;
/*  44 */     this.size = template.size();
/*  45 */     this.hashCode = Objects.hashCode(template);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <A, B> View<A, B> viewUnchecked(Type<?> type, Type<?> resType, PointFree<Function<A, B>> function) {
/*  50 */     return View.create(type, resType, function);
/*     */   }
/*     */ 
/*     */   
/*     */   public <A> RecursivePoint.RecursivePointType<A> buildMuType(Type<A> newType, @Nullable RecursiveTypeFamily newFamily) {
/*  55 */     if (newFamily == null) {
/*     */       
/*  57 */       TypeTemplate newTypeTemplate = newType.template();
/*     */       
/*  59 */       if (Objects.equals(this.template, newTypeTemplate)) {
/*  60 */         newFamily = this;
/*     */       } else {
/*  62 */         newFamily = new RecursiveTypeFamily("ruled " + this.name, newTypeTemplate);
/*     */       } 
/*     */     } 
/*     */     
/*  66 */     RecursivePoint.RecursivePointType<A> newMuType = null;
/*  67 */     for (int i1 = 0; i1 < newFamily.size; i1++) {
/*  68 */       RecursivePoint.RecursivePointType<?> type = newFamily.apply(i1);
/*  69 */       Type<?> unfold = type.unfold();
/*  70 */       if (newType.equals(unfold, true, false)) {
/*  71 */         newMuType = (RecursivePoint.RecursivePointType)type;
/*     */         break;
/*     */       } 
/*     */     } 
/*  75 */     if (newMuType == null) {
/*  76 */       throw new IllegalStateException("Couldn't determine the new type properly");
/*     */     }
/*  78 */     return newMuType;
/*     */   }
/*     */   
/*     */   public String name() {
/*  82 */     return this.name;
/*     */   }
/*     */   
/*     */   public TypeTemplate template() {
/*  86 */     return this.template;
/*     */   }
/*     */   
/*     */   public int size() {
/*  90 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> fold(Algebra algebra) {
/*  97 */     return index -> {
/*     */         RewriteResult<?, ?> result = algebra.apply(index);
/*     */         return RewriteResult.create(viewUnchecked(result.view().type(), result.view().newType(), Functions.fold(apply(index), result, algebra, index)), result.recData());
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RecursivePoint.RecursivePointType<?> apply(int index) {
/* 106 */     if (index < 0) {
/* 107 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 109 */     return (RecursivePoint.RecursivePointType)this.types.computeIfAbsent(Integer.valueOf(index), i -> new RecursivePoint.RecursivePointType(this, i.intValue(), ()));
/*     */   }
/*     */   
/*     */   public <A, B> Either<TypedOptic<?, ?, A, B>, Type.FieldNotFoundException> findType(int index, Type<A> aType, Type<B> bType, Type.TypeMatcher<A, B> matcher, boolean recurse) {
/* 113 */     return apply(index).unfold().findType(aType, bType, matcher, false).flatMap(optic -> {
/*     */           TypeTemplate nc = optic.tType().template();
/*     */           List<FamilyOptic<A, B>> fo = Lists.newArrayList();
/*     */           RecursiveTypeFamily newFamily = new RecursiveTypeFamily(this.name, nc);
/*     */           RecursivePoint.RecursivePointType<?> sType = apply(index);
/*     */           RecursivePoint.RecursivePointType<?> tType = newFamily.apply(index);
/*     */           if (recurse) {
/*     */             FamilyOptic<A, B> arg = ();
/*     */             fo.add(this.template.applyO(arg, aType, bType));
/*     */             OpticParts<A, B> parts = ((FamilyOptic)fo.get(0)).apply(index);
/*     */             return Either.left(mkOptic((Type<?>)sType, (Type<?>)tType, aType, bType, parts));
/*     */           } 
/*     */           return mkSimpleOptic(sType, tType, aType, bType, matcher);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <S, T, A, B> TypedOptic<S, T, A, B> mkOptic(Type<S> sType, Type<T> tType, Type<A> aType, Type<B> bType, OpticParts<A, B> parts) {
/* 135 */     return new TypedOptic(parts
/* 136 */         .bounds(), sType, tType, aType, bType, parts
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 141 */         .optic());
/*     */   }
/*     */ 
/*     */   
/*     */   private <S, T, A, B> Either<TypedOptic<?, ?, A, B>, Type.FieldNotFoundException> mkSimpleOptic(RecursivePoint.RecursivePointType<S> sType, RecursivePoint.RecursivePointType<T> tType, Type<A> aType, Type<B> bType, Type.TypeMatcher<A, B> matcher) {
/* 146 */     return sType.unfold().findType(aType, bType, matcher, false).mapLeft(o -> mkOptic((Type<?>)sType, (Type<?>)tType, o.aType(), o.bType(), new OpticParts(o.bounds(), o.optic())));
/*     */   }
/*     */   
/*     */   public Optional<RewriteResult<?, ?>> everywhere(int index, TypeRewriteRule rule, PointFreeRule optimizationRule) {
/* 150 */     Type<?> sourceType = apply(index).unfold();
/* 151 */     RewriteResult<?, ?> sourceView = (RewriteResult<?, ?>)DataFixUtils.orElse(sourceType.everywhere(rule, optimizationRule, false, false), RewriteResult.nop(sourceType));
/* 152 */     RecursivePoint.RecursivePointType<?> newType = buildMuType(sourceView.view().newType(), null);
/* 153 */     RecursiveTypeFamily newFamily = newType.family();
/*     */     
/* 155 */     List<RewriteResult<?, ?>> views = Lists.newArrayList();
/* 156 */     boolean foundAny = false;
/*     */     
/* 158 */     for (int i = 0; i < this.size; i++) {
/* 159 */       RecursivePoint.RecursivePointType<?> type = apply(i);
/* 160 */       Type<?> unfold = type.unfold();
/* 161 */       boolean nop1 = true;
/*     */       
/* 163 */       RewriteResult<?, ?> view = (RewriteResult<?, ?>)DataFixUtils.orElse(unfold.everywhere(rule, optimizationRule, false, true), RewriteResult.nop(unfold));
/* 164 */       if (!Objects.equals(view.view().function(), Functions.id())) {
/* 165 */         nop1 = false;
/*     */       }
/*     */       
/* 168 */       RecursivePoint.RecursivePointType<?> newMuType = buildMuType(view.view().newType(), newFamily);
/* 169 */       boolean nop = cap2(views, type, rule, optimizationRule, nop1, view, newMuType);
/* 170 */       foundAny = (foundAny || !nop);
/*     */     } 
/* 172 */     if (!foundAny) {
/* 173 */       return Optional.empty();
/*     */     }
/* 175 */     Algebra algebra = new ListAlgebra("everywhere", views);
/* 176 */     RewriteResult<?, ?> fold = fold(algebra).apply(index);
/* 177 */     return Optional.of(RewriteResult.create(viewUnchecked((Type<?>)apply(index), (Type<?>)newType, fold.view().function()), fold.recData()));
/*     */   }
/*     */ 
/*     */   
/*     */   private <A, B> boolean cap2(List<RewriteResult<?, ?>> views, RecursivePoint.RecursivePointType<A> type, TypeRewriteRule rule, PointFreeRule optimizationRule, boolean nop, RewriteResult<?, ?> view, RecursivePoint.RecursivePointType<B> newType) {
/* 182 */     RewriteResult<A, B> newView = RewriteResult.create(newType.in(), new BitSet()).compose(view);
/*     */     
/* 184 */     Optional<RewriteResult<B, ?>> rewrite = rule.rewrite(newView.view().newType());
/* 185 */     if (rewrite.isPresent() && !Objects.equals(((RewriteResult)rewrite.get()).view().function(), Functions.id())) {
/* 186 */       nop = false;
/* 187 */       view = ((RewriteResult)rewrite.get()).compose(newView);
/*     */     } 
/* 189 */     view = RewriteResult.create(view.view().rewriteOrNop(optimizationRule), view.recData());
/* 190 */     views.add(view);
/* 191 */     return nop;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 196 */     return "Mu[" + this.name + ", " + this.size + ", " + this.template + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 201 */     if (this == o) {
/* 202 */       return true;
/*     */     }
/* 204 */     if (!(o instanceof RecursiveTypeFamily)) {
/* 205 */       return false;
/*     */     }
/* 207 */     RecursiveTypeFamily family = (RecursiveTypeFamily)o;
/* 208 */     return Objects.equals(this.template, family.template);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 213 */     return this.hashCode;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\types\families\RecursiveTypeFamily.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */