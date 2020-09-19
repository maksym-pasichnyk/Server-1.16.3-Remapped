/*     */ package org.apache.commons.lang3.time;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DurationFormatUtils
/*     */ {
/*     */   public static final String ISO_EXTENDED_FORMAT_PATTERN = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.SSS'S'";
/*     */   
/*     */   public static String formatDurationHMS(long durationMillis) {
/*  82 */     return formatDuration(durationMillis, "HH:mm:ss.SSS");
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
/*     */   public static String formatDurationISO(long durationMillis) {
/*  98 */     return formatDuration(durationMillis, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.SSS'S'", false);
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
/*     */   public static String formatDuration(long durationMillis, String format) {
/* 113 */     return formatDuration(durationMillis, format, true);
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
/*     */   public static String formatDuration(long durationMillis, String format, boolean padWithZeros) {
/* 130 */     Validate.inclusiveBetween(0L, Long.MAX_VALUE, durationMillis, "durationMillis must not be negative");
/*     */     
/* 132 */     Token[] tokens = lexx(format);
/*     */     
/* 134 */     long days = 0L;
/* 135 */     long hours = 0L;
/* 136 */     long minutes = 0L;
/* 137 */     long seconds = 0L;
/* 138 */     long milliseconds = durationMillis;
/*     */     
/* 140 */     if (Token.containsTokenWithValue(tokens, d)) {
/* 141 */       days = milliseconds / 86400000L;
/* 142 */       milliseconds -= days * 86400000L;
/*     */     } 
/* 144 */     if (Token.containsTokenWithValue(tokens, H)) {
/* 145 */       hours = milliseconds / 3600000L;
/* 146 */       milliseconds -= hours * 3600000L;
/*     */     } 
/* 148 */     if (Token.containsTokenWithValue(tokens, m)) {
/* 149 */       minutes = milliseconds / 60000L;
/* 150 */       milliseconds -= minutes * 60000L;
/*     */     } 
/* 152 */     if (Token.containsTokenWithValue(tokens, s)) {
/* 153 */       seconds = milliseconds / 1000L;
/* 154 */       milliseconds -= seconds * 1000L;
/*     */     } 
/*     */     
/* 157 */     return format(tokens, 0L, 0L, days, hours, minutes, seconds, milliseconds, padWithZeros);
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
/*     */   public static String formatDurationWords(long durationMillis, boolean suppressLeadingZeroElements, boolean suppressTrailingZeroElements) {
/* 180 */     String duration = formatDuration(durationMillis, "d' days 'H' hours 'm' minutes 's' seconds'");
/* 181 */     if (suppressLeadingZeroElements) {
/*     */       
/* 183 */       duration = " " + duration;
/* 184 */       String tmp = StringUtils.replaceOnce(duration, " 0 days", "");
/* 185 */       if (tmp.length() != duration.length()) {
/* 186 */         duration = tmp;
/* 187 */         tmp = StringUtils.replaceOnce(duration, " 0 hours", "");
/* 188 */         if (tmp.length() != duration.length()) {
/* 189 */           duration = tmp;
/* 190 */           tmp = StringUtils.replaceOnce(duration, " 0 minutes", "");
/* 191 */           duration = tmp;
/* 192 */           if (tmp.length() != duration.length()) {
/* 193 */             duration = StringUtils.replaceOnce(tmp, " 0 seconds", "");
/*     */           }
/*     */         } 
/*     */       } 
/* 197 */       if (duration.length() != 0)
/*     */       {
/* 199 */         duration = duration.substring(1);
/*     */       }
/*     */     } 
/* 202 */     if (suppressTrailingZeroElements) {
/* 203 */       String tmp = StringUtils.replaceOnce(duration, " 0 seconds", "");
/* 204 */       if (tmp.length() != duration.length()) {
/* 205 */         duration = tmp;
/* 206 */         tmp = StringUtils.replaceOnce(duration, " 0 minutes", "");
/* 207 */         if (tmp.length() != duration.length()) {
/* 208 */           duration = tmp;
/* 209 */           tmp = StringUtils.replaceOnce(duration, " 0 hours", "");
/* 210 */           if (tmp.length() != duration.length()) {
/* 211 */             duration = StringUtils.replaceOnce(tmp, " 0 days", "");
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 217 */     duration = " " + duration;
/* 218 */     duration = StringUtils.replaceOnce(duration, " 1 seconds", " 1 second");
/* 219 */     duration = StringUtils.replaceOnce(duration, " 1 minutes", " 1 minute");
/* 220 */     duration = StringUtils.replaceOnce(duration, " 1 hours", " 1 hour");
/* 221 */     duration = StringUtils.replaceOnce(duration, " 1 days", " 1 day");
/* 222 */     return duration.trim();
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
/*     */   public static String formatPeriodISO(long startMillis, long endMillis) {
/* 237 */     return formatPeriod(startMillis, endMillis, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.SSS'S'", false, TimeZone.getDefault());
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
/*     */   public static String formatPeriod(long startMillis, long endMillis, String format) {
/* 251 */     return formatPeriod(startMillis, endMillis, format, true, TimeZone.getDefault());
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static String formatPeriod(long startMillis, long endMillis, String format, boolean padWithZeros, TimeZone timezone) {
/* 280 */     Validate.isTrue((startMillis <= endMillis), "startMillis must not be greater than endMillis", new Object[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 289 */     Token[] tokens = lexx(format);
/*     */ 
/*     */ 
/*     */     
/* 293 */     Calendar start = Calendar.getInstance(timezone);
/* 294 */     start.setTime(new Date(startMillis));
/* 295 */     Calendar end = Calendar.getInstance(timezone);
/* 296 */     end.setTime(new Date(endMillis));
/*     */ 
/*     */     
/* 299 */     int milliseconds = end.get(14) - start.get(14);
/* 300 */     int seconds = end.get(13) - start.get(13);
/* 301 */     int minutes = end.get(12) - start.get(12);
/* 302 */     int hours = end.get(11) - start.get(11);
/* 303 */     int days = end.get(5) - start.get(5);
/* 304 */     int months = end.get(2) - start.get(2);
/* 305 */     int years = end.get(1) - start.get(1);
/*     */ 
/*     */     
/* 308 */     while (milliseconds < 0) {
/* 309 */       milliseconds += 1000;
/* 310 */       seconds--;
/*     */     } 
/* 312 */     while (seconds < 0) {
/* 313 */       seconds += 60;
/* 314 */       minutes--;
/*     */     } 
/* 316 */     while (minutes < 0) {
/* 317 */       minutes += 60;
/* 318 */       hours--;
/*     */     } 
/* 320 */     while (hours < 0) {
/* 321 */       hours += 24;
/* 322 */       days--;
/*     */     } 
/*     */     
/* 325 */     if (Token.containsTokenWithValue(tokens, M)) {
/* 326 */       while (days < 0) {
/* 327 */         days += start.getActualMaximum(5);
/* 328 */         months--;
/* 329 */         start.add(2, 1);
/*     */       } 
/*     */       
/* 332 */       while (months < 0) {
/* 333 */         months += 12;
/* 334 */         years--;
/*     */       } 
/*     */       
/* 337 */       if (!Token.containsTokenWithValue(tokens, y) && years != 0) {
/* 338 */         while (years != 0) {
/* 339 */           months += 12 * years;
/* 340 */           years = 0;
/*     */         }
/*     */       
/*     */       }
/*     */     } else {
/*     */       
/* 346 */       if (!Token.containsTokenWithValue(tokens, y)) {
/* 347 */         int target = end.get(1);
/* 348 */         if (months < 0)
/*     */         {
/* 350 */           target--;
/*     */         }
/*     */         
/* 353 */         while (start.get(1) != target) {
/* 354 */           days += start.getActualMaximum(6) - start.get(6);
/*     */ 
/*     */           
/* 357 */           if (start instanceof java.util.GregorianCalendar && start
/* 358 */             .get(2) == 1 && start
/* 359 */             .get(5) == 29) {
/* 360 */             days++;
/*     */           }
/*     */           
/* 363 */           start.add(1, 1);
/*     */           
/* 365 */           days += start.get(6);
/*     */         } 
/*     */         
/* 368 */         years = 0;
/*     */       } 
/*     */       
/* 371 */       while (start.get(2) != end.get(2)) {
/* 372 */         days += start.getActualMaximum(5);
/* 373 */         start.add(2, 1);
/*     */       } 
/*     */       
/* 376 */       months = 0;
/*     */       
/* 378 */       while (days < 0) {
/* 379 */         days += start.getActualMaximum(5);
/* 380 */         months--;
/* 381 */         start.add(2, 1);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 390 */     if (!Token.containsTokenWithValue(tokens, d)) {
/* 391 */       hours += 24 * days;
/* 392 */       days = 0;
/*     */     } 
/* 394 */     if (!Token.containsTokenWithValue(tokens, H)) {
/* 395 */       minutes += 60 * hours;
/* 396 */       hours = 0;
/*     */     } 
/* 398 */     if (!Token.containsTokenWithValue(tokens, m)) {
/* 399 */       seconds += 60 * minutes;
/* 400 */       minutes = 0;
/*     */     } 
/* 402 */     if (!Token.containsTokenWithValue(tokens, s)) {
/* 403 */       milliseconds += 1000 * seconds;
/* 404 */       seconds = 0;
/*     */     } 
/*     */     
/* 407 */     return format(tokens, years, months, days, hours, minutes, seconds, milliseconds, padWithZeros);
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
/*     */   static String format(Token[] tokens, long years, long months, long days, long hours, long minutes, long seconds, long milliseconds, boolean padWithZeros) {
/* 427 */     StringBuilder buffer = new StringBuilder();
/* 428 */     boolean lastOutputSeconds = false;
/* 429 */     for (Token token : tokens) {
/* 430 */       Object value = token.getValue();
/* 431 */       int count = token.getCount();
/* 432 */       if (value instanceof StringBuilder) {
/* 433 */         buffer.append(value.toString());
/*     */       }
/* 435 */       else if (value.equals(y)) {
/* 436 */         buffer.append(paddedValue(years, padWithZeros, count));
/* 437 */         lastOutputSeconds = false;
/* 438 */       } else if (value.equals(M)) {
/* 439 */         buffer.append(paddedValue(months, padWithZeros, count));
/* 440 */         lastOutputSeconds = false;
/* 441 */       } else if (value.equals(d)) {
/* 442 */         buffer.append(paddedValue(days, padWithZeros, count));
/* 443 */         lastOutputSeconds = false;
/* 444 */       } else if (value.equals(H)) {
/* 445 */         buffer.append(paddedValue(hours, padWithZeros, count));
/* 446 */         lastOutputSeconds = false;
/* 447 */       } else if (value.equals(m)) {
/* 448 */         buffer.append(paddedValue(minutes, padWithZeros, count));
/* 449 */         lastOutputSeconds = false;
/* 450 */       } else if (value.equals(s)) {
/* 451 */         buffer.append(paddedValue(seconds, padWithZeros, count));
/* 452 */         lastOutputSeconds = true;
/* 453 */       } else if (value.equals(S)) {
/* 454 */         if (lastOutputSeconds) {
/*     */           
/* 456 */           int width = padWithZeros ? Math.max(3, count) : 3;
/* 457 */           buffer.append(paddedValue(milliseconds, true, width));
/*     */         } else {
/* 459 */           buffer.append(paddedValue(milliseconds, padWithZeros, count));
/*     */         } 
/* 461 */         lastOutputSeconds = false;
/*     */       } 
/*     */     } 
/*     */     
/* 465 */     return buffer.toString();
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
/*     */   private static String paddedValue(long value, boolean padWithZeros, int count) {
/* 478 */     String longString = Long.toString(value);
/* 479 */     return padWithZeros ? StringUtils.leftPad(longString, count, '0') : longString;
/*     */   }
/*     */   
/* 482 */   static final Object y = "y";
/* 483 */   static final Object M = "M";
/* 484 */   static final Object d = "d";
/* 485 */   static final Object H = "H";
/* 486 */   static final Object m = "m";
/* 487 */   static final Object s = "s";
/* 488 */   static final Object S = "S";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Token[] lexx(String format) {
/* 497 */     ArrayList<Token> list = new ArrayList<Token>(format.length());
/*     */     
/* 499 */     boolean inLiteral = false;
/*     */ 
/*     */     
/* 502 */     StringBuilder buffer = null;
/* 503 */     Token previous = null;
/* 504 */     for (int i = 0; i < format.length(); i++) {
/* 505 */       char ch = format.charAt(i);
/* 506 */       if (inLiteral && ch != '\'') {
/* 507 */         buffer.append(ch);
/*     */       } else {
/*     */         
/* 510 */         Object value = null;
/* 511 */         switch (ch) {
/*     */           
/*     */           case '\'':
/* 514 */             if (inLiteral) {
/* 515 */               buffer = null;
/* 516 */               inLiteral = false; break;
/*     */             } 
/* 518 */             buffer = new StringBuilder();
/* 519 */             list.add(new Token(buffer));
/* 520 */             inLiteral = true;
/*     */             break;
/*     */           
/*     */           case 'y':
/* 524 */             value = y;
/*     */             break;
/*     */           case 'M':
/* 527 */             value = M;
/*     */             break;
/*     */           case 'd':
/* 530 */             value = d;
/*     */             break;
/*     */           case 'H':
/* 533 */             value = H;
/*     */             break;
/*     */           case 'm':
/* 536 */             value = m;
/*     */             break;
/*     */           case 's':
/* 539 */             value = s;
/*     */             break;
/*     */           case 'S':
/* 542 */             value = S;
/*     */             break;
/*     */           default:
/* 545 */             if (buffer == null) {
/* 546 */               buffer = new StringBuilder();
/* 547 */               list.add(new Token(buffer));
/*     */             } 
/* 549 */             buffer.append(ch);
/*     */             break;
/*     */         } 
/* 552 */         if (value != null) {
/* 553 */           if (previous != null && previous.getValue().equals(value)) {
/* 554 */             previous.increment();
/*     */           } else {
/* 556 */             Token token = new Token(value);
/* 557 */             list.add(token);
/* 558 */             previous = token;
/*     */           } 
/* 560 */           buffer = null;
/*     */         } 
/*     */       } 
/* 563 */     }  if (inLiteral) {
/* 564 */       throw new IllegalArgumentException("Unmatched quote in format: " + format);
/*     */     }
/* 566 */     return list.<Token>toArray(new Token[list.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Token
/*     */   {
/*     */     private final Object value;
/*     */ 
/*     */ 
/*     */     
/*     */     private int count;
/*     */ 
/*     */ 
/*     */     
/*     */     static boolean containsTokenWithValue(Token[] tokens, Object value) {
/* 583 */       for (Token token : tokens) {
/* 584 */         if (token.getValue() == value) {
/* 585 */           return true;
/*     */         }
/*     */       } 
/* 588 */       return false;
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
/*     */     Token(Object value) {
/* 600 */       this.value = value;
/* 601 */       this.count = 1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Token(Object value, int count) {
/* 612 */       this.value = value;
/* 613 */       this.count = count;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void increment() {
/* 620 */       this.count++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int getCount() {
/* 629 */       return this.count;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Object getValue() {
/* 638 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj2) {
/* 649 */       if (obj2 instanceof Token) {
/* 650 */         Token tok2 = (Token)obj2;
/* 651 */         if (this.value.getClass() != tok2.value.getClass()) {
/* 652 */           return false;
/*     */         }
/* 654 */         if (this.count != tok2.count) {
/* 655 */           return false;
/*     */         }
/* 657 */         if (this.value instanceof StringBuilder)
/* 658 */           return this.value.toString().equals(tok2.value.toString()); 
/* 659 */         if (this.value instanceof Number) {
/* 660 */           return this.value.equals(tok2.value);
/*     */         }
/* 662 */         return (this.value == tok2.value);
/*     */       } 
/*     */       
/* 665 */       return false;
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
/*     */     public int hashCode() {
/* 677 */       return this.value.hashCode();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 687 */       return StringUtils.repeat(this.value.toString(), this.count);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\time\DurationFormatUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */