/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ import io.netty.util.internal.StringUtil;
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
/*    */ public final class DefaultHttp2ResetFrame
/*    */   extends AbstractHttp2StreamFrame
/*    */   implements Http2ResetFrame
/*    */ {
/*    */   private final long errorCode;
/*    */   
/*    */   public DefaultHttp2ResetFrame(Http2Error error) {
/* 37 */     this.errorCode = ((Http2Error)ObjectUtil.checkNotNull(error, "error")).code();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultHttp2ResetFrame(long errorCode) {
/* 46 */     this.errorCode = errorCode;
/*    */   }
/*    */ 
/*    */   
/*    */   public DefaultHttp2ResetFrame stream(Http2FrameStream stream) {
/* 51 */     super.stream(stream);
/* 52 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public String name() {
/* 57 */     return "RST_STREAM";
/*    */   }
/*    */ 
/*    */   
/*    */   public long errorCode() {
/* 62 */     return this.errorCode;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 67 */     return StringUtil.simpleClassName(this) + "(stream=" + stream() + ", errorCode=" + this.errorCode + ')';
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 72 */     if (!(o instanceof DefaultHttp2ResetFrame)) {
/* 73 */       return false;
/*    */     }
/* 75 */     DefaultHttp2ResetFrame other = (DefaultHttp2ResetFrame)o;
/* 76 */     return (super.equals(o) && this.errorCode == other.errorCode);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 81 */     int hash = super.hashCode();
/* 82 */     hash = hash * 31 + (int)(this.errorCode ^ this.errorCode >>> 32L);
/* 83 */     return hash;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\DefaultHttp2ResetFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */