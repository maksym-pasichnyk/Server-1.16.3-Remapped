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
/*    */ public class DecoratingHttp2ConnectionEncoder
/*    */   extends DecoratingHttp2FrameWriter
/*    */   implements Http2ConnectionEncoder
/*    */ {
/*    */   private final Http2ConnectionEncoder delegate;
/*    */   
/*    */   public DecoratingHttp2ConnectionEncoder(Http2ConnectionEncoder delegate) {
/* 29 */     super(delegate);
/* 30 */     this.delegate = (Http2ConnectionEncoder)ObjectUtil.checkNotNull(delegate, "delegate");
/*    */   }
/*    */ 
/*    */   
/*    */   public void lifecycleManager(Http2LifecycleManager lifecycleManager) {
/* 35 */     this.delegate.lifecycleManager(lifecycleManager);
/*    */   }
/*    */ 
/*    */   
/*    */   public Http2Connection connection() {
/* 40 */     return this.delegate.connection();
/*    */   }
/*    */ 
/*    */   
/*    */   public Http2RemoteFlowController flowController() {
/* 45 */     return this.delegate.flowController();
/*    */   }
/*    */ 
/*    */   
/*    */   public Http2FrameWriter frameWriter() {
/* 50 */     return this.delegate.frameWriter();
/*    */   }
/*    */ 
/*    */   
/*    */   public Http2Settings pollSentSettings() {
/* 55 */     return this.delegate.pollSentSettings();
/*    */   }
/*    */ 
/*    */   
/*    */   public void remoteSettings(Http2Settings settings) throws Http2Exception {
/* 60 */     this.delegate.remoteSettings(settings);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\DecoratingHttp2ConnectionEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */