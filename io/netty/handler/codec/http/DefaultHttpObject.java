/*    */ package io.netty.handler.codec.http;
/*    */ 
/*    */ import io.netty.handler.codec.DecoderResult;
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
/*    */ public class DefaultHttpObject
/*    */   implements HttpObject
/*    */ {
/*    */   private static final int HASH_CODE_PRIME = 31;
/* 23 */   private DecoderResult decoderResult = DecoderResult.SUCCESS;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DecoderResult decoderResult() {
/* 31 */     return this.decoderResult;
/*    */   }
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public DecoderResult getDecoderResult() {
/* 37 */     return decoderResult();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDecoderResult(DecoderResult decoderResult) {
/* 42 */     if (decoderResult == null) {
/* 43 */       throw new NullPointerException("decoderResult");
/*    */     }
/* 45 */     this.decoderResult = decoderResult;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 50 */     int result = 1;
/* 51 */     result = 31 * result + this.decoderResult.hashCode();
/* 52 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 57 */     if (!(o instanceof DefaultHttpObject)) {
/* 58 */       return false;
/*    */     }
/*    */     
/* 61 */     DefaultHttpObject other = (DefaultHttpObject)o;
/*    */     
/* 63 */     return decoderResult().equals(other.decoderResult());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\DefaultHttpObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */