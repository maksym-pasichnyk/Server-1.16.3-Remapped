/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Http2HeadersEncoder
/*     */ {
/*  93 */   public static final SensitivityDetector NEVER_SENSITIVE = new SensitivityDetector()
/*     */     {
/*     */       public boolean isSensitive(CharSequence name, CharSequence value) {
/*  96 */         return false;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public static final SensitivityDetector ALWAYS_SENSITIVE = new SensitivityDetector()
/*     */     {
/*     */       public boolean isSensitive(CharSequence name, CharSequence value) {
/* 106 */         return true;
/*     */       }
/*     */     };
/*     */   
/*     */   void encodeHeaders(int paramInt, Http2Headers paramHttp2Headers, ByteBuf paramByteBuf) throws Http2Exception;
/*     */   
/*     */   Configuration configuration();
/*     */   
/*     */   public static interface Configuration {
/*     */     void maxHeaderTableSize(long param1Long) throws Http2Exception;
/*     */     
/*     */     long maxHeaderTableSize();
/*     */     
/*     */     void maxHeaderListSize(long param1Long) throws Http2Exception;
/*     */     
/*     */     long maxHeaderListSize();
/*     */   }
/*     */   
/*     */   public static interface SensitivityDetector {
/*     */     boolean isSensitive(CharSequence param1CharSequence1, CharSequence param1CharSequence2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2HeadersEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */