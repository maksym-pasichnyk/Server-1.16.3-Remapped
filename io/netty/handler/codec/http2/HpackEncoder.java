/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.internal.MathUtil;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class HpackEncoder
/*     */ {
/*     */   private final HeaderEntry[] headerFields;
/*  59 */   private final HeaderEntry head = new HeaderEntry(-1, (CharSequence)AsciiString.EMPTY_STRING, (CharSequence)AsciiString.EMPTY_STRING, 2147483647, null);
/*     */   
/*  61 */   private final HpackHuffmanEncoder hpackHuffmanEncoder = new HpackHuffmanEncoder();
/*     */   
/*     */   private final byte hashMask;
/*     */   
/*     */   private final boolean ignoreMaxHeaderListSize;
/*     */   
/*     */   private long size;
/*     */   private long maxHeaderTableSize;
/*     */   private long maxHeaderListSize;
/*     */   
/*     */   HpackEncoder() {
/*  72 */     this(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HpackEncoder(boolean ignoreMaxHeaderListSize) {
/*  79 */     this(ignoreMaxHeaderListSize, 16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HpackEncoder(boolean ignoreMaxHeaderListSize, int arraySizeHint) {
/*  86 */     this.ignoreMaxHeaderListSize = ignoreMaxHeaderListSize;
/*  87 */     this.maxHeaderTableSize = 4096L;
/*  88 */     this.maxHeaderListSize = 4294967295L;
/*     */ 
/*     */     
/*  91 */     this.headerFields = new HeaderEntry[MathUtil.findNextPositivePowerOfTwo(Math.max(2, Math.min(arraySizeHint, 128)))];
/*  92 */     this.hashMask = (byte)(this.headerFields.length - 1);
/*  93 */     this.head.before = this.head.after = this.head;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void encodeHeaders(int streamId, ByteBuf out, Http2Headers headers, Http2HeadersEncoder.SensitivityDetector sensitivityDetector) throws Http2Exception {
/* 103 */     if (this.ignoreMaxHeaderListSize) {
/* 104 */       encodeHeadersIgnoreMaxHeaderListSize(out, headers, sensitivityDetector);
/*     */     } else {
/* 106 */       encodeHeadersEnforceMaxHeaderListSize(streamId, out, headers, sensitivityDetector);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void encodeHeadersEnforceMaxHeaderListSize(int streamId, ByteBuf out, Http2Headers headers, Http2HeadersEncoder.SensitivityDetector sensitivityDetector) throws Http2Exception {
/* 113 */     long headerSize = 0L;
/*     */     
/* 115 */     for (Map.Entry<CharSequence, CharSequence> header : (Iterable<Map.Entry<CharSequence, CharSequence>>)headers) {
/* 116 */       CharSequence name = header.getKey();
/* 117 */       CharSequence value = header.getValue();
/*     */ 
/*     */       
/* 120 */       headerSize += HpackHeaderField.sizeOf(name, value);
/* 121 */       if (headerSize > this.maxHeaderListSize) {
/* 122 */         Http2CodecUtil.headerListSizeExceeded(streamId, this.maxHeaderListSize, false);
/*     */       }
/*     */     } 
/* 125 */     encodeHeadersIgnoreMaxHeaderListSize(out, headers, sensitivityDetector);
/*     */   }
/*     */ 
/*     */   
/*     */   private void encodeHeadersIgnoreMaxHeaderListSize(ByteBuf out, Http2Headers headers, Http2HeadersEncoder.SensitivityDetector sensitivityDetector) throws Http2Exception {
/* 130 */     for (Map.Entry<CharSequence, CharSequence> header : (Iterable<Map.Entry<CharSequence, CharSequence>>)headers) {
/* 131 */       CharSequence name = header.getKey();
/* 132 */       CharSequence value = header.getValue();
/* 133 */       encodeHeader(out, name, value, sensitivityDetector.isSensitive(name, value), 
/* 134 */           HpackHeaderField.sizeOf(name, value));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void encodeHeader(ByteBuf out, CharSequence name, CharSequence value, boolean sensitive, long headerSize) {
/* 145 */     if (sensitive) {
/* 146 */       int nameIndex = getNameIndex(name);
/* 147 */       encodeLiteral(out, name, value, HpackUtil.IndexType.NEVER, nameIndex);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 152 */     if (this.maxHeaderTableSize == 0L) {
/* 153 */       int staticTableIndex = HpackStaticTable.getIndex(name, value);
/* 154 */       if (staticTableIndex == -1) {
/* 155 */         int nameIndex = HpackStaticTable.getIndex(name);
/* 156 */         encodeLiteral(out, name, value, HpackUtil.IndexType.NONE, nameIndex);
/*     */       } else {
/* 158 */         encodeInteger(out, 128, 7, staticTableIndex);
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 164 */     if (headerSize > this.maxHeaderTableSize) {
/* 165 */       int nameIndex = getNameIndex(name);
/* 166 */       encodeLiteral(out, name, value, HpackUtil.IndexType.NONE, nameIndex);
/*     */       
/*     */       return;
/*     */     } 
/* 170 */     HeaderEntry headerField = getEntry(name, value);
/* 171 */     if (headerField != null) {
/* 172 */       int index = getIndex(headerField.index) + HpackStaticTable.length;
/*     */       
/* 174 */       encodeInteger(out, 128, 7, index);
/*     */     } else {
/* 176 */       int staticTableIndex = HpackStaticTable.getIndex(name, value);
/* 177 */       if (staticTableIndex != -1) {
/*     */         
/* 179 */         encodeInteger(out, 128, 7, staticTableIndex);
/*     */       } else {
/* 181 */         ensureCapacity(headerSize);
/* 182 */         encodeLiteral(out, name, value, HpackUtil.IndexType.INCREMENTAL, getNameIndex(name));
/* 183 */         add(name, value, headerSize);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxHeaderTableSize(ByteBuf out, long maxHeaderTableSize) throws Http2Exception {
/* 192 */     if (maxHeaderTableSize < 0L || maxHeaderTableSize > 4294967295L)
/* 193 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Header Table Size must be >= %d and <= %d but was %d", new Object[] {
/* 194 */             Long.valueOf(0L), Long.valueOf(4294967295L), Long.valueOf(maxHeaderTableSize)
/*     */           }); 
/* 196 */     if (this.maxHeaderTableSize == maxHeaderTableSize) {
/*     */       return;
/*     */     }
/* 199 */     this.maxHeaderTableSize = maxHeaderTableSize;
/* 200 */     ensureCapacity(0L);
/*     */     
/* 202 */     encodeInteger(out, 32, 5, maxHeaderTableSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMaxHeaderTableSize() {
/* 209 */     return this.maxHeaderTableSize;
/*     */   }
/*     */   
/*     */   public void setMaxHeaderListSize(long maxHeaderListSize) throws Http2Exception {
/* 213 */     if (maxHeaderListSize < 0L || maxHeaderListSize > 4294967295L)
/* 214 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Header List Size must be >= %d and <= %d but was %d", new Object[] {
/* 215 */             Long.valueOf(0L), Long.valueOf(4294967295L), Long.valueOf(maxHeaderListSize)
/*     */           }); 
/* 217 */     this.maxHeaderListSize = maxHeaderListSize;
/*     */   }
/*     */   
/*     */   public long getMaxHeaderListSize() {
/* 221 */     return this.maxHeaderListSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void encodeInteger(ByteBuf out, int mask, int n, int i) {
/* 228 */     encodeInteger(out, mask, n, i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void encodeInteger(ByteBuf out, int mask, int n, long i) {
/* 235 */     assert n >= 0 && n <= 8 : "N: " + n;
/* 236 */     int nbits = 255 >>> 8 - n;
/* 237 */     if (i < nbits) {
/* 238 */       out.writeByte((int)(mask | i));
/*     */     } else {
/* 240 */       out.writeByte(mask | nbits);
/* 241 */       long length = i - nbits;
/* 242 */       for (; (length & 0xFFFFFFFFFFFFFF80L) != 0L; length >>>= 7L) {
/* 243 */         out.writeByte((int)(length & 0x7FL | 0x80L));
/*     */       }
/* 245 */       out.writeByte((int)length);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void encodeStringLiteral(ByteBuf out, CharSequence string) {
/* 253 */     int huffmanLength = this.hpackHuffmanEncoder.getEncodedLength(string);
/* 254 */     if (huffmanLength < string.length()) {
/* 255 */       encodeInteger(out, 128, 7, huffmanLength);
/* 256 */       this.hpackHuffmanEncoder.encode(out, string);
/*     */     } else {
/* 258 */       encodeInteger(out, 0, 7, string.length());
/* 259 */       if (string instanceof AsciiString) {
/*     */         
/* 261 */         AsciiString asciiString = (AsciiString)string;
/* 262 */         out.writeBytes(asciiString.array(), asciiString.arrayOffset(), asciiString.length());
/*     */       }
/*     */       else {
/*     */         
/* 266 */         out.writeCharSequence(string, CharsetUtil.ISO_8859_1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void encodeLiteral(ByteBuf out, CharSequence name, CharSequence value, HpackUtil.IndexType indexType, int nameIndex) {
/* 276 */     boolean nameIndexValid = (nameIndex != -1);
/* 277 */     switch (indexType) {
/*     */       case INCREMENTAL:
/* 279 */         encodeInteger(out, 64, 6, nameIndexValid ? nameIndex : 0);
/*     */         break;
/*     */       case NONE:
/* 282 */         encodeInteger(out, 0, 4, nameIndexValid ? nameIndex : 0);
/*     */         break;
/*     */       case NEVER:
/* 285 */         encodeInteger(out, 16, 4, nameIndexValid ? nameIndex : 0);
/*     */         break;
/*     */       default:
/* 288 */         throw new Error("should not reach here");
/*     */     } 
/* 290 */     if (!nameIndexValid) {
/* 291 */       encodeStringLiteral(out, name);
/*     */     }
/* 293 */     encodeStringLiteral(out, value);
/*     */   }
/*     */   
/*     */   private int getNameIndex(CharSequence name) {
/* 297 */     int index = HpackStaticTable.getIndex(name);
/* 298 */     if (index == -1) {
/* 299 */       index = getIndex(name);
/* 300 */       if (index >= 0) {
/* 301 */         index += HpackStaticTable.length;
/*     */       }
/*     */     } 
/* 304 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void ensureCapacity(long headerSize) {
/* 312 */     while (this.maxHeaderTableSize - this.size < headerSize) {
/* 313 */       int index = length();
/* 314 */       if (index == 0) {
/*     */         break;
/*     */       }
/* 317 */       remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int length() {
/* 325 */     return (this.size == 0L) ? 0 : (this.head.after.index - this.head.before.index + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long size() {
/* 332 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   HpackHeaderField getHeaderField(int index) {
/* 339 */     HeaderEntry entry = this.head;
/* 340 */     while (index-- >= 0) {
/* 341 */       entry = entry.before;
/*     */     }
/* 343 */     return entry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private HeaderEntry getEntry(CharSequence name, CharSequence value) {
/* 351 */     if (length() == 0 || name == null || value == null) {
/* 352 */       return null;
/*     */     }
/* 354 */     int h = AsciiString.hashCode(name);
/* 355 */     int i = index(h);
/* 356 */     for (HeaderEntry e = this.headerFields[i]; e != null; e = e.next) {
/*     */       
/* 358 */       if (e.hash == h && (HpackUtil.equalsConstantTime(name, e.name) & HpackUtil.equalsConstantTime(value, e.value)) != 0) {
/* 359 */         return e;
/*     */       }
/*     */     } 
/* 362 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getIndex(CharSequence name) {
/* 370 */     if (length() == 0 || name == null) {
/* 371 */       return -1;
/*     */     }
/* 373 */     int h = AsciiString.hashCode(name);
/* 374 */     int i = index(h);
/* 375 */     for (HeaderEntry e = this.headerFields[i]; e != null; e = e.next) {
/* 376 */       if (e.hash == h && HpackUtil.equalsConstantTime(name, e.name) != 0) {
/* 377 */         return getIndex(e.index);
/*     */       }
/*     */     } 
/* 380 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getIndex(int index) {
/* 387 */     return (index == -1) ? -1 : (index - this.head.before.index + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void add(CharSequence name, CharSequence value, long headerSize) {
/* 397 */     if (headerSize > this.maxHeaderTableSize) {
/* 398 */       clear();
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 403 */     while (this.maxHeaderTableSize - this.size < headerSize) {
/* 404 */       remove();
/*     */     }
/*     */     
/* 407 */     int h = AsciiString.hashCode(name);
/* 408 */     int i = index(h);
/* 409 */     HeaderEntry old = this.headerFields[i];
/* 410 */     HeaderEntry e = new HeaderEntry(h, name, value, this.head.before.index - 1, old);
/* 411 */     this.headerFields[i] = e;
/* 412 */     e.addBefore(this.head);
/* 413 */     this.size += headerSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private HpackHeaderField remove() {
/* 420 */     if (this.size == 0L) {
/* 421 */       return null;
/*     */     }
/* 423 */     HeaderEntry eldest = this.head.after;
/* 424 */     int h = eldest.hash;
/* 425 */     int i = index(h);
/* 426 */     HeaderEntry prev = this.headerFields[i];
/* 427 */     HeaderEntry e = prev;
/* 428 */     while (e != null) {
/* 429 */       HeaderEntry next = e.next;
/* 430 */       if (e == eldest) {
/* 431 */         if (prev == eldest) {
/* 432 */           this.headerFields[i] = next;
/*     */         } else {
/* 434 */           prev.next = next;
/*     */         } 
/* 436 */         eldest.remove();
/* 437 */         this.size -= eldest.size();
/* 438 */         return eldest;
/*     */       } 
/* 440 */       prev = e;
/* 441 */       e = next;
/*     */     } 
/* 443 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clear() {
/* 450 */     Arrays.fill((Object[])this.headerFields, (Object)null);
/* 451 */     this.head.before = this.head.after = this.head;
/* 452 */     this.size = 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int index(int h) {
/* 459 */     return h & this.hashMask;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class HeaderEntry
/*     */     extends HpackHeaderField
/*     */   {
/*     */     HeaderEntry before;
/*     */ 
/*     */     
/*     */     HeaderEntry after;
/*     */     
/*     */     HeaderEntry next;
/*     */     
/*     */     int hash;
/*     */     
/*     */     int index;
/*     */ 
/*     */     
/*     */     HeaderEntry(int hash, CharSequence name, CharSequence value, int index, HeaderEntry next) {
/* 480 */       super(name, value);
/* 481 */       this.index = index;
/* 482 */       this.hash = hash;
/* 483 */       this.next = next;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void remove() {
/* 490 */       this.before.after = this.after;
/* 491 */       this.after.before = this.before;
/* 492 */       this.before = null;
/* 493 */       this.after = null;
/* 494 */       this.next = null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void addBefore(HeaderEntry existingEntry) {
/* 501 */       this.after = existingEntry;
/* 502 */       this.before = existingEntry.before;
/* 503 */       this.before.after = this;
/* 504 */       this.after.before = this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\HpackEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */