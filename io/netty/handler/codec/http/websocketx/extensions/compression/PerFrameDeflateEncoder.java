/*    */ package io.netty.handler.codec.http.websocketx.extensions.compression;
/*    */ 
/*    */ import io.netty.handler.codec.http.websocketx.WebSocketFrame;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class PerFrameDeflateEncoder
/*    */   extends DeflateEncoder
/*    */ {
/*    */   public PerFrameDeflateEncoder(int compressionLevel, int windowSize, boolean noContext) {
/* 36 */     super(compressionLevel, windowSize, noContext);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean acceptOutboundMessage(Object msg) throws Exception {
/* 41 */     return ((msg instanceof io.netty.handler.codec.http.websocketx.TextWebSocketFrame || msg instanceof io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame || msg instanceof io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame) && ((WebSocketFrame)msg)
/*    */ 
/*    */       
/* 44 */       .content().readableBytes() > 0 && (((WebSocketFrame)msg)
/* 45 */       .rsv() & 0x4) == 0);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int rsv(WebSocketFrame msg) {
/* 50 */     return msg.rsv() | 0x4;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean removeFrameTail(WebSocketFrame msg) {
/* 55 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\websocketx\extensions\compression\PerFrameDeflateEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */