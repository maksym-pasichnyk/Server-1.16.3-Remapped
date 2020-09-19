/*     */ package com.mojang.datafixers;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.App2;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.Const;
/*     */ import com.mojang.datafixers.kinds.IdF;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.kinds.K2;
/*     */ import com.mojang.datafixers.kinds.Monoid;
/*     */ import com.mojang.datafixers.optics.Forget;
/*     */ import com.mojang.datafixers.optics.ForgetOpt;
/*     */ import com.mojang.datafixers.optics.Inj2;
/*     */ import com.mojang.datafixers.optics.Optic;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.optics.ReForgetC;
/*     */ import com.mojang.datafixers.optics.Traversal;
/*     */ import com.mojang.datafixers.optics.profunctors.TraversalP;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.RecursivePoint;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ public final class Typed<A> {
/*     */   protected final Type<A> type;
/*     */   
/*     */   public Typed(Type<A> type, DynamicOps<?> ops, A value) {
/*  37 */     this.type = type;
/*  38 */     this.ops = ops;
/*  39 */     this.value = value;
/*     */   }
/*     */   protected final DynamicOps<?> ops; protected final A value;
/*     */   
/*     */   public String toString() {
/*  44 */     return "Typed[" + this.value + "]";
/*     */   }
/*     */   
/*     */   public <FT> FT get(OpticFinder<FT> optic) {
/*  48 */     return (FT)Forget.unbox(((TypedOptic)optic.<A>findType(this.type, false).orThrow()).apply(new TypeToken<Forget.Instance.Mu<FT>>() {  }, (App)new Forget.Instance(), 
/*     */ 
/*     */           
/*  51 */           (App2)Optics.forget(Function.identity())))
/*  52 */       .run(this.value);
/*     */   }
/*     */   
/*     */   public <FT> Typed<FT> getTyped(OpticFinder<FT> optic) {
/*  56 */     TypedOptic<A, ?, FT, FT> o = (TypedOptic<A, ?, FT, FT>)optic.<A>findType(this.type, false).orThrow();
/*  57 */     return new Typed(o.aType(), this.ops, (A)Forget.unbox(o.apply(new TypeToken<Forget.Instance.Mu<FT>>() {  }, (App<Forget.Instance.Mu<FT>, K2>)new Forget.Instance(), 
/*     */ 
/*     */             
/*  60 */             (App2<K2, FT, FT>)Optics.forget(Function.identity())))
/*  61 */         .run(this.value));
/*     */   }
/*     */   
/*     */   public <FT> Optional<FT> getOptional(OpticFinder<FT> optic) {
/*  65 */     TypedOptic<A, ?, FT, FT> optic1 = (TypedOptic<A, ?, FT, FT>)optic.<A>findType(this.type, false).orThrow();
/*  66 */     return ForgetOpt.unbox(optic1.apply(new TypeToken<ForgetOpt.Instance.Mu<FT>>() {  }, (App<ForgetOpt.Instance.Mu<FT>, K2>)new ForgetOpt.Instance(), 
/*     */ 
/*     */           
/*  69 */           (App2<K2, FT, FT>)Optics.forgetOpt(Optional::of)))
/*  70 */       .run(this.value);
/*     */   }
/*     */   
/*     */   public <FT> FT getOrCreate(OpticFinder<FT> optic) {
/*  74 */     return (FT)DataFixUtils.or(getOptional(optic), () -> optic.type().point(this.ops)).orElseThrow(() -> new IllegalStateException("Could not create default value for type: " + optic.type()));
/*     */   }
/*     */   
/*     */   public <FT> FT getOrDefault(OpticFinder<FT> optic, FT def) {
/*  78 */     return ForgetOpt.unbox(((TypedOptic)optic.<A>findType(this.type, false).orThrow()).apply(new TypeToken<ForgetOpt.Instance.Mu<FT>>() {  }, (App)new ForgetOpt.Instance(), 
/*     */ 
/*     */           
/*  81 */           (App2)Optics.forgetOpt(Optional::of)))
/*  82 */       .run(this.value).orElse(def);
/*     */   }
/*     */   
/*     */   public <FT> Optional<Typed<FT>> getOptionalTyped(OpticFinder<FT> optic) {
/*  86 */     TypedOptic<A, ?, FT, FT> o = (TypedOptic<A, ?, FT, FT>)optic.<A>findType(this.type, false).orThrow();
/*  87 */     return ForgetOpt.unbox(o.apply(new TypeToken<ForgetOpt.Instance.Mu<FT>>() {  }, (App<ForgetOpt.Instance.Mu<FT>, K2>)new ForgetOpt.Instance(), 
/*     */ 
/*     */           
/*  90 */           (App2<K2, FT, FT>)Optics.forgetOpt(Optional::of)))
/*  91 */       .run(this.value).map(v -> new Typed(o.aType(), this.ops, (A)v));
/*     */   }
/*     */   
/*     */   public <FT> Typed<FT> getOrCreateTyped(OpticFinder<FT> optic) {
/*  95 */     return (Typed<FT>)DataFixUtils.or(getOptionalTyped(optic), () -> optic.type().pointTyped(this.ops)).orElseThrow(() -> new IllegalStateException("Could not create default value for type: " + optic.type()));
/*     */   }
/*     */   
/*     */   public <FT> Typed<?> set(OpticFinder<FT> optic, FT newValue) {
/*  99 */     return set(optic, new Typed(optic.type(), this.ops, (A)newValue));
/*     */   }
/*     */   
/*     */   public <FT, FR> Typed<?> set(OpticFinder<FT> optic, Type<FR> newType, FR newValue) {
/* 103 */     return set(optic, new Typed(newType, this.ops, (A)newValue));
/*     */   }
/*     */   
/*     */   public <FT, FR> Typed<?> set(OpticFinder<FT> optic, Typed<FR> newValue) {
/* 107 */     TypedOptic<A, ?, FT, FR> field = (TypedOptic<A, ?, FT, FR>)optic.<A, FR>findType(this.type, newValue.type, false).orThrow();
/* 108 */     return setCap(field, newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <B, FT, FR> Typed<B> setCap(TypedOptic<A, B, FT, FR> field, Typed<FR> newValue) {
/* 116 */     B b = (B)ReForgetC.unbox(field.apply(new TypeToken<ReForgetC.Instance.Mu<FR>>() {  }, (App<ReForgetC.Instance.Mu<FR>, K2>)new ReForgetC.Instance(), (App2<K2, FT, FR>)Optics.reForgetC("set", Either.left(Function.identity())))).run(this.value, newValue.value);
/* 117 */     return new Typed(field.tType(), this.ops, (A)b);
/*     */   }
/*     */   
/*     */   public <FT> Typed<?> updateTyped(OpticFinder<FT> optic, Function<Typed<?>, Typed<?>> updater) {
/* 121 */     return updateTyped(optic, optic.type(), updater);
/*     */   }
/*     */   
/*     */   public <FT, FR> Typed<?> updateTyped(OpticFinder<FT> optic, Type<FR> newType, Function<Typed<?>, Typed<?>> updater) {
/* 125 */     TypedOptic<A, ?, FT, FR> field = (TypedOptic<A, ?, FT, FR>)optic.<A, FR>findType(this.type, newType, false).orThrow();
/* 126 */     return updateCap(field, ft -> {
/*     */           Typed<?> newValue = updater.apply(new Typed(optic.type(), this.ops, (A)ft));
/*     */           return field.bType().ifSame(newValue).orElseThrow(());
/*     */         });
/*     */   }
/*     */   
/*     */   public <FT> Typed<?> update(OpticFinder<FT> optic, Function<FT, FT> updater) {
/* 133 */     return update(optic, optic.type(), updater);
/*     */   }
/*     */   
/*     */   public <FT, FR> Typed<?> update(OpticFinder<FT> optic, Type<FR> newType, Function<FT, FR> updater) {
/* 137 */     TypedOptic<A, ?, FT, FR> field = (TypedOptic<A, ?, FT, FR>)optic.<A, FR>findType(this.type, newType, false).orThrow();
/* 138 */     return updateCap(field, updater);
/*     */   }
/*     */   
/*     */   public <FT> Typed<?> updateRecursiveTyped(OpticFinder<FT> optic, Function<Typed<?>, Typed<?>> updater) {
/* 142 */     return updateRecursiveTyped(optic, optic.type(), updater);
/*     */   }
/*     */   
/*     */   public <FT, FR> Typed<?> updateRecursiveTyped(OpticFinder<FT> optic, Type<FR> newType, Function<Typed<?>, Typed<?>> updater) {
/* 146 */     TypedOptic<A, ?, FT, FR> field = (TypedOptic<A, ?, FT, FR>)optic.<A, FR>findType(this.type, newType, true).orThrow();
/* 147 */     return updateCap(field, ft -> {
/*     */           Typed<?> newValue = updater.apply(new Typed(optic.type(), this.ops, (A)ft));
/*     */           return field.bType().ifSame(newValue).orElseThrow(());
/*     */         });
/*     */   }
/*     */   
/*     */   public <FT> Typed<?> updateRecursive(OpticFinder<FT> optic, Function<FT, FT> updater) {
/* 154 */     return updateRecursive(optic, optic.type(), updater);
/*     */   }
/*     */   
/*     */   public <FT, FR> Typed<?> updateRecursive(OpticFinder<FT> optic, Type<FR> newType, Function<FT, FR> updater) {
/* 158 */     TypedOptic<A, ?, FT, FR> field = (TypedOptic<A, ?, FT, FR>)optic.<A, FR>findType(this.type, newType, true).orThrow();
/* 159 */     return updateCap(field, updater);
/*     */   }
/*     */   
/*     */   private <B, FT, FR> Typed<B> updateCap(TypedOptic<A, B, FT, FR> field, Function<FT, FR> updater) {
/* 163 */     Traversal<A, B, FT, FR> traversal = Optics.toTraversal((Optic)field.<K1>upCast(TraversalP.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new));
/* 164 */     B b = (B)IdF.get(traversal.wander((Applicative)IdF.Instance.INSTANCE, ft -> IdF.create(updater.apply(ft))).apply(this.value));
/* 165 */     return new Typed(field.tType(), this.ops, (A)b);
/*     */   }
/*     */   
/*     */   public <FT> List<Typed<FT>> getAllTyped(OpticFinder<FT> optic) {
/* 169 */     TypedOptic<A, ?, FT, ?> field = (TypedOptic<A, ?, FT, ?>)optic.<A, FR>findType(this.type, optic.type(), false).orThrow();
/* 170 */     return (List<Typed<FT>>)getAll(field).stream().map(ft -> new Typed(optic.type(), this.ops, (A)ft)).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public <FT> List<FT> getAll(TypedOptic<A, ?, FT, ?> field) {
/* 174 */     Traversal<A, ?, FT, ?> traversal = Optics.toTraversal((Optic)field.<K1>upCast(TraversalP.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new));
/* 175 */     return (List<FT>)Const.unbox(traversal.wander((Applicative)new Const.Instance(Monoid.listMonoid()), ft -> Const.create(ImmutableList.of(ft))).apply(this.value));
/*     */   }
/*     */   
/*     */   public Typed<A> out() {
/* 179 */     if (!(this.type instanceof RecursivePoint.RecursivePointType)) {
/* 180 */       throw new IllegalArgumentException("Not recursive");
/*     */     }
/* 182 */     Type<A> unfold = ((RecursivePoint.RecursivePointType)this.type).unfold();
/* 183 */     return new Typed(unfold, this.ops, this.value);
/*     */   }
/*     */   
/*     */   public <B> Typed<Either<A, B>> inj1(Type<B> type) {
/* 187 */     return new Typed((Type)DSL.or(this.type, type), this.ops, (A)(new Inj1()).build(this.value));
/*     */   }
/*     */   
/*     */   public <B> Typed<Either<B, A>> inj2(Type<B> type) {
/* 191 */     return new Typed((Type)DSL.or(type, this.type), this.ops, (A)(new Inj2()).build(this.value));
/*     */   }
/*     */   
/*     */   public static <A, B> Typed<Pair<A, B>> pair(Typed<A> first, Typed<B> second) {
/* 195 */     return new Typed<>(DSL.and(first.type, second.type), first.ops, Pair.of(first.value, second.value));
/*     */   }
/*     */   
/*     */   public Type<A> getType() {
/* 199 */     return this.type;
/*     */   }
/*     */   
/*     */   public DynamicOps<?> getOps() {
/* 203 */     return this.ops;
/*     */   }
/*     */   
/*     */   public A getValue() {
/* 207 */     return this.value;
/*     */   }
/*     */   
/*     */   public DataResult<? extends Dynamic<?>> write() {
/* 211 */     return this.type.writeDynamic(this.ops, this.value);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\Typed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */