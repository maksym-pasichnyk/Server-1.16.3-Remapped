/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.handler.codec.Headers;
/*     */ import io.netty.util.AsciiString;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ public interface Http2Headers
/*     */   extends Headers<CharSequence, CharSequence, Http2Headers>
/*     */ {
/*     */   Iterator<Map.Entry<CharSequence, CharSequence>> iterator();
/*     */   
/*     */   Iterator<CharSequence> valueIterator(CharSequence paramCharSequence);
/*     */   
/*     */   Http2Headers method(CharSequence paramCharSequence);
/*     */   
/*     */   Http2Headers scheme(CharSequence paramCharSequence);
/*     */   
/*     */   Http2Headers authority(CharSequence paramCharSequence);
/*     */   
/*     */   Http2Headers path(CharSequence paramCharSequence);
/*     */   
/*     */   Http2Headers status(CharSequence paramCharSequence);
/*     */   
/*     */   CharSequence method();
/*     */   
/*     */   CharSequence scheme();
/*     */   
/*     */   CharSequence authority();
/*     */   
/*     */   CharSequence path();
/*     */   
/*     */   CharSequence status();
/*     */   
/*     */   boolean contains(CharSequence paramCharSequence1, CharSequence paramCharSequence2, boolean paramBoolean);
/*     */   
/*     */   public enum PseudoHeaderName {
/*  38 */     METHOD(":method", true),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  43 */     SCHEME(":scheme", true),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  48 */     AUTHORITY(":authority", true),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  53 */     PATH(":path", true),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  58 */     STATUS(":status", false);
/*     */     
/*     */     private static final char PSEUDO_HEADER_PREFIX = ':';
/*     */     
/*     */     private static final byte PSEUDO_HEADER_PREFIX_BYTE = 58;
/*     */     private final AsciiString value;
/*     */     private final boolean requestOnly;
/*  65 */     private static final CharSequenceMap<PseudoHeaderName> PSEUDO_HEADERS = new CharSequenceMap<PseudoHeaderName>();
/*     */     
/*     */     static {
/*  68 */       for (PseudoHeaderName pseudoHeader : values()) {
/*  69 */         PSEUDO_HEADERS.add(pseudoHeader.value(), pseudoHeader);
/*     */       }
/*     */     }
/*     */     
/*     */     PseudoHeaderName(String value, boolean requestOnly) {
/*  74 */       this.value = AsciiString.cached(value);
/*  75 */       this.requestOnly = requestOnly;
/*     */     }
/*     */ 
/*     */     
/*     */     public AsciiString value() {
/*  80 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static boolean hasPseudoHeaderFormat(CharSequence headerName) {
/*  89 */       if (headerName instanceof AsciiString) {
/*  90 */         AsciiString asciiHeaderName = (AsciiString)headerName;
/*  91 */         return (asciiHeaderName.length() > 0 && asciiHeaderName.byteAt(0) == 58);
/*     */       } 
/*  93 */       return (headerName.length() > 0 && headerName.charAt(0) == ':');
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static boolean isPseudoHeader(CharSequence header) {
/* 101 */       return PSEUDO_HEADERS.contains(header);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static PseudoHeaderName getPseudoHeader(CharSequence header) {
/* 110 */       return (PseudoHeaderName)PSEUDO_HEADERS.get(header);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isRequestOnly() {
/* 119 */       return this.requestOnly;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2Headers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */