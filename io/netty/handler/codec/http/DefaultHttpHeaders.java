/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.handler.codec.CharSequenceValueConverter;
/*     */ import io.netty.handler.codec.DateFormatter;
/*     */ import io.netty.handler.codec.DefaultHeaders;
/*     */ import io.netty.handler.codec.DefaultHeadersImpl;
/*     */ import io.netty.handler.codec.Headers;
/*     */ import io.netty.handler.codec.HeadersUtils;
/*     */ import io.netty.handler.codec.ValueConverter;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.ByteProcessor;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class DefaultHttpHeaders
/*     */   extends HttpHeaders
/*     */ {
/*     */   private static final int HIGHEST_INVALID_VALUE_CHAR_MASK = -16;
/*     */   
/*  47 */   private static final ByteProcessor HEADER_NAME_VALIDATOR = new ByteProcessor()
/*     */     {
/*     */       public boolean process(byte value) throws Exception {
/*  50 */         DefaultHttpHeaders.validateHeaderNameElement(value);
/*  51 */         return true;
/*     */       }
/*     */     };
/*  54 */   static final DefaultHeaders.NameValidator<CharSequence> HttpNameValidator = new DefaultHeaders.NameValidator<CharSequence>()
/*     */     {
/*     */       public void validateName(CharSequence name) {
/*  57 */         if (name == null || name.length() == 0) {
/*  58 */           throw new IllegalArgumentException("empty headers are not allowed [" + name + "]");
/*     */         }
/*  60 */         if (name instanceof AsciiString) {
/*     */           try {
/*  62 */             ((AsciiString)name).forEachByte(DefaultHttpHeaders.HEADER_NAME_VALIDATOR);
/*  63 */           } catch (Exception e) {
/*  64 */             PlatformDependent.throwException(e);
/*     */           } 
/*     */         } else {
/*     */           
/*  68 */           for (int index = 0; index < name.length(); index++) {
/*  69 */             DefaultHttpHeaders.validateHeaderNameElement(name.charAt(index));
/*     */           }
/*     */         } 
/*     */       }
/*     */     };
/*     */   
/*     */   private final DefaultHeaders<CharSequence, CharSequence, ?> headers;
/*     */   
/*     */   public DefaultHttpHeaders() {
/*  78 */     this(true);
/*     */   }
/*     */   
/*     */   public DefaultHttpHeaders(boolean validate) {
/*  82 */     this(validate, nameValidator(validate));
/*     */   }
/*     */   
/*     */   protected DefaultHttpHeaders(boolean validate, DefaultHeaders.NameValidator<CharSequence> nameValidator) {
/*  86 */     this((DefaultHeaders<CharSequence, CharSequence, ?>)new DefaultHeadersImpl(AsciiString.CASE_INSENSITIVE_HASHER, 
/*  87 */           valueConverter(validate), nameValidator));
/*     */   }
/*     */ 
/*     */   
/*     */   protected DefaultHttpHeaders(DefaultHeaders<CharSequence, CharSequence, ?> headers) {
/*  92 */     this.headers = headers;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders add(HttpHeaders headers) {
/*  97 */     if (headers instanceof DefaultHttpHeaders) {
/*  98 */       this.headers.add((Headers)((DefaultHttpHeaders)headers).headers);
/*  99 */       return this;
/*     */     } 
/* 101 */     return super.add(headers);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHeaders set(HttpHeaders headers) {
/* 107 */     if (headers instanceof DefaultHttpHeaders) {
/* 108 */       this.headers.set((Headers)((DefaultHttpHeaders)headers).headers);
/* 109 */       return this;
/*     */     } 
/* 111 */     return super.set(headers);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHeaders add(String name, Object value) {
/* 117 */     this.headers.addObject(name, value);
/* 118 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders add(CharSequence name, Object value) {
/* 123 */     this.headers.addObject(name, value);
/* 124 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders add(String name, Iterable<?> values) {
/* 129 */     this.headers.addObject(name, values);
/* 130 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders add(CharSequence name, Iterable<?> values) {
/* 135 */     this.headers.addObject(name, values);
/* 136 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders addInt(CharSequence name, int value) {
/* 141 */     this.headers.addInt(name, value);
/* 142 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders addShort(CharSequence name, short value) {
/* 147 */     this.headers.addShort(name, value);
/* 148 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders remove(String name) {
/* 153 */     this.headers.remove(name);
/* 154 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders remove(CharSequence name) {
/* 159 */     this.headers.remove(name);
/* 160 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders set(String name, Object value) {
/* 165 */     this.headers.setObject(name, value);
/* 166 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders set(CharSequence name, Object value) {
/* 171 */     this.headers.setObject(name, value);
/* 172 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders set(String name, Iterable<?> values) {
/* 177 */     this.headers.setObject(name, values);
/* 178 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders set(CharSequence name, Iterable<?> values) {
/* 183 */     this.headers.setObject(name, values);
/* 184 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders setInt(CharSequence name, int value) {
/* 189 */     this.headers.setInt(name, value);
/* 190 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders setShort(CharSequence name, short value) {
/* 195 */     this.headers.setShort(name, value);
/* 196 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders clear() {
/* 201 */     this.headers.clear();
/* 202 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String get(String name) {
/* 207 */     return get(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public String get(CharSequence name) {
/* 212 */     return HeadersUtils.getAsString((Headers)this.headers, name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getInt(CharSequence name) {
/* 217 */     return this.headers.getInt(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInt(CharSequence name, int defaultValue) {
/* 222 */     return this.headers.getInt(name, defaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public Short getShort(CharSequence name) {
/* 227 */     return this.headers.getShort(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public short getShort(CharSequence name, short defaultValue) {
/* 232 */     return this.headers.getShort(name, defaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public Long getTimeMillis(CharSequence name) {
/* 237 */     return this.headers.getTimeMillis(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getTimeMillis(CharSequence name, long defaultValue) {
/* 242 */     return this.headers.getTimeMillis(name, defaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getAll(String name) {
/* 247 */     return getAll(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getAll(CharSequence name) {
/* 252 */     return HeadersUtils.getAllAsString((Headers)this.headers, name);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Map.Entry<String, String>> entries() {
/* 257 */     if (isEmpty()) {
/* 258 */       return Collections.emptyList();
/*     */     }
/*     */     
/* 261 */     List<Map.Entry<String, String>> entriesConverted = new ArrayList<Map.Entry<String, String>>(this.headers.size());
/* 262 */     for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)this) {
/* 263 */       entriesConverted.add(entry);
/*     */     }
/* 265 */     return entriesConverted;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Iterator<Map.Entry<String, String>> iterator() {
/* 271 */     return HeadersUtils.iteratorAsString((Iterable)this.headers);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Map.Entry<CharSequence, CharSequence>> iteratorCharSequence() {
/* 276 */     return this.headers.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<String> valueStringIterator(CharSequence name) {
/* 281 */     final Iterator<CharSequence> itr = valueCharSequenceIterator(name);
/* 282 */     return new Iterator<String>()
/*     */       {
/*     */         public boolean hasNext() {
/* 285 */           return itr.hasNext();
/*     */         }
/*     */ 
/*     */         
/*     */         public String next() {
/* 290 */           return ((CharSequence)itr.next()).toString();
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 295 */           itr.remove();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<CharSequence> valueCharSequenceIterator(CharSequence name) {
/* 302 */     return this.headers.valueIterator(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(String name) {
/* 307 */     return contains(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(CharSequence name) {
/* 312 */     return this.headers.contains(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 317 */     return this.headers.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 322 */     return this.headers.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(String name, String value, boolean ignoreCase) {
/* 327 */     return contains(name, value, ignoreCase);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(CharSequence name, CharSequence value, boolean ignoreCase) {
/* 332 */     return this.headers.contains(name, value, ignoreCase ? AsciiString.CASE_INSENSITIVE_HASHER : AsciiString.CASE_SENSITIVE_HASHER);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> names() {
/* 337 */     return HeadersUtils.namesAsString((Headers)this.headers);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 342 */     return (o instanceof DefaultHttpHeaders && this.headers
/* 343 */       .equals((Headers)((DefaultHttpHeaders)o).headers, AsciiString.CASE_SENSITIVE_HASHER));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 348 */     return this.headers.hashCode(AsciiString.CASE_SENSITIVE_HASHER);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders copy() {
/* 353 */     return new DefaultHttpHeaders(this.headers.copy());
/*     */   }
/*     */   
/*     */   private static void validateHeaderNameElement(byte value) {
/* 357 */     switch (value) {
/*     */       case 0:
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       case 12:
/*     */       case 13:
/*     */       case 32:
/*     */       case 44:
/*     */       case 58:
/*     */       case 59:
/*     */       case 61:
/* 369 */         throw new IllegalArgumentException("a header name cannot contain the following prohibited characters: =,;: \\t\\r\\n\\v\\f: " + value);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 374 */     if (value < 0) {
/* 375 */       throw new IllegalArgumentException("a header name cannot contain non-ASCII character: " + value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void validateHeaderNameElement(char value) {
/* 382 */     switch (value) {
/*     */       case '\000':
/*     */       case '\t':
/*     */       case '\n':
/*     */       case '\013':
/*     */       case '\f':
/*     */       case '\r':
/*     */       case ' ':
/*     */       case ',':
/*     */       case ':':
/*     */       case ';':
/*     */       case '=':
/* 394 */         throw new IllegalArgumentException("a header name cannot contain the following prohibited characters: =,;: \\t\\r\\n\\v\\f: " + value);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 399 */     if (value > '') {
/* 400 */       throw new IllegalArgumentException("a header name cannot contain non-ASCII character: " + value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static ValueConverter<CharSequence> valueConverter(boolean validate) {
/* 407 */     return validate ? (ValueConverter<CharSequence>)HeaderValueConverterAndValidator.INSTANCE : (ValueConverter<CharSequence>)HeaderValueConverter.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   static DefaultHeaders.NameValidator<CharSequence> nameValidator(boolean validate) {
/* 412 */     return validate ? HttpNameValidator : DefaultHeaders.NameValidator.NOT_NULL;
/*     */   }
/*     */   
/*     */   private static class HeaderValueConverter extends CharSequenceValueConverter {
/* 416 */     static final HeaderValueConverter INSTANCE = new HeaderValueConverter();
/*     */     private HeaderValueConverter() {}
/*     */     
/*     */     public CharSequence convertObject(Object value) {
/* 420 */       if (value instanceof CharSequence) {
/* 421 */         return (CharSequence)value;
/*     */       }
/* 423 */       if (value instanceof Date) {
/* 424 */         return DateFormatter.format((Date)value);
/*     */       }
/* 426 */       if (value instanceof Calendar) {
/* 427 */         return DateFormatter.format(((Calendar)value).getTime());
/*     */       }
/* 429 */       return value.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class HeaderValueConverterAndValidator extends HeaderValueConverter {
/* 434 */     static final HeaderValueConverterAndValidator INSTANCE = new HeaderValueConverterAndValidator();
/*     */ 
/*     */     
/*     */     public CharSequence convertObject(Object value) {
/* 438 */       CharSequence seq = super.convertObject(value);
/* 439 */       int state = 0;
/*     */       
/* 441 */       for (int index = 0; index < seq.length(); index++) {
/* 442 */         state = validateValueChar(seq, state, seq.charAt(index));
/*     */       }
/*     */       
/* 445 */       if (state != 0) {
/* 446 */         throw new IllegalArgumentException("a header value must not end with '\\r' or '\\n':" + seq);
/*     */       }
/* 448 */       return seq;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static int validateValueChar(CharSequence seq, int state, char character) {
/* 458 */       if ((character & 0xFFFFFFF0) == 0)
/*     */       {
/* 460 */         switch (character) {
/*     */           case '\000':
/* 462 */             throw new IllegalArgumentException("a header value contains a prohibited character '\000': " + seq);
/*     */           case '\013':
/* 464 */             throw new IllegalArgumentException("a header value contains a prohibited character '\\v': " + seq);
/*     */           case '\f':
/* 466 */             throw new IllegalArgumentException("a header value contains a prohibited character '\\f': " + seq);
/*     */         } 
/*     */ 
/*     */       
/*     */       }
/* 471 */       switch (state) {
/*     */         case 0:
/* 473 */           switch (character) {
/*     */             case '\r':
/* 475 */               return 1;
/*     */             case '\n':
/* 477 */               return 2;
/*     */           } 
/*     */           break;
/*     */         case 1:
/* 481 */           switch (character) {
/*     */             case '\n':
/* 483 */               return 2;
/*     */           } 
/* 485 */           throw new IllegalArgumentException("only '\\n' is allowed after '\\r': " + seq);
/*     */         
/*     */         case 2:
/* 488 */           switch (character) {
/*     */             case '\t':
/*     */             case ' ':
/* 491 */               return 0;
/*     */           } 
/* 493 */           throw new IllegalArgumentException("only ' ' and '\\t' are allowed after '\\n': " + seq);
/*     */       } 
/*     */       
/* 496 */       return state;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\DefaultHttpHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */