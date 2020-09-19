/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StringUtil
/*     */ {
/*     */   public static final String EMPTY_STRING = "";
/*  30 */   public static final String NEWLINE = SystemPropertyUtil.get("line.separator", "\n");
/*     */   
/*     */   public static final char DOUBLE_QUOTE = '"';
/*     */   
/*     */   public static final char COMMA = ',';
/*     */   public static final char LINE_FEED = '\n';
/*     */   public static final char CARRIAGE_RETURN = '\r';
/*     */   public static final char TAB = '\t';
/*     */   public static final char SPACE = ' ';
/*  39 */   private static final String[] BYTE2HEX_PAD = new String[256];
/*  40 */   private static final String[] BYTE2HEX_NOPAD = new String[256];
/*     */ 
/*     */   
/*     */   private static final int CSV_NUMBER_ESCAPE_CHARACTERS = 7;
/*     */ 
/*     */   
/*     */   private static final char PACKAGE_SEPARATOR_CHAR = '.';
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  51 */     for (int i = 0; i < BYTE2HEX_PAD.length; i++) {
/*  52 */       String str = Integer.toHexString(i);
/*  53 */       BYTE2HEX_PAD[i] = (i > 15) ? str : ('0' + str);
/*  54 */       BYTE2HEX_NOPAD[i] = str;
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
/*     */   public static String substringAfter(String value, char delim) {
/*  68 */     int pos = value.indexOf(delim);
/*  69 */     if (pos >= 0) {
/*  70 */       return value.substring(pos + 1);
/*     */     }
/*  72 */     return null;
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
/*     */   public static boolean commonSuffixOfLength(String s, String p, int len) {
/*  84 */     return (s != null && p != null && len >= 0 && s.regionMatches(s.length() - len, p, p.length() - len, len));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String byteToHexStringPadded(int value) {
/*  91 */     return BYTE2HEX_PAD[value & 0xFF];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Appendable> T byteToHexStringPadded(T buf, int value) {
/*     */     try {
/*  99 */       buf.append(byteToHexStringPadded(value));
/* 100 */     } catch (IOException e) {
/* 101 */       PlatformDependent.throwException(e);
/*     */     } 
/* 103 */     return buf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toHexStringPadded(byte[] src) {
/* 110 */     return toHexStringPadded(src, 0, src.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toHexStringPadded(byte[] src, int offset, int length) {
/* 117 */     return ((StringBuilder)toHexStringPadded(new StringBuilder(length << 1), src, offset, length)).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Appendable> T toHexStringPadded(T dst, byte[] src) {
/* 124 */     return toHexStringPadded(dst, src, 0, src.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Appendable> T toHexStringPadded(T dst, byte[] src, int offset, int length) {
/* 131 */     int end = offset + length;
/* 132 */     for (int i = offset; i < end; i++) {
/* 133 */       byteToHexStringPadded(dst, src[i]);
/*     */     }
/* 135 */     return dst;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String byteToHexString(int value) {
/* 142 */     return BYTE2HEX_NOPAD[value & 0xFF];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Appendable> T byteToHexString(T buf, int value) {
/*     */     try {
/* 150 */       buf.append(byteToHexString(value));
/* 151 */     } catch (IOException e) {
/* 152 */       PlatformDependent.throwException(e);
/*     */     } 
/* 154 */     return buf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toHexString(byte[] src) {
/* 161 */     return toHexString(src, 0, src.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toHexString(byte[] src, int offset, int length) {
/* 168 */     return ((StringBuilder)toHexString(new StringBuilder(length << 1), src, offset, length)).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Appendable> T toHexString(T dst, byte[] src) {
/* 175 */     return toHexString(dst, src, 0, src.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Appendable> T toHexString(T dst, byte[] src, int offset, int length) {
/* 182 */     assert length >= 0;
/* 183 */     if (length == 0) {
/* 184 */       return dst;
/*     */     }
/*     */     
/* 187 */     int end = offset + length;
/* 188 */     int endMinusOne = end - 1;
/*     */     
/*     */     int i;
/*     */     
/* 192 */     for (i = offset; i < endMinusOne && 
/* 193 */       src[i] == 0; i++);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 198 */     byteToHexString(dst, src[i++]);
/* 199 */     int remaining = end - i;
/* 200 */     toHexStringPadded(dst, src, i, remaining);
/*     */     
/* 202 */     return dst;
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
/*     */   public static int decodeHexNibble(char c) {
/* 215 */     if (c >= '0' && c <= '9') {
/* 216 */       return c - 48;
/*     */     }
/* 218 */     if (c >= 'A' && c <= 'F') {
/* 219 */       return c - 55;
/*     */     }
/* 221 */     if (c >= 'a' && c <= 'f') {
/* 222 */       return c - 87;
/*     */     }
/* 224 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte decodeHexByte(CharSequence s, int pos) {
/* 231 */     int hi = decodeHexNibble(s.charAt(pos));
/* 232 */     int lo = decodeHexNibble(s.charAt(pos + 1));
/* 233 */     if (hi == -1 || lo == -1) {
/* 234 */       throw new IllegalArgumentException(String.format("invalid hex byte '%s' at index %d of '%s'", new Object[] { s
/* 235 */               .subSequence(pos, pos + 2), Integer.valueOf(pos), s }));
/*     */     }
/* 237 */     return (byte)((hi << 4) + lo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decodeHexDump(CharSequence hexDump, int fromIndex, int length) {
/* 248 */     if (length < 0 || (length & 0x1) != 0) {
/* 249 */       throw new IllegalArgumentException("length: " + length);
/*     */     }
/* 251 */     if (length == 0) {
/* 252 */       return EmptyArrays.EMPTY_BYTES;
/*     */     }
/* 254 */     byte[] bytes = new byte[length >>> 1];
/* 255 */     for (int i = 0; i < length; i += 2) {
/* 256 */       bytes[i >>> 1] = decodeHexByte(hexDump, fromIndex + i);
/*     */     }
/* 258 */     return bytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decodeHexDump(CharSequence hexDump) {
/* 265 */     return decodeHexDump(hexDump, 0, hexDump.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String simpleClassName(Object o) {
/* 272 */     if (o == null) {
/* 273 */       return "null_object";
/*     */     }
/* 275 */     return simpleClassName(o.getClass());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String simpleClassName(Class<?> clazz) {
/* 284 */     String className = ((Class)ObjectUtil.<Class<?>>checkNotNull(clazz, "clazz")).getName();
/* 285 */     int lastDotIdx = className.lastIndexOf('.');
/* 286 */     if (lastDotIdx > -1) {
/* 287 */       return className.substring(lastDotIdx + 1);
/*     */     }
/* 289 */     return className;
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
/*     */   public static CharSequence escapeCsv(CharSequence value) {
/* 301 */     return escapeCsv(value, false);
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
/*     */   public static CharSequence escapeCsv(CharSequence value, boolean trimWhiteSpace) {
/* 315 */     int start, last, length = ((CharSequence)ObjectUtil.<CharSequence>checkNotNull(value, "value")).length();
/*     */ 
/*     */     
/* 318 */     if (trimWhiteSpace) {
/* 319 */       start = indexOfFirstNonOwsChar(value, length);
/* 320 */       last = indexOfLastNonOwsChar(value, start, length);
/*     */     } else {
/* 322 */       start = 0;
/* 323 */       last = length - 1;
/*     */     } 
/* 325 */     if (start > last) {
/* 326 */       return "";
/*     */     }
/*     */     
/* 329 */     int firstUnescapedSpecial = -1;
/* 330 */     boolean quoted = false;
/* 331 */     if (isDoubleQuote(value.charAt(start))) {
/* 332 */       quoted = (isDoubleQuote(value.charAt(last)) && last > start);
/* 333 */       if (quoted) {
/* 334 */         start++;
/* 335 */         last--;
/*     */       } else {
/* 337 */         firstUnescapedSpecial = start;
/*     */       } 
/*     */     } 
/*     */     
/* 341 */     if (firstUnescapedSpecial < 0) {
/* 342 */       if (quoted) {
/* 343 */         for (int j = start; j <= last; j++) {
/* 344 */           if (isDoubleQuote(value.charAt(j))) {
/* 345 */             if (j == last || !isDoubleQuote(value.charAt(j + 1))) {
/* 346 */               firstUnescapedSpecial = j;
/*     */               break;
/*     */             } 
/* 349 */             j++;
/*     */           } 
/*     */         } 
/*     */       } else {
/* 353 */         for (int j = start; j <= last; j++) {
/* 354 */           char c = value.charAt(j);
/* 355 */           if (c == '\n' || c == '\r' || c == ',') {
/* 356 */             firstUnescapedSpecial = j;
/*     */             break;
/*     */           } 
/* 359 */           if (isDoubleQuote(c)) {
/* 360 */             if (j == last || !isDoubleQuote(value.charAt(j + 1))) {
/* 361 */               firstUnescapedSpecial = j;
/*     */               break;
/*     */             } 
/* 364 */             j++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 369 */       if (firstUnescapedSpecial < 0)
/*     */       {
/*     */ 
/*     */         
/* 373 */         return quoted ? value.subSequence(start - 1, last + 2) : value.subSequence(start, last + 1);
/*     */       }
/*     */     } 
/*     */     
/* 377 */     StringBuilder result = new StringBuilder(last - start + 1 + 7);
/* 378 */     result.append('"').append(value, start, firstUnescapedSpecial);
/* 379 */     for (int i = firstUnescapedSpecial; i <= last; i++) {
/* 380 */       char c = value.charAt(i);
/* 381 */       if (isDoubleQuote(c)) {
/* 382 */         result.append('"');
/* 383 */         if (i < last && isDoubleQuote(value.charAt(i + 1))) {
/* 384 */           i++;
/*     */         }
/*     */       } 
/* 387 */       result.append(c);
/*     */     } 
/* 389 */     return result.append('"');
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
/*     */   public static CharSequence unescapeCsv(CharSequence value) {
/* 401 */     int length = ((CharSequence)ObjectUtil.<CharSequence>checkNotNull(value, "value")).length();
/* 402 */     if (length == 0) {
/* 403 */       return value;
/*     */     }
/* 405 */     int last = length - 1;
/* 406 */     boolean quoted = (isDoubleQuote(value.charAt(0)) && isDoubleQuote(value.charAt(last)) && length != 1);
/* 407 */     if (!quoted) {
/* 408 */       validateCsvFormat(value);
/* 409 */       return value;
/*     */     } 
/* 411 */     StringBuilder unescaped = InternalThreadLocalMap.get().stringBuilder();
/* 412 */     for (int i = 1; i < last; i++) {
/* 413 */       char current = value.charAt(i);
/* 414 */       if (current == '"') {
/* 415 */         if (isDoubleQuote(value.charAt(i + 1)) && i + 1 != last) {
/*     */ 
/*     */           
/* 418 */           i++;
/*     */         } else {
/*     */           
/* 421 */           throw newInvalidEscapedCsvFieldException(value, i);
/*     */         } 
/*     */       }
/* 424 */       unescaped.append(current);
/*     */     } 
/* 426 */     return unescaped.toString();
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
/*     */   public static List<CharSequence> unescapeCsvFields(CharSequence value) {
/* 438 */     List<CharSequence> unescaped = new ArrayList<CharSequence>(2);
/* 439 */     StringBuilder current = InternalThreadLocalMap.get().stringBuilder();
/* 440 */     boolean quoted = false;
/* 441 */     int last = value.length() - 1;
/* 442 */     for (int i = 0; i <= last; i++) {
/* 443 */       char c = value.charAt(i);
/* 444 */       if (quoted) {
/* 445 */         char next; switch (c) {
/*     */           case '"':
/* 447 */             if (i == last) {
/*     */               
/* 449 */               unescaped.add(current.toString());
/* 450 */               return unescaped;
/*     */             } 
/* 452 */             next = value.charAt(++i);
/* 453 */             if (next == '"') {
/*     */               
/* 455 */               current.append('"');
/*     */               break;
/*     */             } 
/* 458 */             if (next == ',') {
/*     */               
/* 460 */               quoted = false;
/* 461 */               unescaped.add(current.toString());
/* 462 */               current.setLength(0);
/*     */               
/*     */               break;
/*     */             } 
/* 466 */             throw newInvalidEscapedCsvFieldException(value, i - 1);
/*     */           default:
/* 468 */             current.append(c); break;
/*     */         } 
/*     */       } else {
/* 471 */         switch (c) {
/*     */           
/*     */           case ',':
/* 474 */             unescaped.add(current.toString());
/* 475 */             current.setLength(0);
/*     */             break;
/*     */           case '"':
/* 478 */             if (current.length() == 0) {
/* 479 */               quoted = true;
/*     */               break;
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           case '\n':
/*     */           case '\r':
/* 488 */             throw newInvalidEscapedCsvFieldException(value, i);
/*     */           default:
/* 490 */             current.append(c); break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 494 */     if (quoted) {
/* 495 */       throw newInvalidEscapedCsvFieldException(value, last);
/*     */     }
/* 497 */     unescaped.add(current.toString());
/* 498 */     return unescaped;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void validateCsvFormat(CharSequence value) {
/* 507 */     int length = value.length();
/* 508 */     for (int i = 0; i < length; i++) {
/* 509 */       switch (value.charAt(i)) {
/*     */         
/*     */         case '\n':
/*     */         case '\r':
/*     */         case '"':
/*     */         case ',':
/* 515 */           throw newInvalidEscapedCsvFieldException(value, i);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static IllegalArgumentException newInvalidEscapedCsvFieldException(CharSequence value, int index) {
/* 522 */     return new IllegalArgumentException("invalid escaped CSV field: " + value + " index: " + index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int length(String s) {
/* 529 */     return (s == null) ? 0 : s.length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNullOrEmpty(String s) {
/* 536 */     return (s == null || s.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int indexOfNonWhiteSpace(CharSequence seq, int offset) {
/* 547 */     for (; offset < seq.length(); offset++) {
/* 548 */       if (!Character.isWhitespace(seq.charAt(offset))) {
/* 549 */         return offset;
/*     */       }
/*     */     } 
/* 552 */     return -1;
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
/*     */   public static boolean isSurrogate(char c) {
/* 564 */     return (c >= '?' && c <= '?');
/*     */   }
/*     */   
/*     */   private static boolean isDoubleQuote(char c) {
/* 568 */     return (c == '"');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean endsWith(CharSequence s, char c) {
/* 579 */     int len = s.length();
/* 580 */     return (len > 0 && s.charAt(len - 1) == c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSequence trimOws(CharSequence value) {
/* 591 */     int length = value.length();
/* 592 */     if (length == 0) {
/* 593 */       return value;
/*     */     }
/* 595 */     int start = indexOfFirstNonOwsChar(value, length);
/* 596 */     int end = indexOfLastNonOwsChar(value, start, length);
/* 597 */     return (start == 0 && end == length - 1) ? value : value.subSequence(start, end + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int indexOfFirstNonOwsChar(CharSequence value, int length) {
/* 604 */     int i = 0;
/* 605 */     while (i < length && isOws(value.charAt(i))) {
/* 606 */       i++;
/*     */     }
/* 608 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int indexOfLastNonOwsChar(CharSequence value, int start, int length) {
/* 615 */     int i = length - 1;
/* 616 */     while (i > start && isOws(value.charAt(i))) {
/* 617 */       i--;
/*     */     }
/* 619 */     return i;
/*     */   }
/*     */   
/*     */   private static boolean isOws(char c) {
/* 623 */     return (c == ' ' || c == '\t');
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\StringUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */