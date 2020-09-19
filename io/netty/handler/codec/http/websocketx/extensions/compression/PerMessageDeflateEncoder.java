/*    */ package io.netty.handler.codec.http.websocketx.extensions.compression;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.http.websocketx.WebSocketFrame;
/*    */ import java.util.List;
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
/*    */ 
/*    */ class PerMessageDeflateEncoder
/*    */   extends DeflateEncoder
/*    */ {
/*    */   private boolean compressing;
/*    */   
/*    */   public PerMessageDeflateEncoder(int compressionLevel, int windowSize, boolean noContext) {
/* 41 */     super(compressionLevel, windowSize, noContext);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean acceptOutboundMessage(Object msg) throws Exception {
/* 46 */     return (((msg instanceof io.netty.handler.codec.http.websocketx.TextWebSocketFrame || msg instanceof io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame) && (((WebSocketFrame)msg)
/*    */       
/* 48 */       .rsv() & 0x4) == 0) || (msg instanceof io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame && this.compressing));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected int rsv(WebSocketFrame msg) {
/* 54 */     return (msg instanceof io.netty.handler.codec.http.websocketx.TextWebSocketFrame || msg instanceof io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame) ? (msg
/* 55 */       .rsv() | 0x4) : msg.rsv();
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean removeFrameTail(WebSocketFrame msg) {
/* 60 */     return msg.isFinalFragment();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void encode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
/* 66 */     super.encode(ctx, msg, out);
/*    */     
/* 68 */     if (msg.isFinalFragment()) {
/* 69 */       this.compressing = false;
/* 70 */     } else if (msg instanceof io.netty.handler.codec.http.websocketx.TextWebSocketFrame || msg instanceof io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame) {
/* 71 */       this.compressing = true;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\websocketx\extensions\compression\PerMessageDeflateEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */