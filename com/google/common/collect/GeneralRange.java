/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ final class GeneralRange<T>
/*     */   implements Serializable
/*     */ {
/*     */   private final Comparator<? super T> comparator;
/*     */   private final boolean hasLowerBound;
/*     */   @Nullable
/*     */   private final T lowerEndpoint;
/*     */   private final BoundType lowerBoundType;
/*     */   private final boolean hasUpperBound;
/*     */   @Nullable
/*     */   private final T upperEndpoint;
/*     */   private final BoundType upperBoundType;
/*     */   private transient GeneralRange<T> reverse;
/*     */   
/*     */   static <T extends Comparable> GeneralRange<T> from(Range<T> range) {
/*  43 */     C c1 = range.hasLowerBound() ? (C)range.lowerEndpoint() : null;
/*  44 */     BoundType lowerBoundType = range.hasLowerBound() ? range.lowerBoundType() : BoundType.OPEN;
/*     */     
/*  46 */     C c2 = range.hasUpperBound() ? (C)range.upperEndpoint() : null;
/*  47 */     BoundType upperBoundType = range.hasUpperBound() ? range.upperBoundType() : BoundType.OPEN;
/*  48 */     return new GeneralRange<>(
/*  49 */         Ordering.natural(), range
/*  50 */         .hasLowerBound(), (T)c1, lowerBoundType, range
/*     */ 
/*     */         
/*  53 */         .hasUpperBound(), (T)c2, upperBoundType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> GeneralRange<T> all(Comparator<? super T> comparator) {
/*  62 */     return new GeneralRange<>(comparator, false, null, BoundType.OPEN, false, null, BoundType.OPEN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> GeneralRange<T> downTo(Comparator<? super T> comparator, @Nullable T endpoint, BoundType boundType) {
/*  71 */     return new GeneralRange<>(comparator, true, endpoint, boundType, false, null, BoundType.OPEN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> GeneralRange<T> upTo(Comparator<? super T> comparator, @Nullable T endpoint, BoundType boundType) {
/*  80 */     return new GeneralRange<>(comparator, false, null, BoundType.OPEN, true, endpoint, boundType);
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
/*     */   static <T> GeneralRange<T> range(Comparator<? super T> comparator, @Nullable T lower, BoundType lowerType, @Nullable T upper, BoundType upperType) {
/*  93 */     return new GeneralRange<>(comparator, true, lower, lowerType, true, upper, upperType);
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
/*     */   private GeneralRange(Comparator<? super T> comparator, boolean hasLowerBound, @Nullable T lowerEndpoint, BoundType lowerBoundType, boolean hasUpperBound, @Nullable T upperEndpoint, BoundType upperBoundType) {
/* 112 */     this.comparator = (Comparator<? super T>)Preconditions.checkNotNull(comparator);
/* 113 */     this.hasLowerBound = hasLowerBound;
/* 114 */     this.hasUpperBound = hasUpperBound;
/* 115 */     this.lowerEndpoint = lowerEndpoint;
/* 116 */     this.lowerBoundType = (BoundType)Preconditions.checkNotNull(lowerBoundType);
/* 117 */     this.upperEndpoint = upperEndpoint;
/* 118 */     this.upperBoundType = (BoundType)Preconditions.checkNotNull(upperBoundType);
/*     */     
/* 120 */     if (hasLowerBound) {
/* 121 */       comparator.compare(lowerEndpoint, lowerEndpoint);
/*     */     }
/* 123 */     if (hasUpperBound) {
/* 124 */       comparator.compare(upperEndpoint, upperEndpoint);
/*     */     }
/* 126 */     if (hasLowerBound && hasUpperBound) {
/* 127 */       int cmp = comparator.compare(lowerEndpoint, upperEndpoint);
/*     */       
/* 129 */       Preconditions.checkArgument((cmp <= 0), "lowerEndpoint (%s) > upperEndpoint (%s)", lowerEndpoint, upperEndpoint);
/*     */       
/* 131 */       if (cmp == 0) {
/* 132 */         Preconditions.checkArgument(((lowerBoundType != BoundType.OPEN)) | ((upperBoundType != BoundType.OPEN)));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   Comparator<? super T> comparator() {
/* 138 */     return this.comparator;
/*     */   }
/*     */   
/*     */   boolean hasLowerBound() {
/* 142 */     return this.hasLowerBound;
/*     */   }
/*     */   
/*     */   boolean hasUpperBound() {
/* 146 */     return this.hasUpperBound;
/*     */   }
/*     */   
/*     */   boolean isEmpty() {
/* 150 */     return ((hasUpperBound() && tooLow(getUpperEndpoint())) || (
/* 151 */       hasLowerBound() && tooHigh(getLowerEndpoint())));
/*     */   }
/*     */   
/*     */   boolean tooLow(@Nullable T t) {
/* 155 */     if (!hasLowerBound()) {
/* 156 */       return false;
/*     */     }
/* 158 */     T lbound = getLowerEndpoint();
/* 159 */     int cmp = this.comparator.compare(t, lbound);
/* 160 */     return ((cmp < 0) ? 1 : 0) | ((cmp == 0)) & ((getLowerBoundType() == BoundType.OPEN));
/*     */   }
/*     */   
/*     */   boolean tooHigh(@Nullable T t) {
/* 164 */     if (!hasUpperBound()) {
/* 165 */       return false;
/*     */     }
/* 167 */     T ubound = getUpperEndpoint();
/* 168 */     int cmp = this.comparator.compare(t, ubound);
/* 169 */     return ((cmp > 0) ? 1 : 0) | ((cmp == 0)) & ((getUpperBoundType() == BoundType.OPEN));
/*     */   }
/*     */   
/*     */   boolean contains(@Nullable T t) {
/* 173 */     return (!tooLow(t) && !tooHigh(t));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   GeneralRange<T> intersect(GeneralRange<T> other) {
/* 180 */     Preconditions.checkNotNull(other);
/* 181 */     Preconditions.checkArgument(this.comparator.equals(other.comparator));
/*     */     
/* 183 */     boolean hasLowBound = this.hasLowerBound;
/* 184 */     T lowEnd = getLowerEndpoint();
/* 185 */     BoundType lowType = getLowerBoundType();
/* 186 */     if (!hasLowerBound()) {
/* 187 */       hasLowBound = other.hasLowerBound;
/* 188 */       lowEnd = other.getLowerEndpoint();
/* 189 */       lowType = other.getLowerBoundType();
/* 190 */     } else if (other.hasLowerBound()) {
/* 191 */       int cmp = this.comparator.compare(getLowerEndpoint(), other.getLowerEndpoint());
/* 192 */       if (cmp < 0 || (cmp == 0 && other.getLowerBoundType() == BoundType.OPEN)) {
/* 193 */         lowEnd = other.getLowerEndpoint();
/* 194 */         lowType = other.getLowerBoundType();
/*     */       } 
/*     */     } 
/*     */     
/* 198 */     boolean hasUpBound = this.hasUpperBound;
/* 199 */     T upEnd = getUpperEndpoint();
/* 200 */     BoundType upType = getUpperBoundType();
/* 201 */     if (!hasUpperBound()) {
/* 202 */       hasUpBound = other.hasUpperBound;
/* 203 */       upEnd = other.getUpperEndpoint();
/* 204 */       upType = other.getUpperBoundType();
/* 205 */     } else if (other.hasUpperBound()) {
/* 206 */       int cmp = this.comparator.compare(getUpperEndpoint(), other.getUpperEndpoint());
/* 207 */       if (cmp > 0 || (cmp == 0 && other.getUpperBoundType() == BoundType.OPEN)) {
/* 208 */         upEnd = other.getUpperEndpoint();
/* 209 */         upType = other.getUpperBoundType();
/*     */       } 
/*     */     } 
/*     */     
/* 213 */     if (hasLowBound && hasUpBound) {
/* 214 */       int cmp = this.comparator.compare(lowEnd, upEnd);
/* 215 */       if (cmp > 0 || (cmp == 0 && lowType == BoundType.OPEN && upType == BoundType.OPEN)) {
/*     */         
/* 217 */         lowEnd = upEnd;
/* 218 */         lowType = BoundType.OPEN;
/* 219 */         upType = BoundType.CLOSED;
/*     */       } 
/*     */     } 
/*     */     
/* 223 */     return new GeneralRange(this.comparator, hasLowBound, lowEnd, lowType, hasUpBound, upEnd, upType);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 228 */     if (obj instanceof GeneralRange) {
/* 229 */       GeneralRange<?> r = (GeneralRange)obj;
/* 230 */       return (this.comparator.equals(r.comparator) && this.hasLowerBound == r.hasLowerBound && this.hasUpperBound == r.hasUpperBound && 
/*     */ 
/*     */         
/* 233 */         getLowerBoundType().equals(r.getLowerBoundType()) && 
/* 234 */         getUpperBoundType().equals(r.getUpperBoundType()) && 
/* 235 */         Objects.equal(getLowerEndpoint(), r.getLowerEndpoint()) && 
/* 236 */         Objects.equal(getUpperEndpoint(), r.getUpperEndpoint()));
/*     */     } 
/* 238 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 243 */     return Objects.hashCode(new Object[] { this.comparator, 
/*     */           
/* 245 */           getLowerEndpoint(), 
/* 246 */           getLowerBoundType(), 
/* 247 */           getUpperEndpoint(), 
/* 248 */           getUpperBoundType() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   GeneralRange<T> reverse() {
/* 257 */     GeneralRange<T> result = this.reverse;
/* 258 */     if (result == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 267 */       result = new GeneralRange(Ordering.<T>from(this.comparator).reverse(), this.hasUpperBound, getUpperEndpoint(), getUpperBoundType(), this.hasLowerBound, getLowerEndpoint(), getLowerBoundType());
/* 268 */       result.reverse = this;
/* 269 */       return this.reverse = result;
/*     */     } 
/* 271 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 276 */     return this.comparator + ":" + ((this.lowerBoundType == BoundType.CLOSED) ? 91 : 40) + (this.hasLowerBound ? (String)this.lowerEndpoint : "-∞") + ',' + (this.hasUpperBound ? (String)this.upperEndpoint : "∞") + ((this.upperBoundType == BoundType.CLOSED) ? 93 : 41);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   T getLowerEndpoint() {
/* 286 */     return this.lowerEndpoint;
/*     */   }
/*     */   
/*     */   BoundType getLowerBoundType() {
/* 290 */     return this.lowerBoundType;
/*     */   }
/*     */   
/*     */   T getUpperEndpoint() {
/* 294 */     return this.upperEndpoint;
/*     */   }
/*     */   
/*     */   BoundType getUpperBoundType() {
/* 298 */     return this.upperBoundType;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\GeneralRange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */