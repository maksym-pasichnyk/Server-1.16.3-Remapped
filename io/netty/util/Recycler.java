/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.concurrent.FastThreadLocal;
/*     */ import io.netty.util.internal.MathUtil;
/*     */ import io.netty.util.internal.ObjectCleaner;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Recycler<T>
/*     */ {
/*  42 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(Recycler.class);
/*     */ 
/*     */   
/*  45 */   private static final Handle NOOP_HANDLE = new Handle()
/*     */     {
/*     */       public void recycle(Object object) {}
/*     */     };
/*     */ 
/*     */   
/*  51 */   private static final AtomicInteger ID_GENERATOR = new AtomicInteger(-2147483648);
/*  52 */   private static final int OWN_THREAD_ID = ID_GENERATOR.getAndIncrement();
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_INITIAL_MAX_CAPACITY_PER_THREAD = 4096;
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_MAX_CAPACITY_PER_THREAD;
/*     */ 
/*     */   
/*     */   private static final int INITIAL_CAPACITY;
/*     */ 
/*     */   
/*     */   static {
/*  65 */     int maxCapacityPerThread = SystemPropertyUtil.getInt("io.netty.recycler.maxCapacityPerThread", 
/*  66 */         SystemPropertyUtil.getInt("io.netty.recycler.maxCapacity", 4096));
/*  67 */     if (maxCapacityPerThread < 0) {
/*  68 */       maxCapacityPerThread = 4096;
/*     */     }
/*     */     
/*  71 */     DEFAULT_MAX_CAPACITY_PER_THREAD = maxCapacityPerThread;
/*     */   }
/*  73 */   private static final int MAX_SHARED_CAPACITY_FACTOR = Math.max(2, 
/*  74 */       SystemPropertyUtil.getInt("io.netty.recycler.maxSharedCapacityFactor", 2));
/*     */ 
/*     */   
/*  77 */   private static final int MAX_DELAYED_QUEUES_PER_THREAD = Math.max(0, 
/*  78 */       SystemPropertyUtil.getInt("io.netty.recycler.maxDelayedQueuesPerThread", 
/*     */         
/*  80 */         NettyRuntime.availableProcessors() * 2));
/*     */   
/*  82 */   private static final int LINK_CAPACITY = MathUtil.safeFindNextPositivePowerOfTwo(
/*  83 */       Math.max(SystemPropertyUtil.getInt("io.netty.recycler.linkCapacity", 16), 16));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   private static final int RATIO = MathUtil.safeFindNextPositivePowerOfTwo(SystemPropertyUtil.getInt("io.netty.recycler.ratio", 8));
/*     */   private final int maxCapacityPerThread; private final int maxSharedCapacityFactor; private final int ratioMask; private final int maxDelayedQueuesPerThread;
/*  90 */   static { if (logger.isDebugEnabled()) {
/*  91 */       if (DEFAULT_MAX_CAPACITY_PER_THREAD == 0) {
/*  92 */         logger.debug("-Dio.netty.recycler.maxCapacityPerThread: disabled");
/*  93 */         logger.debug("-Dio.netty.recycler.maxSharedCapacityFactor: disabled");
/*  94 */         logger.debug("-Dio.netty.recycler.linkCapacity: disabled");
/*  95 */         logger.debug("-Dio.netty.recycler.ratio: disabled");
/*     */       } else {
/*  97 */         logger.debug("-Dio.netty.recycler.maxCapacityPerThread: {}", Integer.valueOf(DEFAULT_MAX_CAPACITY_PER_THREAD));
/*  98 */         logger.debug("-Dio.netty.recycler.maxSharedCapacityFactor: {}", Integer.valueOf(MAX_SHARED_CAPACITY_FACTOR));
/*  99 */         logger.debug("-Dio.netty.recycler.linkCapacity: {}", Integer.valueOf(LINK_CAPACITY));
/* 100 */         logger.debug("-Dio.netty.recycler.ratio: {}", Integer.valueOf(RATIO));
/*     */       } 
/*     */     }
/*     */     
/* 104 */     INITIAL_CAPACITY = Math.min(DEFAULT_MAX_CAPACITY_PER_THREAD, 256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 224 */     DELAYED_RECYCLED = new FastThreadLocal<Map<Stack<?>, WeakOrderQueue>>()
/*     */       {
/*     */         protected Map<Recycler.Stack<?>, Recycler.WeakOrderQueue> initialValue()
/*     */         {
/* 228 */           return new WeakHashMap<Recycler.Stack<?>, Recycler.WeakOrderQueue>();
/*     */         }
/*     */       }; }
/*     */   private final FastThreadLocal<Stack<T>> threadLocal = new FastThreadLocal<Stack<T>>() {
/*     */       protected Recycler.Stack<T> initialValue() { return new Recycler.Stack<T>(Recycler.this, Thread.currentThread(), Recycler.this.maxCapacityPerThread, Recycler.this.maxSharedCapacityFactor, Recycler.this.ratioMask, Recycler.this.maxDelayedQueuesPerThread); } protected void onRemoval(Recycler.Stack<T> value) { if (value.threadRef.get() == Thread.currentThread() && Recycler.DELAYED_RECYCLED.isSet()) ((Map)Recycler.DELAYED_RECYCLED.get()).remove(value);  }
/*     */     }; private static final FastThreadLocal<Map<Stack<?>, WeakOrderQueue>> DELAYED_RECYCLED; protected Recycler() { this(DEFAULT_MAX_CAPACITY_PER_THREAD); } protected Recycler(int maxCapacityPerThread) { this(maxCapacityPerThread, MAX_SHARED_CAPACITY_FACTOR); } protected Recycler(int maxCapacityPerThread, int maxSharedCapacityFactor) { this(maxCapacityPerThread, maxSharedCapacityFactor, RATIO, MAX_DELAYED_QUEUES_PER_THREAD); } protected Recycler(int maxCapacityPerThread, int maxSharedCapacityFactor, int ratio, int maxDelayedQueuesPerThread) { this.ratioMask = MathUtil.safeFindNextPositivePowerOfTwo(ratio) - 1; if (maxCapacityPerThread <= 0) { this.maxCapacityPerThread = 0; this.maxSharedCapacityFactor = 1; this.maxDelayedQueuesPerThread = 0; } else { this.maxCapacityPerThread = maxCapacityPerThread; this.maxSharedCapacityFactor = Math.max(1, maxSharedCapacityFactor); this.maxDelayedQueuesPerThread = Math.max(0, maxDelayedQueuesPerThread); }  } public final T get() { if (this.maxCapacityPerThread == 0) return newObject(NOOP_HANDLE);  Stack<T> stack = (Stack<T>)this.threadLocal.get(); DefaultHandle<T> handle = stack.pop(); if (handle == null) { handle = stack.newHandle(); handle.value = newObject(handle); }  return (T)handle.value; } @Deprecated public final boolean recycle(T o, Handle<T> handle) { if (handle == NOOP_HANDLE) return false;  DefaultHandle<T> h = (DefaultHandle<T>)handle; if (h.stack.parent != this) return false;  h.recycle(o); return true; } final int threadLocalCapacity() { return ((Stack)this.threadLocal.get()).elements.length; } final int threadLocalSize() { return ((Stack)this.threadLocal.get()).size; } protected abstract T newObject(Handle<T> paramHandle); static final class DefaultHandle<T> implements Handle<T> {
/*     */     private int lastRecycledId; private int recycleId; boolean hasBeenRecycled; private Recycler.Stack<?> stack; private Object value; DefaultHandle(Recycler.Stack<?> stack) { this.stack = stack; } public void recycle(Object object) { if (object != this.value) throw new IllegalArgumentException("object does not belong to handle");  this.stack.push(this); }
/*     */   } private static final class WeakOrderQueue {
/* 236 */     static final WeakOrderQueue DUMMY = new WeakOrderQueue(); private final Head head;
/*     */     private Link tail;
/*     */     private WeakOrderQueue next;
/*     */     private final WeakReference<Thread> owner;
/*     */     
/* 241 */     static final class Link extends AtomicInteger { private final Recycler.DefaultHandle<?>[] elements = (Recycler.DefaultHandle<?>[])new Recycler.DefaultHandle[Recycler.LINK_CAPACITY];
/*     */       
/*     */       private int readIndex;
/*     */       
/*     */       Link next; }
/*     */ 
/*     */ 
/*     */     
/*     */     static final class Head
/*     */       implements Runnable
/*     */     {
/*     */       private final AtomicInteger availableSharedCapacity;
/*     */       Recycler.WeakOrderQueue.Link link;
/*     */       
/*     */       Head(AtomicInteger availableSharedCapacity) {
/* 256 */         this.availableSharedCapacity = availableSharedCapacity;
/*     */       }
/*     */ 
/*     */       
/*     */       public void run() {
/* 261 */         Recycler.WeakOrderQueue.Link head = this.link;
/* 262 */         while (head != null) {
/* 263 */           reclaimSpace(Recycler.LINK_CAPACITY);
/* 264 */           head = head.next;
/*     */         } 
/*     */       }
/*     */       
/*     */       void reclaimSpace(int space) {
/* 269 */         assert space >= 0;
/* 270 */         this.availableSharedCapacity.addAndGet(space);
/*     */       }
/*     */       
/*     */       boolean reserveSpace(int space) {
/* 274 */         return reserveSpace(this.availableSharedCapacity, space);
/*     */       }
/*     */       
/*     */       static boolean reserveSpace(AtomicInteger availableSharedCapacity, int space) {
/* 278 */         assert space >= 0;
/*     */         while (true) {
/* 280 */           int available = availableSharedCapacity.get();
/* 281 */           if (available < space) {
/* 282 */             return false;
/*     */           }
/* 284 */           if (availableSharedCapacity.compareAndSet(available, available - space)) {
/* 285 */             return true;
/*     */           }
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 297 */     private final int id = Recycler.ID_GENERATOR.getAndIncrement();
/*     */     
/*     */     private WeakOrderQueue() {
/* 300 */       this.owner = null;
/* 301 */       this.head = new Head(null);
/*     */     }
/*     */     
/*     */     private WeakOrderQueue(Recycler.Stack<?> stack, Thread thread) {
/* 305 */       this.tail = new Link();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 310 */       this.head = new Head(stack.availableSharedCapacity);
/* 311 */       this.head.link = this.tail;
/* 312 */       this.owner = new WeakReference<Thread>(thread);
/*     */     }
/*     */     
/*     */     static WeakOrderQueue newQueue(Recycler.Stack<?> stack, Thread thread) {
/* 316 */       WeakOrderQueue queue = new WeakOrderQueue(stack, thread);
/*     */ 
/*     */       
/* 319 */       stack.setHead(queue);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 324 */       Head head = queue.head;
/* 325 */       ObjectCleaner.register(queue, head);
/*     */       
/* 327 */       return queue;
/*     */     }
/*     */     
/*     */     private void setNext(WeakOrderQueue next) {
/* 331 */       assert next != this;
/* 332 */       this.next = next;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static WeakOrderQueue allocate(Recycler.Stack<?> stack, Thread thread) {
/* 340 */       return Head.reserveSpace(stack.availableSharedCapacity, Recycler.LINK_CAPACITY) ? 
/* 341 */         newQueue(stack, thread) : null;
/*     */     }
/*     */     
/*     */     void add(Recycler.DefaultHandle<?> handle) {
/* 345 */       handle.lastRecycledId = this.id;
/*     */       
/* 347 */       Link tail = this.tail;
/*     */       int writeIndex;
/* 349 */       if ((writeIndex = tail.get()) == Recycler.LINK_CAPACITY) {
/* 350 */         if (!this.head.reserveSpace(Recycler.LINK_CAPACITY)) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 355 */         this.tail = tail = tail.next = new Link();
/*     */         
/* 357 */         writeIndex = tail.get();
/*     */       } 
/* 359 */       tail.elements[writeIndex] = handle;
/* 360 */       handle.stack = null;
/*     */ 
/*     */       
/* 363 */       tail.lazySet(writeIndex + 1);
/*     */     }
/*     */     
/*     */     boolean hasFinalData() {
/* 367 */       return (this.tail.readIndex != this.tail.get());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     boolean transfer(Recycler.Stack<?> dst) {
/* 373 */       Link head = this.head.link;
/* 374 */       if (head == null) {
/* 375 */         return false;
/*     */       }
/*     */       
/* 378 */       if (head.readIndex == Recycler.LINK_CAPACITY) {
/* 379 */         if (head.next == null) {
/* 380 */           return false;
/*     */         }
/* 382 */         this.head.link = head = head.next;
/*     */       } 
/*     */       
/* 385 */       int srcStart = head.readIndex;
/* 386 */       int srcEnd = head.get();
/* 387 */       int srcSize = srcEnd - srcStart;
/* 388 */       if (srcSize == 0) {
/* 389 */         return false;
/*     */       }
/*     */       
/* 392 */       int dstSize = dst.size;
/* 393 */       int expectedCapacity = dstSize + srcSize;
/*     */       
/* 395 */       if (expectedCapacity > dst.elements.length) {
/* 396 */         int actualCapacity = dst.increaseCapacity(expectedCapacity);
/* 397 */         srcEnd = Math.min(srcStart + actualCapacity - dstSize, srcEnd);
/*     */       } 
/*     */       
/* 400 */       if (srcStart != srcEnd) {
/* 401 */         Recycler.DefaultHandle[] srcElems = (Recycler.DefaultHandle[])head.elements;
/* 402 */         Recycler.DefaultHandle[] dstElems = (Recycler.DefaultHandle[])dst.elements;
/* 403 */         int newDstSize = dstSize;
/* 404 */         for (int i = srcStart; i < srcEnd; i++) {
/* 405 */           Recycler.DefaultHandle<?> element = srcElems[i];
/* 406 */           if (element.recycleId == 0) {
/* 407 */             element.recycleId = element.lastRecycledId;
/* 408 */           } else if (element.recycleId != element.lastRecycledId) {
/* 409 */             throw new IllegalStateException("recycled already");
/*     */           } 
/* 411 */           srcElems[i] = null;
/*     */           
/* 413 */           if (!dst.dropHandle(element)) {
/*     */ 
/*     */ 
/*     */             
/* 417 */             element.stack = dst;
/* 418 */             dstElems[newDstSize++] = element;
/*     */           } 
/*     */         } 
/* 421 */         if (srcEnd == Recycler.LINK_CAPACITY && head.next != null) {
/*     */           
/* 423 */           this.head.reclaimSpace(Recycler.LINK_CAPACITY);
/* 424 */           this.head.link = head.next;
/*     */         } 
/*     */         
/* 427 */         head.readIndex = srcEnd;
/* 428 */         if (dst.size == newDstSize) {
/* 429 */           return false;
/*     */         }
/* 431 */         dst.size = newDstSize;
/* 432 */         return true;
/*     */       } 
/*     */       
/* 435 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class Stack<T>
/*     */   {
/*     */     final Recycler<T> parent;
/*     */ 
/*     */     
/*     */     final WeakReference<Thread> threadRef;
/*     */ 
/*     */     
/*     */     final AtomicInteger availableSharedCapacity;
/*     */ 
/*     */     
/*     */     final int maxDelayedQueues;
/*     */     
/*     */     private final int maxCapacity;
/*     */     
/*     */     private final int ratioMask;
/*     */     
/*     */     private Recycler.DefaultHandle<?>[] elements;
/*     */     
/*     */     private int size;
/*     */     
/* 462 */     private int handleRecycleCount = -1;
/*     */     private Recycler.WeakOrderQueue cursor;
/*     */     private Recycler.WeakOrderQueue prev;
/*     */     private volatile Recycler.WeakOrderQueue head;
/*     */     
/*     */     Stack(Recycler<T> parent, Thread thread, int maxCapacity, int maxSharedCapacityFactor, int ratioMask, int maxDelayedQueues) {
/* 468 */       this.parent = parent;
/* 469 */       this.threadRef = new WeakReference<Thread>(thread);
/* 470 */       this.maxCapacity = maxCapacity;
/* 471 */       this.availableSharedCapacity = new AtomicInteger(Math.max(maxCapacity / maxSharedCapacityFactor, Recycler.LINK_CAPACITY));
/* 472 */       this.elements = (Recycler.DefaultHandle<?>[])new Recycler.DefaultHandle[Math.min(Recycler.INITIAL_CAPACITY, maxCapacity)];
/* 473 */       this.ratioMask = ratioMask;
/* 474 */       this.maxDelayedQueues = maxDelayedQueues;
/*     */     }
/*     */ 
/*     */     
/*     */     synchronized void setHead(Recycler.WeakOrderQueue queue) {
/* 479 */       queue.setNext(this.head);
/* 480 */       this.head = queue;
/*     */     }
/*     */     
/*     */     int increaseCapacity(int expectedCapacity) {
/* 484 */       int newCapacity = this.elements.length;
/* 485 */       int maxCapacity = this.maxCapacity;
/*     */       do {
/* 487 */         newCapacity <<= 1;
/* 488 */       } while (newCapacity < expectedCapacity && newCapacity < maxCapacity);
/*     */       
/* 490 */       newCapacity = Math.min(newCapacity, maxCapacity);
/* 491 */       if (newCapacity != this.elements.length) {
/* 492 */         this.elements = (Recycler.DefaultHandle<?>[])Arrays.<Recycler.DefaultHandle>copyOf((Recycler.DefaultHandle[])this.elements, newCapacity);
/*     */       }
/*     */       
/* 495 */       return newCapacity;
/*     */     }
/*     */ 
/*     */     
/*     */     Recycler.DefaultHandle<T> pop() {
/* 500 */       int size = this.size;
/* 501 */       if (size == 0) {
/* 502 */         if (!scavenge()) {
/* 503 */           return null;
/*     */         }
/* 505 */         size = this.size;
/*     */       } 
/* 507 */       size--;
/* 508 */       Recycler.DefaultHandle<?> ret = this.elements[size];
/* 509 */       this.elements[size] = null;
/* 510 */       if (ret.lastRecycledId != ret.recycleId) {
/* 511 */         throw new IllegalStateException("recycled multiple times");
/*     */       }
/* 513 */       ret.recycleId = 0;
/* 514 */       ret.lastRecycledId = 0;
/* 515 */       this.size = size;
/* 516 */       return (Recycler.DefaultHandle)ret;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean scavenge() {
/* 521 */       if (scavengeSome()) {
/* 522 */         return true;
/*     */       }
/*     */ 
/*     */       
/* 526 */       this.prev = null;
/* 527 */       this.cursor = this.head;
/* 528 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean scavengeSome() {
/* 533 */       Recycler.WeakOrderQueue prev, cursor = this.cursor;
/* 534 */       if (cursor == null) {
/* 535 */         prev = null;
/* 536 */         cursor = this.head;
/* 537 */         if (cursor == null) {
/* 538 */           return false;
/*     */         }
/*     */       } else {
/* 541 */         prev = this.prev;
/*     */       } 
/*     */       
/* 544 */       boolean success = false;
/*     */       do {
/* 546 */         if (cursor.transfer(this)) {
/* 547 */           success = true;
/*     */           break;
/*     */         } 
/* 550 */         Recycler.WeakOrderQueue next = cursor.next;
/* 551 */         if (cursor.owner.get() == null) {
/*     */ 
/*     */ 
/*     */           
/* 555 */           if (cursor.hasFinalData())
/*     */           {
/* 557 */             while (cursor.transfer(this)) {
/* 558 */               success = true;
/*     */             }
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 565 */           if (prev != null) {
/* 566 */             prev.setNext(next);
/*     */           }
/*     */         } else {
/* 569 */           prev = cursor;
/*     */         } 
/*     */         
/* 572 */         cursor = next;
/*     */       }
/* 574 */       while (cursor != null && !success);
/*     */       
/* 576 */       this.prev = prev;
/* 577 */       this.cursor = cursor;
/* 578 */       return success;
/*     */     }
/*     */     
/*     */     void push(Recycler.DefaultHandle<?> item) {
/* 582 */       Thread currentThread = Thread.currentThread();
/* 583 */       if (this.threadRef.get() == currentThread) {
/*     */         
/* 585 */         pushNow(item);
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 590 */         pushLater(item, currentThread);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void pushNow(Recycler.DefaultHandle<?> item) {
/* 595 */       if ((item.recycleId | item.lastRecycledId) != 0) {
/* 596 */         throw new IllegalStateException("recycled already");
/*     */       }
/* 598 */       item.recycleId = item.lastRecycledId = Recycler.OWN_THREAD_ID;
/*     */       
/* 600 */       int size = this.size;
/* 601 */       if (size >= this.maxCapacity || dropHandle(item)) {
/*     */         return;
/*     */       }
/*     */       
/* 605 */       if (size == this.elements.length) {
/* 606 */         this.elements = (Recycler.DefaultHandle<?>[])Arrays.<Recycler.DefaultHandle>copyOf((Recycler.DefaultHandle[])this.elements, Math.min(size << 1, this.maxCapacity));
/*     */       }
/*     */       
/* 609 */       this.elements[size] = item;
/* 610 */       this.size = size + 1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void pushLater(Recycler.DefaultHandle<?> item, Thread thread) {
/* 617 */       Map<Stack<?>, Recycler.WeakOrderQueue> delayedRecycled = (Map<Stack<?>, Recycler.WeakOrderQueue>)Recycler.DELAYED_RECYCLED.get();
/* 618 */       Recycler.WeakOrderQueue queue = delayedRecycled.get(this);
/* 619 */       if (queue == null) {
/* 620 */         if (delayedRecycled.size() >= this.maxDelayedQueues) {
/*     */           
/* 622 */           delayedRecycled.put(this, Recycler.WeakOrderQueue.DUMMY);
/*     */           
/*     */           return;
/*     */         } 
/* 626 */         if ((queue = Recycler.WeakOrderQueue.allocate(this, thread)) == null) {
/*     */           return;
/*     */         }
/*     */         
/* 630 */         delayedRecycled.put(this, queue);
/* 631 */       } else if (queue == Recycler.WeakOrderQueue.DUMMY) {
/*     */         return;
/*     */       } 
/*     */ 
/*     */       
/* 636 */       queue.add(item);
/*     */     }
/*     */     
/*     */     boolean dropHandle(Recycler.DefaultHandle<?> handle) {
/* 640 */       if (!handle.hasBeenRecycled) {
/* 641 */         if ((++this.handleRecycleCount & this.ratioMask) != 0)
/*     */         {
/* 643 */           return true;
/*     */         }
/* 645 */         handle.hasBeenRecycled = true;
/*     */       } 
/* 647 */       return false;
/*     */     }
/*     */     
/*     */     Recycler.DefaultHandle<T> newHandle() {
/* 651 */       return new Recycler.DefaultHandle<T>(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Handle<T> {
/*     */     void recycle(T param1T);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\Recycler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */