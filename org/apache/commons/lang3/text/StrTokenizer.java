/*      */ package org.apache.commons.lang3.text;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import org.apache.commons.lang3.ArrayUtils;
/*      */ import org.apache.commons.lang3.StringUtils;
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
/*      */ public class StrTokenizer
/*      */   implements ListIterator<String>, Cloneable
/*      */ {
/*   92 */   private static final StrTokenizer CSV_TOKENIZER_PROTOTYPE = new StrTokenizer(); static {
/*   93 */     CSV_TOKENIZER_PROTOTYPE.setDelimiterMatcher(StrMatcher.commaMatcher());
/*   94 */     CSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StrMatcher.doubleQuoteMatcher());
/*   95 */     CSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StrMatcher.noneMatcher());
/*   96 */     CSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StrMatcher.trimMatcher());
/*   97 */     CSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(false);
/*   98 */     CSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(false);
/*      */   }
/*  100 */   private static final StrTokenizer TSV_TOKENIZER_PROTOTYPE = new StrTokenizer(); static {
/*  101 */     TSV_TOKENIZER_PROTOTYPE.setDelimiterMatcher(StrMatcher.tabMatcher());
/*  102 */     TSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StrMatcher.doubleQuoteMatcher());
/*  103 */     TSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StrMatcher.noneMatcher());
/*  104 */     TSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StrMatcher.trimMatcher());
/*  105 */     TSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(false);
/*  106 */     TSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private char[] chars;
/*      */   
/*      */   private String[] tokens;
/*      */   
/*      */   private int tokenPos;
/*      */   
/*  117 */   private StrMatcher delimMatcher = StrMatcher.splitMatcher();
/*      */   
/*  119 */   private StrMatcher quoteMatcher = StrMatcher.noneMatcher();
/*      */   
/*  121 */   private StrMatcher ignoredMatcher = StrMatcher.noneMatcher();
/*      */   
/*  123 */   private StrMatcher trimmerMatcher = StrMatcher.noneMatcher();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean emptyAsNull = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean ignoreEmptyTokens = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static StrTokenizer getCSVClone() {
/*  138 */     return (StrTokenizer)CSV_TOKENIZER_PROTOTYPE.clone();
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
/*      */   public static StrTokenizer getCSVInstance() {
/*  151 */     return getCSVClone();
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
/*      */   public static StrTokenizer getCSVInstance(String input) {
/*  164 */     StrTokenizer tok = getCSVClone();
/*  165 */     tok.reset(input);
/*  166 */     return tok;
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
/*      */   public static StrTokenizer getCSVInstance(char[] input) {
/*  179 */     StrTokenizer tok = getCSVClone();
/*  180 */     tok.reset(input);
/*  181 */     return tok;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static StrTokenizer getTSVClone() {
/*  190 */     return (StrTokenizer)TSV_TOKENIZER_PROTOTYPE.clone();
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
/*      */   public static StrTokenizer getTSVInstance() {
/*  203 */     return getTSVClone();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static StrTokenizer getTSVInstance(String input) {
/*  214 */     StrTokenizer tok = getTSVClone();
/*  215 */     tok.reset(input);
/*  216 */     return tok;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static StrTokenizer getTSVInstance(char[] input) {
/*  227 */     StrTokenizer tok = getTSVClone();
/*  228 */     tok.reset(input);
/*  229 */     return tok;
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
/*      */   public StrTokenizer() {
/*  241 */     this.chars = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(String input) {
/*  252 */     if (input != null) {
/*  253 */       this.chars = input.toCharArray();
/*      */     } else {
/*  255 */       this.chars = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(String input, char delim) {
/*  266 */     this(input);
/*  267 */     setDelimiterChar(delim);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(String input, String delim) {
/*  277 */     this(input);
/*  278 */     setDelimiterString(delim);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(String input, StrMatcher delim) {
/*  288 */     this(input);
/*  289 */     setDelimiterMatcher(delim);
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
/*      */   public StrTokenizer(String input, char delim, char quote) {
/*  301 */     this(input, delim);
/*  302 */     setQuoteChar(quote);
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
/*      */   public StrTokenizer(String input, StrMatcher delim, StrMatcher quote) {
/*  314 */     this(input, delim);
/*  315 */     setQuoteMatcher(quote);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(char[] input) {
/*  326 */     this.chars = ArrayUtils.clone(input);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(char[] input, char delim) {
/*  336 */     this(input);
/*  337 */     setDelimiterChar(delim);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(char[] input, String delim) {
/*  347 */     this(input);
/*  348 */     setDelimiterString(delim);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer(char[] input, StrMatcher delim) {
/*  358 */     this(input);
/*  359 */     setDelimiterMatcher(delim);
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
/*      */   public StrTokenizer(char[] input, char delim, char quote) {
/*  371 */     this(input, delim);
/*  372 */     setQuoteChar(quote);
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
/*      */   public StrTokenizer(char[] input, StrMatcher delim, StrMatcher quote) {
/*  384 */     this(input, delim);
/*  385 */     setQuoteMatcher(quote);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  396 */     checkTokenized();
/*  397 */     return this.tokens.length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String nextToken() {
/*  408 */     if (hasNext()) {
/*  409 */       return this.tokens[this.tokenPos++];
/*      */     }
/*  411 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String previousToken() {
/*  420 */     if (hasPrevious()) {
/*  421 */       return this.tokens[--this.tokenPos];
/*      */     }
/*  423 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getTokenArray() {
/*  432 */     checkTokenized();
/*  433 */     return (String[])this.tokens.clone();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getTokenList() {
/*  442 */     checkTokenized();
/*  443 */     List<String> list = new ArrayList<String>(this.tokens.length);
/*  444 */     for (String element : this.tokens) {
/*  445 */       list.add(element);
/*      */     }
/*  447 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer reset() {
/*  458 */     this.tokenPos = 0;
/*  459 */     this.tokens = null;
/*  460 */     return this;
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
/*      */   public StrTokenizer reset(String input) {
/*  472 */     reset();
/*  473 */     if (input != null) {
/*  474 */       this.chars = input.toCharArray();
/*      */     } else {
/*  476 */       this.chars = null;
/*      */     } 
/*  478 */     return this;
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
/*      */   public StrTokenizer reset(char[] input) {
/*  490 */     reset();
/*  491 */     this.chars = ArrayUtils.clone(input);
/*  492 */     return this;
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
/*      */   public boolean hasNext() {
/*  504 */     checkTokenized();
/*  505 */     return (this.tokenPos < this.tokens.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String next() {
/*  516 */     if (hasNext()) {
/*  517 */       return this.tokens[this.tokenPos++];
/*      */     }
/*  519 */     throw new NoSuchElementException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int nextIndex() {
/*  529 */     return this.tokenPos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasPrevious() {
/*  539 */     checkTokenized();
/*  540 */     return (this.tokenPos > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String previous() {
/*  550 */     if (hasPrevious()) {
/*  551 */       return this.tokens[--this.tokenPos];
/*      */     }
/*  553 */     throw new NoSuchElementException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int previousIndex() {
/*  563 */     return this.tokenPos - 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void remove() {
/*  573 */     throw new UnsupportedOperationException("remove() is unsupported");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void set(String obj) {
/*  583 */     throw new UnsupportedOperationException("set() is unsupported");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(String obj) {
/*  593 */     throw new UnsupportedOperationException("add() is unsupported");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkTokenized() {
/*  602 */     if (this.tokens == null) {
/*  603 */       if (this.chars == null) {
/*      */         
/*  605 */         List<String> split = tokenize(null, 0, 0);
/*  606 */         this.tokens = split.<String>toArray(new String[split.size()]);
/*      */       } else {
/*  608 */         List<String> split = tokenize(this.chars, 0, this.chars.length);
/*  609 */         this.tokens = split.<String>toArray(new String[split.size()]);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected List<String> tokenize(char[] srcChars, int offset, int count) {
/*  635 */     if (srcChars == null || count == 0) {
/*  636 */       return Collections.emptyList();
/*      */     }
/*  638 */     StrBuilder buf = new StrBuilder();
/*  639 */     List<String> tokenList = new ArrayList<String>();
/*  640 */     int pos = offset;
/*      */ 
/*      */     
/*  643 */     while (pos >= 0 && pos < count) {
/*      */       
/*  645 */       pos = readNextToken(srcChars, pos, count, buf, tokenList);
/*      */ 
/*      */       
/*  648 */       if (pos >= count) {
/*  649 */         addToken(tokenList, "");
/*      */       }
/*      */     } 
/*  652 */     return tokenList;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addToken(List<String> list, String tok) {
/*  662 */     if (StringUtils.isEmpty(tok)) {
/*  663 */       if (isIgnoreEmptyTokens()) {
/*      */         return;
/*      */       }
/*  666 */       if (isEmptyTokenAsNull()) {
/*  667 */         tok = null;
/*      */       }
/*      */     } 
/*  670 */     list.add(tok);
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
/*      */   private int readNextToken(char[] srcChars, int start, int len, StrBuilder workArea, List<String> tokenList) {
/*  687 */     while (start < len) {
/*  688 */       int removeLen = Math.max(
/*  689 */           getIgnoredMatcher().isMatch(srcChars, start, start, len), 
/*  690 */           getTrimmerMatcher().isMatch(srcChars, start, start, len));
/*  691 */       if (removeLen == 0 || 
/*  692 */         getDelimiterMatcher().isMatch(srcChars, start, start, len) > 0 || 
/*  693 */         getQuoteMatcher().isMatch(srcChars, start, start, len) > 0) {
/*      */         break;
/*      */       }
/*  696 */       start += removeLen;
/*      */     } 
/*      */ 
/*      */     
/*  700 */     if (start >= len) {
/*  701 */       addToken(tokenList, "");
/*  702 */       return -1;
/*      */     } 
/*      */ 
/*      */     
/*  706 */     int delimLen = getDelimiterMatcher().isMatch(srcChars, start, start, len);
/*  707 */     if (delimLen > 0) {
/*  708 */       addToken(tokenList, "");
/*  709 */       return start + delimLen;
/*      */     } 
/*      */ 
/*      */     
/*  713 */     int quoteLen = getQuoteMatcher().isMatch(srcChars, start, start, len);
/*  714 */     if (quoteLen > 0) {
/*  715 */       return readWithQuotes(srcChars, start + quoteLen, len, workArea, tokenList, start, quoteLen);
/*      */     }
/*  717 */     return readWithQuotes(srcChars, start, len, workArea, tokenList, 0, 0);
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
/*      */   private int readWithQuotes(char[] srcChars, int start, int len, StrBuilder workArea, List<String> tokenList, int quoteStart, int quoteLen) {
/*  738 */     workArea.clear();
/*  739 */     int pos = start;
/*  740 */     boolean quoting = (quoteLen > 0);
/*  741 */     int trimStart = 0;
/*      */     
/*  743 */     while (pos < len) {
/*      */ 
/*      */ 
/*      */       
/*  747 */       if (quoting) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  754 */         if (isQuote(srcChars, pos, len, quoteStart, quoteLen)) {
/*  755 */           if (isQuote(srcChars, pos + quoteLen, len, quoteStart, quoteLen)) {
/*      */             
/*  757 */             workArea.append(srcChars, pos, quoteLen);
/*  758 */             pos += quoteLen * 2;
/*  759 */             trimStart = workArea.size();
/*      */             
/*      */             continue;
/*      */           } 
/*      */           
/*  764 */           quoting = false;
/*  765 */           pos += quoteLen;
/*      */           
/*      */           continue;
/*      */         } 
/*      */         
/*  770 */         workArea.append(srcChars[pos++]);
/*  771 */         trimStart = workArea.size();
/*      */ 
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/*  777 */       int delimLen = getDelimiterMatcher().isMatch(srcChars, pos, start, len);
/*  778 */       if (delimLen > 0) {
/*      */         
/*  780 */         addToken(tokenList, workArea.substring(0, trimStart));
/*  781 */         return pos + delimLen;
/*      */       } 
/*      */ 
/*      */       
/*  785 */       if (quoteLen > 0 && isQuote(srcChars, pos, len, quoteStart, quoteLen)) {
/*  786 */         quoting = true;
/*  787 */         pos += quoteLen;
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/*  792 */       int ignoredLen = getIgnoredMatcher().isMatch(srcChars, pos, start, len);
/*  793 */       if (ignoredLen > 0) {
/*  794 */         pos += ignoredLen;
/*      */ 
/*      */         
/*      */         continue;
/*      */       } 
/*      */ 
/*      */       
/*  801 */       int trimmedLen = getTrimmerMatcher().isMatch(srcChars, pos, start, len);
/*  802 */       if (trimmedLen > 0) {
/*  803 */         workArea.append(srcChars, pos, trimmedLen);
/*  804 */         pos += trimmedLen;
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/*  809 */       workArea.append(srcChars[pos++]);
/*  810 */       trimStart = workArea.size();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  815 */     addToken(tokenList, workArea.substring(0, trimStart));
/*  816 */     return -1;
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
/*      */   private boolean isQuote(char[] srcChars, int pos, int len, int quoteStart, int quoteLen) {
/*  831 */     for (int i = 0; i < quoteLen; i++) {
/*  832 */       if (pos + i >= len || srcChars[pos + i] != srcChars[quoteStart + i]) {
/*  833 */         return false;
/*      */       }
/*      */     } 
/*  836 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrMatcher getDelimiterMatcher() {
/*  847 */     return this.delimMatcher;
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
/*      */   public StrTokenizer setDelimiterMatcher(StrMatcher delim) {
/*  859 */     if (delim == null) {
/*  860 */       this.delimMatcher = StrMatcher.noneMatcher();
/*      */     } else {
/*  862 */       this.delimMatcher = delim;
/*      */     } 
/*  864 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer setDelimiterChar(char delim) {
/*  874 */     return setDelimiterMatcher(StrMatcher.charMatcher(delim));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer setDelimiterString(String delim) {
/*  884 */     return setDelimiterMatcher(StrMatcher.stringMatcher(delim));
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
/*      */   public StrMatcher getQuoteMatcher() {
/*  899 */     return this.quoteMatcher;
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
/*      */   public StrTokenizer setQuoteMatcher(StrMatcher quote) {
/*  912 */     if (quote != null) {
/*  913 */       this.quoteMatcher = quote;
/*      */     }
/*  915 */     return this;
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
/*      */   public StrTokenizer setQuoteChar(char quote) {
/*  928 */     return setQuoteMatcher(StrMatcher.charMatcher(quote));
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
/*      */   public StrMatcher getIgnoredMatcher() {
/*  943 */     return this.ignoredMatcher;
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
/*      */   public StrTokenizer setIgnoredMatcher(StrMatcher ignored) {
/*  956 */     if (ignored != null) {
/*  957 */       this.ignoredMatcher = ignored;
/*      */     }
/*  959 */     return this;
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
/*      */   public StrTokenizer setIgnoredChar(char ignored) {
/*  972 */     return setIgnoredMatcher(StrMatcher.charMatcher(ignored));
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
/*      */   public StrMatcher getTrimmerMatcher() {
/*  987 */     return this.trimmerMatcher;
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
/*      */   public StrTokenizer setTrimmerMatcher(StrMatcher trimmer) {
/* 1000 */     if (trimmer != null) {
/* 1001 */       this.trimmerMatcher = trimmer;
/*      */     }
/* 1003 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEmptyTokenAsNull() {
/* 1014 */     return this.emptyAsNull;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer setEmptyTokenAsNull(boolean emptyAsNull) {
/* 1025 */     this.emptyAsNull = emptyAsNull;
/* 1026 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isIgnoreEmptyTokens() {
/* 1037 */     return this.ignoreEmptyTokens;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrTokenizer setIgnoreEmptyTokens(boolean ignoreEmptyTokens) {
/* 1048 */     this.ignoreEmptyTokens = ignoreEmptyTokens;
/* 1049 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getContent() {
/* 1059 */     if (this.chars == null) {
/* 1060 */       return null;
/*      */     }
/* 1062 */     return new String(this.chars);
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
/*      */   public Object clone() {
/*      */     try {
/* 1076 */       return cloneReset();
/* 1077 */     } catch (CloneNotSupportedException ex) {
/* 1078 */       return null;
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
/*      */   Object cloneReset() throws CloneNotSupportedException {
/* 1091 */     StrTokenizer cloned = (StrTokenizer)super.clone();
/* 1092 */     if (cloned.chars != null) {
/* 1093 */       cloned.chars = (char[])cloned.chars.clone();
/*      */     }
/* 1095 */     cloned.reset();
/* 1096 */     return cloned;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1107 */     if (this.tokens == null) {
/* 1108 */       return "StrTokenizer[not tokenized yet]";
/*      */     }
/* 1110 */     return "StrTokenizer" + getTokenList();
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\text\StrTokenizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */