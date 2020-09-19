/*     */ package org.apache.logging.log4j.core.util.datetime;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FastDateParser
/*     */   implements DateParser, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3L;
/*  86 */   static final Locale JAPANESE_IMPERIAL = new Locale("ja", "JP", "JP");
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
/* 101 */   private static final Comparator<String> LONGER_FIRST_LOWERCASE = new Comparator<String>()
/*     */     {
/*     */       public int compare(String left, String right) {
/* 104 */         return right.compareTo(left);
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
/* 120 */     this(pattern, timeZone, locale, null);
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
/* 135 */     this.pattern = pattern;
/* 136 */     this.timeZone = timeZone;
/* 137 */     this.locale = locale;
/*     */     
/* 139 */     Calendar definingCalendar = Calendar.getInstance(timeZone, locale);
/*     */ 
/*     */     
/* 142 */     if (centuryStart != null) {
/* 143 */       definingCalendar.setTime(centuryStart);
/* 144 */       centuryStartYear = definingCalendar.get(1);
/*     */     }
/* 146 */     else if (locale.equals(JAPANESE_IMPERIAL)) {
/* 147 */       centuryStartYear = 0;
/*     */     }
/*     */     else {
/*     */       
/* 151 */       definingCalendar.setTime(new Date());
/* 152 */       centuryStartYear = definingCalendar.get(1) - 80;
/*     */     } 
/* 154 */     this.century = centuryStartYear / 100 * 100;
/* 155 */     this.startYear = centuryStartYear - this.century;
/*     */     
/* 157 */     init(definingCalendar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(Calendar definingCalendar) {
/* 167 */     this.patterns = new ArrayList<>();
/*     */     
/* 169 */     StrategyParser fm = new StrategyParser(definingCalendar);
/*     */     while (true) {
/* 171 */       StrategyAndWidth field = fm.getNextStrategy();
/* 172 */       if (field == null) {
/*     */         break;
/*     */       }
/* 175 */       this.patterns.add(field);
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
/* 190 */       this.strategy = strategy;
/* 191 */       this.width = width;
/*     */     }
/*     */     
/*     */     int getMaxWidth(ListIterator<StrategyAndWidth> lt) {
/* 195 */       if (!this.strategy.isNumber() || !lt.hasNext()) {
/* 196 */         return 0;
/*     */       }
/* 198 */       FastDateParser.Strategy nextStrategy = ((StrategyAndWidth)lt.next()).strategy;
/* 199 */       lt.previous();
/* 200 */       return nextStrategy.isNumber() ? this.width : 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class StrategyParser
/*     */   {
/*     */     private final Calendar definingCalendar;
/*     */     
/*     */     private int currentIdx;
/*     */     
/*     */     StrategyParser(Calendar definingCalendar) {
/* 212 */       this.definingCalendar = definingCalendar;
/*     */     }
/*     */     
/*     */     FastDateParser.StrategyAndWidth getNextStrategy() {
/* 216 */       if (this.currentIdx >= FastDateParser.this.pattern.length()) {
/* 217 */         return null;
/*     */       }
/*     */       
/* 220 */       char c = FastDateParser.this.pattern.charAt(this.currentIdx);
/* 221 */       if (FastDateParser.isFormatLetter(c)) {
/* 222 */         return letterPattern(c);
/*     */       }
/* 224 */       return literal();
/*     */     }
/*     */     
/*     */     private FastDateParser.StrategyAndWidth letterPattern(char c) {
/* 228 */       int begin = this.currentIdx; do {  }
/* 229 */       while (++this.currentIdx < FastDateParser.this.pattern.length() && 
/* 230 */         FastDateParser.this.pattern.charAt(this.currentIdx) == c);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 235 */       int width = this.currentIdx - begin;
/* 236 */       return new FastDateParser.StrategyAndWidth(FastDateParser.this.getStrategy(c, width, this.definingCalendar), width);
/*     */     }
/*     */     
/*     */     private FastDateParser.StrategyAndWidth literal() {
/* 240 */       boolean activeQuote = false;
/*     */       
/* 242 */       StringBuilder sb = new StringBuilder();
/* 243 */       while (this.currentIdx < FastDateParser.this.pattern.length()) {
/* 244 */         char c = FastDateParser.this.pattern.charAt(this.currentIdx);
/* 245 */         if (!activeQuote && FastDateParser.isFormatLetter(c))
/*     */           break; 
/* 247 */         if (c == '\'' && (++this.currentIdx == FastDateParser.this.pattern.length() || FastDateParser.this.pattern.charAt(this.currentIdx) != '\'')) {
/* 248 */           activeQuote = !activeQuote;
/*     */           continue;
/*     */         } 
/* 251 */         this.currentIdx++;
/* 252 */         sb.append(c);
/*     */       } 
/*     */       
/* 255 */       if (activeQuote) {
/* 256 */         throw new IllegalArgumentException("Unterminated quote");
/*     */       }
/*     */       
/* 259 */       String formatField = sb.toString();
/* 260 */       return new FastDateParser.StrategyAndWidth(new FastDateParser.CopyQuotedStrategy(formatField), formatField.length());
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean isFormatLetter(char c) {
/* 265 */     return ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPattern() {
/* 275 */     return this.pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 283 */     return this.timeZone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 291 */     return this.locale;
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
/* 305 */     if (!(obj instanceof FastDateParser)) {
/* 306 */       return false;
/*     */     }
/* 308 */     FastDateParser other = (FastDateParser)obj;
/* 309 */     return (this.pattern.equals(other.pattern) && this.timeZone.equals(other.timeZone) && this.locale.equals(other.locale));
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
/*     */   public int hashCode() {
/* 321 */     return this.pattern.hashCode() + 13 * (this.timeZone.hashCode() + 13 * this.locale.hashCode());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 331 */     return "FastDateParser[" + this.pattern + "," + this.locale + "," + this.timeZone.getID() + "]";
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
/* 345 */     in.defaultReadObject();
/*     */     
/* 347 */     Calendar definingCalendar = Calendar.getInstance(this.timeZone, this.locale);
/* 348 */     init(definingCalendar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object parseObject(String source) throws ParseException {
/* 356 */     return parse(source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date parse(String source) throws ParseException {
/* 364 */     ParsePosition pp = new ParsePosition(0);
/* 365 */     Date date = parse(source, pp);
/* 366 */     if (date == null) {
/*     */       
/* 368 */       if (this.locale.equals(JAPANESE_IMPERIAL)) {
/* 369 */         throw new ParseException("(The " + this.locale + " locale does not support dates before 1868 AD)\n" + "Unparseable date: \"" + source, pp.getErrorIndex());
/*     */       }
/*     */ 
/*     */       
/* 373 */       throw new ParseException("Unparseable date: " + source, pp.getErrorIndex());
/*     */     } 
/* 375 */     return date;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object parseObject(String source, ParsePosition pos) {
/* 383 */     return parse(source, pos);
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
/* 401 */     Calendar cal = Calendar.getInstance(this.timeZone, this.locale);
/* 402 */     cal.clear();
/*     */     
/* 404 */     return parse(source, pos, cal) ? cal.getTime() : null;
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
/* 422 */     ListIterator<StrategyAndWidth> lt = this.patterns.listIterator();
/* 423 */     while (lt.hasNext()) {
/* 424 */       StrategyAndWidth strategyAndWidth = lt.next();
/* 425 */       int maxWidth = strategyAndWidth.getMaxWidth(lt);
/* 426 */       if (!strategyAndWidth.strategy.parse(this, calendar, source, pos, maxWidth)) {
/* 427 */         return false;
/*     */       }
/*     */     } 
/* 430 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static StringBuilder simpleQuote(StringBuilder sb, String value) {
/* 437 */     for (int i = 0; i < value.length(); i++) {
/* 438 */       char c = value.charAt(i);
/* 439 */       switch (c) {
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
/* 452 */           sb.append('\\'); break;
/*     */       } 
/* 454 */       sb.append(c);
/*     */     } 
/*     */     
/* 457 */     return sb;
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
/* 469 */     Map<String, Integer> values = new HashMap<>();
/*     */     
/* 471 */     Map<String, Integer> displayNames = cal.getDisplayNames(field, 0, locale);
/* 472 */     TreeSet<String> sorted = new TreeSet<>(LONGER_FIRST_LOWERCASE);
/* 473 */     for (Map.Entry<String, Integer> displayName : displayNames.entrySet()) {
/* 474 */       String key = ((String)displayName.getKey()).toLowerCase(locale);
/* 475 */       if (sorted.add(key)) {
/* 476 */         values.put(key, displayName.getValue());
/*     */       }
/*     */     } 
/* 479 */     for (String symbol : sorted) {
/* 480 */       simpleQuote(regex, symbol).append('|');
/*     */     }
/* 482 */     return values;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int adjustYear(int twoDigitYear) {
/* 491 */     int trial = this.century + twoDigitYear;
/* 492 */     return (twoDigitYear >= this.startYear) ? trial : (trial + 100);
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
/* 506 */       return false;
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
/* 520 */       createPattern(regex.toString());
/*     */     }
/*     */     
/*     */     void createPattern(String regex) {
/* 524 */       this.pattern = Pattern.compile(regex);
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
/* 535 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth) {
/* 540 */       Matcher matcher = this.pattern.matcher(source.substring(pos.getIndex()));
/* 541 */       if (!matcher.lookingAt()) {
/* 542 */         pos.setErrorIndex(pos.getIndex());
/* 543 */         return false;
/*     */       } 
/* 545 */       pos.setIndex(pos.getIndex() + matcher.end(1));
/* 546 */       setCalendar(parser, calendar, matcher.group(1));
/* 547 */       return true;
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
/* 560 */     switch (f) {
/*     */       default:
/* 562 */         throw new IllegalArgumentException("Format '" + f + "' not supported");
/*     */       case 'D':
/* 564 */         return DAY_OF_YEAR_STRATEGY;
/*     */       case 'E':
/* 566 */         return getLocaleSpecificStrategy(7, definingCalendar);
/*     */       case 'F':
/* 568 */         return DAY_OF_WEEK_IN_MONTH_STRATEGY;
/*     */       case 'G':
/* 570 */         return getLocaleSpecificStrategy(0, definingCalendar);
/*     */       case 'H':
/* 572 */         return HOUR_OF_DAY_STRATEGY;
/*     */       case 'K':
/* 574 */         return HOUR_STRATEGY;
/*     */       case 'M':
/* 576 */         return (width >= 3) ? getLocaleSpecificStrategy(2, definingCalendar) : NUMBER_MONTH_STRATEGY;
/*     */       case 'S':
/* 578 */         return MILLISECOND_STRATEGY;
/*     */       case 'W':
/* 580 */         return WEEK_OF_MONTH_STRATEGY;
/*     */       case 'a':
/* 582 */         return getLocaleSpecificStrategy(9, definingCalendar);
/*     */       case 'd':
/* 584 */         return DAY_OF_MONTH_STRATEGY;
/*     */       case 'h':
/* 586 */         return HOUR12_STRATEGY;
/*     */       case 'k':
/* 588 */         return HOUR24_OF_DAY_STRATEGY;
/*     */       case 'm':
/* 590 */         return MINUTE_STRATEGY;
/*     */       case 's':
/* 592 */         return SECOND_STRATEGY;
/*     */       case 'u':
/* 594 */         return DAY_OF_WEEK_STRATEGY;
/*     */       case 'w':
/* 596 */         return WEEK_OF_YEAR_STRATEGY;
/*     */       case 'Y':
/*     */       case 'y':
/* 599 */         return (width > 2) ? LITERAL_YEAR_STRATEGY : ABBREVIATED_YEAR_STRATEGY;
/*     */       case 'X':
/* 601 */         return ISO8601TimeZoneStrategy.getStrategy(width);
/*     */       case 'Z':
/* 603 */         if (width == 2)
/* 604 */           return ISO8601TimeZoneStrategy.ISO_8601_3_STRATEGY;  break;
/*     */       case 'z':
/*     */         break;
/*     */     } 
/* 608 */     return getLocaleSpecificStrategy(15, definingCalendar);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 613 */   private static final ConcurrentMap<Locale, Strategy>[] caches = (ConcurrentMap<Locale, Strategy>[])new ConcurrentMap[17];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ConcurrentMap<Locale, Strategy> getCache(int field) {
/* 621 */     synchronized (caches) {
/* 622 */       if (caches[field] == null) {
/* 623 */         caches[field] = new ConcurrentHashMap<>(3);
/*     */       }
/* 625 */       return caches[field];
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
/* 636 */     ConcurrentMap<Locale, Strategy> cache = getCache(field);
/* 637 */     Strategy strategy = cache.get(this.locale);
/* 638 */     if (strategy == null) {
/* 639 */       strategy = (field == 15) ? new TimeZoneStrategy(this.locale) : new CaseInsensitiveTextStrategy(field, definingCalendar, this.locale);
/*     */ 
/*     */       
/* 642 */       Strategy inCache = cache.putIfAbsent(this.locale, strategy);
/* 643 */       if (inCache != null) {
/* 644 */         return inCache;
/*     */       }
/*     */     } 
/* 647 */     return strategy;
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
/* 662 */       this.formatField = formatField;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isNumber() {
/* 670 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth) {
/* 675 */       for (int idx = 0; idx < this.formatField.length(); idx++) {
/* 676 */         int sIdx = idx + pos.getIndex();
/* 677 */         if (sIdx == source.length()) {
/* 678 */           pos.setErrorIndex(sIdx);
/* 679 */           return false;
/*     */         } 
/* 681 */         if (this.formatField.charAt(idx) != source.charAt(sIdx)) {
/* 682 */           pos.setErrorIndex(sIdx);
/* 683 */           return false;
/*     */         } 
/*     */       } 
/* 686 */       pos.setIndex(this.formatField.length() + pos.getIndex());
/* 687 */       return true;
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
/* 706 */       this.field = field;
/* 707 */       this.locale = locale;
/*     */       
/* 709 */       StringBuilder regex = new StringBuilder();
/* 710 */       regex.append("((?iu)");
/* 711 */       this.lKeyValues = FastDateParser.appendDisplayNames(definingCalendar, locale, field, regex);
/* 712 */       regex.setLength(regex.length() - 1);
/* 713 */       regex.append(")");
/* 714 */       createPattern(regex);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {
/* 722 */       Integer iVal = this.lKeyValues.get(value.toLowerCase(this.locale));
/* 723 */       cal.set(this.field, iVal.intValue());
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
/* 739 */       this.field = field;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isNumber() {
/* 747 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth) {
/* 752 */       int idx = pos.getIndex();
/* 753 */       int last = source.length();
/*     */       
/* 755 */       if (maxWidth == 0) {
/*     */         
/* 757 */         for (; idx < last; idx++) {
/* 758 */           char c = source.charAt(idx);
/* 759 */           if (!Character.isWhitespace(c)) {
/*     */             break;
/*     */           }
/*     */         } 
/* 763 */         pos.setIndex(idx);
/*     */       } else {
/* 765 */         int end = idx + maxWidth;
/* 766 */         if (last > end) {
/* 767 */           last = end;
/*     */         }
/*     */       } 
/*     */       
/* 771 */       for (; idx < last; idx++) {
/* 772 */         char c = source.charAt(idx);
/* 773 */         if (!Character.isDigit(c)) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 778 */       if (pos.getIndex() == idx) {
/* 779 */         pos.setErrorIndex(idx);
/* 780 */         return false;
/*     */       } 
/*     */       
/* 783 */       int value = Integer.parseInt(source.substring(pos.getIndex(), idx));
/* 784 */       pos.setIndex(idx);
/*     */       
/* 786 */       calendar.set(this.field, modify(parser, value));
/* 787 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int modify(FastDateParser parser, int iValue) {
/* 797 */       return iValue;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 802 */   private static final Strategy ABBREVIATED_YEAR_STRATEGY = new NumberStrategy(1)
/*     */     {
/*     */ 
/*     */       
/*     */       int modify(FastDateParser parser, int iValue)
/*     */       {
/* 808 */         return (iValue < 100) ? parser.adjustYear(iValue) : iValue;
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
/* 820 */     private final Map<String, TzInfo> tzNames = new HashMap<>();
/*     */     private static final int ID = 0;
/*     */     
/*     */     private static class TzInfo { TimeZone zone;
/*     */       int dstOffset;
/*     */       
/*     */       TzInfo(TimeZone tz, boolean useDst) {
/* 827 */         this.zone = tz;
/* 828 */         this.dstOffset = useDst ? tz.getDSTSavings() : 0;
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
/* 842 */       this.locale = locale;
/*     */       
/* 844 */       StringBuilder sb = new StringBuilder();
/* 845 */       sb.append("((?iu)[+-]\\d{4}|GMT[+-]\\d{1,2}:\\d{2}");
/*     */       
/* 847 */       Set<String> sorted = new TreeSet<>(FastDateParser.LONGER_FIRST_LOWERCASE);
/*     */       
/* 849 */       String[][] zones = DateFormatSymbols.getInstance(locale).getZoneStrings();
/* 850 */       for (String[] zoneNames : zones) {
/*     */         
/* 852 */         String tzId = zoneNames[0];
/* 853 */         if (!tzId.equalsIgnoreCase("GMT")) {
/*     */ 
/*     */           
/* 856 */           TimeZone tz = TimeZone.getTimeZone(tzId);
/*     */ 
/*     */           
/* 859 */           TzInfo standard = new TzInfo(tz, false);
/* 860 */           TzInfo tzInfo = standard;
/* 861 */           for (int i = 1; i < zoneNames.length; i++) {
/* 862 */             switch (i) {
/*     */               
/*     */               case 3:
/* 865 */                 tzInfo = new TzInfo(tz, true);
/*     */                 break;
/*     */               case 5:
/* 868 */                 tzInfo = standard;
/*     */                 break;
/*     */             } 
/* 871 */             if (zoneNames[i] != null) {
/* 872 */               String key = zoneNames[i].toLowerCase(locale);
/*     */ 
/*     */               
/* 875 */               if (sorted.add(key)) {
/* 876 */                 this.tzNames.put(key, tzInfo);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 883 */       for (String zoneName : sorted) {
/* 884 */         FastDateParser.simpleQuote(sb.append('|'), zoneName);
/*     */       }
/* 886 */       sb.append(")");
/* 887 */       createPattern(sb);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {
/* 895 */       if (value.charAt(0) == '+' || value.charAt(0) == '-') {
/* 896 */         TimeZone tz = TimeZone.getTimeZone("GMT" + value);
/* 897 */         cal.setTimeZone(tz);
/* 898 */       } else if (value.regionMatches(true, 0, "GMT", 0, 3)) {
/* 899 */         TimeZone tz = TimeZone.getTimeZone(value.toUpperCase());
/* 900 */         cal.setTimeZone(tz);
/*     */       } else {
/* 902 */         TzInfo tzInfo = this.tzNames.get(value.toLowerCase(this.locale));
/* 903 */         cal.set(16, tzInfo.dstOffset);
/* 904 */         cal.set(15, tzInfo.zone.getRawOffset());
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
/* 917 */       createPattern(pattern);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {
/* 925 */       if (value.equals("Z")) {
/* 926 */         cal.setTimeZone(TimeZone.getTimeZone("UTC"));
/*     */       } else {
/* 928 */         cal.setTimeZone(TimeZone.getTimeZone("GMT" + value));
/*     */       } 
/*     */     }
/*     */     
/* 932 */     private static final FastDateParser.Strategy ISO_8601_1_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}))");
/* 933 */     private static final FastDateParser.Strategy ISO_8601_2_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}\\d{2}))");
/* 934 */     private static final FastDateParser.Strategy ISO_8601_3_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}(?::)\\d{2}))");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static FastDateParser.Strategy getStrategy(int tokenLen) {
/* 944 */       switch (tokenLen) {
/*     */         case 1:
/* 946 */           return ISO_8601_1_STRATEGY;
/*     */         case 2:
/* 948 */           return ISO_8601_2_STRATEGY;
/*     */         case 3:
/* 950 */           return ISO_8601_3_STRATEGY;
/*     */       } 
/* 952 */       throw new IllegalArgumentException("invalid number of X");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 957 */   private static final Strategy NUMBER_MONTH_STRATEGY = new NumberStrategy(2)
/*     */     {
/*     */       int modify(FastDateParser parser, int iValue) {
/* 960 */         return iValue - 1;
/*     */       }
/*     */     };
/* 963 */   private static final Strategy LITERAL_YEAR_STRATEGY = new NumberStrategy(1);
/* 964 */   private static final Strategy WEEK_OF_YEAR_STRATEGY = new NumberStrategy(3);
/* 965 */   private static final Strategy WEEK_OF_MONTH_STRATEGY = new NumberStrategy(4);
/* 966 */   private static final Strategy DAY_OF_YEAR_STRATEGY = new NumberStrategy(6);
/* 967 */   private static final Strategy DAY_OF_MONTH_STRATEGY = new NumberStrategy(5);
/* 968 */   private static final Strategy DAY_OF_WEEK_STRATEGY = new NumberStrategy(7)
/*     */     {
/*     */       int modify(FastDateParser parser, int iValue) {
/* 971 */         return (iValue != 7) ? (iValue + 1) : 1;
/*     */       }
/*     */     };
/* 974 */   private static final Strategy DAY_OF_WEEK_IN_MONTH_STRATEGY = new NumberStrategy(8);
/* 975 */   private static final Strategy HOUR_OF_DAY_STRATEGY = new NumberStrategy(11);
/* 976 */   private static final Strategy HOUR24_OF_DAY_STRATEGY = new NumberStrategy(11)
/*     */     {
/*     */       int modify(FastDateParser parser, int iValue) {
/* 979 */         return (iValue == 24) ? 0 : iValue;
/*     */       }
/*     */     };
/* 982 */   private static final Strategy HOUR12_STRATEGY = new NumberStrategy(10)
/*     */     {
/*     */       int modify(FastDateParser parser, int iValue) {
/* 985 */         return (iValue == 12) ? 0 : iValue;
/*     */       }
/*     */     };
/* 988 */   private static final Strategy HOUR_STRATEGY = new NumberStrategy(10);
/* 989 */   private static final Strategy MINUTE_STRATEGY = new NumberStrategy(12);
/* 990 */   private static final Strategy SECOND_STRATEGY = new NumberStrategy(13);
/* 991 */   private static final Strategy MILLISECOND_STRATEGY = new NumberStrategy(14);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\datetime\FastDateParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */