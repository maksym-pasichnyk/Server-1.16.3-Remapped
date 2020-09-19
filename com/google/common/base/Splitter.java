/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Splitter
/*     */ {
/*     */   private final CharMatcher trimmer;
/*     */   private final boolean omitEmptyStrings;
/*     */   private final Strategy strategy;
/*     */   private final int limit;
/*     */   
/*     */   private Splitter(Strategy strategy) {
/* 100 */     this(strategy, false, CharMatcher.none(), 2147483647);
/*     */   }
/*     */   
/*     */   private Splitter(Strategy strategy, boolean omitEmptyStrings, CharMatcher trimmer, int limit) {
/* 104 */     this.strategy = strategy;
/* 105 */     this.omitEmptyStrings = omitEmptyStrings;
/* 106 */     this.trimmer = trimmer;
/* 107 */     this.limit = limit;
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
/*     */   public static Splitter on(char separator) {
/* 119 */     return on(CharMatcher.is(separator));
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
/*     */   public static Splitter on(final CharMatcher separatorMatcher) {
/* 133 */     Preconditions.checkNotNull(separatorMatcher);
/*     */     
/* 135 */     return new Splitter(new Strategy()
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
/*     */           {
/* 139 */             return new Splitter.SplittingIterator(splitter, toSplit)
/*     */               {
/*     */                 int separatorStart(int start) {
/* 142 */                   return separatorMatcher.indexIn(this.toSplit, start);
/*     */                 }
/*     */ 
/*     */                 
/*     */                 int separatorEnd(int separatorPosition) {
/* 147 */                   return separatorPosition + 1;
/*     */                 }
/*     */               };
/*     */           }
/*     */         });
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
/*     */   public static Splitter on(final String separator) {
/* 163 */     Preconditions.checkArgument((separator.length() != 0), "The separator may not be the empty string.");
/* 164 */     if (separator.length() == 1) {
/* 165 */       return on(separator.charAt(0));
/*     */     }
/* 167 */     return new Splitter(new Strategy()
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
/*     */           {
/* 171 */             return new Splitter.SplittingIterator(splitter, toSplit)
/*     */               {
/*     */                 public int separatorStart(int start) {
/* 174 */                   int separatorLength = separator.length();
/*     */ 
/*     */                   
/* 177 */                   for (int p = start, last = this.toSplit.length() - separatorLength; p <= last; p++) {
/* 178 */                     int i = 0; while (true) { if (i < separatorLength) {
/* 179 */                         if (this.toSplit.charAt(i + p) != separator.charAt(i))
/*     */                           break;  i++;
/*     */                         continue;
/*     */                       } 
/* 183 */                       return p; }
/*     */                   
/* 185 */                   }  return -1;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public int separatorEnd(int separatorPosition) {
/* 190 */                   return separatorPosition + separator.length();
/*     */                 }
/*     */               };
/*     */           }
/*     */         });
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
/*     */   @GwtIncompatible
/*     */   public static Splitter on(Pattern separatorPattern) {
/* 209 */     return on(new JdkPattern(separatorPattern));
/*     */   }
/*     */   
/*     */   private static Splitter on(final CommonPattern separatorPattern) {
/* 213 */     Preconditions.checkArgument(
/* 214 */         !separatorPattern.matcher("").matches(), "The pattern may not match the empty string: %s", separatorPattern);
/*     */ 
/*     */ 
/*     */     
/* 218 */     return new Splitter(new Strategy()
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
/*     */           {
/* 222 */             final CommonMatcher matcher = separatorPattern.matcher(toSplit);
/* 223 */             return new Splitter.SplittingIterator(splitter, toSplit)
/*     */               {
/*     */                 public int separatorStart(int start) {
/* 226 */                   return matcher.find(start) ? matcher.start() : -1;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public int separatorEnd(int separatorPosition) {
/* 231 */                   return matcher.end();
/*     */                 }
/*     */               };
/*     */           }
/*     */         });
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
/*     */   public static Splitter onPattern(String separatorPattern) {
/* 252 */     return on(Platform.compilePattern(separatorPattern));
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
/*     */   public static Splitter fixedLength(final int length) {
/* 273 */     Preconditions.checkArgument((length > 0), "The length may not be less than 1");
/*     */     
/* 275 */     return new Splitter(new Strategy()
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
/*     */           {
/* 279 */             return new Splitter.SplittingIterator(splitter, toSplit)
/*     */               {
/*     */                 public int separatorStart(int start) {
/* 282 */                   int nextChunkStart = start + length;
/* 283 */                   return (nextChunkStart < this.toSplit.length()) ? nextChunkStart : -1;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public int separatorEnd(int separatorPosition) {
/* 288 */                   return separatorPosition;
/*     */                 }
/*     */               };
/*     */           }
/*     */         });
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
/*     */   public Splitter omitEmptyStrings() {
/* 312 */     return new Splitter(this.strategy, true, this.trimmer, this.limit);
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
/*     */   public Splitter limit(int limit) {
/* 333 */     Preconditions.checkArgument((limit > 0), "must be greater than zero: %s", limit);
/* 334 */     return new Splitter(this.strategy, this.omitEmptyStrings, this.trimmer, limit);
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
/*     */   public Splitter trimResults() {
/* 347 */     return trimResults(CharMatcher.whitespace());
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
/*     */   public Splitter trimResults(CharMatcher trimmer) {
/* 363 */     Preconditions.checkNotNull(trimmer);
/* 364 */     return new Splitter(this.strategy, this.omitEmptyStrings, trimmer, this.limit);
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
/*     */   public Iterable<String> split(final CharSequence sequence) {
/* 376 */     Preconditions.checkNotNull(sequence);
/*     */     
/* 378 */     return new Iterable<String>()
/*     */       {
/*     */         public Iterator<String> iterator() {
/* 381 */           return Splitter.this.splittingIterator(sequence);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 386 */           return Joiner.on(", ")
/* 387 */             .appendTo((new StringBuilder()).append('['), this)
/* 388 */             .append(']')
/* 389 */             .toString();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private Iterator<String> splittingIterator(CharSequence sequence) {
/* 395 */     return this.strategy.iterator(this, sequence);
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
/*     */   @Beta
/*     */   public List<String> splitToList(CharSequence sequence) {
/* 408 */     Preconditions.checkNotNull(sequence);
/*     */     
/* 410 */     Iterator<String> iterator = splittingIterator(sequence);
/* 411 */     List<String> result = new ArrayList<>();
/*     */     
/* 413 */     while (iterator.hasNext()) {
/* 414 */       result.add(iterator.next());
/*     */     }
/*     */     
/* 417 */     return Collections.unmodifiableList(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public MapSplitter withKeyValueSeparator(String separator) {
/* 428 */     return withKeyValueSeparator(on(separator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public MapSplitter withKeyValueSeparator(char separator) {
/* 439 */     return withKeyValueSeparator(on(separator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public MapSplitter withKeyValueSeparator(Splitter keyValueSplitter) {
/* 450 */     return new MapSplitter(this, keyValueSplitter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static final class MapSplitter
/*     */   {
/*     */     private static final String INVALID_ENTRY_MESSAGE = "Chunk [%s] is not a valid entry";
/*     */     
/*     */     private final Splitter outerSplitter;
/*     */     
/*     */     private final Splitter entrySplitter;
/*     */ 
/*     */     
/*     */     private MapSplitter(Splitter outerSplitter, Splitter entrySplitter) {
/* 466 */       this.outerSplitter = outerSplitter;
/* 467 */       this.entrySplitter = Preconditions.<Splitter>checkNotNull(entrySplitter);
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
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<String, String> split(CharSequence sequence) {
/* 482 */       Map<String, String> map = new LinkedHashMap<>();
/* 483 */       for (String entry : this.outerSplitter.split(sequence)) {
/* 484 */         Iterator<String> entryFields = this.entrySplitter.splittingIterator(entry);
/*     */         
/* 486 */         Preconditions.checkArgument(entryFields.hasNext(), "Chunk [%s] is not a valid entry", entry);
/* 487 */         String key = entryFields.next();
/* 488 */         Preconditions.checkArgument(!map.containsKey(key), "Duplicate key [%s] found.", key);
/*     */         
/* 490 */         Preconditions.checkArgument(entryFields.hasNext(), "Chunk [%s] is not a valid entry", entry);
/* 491 */         String value = entryFields.next();
/* 492 */         map.put(key, value);
/*     */         
/* 494 */         Preconditions.checkArgument(!entryFields.hasNext(), "Chunk [%s] is not a valid entry", entry);
/*     */       } 
/* 496 */       return Collections.unmodifiableMap(map);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static interface Strategy
/*     */   {
/*     */     Iterator<String> iterator(Splitter param1Splitter, CharSequence param1CharSequence);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class SplittingIterator
/*     */     extends AbstractIterator<String>
/*     */   {
/*     */     final CharSequence toSplit;
/*     */ 
/*     */     
/*     */     final CharMatcher trimmer;
/*     */ 
/*     */     
/*     */     final boolean omitEmptyStrings;
/*     */ 
/*     */     
/* 522 */     int offset = 0;
/*     */     int limit;
/*     */     
/*     */     protected SplittingIterator(Splitter splitter, CharSequence toSplit) {
/* 526 */       this.trimmer = splitter.trimmer;
/* 527 */       this.omitEmptyStrings = splitter.omitEmptyStrings;
/* 528 */       this.limit = splitter.limit;
/* 529 */       this.toSplit = toSplit;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected String computeNext() {
/* 539 */       int nextStart = this.offset;
/* 540 */       while (this.offset != -1) {
/* 541 */         int end, start = nextStart;
/*     */ 
/*     */         
/* 544 */         int separatorPosition = separatorStart(this.offset);
/* 545 */         if (separatorPosition == -1) {
/* 546 */           end = this.toSplit.length();
/* 547 */           this.offset = -1;
/*     */         } else {
/* 549 */           end = separatorPosition;
/* 550 */           this.offset = separatorEnd(separatorPosition);
/*     */         } 
/* 552 */         if (this.offset == nextStart) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 559 */           this.offset++;
/* 560 */           if (this.offset > this.toSplit.length()) {
/* 561 */             this.offset = -1;
/*     */           }
/*     */           
/*     */           continue;
/*     */         } 
/* 566 */         while (start < end && this.trimmer.matches(this.toSplit.charAt(start))) {
/* 567 */           start++;
/*     */         }
/* 569 */         while (end > start && this.trimmer.matches(this.toSplit.charAt(end - 1))) {
/* 570 */           end--;
/*     */         }
/*     */         
/* 573 */         if (this.omitEmptyStrings && start == end) {
/*     */           
/* 575 */           nextStart = this.offset;
/*     */           
/*     */           continue;
/*     */         } 
/* 579 */         if (this.limit == 1) {
/*     */ 
/*     */ 
/*     */           
/* 583 */           end = this.toSplit.length();
/* 584 */           this.offset = -1;
/*     */           
/* 586 */           while (end > start && this.trimmer.matches(this.toSplit.charAt(end - 1))) {
/* 587 */             end--;
/*     */           }
/*     */         } else {
/* 590 */           this.limit--;
/*     */         } 
/*     */         
/* 593 */         return this.toSplit.subSequence(start, end).toString();
/*     */       } 
/* 595 */       return endOfData();
/*     */     }
/*     */     
/*     */     abstract int separatorStart(int param1Int);
/*     */     
/*     */     abstract int separatorEnd(int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\base\Splitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */