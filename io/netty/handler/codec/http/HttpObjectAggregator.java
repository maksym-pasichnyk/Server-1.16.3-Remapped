/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.DecoderResult;
/*     */ import io.netty.handler.codec.MessageAggregator;
/*     */ import io.netty.handler.codec.TooLongFrameException;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
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
/*     */ public class HttpObjectAggregator
/*     */   extends MessageAggregator<HttpObject, HttpMessage, HttpContent, FullHttpMessage>
/*     */ {
/*  89 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(HttpObjectAggregator.class);
/*  90 */   private static final FullHttpResponse CONTINUE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE, Unpooled.EMPTY_BUFFER);
/*     */   
/*  92 */   private static final FullHttpResponse EXPECTATION_FAILED = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.EXPECTATION_FAILED, Unpooled.EMPTY_BUFFER);
/*     */   
/*  94 */   private static final FullHttpResponse TOO_LARGE_CLOSE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE, Unpooled.EMPTY_BUFFER);
/*     */   
/*  96 */   private static final FullHttpResponse TOO_LARGE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE, Unpooled.EMPTY_BUFFER);
/*     */   private final boolean closeOnExpectationFailed;
/*     */   
/*     */   static {
/* 100 */     EXPECTATION_FAILED.headers().set((CharSequence)HttpHeaderNames.CONTENT_LENGTH, Integer.valueOf(0));
/* 101 */     TOO_LARGE.headers().set((CharSequence)HttpHeaderNames.CONTENT_LENGTH, Integer.valueOf(0));
/*     */     
/* 103 */     TOO_LARGE_CLOSE.headers().set((CharSequence)HttpHeaderNames.CONTENT_LENGTH, Integer.valueOf(0));
/* 104 */     TOO_LARGE_CLOSE.headers().set((CharSequence)HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
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
/*     */   public HttpObjectAggregator(int maxContentLength) {
/* 116 */     this(maxContentLength, false);
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
/*     */   
/*     */   public HttpObjectAggregator(int maxContentLength, boolean closeOnExpectationFailed) {
/* 129 */     super(maxContentLength);
/* 130 */     this.closeOnExpectationFailed = closeOnExpectationFailed;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isStartMessage(HttpObject msg) throws Exception {
/* 135 */     return msg instanceof HttpMessage;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isContentMessage(HttpObject msg) throws Exception {
/* 140 */     return msg instanceof HttpContent;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isLastContentMessage(HttpContent msg) throws Exception {
/* 145 */     return msg instanceof LastHttpContent;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isAggregated(HttpObject msg) throws Exception {
/* 150 */     return msg instanceof FullHttpMessage;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isContentLengthInvalid(HttpMessage start, int maxContentLength) {
/*     */     try {
/* 156 */       return (HttpUtil.getContentLength(start, -1L) > maxContentLength);
/* 157 */     } catch (NumberFormatException e) {
/* 158 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Object continueResponse(HttpMessage start, int maxContentLength, ChannelPipeline pipeline) {
/* 163 */     if (HttpUtil.isUnsupportedExpectation(start)) {
/*     */       
/* 165 */       pipeline.fireUserEventTriggered(HttpExpectationFailedEvent.INSTANCE);
/* 166 */       return EXPECTATION_FAILED.retainedDuplicate();
/* 167 */     }  if (HttpUtil.is100ContinueExpected(start)) {
/*     */       
/* 169 */       if (HttpUtil.getContentLength(start, -1L) <= maxContentLength) {
/* 170 */         return CONTINUE.retainedDuplicate();
/*     */       }
/* 172 */       pipeline.fireUserEventTriggered(HttpExpectationFailedEvent.INSTANCE);
/* 173 */       return TOO_LARGE.retainedDuplicate();
/*     */     } 
/*     */     
/* 176 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object newContinueResponse(HttpMessage start, int maxContentLength, ChannelPipeline pipeline) {
/* 181 */     Object response = continueResponse(start, maxContentLength, pipeline);
/*     */ 
/*     */     
/* 184 */     if (response != null) {
/* 185 */       start.headers().remove((CharSequence)HttpHeaderNames.EXPECT);
/*     */     }
/* 187 */     return response;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean closeAfterContinueResponse(Object msg) {
/* 192 */     return (this.closeOnExpectationFailed && ignoreContentAfterContinueResponse(msg));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean ignoreContentAfterContinueResponse(Object msg) {
/* 197 */     if (msg instanceof HttpResponse) {
/* 198 */       HttpResponse httpResponse = (HttpResponse)msg;
/* 199 */       return httpResponse.status().codeClass().equals(HttpStatusClass.CLIENT_ERROR);
/*     */     } 
/* 201 */     return false;
/*     */   }
/*     */   
/*     */   protected FullHttpMessage beginAggregation(HttpMessage start, ByteBuf content) throws Exception {
/*     */     AggregatedFullHttpMessage ret;
/* 206 */     assert !(start instanceof FullHttpMessage);
/*     */     
/* 208 */     HttpUtil.setTransferEncodingChunked(start, false);
/*     */ 
/*     */     
/* 211 */     if (start instanceof HttpRequest) {
/* 212 */       ret = new AggregatedFullHttpRequest((HttpRequest)start, content, null);
/* 213 */     } else if (start instanceof HttpResponse) {
/* 214 */       ret = new AggregatedFullHttpResponse((HttpResponse)start, content, null);
/*     */     } else {
/* 216 */       throw new Error();
/*     */     } 
/* 218 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void aggregate(FullHttpMessage aggregated, HttpContent content) throws Exception {
/* 223 */     if (content instanceof LastHttpContent)
/*     */     {
/* 225 */       ((AggregatedFullHttpMessage)aggregated).setTrailingHeaders(((LastHttpContent)content).trailingHeaders());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void finishAggregation(FullHttpMessage aggregated) throws Exception {
/* 237 */     if (!HttpUtil.isContentLengthSet(aggregated)) {
/* 238 */       aggregated.headers().set((CharSequence)HttpHeaderNames.CONTENT_LENGTH, 
/*     */           
/* 240 */           String.valueOf(aggregated.content().readableBytes()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleOversizedMessage(final ChannelHandlerContext ctx, HttpMessage oversized) throws Exception {
/* 246 */     if (oversized instanceof HttpRequest) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 251 */       if (oversized instanceof FullHttpMessage || (
/* 252 */         !HttpUtil.is100ContinueExpected(oversized) && !HttpUtil.isKeepAlive(oversized))) {
/* 253 */         ChannelFuture future = ctx.writeAndFlush(TOO_LARGE_CLOSE.retainedDuplicate());
/* 254 */         future.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */             {
/*     */               public void operationComplete(ChannelFuture future) throws Exception {
/* 257 */                 if (!future.isSuccess()) {
/* 258 */                   HttpObjectAggregator.logger.debug("Failed to send a 413 Request Entity Too Large.", future.cause());
/*     */                 }
/* 260 */                 ctx.close();
/*     */               }
/*     */             });
/*     */       } else {
/* 264 */         ctx.writeAndFlush(TOO_LARGE.retainedDuplicate()).addListener((GenericFutureListener)new ChannelFutureListener()
/*     */             {
/*     */               public void operationComplete(ChannelFuture future) throws Exception {
/* 267 */                 if (!future.isSuccess()) {
/* 268 */                   HttpObjectAggregator.logger.debug("Failed to send a 413 Request Entity Too Large.", future.cause());
/* 269 */                   ctx.close();
/*     */                 } 
/*     */               }
/*     */             });
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 277 */       HttpObjectDecoder decoder = (HttpObjectDecoder)ctx.pipeline().get(HttpObjectDecoder.class);
/* 278 */       if (decoder != null)
/* 279 */         decoder.reset(); 
/*     */     } else {
/* 281 */       if (oversized instanceof HttpResponse) {
/* 282 */         ctx.close();
/* 283 */         throw new TooLongFrameException("Response entity too large: " + oversized);
/*     */       } 
/* 285 */       throw new IllegalStateException();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static abstract class AggregatedFullHttpMessage implements FullHttpMessage {
/*     */     protected final HttpMessage message;
/*     */     private final ByteBuf content;
/*     */     private HttpHeaders trailingHeaders;
/*     */     
/*     */     AggregatedFullHttpMessage(HttpMessage message, ByteBuf content, HttpHeaders trailingHeaders) {
/* 295 */       this.message = message;
/* 296 */       this.content = content;
/* 297 */       this.trailingHeaders = trailingHeaders;
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpHeaders trailingHeaders() {
/* 302 */       HttpHeaders trailingHeaders = this.trailingHeaders;
/* 303 */       if (trailingHeaders == null) {
/* 304 */         return EmptyHttpHeaders.INSTANCE;
/*     */       }
/* 306 */       return trailingHeaders;
/*     */     }
/*     */ 
/*     */     
/*     */     void setTrailingHeaders(HttpHeaders trailingHeaders) {
/* 311 */       this.trailingHeaders = trailingHeaders;
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpVersion getProtocolVersion() {
/* 316 */       return this.message.protocolVersion();
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpVersion protocolVersion() {
/* 321 */       return this.message.protocolVersion();
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpMessage setProtocolVersion(HttpVersion version) {
/* 326 */       this.message.setProtocolVersion(version);
/* 327 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpHeaders headers() {
/* 332 */       return this.message.headers();
/*     */     }
/*     */ 
/*     */     
/*     */     public DecoderResult decoderResult() {
/* 337 */       return this.message.decoderResult();
/*     */     }
/*     */ 
/*     */     
/*     */     public DecoderResult getDecoderResult() {
/* 342 */       return this.message.decoderResult();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setDecoderResult(DecoderResult result) {
/* 347 */       this.message.setDecoderResult(result);
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteBuf content() {
/* 352 */       return this.content;
/*     */     }
/*     */ 
/*     */     
/*     */     public int refCnt() {
/* 357 */       return this.content.refCnt();
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpMessage retain() {
/* 362 */       this.content.retain();
/* 363 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpMessage retain(int increment) {
/* 368 */       this.content.retain(increment);
/* 369 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpMessage touch(Object hint) {
/* 374 */       this.content.touch(hint);
/* 375 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpMessage touch() {
/* 380 */       this.content.touch();
/* 381 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean release() {
/* 386 */       return this.content.release();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean release(int decrement) {
/* 391 */       return this.content.release(decrement);
/*     */     }
/*     */ 
/*     */     
/*     */     public abstract FullHttpMessage copy();
/*     */     
/*     */     public abstract FullHttpMessage duplicate();
/*     */     
/*     */     public abstract FullHttpMessage retainedDuplicate();
/*     */   }
/*     */   
/*     */   private static final class AggregatedFullHttpRequest
/*     */     extends AggregatedFullHttpMessage
/*     */     implements FullHttpRequest
/*     */   {
/*     */     AggregatedFullHttpRequest(HttpRequest request, ByteBuf content, HttpHeaders trailingHeaders) {
/* 407 */       super(request, content, trailingHeaders);
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpRequest copy() {
/* 412 */       return replace(content().copy());
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpRequest duplicate() {
/* 417 */       return replace(content().duplicate());
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpRequest retainedDuplicate() {
/* 422 */       return replace(content().retainedDuplicate());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public FullHttpRequest replace(ByteBuf content) {
/* 428 */       DefaultFullHttpRequest dup = new DefaultFullHttpRequest(protocolVersion(), method(), uri(), content, headers().copy(), trailingHeaders().copy());
/* 429 */       dup.setDecoderResult(decoderResult());
/* 430 */       return dup;
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpRequest retain(int increment) {
/* 435 */       super.retain(increment);
/* 436 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpRequest retain() {
/* 441 */       super.retain();
/* 442 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpRequest touch() {
/* 447 */       super.touch();
/* 448 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpRequest touch(Object hint) {
/* 453 */       super.touch(hint);
/* 454 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpRequest setMethod(HttpMethod method) {
/* 459 */       ((HttpRequest)this.message).setMethod(method);
/* 460 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpRequest setUri(String uri) {
/* 465 */       ((HttpRequest)this.message).setUri(uri);
/* 466 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpMethod getMethod() {
/* 471 */       return ((HttpRequest)this.message).method();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getUri() {
/* 476 */       return ((HttpRequest)this.message).uri();
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpMethod method() {
/* 481 */       return getMethod();
/*     */     }
/*     */ 
/*     */     
/*     */     public String uri() {
/* 486 */       return getUri();
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpRequest setProtocolVersion(HttpVersion version) {
/* 491 */       super.setProtocolVersion(version);
/* 492 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 497 */       return HttpMessageUtil.appendFullRequest(new StringBuilder(256), this).toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class AggregatedFullHttpResponse
/*     */     extends AggregatedFullHttpMessage
/*     */     implements FullHttpResponse {
/*     */     AggregatedFullHttpResponse(HttpResponse message, ByteBuf content, HttpHeaders trailingHeaders) {
/* 505 */       super(message, content, trailingHeaders);
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpResponse copy() {
/* 510 */       return replace(content().copy());
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpResponse duplicate() {
/* 515 */       return replace(content().duplicate());
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpResponse retainedDuplicate() {
/* 520 */       return replace(content().retainedDuplicate());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public FullHttpResponse replace(ByteBuf content) {
/* 526 */       DefaultFullHttpResponse dup = new DefaultFullHttpResponse(getProtocolVersion(), getStatus(), content, headers().copy(), trailingHeaders().copy());
/* 527 */       dup.setDecoderResult(decoderResult());
/* 528 */       return dup;
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpResponse setStatus(HttpResponseStatus status) {
/* 533 */       ((HttpResponse)this.message).setStatus(status);
/* 534 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpResponseStatus getStatus() {
/* 539 */       return ((HttpResponse)this.message).status();
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpResponseStatus status() {
/* 544 */       return getStatus();
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpResponse setProtocolVersion(HttpVersion version) {
/* 549 */       super.setProtocolVersion(version);
/* 550 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpResponse retain(int increment) {
/* 555 */       super.retain(increment);
/* 556 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpResponse retain() {
/* 561 */       super.retain();
/* 562 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpResponse touch(Object hint) {
/* 567 */       super.touch(hint);
/* 568 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public FullHttpResponse touch() {
/* 573 */       super.touch();
/* 574 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 579 */       return HttpMessageUtil.appendFullResponse(new StringBuilder(256), this).toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\HttpObjectAggregator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */