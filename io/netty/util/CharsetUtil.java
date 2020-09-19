/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.internal.InternalThreadLocalMap;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CodingErrorAction;
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
/*     */ public final class CharsetUtil
/*     */ {
/*  37 */   public static final Charset UTF_16 = Charset.forName("UTF-16");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   public static final Charset UTF_16BE = Charset.forName("UTF-16BE");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   public static final Charset UTF_8 = Charset.forName("UTF-8");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   public static final Charset US_ASCII = Charset.forName("US-ASCII");
/*     */   
/*  65 */   private static final Charset[] CHARSETS = new Charset[] { UTF_16, UTF_16BE, UTF_16LE, UTF_8, ISO_8859_1, US_ASCII };
/*     */   
/*     */   public static Charset[] values() {
/*  68 */     return CHARSETS;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static CharsetEncoder getEncoder(Charset charset) {
/*  75 */     return encoder(charset);
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
/*     */   public static CharsetEncoder encoder(Charset charset, CodingErrorAction malformedInputAction, CodingErrorAction unmappableCharacterAction) {
/*  88 */     ObjectUtil.checkNotNull(charset, "charset");
/*  89 */     CharsetEncoder e = charset.newEncoder();
/*  90 */     e.onMalformedInput(malformedInputAction).onUnmappableCharacter(unmappableCharacterAction);
/*  91 */     return e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharsetEncoder encoder(Charset charset, CodingErrorAction codingErrorAction) {
/* 102 */     return encoder(charset, codingErrorAction, codingErrorAction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharsetEncoder encoder(Charset charset) {
/* 112 */     ObjectUtil.checkNotNull(charset, "charset");
/*     */     
/* 114 */     Map<Charset, CharsetEncoder> map = InternalThreadLocalMap.get().charsetEncoderCache();
/* 115 */     CharsetEncoder e = map.get(charset);
/* 116 */     if (e != null) {
/* 117 */       e.reset().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
/* 118 */       return e;
/*     */     } 
/*     */     
/* 121 */     e = encoder(charset, CodingErrorAction.REPLACE, CodingErrorAction.REPLACE);
/* 122 */     map.put(charset, e);
/* 123 */     return e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static CharsetDecoder getDecoder(Charset charset) {
/* 131 */     return decoder(charset);
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
/*     */   public static CharsetDecoder decoder(Charset charset, CodingErrorAction malformedInputAction, CodingErrorAction unmappableCharacterAction) {
/* 144 */     ObjectUtil.checkNotNull(charset, "charset");
/* 145 */     CharsetDecoder d = charset.newDecoder();
/* 146 */     d.onMalformedInput(malformedInputAction).onUnmappableCharacter(unmappableCharacterAction);
/* 147 */     return d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharsetDecoder decoder(Charset charset, CodingErrorAction codingErrorAction) {
/* 158 */     return decoder(charset, codingErrorAction, codingErrorAction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharsetDecoder decoder(Charset charset) {
/* 168 */     ObjectUtil.checkNotNull(charset, "charset");
/*     */     
/* 170 */     Map<Charset, CharsetDecoder> map = InternalThreadLocalMap.get().charsetDecoderCache();
/* 171 */     CharsetDecoder d = map.get(charset);
/* 172 */     if (d != null) {
/* 173 */       d.reset().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
/* 174 */       return d;
/*     */     } 
/*     */     
/* 177 */     d = decoder(charset, CodingErrorAction.REPLACE, CodingErrorAction.REPLACE);
/* 178 */     map.put(charset, d);
/* 179 */     return d;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\CharsetUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */