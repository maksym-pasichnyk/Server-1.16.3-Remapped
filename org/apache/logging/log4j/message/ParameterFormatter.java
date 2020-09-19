/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.logging.log4j.util.StringBuilderFormattable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ParameterFormatter
/*     */ {
/*     */   static final String RECURSION_PREFIX = "[...";
/*     */   static final String RECURSION_SUFFIX = "...]";
/*     */   static final String ERROR_PREFIX = "[!!!";
/*     */   static final String ERROR_SEPARATOR = "=>";
/*     */   static final String ERROR_MSG_SEPARATOR = ":";
/*     */   static final String ERROR_SUFFIX = "!!!]";
/*     */   private static final char DELIM_START = '{';
/*     */   private static final char DELIM_STOP = '}';
/*     */   private static final char ESCAPE_CHAR = '\\';
/*  63 */   private static ThreadLocal<SimpleDateFormat> threadLocalSimpleDateFormat = new ThreadLocal<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int countArgumentPlaceholders(String messagePattern) {
/*  75 */     if (messagePattern == null) {
/*  76 */       return 0;
/*     */     }
/*  78 */     int length = messagePattern.length();
/*  79 */     int result = 0;
/*  80 */     boolean isEscaped = false;
/*  81 */     for (int i = 0; i < length - 1; i++) {
/*  82 */       char curChar = messagePattern.charAt(i);
/*  83 */       if (curChar == '\\') {
/*  84 */         isEscaped = !isEscaped;
/*  85 */       } else if (curChar == '{') {
/*  86 */         if (!isEscaped && messagePattern.charAt(i + 1) == '}') {
/*  87 */           result++;
/*  88 */           i++;
/*     */         } 
/*  90 */         isEscaped = false;
/*     */       } else {
/*  92 */         isEscaped = false;
/*     */       } 
/*     */     } 
/*  95 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int countArgumentPlaceholders2(String messagePattern, int[] indices) {
/* 105 */     if (messagePattern == null) {
/* 106 */       return 0;
/*     */     }
/* 108 */     int length = messagePattern.length();
/* 109 */     int result = 0;
/* 110 */     boolean isEscaped = false;
/* 111 */     for (int i = 0; i < length - 1; i++) {
/* 112 */       char curChar = messagePattern.charAt(i);
/* 113 */       if (curChar == '\\') {
/* 114 */         isEscaped = !isEscaped;
/* 115 */         indices[0] = -1;
/* 116 */         result++;
/* 117 */       } else if (curChar == '{') {
/* 118 */         if (!isEscaped && messagePattern.charAt(i + 1) == '}') {
/* 119 */           indices[result] = i;
/* 120 */           result++;
/* 121 */           i++;
/*     */         } 
/* 123 */         isEscaped = false;
/*     */       } else {
/* 125 */         isEscaped = false;
/*     */       } 
/*     */     } 
/* 128 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int countArgumentPlaceholders3(char[] messagePattern, int length, int[] indices) {
/* 138 */     int result = 0;
/* 139 */     boolean isEscaped = false;
/* 140 */     for (int i = 0; i < length - 1; i++) {
/* 141 */       char curChar = messagePattern[i];
/* 142 */       if (curChar == '\\') {
/* 143 */         isEscaped = !isEscaped;
/* 144 */       } else if (curChar == '{') {
/* 145 */         if (!isEscaped && messagePattern[i + 1] == '}') {
/* 146 */           indices[result] = i;
/* 147 */           result++;
/* 148 */           i++;
/*     */         } 
/* 150 */         isEscaped = false;
/*     */       } else {
/* 152 */         isEscaped = false;
/*     */       } 
/*     */     } 
/* 155 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String format(String messagePattern, Object[] arguments) {
/* 166 */     StringBuilder result = new StringBuilder();
/* 167 */     int argCount = (arguments == null) ? 0 : arguments.length;
/* 168 */     formatMessage(result, messagePattern, arguments, argCount);
/* 169 */     return result.toString();
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
/*     */   static void formatMessage2(StringBuilder buffer, String messagePattern, Object[] arguments, int argCount, int[] indices) {
/* 181 */     if (messagePattern == null || arguments == null || argCount == 0) {
/* 182 */       buffer.append(messagePattern);
/*     */       return;
/*     */     } 
/* 185 */     int previous = 0;
/* 186 */     for (int i = 0; i < argCount; i++) {
/* 187 */       buffer.append(messagePattern, previous, indices[i]);
/* 188 */       previous = indices[i] + 2;
/* 189 */       recursiveDeepToString(arguments[i], buffer, null);
/*     */     } 
/* 191 */     buffer.append(messagePattern, previous, messagePattern.length());
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
/*     */   static void formatMessage3(StringBuilder buffer, char[] messagePattern, int patternLength, Object[] arguments, int argCount, int[] indices) {
/* 203 */     if (messagePattern == null) {
/*     */       return;
/*     */     }
/* 206 */     if (arguments == null || argCount == 0) {
/* 207 */       buffer.append(messagePattern);
/*     */       return;
/*     */     } 
/* 210 */     int previous = 0;
/* 211 */     for (int i = 0; i < argCount; i++) {
/* 212 */       buffer.append(messagePattern, previous, indices[i]);
/* 213 */       previous = indices[i] + 2;
/* 214 */       recursiveDeepToString(arguments[i], buffer, null);
/*     */     } 
/* 216 */     buffer.append(messagePattern, previous, patternLength);
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
/*     */   static void formatMessage(StringBuilder buffer, String messagePattern, Object[] arguments, int argCount) {
/* 228 */     if (messagePattern == null || arguments == null || argCount == 0) {
/* 229 */       buffer.append(messagePattern);
/*     */       return;
/*     */     } 
/* 232 */     int escapeCounter = 0;
/* 233 */     int currentArgument = 0;
/* 234 */     int i = 0;
/* 235 */     int len = messagePattern.length();
/* 236 */     for (; i < len - 1; i++) {
/* 237 */       char curChar = messagePattern.charAt(i);
/* 238 */       if (curChar == '\\') {
/* 239 */         escapeCounter++;
/*     */       } else {
/* 241 */         if (isDelimPair(curChar, messagePattern, i)) {
/* 242 */           i++;
/*     */ 
/*     */           
/* 245 */           writeEscapedEscapeChars(escapeCounter, buffer);
/*     */           
/* 247 */           if (isOdd(escapeCounter)) {
/*     */             
/* 249 */             writeDelimPair(buffer);
/*     */           } else {
/*     */             
/* 252 */             writeArgOrDelimPair(arguments, argCount, currentArgument, buffer);
/* 253 */             currentArgument++;
/*     */           } 
/*     */         } else {
/* 256 */           handleLiteralChar(buffer, escapeCounter, curChar);
/*     */         } 
/* 258 */         escapeCounter = 0;
/*     */       } 
/*     */     } 
/* 261 */     handleRemainingCharIfAny(messagePattern, len, buffer, escapeCounter, i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isDelimPair(char curChar, String messagePattern, int curCharIndex) {
/* 271 */     return (curChar == '{' && messagePattern.charAt(curCharIndex + 1) == '}');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void handleRemainingCharIfAny(String messagePattern, int len, StringBuilder buffer, int escapeCounter, int i) {
/* 282 */     if (i == len - 1) {
/* 283 */       char curChar = messagePattern.charAt(i);
/* 284 */       handleLastChar(buffer, escapeCounter, curChar);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void handleLastChar(StringBuilder buffer, int escapeCounter, char curChar) {
/* 294 */     if (curChar == '\\') {
/* 295 */       writeUnescapedEscapeChars(escapeCounter + 1, buffer);
/*     */     } else {
/* 297 */       handleLiteralChar(buffer, escapeCounter, curChar);
/*     */     } 
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
/*     */   private static void handleLiteralChar(StringBuilder buffer, int escapeCounter, char curChar) {
/* 310 */     writeUnescapedEscapeChars(escapeCounter, buffer);
/* 311 */     buffer.append(curChar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void writeDelimPair(StringBuilder buffer) {
/* 320 */     buffer.append('{');
/* 321 */     buffer.append('}');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isOdd(int number) {
/* 330 */     return ((number & 0x1) == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void writeEscapedEscapeChars(int escapeCounter, StringBuilder buffer) {
/* 340 */     int escapedEscapes = escapeCounter >> 1;
/* 341 */     writeUnescapedEscapeChars(escapedEscapes, buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void writeUnescapedEscapeChars(int escapeCounter, StringBuilder buffer) {
/* 351 */     while (escapeCounter > 0) {
/* 352 */       buffer.append('\\');
/* 353 */       escapeCounter--;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void writeArgOrDelimPair(Object[] arguments, int argCount, int currentArgument, StringBuilder buffer) {
/* 365 */     if (currentArgument < argCount) {
/* 366 */       recursiveDeepToString(arguments[currentArgument], buffer, null);
/*     */     } else {
/* 368 */       writeDelimPair(buffer);
/*     */     } 
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
/*     */   static String deepToString(Object o) {
/* 391 */     if (o == null) {
/* 392 */       return null;
/*     */     }
/* 394 */     if (o instanceof String) {
/* 395 */       return (String)o;
/*     */     }
/* 397 */     StringBuilder str = new StringBuilder();
/* 398 */     Set<String> dejaVu = new HashSet<>();
/* 399 */     recursiveDeepToString(o, str, dejaVu);
/* 400 */     return str.toString();
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
/*     */   private static void recursiveDeepToString(Object o, StringBuilder str, Set<String> dejaVu) {
/* 428 */     if (appendSpecialTypes(o, str)) {
/*     */       return;
/*     */     }
/* 431 */     if (isMaybeRecursive(o)) {
/* 432 */       appendPotentiallyRecursiveValue(o, str, dejaVu);
/*     */     } else {
/* 434 */       tryObjectToString(o, str);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean appendSpecialTypes(Object o, StringBuilder str) {
/* 439 */     if (o == null || o instanceof String) {
/* 440 */       str.append((String)o);
/* 441 */       return true;
/* 442 */     }  if (o instanceof CharSequence) {
/* 443 */       str.append((CharSequence)o);
/* 444 */       return true;
/* 445 */     }  if (o instanceof StringBuilderFormattable) {
/* 446 */       ((StringBuilderFormattable)o).formatTo(str);
/* 447 */       return true;
/* 448 */     }  if (o instanceof Integer) {
/* 449 */       str.append(((Integer)o).intValue());
/* 450 */       return true;
/* 451 */     }  if (o instanceof Long) {
/* 452 */       str.append(((Long)o).longValue());
/* 453 */       return true;
/* 454 */     }  if (o instanceof Double) {
/* 455 */       str.append(((Double)o).doubleValue());
/* 456 */       return true;
/* 457 */     }  if (o instanceof Boolean) {
/* 458 */       str.append(((Boolean)o).booleanValue());
/* 459 */       return true;
/* 460 */     }  if (o instanceof Character) {
/* 461 */       str.append(((Character)o).charValue());
/* 462 */       return true;
/* 463 */     }  if (o instanceof Short) {
/* 464 */       str.append(((Short)o).shortValue());
/* 465 */       return true;
/* 466 */     }  if (o instanceof Float) {
/* 467 */       str.append(((Float)o).floatValue());
/* 468 */       return true;
/*     */     } 
/* 470 */     return appendDate(o, str);
/*     */   }
/*     */   
/*     */   private static boolean appendDate(Object o, StringBuilder str) {
/* 474 */     if (!(o instanceof Date)) {
/* 475 */       return false;
/*     */     }
/* 477 */     Date date = (Date)o;
/* 478 */     SimpleDateFormat format = getSimpleDateFormat();
/* 479 */     str.append(format.format(date));
/* 480 */     return true;
/*     */   }
/*     */   
/*     */   private static SimpleDateFormat getSimpleDateFormat() {
/* 484 */     SimpleDateFormat result = threadLocalSimpleDateFormat.get();
/* 485 */     if (result == null) {
/* 486 */       result = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
/* 487 */       threadLocalSimpleDateFormat.set(result);
/*     */     } 
/* 489 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isMaybeRecursive(Object o) {
/* 496 */     return (o.getClass().isArray() || o instanceof Map || o instanceof Collection);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void appendPotentiallyRecursiveValue(Object o, StringBuilder str, Set<String> dejaVu) {
/* 501 */     Class<?> oClass = o.getClass();
/* 502 */     if (oClass.isArray()) {
/* 503 */       appendArray(o, str, dejaVu, oClass);
/* 504 */     } else if (o instanceof Map) {
/* 505 */       appendMap(o, str, dejaVu);
/* 506 */     } else if (o instanceof Collection) {
/* 507 */       appendCollection(o, str, dejaVu);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void appendArray(Object o, StringBuilder str, Set<String> dejaVu, Class<?> oClass) {
/* 513 */     if (oClass == byte[].class) {
/* 514 */       str.append(Arrays.toString((byte[])o));
/* 515 */     } else if (oClass == short[].class) {
/* 516 */       str.append(Arrays.toString((short[])o));
/* 517 */     } else if (oClass == int[].class) {
/* 518 */       str.append(Arrays.toString((int[])o));
/* 519 */     } else if (oClass == long[].class) {
/* 520 */       str.append(Arrays.toString((long[])o));
/* 521 */     } else if (oClass == float[].class) {
/* 522 */       str.append(Arrays.toString((float[])o));
/* 523 */     } else if (oClass == double[].class) {
/* 524 */       str.append(Arrays.toString((double[])o));
/* 525 */     } else if (oClass == boolean[].class) {
/* 526 */       str.append(Arrays.toString((boolean[])o));
/* 527 */     } else if (oClass == char[].class) {
/* 528 */       str.append(Arrays.toString((char[])o));
/*     */     } else {
/* 530 */       if (dejaVu == null) {
/* 531 */         dejaVu = new HashSet<>();
/*     */       }
/*     */       
/* 534 */       String id = identityToString(o);
/* 535 */       if (dejaVu.contains(id)) {
/* 536 */         str.append("[...").append(id).append("...]");
/*     */       } else {
/* 538 */         dejaVu.add(id);
/* 539 */         Object[] oArray = (Object[])o;
/* 540 */         str.append('[');
/* 541 */         boolean first = true;
/* 542 */         for (Object current : oArray) {
/* 543 */           if (first) {
/* 544 */             first = false;
/*     */           } else {
/* 546 */             str.append(", ");
/*     */           } 
/* 548 */           recursiveDeepToString(current, str, new HashSet<>(dejaVu));
/*     */         } 
/* 550 */         str.append(']');
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void appendMap(Object o, StringBuilder str, Set<String> dejaVu) {
/* 558 */     if (dejaVu == null) {
/* 559 */       dejaVu = new HashSet<>();
/*     */     }
/* 561 */     String id = identityToString(o);
/* 562 */     if (dejaVu.contains(id)) {
/* 563 */       str.append("[...").append(id).append("...]");
/*     */     } else {
/* 565 */       dejaVu.add(id);
/* 566 */       Map<?, ?> oMap = (Map<?, ?>)o;
/* 567 */       str.append('{');
/* 568 */       boolean isFirst = true;
/* 569 */       for (Map.Entry<?, ?> o1 : oMap.entrySet()) {
/* 570 */         Map.Entry<?, ?> current = o1;
/* 571 */         if (isFirst) {
/* 572 */           isFirst = false;
/*     */         } else {
/* 574 */           str.append(", ");
/*     */         } 
/* 576 */         Object key = current.getKey();
/* 577 */         Object value = current.getValue();
/* 578 */         recursiveDeepToString(key, str, new HashSet<>(dejaVu));
/* 579 */         str.append('=');
/* 580 */         recursiveDeepToString(value, str, new HashSet<>(dejaVu));
/*     */       } 
/* 582 */       str.append('}');
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void appendCollection(Object o, StringBuilder str, Set<String> dejaVu) {
/* 588 */     if (dejaVu == null) {
/* 589 */       dejaVu = new HashSet<>();
/*     */     }
/* 591 */     String id = identityToString(o);
/* 592 */     if (dejaVu.contains(id)) {
/* 593 */       str.append("[...").append(id).append("...]");
/*     */     } else {
/* 595 */       dejaVu.add(id);
/* 596 */       Collection<?> oCol = (Collection)o;
/* 597 */       str.append('[');
/* 598 */       boolean isFirst = true;
/* 599 */       for (Object anOCol : oCol) {
/* 600 */         if (isFirst) {
/* 601 */           isFirst = false;
/*     */         } else {
/* 603 */           str.append(", ");
/*     */         } 
/* 605 */         recursiveDeepToString(anOCol, str, new HashSet<>(dejaVu));
/*     */       } 
/* 607 */       str.append(']');
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void tryObjectToString(Object o, StringBuilder str) {
/*     */     try {
/* 614 */       str.append(o.toString());
/* 615 */     } catch (Throwable t) {
/* 616 */       handleErrorInObjectToString(o, str, t);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void handleErrorInObjectToString(Object o, StringBuilder str, Throwable t) {
/* 621 */     str.append("[!!!");
/* 622 */     str.append(identityToString(o));
/* 623 */     str.append("=>");
/* 624 */     String msg = t.getMessage();
/* 625 */     String className = t.getClass().getName();
/* 626 */     str.append(className);
/* 627 */     if (!className.equals(msg)) {
/* 628 */       str.append(":");
/* 629 */       str.append(msg);
/*     */     } 
/* 631 */     str.append("!!!]");
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
/*     */   static String identityToString(Object obj) {
/* 655 */     if (obj == null) {
/* 656 */       return null;
/*     */     }
/* 658 */     return obj.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(obj));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\message\ParameterFormatter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */