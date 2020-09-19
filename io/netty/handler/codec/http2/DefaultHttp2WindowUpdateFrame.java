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
/*    */ public class DefaultHttp2WindowUpdateFrame
/*    */   extends AbstractHttp2StreamFrame
/*    */   implements Http2WindowUpdateFrame
/*    */ {
/*    */   private final int windowUpdateIncrement;
/*    */   
/*    */   public DefaultHttp2WindowUpdateFrame(int windowUpdateIncrement) {
/* 29 */     this.windowUpdateIncrement = windowUpdateIncrement;
/*    */   }
/*    */ 
/*    */   
/*    */   public DefaultHttp2WindowUpdateFrame stream(Http2FrameStream stream) {
/* 34 */     super.stream(stream);
/* 35 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public String name() {
/* 40 */     return "WINDOW_UPDATE";
/*    */   }
/*    */ 
/*    */   
/*    */   public int windowSizeIncrement() {
/* 45 */     return this.windowUpdateIncrement;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\DefaultHttp2WindowUpdateFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */