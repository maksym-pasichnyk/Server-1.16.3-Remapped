/*     */ package io.netty.handler.codec.spdy;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.MessageToMessageDecoder;
/*     */ import io.netty.handler.codec.TooLongFrameException;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpRequest;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpResponse;
/*     */ import io.netty.handler.codec.http.FullHttpMessage;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpMessage;
/*     */ import io.netty.handler.codec.http.HttpMethod;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpUtil;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class SpdyHttpDecoder
/*     */   extends MessageToMessageDecoder<SpdyFrame>
/*     */ {
/*     */   private final boolean validateHeaders;
/*     */   private final int spdyVersion;
/*     */   private final int maxContentLength;
/*     */   private final Map<Integer, FullHttpMessage> messageMap;
/*     */   
/*     */   public SpdyHttpDecoder(SpdyVersion version, int maxContentLength) {
/*  62 */     this(version, maxContentLength, new HashMap<Integer, FullHttpMessage>(), true);
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
/*     */   public SpdyHttpDecoder(SpdyVersion version, int maxContentLength, boolean validateHeaders) {
/*  75 */     this(version, maxContentLength, new HashMap<Integer, FullHttpMessage>(), validateHeaders);
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
/*     */   protected SpdyHttpDecoder(SpdyVersion version, int maxContentLength, Map<Integer, FullHttpMessage> messageMap) {
/*  88 */     this(version, maxContentLength, messageMap, true);
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
/*     */   protected SpdyHttpDecoder(SpdyVersion version, int maxContentLength, Map<Integer, FullHttpMessage> messageMap, boolean validateHeaders) {
/* 103 */     if (version == null) {
/* 104 */       throw new NullPointerException("version");
/*     */     }
/* 106 */     if (maxContentLength <= 0) {
/* 107 */       throw new IllegalArgumentException("maxContentLength must be a positive integer: " + maxContentLength);
/*     */     }
/*     */     
/* 110 */     this.spdyVersion = version.getVersion();
/* 111 */     this.maxContentLength = maxContentLength;
/* 112 */     this.messageMap = messageMap;
/* 113 */     this.validateHeaders = validateHeaders;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/* 119 */     for (Map.Entry<Integer, FullHttpMessage> entry : this.messageMap.entrySet()) {
/* 120 */       ReferenceCountUtil.safeRelease(entry.getValue());
/*     */     }
/* 122 */     this.messageMap.clear();
/* 123 */     super.channelInactive(ctx);
/*     */   }
/*     */   
/*     */   protected FullHttpMessage putMessage(int streamId, FullHttpMessage message) {
/* 127 */     return this.messageMap.put(Integer.valueOf(streamId), message);
/*     */   }
/*     */   
/*     */   protected FullHttpMessage getMessage(int streamId) {
/* 131 */     return this.messageMap.get(Integer.valueOf(streamId));
/*     */   }
/*     */   
/*     */   protected FullHttpMessage removeMessage(int streamId) {
/* 135 */     return this.messageMap.remove(Integer.valueOf(streamId));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, SpdyFrame msg, List<Object> out) throws Exception {
/* 141 */     if (msg instanceof SpdySynStreamFrame) {
/*     */ 
/*     */       
/* 144 */       SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame)msg;
/* 145 */       int streamId = spdySynStreamFrame.streamId();
/*     */       
/* 147 */       if (SpdyCodecUtil.isServerId(streamId)) {
/*     */         
/* 149 */         int associatedToStreamId = spdySynStreamFrame.associatedStreamId();
/*     */ 
/*     */ 
/*     */         
/* 153 */         if (associatedToStreamId == 0) {
/* 154 */           SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INVALID_STREAM);
/*     */           
/* 156 */           ctx.writeAndFlush(spdyRstStreamFrame);
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/*     */ 
/*     */         
/* 163 */         if (spdySynStreamFrame.isLast()) {
/* 164 */           SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
/*     */           
/* 166 */           ctx.writeAndFlush(spdyRstStreamFrame);
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 172 */         if (spdySynStreamFrame.isTruncated()) {
/* 173 */           SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INTERNAL_ERROR);
/*     */           
/* 175 */           ctx.writeAndFlush(spdyRstStreamFrame);
/*     */           
/*     */           return;
/*     */         } 
/*     */         try {
/* 180 */           FullHttpRequest httpRequestWithEntity = createHttpRequest(spdySynStreamFrame, ctx.alloc());
/*     */ 
/*     */           
/* 183 */           httpRequestWithEntity.headers().setInt((CharSequence)SpdyHttpHeaders.Names.STREAM_ID, streamId);
/* 184 */           httpRequestWithEntity.headers().setInt((CharSequence)SpdyHttpHeaders.Names.ASSOCIATED_TO_STREAM_ID, associatedToStreamId);
/* 185 */           httpRequestWithEntity.headers().setInt((CharSequence)SpdyHttpHeaders.Names.PRIORITY, spdySynStreamFrame.priority());
/*     */           
/* 187 */           out.add(httpRequestWithEntity);
/*     */         }
/* 189 */         catch (Throwable ignored) {
/* 190 */           SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
/*     */           
/* 192 */           ctx.writeAndFlush(spdyRstStreamFrame);
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 199 */         if (spdySynStreamFrame.isTruncated()) {
/* 200 */           SpdySynReplyFrame spdySynReplyFrame = new DefaultSpdySynReplyFrame(streamId);
/* 201 */           spdySynReplyFrame.setLast(true);
/* 202 */           SpdyHeaders frameHeaders = spdySynReplyFrame.headers();
/* 203 */           frameHeaders.setInt(SpdyHeaders.HttpNames.STATUS, HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE.code());
/* 204 */           frameHeaders.setObject(SpdyHeaders.HttpNames.VERSION, HttpVersion.HTTP_1_0);
/* 205 */           ctx.writeAndFlush(spdySynReplyFrame);
/*     */           
/*     */           return;
/*     */         } 
/*     */         try {
/* 210 */           FullHttpRequest httpRequestWithEntity = createHttpRequest(spdySynStreamFrame, ctx.alloc());
/*     */ 
/*     */           
/* 213 */           httpRequestWithEntity.headers().setInt((CharSequence)SpdyHttpHeaders.Names.STREAM_ID, streamId);
/*     */           
/* 215 */           if (spdySynStreamFrame.isLast()) {
/* 216 */             out.add(httpRequestWithEntity);
/*     */           } else {
/*     */             
/* 219 */             putMessage(streamId, (FullHttpMessage)httpRequestWithEntity);
/*     */           } 
/* 221 */         } catch (Throwable t) {
/*     */ 
/*     */ 
/*     */           
/* 225 */           SpdySynReplyFrame spdySynReplyFrame = new DefaultSpdySynReplyFrame(streamId);
/* 226 */           spdySynReplyFrame.setLast(true);
/* 227 */           SpdyHeaders frameHeaders = spdySynReplyFrame.headers();
/* 228 */           frameHeaders.setInt(SpdyHeaders.HttpNames.STATUS, HttpResponseStatus.BAD_REQUEST.code());
/* 229 */           frameHeaders.setObject(SpdyHeaders.HttpNames.VERSION, HttpVersion.HTTP_1_0);
/* 230 */           ctx.writeAndFlush(spdySynReplyFrame);
/*     */         }
/*     */       
/*     */       } 
/* 234 */     } else if (msg instanceof SpdySynReplyFrame) {
/*     */       
/* 236 */       SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame)msg;
/* 237 */       int streamId = spdySynReplyFrame.streamId();
/*     */ 
/*     */ 
/*     */       
/* 241 */       if (spdySynReplyFrame.isTruncated()) {
/* 242 */         SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INTERNAL_ERROR);
/*     */         
/* 244 */         ctx.writeAndFlush(spdyRstStreamFrame);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/* 250 */         FullHttpResponse httpResponseWithEntity = createHttpResponse(spdySynReplyFrame, ctx.alloc(), this.validateHeaders);
/*     */ 
/*     */         
/* 253 */         httpResponseWithEntity.headers().setInt((CharSequence)SpdyHttpHeaders.Names.STREAM_ID, streamId);
/*     */         
/* 255 */         if (spdySynReplyFrame.isLast()) {
/* 256 */           HttpUtil.setContentLength((HttpMessage)httpResponseWithEntity, 0L);
/* 257 */           out.add(httpResponseWithEntity);
/*     */         } else {
/*     */           
/* 260 */           putMessage(streamId, (FullHttpMessage)httpResponseWithEntity);
/*     */         } 
/* 262 */       } catch (Throwable t) {
/*     */ 
/*     */         
/* 265 */         SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
/*     */         
/* 267 */         ctx.writeAndFlush(spdyRstStreamFrame);
/*     */       }
/*     */     
/* 270 */     } else if (msg instanceof SpdyHeadersFrame) {
/*     */       FullHttpResponse fullHttpResponse;
/* 272 */       SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame)msg;
/* 273 */       int streamId = spdyHeadersFrame.streamId();
/* 274 */       FullHttpMessage fullHttpMessage = getMessage(streamId);
/*     */       
/* 276 */       if (fullHttpMessage == null) {
/*     */         
/* 278 */         if (SpdyCodecUtil.isServerId(streamId)) {
/*     */ 
/*     */ 
/*     */           
/* 282 */           if (spdyHeadersFrame.isTruncated()) {
/* 283 */             SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INTERNAL_ERROR);
/*     */             
/* 285 */             ctx.writeAndFlush(spdyRstStreamFrame);
/*     */             
/*     */             return;
/*     */           } 
/*     */           try {
/* 290 */             fullHttpResponse = createHttpResponse(spdyHeadersFrame, ctx.alloc(), this.validateHeaders);
/*     */ 
/*     */             
/* 293 */             fullHttpResponse.headers().setInt((CharSequence)SpdyHttpHeaders.Names.STREAM_ID, streamId);
/*     */             
/* 295 */             if (spdyHeadersFrame.isLast()) {
/* 296 */               HttpUtil.setContentLength((HttpMessage)fullHttpResponse, 0L);
/* 297 */               out.add(fullHttpResponse);
/*     */             } else {
/*     */               
/* 300 */               putMessage(streamId, (FullHttpMessage)fullHttpResponse);
/*     */             } 
/* 302 */           } catch (Throwable t) {
/*     */ 
/*     */             
/* 305 */             SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
/*     */             
/* 307 */             ctx.writeAndFlush(spdyRstStreamFrame);
/*     */           } 
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 314 */       if (!spdyHeadersFrame.isTruncated()) {
/* 315 */         for (Map.Entry<CharSequence, CharSequence> e : (Iterable<Map.Entry<CharSequence, CharSequence>>)spdyHeadersFrame.headers()) {
/* 316 */           fullHttpResponse.headers().add(e.getKey(), e.getValue());
/*     */         }
/*     */       }
/*     */       
/* 320 */       if (spdyHeadersFrame.isLast()) {
/* 321 */         HttpUtil.setContentLength((HttpMessage)fullHttpResponse, fullHttpResponse.content().readableBytes());
/* 322 */         removeMessage(streamId);
/* 323 */         out.add(fullHttpResponse);
/*     */       }
/*     */     
/* 326 */     } else if (msg instanceof SpdyDataFrame) {
/*     */       
/* 328 */       SpdyDataFrame spdyDataFrame = (SpdyDataFrame)msg;
/* 329 */       int streamId = spdyDataFrame.streamId();
/* 330 */       FullHttpMessage fullHttpMessage = getMessage(streamId);
/*     */ 
/*     */       
/* 333 */       if (fullHttpMessage == null) {
/*     */         return;
/*     */       }
/*     */       
/* 337 */       ByteBuf content = fullHttpMessage.content();
/* 338 */       if (content.readableBytes() > this.maxContentLength - spdyDataFrame.content().readableBytes()) {
/* 339 */         removeMessage(streamId);
/* 340 */         throw new TooLongFrameException("HTTP content length exceeded " + this.maxContentLength + " bytes.");
/*     */       } 
/*     */ 
/*     */       
/* 344 */       ByteBuf spdyDataFrameData = spdyDataFrame.content();
/* 345 */       int spdyDataFrameDataLen = spdyDataFrameData.readableBytes();
/* 346 */       content.writeBytes(spdyDataFrameData, spdyDataFrameData.readerIndex(), spdyDataFrameDataLen);
/*     */       
/* 348 */       if (spdyDataFrame.isLast()) {
/* 349 */         HttpUtil.setContentLength((HttpMessage)fullHttpMessage, content.readableBytes());
/* 350 */         removeMessage(streamId);
/* 351 */         out.add(fullHttpMessage);
/*     */       }
/*     */     
/* 354 */     } else if (msg instanceof SpdyRstStreamFrame) {
/*     */       
/* 356 */       SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame)msg;
/* 357 */       int streamId = spdyRstStreamFrame.streamId();
/* 358 */       removeMessage(streamId);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static FullHttpRequest createHttpRequest(SpdyHeadersFrame requestFrame, ByteBufAllocator alloc) throws Exception {
/* 365 */     SpdyHeaders headers = requestFrame.headers();
/* 366 */     HttpMethod method = HttpMethod.valueOf(headers.getAsString((CharSequence)SpdyHeaders.HttpNames.METHOD));
/* 367 */     String url = headers.getAsString((CharSequence)SpdyHeaders.HttpNames.PATH);
/* 368 */     HttpVersion httpVersion = HttpVersion.valueOf(headers.getAsString((CharSequence)SpdyHeaders.HttpNames.VERSION));
/* 369 */     headers.remove(SpdyHeaders.HttpNames.METHOD);
/* 370 */     headers.remove(SpdyHeaders.HttpNames.PATH);
/* 371 */     headers.remove(SpdyHeaders.HttpNames.VERSION);
/*     */     
/* 373 */     boolean release = true;
/* 374 */     ByteBuf buffer = alloc.buffer();
/*     */     try {
/* 376 */       DefaultFullHttpRequest defaultFullHttpRequest = new DefaultFullHttpRequest(httpVersion, method, url, buffer);
/*     */ 
/*     */       
/* 379 */       headers.remove(SpdyHeaders.HttpNames.SCHEME);
/*     */ 
/*     */       
/* 382 */       CharSequence host = (CharSequence)headers.get(SpdyHeaders.HttpNames.HOST);
/* 383 */       headers.remove(SpdyHeaders.HttpNames.HOST);
/* 384 */       defaultFullHttpRequest.headers().set((CharSequence)HttpHeaderNames.HOST, host);
/*     */       
/* 386 */       for (Map.Entry<CharSequence, CharSequence> e : (Iterable<Map.Entry<CharSequence, CharSequence>>)requestFrame.headers()) {
/* 387 */         defaultFullHttpRequest.headers().add(e.getKey(), e.getValue());
/*     */       }
/*     */ 
/*     */       
/* 391 */       HttpUtil.setKeepAlive((HttpMessage)defaultFullHttpRequest, true);
/*     */ 
/*     */       
/* 394 */       defaultFullHttpRequest.headers().remove((CharSequence)HttpHeaderNames.TRANSFER_ENCODING);
/* 395 */       release = false;
/* 396 */       return (FullHttpRequest)defaultFullHttpRequest;
/*     */     } finally {
/* 398 */       if (release) {
/* 399 */         buffer.release();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static FullHttpResponse createHttpResponse(SpdyHeadersFrame responseFrame, ByteBufAllocator alloc, boolean validateHeaders) throws Exception {
/* 408 */     SpdyHeaders headers = responseFrame.headers();
/* 409 */     HttpResponseStatus status = HttpResponseStatus.parseLine((CharSequence)headers.get(SpdyHeaders.HttpNames.STATUS));
/* 410 */     HttpVersion version = HttpVersion.valueOf(headers.getAsString((CharSequence)SpdyHeaders.HttpNames.VERSION));
/* 411 */     headers.remove(SpdyHeaders.HttpNames.STATUS);
/* 412 */     headers.remove(SpdyHeaders.HttpNames.VERSION);
/*     */     
/* 414 */     boolean release = true;
/* 415 */     ByteBuf buffer = alloc.buffer();
/*     */     try {
/* 417 */       DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(version, status, buffer, validateHeaders);
/* 418 */       for (Map.Entry<CharSequence, CharSequence> e : (Iterable<Map.Entry<CharSequence, CharSequence>>)responseFrame.headers()) {
/* 419 */         defaultFullHttpResponse.headers().add(e.getKey(), e.getValue());
/*     */       }
/*     */ 
/*     */       
/* 423 */       HttpUtil.setKeepAlive((HttpMessage)defaultFullHttpResponse, true);
/*     */ 
/*     */       
/* 426 */       defaultFullHttpResponse.headers().remove((CharSequence)HttpHeaderNames.TRANSFER_ENCODING);
/* 427 */       defaultFullHttpResponse.headers().remove((CharSequence)HttpHeaderNames.TRAILER);
/*     */       
/* 429 */       release = false;
/* 430 */       return (FullHttpResponse)defaultFullHttpResponse;
/*     */     } finally {
/* 432 */       if (release)
/* 433 */         buffer.release(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\spdy\SpdyHttpDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */