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
/*    */ 
/*    */ public abstract class AbstractHttp2StreamFrame
/*    */   implements Http2StreamFrame
/*    */ {
/*    */   private Http2FrameStream stream;
/*    */   
/*    */   public AbstractHttp2StreamFrame stream(Http2FrameStream stream) {
/* 30 */     this.stream = stream;
/* 31 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public Http2FrameStream stream() {
/* 36 */     return this.stream;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 44 */     if (!(o instanceof Http2StreamFrame)) {
/* 45 */       return false;
/*    */     }
/* 47 */     Http2StreamFrame other = (Http2StreamFrame)o;
/* 48 */     return (this.stream == other.stream() || (this.stream != null && this.stream.equals(other.stream())));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 53 */     Http2FrameStream stream = this.stream;
/* 54 */     if (stream == null) {
/* 55 */       return super.hashCode();
/*    */     }
/* 57 */     return stream.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\AbstractHttp2StreamFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */