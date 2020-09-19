/*     */ package it.unimi.dsi.fastutil.objects;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.BigList;
/*     */ import it.unimi.dsi.fastutil.BigListIterator;
/*     */ import it.unimi.dsi.fastutil.Stack;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractReferenceBigList<K>
/*     */   extends AbstractReferenceCollection<K>
/*     */   implements ReferenceBigList<K>, Stack<K>
/*     */ {
/*     */   protected void ensureIndex(long index) {
/*  44 */     if (index < 0L)
/*  45 */       throw new IndexOutOfBoundsException("Index (" + index + ") is negative"); 
/*  46 */     if (index > size64()) {
/*  47 */       throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + size64() + ")");
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
/*  60 */     if (index < 0L)
/*  61 */       throw new IndexOutOfBoundsException("Index (" + index + ") is negative"); 
/*  62 */     if (index >= size64()) {
/*  63 */       throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + 
/*  64 */           size64() + ")");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(long index, K k) {
/*  74 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(K k) {
/*  85 */     add(size64(), k);
/*  86 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K remove(long i) {
/*  96 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K set(long index, K k) {
/* 106 */     throw new UnsupportedOperationException();
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
/*     */   public boolean addAll(long index, Collection<? extends K> c) {
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
/*     */     //   29: ifeq -> 51
/*     */     //   32: aload_0
/*     */     //   33: lload_1
/*     */     //   34: dup2
/*     */     //   35: lconst_1
/*     */     //   36: ladd
/*     */     //   37: lstore_1
/*     */     //   38: aload #4
/*     */     //   40: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   45: invokevirtual add : (JLjava/lang/Object;)V
/*     */     //   48: goto -> 22
/*     */     //   51: iload #5
/*     */     //   53: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #114	-> 0
/*     */     //   #115	-> 5
/*     */     //   #116	-> 13
/*     */     //   #117	-> 22
/*     */     //   #118	-> 32
/*     */     //   #119	-> 51
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	54	0	this	Lit/unimi/dsi/fastutil/objects/AbstractReferenceBigList;
/*     */     //   0	54	1	index	J
/*     */     //   0	54	3	c	Ljava/util/Collection;
/*     */     //   13	41	4	i	Ljava/util/Iterator;
/*     */     //   22	32	5	retVal	Z
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	54	0	this	Lit/unimi/dsi/fastutil/objects/AbstractReferenceBigList<TK;>;
/*     */     //   0	54	3	c	Ljava/util/Collection<+TK;>;
/*     */     //   13	41	4	i	Ljava/util/Iterator<+TK;>;
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
/*     */   public boolean addAll(Collection<? extends K> c) {
/* 130 */     return addAll(size64(), c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectBigListIterator<K> iterator() {
/* 140 */     return listIterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectBigListIterator<K> listIterator() {
/* 151 */     return listIterator(0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectBigListIterator<K> listIterator(final long index) {
/* 160 */     ensureIndex(index);
/* 161 */     return new ObjectBigListIterator<K>() {
/* 162 */         long pos = index; long last = -1L;
/*     */         
/*     */         public boolean hasNext() {
/* 165 */           return (this.pos < AbstractReferenceBigList.this.size64());
/*     */         }
/*     */         
/*     */         public boolean hasPrevious() {
/* 169 */           return (this.pos > 0L);
/*     */         }
/*     */         
/*     */         public K next() {
/* 173 */           if (!hasNext())
/* 174 */             throw new NoSuchElementException(); 
/* 175 */           return (K)AbstractReferenceBigList.this.get(this.last = this.pos++);
/*     */         }
/*     */         
/*     */         public K previous() {
/* 179 */           if (!hasPrevious())
/* 180 */             throw new NoSuchElementException(); 
/* 181 */           return (K)AbstractReferenceBigList.this.get(this.last = --this.pos);
/*     */         }
/*     */         
/*     */         public long nextIndex() {
/* 185 */           return this.pos;
/*     */         }
/*     */         
/*     */         public long previousIndex() {
/* 189 */           return this.pos - 1L;
/*     */         }
/*     */         
/*     */         public void add(K k) {
/* 193 */           AbstractReferenceBigList.this.add(this.pos++, k);
/* 194 */           this.last = -1L;
/*     */         }
/*     */         
/*     */         public void set(K k) {
/* 198 */           if (this.last == -1L)
/* 199 */             throw new IllegalStateException(); 
/* 200 */           AbstractReferenceBigList.this.set(this.last, k);
/*     */         }
/*     */         
/*     */         public void remove() {
/* 204 */           if (this.last == -1L)
/* 205 */             throw new IllegalStateException(); 
/* 206 */           AbstractReferenceBigList.this.remove(this.last);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 211 */           if (this.last < this.pos)
/* 212 */             this.pos--; 
/* 213 */           this.last = -1L;
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
/*     */   public boolean contains(Object k) {
/* 226 */     return (indexOf(k) >= 0L);
/*     */   }
/*     */   
/*     */   public long indexOf(Object k) {
/* 230 */     ObjectBigListIterator<K> i = listIterator();
/*     */     
/* 232 */     while (i.hasNext()) {
/* 233 */       K e = i.next();
/* 234 */       if (k == e)
/* 235 */         return i.previousIndex(); 
/*     */     } 
/* 237 */     return -1L;
/*     */   }
/*     */   
/*     */   public long lastIndexOf(Object k) {
/* 241 */     ObjectBigListIterator<K> i = listIterator(size64());
/*     */     
/* 243 */     while (i.hasPrevious()) {
/* 244 */       K e = (K)i.previous();
/* 245 */       if (k == e)
/* 246 */         return i.nextIndex(); 
/*     */     } 
/* 248 */     return -1L;
/*     */   }
/*     */   
/*     */   public void size(long size) {
/* 252 */     long i = size64();
/* 253 */     if (size > i) {
/* 254 */       while (i++ < size)
/* 255 */         add((K)null); 
/*     */     } else {
/* 257 */       while (i-- != size)
/* 258 */         remove(i); 
/*     */     } 
/*     */   }
/*     */   public ReferenceBigList<K> subList(long from, long to) {
/* 262 */     ensureIndex(from);
/* 263 */     ensureIndex(to);
/* 264 */     if (from > to)
/* 265 */       throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")"); 
/* 266 */     return new ReferenceSubList<>(this, from, to);
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
/* 277 */     ensureIndex(to);
/* 278 */     ObjectBigListIterator<K> i = listIterator(from);
/* 279 */     long n = to - from;
/* 280 */     if (n < 0L)
/* 281 */       throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")"); 
/* 282 */     while (n-- != 0L) {
/* 283 */       i.next();
/* 284 */       i.remove();
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
/*     */   public void addElements(long index, K[][] a, long offset, long length) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: lload_1
/*     */     //   2: invokevirtual ensureIndex : (J)V
/*     */     //   5: aload_3
/*     */     //   6: lload #4
/*     */     //   8: lload #6
/*     */     //   10: invokestatic ensureOffsetLength : ([[Ljava/lang/Object;JJ)V
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
/*     */     //   39: invokestatic get : ([[Ljava/lang/Object;J)Ljava/lang/Object;
/*     */     //   42: invokevirtual add : (JLjava/lang/Object;)V
/*     */     //   45: goto -> 13
/*     */     //   48: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #296	-> 0
/*     */     //   #297	-> 5
/*     */     //   #298	-> 13
/*     */     //   #299	-> 25
/*     */     //   #300	-> 48
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	49	0	this	Lit/unimi/dsi/fastutil/objects/AbstractReferenceBigList;
/*     */     //   0	49	1	index	J
/*     */     //   0	49	3	a	[[Ljava/lang/Object;
/*     */     //   0	49	4	offset	J
/*     */     //   0	49	6	length	J
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	49	0	this	Lit/unimi/dsi/fastutil/objects/AbstractReferenceBigList<TK;>;
/*     */     //   0	49	3	a	[[TK;
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
/*     */   public void addElements(long index, K[][] a) {
/* 310 */     addElements(index, a, 0L, ObjectBigArrays.length(a));
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
/*     */   public void getElements(long from, Object[][] a, long offset, long length) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: lload_1
/*     */     //   2: invokevirtual listIterator : (J)Lit/unimi/dsi/fastutil/objects/ObjectBigListIterator;
/*     */     //   5: astore #8
/*     */     //   7: aload_3
/*     */     //   8: lload #4
/*     */     //   10: lload #6
/*     */     //   12: invokestatic ensureOffsetLength : ([[Ljava/lang/Object;JJ)V
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
/*     */     //   96: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   101: invokestatic set : ([[Ljava/lang/Object;JLjava/lang/Object;)V
/*     */     //   104: goto -> 74
/*     */     //   107: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #321	-> 0
/*     */     //   #322	-> 7
/*     */     //   #323	-> 15
/*     */     //   #324	-> 27
/*     */     //   #325	-> 56
/*     */     //   #326	-> 74
/*     */     //   #327	-> 86
/*     */     //   #328	-> 107
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	108	0	this	Lit/unimi/dsi/fastutil/objects/AbstractReferenceBigList;
/*     */     //   0	108	1	from	J
/*     */     //   0	108	3	a	[[Ljava/lang/Object;
/*     */     //   0	108	4	offset	J
/*     */     //   0	108	6	length	J
/*     */     //   7	101	8	i	Lit/unimi/dsi/fastutil/objects/ObjectBigListIterator;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	108	0	this	Lit/unimi/dsi/fastutil/objects/AbstractReferenceBigList<TK;>;
/*     */     //   7	101	8	i	Lit/unimi/dsi/fastutil/objects/ObjectBigListIterator<TK;>;
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
/* 336 */     removeElements(0L, size64());
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
/* 349 */     return (int)Math.min(2147483647L, size64());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 359 */     ObjectIterator<K> i = iterator();
/* 360 */     int h = 1;
/* 361 */     long s = size64();
/* 362 */     while (s-- != 0L) {
/* 363 */       K k = i.next();
/* 364 */       h = 31 * h + System.identityHashCode(k);
/*     */     } 
/* 366 */     return h;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 370 */     if (o == this)
/* 371 */       return true; 
/* 372 */     if (!(o instanceof BigList))
/* 373 */       return false; 
/* 374 */     BigList<?> l = (BigList)o;
/* 375 */     long s = size64();
/* 376 */     if (s != l.size64())
/* 377 */       return false; 
/* 378 */     BigListIterator<?> i1 = listIterator(), i2 = l.listIterator();
/* 379 */     while (s-- != 0L) {
/* 380 */       if (i1.next() != i2.next())
/* 381 */         return false; 
/* 382 */     }  return true;
/*     */   }
/*     */   
/*     */   public void push(K o) {
/* 386 */     add(o);
/*     */   }
/*     */   
/*     */   public K pop() {
/* 390 */     if (isEmpty())
/* 391 */       throw new NoSuchElementException(); 
/* 392 */     return remove(size64() - 1L);
/*     */   }
/*     */   
/*     */   public K top() {
/* 396 */     if (isEmpty())
/* 397 */       throw new NoSuchElementException(); 
/* 398 */     return (K)get(size64() - 1L);
/*     */   }
/*     */   
/*     */   public K peek(int i) {
/* 402 */     return (K)get(size64() - 1L - i);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 406 */     StringBuilder s = new StringBuilder();
/* 407 */     ObjectIterator<K> i = iterator();
/* 408 */     long n = size64();
/*     */     
/* 410 */     boolean first = true;
/* 411 */     s.append("[");
/* 412 */     while (n-- != 0L) {
/* 413 */       if (first) {
/* 414 */         first = false;
/*     */       } else {
/* 416 */         s.append(", ");
/* 417 */       }  K k = i.next();
/* 418 */       if (this == k) {
/* 419 */         s.append("(this big list)"); continue;
/*     */       } 
/* 421 */       s.append(String.valueOf(k));
/*     */     } 
/* 423 */     s.append("]");
/* 424 */     return s.toString();
/*     */   }
/*     */   
/*     */   public static class ReferenceSubList<K>
/*     */     extends AbstractReferenceBigList<K>
/*     */     implements Serializable {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     protected final ReferenceBigList<K> l;
/*     */     protected final long from;
/*     */     protected long to;
/*     */     
/*     */     public ReferenceSubList(ReferenceBigList<K> l, long from, long to) {
/* 436 */       this.l = l;
/* 437 */       this.from = from;
/* 438 */       this.to = to;
/*     */     }
/*     */     private boolean assertRange() {
/* 441 */       assert this.from <= this.l.size64();
/* 442 */       assert this.to <= this.l.size64();
/* 443 */       assert this.to >= this.from;
/* 444 */       return true;
/*     */     }
/*     */     
/*     */     public boolean add(K k) {
/* 448 */       this.l.add(this.to, k);
/* 449 */       this.to++;
/* 450 */       assert assertRange();
/* 451 */       return true;
/*     */     }
/*     */     
/*     */     public void add(long index, K k) {
/* 455 */       ensureIndex(index);
/* 456 */       this.l.add(this.from + index, k);
/* 457 */       this.to++;
/* 458 */       assert assertRange();
/*     */     }
/*     */     
/*     */     public boolean addAll(long index, Collection<? extends K> c) {
/* 462 */       ensureIndex(index);
/* 463 */       this.to += c.size();
/* 464 */       return this.l.addAll(this.from + index, c);
/*     */     }
/*     */     
/*     */     public K get(long index) {
/* 468 */       ensureRestrictedIndex(index);
/* 469 */       return (K)this.l.get(this.from + index);
/*     */     }
/*     */     
/*     */     public K remove(long index) {
/* 473 */       ensureRestrictedIndex(index);
/* 474 */       this.to--;
/* 475 */       return (K)this.l.remove(this.from + index);
/*     */     }
/*     */     
/*     */     public K set(long index, K k) {
/* 479 */       ensureRestrictedIndex(index);
/* 480 */       return (K)this.l.set(this.from + index, k);
/*     */     }
/*     */     
/*     */     public long size64() {
/* 484 */       return this.to - this.from;
/*     */     }
/*     */     
/*     */     public void getElements(long from, Object[][] a, long offset, long length) {
/* 488 */       ensureIndex(from);
/* 489 */       if (from + length > size64())
/* 490 */         throw new IndexOutOfBoundsException("End index (" + from + length + ") is greater than list size (" + 
/* 491 */             size64() + ")"); 
/* 492 */       this.l.getElements(this.from + from, a, offset, length);
/*     */     }
/*     */     
/*     */     public void removeElements(long from, long to) {
/* 496 */       ensureIndex(from);
/* 497 */       ensureIndex(to);
/* 498 */       this.l.removeElements(this.from + from, this.from + to);
/* 499 */       this.to -= to - from;
/* 500 */       assert assertRange();
/*     */     }
/*     */     
/*     */     public void addElements(long index, K[][] a, long offset, long length) {
/* 504 */       ensureIndex(index);
/* 505 */       this.l.addElements(this.from + index, a, offset, length);
/* 506 */       this.to += length;
/* 507 */       assert assertRange();
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> listIterator(final long index) {
/* 511 */       ensureIndex(index);
/* 512 */       return new ObjectBigListIterator<K>() {
/* 513 */           long pos = index; long last = -1L;
/*     */           
/*     */           public boolean hasNext() {
/* 516 */             return (this.pos < AbstractReferenceBigList.ReferenceSubList.this.size64());
/*     */           }
/*     */           
/*     */           public boolean hasPrevious() {
/* 520 */             return (this.pos > 0L);
/*     */           }
/*     */           
/*     */           public K next() {
/* 524 */             if (!hasNext())
/* 525 */               throw new NoSuchElementException(); 
/* 526 */             return (K)AbstractReferenceBigList.ReferenceSubList.this.l.get(AbstractReferenceBigList.ReferenceSubList.this.from + (this.last = this.pos++));
/*     */           }
/*     */           
/*     */           public K previous() {
/* 530 */             if (!hasPrevious())
/* 531 */               throw new NoSuchElementException(); 
/* 532 */             return (K)AbstractReferenceBigList.ReferenceSubList.this.l.get(AbstractReferenceBigList.ReferenceSubList.this.from + (this.last = --this.pos));
/*     */           }
/*     */           
/*     */           public long nextIndex() {
/* 536 */             return this.pos;
/*     */           }
/*     */           
/*     */           public long previousIndex() {
/* 540 */             return this.pos - 1L;
/*     */           }
/*     */           
/*     */           public void add(K k) {
/* 544 */             if (this.last == -1L)
/* 545 */               throw new IllegalStateException(); 
/* 546 */             AbstractReferenceBigList.ReferenceSubList.this.add(this.pos++, k);
/* 547 */             this.last = -1L;
/* 548 */             if (!$assertionsDisabled && !AbstractReferenceBigList.ReferenceSubList.this.assertRange()) throw new AssertionError(); 
/*     */           }
/*     */           
/*     */           public void set(K k) {
/* 552 */             if (this.last == -1L)
/* 553 */               throw new IllegalStateException(); 
/* 554 */             AbstractReferenceBigList.ReferenceSubList.this.set(this.last, k);
/*     */           }
/*     */           
/*     */           public void remove() {
/* 558 */             if (this.last == -1L)
/* 559 */               throw new IllegalStateException(); 
/* 560 */             AbstractReferenceBigList.ReferenceSubList.this.remove(this.last);
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 565 */             if (this.last < this.pos)
/* 566 */               this.pos--; 
/* 567 */             this.last = -1L;
/* 568 */             assert AbstractReferenceBigList.ReferenceSubList.this.assertRange();
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public ReferenceBigList<K> subList(long from, long to) {
/* 574 */       ensureIndex(from);
/* 575 */       ensureIndex(to);
/* 576 */       if (from > to)
/* 577 */         throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")"); 
/* 578 */       return new ReferenceSubList(this, from, to);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\AbstractReferenceBigList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */