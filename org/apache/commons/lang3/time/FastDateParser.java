/*     */ package org.apache.commons.lang3.time;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.text.DateFormatSymbols;
/*     */ import java.text.ParseException;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import java.util.TreeSet;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.regex.Matcher;
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
/*     */ public class FastDateParser
/*     */   implements DateParser, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3L;
/*  82 */   static final Locale JAPANESE_IMPERIAL = new Locale("ja", "JP", "JP");
/*     */   
/*     */   private final String pattern;
/*     */   
/*     */   private final TimeZone timeZone;
/*     */   
/*     */   private final Locale locale;
/*     */   
/*     */   private final int century;
/*     */   
/*     */   private final int startYear;
/*     */   
/*     */   private transient List<StrategyAndWidth> patterns;
/*     */ 
/*     */   
/*  97 */   private static final Comparator<String> LONGER_FIRST_LOWERCASE = new Comparator<String>()
/*     */     {
/*     */       public int compare(String left, String right) {
/* 100 */         return right.compareTo(left);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FastDateParser(String pattern, TimeZone timeZone, Locale locale) {
/* 116 */     this(pattern, timeZone, locale, null);
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
/*     */   protected FastDateParser(String pattern, TimeZone timeZone, Locale locale, Date centuryStart) {
/*     */     int centuryStartYear;
/* 131 */     this.pattern = pattern;
/* 132 */     this.timeZone = timeZone;
/* 133 */     this.locale = locale;
/*     */     
/* 135 */     Calendar definingCalendar = Calendar.getInstance(timeZone, locale);
/*     */ 
/*     */     
/* 138 */     if (centuryStart != null) {
/* 139 */       definingCalendar.setTime(centuryStart);
/* 140 */       centuryStartYear = definingCalendar.get(1);
/*     */     }
/* 142 */     else if (locale.equals(JAPANESE_IMPERIAL)) {
/* 143 */       centuryStartYear = 0;
/*     */     }
/*     */     else {
/*     */       
/* 147 */       definingCalendar.setTime(new Date());
/* 148 */       centuryStartYear = definingCalendar.get(1) - 80;
/*     */     } 
/* 150 */     this.century = centuryStartYear / 100 * 100;
/* 151 */     this.startYear = centuryStartYear - this.century;
/*     */     
/* 153 */     init(definingCalendar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(Calendar definingCalendar) {
/* 163 */     this.patterns = new ArrayList<StrategyAndWidth>();
/*     */     
/* 165 */     StrategyParser fm = new StrategyParser(this.pattern, definingCalendar);
/*     */     while (true) {
/* 167 */       StrategyAndWidth field = fm.getNextStrategy();
/* 168 */       if (field == null) {
/*     */         break;
/*     */       }
/* 171 */       this.patterns.add(field);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class StrategyAndWidth
/*     */   {
/*     */     final FastDateParser.Strategy strategy;
/*     */ 
/*     */     
/*     */     final int width;
/*     */ 
/*     */     
/*     */     StrategyAndWidth(FastDateParser.Strategy strategy, int width) {
/* 186 */       this.strategy = strategy;
/* 187 */       this.width = width;
/*     */     }
/*     */     
/*     */     int getMaxWidth(ListIterator<StrategyAndWidth> lt) {
/* 191 */       if (!this.strategy.isNumber() || !lt.hasNext()) {
/* 192 */         return 0;
/*     */       }
/* 194 */       FastDateParser.Strategy nextStrategy = ((StrategyAndWidth)lt.next()).strategy;
/* 195 */       lt.previous();
/* 196 */       return nextStrategy.isNumber() ? this.width : 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class StrategyParser
/*     */   {
/*     */     private final String pattern;
/*     */     
/*     */     private final Calendar definingCalendar;
/*     */     private int currentIdx;
/*     */     
/*     */     StrategyParser(String pattern, Calendar definingCalendar) {
/* 209 */       this.pattern = pattern;
/* 210 */       this.definingCalendar = definingCalendar;
/*     */     }
/*     */     
/*     */     FastDateParser.StrategyAndWidth getNextStrategy() {
/* 214 */       if (this.currentIdx >= this.pattern.length()) {
/* 215 */         return null;
/*     */       }
/*     */       
/* 218 */       char c = this.pattern.charAt(this.currentIdx);
/* 219 */       if (FastDateParser.isFormatLetter(c)) {
/* 220 */         return letterPattern(c);
/*     */       }
/* 222 */       return literal();
/*     */     }
/*     */     
/*     */     private FastDateParser.StrategyAndWidth letterPattern(char c) {
/* 226 */       int begin = this.currentIdx; do {  }
/* 227 */       while (++this.currentIdx < this.pattern.length() && 
/* 228 */         this.pattern.charAt(this.currentIdx) == c);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 233 */       int width = this.currentIdx - begin;
/* 234 */       return new FastDateParser.StrategyAndWidth(FastDateParser.this.getStrategy(c, width, this.definingCalendar), width);
/*     */     }
/*     */     
/*     */     private FastDateParser.StrategyAndWidth literal() {
/* 238 */       boolean activeQuote = false;
/*     */       
/* 240 */       StringBuilder sb = new StringBuilder();
/* 241 */       while (this.currentIdx < this.pattern.length()) {
/* 242 */         char c = this.pattern.charAt(this.currentIdx);
/* 243 */         if (!activeQuote && FastDateParser.isFormatLetter(c))
/*     */           break; 
/* 245 */         if (c == '\'' && (++this.currentIdx == this.pattern.length() || this.pattern.charAt(this.currentIdx) != '\'')) {
/* 246 */           activeQuote = !activeQuote;
/*     */           continue;
/*     */         } 
/* 249 */         this.currentIdx++;
/* 250 */         sb.append(c);
/*     */       } 
/*     */       
/* 253 */       if (activeQuote) {
/* 254 */         throw new IllegalArgumentException("Unterminated quote");
/*     */       }
/*     */       
/* 257 */       String formatField = sb.toString();
/* 258 */       return new FastDateParser.StrategyAndWidth(new FastDateParser.CopyQuotedStrategy(formatField), formatField.length());
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean isFormatLetter(char c) {
/* 263 */     return ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPattern() {
/* 273 */     return this.pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 281 */     return this.timeZone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 289 */     return this.locale;
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
/* 303 */     if (!(obj instanceof FastDateParser)) {
/* 304 */       return false;
/*     */     }
/* 306 */     FastDateParser other = (FastDateParser)obj;
/* 307 */     return (this.pattern.equals(other.pattern) && this.timeZone
/* 308 */       .equals(other.timeZone) && this.locale
/* 309 */       .equals(other.locale));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 319 */     return this.pattern.hashCode() + 13 * (this.timeZone.hashCode() + 13 * this.locale.hashCode());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 329 */     return "FastDateParser[" + this.pattern + "," + this.locale + "," + this.timeZone.getID() + "]";
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
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 343 */     in.defaultReadObject();
/*     */     
/* 345 */     Calendar definingCalendar = Calendar.getInstance(this.timeZone, this.locale);
/* 346 */     init(definingCalendar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object parseObject(String source) throws ParseException {
/* 354 */     return parse(source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date parse(String source) throws ParseException {
/* 362 */     ParsePosition pp = new ParsePosition(0);
/* 363 */     Date date = parse(source, pp);
/* 364 */     if (date == null) {
/*     */       
/* 366 */       if (this.locale.equals(JAPANESE_IMPERIAL)) {
/* 367 */         throw new ParseException("(The " + this.locale + " locale does not support dates before 1868 AD)\nUnparseable date: \"" + source, pp
/*     */             
/* 369 */             .getErrorIndex());
/*     */       }
/* 371 */       throw new ParseException("Unparseable date: " + source, pp.getErrorIndex());
/*     */     } 
/* 373 */     return date;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object parseObject(String source, ParsePosition pos) {
/* 381 */     return parse(source, pos);
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
/*     */   public Date parse(String source, ParsePosition pos) {
/* 399 */     Calendar cal = Calendar.getInstance(this.timeZone, this.locale);
/* 400 */     cal.clear();
/*     */     
/* 402 */     return parse(source, pos, cal) ? cal.getTime() : null;
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
/*     */   public boolean parse(String source, ParsePosition pos, Calendar calendar) {
/* 420 */     ListIterator<StrategyAndWidth> lt = this.patterns.listIterator();
/* 421 */     while (lt.hasNext()) {
/* 422 */       StrategyAndWidth pattern = lt.next();
/* 423 */       int maxWidth = pattern.getMaxWidth(lt);
/* 424 */       if (!pattern.strategy.parse(this, calendar, source, pos, maxWidth)) {
/* 425 */         return false;
/*     */       }
/*     */     } 
/* 428 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static StringBuilder simpleQuote(StringBuilder sb, String value) {
/* 435 */     for (int i = 0; i < value.length(); i++) {
/* 436 */       char c = value.charAt(i);
/* 437 */       switch (c) {
/*     */         case '$':
/*     */         case '(':
/*     */         case ')':
/*     */         case '*':
/*     */         case '+':
/*     */         case '.':
/*     */         case '?':
/*     */         case '[':
/*     */         case '\\':
/*     */         case '^':
/*     */         case '{':
/*     */         case '|':
/* 450 */           sb.append('\\'); break;
/*     */       } 
/* 452 */       sb.append(c);
/*     */     } 
/*     */     
/* 455 */     return sb;
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
/*     */   private static Map<String, Integer> appendDisplayNames(Calendar cal, Locale locale, int field, StringBuilder regex) {
/* 467 */     Map<String, Integer> values = new HashMap<String, Integer>();
/*     */     
/* 469 */     Map<String, Integer> displayNames = cal.getDisplayNames(field, 0, locale);
/* 470 */     TreeSet<String> sorted = new TreeSet<String>(LONGER_FIRST_LOWERCASE);
/* 471 */     for (Map.Entry<String, Integer> displayName : displayNames.entrySet()) {
/* 472 */       String key = ((String)displayName.getKey()).toLowerCase(locale);
/* 473 */       if (sorted.add(key)) {
/* 474 */         values.put(key, displayName.getValue());
/*     */       }
/*     */     } 
/* 477 */     for (String symbol : sorted) {
/* 478 */       simpleQuote(regex, symbol).append('|');
/*     */     }
/* 480 */     return values;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int adjustYear(int twoDigitYear) {
/* 489 */     int trial = this.century + twoDigitYear;
/* 490 */     return (twoDigitYear >= this.startYear) ? trial : (trial + 100);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class Strategy
/*     */   {
/*     */     private Strategy() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isNumber() {
/* 504 */       return false;
/*     */     }
/*     */     
/*     */     abstract boolean parse(FastDateParser param1FastDateParser, Calendar param1Calendar, String param1String, ParsePosition param1ParsePosition, int param1Int);
/*     */   }
/*     */   
/*     */   private static abstract class PatternStrategy
/*     */     extends Strategy
/*     */   {
/*     */     private Pattern pattern;
/*     */     
/*     */     private PatternStrategy() {}
/*     */     
/*     */     void createPattern(StringBuilder regex) {
/* 518 */       createPattern(regex.toString());
/*     */     }
/*     */     
/*     */     void createPattern(String regex) {
/* 522 */       this.pattern = Pattern.compile(regex);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isNumber() {
/* 533 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth) {
/* 538 */       Matcher matcher = this.pattern.matcher(source.substring(pos.getIndex()));
/* 539 */       if (!matcher.lookingAt()) {
/* 540 */         pos.setErrorIndex(pos.getIndex());
/* 541 */         return false;
/*     */       } 
/* 543 */       pos.setIndex(pos.getIndex() + matcher.end(1));
/* 544 */       setCalendar(parser, calendar, matcher.group(1));
/* 545 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract void setCalendar(FastDateParser param1FastDateParser, Calendar param1Calendar, String param1String);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Strategy getStrategy(char f, int width, Calendar definingCalendar) {
/* 558 */     switch (f) {
/*     */       default:
/* 560 */         throw new IllegalArgumentException("Format '" + f + "' not supported");
/*     */       case 'D':
/* 562 */         return DAY_OF_YEAR_STRATEGY;
/*     */       case 'E':
/* 564 */         return getLocaleSpecificStrategy(7, definingCalendar);
/*     */       case 'F':
/* 566 */         return DAY_OF_WEEK_IN_MONTH_STRATEGY;
/*     */       case 'G':
/* 568 */         return getLocaleSpecificStrategy(0, definingCalendar);
/*     */       case 'H':
/* 570 */         return HOUR_OF_DAY_STRATEGY;
/*     */       case 'K':
/* 572 */         return HOUR_STRATEGY;
/*     */       case 'M':
/* 574 */         return (width >= 3) ? getLocaleSpecificStrategy(2, definingCalendar) : NUMBER_MONTH_STRATEGY;
/*     */       case 'S':
/* 576 */         return MILLISECOND_STRATEGY;
/*     */       case 'W':
/* 578 */         return WEEK_OF_MONTH_STRATEGY;
/*     */       case 'a':
/* 580 */         return getLocaleSpecificStrategy(9, definingCalendar);
/*     */       case 'd':
/* 582 */         return DAY_OF_MONTH_STRATEGY;
/*     */       case 'h':
/* 584 */         return HOUR12_STRATEGY;
/*     */       case 'k':
/* 586 */         return HOUR24_OF_DAY_STRATEGY;
/*     */       case 'm':
/* 588 */         return MINUTE_STRATEGY;
/*     */       case 's':
/* 590 */         return SECOND_STRATEGY;
/*     */       case 'u':
/* 592 */         return DAY_OF_WEEK_STRATEGY;
/*     */       case 'w':
/* 594 */         return WEEK_OF_YEAR_STRATEGY;
/*     */       case 'Y':
/*     */       case 'y':
/* 597 */         return (width > 2) ? LITERAL_YEAR_STRATEGY : ABBREVIATED_YEAR_STRATEGY;
/*     */       case 'X':
/* 599 */         return ISO8601TimeZoneStrategy.getStrategy(width);
/*     */       case 'Z':
/* 601 */         if (width == 2)
/* 602 */           return ISO8601TimeZoneStrategy.ISO_8601_3_STRATEGY;  break;
/*     */       case 'z':
/*     */         break;
/*     */     } 
/* 606 */     return getLocaleSpecificStrategy(15, definingCalendar);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 611 */   private static final ConcurrentMap<Locale, Strategy>[] caches = (ConcurrentMap<Locale, Strategy>[])new ConcurrentMap[17];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ConcurrentMap<Locale, Strategy> getCache(int field) {
/* 619 */     synchronized (caches) {
/* 620 */       if (caches[field] == null) {
/* 621 */         caches[field] = new ConcurrentHashMap<Locale, Strategy>(3);
/*     */       }
/* 623 */       return caches[field];
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Strategy getLocaleSpecificStrategy(int field, Calendar definingCalendar) {
/* 634 */     ConcurrentMap<Locale, Strategy> cache = getCache(field);
/* 635 */     Strategy strategy = cache.get(this.locale);
/* 636 */     if (strategy == null) {
/* 637 */       strategy = (field == 15) ? new TimeZoneStrategy(this.locale) : new CaseInsensitiveTextStrategy(field, definingCalendar, this.locale);
/*     */ 
/*     */       
/* 640 */       Strategy inCache = cache.putIfAbsent(this.locale, strategy);
/* 641 */       if (inCache != null) {
/* 642 */         return inCache;
/*     */       }
/*     */     } 
/* 645 */     return strategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CopyQuotedStrategy
/*     */     extends Strategy
/*     */   {
/*     */     private final String formatField;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     CopyQuotedStrategy(String formatField) {
/* 660 */       this.formatField = formatField;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isNumber() {
/* 668 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth) {
/* 673 */       for (int idx = 0; idx < this.formatField.length(); idx++) {
/* 674 */         int sIdx = idx + pos.getIndex();
/* 675 */         if (sIdx == source.length()) {
/* 676 */           pos.setErrorIndex(sIdx);
/* 677 */           return false;
/*     */         } 
/* 679 */         if (this.formatField.charAt(idx) != source.charAt(sIdx)) {
/* 680 */           pos.setErrorIndex(sIdx);
/* 681 */           return false;
/*     */         } 
/*     */       } 
/* 684 */       pos.setIndex(this.formatField.length() + pos.getIndex());
/* 685 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CaseInsensitiveTextStrategy
/*     */     extends PatternStrategy
/*     */   {
/*     */     private final int field;
/*     */ 
/*     */     
/*     */     final Locale locale;
/*     */ 
/*     */     
/*     */     private final Map<String, Integer> lKeyValues;
/*     */ 
/*     */     
/*     */     CaseInsensitiveTextStrategy(int field, Calendar definingCalendar, Locale locale) {
/* 704 */       this.field = field;
/* 705 */       this.locale = locale;
/*     */       
/* 707 */       StringBuilder regex = new StringBuilder();
/* 708 */       regex.append("((?iu)");
/* 709 */       this.lKeyValues = FastDateParser.appendDisplayNames(definingCalendar, locale, field, regex);
/* 710 */       regex.setLength(regex.length() - 1);
/* 711 */       regex.append(")");
/* 712 */       createPattern(regex);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {
/* 720 */       Integer iVal = this.lKeyValues.get(value.toLowerCase(this.locale));
/* 721 */       cal.set(this.field, iVal.intValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class NumberStrategy
/*     */     extends Strategy
/*     */   {
/*     */     private final int field;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     NumberStrategy(int field) {
/* 737 */       this.field = field;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isNumber() {
/* 745 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth) {
/* 750 */       int idx = pos.getIndex();
/* 751 */       int last = source.length();
/*     */       
/* 753 */       if (maxWidth == 0) {
/*     */         
/* 755 */         for (; idx < last; idx++) {
/* 756 */           char c = source.charAt(idx);
/* 757 */           if (!Character.isWhitespace(c)) {
/*     */             break;
/*     */           }
/*     */         } 
/* 761 */         pos.setIndex(idx);
/*     */       } else {
/* 763 */         int end = idx + maxWidth;
/* 764 */         if (last > end) {
/* 765 */           last = end;
/*     */         }
/*     */       } 
/*     */       
/* 769 */       for (; idx < last; idx++) {
/* 770 */         char c = source.charAt(idx);
/* 771 */         if (!Character.isDigit(c)) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 776 */       if (pos.getIndex() == idx) {
/* 777 */         pos.setErrorIndex(idx);
/* 778 */         return false;
/*     */       } 
/*     */       
/* 781 */       int value = Integer.parseInt(source.substring(pos.getIndex(), idx));
/* 782 */       pos.setIndex(idx);
/*     */       
/* 784 */       calendar.set(this.field, modify(parser, value));
/* 785 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int modify(FastDateParser parser, int iValue) {
/* 795 */       return iValue;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 800 */   private static final Strategy ABBREVIATED_YEAR_STRATEGY = new NumberStrategy(1)
/*     */     {
/*     */ 
/*     */       
/*     */       int modify(FastDateParser parser, int iValue)
/*     */       {
/* 806 */         return (iValue < 100) ? parser.adjustYear(iValue) : iValue;
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   static class TimeZoneStrategy
/*     */     extends PatternStrategy
/*     */   {
/*     */     private static final String RFC_822_TIME_ZONE = "[+-]\\d{4}";
/*     */     
/*     */     private static final String GMT_OPTION = "GMT[+-]\\d{1,2}:\\d{2}";
/*     */     private final Locale locale;
/* 818 */     private final Map<String, TzInfo> tzNames = new HashMap<String, TzInfo>();
/*     */     private static final int ID = 0;
/*     */     
/*     */     private static class TzInfo { TimeZone zone;
/*     */       int dstOffset;
/*     */       
/*     */       TzInfo(TimeZone tz, boolean useDst) {
/* 825 */         this.zone = tz;
/* 826 */         this.dstOffset = useDst ? tz.getDSTSavings() : 0;
/*     */       } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     TimeZoneStrategy(Locale locale) {
/* 840 */       this.locale = locale;
/*     */       
/* 842 */       StringBuilder sb = new StringBuilder();
/* 843 */       sb.append("((?iu)[+-]\\d{4}|GMT[+-]\\d{1,2}:\\d{2}");
/*     */       
/* 845 */       Set<String> sorted = new TreeSet<String>(FastDateParser.LONGER_FIRST_LOWERCASE);
/*     */       
/* 847 */       String[][] zones = DateFormatSymbols.getInstance(locale).getZoneStrings();
/* 848 */       for (String[] zoneNames : zones) {
/*     */         
/* 850 */         String tzId = zoneNames[0];
/* 851 */         if (!tzId.equalsIgnoreCase("GMT")) {
/*     */ 
/*     */           
/* 854 */           TimeZone tz = TimeZone.getTimeZone(tzId);
/*     */ 
/*     */           
/* 857 */           TzInfo standard = new TzInfo(tz, false);
/* 858 */           TzInfo tzInfo = standard;
/* 859 */           for (int i = 1; i < zoneNames.length; i++) {
/* 860 */             switch (i) {
/*     */               
/*     */               case 3:
/* 863 */                 tzInfo = new TzInfo(tz, true);
/*     */                 break;
/*     */               case 5:
/* 866 */                 tzInfo = standard;
/*     */                 break;
/*     */             } 
/* 869 */             String key = zoneNames[i].toLowerCase(locale);
/*     */ 
/*     */             
/* 872 */             if (sorted.add(key)) {
/* 873 */               this.tzNames.put(key, tzInfo);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 879 */       for (String zoneName : sorted) {
/* 880 */         FastDateParser.simpleQuote(sb.append('|'), zoneName);
/*     */       }
/* 882 */       sb.append(")");
/* 883 */       createPattern(sb);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {
/* 891 */       if (value.charAt(0) == '+' || value.charAt(0) == '-') {
/* 892 */         TimeZone tz = TimeZone.getTimeZone("GMT" + value);
/* 893 */         cal.setTimeZone(tz);
/* 894 */       } else if (value.regionMatches(true, 0, "GMT", 0, 3)) {
/* 895 */         TimeZone tz = TimeZone.getTimeZone(value.toUpperCase());
/* 896 */         cal.setTimeZone(tz);
/*     */       } else {
/* 898 */         TzInfo tzInfo = this.tzNames.get(value.toLowerCase(this.locale));
/* 899 */         cal.set(16, tzInfo.dstOffset);
/* 900 */         cal.set(15, tzInfo.zone.getRawOffset());
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ISO8601TimeZoneStrategy
/*     */     extends PatternStrategy
/*     */   {
/*     */     ISO8601TimeZoneStrategy(String pattern) {
/* 913 */       createPattern(pattern);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {
/* 921 */       if (value.equals("Z")) {
/* 922 */         cal.setTimeZone(TimeZone.getTimeZone("UTC"));
/*     */       } else {
/* 924 */         cal.setTimeZone(TimeZone.getTimeZone("GMT" + value));
/*     */       } 
/*     */     }
/*     */     
/* 928 */     private static final FastDateParser.Strategy ISO_8601_1_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}))");
/* 929 */     private static final FastDateParser.Strategy ISO_8601_2_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}\\d{2}))");
/* 930 */     private static final FastDateParser.Strategy ISO_8601_3_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}(?::)\\d{2}))");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static FastDateParser.Strategy getStrategy(int tokenLen) {
/* 940 */       switch (tokenLen) {
/*     */         case 1:
/* 942 */           return ISO_8601_1_STRATEGY;
/*     */         case 2:
/* 944 */           return ISO_8601_2_STRATEGY;
/*     */         case 3:
/* 946 */           return ISO_8601_3_STRATEGY;
/*     */       } 
/* 948 */       throw new IllegalArgumentException("invalid number of X");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 953 */   private static final Strategy NUMBER_MONTH_STRATEGY = new NumberStrategy(2)
/*     */     {
/*     */       int modify(FastDateParser parser, int iValue) {
/* 956 */         return iValue - 1;
/*     */       }
/*     */     };
/* 959 */   private static final Strategy LITERAL_YEAR_STRATEGY = new NumberStrategy(1);
/* 960 */   private static final Strategy WEEK_OF_YEAR_STRATEGY = new NumberStrategy(3);
/* 961 */   private static final Strategy WEEK_OF_MONTH_STRATEGY = new NumberStrategy(4);
/* 962 */   private static final Strategy DAY_OF_YEAR_STRATEGY = new NumberStrategy(6);
/* 963 */   private static final Strategy DAY_OF_MONTH_STRATEGY = new NumberStrategy(5);
/* 964 */   private static final Strategy DAY_OF_WEEK_STRATEGY = new NumberStrategy(7)
/*     */     {
/*     */       int modify(FastDateParser parser, int iValue) {
/* 967 */         return (iValue != 7) ? (iValue + 1) : 1;
/*     */       }
/*     */     };
/* 970 */   private static final Strategy DAY_OF_WEEK_IN_MONTH_STRATEGY = new NumberStrategy(8);
/* 971 */   private static final Strategy HOUR_OF_DAY_STRATEGY = new NumberStrategy(11);
/* 972 */   private static final Strategy HOUR24_OF_DAY_STRATEGY = new NumberStrategy(11)
/*     */     {
/*     */       int modify(FastDateParser parser, int iValue) {
/* 975 */         return (iValue == 24) ? 0 : iValue;
/*     */       }
/*     */     };
/* 978 */   private static final Strategy HOUR12_STRATEGY = new NumberStrategy(10)
/*     */     {
/*     */       int modify(FastDateParser parser, int iValue) {
/* 981 */         return (iValue == 12) ? 0 : iValue;
/*     */       }
/*     */     };
/* 984 */   private static final Strategy HOUR_STRATEGY = new NumberStrategy(10);
/* 985 */   private static final Strategy MINUTE_STRATEGY = new NumberStrategy(12);
/* 986 */   private static final Strategy SECOND_STRATEGY = new NumberStrategy(13);
/* 987 */   private static final Strategy MILLISECOND_STRATEGY = new NumberStrategy(14);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\time\FastDateParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */