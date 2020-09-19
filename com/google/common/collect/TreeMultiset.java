/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class TreeMultiset<E>
/*     */   extends AbstractSortedMultiset<E>
/*     */   implements Serializable
/*     */ {
/*     */   private final transient Reference<AvlNode<E>> rootReference;
/*     */   private final transient GeneralRange<E> range;
/*     */   private final transient AvlNode<E> header;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public static <E extends Comparable> TreeMultiset<E> create() {
/*  73 */     return new TreeMultiset<>(Ordering.natural());
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
/*     */   public static <E> TreeMultiset<E> create(@Nullable Comparator<? super E> comparator) {
/*  89 */     return (comparator == null) ? new TreeMultiset<>(
/*  90 */         Ordering.natural()) : new TreeMultiset<>(comparator);
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
/*     */   public static <E extends Comparable> TreeMultiset<E> create(Iterable<? extends E> elements) {
/* 104 */     TreeMultiset<E> multiset = create();
/* 105 */     Iterables.addAll(multiset, elements);
/* 106 */     return multiset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   TreeMultiset(Reference<AvlNode<E>> rootReference, GeneralRange<E> range, AvlNode<E> endLink) {
/* 114 */     super(range.comparator());
/* 115 */     this.rootReference = rootReference;
/* 116 */     this.range = range;
/* 117 */     this.header = endLink;
/*     */   }
/*     */   
/*     */   TreeMultiset(Comparator<? super E> comparator) {
/* 121 */     super(comparator);
/* 122 */     this.range = GeneralRange.all(comparator);
/* 123 */     this.header = new AvlNode<>(null, 1);
/* 124 */     successor(this.header, this.header);
/* 125 */     this.rootReference = new Reference<>();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private enum Aggregate
/*     */   {
/* 132 */     SIZE
/*     */     {
/*     */       int nodeAggregate(TreeMultiset.AvlNode<?> node) {
/* 135 */         return node.elemCount;
/*     */       }
/*     */ 
/*     */       
/*     */       long treeAggregate(@Nullable TreeMultiset.AvlNode<?> root) {
/* 140 */         return (root == null) ? 0L : root.totalCount;
/*     */       }
/*     */     },
/* 143 */     DISTINCT
/*     */     {
/*     */       int nodeAggregate(TreeMultiset.AvlNode<?> node) {
/* 146 */         return 1;
/*     */       }
/*     */ 
/*     */       
/*     */       long treeAggregate(@Nullable TreeMultiset.AvlNode<?> root) {
/* 151 */         return (root == null) ? 0L : root.distinctElements;
/*     */       }
/*     */     };
/*     */     
/*     */     abstract int nodeAggregate(TreeMultiset.AvlNode<?> param1AvlNode);
/*     */     
/*     */     abstract long treeAggregate(@Nullable TreeMultiset.AvlNode<?> param1AvlNode);
/*     */   }
/*     */   
/*     */   private long aggregateForEntries(Aggregate aggr) {
/* 161 */     AvlNode<E> root = this.rootReference.get();
/* 162 */     long total = aggr.treeAggregate(root);
/* 163 */     if (this.range.hasLowerBound()) {
/* 164 */       total -= aggregateBelowRange(aggr, root);
/*     */     }
/* 166 */     if (this.range.hasUpperBound()) {
/* 167 */       total -= aggregateAboveRange(aggr, root);
/*     */     }
/* 169 */     return total;
/*     */   }
/*     */   
/*     */   private long aggregateBelowRange(Aggregate aggr, @Nullable AvlNode<E> node) {
/* 173 */     if (node == null) {
/* 174 */       return 0L;
/*     */     }
/* 176 */     int cmp = comparator().compare(this.range.getLowerEndpoint(), node.elem);
/* 177 */     if (cmp < 0)
/* 178 */       return aggregateBelowRange(aggr, node.left); 
/* 179 */     if (cmp == 0) {
/* 180 */       switch (this.range.getLowerBoundType()) {
/*     */         case OPEN:
/* 182 */           return aggr.nodeAggregate(node) + aggr.treeAggregate(node.left);
/*     */         case CLOSED:
/* 184 */           return aggr.treeAggregate(node.left);
/*     */       } 
/* 186 */       throw new AssertionError();
/*     */     } 
/*     */     
/* 189 */     return aggr.treeAggregate(node.left) + aggr
/* 190 */       .nodeAggregate(node) + 
/* 191 */       aggregateBelowRange(aggr, node.right);
/*     */   }
/*     */ 
/*     */   
/*     */   private long aggregateAboveRange(Aggregate aggr, @Nullable AvlNode<E> node) {
/* 196 */     if (node == null) {
/* 197 */       return 0L;
/*     */     }
/* 199 */     int cmp = comparator().compare(this.range.getUpperEndpoint(), node.elem);
/* 200 */     if (cmp > 0)
/* 201 */       return aggregateAboveRange(aggr, node.right); 
/* 202 */     if (cmp == 0) {
/* 203 */       switch (this.range.getUpperBoundType()) {
/*     */         case OPEN:
/* 205 */           return aggr.nodeAggregate(node) + aggr.treeAggregate(node.right);
/*     */         case CLOSED:
/* 207 */           return aggr.treeAggregate(node.right);
/*     */       } 
/* 209 */       throw new AssertionError();
/*     */     } 
/*     */     
/* 212 */     return aggr.treeAggregate(node.right) + aggr
/* 213 */       .nodeAggregate(node) + 
/* 214 */       aggregateAboveRange(aggr, node.left);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 220 */     return Ints.saturatedCast(aggregateForEntries(Aggregate.SIZE));
/*     */   }
/*     */ 
/*     */   
/*     */   int distinctElements() {
/* 225 */     return Ints.saturatedCast(aggregateForEntries(Aggregate.DISTINCT));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int count(@Nullable Object element) {
/*     */     try {
/* 232 */       E e = (E)element;
/* 233 */       AvlNode<E> root = this.rootReference.get();
/* 234 */       if (!this.range.contains(e) || root == null) {
/* 235 */         return 0;
/*     */       }
/* 237 */       return root.count(comparator(), e);
/* 238 */     } catch (ClassCastException e) {
/* 239 */       return 0;
/* 240 */     } catch (NullPointerException e) {
/* 241 */       return 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int add(@Nullable E element, int occurrences) {
/* 248 */     CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/* 249 */     if (occurrences == 0) {
/* 250 */       return count(element);
/*     */     }
/* 252 */     Preconditions.checkArgument(this.range.contains(element));
/* 253 */     AvlNode<E> root = this.rootReference.get();
/* 254 */     if (root == null) {
/* 255 */       comparator().compare(element, element);
/* 256 */       AvlNode<E> avlNode = new AvlNode<>(element, occurrences);
/* 257 */       successor(this.header, avlNode, this.header);
/* 258 */       this.rootReference.checkAndSet(root, avlNode);
/* 259 */       return 0;
/*     */     } 
/* 261 */     int[] result = new int[1];
/* 262 */     AvlNode<E> newRoot = root.add(comparator(), element, occurrences, result);
/* 263 */     this.rootReference.checkAndSet(root, newRoot);
/* 264 */     return result[0];
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int remove(@Nullable Object element, int occurrences) {
/*     */     AvlNode<E> newRoot;
/* 270 */     CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/* 271 */     if (occurrences == 0) {
/* 272 */       return count(element);
/*     */     }
/* 274 */     AvlNode<E> root = this.rootReference.get();
/* 275 */     int[] result = new int[1];
/*     */ 
/*     */     
/*     */     try {
/* 279 */       E e = (E)element;
/* 280 */       if (!this.range.contains(e) || root == null) {
/* 281 */         return 0;
/*     */       }
/* 283 */       newRoot = root.remove(comparator(), e, occurrences, result);
/* 284 */     } catch (ClassCastException e) {
/* 285 */       return 0;
/* 286 */     } catch (NullPointerException e) {
/* 287 */       return 0;
/*     */     } 
/* 289 */     this.rootReference.checkAndSet(root, newRoot);
/* 290 */     return result[0];
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int setCount(@Nullable E element, int count) {
/* 296 */     CollectPreconditions.checkNonnegative(count, "count");
/* 297 */     if (!this.range.contains(element)) {
/* 298 */       Preconditions.checkArgument((count == 0));
/* 299 */       return 0;
/*     */     } 
/*     */     
/* 302 */     AvlNode<E> root = this.rootReference.get();
/* 303 */     if (root == null) {
/* 304 */       if (count > 0) {
/* 305 */         add(element, count);
/*     */       }
/* 307 */       return 0;
/*     */     } 
/* 309 */     int[] result = new int[1];
/* 310 */     AvlNode<E> newRoot = root.setCount(comparator(), element, count, result);
/* 311 */     this.rootReference.checkAndSet(root, newRoot);
/* 312 */     return result[0];
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean setCount(@Nullable E element, int oldCount, int newCount) {
/* 318 */     CollectPreconditions.checkNonnegative(newCount, "newCount");
/* 319 */     CollectPreconditions.checkNonnegative(oldCount, "oldCount");
/* 320 */     Preconditions.checkArgument(this.range.contains(element));
/*     */     
/* 322 */     AvlNode<E> root = this.rootReference.get();
/* 323 */     if (root == null) {
/* 324 */       if (oldCount == 0) {
/* 325 */         if (newCount > 0) {
/* 326 */           add(element, newCount);
/*     */         }
/* 328 */         return true;
/*     */       } 
/* 330 */       return false;
/*     */     } 
/*     */     
/* 333 */     int[] result = new int[1];
/* 334 */     AvlNode<E> newRoot = root.setCount(comparator(), element, oldCount, newCount, result);
/* 335 */     this.rootReference.checkAndSet(root, newRoot);
/* 336 */     return (result[0] == oldCount);
/*     */   }
/*     */   
/*     */   private Multiset.Entry<E> wrapEntry(final AvlNode<E> baseEntry) {
/* 340 */     return new Multisets.AbstractEntry<E>()
/*     */       {
/*     */         public E getElement() {
/* 343 */           return baseEntry.getElement();
/*     */         }
/*     */ 
/*     */         
/*     */         public int getCount() {
/* 348 */           int result = baseEntry.getCount();
/* 349 */           if (result == 0) {
/* 350 */             return TreeMultiset.this.count(getElement());
/*     */           }
/* 352 */           return result;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private AvlNode<E> firstNode() {
/* 363 */     AvlNode<E> node, root = this.rootReference.get();
/* 364 */     if (root == null) {
/* 365 */       return null;
/*     */     }
/*     */     
/* 368 */     if (this.range.hasLowerBound()) {
/* 369 */       E endpoint = this.range.getLowerEndpoint();
/* 370 */       node = ((AvlNode)this.rootReference.get()).ceiling(comparator(), endpoint);
/* 371 */       if (node == null) {
/* 372 */         return null;
/*     */       }
/* 374 */       if (this.range.getLowerBoundType() == BoundType.OPEN && 
/* 375 */         comparator().compare(endpoint, node.getElement()) == 0) {
/* 376 */         node = node.succ;
/*     */       }
/*     */     } else {
/* 379 */       node = this.header.succ;
/*     */     } 
/* 381 */     return (node == this.header || !this.range.contains(node.getElement())) ? null : node;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private AvlNode<E> lastNode() {
/* 386 */     AvlNode<E> node, root = this.rootReference.get();
/* 387 */     if (root == null) {
/* 388 */       return null;
/*     */     }
/*     */     
/* 391 */     if (this.range.hasUpperBound()) {
/* 392 */       E endpoint = this.range.getUpperEndpoint();
/* 393 */       node = ((AvlNode)this.rootReference.get()).floor(comparator(), endpoint);
/* 394 */       if (node == null) {
/* 395 */         return null;
/*     */       }
/* 397 */       if (this.range.getUpperBoundType() == BoundType.OPEN && 
/* 398 */         comparator().compare(endpoint, node.getElement()) == 0) {
/* 399 */         node = node.pred;
/*     */       }
/*     */     } else {
/* 402 */       node = this.header.pred;
/*     */     } 
/* 404 */     return (node == this.header || !this.range.contains(node.getElement())) ? null : node;
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Multiset.Entry<E>> entryIterator() {
/* 409 */     return (Iterator)new Iterator<Multiset.Entry<Multiset.Entry<E>>>() {
/* 410 */         TreeMultiset.AvlNode<E> current = TreeMultiset.this.firstNode();
/*     */         
/*     */         Multiset.Entry<E> prevEntry;
/*     */         
/*     */         public boolean hasNext() {
/* 415 */           if (this.current == null)
/* 416 */             return false; 
/* 417 */           if (TreeMultiset.this.range.tooHigh(this.current.getElement())) {
/* 418 */             this.current = null;
/* 419 */             return false;
/*     */           } 
/* 421 */           return true;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public Multiset.Entry<E> next() {
/* 427 */           if (!hasNext()) {
/* 428 */             throw new NoSuchElementException();
/*     */           }
/* 430 */           Multiset.Entry<E> result = TreeMultiset.this.wrapEntry(this.current);
/* 431 */           this.prevEntry = result;
/* 432 */           if (this.current.succ == TreeMultiset.this.header) {
/* 433 */             this.current = null;
/*     */           } else {
/* 435 */             this.current = this.current.succ;
/*     */           } 
/* 437 */           return result;
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 442 */           CollectPreconditions.checkRemove((this.prevEntry != null));
/* 443 */           TreeMultiset.this.setCount(this.prevEntry.getElement(), 0);
/* 444 */           this.prevEntry = null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Multiset.Entry<E>> descendingEntryIterator() {
/* 451 */     return (Iterator)new Iterator<Multiset.Entry<Multiset.Entry<E>>>() {
/* 452 */         TreeMultiset.AvlNode<E> current = TreeMultiset.this.lastNode();
/* 453 */         Multiset.Entry<E> prevEntry = null;
/*     */ 
/*     */         
/*     */         public boolean hasNext() {
/* 457 */           if (this.current == null)
/* 458 */             return false; 
/* 459 */           if (TreeMultiset.this.range.tooLow(this.current.getElement())) {
/* 460 */             this.current = null;
/* 461 */             return false;
/*     */           } 
/* 463 */           return true;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public Multiset.Entry<E> next() {
/* 469 */           if (!hasNext()) {
/* 470 */             throw new NoSuchElementException();
/*     */           }
/* 472 */           Multiset.Entry<E> result = TreeMultiset.this.wrapEntry(this.current);
/* 473 */           this.prevEntry = result;
/* 474 */           if (this.current.pred == TreeMultiset.this.header) {
/* 475 */             this.current = null;
/*     */           } else {
/* 477 */             this.current = this.current.pred;
/*     */           } 
/* 479 */           return result;
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 484 */           CollectPreconditions.checkRemove((this.prevEntry != null));
/* 485 */           TreeMultiset.this.setCount(this.prevEntry.getElement(), 0);
/* 486 */           this.prevEntry = null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> headMultiset(@Nullable E upperBound, BoundType boundType) {
/* 493 */     return new TreeMultiset(this.rootReference, this.range
/*     */         
/* 495 */         .intersect(GeneralRange.upTo(comparator(), upperBound, boundType)), this.header);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> tailMultiset(@Nullable E lowerBound, BoundType boundType) {
/* 501 */     return new TreeMultiset(this.rootReference, this.range
/*     */         
/* 503 */         .intersect(GeneralRange.downTo(comparator(), lowerBound, boundType)), this.header);
/*     */   }
/*     */ 
/*     */   
/*     */   static int distinctElements(@Nullable AvlNode<?> node) {
/* 508 */     return (node == null) ? 0 : node.distinctElements;
/*     */   }
/*     */   
/*     */   private static final class Reference<T> { @Nullable
/*     */     private T value;
/*     */     
/*     */     @Nullable
/*     */     public T get() {
/* 516 */       return this.value;
/*     */     }
/*     */     private Reference() {}
/*     */     public void checkAndSet(@Nullable T expected, T newValue) {
/* 520 */       if (this.value != expected) {
/* 521 */         throw new ConcurrentModificationException();
/*     */       }
/* 523 */       this.value = newValue;
/*     */     } }
/*     */ 
/*     */   
/*     */   private static final class AvlNode<E>
/*     */     extends Multisets.AbstractEntry<E>
/*     */   {
/*     */     @Nullable
/*     */     private final E elem;
/*     */     private int elemCount;
/*     */     private int distinctElements;
/*     */     private long totalCount;
/*     */     private int height;
/*     */     private AvlNode<E> left;
/*     */     private AvlNode<E> right;
/*     */     private AvlNode<E> pred;
/*     */     private AvlNode<E> succ;
/*     */     
/*     */     AvlNode(@Nullable E elem, int elemCount) {
/* 542 */       Preconditions.checkArgument((elemCount > 0));
/* 543 */       this.elem = elem;
/* 544 */       this.elemCount = elemCount;
/* 545 */       this.totalCount = elemCount;
/* 546 */       this.distinctElements = 1;
/* 547 */       this.height = 1;
/* 548 */       this.left = null;
/* 549 */       this.right = null;
/*     */     }
/*     */     
/*     */     public int count(Comparator<? super E> comparator, E e) {
/* 553 */       int cmp = comparator.compare(e, this.elem);
/* 554 */       if (cmp < 0)
/* 555 */         return (this.left == null) ? 0 : this.left.count(comparator, e); 
/* 556 */       if (cmp > 0) {
/* 557 */         return (this.right == null) ? 0 : this.right.count(comparator, e);
/*     */       }
/* 559 */       return this.elemCount;
/*     */     }
/*     */ 
/*     */     
/*     */     private AvlNode<E> addRightChild(E e, int count) {
/* 564 */       this.right = new AvlNode(e, count);
/* 565 */       TreeMultiset.successor(this, this.right, this.succ);
/* 566 */       this.height = Math.max(2, this.height);
/* 567 */       this.distinctElements++;
/* 568 */       this.totalCount += count;
/* 569 */       return this;
/*     */     }
/*     */     
/*     */     private AvlNode<E> addLeftChild(E e, int count) {
/* 573 */       this.left = new AvlNode(e, count);
/* 574 */       TreeMultiset.successor(this.pred, this.left, this);
/* 575 */       this.height = Math.max(2, this.height);
/* 576 */       this.distinctElements++;
/* 577 */       this.totalCount += count;
/* 578 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     AvlNode<E> add(Comparator<? super E> comparator, @Nullable E e, int count, int[] result) {
/* 586 */       int cmp = comparator.compare(e, this.elem);
/* 587 */       if (cmp < 0) {
/* 588 */         AvlNode<E> initLeft = this.left;
/* 589 */         if (initLeft == null) {
/* 590 */           result[0] = 0;
/* 591 */           return addLeftChild(e, count);
/*     */         } 
/* 593 */         int initHeight = initLeft.height;
/*     */         
/* 595 */         this.left = initLeft.add(comparator, e, count, result);
/* 596 */         if (result[0] == 0) {
/* 597 */           this.distinctElements++;
/*     */         }
/* 599 */         this.totalCount += count;
/* 600 */         return (this.left.height == initHeight) ? this : rebalance();
/* 601 */       }  if (cmp > 0) {
/* 602 */         AvlNode<E> initRight = this.right;
/* 603 */         if (initRight == null) {
/* 604 */           result[0] = 0;
/* 605 */           return addRightChild(e, count);
/*     */         } 
/* 607 */         int initHeight = initRight.height;
/*     */         
/* 609 */         this.right = initRight.add(comparator, e, count, result);
/* 610 */         if (result[0] == 0) {
/* 611 */           this.distinctElements++;
/*     */         }
/* 613 */         this.totalCount += count;
/* 614 */         return (this.right.height == initHeight) ? this : rebalance();
/*     */       } 
/*     */ 
/*     */       
/* 618 */       result[0] = this.elemCount;
/* 619 */       long resultCount = this.elemCount + count;
/* 620 */       Preconditions.checkArgument((resultCount <= 2147483647L));
/* 621 */       this.elemCount += count;
/* 622 */       this.totalCount += count;
/* 623 */       return this;
/*     */     }
/*     */     
/*     */     AvlNode<E> remove(Comparator<? super E> comparator, @Nullable E e, int count, int[] result) {
/* 627 */       int cmp = comparator.compare(e, this.elem);
/* 628 */       if (cmp < 0) {
/* 629 */         AvlNode<E> initLeft = this.left;
/* 630 */         if (initLeft == null) {
/* 631 */           result[0] = 0;
/* 632 */           return this;
/*     */         } 
/*     */         
/* 635 */         this.left = initLeft.remove(comparator, e, count, result);
/*     */         
/* 637 */         if (result[0] > 0) {
/* 638 */           if (count >= result[0]) {
/* 639 */             this.distinctElements--;
/* 640 */             this.totalCount -= result[0];
/*     */           } else {
/* 642 */             this.totalCount -= count;
/*     */           } 
/*     */         }
/* 645 */         return (result[0] == 0) ? this : rebalance();
/* 646 */       }  if (cmp > 0) {
/* 647 */         AvlNode<E> initRight = this.right;
/* 648 */         if (initRight == null) {
/* 649 */           result[0] = 0;
/* 650 */           return this;
/*     */         } 
/*     */         
/* 653 */         this.right = initRight.remove(comparator, e, count, result);
/*     */         
/* 655 */         if (result[0] > 0) {
/* 656 */           if (count >= result[0]) {
/* 657 */             this.distinctElements--;
/* 658 */             this.totalCount -= result[0];
/*     */           } else {
/* 660 */             this.totalCount -= count;
/*     */           } 
/*     */         }
/* 663 */         return rebalance();
/*     */       } 
/*     */ 
/*     */       
/* 667 */       result[0] = this.elemCount;
/* 668 */       if (count >= this.elemCount) {
/* 669 */         return deleteMe();
/*     */       }
/* 671 */       this.elemCount -= count;
/* 672 */       this.totalCount -= count;
/* 673 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     AvlNode<E> setCount(Comparator<? super E> comparator, @Nullable E e, int count, int[] result) {
/* 678 */       int cmp = comparator.compare(e, this.elem);
/* 679 */       if (cmp < 0) {
/* 680 */         AvlNode<E> initLeft = this.left;
/* 681 */         if (initLeft == null) {
/* 682 */           result[0] = 0;
/* 683 */           return (count > 0) ? addLeftChild(e, count) : this;
/*     */         } 
/*     */         
/* 686 */         this.left = initLeft.setCount(comparator, e, count, result);
/*     */         
/* 688 */         if (count == 0 && result[0] != 0) {
/* 689 */           this.distinctElements--;
/* 690 */         } else if (count > 0 && result[0] == 0) {
/* 691 */           this.distinctElements++;
/*     */         } 
/*     */         
/* 694 */         this.totalCount += (count - result[0]);
/* 695 */         return rebalance();
/* 696 */       }  if (cmp > 0) {
/* 697 */         AvlNode<E> initRight = this.right;
/* 698 */         if (initRight == null) {
/* 699 */           result[0] = 0;
/* 700 */           return (count > 0) ? addRightChild(e, count) : this;
/*     */         } 
/*     */         
/* 703 */         this.right = initRight.setCount(comparator, e, count, result);
/*     */         
/* 705 */         if (count == 0 && result[0] != 0) {
/* 706 */           this.distinctElements--;
/* 707 */         } else if (count > 0 && result[0] == 0) {
/* 708 */           this.distinctElements++;
/*     */         } 
/*     */         
/* 711 */         this.totalCount += (count - result[0]);
/* 712 */         return rebalance();
/*     */       } 
/*     */ 
/*     */       
/* 716 */       result[0] = this.elemCount;
/* 717 */       if (count == 0) {
/* 718 */         return deleteMe();
/*     */       }
/* 720 */       this.totalCount += (count - this.elemCount);
/* 721 */       this.elemCount = count;
/* 722 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     AvlNode<E> setCount(Comparator<? super E> comparator, @Nullable E e, int expectedCount, int newCount, int[] result) {
/* 731 */       int cmp = comparator.compare(e, this.elem);
/* 732 */       if (cmp < 0) {
/* 733 */         AvlNode<E> initLeft = this.left;
/* 734 */         if (initLeft == null) {
/* 735 */           result[0] = 0;
/* 736 */           if (expectedCount == 0 && newCount > 0) {
/* 737 */             return addLeftChild(e, newCount);
/*     */           }
/* 739 */           return this;
/*     */         } 
/*     */         
/* 742 */         this.left = initLeft.setCount(comparator, e, expectedCount, newCount, result);
/*     */         
/* 744 */         if (result[0] == expectedCount) {
/* 745 */           if (newCount == 0 && result[0] != 0) {
/* 746 */             this.distinctElements--;
/* 747 */           } else if (newCount > 0 && result[0] == 0) {
/* 748 */             this.distinctElements++;
/*     */           } 
/* 750 */           this.totalCount += (newCount - result[0]);
/*     */         } 
/* 752 */         return rebalance();
/* 753 */       }  if (cmp > 0) {
/* 754 */         AvlNode<E> initRight = this.right;
/* 755 */         if (initRight == null) {
/* 756 */           result[0] = 0;
/* 757 */           if (expectedCount == 0 && newCount > 0) {
/* 758 */             return addRightChild(e, newCount);
/*     */           }
/* 760 */           return this;
/*     */         } 
/*     */         
/* 763 */         this.right = initRight.setCount(comparator, e, expectedCount, newCount, result);
/*     */         
/* 765 */         if (result[0] == expectedCount) {
/* 766 */           if (newCount == 0 && result[0] != 0) {
/* 767 */             this.distinctElements--;
/* 768 */           } else if (newCount > 0 && result[0] == 0) {
/* 769 */             this.distinctElements++;
/*     */           } 
/* 771 */           this.totalCount += (newCount - result[0]);
/*     */         } 
/* 773 */         return rebalance();
/*     */       } 
/*     */ 
/*     */       
/* 777 */       result[0] = this.elemCount;
/* 778 */       if (expectedCount == this.elemCount) {
/* 779 */         if (newCount == 0) {
/* 780 */           return deleteMe();
/*     */         }
/* 782 */         this.totalCount += (newCount - this.elemCount);
/* 783 */         this.elemCount = newCount;
/*     */       } 
/* 785 */       return this;
/*     */     }
/*     */     
/*     */     private AvlNode<E> deleteMe() {
/* 789 */       int oldElemCount = this.elemCount;
/* 790 */       this.elemCount = 0;
/* 791 */       TreeMultiset.successor(this.pred, this.succ);
/* 792 */       if (this.left == null)
/* 793 */         return this.right; 
/* 794 */       if (this.right == null)
/* 795 */         return this.left; 
/* 796 */       if (this.left.height >= this.right.height) {
/* 797 */         AvlNode<E> avlNode = this.pred;
/*     */         
/* 799 */         avlNode.left = this.left.removeMax(avlNode);
/* 800 */         avlNode.right = this.right;
/* 801 */         this.distinctElements--;
/* 802 */         this.totalCount -= oldElemCount;
/* 803 */         return avlNode.rebalance();
/*     */       } 
/* 805 */       AvlNode<E> newTop = this.succ;
/* 806 */       newTop.right = this.right.removeMin(newTop);
/* 807 */       newTop.left = this.left;
/* 808 */       this.distinctElements--;
/* 809 */       this.totalCount -= oldElemCount;
/* 810 */       return newTop.rebalance();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private AvlNode<E> removeMin(AvlNode<E> node) {
/* 816 */       if (this.left == null) {
/* 817 */         return this.right;
/*     */       }
/* 819 */       this.left = this.left.removeMin(node);
/* 820 */       this.distinctElements--;
/* 821 */       this.totalCount -= node.elemCount;
/* 822 */       return rebalance();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private AvlNode<E> removeMax(AvlNode<E> node) {
/* 828 */       if (this.right == null) {
/* 829 */         return this.left;
/*     */       }
/* 831 */       this.right = this.right.removeMax(node);
/* 832 */       this.distinctElements--;
/* 833 */       this.totalCount -= node.elemCount;
/* 834 */       return rebalance();
/*     */     }
/*     */ 
/*     */     
/*     */     private void recomputeMultiset() {
/* 839 */       this
/* 840 */         .distinctElements = 1 + TreeMultiset.distinctElements(this.left) + TreeMultiset.distinctElements(this.right);
/* 841 */       this.totalCount = this.elemCount + totalCount(this.left) + totalCount(this.right);
/*     */     }
/*     */     
/*     */     private void recomputeHeight() {
/* 845 */       this.height = 1 + Math.max(height(this.left), height(this.right));
/*     */     }
/*     */     
/*     */     private void recompute() {
/* 849 */       recomputeMultiset();
/* 850 */       recomputeHeight();
/*     */     }
/*     */     
/*     */     private AvlNode<E> rebalance() {
/* 854 */       switch (balanceFactor()) {
/*     */         case -2:
/* 856 */           if (this.right.balanceFactor() > 0) {
/* 857 */             this.right = this.right.rotateRight();
/*     */           }
/* 859 */           return rotateLeft();
/*     */         case 2:
/* 861 */           if (this.left.balanceFactor() < 0) {
/* 862 */             this.left = this.left.rotateLeft();
/*     */           }
/* 864 */           return rotateRight();
/*     */       } 
/* 866 */       recomputeHeight();
/* 867 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     private int balanceFactor() {
/* 872 */       return height(this.left) - height(this.right);
/*     */     }
/*     */     
/*     */     private AvlNode<E> rotateLeft() {
/* 876 */       Preconditions.checkState((this.right != null));
/* 877 */       AvlNode<E> newTop = this.right;
/* 878 */       this.right = newTop.left;
/* 879 */       newTop.left = this;
/* 880 */       newTop.totalCount = this.totalCount;
/* 881 */       newTop.distinctElements = this.distinctElements;
/* 882 */       recompute();
/* 883 */       newTop.recomputeHeight();
/* 884 */       return newTop;
/*     */     }
/*     */     
/*     */     private AvlNode<E> rotateRight() {
/* 888 */       Preconditions.checkState((this.left != null));
/* 889 */       AvlNode<E> newTop = this.left;
/* 890 */       this.left = newTop.right;
/* 891 */       newTop.right = this;
/* 892 */       newTop.totalCount = this.totalCount;
/* 893 */       newTop.distinctElements = this.distinctElements;
/* 894 */       recompute();
/* 895 */       newTop.recomputeHeight();
/* 896 */       return newTop;
/*     */     }
/*     */     
/*     */     private static long totalCount(@Nullable AvlNode<?> node) {
/* 900 */       return (node == null) ? 0L : node.totalCount;
/*     */     }
/*     */     
/*     */     private static int height(@Nullable AvlNode<?> node) {
/* 904 */       return (node == null) ? 0 : node.height;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private AvlNode<E> ceiling(Comparator<? super E> comparator, E e) {
/* 909 */       int cmp = comparator.compare(e, this.elem);
/* 910 */       if (cmp < 0)
/* 911 */         return (this.left == null) ? this : (AvlNode<E>)MoreObjects.firstNonNull(this.left.ceiling(comparator, e), this); 
/* 912 */       if (cmp == 0) {
/* 913 */         return this;
/*     */       }
/* 915 */       return (this.right == null) ? null : this.right.ceiling(comparator, e);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private AvlNode<E> floor(Comparator<? super E> comparator, E e) {
/* 921 */       int cmp = comparator.compare(e, this.elem);
/* 922 */       if (cmp > 0)
/* 923 */         return (this.right == null) ? this : (AvlNode<E>)MoreObjects.firstNonNull(this.right.floor(comparator, e), this); 
/* 924 */       if (cmp == 0) {
/* 925 */         return this;
/*     */       }
/* 927 */       return (this.left == null) ? null : this.left.floor(comparator, e);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public E getElement() {
/* 933 */       return this.elem;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getCount() {
/* 938 */       return this.elemCount;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 943 */       return Multisets.<E>immutableEntry(getElement(), getCount()).toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static <T> void successor(AvlNode<T> a, AvlNode<T> b) {
/* 948 */     a.succ = b;
/* 949 */     b.pred = a;
/*     */   }
/*     */   
/*     */   private static <T> void successor(AvlNode<T> a, AvlNode<T> b, AvlNode<T> c) {
/* 953 */     successor(a, b);
/* 954 */     successor(b, c);
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
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 969 */     stream.defaultWriteObject();
/* 970 */     stream.writeObject(elementSet().comparator());
/* 971 */     Serialization.writeMultiset(this, stream);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 976 */     stream.defaultReadObject();
/*     */ 
/*     */     
/* 979 */     Comparator<? super E> comparator = (Comparator<? super E>)stream.readObject();
/* 980 */     Serialization.<AbstractSortedMultiset>getFieldSetter(AbstractSortedMultiset.class, "comparator").set(this, comparator);
/* 981 */     Serialization.<TreeMultiset<E>>getFieldSetter((Class)TreeMultiset.class, "range")
/* 982 */       .set(this, GeneralRange.all(comparator));
/* 983 */     Serialization.<TreeMultiset<E>>getFieldSetter((Class)TreeMultiset.class, "rootReference")
/* 984 */       .set(this, new Reference());
/* 985 */     AvlNode<E> header = new AvlNode<>(null, 1);
/* 986 */     Serialization.<TreeMultiset<E>>getFieldSetter((Class)TreeMultiset.class, "header").set(this, header);
/* 987 */     successor(header, header);
/* 988 */     Serialization.populateMultiset(this, stream);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\TreeMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */