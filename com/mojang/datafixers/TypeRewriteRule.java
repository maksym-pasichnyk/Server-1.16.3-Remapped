/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.functions.Functions;
/*     */ import com.mojang.datafixers.functions.PointFreeRule;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface TypeRewriteRule
/*     */ {
/*     */   static TypeRewriteRule nop() {
/*  20 */     return Nop.INSTANCE;
/*     */   }
/*     */   
/*     */   public enum Nop implements TypeRewriteRule, Supplier<TypeRewriteRule> {
/*  24 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> type) {
/*  28 */       return Optional.of(RewriteResult.nop(type));
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeRewriteRule get() {
/*  33 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   static TypeRewriteRule seq(List<TypeRewriteRule> rules) {
/*  38 */     return new Seq(rules);
/*     */   }
/*     */   
/*     */   static TypeRewriteRule seq(TypeRewriteRule first, TypeRewriteRule second) {
/*  42 */     if (Objects.equals(first, nop())) {
/*  43 */       return second;
/*     */     }
/*  45 */     if (Objects.equals(second, nop())) {
/*  46 */       return first;
/*     */     }
/*  48 */     return seq((List<TypeRewriteRule>)ImmutableList.of(first, second));
/*     */   }
/*     */   
/*     */   static TypeRewriteRule seq(TypeRewriteRule firstRule, TypeRewriteRule... rules) {
/*  52 */     if (rules.length == 0) {
/*  53 */       return firstRule;
/*     */     }
/*  55 */     int lastRule = rules.length - 1;
/*  56 */     TypeRewriteRule tail = rules[lastRule];
/*  57 */     while (lastRule > 0) {
/*  58 */       lastRule--;
/*  59 */       tail = seq(rules[lastRule], tail);
/*     */     } 
/*  61 */     return seq(firstRule, tail);
/*     */   }
/*     */   
/*     */   public static final class Seq implements TypeRewriteRule {
/*     */     protected final List<TypeRewriteRule> rules;
/*     */     private final int hashCode;
/*     */     
/*     */     public Seq(List<TypeRewriteRule> rules) {
/*  69 */       this.rules = (List<TypeRewriteRule>)ImmutableList.copyOf(rules);
/*  70 */       this.hashCode = this.rules.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> type) {
/*  75 */       RewriteResult<A, ?> result = RewriteResult.nop(type);
/*  76 */       for (TypeRewriteRule rule : this.rules) {
/*  77 */         Optional<RewriteResult<A, ?>> newResult = cap1(rule, result);
/*  78 */         if (!newResult.isPresent()) {
/*  79 */           return Optional.empty();
/*     */         }
/*  81 */         result = newResult.get();
/*     */       } 
/*  83 */       return Optional.of(result);
/*     */     }
/*     */     
/*     */     protected <A, B> Optional<RewriteResult<A, ?>> cap1(TypeRewriteRule rule, RewriteResult<A, B> f) {
/*  87 */       return rule.<B>rewrite(f.view.newType).map(s -> s.compose(f));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/*  92 */       if (obj == this) {
/*  93 */         return true;
/*     */       }
/*  95 */       if (!(obj instanceof Seq)) {
/*  96 */         return false;
/*     */       }
/*  98 */       Seq that = (Seq)obj;
/*  99 */       return Objects.equals(this.rules, that.rules);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 104 */       return this.hashCode;
/*     */     }
/*     */   }
/*     */   
/*     */   static TypeRewriteRule orElse(TypeRewriteRule first, TypeRewriteRule second) {
/* 109 */     return orElse(first, () -> second);
/*     */   }
/*     */   
/*     */   static TypeRewriteRule orElse(TypeRewriteRule first, Supplier<TypeRewriteRule> second) {
/* 113 */     return new OrElse(first, second);
/*     */   }
/*     */   
/*     */   public static final class OrElse implements TypeRewriteRule {
/*     */     protected final TypeRewriteRule first;
/*     */     protected final Supplier<TypeRewriteRule> second;
/*     */     private final int hashCode;
/*     */     
/*     */     public OrElse(TypeRewriteRule first, Supplier<TypeRewriteRule> second) {
/* 122 */       this.first = first;
/* 123 */       this.second = second;
/* 124 */       this.hashCode = Objects.hash(new Object[] { first, second });
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> type) {
/* 129 */       Optional<RewriteResult<A, ?>> view = this.first.rewrite(type);
/* 130 */       if (view.isPresent()) {
/* 131 */         return view;
/*     */       }
/* 133 */       return ((TypeRewriteRule)this.second.get()).rewrite(type);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 138 */       if (obj == this) {
/* 139 */         return true;
/*     */       }
/* 141 */       if (!(obj instanceof OrElse)) {
/* 142 */         return false;
/*     */       }
/* 144 */       OrElse that = (OrElse)obj;
/* 145 */       return (Objects.equals(this.first, that.first) && Objects.equals(this.second, that.second));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 150 */       return this.hashCode;
/*     */     }
/*     */   }
/*     */   
/*     */   static TypeRewriteRule all(TypeRewriteRule rule, boolean recurse, boolean checkIndex) {
/* 155 */     return new All(rule, recurse, checkIndex);
/*     */   }
/*     */   
/*     */   static TypeRewriteRule one(TypeRewriteRule rule) {
/* 159 */     return new One(rule);
/*     */   }
/*     */   
/*     */   static TypeRewriteRule once(TypeRewriteRule rule) {
/* 163 */     return orElse(rule, () -> one(once(rule)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeRewriteRule checkOnce(TypeRewriteRule rule, Consumer<Type<?>> onFail) {
/* 169 */     return rule;
/*     */   }
/*     */   
/*     */   static TypeRewriteRule everywhere(TypeRewriteRule rule, PointFreeRule optimizationRule, boolean recurse, boolean checkIndex) {
/* 173 */     return new Everywhere(rule, optimizationRule, recurse, checkIndex);
/*     */   }
/*     */   
/*     */   static <B> TypeRewriteRule ifSame(Type<B> targetType, RewriteResult<B, ?> value) {
/* 177 */     return new IfSame<>(targetType, value);
/*     */   }
/*     */   
/*     */   <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> paramType);
/*     */   
/*     */   public static class All implements TypeRewriteRule {
/*     */     private final TypeRewriteRule rule;
/*     */     private final boolean recurse;
/*     */     
/*     */     public All(TypeRewriteRule rule, boolean recurse, boolean checkIndex) {
/* 187 */       this.rule = rule;
/* 188 */       this.recurse = recurse;
/* 189 */       this.checkIndex = checkIndex;
/* 190 */       this.hashCode = Objects.hash(new Object[] { rule, Boolean.valueOf(recurse), Boolean.valueOf(checkIndex) });
/*     */     }
/*     */     private final boolean checkIndex; private final int hashCode;
/*     */     
/*     */     public <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> type) {
/* 195 */       return Optional.of(type.all(this.rule, this.recurse, this.checkIndex));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 200 */       if (obj == this) {
/* 201 */         return true;
/*     */       }
/* 203 */       if (!(obj instanceof All)) {
/* 204 */         return false;
/*     */       }
/* 206 */       All that = (All)obj;
/* 207 */       return (Objects.equals(this.rule, that.rule) && this.recurse == that.recurse && this.checkIndex == that.checkIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 212 */       return this.hashCode;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class One implements TypeRewriteRule {
/*     */     private final TypeRewriteRule rule;
/*     */     
/*     */     public One(TypeRewriteRule rule) {
/* 220 */       this.rule = rule;
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> type) {
/* 225 */       return type.one(this.rule);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 230 */       if (obj == this) {
/* 231 */         return true;
/*     */       }
/* 233 */       if (!(obj instanceof One)) {
/* 234 */         return false;
/*     */       }
/* 236 */       One that = (One)obj;
/* 237 */       return Objects.equals(this.rule, that.rule);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 242 */       return this.rule.hashCode();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CheckOnce implements TypeRewriteRule {
/*     */     private final TypeRewriteRule rule;
/*     */     private final Consumer<Type<?>> onFail;
/*     */     
/*     */     public CheckOnce(TypeRewriteRule rule, Consumer<Type<?>> onFail) {
/* 251 */       this.rule = rule;
/* 252 */       this.onFail = onFail;
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> type) {
/* 257 */       Optional<RewriteResult<A, ?>> result = this.rule.rewrite(type);
/* 258 */       if (!result.isPresent() || Objects.equals(((RewriteResult)result.get()).view.function(), Functions.id())) {
/* 259 */         this.onFail.accept(type);
/*     */       }
/* 261 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 266 */       if (this == o) {
/* 267 */         return true;
/*     */       }
/* 269 */       return (o instanceof CheckOnce && Objects.equals(this.rule, ((CheckOnce)o).rule));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 274 */       return Objects.hash(new Object[] { this.rule });
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Everywhere implements TypeRewriteRule {
/*     */     protected final TypeRewriteRule rule;
/*     */     protected final PointFreeRule optimizationRule;
/*     */     protected final boolean recurse;
/*     */     private final boolean checkIndex;
/*     */     private final int hashCode;
/*     */     
/*     */     public Everywhere(TypeRewriteRule rule, PointFreeRule optimizationRule, boolean recurse, boolean checkIndex) {
/* 286 */       this.rule = rule;
/* 287 */       this.optimizationRule = optimizationRule;
/* 288 */       this.recurse = recurse;
/* 289 */       this.checkIndex = checkIndex;
/* 290 */       this.hashCode = Objects.hash(new Object[] { rule, optimizationRule, Boolean.valueOf(recurse), Boolean.valueOf(checkIndex) });
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> type) {
/* 295 */       return type.everywhere(this.rule, this.optimizationRule, this.recurse, this.checkIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 300 */       if (obj == this) {
/* 301 */         return true;
/*     */       }
/* 303 */       if (!(obj instanceof Everywhere)) {
/* 304 */         return false;
/*     */       }
/* 306 */       Everywhere that = (Everywhere)obj;
/* 307 */       return (Objects.equals(this.rule, that.rule) && Objects.equals(this.optimizationRule, that.optimizationRule) && this.recurse == that.recurse && this.checkIndex == that.checkIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 312 */       return this.hashCode;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class IfSame<B> implements TypeRewriteRule {
/*     */     private final Type<B> targetType;
/*     */     private final RewriteResult<B, ?> value;
/*     */     private final int hashCode;
/*     */     
/*     */     public IfSame(Type<B> targetType, RewriteResult<B, ?> value) {
/* 322 */       this.targetType = targetType;
/* 323 */       this.value = value;
/* 324 */       this.hashCode = Objects.hash(new Object[] { targetType, value });
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> type) {
/* 329 */       return type.ifSame(this.targetType, this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 334 */       if (obj == this) {
/* 335 */         return true;
/*     */       }
/* 337 */       if (!(obj instanceof IfSame)) {
/* 338 */         return false;
/*     */       }
/* 340 */       IfSame<?> that = (IfSame)obj;
/* 341 */       return (Objects.equals(this.targetType, that.targetType) && Objects.equals(this.value, that.value));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 346 */       return this.hashCode;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\TypeRewriteRule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */