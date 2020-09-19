/*    */ package io.netty.handler.ssl.ocsp;
/*    */ 
/*    */ import io.netty.channel.ChannelHandler;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*    */ import io.netty.handler.ssl.ReferenceCountedOpenSslEngine;
/*    */ import io.netty.handler.ssl.SslHandshakeCompletionEvent;
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ import io.netty.util.internal.ThrowableUtil;
/*    */ import javax.net.ssl.SSLHandshakeException;
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
/*    */ public abstract class OcspClientHandler
/*    */   extends ChannelInboundHandlerAdapter
/*    */ {
/* 38 */   private static final SSLHandshakeException OCSP_VERIFICATION_EXCEPTION = (SSLHandshakeException)ThrowableUtil.unknownStackTrace(new SSLHandshakeException("Bad OCSP response"), OcspClientHandler.class, "verify(...)");
/*    */   
/*    */   private final ReferenceCountedOpenSslEngine engine;
/*    */ 
/*    */   
/*    */   protected OcspClientHandler(ReferenceCountedOpenSslEngine engine) {
/* 44 */     this.engine = (ReferenceCountedOpenSslEngine)ObjectUtil.checkNotNull(engine, "engine");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract boolean verify(ChannelHandlerContext paramChannelHandlerContext, ReferenceCountedOpenSslEngine paramReferenceCountedOpenSslEngine) throws Exception;
/*    */ 
/*    */ 
/*    */   
/*    */   public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
/* 54 */     if (evt instanceof SslHandshakeCompletionEvent) {
/* 55 */       ctx.pipeline().remove((ChannelHandler)this);
/*    */       
/* 57 */       SslHandshakeCompletionEvent event = (SslHandshakeCompletionEvent)evt;
/* 58 */       if (event.isSuccess() && !verify(ctx, this.engine)) {
/* 59 */         throw OCSP_VERIFICATION_EXCEPTION;
/*    */       }
/*    */     } 
/*    */     
/* 63 */     ctx.fireUserEventTriggered(evt);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\ocsp\OcspClientHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */