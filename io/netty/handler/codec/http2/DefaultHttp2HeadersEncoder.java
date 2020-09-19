/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultHttp2HeadersEncoder
/*    */   implements Http2HeadersEncoder, Http2HeadersEncoder.Configuration
/*    */ {
/*    */   private final HpackEncoder hpackEncoder;
/*    */   private final Http2HeadersEncoder.SensitivityDetector sensitivityDetector;
/* 30 */   private final ByteBuf tableSizeChangeOutput = Unpooled.buffer();
/*    */   
/*    */   public DefaultHttp2HeadersEncoder() {
/* 33 */     this(NEVER_SENSITIVE);
/*    */   }
/*    */   
/*    */   public DefaultHttp2HeadersEncoder(Http2HeadersEncoder.SensitivityDetector sensitivityDetector) {
/* 37 */     this(sensitivityDetector, new HpackEncoder());
/*    */   }
/*    */   
/*    */   public DefaultHttp2HeadersEncoder(Http2HeadersEncoder.SensitivityDetector sensitivityDetector, boolean ignoreMaxHeaderListSize) {
/* 41 */     this(sensitivityDetector, new HpackEncoder(ignoreMaxHeaderListSize));
/*    */   }
/*    */ 
/*    */   
/*    */   public DefaultHttp2HeadersEncoder(Http2HeadersEncoder.SensitivityDetector sensitivityDetector, boolean ignoreMaxHeaderListSize, int dynamicTableArraySizeHint) {
/* 46 */     this(sensitivityDetector, new HpackEncoder(ignoreMaxHeaderListSize, dynamicTableArraySizeHint));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   DefaultHttp2HeadersEncoder(Http2HeadersEncoder.SensitivityDetector sensitivityDetector, HpackEncoder hpackEncoder) {
/* 54 */     this.sensitivityDetector = (Http2HeadersEncoder.SensitivityDetector)ObjectUtil.checkNotNull(sensitivityDetector, "sensitiveDetector");
/* 55 */     this.hpackEncoder = (HpackEncoder)ObjectUtil.checkNotNull(hpackEncoder, "hpackEncoder");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void encodeHeaders(int streamId, Http2Headers headers, ByteBuf buffer) throws Http2Exception {
/*    */     try {
/* 63 */       if (this.tableSizeChangeOutput.isReadable()) {
/* 64 */         buffer.writeBytes(this.tableSizeChangeOutput);
/* 65 */         this.tableSizeChangeOutput.clear();
/*    */       } 
/*    */       
/* 68 */       this.hpackEncoder.encodeHeaders(streamId, buffer, headers, this.sensitivityDetector);
/* 69 */     } catch (Http2Exception e) {
/* 70 */       throw e;
/* 71 */     } catch (Throwable t) {
/* 72 */       throw Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, t, "Failed encoding headers block: %s", new Object[] { t.getMessage() });
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void maxHeaderTableSize(long max) throws Http2Exception {
/* 78 */     this.hpackEncoder.setMaxHeaderTableSize(this.tableSizeChangeOutput, max);
/*    */   }
/*    */ 
/*    */   
/*    */   public long maxHeaderTableSize() {
/* 83 */     return this.hpackEncoder.getMaxHeaderTableSize();
/*    */   }
/*    */ 
/*    */   
/*    */   public void maxHeaderListSize(long max) throws Http2Exception {
/* 88 */     this.hpackEncoder.setMaxHeaderListSize(max);
/*    */   }
/*    */ 
/*    */   
/*    */   public long maxHeaderListSize() {
/* 93 */     return this.hpackEncoder.getMaxHeaderListSize();
/*    */   }
/*    */ 
/*    */   
/*    */   public Http2HeadersEncoder.Configuration configuration() {
/* 98 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\DefaultHttp2HeadersEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */