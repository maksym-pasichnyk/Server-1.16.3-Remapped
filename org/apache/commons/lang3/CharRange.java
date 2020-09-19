/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ final class CharRange
/*     */   implements Iterable<Character>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 8270183163158333422L;
/*     */   private final char start;
/*     */   private final char end;
/*     */   private final boolean negated;
/*     */   private transient String iToString;
/*     */   
/*     */   private CharRange(char start, char end, boolean negated) {
/*  68 */     if (start > end) {
/*  69 */       char temp = start;
/*  70 */       start = end;
/*  71 */       end = temp;
/*     */     } 
/*     */     
/*  74 */     this.start = start;
/*  75 */     this.end = end;
/*  76 */     this.negated = negated;
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
/*     */   public static CharRange is(char ch) {
/*  88 */     return new CharRange(ch, ch, false);
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
/*     */   public static CharRange isNot(char ch) {
/* 100 */     return new CharRange(ch, ch, true);
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
/*     */   public static CharRange isIn(char start, char end) {
/* 113 */     return new CharRange(start, end, false);
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
/*     */   public static CharRange isNotIn(char start, char end) {
/* 126 */     return new CharRange(start, end, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getStart() {
/* 137 */     return this.start;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getEnd() {
/* 146 */     return this.end;
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
/*     */   public boolean isNegated() {
/* 158 */     return this.negated;
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
/*     */   public boolean contains(char ch) {
/* 170 */     return (((ch >= this.start && ch <= this.end)) != this.negated);
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
/*     */   public boolean contains(CharRange range) {
/* 182 */     if (range == null) {
/* 183 */       throw new IllegalArgumentException("The Range must not be null");
/*     */     }
/* 185 */     if (this.negated) {
/* 186 */       if (range.negated) {
/* 187 */         return (this.start >= range.start && this.end <= range.end);
/*     */       }
/* 189 */       return (range.end < this.start || range.start > this.end);
/*     */     } 
/* 191 */     if (range.negated) {
/* 192 */       return (this.start == '\000' && this.end == Character.MAX_VALUE);
/*     */     }
/* 194 */     return (this.start <= range.start && this.end >= range.end);
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
/*     */   public boolean equals(Object obj) {
/* 208 */     if (obj == this) {
/* 209 */       return true;
/*     */     }
/* 211 */     if (!(obj instanceof CharRange)) {
/* 212 */       return false;
/*     */     }
/* 214 */     CharRange other = (CharRange)obj;
/* 215 */     return (this.start == other.start && this.end == other.end && this.negated == other.negated);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 225 */     return 83 + this.start + 7 * this.end + (this.negated ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 235 */     if (this.iToString == null) {
/* 236 */       StringBuilder buf = new StringBuilder(4);
/* 237 */       if (isNegated()) {
/* 238 */         buf.append('^');
/*     */       }
/* 240 */       buf.append(this.start);
/* 241 */       if (this.start != this.end) {
/* 242 */         buf.append('-');
/* 243 */         buf.append(this.end);
/*     */       } 
/* 245 */       this.iToString = buf.toString();
/*     */     } 
/* 247 */     return this.iToString;
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
/*     */   public Iterator<Character> iterator() {
/* 261 */     return new CharacterIterator(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CharacterIterator
/*     */     implements Iterator<Character>
/*     */   {
/*     */     private char current;
/*     */ 
/*     */     
/*     */     private final CharRange range;
/*     */ 
/*     */     
/*     */     private boolean hasNext;
/*     */ 
/*     */ 
/*     */     
/*     */     private CharacterIterator(CharRange r) {
/* 281 */       this.range = r;
/* 282 */       this.hasNext = true;
/*     */       
/* 284 */       if (this.range.negated) {
/* 285 */         if (this.range.start == '\000') {
/* 286 */           if (this.range.end == Character.MAX_VALUE) {
/*     */             
/* 288 */             this.hasNext = false;
/*     */           } else {
/* 290 */             this.current = (char)(this.range.end + 1);
/*     */           } 
/*     */         } else {
/* 293 */           this.current = Character.MIN_VALUE;
/*     */         } 
/*     */       } else {
/* 296 */         this.current = this.range.start;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void prepareNext() {
/* 304 */       if (this.range.negated) {
/* 305 */         if (this.current == Character.MAX_VALUE) {
/* 306 */           this.hasNext = false;
/* 307 */         } else if (this.current + 1 == this.range.start) {
/* 308 */           if (this.range.end == Character.MAX_VALUE) {
/* 309 */             this.hasNext = false;
/*     */           } else {
/* 311 */             this.current = (char)(this.range.end + 1);
/*     */           } 
/*     */         } else {
/* 314 */           this.current = (char)(this.current + 1);
/*     */         } 
/* 316 */       } else if (this.current < this.range.end) {
/* 317 */         this.current = (char)(this.current + 1);
/*     */       } else {
/* 319 */         this.hasNext = false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 330 */       return this.hasNext;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Character next() {
/* 340 */       if (!this.hasNext) {
/* 341 */         throw new NoSuchElementException();
/*     */       }
/* 343 */       char cur = this.current;
/* 344 */       prepareNext();
/* 345 */       return Character.valueOf(cur);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void remove() {
/* 356 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\CharRange.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */