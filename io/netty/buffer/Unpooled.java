/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Unpooled
/*     */ {
/*  73 */   private static final ByteBufAllocator ALLOC = UnpooledByteBufAllocator.DEFAULT;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public static final ByteOrder BIG_ENDIAN = ByteOrder.BIG_ENDIAN;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   public static final ByteOrder LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   public static final ByteBuf EMPTY_BUFFER = ALLOC.buffer(0, 0);
/*     */   
/*     */   static {
/*  91 */     assert EMPTY_BUFFER instanceof EmptyByteBuf : "EMPTY_BUFFER must be an EmptyByteBuf.";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf buffer() {
/*  99 */     return ALLOC.heapBuffer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf directBuffer() {
/* 107 */     return ALLOC.directBuffer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf buffer(int initialCapacity) {
/* 116 */     return ALLOC.heapBuffer(initialCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf directBuffer(int initialCapacity) {
/* 125 */     return ALLOC.directBuffer(initialCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf buffer(int initialCapacity, int maxCapacity) {
/* 135 */     return ALLOC.heapBuffer(initialCapacity, maxCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf directBuffer(int initialCapacity, int maxCapacity) {
/* 145 */     return ALLOC.directBuffer(initialCapacity, maxCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf wrappedBuffer(byte[] array) {
/* 154 */     if (array.length == 0) {
/* 155 */       return EMPTY_BUFFER;
/*     */     }
/* 157 */     return new UnpooledHeapByteBuf(ALLOC, array, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf wrappedBuffer(byte[] array, int offset, int length) {
/* 166 */     if (length == 0) {
/* 167 */       return EMPTY_BUFFER;
/*     */     }
/*     */     
/* 170 */     if (offset == 0 && length == array.length) {
/* 171 */       return wrappedBuffer(array);
/*     */     }
/*     */     
/* 174 */     return wrappedBuffer(array).slice(offset, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf wrappedBuffer(ByteBuffer buffer) {
/* 183 */     if (!buffer.hasRemaining()) {
/* 184 */       return EMPTY_BUFFER;
/*     */     }
/* 186 */     if (!buffer.isDirect() && buffer.hasArray())
/* 187 */       return wrappedBuffer(buffer
/* 188 */           .array(), buffer
/* 189 */           .arrayOffset() + buffer.position(), buffer
/* 190 */           .remaining()).order(buffer.order()); 
/* 191 */     if (PlatformDependent.hasUnsafe()) {
/* 192 */       if (buffer.isReadOnly()) {
/* 193 */         if (buffer.isDirect()) {
/* 194 */           return new ReadOnlyUnsafeDirectByteBuf(ALLOC, buffer);
/*     */         }
/* 196 */         return new ReadOnlyByteBufferBuf(ALLOC, buffer);
/*     */       } 
/*     */       
/* 199 */       return new UnpooledUnsafeDirectByteBuf(ALLOC, buffer, buffer.remaining());
/*     */     } 
/*     */     
/* 202 */     if (buffer.isReadOnly()) {
/* 203 */       return new ReadOnlyByteBufferBuf(ALLOC, buffer);
/*     */     }
/* 205 */     return new UnpooledDirectByteBuf(ALLOC, buffer, buffer.remaining());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf wrappedBuffer(long memoryAddress, int size, boolean doFree) {
/* 215 */     return new WrappedUnpooledUnsafeDirectByteBuf(ALLOC, memoryAddress, size, doFree);
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
/*     */   public static ByteBuf wrappedBuffer(ByteBuf buffer) {
/* 227 */     if (buffer.isReadable()) {
/* 228 */       return buffer.slice();
/*     */     }
/* 230 */     buffer.release();
/* 231 */     return EMPTY_BUFFER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf wrappedBuffer(byte[]... arrays) {
/* 241 */     return wrappedBuffer(16, arrays);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf wrappedBuffer(ByteBuf... buffers) {
/* 252 */     return wrappedBuffer(16, buffers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf wrappedBuffer(ByteBuffer... buffers) {
/* 261 */     return wrappedBuffer(16, buffers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf wrappedBuffer(int maxNumComponents, byte[]... arrays) {
/* 270 */     switch (arrays.length) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 295 */         return EMPTY_BUFFER;
/*     */       case 1:
/*     */         if ((arrays[0]).length != 0)
/*     */           return wrappedBuffer(arrays[0]); 
/*     */     }  List<ByteBuf> components = new ArrayList<ByteBuf>(arrays.length);
/*     */     for (byte[] a : arrays) {
/*     */       if (a == null)
/*     */         break; 
/*     */       if (a.length > 0)
/*     */         components.add(wrappedBuffer(a)); 
/*     */     } 
/*     */     if (!components.isEmpty())
/*     */       return new CompositeByteBuf(ALLOC, false, maxNumComponents, components);  } public static ByteBuf wrappedBuffer(int maxNumComponents, ByteBuf... buffers) { ByteBuf buffer;
/* 308 */     switch (buffers.length) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 329 */         return EMPTY_BUFFER;
/*     */       case 1:
/*     */         buffer = buffers[0]; if (buffer.isReadable())
/*     */           return wrappedBuffer(buffer.order(BIG_ENDIAN));  buffer.release();
/*     */     }  for (int i = 0; i < buffers.length; i++) {
/*     */       ByteBuf buf = buffers[i];
/*     */       if (buf.isReadable())
/*     */         return new CompositeByteBuf(ALLOC, false, maxNumComponents, buffers, i, buffers.length); 
/*     */       buf.release();
/* 338 */     }  } public static ByteBuf wrappedBuffer(int maxNumComponents, ByteBuffer... buffers) { switch (buffers.length)
/*     */     
/*     */     { 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 363 */         return EMPTY_BUFFER;
/*     */       case 1:
/*     */         if (buffers[0].hasRemaining())
/*     */           return wrappedBuffer(buffers[0].order(BIG_ENDIAN));  }  List<ByteBuf> components = new ArrayList<ByteBuf>(buffers.length); for (ByteBuffer b : buffers) { if (b == null)
/*     */         break;  if (b.remaining() > 0)
/*     */         components.add(wrappedBuffer(b.order(BIG_ENDIAN)));  }
/*     */      if (!components.isEmpty())
/* 370 */       return new CompositeByteBuf(ALLOC, false, maxNumComponents, components);  } public static CompositeByteBuf compositeBuffer() { return compositeBuffer(16); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CompositeByteBuf compositeBuffer(int maxNumComponents) {
/* 377 */     return new CompositeByteBuf(ALLOC, false, maxNumComponents);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copiedBuffer(byte[] array) {
/* 386 */     if (array.length == 0) {
/* 387 */       return EMPTY_BUFFER;
/*     */     }
/* 389 */     return wrappedBuffer((byte[])array.clone());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copiedBuffer(byte[] array, int offset, int length) {
/* 399 */     if (length == 0) {
/* 400 */       return EMPTY_BUFFER;
/*     */     }
/* 402 */     byte[] copy = new byte[length];
/* 403 */     System.arraycopy(array, offset, copy, 0, length);
/* 404 */     return wrappedBuffer(copy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copiedBuffer(ByteBuffer buffer) {
/* 414 */     int length = buffer.remaining();
/* 415 */     if (length == 0) {
/* 416 */       return EMPTY_BUFFER;
/*     */     }
/* 418 */     byte[] copy = new byte[length];
/*     */ 
/*     */     
/* 421 */     ByteBuffer duplicate = buffer.duplicate();
/* 422 */     duplicate.get(copy);
/* 423 */     return wrappedBuffer(copy).order(duplicate.order());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copiedBuffer(ByteBuf buffer) {
/* 433 */     int readable = buffer.readableBytes();
/* 434 */     if (readable > 0) {
/* 435 */       ByteBuf copy = buffer(readable);
/* 436 */       copy.writeBytes(buffer, buffer.readerIndex(), readable);
/* 437 */       return copy;
/*     */     } 
/* 439 */     return EMPTY_BUFFER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copiedBuffer(byte[]... arrays) {
/* 450 */     switch (arrays.length) {
/*     */       case 0:
/* 452 */         return EMPTY_BUFFER;
/*     */       case 1:
/* 454 */         if ((arrays[0]).length == 0) {
/* 455 */           return EMPTY_BUFFER;
/*     */         }
/* 457 */         return copiedBuffer(arrays[0]);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 462 */     int length = 0;
/* 463 */     for (byte[] a : arrays) {
/* 464 */       if (Integer.MAX_VALUE - length < a.length) {
/* 465 */         throw new IllegalArgumentException("The total length of the specified arrays is too big.");
/*     */       }
/*     */       
/* 468 */       length += a.length;
/*     */     } 
/*     */     
/* 471 */     if (length == 0) {
/* 472 */       return EMPTY_BUFFER;
/*     */     }
/*     */     
/* 475 */     byte[] mergedArray = new byte[length];
/* 476 */     for (int i = 0, j = 0; i < arrays.length; i++) {
/* 477 */       byte[] a = arrays[i];
/* 478 */       System.arraycopy(a, 0, mergedArray, j, a.length);
/* 479 */       j += a.length;
/*     */     } 
/*     */     
/* 482 */     return wrappedBuffer(mergedArray);
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
/*     */   public static ByteBuf copiedBuffer(ByteBuf... buffers) {
/* 496 */     switch (buffers.length) {
/*     */       case 0:
/* 498 */         return EMPTY_BUFFER;
/*     */       case 1:
/* 500 */         return copiedBuffer(buffers[0]);
/*     */     } 
/*     */ 
/*     */     
/* 504 */     ByteOrder order = null;
/* 505 */     int length = 0;
/* 506 */     for (ByteBuf b : buffers) {
/* 507 */       int bLen = b.readableBytes();
/* 508 */       if (bLen > 0) {
/*     */ 
/*     */         
/* 511 */         if (Integer.MAX_VALUE - length < bLen) {
/* 512 */           throw new IllegalArgumentException("The total length of the specified buffers is too big.");
/*     */         }
/*     */         
/* 515 */         length += bLen;
/* 516 */         if (order != null) {
/* 517 */           if (!order.equals(b.order())) {
/* 518 */             throw new IllegalArgumentException("inconsistent byte order");
/*     */           }
/*     */         } else {
/* 521 */           order = b.order();
/*     */         } 
/*     */       } 
/*     */     } 
/* 525 */     if (length == 0) {
/* 526 */       return EMPTY_BUFFER;
/*     */     }
/*     */     
/* 529 */     byte[] mergedArray = new byte[length];
/* 530 */     for (int i = 0, j = 0; i < buffers.length; i++) {
/* 531 */       ByteBuf b = buffers[i];
/* 532 */       int bLen = b.readableBytes();
/* 533 */       b.getBytes(b.readerIndex(), mergedArray, j, bLen);
/* 534 */       j += bLen;
/*     */     } 
/*     */     
/* 537 */     return wrappedBuffer(mergedArray).order(order);
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
/*     */   public static ByteBuf copiedBuffer(ByteBuffer... buffers) {
/* 551 */     switch (buffers.length) {
/*     */       case 0:
/* 553 */         return EMPTY_BUFFER;
/*     */       case 1:
/* 555 */         return copiedBuffer(buffers[0]);
/*     */     } 
/*     */ 
/*     */     
/* 559 */     ByteOrder order = null;
/* 560 */     int length = 0;
/* 561 */     for (ByteBuffer b : buffers) {
/* 562 */       int bLen = b.remaining();
/* 563 */       if (bLen > 0) {
/*     */ 
/*     */         
/* 566 */         if (Integer.MAX_VALUE - length < bLen) {
/* 567 */           throw new IllegalArgumentException("The total length of the specified buffers is too big.");
/*     */         }
/*     */         
/* 570 */         length += bLen;
/* 571 */         if (order != null) {
/* 572 */           if (!order.equals(b.order())) {
/* 573 */             throw new IllegalArgumentException("inconsistent byte order");
/*     */           }
/*     */         } else {
/* 576 */           order = b.order();
/*     */         } 
/*     */       } 
/*     */     } 
/* 580 */     if (length == 0) {
/* 581 */       return EMPTY_BUFFER;
/*     */     }
/*     */     
/* 584 */     byte[] mergedArray = new byte[length];
/* 585 */     for (int i = 0, j = 0; i < buffers.length; i++) {
/*     */ 
/*     */       
/* 588 */       ByteBuffer b = buffers[i].duplicate();
/* 589 */       int bLen = b.remaining();
/* 590 */       b.get(mergedArray, j, bLen);
/* 591 */       j += bLen;
/*     */     } 
/*     */     
/* 594 */     return wrappedBuffer(mergedArray).order(order);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copiedBuffer(CharSequence string, Charset charset) {
/* 604 */     if (string == null) {
/* 605 */       throw new NullPointerException("string");
/*     */     }
/*     */     
/* 608 */     if (string instanceof CharBuffer) {
/* 609 */       return copiedBuffer((CharBuffer)string, charset);
/*     */     }
/*     */     
/* 612 */     return copiedBuffer(CharBuffer.wrap(string), charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copiedBuffer(CharSequence string, int offset, int length, Charset charset) {
/* 623 */     if (string == null) {
/* 624 */       throw new NullPointerException("string");
/*     */     }
/* 626 */     if (length == 0) {
/* 627 */       return EMPTY_BUFFER;
/*     */     }
/*     */     
/* 630 */     if (string instanceof CharBuffer) {
/* 631 */       CharBuffer buf = (CharBuffer)string;
/* 632 */       if (buf.hasArray()) {
/* 633 */         return copiedBuffer(buf
/* 634 */             .array(), buf
/* 635 */             .arrayOffset() + buf.position() + offset, length, charset);
/*     */       }
/*     */ 
/*     */       
/* 639 */       buf = buf.slice();
/* 640 */       buf.limit(length);
/* 641 */       buf.position(offset);
/* 642 */       return copiedBuffer(buf, charset);
/*     */     } 
/*     */     
/* 645 */     return copiedBuffer(CharBuffer.wrap(string, offset, offset + length), charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copiedBuffer(char[] array, Charset charset) {
/* 655 */     if (array == null) {
/* 656 */       throw new NullPointerException("array");
/*     */     }
/* 658 */     return copiedBuffer(array, 0, array.length, charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copiedBuffer(char[] array, int offset, int length, Charset charset) {
/* 668 */     if (array == null) {
/* 669 */       throw new NullPointerException("array");
/*     */     }
/* 671 */     if (length == 0) {
/* 672 */       return EMPTY_BUFFER;
/*     */     }
/* 674 */     return copiedBuffer(CharBuffer.wrap(array, offset, length), charset);
/*     */   }
/*     */   
/*     */   private static ByteBuf copiedBuffer(CharBuffer buffer, Charset charset) {
/* 678 */     return ByteBufUtil.encodeString0(ALLOC, true, buffer, charset, 0);
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
/*     */   public static ByteBuf unmodifiableBuffer(ByteBuf buffer) {
/* 691 */     ByteOrder endianness = buffer.order();
/* 692 */     if (endianness == BIG_ENDIAN) {
/* 693 */       return new ReadOnlyByteBuf(buffer);
/*     */     }
/*     */     
/* 696 */     return (new ReadOnlyByteBuf(buffer.order(BIG_ENDIAN))).order(LITTLE_ENDIAN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copyInt(int value) {
/* 703 */     ByteBuf buf = buffer(4);
/* 704 */     buf.writeInt(value);
/* 705 */     return buf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copyInt(int... values) {
/* 712 */     if (values == null || values.length == 0) {
/* 713 */       return EMPTY_BUFFER;
/*     */     }
/* 715 */     ByteBuf buffer = buffer(values.length * 4);
/* 716 */     for (int v : values) {
/* 717 */       buffer.writeInt(v);
/*     */     }
/* 719 */     return buffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copyShort(int value) {
/* 726 */     ByteBuf buf = buffer(2);
/* 727 */     buf.writeShort(value);
/* 728 */     return buf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copyShort(short... values) {
/* 735 */     if (values == null || values.length == 0) {
/* 736 */       return EMPTY_BUFFER;
/*     */     }
/* 738 */     ByteBuf buffer = buffer(values.length * 2);
/* 739 */     for (int v : values) {
/* 740 */       buffer.writeShort(v);
/*     */     }
/* 742 */     return buffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copyShort(int... values) {
/* 749 */     if (values == null || values.length == 0) {
/* 750 */       return EMPTY_BUFFER;
/*     */     }
/* 752 */     ByteBuf buffer = buffer(values.length * 2);
/* 753 */     for (int v : values) {
/* 754 */       buffer.writeShort(v);
/*     */     }
/* 756 */     return buffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copyMedium(int value) {
/* 763 */     ByteBuf buf = buffer(3);
/* 764 */     buf.writeMedium(value);
/* 765 */     return buf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copyMedium(int... values) {
/* 772 */     if (values == null || values.length == 0) {
/* 773 */       return EMPTY_BUFFER;
/*     */     }
/* 775 */     ByteBuf buffer = buffer(values.length * 3);
/* 776 */     for (int v : values) {
/* 777 */       buffer.writeMedium(v);
/*     */     }
/* 779 */     return buffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copyLong(long value) {
/* 786 */     ByteBuf buf = buffer(8);
/* 787 */     buf.writeLong(value);
/* 788 */     return buf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copyLong(long... values) {
/* 795 */     if (values == null || values.length == 0) {
/* 796 */       return EMPTY_BUFFER;
/*     */     }
/* 798 */     ByteBuf buffer = buffer(values.length * 8);
/* 799 */     for (long v : values) {
/* 800 */       buffer.writeLong(v);
/*     */     }
/* 802 */     return buffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copyBoolean(boolean value) {
/* 809 */     ByteBuf buf = buffer(1);
/* 810 */     buf.writeBoolean(value);
/* 811 */     return buf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copyBoolean(boolean... values) {
/* 818 */     if (values == null || values.length == 0) {
/* 819 */       return EMPTY_BUFFER;
/*     */     }
/* 821 */     ByteBuf buffer = buffer(values.length);
/* 822 */     for (boolean v : values) {
/* 823 */       buffer.writeBoolean(v);
/*     */     }
/* 825 */     return buffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copyFloat(float value) {
/* 832 */     ByteBuf buf = buffer(4);
/* 833 */     buf.writeFloat(value);
/* 834 */     return buf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copyFloat(float... values) {
/* 841 */     if (values == null || values.length == 0) {
/* 842 */       return EMPTY_BUFFER;
/*     */     }
/* 844 */     ByteBuf buffer = buffer(values.length * 4);
/* 845 */     for (float v : values) {
/* 846 */       buffer.writeFloat(v);
/*     */     }
/* 848 */     return buffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copyDouble(double value) {
/* 855 */     ByteBuf buf = buffer(8);
/* 856 */     buf.writeDouble(value);
/* 857 */     return buf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf copyDouble(double... values) {
/* 864 */     if (values == null || values.length == 0) {
/* 865 */       return EMPTY_BUFFER;
/*     */     }
/* 867 */     ByteBuf buffer = buffer(values.length * 8);
/* 868 */     for (double v : values) {
/* 869 */       buffer.writeDouble(v);
/*     */     }
/* 871 */     return buffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf unreleasableBuffer(ByteBuf buf) {
/* 878 */     return new UnreleasableByteBuf(buf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf unmodifiableBuffer(ByteBuf... buffers) {
/* 886 */     return new FixedCompositeByteBuf(ALLOC, buffers);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\Unpooled.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */