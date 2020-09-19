/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Booleans;
/*     */ import java.io.Serializable;
/*     */ import java.util.NoSuchElementException;
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
/*     */ @GwtCompatible
/*     */ abstract class Cut<C extends Comparable>
/*     */   implements Comparable<Cut<C>>, Serializable
/*     */ {
/*     */   final C endpoint;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   Cut(@Nullable C endpoint) {
/*  39 */     this.endpoint = endpoint;
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
/*     */   Cut<C> canonical(DiscreteDomain<C> domain) {
/*  65 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Cut<C> that) {
/*  71 */     if (that == belowAll()) {
/*  72 */       return 1;
/*     */     }
/*  74 */     if (that == aboveAll()) {
/*  75 */       return -1;
/*     */     }
/*  77 */     int result = Range.compareOrThrow((Comparable)this.endpoint, (Comparable)that.endpoint);
/*  78 */     if (result != 0) {
/*  79 */       return result;
/*     */     }
/*     */     
/*  82 */     return Booleans.compare(this instanceof AboveValue, that instanceof AboveValue);
/*     */   }
/*     */   
/*     */   C endpoint() {
/*  86 */     return this.endpoint;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  92 */     if (obj instanceof Cut) {
/*     */       
/*  94 */       Cut<C> that = (Cut<C>)obj;
/*     */       try {
/*  96 */         int compareResult = compareTo(that);
/*  97 */         return (compareResult == 0);
/*  98 */       } catch (ClassCastException classCastException) {}
/*     */     } 
/*     */     
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <C extends Comparable> Cut<C> belowAll() {
/* 110 */     return BelowAll.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class BelowAll
/*     */     extends Cut<Comparable<?>>
/*     */   {
/* 116 */     private static final BelowAll INSTANCE = new BelowAll(); private static final long serialVersionUID = 0L;
/*     */     
/*     */     private BelowAll() {
/* 119 */       super(null);
/*     */     }
/*     */ 
/*     */     
/*     */     Comparable<?> endpoint() {
/* 124 */       throw new IllegalStateException("range unbounded on this side");
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isLessThan(Comparable<?> value) {
/* 129 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsLowerBound() {
/* 134 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsUpperBound() {
/* 139 */       throw new AssertionError("this statement should be unreachable");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Cut<Comparable<?>> withLowerBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain) {
/* 145 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Cut<Comparable<?>> withUpperBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain) {
/* 151 */       throw new AssertionError("this statement should be unreachable");
/*     */     }
/*     */ 
/*     */     
/*     */     void describeAsLowerBound(StringBuilder sb) {
/* 156 */       sb.append("(-∞");
/*     */     }
/*     */ 
/*     */     
/*     */     void describeAsUpperBound(StringBuilder sb) {
/* 161 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     Comparable<?> leastValueAbove(DiscreteDomain<Comparable<?>> domain) {
/* 166 */       return domain.minValue();
/*     */     }
/*     */ 
/*     */     
/*     */     Comparable<?> greatestValueBelow(DiscreteDomain<Comparable<?>> domain) {
/* 171 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     Cut<Comparable<?>> canonical(DiscreteDomain<Comparable<?>> domain) {
/*     */       try {
/* 177 */         return Cut.belowValue(domain.minValue());
/* 178 */       } catch (NoSuchElementException e) {
/* 179 */         return this;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(Cut<Comparable<?>> o) {
/* 185 */       return (o == this) ? 0 : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 190 */       return "-∞";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 194 */       return INSTANCE;
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
/*     */   static <C extends Comparable> Cut<C> aboveAll() {
/* 206 */     return AboveAll.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class AboveAll extends Cut<Comparable<?>> {
/* 210 */     private static final AboveAll INSTANCE = new AboveAll(); private static final long serialVersionUID = 0L;
/*     */     
/*     */     private AboveAll() {
/* 213 */       super(null);
/*     */     }
/*     */ 
/*     */     
/*     */     Comparable<?> endpoint() {
/* 218 */       throw new IllegalStateException("range unbounded on this side");
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isLessThan(Comparable<?> value) {
/* 223 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsLowerBound() {
/* 228 */       throw new AssertionError("this statement should be unreachable");
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsUpperBound() {
/* 233 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Cut<Comparable<?>> withLowerBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain) {
/* 239 */       throw new AssertionError("this statement should be unreachable");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Cut<Comparable<?>> withUpperBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain) {
/* 245 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     
/*     */     void describeAsLowerBound(StringBuilder sb) {
/* 250 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     void describeAsUpperBound(StringBuilder sb) {
/* 255 */       sb.append("+∞)");
/*     */     }
/*     */ 
/*     */     
/*     */     Comparable<?> leastValueAbove(DiscreteDomain<Comparable<?>> domain) {
/* 260 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     Comparable<?> greatestValueBelow(DiscreteDomain<Comparable<?>> domain) {
/* 265 */       return domain.maxValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(Cut<Comparable<?>> o) {
/* 270 */       return (o == this) ? 0 : 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 275 */       return "+∞";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 279 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static <C extends Comparable> Cut<C> belowValue(C endpoint) {
/* 286 */     return new BelowValue<>(endpoint);
/*     */   }
/*     */   private static final class BelowValue<C extends Comparable> extends Cut<C> { private static final long serialVersionUID = 0L;
/*     */     
/*     */     BelowValue(C endpoint) {
/* 291 */       super((C)Preconditions.checkNotNull(endpoint));
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isLessThan(C value) {
/* 296 */       return (Range.compareOrThrow((Comparable)this.endpoint, (Comparable)value) <= 0);
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsLowerBound() {
/* 301 */       return BoundType.CLOSED;
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsUpperBound() {
/* 306 */       return BoundType.OPEN;
/*     */     }
/*     */     
/*     */     Cut<C> withLowerBoundType(BoundType boundType, DiscreteDomain<C> domain) {
/*     */       C previous;
/* 311 */       switch (boundType) {
/*     */         case CLOSED:
/* 313 */           return this;
/*     */         case OPEN:
/* 315 */           previous = domain.previous(this.endpoint);
/* 316 */           return (previous == null) ? Cut.<C>belowAll() : new Cut.AboveValue<>(previous);
/*     */       } 
/* 318 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     Cut<C> withUpperBoundType(BoundType boundType, DiscreteDomain<C> domain) {
/*     */       C previous;
/* 324 */       switch (boundType) {
/*     */         case CLOSED:
/* 326 */           previous = domain.previous(this.endpoint);
/* 327 */           return (previous == null) ? Cut.<C>aboveAll() : new Cut.AboveValue<>(previous);
/*     */         case OPEN:
/* 329 */           return this;
/*     */       } 
/* 331 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void describeAsLowerBound(StringBuilder sb) {
/* 337 */       sb.append('[').append(this.endpoint);
/*     */     }
/*     */ 
/*     */     
/*     */     void describeAsUpperBound(StringBuilder sb) {
/* 342 */       sb.append(this.endpoint).append(')');
/*     */     }
/*     */ 
/*     */     
/*     */     C leastValueAbove(DiscreteDomain<C> domain) {
/* 347 */       return this.endpoint;
/*     */     }
/*     */ 
/*     */     
/*     */     C greatestValueBelow(DiscreteDomain<C> domain) {
/* 352 */       return domain.previous(this.endpoint);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 357 */       return this.endpoint.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 362 */       return "\\" + this.endpoint + "/";
/*     */     } }
/*     */   abstract boolean isLessThan(C paramC);
/*     */   
/*     */   abstract BoundType typeAsLowerBound();
/*     */   
/*     */   static <C extends Comparable> Cut<C> aboveValue(C endpoint) {
/* 369 */     return new AboveValue<>(endpoint);
/*     */   } abstract BoundType typeAsUpperBound(); abstract Cut<C> withLowerBoundType(BoundType paramBoundType, DiscreteDomain<C> paramDiscreteDomain); abstract Cut<C> withUpperBoundType(BoundType paramBoundType, DiscreteDomain<C> paramDiscreteDomain); abstract void describeAsLowerBound(StringBuilder paramStringBuilder); abstract void describeAsUpperBound(StringBuilder paramStringBuilder);
/*     */   abstract C leastValueAbove(DiscreteDomain<C> paramDiscreteDomain);
/*     */   abstract C greatestValueBelow(DiscreteDomain<C> paramDiscreteDomain);
/*     */   private static final class AboveValue<C extends Comparable> extends Cut<C> { AboveValue(C endpoint) {
/* 374 */       super((C)Preconditions.checkNotNull(endpoint));
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     boolean isLessThan(C value) {
/* 379 */       return (Range.compareOrThrow((Comparable)this.endpoint, (Comparable)value) < 0);
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsLowerBound() {
/* 384 */       return BoundType.OPEN;
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsUpperBound() {
/* 389 */       return BoundType.CLOSED;
/*     */     }
/*     */     
/*     */     Cut<C> withLowerBoundType(BoundType boundType, DiscreteDomain<C> domain) {
/*     */       C next;
/* 394 */       switch (boundType) {
/*     */         case OPEN:
/* 396 */           return this;
/*     */         case CLOSED:
/* 398 */           next = domain.next(this.endpoint);
/* 399 */           return (next == null) ? Cut.<C>belowAll() : belowValue(next);
/*     */       } 
/* 401 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     Cut<C> withUpperBoundType(BoundType boundType, DiscreteDomain<C> domain) {
/*     */       C next;
/* 407 */       switch (boundType) {
/*     */         case OPEN:
/* 409 */           next = domain.next(this.endpoint);
/* 410 */           return (next == null) ? Cut.<C>aboveAll() : belowValue(next);
/*     */         case CLOSED:
/* 412 */           return this;
/*     */       } 
/* 414 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void describeAsLowerBound(StringBuilder sb) {
/* 420 */       sb.append('(').append(this.endpoint);
/*     */     }
/*     */ 
/*     */     
/*     */     void describeAsUpperBound(StringBuilder sb) {
/* 425 */       sb.append(this.endpoint).append(']');
/*     */     }
/*     */ 
/*     */     
/*     */     C leastValueAbove(DiscreteDomain<C> domain) {
/* 430 */       return domain.next(this.endpoint);
/*     */     }
/*     */ 
/*     */     
/*     */     C greatestValueBelow(DiscreteDomain<C> domain) {
/* 435 */       return this.endpoint;
/*     */     }
/*     */ 
/*     */     
/*     */     Cut<C> canonical(DiscreteDomain<C> domain) {
/* 440 */       C next = leastValueAbove(domain);
/* 441 */       return (next != null) ? belowValue(next) : Cut.<C>aboveAll();
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 446 */       return this.endpoint.hashCode() ^ 0xFFFFFFFF;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 451 */       return "/" + this.endpoint + "\\";
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\Cut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */