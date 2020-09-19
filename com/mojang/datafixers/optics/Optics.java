/*     */ package com.mojang.datafixers.optics;
/*     */ 
/*     */ import com.mojang.datafixers.FunctionType;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.App2;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.optics.profunctors.AffineP;
/*     */ import com.mojang.datafixers.optics.profunctors.Cartesian;
/*     */ import com.mojang.datafixers.optics.profunctors.Cocartesian;
/*     */ import com.mojang.datafixers.optics.profunctors.GetterP;
/*     */ import com.mojang.datafixers.optics.profunctors.Profunctor;
/*     */ import com.mojang.datafixers.optics.profunctors.TraversalP;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Optics
/*     */ {
/*     */   public static <S, T, A, B> Adapter<S, T, A, B> toAdapter(Optic<? super Profunctor.Mu, S, T, A, B> optic) {
/*  26 */     Function<App2<Adapter.Mu<A, B>, A, B>, App2<Adapter.Mu<A, B>, S, T>> eval = optic.eval((App)new Adapter.Instance<>());
/*  27 */     return Adapter.unbox(eval.apply(adapter(Function.identity(), Function.identity())));
/*     */   }
/*     */   
/*     */   public static <S, T, A, B> Lens<S, T, A, B> toLens(Optic<? super Cartesian.Mu, S, T, A, B> optic) {
/*  31 */     Function<App2<Lens.Mu<A, B>, A, B>, App2<Lens.Mu<A, B>, S, T>> eval = optic.eval((App)new Lens.Instance<>());
/*  32 */     return Lens.unbox(eval.apply(lens(Function.identity(), (b, a) -> b)));
/*     */   }
/*     */   
/*     */   public static <S, T, A, B> Prism<S, T, A, B> toPrism(Optic<? super Cocartesian.Mu, S, T, A, B> optic) {
/*  36 */     Function<App2<Prism.Mu<A, B>, A, B>, App2<Prism.Mu<A, B>, S, T>> eval = optic.eval((App)new Prism.Instance<>());
/*  37 */     return Prism.unbox(eval.apply(prism(Either::right, Function.identity())));
/*     */   }
/*     */   
/*     */   public static <S, T, A, B> Affine<S, T, A, B> toAffine(Optic<? super AffineP.Mu, S, T, A, B> optic) {
/*  41 */     Function<App2<Affine.Mu<A, B>, A, B>, App2<Affine.Mu<A, B>, S, T>> eval = optic.eval((App)new Affine.Instance<>());
/*  42 */     return Affine.unbox(eval.apply(affine(Either::right, (b, a) -> b)));
/*     */   }
/*     */   
/*     */   public static <S, T, A, B> Getter<S, T, A, B> toGetter(Optic<? super GetterP.Mu, S, T, A, B> optic) {
/*  46 */     Function<App2<Getter.Mu<A, B>, A, B>, App2<Getter.Mu<A, B>, S, T>> eval = optic.eval((App)new Getter.Instance<>());
/*  47 */     return Getter.unbox(eval.apply(getter(Function.identity())));
/*     */   }
/*     */   
/*     */   public static <S, T, A, B> Traversal<S, T, A, B> toTraversal(Optic<? super TraversalP.Mu, S, T, A, B> optic) {
/*  51 */     Function<App2<Traversal.Mu<A, B>, A, B>, App2<Traversal.Mu<A, B>, S, T>> eval = optic.eval((App)new Traversal.Instance<>());
/*  52 */     return Traversal.unbox(eval.apply(new Traversal<A, B, A, B>()
/*     */           {
/*     */             public <F extends com.mojang.datafixers.kinds.K1> FunctionType<A, App<F, B>> wander(Applicative<F, ?> applicative, FunctionType<A, App<F, B>> input) {
/*  55 */               return input;
/*     */             }
/*     */           }));
/*     */   }
/*     */   
/*     */   static <S, T, A, B, F> Lens<S, T, Pair<F, A>, B> merge(Lens<S, ?, F, ?> getter, Lens<S, T, A, B> lens) {
/*  61 */     return lens(s -> Pair.of(getter.view(s), lens.view(s)), lens::update);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <S, T> Adapter<S, T, S, T> id() {
/*  68 */     return new IdAdapter<>();
/*     */   }
/*     */   
/*     */   public static <S, T, A, B> Adapter<S, T, A, B> adapter(final Function<S, A> from, final Function<B, T> to) {
/*  72 */     return new Adapter<S, T, A, B>()
/*     */       {
/*     */         public A from(S s) {
/*  75 */           return from.apply(s);
/*     */         }
/*     */ 
/*     */         
/*     */         public T to(B b) {
/*  80 */           return to.apply(b);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <S, T, A, B> Lens<S, T, A, B> lens(final Function<S, A> view, final BiFunction<B, S, T> update) {
/*  86 */     return new Lens<S, T, A, B>()
/*     */       {
/*     */         public A view(S s) {
/*  89 */           return view.apply(s);
/*     */         }
/*     */ 
/*     */         
/*     */         public T update(B b, S s) {
/*  94 */           return update.apply(b, s);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <S, T, A, B> Prism<S, T, A, B> prism(final Function<S, Either<T, A>> match, final Function<B, T> build) {
/* 100 */     return new Prism<S, T, A, B>()
/*     */       {
/*     */         public Either<T, A> match(S s) {
/* 103 */           return match.apply(s);
/*     */         }
/*     */ 
/*     */         
/*     */         public T build(B b) {
/* 108 */           return build.apply(b);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <S, T, A, B> Affine<S, T, A, B> affine(final Function<S, Either<T, A>> preview, final BiFunction<B, S, T> build) {
/* 114 */     return new Affine<S, T, A, B>()
/*     */       {
/*     */         public Either<T, A> preview(S s) {
/* 117 */           return preview.apply(s);
/*     */         }
/*     */ 
/*     */         
/*     */         public T set(B b, S s) {
/* 122 */           return build.apply(b, s);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <S, T, A, B> Getter<S, T, A, B> getter(Function<S, A> get) {
/* 128 */     return get::apply;
/*     */   }
/*     */   
/*     */   public static <R, A, B> Forget<R, A, B> forget(Function<A, R> function) {
/* 132 */     return function::apply;
/*     */   }
/*     */   
/*     */   public static <R, A, B> ForgetOpt<R, A, B> forgetOpt(Function<A, Optional<R>> function) {
/* 136 */     return function::apply;
/*     */   }
/*     */   
/*     */   public static <R, A, B> ForgetE<R, A, B> forgetE(Function<A, Either<B, R>> function) {
/* 140 */     return function::apply;
/*     */   }
/*     */   
/*     */   public static <R, A, B> ReForget<R, A, B> reForget(Function<R, B> function) {
/* 144 */     return function::apply;
/*     */   }
/*     */   
/*     */   public static <S, T, A, B> Grate<S, T, A, B> grate(FunctionType<FunctionType<FunctionType<S, A>, B>, T> grate) {
/* 148 */     return grate::apply;
/*     */   }
/*     */   
/*     */   public static <R, A, B> ReForgetEP<R, A, B> reForgetEP(final String name, final Function<Either<A, Pair<A, R>>, B> function) {
/* 152 */     return new ReForgetEP<R, A, B>()
/*     */       {
/*     */         public B run(Either<A, Pair<A, R>> e) {
/* 155 */           return function.apply(e);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 160 */           return "ReForgetEP_" + name;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <R, A, B> ReForgetE<R, A, B> reForgetE(final String name, final Function<Either<A, R>, B> function) {
/* 166 */     return new ReForgetE<R, A, B>()
/*     */       {
/*     */         public B run(Either<A, R> t) {
/* 169 */           return function.apply(t);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 174 */           return "ReForgetE_" + name;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <R, A, B> ReForgetP<R, A, B> reForgetP(final String name, final BiFunction<A, R, B> function) {
/* 180 */     return new ReForgetP<R, A, B>()
/*     */       {
/*     */         public B run(A a, R r) {
/* 183 */           return function.apply(a, r);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 188 */           return "ReForgetP_" + name;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <R, A, B> ReForgetC<R, A, B> reForgetC(final String name, final Either<Function<R, B>, BiFunction<A, R, B>> either) {
/* 194 */     return new ReForgetC<R, A, B>()
/*     */       {
/*     */         public Either<Function<R, B>, BiFunction<A, R, B>> impl() {
/* 197 */           return either;
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 202 */           return "ReForgetC_" + name;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <I, J, X> PStore<I, J, X> pStore(final Function<J, X> peek, final Supplier<I> pos) {
/* 208 */     return new PStore<I, J, X>()
/*     */       {
/*     */         public X peek(J j) {
/* 211 */           return peek.apply(j);
/*     */         }
/*     */ 
/*     */         
/*     */         public I pos() {
/* 216 */           return pos.get();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <A, B> Function<A, B> getFunc(App2<FunctionType.Mu, A, B> box) {
/* 222 */     return FunctionType.unbox(box);
/*     */   }
/*     */   
/*     */   public static <F, G, F2> Proj1<F, G, F2> proj1() {
/* 226 */     return new Proj1<>();
/*     */   }
/*     */   
/*     */   public static <F, G, G2> Proj2<F, G, G2> proj2() {
/* 230 */     return new Proj2<>();
/*     */   }
/*     */   
/*     */   public static <F, G, F2> Inj1<F, G, F2> inj1() {
/* 234 */     return new Inj1<>();
/*     */   }
/*     */   
/*     */   public static <F, G, G2> Inj2<F, G, G2> inj2() {
/* 238 */     return new Inj2<>();
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
/*     */   
/*     */   public static <F, G, F2, G2, A, B> Lens<Either<F, G>, Either<F2, G2>, A, B> eitherLens(Lens<F, F2, A, B> fLens, Lens<G, G2, A, B> gLens) {
/* 267 */     return lens(either -> either.map(fLens::view, gLens::view), (b, either) -> either.mapBoth((), ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <F, G, F2, G2, A, B> Affine<Either<F, G>, Either<F2, G2>, A, B> eitherAffine(Affine<F, F2, A, B> fAffine, Affine<G, G2, A, B> gAffine) {
/* 274 */     return affine(either -> (Either)either.map((), ()), (b, either) -> either.mapBoth((), ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <F, G, F2, G2, A, B> Traversal<Either<F, G>, Either<F2, G2>, A, B> eitherTraversal(final Traversal<F, F2, A, B> fOptic, final Traversal<G, G2, A, B> gOptic) {
/* 284 */     return new Traversal<Either<F, G>, Either<F2, G2>, A, B>()
/*     */       {
/*     */         public <FT extends com.mojang.datafixers.kinds.K1> FunctionType<Either<F, G>, App<FT, Either<F2, G2>>> wander(Applicative<FT, ?> applicative, FunctionType<A, App<FT, B>> input) {
/* 287 */           return e -> (App)e.map((), ());
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\Optics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */