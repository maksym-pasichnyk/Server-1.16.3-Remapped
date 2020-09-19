/*      */ package com.google.common.base;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import java.util.function.Predicate;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated = true)
/*      */ public abstract class CharMatcher
/*      */   implements Predicate<Character>
/*      */ {
/*      */   public static CharMatcher any() {
/*  108 */     return Any.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher none() {
/*  117 */     return None.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher whitespace() {
/*  134 */     return Whitespace.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher breakingWhitespace() {
/*  145 */     return BreakingWhitespace.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher ascii() {
/*  154 */     return Ascii.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher digit() {
/*  165 */     return Digit.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher javaDigit() {
/*  176 */     return JavaDigit.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher javaLetter() {
/*  187 */     return JavaLetter.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher javaLetterOrDigit() {
/*  197 */     return JavaLetterOrDigit.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher javaUpperCase() {
/*  207 */     return JavaUpperCase.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher javaLowerCase() {
/*  217 */     return JavaLowerCase.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher javaIsoControl() {
/*  227 */     return JavaIsoControl.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher invisible() {
/*  238 */     return Invisible.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher singleWidth() {
/*  252 */     return SingleWidth.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  272 */   public static final CharMatcher WHITESPACE = whitespace();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  284 */   public static final CharMatcher BREAKING_WHITESPACE = breakingWhitespace();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  294 */   public static final CharMatcher ASCII = ascii();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  306 */   public static final CharMatcher DIGIT = digit();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  317 */   public static final CharMatcher JAVA_DIGIT = javaDigit();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  329 */   public static final CharMatcher JAVA_LETTER = javaLetter();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  339 */   public static final CharMatcher JAVA_LETTER_OR_DIGIT = javaLetterOrDigit();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  349 */   public static final CharMatcher JAVA_UPPER_CASE = javaUpperCase();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  359 */   public static final CharMatcher JAVA_LOWER_CASE = javaLowerCase();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  369 */   public static final CharMatcher JAVA_ISO_CONTROL = javaIsoControl();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  380 */   public static final CharMatcher INVISIBLE = invisible();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  394 */   public static final CharMatcher SINGLE_WIDTH = singleWidth();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  403 */   public static final CharMatcher ANY = any();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  412 */   public static final CharMatcher NONE = none();
/*      */ 
/*      */   
/*      */   private static final int DISTINCT_CHARS = 65536;
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher is(char match) {
/*  420 */     return new Is(match);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher isNot(char match) {
/*  429 */     return new IsNot(match);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher anyOf(CharSequence sequence) {
/*  437 */     switch (sequence.length()) {
/*      */       case 0:
/*  439 */         return none();
/*      */       case 1:
/*  441 */         return is(sequence.charAt(0));
/*      */       case 2:
/*  443 */         return isEither(sequence.charAt(0), sequence.charAt(1));
/*      */     } 
/*      */ 
/*      */     
/*  447 */     return new AnyOf(sequence);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher noneOf(CharSequence sequence) {
/*  456 */     return anyOf(sequence).negate();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher inRange(char startInclusive, char endInclusive) {
/*  467 */     return new InRange(startInclusive, endInclusive);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher forPredicate(Predicate<? super Character> predicate) {
/*  475 */     return (predicate instanceof CharMatcher) ? (CharMatcher)predicate : new ForPredicate(predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CharMatcher negate() {
/*  497 */     return new Negated(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CharMatcher and(CharMatcher other) {
/*  504 */     return new And(this, other);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CharMatcher or(CharMatcher other) {
/*  511 */     return new Or(this, other);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CharMatcher precomputed() {
/*  524 */     return Platform.precomputeCharMatcher(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   CharMatcher precomputedInternal() {
/*  541 */     BitSet table = new BitSet();
/*  542 */     setBits(table);
/*  543 */     int totalCharacters = table.cardinality();
/*  544 */     if (totalCharacters * 2 <= 65536) {
/*  545 */       return precomputedPositive(totalCharacters, table, toString());
/*      */     }
/*      */     
/*  548 */     table.flip(0, 65536);
/*  549 */     int negatedCharacters = 65536 - totalCharacters;
/*  550 */     String suffix = ".negate()";
/*  551 */     final String description = toString();
/*      */ 
/*      */     
/*  554 */     String negatedDescription = description.endsWith(suffix) ? description.substring(0, description.length() - suffix.length()) : (description + suffix);
/*      */     
/*  556 */     return new NegatedFastMatcher(
/*  557 */         precomputedPositive(negatedCharacters, table, negatedDescription))
/*      */       {
/*      */         public String toString() {
/*  560 */           return description;
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   private static CharMatcher precomputedPositive(int totalCharacters, BitSet table, String description) {
/*      */     char c1;
/*      */     char c2;
/*  572 */     switch (totalCharacters) {
/*      */       case 0:
/*  574 */         return none();
/*      */       case 1:
/*  576 */         return is((char)table.nextSetBit(0));
/*      */       case 2:
/*  578 */         c1 = (char)table.nextSetBit(0);
/*  579 */         c2 = (char)table.nextSetBit(c1 + 1);
/*  580 */         return isEither(c1, c2);
/*      */     } 
/*  582 */     return isSmall(totalCharacters, table.length()) ? 
/*  583 */       SmallCharMatcher.from(table, description) : new BitSetMatcher(table, description);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   private static boolean isSmall(int totalCharacters, int tableLength) {
/*  590 */     return (totalCharacters <= 1023 && tableLength > totalCharacters * 4 * 16);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   void setBits(BitSet table) {
/*  600 */     for (int c = 65535; c >= 0; c--) {
/*  601 */       if (matches((char)c)) {
/*  602 */         table.set(c);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean matchesAnyOf(CharSequence sequence) {
/*  621 */     return !matchesNoneOf(sequence);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean matchesAllOf(CharSequence sequence) {
/*  635 */     for (int i = sequence.length() - 1; i >= 0; i--) {
/*  636 */       if (!matches(sequence.charAt(i))) {
/*  637 */         return false;
/*      */       }
/*      */     } 
/*  640 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean matchesNoneOf(CharSequence sequence) {
/*  655 */     return (indexIn(sequence) == -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int indexIn(CharSequence sequence) {
/*  669 */     return indexIn(sequence, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int indexIn(CharSequence sequence, int start) {
/*  688 */     int length = sequence.length();
/*  689 */     Preconditions.checkPositionIndex(start, length);
/*  690 */     for (int i = start; i < length; i++) {
/*  691 */       if (matches(sequence.charAt(i))) {
/*  692 */         return i;
/*      */       }
/*      */     } 
/*  695 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int lastIndexIn(CharSequence sequence) {
/*  709 */     for (int i = sequence.length() - 1; i >= 0; i--) {
/*  710 */       if (matches(sequence.charAt(i))) {
/*  711 */         return i;
/*      */       }
/*      */     } 
/*  714 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int countIn(CharSequence sequence) {
/*  721 */     int count = 0;
/*  722 */     for (int i = 0; i < sequence.length(); i++) {
/*  723 */       if (matches(sequence.charAt(i))) {
/*  724 */         count++;
/*      */       }
/*      */     } 
/*  727 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String removeFrom(CharSequence sequence) {
/*  739 */     String string = sequence.toString();
/*  740 */     int pos = indexIn(string);
/*  741 */     if (pos == -1) {
/*  742 */       return string;
/*      */     }
/*      */     
/*  745 */     char[] chars = string.toCharArray();
/*  746 */     int spread = 1;
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/*  751 */       pos++;
/*      */       
/*  753 */       while (pos != chars.length) {
/*      */ 
/*      */         
/*  756 */         if (matches(chars[pos]))
/*      */         
/*      */         { 
/*      */ 
/*      */ 
/*      */           
/*  762 */           spread++; continue; }  chars[pos - spread] = chars[pos]; pos++;
/*      */       }  break;
/*  764 */     }  return new String(chars, 0, pos - spread);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String retainFrom(CharSequence sequence) {
/*  776 */     return negate().removeFrom(sequence);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replaceFrom(CharSequence sequence, char replacement) {
/*  797 */     String string = sequence.toString();
/*  798 */     int pos = indexIn(string);
/*  799 */     if (pos == -1) {
/*  800 */       return string;
/*      */     }
/*  802 */     char[] chars = string.toCharArray();
/*  803 */     chars[pos] = replacement;
/*  804 */     for (int i = pos + 1; i < chars.length; i++) {
/*  805 */       if (matches(chars[i])) {
/*  806 */         chars[i] = replacement;
/*      */       }
/*      */     } 
/*  809 */     return new String(chars);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replaceFrom(CharSequence sequence, CharSequence replacement) {
/*  829 */     int replacementLen = replacement.length();
/*  830 */     if (replacementLen == 0) {
/*  831 */       return removeFrom(sequence);
/*      */     }
/*  833 */     if (replacementLen == 1) {
/*  834 */       return replaceFrom(sequence, replacement.charAt(0));
/*      */     }
/*      */     
/*  837 */     String string = sequence.toString();
/*  838 */     int pos = indexIn(string);
/*  839 */     if (pos == -1) {
/*  840 */       return string;
/*      */     }
/*      */     
/*  843 */     int len = string.length();
/*  844 */     StringBuilder buf = new StringBuilder(len * 3 / 2 + 16);
/*      */     
/*  846 */     int oldpos = 0;
/*      */     do {
/*  848 */       buf.append(string, oldpos, pos);
/*  849 */       buf.append(replacement);
/*  850 */       oldpos = pos + 1;
/*  851 */       pos = indexIn(string, oldpos);
/*  852 */     } while (pos != -1);
/*      */     
/*  854 */     buf.append(string, oldpos, len);
/*  855 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String trimFrom(CharSequence sequence) {
/*  873 */     int len = sequence.length();
/*      */     
/*      */     int first;
/*      */     
/*  877 */     for (first = 0; first < len && 
/*  878 */       matches(sequence.charAt(first)); first++);
/*      */     
/*      */     int last;
/*      */     
/*  882 */     for (last = len - 1; last > first && 
/*  883 */       matches(sequence.charAt(last)); last--);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  888 */     return sequence.subSequence(first, last + 1).toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String trimLeadingFrom(CharSequence sequence) {
/*  900 */     int len = sequence.length();
/*  901 */     for (int first = 0; first < len; first++) {
/*  902 */       if (!matches(sequence.charAt(first))) {
/*  903 */         return sequence.subSequence(first, len).toString();
/*      */       }
/*      */     } 
/*  906 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String trimTrailingFrom(CharSequence sequence) {
/*  918 */     int len = sequence.length();
/*  919 */     for (int last = len - 1; last >= 0; last--) {
/*  920 */       if (!matches(sequence.charAt(last))) {
/*  921 */         return sequence.subSequence(0, last + 1).toString();
/*      */       }
/*      */     } 
/*  924 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String collapseFrom(CharSequence sequence, char replacement) {
/*  947 */     int len = sequence.length();
/*  948 */     for (int i = 0; i < len; i++) {
/*  949 */       char c = sequence.charAt(i);
/*  950 */       if (matches(c)) {
/*  951 */         if (c == replacement && (i == len - 1 || !matches(sequence.charAt(i + 1)))) {
/*      */           
/*  953 */           i++;
/*      */         } else {
/*  955 */           StringBuilder builder = (new StringBuilder(len)).append(sequence, 0, i).append(replacement);
/*  956 */           return finishCollapseFrom(sequence, i + 1, len, replacement, builder, true);
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/*  961 */     return sequence.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String trimAndCollapseFrom(CharSequence sequence, char replacement) {
/*  971 */     int len = sequence.length();
/*  972 */     int first = 0;
/*  973 */     int last = len - 1;
/*      */     
/*  975 */     while (first < len && matches(sequence.charAt(first))) {
/*  976 */       first++;
/*      */     }
/*      */     
/*  979 */     while (last > first && matches(sequence.charAt(last))) {
/*  980 */       last--;
/*      */     }
/*      */     
/*  983 */     return (first == 0 && last == len - 1) ? 
/*  984 */       collapseFrom(sequence, replacement) : 
/*  985 */       finishCollapseFrom(sequence, first, last + 1, replacement, new StringBuilder(last + 1 - first), false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String finishCollapseFrom(CharSequence sequence, int start, int end, char replacement, StringBuilder builder, boolean inMatchingGroup) {
/*  996 */     for (int i = start; i < end; i++) {
/*  997 */       char c = sequence.charAt(i);
/*  998 */       if (matches(c)) {
/*  999 */         if (!inMatchingGroup) {
/* 1000 */           builder.append(replacement);
/* 1001 */           inMatchingGroup = true;
/*      */         } 
/*      */       } else {
/* 1004 */         builder.append(c);
/* 1005 */         inMatchingGroup = false;
/*      */       } 
/*      */     } 
/* 1008 */     return builder.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean apply(Character character) {
/* 1018 */     return matches(character.charValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1027 */     return super.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String showCharacter(char c) {
/* 1035 */     String hex = "0123456789ABCDEF";
/* 1036 */     char[] tmp = { '\\', 'u', Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE };
/* 1037 */     for (int i = 0; i < 4; i++) {
/* 1038 */       tmp[5 - i] = hex.charAt(c & 0xF);
/* 1039 */       c = (char)(c >> 4);
/*      */     } 
/* 1041 */     return String.copyValueOf(tmp);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class FastMatcher
/*      */     extends CharMatcher
/*      */   {
/*      */     public final CharMatcher precomputed() {
/* 1051 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher negate() {
/* 1056 */       return new CharMatcher.NegatedFastMatcher(this);
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class NamedFastMatcher
/*      */     extends FastMatcher
/*      */   {
/*      */     private final String description;
/*      */     
/*      */     NamedFastMatcher(String description) {
/* 1066 */       this.description = Preconditions.<String>checkNotNull(description);
/*      */     }
/*      */ 
/*      */     
/*      */     public final String toString() {
/* 1071 */       return this.description;
/*      */     }
/*      */   }
/*      */   
/*      */   static class NegatedFastMatcher
/*      */     extends Negated
/*      */   {
/*      */     NegatedFastMatcher(CharMatcher original) {
/* 1079 */       super(original);
/*      */     }
/*      */ 
/*      */     
/*      */     public final CharMatcher precomputed() {
/* 1084 */       return this;
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   private static final class BitSetMatcher
/*      */     extends NamedFastMatcher
/*      */   {
/*      */     private final BitSet table;
/*      */     
/*      */     private BitSetMatcher(BitSet table, String description) {
/* 1095 */       super(description);
/* 1096 */       if (table.length() + 64 < table.size()) {
/* 1097 */         table = (BitSet)table.clone();
/*      */       }
/*      */       
/* 1100 */       this.table = table;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1105 */       return this.table.get(c);
/*      */     }
/*      */ 
/*      */     
/*      */     void setBits(BitSet bitSet) {
/* 1110 */       bitSet.or(this.table);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class Any
/*      */     extends NamedFastMatcher
/*      */   {
/* 1119 */     static final Any INSTANCE = new Any();
/*      */     
/*      */     private Any() {
/* 1122 */       super("CharMatcher.any()");
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1127 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexIn(CharSequence sequence) {
/* 1132 */       return (sequence.length() == 0) ? -1 : 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexIn(CharSequence sequence, int start) {
/* 1137 */       int length = sequence.length();
/* 1138 */       Preconditions.checkPositionIndex(start, length);
/* 1139 */       return (start == length) ? -1 : start;
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastIndexIn(CharSequence sequence) {
/* 1144 */       return sequence.length() - 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matchesAllOf(CharSequence sequence) {
/* 1149 */       Preconditions.checkNotNull(sequence);
/* 1150 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matchesNoneOf(CharSequence sequence) {
/* 1155 */       return (sequence.length() == 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public String removeFrom(CharSequence sequence) {
/* 1160 */       Preconditions.checkNotNull(sequence);
/* 1161 */       return "";
/*      */     }
/*      */ 
/*      */     
/*      */     public String replaceFrom(CharSequence sequence, char replacement) {
/* 1166 */       char[] array = new char[sequence.length()];
/* 1167 */       Arrays.fill(array, replacement);
/* 1168 */       return new String(array);
/*      */     }
/*      */ 
/*      */     
/*      */     public String replaceFrom(CharSequence sequence, CharSequence replacement) {
/* 1173 */       StringBuilder result = new StringBuilder(sequence.length() * replacement.length());
/* 1174 */       for (int i = 0; i < sequence.length(); i++) {
/* 1175 */         result.append(replacement);
/*      */       }
/* 1177 */       return result.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public String collapseFrom(CharSequence sequence, char replacement) {
/* 1182 */       return (sequence.length() == 0) ? "" : String.valueOf(replacement);
/*      */     }
/*      */ 
/*      */     
/*      */     public String trimFrom(CharSequence sequence) {
/* 1187 */       Preconditions.checkNotNull(sequence);
/* 1188 */       return "";
/*      */     }
/*      */ 
/*      */     
/*      */     public int countIn(CharSequence sequence) {
/* 1193 */       return sequence.length();
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher and(CharMatcher other) {
/* 1198 */       return Preconditions.<CharMatcher>checkNotNull(other);
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher or(CharMatcher other) {
/* 1203 */       Preconditions.checkNotNull(other);
/* 1204 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher negate() {
/* 1209 */       return none();
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class None
/*      */     extends NamedFastMatcher
/*      */   {
/* 1216 */     static final None INSTANCE = new None();
/*      */     
/*      */     private None() {
/* 1219 */       super("CharMatcher.none()");
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1224 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexIn(CharSequence sequence) {
/* 1229 */       Preconditions.checkNotNull(sequence);
/* 1230 */       return -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexIn(CharSequence sequence, int start) {
/* 1235 */       int length = sequence.length();
/* 1236 */       Preconditions.checkPositionIndex(start, length);
/* 1237 */       return -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastIndexIn(CharSequence sequence) {
/* 1242 */       Preconditions.checkNotNull(sequence);
/* 1243 */       return -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matchesAllOf(CharSequence sequence) {
/* 1248 */       return (sequence.length() == 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matchesNoneOf(CharSequence sequence) {
/* 1253 */       Preconditions.checkNotNull(sequence);
/* 1254 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public String removeFrom(CharSequence sequence) {
/* 1259 */       return sequence.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public String replaceFrom(CharSequence sequence, char replacement) {
/* 1264 */       return sequence.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public String replaceFrom(CharSequence sequence, CharSequence replacement) {
/* 1269 */       Preconditions.checkNotNull(replacement);
/* 1270 */       return sequence.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public String collapseFrom(CharSequence sequence, char replacement) {
/* 1275 */       return sequence.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public String trimFrom(CharSequence sequence) {
/* 1280 */       return sequence.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public String trimLeadingFrom(CharSequence sequence) {
/* 1285 */       return sequence.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public String trimTrailingFrom(CharSequence sequence) {
/* 1290 */       return sequence.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public int countIn(CharSequence sequence) {
/* 1295 */       Preconditions.checkNotNull(sequence);
/* 1296 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher and(CharMatcher other) {
/* 1301 */       Preconditions.checkNotNull(other);
/* 1302 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher or(CharMatcher other) {
/* 1307 */       return Preconditions.<CharMatcher>checkNotNull(other);
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher negate() {
/* 1312 */       return any();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static final class Whitespace
/*      */     extends NamedFastMatcher
/*      */   {
/*      */     static final String TABLE = " 　\r   　 \013　   　 \t     \f 　 　　 \n 　";
/*      */     
/*      */     static final int MULTIPLIER = 1682554634;
/*      */     
/* 1326 */     static final int SHIFT = Integer.numberOfLeadingZeros(" 　\r   　 \013　   　 \t     \f 　 　　 \n 　".length() - 1);
/*      */     
/* 1328 */     static final Whitespace INSTANCE = new Whitespace();
/*      */     
/*      */     Whitespace() {
/* 1331 */       super("CharMatcher.whitespace()");
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1336 */       return (" 　\r   　 \013　   　 \t     \f 　 　　 \n 　".charAt(1682554634 * c >>> SHIFT) == c);
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1342 */       for (int i = 0; i < " 　\r   　 \013　   　 \t     \f 　 　　 \n 　".length(); i++) {
/* 1343 */         table.set(" 　\r   　 \013　   　 \t     \f 　 　　 \n 　".charAt(i));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class BreakingWhitespace
/*      */     extends CharMatcher
/*      */   {
/* 1351 */     static final CharMatcher INSTANCE = new BreakingWhitespace();
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1355 */       switch (c) {
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\013':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/*      */         case '':
/*      */         case ' ':
/*      */         case ' ':
/*      */         case ' ':
/*      */         case ' ':
/*      */         case '　':
/* 1368 */           return true;
/*      */         case ' ':
/* 1370 */           return false;
/*      */       } 
/* 1372 */       return (c >= ' ' && c <= ' ');
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1378 */       return "CharMatcher.breakingWhitespace()";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class Ascii
/*      */     extends NamedFastMatcher
/*      */   {
/* 1385 */     static final Ascii INSTANCE = new Ascii();
/*      */     
/*      */     Ascii() {
/* 1388 */       super("CharMatcher.ascii()");
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1393 */       return (c <= '');
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RangesMatcher
/*      */     extends CharMatcher
/*      */   {
/*      */     private final String description;
/*      */     private final char[] rangeStarts;
/*      */     private final char[] rangeEnds;
/*      */     
/*      */     RangesMatcher(String description, char[] rangeStarts, char[] rangeEnds) {
/* 1405 */       this.description = description;
/* 1406 */       this.rangeStarts = rangeStarts;
/* 1407 */       this.rangeEnds = rangeEnds;
/* 1408 */       Preconditions.checkArgument((rangeStarts.length == rangeEnds.length));
/* 1409 */       for (int i = 0; i < rangeStarts.length; i++) {
/* 1410 */         Preconditions.checkArgument((rangeStarts[i] <= rangeEnds[i]));
/* 1411 */         if (i + 1 < rangeStarts.length) {
/* 1412 */           Preconditions.checkArgument((rangeEnds[i] < rangeStarts[i + 1]));
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1419 */       int index = Arrays.binarySearch(this.rangeStarts, c);
/* 1420 */       if (index >= 0) {
/* 1421 */         return true;
/*      */       }
/* 1423 */       index = (index ^ 0xFFFFFFFF) - 1;
/* 1424 */       return (index >= 0 && c <= this.rangeEnds[index]);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1430 */       return this.description;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class Digit
/*      */     extends RangesMatcher
/*      */   {
/*      */     private static final String ZEROES = "0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０";
/*      */ 
/*      */ 
/*      */     
/*      */     private static char[] zeroes() {
/* 1444 */       return "0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".toCharArray();
/*      */     }
/*      */     
/*      */     private static char[] nines() {
/* 1448 */       char[] nines = new char["0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".length()];
/* 1449 */       for (int i = 0; i < "0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".length(); i++) {
/* 1450 */         nines[i] = (char)("0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".charAt(i) + 9);
/*      */       }
/* 1452 */       return nines;
/*      */     }
/*      */     
/* 1455 */     static final Digit INSTANCE = new Digit();
/*      */     
/*      */     private Digit() {
/* 1458 */       super("CharMatcher.digit()", zeroes(), nines());
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class JavaDigit
/*      */     extends CharMatcher
/*      */   {
/* 1465 */     static final JavaDigit INSTANCE = new JavaDigit();
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1469 */       return Character.isDigit(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1474 */       return "CharMatcher.javaDigit()";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class JavaLetter
/*      */     extends CharMatcher
/*      */   {
/* 1481 */     static final JavaLetter INSTANCE = new JavaLetter();
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1485 */       return Character.isLetter(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1490 */       return "CharMatcher.javaLetter()";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class JavaLetterOrDigit
/*      */     extends CharMatcher
/*      */   {
/* 1497 */     static final JavaLetterOrDigit INSTANCE = new JavaLetterOrDigit();
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1501 */       return Character.isLetterOrDigit(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1506 */       return "CharMatcher.javaLetterOrDigit()";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class JavaUpperCase
/*      */     extends CharMatcher
/*      */   {
/* 1513 */     static final JavaUpperCase INSTANCE = new JavaUpperCase();
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1517 */       return Character.isUpperCase(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1522 */       return "CharMatcher.javaUpperCase()";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class JavaLowerCase
/*      */     extends CharMatcher
/*      */   {
/* 1529 */     static final JavaLowerCase INSTANCE = new JavaLowerCase();
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1533 */       return Character.isLowerCase(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1538 */       return "CharMatcher.javaLowerCase()";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class JavaIsoControl
/*      */     extends NamedFastMatcher
/*      */   {
/* 1545 */     static final JavaIsoControl INSTANCE = new JavaIsoControl();
/*      */     
/*      */     private JavaIsoControl() {
/* 1548 */       super("CharMatcher.javaIsoControl()");
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1553 */       return (c <= '\037' || (c >= '' && c <= ''));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class Invisible
/*      */     extends RangesMatcher
/*      */   {
/*      */     private static final String RANGE_STARTS = "\000­؀؜۝܏ ᠎   ⁦⁧⁨⁩⁪　?﻿￹￺";
/*      */ 
/*      */     
/*      */     private static final String RANGE_ENDS = "  ­؄؜۝܏ ᠎‏ ⁤⁦⁧⁨⁩⁯　﻿￹￻";
/*      */     
/* 1567 */     static final Invisible INSTANCE = new Invisible();
/*      */     
/*      */     private Invisible() {
/* 1570 */       super("CharMatcher.invisible()", "\000­؀؜۝܏ ᠎   ⁦⁧⁨⁩⁪　?﻿￹￺".toCharArray(), "  ­؄؜۝܏ ᠎‏ ⁤⁦⁧⁨⁩⁯　﻿￹￻".toCharArray());
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class SingleWidth
/*      */     extends RangesMatcher
/*      */   {
/* 1577 */     static final SingleWidth INSTANCE = new SingleWidth();
/*      */     
/*      */     private SingleWidth() {
/* 1580 */       super("CharMatcher.singleWidth()", "\000־א׳؀ݐ฀Ḁ℀ﭐﹰ｡"
/*      */           
/* 1582 */           .toCharArray(), "ӹ־ת״ۿݿ๿₯℺﷿﻿ￜ"
/* 1583 */           .toCharArray());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class Negated
/*      */     extends CharMatcher
/*      */   {
/*      */     final CharMatcher original;
/*      */ 
/*      */     
/*      */     Negated(CharMatcher original) {
/* 1595 */       this.original = Preconditions.<CharMatcher>checkNotNull(original);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1600 */       return !this.original.matches(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matchesAllOf(CharSequence sequence) {
/* 1605 */       return this.original.matchesNoneOf(sequence);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matchesNoneOf(CharSequence sequence) {
/* 1610 */       return this.original.matchesAllOf(sequence);
/*      */     }
/*      */ 
/*      */     
/*      */     public int countIn(CharSequence sequence) {
/* 1615 */       return sequence.length() - this.original.countIn(sequence);
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1621 */       BitSet tmp = new BitSet();
/* 1622 */       this.original.setBits(tmp);
/* 1623 */       tmp.flip(0, 65536);
/* 1624 */       table.or(tmp);
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher negate() {
/* 1629 */       return this.original;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1634 */       return this.original + ".negate()";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class And
/*      */     extends CharMatcher
/*      */   {
/*      */     final CharMatcher first;
/*      */     final CharMatcher second;
/*      */     
/*      */     And(CharMatcher a, CharMatcher b) {
/* 1645 */       this.first = Preconditions.<CharMatcher>checkNotNull(a);
/* 1646 */       this.second = Preconditions.<CharMatcher>checkNotNull(b);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1651 */       return (this.first.matches(c) && this.second.matches(c));
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1657 */       BitSet tmp1 = new BitSet();
/* 1658 */       this.first.setBits(tmp1);
/* 1659 */       BitSet tmp2 = new BitSet();
/* 1660 */       this.second.setBits(tmp2);
/* 1661 */       tmp1.and(tmp2);
/* 1662 */       table.or(tmp1);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1667 */       return "CharMatcher.and(" + this.first + ", " + this.second + ")";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class Or
/*      */     extends CharMatcher
/*      */   {
/*      */     final CharMatcher first;
/*      */     final CharMatcher second;
/*      */     
/*      */     Or(CharMatcher a, CharMatcher b) {
/* 1678 */       this.first = Preconditions.<CharMatcher>checkNotNull(a);
/* 1679 */       this.second = Preconditions.<CharMatcher>checkNotNull(b);
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1685 */       this.first.setBits(table);
/* 1686 */       this.second.setBits(table);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1691 */       return (this.first.matches(c) || this.second.matches(c));
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1696 */       return "CharMatcher.or(" + this.first + ", " + this.second + ")";
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class Is
/*      */     extends FastMatcher
/*      */   {
/*      */     private final char match;
/*      */ 
/*      */     
/*      */     Is(char match) {
/* 1708 */       this.match = match;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1713 */       return (c == this.match);
/*      */     }
/*      */ 
/*      */     
/*      */     public String replaceFrom(CharSequence sequence, char replacement) {
/* 1718 */       return sequence.toString().replace(this.match, replacement);
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher and(CharMatcher other) {
/* 1723 */       return other.matches(this.match) ? this : none();
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher or(CharMatcher other) {
/* 1728 */       return other.matches(this.match) ? other : super.or(other);
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher negate() {
/* 1733 */       return isNot(this.match);
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1739 */       table.set(this.match);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1744 */       return "CharMatcher.is('" + CharMatcher.showCharacter(this.match) + "')";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class IsNot
/*      */     extends FastMatcher
/*      */   {
/*      */     private final char match;
/*      */     
/*      */     IsNot(char match) {
/* 1754 */       this.match = match;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1759 */       return (c != this.match);
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher and(CharMatcher other) {
/* 1764 */       return other.matches(this.match) ? super.and(other) : other;
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher or(CharMatcher other) {
/* 1769 */       return other.matches(this.match) ? any() : this;
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1775 */       table.set(0, this.match);
/* 1776 */       table.set(this.match + 1, 65536);
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher negate() {
/* 1781 */       return is(this.match);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1786 */       return "CharMatcher.isNot('" + CharMatcher.showCharacter(this.match) + "')";
/*      */     }
/*      */   }
/*      */   
/*      */   private static IsEither isEither(char c1, char c2) {
/* 1791 */     return new IsEither(c1, c2);
/*      */   }
/*      */   
/*      */   public abstract boolean matches(char paramChar);
/*      */   
/*      */   private static final class IsEither extends FastMatcher {
/*      */     private final char match1;
/*      */     private final char match2;
/*      */     
/*      */     IsEither(char match1, char match2) {
/* 1801 */       this.match1 = match1;
/* 1802 */       this.match2 = match2;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1807 */       return (c == this.match1 || c == this.match2);
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1813 */       table.set(this.match1);
/* 1814 */       table.set(this.match2);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1819 */       return "CharMatcher.anyOf(\"" + CharMatcher.showCharacter(this.match1) + CharMatcher.showCharacter(this.match2) + "\")";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class AnyOf
/*      */     extends CharMatcher
/*      */   {
/*      */     private final char[] chars;
/*      */     
/*      */     public AnyOf(CharSequence chars) {
/* 1829 */       this.chars = chars.toString().toCharArray();
/* 1830 */       Arrays.sort(this.chars);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1835 */       return (Arrays.binarySearch(this.chars, c) >= 0);
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1841 */       for (char c : this.chars) {
/* 1842 */         table.set(c);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1848 */       StringBuilder description = new StringBuilder("CharMatcher.anyOf(\"");
/* 1849 */       for (char c : this.chars) {
/* 1850 */         description.append(CharMatcher.showCharacter(c));
/*      */       }
/* 1852 */       description.append("\")");
/* 1853 */       return description.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class InRange
/*      */     extends FastMatcher
/*      */   {
/*      */     private final char startInclusive;
/*      */     private final char endInclusive;
/*      */     
/*      */     InRange(char startInclusive, char endInclusive) {
/* 1864 */       Preconditions.checkArgument((endInclusive >= startInclusive));
/* 1865 */       this.startInclusive = startInclusive;
/* 1866 */       this.endInclusive = endInclusive;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1871 */       return (this.startInclusive <= c && c <= this.endInclusive);
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1877 */       table.set(this.startInclusive, this.endInclusive + 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1882 */       return "CharMatcher.inRange('" + CharMatcher
/* 1883 */         .showCharacter(this.startInclusive) + "', '" + CharMatcher
/*      */         
/* 1885 */         .showCharacter(this.endInclusive) + "')";
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class ForPredicate
/*      */     extends CharMatcher
/*      */   {
/*      */     private final Predicate<? super Character> predicate;
/*      */     
/*      */     ForPredicate(Predicate<? super Character> predicate) {
/* 1896 */       this.predicate = Preconditions.<Predicate<? super Character>>checkNotNull(predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1901 */       return this.predicate.apply(Character.valueOf(c));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean apply(Character character) {
/* 1907 */       return this.predicate.apply(Preconditions.checkNotNull(character));
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1912 */       return "CharMatcher.forPredicate(" + this.predicate + ")";
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\base\CharMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */