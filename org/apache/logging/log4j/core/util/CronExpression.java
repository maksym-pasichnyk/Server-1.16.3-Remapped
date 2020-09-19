/*      */ package org.apache.logging.log4j.core.util;
/*      */ 
/*      */ import java.text.ParseException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.SortedSet;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TimeZone;
/*      */ import java.util.TreeSet;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class CronExpression
/*      */ {
/*      */   protected static final int SECOND = 0;
/*      */   protected static final int MINUTE = 1;
/*      */   protected static final int HOUR = 2;
/*      */   protected static final int DAY_OF_MONTH = 3;
/*      */   protected static final int MONTH = 4;
/*      */   protected static final int DAY_OF_WEEK = 5;
/*      */   protected static final int YEAR = 6;
/*      */   protected static final int ALL_SPEC_INT = 99;
/*      */   protected static final int NO_SPEC_INT = 98;
/*  208 */   protected static final Integer ALL_SPEC = Integer.valueOf(99);
/*  209 */   protected static final Integer NO_SPEC = Integer.valueOf(98);
/*      */   
/*  211 */   protected static final Map<String, Integer> monthMap = new HashMap<>(20);
/*  212 */   protected static final Map<String, Integer> dayMap = new HashMap<>(60);
/*      */   
/*      */   static {
/*  215 */     monthMap.put("JAN", Integer.valueOf(0));
/*  216 */     monthMap.put("FEB", Integer.valueOf(1));
/*  217 */     monthMap.put("MAR", Integer.valueOf(2));
/*  218 */     monthMap.put("APR", Integer.valueOf(3));
/*  219 */     monthMap.put("MAY", Integer.valueOf(4));
/*  220 */     monthMap.put("JUN", Integer.valueOf(5));
/*  221 */     monthMap.put("JUL", Integer.valueOf(6));
/*  222 */     monthMap.put("AUG", Integer.valueOf(7));
/*  223 */     monthMap.put("SEP", Integer.valueOf(8));
/*  224 */     monthMap.put("OCT", Integer.valueOf(9));
/*  225 */     monthMap.put("NOV", Integer.valueOf(10));
/*  226 */     monthMap.put("DEC", Integer.valueOf(11));
/*      */     
/*  228 */     dayMap.put("SUN", Integer.valueOf(1));
/*  229 */     dayMap.put("MON", Integer.valueOf(2));
/*  230 */     dayMap.put("TUE", Integer.valueOf(3));
/*  231 */     dayMap.put("WED", Integer.valueOf(4));
/*  232 */     dayMap.put("THU", Integer.valueOf(5));
/*  233 */     dayMap.put("FRI", Integer.valueOf(6));
/*  234 */     dayMap.put("SAT", Integer.valueOf(7));
/*      */   }
/*      */   
/*      */   private final String cronExpression;
/*  238 */   private TimeZone timeZone = null;
/*      */   
/*      */   protected transient TreeSet<Integer> seconds;
/*      */   protected transient TreeSet<Integer> minutes;
/*      */   protected transient TreeSet<Integer> hours;
/*      */   protected transient TreeSet<Integer> daysOfMonth;
/*      */   protected transient TreeSet<Integer> months;
/*      */   protected transient TreeSet<Integer> daysOfWeek;
/*      */   protected transient TreeSet<Integer> years;
/*      */   protected transient boolean lastdayOfWeek = false;
/*  248 */   protected transient int nthdayOfWeek = 0;
/*      */   protected transient boolean lastdayOfMonth = false;
/*      */   protected transient boolean nearestWeekday = false;
/*  251 */   protected transient int lastdayOffset = 0;
/*      */   
/*      */   protected transient boolean expressionParsed = false;
/*  254 */   public static final int MAX_YEAR = Calendar.getInstance().get(1) + 100;
/*  255 */   public static final Calendar MIN_CAL = Calendar.getInstance();
/*      */   static {
/*  257 */     MIN_CAL.set(1970, 0, 1);
/*      */   }
/*  259 */   public static final Date MIN_DATE = MIN_CAL.getTime();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CronExpression(String cronExpression) throws ParseException {
/*  271 */     if (cronExpression == null) {
/*  272 */       throw new IllegalArgumentException("cronExpression cannot be null");
/*      */     }
/*      */     
/*  275 */     this.cronExpression = cronExpression.toUpperCase(Locale.US);
/*      */     
/*  277 */     buildExpression(this.cronExpression);
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
/*      */   public boolean isSatisfiedBy(Date date) {
/*  290 */     Calendar testDateCal = Calendar.getInstance(getTimeZone());
/*  291 */     testDateCal.setTime(date);
/*  292 */     testDateCal.set(14, 0);
/*  293 */     Date originalDate = testDateCal.getTime();
/*      */     
/*  295 */     testDateCal.add(13, -1);
/*      */     
/*  297 */     Date timeAfter = getTimeAfter(testDateCal.getTime());
/*      */     
/*  299 */     return (timeAfter != null && timeAfter.equals(originalDate));
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
/*      */   public Date getNextValidTimeAfter(Date date) {
/*  311 */     return getTimeAfter(date);
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
/*      */   public Date getNextInvalidTimeAfter(Date date) {
/*  323 */     long difference = 1000L;
/*      */ 
/*      */     
/*  326 */     Calendar adjustCal = Calendar.getInstance(getTimeZone());
/*  327 */     adjustCal.setTime(date);
/*  328 */     adjustCal.set(14, 0);
/*  329 */     Date lastDate = adjustCal.getTime();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  338 */     while (difference == 1000L) {
/*  339 */       Date newDate = getTimeAfter(lastDate);
/*  340 */       if (newDate == null) {
/*      */         break;
/*      */       }
/*      */       
/*  344 */       difference = newDate.getTime() - lastDate.getTime();
/*      */       
/*  346 */       if (difference == 1000L) {
/*  347 */         lastDate = newDate;
/*      */       }
/*      */     } 
/*      */     
/*  351 */     return new Date(lastDate.getTime() + 1000L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TimeZone getTimeZone() {
/*  359 */     if (this.timeZone == null) {
/*  360 */       this.timeZone = TimeZone.getDefault();
/*      */     }
/*      */     
/*  363 */     return this.timeZone;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTimeZone(TimeZone timeZone) {
/*  371 */     this.timeZone = timeZone;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  381 */     return this.cronExpression;
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
/*      */   public static boolean isValidExpression(String cronExpression) {
/*      */     try {
/*  395 */       new CronExpression(cronExpression);
/*  396 */     } catch (ParseException pe) {
/*  397 */       return false;
/*      */     } 
/*      */     
/*  400 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void validateExpression(String cronExpression) throws ParseException {
/*  405 */     new CronExpression(cronExpression);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void buildExpression(String expression) throws ParseException {
/*  416 */     this.expressionParsed = true;
/*      */ 
/*      */     
/*      */     try {
/*  420 */       if (this.seconds == null) {
/*  421 */         this.seconds = new TreeSet<>();
/*      */       }
/*  423 */       if (this.minutes == null) {
/*  424 */         this.minutes = new TreeSet<>();
/*      */       }
/*  426 */       if (this.hours == null) {
/*  427 */         this.hours = new TreeSet<>();
/*      */       }
/*  429 */       if (this.daysOfMonth == null) {
/*  430 */         this.daysOfMonth = new TreeSet<>();
/*      */       }
/*  432 */       if (this.months == null) {
/*  433 */         this.months = new TreeSet<>();
/*      */       }
/*  435 */       if (this.daysOfWeek == null) {
/*  436 */         this.daysOfWeek = new TreeSet<>();
/*      */       }
/*  438 */       if (this.years == null) {
/*  439 */         this.years = new TreeSet<>();
/*      */       }
/*      */       
/*  442 */       int exprOn = 0;
/*      */       
/*  444 */       StringTokenizer exprsTok = new StringTokenizer(expression, " \t", false);
/*      */ 
/*      */       
/*  447 */       while (exprsTok.hasMoreTokens() && exprOn <= 6) {
/*  448 */         String expr = exprsTok.nextToken().trim();
/*      */ 
/*      */         
/*  451 */         if (exprOn == 3 && expr.indexOf('L') != -1 && expr.length() > 1 && expr.contains(",")) {
/*  452 */           throw new ParseException("Support for specifying 'L' and 'LW' with other days of the month is not implemented", -1);
/*      */         }
/*      */         
/*  455 */         if (exprOn == 5 && expr.indexOf('L') != -1 && expr.length() > 1 && expr.contains(",")) {
/*  456 */           throw new ParseException("Support for specifying 'L' with other days of the week is not implemented", -1);
/*      */         }
/*  458 */         if (exprOn == 5 && expr.indexOf('#') != -1 && expr.indexOf('#', expr.indexOf('#') + 1) != -1) {
/*  459 */           throw new ParseException("Support for specifying multiple \"nth\" days is not implemented.", -1);
/*      */         }
/*      */         
/*  462 */         StringTokenizer vTok = new StringTokenizer(expr, ",");
/*  463 */         while (vTok.hasMoreTokens()) {
/*  464 */           String v = vTok.nextToken();
/*  465 */           storeExpressionVals(0, v, exprOn);
/*      */         } 
/*      */         
/*  468 */         exprOn++;
/*      */       } 
/*      */       
/*  471 */       if (exprOn <= 5) {
/*  472 */         throw new ParseException("Unexpected end of expression.", expression.length());
/*      */       }
/*      */ 
/*      */       
/*  476 */       if (exprOn <= 6) {
/*  477 */         storeExpressionVals(0, "*", 6);
/*      */       }
/*      */       
/*  480 */       TreeSet<Integer> dow = getSet(5);
/*  481 */       TreeSet<Integer> dom = getSet(3);
/*      */ 
/*      */       
/*  484 */       boolean dayOfMSpec = !dom.contains(NO_SPEC);
/*  485 */       boolean dayOfWSpec = !dow.contains(NO_SPEC);
/*      */       
/*  487 */       if ((!dayOfMSpec || dayOfWSpec) && (
/*  488 */         !dayOfWSpec || dayOfMSpec)) {
/*  489 */         throw new ParseException("Support for specifying both a day-of-week AND a day-of-month parameter is not implemented.", 0);
/*      */       
/*      */       }
/*      */     }
/*  493 */     catch (ParseException pe) {
/*  494 */       throw pe;
/*  495 */     } catch (Exception e) {
/*  496 */       throw new ParseException("Illegal cron expression format (" + e.toString() + ")", 0);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int storeExpressionVals(int pos, String s, int type) throws ParseException {
/*  504 */     int incr = 0;
/*  505 */     int i = skipWhiteSpace(pos, s);
/*  506 */     if (i >= s.length()) {
/*  507 */       return i;
/*      */     }
/*  509 */     char c = s.charAt(i);
/*  510 */     if (c >= 'A' && c <= 'Z' && !s.equals("L") && !s.equals("LW") && !s.matches("^L-[0-9]*[W]?")) {
/*  511 */       String sub = s.substring(i, i + 3);
/*  512 */       int sval = -1;
/*  513 */       int eval = -1;
/*  514 */       if (type == 4) {
/*  515 */         sval = getMonthNumber(sub) + 1;
/*  516 */         if (sval <= 0) {
/*  517 */           throw new ParseException("Invalid Month value: '" + sub + "'", i);
/*      */         }
/*  519 */         if (s.length() > i + 3) {
/*  520 */           c = s.charAt(i + 3);
/*  521 */           if (c == '-') {
/*  522 */             i += 4;
/*  523 */             sub = s.substring(i, i + 3);
/*  524 */             eval = getMonthNumber(sub) + 1;
/*  525 */             if (eval <= 0) {
/*  526 */               throw new ParseException("Invalid Month value: '" + sub + "'", i);
/*      */             }
/*      */           } 
/*      */         } 
/*  530 */       } else if (type == 5) {
/*  531 */         sval = getDayOfWeekNumber(sub);
/*  532 */         if (sval < 0) {
/*  533 */           throw new ParseException("Invalid Day-of-Week value: '" + sub + "'", i);
/*      */         }
/*      */         
/*  536 */         if (s.length() > i + 3) {
/*  537 */           c = s.charAt(i + 3);
/*  538 */           if (c == '-') {
/*  539 */             i += 4;
/*  540 */             sub = s.substring(i, i + 3);
/*  541 */             eval = getDayOfWeekNumber(sub);
/*  542 */             if (eval < 0) {
/*  543 */               throw new ParseException("Invalid Day-of-Week value: '" + sub + "'", i);
/*      */             
/*      */             }
/*      */           }
/*  547 */           else if (c == '#') {
/*      */             try {
/*  549 */               i += 4;
/*  550 */               this.nthdayOfWeek = Integer.parseInt(s.substring(i));
/*  551 */               if (this.nthdayOfWeek < 1 || this.nthdayOfWeek > 5) {
/*  552 */                 throw new Exception();
/*      */               }
/*  554 */             } catch (Exception e) {
/*  555 */               throw new ParseException("A numeric value between 1 and 5 must follow the '#' option", i);
/*      */             }
/*      */           
/*      */           }
/*  559 */           else if (c == 'L') {
/*  560 */             this.lastdayOfWeek = true;
/*  561 */             i++;
/*      */           } 
/*      */         } 
/*      */       } else {
/*      */         
/*  566 */         throw new ParseException("Illegal characters for this position: '" + sub + "'", i);
/*      */       } 
/*      */ 
/*      */       
/*  570 */       if (eval != -1) {
/*  571 */         incr = 1;
/*      */       }
/*  573 */       addToSet(sval, eval, incr, type);
/*  574 */       return i + 3;
/*      */     } 
/*      */     
/*  577 */     if (c == '?') {
/*  578 */       i++;
/*  579 */       if (i + 1 < s.length() && s.charAt(i) != ' ' && s.charAt(i + 1) != '\t')
/*      */       {
/*  581 */         throw new ParseException("Illegal character after '?': " + s.charAt(i), i);
/*      */       }
/*      */       
/*  584 */       if (type != 5 && type != 3) {
/*  585 */         throw new ParseException("'?' can only be specfied for Day-of-Month or Day-of-Week.", i);
/*      */       }
/*      */ 
/*      */       
/*  589 */       if (type == 5 && !this.lastdayOfMonth) {
/*  590 */         int val = ((Integer)this.daysOfMonth.last()).intValue();
/*  591 */         if (val == 98) {
/*  592 */           throw new ParseException("'?' can only be specfied for Day-of-Month -OR- Day-of-Week.", i);
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  598 */       addToSet(98, -1, 0, type);
/*  599 */       return i;
/*      */     } 
/*      */     
/*  602 */     if (c == '*' || c == '/') {
/*  603 */       if (c == '*' && i + 1 >= s.length()) {
/*  604 */         addToSet(99, -1, incr, type);
/*  605 */         return i + 1;
/*  606 */       }  if (c == '/' && (i + 1 >= s.length() || s.charAt(i + 1) == ' ' || s.charAt(i + 1) == '\t'))
/*      */       {
/*      */         
/*  609 */         throw new ParseException("'/' must be followed by an integer.", i); } 
/*  610 */       if (c == '*') {
/*  611 */         i++;
/*      */       }
/*  613 */       c = s.charAt(i);
/*  614 */       if (c == '/') {
/*  615 */         i++;
/*  616 */         if (i >= s.length()) {
/*  617 */           throw new ParseException("Unexpected end of string.", i);
/*      */         }
/*      */         
/*  620 */         incr = getNumericValue(s, i);
/*      */         
/*  622 */         i++;
/*  623 */         if (incr > 10) {
/*  624 */           i++;
/*      */         }
/*  626 */         if (incr > 59 && (type == 0 || type == 1))
/*  627 */           throw new ParseException("Increment > 60 : " + incr, i); 
/*  628 */         if (incr > 23 && type == 2)
/*  629 */           throw new ParseException("Increment > 24 : " + incr, i); 
/*  630 */         if (incr > 31 && type == 3)
/*  631 */           throw new ParseException("Increment > 31 : " + incr, i); 
/*  632 */         if (incr > 7 && type == 5)
/*  633 */           throw new ParseException("Increment > 7 : " + incr, i); 
/*  634 */         if (incr > 12 && type == 4) {
/*  635 */           throw new ParseException("Increment > 12 : " + incr, i);
/*      */         }
/*      */       } else {
/*  638 */         incr = 1;
/*      */       } 
/*      */       
/*  641 */       addToSet(99, -1, incr, type);
/*  642 */       return i;
/*  643 */     }  if (c == 'L') {
/*  644 */       i++;
/*  645 */       if (type == 3) {
/*  646 */         this.lastdayOfMonth = true;
/*      */       }
/*  648 */       if (type == 5) {
/*  649 */         addToSet(7, 7, 0, type);
/*      */       }
/*  651 */       if (type == 3 && s.length() > i) {
/*  652 */         c = s.charAt(i);
/*  653 */         if (c == '-') {
/*  654 */           ValueSet vs = getValue(0, s, i + 1);
/*  655 */           this.lastdayOffset = vs.value;
/*  656 */           if (this.lastdayOffset > 30) {
/*  657 */             throw new ParseException("Offset from last day must be <= 30", i + 1);
/*      */           }
/*  659 */           i = vs.pos;
/*      */         } 
/*  661 */         if (s.length() > i) {
/*  662 */           c = s.charAt(i);
/*  663 */           if (c == 'W') {
/*  664 */             this.nearestWeekday = true;
/*  665 */             i++;
/*      */           } 
/*      */         } 
/*      */       } 
/*  669 */       return i;
/*  670 */     }  if (c >= '0' && c <= '9') {
/*  671 */       int val = Integer.parseInt(String.valueOf(c));
/*  672 */       i++;
/*  673 */       if (i >= s.length()) {
/*  674 */         addToSet(val, -1, -1, type);
/*      */       } else {
/*  676 */         c = s.charAt(i);
/*  677 */         if (c >= '0' && c <= '9') {
/*  678 */           ValueSet vs = getValue(val, s, i);
/*  679 */           val = vs.value;
/*  680 */           i = vs.pos;
/*      */         } 
/*  682 */         i = checkNext(i, s, val, type);
/*  683 */         return i;
/*      */       } 
/*      */     } else {
/*  686 */       throw new ParseException("Unexpected character: " + c, i);
/*      */     } 
/*      */     
/*  689 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected int checkNext(int pos, String s, int val, int type) throws ParseException {
/*  695 */     int end = -1;
/*  696 */     int i = pos;
/*      */     
/*  698 */     if (i >= s.length()) {
/*  699 */       addToSet(val, end, -1, type);
/*  700 */       return i;
/*      */     } 
/*      */     
/*  703 */     char c = s.charAt(pos);
/*      */     
/*  705 */     if (c == 'L') {
/*  706 */       if (type == 5) {
/*  707 */         if (val < 1 || val > 7) {
/*  708 */           throw new ParseException("Day-of-Week values must be between 1 and 7", -1);
/*      */         }
/*  710 */         this.lastdayOfWeek = true;
/*      */       } else {
/*  712 */         throw new ParseException("'L' option is not valid here. (pos=" + i + ")", i);
/*      */       } 
/*  714 */       TreeSet<Integer> set = getSet(type);
/*  715 */       set.add(Integer.valueOf(val));
/*  716 */       i++;
/*  717 */       return i;
/*      */     } 
/*      */     
/*  720 */     if (c == 'W') {
/*  721 */       if (type == 3) {
/*  722 */         this.nearestWeekday = true;
/*      */       } else {
/*  724 */         throw new ParseException("'W' option is not valid here. (pos=" + i + ")", i);
/*      */       } 
/*  726 */       if (val > 31) {
/*  727 */         throw new ParseException("The 'W' option does not make sense with values larger than 31 (max number of days in a month)", i);
/*      */       }
/*  729 */       TreeSet<Integer> set = getSet(type);
/*  730 */       set.add(Integer.valueOf(val));
/*  731 */       i++;
/*  732 */       return i;
/*      */     } 
/*      */     
/*  735 */     if (c == '#') {
/*  736 */       if (type != 5) {
/*  737 */         throw new ParseException("'#' option is not valid here. (pos=" + i + ")", i);
/*      */       }
/*  739 */       i++;
/*      */       try {
/*  741 */         this.nthdayOfWeek = Integer.parseInt(s.substring(i));
/*  742 */         if (this.nthdayOfWeek < 1 || this.nthdayOfWeek > 5) {
/*  743 */           throw new Exception();
/*      */         }
/*  745 */       } catch (Exception e) {
/*  746 */         throw new ParseException("A numeric value between 1 and 5 must follow the '#' option", i);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  751 */       TreeSet<Integer> set = getSet(type);
/*  752 */       set.add(Integer.valueOf(val));
/*  753 */       i++;
/*  754 */       return i;
/*      */     } 
/*      */     
/*  757 */     if (c == '-') {
/*  758 */       i++;
/*  759 */       c = s.charAt(i);
/*  760 */       int v = Integer.parseInt(String.valueOf(c));
/*  761 */       end = v;
/*  762 */       i++;
/*  763 */       if (i >= s.length()) {
/*  764 */         addToSet(val, end, 1, type);
/*  765 */         return i;
/*      */       } 
/*  767 */       c = s.charAt(i);
/*  768 */       if (c >= '0' && c <= '9') {
/*  769 */         ValueSet vs = getValue(v, s, i);
/*  770 */         end = vs.value;
/*  771 */         i = vs.pos;
/*      */       } 
/*  773 */       if (i < s.length() && (c = s.charAt(i)) == '/') {
/*  774 */         i++;
/*  775 */         c = s.charAt(i);
/*  776 */         int v2 = Integer.parseInt(String.valueOf(c));
/*  777 */         i++;
/*  778 */         if (i >= s.length()) {
/*  779 */           addToSet(val, end, v2, type);
/*  780 */           return i;
/*      */         } 
/*  782 */         c = s.charAt(i);
/*  783 */         if (c >= '0' && c <= '9') {
/*  784 */           ValueSet vs = getValue(v2, s, i);
/*  785 */           int v3 = vs.value;
/*  786 */           addToSet(val, end, v3, type);
/*  787 */           i = vs.pos;
/*  788 */           return i;
/*      */         } 
/*  790 */         addToSet(val, end, v2, type);
/*  791 */         return i;
/*      */       } 
/*      */       
/*  794 */       addToSet(val, end, 1, type);
/*  795 */       return i;
/*      */     } 
/*      */ 
/*      */     
/*  799 */     if (c == '/') {
/*  800 */       i++;
/*  801 */       c = s.charAt(i);
/*  802 */       int v2 = Integer.parseInt(String.valueOf(c));
/*  803 */       i++;
/*  804 */       if (i >= s.length()) {
/*  805 */         addToSet(val, end, v2, type);
/*  806 */         return i;
/*      */       } 
/*  808 */       c = s.charAt(i);
/*  809 */       if (c >= '0' && c <= '9') {
/*  810 */         ValueSet vs = getValue(v2, s, i);
/*  811 */         int v3 = vs.value;
/*  812 */         addToSet(val, end, v3, type);
/*  813 */         i = vs.pos;
/*  814 */         return i;
/*      */       } 
/*  816 */       throw new ParseException("Unexpected character '" + c + "' after '/'", i);
/*      */     } 
/*      */ 
/*      */     
/*  820 */     addToSet(val, end, 0, type);
/*  821 */     i++;
/*  822 */     return i;
/*      */   }
/*      */   
/*      */   public String getCronExpression() {
/*  826 */     return this.cronExpression;
/*      */   }
/*      */   
/*      */   public String getExpressionSummary() {
/*  830 */     StringBuilder buf = new StringBuilder();
/*      */     
/*  832 */     buf.append("seconds: ");
/*  833 */     buf.append(getExpressionSetSummary(this.seconds));
/*  834 */     buf.append("\n");
/*  835 */     buf.append("minutes: ");
/*  836 */     buf.append(getExpressionSetSummary(this.minutes));
/*  837 */     buf.append("\n");
/*  838 */     buf.append("hours: ");
/*  839 */     buf.append(getExpressionSetSummary(this.hours));
/*  840 */     buf.append("\n");
/*  841 */     buf.append("daysOfMonth: ");
/*  842 */     buf.append(getExpressionSetSummary(this.daysOfMonth));
/*  843 */     buf.append("\n");
/*  844 */     buf.append("months: ");
/*  845 */     buf.append(getExpressionSetSummary(this.months));
/*  846 */     buf.append("\n");
/*  847 */     buf.append("daysOfWeek: ");
/*  848 */     buf.append(getExpressionSetSummary(this.daysOfWeek));
/*  849 */     buf.append("\n");
/*  850 */     buf.append("lastdayOfWeek: ");
/*  851 */     buf.append(this.lastdayOfWeek);
/*  852 */     buf.append("\n");
/*  853 */     buf.append("nearestWeekday: ");
/*  854 */     buf.append(this.nearestWeekday);
/*  855 */     buf.append("\n");
/*  856 */     buf.append("NthDayOfWeek: ");
/*  857 */     buf.append(this.nthdayOfWeek);
/*  858 */     buf.append("\n");
/*  859 */     buf.append("lastdayOfMonth: ");
/*  860 */     buf.append(this.lastdayOfMonth);
/*  861 */     buf.append("\n");
/*  862 */     buf.append("years: ");
/*  863 */     buf.append(getExpressionSetSummary(this.years));
/*  864 */     buf.append("\n");
/*      */     
/*  866 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getExpressionSetSummary(Set<Integer> set) {
/*  871 */     if (set.contains(NO_SPEC)) {
/*  872 */       return "?";
/*      */     }
/*  874 */     if (set.contains(ALL_SPEC)) {
/*  875 */       return "*";
/*      */     }
/*      */     
/*  878 */     StringBuilder buf = new StringBuilder();
/*      */     
/*  880 */     Iterator<Integer> itr = set.iterator();
/*  881 */     boolean first = true;
/*  882 */     while (itr.hasNext()) {
/*  883 */       Integer iVal = itr.next();
/*  884 */       String val = iVal.toString();
/*  885 */       if (!first) {
/*  886 */         buf.append(",");
/*      */       }
/*  888 */       buf.append(val);
/*  889 */       first = false;
/*      */     } 
/*      */     
/*  892 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getExpressionSetSummary(ArrayList<Integer> list) {
/*  897 */     if (list.contains(NO_SPEC)) {
/*  898 */       return "?";
/*      */     }
/*  900 */     if (list.contains(ALL_SPEC)) {
/*  901 */       return "*";
/*      */     }
/*      */     
/*  904 */     StringBuilder buf = new StringBuilder();
/*      */     
/*  906 */     Iterator<Integer> itr = list.iterator();
/*  907 */     boolean first = true;
/*  908 */     while (itr.hasNext()) {
/*  909 */       Integer iVal = itr.next();
/*  910 */       String val = iVal.toString();
/*  911 */       if (!first) {
/*  912 */         buf.append(",");
/*      */       }
/*  914 */       buf.append(val);
/*  915 */       first = false;
/*      */     } 
/*      */     
/*  918 */     return buf.toString();
/*      */   }
/*      */   
/*      */   protected int skipWhiteSpace(int i, String s) {
/*  922 */     for (; i < s.length() && (s.charAt(i) == ' ' || s.charAt(i) == '\t'); i++);
/*      */ 
/*      */ 
/*      */     
/*  926 */     return i;
/*      */   }
/*      */   
/*      */   protected int findNextWhiteSpace(int i, String s) {
/*  930 */     for (; i < s.length() && (s.charAt(i) != ' ' || s.charAt(i) != '\t'); i++);
/*      */ 
/*      */ 
/*      */     
/*  934 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addToSet(int val, int end, int incr, int type) throws ParseException {
/*  940 */     TreeSet<Integer> set = getSet(type);
/*      */     
/*  942 */     if (type == 0 || type == 1) {
/*  943 */       if ((val < 0 || val > 59 || end > 59) && val != 99) {
/*  944 */         throw new ParseException("Minute and Second values must be between 0 and 59", -1);
/*      */       
/*      */       }
/*      */     }
/*  948 */     else if (type == 2) {
/*  949 */       if ((val < 0 || val > 23 || end > 23) && val != 99) {
/*  950 */         throw new ParseException("Hour values must be between 0 and 23", -1);
/*      */       }
/*      */     }
/*  953 */     else if (type == 3) {
/*  954 */       if ((val < 1 || val > 31 || end > 31) && val != 99 && val != 98)
/*      */       {
/*  956 */         throw new ParseException("Day of month values must be between 1 and 31", -1);
/*      */       }
/*      */     }
/*  959 */     else if (type == 4) {
/*  960 */       if ((val < 1 || val > 12 || end > 12) && val != 99) {
/*  961 */         throw new ParseException("Month values must be between 1 and 12", -1);
/*      */       }
/*      */     }
/*  964 */     else if (type == 5 && (
/*  965 */       val == 0 || val > 7 || end > 7) && val != 99 && val != 98) {
/*      */       
/*  967 */       throw new ParseException("Day-of-Week values must be between 1 and 7", -1);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  972 */     if ((incr == 0 || incr == -1) && val != 99) {
/*  973 */       if (val != -1) {
/*  974 */         set.add(Integer.valueOf(val));
/*      */       } else {
/*  976 */         set.add(NO_SPEC);
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  982 */     int startAt = val;
/*  983 */     int stopAt = end;
/*      */     
/*  985 */     if (val == 99 && incr <= 0) {
/*  986 */       incr = 1;
/*  987 */       set.add(ALL_SPEC);
/*      */     } 
/*      */     
/*  990 */     if (type == 0 || type == 1) {
/*  991 */       if (stopAt == -1) {
/*  992 */         stopAt = 59;
/*      */       }
/*  994 */       if (startAt == -1 || startAt == 99) {
/*  995 */         startAt = 0;
/*      */       }
/*  997 */     } else if (type == 2) {
/*  998 */       if (stopAt == -1) {
/*  999 */         stopAt = 23;
/*      */       }
/* 1001 */       if (startAt == -1 || startAt == 99) {
/* 1002 */         startAt = 0;
/*      */       }
/* 1004 */     } else if (type == 3) {
/* 1005 */       if (stopAt == -1) {
/* 1006 */         stopAt = 31;
/*      */       }
/* 1008 */       if (startAt == -1 || startAt == 99) {
/* 1009 */         startAt = 1;
/*      */       }
/* 1011 */     } else if (type == 4) {
/* 1012 */       if (stopAt == -1) {
/* 1013 */         stopAt = 12;
/*      */       }
/* 1015 */       if (startAt == -1 || startAt == 99) {
/* 1016 */         startAt = 1;
/*      */       }
/* 1018 */     } else if (type == 5) {
/* 1019 */       if (stopAt == -1) {
/* 1020 */         stopAt = 7;
/*      */       }
/* 1022 */       if (startAt == -1 || startAt == 99) {
/* 1023 */         startAt = 1;
/*      */       }
/* 1025 */     } else if (type == 6) {
/* 1026 */       if (stopAt == -1) {
/* 1027 */         stopAt = MAX_YEAR;
/*      */       }
/* 1029 */       if (startAt == -1 || startAt == 99) {
/* 1030 */         startAt = 1970;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1037 */     int max = -1;
/* 1038 */     if (stopAt < startAt) {
/* 1039 */       switch (type) {
/*      */         case 0:
/* 1041 */           max = 60;
/*      */           break;
/*      */         case 1:
/* 1044 */           max = 60;
/*      */           break;
/*      */         case 2:
/* 1047 */           max = 24;
/*      */           break;
/*      */         case 4:
/* 1050 */           max = 12;
/*      */           break;
/*      */         case 5:
/* 1053 */           max = 7;
/*      */           break;
/*      */         case 3:
/* 1056 */           max = 31;
/*      */           break;
/*      */         case 6:
/* 1059 */           throw new IllegalArgumentException("Start year must be less than stop year");
/*      */         default:
/* 1061 */           throw new IllegalArgumentException("Unexpected type encountered");
/*      */       } 
/* 1063 */       stopAt += max;
/*      */     } 
/*      */     int i;
/* 1066 */     for (i = startAt; i <= stopAt; i += incr) {
/* 1067 */       if (max == -1) {
/*      */         
/* 1069 */         set.add(Integer.valueOf(i));
/*      */       } else {
/*      */         
/* 1072 */         int i2 = i % max;
/*      */ 
/*      */         
/* 1075 */         if (i2 == 0 && (type == 4 || type == 5 || type == 3)) {
/* 1076 */           i2 = max;
/*      */         }
/*      */         
/* 1079 */         set.add(Integer.valueOf(i2));
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   TreeSet<Integer> getSet(int type) {
/* 1085 */     switch (type) {
/*      */       case 0:
/* 1087 */         return this.seconds;
/*      */       case 1:
/* 1089 */         return this.minutes;
/*      */       case 2:
/* 1091 */         return this.hours;
/*      */       case 3:
/* 1093 */         return this.daysOfMonth;
/*      */       case 4:
/* 1095 */         return this.months;
/*      */       case 5:
/* 1097 */         return this.daysOfWeek;
/*      */       case 6:
/* 1099 */         return this.years;
/*      */     } 
/* 1101 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   protected ValueSet getValue(int v, String s, int i) {
/* 1106 */     char c = s.charAt(i);
/* 1107 */     StringBuilder s1 = new StringBuilder(String.valueOf(v));
/* 1108 */     while (c >= '0' && c <= '9') {
/* 1109 */       s1.append(c);
/* 1110 */       i++;
/* 1111 */       if (i >= s.length()) {
/*      */         break;
/*      */       }
/* 1114 */       c = s.charAt(i);
/*      */     } 
/* 1116 */     ValueSet val = new ValueSet();
/*      */     
/* 1118 */     val.pos = (i < s.length()) ? i : (i + 1);
/* 1119 */     val.value = Integer.parseInt(s1.toString());
/* 1120 */     return val;
/*      */   }
/*      */   
/*      */   protected int getNumericValue(String s, int i) {
/* 1124 */     int endOfVal = findNextWhiteSpace(i, s);
/* 1125 */     String val = s.substring(i, endOfVal);
/* 1126 */     return Integer.parseInt(val);
/*      */   }
/*      */   
/*      */   protected int getMonthNumber(String s) {
/* 1130 */     Integer integer = monthMap.get(s);
/*      */     
/* 1132 */     if (integer == null) {
/* 1133 */       return -1;
/*      */     }
/*      */     
/* 1136 */     return integer.intValue();
/*      */   }
/*      */   
/*      */   protected int getDayOfWeekNumber(String s) {
/* 1140 */     Integer integer = dayMap.get(s);
/*      */     
/* 1142 */     if (integer == null) {
/* 1143 */       return -1;
/*      */     }
/*      */     
/* 1146 */     return integer.intValue();
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
/*      */   public Date getTimeAfter(Date afterTime) {
/* 1158 */     Calendar cl = new GregorianCalendar(getTimeZone());
/*      */ 
/*      */ 
/*      */     
/* 1162 */     afterTime = new Date(afterTime.getTime() + 1000L);
/*      */     
/* 1164 */     cl.setTime(afterTime);
/* 1165 */     cl.set(14, 0);
/*      */     
/* 1167 */     boolean gotOne = false;
/*      */     
/* 1169 */     while (!gotOne) {
/*      */       
/* 1171 */       if (cl.get(1) > 2999) {
/* 1172 */         return null;
/*      */       }
/*      */       
/* 1175 */       SortedSet<Integer> st = null;
/* 1176 */       int t = 0;
/*      */       
/* 1178 */       int sec = cl.get(13);
/* 1179 */       int min = cl.get(12);
/*      */ 
/*      */       
/* 1182 */       st = this.seconds.tailSet(Integer.valueOf(sec));
/* 1183 */       if (st != null && st.size() != 0) {
/* 1184 */         sec = ((Integer)st.first()).intValue();
/*      */       } else {
/* 1186 */         sec = ((Integer)this.seconds.first()).intValue();
/* 1187 */         min++;
/* 1188 */         cl.set(12, min);
/*      */       } 
/* 1190 */       cl.set(13, sec);
/*      */       
/* 1192 */       min = cl.get(12);
/* 1193 */       int hr = cl.get(11);
/* 1194 */       t = -1;
/*      */ 
/*      */       
/* 1197 */       st = this.minutes.tailSet(Integer.valueOf(min));
/* 1198 */       if (st != null && st.size() != 0) {
/* 1199 */         t = min;
/* 1200 */         min = ((Integer)st.first()).intValue();
/*      */       } else {
/* 1202 */         min = ((Integer)this.minutes.first()).intValue();
/* 1203 */         hr++;
/*      */       } 
/* 1205 */       if (min != t) {
/* 1206 */         cl.set(13, 0);
/* 1207 */         cl.set(12, min);
/* 1208 */         setCalendarHour(cl, hr);
/*      */         continue;
/*      */       } 
/* 1211 */       cl.set(12, min);
/*      */       
/* 1213 */       hr = cl.get(11);
/* 1214 */       int day = cl.get(5);
/* 1215 */       t = -1;
/*      */ 
/*      */       
/* 1218 */       st = this.hours.tailSet(Integer.valueOf(hr));
/* 1219 */       if (st != null && st.size() != 0) {
/* 1220 */         t = hr;
/* 1221 */         hr = ((Integer)st.first()).intValue();
/*      */       } else {
/* 1223 */         hr = ((Integer)this.hours.first()).intValue();
/* 1224 */         day++;
/*      */       } 
/* 1226 */       if (hr != t) {
/* 1227 */         cl.set(13, 0);
/* 1228 */         cl.set(12, 0);
/* 1229 */         cl.set(5, day);
/* 1230 */         setCalendarHour(cl, hr);
/*      */         continue;
/*      */       } 
/* 1233 */       cl.set(11, hr);
/*      */       
/* 1235 */       day = cl.get(5);
/* 1236 */       int mon = cl.get(2) + 1;
/*      */ 
/*      */       
/* 1239 */       t = -1;
/* 1240 */       int tmon = mon;
/*      */ 
/*      */       
/* 1243 */       boolean dayOfMSpec = !this.daysOfMonth.contains(NO_SPEC);
/* 1244 */       boolean dayOfWSpec = !this.daysOfWeek.contains(NO_SPEC);
/* 1245 */       if (dayOfMSpec && !dayOfWSpec) {
/* 1246 */         st = this.daysOfMonth.tailSet(Integer.valueOf(day));
/* 1247 */         if (this.lastdayOfMonth) {
/* 1248 */           if (!this.nearestWeekday) {
/* 1249 */             t = day;
/* 1250 */             day = getLastDayOfMonth(mon, cl.get(1));
/* 1251 */             day -= this.lastdayOffset;
/* 1252 */             if (t > day) {
/* 1253 */               mon++;
/* 1254 */               if (mon > 12) {
/* 1255 */                 mon = 1;
/* 1256 */                 tmon = 3333;
/* 1257 */                 cl.add(1, 1);
/*      */               } 
/* 1259 */               day = 1;
/*      */             } 
/*      */           } else {
/* 1262 */             t = day;
/* 1263 */             day = getLastDayOfMonth(mon, cl.get(1));
/* 1264 */             day -= this.lastdayOffset;
/*      */             
/* 1266 */             Calendar tcal = Calendar.getInstance(getTimeZone());
/* 1267 */             tcal.set(13, 0);
/* 1268 */             tcal.set(12, 0);
/* 1269 */             tcal.set(11, 0);
/* 1270 */             tcal.set(5, day);
/* 1271 */             tcal.set(2, mon - 1);
/* 1272 */             tcal.set(1, cl.get(1));
/*      */             
/* 1274 */             int ldom = getLastDayOfMonth(mon, cl.get(1));
/* 1275 */             int dow = tcal.get(7);
/*      */             
/* 1277 */             if (dow == 7 && day == 1) {
/* 1278 */               day += 2;
/* 1279 */             } else if (dow == 7) {
/* 1280 */               day--;
/* 1281 */             } else if (dow == 1 && day == ldom) {
/* 1282 */               day -= 2;
/* 1283 */             } else if (dow == 1) {
/* 1284 */               day++;
/*      */             } 
/*      */             
/* 1287 */             tcal.set(13, sec);
/* 1288 */             tcal.set(12, min);
/* 1289 */             tcal.set(11, hr);
/* 1290 */             tcal.set(5, day);
/* 1291 */             tcal.set(2, mon - 1);
/* 1292 */             Date nTime = tcal.getTime();
/* 1293 */             if (nTime.before(afterTime)) {
/* 1294 */               day = 1;
/* 1295 */               mon++;
/*      */             } 
/*      */           } 
/* 1298 */         } else if (this.nearestWeekday) {
/* 1299 */           t = day;
/* 1300 */           day = ((Integer)this.daysOfMonth.first()).intValue();
/*      */           
/* 1302 */           Calendar tcal = Calendar.getInstance(getTimeZone());
/* 1303 */           tcal.set(13, 0);
/* 1304 */           tcal.set(12, 0);
/* 1305 */           tcal.set(11, 0);
/* 1306 */           tcal.set(5, day);
/* 1307 */           tcal.set(2, mon - 1);
/* 1308 */           tcal.set(1, cl.get(1));
/*      */           
/* 1310 */           int ldom = getLastDayOfMonth(mon, cl.get(1));
/* 1311 */           int dow = tcal.get(7);
/*      */           
/* 1313 */           if (dow == 7 && day == 1) {
/* 1314 */             day += 2;
/* 1315 */           } else if (dow == 7) {
/* 1316 */             day--;
/* 1317 */           } else if (dow == 1 && day == ldom) {
/* 1318 */             day -= 2;
/* 1319 */           } else if (dow == 1) {
/* 1320 */             day++;
/*      */           } 
/*      */ 
/*      */           
/* 1324 */           tcal.set(13, sec);
/* 1325 */           tcal.set(12, min);
/* 1326 */           tcal.set(11, hr);
/* 1327 */           tcal.set(5, day);
/* 1328 */           tcal.set(2, mon - 1);
/* 1329 */           Date nTime = tcal.getTime();
/* 1330 */           if (nTime.before(afterTime)) {
/* 1331 */             day = ((Integer)this.daysOfMonth.first()).intValue();
/* 1332 */             mon++;
/*      */           } 
/* 1334 */         } else if (st != null && st.size() != 0) {
/* 1335 */           t = day;
/* 1336 */           day = ((Integer)st.first()).intValue();
/*      */           
/* 1338 */           int lastDay = getLastDayOfMonth(mon, cl.get(1));
/* 1339 */           if (day > lastDay) {
/* 1340 */             day = ((Integer)this.daysOfMonth.first()).intValue();
/* 1341 */             mon++;
/*      */           } 
/*      */         } else {
/* 1344 */           day = ((Integer)this.daysOfMonth.first()).intValue();
/* 1345 */           mon++;
/*      */         } 
/*      */         
/* 1348 */         if (day != t || mon != tmon) {
/* 1349 */           cl.set(13, 0);
/* 1350 */           cl.set(12, 0);
/* 1351 */           cl.set(11, 0);
/* 1352 */           cl.set(5, day);
/* 1353 */           cl.set(2, mon - 1);
/*      */ 
/*      */           
/*      */           continue;
/*      */         } 
/* 1358 */       } else if (dayOfWSpec && !dayOfMSpec) {
/* 1359 */         if (this.lastdayOfWeek) {
/*      */           
/* 1361 */           int dow = ((Integer)this.daysOfWeek.first()).intValue();
/*      */           
/* 1363 */           int cDow = cl.get(7);
/* 1364 */           int daysToAdd = 0;
/* 1365 */           if (cDow < dow) {
/* 1366 */             daysToAdd = dow - cDow;
/*      */           }
/* 1368 */           if (cDow > dow) {
/* 1369 */             daysToAdd = dow + 7 - cDow;
/*      */           }
/*      */           
/* 1372 */           int lDay = getLastDayOfMonth(mon, cl.get(1));
/*      */           
/* 1374 */           if (day + daysToAdd > lDay) {
/*      */             
/* 1376 */             cl.set(13, 0);
/* 1377 */             cl.set(12, 0);
/* 1378 */             cl.set(11, 0);
/* 1379 */             cl.set(5, 1);
/* 1380 */             cl.set(2, mon);
/*      */ 
/*      */             
/*      */             continue;
/*      */           } 
/*      */           
/* 1386 */           while (day + daysToAdd + 7 <= lDay) {
/* 1387 */             daysToAdd += 7;
/*      */           }
/*      */           
/* 1390 */           day += daysToAdd;
/*      */           
/* 1392 */           if (daysToAdd > 0) {
/* 1393 */             cl.set(13, 0);
/* 1394 */             cl.set(12, 0);
/* 1395 */             cl.set(11, 0);
/* 1396 */             cl.set(5, day);
/* 1397 */             cl.set(2, mon - 1);
/*      */ 
/*      */             
/*      */             continue;
/*      */           } 
/* 1402 */         } else if (this.nthdayOfWeek != 0) {
/*      */           
/* 1404 */           int dow = ((Integer)this.daysOfWeek.first()).intValue();
/*      */           
/* 1406 */           int cDow = cl.get(7);
/* 1407 */           int daysToAdd = 0;
/* 1408 */           if (cDow < dow) {
/* 1409 */             daysToAdd = dow - cDow;
/* 1410 */           } else if (cDow > dow) {
/* 1411 */             daysToAdd = dow + 7 - cDow;
/*      */           } 
/*      */           
/* 1414 */           boolean dayShifted = false;
/* 1415 */           if (daysToAdd > 0) {
/* 1416 */             dayShifted = true;
/*      */           }
/*      */           
/* 1419 */           day += daysToAdd;
/* 1420 */           int weekOfMonth = day / 7;
/* 1421 */           if (day % 7 > 0) {
/* 1422 */             weekOfMonth++;
/*      */           }
/*      */           
/* 1425 */           daysToAdd = (this.nthdayOfWeek - weekOfMonth) * 7;
/* 1426 */           day += daysToAdd;
/* 1427 */           if (daysToAdd < 0 || day > getLastDayOfMonth(mon, cl.get(1))) {
/*      */ 
/*      */             
/* 1430 */             cl.set(13, 0);
/* 1431 */             cl.set(12, 0);
/* 1432 */             cl.set(11, 0);
/* 1433 */             cl.set(5, 1);
/* 1434 */             cl.set(2, mon);
/*      */             continue;
/*      */           } 
/* 1437 */           if (daysToAdd > 0 || dayShifted) {
/* 1438 */             cl.set(13, 0);
/* 1439 */             cl.set(12, 0);
/* 1440 */             cl.set(11, 0);
/* 1441 */             cl.set(5, day);
/* 1442 */             cl.set(2, mon - 1);
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } else {
/* 1447 */           int cDow = cl.get(7);
/* 1448 */           int dow = ((Integer)this.daysOfWeek.first()).intValue();
/*      */           
/* 1450 */           st = this.daysOfWeek.tailSet(Integer.valueOf(cDow));
/* 1451 */           if (st != null && st.size() > 0) {
/* 1452 */             dow = ((Integer)st.first()).intValue();
/*      */           }
/*      */           
/* 1455 */           int daysToAdd = 0;
/* 1456 */           if (cDow < dow) {
/* 1457 */             daysToAdd = dow - cDow;
/*      */           }
/* 1459 */           if (cDow > dow) {
/* 1460 */             daysToAdd = dow + 7 - cDow;
/*      */           }
/*      */           
/* 1463 */           int lDay = getLastDayOfMonth(mon, cl.get(1));
/*      */           
/* 1465 */           if (day + daysToAdd > lDay) {
/*      */             
/* 1467 */             cl.set(13, 0);
/* 1468 */             cl.set(12, 0);
/* 1469 */             cl.set(11, 0);
/* 1470 */             cl.set(5, 1);
/* 1471 */             cl.set(2, mon);
/*      */             continue;
/*      */           } 
/* 1474 */           if (daysToAdd > 0) {
/* 1475 */             cl.set(13, 0);
/* 1476 */             cl.set(12, 0);
/* 1477 */             cl.set(11, 0);
/* 1478 */             cl.set(5, day + daysToAdd);
/* 1479 */             cl.set(2, mon - 1);
/*      */ 
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*      */       } else {
/* 1486 */         throw new UnsupportedOperationException("Support for specifying both a day-of-week AND a day-of-month parameter is not implemented.");
/*      */       } 
/*      */       
/* 1489 */       cl.set(5, day);
/*      */       
/* 1491 */       mon = cl.get(2) + 1;
/*      */ 
/*      */       
/* 1494 */       int year = cl.get(1);
/* 1495 */       t = -1;
/*      */ 
/*      */ 
/*      */       
/* 1499 */       if (year > MAX_YEAR) {
/* 1500 */         return null;
/*      */       }
/*      */ 
/*      */       
/* 1504 */       st = this.months.tailSet(Integer.valueOf(mon));
/* 1505 */       if (st != null && st.size() != 0) {
/* 1506 */         t = mon;
/* 1507 */         mon = ((Integer)st.first()).intValue();
/*      */       } else {
/* 1509 */         mon = ((Integer)this.months.first()).intValue();
/* 1510 */         year++;
/*      */       } 
/* 1512 */       if (mon != t) {
/* 1513 */         cl.set(13, 0);
/* 1514 */         cl.set(12, 0);
/* 1515 */         cl.set(11, 0);
/* 1516 */         cl.set(5, 1);
/* 1517 */         cl.set(2, mon - 1);
/*      */ 
/*      */         
/* 1520 */         cl.set(1, year);
/*      */         continue;
/*      */       } 
/* 1523 */       cl.set(2, mon - 1);
/*      */ 
/*      */ 
/*      */       
/* 1527 */       year = cl.get(1);
/* 1528 */       t = -1;
/*      */ 
/*      */       
/* 1531 */       st = this.years.tailSet(Integer.valueOf(year));
/* 1532 */       if (st != null && st.size() != 0) {
/* 1533 */         t = year;
/* 1534 */         year = ((Integer)st.first()).intValue();
/*      */       } else {
/* 1536 */         return null;
/*      */       } 
/*      */       
/* 1539 */       if (year != t) {
/* 1540 */         cl.set(13, 0);
/* 1541 */         cl.set(12, 0);
/* 1542 */         cl.set(11, 0);
/* 1543 */         cl.set(5, 1);
/* 1544 */         cl.set(2, 0);
/*      */ 
/*      */         
/* 1547 */         cl.set(1, year);
/*      */         continue;
/*      */       } 
/* 1550 */       cl.set(1, year);
/*      */       
/* 1552 */       gotOne = true;
/*      */     } 
/*      */     
/* 1555 */     return cl.getTime();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setCalendarHour(Calendar cal, int hour) {
/* 1566 */     cal.set(11, hour);
/* 1567 */     if (cal.get(11) != hour && hour != 24) {
/* 1568 */       cal.set(11, hour + 1);
/*      */     }
/*      */   }
/*      */   
/*      */   protected Date getTimeBefore(Date targetDate) {
/* 1573 */     Calendar cl = Calendar.getInstance(getTimeZone());
/*      */ 
/*      */     
/* 1576 */     Date start = targetDate;
/* 1577 */     long minIncrement = findMinIncrement();
/*      */     
/*      */     while (true) {
/* 1580 */       Date prevCheckDate = new Date(start.getTime() - minIncrement);
/* 1581 */       Date prevFireTime = getTimeAfter(prevCheckDate);
/* 1582 */       if (prevFireTime == null || prevFireTime.before(MIN_DATE)) {
/* 1583 */         return null;
/*      */       }
/* 1585 */       start = prevCheckDate;
/* 1586 */       if (prevFireTime.compareTo(targetDate) < 0)
/* 1587 */         return prevFireTime; 
/*      */     } 
/*      */   }
/*      */   public Date getPrevFireTime(Date targetDate) {
/* 1591 */     return getTimeBefore(targetDate);
/*      */   }
/*      */   
/*      */   private long findMinIncrement() {
/* 1595 */     if (this.seconds.size() != 1)
/* 1596 */       return (minInSet(this.seconds) * 1000); 
/* 1597 */     if (((Integer)this.seconds.first()).intValue() == 99) {
/* 1598 */       return 1000L;
/*      */     }
/* 1600 */     if (this.minutes.size() != 1)
/* 1601 */       return (minInSet(this.minutes) * 60000); 
/* 1602 */     if (((Integer)this.minutes.first()).intValue() == 99) {
/* 1603 */       return 60000L;
/*      */     }
/* 1605 */     if (this.hours.size() != 1)
/* 1606 */       return (minInSet(this.hours) * 3600000); 
/* 1607 */     if (((Integer)this.hours.first()).intValue() == 99) {
/* 1608 */       return 3600000L;
/*      */     }
/* 1610 */     return 86400000L;
/*      */   }
/*      */   
/*      */   private int minInSet(TreeSet<Integer> set) {
/* 1614 */     int previous = 0;
/* 1615 */     int min = Integer.MAX_VALUE;
/* 1616 */     boolean first = true;
/* 1617 */     for (Iterator<Integer> i$ = set.iterator(); i$.hasNext(); ) { int value = ((Integer)i$.next()).intValue();
/* 1618 */       if (first) {
/* 1619 */         previous = value;
/* 1620 */         first = false;
/*      */         continue;
/*      */       } 
/* 1623 */       int diff = value - previous;
/* 1624 */       if (diff < min) {
/* 1625 */         min = diff;
/*      */       } }
/*      */ 
/*      */     
/* 1629 */     return min;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Date getFinalFireTime() {
/* 1638 */     return null;
/*      */   }
/*      */   
/*      */   protected boolean isLeapYear(int year) {
/* 1642 */     return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
/*      */   }
/*      */ 
/*      */   
/*      */   protected int getLastDayOfMonth(int monthNum, int year) {
/* 1647 */     switch (monthNum) {
/*      */       case 1:
/* 1649 */         return 31;
/*      */       case 2:
/* 1651 */         return isLeapYear(year) ? 29 : 28;
/*      */       case 3:
/* 1653 */         return 31;
/*      */       case 4:
/* 1655 */         return 30;
/*      */       case 5:
/* 1657 */         return 31;
/*      */       case 6:
/* 1659 */         return 30;
/*      */       case 7:
/* 1661 */         return 31;
/*      */       case 8:
/* 1663 */         return 31;
/*      */       case 9:
/* 1665 */         return 30;
/*      */       case 10:
/* 1667 */         return 31;
/*      */       case 11:
/* 1669 */         return 30;
/*      */       case 12:
/* 1671 */         return 31;
/*      */     } 
/* 1673 */     throw new IllegalArgumentException("Illegal month number: " + monthNum);
/*      */   }
/*      */   
/*      */   private class ValueSet {
/*      */     public int value;
/*      */     public int pos;
/*      */     
/*      */     private ValueSet() {}
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\CronExpression.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */