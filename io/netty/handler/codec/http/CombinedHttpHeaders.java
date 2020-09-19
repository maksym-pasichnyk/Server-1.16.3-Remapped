/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.handler.codec.DefaultHeaders;
/*     */ import io.netty.handler.codec.Headers;
/*     */ import io.netty.handler.codec.ValueConverter;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.HashingStrategy;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public class CombinedHttpHeaders
/*     */   extends DefaultHttpHeaders
/*     */ {
/*     */   public CombinedHttpHeaders(boolean validate) {
/*  40 */     super(new CombinedHttpHeadersImpl(AsciiString.CASE_INSENSITIVE_HASHER, valueConverter(validate), nameValidator(validate)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(CharSequence name, CharSequence value, boolean ignoreCase) {
/*  45 */     return super.containsValue(name, StringUtil.trimOws(value), ignoreCase);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class CombinedHttpHeadersImpl
/*     */     extends DefaultHeaders<CharSequence, CharSequence, CombinedHttpHeadersImpl>
/*     */   {
/*     */     private static final int VALUE_LENGTH_ESTIMATE = 10;
/*     */     
/*     */     private CsvValueEscaper<Object> objectEscaper;
/*     */     private CsvValueEscaper<CharSequence> charSequenceEscaper;
/*     */     
/*     */     private CsvValueEscaper<Object> objectEscaper() {
/*  58 */       if (this.objectEscaper == null) {
/*  59 */         this.objectEscaper = new CsvValueEscaper()
/*     */           {
/*     */             public CharSequence escape(Object value) {
/*  62 */               return StringUtil.escapeCsv((CharSequence)CombinedHttpHeaders.CombinedHttpHeadersImpl.this.valueConverter().convertObject(value), true);
/*     */             }
/*     */           };
/*     */       }
/*  66 */       return this.objectEscaper;
/*     */     }
/*     */     
/*     */     private CsvValueEscaper<CharSequence> charSequenceEscaper() {
/*  70 */       if (this.charSequenceEscaper == null) {
/*  71 */         this.charSequenceEscaper = new CsvValueEscaper<CharSequence>()
/*     */           {
/*     */             public CharSequence escape(CharSequence value) {
/*  74 */               return StringUtil.escapeCsv(value, true);
/*     */             }
/*     */           };
/*     */       }
/*  78 */       return this.charSequenceEscaper;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public CombinedHttpHeadersImpl(HashingStrategy<CharSequence> nameHashingStrategy, ValueConverter<CharSequence> valueConverter, DefaultHeaders.NameValidator<CharSequence> nameValidator) {
/*  84 */       super(nameHashingStrategy, valueConverter, nameValidator);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<CharSequence> valueIterator(CharSequence name) {
/*  89 */       Iterator<CharSequence> itr = super.valueIterator(name);
/*  90 */       if (!itr.hasNext()) {
/*  91 */         return itr;
/*     */       }
/*  93 */       Iterator<CharSequence> unescapedItr = StringUtil.unescapeCsvFields(itr.next()).iterator();
/*  94 */       if (itr.hasNext()) {
/*  95 */         throw new IllegalStateException("CombinedHttpHeaders should only have one value");
/*     */       }
/*  97 */       return unescapedItr;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<CharSequence> getAll(CharSequence name) {
/* 102 */       List<CharSequence> values = super.getAll(name);
/* 103 */       if (values.isEmpty()) {
/* 104 */         return values;
/*     */       }
/* 106 */       if (values.size() != 1) {
/* 107 */         throw new IllegalStateException("CombinedHttpHeaders should only have one value");
/*     */       }
/* 109 */       return StringUtil.unescapeCsvFields(values.get(0));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public CombinedHttpHeadersImpl add(Headers<? extends CharSequence, ? extends CharSequence, ?> headers) {
/* 115 */       if (headers == this) {
/* 116 */         throw new IllegalArgumentException("can't add to itself.");
/*     */       }
/* 118 */       if (headers instanceof CombinedHttpHeadersImpl) {
/* 119 */         if (isEmpty()) {
/*     */           
/* 121 */           addImpl(headers);
/*     */         } else {
/*     */           
/* 124 */           for (Map.Entry<? extends CharSequence, ? extends CharSequence> header : headers) {
/* 125 */             addEscapedValue(header.getKey(), header.getValue());
/*     */           }
/*     */         } 
/*     */       } else {
/* 129 */         for (Map.Entry<? extends CharSequence, ? extends CharSequence> header : headers) {
/* 130 */           add(header.getKey(), header.getValue());
/*     */         }
/*     */       } 
/* 133 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public CombinedHttpHeadersImpl set(Headers<? extends CharSequence, ? extends CharSequence, ?> headers) {
/* 138 */       if (headers == this) {
/* 139 */         return this;
/*     */       }
/* 141 */       clear();
/* 142 */       return add(headers);
/*     */     }
/*     */ 
/*     */     
/*     */     public CombinedHttpHeadersImpl setAll(Headers<? extends CharSequence, ? extends CharSequence, ?> headers) {
/* 147 */       if (headers == this) {
/* 148 */         return this;
/*     */       }
/* 150 */       for (CharSequence key : headers.names()) {
/* 151 */         remove(key);
/*     */       }
/* 153 */       return add(headers);
/*     */     }
/*     */ 
/*     */     
/*     */     public CombinedHttpHeadersImpl add(CharSequence name, CharSequence value) {
/* 158 */       return addEscapedValue(name, charSequenceEscaper().escape(value));
/*     */     }
/*     */ 
/*     */     
/*     */     public CombinedHttpHeadersImpl add(CharSequence name, CharSequence... values) {
/* 163 */       return addEscapedValue(name, commaSeparate(charSequenceEscaper(), values));
/*     */     }
/*     */ 
/*     */     
/*     */     public CombinedHttpHeadersImpl add(CharSequence name, Iterable<? extends CharSequence> values) {
/* 168 */       return addEscapedValue(name, commaSeparate(charSequenceEscaper(), values));
/*     */     }
/*     */ 
/*     */     
/*     */     public CombinedHttpHeadersImpl addObject(CharSequence name, Object value) {
/* 173 */       return addEscapedValue(name, commaSeparate(objectEscaper(), new Object[] { value }));
/*     */     }
/*     */ 
/*     */     
/*     */     public CombinedHttpHeadersImpl addObject(CharSequence name, Iterable<?> values) {
/* 178 */       return addEscapedValue(name, commaSeparate(objectEscaper(), values));
/*     */     }
/*     */ 
/*     */     
/*     */     public CombinedHttpHeadersImpl addObject(CharSequence name, Object... values) {
/* 183 */       return addEscapedValue(name, commaSeparate(objectEscaper(), values));
/*     */     }
/*     */ 
/*     */     
/*     */     public CombinedHttpHeadersImpl set(CharSequence name, CharSequence... values) {
/* 188 */       set(name, commaSeparate(charSequenceEscaper(), values));
/* 189 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public CombinedHttpHeadersImpl set(CharSequence name, Iterable<? extends CharSequence> values) {
/* 194 */       set(name, commaSeparate(charSequenceEscaper(), values));
/* 195 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public CombinedHttpHeadersImpl setObject(CharSequence name, Object value) {
/* 200 */       set(name, commaSeparate(objectEscaper(), new Object[] { value }));
/* 201 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public CombinedHttpHeadersImpl setObject(CharSequence name, Object... values) {
/* 206 */       set(name, commaSeparate(objectEscaper(), values));
/* 207 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public CombinedHttpHeadersImpl setObject(CharSequence name, Iterable<?> values) {
/* 212 */       set(name, commaSeparate(objectEscaper(), values));
/* 213 */       return this;
/*     */     }
/*     */     
/*     */     private CombinedHttpHeadersImpl addEscapedValue(CharSequence name, CharSequence escapedValue) {
/* 217 */       CharSequence currentValue = (CharSequence)get(name);
/* 218 */       if (currentValue == null) {
/* 219 */         super.add(name, escapedValue);
/*     */       } else {
/* 221 */         set(name, commaSeparateEscapedValues(currentValue, escapedValue));
/*     */       } 
/* 223 */       return this;
/*     */     }
/*     */     
/*     */     private static <T> CharSequence commaSeparate(CsvValueEscaper<T> escaper, T... values) {
/* 227 */       StringBuilder sb = new StringBuilder(values.length * 10);
/* 228 */       if (values.length > 0) {
/* 229 */         int end = values.length - 1;
/* 230 */         for (int i = 0; i < end; i++) {
/* 231 */           sb.append(escaper.escape(values[i])).append(',');
/*     */         }
/* 233 */         sb.append(escaper.escape(values[end]));
/*     */       } 
/* 235 */       return sb;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static <T> CharSequence commaSeparate(CsvValueEscaper<T> escaper, Iterable<? extends T> values) {
/* 241 */       StringBuilder sb = (values instanceof Collection) ? new StringBuilder(((Collection)values).size() * 10) : new StringBuilder();
/* 242 */       Iterator<? extends T> iterator = values.iterator();
/* 243 */       if (iterator.hasNext()) {
/* 244 */         T next = iterator.next();
/* 245 */         while (iterator.hasNext()) {
/* 246 */           sb.append(escaper.escape(next)).append(',');
/* 247 */           next = iterator.next();
/*     */         } 
/* 249 */         sb.append(escaper.escape(next));
/*     */       } 
/* 251 */       return sb;
/*     */     }
/*     */     
/*     */     private static CharSequence commaSeparateEscapedValues(CharSequence currentValue, CharSequence value) {
/* 255 */       return (new StringBuilder(currentValue.length() + 1 + value.length()))
/* 256 */         .append(currentValue)
/* 257 */         .append(',')
/* 258 */         .append(value);
/*     */     }
/*     */     
/*     */     private static interface CsvValueEscaper<T> {
/*     */       CharSequence escape(T param2T);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\CombinedHttpHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */