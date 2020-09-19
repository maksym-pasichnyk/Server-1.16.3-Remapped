/*    */ package io.netty.handler.codec.memcache;
/*    */ 
/*    */ import io.netty.handler.codec.DecoderResult;
/*    */ import io.netty.util.AbstractReferenceCounted;
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
/*    */ public abstract class AbstractMemcacheObject
/*    */   extends AbstractReferenceCounted
/*    */   implements MemcacheObject
/*    */ {
/* 28 */   private DecoderResult decoderResult = DecoderResult.SUCCESS;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DecoderResult decoderResult() {
/* 36 */     return this.decoderResult;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDecoderResult(DecoderResult result) {
/* 41 */     if (result == null) {
/* 42 */       throw new NullPointerException("DecoderResult should not be null.");
/*    */     }
/*    */     
/* 45 */     this.decoderResult = result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\AbstractMemcacheObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */