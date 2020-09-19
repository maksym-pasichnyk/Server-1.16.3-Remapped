/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ import java.util.List;
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
/*    */ public class DecoratingHttp2ConnectionDecoder
/*    */   implements Http2ConnectionDecoder
/*    */ {
/*    */   private final Http2ConnectionDecoder delegate;
/*    */   
/*    */   public DecoratingHttp2ConnectionDecoder(Http2ConnectionDecoder delegate) {
/* 33 */     this.delegate = (Http2ConnectionDecoder)ObjectUtil.checkNotNull(delegate, "delegate");
/*    */   }
/*    */ 
/*    */   
/*    */   public void lifecycleManager(Http2LifecycleManager lifecycleManager) {
/* 38 */     this.delegate.lifecycleManager(lifecycleManager);
/*    */   }
/*    */ 
/*    */   
/*    */   public Http2Connection connection() {
/* 43 */     return this.delegate.connection();
/*    */   }
/*    */ 
/*    */   
/*    */   public Http2LocalFlowController flowController() {
/* 48 */     return this.delegate.flowController();
/*    */   }
/*    */ 
/*    */   
/*    */   public void frameListener(Http2FrameListener listener) {
/* 53 */     this.delegate.frameListener(listener);
/*    */   }
/*    */ 
/*    */   
/*    */   public Http2FrameListener frameListener() {
/* 58 */     return this.delegate.frameListener();
/*    */   }
/*    */ 
/*    */   
/*    */   public void decodeFrame(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Http2Exception {
/* 63 */     this.delegate.decodeFrame(ctx, in, out);
/*    */   }
/*    */ 
/*    */   
/*    */   public Http2Settings localSettings() {
/* 68 */     return this.delegate.localSettings();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean prefaceReceived() {
/* 73 */     return this.delegate.prefaceReceived();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 78 */     this.delegate.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\DecoratingHttp2ConnectionDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */