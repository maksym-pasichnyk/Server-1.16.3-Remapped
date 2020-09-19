/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible
/*     */ public abstract class Converter<A, B>
/*     */   implements Function<A, B>
/*     */ {
/*     */   private final boolean handleNullAutomatically;
/*     */   @LazyInit
/*     */   private transient Converter<B, A> reverse;
/*     */   
/*     */   protected Converter() {
/* 123 */     this(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Converter(boolean handleNullAutomatically) {
/* 130 */     this.handleNullAutomatically = handleNullAutomatically;
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
/*     */   protected abstract B doForward(A paramA);
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
/*     */   protected abstract A doBackward(B paramB);
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
/*     */   @Nullable
/*     */   @CanIgnoreReturnValue
/*     */   public final B convert(@Nullable A a) {
/* 168 */     return correctedDoForward(a);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   B correctedDoForward(@Nullable A a) {
/* 173 */     if (this.handleNullAutomatically)
/*     */     {
/* 175 */       return (a == null) ? null : Preconditions.<B>checkNotNull(doForward(a));
/*     */     }
/* 177 */     return doForward(a);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   A correctedDoBackward(@Nullable B b) {
/* 183 */     if (this.handleNullAutomatically)
/*     */     {
/* 185 */       return (b == null) ? null : Preconditions.<A>checkNotNull(doBackward(b));
/*     */     }
/* 187 */     return doBackward(b);
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
/*     */   @CanIgnoreReturnValue
/*     */   public Iterable<B> convertAll(final Iterable<? extends A> fromIterable) {
/* 201 */     Preconditions.checkNotNull(fromIterable, "fromIterable");
/* 202 */     return new Iterable<B>()
/*     */       {
/*     */         public Iterator<B> iterator() {
/* 205 */           return new Iterator<B>() {
/* 206 */               private final Iterator<? extends A> fromIterator = fromIterable.iterator();
/*     */ 
/*     */               
/*     */               public boolean hasNext() {
/* 210 */                 return this.fromIterator.hasNext();
/*     */               }
/*     */ 
/*     */               
/*     */               public B next() {
/* 215 */                 return (B)Converter.this.convert(this.fromIterator.next());
/*     */               }
/*     */ 
/*     */               
/*     */               public void remove() {
/* 220 */                 this.fromIterator.remove();
/*     */               }
/*     */             };
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
/*     */   @CanIgnoreReturnValue
/*     */   public Converter<B, A> reverse() {
/* 237 */     Converter<B, A> result = this.reverse;
/* 238 */     return (result == null) ? (this.reverse = new ReverseConverter<>(this)) : result;
/*     */   }
/*     */   
/*     */   private static final class ReverseConverter<A, B> extends Converter<B, A> implements Serializable {
/*     */     final Converter<A, B> original;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ReverseConverter(Converter<A, B> original) {
/* 246 */       this.original = original;
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
/*     */     protected A doForward(B b) {
/* 258 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     protected B doBackward(A a) {
/* 263 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     A correctedDoForward(@Nullable B b) {
/* 269 */       return this.original.correctedDoBackward(b);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     B correctedDoBackward(@Nullable A a) {
/* 275 */       return this.original.correctedDoForward(a);
/*     */     }
/*     */ 
/*     */     
/*     */     public Converter<A, B> reverse() {
/* 280 */       return this.original;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 285 */       if (object instanceof ReverseConverter) {
/* 286 */         ReverseConverter<?, ?> that = (ReverseConverter<?, ?>)object;
/* 287 */         return this.original.equals(that.original);
/*     */       } 
/* 289 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 294 */       return this.original.hashCode() ^ 0xFFFFFFFF;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 299 */       return this.original + ".reverse()";
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
/*     */   public final <C> Converter<A, C> andThen(Converter<B, C> secondConverter) {
/* 313 */     return doAndThen(secondConverter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   <C> Converter<A, C> doAndThen(Converter<B, C> secondConverter) {
/* 320 */     return new ConverterComposition<>(this, Preconditions.<Converter<B, C>>checkNotNull(secondConverter));
/*     */   }
/*     */   
/*     */   private static final class ConverterComposition<A, B, C> extends Converter<A, C> implements Serializable {
/*     */     final Converter<A, B> first;
/*     */     final Converter<B, C> second;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ConverterComposition(Converter<A, B> first, Converter<B, C> second) {
/* 329 */       this.first = first;
/* 330 */       this.second = second;
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
/*     */     protected C doForward(A a) {
/* 342 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     protected A doBackward(C c) {
/* 347 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     C correctedDoForward(@Nullable A a) {
/* 353 */       return this.second.correctedDoForward(this.first.correctedDoForward(a));
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     A correctedDoBackward(@Nullable C c) {
/* 359 */       return this.first.correctedDoBackward(this.second.correctedDoBackward(c));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 364 */       if (object instanceof ConverterComposition) {
/* 365 */         ConverterComposition<?, ?, ?> that = (ConverterComposition<?, ?, ?>)object;
/* 366 */         return (this.first.equals(that.first) && this.second.equals(that.second));
/*     */       } 
/* 368 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 373 */       return 31 * this.first.hashCode() + this.second.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 378 */       return this.first + ".andThen(" + this.second + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @Nullable
/*     */   @CanIgnoreReturnValue
/*     */   public final B apply(@Nullable A a) {
/* 392 */     return convert(a);
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
/*     */   public boolean equals(@Nullable Object object) {
/* 408 */     return super.equals(object);
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
/*     */   public static <A, B> Converter<A, B> from(Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction) {
/* 430 */     return new FunctionBasedConverter<>(forwardFunction, backwardFunction);
/*     */   }
/*     */   
/*     */   private static final class FunctionBasedConverter<A, B>
/*     */     extends Converter<A, B>
/*     */     implements Serializable
/*     */   {
/*     */     private final Function<? super A, ? extends B> forwardFunction;
/*     */     private final Function<? super B, ? extends A> backwardFunction;
/*     */     
/*     */     private FunctionBasedConverter(Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction) {
/* 441 */       this.forwardFunction = Preconditions.<Function<? super A, ? extends B>>checkNotNull(forwardFunction);
/* 442 */       this.backwardFunction = Preconditions.<Function<? super B, ? extends A>>checkNotNull(backwardFunction);
/*     */     }
/*     */ 
/*     */     
/*     */     protected B doForward(A a) {
/* 447 */       return this.forwardFunction.apply(a);
/*     */     }
/*     */ 
/*     */     
/*     */     protected A doBackward(B b) {
/* 452 */       return this.backwardFunction.apply(b);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 457 */       if (object instanceof FunctionBasedConverter) {
/* 458 */         FunctionBasedConverter<?, ?> that = (FunctionBasedConverter<?, ?>)object;
/* 459 */         return (this.forwardFunction.equals(that.forwardFunction) && this.backwardFunction
/* 460 */           .equals(that.backwardFunction));
/*     */       } 
/* 462 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 467 */       return this.forwardFunction.hashCode() * 31 + this.backwardFunction.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 472 */       return "Converter.from(" + this.forwardFunction + ", " + this.backwardFunction + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Converter<T, T> identity() {
/* 481 */     return IdentityConverter.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class IdentityConverter<T>
/*     */     extends Converter<T, T>
/*     */     implements Serializable
/*     */   {
/* 489 */     static final IdentityConverter INSTANCE = new IdentityConverter();
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     protected T doForward(T t) {
/* 493 */       return t;
/*     */     }
/*     */ 
/*     */     
/*     */     protected T doBackward(T t) {
/* 498 */       return t;
/*     */     }
/*     */ 
/*     */     
/*     */     public IdentityConverter<T> reverse() {
/* 503 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     <S> Converter<T, S> doAndThen(Converter<T, S> otherConverter) {
/* 508 */       return Preconditions.<Converter<T, S>>checkNotNull(otherConverter, "otherConverter");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 518 */       return "Converter.identity()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 522 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\base\Converter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */