/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class HpackDecoder
/*     */ {
/*  55 */   private static final Http2Exception DECODE_ULE_128_DECOMPRESSION_EXCEPTION = (Http2Exception)ThrowableUtil.unknownStackTrace(
/*  56 */       Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - decompression failure", new Object[0]), HpackDecoder.class, "decodeULE128(..)");
/*     */   
/*  58 */   private static final Http2Exception DECODE_ULE_128_TO_LONG_DECOMPRESSION_EXCEPTION = (Http2Exception)ThrowableUtil.unknownStackTrace(
/*  59 */       Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - long overflow", new Object[0]), HpackDecoder.class, "decodeULE128(..)");
/*  60 */   private static final Http2Exception DECODE_ULE_128_TO_INT_DECOMPRESSION_EXCEPTION = (Http2Exception)ThrowableUtil.unknownStackTrace(
/*  61 */       Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - int overflow", new Object[0]), HpackDecoder.class, "decodeULE128ToInt(..)");
/*  62 */   private static final Http2Exception DECODE_ILLEGAL_INDEX_VALUE = (Http2Exception)ThrowableUtil.unknownStackTrace(
/*  63 */       Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - illegal index value", new Object[0]), HpackDecoder.class, "decode(..)");
/*  64 */   private static final Http2Exception INDEX_HEADER_ILLEGAL_INDEX_VALUE = (Http2Exception)ThrowableUtil.unknownStackTrace(
/*  65 */       Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - illegal index value", new Object[0]), HpackDecoder.class, "indexHeader(..)");
/*  66 */   private static final Http2Exception READ_NAME_ILLEGAL_INDEX_VALUE = (Http2Exception)ThrowableUtil.unknownStackTrace(
/*  67 */       Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - illegal index value", new Object[0]), HpackDecoder.class, "readName(..)");
/*  68 */   private static final Http2Exception INVALID_MAX_DYNAMIC_TABLE_SIZE = (Http2Exception)ThrowableUtil.unknownStackTrace(
/*  69 */       Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - invalid max dynamic table size", new Object[0]), HpackDecoder.class, "setDynamicTableSize(..)");
/*     */   
/*  71 */   private static final Http2Exception MAX_DYNAMIC_TABLE_SIZE_CHANGE_REQUIRED = (Http2Exception)ThrowableUtil.unknownStackTrace(
/*  72 */       Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - max dynamic table size change required", new Object[0]), HpackDecoder.class, "decode(..)");
/*     */   
/*     */   private static final byte READ_HEADER_REPRESENTATION = 0;
/*     */   
/*     */   private static final byte READ_MAX_DYNAMIC_TABLE_SIZE = 1;
/*     */   
/*     */   private static final byte READ_INDEXED_HEADER = 2;
/*     */   
/*     */   private static final byte READ_INDEXED_HEADER_NAME = 3;
/*     */   
/*     */   private static final byte READ_LITERAL_HEADER_NAME_LENGTH_PREFIX = 4;
/*     */   
/*     */   private static final byte READ_LITERAL_HEADER_NAME_LENGTH = 5;
/*     */   
/*     */   private static final byte READ_LITERAL_HEADER_NAME = 6;
/*     */   
/*     */   private static final byte READ_LITERAL_HEADER_VALUE_LENGTH_PREFIX = 7;
/*     */   
/*     */   private static final byte READ_LITERAL_HEADER_VALUE_LENGTH = 8;
/*     */   
/*     */   private static final byte READ_LITERAL_HEADER_VALUE = 9;
/*     */   private final HpackDynamicTable hpackDynamicTable;
/*     */   private final HpackHuffmanDecoder hpackHuffmanDecoder;
/*     */   private long maxHeaderListSizeGoAway;
/*     */   private long maxHeaderListSize;
/*     */   private long maxDynamicTableSize;
/*     */   private long encoderMaxDynamicTableSize;
/*     */   private boolean maxDynamicTableSizeChangeRequired;
/*     */   
/*     */   HpackDecoder(long maxHeaderListSize, int initialHuffmanDecodeCapacity) {
/* 102 */     this(maxHeaderListSize, initialHuffmanDecodeCapacity, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   HpackDecoder(long maxHeaderListSize, int initialHuffmanDecodeCapacity, int maxHeaderTableSize) {
/* 110 */     this.maxHeaderListSize = ObjectUtil.checkPositive(maxHeaderListSize, "maxHeaderListSize");
/* 111 */     this.maxHeaderListSizeGoAway = Http2CodecUtil.calculateMaxHeaderListSizeGoAway(maxHeaderListSize);
/*     */     
/* 113 */     this.maxDynamicTableSize = this.encoderMaxDynamicTableSize = maxHeaderTableSize;
/* 114 */     this.maxDynamicTableSizeChangeRequired = false;
/* 115 */     this.hpackDynamicTable = new HpackDynamicTable(maxHeaderTableSize);
/* 116 */     this.hpackHuffmanDecoder = new HpackHuffmanDecoder(initialHuffmanDecodeCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void decode(int streamId, ByteBuf in, Http2Headers headers, boolean validateHeaders) throws Http2Exception {
/* 125 */     int index = 0;
/* 126 */     long headersLength = 0L;
/* 127 */     int nameLength = 0;
/* 128 */     int valueLength = 0;
/* 129 */     byte state = 0;
/* 130 */     boolean huffmanEncoded = false;
/* 131 */     CharSequence name = null;
/* 132 */     HeaderType headerType = null;
/* 133 */     HpackUtil.IndexType indexType = HpackUtil.IndexType.NONE;
/* 134 */     while (in.isReadable()) {
/* 135 */       byte b; HpackHeaderField indexedHeader; CharSequence value; switch (state) {
/*     */         case 0:
/* 137 */           b = in.readByte();
/* 138 */           if (this.maxDynamicTableSizeChangeRequired && (b & 0xE0) != 32)
/*     */           {
/* 140 */             throw MAX_DYNAMIC_TABLE_SIZE_CHANGE_REQUIRED;
/*     */           }
/* 142 */           if (b < 0) {
/*     */             
/* 144 */             index = b & Byte.MAX_VALUE;
/* 145 */             switch (index) {
/*     */               case 0:
/* 147 */                 throw DECODE_ILLEGAL_INDEX_VALUE;
/*     */               case 127:
/* 149 */                 state = 2;
/*     */                 continue;
/*     */             } 
/* 152 */             HpackHeaderField hpackHeaderField = getIndexedHeader(index);
/* 153 */             headerType = validate(hpackHeaderField.name, headerType, validateHeaders);
/* 154 */             headersLength = addHeader(headers, hpackHeaderField.name, hpackHeaderField.value, headersLength);
/*     */             continue;
/*     */           } 
/* 157 */           if ((b & 0x40) == 64) {
/*     */             
/* 159 */             indexType = HpackUtil.IndexType.INCREMENTAL;
/* 160 */             index = b & 0x3F;
/* 161 */             switch (index) {
/*     */               case 0:
/* 163 */                 state = 4;
/*     */                 continue;
/*     */               case 63:
/* 166 */                 state = 3;
/*     */                 continue;
/*     */             } 
/*     */             
/* 170 */             name = readName(index);
/* 171 */             headerType = validate(name, headerType, validateHeaders);
/* 172 */             nameLength = name.length();
/* 173 */             state = 7; continue;
/*     */           } 
/* 175 */           if ((b & 0x20) == 32) {
/*     */             
/* 177 */             index = b & 0x1F;
/* 178 */             if (index == 31) {
/* 179 */               state = 1; continue;
/*     */             } 
/* 181 */             setDynamicTableSize(index);
/* 182 */             state = 0;
/*     */             
/*     */             continue;
/*     */           } 
/* 186 */           indexType = ((b & 0x10) == 16) ? HpackUtil.IndexType.NEVER : HpackUtil.IndexType.NONE;
/* 187 */           index = b & 0xF;
/* 188 */           switch (index) {
/*     */             case 0:
/* 190 */               state = 4;
/*     */               continue;
/*     */             case 15:
/* 193 */               state = 3;
/*     */               continue;
/*     */           } 
/*     */           
/* 197 */           name = readName(index);
/* 198 */           headerType = validate(name, headerType, validateHeaders);
/* 199 */           nameLength = name.length();
/* 200 */           state = 7;
/*     */           continue;
/*     */ 
/*     */ 
/*     */         
/*     */         case 1:
/* 206 */           setDynamicTableSize(decodeULE128(in, index));
/* 207 */           state = 0;
/*     */           continue;
/*     */         
/*     */         case 2:
/* 211 */           indexedHeader = getIndexedHeader(decodeULE128(in, index));
/* 212 */           headerType = validate(indexedHeader.name, headerType, validateHeaders);
/* 213 */           headersLength = addHeader(headers, indexedHeader.name, indexedHeader.value, headersLength);
/* 214 */           state = 0;
/*     */           continue;
/*     */ 
/*     */         
/*     */         case 3:
/* 219 */           name = readName(decodeULE128(in, index));
/* 220 */           headerType = validate(name, headerType, validateHeaders);
/* 221 */           nameLength = name.length();
/* 222 */           state = 7;
/*     */           continue;
/*     */         
/*     */         case 4:
/* 226 */           b = in.readByte();
/* 227 */           huffmanEncoded = ((b & 0x80) == 128);
/* 228 */           index = b & Byte.MAX_VALUE;
/* 229 */           if (index == 127) {
/* 230 */             state = 5; continue;
/*     */           } 
/* 232 */           if (index > this.maxHeaderListSizeGoAway - headersLength) {
/* 233 */             Http2CodecUtil.headerListSizeExceeded(this.maxHeaderListSizeGoAway);
/*     */           }
/* 235 */           nameLength = index;
/* 236 */           state = 6;
/*     */           continue;
/*     */ 
/*     */ 
/*     */         
/*     */         case 5:
/* 242 */           nameLength = decodeULE128(in, index);
/*     */           
/* 244 */           if (nameLength > this.maxHeaderListSizeGoAway - headersLength) {
/* 245 */             Http2CodecUtil.headerListSizeExceeded(this.maxHeaderListSizeGoAway);
/*     */           }
/* 247 */           state = 6;
/*     */           continue;
/*     */ 
/*     */         
/*     */         case 6:
/* 252 */           if (in.readableBytes() < nameLength) {
/* 253 */             throw notEnoughDataException(in);
/*     */           }
/*     */           
/* 256 */           name = readStringLiteral(in, nameLength, huffmanEncoded);
/* 257 */           headerType = validate(name, headerType, validateHeaders);
/*     */           
/* 259 */           state = 7;
/*     */           continue;
/*     */         
/*     */         case 7:
/* 263 */           b = in.readByte();
/* 264 */           huffmanEncoded = ((b & 0x80) == 128);
/* 265 */           index = b & Byte.MAX_VALUE;
/* 266 */           switch (index) {
/*     */             case 127:
/* 268 */               state = 8;
/*     */               continue;
/*     */             case 0:
/* 271 */               headerType = validate(name, headerType, validateHeaders);
/* 272 */               headersLength = insertHeader(headers, name, (CharSequence)AsciiString.EMPTY_STRING, indexType, headersLength);
/* 273 */               state = 0;
/*     */               continue;
/*     */           } 
/*     */           
/* 277 */           if (index + nameLength > this.maxHeaderListSizeGoAway - headersLength) {
/* 278 */             Http2CodecUtil.headerListSizeExceeded(this.maxHeaderListSizeGoAway);
/*     */           }
/* 280 */           valueLength = index;
/* 281 */           state = 9;
/*     */           continue;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 8:
/* 288 */           valueLength = decodeULE128(in, index);
/*     */ 
/*     */           
/* 291 */           if (valueLength + nameLength > this.maxHeaderListSizeGoAway - headersLength) {
/* 292 */             Http2CodecUtil.headerListSizeExceeded(this.maxHeaderListSizeGoAway);
/*     */           }
/* 294 */           state = 9;
/*     */           continue;
/*     */ 
/*     */         
/*     */         case 9:
/* 299 */           if (in.readableBytes() < valueLength) {
/* 300 */             throw notEnoughDataException(in);
/*     */           }
/*     */           
/* 303 */           value = readStringLiteral(in, valueLength, huffmanEncoded);
/* 304 */           headerType = validate(name, headerType, validateHeaders);
/* 305 */           headersLength = insertHeader(headers, name, value, indexType, headersLength);
/* 306 */           state = 0;
/*     */           continue;
/*     */       } 
/*     */       
/* 310 */       throw new Error("should not reach here state: " + state);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 317 */     if (headersLength > this.maxHeaderListSize) {
/* 318 */       Http2CodecUtil.headerListSizeExceeded(streamId, this.maxHeaderListSize, true);
/*     */     }
/*     */     
/* 321 */     if (state != 0) {
/* 322 */       throw Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "Incomplete header block fragment.", new Object[0]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxHeaderTableSize(long maxHeaderTableSize) throws Http2Exception {
/* 331 */     if (maxHeaderTableSize < 0L || maxHeaderTableSize > 4294967295L)
/* 332 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Header Table Size must be >= %d and <= %d but was %d", new Object[] {
/* 333 */             Long.valueOf(0L), Long.valueOf(4294967295L), Long.valueOf(maxHeaderTableSize)
/*     */           }); 
/* 335 */     this.maxDynamicTableSize = maxHeaderTableSize;
/* 336 */     if (this.maxDynamicTableSize < this.encoderMaxDynamicTableSize) {
/*     */ 
/*     */       
/* 339 */       this.maxDynamicTableSizeChangeRequired = true;
/* 340 */       this.hpackDynamicTable.setCapacity(this.maxDynamicTableSize);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setMaxHeaderListSize(long maxHeaderListSize, long maxHeaderListSizeGoAway) throws Http2Exception {
/* 345 */     if (maxHeaderListSizeGoAway < maxHeaderListSize || maxHeaderListSizeGoAway < 0L)
/* 346 */       throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "Header List Size GO_AWAY %d must be positive and >= %d", new Object[] {
/* 347 */             Long.valueOf(maxHeaderListSizeGoAway), Long.valueOf(maxHeaderListSize)
/*     */           }); 
/* 349 */     if (maxHeaderListSize < 0L || maxHeaderListSize > 4294967295L)
/* 350 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Header List Size must be >= %d and <= %d but was %d", new Object[] {
/* 351 */             Long.valueOf(0L), Long.valueOf(4294967295L), Long.valueOf(maxHeaderListSize)
/*     */           }); 
/* 353 */     this.maxHeaderListSize = maxHeaderListSize;
/* 354 */     this.maxHeaderListSizeGoAway = maxHeaderListSizeGoAway;
/*     */   }
/*     */   
/*     */   public long getMaxHeaderListSize() {
/* 358 */     return this.maxHeaderListSize;
/*     */   }
/*     */   
/*     */   public long getMaxHeaderListSizeGoAway() {
/* 362 */     return this.maxHeaderListSizeGoAway;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMaxHeaderTableSize() {
/* 370 */     return this.hpackDynamicTable.capacity();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int length() {
/* 377 */     return this.hpackDynamicTable.length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long size() {
/* 384 */     return this.hpackDynamicTable.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   HpackHeaderField getHeaderField(int index) {
/* 391 */     return this.hpackDynamicTable.getEntry(index + 1);
/*     */   }
/*     */   
/*     */   private void setDynamicTableSize(long dynamicTableSize) throws Http2Exception {
/* 395 */     if (dynamicTableSize > this.maxDynamicTableSize) {
/* 396 */       throw INVALID_MAX_DYNAMIC_TABLE_SIZE;
/*     */     }
/* 398 */     this.encoderMaxDynamicTableSize = dynamicTableSize;
/* 399 */     this.maxDynamicTableSizeChangeRequired = false;
/* 400 */     this.hpackDynamicTable.setCapacity(dynamicTableSize);
/*     */   }
/*     */ 
/*     */   
/*     */   private HeaderType validate(CharSequence name, HeaderType previousHeaderType, boolean validateHeaders) throws Http2Exception {
/* 405 */     if (!validateHeaders) {
/* 406 */       return null;
/*     */     }
/*     */     
/* 409 */     if (Http2Headers.PseudoHeaderName.hasPseudoHeaderFormat(name)) {
/* 410 */       if (previousHeaderType == HeaderType.REGULAR_HEADER) {
/* 411 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Pseudo-header field '%s' found after regular header.", new Object[] { name });
/*     */       }
/*     */       
/* 414 */       Http2Headers.PseudoHeaderName pseudoHeader = Http2Headers.PseudoHeaderName.getPseudoHeader(name);
/* 415 */       if (pseudoHeader == null) {
/* 416 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Invalid HTTP/2 pseudo-header '%s' encountered.", new Object[] { name });
/*     */       }
/*     */       
/* 419 */       HeaderType currentHeaderType = pseudoHeader.isRequestOnly() ? HeaderType.REQUEST_PSEUDO_HEADER : HeaderType.RESPONSE_PSEUDO_HEADER;
/*     */       
/* 421 */       if (previousHeaderType != null && currentHeaderType != previousHeaderType) {
/* 422 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Mix of request and response pseudo-headers.", new Object[0]);
/*     */       }
/*     */       
/* 425 */       return currentHeaderType;
/*     */     } 
/*     */     
/* 428 */     return HeaderType.REGULAR_HEADER;
/*     */   }
/*     */   
/*     */   private CharSequence readName(int index) throws Http2Exception {
/* 432 */     if (index <= HpackStaticTable.length) {
/* 433 */       HpackHeaderField hpackHeaderField = HpackStaticTable.getEntry(index);
/* 434 */       return hpackHeaderField.name;
/*     */     } 
/* 436 */     if (index - HpackStaticTable.length <= this.hpackDynamicTable.length()) {
/* 437 */       HpackHeaderField hpackHeaderField = this.hpackDynamicTable.getEntry(index - HpackStaticTable.length);
/* 438 */       return hpackHeaderField.name;
/*     */     } 
/* 440 */     throw READ_NAME_ILLEGAL_INDEX_VALUE;
/*     */   }
/*     */   
/*     */   private HpackHeaderField getIndexedHeader(int index) throws Http2Exception {
/* 444 */     if (index <= HpackStaticTable.length) {
/* 445 */       return HpackStaticTable.getEntry(index);
/*     */     }
/* 447 */     if (index - HpackStaticTable.length <= this.hpackDynamicTable.length()) {
/* 448 */       return this.hpackDynamicTable.getEntry(index - HpackStaticTable.length);
/*     */     }
/* 450 */     throw INDEX_HEADER_ILLEGAL_INDEX_VALUE;
/*     */   }
/*     */ 
/*     */   
/*     */   private long insertHeader(Http2Headers headers, CharSequence name, CharSequence value, HpackUtil.IndexType indexType, long headerSize) throws Http2Exception {
/* 455 */     headerSize = addHeader(headers, name, value, headerSize);
/*     */     
/* 457 */     switch (indexType) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case NONE:
/*     */       case NEVER:
/* 470 */         return headerSize;
/*     */       case INCREMENTAL:
/*     */         this.hpackDynamicTable.add(new HpackHeaderField(name, value));
/*     */     } 
/*     */     throw new Error("should not reach here"); } private long addHeader(Http2Headers headers, CharSequence name, CharSequence value, long headersLength) throws Http2Exception {
/* 475 */     headersLength += HpackHeaderField.sizeOf(name, value);
/* 476 */     if (headersLength > this.maxHeaderListSizeGoAway) {
/* 477 */       Http2CodecUtil.headerListSizeExceeded(this.maxHeaderListSizeGoAway);
/*     */     }
/* 479 */     headers.add(name, value);
/* 480 */     return headersLength;
/*     */   }
/*     */   
/*     */   private CharSequence readStringLiteral(ByteBuf in, int length, boolean huffmanEncoded) throws Http2Exception {
/* 484 */     if (huffmanEncoded) {
/* 485 */       return (CharSequence)this.hpackHuffmanDecoder.decode(in, length);
/*     */     }
/* 487 */     byte[] buf = new byte[length];
/* 488 */     in.readBytes(buf);
/* 489 */     return (CharSequence)new AsciiString(buf, false);
/*     */   }
/*     */   
/*     */   private static IllegalArgumentException notEnoughDataException(ByteBuf in) {
/* 493 */     return new IllegalArgumentException("decode only works with an entire header block! " + in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int decodeULE128(ByteBuf in, int result) throws Http2Exception {
/* 502 */     int readerIndex = in.readerIndex();
/* 503 */     long v = decodeULE128(in, result);
/* 504 */     if (v > 2147483647L) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 510 */       in.readerIndex(readerIndex);
/* 511 */       throw DECODE_ULE_128_TO_INT_DECOMPRESSION_EXCEPTION;
/*     */     } 
/* 513 */     return (int)v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static long decodeULE128(ByteBuf in, long result) throws Http2Exception {
/* 522 */     assert result <= 127L && result >= 0L;
/* 523 */     boolean resultStartedAtZero = (result == 0L);
/* 524 */     int writerIndex = in.writerIndex();
/* 525 */     for (int readerIndex = in.readerIndex(), shift = 0; readerIndex < writerIndex; readerIndex++, shift += 7) {
/* 526 */       byte b = in.getByte(readerIndex);
/* 527 */       if (shift == 56 && ((b & 0x80) != 0 || (b == Byte.MAX_VALUE && !resultStartedAtZero)))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 535 */         throw DECODE_ULE_128_TO_LONG_DECOMPRESSION_EXCEPTION;
/*     */       }
/*     */       
/* 538 */       if ((b & 0x80) == 0) {
/* 539 */         in.readerIndex(readerIndex + 1);
/* 540 */         return result + ((b & 0x7FL) << shift);
/*     */       } 
/* 542 */       result += (b & 0x7FL) << shift;
/*     */     } 
/*     */     
/* 545 */     throw DECODE_ULE_128_DECOMPRESSION_EXCEPTION;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private enum HeaderType
/*     */   {
/* 552 */     REGULAR_HEADER,
/* 553 */     REQUEST_PSEUDO_HEADER,
/* 554 */     RESPONSE_PSEUDO_HEADER;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\HpackDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */