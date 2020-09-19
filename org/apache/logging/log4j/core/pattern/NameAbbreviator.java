/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public abstract class NameAbbreviator
/*     */ {
/*  33 */   private static final NameAbbreviator DEFAULT = new NOPAbbreviator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NameAbbreviator getAbbreviator(String pattern) {
/*  49 */     if (pattern.length() > 0) {
/*     */       boolean isNegativeNumber;
/*     */       
/*  52 */       String number, trimmed = pattern.trim();
/*     */       
/*  54 */       if (trimmed.isEmpty()) {
/*  55 */         return DEFAULT;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  62 */       if (trimmed.length() > 1 && trimmed.charAt(0) == '-') {
/*  63 */         isNegativeNumber = true;
/*  64 */         number = trimmed.substring(1);
/*     */       } else {
/*  66 */         isNegativeNumber = false;
/*  67 */         number = trimmed;
/*     */       } 
/*     */       
/*  70 */       int i = 0;
/*     */ 
/*     */       
/*  73 */       while (i < number.length() && number.charAt(i) >= '0' && number.charAt(i) <= '9') {
/*  74 */         i++;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  80 */       if (i == number.length()) {
/*  81 */         return new MaxElementAbbreviator(Integer.parseInt(number), isNegativeNumber ? MaxElementAbbreviator.Strategy.DROP : MaxElementAbbreviator.Strategy.RETAIN);
/*     */       }
/*     */ 
/*     */       
/*  85 */       ArrayList<PatternAbbreviatorFragment> fragments = new ArrayList<>(5);
/*     */ 
/*     */       
/*  88 */       int pos = 0;
/*     */       
/*  90 */       while (pos < trimmed.length() && pos >= 0) {
/*  91 */         int charCount, ellipsisPos = pos;
/*     */         
/*  93 */         if (trimmed.charAt(pos) == '*') {
/*  94 */           charCount = Integer.MAX_VALUE;
/*  95 */           ellipsisPos++;
/*     */         }
/*  97 */         else if (trimmed.charAt(pos) >= '0' && trimmed.charAt(pos) <= '9') {
/*  98 */           charCount = trimmed.charAt(pos) - 48;
/*  99 */           ellipsisPos++;
/*     */         } else {
/* 101 */           charCount = 0;
/*     */         } 
/*     */ 
/*     */         
/* 105 */         char ellipsis = Character.MIN_VALUE;
/*     */         
/* 107 */         if (ellipsisPos < trimmed.length()) {
/* 108 */           ellipsis = trimmed.charAt(ellipsisPos);
/*     */           
/* 110 */           if (ellipsis == '.') {
/* 111 */             ellipsis = Character.MIN_VALUE;
/*     */           }
/*     */         } 
/*     */         
/* 115 */         fragments.add(new PatternAbbreviatorFragment(charCount, ellipsis));
/* 116 */         pos = trimmed.indexOf('.', pos);
/*     */         
/* 118 */         if (pos == -1) {
/*     */           break;
/*     */         }
/*     */         
/* 122 */         pos++;
/*     */       } 
/*     */       
/* 125 */       return new PatternAbbreviator(fragments);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     return DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NameAbbreviator getDefaultAbbreviator() {
/* 140 */     return DEFAULT;
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
/*     */   public abstract void abbreviate(String paramString, StringBuilder paramStringBuilder);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class NOPAbbreviator
/*     */     extends NameAbbreviator
/*     */   {
/*     */     public void abbreviate(String original, StringBuilder destination) {
/* 166 */       destination.append(original);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class MaxElementAbbreviator
/*     */     extends NameAbbreviator
/*     */   {
/*     */     private final int count;
/*     */     
/*     */     private final Strategy strategy;
/*     */ 
/*     */     
/*     */     private enum Strategy
/*     */     {
/* 181 */       DROP(0)
/*     */       {
/*     */         void abbreviate(int count, String original, StringBuilder destination)
/*     */         {
/* 185 */           int start = 0;
/*     */           
/* 187 */           for (int i = 0; i < count; i++) {
/* 188 */             int nextStart = original.indexOf('.', start);
/* 189 */             if (nextStart == -1) {
/* 190 */               destination.append(original);
/*     */               return;
/*     */             } 
/* 193 */             start = nextStart + 1;
/*     */           } 
/* 195 */           destination.append(original, start, original.length());
/*     */         }
/*     */       },
/* 198 */       RETAIN(1)
/*     */       {
/*     */ 
/*     */         
/*     */         void abbreviate(int count, String original, StringBuilder destination)
/*     */         {
/* 204 */           int end = original.length() - 1;
/*     */           
/* 206 */           for (int i = count; i > 0; i--) {
/* 207 */             end = original.lastIndexOf('.', end - 1);
/* 208 */             if (end == -1) {
/* 209 */               destination.append(original);
/*     */               return;
/*     */             } 
/*     */           } 
/* 213 */           destination.append(original, end + 1, original.length());
/*     */         }
/*     */       };
/*     */       
/*     */       final int minCount;
/*     */       
/*     */       Strategy(int minCount) {
/* 220 */         this.minCount = minCount;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       abstract void abbreviate(int param2Int, String param2String, StringBuilder param2StringBuilder);
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
/*     */     public MaxElementAbbreviator(int count, Strategy strategy) {
/* 243 */       this.count = Math.max(count, strategy.minCount);
/* 244 */       this.strategy = strategy;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void abbreviate(String original, StringBuilder destination) {
/* 255 */       this.strategy.abbreviate(this.count, original, destination);
/*     */     } } private enum Strategy { DROP(0) {
/*     */       void abbreviate(int count, String original, StringBuilder destination) {
/*     */         int start = 0;
/*     */         for (int i = 0; i < count; i++) {
/*     */           int nextStart = original.indexOf('.', start);
/*     */           if (nextStart == -1) {
/*     */             destination.append(original);
/*     */             return;
/*     */           } 
/*     */           start = nextStart + 1;
/*     */         } 
/*     */         destination.append(original, start, original.length());
/*     */       }
/*     */     },
/*     */     RETAIN(1) { void abbreviate(int count, String original, StringBuilder destination) {
/*     */         int end = original.length() - 1;
/*     */         for (int i = count; i > 0; i--) {
/*     */           end = original.lastIndexOf('.', end - 1);
/*     */           if (end == -1) {
/*     */             destination.append(original);
/*     */             return;
/*     */           } 
/*     */         } 
/*     */         destination.append(original, end + 1, original.length());
/*     */       } }; final int minCount; Strategy(int minCount) {
/*     */       this.minCount = minCount;
/*     */     } abstract void abbreviate(int param1Int, String param1String, StringBuilder param1StringBuilder); } private static class PatternAbbreviatorFragment { public PatternAbbreviatorFragment(int charCount, char ellipsis) {
/* 283 */       this.charCount = charCount;
/* 284 */       this.ellipsis = ellipsis;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private final int charCount;
/*     */     
/*     */     private final char ellipsis;
/*     */ 
/*     */     
/*     */     public int abbreviate(StringBuilder buf, int startPos) {
/* 295 */       int start = (startPos < 0) ? 0 : startPos;
/* 296 */       int max = buf.length();
/* 297 */       int nextDot = -1;
/* 298 */       for (int i = start; i < max; i++) {
/* 299 */         if (buf.charAt(i) == '.') {
/* 300 */           nextDot = i;
/*     */           break;
/*     */         } 
/*     */       } 
/* 304 */       if (nextDot != -1) {
/* 305 */         if (nextDot - startPos > this.charCount) {
/* 306 */           buf.delete(startPos + this.charCount, nextDot);
/* 307 */           nextDot = startPos + this.charCount;
/*     */           
/* 309 */           if (this.ellipsis != '\000') {
/* 310 */             buf.insert(nextDot, this.ellipsis);
/* 311 */             nextDot++;
/*     */           } 
/*     */         } 
/* 314 */         nextDot++;
/*     */       } 
/* 316 */       return nextDot;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PatternAbbreviator
/*     */     extends NameAbbreviator
/*     */   {
/*     */     private final NameAbbreviator.PatternAbbreviatorFragment[] fragments;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public PatternAbbreviator(List<NameAbbreviator.PatternAbbreviatorFragment> fragments) {
/* 335 */       if (fragments.isEmpty()) {
/* 336 */         throw new IllegalArgumentException("fragments must have at least one element");
/*     */       }
/*     */ 
/*     */       
/* 340 */       this.fragments = new NameAbbreviator.PatternAbbreviatorFragment[fragments.size()];
/* 341 */       fragments.toArray(this.fragments);
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
/*     */     public void abbreviate(String original, StringBuilder destination) {
/* 355 */       int pos = destination.length();
/* 356 */       int max = pos + original.length();
/* 357 */       StringBuilder sb = destination.append(original);
/*     */       
/* 359 */       for (int i = 0; i < this.fragments.length - 1 && pos < original.length(); i++) {
/* 360 */         pos = this.fragments[i].abbreviate(sb, pos);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 366 */       NameAbbreviator.PatternAbbreviatorFragment terminalFragment = this.fragments[this.fragments.length - 1];
/*     */       
/* 368 */       while (pos < max && pos >= 0)
/* 369 */         pos = terminalFragment.abbreviate(sb, pos); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\pattern\NameAbbreviator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */