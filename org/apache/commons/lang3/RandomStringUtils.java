/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.util.Random;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RandomStringUtils
/*     */ {
/*  42 */   private static final Random RANDOM = new Random();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String random(int count) {
/*  68 */     return random(count, false, false);
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
/*     */   public static String randomAscii(int count) {
/*  82 */     return random(count, 32, 127, false, false);
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
/*     */   public static String randomAscii(int minLengthInclusive, int maxLengthExclusive) {
/*  98 */     return randomAscii(RandomUtils.nextInt(minLengthInclusive, maxLengthExclusive));
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
/*     */   public static String randomAlphabetic(int count) {
/* 112 */     return random(count, true, false);
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
/*     */   public static String randomAlphabetic(int minLengthInclusive, int maxLengthExclusive) {
/* 127 */     return randomAlphabetic(RandomUtils.nextInt(minLengthInclusive, maxLengthExclusive));
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
/*     */   public static String randomAlphanumeric(int count) {
/* 141 */     return random(count, true, true);
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
/*     */   public static String randomAlphanumeric(int minLengthInclusive, int maxLengthExclusive) {
/* 156 */     return randomAlphanumeric(RandomUtils.nextInt(minLengthInclusive, maxLengthExclusive));
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
/*     */   public static String randomGraph(int count) {
/* 171 */     return random(count, 33, 126, false, false);
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
/*     */   public static String randomGraph(int minLengthInclusive, int maxLengthExclusive) {
/* 186 */     return randomGraph(RandomUtils.nextInt(minLengthInclusive, maxLengthExclusive));
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
/*     */   public static String randomNumeric(int count) {
/* 200 */     return random(count, false, true);
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
/*     */   public static String randomNumeric(int minLengthInclusive, int maxLengthExclusive) {
/* 215 */     return randomNumeric(RandomUtils.nextInt(minLengthInclusive, maxLengthExclusive));
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
/*     */   public static String randomPrint(int count) {
/* 230 */     return random(count, 32, 126, false, false);
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
/*     */   public static String randomPrint(int minLengthInclusive, int maxLengthExclusive) {
/* 245 */     return randomPrint(RandomUtils.nextInt(minLengthInclusive, maxLengthExclusive));
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
/*     */   public static String random(int count, boolean letters, boolean numbers) {
/* 263 */     return random(count, 0, 0, letters, numbers);
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
/*     */   public static String random(int count, int start, int end, boolean letters, boolean numbers) {
/* 283 */     return random(count, start, end, letters, numbers, null, RANDOM);
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
/*     */   public static String random(int count, int start, int end, boolean letters, boolean numbers, char... chars) {
/* 307 */     return random(count, start, end, letters, numbers, chars, RANDOM);
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
/*     */   public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random) {
/* 345 */     if (count == 0)
/* 346 */       return ""; 
/* 347 */     if (count < 0) {
/* 348 */       throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
/*     */     }
/* 350 */     if (chars != null && chars.length == 0) {
/* 351 */       throw new IllegalArgumentException("The chars array must not be empty");
/*     */     }
/*     */     
/* 354 */     if (start == 0 && end == 0) {
/* 355 */       if (chars != null) {
/* 356 */         end = chars.length;
/*     */       }
/* 358 */       else if (!letters && !numbers) {
/* 359 */         end = Integer.MAX_VALUE;
/*     */       } else {
/* 361 */         end = 123;
/* 362 */         start = 32;
/*     */       }
/*     */     
/*     */     }
/* 366 */     else if (end <= start) {
/* 367 */       throw new IllegalArgumentException("Parameter end (" + end + ") must be greater than start (" + start + ")");
/*     */     } 
/*     */ 
/*     */     
/* 371 */     char[] buffer = new char[count];
/* 372 */     int gap = end - start;
/*     */     
/* 374 */     while (count-- != 0) {
/*     */       char ch;
/* 376 */       if (chars == null) {
/* 377 */         ch = (char)(random.nextInt(gap) + start);
/*     */       } else {
/* 379 */         ch = chars[random.nextInt(gap) + start];
/*     */       } 
/* 381 */       if ((letters && Character.isLetter(ch)) || (numbers && 
/* 382 */         Character.isDigit(ch)) || (!letters && !numbers)) {
/*     */         
/* 384 */         if (ch >= '?' && ch <= '?') {
/* 385 */           if (count == 0) {
/* 386 */             count++;
/*     */             continue;
/*     */           } 
/* 389 */           buffer[count] = ch;
/* 390 */           count--;
/* 391 */           buffer[count] = (char)(55296 + random.nextInt(128)); continue;
/*     */         } 
/* 393 */         if (ch >= '?' && ch <= '?') {
/* 394 */           if (count == 0) {
/* 395 */             count++;
/*     */             continue;
/*     */           } 
/* 398 */           buffer[count] = (char)(56320 + random.nextInt(128));
/* 399 */           count--;
/* 400 */           buffer[count] = ch; continue;
/*     */         } 
/* 402 */         if (ch >= '?' && ch <= '?') {
/*     */           
/* 404 */           count++; continue;
/*     */         } 
/* 406 */         buffer[count] = ch;
/*     */         continue;
/*     */       } 
/* 409 */       count++;
/*     */     } 
/*     */     
/* 412 */     return new String(buffer);
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
/*     */   public static String random(int count, String chars) {
/* 430 */     if (chars == null) {
/* 431 */       return random(count, 0, 0, false, false, null, RANDOM);
/*     */     }
/* 433 */     return random(count, chars.toCharArray());
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
/*     */   public static String random(int count, char... chars) {
/* 449 */     if (chars == null) {
/* 450 */       return random(count, 0, 0, false, false, null, RANDOM);
/*     */     }
/* 452 */     return random(count, 0, chars.length, false, false, chars, RANDOM);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\RandomStringUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */