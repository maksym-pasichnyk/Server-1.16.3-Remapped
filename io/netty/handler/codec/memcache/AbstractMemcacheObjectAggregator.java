/*    */ package io.netty.handler.codec.memcache;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractMemcacheObjectAggregator<H extends MemcacheMessage>
/*    */   extends MessageAggregator<MemcacheObject, H, MemcacheContent, FullMemcacheMessage>
/*    */ {
/*    */   protected AbstractMemcacheObjectAggregator(int maxContentLength) {
/* 50 */     super(maxContentLength);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isContentMessage(MemcacheObject msg) throws Exception {
/* 55 */     return msg instanceof MemcacheContent;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isLastContentMessage(MemcacheContent msg) throws Exception {
/* 60 */     return msg instanceof LastMemcacheContent;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isAggregated(MemcacheObject msg) throws Exception {
/* 65 */     return msg instanceof FullMemcacheMessage;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isContentLengthInvalid(H start, int maxContentLength) {
/* 70 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Object newContinueResponse(H start, int maxContentLength, ChannelPipeline pipeline) {
/* 75 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean closeAfterContinueResponse(Object msg) throws Exception {
/* 80 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean ignoreContentAfterContinueResponse(Object msg) throws Exception {
/* 85 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\AbstractMemcacheObjectAggregator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */