/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.handler.codec.CharSequenceValueConverter;
/*     */ import io.netty.handler.codec.DefaultHeaders;
/*     */ import io.netty.handler.codec.Headers;
/*     */ import io.netty.handler.codec.ValueConverter;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.ByteProcessor;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultHttp2Headers
/*     */   extends DefaultHeaders<CharSequence, CharSequence, Http2Headers>
/*     */   implements Http2Headers
/*     */ {
/*  34 */   private static final ByteProcessor HTTP2_NAME_VALIDATOR_PROCESSOR = new ByteProcessor()
/*     */     {
/*     */       public boolean process(byte value) {
/*  37 */         return !AsciiString.isUpperCase(value);
/*     */       }
/*     */     };
/*  40 */   static final DefaultHeaders.NameValidator<CharSequence> HTTP2_NAME_VALIDATOR = new DefaultHeaders.NameValidator<CharSequence>()
/*     */     {
/*     */       public void validateName(CharSequence name) {
/*  43 */         if (name == null || name.length() == 0) {
/*  44 */           PlatformDependent.throwException(Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "empty headers are not allowed [%s]", new Object[] { name }));
/*     */         }
/*     */         
/*  47 */         if (name instanceof AsciiString) {
/*     */           int index;
/*     */           try {
/*  50 */             index = ((AsciiString)name).forEachByte(DefaultHttp2Headers.HTTP2_NAME_VALIDATOR_PROCESSOR);
/*  51 */           } catch (Http2Exception e) {
/*  52 */             PlatformDependent.throwException(e);
/*     */             return;
/*  54 */           } catch (Throwable t) {
/*  55 */             PlatformDependent.throwException(Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, t, "unexpected error. invalid header name [%s]", new Object[] { name }));
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/*  60 */           if (index != -1) {
/*  61 */             PlatformDependent.throwException(Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "invalid header name [%s]", new Object[] { name }));
/*     */           }
/*     */         } else {
/*     */           
/*  65 */           for (int i = 0; i < name.length(); i++) {
/*  66 */             if (AsciiString.isUpperCase(name.charAt(i))) {
/*  67 */               PlatformDependent.throwException(Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "invalid header name [%s]", new Object[] { name }));
/*     */             }
/*     */           } 
/*     */         } 
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  75 */   private DefaultHeaders.HeaderEntry<CharSequence, CharSequence> firstNonPseudo = this.head;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttp2Headers() {
/*  84 */     this(true);
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
/*     */   public DefaultHttp2Headers(boolean validate) {
/*  96 */     super(AsciiString.CASE_SENSITIVE_HASHER, (ValueConverter)CharSequenceValueConverter.INSTANCE, validate ? HTTP2_NAME_VALIDATOR : DefaultHeaders.NameValidator.NOT_NULL);
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
/*     */   public DefaultHttp2Headers(boolean validate, int arraySizeHint) {
/* 112 */     super(AsciiString.CASE_SENSITIVE_HASHER, (ValueConverter)CharSequenceValueConverter.INSTANCE, validate ? HTTP2_NAME_VALIDATOR : DefaultHeaders.NameValidator.NOT_NULL, arraySizeHint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2Headers clear() {
/* 120 */     this.firstNonPseudo = this.head;
/* 121 */     return (Http2Headers)super.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 126 */     return (o instanceof Http2Headers && equals((Http2Headers)o, AsciiString.CASE_SENSITIVE_HASHER));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 131 */     return hashCode(AsciiString.CASE_SENSITIVE_HASHER);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers method(CharSequence value) {
/* 136 */     set(Http2Headers.PseudoHeaderName.METHOD.value(), value);
/* 137 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers scheme(CharSequence value) {
/* 142 */     set(Http2Headers.PseudoHeaderName.SCHEME.value(), value);
/* 143 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers authority(CharSequence value) {
/* 148 */     set(Http2Headers.PseudoHeaderName.AUTHORITY.value(), value);
/* 149 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers path(CharSequence value) {
/* 154 */     set(Http2Headers.PseudoHeaderName.PATH.value(), value);
/* 155 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers status(CharSequence value) {
/* 160 */     set(Http2Headers.PseudoHeaderName.STATUS.value(), value);
/* 161 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence method() {
/* 166 */     return (CharSequence)get(Http2Headers.PseudoHeaderName.METHOD.value());
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence scheme() {
/* 171 */     return (CharSequence)get(Http2Headers.PseudoHeaderName.SCHEME.value());
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence authority() {
/* 176 */     return (CharSequence)get(Http2Headers.PseudoHeaderName.AUTHORITY.value());
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence path() {
/* 181 */     return (CharSequence)get(Http2Headers.PseudoHeaderName.PATH.value());
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence status() {
/* 186 */     return (CharSequence)get(Http2Headers.PseudoHeaderName.STATUS.value());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(CharSequence name, CharSequence value) {
/* 191 */     return contains(name, value, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(CharSequence name, CharSequence value, boolean caseInsensitive) {
/* 196 */     return contains(name, value, caseInsensitive ? AsciiString.CASE_INSENSITIVE_HASHER : AsciiString.CASE_SENSITIVE_HASHER);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final DefaultHeaders.HeaderEntry<CharSequence, CharSequence> newHeaderEntry(int h, CharSequence name, CharSequence value, DefaultHeaders.HeaderEntry<CharSequence, CharSequence> next) {
/* 202 */     return new Http2HeaderEntry(h, name, value, next);
/*     */   }
/*     */   
/*     */   private final class Http2HeaderEntry
/*     */     extends DefaultHeaders.HeaderEntry<CharSequence, CharSequence> {
/*     */     protected Http2HeaderEntry(int hash, CharSequence key, CharSequence value, DefaultHeaders.HeaderEntry<CharSequence, CharSequence> next) {
/* 208 */       super(hash, key);
/* 209 */       this.value = value;
/* 210 */       this.next = next;
/*     */ 
/*     */       
/* 213 */       if (Http2Headers.PseudoHeaderName.hasPseudoHeaderFormat(key)) {
/* 214 */         this.after = DefaultHttp2Headers.this.firstNonPseudo;
/* 215 */         this.before = DefaultHttp2Headers.this.firstNonPseudo.before();
/*     */       } else {
/* 217 */         this.after = DefaultHttp2Headers.this.head;
/* 218 */         this.before = DefaultHttp2Headers.this.head.before();
/* 219 */         if (DefaultHttp2Headers.this.firstNonPseudo == DefaultHttp2Headers.this.head) {
/* 220 */           DefaultHttp2Headers.this.firstNonPseudo = this;
/*     */         }
/*     */       } 
/* 223 */       pointNeighborsToThis();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void remove() {
/* 228 */       if (this == DefaultHttp2Headers.this.firstNonPseudo) {
/* 229 */         DefaultHttp2Headers.this.firstNonPseudo = DefaultHttp2Headers.this.firstNonPseudo.after();
/*     */       }
/* 231 */       super.remove();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\DefaultHttp2Headers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */