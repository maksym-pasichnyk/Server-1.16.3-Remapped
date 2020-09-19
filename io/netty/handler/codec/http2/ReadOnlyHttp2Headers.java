/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.handler.codec.CharSequenceValueConverter;
/*     */ import io.netty.handler.codec.Headers;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.HashingStrategy;
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ReadOnlyHttp2Headers
/*     */   implements Http2Headers
/*     */ {
/*     */   private static final byte PSEUDO_HEADER_TOKEN = 58;
/*     */   private final AsciiString[] pseudoHeaders;
/*     */   private final AsciiString[] otherHeaders;
/*     */   
/*     */   public static ReadOnlyHttp2Headers trailers(boolean validateHeaders, AsciiString... otherHeaders) {
/*  65 */     return new ReadOnlyHttp2Headers(validateHeaders, EmptyArrays.EMPTY_ASCII_STRINGS, otherHeaders);
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
/*     */   public static ReadOnlyHttp2Headers clientHeaders(boolean validateHeaders, AsciiString method, AsciiString path, AsciiString scheme, AsciiString authority, AsciiString... otherHeaders) {
/*  87 */     return new ReadOnlyHttp2Headers(validateHeaders, new AsciiString[] { Http2Headers.PseudoHeaderName.METHOD
/*     */           
/*  89 */           .value(), method, Http2Headers.PseudoHeaderName.PATH.value(), path, Http2Headers.PseudoHeaderName.SCHEME
/*  90 */           .value(), scheme, Http2Headers.PseudoHeaderName.AUTHORITY.value(), authority }, otherHeaders);
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
/*     */   public static ReadOnlyHttp2Headers serverHeaders(boolean validateHeaders, AsciiString status, AsciiString... otherHeaders) {
/* 110 */     return new ReadOnlyHttp2Headers(validateHeaders, new AsciiString[] { Http2Headers.PseudoHeaderName.STATUS
/* 111 */           .value(), status }, otherHeaders);
/*     */   }
/*     */ 
/*     */   
/*     */   private ReadOnlyHttp2Headers(boolean validateHeaders, AsciiString[] pseudoHeaders, AsciiString... otherHeaders) {
/* 116 */     assert (pseudoHeaders.length & 0x1) == 0;
/* 117 */     if ((otherHeaders.length & 0x1) != 0) {
/* 118 */       throw newInvalidArraySizeException();
/*     */     }
/* 120 */     if (validateHeaders) {
/* 121 */       validateHeaders(pseudoHeaders, otherHeaders);
/*     */     }
/* 123 */     this.pseudoHeaders = pseudoHeaders;
/* 124 */     this.otherHeaders = otherHeaders;
/*     */   }
/*     */   
/*     */   private static IllegalArgumentException newInvalidArraySizeException() {
/* 128 */     return new IllegalArgumentException("pseudoHeaders and otherHeaders must be arrays of [name, value] pairs");
/*     */   }
/*     */ 
/*     */   
/*     */   private static void validateHeaders(AsciiString[] pseudoHeaders, AsciiString... otherHeaders) {
/* 133 */     for (int i = 1; i < pseudoHeaders.length; i += 2) {
/*     */       
/* 135 */       if (pseudoHeaders[i] == null) {
/* 136 */         throw new IllegalArgumentException("pseudoHeaders value at index " + i + " is null");
/*     */       }
/*     */     } 
/*     */     
/* 140 */     boolean seenNonPseudoHeader = false;
/* 141 */     int otherHeadersEnd = otherHeaders.length - 1;
/* 142 */     for (int j = 0; j < otherHeadersEnd; j += 2) {
/* 143 */       AsciiString name = otherHeaders[j];
/* 144 */       DefaultHttp2Headers.HTTP2_NAME_VALIDATOR.validateName(name);
/* 145 */       if (!seenNonPseudoHeader && !name.isEmpty() && name.byteAt(0) != 58) {
/* 146 */         seenNonPseudoHeader = true;
/* 147 */       } else if (seenNonPseudoHeader && !name.isEmpty() && name.byteAt(0) == 58) {
/* 148 */         throw new IllegalArgumentException("otherHeaders name at index " + j + " is a pseudo header that appears after non-pseudo headers.");
/*     */       } 
/*     */       
/* 151 */       if (otherHeaders[j + 1] == null) {
/* 152 */         throw new IllegalArgumentException("otherHeaders value at index " + (j + 1) + " is null");
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private AsciiString get0(CharSequence name) {
/* 158 */     int nameHash = AsciiString.hashCode(name);
/*     */     
/* 160 */     int pseudoHeadersEnd = this.pseudoHeaders.length - 1;
/* 161 */     for (int i = 0; i < pseudoHeadersEnd; i += 2) {
/* 162 */       AsciiString roName = this.pseudoHeaders[i];
/* 163 */       if (roName.hashCode() == nameHash && roName.contentEqualsIgnoreCase(name)) {
/* 164 */         return this.pseudoHeaders[i + 1];
/*     */       }
/*     */     } 
/*     */     
/* 168 */     int otherHeadersEnd = this.otherHeaders.length - 1;
/* 169 */     for (int j = 0; j < otherHeadersEnd; j += 2) {
/* 170 */       AsciiString roName = this.otherHeaders[j];
/* 171 */       if (roName.hashCode() == nameHash && roName.contentEqualsIgnoreCase(name)) {
/* 172 */         return this.otherHeaders[j + 1];
/*     */       }
/*     */     } 
/* 175 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence get(CharSequence name) {
/* 180 */     return (CharSequence)get0(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence get(CharSequence name, CharSequence defaultValue) {
/* 185 */     CharSequence value = get(name);
/* 186 */     return (value != null) ? value : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence getAndRemove(CharSequence name) {
/* 191 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence getAndRemove(CharSequence name, CharSequence defaultValue) {
/* 196 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public List<CharSequence> getAll(CharSequence name) {
/* 201 */     int nameHash = AsciiString.hashCode(name);
/* 202 */     List<CharSequence> values = new ArrayList<CharSequence>();
/*     */     
/* 204 */     int pseudoHeadersEnd = this.pseudoHeaders.length - 1;
/* 205 */     for (int i = 0; i < pseudoHeadersEnd; i += 2) {
/* 206 */       AsciiString roName = this.pseudoHeaders[i];
/* 207 */       if (roName.hashCode() == nameHash && roName.contentEqualsIgnoreCase(name)) {
/* 208 */         values.add(this.pseudoHeaders[i + 1]);
/*     */       }
/*     */     } 
/*     */     
/* 212 */     int otherHeadersEnd = this.otherHeaders.length - 1;
/* 213 */     for (int j = 0; j < otherHeadersEnd; j += 2) {
/* 214 */       AsciiString roName = this.otherHeaders[j];
/* 215 */       if (roName.hashCode() == nameHash && roName.contentEqualsIgnoreCase(name)) {
/* 216 */         values.add(this.otherHeaders[j + 1]);
/*     */       }
/*     */     } 
/*     */     
/* 220 */     return values;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<CharSequence> getAllAndRemove(CharSequence name) {
/* 225 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean getBoolean(CharSequence name) {
/* 230 */     AsciiString value = get0(name);
/* 231 */     return (value != null) ? Boolean.valueOf(CharSequenceValueConverter.INSTANCE.convertToBoolean((CharSequence)value)) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getBoolean(CharSequence name, boolean defaultValue) {
/* 236 */     Boolean value = getBoolean(name);
/* 237 */     return (value != null) ? value.booleanValue() : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public Byte getByte(CharSequence name) {
/* 242 */     AsciiString value = get0(name);
/* 243 */     return (value != null) ? Byte.valueOf(CharSequenceValueConverter.INSTANCE.convertToByte((CharSequence)value)) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getByte(CharSequence name, byte defaultValue) {
/* 248 */     Byte value = getByte(name);
/* 249 */     return (value != null) ? value.byteValue() : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public Character getChar(CharSequence name) {
/* 254 */     AsciiString value = get0(name);
/* 255 */     return (value != null) ? Character.valueOf(CharSequenceValueConverter.INSTANCE.convertToChar((CharSequence)value)) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public char getChar(CharSequence name, char defaultValue) {
/* 260 */     Character value = getChar(name);
/* 261 */     return (value != null) ? value.charValue() : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public Short getShort(CharSequence name) {
/* 266 */     AsciiString value = get0(name);
/* 267 */     return (value != null) ? Short.valueOf(CharSequenceValueConverter.INSTANCE.convertToShort((CharSequence)value)) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public short getShort(CharSequence name, short defaultValue) {
/* 272 */     Short value = getShort(name);
/* 273 */     return (value != null) ? value.shortValue() : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getInt(CharSequence name) {
/* 278 */     AsciiString value = get0(name);
/* 279 */     return (value != null) ? Integer.valueOf(CharSequenceValueConverter.INSTANCE.convertToInt((CharSequence)value)) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInt(CharSequence name, int defaultValue) {
/* 284 */     Integer value = getInt(name);
/* 285 */     return (value != null) ? value.intValue() : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public Long getLong(CharSequence name) {
/* 290 */     AsciiString value = get0(name);
/* 291 */     return (value != null) ? Long.valueOf(CharSequenceValueConverter.INSTANCE.convertToLong((CharSequence)value)) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLong(CharSequence name, long defaultValue) {
/* 296 */     Long value = getLong(name);
/* 297 */     return (value != null) ? value.longValue() : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public Float getFloat(CharSequence name) {
/* 302 */     AsciiString value = get0(name);
/* 303 */     return (value != null) ? Float.valueOf(CharSequenceValueConverter.INSTANCE.convertToFloat((CharSequence)value)) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getFloat(CharSequence name, float defaultValue) {
/* 308 */     Float value = getFloat(name);
/* 309 */     return (value != null) ? value.floatValue() : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public Double getDouble(CharSequence name) {
/* 314 */     AsciiString value = get0(name);
/* 315 */     return (value != null) ? Double.valueOf(CharSequenceValueConverter.INSTANCE.convertToDouble((CharSequence)value)) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getDouble(CharSequence name, double defaultValue) {
/* 320 */     Double value = getDouble(name);
/* 321 */     return (value != null) ? value.doubleValue() : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public Long getTimeMillis(CharSequence name) {
/* 326 */     AsciiString value = get0(name);
/* 327 */     return (value != null) ? Long.valueOf(CharSequenceValueConverter.INSTANCE.convertToTimeMillis((CharSequence)value)) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getTimeMillis(CharSequence name, long defaultValue) {
/* 332 */     Long value = getTimeMillis(name);
/* 333 */     return (value != null) ? value.longValue() : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean getBooleanAndRemove(CharSequence name) {
/* 338 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getBooleanAndRemove(CharSequence name, boolean defaultValue) {
/* 343 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Byte getByteAndRemove(CharSequence name) {
/* 348 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getByteAndRemove(CharSequence name, byte defaultValue) {
/* 353 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Character getCharAndRemove(CharSequence name) {
/* 358 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public char getCharAndRemove(CharSequence name, char defaultValue) {
/* 363 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Short getShortAndRemove(CharSequence name) {
/* 368 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public short getShortAndRemove(CharSequence name, short defaultValue) {
/* 373 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getIntAndRemove(CharSequence name) {
/* 378 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIntAndRemove(CharSequence name, int defaultValue) {
/* 383 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Long getLongAndRemove(CharSequence name) {
/* 388 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLongAndRemove(CharSequence name, long defaultValue) {
/* 393 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Float getFloatAndRemove(CharSequence name) {
/* 398 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public float getFloatAndRemove(CharSequence name, float defaultValue) {
/* 403 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Double getDoubleAndRemove(CharSequence name) {
/* 408 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public double getDoubleAndRemove(CharSequence name, double defaultValue) {
/* 413 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Long getTimeMillisAndRemove(CharSequence name) {
/* 418 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public long getTimeMillisAndRemove(CharSequence name, long defaultValue) {
/* 423 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(CharSequence name) {
/* 428 */     return (get(name) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(CharSequence name, CharSequence value) {
/* 433 */     return contains(name, value, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsObject(CharSequence name, Object value) {
/* 438 */     if (value instanceof CharSequence) {
/* 439 */       return contains(name, (CharSequence)value);
/*     */     }
/* 441 */     return contains(name, value.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsBoolean(CharSequence name, boolean value) {
/* 446 */     return contains(name, String.valueOf(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsByte(CharSequence name, byte value) {
/* 451 */     return contains(name, String.valueOf(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsChar(CharSequence name, char value) {
/* 456 */     return contains(name, String.valueOf(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsShort(CharSequence name, short value) {
/* 461 */     return contains(name, String.valueOf(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsInt(CharSequence name, int value) {
/* 466 */     return contains(name, String.valueOf(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsLong(CharSequence name, long value) {
/* 471 */     return contains(name, String.valueOf(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsFloat(CharSequence name, float value) {
/* 476 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsDouble(CharSequence name, double value) {
/* 481 */     return contains(name, String.valueOf(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsTimeMillis(CharSequence name, long value) {
/* 486 */     return contains(name, String.valueOf(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 491 */     return this.pseudoHeaders.length + this.otherHeaders.length >>> 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 496 */     return (this.pseudoHeaders.length == 0 && this.otherHeaders.length == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<CharSequence> names() {
/* 501 */     if (isEmpty()) {
/* 502 */       return Collections.emptySet();
/*     */     }
/* 504 */     Set<CharSequence> names = new LinkedHashSet<CharSequence>(size());
/* 505 */     int pseudoHeadersEnd = this.pseudoHeaders.length - 1;
/* 506 */     for (int i = 0; i < pseudoHeadersEnd; i += 2) {
/* 507 */       names.add(this.pseudoHeaders[i]);
/*     */     }
/*     */     
/* 510 */     int otherHeadersEnd = this.otherHeaders.length - 1;
/* 511 */     for (int j = 0; j < otherHeadersEnd; j += 2) {
/* 512 */       names.add(this.otherHeaders[j]);
/*     */     }
/* 514 */     return names;
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers add(CharSequence name, CharSequence value) {
/* 519 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers add(CharSequence name, Iterable<? extends CharSequence> values) {
/* 524 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers add(CharSequence name, CharSequence... values) {
/* 529 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers addObject(CharSequence name, Object value) {
/* 534 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers addObject(CharSequence name, Iterable<?> values) {
/* 539 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers addObject(CharSequence name, Object... values) {
/* 544 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers addBoolean(CharSequence name, boolean value) {
/* 549 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers addByte(CharSequence name, byte value) {
/* 554 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers addChar(CharSequence name, char value) {
/* 559 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers addShort(CharSequence name, short value) {
/* 564 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers addInt(CharSequence name, int value) {
/* 569 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers addLong(CharSequence name, long value) {
/* 574 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers addFloat(CharSequence name, float value) {
/* 579 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers addDouble(CharSequence name, double value) {
/* 584 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers addTimeMillis(CharSequence name, long value) {
/* 589 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers add(Headers<? extends CharSequence, ? extends CharSequence, ?> headers) {
/* 594 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers set(CharSequence name, CharSequence value) {
/* 599 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers set(CharSequence name, Iterable<? extends CharSequence> values) {
/* 604 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers set(CharSequence name, CharSequence... values) {
/* 609 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers setObject(CharSequence name, Object value) {
/* 614 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers setObject(CharSequence name, Iterable<?> values) {
/* 619 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers setObject(CharSequence name, Object... values) {
/* 624 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers setBoolean(CharSequence name, boolean value) {
/* 629 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers setByte(CharSequence name, byte value) {
/* 634 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers setChar(CharSequence name, char value) {
/* 639 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers setShort(CharSequence name, short value) {
/* 644 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers setInt(CharSequence name, int value) {
/* 649 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers setLong(CharSequence name, long value) {
/* 654 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers setFloat(CharSequence name, float value) {
/* 659 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers setDouble(CharSequence name, double value) {
/* 664 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers setTimeMillis(CharSequence name, long value) {
/* 669 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers set(Headers<? extends CharSequence, ? extends CharSequence, ?> headers) {
/* 674 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers setAll(Headers<? extends CharSequence, ? extends CharSequence, ?> headers) {
/* 679 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(CharSequence name) {
/* 684 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers clear() {
/* 689 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Map.Entry<CharSequence, CharSequence>> iterator() {
/* 694 */     return new ReadOnlyIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<CharSequence> valueIterator(CharSequence name) {
/* 699 */     return new ReadOnlyValueIterator(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers method(CharSequence value) {
/* 704 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers scheme(CharSequence value) {
/* 709 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers authority(CharSequence value) {
/* 714 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers path(CharSequence value) {
/* 719 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers status(CharSequence value) {
/* 724 */     throw new UnsupportedOperationException("read only");
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence method() {
/* 729 */     return get((CharSequence)Http2Headers.PseudoHeaderName.METHOD.value());
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence scheme() {
/* 734 */     return get((CharSequence)Http2Headers.PseudoHeaderName.SCHEME.value());
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence authority() {
/* 739 */     return get((CharSequence)Http2Headers.PseudoHeaderName.AUTHORITY.value());
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence path() {
/* 744 */     return get((CharSequence)Http2Headers.PseudoHeaderName.PATH.value());
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence status() {
/* 749 */     return get((CharSequence)Http2Headers.PseudoHeaderName.STATUS.value());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(CharSequence name, CharSequence value, boolean caseInsensitive) {
/* 754 */     int nameHash = AsciiString.hashCode(name);
/* 755 */     HashingStrategy<CharSequence> strategy = caseInsensitive ? AsciiString.CASE_INSENSITIVE_HASHER : AsciiString.CASE_SENSITIVE_HASHER;
/*     */     
/* 757 */     int valueHash = strategy.hashCode(value);
/*     */     
/* 759 */     return (contains(name, nameHash, value, valueHash, strategy, this.otherHeaders) || 
/* 760 */       contains(name, nameHash, value, valueHash, strategy, this.pseudoHeaders));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean contains(CharSequence name, int nameHash, CharSequence value, int valueHash, HashingStrategy<CharSequence> hashingStrategy, AsciiString[] headers) {
/* 765 */     int headersEnd = headers.length - 1;
/* 766 */     for (int i = 0; i < headersEnd; i += 2) {
/* 767 */       AsciiString roName = headers[i];
/* 768 */       AsciiString roValue = headers[i + 1];
/* 769 */       if (roName.hashCode() == nameHash && roValue.hashCode() == valueHash && roName
/* 770 */         .contentEqualsIgnoreCase(name) && hashingStrategy.equals(roValue, value)) {
/* 771 */         return true;
/*     */       }
/*     */     } 
/* 774 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 779 */     StringBuilder builder = (new StringBuilder(getClass().getSimpleName())).append('[');
/* 780 */     String separator = "";
/* 781 */     for (Map.Entry<CharSequence, CharSequence> entry : (Iterable<Map.Entry<CharSequence, CharSequence>>)this) {
/* 782 */       builder.append(separator);
/* 783 */       builder.append(entry.getKey()).append(": ").append(entry.getValue());
/* 784 */       separator = ", ";
/*     */     } 
/* 786 */     return builder.append(']').toString();
/*     */   }
/*     */   
/*     */   private final class ReadOnlyValueIterator implements Iterator<CharSequence> {
/*     */     private int i;
/*     */     private final int nameHash;
/*     */     private final CharSequence name;
/* 793 */     private AsciiString[] current = (ReadOnlyHttp2Headers.this.pseudoHeaders.length != 0) ? ReadOnlyHttp2Headers.this.pseudoHeaders : ReadOnlyHttp2Headers.this.otherHeaders;
/*     */     private AsciiString next;
/*     */     
/*     */     ReadOnlyValueIterator(CharSequence name) {
/* 797 */       this.nameHash = AsciiString.hashCode(name);
/* 798 */       this.name = name;
/* 799 */       calculateNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 804 */       return (this.next != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public CharSequence next() {
/* 809 */       if (!hasNext()) {
/* 810 */         throw new NoSuchElementException();
/*     */       }
/* 812 */       AsciiString asciiString = this.next;
/* 813 */       calculateNext();
/* 814 */       return (CharSequence)asciiString;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 819 */       throw new UnsupportedOperationException("read only");
/*     */     }
/*     */     
/*     */     private void calculateNext() {
/* 823 */       for (; this.i < this.current.length; this.i += 2) {
/* 824 */         AsciiString roName = this.current[this.i];
/* 825 */         if (roName.hashCode() == this.nameHash && roName.contentEqualsIgnoreCase(this.name)) {
/* 826 */           this.next = this.current[this.i + 1];
/* 827 */           this.i += 2;
/*     */           return;
/*     */         } 
/*     */       } 
/* 831 */       if (this.i >= this.current.length && this.current == ReadOnlyHttp2Headers.this.pseudoHeaders) {
/* 832 */         this.i = 0;
/* 833 */         this.current = ReadOnlyHttp2Headers.this.otherHeaders;
/* 834 */         calculateNext();
/*     */       } else {
/* 836 */         this.next = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private final class ReadOnlyIterator
/*     */     implements Map.Entry<CharSequence, CharSequence>, Iterator<Map.Entry<CharSequence, CharSequence>> {
/*     */     private int i;
/* 844 */     private AsciiString[] current = (ReadOnlyHttp2Headers.this.pseudoHeaders.length != 0) ? ReadOnlyHttp2Headers.this.pseudoHeaders : ReadOnlyHttp2Headers.this.otherHeaders;
/*     */     
/*     */     private AsciiString key;
/*     */     private AsciiString value;
/*     */     
/*     */     public boolean hasNext() {
/* 850 */       return (this.i != this.current.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map.Entry<CharSequence, CharSequence> next() {
/* 855 */       if (!hasNext()) {
/* 856 */         throw new NoSuchElementException();
/*     */       }
/* 858 */       this.key = this.current[this.i];
/* 859 */       this.value = this.current[this.i + 1];
/* 860 */       this.i += 2;
/* 861 */       if (this.i == this.current.length && this.current == ReadOnlyHttp2Headers.this.pseudoHeaders) {
/* 862 */         this.current = ReadOnlyHttp2Headers.this.otherHeaders;
/* 863 */         this.i = 0;
/*     */       } 
/* 865 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public CharSequence getKey() {
/* 870 */       return (CharSequence)this.key;
/*     */     }
/*     */ 
/*     */     
/*     */     public CharSequence getValue() {
/* 875 */       return (CharSequence)this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public CharSequence setValue(CharSequence value) {
/* 880 */       throw new UnsupportedOperationException("read only");
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 885 */       throw new UnsupportedOperationException("read only");
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 890 */       return this.key.toString() + '=' + this.value.toString();
/*     */     }
/*     */     
/*     */     private ReadOnlyIterator() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\ReadOnlyHttp2Headers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */