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
/*    */ class PerMessageDeflateDecoder
/*    */   extends DeflateDecoder
/*    */ {
/*    */   private boolean compressing;
/*    */   
/*    */   public PerMessageDeflateDecoder(boolean noContext) {
/* 39 */     super(noContext);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean acceptInboundMessage(Object msg) throws Exception {
/* 44 */     return (((msg instanceof io.netty.handler.codec.http.websocketx.TextWebSocketFrame || msg instanceof io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame) && (((WebSocketFrame)msg)
/*    */       
/* 46 */       .rsv() & 0x4) > 0) || (msg instanceof io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame && this.compressing));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected int newRsv(WebSocketFrame msg) {
/* 52 */     return ((msg.rsv() & 0x4) > 0) ? (msg
/* 53 */       .rsv() ^ 0x4) : msg.rsv();
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean appendFrameTail(WebSocketFrame msg) {
/* 58 */     return msg.isFinalFragment();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
/* 64 */     super.decode(ctx, msg, out);
/*    */     
/* 66 */     if (msg.isFinalFragment()) {
/* 67 */       this.compressing = false;
/* 68 */     } else if (msg instanceof io.netty.handler.codec.http.websocketx.TextWebSocketFrame || msg instanceof io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame) {
/* 69 */       this.compressing = true;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\websocketx\extensions\compression\PerMessageDeflateDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */