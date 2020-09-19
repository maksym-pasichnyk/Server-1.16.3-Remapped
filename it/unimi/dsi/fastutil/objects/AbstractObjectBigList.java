/*     */ package it.unimi.dsi.fastutil.objects;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.BigList;
/*     */ import it.unimi.dsi.fastutil.BigListIterator;
/*     */ import it.unimi.dsi.fastutil.Stack;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractObjectBigList<K>
/*     */   extends AbstractObjectCollection<K>
/*     */   implements ObjectBigList<K>, Stack<K>
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
/*     */     //   0	54	0	this	Lit/unimi/dsi/fastutil/objects/AbstractObjectBigList;
/*     */     //   0	54	1	index	J
/*     */     //   0	54	3	c	Ljava/util/Collection;
/*     */     //   13	41	4	i	Ljava/util/Iterator;
/*     */     //   22	32	5	retVal	Z
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	54	0	this	Lit/unimi/dsi/fastutil/objects/AbstractObjectBigList<TK;>;
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
/* 165 */           return (this.pos < AbstractObjectBigList.this.size64());
/*     */         }
/*     */         
/*     */         public boolean hasPrevious() {
/* 169 */           return (this.pos > 0L);
/*     */         }
/*     */         
/*     */         public K next() {
/* 173 */           if (!hasNext())
/* 174 */             throw new NoSuchElementException(); 
/* 175 */           return (K)AbstractObjectBigList.this.get(this.last = this.pos++);
/*     */         }
/*     */         
/*     */         public K previous() {
/* 179 */           if (!hasPrevious())
/* 180 */             throw new NoSuchElementException(); 
/* 181 */           return (K)AbstractObjectBigList.this.get(this.last = --this.pos);
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
/* 193 */           AbstractObjectBigList.this.add(this.pos++, k);
/* 194 */           this.last = -1L;
/*     */         }
/*     */         
/*     */         public void set(K k) {
/* 198 */           if (this.last == -1L)
/* 199 */             throw new IllegalStateException(); 
/* 200 */           AbstractObjectBigList.this.set(this.last, k);
/*     */         }
/*     */         
/*     */         public void remove() {
/* 204 */           if (this.last == -1L)
/* 205 */             throw new IllegalStateException(); 
/* 206 */           AbstractObjectBigList.this.remove(this.last);
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
/* 234 */       if (Objects.equals(k, e))
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
/* 245 */       if (Objects.equals(k, e))
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
/*     */   public ObjectBigList<K> subList(long from, long to) {
/* 262 */     ensureIndex(from);
/* 263 */     ensureIndex(to);
/* 264 */     if (from > to)
/* 265 */       throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")"); 
/* 266 */     return new ObjectSubList<>(this, from, to);
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
/*     */     //   0	49	0	this	Lit/unimi/dsi/fastutil/objects/AbstractObjectBigList;
/*     */     //   0	49	1	index	J
/*     */     //   0	49	3	a	[[Ljava/lang/Object;
/*     */     //   0	49	4	offset	J
/*     */     //   0	49	6	length	J
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	49	0	this	Lit/unimi/dsi/fastutil/objects/AbstractObjectBigList<TK;>;
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
/*     */     //   0	108	0	this	Lit/unimi/dsi/fastutil/objects/AbstractObjectBigList;
/*     */     //   0	108	1	from	J
/*     */     //   0	108	3	a	[[Ljava/lang/Object;
/*     */     //   0	108	4	offset	J
/*     */     //   0	108	6	length	J
/*     */     //   7	101	8	i	Lit/unimi/dsi/fastutil/objects/ObjectBigListIterator;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	108	0	this	Lit/unimi/dsi/fastutil/objects/AbstractObjectBigList<TK;>;
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
/*     */   private boolean valEquals(Object a, Object b) {
/* 352 */     return (a == null) ? ((b == null)) : a.equals(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 362 */     ObjectIterator<K> i = iterator();
/* 363 */     int h = 1;
/* 364 */     long s = size64();
/* 365 */     while (s-- != 0L) {
/* 366 */       K k = i.next();
/* 367 */       h = 31 * h + ((k == null) ? 0 : k.hashCode());
/*     */     } 
/* 369 */     return h;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 373 */     if (o == this)
/* 374 */       return true; 
/* 375 */     if (!(o instanceof BigList))
/* 376 */       return false; 
/* 377 */     BigList<?> l = (BigList)o;
/* 378 */     long s = size64();
/* 379 */     if (s != l.size64())
/* 380 */       return false; 
/* 381 */     BigListIterator<?> i1 = listIterator(), i2 = l.listIterator();
/* 382 */     while (s-- != 0L) {
/* 383 */       if (!valEquals(i1.next(), i2.next()))
/* 384 */         return false; 
/* 385 */     }  return true;
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
/*     */   public int compareTo(BigList<? extends K> l) {
/* 403 */     if (l == this)
/* 404 */       return 0; 
/* 405 */     if (l instanceof ObjectBigList) {
/* 406 */       ObjectBigListIterator<K> objectBigListIterator1 = listIterator(), objectBigListIterator2 = ((ObjectBigList)l).listIterator();
/*     */ 
/*     */       
/* 409 */       while (objectBigListIterator1.hasNext() && objectBigListIterator2.hasNext()) {
/* 410 */         K e1 = objectBigListIterator1.next();
/* 411 */         K e2 = objectBigListIterator2.next(); int r;
/* 412 */         if ((r = ((Comparable<K>)e1).compareTo(e2)) != 0)
/* 413 */           return r; 
/*     */       } 
/* 415 */       return objectBigListIterator2.hasNext() ? -1 : (objectBigListIterator1.hasNext() ? 1 : 0);
/*     */     } 
/* 417 */     BigListIterator<? extends K> i1 = listIterator(), i2 = l.listIterator();
/*     */     
/* 419 */     while (i1.hasNext() && i2.hasNext()) {
/* 420 */       int r; if ((r = ((Comparable<Object>)i1.next()).compareTo(i2.next())) != 0)
/* 421 */         return r; 
/*     */     } 
/* 423 */     return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
/*     */   }
/*     */   
/*     */   public void push(K o) {
/* 427 */     add(o);
/*     */   }
/*     */   
/*     */   public K pop() {
/* 431 */     if (isEmpty())
/* 432 */       throw new NoSuchElementException(); 
/* 433 */     return remove(size64() - 1L);
/*     */   }
/*     */   
/*     */   public K top() {
/* 437 */     if (isEmpty())
/* 438 */       throw new NoSuchElementException(); 
/* 439 */     return (K)get(size64() - 1L);
/*     */   }
/*     */   
/*     */   public K peek(int i) {
/* 443 */     return (K)get(size64() - 1L - i);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 447 */     StringBuilder s = new StringBuilder();
/* 448 */     ObjectIterator<K> i = iterator();
/* 449 */     long n = size64();
/*     */     
/* 451 */     boolean first = true;
/* 452 */     s.append("[");
/* 453 */     while (n-- != 0L) {
/* 454 */       if (first) {
/* 455 */         first = false;
/*     */       } else {
/* 457 */         s.append(", ");
/* 458 */       }  K k = i.next();
/* 459 */       if (this == k) {
/* 460 */         s.append("(this big list)"); continue;
/*     */       } 
/* 462 */       s.append(String.valueOf(k));
/*     */     } 
/* 464 */     s.append("]");
/* 465 */     return s.toString();
/*     */   }
/*     */   
/*     */   public static class ObjectSubList<K>
/*     */     extends AbstractObjectBigList<K>
/*     */     implements Serializable {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     protected final ObjectBigList<K> l;
/*     */     protected final long from;
/*     */     protected long to;
/*     */     
/*     */     public ObjectSubList(ObjectBigList<K> l, long from, long to) {
/* 477 */       this.l = l;
/* 478 */       this.from = from;
/* 479 */       this.to = to;
/*     */     }
/*     */     private boolean assertRange() {
/* 482 */       assert this.from <= this.l.size64();
/* 483 */       assert this.to <= this.l.size64();
/* 484 */       assert this.to >= this.from;
/* 485 */       return true;
/*     */     }
/*     */     
/*     */     public boolean add(K k) {
/* 489 */       this.l.add(this.to, k);
/* 490 */       this.to++;
/* 491 */       assert assertRange();
/* 492 */       return true;
/*     */     }
/*     */     
/*     */     public void add(long index, K k) {
/* 496 */       ensureIndex(index);
/* 497 */       this.l.add(this.from + index, k);
/* 498 */       this.to++;
/* 499 */       assert assertRange();
/*     */     }
/*     */     
/*     */     public boolean addAll(long index, Collection<? extends K> c) {
/* 503 */       ensureIndex(index);
/* 504 */       this.to += c.size();
/* 505 */       return this.l.addAll(this.from + index, c);
/*     */     }
/*     */     
/*     */     public K get(long index) {
/* 509 */       ensureRestrictedIndex(index);
/* 510 */       return (K)this.l.get(this.from + index);
/*     */     }
/*     */     
/*     */     public K remove(long index) {
/* 514 */       ensureRestrictedIndex(index);
/* 515 */       this.to--;
/* 516 */       return (K)this.l.remove(this.from + index);
/*     */     }
/*     */     
/*     */     public K set(long index, K k) {
/* 520 */       ensureRestrictedIndex(index);
/* 521 */       return (K)this.l.set(this.from + index, k);
/*     */     }
/*     */     
/*     */     public long size64() {
/* 525 */       return this.to - this.from;
/*     */     }
/*     */     
/*     */     public void getElements(long from, Object[][] a, long offset, long length) {
/* 529 */       ensureIndex(from);
/* 530 */       if (from + length > size64())
/* 531 */         throw new IndexOutOfBoundsException("End index (" + from + length + ") is greater than list size (" + 
/* 532 */             size64() + ")"); 
/* 533 */       this.l.getElements(this.from + from, a, offset, length);
/*     */     }
/*     */     
/*     */     public void removeElements(long from, long to) {
/* 537 */       ensureIndex(from);
/* 538 */       ensureIndex(to);
/* 539 */       this.l.removeElements(this.from + from, this.from + to);
/* 540 */       this.to -= to - from;
/* 541 */       assert assertRange();
/*     */     }
/*     */     
/*     */     public void addElements(long index, K[][] a, long offset, long length) {
/* 545 */       ensureIndex(index);
/* 546 */       this.l.addElements(this.from + index, a, offset, length);
/* 547 */       this.to += length;
/* 548 */       assert assertRange();
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> listIterator(final long index) {
/* 552 */       ensureIndex(index);
/* 553 */       return new ObjectBigListIterator<K>() {
/* 554 */           long pos = index; long last = -1L;
/*     */           
/*     */           public boolean hasNext() {
/* 557 */             return (this.pos < AbstractObjectBigList.ObjectSubList.this.size64());
/*     */           }
/*     */           
/*     */           public boolean hasPrevious() {
/* 561 */             return (this.pos > 0L);
/*     */           }
/*     */           
/*     */           public K next() {
/* 565 */             if (!hasNext())
/* 566 */               throw new NoSuchElementException(); 
/* 567 */             return (K)AbstractObjectBigList.ObjectSubList.this.l.get(AbstractObjectBigList.ObjectSubList.this.from + (this.last = this.pos++));
/*     */           }
/*     */           
/*     */           public K previous() {
/* 571 */             if (!hasPrevious())
/* 572 */               throw new NoSuchElementException(); 
/* 573 */             return (K)AbstractObjectBigList.ObjectSubList.this.l.get(AbstractObjectBigList.ObjectSubList.this.from + (this.last = --this.pos));
/*     */           }
/*     */           
/*     */           public long nextIndex() {
/* 577 */             return this.pos;
/*     */           }
/*     */           
/*     */           public long previousIndex() {
/* 581 */             return this.pos - 1L;
/*     */           }
/*     */           
/*     */           public void add(K k) {
/* 585 */             if (this.last == -1L)
/* 586 */               throw new IllegalStateException(); 
/* 587 */             AbstractObjectBigList.ObjectSubList.this.add(this.pos++, k);
/* 588 */             this.last = -1L;
/* 589 */             if (!$assertionsDisabled && !AbstractObjectBigList.ObjectSubList.this.assertRange()) throw new AssertionError(); 
/*     */           }
/*     */           
/*     */           public void set(K k) {
/* 593 */             if (this.last == -1L)
/* 594 */               throw new IllegalStateException(); 
/* 595 */             AbstractObjectBigList.ObjectSubList.this.set(this.last, k);
/*     */           }
/*     */           
/*     */           public void remove() {
/* 599 */             if (this.last == -1L)
/* 600 */               throw new IllegalStateException(); 
/* 601 */             AbstractObjectBigList.ObjectSubList.this.remove(this.last);
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 606 */             if (this.last < this.pos)
/* 607 */               this.pos--; 
/* 608 */             this.last = -1L;
/* 609 */             assert AbstractObjectBigList.ObjectSubList.this.assertRange();
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public ObjectBigList<K> subList(long from, long to) {
/* 615 */       ensureIndex(from);
/* 616 */       ensureIndex(to);
/* 617 */       if (from > to)
/* 618 */         throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")"); 
/* 619 */       return new ObjectSubList(this, from, to);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\AbstractObjectBigList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */