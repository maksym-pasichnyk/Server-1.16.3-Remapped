/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandler;
/*     */ import io.netty.channel.ChannelOutboundHandler;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.CombinedChannelDuplexHandler;
/*     */ import io.netty.handler.codec.PrematureChannelClosureException;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ public final class HttpClientCodec
/*     */   extends CombinedChannelDuplexHandler<HttpResponseDecoder, HttpRequestEncoder>
/*     */   implements HttpClientUpgradeHandler.SourceCodec
/*     */ {
/*  49 */   private final Queue<HttpMethod> queue = new ArrayDeque<HttpMethod>();
/*     */   
/*     */   private final boolean parseHttpAfterConnectRequest;
/*     */   
/*     */   private boolean done;
/*     */   
/*  55 */   private final AtomicLong requestResponseCounter = new AtomicLong();
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean failOnMissingResponse;
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpClientCodec() {
/*  64 */     this(4096, 8192, 8192, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpClientCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize) {
/*  71 */     this(maxInitialLineLength, maxHeaderSize, maxChunkSize, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpClientCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean failOnMissingResponse) {
/*  79 */     this(maxInitialLineLength, maxHeaderSize, maxChunkSize, failOnMissingResponse, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpClientCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean failOnMissingResponse, boolean validateHeaders) {
/*  88 */     this(maxInitialLineLength, maxHeaderSize, maxChunkSize, failOnMissingResponse, validateHeaders, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpClientCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean failOnMissingResponse, boolean validateHeaders, boolean parseHttpAfterConnectRequest) {
/*  97 */     init((ChannelInboundHandler)new Decoder(maxInitialLineLength, maxHeaderSize, maxChunkSize, validateHeaders), (ChannelOutboundHandler)new Encoder());
/*  98 */     this.failOnMissingResponse = failOnMissingResponse;
/*  99 */     this.parseHttpAfterConnectRequest = parseHttpAfterConnectRequest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpClientCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean failOnMissingResponse, boolean validateHeaders, int initialBufferSize) {
/* 108 */     this(maxInitialLineLength, maxHeaderSize, maxChunkSize, failOnMissingResponse, validateHeaders, initialBufferSize, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpClientCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean failOnMissingResponse, boolean validateHeaders, int initialBufferSize, boolean parseHttpAfterConnectRequest) {
/* 118 */     init((ChannelInboundHandler)new Decoder(maxInitialLineLength, maxHeaderSize, maxChunkSize, validateHeaders, initialBufferSize), (ChannelOutboundHandler)new Encoder());
/*     */     
/* 120 */     this.parseHttpAfterConnectRequest = parseHttpAfterConnectRequest;
/* 121 */     this.failOnMissingResponse = failOnMissingResponse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepareUpgradeFrom(ChannelHandlerContext ctx) {
/* 129 */     ((Encoder)outboundHandler()).upgraded = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgradeFrom(ChannelHandlerContext ctx) {
/* 138 */     ChannelPipeline p = ctx.pipeline();
/* 139 */     p.remove((ChannelHandler)this);
/*     */   }
/*     */   
/*     */   public void setSingleDecode(boolean singleDecode) {
/* 143 */     ((HttpResponseDecoder)inboundHandler()).setSingleDecode(singleDecode);
/*     */   }
/*     */   
/*     */   public boolean isSingleDecode() {
/* 147 */     return ((HttpResponseDecoder)inboundHandler()).isSingleDecode();
/*     */   }
/*     */   
/*     */   private final class Encoder
/*     */     extends HttpRequestEncoder
/*     */   {
/*     */     boolean upgraded;
/*     */     
/*     */     private Encoder() {}
/*     */     
/*     */     protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
/* 158 */       if (this.upgraded) {
/* 159 */         out.add(ReferenceCountUtil.retain(msg));
/*     */         
/*     */         return;
/*     */       } 
/* 163 */       if (msg instanceof HttpRequest && !HttpClientCodec.this.done) {
/* 164 */         HttpClientCodec.this.queue.offer(((HttpRequest)msg).method());
/*     */       }
/*     */       
/* 167 */       super.encode(ctx, msg, out);
/*     */       
/* 169 */       if (HttpClientCodec.this.failOnMissingResponse && !HttpClientCodec.this.done)
/*     */       {
/* 171 */         if (msg instanceof LastHttpContent)
/*     */         {
/* 173 */           HttpClientCodec.this.requestResponseCounter.incrementAndGet();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Decoder extends HttpResponseDecoder {
/*     */     Decoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders) {
/* 181 */       super(maxInitialLineLength, maxHeaderSize, maxChunkSize, validateHeaders);
/*     */     }
/*     */ 
/*     */     
/*     */     Decoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders, int initialBufferSize) {
/* 186 */       super(maxInitialLineLength, maxHeaderSize, maxChunkSize, validateHeaders, initialBufferSize);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
/* 192 */       if (HttpClientCodec.this.done) {
/* 193 */         int readable = actualReadableBytes();
/* 194 */         if (readable == 0) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 199 */         out.add(buffer.readBytes(readable));
/*     */       } else {
/* 201 */         int oldSize = out.size();
/* 202 */         super.decode(ctx, buffer, out);
/* 203 */         if (HttpClientCodec.this.failOnMissingResponse) {
/* 204 */           int size = out.size();
/* 205 */           for (int i = oldSize; i < size; i++) {
/* 206 */             decrement(out.get(i));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private void decrement(Object msg) {
/* 213 */       if (msg == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 218 */       if (msg instanceof LastHttpContent) {
/* 219 */         HttpClientCodec.this.requestResponseCounter.decrementAndGet();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isContentAlwaysEmpty(HttpMessage msg) {
/* 225 */       int statusCode = ((HttpResponse)msg).status().code();
/* 226 */       if (statusCode == 100 || statusCode == 101)
/*     */       {
/*     */         
/* 229 */         return super.isContentAlwaysEmpty(msg);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 234 */       HttpMethod method = HttpClientCodec.this.queue.poll();
/*     */       
/* 236 */       char firstChar = method.name().charAt(0);
/* 237 */       switch (firstChar) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 'H':
/* 243 */           if (HttpMethod.HEAD.equals(method)) {
/* 244 */             return true;
/*     */           }
/*     */           break;
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
/*     */         case 'C':
/* 262 */           if (statusCode == 200 && 
/* 263 */             HttpMethod.CONNECT.equals(method)) {
/*     */ 
/*     */             
/* 266 */             if (!HttpClientCodec.this.parseHttpAfterConnectRequest) {
/* 267 */               HttpClientCodec.this.done = true;
/* 268 */               HttpClientCodec.this.queue.clear();
/*     */             } 
/* 270 */             return true;
/*     */           } 
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 276 */       return super.isContentAlwaysEmpty(msg);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/* 282 */       super.channelInactive(ctx);
/*     */       
/* 284 */       if (HttpClientCodec.this.failOnMissingResponse) {
/* 285 */         long missingResponses = HttpClientCodec.this.requestResponseCounter.get();
/* 286 */         if (missingResponses > 0L)
/* 287 */           ctx.fireExceptionCaught((Throwable)new PrematureChannelClosureException("channel gone inactive with " + missingResponses + " missing response(s)")); 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\HttpClientCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */