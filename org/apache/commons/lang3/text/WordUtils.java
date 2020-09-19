/*     */ package org.apache.commons.lang3.text;
/*     */ 
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.commons.lang3.SystemUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WordUtils
/*     */ {
/*     */   public static String wrap(String str, int wrapLength) {
/* 100 */     return wrap(str, wrapLength, null, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String wrap(String str, int wrapLength, String newLineStr, boolean wrapLongWords) {
/* 176 */     return wrap(str, wrapLength, newLineStr, wrapLongWords, " ");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String wrap(String str, int wrapLength, String newLineStr, boolean wrapLongWords, String wrapOn) {
/* 269 */     if (str == null) {
/* 270 */       return null;
/*     */     }
/* 272 */     if (newLineStr == null) {
/* 273 */       newLineStr = SystemUtils.LINE_SEPARATOR;
/*     */     }
/* 275 */     if (wrapLength < 1) {
/* 276 */       wrapLength = 1;
/*     */     }
/* 278 */     if (StringUtils.isBlank(wrapOn)) {
/* 279 */       wrapOn = " ";
/*     */     }
/* 281 */     Pattern patternToWrapOn = Pattern.compile(wrapOn);
/* 282 */     int inputLineLength = str.length();
/* 283 */     int offset = 0;
/* 284 */     StringBuilder wrappedLine = new StringBuilder(inputLineLength + 32);
/*     */     
/* 286 */     while (offset < inputLineLength) {
/* 287 */       int spaceToWrapAt = -1;
/* 288 */       Matcher matcher = patternToWrapOn.matcher(str.substring(offset, Math.min(offset + wrapLength + 1, inputLineLength)));
/* 289 */       if (matcher.find()) {
/* 290 */         if (matcher.start() == 0) {
/* 291 */           offset += matcher.end();
/*     */           continue;
/*     */         } 
/* 294 */         spaceToWrapAt = matcher.start();
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 299 */       if (inputLineLength - offset <= wrapLength) {
/*     */         break;
/*     */       }
/*     */       
/* 303 */       while (matcher.find()) {
/* 304 */         spaceToWrapAt = matcher.start() + offset;
/*     */       }
/*     */       
/* 307 */       if (spaceToWrapAt >= offset) {
/*     */         
/* 309 */         wrappedLine.append(str.substring(offset, spaceToWrapAt));
/* 310 */         wrappedLine.append(newLineStr);
/* 311 */         offset = spaceToWrapAt + 1;
/*     */         
/*     */         continue;
/*     */       } 
/* 315 */       if (wrapLongWords) {
/*     */         
/* 317 */         wrappedLine.append(str.substring(offset, wrapLength + offset));
/* 318 */         wrappedLine.append(newLineStr);
/* 319 */         offset += wrapLength;
/*     */         continue;
/*     */       } 
/* 322 */       matcher = patternToWrapOn.matcher(str.substring(offset + wrapLength));
/* 323 */       if (matcher.find()) {
/* 324 */         spaceToWrapAt = matcher.start() + offset + wrapLength;
/*     */       }
/*     */       
/* 327 */       if (spaceToWrapAt >= 0) {
/* 328 */         wrappedLine.append(str.substring(offset, spaceToWrapAt));
/* 329 */         wrappedLine.append(newLineStr);
/* 330 */         offset = spaceToWrapAt + 1; continue;
/*     */       } 
/* 332 */       wrappedLine.append(str.substring(offset));
/* 333 */       offset = inputLineLength;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 340 */     wrappedLine.append(str.substring(offset));
/*     */     
/* 342 */     return wrappedLine.toString();
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
/*     */   public static String capitalize(String str) {
/* 370 */     return capitalize(str, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String capitalize(String str, char... delimiters) {
/* 403 */     int delimLen = (delimiters == null) ? -1 : delimiters.length;
/* 404 */     if (StringUtils.isEmpty(str) || delimLen == 0) {
/* 405 */       return str;
/*     */     }
/* 407 */     char[] buffer = str.toCharArray();
/* 408 */     boolean capitalizeNext = true;
/* 409 */     for (int i = 0; i < buffer.length; i++) {
/* 410 */       char ch = buffer[i];
/* 411 */       if (isDelimiter(ch, delimiters)) {
/* 412 */         capitalizeNext = true;
/* 413 */       } else if (capitalizeNext) {
/* 414 */         buffer[i] = Character.toTitleCase(ch);
/* 415 */         capitalizeNext = false;
/*     */       } 
/*     */     } 
/* 418 */     return new String(buffer);
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
/*     */   public static String capitalizeFully(String str) {
/* 442 */     return capitalizeFully(str, null);
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
/*     */   
/*     */   public static String capitalizeFully(String str, char... delimiters) {
/* 472 */     int delimLen = (delimiters == null) ? -1 : delimiters.length;
/* 473 */     if (StringUtils.isEmpty(str) || delimLen == 0) {
/* 474 */       return str;
/*     */     }
/* 476 */     str = str.toLowerCase();
/* 477 */     return capitalize(str, delimiters);
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
/*     */   public static String uncapitalize(String str) {
/* 499 */     return uncapitalize(str, null);
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
/*     */   public static String uncapitalize(String str, char... delimiters) {
/* 528 */     int delimLen = (delimiters == null) ? -1 : delimiters.length;
/* 529 */     if (StringUtils.isEmpty(str) || delimLen == 0) {
/* 530 */       return str;
/*     */     }
/* 532 */     char[] buffer = str.toCharArray();
/* 533 */     boolean uncapitalizeNext = true;
/* 534 */     for (int i = 0; i < buffer.length; i++) {
/* 535 */       char ch = buffer[i];
/* 536 */       if (isDelimiter(ch, delimiters)) {
/* 537 */         uncapitalizeNext = true;
/* 538 */       } else if (uncapitalizeNext) {
/* 539 */         buffer[i] = Character.toLowerCase(ch);
/* 540 */         uncapitalizeNext = false;
/*     */       } 
/*     */     } 
/* 543 */     return new String(buffer);
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
/*     */   public static String swapCase(String str) {
/* 570 */     if (StringUtils.isEmpty(str)) {
/* 571 */       return str;
/*     */     }
/* 573 */     char[] buffer = str.toCharArray();
/*     */     
/* 575 */     boolean whitespace = true;
/*     */     
/* 577 */     for (int i = 0; i < buffer.length; i++) {
/* 578 */       char ch = buffer[i];
/* 579 */       if (Character.isUpperCase(ch)) {
/* 580 */         buffer[i] = Character.toLowerCase(ch);
/* 581 */         whitespace = false;
/* 582 */       } else if (Character.isTitleCase(ch)) {
/* 583 */         buffer[i] = Character.toLowerCase(ch);
/* 584 */         whitespace = false;
/* 585 */       } else if (Character.isLowerCase(ch)) {
/* 586 */         if (whitespace) {
/* 587 */           buffer[i] = Character.toTitleCase(ch);
/* 588 */           whitespace = false;
/*     */         } else {
/* 590 */           buffer[i] = Character.toUpperCase(ch);
/*     */         } 
/*     */       } else {
/* 593 */         whitespace = Character.isWhitespace(ch);
/*     */       } 
/*     */     } 
/* 596 */     return new String(buffer);
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
/*     */   public static String initials(String str) {
/* 622 */     return initials(str, null);
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
/*     */   
/*     */   public static String initials(String str, char... delimiters) {
/* 652 */     if (StringUtils.isEmpty(str)) {
/* 653 */       return str;
/*     */     }
/* 655 */     if (delimiters != null && delimiters.length == 0) {
/* 656 */       return "";
/*     */     }
/* 658 */     int strLen = str.length();
/* 659 */     char[] buf = new char[strLen / 2 + 1];
/* 660 */     int count = 0;
/* 661 */     boolean lastWasGap = true;
/* 662 */     for (int i = 0; i < strLen; i++) {
/* 663 */       char ch = str.charAt(i);
/*     */       
/* 665 */       if (isDelimiter(ch, delimiters)) {
/* 666 */         lastWasGap = true;
/* 667 */       } else if (lastWasGap) {
/* 668 */         buf[count++] = ch;
/* 669 */         lastWasGap = false;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 674 */     return new String(buf, 0, count);
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
/*     */   public static boolean containsAllWords(CharSequence word, CharSequence... words) {
/* 702 */     if (StringUtils.isEmpty(word) || ArrayUtils.isEmpty((Object[])words)) {
/* 703 */       return false;
/*     */     }
/* 705 */     for (CharSequence w : words) {
/* 706 */       if (StringUtils.isBlank(w)) {
/* 707 */         return false;
/*     */       }
/* 709 */       Pattern p = Pattern.compile(".*\\b" + w + "\\b.*");
/* 710 */       if (!p.matcher(word).matches()) {
/* 711 */         return false;
/*     */       }
/*     */     } 
/* 714 */     return true;
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
/*     */   private static boolean isDelimiter(char ch, char[] delimiters) {
/* 726 */     if (delimiters == null) {
/* 727 */       return Character.isWhitespace(ch);
/*     */     }
/* 729 */     for (char delimiter : delimiters) {
/* 730 */       if (ch == delimiter) {
/* 731 */         return true;
/*     */       }
/*     */     } 
/* 734 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\text\WordUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */