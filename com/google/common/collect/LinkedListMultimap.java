/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractSequentialList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public class LinkedListMultimap<K, V>
/*     */   extends AbstractMultimap<K, V>
/*     */   implements ListMultimap<K, V>, Serializable
/*     */ {
/*     */   private transient Node<K, V> head;
/*     */   private transient Node<K, V> tail;
/*     */   private transient Map<K, KeyList<K, V>> keyToKeyList;
/*     */   private transient int size;
/*     */   private transient int modCount;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   private static final class Node<K, V>
/*     */     extends AbstractMapEntry<K, V>
/*     */   {
/*     */     final K key;
/*     */     V value;
/*     */     Node<K, V> next;
/*     */     Node<K, V> previous;
/*     */     Node<K, V> nextSibling;
/*     */     Node<K, V> previousSibling;
/*     */     
/*     */     Node(@Nullable K key, @Nullable V value) {
/* 122 */       this.key = key;
/* 123 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public K getKey() {
/* 128 */       return this.key;
/*     */     }
/*     */ 
/*     */     
/*     */     public V getValue() {
/* 133 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public V setValue(@Nullable V newValue) {
/* 138 */       V result = this.value;
/* 139 */       this.value = newValue;
/* 140 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class KeyList<K, V> {
/*     */     LinkedListMultimap.Node<K, V> head;
/*     */     LinkedListMultimap.Node<K, V> tail;
/*     */     int count;
/*     */     
/*     */     KeyList(LinkedListMultimap.Node<K, V> firstNode) {
/* 150 */       this.head = firstNode;
/* 151 */       this.tail = firstNode;
/* 152 */       firstNode.previousSibling = null;
/* 153 */       firstNode.nextSibling = null;
/* 154 */       this.count = 1;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> LinkedListMultimap<K, V> create() {
/* 175 */     return new LinkedListMultimap<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> LinkedListMultimap<K, V> create(int expectedKeys) {
/* 186 */     return new LinkedListMultimap<>(expectedKeys);
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
/*     */   public static <K, V> LinkedListMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
/* 198 */     return new LinkedListMultimap<>(multimap);
/*     */   }
/*     */   
/*     */   LinkedListMultimap() {
/* 202 */     this.keyToKeyList = Maps.newHashMap();
/*     */   }
/*     */   
/*     */   private LinkedListMultimap(int expectedKeys) {
/* 206 */     this.keyToKeyList = new HashMap<>(expectedKeys);
/*     */   }
/*     */   
/*     */   private LinkedListMultimap(Multimap<? extends K, ? extends V> multimap) {
/* 210 */     this(multimap.keySet().size());
/* 211 */     putAll(multimap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private Node<K, V> addNode(@Nullable K key, @Nullable V value, @Nullable Node<K, V> nextSibling) {
/* 222 */     Node<K, V> node = new Node<>(key, value);
/* 223 */     if (this.head == null) {
/* 224 */       this.head = this.tail = node;
/* 225 */       this.keyToKeyList.put(key, new KeyList<>(node));
/* 226 */       this.modCount++;
/* 227 */     } else if (nextSibling == null) {
/* 228 */       this.tail.next = node;
/* 229 */       node.previous = this.tail;
/* 230 */       this.tail = node;
/* 231 */       KeyList<K, V> keyList = this.keyToKeyList.get(key);
/* 232 */       if (keyList == null) {
/* 233 */         this.keyToKeyList.put(key, keyList = new KeyList<>(node));
/* 234 */         this.modCount++;
/*     */       } else {
/* 236 */         keyList.count++;
/* 237 */         Node<K, V> keyTail = keyList.tail;
/* 238 */         keyTail.nextSibling = node;
/* 239 */         node.previousSibling = keyTail;
/* 240 */         keyList.tail = node;
/*     */       } 
/*     */     } else {
/* 243 */       KeyList<K, V> keyList = this.keyToKeyList.get(key);
/* 244 */       keyList.count++;
/* 245 */       node.previous = nextSibling.previous;
/* 246 */       node.previousSibling = nextSibling.previousSibling;
/* 247 */       node.next = nextSibling;
/* 248 */       node.nextSibling = nextSibling;
/* 249 */       if (nextSibling.previousSibling == null) {
/* 250 */         ((KeyList)this.keyToKeyList.get(key)).head = node;
/*     */       } else {
/* 252 */         nextSibling.previousSibling.nextSibling = node;
/*     */       } 
/* 254 */       if (nextSibling.previous == null) {
/* 255 */         this.head = node;
/*     */       } else {
/* 257 */         nextSibling.previous.next = node;
/*     */       } 
/* 259 */       nextSibling.previous = node;
/* 260 */       nextSibling.previousSibling = node;
/*     */     } 
/* 262 */     this.size++;
/* 263 */     return node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeNode(Node<K, V> node) {
/* 272 */     if (node.previous != null) {
/* 273 */       node.previous.next = node.next;
/*     */     } else {
/* 275 */       this.head = node.next;
/*     */     } 
/* 277 */     if (node.next != null) {
/* 278 */       node.next.previous = node.previous;
/*     */     } else {
/* 280 */       this.tail = node.previous;
/*     */     } 
/* 282 */     if (node.previousSibling == null && node.nextSibling == null) {
/* 283 */       KeyList<K, V> keyList = this.keyToKeyList.remove(node.key);
/* 284 */       keyList.count = 0;
/* 285 */       this.modCount++;
/*     */     } else {
/* 287 */       KeyList<K, V> keyList = this.keyToKeyList.get(node.key);
/* 288 */       keyList.count--;
/*     */       
/* 290 */       if (node.previousSibling == null) {
/* 291 */         keyList.head = node.nextSibling;
/*     */       } else {
/* 293 */         node.previousSibling.nextSibling = node.nextSibling;
/*     */       } 
/*     */       
/* 296 */       if (node.nextSibling == null) {
/* 297 */         keyList.tail = node.previousSibling;
/*     */       } else {
/* 299 */         node.nextSibling.previousSibling = node.previousSibling;
/*     */       } 
/*     */     } 
/* 302 */     this.size--;
/*     */   }
/*     */ 
/*     */   
/*     */   private void removeAllNodes(@Nullable Object key) {
/* 307 */     Iterators.clear(new ValueForKeyIterator(key));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void checkElement(@Nullable Object node) {
/* 312 */     if (node == null)
/* 313 */       throw new NoSuchElementException(); 
/*     */   }
/*     */   
/*     */   private class NodeIterator
/*     */     implements ListIterator<Map.Entry<K, V>>
/*     */   {
/*     */     int nextIndex;
/*     */     LinkedListMultimap.Node<K, V> next;
/*     */     LinkedListMultimap.Node<K, V> current;
/*     */     LinkedListMultimap.Node<K, V> previous;
/* 323 */     int expectedModCount = LinkedListMultimap.this.modCount;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void checkForConcurrentModification() {
/* 344 */       if (LinkedListMultimap.this.modCount != this.expectedModCount) {
/* 345 */         throw new ConcurrentModificationException();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 351 */       checkForConcurrentModification();
/* 352 */       return (this.next != null);
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public LinkedListMultimap.Node<K, V> next() {
/* 358 */       checkForConcurrentModification();
/* 359 */       LinkedListMultimap.checkElement(this.next);
/* 360 */       this.previous = this.current = this.next;
/* 361 */       this.next = this.next.next;
/* 362 */       this.nextIndex++;
/* 363 */       return this.current;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 368 */       checkForConcurrentModification();
/* 369 */       CollectPreconditions.checkRemove((this.current != null));
/* 370 */       if (this.current != this.next) {
/* 371 */         this.previous = this.current.previous;
/* 372 */         this.nextIndex--;
/*     */       } else {
/* 374 */         this.next = this.current.next;
/*     */       } 
/* 376 */       LinkedListMultimap.this.removeNode(this.current);
/* 377 */       this.current = null;
/* 378 */       this.expectedModCount = LinkedListMultimap.this.modCount;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasPrevious() {
/* 383 */       checkForConcurrentModification();
/* 384 */       return (this.previous != null);
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public LinkedListMultimap.Node<K, V> previous() {
/* 390 */       checkForConcurrentModification();
/* 391 */       LinkedListMultimap.checkElement(this.previous);
/* 392 */       this.next = this.current = this.previous;
/* 393 */       this.previous = this.previous.previous;
/* 394 */       this.nextIndex--;
/* 395 */       return this.current;
/*     */     }
/*     */ 
/*     */     
/*     */     public int nextIndex() {
/* 400 */       return this.nextIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     public int previousIndex() {
/* 405 */       return this.nextIndex - 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void set(Map.Entry<K, V> e) {
/* 410 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(Map.Entry<K, V> e) {
/* 415 */       throw new UnsupportedOperationException(); }
/*     */     NodeIterator(int index) { int size = LinkedListMultimap.this.size(); Preconditions.checkPositionIndex(index, size); if (index >= size / 2) { this.previous = LinkedListMultimap.this.tail; this.nextIndex = size; while (index++ < size)
/*     */           previous();  } else { this.next = LinkedListMultimap.this.head; while (index-- > 0)
/*     */           next();  }
/* 419 */        this.current = null; } void setValue(V value) { Preconditions.checkState((this.current != null));
/* 420 */       this.current.value = value; }
/*     */   
/*     */   }
/*     */   
/*     */   private class DistinctKeyIterator
/*     */     implements Iterator<K> {
/* 426 */     final Set<K> seenKeys = Sets.newHashSetWithExpectedSize(LinkedListMultimap.this.keySet().size());
/* 427 */     LinkedListMultimap.Node<K, V> next = LinkedListMultimap.this.head;
/*     */     LinkedListMultimap.Node<K, V> current;
/* 429 */     int expectedModCount = LinkedListMultimap.this.modCount;
/*     */     
/*     */     private void checkForConcurrentModification() {
/* 432 */       if (LinkedListMultimap.this.modCount != this.expectedModCount) {
/* 433 */         throw new ConcurrentModificationException();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 439 */       checkForConcurrentModification();
/* 440 */       return (this.next != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public K next() {
/* 445 */       checkForConcurrentModification();
/* 446 */       LinkedListMultimap.checkElement(this.next);
/* 447 */       this.current = this.next;
/* 448 */       this.seenKeys.add(this.current.key);
/*     */       do {
/* 450 */         this.next = this.next.next;
/* 451 */       } while (this.next != null && !this.seenKeys.add(this.next.key));
/* 452 */       return this.current.key;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 457 */       checkForConcurrentModification();
/* 458 */       CollectPreconditions.checkRemove((this.current != null));
/* 459 */       LinkedListMultimap.this.removeAllNodes(this.current.key);
/* 460 */       this.current = null;
/* 461 */       this.expectedModCount = LinkedListMultimap.this.modCount;
/*     */     }
/*     */     
/*     */     private DistinctKeyIterator() {}
/*     */   }
/*     */   
/*     */   private class ValueForKeyIterator implements ListIterator<V> {
/*     */     final Object key;
/*     */     int nextIndex;
/*     */     LinkedListMultimap.Node<K, V> next;
/*     */     LinkedListMultimap.Node<K, V> current;
/*     */     LinkedListMultimap.Node<K, V> previous;
/*     */     
/*     */     ValueForKeyIterator(Object key) {
/* 475 */       this.key = key;
/* 476 */       LinkedListMultimap.KeyList<K, V> keyList = (LinkedListMultimap.KeyList<K, V>)LinkedListMultimap.this.keyToKeyList.get(key);
/* 477 */       this.next = (keyList == null) ? null : keyList.head;
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
/*     */     public ValueForKeyIterator(Object key, int index) {
/* 490 */       LinkedListMultimap.KeyList<K, V> keyList = (LinkedListMultimap.KeyList<K, V>)LinkedListMultimap.this.keyToKeyList.get(key);
/* 491 */       int size = (keyList == null) ? 0 : keyList.count;
/* 492 */       Preconditions.checkPositionIndex(index, size);
/* 493 */       if (index >= size / 2) {
/* 494 */         this.previous = (keyList == null) ? null : keyList.tail;
/* 495 */         this.nextIndex = size;
/* 496 */         while (index++ < size) {
/* 497 */           previous();
/*     */         }
/*     */       } else {
/* 500 */         this.next = (keyList == null) ? null : keyList.head;
/* 501 */         while (index-- > 0) {
/* 502 */           next();
/*     */         }
/*     */       } 
/* 505 */       this.key = key;
/* 506 */       this.current = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 511 */       return (this.next != null);
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public V next() {
/* 517 */       LinkedListMultimap.checkElement(this.next);
/* 518 */       this.previous = this.current = this.next;
/* 519 */       this.next = this.next.nextSibling;
/* 520 */       this.nextIndex++;
/* 521 */       return this.current.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasPrevious() {
/* 526 */       return (this.previous != null);
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public V previous() {
/* 532 */       LinkedListMultimap.checkElement(this.previous);
/* 533 */       this.next = this.current = this.previous;
/* 534 */       this.previous = this.previous.previousSibling;
/* 535 */       this.nextIndex--;
/* 536 */       return this.current.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public int nextIndex() {
/* 541 */       return this.nextIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     public int previousIndex() {
/* 546 */       return this.nextIndex - 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 551 */       CollectPreconditions.checkRemove((this.current != null));
/* 552 */       if (this.current != this.next) {
/* 553 */         this.previous = this.current.previousSibling;
/* 554 */         this.nextIndex--;
/*     */       } else {
/* 556 */         this.next = this.current.nextSibling;
/*     */       } 
/* 558 */       LinkedListMultimap.this.removeNode(this.current);
/* 559 */       this.current = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void set(V value) {
/* 564 */       Preconditions.checkState((this.current != null));
/* 565 */       this.current.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void add(V value) {
/* 571 */       this.previous = LinkedListMultimap.this.addNode((K)this.key, value, this.next);
/* 572 */       this.nextIndex++;
/* 573 */       this.current = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 581 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 586 */     return (this.head == null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(@Nullable Object key) {
/* 591 */     return this.keyToKeyList.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/* 596 */     return values().contains(value);
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
/*     */   @CanIgnoreReturnValue
/*     */   public boolean put(@Nullable K key, @Nullable V value) {
/* 611 */     addNode(key, value, null);
/* 612 */     return true;
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
/*     */   @CanIgnoreReturnValue
/*     */   public List<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
/* 630 */     List<V> oldValues = getCopy(key);
/* 631 */     ListIterator<V> keyValues = new ValueForKeyIterator(key);
/* 632 */     Iterator<? extends V> newValues = values.iterator();
/*     */ 
/*     */     
/* 635 */     while (keyValues.hasNext() && newValues.hasNext()) {
/* 636 */       keyValues.next();
/* 637 */       keyValues.set(newValues.next());
/*     */     } 
/*     */ 
/*     */     
/* 641 */     while (keyValues.hasNext()) {
/* 642 */       keyValues.next();
/* 643 */       keyValues.remove();
/*     */     } 
/*     */ 
/*     */     
/* 647 */     while (newValues.hasNext()) {
/* 648 */       keyValues.add(newValues.next());
/*     */     }
/*     */     
/* 651 */     return oldValues;
/*     */   }
/*     */   
/*     */   private List<V> getCopy(@Nullable Object key) {
/* 655 */     return Collections.unmodifiableList(Lists.newArrayList(new ValueForKeyIterator(key)));
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
/*     */   public List<V> removeAll(@Nullable Object key) {
/* 667 */     List<V> oldValues = getCopy(key);
/* 668 */     removeAllNodes(key);
/* 669 */     return oldValues;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 674 */     this.head = null;
/* 675 */     this.tail = null;
/* 676 */     this.keyToKeyList.clear();
/* 677 */     this.size = 0;
/* 678 */     this.modCount++;
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
/*     */   public List<V> get(@Nullable final K key) {
/* 694 */     return new AbstractSequentialList<V>()
/*     */       {
/*     */         public int size() {
/* 697 */           LinkedListMultimap.KeyList<K, V> keyList = (LinkedListMultimap.KeyList<K, V>)LinkedListMultimap.this.keyToKeyList.get(key);
/* 698 */           return (keyList == null) ? 0 : keyList.count;
/*     */         }
/*     */ 
/*     */         
/*     */         public ListIterator<V> listIterator(int index) {
/* 703 */           return new LinkedListMultimap.ValueForKeyIterator(key, index);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Set<K> createKeySet() {
/*     */     class KeySetImpl
/*     */       extends Sets.ImprovedAbstractSet<K>
/*     */     {
/*     */       public int size() {
/* 714 */         return LinkedListMultimap.this.keyToKeyList.size();
/*     */       }
/*     */ 
/*     */       
/*     */       public Iterator<K> iterator() {
/* 719 */         return new LinkedListMultimap.DistinctKeyIterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean contains(Object key) {
/* 724 */         return LinkedListMultimap.this.containsKey(key);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object o) {
/* 729 */         return !LinkedListMultimap.this.removeAll(o).isEmpty();
/*     */       }
/*     */     };
/* 732 */     return new KeySetImpl();
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
/*     */   public List<V> values() {
/* 746 */     return (List<V>)super.values();
/*     */   }
/*     */ 
/*     */   
/*     */   List<V> createValues() {
/*     */     class ValuesImpl
/*     */       extends AbstractSequentialList<V>
/*     */     {
/*     */       public int size() {
/* 755 */         return LinkedListMultimap.this.size;
/*     */       }
/*     */ 
/*     */       
/*     */       public ListIterator<V> listIterator(int index) {
/* 760 */         final LinkedListMultimap<K, V>.NodeIterator nodeItr = new LinkedListMultimap.NodeIterator(index);
/* 761 */         return new TransformedListIterator<Map.Entry<K, V>, V>(nodeItr)
/*     */           {
/*     */             V transform(Map.Entry<K, V> entry) {
/* 764 */               return entry.getValue();
/*     */             }
/*     */ 
/*     */             
/*     */             public void set(V value) {
/* 769 */               nodeItr.setValue(value);
/*     */             }
/*     */           };
/*     */       }
/*     */     };
/* 774 */     return new ValuesImpl();
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
/*     */   public List<Map.Entry<K, V>> entries() {
/* 797 */     return (List<Map.Entry<K, V>>)super.entries();
/*     */   }
/*     */ 
/*     */   
/*     */   List<Map.Entry<K, V>> createEntries() {
/*     */     class EntriesImpl
/*     */       extends AbstractSequentialList<Map.Entry<K, V>>
/*     */     {
/*     */       public int size() {
/* 806 */         return LinkedListMultimap.this.size;
/*     */       }
/*     */ 
/*     */       
/*     */       public ListIterator<Map.Entry<K, V>> listIterator(int index) {
/* 811 */         return new LinkedListMultimap.NodeIterator(index);
/*     */       }
/*     */ 
/*     */       
/*     */       public void forEach(Consumer<? super Map.Entry<K, V>> action) {
/* 816 */         Preconditions.checkNotNull(action);
/* 817 */         for (LinkedListMultimap.Node<K, V> node = LinkedListMultimap.this.head; node != null; node = node.next) {
/* 818 */           action.accept(node);
/*     */         }
/*     */       }
/*     */     };
/* 822 */     return new EntriesImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Map.Entry<K, V>> entryIterator() {
/* 827 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */   
/*     */   Map<K, Collection<V>> createAsMap() {
/* 832 */     return new Multimaps.AsMap<>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 842 */     stream.defaultWriteObject();
/* 843 */     stream.writeInt(size());
/* 844 */     for (Map.Entry<K, V> entry : entries()) {
/* 845 */       stream.writeObject(entry.getKey());
/* 846 */       stream.writeObject(entry.getValue());
/*     */     } 
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 852 */     stream.defaultReadObject();
/* 853 */     this.keyToKeyList = Maps.newLinkedHashMap();
/* 854 */     int size = stream.readInt();
/* 855 */     for (int i = 0; i < size; i++) {
/*     */       
/* 857 */       K key = (K)stream.readObject();
/*     */       
/* 859 */       V value = (V)stream.readObject();
/* 860 */       put(key, value);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\LinkedListMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */