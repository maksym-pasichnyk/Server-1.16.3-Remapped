/*     */ package it.unimi.dsi.fastutil;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.LongComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BigArrays
/*     */ {
/*     */   public static final int SEGMENT_SHIFT = 27;
/*     */   public static final int SEGMENT_SIZE = 134217728;
/*     */   public static final int SEGMENT_MASK = 134217727;
/*     */   private static final int SMALL = 7;
/*     */   private static final int MEDIUM = 40;
/*     */   
/*     */   public static int segment(long index) {
/* 190 */     return (int)(index >>> 27L);
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
/*     */   public static int displacement(long index) {
/* 202 */     return (int)(index & 0x7FFFFFFL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long start(int segment) {
/* 213 */     return segment << 27L;
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
/*     */   public static long index(int segment, int displacement) {
/* 229 */     return start(segment) + displacement;
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
/*     */   public static void ensureFromTo(long bigArrayLength, long from, long to) {
/* 252 */     if (from < 0L) throw new ArrayIndexOutOfBoundsException("Start index (" + from + ") is negative"); 
/* 253 */     if (from > to) throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")"); 
/* 254 */     if (to > bigArrayLength) throw new ArrayIndexOutOfBoundsException("End index (" + to + ") is greater than big-array length (" + bigArrayLength + ")");
/*     */   
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
/*     */   public static void ensureOffsetLength(long bigArrayLength, long offset, long length) {
/* 278 */     if (offset < 0L) throw new ArrayIndexOutOfBoundsException("Offset (" + offset + ") is negative"); 
/* 279 */     if (length < 0L) throw new IllegalArgumentException("Length (" + length + ") is negative"); 
/* 280 */     if (offset + length > bigArrayLength) throw new ArrayIndexOutOfBoundsException("Last index (" + (offset + length) + ") is greater than big-array length (" + bigArrayLength + ")");
/*     */   
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
/*     */   public static void ensureLength(long bigArrayLength) {
/* 293 */     if (bigArrayLength < 0L) throw new IllegalArgumentException("Negative big-array size: " + bigArrayLength); 
/* 294 */     if (bigArrayLength >= 288230376017494016L) throw new IllegalArgumentException("Big-array size too big: " + bigArrayLength);
/*     */   
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
/*     */   private static void inPlaceMerge(long from, long mid, long to, LongComparator comp, BigSwapper swapper) {
/*     */     // Byte code:
/*     */     //   0: lload_0
/*     */     //   1: lload_2
/*     */     //   2: lcmp
/*     */     //   3: ifge -> 13
/*     */     //   6: lload_2
/*     */     //   7: lload #4
/*     */     //   9: lcmp
/*     */     //   10: iflt -> 14
/*     */     //   13: return
/*     */     //   14: lload #4
/*     */     //   16: lload_0
/*     */     //   17: lsub
/*     */     //   18: ldc2_w 2
/*     */     //   21: lcmp
/*     */     //   22: ifne -> 47
/*     */     //   25: aload #6
/*     */     //   27: lload_2
/*     */     //   28: lload_0
/*     */     //   29: invokeinterface compare : (JJ)I
/*     */     //   34: ifge -> 46
/*     */     //   37: aload #7
/*     */     //   39: lload_0
/*     */     //   40: lload_2
/*     */     //   41: invokeinterface swap : (JJ)V
/*     */     //   46: return
/*     */     //   47: lload_2
/*     */     //   48: lload_0
/*     */     //   49: lsub
/*     */     //   50: lload #4
/*     */     //   52: lload_2
/*     */     //   53: lsub
/*     */     //   54: lcmp
/*     */     //   55: ifle -> 84
/*     */     //   58: lload_0
/*     */     //   59: lload_2
/*     */     //   60: lload_0
/*     */     //   61: lsub
/*     */     //   62: ldc2_w 2
/*     */     //   65: ldiv
/*     */     //   66: ladd
/*     */     //   67: lstore #8
/*     */     //   69: lload_2
/*     */     //   70: lload #4
/*     */     //   72: lload #8
/*     */     //   74: aload #6
/*     */     //   76: invokestatic lowerBound : (JJJLit/unimi/dsi/fastutil/longs/LongComparator;)J
/*     */     //   79: lstore #10
/*     */     //   81: goto -> 107
/*     */     //   84: lload_2
/*     */     //   85: lload #4
/*     */     //   87: lload_2
/*     */     //   88: lsub
/*     */     //   89: ldc2_w 2
/*     */     //   92: ldiv
/*     */     //   93: ladd
/*     */     //   94: lstore #10
/*     */     //   96: lload_0
/*     */     //   97: lload_2
/*     */     //   98: lload #10
/*     */     //   100: aload #6
/*     */     //   102: invokestatic upperBound : (JJJLit/unimi/dsi/fastutil/longs/LongComparator;)J
/*     */     //   105: lstore #8
/*     */     //   107: lload #8
/*     */     //   109: lstore #12
/*     */     //   111: lload_2
/*     */     //   112: lstore #14
/*     */     //   114: lload #10
/*     */     //   116: lstore #16
/*     */     //   118: lload #14
/*     */     //   120: lload #12
/*     */     //   122: lcmp
/*     */     //   123: ifeq -> 254
/*     */     //   126: lload #14
/*     */     //   128: lload #16
/*     */     //   130: lcmp
/*     */     //   131: ifeq -> 254
/*     */     //   134: lload #12
/*     */     //   136: lstore #18
/*     */     //   138: lload #14
/*     */     //   140: lstore #20
/*     */     //   142: lload #18
/*     */     //   144: lload #20
/*     */     //   146: lconst_1
/*     */     //   147: lsub
/*     */     //   148: dup2
/*     */     //   149: lstore #20
/*     */     //   151: lcmp
/*     */     //   152: ifge -> 174
/*     */     //   155: aload #7
/*     */     //   157: lload #18
/*     */     //   159: dup2
/*     */     //   160: lconst_1
/*     */     //   161: ladd
/*     */     //   162: lstore #18
/*     */     //   164: lload #20
/*     */     //   166: invokeinterface swap : (JJ)V
/*     */     //   171: goto -> 142
/*     */     //   174: lload #14
/*     */     //   176: lstore #18
/*     */     //   178: lload #16
/*     */     //   180: lstore #20
/*     */     //   182: lload #18
/*     */     //   184: lload #20
/*     */     //   186: lconst_1
/*     */     //   187: lsub
/*     */     //   188: dup2
/*     */     //   189: lstore #20
/*     */     //   191: lcmp
/*     */     //   192: ifge -> 214
/*     */     //   195: aload #7
/*     */     //   197: lload #18
/*     */     //   199: dup2
/*     */     //   200: lconst_1
/*     */     //   201: ladd
/*     */     //   202: lstore #18
/*     */     //   204: lload #20
/*     */     //   206: invokeinterface swap : (JJ)V
/*     */     //   211: goto -> 182
/*     */     //   214: lload #12
/*     */     //   216: lstore #18
/*     */     //   218: lload #16
/*     */     //   220: lstore #20
/*     */     //   222: lload #18
/*     */     //   224: lload #20
/*     */     //   226: lconst_1
/*     */     //   227: lsub
/*     */     //   228: dup2
/*     */     //   229: lstore #20
/*     */     //   231: lcmp
/*     */     //   232: ifge -> 254
/*     */     //   235: aload #7
/*     */     //   237: lload #18
/*     */     //   239: dup2
/*     */     //   240: lconst_1
/*     */     //   241: ladd
/*     */     //   242: lstore #18
/*     */     //   244: lload #20
/*     */     //   246: invokeinterface swap : (JJ)V
/*     */     //   251: goto -> 222
/*     */     //   254: lload #8
/*     */     //   256: lload #10
/*     */     //   258: lload_2
/*     */     //   259: lsub
/*     */     //   260: ladd
/*     */     //   261: lstore_2
/*     */     //   262: lload_0
/*     */     //   263: lload #8
/*     */     //   265: lload_2
/*     */     //   266: aload #6
/*     */     //   268: aload #7
/*     */     //   270: invokestatic inPlaceMerge : (JJJLit/unimi/dsi/fastutil/longs/LongComparator;Lit/unimi/dsi/fastutil/BigSwapper;)V
/*     */     //   273: lload_2
/*     */     //   274: lload #10
/*     */     //   276: lload #4
/*     */     //   278: aload #6
/*     */     //   280: aload #7
/*     */     //   282: invokestatic inPlaceMerge : (JJJLit/unimi/dsi/fastutil/longs/LongComparator;Lit/unimi/dsi/fastutil/BigSwapper;)V
/*     */     //   285: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #308	-> 0
/*     */     //   #309	-> 14
/*     */     //   #310	-> 25
/*     */     //   #311	-> 37
/*     */     //   #313	-> 46
/*     */     //   #317	-> 47
/*     */     //   #318	-> 58
/*     */     //   #319	-> 69
/*     */     //   #321	-> 84
/*     */     //   #322	-> 96
/*     */     //   #325	-> 107
/*     */     //   #326	-> 111
/*     */     //   #327	-> 114
/*     */     //   #328	-> 118
/*     */     //   #329	-> 134
/*     */     //   #330	-> 138
/*     */     //   #331	-> 142
/*     */     //   #332	-> 155
/*     */     //   #333	-> 174
/*     */     //   #334	-> 178
/*     */     //   #335	-> 182
/*     */     //   #336	-> 195
/*     */     //   #337	-> 214
/*     */     //   #338	-> 218
/*     */     //   #339	-> 222
/*     */     //   #340	-> 235
/*     */     //   #343	-> 254
/*     */     //   #344	-> 262
/*     */     //   #345	-> 273
/*     */     //   #346	-> 285
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   69	15	8	firstCut	J
/*     */     //   81	3	10	secondCut	J
/*     */     //   138	116	18	first1	J
/*     */     //   142	112	20	last1	J
/*     */     //   0	286	0	from	J
/*     */     //   0	286	2	mid	J
/*     */     //   0	286	4	to	J
/*     */     //   0	286	6	comp	Lit/unimi/dsi/fastutil/longs/LongComparator;
/*     */     //   0	286	7	swapper	Lit/unimi/dsi/fastutil/BigSwapper;
/*     */     //   107	179	8	firstCut	J
/*     */     //   96	190	10	secondCut	J
/*     */     //   111	175	12	first2	J
/*     */     //   114	172	14	middle2	J
/*     */     //   118	168	16	last2	J
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
/*     */   private static long lowerBound(long mid, long to, long firstCut, LongComparator comp) {
/* 366 */     long len = to - mid;
/* 367 */     while (len > 0L) {
/* 368 */       long half = len / 2L;
/* 369 */       long middle = mid + half;
/* 370 */       if (comp.compare(middle, firstCut) < 0) {
/* 371 */         mid = middle + 1L;
/* 372 */         len -= half + 1L; continue;
/*     */       } 
/* 374 */       len = half;
/*     */     } 
/*     */     
/* 377 */     return mid;
/*     */   }
/*     */ 
/*     */   
/*     */   private static long med3(long a, long b, long c, LongComparator comp) {
/* 382 */     int ab = comp.compare(a, b);
/* 383 */     int ac = comp.compare(a, c);
/* 384 */     int bc = comp.compare(b, c);
/* 385 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
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
/*     */   public static void mergeSort(long from, long to, LongComparator comp, BigSwapper swapper) {
/* 414 */     long length = to - from;
/*     */ 
/*     */     
/* 417 */     if (length < 7L) {
/* 418 */       long i; for (i = from; i < to; i++) {
/* 419 */         long j; for (j = i; j > from && comp.compare(j - 1L, j) > 0; j--) {
/* 420 */           swapper.swap(j, j - 1L);
/*     */         }
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 427 */     long mid = from + to >>> 1L;
/* 428 */     mergeSort(from, mid, comp, swapper);
/* 429 */     mergeSort(mid, to, comp, swapper);
/*     */ 
/*     */ 
/*     */     
/* 433 */     if (comp.compare(mid - 1L, mid) <= 0) {
/*     */       return;
/*     */     }
/* 436 */     inPlaceMerge(from, mid, to, comp, swapper);
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
/*     */   public static void quickSort(long from, long to, LongComparator comp, BigSwapper swapper) {
/* 461 */     long len = to - from;
/*     */     
/* 463 */     if (len < 7L) {
/* 464 */       long i; for (i = from; i < to; i++) {
/* 465 */         long j; for (j = i; j > from && comp.compare(j - 1L, j) > 0; j--) {
/* 466 */           swapper.swap(j, j - 1L);
/*     */         }
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 472 */     long m = from + len / 2L;
/* 473 */     if (len > 7L) {
/* 474 */       long l = from, n = to - 1L;
/* 475 */       if (len > 40L) {
/* 476 */         long s = len / 8L;
/* 477 */         l = med3(l, l + s, l + 2L * s, comp);
/* 478 */         m = med3(m - s, m, m + s, comp);
/* 479 */         n = med3(n - 2L * s, n - s, n, comp);
/*     */       } 
/* 481 */       m = med3(l, m, n, comp);
/*     */     } 
/*     */ 
/*     */     
/* 485 */     long a = from, b = a, c = to - 1L, d = c;
/*     */     
/*     */     int comparison;
/*     */     
/* 489 */     while (b <= c && (comparison = comp.compare(b, m)) <= 0) {
/* 490 */       if (comparison == 0) {
/* 491 */         if (a == m) { m = b; }
/* 492 */         else if (b == m) { m = a; }
/* 493 */          swapper.swap(a++, b);
/*     */       } 
/* 495 */       b++;
/*     */     } 
/* 497 */     while (c >= b && (comparison = comp.compare(c, m)) >= 0) {
/* 498 */       if (comparison == 0) {
/* 499 */         if (c == m) { m = d; }
/* 500 */         else if (d == m) { m = c; }
/* 501 */          swapper.swap(c, d--);
/*     */       } 
/* 503 */       c--;
/*     */     } 
/* 505 */     if (b > c) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 513 */       long n = from + len;
/* 514 */       long s = Math.min(a - from, b - a);
/* 515 */       vecSwap(swapper, from, b - s, s);
/* 516 */       s = Math.min(d - c, n - d - 1L);
/* 517 */       vecSwap(swapper, b, n - s, s);
/*     */ 
/*     */       
/* 520 */       if ((s = b - a) > 1L) quickSort(from, from + s, comp, swapper); 
/* 521 */       if ((s = d - c) > 1L) quickSort(n - s, n, comp, swapper);
/*     */       
/*     */       return;
/*     */     } 
/*     */     if (b == m) {
/*     */       m = d;
/*     */     } else if (c == m) {
/*     */       m = c;
/*     */     } 
/*     */     swapper.swap(b++, c--);
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
/*     */   private static long upperBound(long from, long mid, long secondCut, LongComparator comp) {
/* 542 */     long len = mid - from;
/* 543 */     while (len > 0L) {
/* 544 */       long half = len / 2L;
/* 545 */       long middle = from + half;
/* 546 */       if (comp.compare(secondCut, middle) < 0) {
/* 547 */         len = half; continue;
/*     */       } 
/* 549 */       from = middle + 1L;
/* 550 */       len -= half + 1L;
/*     */     } 
/*     */     
/* 553 */     return from;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void vecSwap(BigSwapper swapper, long from, long l, long s) {
/* 558 */     for (int i = 0; i < s; i++, from++, l++)
/* 559 */       swapper.swap(from, l); 
/*     */   }
/*     */   
/*     */   public static void main(String[] arg) {
/*     */     // Byte code:
/*     */     //   0: lconst_1
/*     */     //   1: aload_0
/*     */     //   2: iconst_0
/*     */     //   3: aaload
/*     */     //   4: invokestatic parseInt : (Ljava/lang/String;)I
/*     */     //   7: lshl
/*     */     //   8: invokestatic newBigArray : (J)[[I
/*     */     //   11: astore_1
/*     */     //   12: bipush #10
/*     */     //   14: istore #10
/*     */     //   16: iload #10
/*     */     //   18: iinc #10, -1
/*     */     //   21: ifeq -> 372
/*     */     //   24: invokestatic currentTimeMillis : ()J
/*     */     //   27: lneg
/*     */     //   28: lstore #8
/*     */     //   30: lconst_0
/*     */     //   31: lstore_2
/*     */     //   32: aload_1
/*     */     //   33: invokestatic length : ([[I)J
/*     */     //   36: lstore #11
/*     */     //   38: lload #11
/*     */     //   40: dup2
/*     */     //   41: lconst_1
/*     */     //   42: lsub
/*     */     //   43: lstore #11
/*     */     //   45: lconst_0
/*     */     //   46: lcmp
/*     */     //   47: ifeq -> 66
/*     */     //   50: lload_2
/*     */     //   51: lload #11
/*     */     //   53: aload_1
/*     */     //   54: lload #11
/*     */     //   56: invokestatic get : ([[IJ)I
/*     */     //   59: i2l
/*     */     //   60: lxor
/*     */     //   61: lxor
/*     */     //   62: lstore_2
/*     */     //   63: goto -> 38
/*     */     //   66: lload_2
/*     */     //   67: lconst_0
/*     */     //   68: lcmp
/*     */     //   69: ifne -> 78
/*     */     //   72: getstatic java/lang/System.err : Ljava/io/PrintStream;
/*     */     //   75: invokevirtual println : ()V
/*     */     //   78: getstatic java/lang/System.out : Ljava/io/PrintStream;
/*     */     //   81: new java/lang/StringBuilder
/*     */     //   84: dup
/*     */     //   85: invokespecial <init> : ()V
/*     */     //   88: ldc 'Single loop: '
/*     */     //   90: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   93: lload #8
/*     */     //   95: invokestatic currentTimeMillis : ()J
/*     */     //   98: ladd
/*     */     //   99: invokevirtual append : (J)Ljava/lang/StringBuilder;
/*     */     //   102: ldc 'ms'
/*     */     //   104: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   107: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   110: invokevirtual println : (Ljava/lang/String;)V
/*     */     //   113: invokestatic currentTimeMillis : ()J
/*     */     //   116: lneg
/*     */     //   117: lstore #8
/*     */     //   119: lconst_0
/*     */     //   120: lstore #4
/*     */     //   122: aload_1
/*     */     //   123: arraylength
/*     */     //   124: istore #11
/*     */     //   126: iload #11
/*     */     //   128: iinc #11, -1
/*     */     //   131: ifeq -> 178
/*     */     //   134: aload_1
/*     */     //   135: iload #11
/*     */     //   137: aaload
/*     */     //   138: astore #12
/*     */     //   140: aload #12
/*     */     //   142: arraylength
/*     */     //   143: istore #13
/*     */     //   145: iload #13
/*     */     //   147: iinc #13, -1
/*     */     //   150: ifeq -> 175
/*     */     //   153: lload #4
/*     */     //   155: aload #12
/*     */     //   157: iload #13
/*     */     //   159: iaload
/*     */     //   160: i2l
/*     */     //   161: iload #11
/*     */     //   163: iload #13
/*     */     //   165: invokestatic index : (II)J
/*     */     //   168: lxor
/*     */     //   169: lxor
/*     */     //   170: lstore #4
/*     */     //   172: goto -> 145
/*     */     //   175: goto -> 126
/*     */     //   178: lload #4
/*     */     //   180: lconst_0
/*     */     //   181: lcmp
/*     */     //   182: ifne -> 191
/*     */     //   185: getstatic java/lang/System.err : Ljava/io/PrintStream;
/*     */     //   188: invokevirtual println : ()V
/*     */     //   191: lload_2
/*     */     //   192: lload #4
/*     */     //   194: lcmp
/*     */     //   195: ifeq -> 206
/*     */     //   198: new java/lang/AssertionError
/*     */     //   201: dup
/*     */     //   202: invokespecial <init> : ()V
/*     */     //   205: athrow
/*     */     //   206: getstatic java/lang/System.out : Ljava/io/PrintStream;
/*     */     //   209: new java/lang/StringBuilder
/*     */     //   212: dup
/*     */     //   213: invokespecial <init> : ()V
/*     */     //   216: ldc 'Double loop: '
/*     */     //   218: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   221: lload #8
/*     */     //   223: invokestatic currentTimeMillis : ()J
/*     */     //   226: ladd
/*     */     //   227: invokevirtual append : (J)Ljava/lang/StringBuilder;
/*     */     //   230: ldc 'ms'
/*     */     //   232: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   235: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   238: invokevirtual println : (Ljava/lang/String;)V
/*     */     //   241: lconst_0
/*     */     //   242: lstore #6
/*     */     //   244: aload_1
/*     */     //   245: invokestatic length : ([[I)J
/*     */     //   248: lstore #11
/*     */     //   250: aload_1
/*     */     //   251: arraylength
/*     */     //   252: istore #13
/*     */     //   254: iload #13
/*     */     //   256: iinc #13, -1
/*     */     //   259: ifeq -> 306
/*     */     //   262: aload_1
/*     */     //   263: iload #13
/*     */     //   265: aaload
/*     */     //   266: astore #14
/*     */     //   268: aload #14
/*     */     //   270: arraylength
/*     */     //   271: istore #15
/*     */     //   273: iload #15
/*     */     //   275: iinc #15, -1
/*     */     //   278: ifeq -> 303
/*     */     //   281: lload #4
/*     */     //   283: aload #14
/*     */     //   285: iload #15
/*     */     //   287: iaload
/*     */     //   288: i2l
/*     */     //   289: lload #11
/*     */     //   291: lconst_1
/*     */     //   292: lsub
/*     */     //   293: dup2
/*     */     //   294: lstore #11
/*     */     //   296: lxor
/*     */     //   297: lxor
/*     */     //   298: lstore #4
/*     */     //   300: goto -> 273
/*     */     //   303: goto -> 254
/*     */     //   306: lload #6
/*     */     //   308: lconst_0
/*     */     //   309: lcmp
/*     */     //   310: ifne -> 319
/*     */     //   313: getstatic java/lang/System.err : Ljava/io/PrintStream;
/*     */     //   316: invokevirtual println : ()V
/*     */     //   319: lload_2
/*     */     //   320: lload #6
/*     */     //   322: lcmp
/*     */     //   323: ifeq -> 334
/*     */     //   326: new java/lang/AssertionError
/*     */     //   329: dup
/*     */     //   330: invokespecial <init> : ()V
/*     */     //   333: athrow
/*     */     //   334: getstatic java/lang/System.out : Ljava/io/PrintStream;
/*     */     //   337: new java/lang/StringBuilder
/*     */     //   340: dup
/*     */     //   341: invokespecial <init> : ()V
/*     */     //   344: ldc 'Double loop (with additional index): '
/*     */     //   346: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   349: lload #8
/*     */     //   351: invokestatic currentTimeMillis : ()J
/*     */     //   354: ladd
/*     */     //   355: invokevirtual append : (J)Ljava/lang/StringBuilder;
/*     */     //   358: ldc 'ms'
/*     */     //   360: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   363: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   366: invokevirtual println : (Ljava/lang/String;)V
/*     */     //   369: goto -> 16
/*     */     //   372: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #563	-> 0
/*     */     //   #566	-> 12
/*     */     //   #568	-> 24
/*     */     //   #570	-> 30
/*     */     //   #571	-> 32
/*     */     //   #572	-> 50
/*     */     //   #573	-> 66
/*     */     //   #575	-> 78
/*     */     //   #577	-> 113
/*     */     //   #579	-> 119
/*     */     //   #580	-> 122
/*     */     //   #581	-> 134
/*     */     //   #582	-> 140
/*     */     //   #583	-> 153
/*     */     //   #584	-> 175
/*     */     //   #585	-> 178
/*     */     //   #586	-> 191
/*     */     //   #588	-> 206
/*     */     //   #590	-> 241
/*     */     //   #591	-> 244
/*     */     //   #592	-> 250
/*     */     //   #593	-> 262
/*     */     //   #594	-> 268
/*     */     //   #595	-> 281
/*     */     //   #596	-> 303
/*     */     //   #597	-> 306
/*     */     //   #598	-> 319
/*     */     //   #600	-> 334
/*     */     //   #601	-> 369
/*     */     //   #602	-> 372
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   38	28	11	i	J
/*     */     //   145	30	13	d	I
/*     */     //   140	35	12	t	[I
/*     */     //   126	52	11	i	I
/*     */     //   273	30	15	d	I
/*     */     //   268	35	14	t	[I
/*     */     //   254	52	13	i	I
/*     */     //   250	119	11	j	J
/*     */     //   32	340	2	x	J
/*     */     //   122	250	4	y	J
/*     */     //   244	128	6	z	J
/*     */     //   30	342	8	start	J
/*     */     //   16	356	10	k	I
/*     */     //   0	373	0	arg	[Ljava/lang/String;
/*     */     //   12	361	1	a	[[I
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\BigArrays.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */