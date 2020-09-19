/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.concurrent.ConcurrentMap;
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
/*     */ @Sharable
/*     */ public abstract class ChannelInitializer<C extends Channel>
/*     */   extends ChannelInboundHandlerAdapter
/*     */ {
/*  55 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ChannelInitializer.class);
/*     */ 
/*     */   
/*  58 */   private final ConcurrentMap<ChannelHandlerContext, Boolean> initMap = PlatformDependent.newConcurrentHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void initChannel(C paramC) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void channelRegistered(ChannelHandlerContext ctx) throws Exception {
/*  76 */     if (initChannel(ctx)) {
/*     */ 
/*     */       
/*  79 */       ctx.pipeline().fireChannelRegistered();
/*     */     } else {
/*     */       
/*  82 */       ctx.fireChannelRegistered();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/*  91 */     logger.warn("Failed to initialize a channel. Closing: " + ctx.channel(), cause);
/*  92 */     ctx.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
/* 100 */     if (ctx.channel().isRegistered())
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 105 */       initChannel(ctx);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean initChannel(ChannelHandlerContext ctx) throws Exception {
/* 111 */     if (this.initMap.putIfAbsent(ctx, Boolean.TRUE) == null) {
/*     */       try {
/* 113 */         initChannel((C)ctx.channel());
/* 114 */       } catch (Throwable cause) {
/*     */ 
/*     */         
/* 117 */         exceptionCaught(ctx, cause);
/*     */       } finally {
/* 119 */         remove(ctx);
/*     */       } 
/* 121 */       return true;
/*     */     } 
/* 123 */     return false;
/*     */   }
/*     */   
/*     */   private void remove(ChannelHandlerContext ctx) {
/*     */     try {
/* 128 */       ChannelPipeline pipeline = ctx.pipeline();
/* 129 */       if (pipeline.context(this) != null) {
/* 130 */         pipeline.remove(this);
/*     */       }
/*     */     } finally {
/* 133 */       this.initMap.remove(ctx);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\ChannelInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */