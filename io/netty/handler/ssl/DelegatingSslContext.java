/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLSessionContext;
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
/*     */ public abstract class DelegatingSslContext
/*     */   extends SslContext
/*     */ {
/*     */   private final SslContext ctx;
/*     */   
/*     */   protected DelegatingSslContext(SslContext ctx) {
/*  33 */     this.ctx = (SslContext)ObjectUtil.checkNotNull(ctx, "ctx");
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isClient() {
/*  38 */     return this.ctx.isClient();
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<String> cipherSuites() {
/*  43 */     return this.ctx.cipherSuites();
/*     */   }
/*     */ 
/*     */   
/*     */   public final long sessionCacheSize() {
/*  48 */     return this.ctx.sessionCacheSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public final long sessionTimeout() {
/*  53 */     return this.ctx.sessionTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public final ApplicationProtocolNegotiator applicationProtocolNegotiator() {
/*  58 */     return this.ctx.applicationProtocolNegotiator();
/*     */   }
/*     */ 
/*     */   
/*     */   public final SSLEngine newEngine(ByteBufAllocator alloc) {
/*  63 */     SSLEngine engine = this.ctx.newEngine(alloc);
/*  64 */     initEngine(engine);
/*  65 */     return engine;
/*     */   }
/*     */ 
/*     */   
/*     */   public final SSLEngine newEngine(ByteBufAllocator alloc, String peerHost, int peerPort) {
/*  70 */     SSLEngine engine = this.ctx.newEngine(alloc, peerHost, peerPort);
/*  71 */     initEngine(engine);
/*  72 */     return engine;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final SslHandler newHandler(ByteBufAllocator alloc, boolean startTls) {
/*  77 */     SslHandler handler = this.ctx.newHandler(alloc, startTls);
/*  78 */     initHandler(handler);
/*  79 */     return handler;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final SslHandler newHandler(ByteBufAllocator alloc, String peerHost, int peerPort, boolean startTls) {
/*  84 */     SslHandler handler = this.ctx.newHandler(alloc, peerHost, peerPort, startTls);
/*  85 */     initHandler(handler);
/*  86 */     return handler;
/*     */   }
/*     */ 
/*     */   
/*     */   public final SSLSessionContext sessionContext() {
/*  91 */     return this.ctx.sessionContext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void initEngine(SSLEngine paramSSLEngine);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initHandler(SslHandler handler) {
/* 104 */     initEngine(handler.engine());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\DelegatingSslContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */