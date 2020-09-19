/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandler;
/*     */ import io.netty.channel.ChannelOutboundHandler;
/*     */ import io.netty.channel.CombinedChannelDuplexHandler;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
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
/*     */ public final class HttpServerCodec
/*     */   extends CombinedChannelDuplexHandler<HttpRequestDecoder, HttpResponseEncoder>
/*     */   implements HttpServerUpgradeHandler.SourceCodec
/*     */ {
/*  36 */   private final Queue<HttpMethod> queue = new ArrayDeque<HttpMethod>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpServerCodec() {
/*  44 */     this(4096, 8192, 8192);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpServerCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize) {
/*  51 */     init((ChannelInboundHandler)new HttpServerRequestDecoder(maxInitialLineLength, maxHeaderSize, maxChunkSize), (ChannelOutboundHandler)new HttpServerResponseEncoder());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpServerCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders) {
/*  59 */     init((ChannelInboundHandler)new HttpServerRequestDecoder(maxInitialLineLength, maxHeaderSize, maxChunkSize, validateHeaders), (ChannelOutboundHandler)new HttpServerResponseEncoder());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpServerCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders, int initialBufferSize) {
/*  68 */     init((ChannelInboundHandler)new HttpServerRequestDecoder(maxInitialLineLength, maxHeaderSize, maxChunkSize, validateHeaders, initialBufferSize), (ChannelOutboundHandler)new HttpServerResponseEncoder());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgradeFrom(ChannelHandlerContext ctx) {
/*  80 */     ctx.pipeline().remove((ChannelHandler)this);
/*     */   }
/*     */   
/*     */   private final class HttpServerRequestDecoder extends HttpRequestDecoder {
/*     */     public HttpServerRequestDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize) {
/*  85 */       super(maxInitialLineLength, maxHeaderSize, maxChunkSize);
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpServerRequestDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders) {
/*  90 */       super(maxInitialLineLength, maxHeaderSize, maxChunkSize, validateHeaders);
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpServerRequestDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders, int initialBufferSize) {
/*  95 */       super(maxInitialLineLength, maxHeaderSize, maxChunkSize, validateHeaders, initialBufferSize);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
/* 100 */       int oldSize = out.size();
/* 101 */       super.decode(ctx, buffer, out);
/* 102 */       int size = out.size();
/* 103 */       for (int i = oldSize; i < size; i++) {
/* 104 */         Object obj = out.get(i);
/* 105 */         if (obj instanceof HttpRequest)
/* 106 */           HttpServerCodec.this.queue.add(((HttpRequest)obj).method()); 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private final class HttpServerResponseEncoder
/*     */     extends HttpResponseEncoder {
/*     */     private HttpMethod method;
/*     */     
/*     */     private HttpServerResponseEncoder() {}
/*     */     
/*     */     protected void sanitizeHeadersBeforeEncode(HttpResponse msg, boolean isAlwaysEmpty) {
/* 118 */       if (!isAlwaysEmpty && this.method == HttpMethod.CONNECT && msg.status().codeClass() == HttpStatusClass.SUCCESS) {
/*     */ 
/*     */         
/* 121 */         msg.headers().remove((CharSequence)HttpHeaderNames.TRANSFER_ENCODING);
/*     */         
/*     */         return;
/*     */       } 
/* 125 */       super.sanitizeHeadersBeforeEncode(msg, isAlwaysEmpty);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isContentAlwaysEmpty(HttpResponse msg) {
/* 130 */       this.method = HttpServerCodec.this.queue.poll();
/* 131 */       return (HttpMethod.HEAD.equals(this.method) || super.isContentAlwaysEmpty(msg));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\HttpServerCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */