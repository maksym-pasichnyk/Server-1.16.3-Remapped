/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
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
/*    */ public final class Http2FrameStreamException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = -4407186173493887044L;
/*    */   private final Http2Error error;
/*    */   private final Http2FrameStream stream;
/*    */   
/*    */   public Http2FrameStreamException(Http2FrameStream stream, Http2Error error, Throwable cause) {
/* 35 */     super(cause.getMessage(), cause);
/* 36 */     this.stream = (Http2FrameStream)ObjectUtil.checkNotNull(stream, "stream");
/* 37 */     this.error = (Http2Error)ObjectUtil.checkNotNull(error, "error");
/*    */   }
/*    */   
/*    */   public Http2Error error() {
/* 41 */     return this.error;
/*    */   }
/*    */   
/*    */   public Http2FrameStream stream() {
/* 45 */     return this.stream;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2FrameStreamException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */