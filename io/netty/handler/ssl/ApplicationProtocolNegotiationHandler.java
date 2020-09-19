/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ApplicationProtocolNegotiationHandler
/*     */   extends ChannelInboundHandlerAdapter
/*     */ {
/*  65 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ApplicationProtocolNegotiationHandler.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String fallbackProtocol;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ApplicationProtocolNegotiationHandler(String fallbackProtocol) {
/*  76 */     this.fallbackProtocol = (String)ObjectUtil.checkNotNull(fallbackProtocol, "fallbackProtocol");
/*     */   }
/*     */ 
/*     */   
/*     */   public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
/*  81 */     if (evt instanceof SslHandshakeCompletionEvent) {
/*  82 */       ctx.pipeline().remove((ChannelHandler)this);
/*     */       
/*  84 */       SslHandshakeCompletionEvent handshakeEvent = (SslHandshakeCompletionEvent)evt;
/*  85 */       if (handshakeEvent.isSuccess()) {
/*  86 */         SslHandler sslHandler = (SslHandler)ctx.pipeline().get(SslHandler.class);
/*  87 */         if (sslHandler == null) {
/*  88 */           throw new IllegalStateException("cannot find a SslHandler in the pipeline (required for application-level protocol negotiation)");
/*     */         }
/*     */         
/*  91 */         String protocol = sslHandler.applicationProtocol();
/*  92 */         configurePipeline(ctx, (protocol != null) ? protocol : this.fallbackProtocol);
/*     */       } else {
/*  94 */         handshakeFailure(ctx, handshakeEvent.cause());
/*     */       } 
/*     */     } 
/*     */     
/*  98 */     ctx.fireUserEventTriggered(evt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void configurePipeline(ChannelHandlerContext paramChannelHandlerContext, String paramString) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handshakeFailure(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/* 115 */     logger.warn("{} TLS handshake failed:", ctx.channel(), cause);
/* 116 */     ctx.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/* 121 */     logger.warn("{} Failed to select the application-level protocol:", ctx.channel(), cause);
/* 122 */     ctx.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\ApplicationProtocolNegotiationHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */