/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.http.FullHttpMessage;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpMessage;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpStatusClass;
/*     */ import io.netty.handler.codec.http.HttpUtil;
/*     */ import io.netty.util.internal.ObjectUtil;
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
/*     */ public class InboundHttp2ToHttpAdapter
/*     */   extends Http2EventAdapter
/*     */ {
/*  43 */   private static final ImmediateSendDetector DEFAULT_SEND_DETECTOR = new ImmediateSendDetector()
/*     */     {
/*     */       public boolean mustSendImmediately(FullHttpMessage msg) {
/*  46 */         if (msg instanceof FullHttpResponse) {
/*  47 */           return (((FullHttpResponse)msg).status().codeClass() == HttpStatusClass.INFORMATIONAL);
/*     */         }
/*  49 */         if (msg instanceof FullHttpRequest) {
/*  50 */           return msg.headers().contains((CharSequence)HttpHeaderNames.EXPECT);
/*     */         }
/*  52 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public FullHttpMessage copyIfNeeded(FullHttpMessage msg) {
/*  57 */         if (msg instanceof FullHttpRequest) {
/*  58 */           FullHttpRequest copy = ((FullHttpRequest)msg).replace(Unpooled.buffer(0));
/*  59 */           copy.headers().remove((CharSequence)HttpHeaderNames.EXPECT);
/*  60 */           return (FullHttpMessage)copy;
/*     */         } 
/*  62 */         return null;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   private final int maxContentLength;
/*     */   
/*     */   private final ImmediateSendDetector sendDetector;
/*     */   
/*     */   private final Http2Connection.PropertyKey messageKey;
/*     */ 
/*     */   
/*     */   protected InboundHttp2ToHttpAdapter(Http2Connection connection, int maxContentLength, boolean validateHttpHeaders, boolean propagateSettings) {
/*  76 */     ObjectUtil.checkNotNull(connection, "connection");
/*  77 */     if (maxContentLength <= 0) {
/*  78 */       throw new IllegalArgumentException("maxContentLength: " + maxContentLength + " (expected: > 0)");
/*     */     }
/*  80 */     this.connection = connection;
/*  81 */     this.maxContentLength = maxContentLength;
/*  82 */     this.validateHttpHeaders = validateHttpHeaders;
/*  83 */     this.propagateSettings = propagateSettings;
/*  84 */     this.sendDetector = DEFAULT_SEND_DETECTOR;
/*  85 */     this.messageKey = connection.newKey();
/*     */   }
/*     */   private final boolean propagateSettings; protected final Http2Connection connection; protected final boolean validateHttpHeaders;
/*     */   private static interface ImmediateSendDetector {
/*     */     boolean mustSendImmediately(FullHttpMessage param1FullHttpMessage);
/*     */     
/*     */     FullHttpMessage copyIfNeeded(FullHttpMessage param1FullHttpMessage); }
/*     */   
/*     */   protected final void removeMessage(Http2Stream stream, boolean release) {
/*  94 */     FullHttpMessage msg = stream.<FullHttpMessage>removeProperty(this.messageKey);
/*  95 */     if (release && msg != null) {
/*  96 */       msg.release();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final FullHttpMessage getMessage(Http2Stream stream) {
/* 106 */     return stream.<FullHttpMessage>getProperty(this.messageKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void putMessage(Http2Stream stream, FullHttpMessage message) {
/* 115 */     FullHttpMessage previous = stream.<FullHttpMessage>setProperty(this.messageKey, message);
/* 116 */     if (previous != message && previous != null) {
/* 117 */       previous.release();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onStreamRemoved(Http2Stream stream) {
/* 123 */     removeMessage(stream, true);
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
/*     */   protected void fireChannelRead(ChannelHandlerContext ctx, FullHttpMessage msg, boolean release, Http2Stream stream) {
/* 136 */     removeMessage(stream, release);
/* 137 */     HttpUtil.setContentLength((HttpMessage)msg, msg.content().readableBytes());
/* 138 */     ctx.fireChannelRead(msg);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FullHttpMessage newMessage(Http2Stream stream, Http2Headers headers, boolean validateHttpHeaders, ByteBufAllocator alloc) throws Http2Exception {
/* 157 */     return this.connection.isServer() ? (FullHttpMessage)HttpConversionUtil.toFullHttpRequest(stream.id(), headers, alloc, validateHttpHeaders) : 
/* 158 */       (FullHttpMessage)HttpConversionUtil.toFullHttpResponse(stream.id(), headers, alloc, validateHttpHeaders);
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
/*     */   protected FullHttpMessage processHeadersBegin(ChannelHandlerContext ctx, Http2Stream stream, Http2Headers headers, boolean endOfStream, boolean allowAppend, boolean appendToTrailer) throws Http2Exception {
/* 188 */     FullHttpMessage msg = getMessage(stream);
/* 189 */     boolean release = true;
/* 190 */     if (msg == null) {
/* 191 */       msg = newMessage(stream, headers, this.validateHttpHeaders, ctx.alloc());
/* 192 */     } else if (allowAppend) {
/* 193 */       release = false;
/* 194 */       HttpConversionUtil.addHttp2ToHttpHeaders(stream.id(), headers, msg, appendToTrailer);
/*     */     } else {
/* 196 */       release = false;
/* 197 */       msg = null;
/*     */     } 
/*     */     
/* 200 */     if (this.sendDetector.mustSendImmediately(msg)) {
/*     */ 
/*     */       
/* 203 */       FullHttpMessage copy = endOfStream ? null : this.sendDetector.copyIfNeeded(msg);
/* 204 */       fireChannelRead(ctx, msg, release, stream);
/* 205 */       return copy;
/*     */     } 
/*     */     
/* 208 */     return msg;
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
/*     */   
/*     */   private void processHeadersEnd(ChannelHandlerContext ctx, Http2Stream stream, FullHttpMessage msg, boolean endOfStream) {
/* 222 */     if (endOfStream) {
/*     */       
/* 224 */       fireChannelRead(ctx, msg, (getMessage(stream) != msg), stream);
/*     */     } else {
/* 226 */       putMessage(stream, msg);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream) throws Http2Exception {
/* 233 */     Http2Stream stream = this.connection.stream(streamId);
/* 234 */     FullHttpMessage msg = getMessage(stream);
/* 235 */     if (msg == null) {
/* 236 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Data Frame received for unknown stream id %d", new Object[] { Integer.valueOf(streamId) });
/*     */     }
/*     */     
/* 239 */     ByteBuf content = msg.content();
/* 240 */     int dataReadableBytes = data.readableBytes();
/* 241 */     if (content.readableBytes() > this.maxContentLength - dataReadableBytes) {
/* 242 */       throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "Content length exceeded max of %d for stream id %d", new Object[] {
/* 243 */             Integer.valueOf(this.maxContentLength), Integer.valueOf(streamId)
/*     */           });
/*     */     }
/* 246 */     content.writeBytes(data, data.readerIndex(), dataReadableBytes);
/*     */     
/* 248 */     if (endOfStream) {
/* 249 */       fireChannelRead(ctx, msg, false, stream);
/*     */     }
/*     */ 
/*     */     
/* 253 */     return dataReadableBytes + padding;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endOfStream) throws Http2Exception {
/* 259 */     Http2Stream stream = this.connection.stream(streamId);
/* 260 */     FullHttpMessage msg = processHeadersBegin(ctx, stream, headers, endOfStream, true, true);
/* 261 */     if (msg != null) {
/* 262 */       processHeadersEnd(ctx, stream, msg, endOfStream);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream) throws Http2Exception {
/* 269 */     Http2Stream stream = this.connection.stream(streamId);
/* 270 */     FullHttpMessage msg = processHeadersBegin(ctx, stream, headers, endOfStream, true, true);
/* 271 */     if (msg != null) {
/*     */ 
/*     */       
/* 274 */       if (streamDependency != 0) {
/* 275 */         msg.headers().setInt((CharSequence)HttpConversionUtil.ExtensionHeaderNames.STREAM_DEPENDENCY_ID.text(), streamDependency);
/*     */       }
/*     */       
/* 278 */       msg.headers().setShort((CharSequence)HttpConversionUtil.ExtensionHeaderNames.STREAM_WEIGHT.text(), weight);
/*     */       
/* 280 */       processHeadersEnd(ctx, stream, msg, endOfStream);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRstStreamRead(ChannelHandlerContext ctx, int streamId, long errorCode) throws Http2Exception {
/* 286 */     Http2Stream stream = this.connection.stream(streamId);
/* 287 */     FullHttpMessage msg = getMessage(stream);
/* 288 */     if (msg != null) {
/* 289 */       onRstStreamRead(stream, msg);
/*     */     }
/* 291 */     ctx.fireExceptionCaught(Http2Exception.streamError(streamId, Http2Error.valueOf(errorCode), "HTTP/2 to HTTP layer caught stream reset", new Object[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onPushPromiseRead(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding) throws Http2Exception {
/* 299 */     Http2Stream promisedStream = this.connection.stream(promisedStreamId);
/* 300 */     if (headers.status() == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 306 */       headers.status((CharSequence)HttpResponseStatus.OK.codeAsText());
/*     */     }
/* 308 */     FullHttpMessage msg = processHeadersBegin(ctx, promisedStream, headers, false, false, false);
/* 309 */     if (msg == null) {
/* 310 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Push Promise Frame received for pre-existing stream id %d", new Object[] {
/* 311 */             Integer.valueOf(promisedStreamId)
/*     */           });
/*     */     }
/* 314 */     msg.headers().setInt((CharSequence)HttpConversionUtil.ExtensionHeaderNames.STREAM_PROMISE_ID.text(), streamId);
/* 315 */     msg.headers().setShort((CharSequence)HttpConversionUtil.ExtensionHeaderNames.STREAM_WEIGHT.text(), (short)16);
/*     */ 
/*     */     
/* 318 */     processHeadersEnd(ctx, promisedStream, msg, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSettingsRead(ChannelHandlerContext ctx, Http2Settings settings) throws Http2Exception {
/* 323 */     if (this.propagateSettings)
/*     */     {
/* 325 */       ctx.fireChannelRead(settings);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onRstStreamRead(Http2Stream stream, FullHttpMessage msg) {
/* 333 */     removeMessage(stream, true);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\InboundHttp2ToHttpAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */