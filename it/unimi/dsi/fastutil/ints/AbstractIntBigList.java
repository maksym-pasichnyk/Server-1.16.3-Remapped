/*     */ package it.unimi.dsi.fastutil.ints;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.BigList;
/*     */ import it.unimi.dsi.fastutil.BigListIterator;
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
/*     */ 
/*     */ public abstract class AbstractIntBigList
/*     */   extends AbstractIntCollection
/*     */   implements IntBigList, IntStack
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
/*     */   public void add(long index, int k) {
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
/*     */   public boolean add(int k) {
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
/*     */   public int removeInt(long i) {
/*  92 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int set(long index, int k) {
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
/*     */   public boolean addAll(long index, Collection<? extends Integer> c) {
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
/*     */     //   45: checkcast java/lang/Integer
/*     */     //   48: invokevirtual add : (JLjava/lang/Integer;)V
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
/*     */     //   0	57	0	this	Lit/unimi/dsi/fastutil/ints/AbstractIntBigList;
/*     */     //   0	57	1	index	J
/*     */     //   0	57	3	c	Ljava/util/Collection;
/*     */     //   13	44	4	i	Ljava/util/Iterator;
/*     */     //   22	35	5	retVal	Z
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	57	3	c	Ljava/util/Collection<+Ljava/lang/Integer;>;
/*     */     //   13	44	4	i	Ljava/util/Iterator<+Ljava/lang/Integer;>;
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
/*     */   public boolean addAll(Collection<? extends Integer> c) {
/* 126 */     return addAll(size64(), c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IntBigListIterator iterator() {
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
/*     */   public IntBigListIterator listIterator() {
/* 147 */     return listIterator(0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IntBigListIterator listIterator(final long index) {
/* 156 */     ensureIndex(index);
/* 157 */     return new IntBigListIterator() {
/* 158 */         long pos = index; long last = -1L;
/*     */         
/*     */         public boolean hasNext() {
/* 161 */           return (this.pos < AbstractIntBigList.this.size64());
/*     */         }
/*     */         
/*     */         public boolean hasPrevious() {
/* 165 */           return (this.pos > 0L);
/*     */         }
/*     */         
/*     */         public int nextInt() {
/* 169 */           if (!hasNext())
/* 170 */             throw new NoSuchElementException(); 
/* 171 */           return AbstractIntBigList.this.getInt(this.last = this.pos++);
/*     */         }
/*     */         
/*     */         public int previousInt() {
/* 175 */           if (!hasPrevious())
/* 176 */             throw new NoSuchElementException(); 
/* 177 */           return AbstractIntBigList.this.getInt(this.last = --this.pos);
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
/*     */         public void add(int k) {
/* 189 */           AbstractIntBigList.this.add(this.pos++, k);
/* 190 */           this.last = -1L;
/*     */         }
/*     */         
/*     */         public void set(int k) {
/* 194 */           if (this.last == -1L)
/* 195 */             throw new IllegalStateException(); 
/* 196 */           AbstractIntBigList.this.set(this.last, k);
/*     */         }
/*     */         
/*     */         public void remove() {
/* 200 */           if (this.last == -1L)
/* 201 */             throw new IllegalStateException(); 
/* 202 */           AbstractIntBigList.this.removeInt(this.last);
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
/*     */   public boolean contains(int k) {
/* 222 */     return (indexOf(k) >= 0L);
/*     */   }
/*     */   
/*     */   public long indexOf(int k) {
/* 226 */     IntBigListIterator i = listIterator();
/*     */     
/* 228 */     while (i.hasNext()) {
/* 229 */       int e = i.nextInt();
/* 230 */       if (k == e)
/* 231 */         return i.previousIndex(); 
/*     */     } 
/* 233 */     return -1L;
/*     */   }
/*     */   
/*     */   public long lastIndexOf(int k) {
/* 237 */     IntBigListIterator i = listIterator(size64());
/*     */     
/* 239 */     while (i.hasPrevious()) {
/* 240 */       int e = i.previousInt();
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
/* 251 */         add(0); 
/*     */     } else {
/* 253 */       while (i-- != size)
/* 254 */         remove(i); 
/*     */     } 
/*     */   }
/*     */   public IntBigList subList(long from, long to) {
/* 258 */     ensureIndex(from);
/* 259 */     ensureIndex(to);
/* 260 */     if (from > to)
/* 261 */       throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")"); 
/* 262 */     return new IntSubList(this, from, to);
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
/* 274 */     IntBigListIterator i = listIterator(from);
/* 275 */     long n = to - from;
/* 276 */     if (n < 0L)
/* 277 */       throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")"); 
/* 278 */     while (n-- != 0L) {
/* 279 */       i.nextInt();
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
/*     */   public void addElements(long index, int[][] a, long offset, long length) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: lload_1
/*     */     //   2: invokevirtual ensureIndex : (J)V
/*     */     //   5: aload_3
/*     */     //   6: lload #4
/*     */     //   8: lload #6
/*     */     //   10: invokestatic ensureOffsetLength : ([[IJJ)V
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
/*     */     //   39: invokestatic get : ([[IJ)I
/*     */     //   42: invokevirtual add : (JI)V
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
/*     */     //   0	49	0	this	Lit/unimi/dsi/fastutil/ints/AbstractIntBigList;
/*     */     //   0	49	1	index	J
/*     */     //   0	49	3	a	[[I
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
/*     */   public void addElements(long index, int[][] a) {
/* 306 */     addElements(index, a, 0L, IntBigArrays.length(a));
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
/*     */   public void getElements(long from, int[][] a, long offset, long length) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: lload_1
/*     */     //   2: invokevirtual listIterator : (J)Lit/unimi/dsi/fastutil/ints/IntBigListIterator;
/*     */     //   5: astore #8
/*     */     //   7: aload_3
/*     */     //   8: lload #4
/*     */     //   10: lload #6
/*     */     //   12: invokestatic ensureOffsetLength : ([[IJJ)V
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
/*     */     //   96: invokeinterface nextInt : ()I
/*     */     //   101: invokestatic set : ([[IJI)V
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
/*     */     //   0	108	0	this	Lit/unimi/dsi/fastutil/ints/AbstractIntBigList;
/*     */     //   0	108	1	from	J
/*     */     //   0	108	3	a	[[I
/*     */     //   0	108	4	offset	J
/*     */     //   0	108	6	length	J
/*     */     //   7	101	8	i	Lit/unimi/dsi/fastutil/ints/IntBigListIterator;
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
/* 358 */     IntIterator i = iterator();
/* 359 */     int h = 1;
/* 360 */     long s = size64();
/* 361 */     while (s-- != 0L) {
/* 362 */       int k = i.nextInt();
/* 363 */       h = 31 * h + k;
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
/* 377 */     if (l instanceof IntBigList) {
/* 378 */       IntBigListIterator intBigListIterator1 = listIterator(), intBigListIterator2 = ((IntBigList)l).listIterator();
/* 379 */       while (s-- != 0L) {
/* 380 */         if (intBigListIterator1.nextInt() != intBigListIterator2.nextInt())
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
/*     */   public int compareTo(BigList<? extends Integer> l) {
/* 406 */     if (l == this)
/* 407 */       return 0; 
/* 408 */     if (l instanceof IntBigList) {
/* 409 */       IntBigListIterator intBigListIterator1 = listIterator(), intBigListIterator2 = ((IntBigList)l).listIterator();
/*     */ 
/*     */       
/* 412 */       while (intBigListIterator1.hasNext() && intBigListIterator2.hasNext()) {
/* 413 */         int e1 = intBigListIterator1.nextInt();
/* 414 */         int e2 = intBigListIterator2.nextInt(); int r;
/* 415 */         if ((r = Integer.compare(e1, e2)) != 0)
/* 416 */           return r; 
/*     */       } 
/* 418 */       return intBigListIterator2.hasNext() ? -1 : (intBigListIterator1.hasNext() ? 1 : 0);
/*     */     } 
/* 420 */     BigListIterator<? extends Integer> i1 = listIterator(), i2 = l.listIterator();
/*     */     
/* 422 */     while (i1.hasNext() && i2.hasNext()) {
/* 423 */       int r; if ((r = ((Comparable<Object>)i1.next()).compareTo(i2.next())) != 0)
/* 424 */         return r; 
/*     */     } 
/* 426 */     return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
/*     */   }
/*     */   
/*     */   public void push(int o) {
/* 430 */     add(o);
/*     */   }
/*     */   
/*     */   public int popInt() {
/* 434 */     if (isEmpty())
/* 435 */       throw new NoSuchElementException(); 
/* 436 */     return removeInt(size64() - 1L);
/*     */   }
/*     */   
/*     */   public int topInt() {
/* 440 */     if (isEmpty())
/* 441 */       throw new NoSuchElementException(); 
/* 442 */     return getInt(size64() - 1L);
/*     */   }
/*     */   
/*     */   public int peekInt(int i) {
/* 446 */     return getInt(size64() - 1L - i);
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
/*     */   public boolean rem(int k) {
/* 458 */     long index = indexOf(k);
/* 459 */     if (index == -1L)
/* 460 */       return false; 
/* 461 */     removeInt(index);
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
/*     */   public boolean addAll(long index, IntCollection c) {
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
/*     */   public boolean addAll(long index, IntBigList l) {
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
/*     */   public boolean addAll(IntCollection c) {
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
/*     */   public boolean addAll(IntBigList l) {
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
/*     */   public void add(long index, Integer ok) {
/* 519 */     add(index, ok.intValue());
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
/*     */   public Integer set(long index, Integer ok) {
/* 532 */     return Integer.valueOf(set(index, ok.intValue()));
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
/*     */   public Integer get(long index) {
/* 545 */     return Integer.valueOf(getInt(index));
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
/* 558 */     return indexOf(((Integer)ok).intValue());
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
/* 571 */     return lastIndexOf(((Integer)ok).intValue());
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
/*     */   public Integer remove(long index) {
/* 584 */     return Integer.valueOf(removeInt(index));
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
/*     */   public void push(Integer o) {
/* 597 */     push(o.intValue());
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
/*     */   public Integer pop() {
/* 610 */     return Integer.valueOf(popInt());
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
/*     */   public Integer top() {
/* 623 */     return Integer.valueOf(topInt());
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
/*     */   public Integer peek(int i) {
/* 636 */     return Integer.valueOf(peekInt(i));
/*     */   }
/*     */   
/*     */   public String toString() {
/* 640 */     StringBuilder s = new StringBuilder();
/* 641 */     IntIterator i = iterator();
/* 642 */     long n = size64();
/*     */     
/* 644 */     boolean first = true;
/* 645 */     s.append("[");
/* 646 */     while (n-- != 0L) {
/* 647 */       if (first) {
/* 648 */         first = false;
/*     */       } else {
/* 650 */         s.append(", ");
/* 651 */       }  int k = i.nextInt();
/* 652 */       s.append(String.valueOf(k));
/*     */     } 
/* 654 */     s.append("]");
/* 655 */     return s.toString();
/*     */   }
/*     */   
/*     */   public static class IntSubList
/*     */     extends AbstractIntBigList
/*     */     implements Serializable {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     protected final IntBigList l;
/*     */     protected final long from;
/*     */     protected long to;
/*     */     
/*     */     public IntSubList(IntBigList l, long from, long to) {
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
/*     */     public boolean add(int k) {
/* 679 */       this.l.add(this.to, k);
/* 680 */       this.to++;
/* 681 */       assert assertRange();
/* 682 */       return true;
/*     */     }
/*     */     
/*     */     public void add(long index, int k) {
/* 686 */       ensureIndex(index);
/* 687 */       this.l.add(this.from + index, k);
/* 688 */       this.to++;
/* 689 */       assert assertRange();
/*     */     }
/*     */     
/*     */     public boolean addAll(long index, Collection<? extends Integer> c) {
/* 693 */       ensureIndex(index);
/* 694 */       this.to += c.size();
/* 695 */       return this.l.addAll(this.from + index, c);
/*     */     }
/*     */     
/*     */     public int getInt(long index) {
/* 699 */       ensureRestrictedIndex(index);
/* 700 */       return this.l.getInt(this.from + index);
/*     */     }
/*     */     
/*     */     public int removeInt(long index) {
/* 704 */       ensureRestrictedIndex(index);
/* 705 */       this.to--;
/* 706 */       return this.l.removeInt(this.from + index);
/*     */     }
/*     */     
/*     */     public int set(long index, int k) {
/* 710 */       ensureRestrictedIndex(index);
/* 711 */       return this.l.set(this.from + index, k);
/*     */     }
/*     */     
/*     */     public long size64() {
/* 715 */       return this.to - this.from;
/*     */     }
/*     */     
/*     */     public void getElements(long from, int[][] a, long offset, long length) {
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
/*     */     public void addElements(long index, int[][] a, long offset, long length) {
/* 735 */       ensureIndex(index);
/* 736 */       this.l.addElements(this.from + index, a, offset, length);
/* 737 */       this.to += length;
/* 738 */       assert assertRange();
/*     */     }
/*     */     
/*     */     public IntBigListIterator listIterator(final long index) {
/* 742 */       ensureIndex(index);
/* 743 */       return new IntBigListIterator() {
/* 744 */           long pos = index; long last = -1L;
/*     */           
/*     */           public boolean hasNext() {
/* 747 */             return (this.pos < AbstractIntBigList.IntSubList.this.size64());
/*     */           }
/*     */           
/*     */           public boolean hasPrevious() {
/* 751 */             return (this.pos > 0L);
/*     */           }
/*     */           
/*     */           public int nextInt() {
/* 755 */             if (!hasNext())
/* 756 */               throw new NoSuchElementException(); 
/* 757 */             return AbstractIntBigList.IntSubList.this.l.getInt(AbstractIntBigList.IntSubList.this.from + (this.last = this.pos++));
/*     */           }
/*     */           
/*     */           public int previousInt() {
/* 761 */             if (!hasPrevious())
/* 762 */               throw new NoSuchElementException(); 
/* 763 */             return AbstractIntBigList.IntSubList.this.l.getInt(AbstractIntBigList.IntSubList.this.from + (this.last = --this.pos));
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
/*     */           public void add(int k) {
/* 775 */             if (this.last == -1L)
/* 776 */               throw new IllegalStateException(); 
/* 777 */             AbstractIntBigList.IntSubList.this.add(this.pos++, k);
/* 778 */             this.last = -1L;
/* 779 */             if (!$assertionsDisabled && !AbstractIntBigList.IntSubList.this.assertRange()) throw new AssertionError(); 
/*     */           }
/*     */           
/*     */           public void set(int k) {
/* 783 */             if (this.last == -1L)
/* 784 */               throw new IllegalStateException(); 
/* 785 */             AbstractIntBigList.IntSubList.this.set(this.last, k);
/*     */           }
/*     */           
/*     */           public void remove() {
/* 789 */             if (this.last == -1L)
/* 790 */               throw new IllegalStateException(); 
/* 791 */             AbstractIntBigList.IntSubList.this.removeInt(this.last);
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 796 */             if (this.last < this.pos)
/* 797 */               this.pos--; 
/* 798 */             this.last = -1L;
/* 799 */             assert AbstractIntBigList.IntSubList.this.assertRange();
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public IntBigList subList(long from, long to) {
/* 805 */       ensureIndex(from);
/* 806 */       ensureIndex(to);
/* 807 */       if (from > to)
/* 808 */         throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")"); 
/* 809 */       return new IntSubList(this, from, to);
/*     */     }
/*     */     
/*     */     public boolean rem(int k) {
/* 813 */       long index = indexOf(k);
/* 814 */       if (index == -1L)
/* 815 */         return false; 
/* 816 */       this.to--;
/* 817 */       this.l.removeInt(this.from + index);
/* 818 */       assert assertRange();
/* 819 */       return true;
/*     */     }
/*     */     
/*     */     public boolean addAll(long index, IntCollection c) {
/* 823 */       ensureIndex(index);
/* 824 */       return super.addAll(index, c);
/*     */     }
/*     */     
/*     */     public boolean addAll(long index, IntBigList l) {
/* 828 */       ensureIndex(index);
/* 829 */       return super.addAll(index, l);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\ints\AbstractIntBigList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */