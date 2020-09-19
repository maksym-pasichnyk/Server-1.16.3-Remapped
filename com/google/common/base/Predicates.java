/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.Nullable;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Predicates
/*     */ {
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Predicate<T> alwaysTrue() {
/*  54 */     return ObjectPredicate.ALWAYS_TRUE.withNarrowedType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Predicate<T> alwaysFalse() {
/*  62 */     return ObjectPredicate.ALWAYS_FALSE.withNarrowedType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Predicate<T> isNull() {
/*  71 */     return ObjectPredicate.IS_NULL.withNarrowedType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Predicate<T> notNull() {
/*  80 */     return ObjectPredicate.NOT_NULL.withNarrowedType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> not(Predicate<T> predicate) {
/*  88 */     return new NotPredicate<>(predicate);
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
/*     */   public static <T> Predicate<T> and(Iterable<? extends Predicate<? super T>> components) {
/* 100 */     return new AndPredicate<>(defensiveCopy(components));
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
/*     */   public static <T> Predicate<T> and(Predicate<? super T>... components) {
/* 112 */     return new AndPredicate<>(defensiveCopy(components));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> and(Predicate<? super T> first, Predicate<? super T> second) {
/* 121 */     return new AndPredicate<>(asList(Preconditions.<Predicate>checkNotNull(first), Preconditions.<Predicate>checkNotNull(second)));
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
/*     */   public static <T> Predicate<T> or(Iterable<? extends Predicate<? super T>> components) {
/* 133 */     return new OrPredicate<>(defensiveCopy(components));
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
/*     */   public static <T> Predicate<T> or(Predicate<? super T>... components) {
/* 145 */     return new OrPredicate<>(defensiveCopy(components));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> or(Predicate<? super T> first, Predicate<? super T> second) {
/* 154 */     return new OrPredicate<>(asList(Preconditions.<Predicate>checkNotNull(first), Preconditions.<Predicate>checkNotNull(second)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> equalTo(@Nullable T target) {
/* 162 */     return (target == null) ? isNull() : new IsEqualToPredicate<>(target);
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
/*     */   @GwtIncompatible
/*     */   public static Predicate<Object> instanceOf(Class<?> clazz) {
/* 180 */     return new InstanceOfPredicate(clazz);
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
/*     */   @Deprecated
/*     */   @GwtIncompatible
/*     */   @Beta
/*     */   public static Predicate<Class<?>> assignableFrom(Class<?> clazz) {
/* 204 */     return subtypeOf(clazz);
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
/*     */   @GwtIncompatible
/*     */   @Beta
/*     */   public static Predicate<Class<?>> subtypeOf(Class<?> clazz) {
/* 222 */     return new SubtypeOfPredicate(clazz);
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
/*     */   public static <T> Predicate<T> in(Collection<? extends T> target) {
/* 237 */     return new InPredicate<>(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A, B> Predicate<A> compose(Predicate<B> predicate, Function<A, ? extends B> function) {
/* 248 */     return new CompositionPredicate<>(predicate, function);
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
/*     */   @GwtIncompatible
/*     */   public static Predicate<CharSequence> containsPattern(String pattern) {
/* 261 */     return new ContainsPatternFromStringPredicate(pattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.util.regex.Pattern")
/*     */   public static Predicate<CharSequence> contains(Pattern pattern) {
/* 273 */     return new ContainsPatternPredicate(new JdkPattern(pattern));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   enum ObjectPredicate
/*     */     implements Predicate<Object>
/*     */   {
/* 281 */     ALWAYS_TRUE
/*     */     {
/*     */       public boolean apply(@Nullable Object o) {
/* 284 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 289 */         return "Predicates.alwaysTrue()";
/*     */       }
/*     */     },
/*     */     
/* 293 */     ALWAYS_FALSE
/*     */     {
/*     */       public boolean apply(@Nullable Object o) {
/* 296 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 301 */         return "Predicates.alwaysFalse()";
/*     */       }
/*     */     },
/*     */     
/* 305 */     IS_NULL
/*     */     {
/*     */       public boolean apply(@Nullable Object o) {
/* 308 */         return (o == null);
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 313 */         return "Predicates.isNull()";
/*     */       }
/*     */     },
/*     */     
/* 317 */     NOT_NULL
/*     */     {
/*     */       public boolean apply(@Nullable Object o) {
/* 320 */         return (o != null);
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 325 */         return "Predicates.notNull()";
/*     */       }
/*     */     };
/*     */ 
/*     */     
/*     */     <T> Predicate<T> withNarrowedType() {
/* 331 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class NotPredicate<T> implements Predicate<T>, Serializable {
/*     */     final Predicate<T> predicate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     NotPredicate(Predicate<T> predicate) {
/* 340 */       this.predicate = Preconditions.<Predicate<T>>checkNotNull(predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(@Nullable T t) {
/* 345 */       return !this.predicate.apply(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 350 */       return this.predicate.hashCode() ^ 0xFFFFFFFF;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 355 */       if (obj instanceof NotPredicate) {
/* 356 */         NotPredicate<?> that = (NotPredicate)obj;
/* 357 */         return this.predicate.equals(that.predicate);
/*     */       } 
/* 359 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 364 */       return "Predicates.not(" + this.predicate + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 370 */   private static final Joiner COMMA_JOINER = Joiner.on(',');
/*     */   
/*     */   private static class AndPredicate<T> implements Predicate<T>, Serializable {
/*     */     private final List<? extends Predicate<? super T>> components;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private AndPredicate(List<? extends Predicate<? super T>> components) {
/* 377 */       this.components = components;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean apply(@Nullable T t) {
/* 383 */       for (int i = 0; i < this.components.size(); i++) {
/* 384 */         if (!((Predicate<T>)this.components.get(i)).apply(t)) {
/* 385 */           return false;
/*     */         }
/*     */       } 
/* 388 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 394 */       return this.components.hashCode() + 306654252;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 399 */       if (obj instanceof AndPredicate) {
/* 400 */         AndPredicate<?> that = (AndPredicate)obj;
/* 401 */         return this.components.equals(that.components);
/*     */       } 
/* 403 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 408 */       return "Predicates.and(" + Predicates.COMMA_JOINER.join(this.components) + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class OrPredicate<T>
/*     */     implements Predicate<T>, Serializable
/*     */   {
/*     */     private final List<? extends Predicate<? super T>> components;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private OrPredicate(List<? extends Predicate<? super T>> components) {
/* 419 */       this.components = components;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean apply(@Nullable T t) {
/* 425 */       for (int i = 0; i < this.components.size(); i++) {
/* 426 */         if (((Predicate<T>)this.components.get(i)).apply(t)) {
/* 427 */           return true;
/*     */         }
/*     */       } 
/* 430 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 436 */       return this.components.hashCode() + 87855567;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 441 */       if (obj instanceof OrPredicate) {
/* 442 */         OrPredicate<?> that = (OrPredicate)obj;
/* 443 */         return this.components.equals(that.components);
/*     */       } 
/* 445 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 450 */       return "Predicates.or(" + Predicates.COMMA_JOINER.join(this.components) + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class IsEqualToPredicate<T>
/*     */     implements Predicate<T>, Serializable
/*     */   {
/*     */     private final T target;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private IsEqualToPredicate(T target) {
/* 461 */       this.target = target;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(T t) {
/* 466 */       return this.target.equals(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 471 */       return this.target.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 476 */       if (obj instanceof IsEqualToPredicate) {
/* 477 */         IsEqualToPredicate<?> that = (IsEqualToPredicate)obj;
/* 478 */         return this.target.equals(that.target);
/*     */       } 
/* 480 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 485 */       return "Predicates.equalTo(" + this.target + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static class InstanceOfPredicate
/*     */     implements Predicate<Object>, Serializable
/*     */   {
/*     */     private final Class<?> clazz;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private InstanceOfPredicate(Class<?> clazz) {
/* 497 */       this.clazz = Preconditions.<Class<?>>checkNotNull(clazz);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(@Nullable Object o) {
/* 502 */       return this.clazz.isInstance(o);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 507 */       return this.clazz.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 512 */       if (obj instanceof InstanceOfPredicate) {
/* 513 */         InstanceOfPredicate that = (InstanceOfPredicate)obj;
/* 514 */         return (this.clazz == that.clazz);
/*     */       } 
/* 516 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 521 */       return "Predicates.instanceOf(" + this.clazz.getName() + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static class SubtypeOfPredicate
/*     */     implements Predicate<Class<?>>, Serializable
/*     */   {
/*     */     private final Class<?> clazz;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private SubtypeOfPredicate(Class<?> clazz) {
/* 533 */       this.clazz = Preconditions.<Class<?>>checkNotNull(clazz);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(Class<?> input) {
/* 538 */       return this.clazz.isAssignableFrom(input);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 543 */       return this.clazz.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 548 */       if (obj instanceof SubtypeOfPredicate) {
/* 549 */         SubtypeOfPredicate that = (SubtypeOfPredicate)obj;
/* 550 */         return (this.clazz == that.clazz);
/*     */       } 
/* 552 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 557 */       return "Predicates.subtypeOf(" + this.clazz.getName() + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class InPredicate<T>
/*     */     implements Predicate<T>, Serializable
/*     */   {
/*     */     private final Collection<?> target;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private InPredicate(Collection<?> target) {
/* 568 */       this.target = Preconditions.<Collection>checkNotNull(target);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(@Nullable T t) {
/*     */       try {
/* 574 */         return this.target.contains(t);
/* 575 */       } catch (NullPointerException e) {
/* 576 */         return false;
/* 577 */       } catch (ClassCastException e) {
/* 578 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 584 */       if (obj instanceof InPredicate) {
/* 585 */         InPredicate<?> that = (InPredicate)obj;
/* 586 */         return this.target.equals(that.target);
/*     */       } 
/* 588 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 593 */       return this.target.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 598 */       return "Predicates.in(" + this.target + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CompositionPredicate<A, B>
/*     */     implements Predicate<A>, Serializable
/*     */   {
/*     */     final Predicate<B> p;
/*     */     final Function<A, ? extends B> f;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private CompositionPredicate(Predicate<B> p, Function<A, ? extends B> f) {
/* 610 */       this.p = Preconditions.<Predicate<B>>checkNotNull(p);
/* 611 */       this.f = Preconditions.<Function<A, ? extends B>>checkNotNull(f);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(@Nullable A a) {
/* 616 */       return this.p.apply(this.f.apply(a));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 621 */       if (obj instanceof CompositionPredicate) {
/* 622 */         CompositionPredicate<?, ?> that = (CompositionPredicate<?, ?>)obj;
/* 623 */         return (this.f.equals(that.f) && this.p.equals(that.p));
/*     */       } 
/* 625 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 630 */       return this.f.hashCode() ^ this.p.hashCode();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 636 */       return this.p + "(" + this.f + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static class ContainsPatternPredicate
/*     */     implements Predicate<CharSequence>, Serializable
/*     */   {
/*     */     final CommonPattern pattern;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ContainsPatternPredicate(CommonPattern pattern) {
/* 648 */       this.pattern = Preconditions.<CommonPattern>checkNotNull(pattern);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(CharSequence t) {
/* 653 */       return this.pattern.matcher(t).find();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 661 */       return Objects.hashCode(new Object[] { this.pattern.pattern(), Integer.valueOf(this.pattern.flags()) });
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 666 */       if (obj instanceof ContainsPatternPredicate) {
/* 667 */         ContainsPatternPredicate that = (ContainsPatternPredicate)obj;
/*     */ 
/*     */ 
/*     */         
/* 671 */         return (Objects.equal(this.pattern.pattern(), that.pattern.pattern()) && this.pattern
/* 672 */           .flags() == that.pattern.flags());
/*     */       } 
/* 674 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 683 */       String patternString = MoreObjects.toStringHelper(this.pattern).add("pattern", this.pattern.pattern()).add("pattern.flags", this.pattern.flags()).toString();
/* 684 */       return "Predicates.contains(" + patternString + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static class ContainsPatternFromStringPredicate
/*     */     extends ContainsPatternPredicate
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ContainsPatternFromStringPredicate(String string) {
/* 695 */       super(Platform.compilePattern(string));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 700 */       return "Predicates.containsPattern(" + this.pattern.pattern() + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> List<Predicate<? super T>> asList(Predicate<? super T> first, Predicate<? super T> second) {
/* 709 */     return Arrays.asList((Predicate<? super T>[])new Predicate[] { first, second });
/*     */   }
/*     */   
/*     */   private static <T> List<T> defensiveCopy(T... array) {
/* 713 */     return defensiveCopy(Arrays.asList(array));
/*     */   }
/*     */   
/*     */   static <T> List<T> defensiveCopy(Iterable<T> iterable) {
/* 717 */     ArrayList<T> list = new ArrayList<>();
/* 718 */     for (T element : iterable) {
/* 719 */       list.add(Preconditions.checkNotNull(element));
/*     */     }
/* 721 */     return list;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\base\Predicates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */