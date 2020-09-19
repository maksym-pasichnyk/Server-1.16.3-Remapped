/*     */ package it.unimi.dsi.fastutil.longs;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.BigList;
/*     */ import it.unimi.dsi.fastutil.BigListIterator;
/*     */ import it.unimi.dsi.fastutil.HashCommon;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractLongBigList
/*     */   extends AbstractLongCollection
/*     */   implements LongBigList, LongStack
/*     */ {
/*     */   protected void ensureIndex(long index) {
/*  40 */     if (index < 0L)
/*  41 */       throw new IndexOutOfBoundsException("Index (" + index + ") is negative"); 
/*  42 */     if (index > size64()) {
/*  43 */       throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + size64() + ")");
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
/*     */   protected void ensureRestrictedIndex(long index) {
/*  56 */     if (index < 0L)
/*  57 */       throw new IndexOutOfBoundsException("Index (" + index + ") is negative"); 
/*  58 */     if (index >= size64()) {
/*  59 */       throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + 
/*  60 */           size64() + ")");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(long index, long k) {
/*  70 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(long k) {
/*  81 */     add(size64(), k);
/*  82 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long removeLong(long i) {
/*  92 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long set(long index, long k) {
/* 102 */     throw new UnsupportedOperationException();
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
/*     */   public boolean addAll(long index, Collection<? extends Long> c) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: lload_1
/*     */     //   2: invokevirtual ensureIndex : (J)V
/*     */     //   5: aload_3
/*     */     //   6: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   11: astore #4
/*     */     //   13: aload #4
/*     */     //   15: invokeinterface hasNext : ()Z
/*     */     //   20: istore #5
/*     */     //   22: aload #4
/*     */     //   24: invokeinterface hasNext : ()Z
/*     */     //   29: ifeq -> 54
/*     */     //   32: aload_0
/*     */     //   33: lload_1
/*     */     //   34: dup2
/*     */     //   35: lconst_1
/*     */     //   36: ladd
/*     */     //   37: lstore_1
/*     */     //   38: aload #4
/*     */     //   40: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   45: checkcast java/lang/Long
/*     */     //   48: invokevirtual add : (JLjava/lang/Long;)V
/*     */     //   51: goto -> 22
/*     */     //   54: iload #5
/*     */     //   56: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #110	-> 0
/*     */     //   #111	-> 5
/*     */     //   #112	-> 13
/*     */     //   #113	-> 22
/*     */     //   #114	-> 32
/*     */     //   #115	-> 54
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	57	0	this	Lit/unimi/dsi/fastutil/longs/AbstractLongBigList;
/*     */     //   0	57	1	index	J
/*     */     //   0	57	3	c	Ljava/util/Collection;
/*     */     //   13	44	4	i	Ljava/util/Iterator;
/*     */     //   22	35	5	retVal	Z
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	57	3	c	Ljava/util/Collection<+Ljava/lang/Long;>;
/*     */     //   13	44	4	i	Ljava/util/Iterator<+Ljava/lang/Long;>;
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
/*     */   public boolean addAll(Collection<? extends Long> c) {
/* 126 */     return addAll(size64(), c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LongBigListIterator iterator() {
/* 136 */     return listIterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LongBigListIterator listIterator() {
/* 147 */     return listIterator(0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LongBigListIterator listIterator(final long index) {
/* 156 */     ensureIndex(index);
/* 157 */     return new LongBigListIterator() {
/* 158 */         long pos = index; long last = -1L;
/*     */         
/*     */         public boolean hasNext() {
/* 161 */           return (this.pos < AbstractLongBigList.this.size64());
/*     */         }
/*     */         
/*     */         public boolean hasPrevious() {
/* 165 */           return (this.pos > 0L);
/*     */         }
/*     */         
/*     */         public long nextLong() {
/* 169 */           if (!hasNext())
/* 170 */             throw new NoSuchElementException(); 
/* 171 */           return AbstractLongBigList.this.getLong(this.last = this.pos++);
/*     */         }
/*     */         
/*     */         public long previousLong() {
/* 175 */           if (!hasPrevious())
/* 176 */             throw new NoSuchElementException(); 
/* 177 */           return AbstractLongBigList.this.getLong(this.last = --this.pos);
/*     */         }
/*     */         
/*     */         public long nextIndex() {
/* 181 */           return this.pos;
/*     */         }
/*     */         
/*     */         public long previousIndex() {
/* 185 */           return this.pos - 1L;
/*     */         }
/*     */         
/*     */         public void add(long k) {
/* 189 */           AbstractLongBigList.this.add(this.pos++, k);
/* 190 */           this.last = -1L;
/*     */         }
/*     */         
/*     */         public void set(long k) {
/* 194 */           if (this.last == -1L)
/* 195 */             throw new IllegalStateException(); 
/* 196 */           AbstractLongBigList.this.set(this.last, k);
/*     */         }
/*     */         
/*     */         public void remove() {
/* 200 */           if (this.last == -1L)
/* 201 */             throw new IllegalStateException(); 
/* 202 */           AbstractLongBigList.this.removeLong(this.last);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 207 */           if (this.last < this.pos)
/* 208 */             this.pos--; 
/* 209 */           this.last = -1L;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(long k) {
/* 222 */     return (indexOf(k) >= 0L);
/*     */   }
/*     */   
/*     */   public long indexOf(long k) {
/* 226 */     LongBigListIterator i = listIterator();
/*     */     
/* 228 */     while (i.hasNext()) {
/* 229 */       long e = i.nextLong();
/* 230 */       if (k == e)
/* 231 */         return i.previousIndex(); 
/*     */     } 
/* 233 */     return -1L;
/*     */   }
/*     */   
/*     */   public long lastIndexOf(long k) {
/* 237 */     LongBigListIterator i = listIterator(size64());
/*     */     
/* 239 */     while (i.hasPrevious()) {
/* 240 */       long e = i.previousLong();
/* 241 */       if (k == e)
/* 242 */         return i.nextIndex(); 
/*     */     } 
/* 244 */     return -1L;
/*     */   }
/*     */   
/*     */   public void size(long size) {
/* 248 */     long i = size64();
/* 249 */     if (size > i) {
/* 250 */       while (i++ < size)
/* 251 */         add(0L); 
/*     */     } else {
/* 253 */       while (i-- != size)
/* 254 */         remove(i); 
/*     */     } 
/*     */   }
/*     */   public LongBigList subList(long from, long to) {
/* 258 */     ensureIndex(from);
/* 259 */     ensureIndex(to);
/* 260 */     if (from > to)
/* 261 */       throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")"); 
/* 262 */     return new LongSubList(this, from, to);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeElements(long from, long to) {
/* 273 */     ensureIndex(to);
/* 274 */     LongBigListIterator i = listIterator(from);
/* 275 */     long n = to - from;
/* 276 */     if (n < 0L)
/* 277 */       throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")"); 
/* 278 */     while (n-- != 0L) {
/* 279 */       i.nextLong();
/* 280 */       i.remove();
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
/*     */   public void addElements(long index, long[][] a, long offset, long length) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: lload_1
/*     */     //   2: invokevirtual ensureIndex : (J)V
/*     */     //   5: aload_3
/*     */     //   6: lload #4
/*     */     //   8: lload #6
/*     */     //   10: invokestatic ensureOffsetLength : ([[JJJ)V
/*     */     //   13: lload #6
/*     */     //   15: dup2
/*     */     //   16: lconst_1
/*     */     //   17: lsub
/*     */     //   18: lstore #6
/*     */     //   20: lconst_0
/*     */     //   21: lcmp
/*     */     //   22: ifeq -> 48
/*     */     //   25: aload_0
/*     */     //   26: lload_1
/*     */     //   27: dup2
/*     */     //   28: lconst_1
/*     */     //   29: ladd
/*     */     //   30: lstore_1
/*     */     //   31: aload_3
/*     */     //   32: lload #4
/*     */     //   34: dup2
/*     */     //   35: lconst_1
/*     */     //   36: ladd
/*     */     //   37: lstore #4
/*     */     //   39: invokestatic get : ([[JJ)J
/*     */     //   42: invokevirtual add : (JJ)V
/*     */     //   45: goto -> 13
/*     */     //   48: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #292	-> 0
/*     */     //   #293	-> 5
/*     */     //   #294	-> 13
/*     */     //   #295	-> 25
/*     */     //   #296	-> 48
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	49	0	this	Lit/unimi/dsi/fastutil/longs/AbstractLongBigList;
/*     */     //   0	49	1	index	J
/*     */     //   0	49	3	a	[[J
/*     */     //   0	49	4	offset	J
/*     */     //   0	49	6	length	J
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
/*     */   public void addElements(long index, long[][] a) {
/* 306 */     addElements(index, a, 0L, LongBigArrays.length(a));
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
/*     */   public void getElements(long from, long[][] a, long offset, long length) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: lload_1
/*     */     //   2: invokevirtual listIterator : (J)Lit/unimi/dsi/fastutil/longs/LongBigListIterator;
/*     */     //   5: astore #8
/*     */     //   7: aload_3
/*     */     //   8: lload #4
/*     */     //   10: lload #6
/*     */     //   12: invokestatic ensureOffsetLength : ([[JJJ)V
/*     */     //   15: lload_1
/*     */     //   16: lload #6
/*     */     //   18: ladd
/*     */     //   19: aload_0
/*     */     //   20: invokevirtual size64 : ()J
/*     */     //   23: lcmp
/*     */     //   24: ifle -> 74
/*     */     //   27: new java/lang/IndexOutOfBoundsException
/*     */     //   30: dup
/*     */     //   31: new java/lang/StringBuilder
/*     */     //   34: dup
/*     */     //   35: invokespecial <init> : ()V
/*     */     //   38: ldc 'End index ('
/*     */     //   40: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   43: lload_1
/*     */     //   44: lload #6
/*     */     //   46: ladd
/*     */     //   47: invokevirtual append : (J)Ljava/lang/StringBuilder;
/*     */     //   50: ldc ') is greater than list size ('
/*     */     //   52: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   55: aload_0
/*     */     //   56: invokevirtual size64 : ()J
/*     */     //   59: invokevirtual append : (J)Ljava/lang/StringBuilder;
/*     */     //   62: ldc ')'
/*     */     //   64: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   67: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   70: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   73: athrow
/*     */     //   74: lload #6
/*     */     //   76: dup2
/*     */     //   77: lconst_1
/*     */     //   78: lsub
/*     */     //   79: lstore #6
/*     */     //   81: lconst_0
/*     */     //   82: lcmp
/*     */     //   83: ifeq -> 107
/*     */     //   86: aload_3
/*     */     //   87: lload #4
/*     */     //   89: dup2
/*     */     //   90: lconst_1
/*     */     //   91: ladd
/*     */     //   92: lstore #4
/*     */     //   94: aload #8
/*     */     //   96: invokeinterface nextLong : ()J
/*     */     //   101: invokestatic set : ([[JJJ)V
/*     */     //   104: goto -> 74
/*     */     //   107: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #317	-> 0
/*     */     //   #318	-> 7
/*     */     //   #319	-> 15
/*     */     //   #320	-> 27
/*     */     //   #321	-> 56
/*     */     //   #322	-> 74
/*     */     //   #323	-> 86
/*     */     //   #324	-> 107
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	108	0	this	Lit/unimi/dsi/fastutil/longs/AbstractLongBigList;
/*     */     //   0	108	1	from	J
/*     */     //   0	108	3	a	[[J
/*     */     //   0	108	4	offset	J
/*     */     //   0	108	6	length	J
/*     */     //   7	101	8	i	Lit/unimi/dsi/fastutil/longs/LongBigListIterator;
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
/*     */   public void clear() {
/* 332 */     removeElements(0L, size64());
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
/*     */   @Deprecated
/*     */   public int size() {
/* 345 */     return (int)Math.min(2147483647L, size64());
/*     */   }
/*     */   private boolean valEquals(Object a, Object b) {
/* 348 */     return (a == null) ? ((b == null)) : a.equals(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 358 */     LongIterator i = iterator();
/* 359 */     int h = 1;
/* 360 */     long s = size64();
/* 361 */     while (s-- != 0L) {
/* 362 */       long k = i.nextLong();
/* 363 */       h = 31 * h + HashCommon.long2int(k);
/*     */     } 
/* 365 */     return h;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 369 */     if (o == this)
/* 370 */       return true; 
/* 371 */     if (!(o instanceof BigList))
/* 372 */       return false; 
/* 373 */     BigList<?> l = (BigList)o;
/* 374 */     long s = size64();
/* 375 */     if (s != l.size64())
/* 376 */       return false; 
/* 377 */     if (l instanceof LongBigList) {
/* 378 */       LongBigListIterator longBigListIterator1 = listIterator(), longBigListIterator2 = ((LongBigList)l).listIterator();
/* 379 */       while (s-- != 0L) {
/* 380 */         if (longBigListIterator1.nextLong() != longBigListIterator2.nextLong())
/* 381 */           return false; 
/* 382 */       }  return true;
/*     */     } 
/* 384 */     BigListIterator<?> i1 = listIterator(), i2 = l.listIterator();
/* 385 */     while (s-- != 0L) {
/* 386 */       if (!valEquals(i1.next(), i2.next()))
/* 387 */         return false; 
/* 388 */     }  return true;
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
/*     */   public int compareTo(BigList<? extends Long> l) {
/* 406 */     if (l == this)
/* 407 */       return 0; 
/* 408 */     if (l instanceof LongBigList) {
/* 409 */       LongBigListIterator longBigListIterator1 = listIterator(), longBigListIterator2 = ((LongBigList)l).listIterator();
/*     */ 
/*     */       
/* 412 */       while (longBigListIterator1.hasNext() && longBigListIterator2.hasNext()) {
/* 413 */         long e1 = longBigListIterator1.nextLong();
/* 414 */         long e2 = longBigListIterator2.nextLong(); int r;
/* 415 */         if ((r = Long.compare(e1, e2)) != 0)
/* 416 */           return r; 
/*     */       } 
/* 418 */       return longBigListIterator2.hasNext() ? -1 : (longBigListIterator1.hasNext() ? 1 : 0);
/*     */     } 
/* 420 */     BigListIterator<? extends Long> i1 = listIterator(), i2 = l.listIterator();
/*     */     
/* 422 */     while (i1.hasNext() && i2.hasNext()) {
/* 423 */       int r; if ((r = ((Comparable<Object>)i1.next()).compareTo(i2.next())) != 0)
/* 424 */         return r; 
/*     */     } 
/* 426 */     return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
/*     */   }
/*     */   
/*     */   public void push(long o) {
/* 430 */     add(o);
/*     */   }
/*     */   
/*     */   public long popLong() {
/* 434 */     if (isEmpty())
/* 435 */       throw new NoSuchElementException(); 
/* 436 */     return removeLong(size64() - 1L);
/*     */   }
/*     */   
/*     */   public long topLong() {
/* 440 */     if (isEmpty())
/* 441 */       throw new NoSuchElementException(); 
/* 442 */     return getLong(size64() - 1L);
/*     */   }
/*     */   
/*     */   public long peekLong(int i) {
/* 446 */     return getLong(size64() - 1L - i);
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
/*     */   public boolean rem(long k) {
/* 458 */     long index = indexOf(k);
/* 459 */     if (index == -1L)
/* 460 */       return false; 
/* 461 */     removeLong(index);
/* 462 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAll(long index, LongCollection c) {
/* 473 */     return addAll(index, c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAll(long index, LongBigList l) {
/* 484 */     return addAll(index, l);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAll(LongCollection c) {
/* 495 */     return addAll(size64(), c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAll(LongBigList l) {
/* 506 */     return addAll(size64(), l);
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
/*     */   @Deprecated
/*     */   public void add(long index, Long ok) {
/* 519 */     add(index, ok.longValue());
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
/*     */   @Deprecated
/*     */   public Long set(long index, Long ok) {
/* 532 */     return Long.valueOf(set(index, ok.longValue()));
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
/*     */   @Deprecated
/*     */   public Long get(long index) {
/* 545 */     return Long.valueOf(getLong(index));
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
/*     */   @Deprecated
/*     */   public long indexOf(Object ok) {
/* 558 */     return indexOf(((Long)ok).longValue());
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
/*     */   @Deprecated
/*     */   public long lastIndexOf(Object ok) {
/* 571 */     return lastIndexOf(((Long)ok).longValue());
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
/*     */   @Deprecated
/*     */   public Long remove(long index) {
/* 584 */     return Long.valueOf(removeLong(index));
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
/*     */   @Deprecated
/*     */   public void push(Long o) {
/* 597 */     push(o.longValue());
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
/*     */   @Deprecated
/*     */   public Long pop() {
/* 610 */     return Long.valueOf(popLong());
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
/*     */   @Deprecated
/*     */   public Long top() {
/* 623 */     return Long.valueOf(topLong());
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
/*     */   @Deprecated
/*     */   public Long peek(int i) {
/* 636 */     return Long.valueOf(peekLong(i));
/*     */   }
/*     */   
/*     */   public String toString() {
/* 640 */     StringBuilder s = new StringBuilder();
/* 641 */     LongIterator i = iterator();
/* 642 */     long n = size64();
/*     */     
/* 644 */     boolean first = true;
/* 645 */     s.append("[");
/* 646 */     while (n-- != 0L) {
/* 647 */       if (first) {
/* 648 */         first = false;
/*     */       } else {
/* 650 */         s.append(", ");
/* 651 */       }  long k = i.nextLong();
/* 652 */       s.append(String.valueOf(k));
/*     */     } 
/* 654 */     s.append("]");
/* 655 */     return s.toString();
/*     */   }
/*     */   
/*     */   public static class LongSubList
/*     */     extends AbstractLongBigList
/*     */     implements Serializable {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     protected final LongBigList l;
/*     */     protected final long from;
/*     */     protected long to;
/*     */     
/*     */     public LongSubList(LongBigList l, long from, long to) {
/* 667 */       this.l = l;
/* 668 */       this.from = from;
/* 669 */       this.to = to;
/*     */     }
/*     */     private boolean assertRange() {
/* 672 */       assert this.from <= this.l.size64();
/* 673 */       assert this.to <= this.l.size64();
/* 674 */       assert this.to >= this.from;
/* 675 */       return true;
/*     */     }
/*     */     
/*     */     public boolean add(long k) {
/* 679 */       this.l.add(this.to, k);
/* 680 */       this.to++;
/* 681 */       assert assertRange();
/* 682 */       return true;
/*     */     }
/*     */     
/*     */     public void add(long index, long k) {
/* 686 */       ensureIndex(index);
/* 687 */       this.l.add(this.from + index, k);
/* 688 */       this.to++;
/* 689 */       assert assertRange();
/*     */     }
/*     */     
/*     */     public boolean addAll(long index, Collection<? extends Long> c) {
/* 693 */       ensureIndex(index);
/* 694 */       this.to += c.size();
/* 695 */       return this.l.addAll(this.from + index, c);
/*     */     }
/*     */     
/*     */     public long getLong(long index) {
/* 699 */       ensureRestrictedIndex(index);
/* 700 */       return this.l.getLong(this.from + index);
/*     */     }
/*     */     
/*     */     public long removeLong(long index) {
/* 704 */       ensureRestrictedIndex(index);
/* 705 */       this.to--;
/* 706 */       return this.l.removeLong(this.from + index);
/*     */     }
/*     */     
/*     */     public long set(long index, long k) {
/* 710 */       ensureRestrictedIndex(index);
/* 711 */       return this.l.set(this.from + index, k);
/*     */     }
/*     */     
/*     */     public long size64() {
/* 715 */       return this.to - this.from;
/*     */     }
/*     */     
/*     */     public void getElements(long from, long[][] a, long offset, long length) {
/* 719 */       ensureIndex(from);
/* 720 */       if (from + length > size64())
/* 721 */         throw new IndexOutOfBoundsException("End index (" + from + length + ") is greater than list size (" + 
/* 722 */             size64() + ")"); 
/* 723 */       this.l.getElements(this.from + from, a, offset, length);
/*     */     }
/*     */     
/*     */     public void removeElements(long from, long to) {
/* 727 */       ensureIndex(from);
/* 728 */       ensureIndex(to);
/* 729 */       this.l.removeElements(this.from + from, this.from + to);
/* 730 */       this.to -= to - from;
/* 731 */       assert assertRange();
/*     */     }
/*     */     
/*     */     public void addElements(long index, long[][] a, long offset, long length) {
/* 735 */       ensureIndex(index);
/* 736 */       this.l.addElements(this.from + index, a, offset, length);
/* 737 */       this.to += length;
/* 738 */       assert assertRange();
/*     */     }
/*     */     
/*     */     public LongBigListIterator listIterator(final long index) {
/* 742 */       ensureIndex(index);
/* 743 */       return new LongBigListIterator() {
/* 744 */           long pos = index; long last = -1L;
/*     */           
/*     */           public boolean hasNext() {
/* 747 */             return (this.pos < AbstractLongBigList.LongSubList.this.size64());
/*     */           }
/*     */           
/*     */           public boolean hasPrevious() {
/* 751 */             return (this.pos > 0L);
/*     */           }
/*     */           
/*     */           public long nextLong() {
/* 755 */             if (!hasNext())
/* 756 */               throw new NoSuchElementException(); 
/* 757 */             return AbstractLongBigList.LongSubList.this.l.getLong(AbstractLongBigList.LongSubList.this.from + (this.last = this.pos++));
/*     */           }
/*     */           
/*     */           public long previousLong() {
/* 761 */             if (!hasPrevious())
/* 762 */               throw new NoSuchElementException(); 
/* 763 */             return AbstractLongBigList.LongSubList.this.l.getLong(AbstractLongBigList.LongSubList.this.from + (this.last = --this.pos));
/*     */           }
/*     */           
/*     */           public long nextIndex() {
/* 767 */             return this.pos;
/*     */           }
/*     */           
/*     */           public long previousIndex() {
/* 771 */             return this.pos - 1L;
/*     */           }
/*     */           
/*     */           public void add(long k) {
/* 775 */             if (this.last == -1L)
/* 776 */               throw new IllegalStateException(); 
/* 777 */             AbstractLongBigList.LongSubList.this.add(this.pos++, k);
/* 778 */             this.last = -1L;
/* 779 */             if (!$assertionsDisabled && !AbstractLongBigList.LongSubList.this.assertRange()) throw new AssertionError(); 
/*     */           }
/*     */           
/*     */           public void set(long k) {
/* 783 */             if (this.last == -1L)
/* 784 */               throw new IllegalStateException(); 
/* 785 */             AbstractLongBigList.LongSubList.this.set(this.last, k);
/*     */           }
/*     */           
/*     */           public void remove() {
/* 789 */             if (this.last == -1L)
/* 790 */               throw new IllegalStateException(); 
/* 791 */             AbstractLongBigList.LongSubList.this.removeLong(this.last);
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 796 */             if (this.last < this.pos)
/* 797 */               this.pos--; 
/* 798 */             this.last = -1L;
/* 799 */             assert AbstractLongBigList.LongSubList.this.assertRange();
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public LongBigList subList(long from, long to) {
/* 805 */       ensureIndex(from);
/* 806 */       ensureIndex(to);
/* 807 */       if (from > to)
/* 808 */         throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")"); 
/* 809 */       return new LongSubList(this, from, to);
/*     */     }
/*     */     
/*     */     public boolean rem(long k) {
/* 813 */       long index = indexOf(k);
/* 814 */       if (index == -1L)
/* 815 */         return false; 
/* 816 */       this.to--;
/* 817 */       this.l.removeLong(this.from + index);
/* 818 */       assert assertRange();
/* 819 */       return true;
/*     */     }
/*     */     
/*     */     public boolean addAll(long index, LongCollection c) {
/* 823 */       ensureIndex(index);
/* 824 */       return super.addAll(index, c);
/*     */     }
/*     */     
/*     */     public boolean addAll(long index, LongBigList l) {
/* 828 */       ensureIndex(index);
/* 829 */       return super.addAll(index, l);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\longs\AbstractLongBigList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */