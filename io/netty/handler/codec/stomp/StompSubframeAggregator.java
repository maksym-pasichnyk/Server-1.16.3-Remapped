/*    */ package io.netty.handler.codec.stomp;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StompSubframeAggregator
/*    */   extends MessageAggregator<StompSubframe, StompHeadersSubframe, StompContentSubframe, StompFrame>
/*    */ {
/*    */   public StompSubframeAggregator(int maxContentLength) {
/* 42 */     super(maxContentLength);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isStartMessage(StompSubframe msg) throws Exception {
/* 47 */     return msg instanceof StompHeadersSubframe;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isContentMessage(StompSubframe msg) throws Exception {
/* 52 */     return msg instanceof StompContentSubframe;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isLastContentMessage(StompContentSubframe msg) throws Exception {
/* 57 */     return msg instanceof LastStompContentSubframe;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isAggregated(StompSubframe msg) throws Exception {
/* 62 */     return msg instanceof StompFrame;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isContentLengthInvalid(StompHeadersSubframe start, int maxContentLength) {
/* 67 */     return ((int)Math.min(2147483647L, start.headers().getLong(StompHeaders.CONTENT_LENGTH, -1L)) > maxContentLength);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Object newContinueResponse(StompHeadersSubframe start, int maxContentLength, ChannelPipeline pipeline) {
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
/*    */   protected StompFrame beginAggregation(StompHeadersSubframe start, ByteBuf content) throws Exception {
/* 88 */     StompFrame ret = new DefaultStompFrame(start.command(), content);
/* 89 */     ret.headers().set(start.headers());
/* 90 */     return ret;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\stomp\StompSubframeAggregator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */