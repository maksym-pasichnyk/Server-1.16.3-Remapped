/*      */ package io.netty.util;
/*      */ 
/*      */ import io.netty.util.internal.EmptyArrays;
/*      */ import io.netty.util.internal.InternalThreadLocalMap;
/*      */ import io.netty.util.internal.MathUtil;
/*      */ import io.netty.util.internal.ObjectUtil;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.CharBuffer;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.CharsetEncoder;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.regex.Pattern;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class AsciiString
/*      */   implements CharSequence, Comparable<CharSequence>
/*      */ {
/*   47 */   public static final AsciiString EMPTY_STRING = cached("");
/*      */ 
/*      */ 
/*      */   
/*      */   private static final char MAX_CHAR_VALUE = 'ÿ';
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int INDEX_NOT_FOUND = -1;
/*      */ 
/*      */ 
/*      */   
/*      */   private final byte[] value;
/*      */ 
/*      */ 
/*      */   
/*      */   private final int offset;
/*      */ 
/*      */ 
/*      */   
/*      */   private final int length;
/*      */ 
/*      */   
/*      */   private int hash;
/*      */ 
/*      */   
/*      */   private String string;
/*      */ 
/*      */ 
/*      */   
/*      */   public AsciiString(byte[] value) {
/*   78 */     this(value, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AsciiString(byte[] value, boolean copy) {
/*   86 */     this(value, 0, value.length, copy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AsciiString(byte[] value, int start, int length, boolean copy) {
/*   95 */     if (copy) {
/*   96 */       this.value = Arrays.copyOfRange(value, start, start + length);
/*   97 */       this.offset = 0;
/*      */     } else {
/*   99 */       if (MathUtil.isOutOfBounds(start, length, value.length)) {
/*  100 */         throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= start + length(" + length + ") <= value.length(" + value.length + ')');
/*      */       }
/*      */       
/*  103 */       this.value = value;
/*  104 */       this.offset = start;
/*      */     } 
/*  106 */     this.length = length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AsciiString(ByteBuffer value) {
/*  114 */     this(value, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AsciiString(ByteBuffer value, boolean copy) {
/*  124 */     this(value, value.position(), value.remaining(), copy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AsciiString(ByteBuffer value, int start, int length, boolean copy) {
/*  134 */     if (MathUtil.isOutOfBounds(start, length, value.capacity())) {
/*  135 */       throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= start + length(" + length + ") <= value.capacity(" + value
/*  136 */           .capacity() + ')');
/*      */     }
/*      */     
/*  139 */     if (value.hasArray()) {
/*  140 */       if (copy) {
/*  141 */         int bufferOffset = value.arrayOffset() + start;
/*  142 */         this.value = Arrays.copyOfRange(value.array(), bufferOffset, bufferOffset + length);
/*  143 */         this.offset = 0;
/*      */       } else {
/*  145 */         this.value = value.array();
/*  146 */         this.offset = start;
/*      */       } 
/*      */     } else {
/*  149 */       this.value = new byte[length];
/*  150 */       int oldPos = value.position();
/*  151 */       value.get(this.value, 0, length);
/*  152 */       value.position(oldPos);
/*  153 */       this.offset = 0;
/*      */     } 
/*  155 */     this.length = length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AsciiString(char[] value) {
/*  162 */     this(value, 0, value.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AsciiString(char[] value, int start, int length) {
/*  170 */     if (MathUtil.isOutOfBounds(start, length, value.length)) {
/*  171 */       throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= start + length(" + length + ") <= value.length(" + value.length + ')');
/*      */     }
/*      */ 
/*      */     
/*  175 */     this.value = new byte[length];
/*  176 */     for (int i = 0, j = start; i < length; i++, j++) {
/*  177 */       this.value[i] = c2b(value[j]);
/*      */     }
/*  179 */     this.offset = 0;
/*  180 */     this.length = length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AsciiString(char[] value, Charset charset) {
/*  187 */     this(value, charset, 0, value.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AsciiString(char[] value, Charset charset, int start, int length) {
/*  195 */     CharBuffer cbuf = CharBuffer.wrap(value, start, length);
/*  196 */     CharsetEncoder encoder = CharsetUtil.encoder(charset);
/*  197 */     ByteBuffer nativeBuffer = ByteBuffer.allocate((int)(encoder.maxBytesPerChar() * length));
/*  198 */     encoder.encode(cbuf, nativeBuffer, true);
/*  199 */     int bufferOffset = nativeBuffer.arrayOffset();
/*  200 */     this.value = Arrays.copyOfRange(nativeBuffer.array(), bufferOffset, bufferOffset + nativeBuffer.position());
/*  201 */     this.offset = 0;
/*  202 */     this.length = this.value.length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AsciiString(CharSequence value) {
/*  209 */     this(value, 0, value.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AsciiString(CharSequence value, int start, int length) {
/*  217 */     if (MathUtil.isOutOfBounds(start, length, value.length())) {
/*  218 */       throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= start + length(" + length + ") <= value.length(" + value
/*  219 */           .length() + ')');
/*      */     }
/*      */     
/*  222 */     this.value = new byte[length];
/*  223 */     for (int i = 0, j = start; i < length; i++, j++) {
/*  224 */       this.value[i] = c2b(value.charAt(j));
/*      */     }
/*  226 */     this.offset = 0;
/*  227 */     this.length = length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AsciiString(CharSequence value, Charset charset) {
/*  234 */     this(value, charset, 0, value.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AsciiString(CharSequence value, Charset charset, int start, int length) {
/*  242 */     CharBuffer cbuf = CharBuffer.wrap(value, start, start + length);
/*  243 */     CharsetEncoder encoder = CharsetUtil.encoder(charset);
/*  244 */     ByteBuffer nativeBuffer = ByteBuffer.allocate((int)(encoder.maxBytesPerChar() * length));
/*  245 */     encoder.encode(cbuf, nativeBuffer, true);
/*  246 */     int offset = nativeBuffer.arrayOffset();
/*  247 */     this.value = Arrays.copyOfRange(nativeBuffer.array(), offset, offset + nativeBuffer.position());
/*  248 */     this.offset = 0;
/*  249 */     this.length = this.value.length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int forEachByte(ByteProcessor visitor) throws Exception {
/*  259 */     return forEachByte0(0, length(), visitor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int forEachByte(int index, int length, ByteProcessor visitor) throws Exception {
/*  270 */     if (MathUtil.isOutOfBounds(index, length, length())) {
/*  271 */       throw new IndexOutOfBoundsException("expected: 0 <= index(" + index + ") <= start + length(" + length + ") <= length(" + 
/*  272 */           length() + ')');
/*      */     }
/*  274 */     return forEachByte0(index, length, visitor);
/*      */   }
/*      */   
/*      */   private int forEachByte0(int index, int length, ByteProcessor visitor) throws Exception {
/*  278 */     int len = this.offset + index + length;
/*  279 */     for (int i = this.offset + index; i < len; i++) {
/*  280 */       if (!visitor.process(this.value[i])) {
/*  281 */         return i - this.offset;
/*      */       }
/*      */     } 
/*  284 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int forEachByteDesc(ByteProcessor visitor) throws Exception {
/*  294 */     return forEachByteDesc0(0, length(), visitor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int forEachByteDesc(int index, int length, ByteProcessor visitor) throws Exception {
/*  305 */     if (MathUtil.isOutOfBounds(index, length, length())) {
/*  306 */       throw new IndexOutOfBoundsException("expected: 0 <= index(" + index + ") <= start + length(" + length + ") <= length(" + 
/*  307 */           length() + ')');
/*      */     }
/*  309 */     return forEachByteDesc0(index, length, visitor);
/*      */   }
/*      */   
/*      */   private int forEachByteDesc0(int index, int length, ByteProcessor visitor) throws Exception {
/*  313 */     int end = this.offset + index;
/*  314 */     for (int i = this.offset + index + length - 1; i >= end; i--) {
/*  315 */       if (!visitor.process(this.value[i])) {
/*  316 */         return i - this.offset;
/*      */       }
/*      */     } 
/*  319 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public byte byteAt(int index) {
/*  325 */     if (index < 0 || index >= this.length) {
/*  326 */       throw new IndexOutOfBoundsException("index: " + index + " must be in the range [0," + this.length + ")");
/*      */     }
/*      */     
/*  329 */     if (PlatformDependent.hasUnsafe()) {
/*  330 */       return PlatformDependent.getByte(this.value, index + this.offset);
/*      */     }
/*  332 */     return this.value[index + this.offset];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/*  339 */     return (this.length == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int length() {
/*  347 */     return this.length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void arrayChanged() {
/*  355 */     this.string = null;
/*  356 */     this.hash = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] array() {
/*  367 */     return this.value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int arrayOffset() {
/*  376 */     return this.offset;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEntireArrayUsed() {
/*  384 */     return (this.offset == 0 && this.length == this.value.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] toByteArray() {
/*  391 */     return toByteArray(0, length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] toByteArray(int start, int end) {
/*  399 */     return Arrays.copyOfRange(this.value, start + this.offset, end + this.offset);
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
/*      */   public void copy(int srcIdx, byte[] dst, int dstIdx, int length) {
/*  411 */     if (MathUtil.isOutOfBounds(srcIdx, length, length())) {
/*  412 */       throw new IndexOutOfBoundsException("expected: 0 <= srcIdx(" + srcIdx + ") <= srcIdx + length(" + length + ") <= srcLen(" + 
/*  413 */           length() + ')');
/*      */     }
/*      */     
/*  416 */     System.arraycopy(this.value, srcIdx + this.offset, ObjectUtil.checkNotNull(dst, "dst"), dstIdx, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public char charAt(int index) {
/*  421 */     return b2c(byteAt(index));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean contains(CharSequence cs) {
/*  431 */     return (indexOf(cs) >= 0);
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
/*      */   public int compareTo(CharSequence string) {
/*  449 */     if (this == string) {
/*  450 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*  454 */     int length1 = length();
/*  455 */     int length2 = string.length();
/*  456 */     int minLength = Math.min(length1, length2);
/*  457 */     for (int i = 0, j = arrayOffset(); i < minLength; i++, j++) {
/*  458 */       int result = b2c(this.value[j]) - string.charAt(i);
/*  459 */       if (result != 0) {
/*  460 */         return result;
/*      */       }
/*      */     } 
/*      */     
/*  464 */     return length1 - length2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AsciiString concat(CharSequence string) {
/*  474 */     int thisLen = length();
/*  475 */     int thatLen = string.length();
/*  476 */     if (thatLen == 0) {
/*  477 */       return this;
/*      */     }
/*      */     
/*  480 */     if (string.getClass() == AsciiString.class) {
/*  481 */       AsciiString that = (AsciiString)string;
/*  482 */       if (isEmpty()) {
/*  483 */         return that;
/*      */       }
/*      */       
/*  486 */       byte[] arrayOfByte = new byte[thisLen + thatLen];
/*  487 */       System.arraycopy(this.value, arrayOffset(), arrayOfByte, 0, thisLen);
/*  488 */       System.arraycopy(that.value, that.arrayOffset(), arrayOfByte, thisLen, thatLen);
/*  489 */       return new AsciiString(arrayOfByte, false);
/*      */     } 
/*      */     
/*  492 */     if (isEmpty()) {
/*  493 */       return new AsciiString(string);
/*      */     }
/*      */     
/*  496 */     byte[] newValue = new byte[thisLen + thatLen];
/*  497 */     System.arraycopy(this.value, arrayOffset(), newValue, 0, thisLen);
/*  498 */     for (int i = thisLen, j = 0; i < newValue.length; i++, j++) {
/*  499 */       newValue[i] = c2b(string.charAt(j));
/*      */     }
/*      */     
/*  502 */     return new AsciiString(newValue, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean endsWith(CharSequence suffix) {
/*  513 */     int suffixLen = suffix.length();
/*  514 */     return regionMatches(length() - suffixLen, suffix, 0, suffixLen);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean contentEqualsIgnoreCase(CharSequence string) {
/*  525 */     if (string == null || string.length() != length()) {
/*  526 */       return false;
/*      */     }
/*      */     
/*  529 */     if (string.getClass() == AsciiString.class) {
/*  530 */       AsciiString rhs = (AsciiString)string;
/*  531 */       for (int k = arrayOffset(), m = rhs.arrayOffset(); k < length(); k++, m++) {
/*  532 */         if (!equalsIgnoreCase(this.value[k], rhs.value[m])) {
/*  533 */           return false;
/*      */         }
/*      */       } 
/*  536 */       return true;
/*      */     } 
/*      */     
/*  539 */     for (int i = arrayOffset(), j = 0; i < length(); i++, j++) {
/*  540 */       if (!equalsIgnoreCase(b2c(this.value[i]), string.charAt(j))) {
/*  541 */         return false;
/*      */       }
/*      */     } 
/*  544 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public char[] toCharArray() {
/*  553 */     return toCharArray(0, length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public char[] toCharArray(int start, int end) {
/*  562 */     int length = end - start;
/*  563 */     if (length == 0) {
/*  564 */       return EmptyArrays.EMPTY_CHARS;
/*      */     }
/*      */     
/*  567 */     if (MathUtil.isOutOfBounds(start, length, length())) {
/*  568 */       throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= srcIdx + length(" + length + ") <= srcLen(" + 
/*  569 */           length() + ')');
/*      */     }
/*      */     
/*  572 */     char[] buffer = new char[length];
/*  573 */     for (int i = 0, j = start + arrayOffset(); i < length; i++, j++) {
/*  574 */       buffer[i] = b2c(this.value[j]);
/*      */     }
/*  576 */     return buffer;
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
/*      */   public void copy(int srcIdx, char[] dst, int dstIdx, int length) {
/*  588 */     if (dst == null) {
/*  589 */       throw new NullPointerException("dst");
/*      */     }
/*      */     
/*  592 */     if (MathUtil.isOutOfBounds(srcIdx, length, length())) {
/*  593 */       throw new IndexOutOfBoundsException("expected: 0 <= srcIdx(" + srcIdx + ") <= srcIdx + length(" + length + ") <= srcLen(" + 
/*  594 */           length() + ')');
/*      */     }
/*      */     
/*  597 */     int dstEnd = dstIdx + length;
/*  598 */     for (int i = dstIdx, j = srcIdx + arrayOffset(); i < dstEnd; i++, j++) {
/*  599 */       dst[i] = b2c(this.value[j]);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AsciiString subSequence(int start) {
/*  610 */     return subSequence(start, length());
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
/*      */   public AsciiString subSequence(int start, int end) {
/*  622 */     return subSequence(start, end, true);
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
/*      */   public AsciiString subSequence(int start, int end, boolean copy) {
/*  635 */     if (MathUtil.isOutOfBounds(start, end - start, length())) {
/*  636 */       throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= end (" + end + ") <= length(" + 
/*  637 */           length() + ')');
/*      */     }
/*      */     
/*  640 */     if (start == 0 && end == length()) {
/*  641 */       return this;
/*      */     }
/*      */     
/*  644 */     if (end == start) {
/*  645 */       return EMPTY_STRING;
/*      */     }
/*      */     
/*  648 */     return new AsciiString(this.value, start + this.offset, end - start, copy);
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
/*      */   public int indexOf(CharSequence string) {
/*  661 */     return indexOf(string, 0);
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
/*      */   public int indexOf(CharSequence subString, int start) {
/*  675 */     int subCount = subString.length();
/*  676 */     if (start < 0) {
/*  677 */       start = 0;
/*      */     }
/*  679 */     if (subCount <= 0) {
/*  680 */       return (start < this.length) ? start : this.length;
/*      */     }
/*  682 */     if (subCount > this.length - start) {
/*  683 */       return -1;
/*      */     }
/*      */     
/*  686 */     char firstChar = subString.charAt(0);
/*  687 */     if (firstChar > 'ÿ') {
/*  688 */       return -1;
/*      */     }
/*  690 */     byte firstCharAsByte = c2b0(firstChar);
/*  691 */     int len = this.offset + this.length - subCount;
/*  692 */     for (int i = start + this.offset; i <= len; i++) {
/*  693 */       if (this.value[i] == firstCharAsByte) {
/*  694 */         int o1 = i, o2 = 0;
/*  695 */         while (++o2 < subCount && b2c(this.value[++o1]) == subString.charAt(o2));
/*      */ 
/*      */         
/*  698 */         if (o2 == subCount) {
/*  699 */           return i - this.offset;
/*      */         }
/*      */       } 
/*      */     } 
/*  703 */     return -1;
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
/*      */   public int indexOf(char ch, int start) {
/*  716 */     if (ch > 'ÿ') {
/*  717 */       return -1;
/*      */     }
/*      */     
/*  720 */     if (start < 0) {
/*  721 */       start = 0;
/*      */     }
/*      */     
/*  724 */     byte chAsByte = c2b0(ch);
/*  725 */     int len = this.offset + start + this.length;
/*  726 */     for (int i = start + this.offset; i < len; i++) {
/*  727 */       if (this.value[i] == chAsByte) {
/*  728 */         return i - this.offset;
/*      */       }
/*      */     } 
/*  731 */     return -1;
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
/*      */   public int lastIndexOf(CharSequence string) {
/*  745 */     return lastIndexOf(string, length());
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
/*      */   public int lastIndexOf(CharSequence subString, int start) {
/*  759 */     int subCount = subString.length();
/*  760 */     if (start < 0) {
/*  761 */       start = 0;
/*      */     }
/*  763 */     if (subCount <= 0) {
/*  764 */       return (start < this.length) ? start : this.length;
/*      */     }
/*  766 */     if (subCount > this.length - start) {
/*  767 */       return -1;
/*      */     }
/*      */     
/*  770 */     char firstChar = subString.charAt(0);
/*  771 */     if (firstChar > 'ÿ') {
/*  772 */       return -1;
/*      */     }
/*  774 */     byte firstCharAsByte = c2b0(firstChar);
/*  775 */     int end = this.offset + start;
/*  776 */     for (int i = this.offset + this.length - subCount; i >= end; i--) {
/*  777 */       if (this.value[i] == firstCharAsByte) {
/*  778 */         int o1 = i, o2 = 0;
/*  779 */         while (++o2 < subCount && b2c(this.value[++o1]) == subString.charAt(o2));
/*      */ 
/*      */         
/*  782 */         if (o2 == subCount) {
/*  783 */           return i - this.offset;
/*      */         }
/*      */       } 
/*      */     } 
/*  787 */     return -1;
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
/*      */   public boolean regionMatches(int thisStart, CharSequence string, int start, int length) {
/*  802 */     if (string == null) {
/*  803 */       throw new NullPointerException("string");
/*      */     }
/*      */     
/*  806 */     if (start < 0 || string.length() - start < length) {
/*  807 */       return false;
/*      */     }
/*      */     
/*  810 */     int thisLen = length();
/*  811 */     if (thisStart < 0 || thisLen - thisStart < length) {
/*  812 */       return false;
/*      */     }
/*      */     
/*  815 */     if (length <= 0) {
/*  816 */       return true;
/*      */     }
/*      */     
/*  819 */     int thatEnd = start + length;
/*  820 */     for (int i = start, j = thisStart + arrayOffset(); i < thatEnd; i++, j++) {
/*  821 */       if (b2c(this.value[j]) != string.charAt(i)) {
/*  822 */         return false;
/*      */       }
/*      */     } 
/*  825 */     return true;
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
/*      */   public boolean regionMatches(boolean ignoreCase, int thisStart, CharSequence string, int start, int length) {
/*  841 */     if (!ignoreCase) {
/*  842 */       return regionMatches(thisStart, string, start, length);
/*      */     }
/*      */     
/*  845 */     if (string == null) {
/*  846 */       throw new NullPointerException("string");
/*      */     }
/*      */     
/*  849 */     int thisLen = length();
/*  850 */     if (thisStart < 0 || length > thisLen - thisStart) {
/*  851 */       return false;
/*      */     }
/*  853 */     if (start < 0 || length > string.length() - start) {
/*  854 */       return false;
/*      */     }
/*      */     
/*  857 */     thisStart += arrayOffset();
/*  858 */     int thisEnd = thisStart + length;
/*  859 */     while (thisStart < thisEnd) {
/*  860 */       if (!equalsIgnoreCase(b2c(this.value[thisStart++]), string.charAt(start++))) {
/*  861 */         return false;
/*      */       }
/*      */     } 
/*  864 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AsciiString replace(char oldChar, char newChar) {
/*  875 */     if (oldChar > 'ÿ') {
/*  876 */       return this;
/*      */     }
/*      */     
/*  879 */     byte oldCharAsByte = c2b0(oldChar);
/*  880 */     byte newCharAsByte = c2b(newChar);
/*  881 */     int len = this.offset + this.length;
/*  882 */     for (int i = this.offset; i < len; i++) {
/*  883 */       if (this.value[i] == oldCharAsByte) {
/*  884 */         byte[] buffer = new byte[length()];
/*  885 */         System.arraycopy(this.value, this.offset, buffer, 0, i - this.offset);
/*  886 */         buffer[i - this.offset] = newCharAsByte;
/*  887 */         i++;
/*  888 */         for (; i < len; i++) {
/*  889 */           byte oldValue = this.value[i];
/*  890 */           buffer[i - this.offset] = (oldValue != oldCharAsByte) ? oldValue : newCharAsByte;
/*      */         } 
/*  892 */         return new AsciiString(buffer, false);
/*      */       } 
/*      */     } 
/*  895 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean startsWith(CharSequence prefix) {
/*  906 */     return startsWith(prefix, 0);
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
/*      */   public boolean startsWith(CharSequence prefix, int start) {
/*  920 */     return regionMatches(start, prefix, 0, prefix.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AsciiString toLowerCase() {
/*  929 */     boolean lowercased = true;
/*      */     
/*  931 */     int len = length() + arrayOffset(); int i;
/*  932 */     for (i = arrayOffset(); i < len; i++) {
/*  933 */       byte b = this.value[i];
/*  934 */       if (b >= 65 && b <= 90) {
/*  935 */         lowercased = false;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/*  941 */     if (lowercased) {
/*  942 */       return this;
/*      */     }
/*      */     
/*  945 */     byte[] newValue = new byte[length()]; int j;
/*  946 */     for (i = 0, j = arrayOffset(); i < newValue.length; i++, j++) {
/*  947 */       newValue[i] = toLowerCase(this.value[j]);
/*      */     }
/*      */     
/*  950 */     return new AsciiString(newValue, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AsciiString toUpperCase() {
/*  959 */     boolean uppercased = true;
/*      */     
/*  961 */     int len = length() + arrayOffset(); int i;
/*  962 */     for (i = arrayOffset(); i < len; i++) {
/*  963 */       byte b = this.value[i];
/*  964 */       if (b >= 97 && b <= 122) {
/*  965 */         uppercased = false;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/*  971 */     if (uppercased) {
/*  972 */       return this;
/*      */     }
/*      */     
/*  975 */     byte[] newValue = new byte[length()]; int j;
/*  976 */     for (i = 0, j = arrayOffset(); i < newValue.length; i++, j++) {
/*  977 */       newValue[i] = toUpperCase(this.value[j]);
/*      */     }
/*      */     
/*  980 */     return new AsciiString(newValue, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharSequence trim(CharSequence c) {
/*  991 */     if (c.getClass() == AsciiString.class) {
/*  992 */       return ((AsciiString)c).trim();
/*      */     }
/*  994 */     if (c instanceof String) {
/*  995 */       return ((String)c).trim();
/*      */     }
/*  997 */     int start = 0, last = c.length() - 1;
/*  998 */     int end = last;
/*  999 */     while (start <= end && c.charAt(start) <= ' ') {
/* 1000 */       start++;
/*      */     }
/* 1002 */     while (end >= start && c.charAt(end) <= ' ') {
/* 1003 */       end--;
/*      */     }
/* 1005 */     if (start == 0 && end == last) {
/* 1006 */       return c;
/*      */     }
/* 1008 */     return c.subSequence(start, end);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AsciiString trim() {
/* 1018 */     int start = arrayOffset(), last = arrayOffset() + length() - 1;
/* 1019 */     int end = last;
/* 1020 */     while (start <= end && this.value[start] <= 32) {
/* 1021 */       start++;
/*      */     }
/* 1023 */     while (end >= start && this.value[end] <= 32) {
/* 1024 */       end--;
/*      */     }
/* 1026 */     if (start == 0 && end == last) {
/* 1027 */       return this;
/*      */     }
/* 1029 */     return new AsciiString(this.value, start, end - start + 1, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean contentEquals(CharSequence a) {
/* 1039 */     if (a == null || a.length() != length()) {
/* 1040 */       return false;
/*      */     }
/* 1042 */     if (a.getClass() == AsciiString.class) {
/* 1043 */       return equals(a);
/*      */     }
/*      */     
/* 1046 */     for (int i = arrayOffset(), j = 0; j < a.length(); i++, j++) {
/* 1047 */       if (b2c(this.value[i]) != a.charAt(j)) {
/* 1048 */         return false;
/*      */       }
/*      */     } 
/* 1051 */     return true;
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
/*      */   public boolean matches(String expr) {
/* 1063 */     return Pattern.matches(expr, this);
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
/*      */   public AsciiString[] split(String expr, int max) {
/* 1078 */     return toAsciiStringArray(Pattern.compile(expr).split(this, max));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AsciiString[] split(char delim) {
/* 1085 */     List<AsciiString> res = InternalThreadLocalMap.get().arrayList();
/*      */     
/* 1087 */     int start = 0;
/* 1088 */     int length = length(); int i;
/* 1089 */     for (i = start; i < length; i++) {
/* 1090 */       if (charAt(i) == delim) {
/* 1091 */         if (start == i) {
/* 1092 */           res.add(EMPTY_STRING);
/*      */         } else {
/* 1094 */           res.add(new AsciiString(this.value, start + arrayOffset(), i - start, false));
/*      */         } 
/* 1096 */         start = i + 1;
/*      */       } 
/*      */     } 
/*      */     
/* 1100 */     if (start == 0) {
/* 1101 */       res.add(this);
/*      */     }
/* 1103 */     else if (start != length) {
/*      */       
/* 1105 */       res.add(new AsciiString(this.value, start + arrayOffset(), length - start, false));
/*      */     } else {
/*      */       
/* 1108 */       for (i = res.size() - 1; i >= 0 && (
/* 1109 */         (AsciiString)res.get(i)).isEmpty(); i--) {
/* 1110 */         res.remove(i);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1118 */     return res.<AsciiString>toArray(new AsciiString[res.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1128 */     int h = this.hash;
/* 1129 */     if (h == 0) {
/* 1130 */       h = PlatformDependent.hashCodeAscii(this.value, this.offset, this.length);
/* 1131 */       this.hash = h;
/*      */     } 
/* 1133 */     return h;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean equals(Object obj) {
/* 1138 */     if (obj == null || obj.getClass() != AsciiString.class) {
/* 1139 */       return false;
/*      */     }
/* 1141 */     if (this == obj) {
/* 1142 */       return true;
/*      */     }
/*      */     
/* 1145 */     AsciiString other = (AsciiString)obj;
/* 1146 */     return (length() == other.length() && 
/* 1147 */       hashCode() == other.hashCode() && 
/* 1148 */       PlatformDependent.equals(array(), arrayOffset(), other.array(), other.arrayOffset(), length()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1157 */     String cache = this.string;
/* 1158 */     if (cache == null) {
/* 1159 */       cache = toString(0);
/* 1160 */       this.string = cache;
/*      */     } 
/* 1162 */     return cache;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString(int start) {
/* 1170 */     return toString(start, length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString(int start, int end) {
/* 1177 */     int length = end - start;
/* 1178 */     if (length == 0) {
/* 1179 */       return "";
/*      */     }
/*      */     
/* 1182 */     if (MathUtil.isOutOfBounds(start, length, length())) {
/* 1183 */       throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= srcIdx + length(" + length + ") <= srcLen(" + 
/* 1184 */           length() + ')');
/*      */     }
/*      */ 
/*      */     
/* 1188 */     String str = new String(this.value, 0, start + this.offset, length);
/* 1189 */     return str;
/*      */   }
/*      */   
/*      */   public boolean parseBoolean() {
/* 1193 */     return (this.length >= 1 && this.value[this.offset] != 0);
/*      */   }
/*      */   
/*      */   public char parseChar() {
/* 1197 */     return parseChar(0);
/*      */   }
/*      */   
/*      */   public char parseChar(int start) {
/* 1201 */     if (start + 1 >= length()) {
/* 1202 */       throw new IndexOutOfBoundsException("2 bytes required to convert to character. index " + start + " would go out of bounds.");
/*      */     }
/*      */     
/* 1205 */     int startWithOffset = start + this.offset;
/* 1206 */     return (char)(b2c(this.value[startWithOffset]) << 8 | b2c(this.value[startWithOffset + 1]));
/*      */   }
/*      */   
/*      */   public short parseShort() {
/* 1210 */     return parseShort(0, length(), 10);
/*      */   }
/*      */   
/*      */   public short parseShort(int radix) {
/* 1214 */     return parseShort(0, length(), radix);
/*      */   }
/*      */   
/*      */   public short parseShort(int start, int end) {
/* 1218 */     return parseShort(start, end, 10);
/*      */   }
/*      */   
/*      */   public short parseShort(int start, int end, int radix) {
/* 1222 */     int intValue = parseInt(start, end, radix);
/* 1223 */     short result = (short)intValue;
/* 1224 */     if (result != intValue) {
/* 1225 */       throw new NumberFormatException(subSequence(start, end, false).toString());
/*      */     }
/* 1227 */     return result;
/*      */   }
/*      */   
/*      */   public int parseInt() {
/* 1231 */     return parseInt(0, length(), 10);
/*      */   }
/*      */   
/*      */   public int parseInt(int radix) {
/* 1235 */     return parseInt(0, length(), radix);
/*      */   }
/*      */   
/*      */   public int parseInt(int start, int end) {
/* 1239 */     return parseInt(start, end, 10);
/*      */   }
/*      */   
/*      */   public int parseInt(int start, int end, int radix) {
/* 1243 */     if (radix < 2 || radix > 36) {
/* 1244 */       throw new NumberFormatException();
/*      */     }
/*      */     
/* 1247 */     if (start == end) {
/* 1248 */       throw new NumberFormatException();
/*      */     }
/*      */     
/* 1251 */     int i = start;
/* 1252 */     boolean negative = (byteAt(i) == 45);
/* 1253 */     if (negative && ++i == end) {
/* 1254 */       throw new NumberFormatException(subSequence(start, end, false).toString());
/*      */     }
/*      */     
/* 1257 */     return parseInt(i, end, radix, negative);
/*      */   }
/*      */   
/*      */   private int parseInt(int start, int end, int radix, boolean negative) {
/* 1261 */     int max = Integer.MIN_VALUE / radix;
/* 1262 */     int result = 0;
/* 1263 */     int currOffset = start;
/* 1264 */     while (currOffset < end) {
/* 1265 */       int digit = Character.digit((char)(this.value[currOffset++ + this.offset] & 0xFF), radix);
/* 1266 */       if (digit == -1) {
/* 1267 */         throw new NumberFormatException(subSequence(start, end, false).toString());
/*      */       }
/* 1269 */       if (max > result) {
/* 1270 */         throw new NumberFormatException(subSequence(start, end, false).toString());
/*      */       }
/* 1272 */       int next = result * radix - digit;
/* 1273 */       if (next > result) {
/* 1274 */         throw new NumberFormatException(subSequence(start, end, false).toString());
/*      */       }
/* 1276 */       result = next;
/*      */     } 
/* 1278 */     if (!negative) {
/* 1279 */       result = -result;
/* 1280 */       if (result < 0) {
/* 1281 */         throw new NumberFormatException(subSequence(start, end, false).toString());
/*      */       }
/*      */     } 
/* 1284 */     return result;
/*      */   }
/*      */   
/*      */   public long parseLong() {
/* 1288 */     return parseLong(0, length(), 10);
/*      */   }
/*      */   
/*      */   public long parseLong(int radix) {
/* 1292 */     return parseLong(0, length(), radix);
/*      */   }
/*      */   
/*      */   public long parseLong(int start, int end) {
/* 1296 */     return parseLong(start, end, 10);
/*      */   }
/*      */   
/*      */   public long parseLong(int start, int end, int radix) {
/* 1300 */     if (radix < 2 || radix > 36) {
/* 1301 */       throw new NumberFormatException();
/*      */     }
/*      */     
/* 1304 */     if (start == end) {
/* 1305 */       throw new NumberFormatException();
/*      */     }
/*      */     
/* 1308 */     int i = start;
/* 1309 */     boolean negative = (byteAt(i) == 45);
/* 1310 */     if (negative && ++i == end) {
/* 1311 */       throw new NumberFormatException(subSequence(start, end, false).toString());
/*      */     }
/*      */     
/* 1314 */     return parseLong(i, end, radix, negative);
/*      */   }
/*      */   
/*      */   private long parseLong(int start, int end, int radix, boolean negative) {
/* 1318 */     long max = Long.MIN_VALUE / radix;
/* 1319 */     long result = 0L;
/* 1320 */     int currOffset = start;
/* 1321 */     while (currOffset < end) {
/* 1322 */       int digit = Character.digit((char)(this.value[currOffset++ + this.offset] & 0xFF), radix);
/* 1323 */       if (digit == -1) {
/* 1324 */         throw new NumberFormatException(subSequence(start, end, false).toString());
/*      */       }
/* 1326 */       if (max > result) {
/* 1327 */         throw new NumberFormatException(subSequence(start, end, false).toString());
/*      */       }
/* 1329 */       long next = result * radix - digit;
/* 1330 */       if (next > result) {
/* 1331 */         throw new NumberFormatException(subSequence(start, end, false).toString());
/*      */       }
/* 1333 */       result = next;
/*      */     } 
/* 1335 */     if (!negative) {
/* 1336 */       result = -result;
/* 1337 */       if (result < 0L) {
/* 1338 */         throw new NumberFormatException(subSequence(start, end, false).toString());
/*      */       }
/*      */     } 
/* 1341 */     return result;
/*      */   }
/*      */   
/*      */   public float parseFloat() {
/* 1345 */     return parseFloat(0, length());
/*      */   }
/*      */   
/*      */   public float parseFloat(int start, int end) {
/* 1349 */     return Float.parseFloat(toString(start, end));
/*      */   }
/*      */   
/*      */   public double parseDouble() {
/* 1353 */     return parseDouble(0, length());
/*      */   }
/*      */   
/*      */   public double parseDouble(int start, int end) {
/* 1357 */     return Double.parseDouble(toString(start, end));
/*      */   }
/*      */   
/* 1360 */   public static final HashingStrategy<CharSequence> CASE_INSENSITIVE_HASHER = new HashingStrategy<CharSequence>()
/*      */     {
/*      */       public int hashCode(CharSequence o)
/*      */       {
/* 1364 */         return AsciiString.hashCode(o);
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean equals(CharSequence a, CharSequence b) {
/* 1369 */         return AsciiString.contentEqualsIgnoreCase(a, b);
/*      */       }
/*      */     };
/*      */   
/* 1373 */   public static final HashingStrategy<CharSequence> CASE_SENSITIVE_HASHER = new HashingStrategy<CharSequence>()
/*      */     {
/*      */       public int hashCode(CharSequence o)
/*      */       {
/* 1377 */         return AsciiString.hashCode(o);
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean equals(CharSequence a, CharSequence b) {
/* 1382 */         return AsciiString.contentEquals(a, b);
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AsciiString of(CharSequence string) {
/* 1391 */     return (string.getClass() == AsciiString.class) ? (AsciiString)string : new AsciiString(string);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AsciiString cached(String string) {
/* 1401 */     AsciiString asciiString = new AsciiString(string);
/* 1402 */     asciiString.string = string;
/* 1403 */     return asciiString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int hashCode(CharSequence value) {
/* 1412 */     if (value == null) {
/* 1413 */       return 0;
/*      */     }
/* 1415 */     if (value.getClass() == AsciiString.class) {
/* 1416 */       return value.hashCode();
/*      */     }
/*      */     
/* 1419 */     return PlatformDependent.hashCodeAscii(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contains(CharSequence a, CharSequence b) {
/* 1426 */     return contains(a, b, DefaultCharEqualityComparator.INSTANCE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean containsIgnoreCase(CharSequence a, CharSequence b) {
/* 1433 */     return contains(a, b, AsciiCaseInsensitiveCharEqualityComparator.INSTANCE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contentEqualsIgnoreCase(CharSequence a, CharSequence b) {
/* 1441 */     if (a == null || b == null) {
/* 1442 */       return (a == b);
/*      */     }
/*      */     
/* 1445 */     if (a.getClass() == AsciiString.class) {
/* 1446 */       return ((AsciiString)a).contentEqualsIgnoreCase(b);
/*      */     }
/* 1448 */     if (b.getClass() == AsciiString.class) {
/* 1449 */       return ((AsciiString)b).contentEqualsIgnoreCase(a);
/*      */     }
/*      */     
/* 1452 */     if (a.length() != b.length()) {
/* 1453 */       return false;
/*      */     }
/* 1455 */     for (int i = 0, j = 0; i < a.length(); i++, j++) {
/* 1456 */       if (!equalsIgnoreCase(a.charAt(i), b.charAt(j))) {
/* 1457 */         return false;
/*      */       }
/*      */     } 
/* 1460 */     return true;
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
/*      */   public static boolean containsContentEqualsIgnoreCase(Collection<CharSequence> collection, CharSequence value) {
/* 1473 */     for (CharSequence v : collection) {
/* 1474 */       if (contentEqualsIgnoreCase(value, v)) {
/* 1475 */         return true;
/*      */       }
/*      */     } 
/* 1478 */     return false;
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
/*      */   public static boolean containsAllContentEqualsIgnoreCase(Collection<CharSequence> a, Collection<CharSequence> b) {
/* 1491 */     for (CharSequence v : b) {
/* 1492 */       if (!containsContentEqualsIgnoreCase(a, v)) {
/* 1493 */         return false;
/*      */       }
/*      */     } 
/* 1496 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contentEquals(CharSequence a, CharSequence b) {
/* 1503 */     if (a == null || b == null) {
/* 1504 */       return (a == b);
/*      */     }
/*      */     
/* 1507 */     if (a.getClass() == AsciiString.class) {
/* 1508 */       return ((AsciiString)a).contentEquals(b);
/*      */     }
/*      */     
/* 1511 */     if (b.getClass() == AsciiString.class) {
/* 1512 */       return ((AsciiString)b).contentEquals(a);
/*      */     }
/*      */     
/* 1515 */     if (a.length() != b.length()) {
/* 1516 */       return false;
/*      */     }
/* 1518 */     for (int i = 0; i < a.length(); i++) {
/* 1519 */       if (a.charAt(i) != b.charAt(i)) {
/* 1520 */         return false;
/*      */       }
/*      */     } 
/* 1523 */     return true;
/*      */   }
/*      */   
/*      */   private static AsciiString[] toAsciiStringArray(String[] jdkResult) {
/* 1527 */     AsciiString[] res = new AsciiString[jdkResult.length];
/* 1528 */     for (int i = 0; i < jdkResult.length; i++) {
/* 1529 */       res[i] = new AsciiString(jdkResult[i]);
/*      */     }
/* 1531 */     return res;
/*      */   }
/*      */   
/*      */   private static interface CharEqualityComparator {
/*      */     boolean equals(char param1Char1, char param1Char2);
/*      */   }
/*      */   
/*      */   private static final class DefaultCharEqualityComparator implements CharEqualityComparator {
/* 1539 */     static final DefaultCharEqualityComparator INSTANCE = new DefaultCharEqualityComparator();
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(char a, char b) {
/* 1544 */       return (a == b);
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class AsciiCaseInsensitiveCharEqualityComparator
/*      */     implements CharEqualityComparator {
/* 1550 */     static final AsciiCaseInsensitiveCharEqualityComparator INSTANCE = new AsciiCaseInsensitiveCharEqualityComparator();
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(char a, char b) {
/* 1555 */       return AsciiString.equalsIgnoreCase(a, b);
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class GeneralCaseInsensitiveCharEqualityComparator
/*      */     implements CharEqualityComparator {
/* 1561 */     static final GeneralCaseInsensitiveCharEqualityComparator INSTANCE = new GeneralCaseInsensitiveCharEqualityComparator();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(char a, char b) {
/* 1567 */       return (Character.toUpperCase(a) == Character.toUpperCase(b) || 
/* 1568 */         Character.toLowerCase(a) == Character.toLowerCase(b));
/*      */     }
/*      */   }
/*      */   
/*      */   private static boolean contains(CharSequence a, CharSequence b, CharEqualityComparator cmp) {
/* 1573 */     if (a == null || b == null || a.length() < b.length()) {
/* 1574 */       return false;
/*      */     }
/* 1576 */     if (b.length() == 0) {
/* 1577 */       return true;
/*      */     }
/* 1579 */     int bStart = 0;
/* 1580 */     for (int i = 0; i < a.length(); i++) {
/* 1581 */       if (cmp.equals(b.charAt(bStart), a.charAt(i))) {
/*      */         
/* 1583 */         if (++bStart == b.length())
/* 1584 */           return true; 
/*      */       } else {
/* 1586 */         if (a.length() - i < b.length())
/*      */         {
/* 1588 */           return false;
/*      */         }
/* 1590 */         bStart = 0;
/*      */       } 
/*      */     } 
/* 1593 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean regionMatchesCharSequences(CharSequence cs, int csStart, CharSequence string, int start, int length, CharEqualityComparator charEqualityComparator) {
/* 1600 */     if (csStart < 0 || length > cs.length() - csStart) {
/* 1601 */       return false;
/*      */     }
/* 1603 */     if (start < 0 || length > string.length() - start) {
/* 1604 */       return false;
/*      */     }
/*      */     
/* 1607 */     int csIndex = csStart;
/* 1608 */     int csEnd = csIndex + length;
/* 1609 */     int stringIndex = start;
/*      */     
/* 1611 */     while (csIndex < csEnd) {
/* 1612 */       char c1 = cs.charAt(csIndex++);
/* 1613 */       char c2 = string.charAt(stringIndex++);
/*      */       
/* 1615 */       if (!charEqualityComparator.equals(c1, c2)) {
/* 1616 */         return false;
/*      */       }
/*      */     } 
/* 1619 */     return true;
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
/*      */   public static boolean regionMatches(CharSequence cs, boolean ignoreCase, int csStart, CharSequence string, int start, int length) {
/* 1634 */     if (cs == null || string == null) {
/* 1635 */       return false;
/*      */     }
/*      */     
/* 1638 */     if (cs instanceof String && string instanceof String) {
/* 1639 */       return ((String)cs).regionMatches(ignoreCase, csStart, (String)string, start, length);
/*      */     }
/*      */     
/* 1642 */     if (cs instanceof AsciiString) {
/* 1643 */       return ((AsciiString)cs).regionMatches(ignoreCase, csStart, string, start, length);
/*      */     }
/*      */     
/* 1646 */     return regionMatchesCharSequences(cs, csStart, string, start, length, ignoreCase ? GeneralCaseInsensitiveCharEqualityComparator.INSTANCE : DefaultCharEqualityComparator.INSTANCE);
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
/*      */   public static boolean regionMatchesAscii(CharSequence cs, boolean ignoreCase, int csStart, CharSequence string, int start, int length) {
/* 1663 */     if (cs == null || string == null) {
/* 1664 */       return false;
/*      */     }
/*      */     
/* 1667 */     if (!ignoreCase && cs instanceof String && string instanceof String)
/*      */     {
/*      */ 
/*      */       
/* 1671 */       return ((String)cs).regionMatches(false, csStart, (String)string, start, length);
/*      */     }
/*      */     
/* 1674 */     if (cs instanceof AsciiString) {
/* 1675 */       return ((AsciiString)cs).regionMatches(ignoreCase, csStart, string, start, length);
/*      */     }
/*      */     
/* 1678 */     return regionMatchesCharSequences(cs, csStart, string, start, length, ignoreCase ? AsciiCaseInsensitiveCharEqualityComparator.INSTANCE : DefaultCharEqualityComparator.INSTANCE);
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
/*      */   public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
/* 1714 */     if (str == null || searchStr == null) {
/* 1715 */       return -1;
/*      */     }
/* 1717 */     if (startPos < 0) {
/* 1718 */       startPos = 0;
/*      */     }
/* 1720 */     int searchStrLen = searchStr.length();
/* 1721 */     int endLimit = str.length() - searchStrLen + 1;
/* 1722 */     if (startPos > endLimit) {
/* 1723 */       return -1;
/*      */     }
/* 1725 */     if (searchStrLen == 0) {
/* 1726 */       return startPos;
/*      */     }
/* 1728 */     for (int i = startPos; i < endLimit; i++) {
/* 1729 */       if (regionMatches(str, true, i, searchStr, 0, searchStrLen)) {
/* 1730 */         return i;
/*      */       }
/*      */     } 
/* 1733 */     return -1;
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
/*      */   public static int indexOfIgnoreCaseAscii(CharSequence str, CharSequence searchStr, int startPos) {
/* 1767 */     if (str == null || searchStr == null) {
/* 1768 */       return -1;
/*      */     }
/* 1770 */     if (startPos < 0) {
/* 1771 */       startPos = 0;
/*      */     }
/* 1773 */     int searchStrLen = searchStr.length();
/* 1774 */     int endLimit = str.length() - searchStrLen + 1;
/* 1775 */     if (startPos > endLimit) {
/* 1776 */       return -1;
/*      */     }
/* 1778 */     if (searchStrLen == 0) {
/* 1779 */       return startPos;
/*      */     }
/* 1781 */     for (int i = startPos; i < endLimit; i++) {
/* 1782 */       if (regionMatchesAscii(str, true, i, searchStr, 0, searchStrLen)) {
/* 1783 */         return i;
/*      */       }
/*      */     } 
/* 1786 */     return -1;
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
/*      */   public static int indexOf(CharSequence cs, char searchChar, int start) {
/* 1801 */     if (cs instanceof String)
/* 1802 */       return ((String)cs).indexOf(searchChar, start); 
/* 1803 */     if (cs instanceof AsciiString) {
/* 1804 */       return ((AsciiString)cs).indexOf(searchChar, start);
/*      */     }
/* 1806 */     if (cs == null) {
/* 1807 */       return -1;
/*      */     }
/* 1809 */     int sz = cs.length();
/* 1810 */     for (int i = (start < 0) ? 0 : start; i < sz; i++) {
/* 1811 */       if (cs.charAt(i) == searchChar) {
/* 1812 */         return i;
/*      */       }
/*      */     } 
/* 1815 */     return -1;
/*      */   }
/*      */   
/*      */   private static boolean equalsIgnoreCase(byte a, byte b) {
/* 1819 */     return (a == b || toLowerCase(a) == toLowerCase(b));
/*      */   }
/*      */   
/*      */   private static boolean equalsIgnoreCase(char a, char b) {
/* 1823 */     return (a == b || toLowerCase(a) == toLowerCase(b));
/*      */   }
/*      */   
/*      */   private static byte toLowerCase(byte b) {
/* 1827 */     return isUpperCase(b) ? (byte)(b + 32) : b;
/*      */   }
/*      */   
/*      */   private static char toLowerCase(char c) {
/* 1831 */     return isUpperCase(c) ? (char)(c + 32) : c;
/*      */   }
/*      */   
/*      */   private static byte toUpperCase(byte b) {
/* 1835 */     return isLowerCase(b) ? (byte)(b - 32) : b;
/*      */   }
/*      */   
/*      */   private static boolean isLowerCase(byte value) {
/* 1839 */     return (value >= 97 && value <= 122);
/*      */   }
/*      */   
/*      */   public static boolean isUpperCase(byte value) {
/* 1843 */     return (value >= 65 && value <= 90);
/*      */   }
/*      */   
/*      */   public static boolean isUpperCase(char value) {
/* 1847 */     return (value >= 'A' && value <= 'Z');
/*      */   }
/*      */   
/*      */   public static byte c2b(char c) {
/* 1851 */     return (byte)((c > 'ÿ') ? 63 : c);
/*      */   }
/*      */   
/*      */   private static byte c2b0(char c) {
/* 1855 */     return (byte)c;
/*      */   }
/*      */   
/*      */   public static char b2c(byte b) {
/* 1859 */     return (char)(b & 0xFF);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\AsciiString.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */