/*      */ package io.netty.buffer;
/*      */ 
/*      */ import io.netty.util.ReferenceCounted;
/*      */ import io.netty.util.internal.EmptyArrays;
/*      */ import io.netty.util.internal.ObjectUtil;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.nio.channels.GatheringByteChannel;
/*      */ import java.nio.channels.ScatteringByteChannel;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.NoSuchElementException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CompositeByteBuf
/*      */   extends AbstractReferenceCountedByteBuf
/*      */   implements Iterable<ByteBuf>
/*      */ {
/*   46 */   private static final ByteBuffer EMPTY_NIO_BUFFER = Unpooled.EMPTY_BUFFER.nioBuffer();
/*   47 */   private static final Iterator<ByteBuf> EMPTY_ITERATOR = Collections.<ByteBuf>emptyList().iterator();
/*      */   
/*      */   private final ByteBufAllocator alloc;
/*      */   
/*      */   private final boolean direct;
/*      */   private final ComponentList components;
/*      */   private final int maxNumComponents;
/*      */   private boolean freed;
/*      */   
/*      */   public CompositeByteBuf(ByteBufAllocator alloc, boolean direct, int maxNumComponents) {
/*   57 */     super(2147483647);
/*   58 */     if (alloc == null) {
/*   59 */       throw new NullPointerException("alloc");
/*      */     }
/*   61 */     this.alloc = alloc;
/*   62 */     this.direct = direct;
/*   63 */     this.maxNumComponents = maxNumComponents;
/*   64 */     this.components = newList(maxNumComponents);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf(ByteBufAllocator alloc, boolean direct, int maxNumComponents, ByteBuf... buffers) {
/*   68 */     this(alloc, direct, maxNumComponents, buffers, 0, buffers.length);
/*      */   }
/*      */ 
/*      */   
/*      */   CompositeByteBuf(ByteBufAllocator alloc, boolean direct, int maxNumComponents, ByteBuf[] buffers, int offset, int len) {
/*   73 */     super(2147483647);
/*   74 */     if (alloc == null) {
/*   75 */       throw new NullPointerException("alloc");
/*      */     }
/*   77 */     if (maxNumComponents < 2) {
/*   78 */       throw new IllegalArgumentException("maxNumComponents: " + maxNumComponents + " (expected: >= 2)");
/*      */     }
/*      */ 
/*      */     
/*   82 */     this.alloc = alloc;
/*   83 */     this.direct = direct;
/*   84 */     this.maxNumComponents = maxNumComponents;
/*   85 */     this.components = newList(maxNumComponents);
/*      */     
/*   87 */     addComponents0(false, 0, buffers, offset, len);
/*   88 */     consolidateIfNeeded();
/*   89 */     setIndex(0, capacity());
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf(ByteBufAllocator alloc, boolean direct, int maxNumComponents, Iterable<ByteBuf> buffers) {
/*   94 */     super(2147483647);
/*   95 */     if (alloc == null) {
/*   96 */       throw new NullPointerException("alloc");
/*      */     }
/*   98 */     if (maxNumComponents < 2) {
/*   99 */       throw new IllegalArgumentException("maxNumComponents: " + maxNumComponents + " (expected: >= 2)");
/*      */     }
/*      */ 
/*      */     
/*  103 */     this.alloc = alloc;
/*  104 */     this.direct = direct;
/*  105 */     this.maxNumComponents = maxNumComponents;
/*  106 */     this.components = newList(maxNumComponents);
/*      */     
/*  108 */     addComponents0(false, 0, buffers);
/*  109 */     consolidateIfNeeded();
/*  110 */     setIndex(0, capacity());
/*      */   }
/*      */   
/*      */   private static ComponentList newList(int maxNumComponents) {
/*  114 */     return new ComponentList(Math.min(16, maxNumComponents));
/*      */   }
/*      */ 
/*      */   
/*      */   CompositeByteBuf(ByteBufAllocator alloc) {
/*  119 */     super(2147483647);
/*  120 */     this.alloc = alloc;
/*  121 */     this.direct = false;
/*  122 */     this.maxNumComponents = 0;
/*  123 */     this.components = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompositeByteBuf addComponent(ByteBuf buffer) {
/*  137 */     return addComponent(false, buffer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompositeByteBuf addComponents(ByteBuf... buffers) {
/*  152 */     return addComponents(false, buffers);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompositeByteBuf addComponents(Iterable<ByteBuf> buffers) {
/*  167 */     return addComponents(false, buffers);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompositeByteBuf addComponent(int cIndex, ByteBuf buffer) {
/*  182 */     return addComponent(false, cIndex, buffer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompositeByteBuf addComponent(boolean increaseWriterIndex, ByteBuf buffer) {
/*  194 */     ObjectUtil.checkNotNull(buffer, "buffer");
/*  195 */     addComponent0(increaseWriterIndex, this.components.size(), buffer);
/*  196 */     consolidateIfNeeded();
/*  197 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompositeByteBuf addComponents(boolean increaseWriterIndex, ByteBuf... buffers) {
/*  210 */     addComponents0(increaseWriterIndex, this.components.size(), buffers, 0, buffers.length);
/*  211 */     consolidateIfNeeded();
/*  212 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompositeByteBuf addComponents(boolean increaseWriterIndex, Iterable<ByteBuf> buffers) {
/*  225 */     addComponents0(increaseWriterIndex, this.components.size(), buffers);
/*  226 */     consolidateIfNeeded();
/*  227 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompositeByteBuf addComponent(boolean increaseWriterIndex, int cIndex, ByteBuf buffer) {
/*  240 */     ObjectUtil.checkNotNull(buffer, "buffer");
/*  241 */     addComponent0(increaseWriterIndex, cIndex, buffer);
/*  242 */     consolidateIfNeeded();
/*  243 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int addComponent0(boolean increaseWriterIndex, int cIndex, ByteBuf buffer) {
/*  250 */     assert buffer != null;
/*  251 */     boolean wasAdded = false;
/*      */     try {
/*  253 */       checkComponentIndex(cIndex);
/*      */       
/*  255 */       int readableBytes = buffer.readableBytes();
/*      */ 
/*      */ 
/*      */       
/*  259 */       Component c = new Component(buffer.order(ByteOrder.BIG_ENDIAN).slice());
/*  260 */       if (cIndex == this.components.size()) {
/*  261 */         wasAdded = this.components.add(c);
/*  262 */         if (cIndex == 0) {
/*  263 */           c.endOffset = readableBytes;
/*      */         } else {
/*  265 */           Component prev = this.components.get(cIndex - 1);
/*  266 */           c.offset = prev.endOffset;
/*  267 */           c.endOffset = c.offset + readableBytes;
/*      */         } 
/*      */       } else {
/*  270 */         this.components.add(cIndex, c);
/*  271 */         wasAdded = true;
/*  272 */         if (readableBytes != 0) {
/*  273 */           updateComponentOffsets(cIndex);
/*      */         }
/*      */       } 
/*  276 */       if (increaseWriterIndex) {
/*  277 */         writerIndex(writerIndex() + buffer.readableBytes());
/*      */       }
/*  279 */       return cIndex;
/*      */     } finally {
/*  281 */       if (!wasAdded) {
/*  282 */         buffer.release();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompositeByteBuf addComponents(int cIndex, ByteBuf... buffers) {
/*  302 */     addComponents0(false, cIndex, buffers, 0, buffers.length);
/*  303 */     consolidateIfNeeded();
/*  304 */     return this;
/*      */   }
/*      */   
/*      */   private int addComponents0(boolean increaseWriterIndex, int cIndex, ByteBuf[] buffers, int offset, int len) {
/*  308 */     ObjectUtil.checkNotNull(buffers, "buffers");
/*  309 */     int i = offset;
/*      */     try {
/*  311 */       checkComponentIndex(cIndex);
/*      */ 
/*      */       
/*  314 */       while (i < len) {
/*      */ 
/*      */         
/*  317 */         ByteBuf b = buffers[i++];
/*  318 */         if (b == null) {
/*      */           break;
/*      */         }
/*  321 */         cIndex = addComponent0(increaseWriterIndex, cIndex, b) + 1;
/*  322 */         int size = this.components.size();
/*  323 */         if (cIndex > size) {
/*  324 */           cIndex = size;
/*      */         }
/*      */       } 
/*  327 */       return cIndex;
/*      */     } finally {
/*  329 */       for (; i < len; i++) {
/*  330 */         ByteBuf b = buffers[i];
/*  331 */         if (b != null) {
/*      */           try {
/*  333 */             b.release();
/*  334 */           } catch (Throwable throwable) {}
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompositeByteBuf addComponents(int cIndex, Iterable<ByteBuf> buffers) {
/*  356 */     addComponents0(false, cIndex, buffers);
/*  357 */     consolidateIfNeeded();
/*  358 */     return this;
/*      */   }
/*      */   
/*      */   private int addComponents0(boolean increaseIndex, int cIndex, Iterable<ByteBuf> buffers) {
/*  362 */     if (buffers instanceof ByteBuf)
/*      */     {
/*  364 */       return addComponent0(increaseIndex, cIndex, (ByteBuf)buffers);
/*      */     }
/*  366 */     ObjectUtil.checkNotNull(buffers, "buffers");
/*      */     
/*  368 */     if (!(buffers instanceof Collection)) {
/*  369 */       List<ByteBuf> list = new ArrayList<ByteBuf>();
/*      */       try {
/*  371 */         for (ByteBuf b : buffers) {
/*  372 */           list.add(b);
/*      */         }
/*  374 */         buffers = list;
/*      */       } finally {
/*  376 */         if (buffers != list) {
/*  377 */           for (ByteBuf b : buffers) {
/*  378 */             if (b != null) {
/*      */               try {
/*  380 */                 b.release();
/*  381 */               } catch (Throwable throwable) {}
/*      */             }
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  390 */     Collection<ByteBuf> col = (Collection<ByteBuf>)buffers;
/*  391 */     return addComponents0(increaseIndex, cIndex, col.<ByteBuf>toArray(new ByteBuf[col.size()]), 0, col.size());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void consolidateIfNeeded() {
/*  401 */     int numComponents = this.components.size();
/*  402 */     if (numComponents > this.maxNumComponents) {
/*  403 */       int capacity = (this.components.get(numComponents - 1)).endOffset;
/*      */       
/*  405 */       ByteBuf consolidated = allocBuffer(capacity);
/*      */ 
/*      */       
/*  408 */       for (int i = 0; i < numComponents; i++) {
/*  409 */         Component component = this.components.get(i);
/*  410 */         ByteBuf b = component.buf;
/*  411 */         consolidated.writeBytes(b);
/*  412 */         component.freeIfNecessary();
/*      */       } 
/*  414 */       Component c = new Component(consolidated);
/*  415 */       c.endOffset = c.length;
/*  416 */       this.components.clear();
/*  417 */       this.components.add(c);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkComponentIndex(int cIndex) {
/*  422 */     ensureAccessible();
/*  423 */     if (cIndex < 0 || cIndex > this.components.size())
/*  424 */       throw new IndexOutOfBoundsException(String.format("cIndex: %d (expected: >= 0 && <= numComponents(%d))", new Object[] {
/*      */               
/*  426 */               Integer.valueOf(cIndex), Integer.valueOf(this.components.size())
/*      */             })); 
/*      */   }
/*      */   
/*      */   private void checkComponentIndex(int cIndex, int numComponents) {
/*  431 */     ensureAccessible();
/*  432 */     if (cIndex < 0 || cIndex + numComponents > this.components.size())
/*  433 */       throw new IndexOutOfBoundsException(String.format("cIndex: %d, numComponents: %d (expected: cIndex >= 0 && cIndex + numComponents <= totalNumComponents(%d))", new Object[] {
/*      */ 
/*      */               
/*  436 */               Integer.valueOf(cIndex), Integer.valueOf(numComponents), Integer.valueOf(this.components.size())
/*      */             })); 
/*      */   }
/*      */   
/*      */   private void updateComponentOffsets(int cIndex) {
/*  441 */     int size = this.components.size();
/*  442 */     if (size <= cIndex) {
/*      */       return;
/*      */     }
/*      */     
/*  446 */     Component c = this.components.get(cIndex);
/*  447 */     if (cIndex == 0) {
/*  448 */       c.offset = 0;
/*  449 */       c.endOffset = c.length;
/*  450 */       cIndex++;
/*      */     } 
/*      */     
/*  453 */     for (int i = cIndex; i < size; i++) {
/*  454 */       Component prev = this.components.get(i - 1);
/*  455 */       Component cur = this.components.get(i);
/*  456 */       cur.offset = prev.endOffset;
/*  457 */       cur.endOffset = cur.offset + cur.length;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompositeByteBuf removeComponent(int cIndex) {
/*  467 */     checkComponentIndex(cIndex);
/*  468 */     Component comp = this.components.remove(cIndex);
/*  469 */     comp.freeIfNecessary();
/*  470 */     if (comp.length > 0)
/*      */     {
/*  472 */       updateComponentOffsets(cIndex);
/*      */     }
/*  474 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompositeByteBuf removeComponents(int cIndex, int numComponents) {
/*  484 */     checkComponentIndex(cIndex, numComponents);
/*      */     
/*  486 */     if (numComponents == 0) {
/*  487 */       return this;
/*      */     }
/*  489 */     int endIndex = cIndex + numComponents;
/*  490 */     boolean needsUpdate = false;
/*  491 */     for (int i = cIndex; i < endIndex; i++) {
/*  492 */       Component c = this.components.get(i);
/*  493 */       if (c.length > 0) {
/*  494 */         needsUpdate = true;
/*      */       }
/*  496 */       c.freeIfNecessary();
/*      */     } 
/*  498 */     this.components.removeRange(cIndex, endIndex);
/*      */     
/*  500 */     if (needsUpdate)
/*      */     {
/*  502 */       updateComponentOffsets(cIndex);
/*      */     }
/*  504 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Iterator<ByteBuf> iterator() {
/*  509 */     ensureAccessible();
/*  510 */     if (this.components.isEmpty()) {
/*  511 */       return EMPTY_ITERATOR;
/*      */     }
/*  513 */     return new CompositeByteBufIterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ByteBuf> decompose(int offset, int length) {
/*  520 */     checkIndex(offset, length);
/*  521 */     if (length == 0) {
/*  522 */       return Collections.emptyList();
/*      */     }
/*      */     
/*  525 */     int componentId = toComponentIndex(offset);
/*  526 */     List<ByteBuf> slice = new ArrayList<ByteBuf>(this.components.size());
/*      */ 
/*      */     
/*  529 */     Component firstC = this.components.get(componentId);
/*  530 */     ByteBuf first = firstC.buf.duplicate();
/*  531 */     first.readerIndex(offset - firstC.offset);
/*      */     
/*  533 */     ByteBuf buf = first;
/*  534 */     int bytesToSlice = length;
/*      */     do {
/*  536 */       int readableBytes = buf.readableBytes();
/*  537 */       if (bytesToSlice <= readableBytes) {
/*      */         
/*  539 */         buf.writerIndex(buf.readerIndex() + bytesToSlice);
/*  540 */         slice.add(buf);
/*      */         
/*      */         break;
/*      */       } 
/*  544 */       slice.add(buf);
/*  545 */       bytesToSlice -= readableBytes;
/*  546 */       componentId++;
/*      */ 
/*      */       
/*  549 */       buf = (this.components.get(componentId)).buf.duplicate();
/*      */     }
/*  551 */     while (bytesToSlice > 0);
/*      */ 
/*      */     
/*  554 */     for (int i = 0; i < slice.size(); i++) {
/*  555 */       slice.set(i, ((ByteBuf)slice.get(i)).slice());
/*      */     }
/*      */     
/*  558 */     return slice;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDirect() {
/*  563 */     int size = this.components.size();
/*  564 */     if (size == 0) {
/*  565 */       return false;
/*      */     }
/*  567 */     for (int i = 0; i < size; i++) {
/*  568 */       if (!(this.components.get(i)).buf.isDirect()) {
/*  569 */         return false;
/*      */       }
/*      */     } 
/*  572 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasArray() {
/*  577 */     switch (this.components.size()) {
/*      */       case 0:
/*  579 */         return true;
/*      */       case 1:
/*  581 */         return (this.components.get(0)).buf.hasArray();
/*      */     } 
/*  583 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] array() {
/*  589 */     switch (this.components.size()) {
/*      */       case 0:
/*  591 */         return EmptyArrays.EMPTY_BYTES;
/*      */       case 1:
/*  593 */         return (this.components.get(0)).buf.array();
/*      */     } 
/*  595 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int arrayOffset() {
/*  601 */     switch (this.components.size()) {
/*      */       case 0:
/*  603 */         return 0;
/*      */       case 1:
/*  605 */         return (this.components.get(0)).buf.arrayOffset();
/*      */     } 
/*  607 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasMemoryAddress() {
/*  613 */     switch (this.components.size()) {
/*      */       case 0:
/*  615 */         return Unpooled.EMPTY_BUFFER.hasMemoryAddress();
/*      */       case 1:
/*  617 */         return (this.components.get(0)).buf.hasMemoryAddress();
/*      */     } 
/*  619 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long memoryAddress() {
/*  625 */     switch (this.components.size()) {
/*      */       case 0:
/*  627 */         return Unpooled.EMPTY_BUFFER.memoryAddress();
/*      */       case 1:
/*  629 */         return (this.components.get(0)).buf.memoryAddress();
/*      */     } 
/*  631 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int capacity() {
/*  637 */     int numComponents = this.components.size();
/*  638 */     if (numComponents == 0) {
/*  639 */       return 0;
/*      */     }
/*  641 */     return (this.components.get(numComponents - 1)).endOffset;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf capacity(int newCapacity) {
/*  646 */     checkNewCapacity(newCapacity);
/*      */     
/*  648 */     int oldCapacity = capacity();
/*  649 */     if (newCapacity > oldCapacity) {
/*  650 */       int paddingLength = newCapacity - oldCapacity;
/*      */       
/*  652 */       int nComponents = this.components.size();
/*  653 */       if (nComponents < this.maxNumComponents) {
/*  654 */         ByteBuf padding = allocBuffer(paddingLength);
/*  655 */         padding.setIndex(0, paddingLength);
/*  656 */         addComponent0(false, this.components.size(), padding);
/*      */       } else {
/*  658 */         ByteBuf padding = allocBuffer(paddingLength);
/*  659 */         padding.setIndex(0, paddingLength);
/*      */ 
/*      */         
/*  662 */         addComponent0(false, this.components.size(), padding);
/*  663 */         consolidateIfNeeded();
/*      */       } 
/*  665 */     } else if (newCapacity < oldCapacity) {
/*  666 */       int bytesToTrim = oldCapacity - newCapacity;
/*  667 */       for (ListIterator<Component> i = this.components.listIterator(this.components.size()); i.hasPrevious(); ) {
/*  668 */         Component c = i.previous();
/*  669 */         if (bytesToTrim >= c.length) {
/*  670 */           bytesToTrim -= c.length;
/*  671 */           i.remove();
/*      */           
/*      */           continue;
/*      */         } 
/*      */         
/*  676 */         Component newC = new Component(c.buf.slice(0, c.length - bytesToTrim));
/*  677 */         newC.offset = c.offset;
/*  678 */         newC.endOffset = newC.offset + newC.length;
/*  679 */         i.set(newC);
/*      */       } 
/*      */ 
/*      */       
/*  683 */       if (readerIndex() > newCapacity) {
/*  684 */         setIndex(newCapacity, newCapacity);
/*  685 */       } else if (writerIndex() > newCapacity) {
/*  686 */         writerIndex(newCapacity);
/*      */       } 
/*      */     } 
/*  689 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBufAllocator alloc() {
/*  694 */     return this.alloc;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteOrder order() {
/*  699 */     return ByteOrder.BIG_ENDIAN;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int numComponents() {
/*  706 */     return this.components.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int maxNumComponents() {
/*  713 */     return this.maxNumComponents;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int toComponentIndex(int offset) {
/*  720 */     checkIndex(offset);
/*      */     
/*  722 */     for (int low = 0, high = this.components.size(); low <= high; ) {
/*  723 */       int mid = low + high >>> 1;
/*  724 */       Component c = this.components.get(mid);
/*  725 */       if (offset >= c.endOffset) {
/*  726 */         low = mid + 1; continue;
/*  727 */       }  if (offset < c.offset) {
/*  728 */         high = mid - 1; continue;
/*      */       } 
/*  730 */       return mid;
/*      */     } 
/*      */ 
/*      */     
/*  734 */     throw new Error("should not reach here");
/*      */   }
/*      */   
/*      */   public int toByteIndex(int cIndex) {
/*  738 */     checkComponentIndex(cIndex);
/*  739 */     return (this.components.get(cIndex)).offset;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getByte(int index) {
/*  744 */     return _getByte(index);
/*      */   }
/*      */ 
/*      */   
/*      */   protected byte _getByte(int index) {
/*  749 */     Component c = findComponent(index);
/*  750 */     return c.buf.getByte(index - c.offset);
/*      */   }
/*      */ 
/*      */   
/*      */   protected short _getShort(int index) {
/*  755 */     Component c = findComponent(index);
/*  756 */     if (index + 2 <= c.endOffset)
/*  757 */       return c.buf.getShort(index - c.offset); 
/*  758 */     if (order() == ByteOrder.BIG_ENDIAN) {
/*  759 */       return (short)((_getByte(index) & 0xFF) << 8 | _getByte(index + 1) & 0xFF);
/*      */     }
/*  761 */     return (short)(_getByte(index) & 0xFF | (_getByte(index + 1) & 0xFF) << 8);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected short _getShortLE(int index) {
/*  767 */     Component c = findComponent(index);
/*  768 */     if (index + 2 <= c.endOffset)
/*  769 */       return c.buf.getShortLE(index - c.offset); 
/*  770 */     if (order() == ByteOrder.BIG_ENDIAN) {
/*  771 */       return (short)(_getByte(index) & 0xFF | (_getByte(index + 1) & 0xFF) << 8);
/*      */     }
/*  773 */     return (short)((_getByte(index) & 0xFF) << 8 | _getByte(index + 1) & 0xFF);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected int _getUnsignedMedium(int index) {
/*  779 */     Component c = findComponent(index);
/*  780 */     if (index + 3 <= c.endOffset)
/*  781 */       return c.buf.getUnsignedMedium(index - c.offset); 
/*  782 */     if (order() == ByteOrder.BIG_ENDIAN) {
/*  783 */       return (_getShort(index) & 0xFFFF) << 8 | _getByte(index + 2) & 0xFF;
/*      */     }
/*  785 */     return _getShort(index) & 0xFFFF | (_getByte(index + 2) & 0xFF) << 16;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected int _getUnsignedMediumLE(int index) {
/*  791 */     Component c = findComponent(index);
/*  792 */     if (index + 3 <= c.endOffset)
/*  793 */       return c.buf.getUnsignedMediumLE(index - c.offset); 
/*  794 */     if (order() == ByteOrder.BIG_ENDIAN) {
/*  795 */       return _getShortLE(index) & 0xFFFF | (_getByte(index + 2) & 0xFF) << 16;
/*      */     }
/*  797 */     return (_getShortLE(index) & 0xFFFF) << 8 | _getByte(index + 2) & 0xFF;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected int _getInt(int index) {
/*  803 */     Component c = findComponent(index);
/*  804 */     if (index + 4 <= c.endOffset)
/*  805 */       return c.buf.getInt(index - c.offset); 
/*  806 */     if (order() == ByteOrder.BIG_ENDIAN) {
/*  807 */       return (_getShort(index) & 0xFFFF) << 16 | _getShort(index + 2) & 0xFFFF;
/*      */     }
/*  809 */     return _getShort(index) & 0xFFFF | (_getShort(index + 2) & 0xFFFF) << 16;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected int _getIntLE(int index) {
/*  815 */     Component c = findComponent(index);
/*  816 */     if (index + 4 <= c.endOffset)
/*  817 */       return c.buf.getIntLE(index - c.offset); 
/*  818 */     if (order() == ByteOrder.BIG_ENDIAN) {
/*  819 */       return _getShortLE(index) & 0xFFFF | (_getShortLE(index + 2) & 0xFFFF) << 16;
/*      */     }
/*  821 */     return (_getShortLE(index) & 0xFFFF) << 16 | _getShortLE(index + 2) & 0xFFFF;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected long _getLong(int index) {
/*  827 */     Component c = findComponent(index);
/*  828 */     if (index + 8 <= c.endOffset)
/*  829 */       return c.buf.getLong(index - c.offset); 
/*  830 */     if (order() == ByteOrder.BIG_ENDIAN) {
/*  831 */       return (_getInt(index) & 0xFFFFFFFFL) << 32L | _getInt(index + 4) & 0xFFFFFFFFL;
/*      */     }
/*  833 */     return _getInt(index) & 0xFFFFFFFFL | (_getInt(index + 4) & 0xFFFFFFFFL) << 32L;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected long _getLongLE(int index) {
/*  839 */     Component c = findComponent(index);
/*  840 */     if (index + 8 <= c.endOffset)
/*  841 */       return c.buf.getLongLE(index - c.offset); 
/*  842 */     if (order() == ByteOrder.BIG_ENDIAN) {
/*  843 */       return _getIntLE(index) & 0xFFFFFFFFL | (_getIntLE(index + 4) & 0xFFFFFFFFL) << 32L;
/*      */     }
/*  845 */     return (_getIntLE(index) & 0xFFFFFFFFL) << 32L | _getIntLE(index + 4) & 0xFFFFFFFFL;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
/*  851 */     checkDstIndex(index, length, dstIndex, dst.length);
/*  852 */     if (length == 0) {
/*  853 */       return this;
/*      */     }
/*      */     
/*  856 */     int i = toComponentIndex(index);
/*  857 */     while (length > 0) {
/*  858 */       Component c = this.components.get(i);
/*  859 */       ByteBuf s = c.buf;
/*  860 */       int adjustment = c.offset;
/*  861 */       int localLength = Math.min(length, s.capacity() - index - adjustment);
/*  862 */       s.getBytes(index - adjustment, dst, dstIndex, localLength);
/*  863 */       index += localLength;
/*  864 */       dstIndex += localLength;
/*  865 */       length -= localLength;
/*  866 */       i++;
/*      */     } 
/*  868 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, ByteBuffer dst) {
/*  873 */     int limit = dst.limit();
/*  874 */     int length = dst.remaining();
/*      */     
/*  876 */     checkIndex(index, length);
/*  877 */     if (length == 0) {
/*  878 */       return this;
/*      */     }
/*      */     
/*  881 */     int i = toComponentIndex(index);
/*      */     try {
/*  883 */       while (length > 0) {
/*  884 */         Component c = this.components.get(i);
/*  885 */         ByteBuf s = c.buf;
/*  886 */         int adjustment = c.offset;
/*  887 */         int localLength = Math.min(length, s.capacity() - index - adjustment);
/*  888 */         dst.limit(dst.position() + localLength);
/*  889 */         s.getBytes(index - adjustment, dst);
/*  890 */         index += localLength;
/*  891 */         length -= localLength;
/*  892 */         i++;
/*      */       } 
/*      */     } finally {
/*  895 */       dst.limit(limit);
/*      */     } 
/*  897 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
/*  902 */     checkDstIndex(index, length, dstIndex, dst.capacity());
/*  903 */     if (length == 0) {
/*  904 */       return this;
/*      */     }
/*      */     
/*  907 */     int i = toComponentIndex(index);
/*  908 */     while (length > 0) {
/*  909 */       Component c = this.components.get(i);
/*  910 */       ByteBuf s = c.buf;
/*  911 */       int adjustment = c.offset;
/*  912 */       int localLength = Math.min(length, s.capacity() - index - adjustment);
/*  913 */       s.getBytes(index - adjustment, dst, dstIndex, localLength);
/*  914 */       index += localLength;
/*  915 */       dstIndex += localLength;
/*  916 */       length -= localLength;
/*  917 */       i++;
/*      */     } 
/*  919 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
/*  925 */     int count = nioBufferCount();
/*  926 */     if (count == 1) {
/*  927 */       return out.write(internalNioBuffer(index, length));
/*      */     }
/*  929 */     long writtenBytes = out.write(nioBuffers(index, length));
/*  930 */     if (writtenBytes > 2147483647L) {
/*  931 */       return Integer.MAX_VALUE;
/*      */     }
/*  933 */     return (int)writtenBytes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
/*  941 */     int count = nioBufferCount();
/*  942 */     if (count == 1) {
/*  943 */       return out.write(internalNioBuffer(index, length), position);
/*      */     }
/*  945 */     long writtenBytes = 0L;
/*  946 */     for (ByteBuffer buf : nioBuffers(index, length)) {
/*  947 */       writtenBytes += out.write(buf, position + writtenBytes);
/*      */     }
/*  949 */     if (writtenBytes > 2147483647L) {
/*  950 */       return Integer.MAX_VALUE;
/*      */     }
/*  952 */     return (int)writtenBytes;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
/*  958 */     checkIndex(index, length);
/*  959 */     if (length == 0) {
/*  960 */       return this;
/*      */     }
/*      */     
/*  963 */     int i = toComponentIndex(index);
/*  964 */     while (length > 0) {
/*  965 */       Component c = this.components.get(i);
/*  966 */       ByteBuf s = c.buf;
/*  967 */       int adjustment = c.offset;
/*  968 */       int localLength = Math.min(length, s.capacity() - index - adjustment);
/*  969 */       s.getBytes(index - adjustment, out, localLength);
/*  970 */       index += localLength;
/*  971 */       length -= localLength;
/*  972 */       i++;
/*      */     } 
/*  974 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setByte(int index, int value) {
/*  979 */     Component c = findComponent(index);
/*  980 */     c.buf.setByte(index - c.offset, value);
/*  981 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void _setByte(int index, int value) {
/*  986 */     setByte(index, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setShort(int index, int value) {
/*  991 */     return (CompositeByteBuf)super.setShort(index, value);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void _setShort(int index, int value) {
/*  996 */     Component c = findComponent(index);
/*  997 */     if (index + 2 <= c.endOffset) {
/*  998 */       c.buf.setShort(index - c.offset, value);
/*  999 */     } else if (order() == ByteOrder.BIG_ENDIAN) {
/* 1000 */       _setByte(index, (byte)(value >>> 8));
/* 1001 */       _setByte(index + 1, (byte)value);
/*      */     } else {
/* 1003 */       _setByte(index, (byte)value);
/* 1004 */       _setByte(index + 1, (byte)(value >>> 8));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void _setShortLE(int index, int value) {
/* 1010 */     Component c = findComponent(index);
/* 1011 */     if (index + 2 <= c.endOffset) {
/* 1012 */       c.buf.setShortLE(index - c.offset, value);
/* 1013 */     } else if (order() == ByteOrder.BIG_ENDIAN) {
/* 1014 */       _setByte(index, (byte)value);
/* 1015 */       _setByte(index + 1, (byte)(value >>> 8));
/*      */     } else {
/* 1017 */       _setByte(index, (byte)(value >>> 8));
/* 1018 */       _setByte(index + 1, (byte)value);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setMedium(int index, int value) {
/* 1024 */     return (CompositeByteBuf)super.setMedium(index, value);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void _setMedium(int index, int value) {
/* 1029 */     Component c = findComponent(index);
/* 1030 */     if (index + 3 <= c.endOffset) {
/* 1031 */       c.buf.setMedium(index - c.offset, value);
/* 1032 */     } else if (order() == ByteOrder.BIG_ENDIAN) {
/* 1033 */       _setShort(index, (short)(value >> 8));
/* 1034 */       _setByte(index + 2, (byte)value);
/*      */     } else {
/* 1036 */       _setShort(index, (short)value);
/* 1037 */       _setByte(index + 2, (byte)(value >>> 16));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void _setMediumLE(int index, int value) {
/* 1043 */     Component c = findComponent(index);
/* 1044 */     if (index + 3 <= c.endOffset) {
/* 1045 */       c.buf.setMediumLE(index - c.offset, value);
/* 1046 */     } else if (order() == ByteOrder.BIG_ENDIAN) {
/* 1047 */       _setShortLE(index, (short)value);
/* 1048 */       _setByte(index + 2, (byte)(value >>> 16));
/*      */     } else {
/* 1050 */       _setShortLE(index, (short)(value >> 8));
/* 1051 */       _setByte(index + 2, (byte)value);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setInt(int index, int value) {
/* 1057 */     return (CompositeByteBuf)super.setInt(index, value);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void _setInt(int index, int value) {
/* 1062 */     Component c = findComponent(index);
/* 1063 */     if (index + 4 <= c.endOffset) {
/* 1064 */       c.buf.setInt(index - c.offset, value);
/* 1065 */     } else if (order() == ByteOrder.BIG_ENDIAN) {
/* 1066 */       _setShort(index, (short)(value >>> 16));
/* 1067 */       _setShort(index + 2, (short)value);
/*      */     } else {
/* 1069 */       _setShort(index, (short)value);
/* 1070 */       _setShort(index + 2, (short)(value >>> 16));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void _setIntLE(int index, int value) {
/* 1076 */     Component c = findComponent(index);
/* 1077 */     if (index + 4 <= c.endOffset) {
/* 1078 */       c.buf.setIntLE(index - c.offset, value);
/* 1079 */     } else if (order() == ByteOrder.BIG_ENDIAN) {
/* 1080 */       _setShortLE(index, (short)value);
/* 1081 */       _setShortLE(index + 2, (short)(value >>> 16));
/*      */     } else {
/* 1083 */       _setShortLE(index, (short)(value >>> 16));
/* 1084 */       _setShortLE(index + 2, (short)value);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setLong(int index, long value) {
/* 1090 */     return (CompositeByteBuf)super.setLong(index, value);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void _setLong(int index, long value) {
/* 1095 */     Component c = findComponent(index);
/* 1096 */     if (index + 8 <= c.endOffset) {
/* 1097 */       c.buf.setLong(index - c.offset, value);
/* 1098 */     } else if (order() == ByteOrder.BIG_ENDIAN) {
/* 1099 */       _setInt(index, (int)(value >>> 32L));
/* 1100 */       _setInt(index + 4, (int)value);
/*      */     } else {
/* 1102 */       _setInt(index, (int)value);
/* 1103 */       _setInt(index + 4, (int)(value >>> 32L));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void _setLongLE(int index, long value) {
/* 1109 */     Component c = findComponent(index);
/* 1110 */     if (index + 8 <= c.endOffset) {
/* 1111 */       c.buf.setLongLE(index - c.offset, value);
/* 1112 */     } else if (order() == ByteOrder.BIG_ENDIAN) {
/* 1113 */       _setIntLE(index, (int)value);
/* 1114 */       _setIntLE(index + 4, (int)(value >>> 32L));
/*      */     } else {
/* 1116 */       _setIntLE(index, (int)(value >>> 32L));
/* 1117 */       _setIntLE(index + 4, (int)value);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
/* 1123 */     checkSrcIndex(index, length, srcIndex, src.length);
/* 1124 */     if (length == 0) {
/* 1125 */       return this;
/*      */     }
/*      */     
/* 1128 */     int i = toComponentIndex(index);
/* 1129 */     while (length > 0) {
/* 1130 */       Component c = this.components.get(i);
/* 1131 */       ByteBuf s = c.buf;
/* 1132 */       int adjustment = c.offset;
/* 1133 */       int localLength = Math.min(length, s.capacity() - index - adjustment);
/* 1134 */       s.setBytes(index - adjustment, src, srcIndex, localLength);
/* 1135 */       index += localLength;
/* 1136 */       srcIndex += localLength;
/* 1137 */       length -= localLength;
/* 1138 */       i++;
/*      */     } 
/* 1140 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, ByteBuffer src) {
/* 1145 */     int limit = src.limit();
/* 1146 */     int length = src.remaining();
/*      */     
/* 1148 */     checkIndex(index, length);
/* 1149 */     if (length == 0) {
/* 1150 */       return this;
/*      */     }
/*      */     
/* 1153 */     int i = toComponentIndex(index);
/*      */     try {
/* 1155 */       while (length > 0) {
/* 1156 */         Component c = this.components.get(i);
/* 1157 */         ByteBuf s = c.buf;
/* 1158 */         int adjustment = c.offset;
/* 1159 */         int localLength = Math.min(length, s.capacity() - index - adjustment);
/* 1160 */         src.limit(src.position() + localLength);
/* 1161 */         s.setBytes(index - adjustment, src);
/* 1162 */         index += localLength;
/* 1163 */         length -= localLength;
/* 1164 */         i++;
/*      */       } 
/*      */     } finally {
/* 1167 */       src.limit(limit);
/*      */     } 
/* 1169 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
/* 1174 */     checkSrcIndex(index, length, srcIndex, src.capacity());
/* 1175 */     if (length == 0) {
/* 1176 */       return this;
/*      */     }
/*      */     
/* 1179 */     int i = toComponentIndex(index);
/* 1180 */     while (length > 0) {
/* 1181 */       Component c = this.components.get(i);
/* 1182 */       ByteBuf s = c.buf;
/* 1183 */       int adjustment = c.offset;
/* 1184 */       int localLength = Math.min(length, s.capacity() - index - adjustment);
/* 1185 */       s.setBytes(index - adjustment, src, srcIndex, localLength);
/* 1186 */       index += localLength;
/* 1187 */       srcIndex += localLength;
/* 1188 */       length -= localLength;
/* 1189 */       i++;
/*      */     } 
/* 1191 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int index, InputStream in, int length) throws IOException {
/* 1196 */     checkIndex(index, length);
/* 1197 */     if (length == 0) {
/* 1198 */       return in.read(EmptyArrays.EMPTY_BYTES);
/*      */     }
/*      */     
/* 1201 */     int i = toComponentIndex(index);
/* 1202 */     int readBytes = 0;
/*      */     
/*      */     do {
/* 1205 */       Component c = this.components.get(i);
/* 1206 */       ByteBuf s = c.buf;
/* 1207 */       int adjustment = c.offset;
/* 1208 */       int localLength = Math.min(length, s.capacity() - index - adjustment);
/* 1209 */       if (localLength == 0) {
/*      */         
/* 1211 */         i++;
/*      */       } else {
/*      */         
/* 1214 */         int localReadBytes = s.setBytes(index - adjustment, in, localLength);
/* 1215 */         if (localReadBytes < 0) {
/* 1216 */           if (readBytes == 0) {
/* 1217 */             return -1;
/*      */           }
/*      */           
/*      */           break;
/*      */         } 
/*      */         
/* 1223 */         if (localReadBytes == localLength)
/* 1224 */         { index += localLength;
/* 1225 */           length -= localLength;
/* 1226 */           readBytes += localLength;
/* 1227 */           i++; }
/*      */         else
/* 1229 */         { index += localReadBytes;
/* 1230 */           length -= localReadBytes;
/* 1231 */           readBytes += localReadBytes; } 
/*      */       } 
/* 1233 */     } while (length > 0);
/*      */     
/* 1235 */     return readBytes;
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
/* 1240 */     checkIndex(index, length);
/* 1241 */     if (length == 0) {
/* 1242 */       return in.read(EMPTY_NIO_BUFFER);
/*      */     }
/*      */     
/* 1245 */     int i = toComponentIndex(index);
/* 1246 */     int readBytes = 0;
/*      */     do {
/* 1248 */       Component c = this.components.get(i);
/* 1249 */       ByteBuf s = c.buf;
/* 1250 */       int adjustment = c.offset;
/* 1251 */       int localLength = Math.min(length, s.capacity() - index - adjustment);
/* 1252 */       if (localLength == 0) {
/*      */         
/* 1254 */         i++;
/*      */       } else {
/*      */         
/* 1257 */         int localReadBytes = s.setBytes(index - adjustment, in, localLength);
/*      */         
/* 1259 */         if (localReadBytes == 0) {
/*      */           break;
/*      */         }
/*      */         
/* 1263 */         if (localReadBytes < 0) {
/* 1264 */           if (readBytes == 0) {
/* 1265 */             return -1;
/*      */           }
/*      */           
/*      */           break;
/*      */         } 
/*      */         
/* 1271 */         if (localReadBytes == localLength)
/* 1272 */         { index += localLength;
/* 1273 */           length -= localLength;
/* 1274 */           readBytes += localLength;
/* 1275 */           i++; }
/*      */         else
/* 1277 */         { index += localReadBytes;
/* 1278 */           length -= localReadBytes;
/* 1279 */           readBytes += localReadBytes; } 
/*      */       } 
/* 1281 */     } while (length > 0);
/*      */     
/* 1283 */     return readBytes;
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
/* 1288 */     checkIndex(index, length);
/* 1289 */     if (length == 0) {
/* 1290 */       return in.read(EMPTY_NIO_BUFFER, position);
/*      */     }
/*      */     
/* 1293 */     int i = toComponentIndex(index);
/* 1294 */     int readBytes = 0;
/*      */     do {
/* 1296 */       Component c = this.components.get(i);
/* 1297 */       ByteBuf s = c.buf;
/* 1298 */       int adjustment = c.offset;
/* 1299 */       int localLength = Math.min(length, s.capacity() - index - adjustment);
/* 1300 */       if (localLength == 0) {
/*      */         
/* 1302 */         i++;
/*      */       } else {
/*      */         
/* 1305 */         int localReadBytes = s.setBytes(index - adjustment, in, position + readBytes, localLength);
/*      */         
/* 1307 */         if (localReadBytes == 0) {
/*      */           break;
/*      */         }
/*      */         
/* 1311 */         if (localReadBytes < 0) {
/* 1312 */           if (readBytes == 0) {
/* 1313 */             return -1;
/*      */           }
/*      */           
/*      */           break;
/*      */         } 
/*      */         
/* 1319 */         if (localReadBytes == localLength)
/* 1320 */         { index += localLength;
/* 1321 */           length -= localLength;
/* 1322 */           readBytes += localLength;
/* 1323 */           i++; }
/*      */         else
/* 1325 */         { index += localReadBytes;
/* 1326 */           length -= localReadBytes;
/* 1327 */           readBytes += localReadBytes; } 
/*      */       } 
/* 1329 */     } while (length > 0);
/*      */     
/* 1331 */     return readBytes;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf copy(int index, int length) {
/* 1336 */     checkIndex(index, length);
/* 1337 */     ByteBuf dst = allocBuffer(length);
/* 1338 */     if (length != 0) {
/* 1339 */       copyTo(index, length, toComponentIndex(index), dst);
/*      */     }
/* 1341 */     return dst;
/*      */   }
/*      */   
/*      */   private void copyTo(int index, int length, int componentId, ByteBuf dst) {
/* 1345 */     int dstIndex = 0;
/* 1346 */     int i = componentId;
/*      */     
/* 1348 */     while (length > 0) {
/* 1349 */       Component c = this.components.get(i);
/* 1350 */       ByteBuf s = c.buf;
/* 1351 */       int adjustment = c.offset;
/* 1352 */       int localLength = Math.min(length, s.capacity() - index - adjustment);
/* 1353 */       s.getBytes(index - adjustment, dst, dstIndex, localLength);
/* 1354 */       index += localLength;
/* 1355 */       dstIndex += localLength;
/* 1356 */       length -= localLength;
/* 1357 */       i++;
/*      */     } 
/*      */     
/* 1360 */     dst.writerIndex(dst.capacity());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteBuf component(int cIndex) {
/* 1370 */     return internalComponent(cIndex).duplicate();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteBuf componentAtOffset(int offset) {
/* 1380 */     return internalComponentAtOffset(offset).duplicate();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteBuf internalComponent(int cIndex) {
/* 1390 */     checkComponentIndex(cIndex);
/* 1391 */     return (this.components.get(cIndex)).buf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteBuf internalComponentAtOffset(int offset) {
/* 1401 */     return (findComponent(offset)).buf;
/*      */   }
/*      */   
/*      */   private Component findComponent(int offset) {
/* 1405 */     checkIndex(offset);
/*      */     
/* 1407 */     for (int low = 0, high = this.components.size(); low <= high; ) {
/* 1408 */       int mid = low + high >>> 1;
/* 1409 */       Component c = this.components.get(mid);
/* 1410 */       if (offset >= c.endOffset) {
/* 1411 */         low = mid + 1; continue;
/* 1412 */       }  if (offset < c.offset) {
/* 1413 */         high = mid - 1; continue;
/*      */       } 
/* 1415 */       assert c.length != 0;
/* 1416 */       return c;
/*      */     } 
/*      */ 
/*      */     
/* 1420 */     throw new Error("should not reach here");
/*      */   }
/*      */ 
/*      */   
/*      */   public int nioBufferCount() {
/* 1425 */     switch (this.components.size()) {
/*      */       case 0:
/* 1427 */         return 1;
/*      */       case 1:
/* 1429 */         return (this.components.get(0)).buf.nioBufferCount();
/*      */     } 
/* 1431 */     int count = 0;
/* 1432 */     int componentsCount = this.components.size();
/* 1433 */     for (int i = 0; i < componentsCount; i++) {
/* 1434 */       Component c = this.components.get(i);
/* 1435 */       count += c.buf.nioBufferCount();
/*      */     } 
/* 1437 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteBuffer internalNioBuffer(int index, int length) {
/* 1443 */     switch (this.components.size()) {
/*      */       case 0:
/* 1445 */         return EMPTY_NIO_BUFFER;
/*      */       case 1:
/* 1447 */         return (this.components.get(0)).buf.internalNioBuffer(index, length);
/*      */     } 
/* 1449 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer nioBuffer(int index, int length) {
/*      */     ByteBuf buf;
/* 1455 */     checkIndex(index, length);
/*      */     
/* 1457 */     switch (this.components.size()) {
/*      */       case 0:
/* 1459 */         return EMPTY_NIO_BUFFER;
/*      */       case 1:
/* 1461 */         buf = (this.components.get(0)).buf;
/* 1462 */         if (buf.nioBufferCount() == 1) {
/* 1463 */           return (this.components.get(0)).buf.nioBuffer(index, length);
/*      */         }
/*      */         break;
/*      */     } 
/* 1467 */     ByteBuffer merged = ByteBuffer.allocate(length).order(order());
/* 1468 */     ByteBuffer[] buffers = nioBuffers(index, length);
/*      */     
/* 1470 */     for (ByteBuffer byteBuffer : buffers) {
/* 1471 */       merged.put(byteBuffer);
/*      */     }
/*      */     
/* 1474 */     merged.flip();
/* 1475 */     return merged;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer[] nioBuffers(int index, int length) {
/* 1480 */     checkIndex(index, length);
/* 1481 */     if (length == 0) {
/* 1482 */       return new ByteBuffer[] { EMPTY_NIO_BUFFER };
/*      */     }
/*      */     
/* 1485 */     List<ByteBuffer> buffers = new ArrayList<ByteBuffer>(this.components.size());
/* 1486 */     int i = toComponentIndex(index);
/* 1487 */     while (length > 0) {
/* 1488 */       Component c = this.components.get(i);
/* 1489 */       ByteBuf s = c.buf;
/* 1490 */       int adjustment = c.offset;
/* 1491 */       int localLength = Math.min(length, s.capacity() - index - adjustment);
/* 1492 */       switch (s.nioBufferCount()) {
/*      */         case 0:
/* 1494 */           throw new UnsupportedOperationException();
/*      */         case 1:
/* 1496 */           buffers.add(s.nioBuffer(index - adjustment, localLength));
/*      */           break;
/*      */         default:
/* 1499 */           Collections.addAll(buffers, s.nioBuffers(index - adjustment, localLength));
/*      */           break;
/*      */       } 
/* 1502 */       index += localLength;
/* 1503 */       length -= localLength;
/* 1504 */       i++;
/*      */     } 
/*      */     
/* 1507 */     return buffers.<ByteBuffer>toArray(new ByteBuffer[buffers.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompositeByteBuf consolidate() {
/* 1514 */     ensureAccessible();
/* 1515 */     int numComponents = numComponents();
/* 1516 */     if (numComponents <= 1) {
/* 1517 */       return this;
/*      */     }
/*      */     
/* 1520 */     Component last = this.components.get(numComponents - 1);
/* 1521 */     int capacity = last.endOffset;
/* 1522 */     ByteBuf consolidated = allocBuffer(capacity);
/*      */     
/* 1524 */     for (int i = 0; i < numComponents; i++) {
/* 1525 */       Component c = this.components.get(i);
/* 1526 */       ByteBuf b = c.buf;
/* 1527 */       consolidated.writeBytes(b);
/* 1528 */       c.freeIfNecessary();
/*      */     } 
/*      */     
/* 1531 */     this.components.clear();
/* 1532 */     this.components.add(new Component(consolidated));
/* 1533 */     updateComponentOffsets(0);
/* 1534 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompositeByteBuf consolidate(int cIndex, int numComponents) {
/* 1544 */     checkComponentIndex(cIndex, numComponents);
/* 1545 */     if (numComponents <= 1) {
/* 1546 */       return this;
/*      */     }
/*      */     
/* 1549 */     int endCIndex = cIndex + numComponents;
/* 1550 */     Component last = this.components.get(endCIndex - 1);
/* 1551 */     int capacity = last.endOffset - (this.components.get(cIndex)).offset;
/* 1552 */     ByteBuf consolidated = allocBuffer(capacity);
/*      */     
/* 1554 */     for (int i = cIndex; i < endCIndex; i++) {
/* 1555 */       Component c = this.components.get(i);
/* 1556 */       ByteBuf b = c.buf;
/* 1557 */       consolidated.writeBytes(b);
/* 1558 */       c.freeIfNecessary();
/*      */     } 
/*      */     
/* 1561 */     this.components.removeRange(cIndex + 1, endCIndex);
/* 1562 */     this.components.set(cIndex, new Component(consolidated));
/* 1563 */     updateComponentOffsets(cIndex);
/* 1564 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompositeByteBuf discardReadComponents() {
/* 1571 */     ensureAccessible();
/* 1572 */     int readerIndex = readerIndex();
/* 1573 */     if (readerIndex == 0) {
/* 1574 */       return this;
/*      */     }
/*      */ 
/*      */     
/* 1578 */     int writerIndex = writerIndex();
/* 1579 */     if (readerIndex == writerIndex && writerIndex == capacity()) {
/* 1580 */       int size = this.components.size();
/* 1581 */       for (int j = 0; j < size; j++) {
/* 1582 */         this.components.get(j).freeIfNecessary();
/*      */       }
/* 1584 */       this.components.clear();
/* 1585 */       setIndex(0, 0);
/* 1586 */       adjustMarkers(readerIndex);
/* 1587 */       return this;
/*      */     } 
/*      */ 
/*      */     
/* 1591 */     int firstComponentId = toComponentIndex(readerIndex);
/* 1592 */     for (int i = 0; i < firstComponentId; i++) {
/* 1593 */       this.components.get(i).freeIfNecessary();
/*      */     }
/* 1595 */     this.components.removeRange(0, firstComponentId);
/*      */ 
/*      */     
/* 1598 */     Component first = this.components.get(0);
/* 1599 */     int offset = first.offset;
/* 1600 */     updateComponentOffsets(0);
/* 1601 */     setIndex(readerIndex - offset, writerIndex - offset);
/* 1602 */     adjustMarkers(offset);
/* 1603 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf discardReadBytes() {
/* 1608 */     ensureAccessible();
/* 1609 */     int readerIndex = readerIndex();
/* 1610 */     if (readerIndex == 0) {
/* 1611 */       return this;
/*      */     }
/*      */ 
/*      */     
/* 1615 */     int writerIndex = writerIndex();
/* 1616 */     if (readerIndex == writerIndex && writerIndex == capacity()) {
/* 1617 */       int size = this.components.size();
/* 1618 */       for (int j = 0; j < size; j++) {
/* 1619 */         this.components.get(j).freeIfNecessary();
/*      */       }
/* 1621 */       this.components.clear();
/* 1622 */       setIndex(0, 0);
/* 1623 */       adjustMarkers(readerIndex);
/* 1624 */       return this;
/*      */     } 
/*      */ 
/*      */     
/* 1628 */     int firstComponentId = toComponentIndex(readerIndex);
/* 1629 */     for (int i = 0; i < firstComponentId; i++) {
/* 1630 */       this.components.get(i).freeIfNecessary();
/*      */     }
/*      */ 
/*      */     
/* 1634 */     Component c = this.components.get(firstComponentId);
/* 1635 */     int adjustment = readerIndex - c.offset;
/* 1636 */     if (adjustment == c.length) {
/*      */       
/* 1638 */       firstComponentId++;
/*      */     } else {
/* 1640 */       Component newC = new Component(c.buf.slice(adjustment, c.length - adjustment));
/* 1641 */       this.components.set(firstComponentId, newC);
/*      */     } 
/*      */     
/* 1644 */     this.components.removeRange(0, firstComponentId);
/*      */ 
/*      */     
/* 1647 */     updateComponentOffsets(0);
/* 1648 */     setIndex(0, writerIndex - readerIndex);
/* 1649 */     adjustMarkers(readerIndex);
/* 1650 */     return this;
/*      */   }
/*      */   
/*      */   private ByteBuf allocBuffer(int capacity) {
/* 1654 */     return this.direct ? alloc().directBuffer(capacity) : alloc().heapBuffer(capacity);
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1659 */     String result = super.toString();
/* 1660 */     result = result.substring(0, result.length() - 1);
/* 1661 */     return result + ", components=" + this.components.size() + ')';
/*      */   }
/*      */   
/*      */   private static final class Component {
/*      */     final ByteBuf buf;
/*      */     final int length;
/*      */     int offset;
/*      */     int endOffset;
/*      */     
/*      */     Component(ByteBuf buf) {
/* 1671 */       this.buf = buf;
/* 1672 */       this.length = buf.readableBytes();
/*      */     }
/*      */     
/*      */     void freeIfNecessary() {
/* 1676 */       this.buf.release();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf readerIndex(int readerIndex) {
/* 1682 */     return (CompositeByteBuf)super.readerIndex(readerIndex);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writerIndex(int writerIndex) {
/* 1687 */     return (CompositeByteBuf)super.writerIndex(writerIndex);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setIndex(int readerIndex, int writerIndex) {
/* 1692 */     return (CompositeByteBuf)super.setIndex(readerIndex, writerIndex);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf clear() {
/* 1697 */     return (CompositeByteBuf)super.clear();
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf markReaderIndex() {
/* 1702 */     return (CompositeByteBuf)super.markReaderIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf resetReaderIndex() {
/* 1707 */     return (CompositeByteBuf)super.resetReaderIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf markWriterIndex() {
/* 1712 */     return (CompositeByteBuf)super.markWriterIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf resetWriterIndex() {
/* 1717 */     return (CompositeByteBuf)super.resetWriterIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf ensureWritable(int minWritableBytes) {
/* 1722 */     return (CompositeByteBuf)super.ensureWritable(minWritableBytes);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, ByteBuf dst) {
/* 1727 */     return (CompositeByteBuf)super.getBytes(index, dst);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, ByteBuf dst, int length) {
/* 1732 */     return (CompositeByteBuf)super.getBytes(index, dst, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, byte[] dst) {
/* 1737 */     return (CompositeByteBuf)super.getBytes(index, dst);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setBoolean(int index, boolean value) {
/* 1742 */     return (CompositeByteBuf)super.setBoolean(index, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setChar(int index, int value) {
/* 1747 */     return (CompositeByteBuf)super.setChar(index, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setFloat(int index, float value) {
/* 1752 */     return (CompositeByteBuf)super.setFloat(index, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setDouble(int index, double value) {
/* 1757 */     return (CompositeByteBuf)super.setDouble(index, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, ByteBuf src) {
/* 1762 */     return (CompositeByteBuf)super.setBytes(index, src);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, ByteBuf src, int length) {
/* 1767 */     return (CompositeByteBuf)super.setBytes(index, src, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, byte[] src) {
/* 1772 */     return (CompositeByteBuf)super.setBytes(index, src);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf setZero(int index, int length) {
/* 1777 */     return (CompositeByteBuf)super.setZero(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf readBytes(ByteBuf dst) {
/* 1782 */     return (CompositeByteBuf)super.readBytes(dst);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf readBytes(ByteBuf dst, int length) {
/* 1787 */     return (CompositeByteBuf)super.readBytes(dst, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
/* 1792 */     return (CompositeByteBuf)super.readBytes(dst, dstIndex, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf readBytes(byte[] dst) {
/* 1797 */     return (CompositeByteBuf)super.readBytes(dst);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf readBytes(byte[] dst, int dstIndex, int length) {
/* 1802 */     return (CompositeByteBuf)super.readBytes(dst, dstIndex, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf readBytes(ByteBuffer dst) {
/* 1807 */     return (CompositeByteBuf)super.readBytes(dst);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf readBytes(OutputStream out, int length) throws IOException {
/* 1812 */     return (CompositeByteBuf)super.readBytes(out, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf skipBytes(int length) {
/* 1817 */     return (CompositeByteBuf)super.skipBytes(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeBoolean(boolean value) {
/* 1822 */     return (CompositeByteBuf)super.writeBoolean(value);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeByte(int value) {
/* 1827 */     return (CompositeByteBuf)super.writeByte(value);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeShort(int value) {
/* 1832 */     return (CompositeByteBuf)super.writeShort(value);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeMedium(int value) {
/* 1837 */     return (CompositeByteBuf)super.writeMedium(value);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeInt(int value) {
/* 1842 */     return (CompositeByteBuf)super.writeInt(value);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeLong(long value) {
/* 1847 */     return (CompositeByteBuf)super.writeLong(value);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeChar(int value) {
/* 1852 */     return (CompositeByteBuf)super.writeChar(value);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeFloat(float value) {
/* 1857 */     return (CompositeByteBuf)super.writeFloat(value);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeDouble(double value) {
/* 1862 */     return (CompositeByteBuf)super.writeDouble(value);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeBytes(ByteBuf src) {
/* 1867 */     return (CompositeByteBuf)super.writeBytes(src);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeBytes(ByteBuf src, int length) {
/* 1872 */     return (CompositeByteBuf)super.writeBytes(src, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
/* 1877 */     return (CompositeByteBuf)super.writeBytes(src, srcIndex, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeBytes(byte[] src) {
/* 1882 */     return (CompositeByteBuf)super.writeBytes(src);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeBytes(byte[] src, int srcIndex, int length) {
/* 1887 */     return (CompositeByteBuf)super.writeBytes(src, srcIndex, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeBytes(ByteBuffer src) {
/* 1892 */     return (CompositeByteBuf)super.writeBytes(src);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf writeZero(int length) {
/* 1897 */     return (CompositeByteBuf)super.writeZero(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf retain(int increment) {
/* 1902 */     return (CompositeByteBuf)super.retain(increment);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf retain() {
/* 1907 */     return (CompositeByteBuf)super.retain();
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf touch() {
/* 1912 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf touch(Object hint) {
/* 1917 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer[] nioBuffers() {
/* 1922 */     return nioBuffers(readerIndex(), readableBytes());
/*      */   }
/*      */ 
/*      */   
/*      */   public CompositeByteBuf discardSomeReadBytes() {
/* 1927 */     return discardReadComponents();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void deallocate() {
/* 1932 */     if (this.freed) {
/*      */       return;
/*      */     }
/*      */     
/* 1936 */     this.freed = true;
/* 1937 */     int size = this.components.size();
/*      */ 
/*      */     
/* 1940 */     for (int i = 0; i < size; i++) {
/* 1941 */       this.components.get(i).freeIfNecessary();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf unwrap() {
/* 1947 */     return null;
/*      */   }
/*      */   
/*      */   private final class CompositeByteBufIterator implements Iterator<ByteBuf> {
/* 1951 */     private final int size = CompositeByteBuf.this.components.size();
/*      */     
/*      */     private int index;
/*      */     
/*      */     public boolean hasNext() {
/* 1956 */       return (this.size > this.index);
/*      */     }
/*      */ 
/*      */     
/*      */     public ByteBuf next() {
/* 1961 */       if (this.size != CompositeByteBuf.this.components.size()) {
/* 1962 */         throw new ConcurrentModificationException();
/*      */       }
/* 1964 */       if (!hasNext()) {
/* 1965 */         throw new NoSuchElementException();
/*      */       }
/*      */       try {
/* 1968 */         return (CompositeByteBuf.this.components.get(this.index++)).buf;
/* 1969 */       } catch (IndexOutOfBoundsException e) {
/* 1970 */         throw new ConcurrentModificationException();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1976 */       throw new UnsupportedOperationException("Read-Only");
/*      */     }
/*      */     
/*      */     private CompositeByteBufIterator() {} }
/*      */   
/*      */   private static final class ComponentList extends ArrayList<Component> {
/*      */     ComponentList(int initialCapacity) {
/* 1983 */       super(initialCapacity);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void removeRange(int fromIndex, int toIndex) {
/* 1989 */       super.removeRange(fromIndex, toIndex);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\CompositeByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */