/*     */ package com.mojang.datafixers.functions;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.View;
/*     */ import com.mojang.datafixers.optics.Optic;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.types.Func;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.Algebra;
/*     */ import com.mojang.datafixers.types.families.ListAlgebra;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import org.apache.commons.lang3.ObjectUtils;
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
/*     */ public interface PointFreeRule
/*     */ {
/*     */   default <A, B> Optional<View<A, B>> rewrite(View<A, B> view) {
/*  36 */     return rewrite(view.getFuncType(), view.function()).map(pf -> View.create(view.type(), view.newType(), pf));
/*     */   }
/*     */   
/*     */   default <A> PointFree<A> rewriteOrNop(Type<A> type, PointFree<A> expr) {
/*  40 */     return (PointFree<A>)DataFixUtils.orElse(rewrite(type, expr), expr);
/*     */   }
/*     */   
/*     */   default <A, B> View<A, B> rewriteOrNop(View<A, B> view) {
/*  44 */     return (View<A, B>)DataFixUtils.orElse(rewrite(view), view);
/*     */   }
/*     */   
/*     */   static PointFreeRule nop() {
/*  48 */     return Nop.INSTANCE;
/*     */   }
/*     */   
/*     */   public enum Nop implements PointFreeRule, Supplier<PointFreeRule> {
/*  52 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public <A> Optional<PointFree<A>> rewrite(Type<A> type, PointFree<A> expr) {
/*  56 */       return Optional.of(expr);
/*     */     }
/*     */ 
/*     */     
/*     */     public PointFreeRule get() {
/*  61 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum BangEta implements PointFreeRule {
/*  66 */     INSTANCE;
/*     */ 
/*     */ 
/*     */     
/*     */     public <A> Optional<? extends PointFree<A>> rewrite(Type<A> type, PointFree<A> expr) {
/*  71 */       if (expr instanceof Bang) {
/*  72 */         return Optional.empty();
/*     */       }
/*  74 */       if (type instanceof Func) {
/*  75 */         Func<?, ?> func = (Func)type;
/*  76 */         if (func.second() instanceof com.mojang.datafixers.types.constant.EmptyPart) {
/*  77 */           return Optional.of((PointFree)Functions.bang());
/*     */         }
/*     */       } 
/*  80 */       return Optional.empty();
/*     */     }
/*     */   }
/*     */   
/*     */   public enum CompAssocLeft implements PointFreeRule {
/*  85 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public <A> Optional<? extends PointFree<A>> rewrite(Type<A> type, PointFree<A> expr) {
/*  89 */       if (expr instanceof Comp) {
/*  90 */         Comp<?, ?, ?> comp2 = (Comp)expr;
/*  91 */         PointFree<? extends Function<?, ?>> second = comp2.second;
/*  92 */         if (second instanceof Comp) {
/*  93 */           Comp<?, ?, ?> comp1 = (Comp)second;
/*  94 */           return (Optional)swap(comp1, comp2);
/*     */         } 
/*     */       } 
/*  97 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     private static <A, B, C, D, E> Optional<PointFree<E>> swap(Comp<A, B, C> comp1, Comp<?, ?, D> comp2raw) {
/* 102 */       Comp<?, ?, D> comp = comp2raw;
/* 103 */       return Optional.of((PointFree)new Comp<>(comp1.middleType, new Comp<>(comp.middleType, comp.first, comp1.first), comp1.second));
/*     */     }
/*     */   }
/*     */   
/*     */   public enum CompAssocRight implements PointFreeRule {
/* 108 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public <A> Optional<? extends PointFree<A>> rewrite(Type<A> type, PointFree<A> expr) {
/* 112 */       if (expr instanceof Comp) {
/* 113 */         Comp<?, ?, ?> comp1 = (Comp)expr;
/* 114 */         PointFree<? extends Function<?, ?>> first = comp1.first;
/* 115 */         if (first instanceof Comp) {
/* 116 */           Comp<?, ?, ?> comp2 = (Comp)first;
/* 117 */           return (Optional)swap(comp1, comp2);
/*     */         } 
/*     */       } 
/* 120 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     private static <A, B, C, D, E> Optional<PointFree<E>> swap(Comp<A, B, D> comp1, Comp<?, C, ?> comp2raw) {
/* 125 */       Comp<?, C, ?> comp = comp2raw;
/* 126 */       return Optional.of((PointFree)new Comp<>(comp.middleType, comp.first, new Comp<>(comp1.middleType, (PointFree)comp.second, comp1.second)));
/*     */     }
/*     */   }
/*     */   
/*     */   public enum LensAppId implements PointFreeRule {
/* 131 */     INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <A> Optional<? extends PointFree<A>> rewrite(Type<A> type, PointFree<A> expr) {
/* 137 */       if (expr instanceof Apply) {
/* 138 */         Apply<?, A> apply = (Apply)expr;
/* 139 */         PointFree<? extends Function<?, A>> func = apply.func;
/* 140 */         if (func instanceof ProfunctorTransformer && Objects.equals(apply.arg, Functions.id())) {
/* 141 */           return Optional.of((PointFree)Functions.id());
/*     */         }
/*     */       } 
/* 144 */       return Optional.empty();
/*     */     }
/*     */   }
/*     */   
/*     */   public enum AppNest implements PointFreeRule {
/* 149 */     INSTANCE;
/*     */ 
/*     */ 
/*     */     
/*     */     public <A> Optional<? extends PointFree<A>> rewrite(Type<A> type, PointFree<A> expr) {
/* 154 */       if (expr instanceof Apply) {
/* 155 */         Apply<?, ?> applyFirst = (Apply)expr;
/* 156 */         if (applyFirst.arg instanceof Apply) {
/* 157 */           Apply<?, ?> applySecond = (Apply)applyFirst.arg;
/* 158 */           return cap(applyFirst, applySecond);
/*     */         } 
/*     */       } 
/* 161 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     private <A, B, C, D, E> Optional<? extends PointFree<A>> cap(Apply<D, E> applyFirst, Apply<B, C> applySecond) {
/* 166 */       PointFree<?> func = applySecond.func;
/* 167 */       return Optional.of(Functions.app(Functions.comp(applyFirst.argType, applyFirst.func, (PointFree)func), applySecond.arg, applySecond.argType));
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface CompRewrite
/*     */     extends PointFreeRule {
/*     */     default <A> Optional<? extends PointFree<A>> rewrite(Type<A> type, PointFree<A> expr) {
/* 174 */       if (expr instanceof Comp) {
/* 175 */         Comp<?, ?, ?> comp = (Comp)expr;
/* 176 */         PointFree<? extends Function<?, ?>> first = comp.first;
/* 177 */         PointFree<? extends Function<?, ?>> second = comp.second;
/* 178 */         if (first instanceof Comp) {
/* 179 */           Comp<?, ?, ?> firstComp = (Comp)first;
/* 180 */           return doRewrite(type, comp.middleType, firstComp.second, comp.second).map(result -> {
/*     */                 if (result instanceof Comp) {
/*     */                   Comp<?, ?, ?> resultComp = (Comp<?, ?, ?>)result;
/*     */                   return buildLeftNested(resultComp, firstComp);
/*     */                 } 
/*     */                 return buildRight(firstComp, result);
/*     */               });
/*     */         } 
/* 188 */         if (second instanceof Comp) {
/* 189 */           Comp<?, ?, ?> secondComp = (Comp)second;
/* 190 */           return doRewrite(type, comp.middleType, comp.first, secondComp.first).map(result -> {
/*     */                 if (result instanceof Comp) {
/*     */                   Comp<?, ?, ?> resultComp = (Comp<?, ?, ?>)result;
/*     */                   return buildRightNested(secondComp, resultComp);
/*     */                 } 
/*     */                 return buildLeft(result, secondComp);
/*     */               });
/*     */         } 
/* 198 */         return (Optional)doRewrite(type, comp.middleType, comp.first, comp.second);
/*     */       } 
/* 200 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     static <A, B, C, D> PointFree<D> buildLeft(PointFree<?> result, Comp<A, B, C> comp) {
/* 205 */       return (PointFree)new Comp<>(comp.middleType, (PointFree)result, comp.second);
/*     */     }
/*     */ 
/*     */     
/*     */     static <A, B, C, D> PointFree<D> buildRight(Comp<A, B, C> comp, PointFree<?> result) {
/* 210 */       return (PointFree)new Comp<>(comp.middleType, comp.first, (PointFree)result);
/*     */     }
/*     */ 
/*     */     
/*     */     static <A, B, C, D, E> PointFree<E> buildLeftNested(Comp<A, B, C> comp1, Comp<?, ?, D> comp2raw) {
/* 215 */       Comp<?, ?, D> comp = comp2raw;
/* 216 */       return (PointFree)new Comp<>(comp1.middleType, new Comp<>(comp.middleType, comp.first, comp1.first), comp1.second);
/*     */     }
/*     */ 
/*     */     
/*     */     static <A, B, C, D, E> PointFree<E> buildRightNested(Comp<A, B, D> comp1, Comp<?, C, ?> comp2raw) {
/* 221 */       Comp<?, C, ?> comp = comp2raw;
/* 222 */       return (PointFree)new Comp<>(comp.middleType, comp.first, new Comp<>(comp1.middleType, (PointFree)comp.second, comp1.second));
/*     */     }
/*     */     
/*     */     <A> Optional<? extends PointFree<?>> doRewrite(Type<A> param1Type, Type<?> param1Type1, PointFree<? extends Function<?, ?>> param1PointFree1, PointFree<? extends Function<?, ?>> param1PointFree2);
/*     */   }
/*     */   
/*     */   public enum SortProj implements CompRewrite {
/* 229 */     INSTANCE;
/*     */ 
/*     */ 
/*     */     
/*     */     public <A> Optional<? extends PointFree<?>> doRewrite(Type<A> type, Type<?> middleType, PointFree<? extends Function<?, ?>> first, PointFree<? extends Function<?, ?>> second) {
/* 234 */       if (first instanceof Apply && second instanceof Apply) {
/* 235 */         Apply<?, ?> applyFirst = (Apply)first;
/* 236 */         Apply<?, ?> applySecond = (Apply)second;
/* 237 */         PointFree<? extends Function<?, ?>> firstFunc = applyFirst.func;
/* 238 */         PointFree<? extends Function<?, ?>> secondFunc = applySecond.func;
/* 239 */         if (firstFunc instanceof ProfunctorTransformer && secondFunc instanceof ProfunctorTransformer) {
/* 240 */           ProfunctorTransformer<?, ?, ?, ?> firstOptic = (ProfunctorTransformer)firstFunc;
/* 241 */           ProfunctorTransformer<?, ?, ?, ?> secondOptic = (ProfunctorTransformer)secondFunc;
/*     */           
/* 243 */           Optic<?, ?, ?, ?, ?> fo = firstOptic.optic;
/* 244 */           while (fo instanceof Optic.CompositionOptic) {
/* 245 */             fo = ((Optic.CompositionOptic)fo).outer();
/*     */           }
/*     */           
/* 248 */           Optic<?, ?, ?, ?, ?> so = secondOptic.optic;
/* 249 */           while (so instanceof Optic.CompositionOptic) {
/* 250 */             so = ((Optic.CompositionOptic)so).outer();
/*     */           }
/*     */           
/* 253 */           if (Objects.equals(fo, Optics.proj2()) && Objects.equals(so, Optics.proj1())) {
/* 254 */             Func<?, ?> firstArg = (Func)applyFirst.argType;
/* 255 */             Func<?, ?> secondArg = (Func)applySecond.argType;
/* 256 */             return Optional.of(cap(firstArg, secondArg, applyFirst, applySecond));
/*     */           } 
/*     */         } 
/*     */       } 
/* 260 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     private <R, A, A2, B, B2> R cap(Func<B, B2> firstArg, Func<A, A2> secondArg, Apply<?, ?> first, Apply<?, ?> second) {
/* 265 */       return (R)Functions.comp(
/* 266 */           DSL.and(secondArg.first(), firstArg.second()), (PointFree)second, (PointFree)first);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public enum SortInj
/*     */     implements CompRewrite
/*     */   {
/* 274 */     INSTANCE;
/*     */ 
/*     */ 
/*     */     
/*     */     public <A> Optional<? extends PointFree<?>> doRewrite(Type<A> type, Type<?> middleType, PointFree<? extends Function<?, ?>> first, PointFree<? extends Function<?, ?>> second) {
/* 279 */       if (first instanceof Apply && second instanceof Apply) {
/* 280 */         Apply<?, ?> applyFirst = (Apply)first;
/* 281 */         Apply<?, ?> applySecond = (Apply)second;
/* 282 */         PointFree<? extends Function<?, ?>> firstFunc = applyFirst.func;
/* 283 */         PointFree<? extends Function<?, ?>> secondFunc = applySecond.func;
/* 284 */         if (firstFunc instanceof ProfunctorTransformer && secondFunc instanceof ProfunctorTransformer) {
/* 285 */           ProfunctorTransformer<?, ?, ?, ?> firstOptic = (ProfunctorTransformer)firstFunc;
/* 286 */           ProfunctorTransformer<?, ?, ?, ?> secondOptic = (ProfunctorTransformer)secondFunc;
/*     */           
/* 288 */           Optic<?, ?, ?, ?, ?> fo = firstOptic.optic;
/* 289 */           while (fo instanceof Optic.CompositionOptic) {
/* 290 */             fo = ((Optic.CompositionOptic)fo).outer();
/*     */           }
/*     */           
/* 293 */           Optic<?, ?, ?, ?, ?> so = secondOptic.optic;
/* 294 */           while (so instanceof Optic.CompositionOptic) {
/* 295 */             so = ((Optic.CompositionOptic)so).outer();
/*     */           }
/*     */           
/* 298 */           if (Objects.equals(fo, Optics.inj2()) && Objects.equals(so, Optics.inj1())) {
/* 299 */             Func<?, ?> firstArg = (Func)applyFirst.argType;
/* 300 */             Func<?, ?> secondArg = (Func)applySecond.argType;
/* 301 */             return Optional.of(cap(firstArg, secondArg, applyFirst, applySecond));
/*     */           } 
/*     */         } 
/*     */       } 
/* 305 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     private <R, A, A2, B, B2> R cap(Func<B, B2> firstArg, Func<A, A2> secondArg, Apply<?, ?> first, Apply<?, ?> second) {
/* 310 */       return (R)Functions.comp(
/* 311 */           DSL.or(secondArg.first(), firstArg.second()), (PointFree)second, (PointFree)first);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public enum LensCompFunc
/*     */     implements PointFreeRule
/*     */   {
/* 319 */     INSTANCE;
/*     */ 
/*     */ 
/*     */     
/*     */     public <A> Optional<? extends PointFree<A>> rewrite(Type<A> type, PointFree<A> expr) {
/* 324 */       if (expr instanceof Comp) {
/* 325 */         Comp<?, ?, ?> comp = (Comp)expr;
/* 326 */         PointFree<? extends Function<?, ?>> first = comp.first;
/* 327 */         PointFree<? extends Function<?, ?>> second = comp.second;
/* 328 */         if (first instanceof ProfunctorTransformer && second instanceof ProfunctorTransformer) {
/* 329 */           ProfunctorTransformer<?, ?, ?, ?> firstOptic = (ProfunctorTransformer)first;
/* 330 */           ProfunctorTransformer<?, ?, ?, ?> secondOptic = (ProfunctorTransformer)second;
/* 331 */           return Optional.of(cap(firstOptic, secondOptic));
/*     */         } 
/*     */       } 
/* 334 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     private <R, X, Y, S, T, A, B> R cap(ProfunctorTransformer<X, Y, ?, ?> first, ProfunctorTransformer<S, T, A, B> second) {
/* 339 */       ProfunctorTransformer<X, Y, ?, ?> profunctorTransformer = first;
/* 340 */       return (R)Functions.profunctorTransformer(profunctorTransformer.optic.compose(second.optic));
/*     */     }
/*     */   }
/*     */   
/*     */   public enum LensComp implements CompRewrite {
/* 345 */     INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <A> Optional<? extends PointFree<?>> doRewrite(Type<A> type, Type<?> middleType, PointFree<? extends Function<?, ?>> first, PointFree<? extends Function<?, ?>> second) {
/* 351 */       if (first instanceof Apply && second instanceof Apply) {
/* 352 */         Apply<?, ?> applyFirst = (Apply)first;
/* 353 */         Apply<?, ?> applySecond = (Apply)second;
/* 354 */         PointFree<? extends Function<?, ?>> firstFunc = applyFirst.func;
/* 355 */         PointFree<? extends Function<?, ?>> secondFunc = applySecond.func;
/* 356 */         if (firstFunc instanceof ProfunctorTransformer && secondFunc instanceof ProfunctorTransformer) {
/* 357 */           ProfunctorTransformer<?, ?, ?, ?> lensPFFirst = (ProfunctorTransformer)firstFunc;
/* 358 */           ProfunctorTransformer<?, ?, ?, ?> lensPFSecond = (ProfunctorTransformer)secondFunc;
/*     */           
/* 360 */           if (Objects.equals(lensPFFirst.optic, lensPFSecond.optic)) {
/* 361 */             Func<?, ?> firstFuncType = (Func)applyFirst.argType;
/* 362 */             Func<?, ?> secondFuncType = (Func)applySecond.argType;
/* 363 */             return cap(lensPFFirst, lensPFSecond, applyFirst.arg, applySecond.arg, firstFuncType, secondFuncType);
/*     */           } 
/*     */         } 
/*     */       } 
/* 367 */       return Optional.empty();
/*     */     }
/*     */     
/*     */     private <R, A, B, C, S, T, U> Optional<? extends PointFree<R>> cap(ProfunctorTransformer<S, T, A, B> l1, ProfunctorTransformer<?, U, ?, C> l2, PointFree<?> f1, PointFree<?> f2, Func<?, ?> firstType, Func<?, ?> secondType) {
/* 371 */       return cap2(l1, (ProfunctorTransformer)l2, (PointFree)f1, (PointFree)f2, (Func)firstType, (Func)secondType);
/*     */     }
/*     */     
/*     */     private <R, P extends com.mojang.datafixers.kinds.K2, Proof extends com.mojang.datafixers.kinds.K1, A, B, C, S, T, U> Optional<? extends PointFree<R>> cap2(ProfunctorTransformer<S, T, A, B> l1, ProfunctorTransformer<T, U, B, C> l2, PointFree<Function<B, C>> f1, PointFree<Function<A, B>> f2, Func<B, C> firstType, Func<A, B> secondType) {
/* 375 */       PointFree<Function<Function<A, C>, Function<S, U>>> lens = l1;
/* 376 */       PointFree<Function<A, C>> arg = Functions.comp(firstType.first(), f1, f2);
/* 377 */       return Optional.of(Functions.app((PointFree)lens, arg, DSL.func(secondType.first(), firstType.second())));
/*     */     }
/*     */   }
/*     */   
/*     */   public enum CataFuseSame implements CompRewrite {
/* 382 */     INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <A> Optional<? extends PointFree<?>> doRewrite(Type<A> type, Type<?> middleType, PointFree<? extends Function<?, ?>> first, PointFree<? extends Function<?, ?>> second) {
/* 388 */       if (first instanceof Fold && second instanceof Fold) {
/*     */         
/* 390 */         Fold<?, ?> firstFold = (Fold)first;
/* 391 */         Fold<?, ?> secondFold = (Fold)second;
/* 392 */         RecursiveTypeFamily family = firstFold.aType.family();
/* 393 */         if (Objects.equals(family, secondFold.aType.family()) && firstFold.index == secondFold.index) {
/*     */           
/* 395 */           List<RewriteResult<?, ?>> newAlgebra = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */           
/* 399 */           boolean foundOne = false;
/* 400 */           for (int i = 0; i < family.size(); i++) {
/* 401 */             RewriteResult<?, ?> firstAlgFunc = firstFold.algebra.apply(i);
/* 402 */             RewriteResult<?, ?> secondAlgFunc = secondFold.algebra.apply(i);
/* 403 */             boolean firstId = Objects.equals(PointFreeRule.CompAssocRight.INSTANCE.<A, B>rewriteOrNop(firstAlgFunc.view()).function(), Functions.id());
/* 404 */             boolean secondId = Objects.equals(secondAlgFunc.view().function(), Functions.id());
/*     */             
/* 406 */             if (firstId && secondId) {
/* 407 */               newAlgebra.add(firstFold.algebra.apply(i));
/* 408 */             } else if (!foundOne && !firstId && !secondId) {
/* 409 */               newAlgebra.add(getCompose(firstAlgFunc, secondAlgFunc));
/* 410 */               foundOne = true;
/*     */             } else {
/* 412 */               return Optional.empty();
/*     */             } 
/*     */           } 
/* 415 */           ListAlgebra listAlgebra = new ListAlgebra("FusedSame", newAlgebra);
/* 416 */           return Optional.of(((RewriteResult)family.fold((Algebra)listAlgebra).apply(firstFold.index)).view().function());
/*     */         } 
/*     */       } 
/* 419 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     private <B> RewriteResult<?, ?> getCompose(RewriteResult<B, ?> firstAlgFunc, RewriteResult<?, ?> secondAlgFunc) {
/* 424 */       return firstAlgFunc.compose(secondAlgFunc);
/*     */     }
/*     */   }
/*     */   
/*     */   public enum CataFuseDifferent implements CompRewrite {
/* 429 */     INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <A> Optional<? extends PointFree<?>> doRewrite(Type<A> type, Type<?> middleType, PointFree<? extends Function<?, ?>> first, PointFree<? extends Function<?, ?>> second) {
/* 435 */       if (first instanceof Fold && second instanceof Fold) {
/*     */         
/* 437 */         Fold<?, ?> firstFold = (Fold)first;
/* 438 */         Fold<?, ?> secondFold = (Fold)second;
/* 439 */         RecursiveTypeFamily family = firstFold.aType.family();
/* 440 */         if (Objects.equals(family, secondFold.aType.family()) && firstFold.index == secondFold.index) {
/*     */           
/* 442 */           List<RewriteResult<?, ?>> newAlgebra = Lists.newArrayList();
/*     */           
/* 444 */           BitSet firstModifies = new BitSet(family.size());
/* 445 */           BitSet secondModifies = new BitSet(family.size());
/*     */           
/* 447 */           for (int i = 0; i < family.size(); i++) {
/* 448 */             RewriteResult<?, ?> firstAlgFunc = firstFold.algebra.apply(i);
/* 449 */             RewriteResult<?, ?> secondAlgFunc = secondFold.algebra.apply(i);
/* 450 */             boolean firstId = Objects.equals(PointFreeRule.CompAssocRight.INSTANCE.<A, B>rewriteOrNop(firstAlgFunc.view()).function(), Functions.id());
/* 451 */             boolean secondId = Objects.equals(secondAlgFunc.view().function(), Functions.id());
/* 452 */             firstModifies.set(i, !firstId);
/* 453 */             secondModifies.set(i, !secondId);
/*     */           } 
/*     */           
/* 456 */           BitSet newSet = (BitSet)ObjectUtils.clone(firstModifies);
/* 457 */           newSet.or(secondModifies);
/*     */ 
/*     */ 
/*     */           
/* 461 */           for (int j = 0; j < family.size(); j++) {
/* 462 */             RewriteResult<?, ?> firstAlgFunc = firstFold.algebra.apply(j);
/* 463 */             RewriteResult<?, ?> secondAlgFunc = secondFold.algebra.apply(j);
/* 464 */             PointFree<?> firstF = PointFreeRule.CompAssocRight.INSTANCE.<A, B>rewriteOrNop(firstAlgFunc.view()).function();
/* 465 */             PointFree<?> secondF = PointFreeRule.CompAssocRight.INSTANCE.<A, B>rewriteOrNop(secondAlgFunc.view()).function();
/* 466 */             boolean firstId = Objects.equals(firstF, Functions.id());
/* 467 */             boolean secondId = Objects.equals(secondF, Functions.id());
/* 468 */             if (firstAlgFunc.recData().intersects(secondModifies) || secondAlgFunc.recData().intersects(firstModifies))
/*     */             {
/* 470 */               return Optional.empty();
/*     */             }
/* 472 */             if (firstId) {
/* 473 */               newAlgebra.add(secondAlgFunc);
/* 474 */             } else if (secondId) {
/* 475 */               newAlgebra.add(firstAlgFunc);
/*     */             } else {
/* 477 */               return Optional.empty();
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/* 482 */           ListAlgebra listAlgebra = new ListAlgebra("FusedDifferent", newAlgebra);
/* 483 */           return Optional.of(((RewriteResult)family.fold((Algebra)listAlgebra).apply(firstFold.index)).view().function());
/*     */         } 
/*     */       } 
/* 486 */       return Optional.empty();
/*     */     }
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
/*     */   static PointFreeRule seq(PointFreeRule first, Supplier<PointFreeRule> second) {
/* 507 */     return seq((List<Supplier<PointFreeRule>>)ImmutableList.of(() -> first, second));
/*     */   }
/*     */   
/*     */   static PointFreeRule seq(List<Supplier<PointFreeRule>> rules) {
/* 511 */     return new Seq(rules);
/*     */   }
/*     */   
/*     */   public static final class Seq implements PointFreeRule {
/*     */     private final List<Supplier<PointFreeRule>> rules;
/*     */     
/*     */     public Seq(List<Supplier<PointFreeRule>> rules) {
/* 518 */       this.rules = (List<Supplier<PointFreeRule>>)ImmutableList.copyOf(rules);
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> Optional<? extends PointFree<A>> rewrite(Type<A> type, PointFree<A> expr) {
/* 523 */       Optional<? extends PointFree<A>> result = Optional.of(expr);
/* 524 */       for (Supplier<PointFreeRule> rule : this.rules) {
/* 525 */         result = result.flatMap(pf -> ((PointFreeRule)rule.get()).rewrite(type, pf));
/*     */       }
/* 527 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 532 */       if (obj == this) {
/* 533 */         return true;
/*     */       }
/* 535 */       if (!(obj instanceof Seq)) {
/* 536 */         return false;
/*     */       }
/* 538 */       Seq that = (Seq)obj;
/* 539 */       return Objects.equals(this.rules, that.rules);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 544 */       return Objects.hash(new Object[] { this.rules });
/*     */     }
/*     */   }
/*     */   
/*     */   static PointFreeRule orElse(PointFreeRule first, PointFreeRule second) {
/* 549 */     return new OrElse(first, () -> second);
/*     */   }
/*     */   
/*     */   static PointFreeRule orElseStrict(PointFreeRule first, Supplier<PointFreeRule> second) {
/* 553 */     return new OrElse(first, second);
/*     */   }
/*     */   
/*     */   public static final class OrElse implements PointFreeRule {
/*     */     protected final PointFreeRule first;
/*     */     protected final Supplier<PointFreeRule> second;
/*     */     
/*     */     public OrElse(PointFreeRule first, Supplier<PointFreeRule> second) {
/* 561 */       this.first = first;
/* 562 */       this.second = second;
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> Optional<? extends PointFree<A>> rewrite(Type<A> type, PointFree<A> expr) {
/* 567 */       Optional<? extends PointFree<A>> view = this.first.rewrite(type, expr);
/* 568 */       if (view.isPresent()) {
/* 569 */         return view;
/*     */       }
/* 571 */       return ((PointFreeRule)this.second.get()).rewrite(type, expr);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 576 */       if (obj == this) {
/* 577 */         return true;
/*     */       }
/* 579 */       if (!(obj instanceof OrElse)) {
/* 580 */         return false;
/*     */       }
/* 582 */       OrElse that = (OrElse)obj;
/* 583 */       return (Objects.equals(this.first, that.first) && Objects.equals(this.second, that.second));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 588 */       return Objects.hash(new Object[] { this.first, this.second });
/*     */     }
/*     */   }
/*     */   
/*     */   static PointFreeRule all(PointFreeRule rule) {
/* 593 */     return new All(rule);
/*     */   }
/*     */   
/*     */   static PointFreeRule one(PointFreeRule rule) {
/* 597 */     return new One(rule);
/*     */   }
/*     */   
/*     */   static PointFreeRule once(PointFreeRule rule) {
/* 601 */     return orElseStrict(rule, () -> one(once(rule)));
/*     */   }
/*     */   
/*     */   static PointFreeRule many(PointFreeRule rule) {
/* 605 */     return new Many(rule);
/*     */   }
/*     */   
/*     */   static PointFreeRule everywhere(PointFreeRule rule) {
/* 609 */     return seq(orElse(rule, Nop.INSTANCE), () -> all(everywhere(rule)));
/*     */   }
/*     */   
/*     */   <A> Optional<? extends PointFree<A>> rewrite(Type<A> paramType, PointFree<A> paramPointFree);
/*     */   
/*     */   public static final class All implements PointFreeRule {
/*     */     public All(PointFreeRule rule) {
/* 616 */       this.rule = rule;
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> Optional<? extends PointFree<A>> rewrite(Type<A> type, PointFree<A> expr) {
/* 621 */       return expr.all(this.rule, type);
/*     */     }
/*     */     private final PointFreeRule rule;
/*     */     
/*     */     public boolean equals(Object obj) {
/* 626 */       if (obj == this) {
/* 627 */         return true;
/*     */       }
/* 629 */       if (!(obj instanceof All)) {
/* 630 */         return false;
/*     */       }
/* 632 */       All that = (All)obj;
/* 633 */       return Objects.equals(this.rule, that.rule);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 638 */       return this.rule.hashCode();
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class One implements PointFreeRule {
/*     */     private final PointFreeRule rule;
/*     */     
/*     */     public One(PointFreeRule rule) {
/* 646 */       this.rule = rule;
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> Optional<? extends PointFree<A>> rewrite(Type<A> type, PointFree<A> expr) {
/* 651 */       return expr.one(this.rule, type);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 656 */       if (obj == this) {
/* 657 */         return true;
/*     */       }
/* 659 */       if (!(obj instanceof One)) {
/* 660 */         return false;
/*     */       }
/* 662 */       One that = (One)obj;
/* 663 */       return Objects.equals(this.rule, that.rule);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 668 */       return this.rule.hashCode();
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Many implements PointFreeRule {
/*     */     private final PointFreeRule rule;
/*     */     
/*     */     public Many(PointFreeRule rule) {
/* 676 */       this.rule = rule;
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> Optional<? extends PointFree<A>> rewrite(Type<A> type, PointFree<A> expr) {
/* 681 */       Optional<? extends PointFree<A>> result = Optional.of(expr);
/*     */       while (true) {
/* 683 */         Optional<? extends PointFree<A>> newResult = result.flatMap(e -> this.rule.rewrite(type, e).map(()));
/* 684 */         if (!newResult.isPresent()) {
/* 685 */           return result;
/*     */         }
/* 687 */         result = newResult;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 693 */       if (this == o) {
/* 694 */         return true;
/*     */       }
/* 696 */       if (o == null || getClass() != o.getClass()) {
/* 697 */         return false;
/*     */       }
/* 699 */       Many many = (Many)o;
/* 700 */       return Objects.equals(this.rule, many.rule);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 705 */       return Objects.hash(new Object[] { this.rule });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\functions\PointFreeRule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */