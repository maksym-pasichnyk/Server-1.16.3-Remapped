/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.util.AbstractQueue;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Queue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class MinMaxPriorityQueue<E>
/*     */   extends AbstractQueue<E>
/*     */ {
/*     */   private final Heap minHeap;
/*     */   private final Heap maxHeap;
/*     */   @VisibleForTesting
/*     */   final int maximumSize;
/*     */   private Object[] queue;
/*     */   private int size;
/*     */   private int modCount;
/*     */   private static final int EVEN_POWERS_OF_TWO = 1431655765;
/*     */   private static final int ODD_POWERS_OF_TWO = -1431655766;
/*     */   private static final int DEFAULT_CAPACITY = 11;
/*     */   
/*     */   public static <E extends Comparable<E>> MinMaxPriorityQueue<E> create() {
/* 113 */     return (new Builder(Ordering.natural())).create();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<E>> MinMaxPriorityQueue<E> create(Iterable<? extends E> initialContents) {
/* 122 */     return (new Builder(Ordering.natural())).create(initialContents);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <B> Builder<B> orderedBy(Comparator<B> comparator) {
/* 131 */     return new Builder<>(comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder<Comparable> expectedSize(int expectedSize) {
/* 140 */     return (new Builder<>(Ordering.natural())).expectedSize(expectedSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder<Comparable> maximumSize(int maximumSize) {
/* 151 */     return (new Builder<>(Ordering.natural())).maximumSize(maximumSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static final class Builder<B>
/*     */   {
/*     */     private static final int UNSET_EXPECTED_SIZE = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Comparator<B> comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 176 */     private int expectedSize = -1;
/* 177 */     private int maximumSize = Integer.MAX_VALUE;
/*     */     
/*     */     private Builder(Comparator<B> comparator) {
/* 180 */       this.comparator = (Comparator<B>)Preconditions.checkNotNull(comparator);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<B> expectedSize(int expectedSize) {
/* 189 */       Preconditions.checkArgument((expectedSize >= 0));
/* 190 */       this.expectedSize = expectedSize;
/* 191 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<B> maximumSize(int maximumSize) {
/* 202 */       Preconditions.checkArgument((maximumSize > 0));
/* 203 */       this.maximumSize = maximumSize;
/* 204 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T extends B> MinMaxPriorityQueue<T> create() {
/* 212 */       return create(Collections.emptySet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T extends B> MinMaxPriorityQueue<T> create(Iterable<? extends T> initialContents) {
/* 222 */       MinMaxPriorityQueue<T> queue = new MinMaxPriorityQueue<>(this, MinMaxPriorityQueue.initialQueueSize(this.expectedSize, this.maximumSize, initialContents));
/* 223 */       for (T element : initialContents) {
/* 224 */         queue.offer(element);
/*     */       }
/* 226 */       return queue;
/*     */     }
/*     */ 
/*     */     
/*     */     private <T extends B> Ordering<T> ordering() {
/* 231 */       return Ordering.from(this.comparator);
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
/*     */   private MinMaxPriorityQueue(Builder<? super E> builder, int queueSize) {
/* 243 */     Ordering<E> ordering = builder.ordering();
/* 244 */     this.minHeap = new Heap(ordering);
/* 245 */     this.maxHeap = new Heap(ordering.reverse());
/* 246 */     this.minHeap.otherHeap = this.maxHeap;
/* 247 */     this.maxHeap.otherHeap = this.minHeap;
/*     */     
/* 249 */     this.maximumSize = builder.maximumSize;
/*     */     
/* 251 */     this.queue = new Object[queueSize];
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 256 */     return this.size;
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
/*     */   @CanIgnoreReturnValue
/*     */   public boolean add(E element) {
/* 270 */     offer(element);
/* 271 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean addAll(Collection<? extends E> newElements) {
/* 277 */     boolean modified = false;
/* 278 */     for (E element : newElements) {
/* 279 */       offer(element);
/* 280 */       modified = true;
/*     */     } 
/* 282 */     return modified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean offer(E element) {
/* 294 */     Preconditions.checkNotNull(element);
/* 295 */     this.modCount++;
/* 296 */     int insertIndex = this.size++;
/*     */     
/* 298 */     growIfNeeded();
/*     */ 
/*     */ 
/*     */     
/* 302 */     heapForIndex(insertIndex).bubbleUp(insertIndex, element);
/* 303 */     return (this.size <= this.maximumSize || pollLast() != element);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E poll() {
/* 309 */     return isEmpty() ? null : removeAndGet(0);
/*     */   }
/*     */ 
/*     */   
/*     */   E elementData(int index) {
/* 314 */     return (E)this.queue[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public E peek() {
/* 319 */     return isEmpty() ? null : elementData(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getMaxElementIndex() {
/* 326 */     switch (this.size) {
/*     */       case 1:
/* 328 */         return 0;
/*     */       case 2:
/* 330 */         return 1;
/*     */     } 
/*     */ 
/*     */     
/* 334 */     return (this.maxHeap.compareElements(1, 2) <= 0) ? 1 : 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E pollFirst() {
/* 344 */     return poll();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E removeFirst() {
/* 354 */     return remove();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E peekFirst() {
/* 362 */     return peek();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E pollLast() {
/* 371 */     return isEmpty() ? null : removeAndGet(getMaxElementIndex());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E removeLast() {
/* 381 */     if (isEmpty()) {
/* 382 */       throw new NoSuchElementException();
/*     */     }
/* 384 */     return removeAndGet(getMaxElementIndex());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E peekLast() {
/* 392 */     return isEmpty() ? null : elementData(getMaxElementIndex());
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
/*     */   @VisibleForTesting
/*     */   @CanIgnoreReturnValue
/*     */   MoveDesc<E> removeAt(int index) {
/* 413 */     Preconditions.checkPositionIndex(index, this.size);
/* 414 */     this.modCount++;
/* 415 */     this.size--;
/* 416 */     if (this.size == index) {
/* 417 */       this.queue[this.size] = null;
/* 418 */       return null;
/*     */     } 
/* 420 */     E actualLastElement = elementData(this.size);
/* 421 */     int lastElementAt = heapForIndex(this.size).swapWithConceptuallyLastElement(actualLastElement);
/* 422 */     if (lastElementAt == index) {
/*     */ 
/*     */ 
/*     */       
/* 426 */       this.queue[this.size] = null;
/* 427 */       return null;
/*     */     } 
/* 429 */     E toTrickle = elementData(this.size);
/* 430 */     this.queue[this.size] = null;
/* 431 */     MoveDesc<E> changes = fillHole(index, toTrickle);
/* 432 */     if (lastElementAt < index) {
/*     */       
/* 434 */       if (changes == null)
/*     */       {
/* 436 */         return new MoveDesc<>(actualLastElement, toTrickle);
/*     */       }
/*     */ 
/*     */       
/* 440 */       return new MoveDesc<>(actualLastElement, changes.replaced);
/*     */     } 
/*     */ 
/*     */     
/* 444 */     return changes;
/*     */   }
/*     */   
/*     */   private MoveDesc<E> fillHole(int index, E toTrickle) {
/* 448 */     Heap heap = heapForIndex(index);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 456 */     int vacated = heap.fillHoleAt(index);
/*     */     
/* 458 */     int bubbledTo = heap.bubbleUpAlternatingLevels(vacated, toTrickle);
/* 459 */     if (bubbledTo == vacated)
/*     */     {
/*     */ 
/*     */       
/* 463 */       return heap.tryCrossOverAndBubbleUp(index, vacated, toTrickle);
/*     */     }
/* 465 */     return (bubbledTo < index) ? new MoveDesc<>(toTrickle, elementData(index)) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   static class MoveDesc<E>
/*     */   {
/*     */     final E toTrickle;
/*     */     final E replaced;
/*     */     
/*     */     MoveDesc(E toTrickle, E replaced) {
/* 475 */       this.toTrickle = toTrickle;
/* 476 */       this.replaced = replaced;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private E removeAndGet(int index) {
/* 484 */     E value = elementData(index);
/* 485 */     removeAt(index);
/* 486 */     return value;
/*     */   }
/*     */   
/*     */   private Heap heapForIndex(int i) {
/* 490 */     return isEvenLevel(i) ? this.minHeap : this.maxHeap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static boolean isEvenLevel(int index) {
/* 498 */     int oneBased = index + 1 ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/* 499 */     Preconditions.checkState((oneBased > 0), "negative index");
/* 500 */     return ((oneBased & 0x55555555) > (oneBased & 0xAAAAAAAA));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   boolean isIntact() {
/* 511 */     for (int i = 1; i < this.size; i++) {
/* 512 */       if (!heapForIndex(i).verifyIndex(i)) {
/* 513 */         return false;
/*     */       }
/*     */     } 
/* 516 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class Heap
/*     */   {
/*     */     final Ordering<E> ordering;
/*     */ 
/*     */     
/*     */     @Weak
/*     */     Heap otherHeap;
/*     */ 
/*     */     
/*     */     Heap(Ordering<E> ordering) {
/* 531 */       this.ordering = ordering;
/*     */     }
/*     */     
/*     */     int compareElements(int a, int b) {
/* 535 */       return this.ordering.compare(MinMaxPriorityQueue.this.elementData(a), MinMaxPriorityQueue.this.elementData(b));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     MinMaxPriorityQueue.MoveDesc<E> tryCrossOverAndBubbleUp(int removeIndex, int vacated, E toTrickle) {
/*     */       E parent;
/* 544 */       int crossOver = crossOver(vacated, toTrickle);
/* 545 */       if (crossOver == vacated) {
/* 546 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 554 */       if (crossOver < removeIndex) {
/*     */ 
/*     */         
/* 557 */         parent = MinMaxPriorityQueue.this.elementData(removeIndex);
/*     */       } else {
/* 559 */         parent = MinMaxPriorityQueue.this.elementData(getParentIndex(removeIndex));
/*     */       } 
/*     */       
/* 562 */       if (this.otherHeap.bubbleUpAlternatingLevels(crossOver, toTrickle) < removeIndex) {
/* 563 */         return new MinMaxPriorityQueue.MoveDesc<>(toTrickle, parent);
/*     */       }
/* 565 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void bubbleUp(int index, E x) {
/*     */       Heap heap;
/* 573 */       int crossOver = crossOverUp(index, x);
/*     */ 
/*     */       
/* 576 */       if (crossOver == index) {
/* 577 */         heap = this;
/*     */       } else {
/* 579 */         index = crossOver;
/* 580 */         heap = this.otherHeap;
/*     */       } 
/* 582 */       heap.bubbleUpAlternatingLevels(index, x);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     int bubbleUpAlternatingLevels(int index, E x) {
/* 591 */       while (index > 2) {
/* 592 */         int grandParentIndex = getGrandparentIndex(index);
/* 593 */         E e = MinMaxPriorityQueue.this.elementData(grandParentIndex);
/* 594 */         if (this.ordering.compare(e, x) <= 0) {
/*     */           break;
/*     */         }
/* 597 */         MinMaxPriorityQueue.this.queue[index] = e;
/* 598 */         index = grandParentIndex;
/*     */       } 
/* 600 */       MinMaxPriorityQueue.this.queue[index] = x;
/* 601 */       return index;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int findMin(int index, int len) {
/* 610 */       if (index >= MinMaxPriorityQueue.this.size) {
/* 611 */         return -1;
/*     */       }
/* 613 */       Preconditions.checkState((index > 0));
/* 614 */       int limit = Math.min(index, MinMaxPriorityQueue.this.size - len) + len;
/* 615 */       int minIndex = index;
/* 616 */       for (int i = index + 1; i < limit; i++) {
/* 617 */         if (compareElements(i, minIndex) < 0) {
/* 618 */           minIndex = i;
/*     */         }
/*     */       } 
/* 621 */       return minIndex;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int findMinChild(int index) {
/* 628 */       return findMin(getLeftChildIndex(index), 2);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int findMinGrandChild(int index) {
/* 635 */       int leftChildIndex = getLeftChildIndex(index);
/* 636 */       if (leftChildIndex < 0) {
/* 637 */         return -1;
/*     */       }
/* 639 */       return findMin(getLeftChildIndex(leftChildIndex), 4);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int crossOverUp(int index, E x) {
/* 648 */       if (index == 0) {
/* 649 */         MinMaxPriorityQueue.this.queue[0] = x;
/* 650 */         return 0;
/*     */       } 
/* 652 */       int parentIndex = getParentIndex(index);
/* 653 */       E parentElement = MinMaxPriorityQueue.this.elementData(parentIndex);
/* 654 */       if (parentIndex != 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 659 */         int grandparentIndex = getParentIndex(parentIndex);
/* 660 */         int uncleIndex = getRightChildIndex(grandparentIndex);
/* 661 */         if (uncleIndex != parentIndex && getLeftChildIndex(uncleIndex) >= MinMaxPriorityQueue.this.size) {
/* 662 */           E uncleElement = MinMaxPriorityQueue.this.elementData(uncleIndex);
/* 663 */           if (this.ordering.compare(uncleElement, parentElement) < 0) {
/* 664 */             parentIndex = uncleIndex;
/* 665 */             parentElement = uncleElement;
/*     */           } 
/*     */         } 
/*     */       } 
/* 669 */       if (this.ordering.compare(parentElement, x) < 0) {
/* 670 */         MinMaxPriorityQueue.this.queue[index] = parentElement;
/* 671 */         MinMaxPriorityQueue.this.queue[parentIndex] = x;
/* 672 */         return parentIndex;
/*     */       } 
/* 674 */       MinMaxPriorityQueue.this.queue[index] = x;
/* 675 */       return index;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int swapWithConceptuallyLastElement(E actualLastElement) {
/* 689 */       int parentIndex = getParentIndex(MinMaxPriorityQueue.this.size);
/* 690 */       if (parentIndex != 0) {
/* 691 */         int grandparentIndex = getParentIndex(parentIndex);
/* 692 */         int uncleIndex = getRightChildIndex(grandparentIndex);
/* 693 */         if (uncleIndex != parentIndex && getLeftChildIndex(uncleIndex) >= MinMaxPriorityQueue.this.size) {
/* 694 */           E uncleElement = MinMaxPriorityQueue.this.elementData(uncleIndex);
/* 695 */           if (this.ordering.compare(uncleElement, actualLastElement) < 0) {
/* 696 */             MinMaxPriorityQueue.this.queue[uncleIndex] = actualLastElement;
/* 697 */             MinMaxPriorityQueue.this.queue[MinMaxPriorityQueue.this.size] = uncleElement;
/* 698 */             return uncleIndex;
/*     */           } 
/*     */         } 
/*     */       } 
/* 702 */       return MinMaxPriorityQueue.this.size;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int crossOver(int index, E x) {
/* 712 */       int minChildIndex = findMinChild(index);
/*     */ 
/*     */       
/* 715 */       if (minChildIndex > 0 && this.ordering.compare(MinMaxPriorityQueue.this.elementData(minChildIndex), x) < 0) {
/* 716 */         MinMaxPriorityQueue.this.queue[index] = MinMaxPriorityQueue.this.elementData(minChildIndex);
/* 717 */         MinMaxPriorityQueue.this.queue[minChildIndex] = x;
/* 718 */         return minChildIndex;
/*     */       } 
/* 720 */       return crossOverUp(index, x);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int fillHoleAt(int index) {
/*     */       int minGrandchildIndex;
/* 733 */       while ((minGrandchildIndex = findMinGrandChild(index)) > 0) {
/* 734 */         MinMaxPriorityQueue.this.queue[index] = MinMaxPriorityQueue.this.elementData(minGrandchildIndex);
/* 735 */         index = minGrandchildIndex;
/*     */       } 
/* 737 */       return index;
/*     */     }
/*     */     
/*     */     private boolean verifyIndex(int i) {
/* 741 */       if (getLeftChildIndex(i) < MinMaxPriorityQueue.this.size && compareElements(i, getLeftChildIndex(i)) > 0) {
/* 742 */         return false;
/*     */       }
/* 744 */       if (getRightChildIndex(i) < MinMaxPriorityQueue.this.size && compareElements(i, getRightChildIndex(i)) > 0) {
/* 745 */         return false;
/*     */       }
/* 747 */       if (i > 0 && compareElements(i, getParentIndex(i)) > 0) {
/* 748 */         return false;
/*     */       }
/* 750 */       if (i > 2 && compareElements(getGrandparentIndex(i), i) > 0) {
/* 751 */         return false;
/*     */       }
/* 753 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private int getLeftChildIndex(int i) {
/* 759 */       return i * 2 + 1;
/*     */     }
/*     */     
/*     */     private int getRightChildIndex(int i) {
/* 763 */       return i * 2 + 2;
/*     */     }
/*     */     
/*     */     private int getParentIndex(int i) {
/* 767 */       return (i - 1) / 2;
/*     */     }
/*     */     
/*     */     private int getGrandparentIndex(int i) {
/* 771 */       return getParentIndex(getParentIndex(i));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class QueueIterator
/*     */     implements Iterator<E>
/*     */   {
/* 782 */     private int cursor = -1;
/* 783 */     private int nextCursor = -1;
/* 784 */     private int expectedModCount = MinMaxPriorityQueue.this.modCount;
/*     */     
/*     */     private Queue<E> forgetMeNot;
/*     */     
/*     */     private List<E> skipMe;
/*     */     
/*     */     private E lastFromForgetMeNot;
/*     */     private boolean canRemove;
/*     */     
/*     */     public boolean hasNext() {
/* 794 */       checkModCount();
/* 795 */       nextNotInSkipMe(this.cursor + 1);
/* 796 */       return (this.nextCursor < MinMaxPriorityQueue.this.size() || (this.forgetMeNot != null && 
/* 797 */         !this.forgetMeNot.isEmpty()));
/*     */     }
/*     */ 
/*     */     
/*     */     public E next() {
/* 802 */       checkModCount();
/* 803 */       nextNotInSkipMe(this.cursor + 1);
/* 804 */       if (this.nextCursor < MinMaxPriorityQueue.this.size()) {
/* 805 */         this.cursor = this.nextCursor;
/* 806 */         this.canRemove = true;
/* 807 */         return MinMaxPriorityQueue.this.elementData(this.cursor);
/* 808 */       }  if (this.forgetMeNot != null) {
/* 809 */         this.cursor = MinMaxPriorityQueue.this.size();
/* 810 */         this.lastFromForgetMeNot = this.forgetMeNot.poll();
/* 811 */         if (this.lastFromForgetMeNot != null) {
/* 812 */           this.canRemove = true;
/* 813 */           return this.lastFromForgetMeNot;
/*     */         } 
/*     */       } 
/* 816 */       throw new NoSuchElementException("iterator moved past last element in queue.");
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 821 */       CollectPreconditions.checkRemove(this.canRemove);
/* 822 */       checkModCount();
/* 823 */       this.canRemove = false;
/* 824 */       this.expectedModCount++;
/* 825 */       if (this.cursor < MinMaxPriorityQueue.this.size()) {
/* 826 */         MinMaxPriorityQueue.MoveDesc<E> moved = MinMaxPriorityQueue.this.removeAt(this.cursor);
/* 827 */         if (moved != null) {
/* 828 */           if (this.forgetMeNot == null) {
/* 829 */             this.forgetMeNot = new ArrayDeque<>();
/* 830 */             this.skipMe = new ArrayList<>(3);
/*     */           } 
/* 832 */           if (!foundAndRemovedExactReference(this.skipMe, moved.toTrickle)) {
/* 833 */             this.forgetMeNot.add(moved.toTrickle);
/*     */           }
/* 835 */           if (!foundAndRemovedExactReference(this.forgetMeNot, moved.replaced)) {
/* 836 */             this.skipMe.add(moved.replaced);
/*     */           }
/*     */         } 
/* 839 */         this.cursor--;
/* 840 */         this.nextCursor--;
/*     */       } else {
/* 842 */         Preconditions.checkState(removeExact(this.lastFromForgetMeNot));
/* 843 */         this.lastFromForgetMeNot = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean foundAndRemovedExactReference(Iterable<E> elements, E target) {
/* 849 */       for (Iterator<E> it = elements.iterator(); it.hasNext(); ) {
/* 850 */         E element = it.next();
/* 851 */         if (element == target) {
/* 852 */           it.remove();
/* 853 */           return true;
/*     */         } 
/*     */       } 
/* 856 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean removeExact(Object target) {
/* 861 */       for (int i = 0; i < MinMaxPriorityQueue.this.size; i++) {
/* 862 */         if (MinMaxPriorityQueue.this.queue[i] == target) {
/* 863 */           MinMaxPriorityQueue.this.removeAt(i);
/* 864 */           return true;
/*     */         } 
/*     */       } 
/* 867 */       return false;
/*     */     }
/*     */     
/*     */     private void checkModCount() {
/* 871 */       if (MinMaxPriorityQueue.this.modCount != this.expectedModCount) {
/* 872 */         throw new ConcurrentModificationException();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void nextNotInSkipMe(int c) {
/* 881 */       if (this.nextCursor < c) {
/* 882 */         if (this.skipMe != null) {
/* 883 */           while (c < MinMaxPriorityQueue.this.size() && foundAndRemovedExactReference(this.skipMe, MinMaxPriorityQueue.this.elementData(c))) {
/* 884 */             c++;
/*     */           }
/*     */         }
/* 887 */         this.nextCursor = c;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private QueueIterator() {}
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
/*     */   public Iterator<E> iterator() {
/* 916 */     return new QueueIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 921 */     for (int i = 0; i < this.size; i++) {
/* 922 */       this.queue[i] = null;
/*     */     }
/* 924 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 929 */     Object[] copyTo = new Object[this.size];
/* 930 */     System.arraycopy(this.queue, 0, copyTo, 0, this.size);
/* 931 */     return copyTo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<? super E> comparator() {
/* 940 */     return this.minHeap.ordering;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   int capacity() {
/* 945 */     return this.queue.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static int initialQueueSize(int configuredExpectedSize, int maximumSize, Iterable<?> initialContents) {
/* 956 */     int result = (configuredExpectedSize == -1) ? 11 : configuredExpectedSize;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 962 */     if (initialContents instanceof Collection) {
/* 963 */       int initialSize = ((Collection)initialContents).size();
/* 964 */       result = Math.max(result, initialSize);
/*     */     } 
/*     */ 
/*     */     
/* 968 */     return capAtMaximumSize(result, maximumSize);
/*     */   }
/*     */   
/*     */   private void growIfNeeded() {
/* 972 */     if (this.size > this.queue.length) {
/* 973 */       int newCapacity = calculateNewCapacity();
/* 974 */       Object[] newQueue = new Object[newCapacity];
/* 975 */       System.arraycopy(this.queue, 0, newQueue, 0, this.queue.length);
/* 976 */       this.queue = newQueue;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int calculateNewCapacity() {
/* 982 */     int oldCapacity = this.queue.length;
/*     */ 
/*     */ 
/*     */     
/* 986 */     int newCapacity = (oldCapacity < 64) ? ((oldCapacity + 1) * 2) : IntMath.checkedMultiply(oldCapacity / 2, 3);
/* 987 */     return capAtMaximumSize(newCapacity, this.maximumSize);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int capAtMaximumSize(int queueSize, int maximumSize) {
/* 992 */     return Math.min(queueSize - 1, maximumSize) + 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\MinMaxPriorityQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */