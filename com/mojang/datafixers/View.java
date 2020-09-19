/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.mojang.datafixers.functions.Functions;
/*     */ import com.mojang.datafixers.functions.PointFree;
/*     */ import com.mojang.datafixers.functions.PointFreeRule;
/*     */ import com.mojang.datafixers.kinds.App2;
/*     */ import com.mojang.datafixers.kinds.K2;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ public final class View<A, B>
/*     */   implements App2<View.Mu, A, B>
/*     */ {
/*     */   private final Type<A> type;
/*     */   protected final Type<B> newType;
/*     */   
/*     */   static <A, B> View<A, B> unbox(App2<Mu, A, B> box) {
/*  21 */     return (View)box;
/*     */   } private final PointFree<Function<A, B>> function;
/*     */   static final class Mu implements K2 {}
/*     */   public static <A> View<A, A> nopView(Type<A> type) {
/*  25 */     return create(type, type, Functions.id());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public View(Type<A> type, Type<B> newType, PointFree<Function<A, B>> function) {
/*  33 */     this.type = type;
/*  34 */     this.newType = newType;
/*  35 */     this.function = function;
/*     */   }
/*     */   
/*     */   public Type<A> type() {
/*  39 */     return this.type;
/*     */   }
/*     */   
/*     */   public Type<B> newType() {
/*  43 */     return this.newType;
/*     */   }
/*     */   
/*     */   public PointFree<Function<A, B>> function() {
/*  47 */     return this.function;
/*     */   }
/*     */   
/*     */   public Type<Function<A, B>> getFuncType() {
/*  51 */     return DSL.func(this.type, this.newType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  56 */     return "View[" + this.function + "," + this.newType + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  61 */     if (this == o) {
/*  62 */       return true;
/*     */     }
/*  64 */     if (o == null || getClass() != o.getClass()) {
/*  65 */       return false;
/*     */     }
/*  67 */     View<?, ?> view = (View<?, ?>)o;
/*  68 */     return (Objects.equals(this.type, view.type) && Objects.equals(this.newType, view.newType) && Objects.equals(this.function, view.function));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  73 */     return Objects.hash(new Object[] { this.type, this.newType, this.function });
/*     */   }
/*     */   
/*     */   public Optional<? extends View<A, B>> rewrite(PointFreeRule rule) {
/*  77 */     return rule.rewrite(DSL.func(this.type, this.newType), function()).map(f -> create(this.type, this.newType, f));
/*     */   }
/*     */   
/*     */   public View<A, B> rewriteOrNop(PointFreeRule rule) {
/*  81 */     return DataFixUtils.<View<A, B>>orElse(rewrite(rule), this);
/*     */   }
/*     */   
/*     */   public <C> View<A, C> flatMap(Function<Type<B>, View<B, C>> function) {
/*  85 */     View<B, C> instance = function.apply(this.newType);
/*  86 */     return new View(this.type, instance.newType, Functions.comp(this.newType, instance.function(), function()));
/*     */   }
/*     */   
/*     */   public static <A, B> View<A, B> create(Type<A> type, Type<B> newType, PointFree<Function<A, B>> function) {
/*  90 */     return new View<>(type, newType, function);
/*     */   }
/*     */   
/*     */   public static <A, B> View<A, B> create(String name, Type<A> type, Type<B> newType, Function<DynamicOps<?>, Function<A, B>> function) {
/*  94 */     return new View<>(type, newType, Functions.fun(name, function));
/*     */   }
/*     */ 
/*     */   
/*     */   public <C> View<C, B> compose(View<C, A> that) {
/*  99 */     if (Objects.equals(function(), Functions.id())) {
/* 100 */       return new View(that.type(), newType(), that.function());
/*     */     }
/* 102 */     if (Objects.equals(that.function(), Functions.id())) {
/* 103 */       return new View(that.type(), newType(), function());
/*     */     }
/* 105 */     return create(that.type, this.newType, Functions.comp(that.newType, function(), that.function()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\View.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */