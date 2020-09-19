/*    */ package io.netty.handler.codec.http2;
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
/*    */ 
/*    */ 
/*    */ public enum Http2Error
/*    */ {
/* 25 */   NO_ERROR(0L),
/* 26 */   PROTOCOL_ERROR(1L),
/* 27 */   INTERNAL_ERROR(2L),
/* 28 */   FLOW_CONTROL_ERROR(3L),
/* 29 */   SETTINGS_TIMEOUT(4L),
/* 30 */   STREAM_CLOSED(5L),
/* 31 */   FRAME_SIZE_ERROR(6L),
/* 32 */   REFUSED_STREAM(7L),
/* 33 */   CANCEL(8L),
/* 34 */   COMPRESSION_ERROR(9L),
/* 35 */   CONNECT_ERROR(10L),
/* 36 */   ENHANCE_YOUR_CALM(11L),
/* 37 */   INADEQUATE_SECURITY(12L),
/* 38 */   HTTP_1_1_REQUIRED(13L);
/*    */   private final long code;
/*    */   private static final Http2Error[] INT_TO_ENUM_MAP;
/*    */   
/*    */   static {
/* 43 */     Http2Error[] errors = values();
/* 44 */     Http2Error[] map = new Http2Error[errors.length];
/* 45 */     for (Http2Error error : errors) {
/* 46 */       map[(int)error.code()] = error;
/*    */     }
/* 48 */     INT_TO_ENUM_MAP = map;
/*    */   }
/*    */   
/*    */   Http2Error(long code) {
/* 52 */     this.code = code;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long code() {
/* 59 */     return this.code;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2Error.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */