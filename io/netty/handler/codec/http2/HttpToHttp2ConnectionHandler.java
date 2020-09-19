/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.http.EmptyHttpHeaders;
/*     */ import io.netty.handler.codec.http.FullHttpMessage;
/*     */ import io.netty.handler.codec.http.HttpContent;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpMessage;
/*     */ import io.netty.handler.codec.http.LastHttpContent;
/*     */ import io.netty.util.ReferenceCountUtil;
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
/*     */ public class HttpToHttp2ConnectionHandler
/*     */   extends Http2ConnectionHandler
/*     */ {
/*     */   private final boolean validateHeaders;
/*     */   private int currentStreamId;
/*     */   
/*     */   protected HttpToHttp2ConnectionHandler(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder, Http2Settings initialSettings, boolean validateHeaders) {
/*  44 */     super(decoder, encoder, initialSettings);
/*  45 */     this.validateHeaders = validateHeaders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getStreamId(HttpHeaders httpHeaders) throws Exception {
/*  56 */     return httpHeaders.getInt((CharSequence)HttpConversionUtil.ExtensionHeaderNames.STREAM_ID.text(), 
/*  57 */         connection().local().incrementAndGetNextStreamId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
/*  66 */     if (!(msg instanceof HttpMessage) && !(msg instanceof HttpContent)) {
/*  67 */       ctx.write(msg, promise);
/*     */       
/*     */       return;
/*     */     } 
/*  71 */     boolean release = true;
/*     */     
/*  73 */     Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
/*     */     try {
/*  75 */       Http2ConnectionEncoder encoder = encoder();
/*  76 */       boolean endStream = false;
/*  77 */       if (msg instanceof HttpMessage) {
/*  78 */         HttpMessage httpMsg = (HttpMessage)msg;
/*     */ 
/*     */         
/*  81 */         this.currentStreamId = getStreamId(httpMsg.headers());
/*     */ 
/*     */         
/*  84 */         Http2Headers http2Headers = HttpConversionUtil.toHttp2Headers(httpMsg, this.validateHeaders);
/*  85 */         endStream = (msg instanceof FullHttpMessage && !((FullHttpMessage)msg).content().isReadable());
/*  86 */         writeHeaders(ctx, encoder, this.currentStreamId, httpMsg.headers(), http2Headers, endStream, promiseAggregator);
/*     */       } 
/*     */ 
/*     */       
/*  90 */       if (!endStream && msg instanceof HttpContent) {
/*  91 */         HttpHeaders httpHeaders; boolean isLastContent = false;
/*  92 */         EmptyHttpHeaders emptyHttpHeaders = EmptyHttpHeaders.INSTANCE;
/*  93 */         Http2Headers http2Trailers = EmptyHttp2Headers.INSTANCE;
/*  94 */         if (msg instanceof LastHttpContent) {
/*  95 */           isLastContent = true;
/*     */ 
/*     */           
/*  98 */           LastHttpContent lastContent = (LastHttpContent)msg;
/*  99 */           httpHeaders = lastContent.trailingHeaders();
/* 100 */           http2Trailers = HttpConversionUtil.toHttp2Headers(httpHeaders, this.validateHeaders);
/*     */         } 
/*     */ 
/*     */         
/* 104 */         ByteBuf content = ((HttpContent)msg).content();
/* 105 */         endStream = (isLastContent && httpHeaders.isEmpty());
/* 106 */         release = false;
/* 107 */         encoder.writeData(ctx, this.currentStreamId, content, 0, endStream, promiseAggregator.newPromise());
/*     */         
/* 109 */         if (!httpHeaders.isEmpty())
/*     */         {
/* 111 */           writeHeaders(ctx, encoder, this.currentStreamId, httpHeaders, http2Trailers, true, promiseAggregator);
/*     */         }
/*     */       } 
/* 114 */     } catch (Throwable t) {
/* 115 */       onError(ctx, true, t);
/* 116 */       promiseAggregator.setFailure(t);
/*     */     } finally {
/* 118 */       if (release) {
/* 119 */         ReferenceCountUtil.release(msg);
/*     */       }
/* 121 */       promiseAggregator.doneAllocatingPromises();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void writeHeaders(ChannelHandlerContext ctx, Http2ConnectionEncoder encoder, int streamId, HttpHeaders headers, Http2Headers http2Headers, boolean endStream, Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator) {
/* 128 */     int dependencyId = headers.getInt((CharSequence)HttpConversionUtil.ExtensionHeaderNames.STREAM_DEPENDENCY_ID
/* 129 */         .text(), 0);
/* 130 */     short weight = headers.getShort((CharSequence)HttpConversionUtil.ExtensionHeaderNames.STREAM_WEIGHT
/* 131 */         .text(), (short)16);
/* 132 */     encoder.writeHeaders(ctx, streamId, http2Headers, dependencyId, weight, false, 0, endStream, promiseAggregator
/* 133 */         .newPromise());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\HttpToHttp2ConnectionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */