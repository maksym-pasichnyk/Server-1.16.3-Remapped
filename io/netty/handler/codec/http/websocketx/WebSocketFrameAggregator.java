/*    */ package io.netty.handler.codec.http.websocketx;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufHolder;
/*    */ import io.netty.channel.ChannelPipeline;
/*    */ import io.netty.handler.codec.MessageAggregator;
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
/*    */ public class WebSocketFrameAggregator
/*    */   extends MessageAggregator<WebSocketFrame, WebSocketFrame, ContinuationWebSocketFrame, WebSocketFrame>
/*    */ {
/*    */   public WebSocketFrameAggregator(int maxContentLength) {
/* 39 */     super(maxContentLength);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isStartMessage(WebSocketFrame msg) throws Exception {
/* 44 */     return (msg instanceof TextWebSocketFrame || msg instanceof BinaryWebSocketFrame);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isContentMessage(WebSocketFrame msg) throws Exception {
/* 49 */     return msg instanceof ContinuationWebSocketFrame;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isLastContentMessage(ContinuationWebSocketFrame msg) throws Exception {
/* 54 */     return (isContentMessage(msg) && msg.isFinalFragment());
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isAggregated(WebSocketFrame msg) throws Exception {
/* 59 */     if (msg.isFinalFragment()) {
/* 60 */       return !isContentMessage(msg);
/*    */     }
/*    */     
/* 63 */     return (!isStartMessage(msg) && !isContentMessage(msg));
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isContentLengthInvalid(WebSocketFrame start, int maxContentLength) {
/* 68 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Object newContinueResponse(WebSocketFrame start, int maxContentLength, ChannelPipeline pipeline) {
/* 73 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean closeAfterContinueResponse(Object msg) throws Exception {
/* 78 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean ignoreContentAfterContinueResponse(Object msg) throws Exception {
/* 83 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   protected WebSocketFrame beginAggregation(WebSocketFrame start, ByteBuf content) throws Exception {
/* 88 */     if (start instanceof TextWebSocketFrame) {
/* 89 */       return new TextWebSocketFrame(true, start.rsv(), content);
/*    */     }
/*    */     
/* 92 */     if (start instanceof BinaryWebSocketFrame) {
/* 93 */       return new BinaryWebSocketFrame(true, start.rsv(), content);
/*    */     }
/*    */ 
/*    */     
/* 97 */     throw new Error();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\websocketx\WebSocketFrameAggregator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */