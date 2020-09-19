/*    */ package io.netty.handler.codec.memcache.binary;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufHolder;
/*    */ import io.netty.handler.codec.memcache.AbstractMemcacheObjectAggregator;
/*    */ import io.netty.handler.codec.memcache.FullMemcacheMessage;
/*    */ import io.netty.handler.codec.memcache.MemcacheObject;
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
/*    */ public class BinaryMemcacheObjectAggregator
/*    */   extends AbstractMemcacheObjectAggregator<BinaryMemcacheMessage>
/*    */ {
/*    */   public BinaryMemcacheObjectAggregator(int maxContentLength) {
/* 35 */     super(maxContentLength);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isStartMessage(MemcacheObject msg) throws Exception {
/* 40 */     return msg instanceof BinaryMemcacheMessage;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FullMemcacheMessage beginAggregation(BinaryMemcacheMessage start, ByteBuf content) throws Exception {
/* 45 */     if (start instanceof BinaryMemcacheRequest) {
/* 46 */       return toFullRequest((BinaryMemcacheRequest)start, content);
/*    */     }
/*    */     
/* 49 */     if (start instanceof BinaryMemcacheResponse) {
/* 50 */       return toFullResponse((BinaryMemcacheResponse)start, content);
/*    */     }
/*    */ 
/*    */     
/* 54 */     throw new Error();
/*    */   }
/*    */   
/*    */   private static FullBinaryMemcacheRequest toFullRequest(BinaryMemcacheRequest request, ByteBuf content) {
/* 58 */     ByteBuf key = (request.key() == null) ? null : request.key().retain();
/* 59 */     ByteBuf extras = (request.extras() == null) ? null : request.extras().retain();
/* 60 */     DefaultFullBinaryMemcacheRequest fullRequest = new DefaultFullBinaryMemcacheRequest(key, extras, content);
/*    */ 
/*    */     
/* 63 */     fullRequest.setMagic(request.magic());
/* 64 */     fullRequest.setOpcode(request.opcode());
/* 65 */     fullRequest.setKeyLength(request.keyLength());
/* 66 */     fullRequest.setExtrasLength(request.extrasLength());
/* 67 */     fullRequest.setDataType(request.dataType());
/* 68 */     fullRequest.setTotalBodyLength(request.totalBodyLength());
/* 69 */     fullRequest.setOpaque(request.opaque());
/* 70 */     fullRequest.setCas(request.cas());
/* 71 */     fullRequest.setReserved(request.reserved());
/*    */     
/* 73 */     return fullRequest;
/*    */   }
/*    */   
/*    */   private static FullBinaryMemcacheResponse toFullResponse(BinaryMemcacheResponse response, ByteBuf content) {
/* 77 */     ByteBuf key = (response.key() == null) ? null : response.key().retain();
/* 78 */     ByteBuf extras = (response.extras() == null) ? null : response.extras().retain();
/* 79 */     DefaultFullBinaryMemcacheResponse fullResponse = new DefaultFullBinaryMemcacheResponse(key, extras, content);
/*    */ 
/*    */     
/* 82 */     fullResponse.setMagic(response.magic());
/* 83 */     fullResponse.setOpcode(response.opcode());
/* 84 */     fullResponse.setKeyLength(response.keyLength());
/* 85 */     fullResponse.setExtrasLength(response.extrasLength());
/* 86 */     fullResponse.setDataType(response.dataType());
/* 87 */     fullResponse.setTotalBodyLength(response.totalBodyLength());
/* 88 */     fullResponse.setOpaque(response.opaque());
/* 89 */     fullResponse.setCas(response.cas());
/* 90 */     fullResponse.setStatus(response.status());
/*    */     
/* 92 */     return fullResponse;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\binary\BinaryMemcacheObjectAggregator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */