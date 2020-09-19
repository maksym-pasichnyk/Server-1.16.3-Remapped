/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.embedded.EmbeddedChannel;
/*     */ import io.netty.handler.codec.MessageToMessageCodec;
/*     */ import io.netty.util.ReferenceCountUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class HttpContentEncoder
/*     */   extends MessageToMessageCodec<HttpRequest, HttpObject>
/*     */ {
/*     */   private enum State
/*     */   {
/*  54 */     PASS_THROUGH,
/*  55 */     AWAIT_HEADERS,
/*  56 */     AWAIT_CONTENT;
/*     */   }
/*     */   
/*  59 */   private static final CharSequence ZERO_LENGTH_HEAD = "HEAD";
/*  60 */   private static final CharSequence ZERO_LENGTH_CONNECT = "CONNECT";
/*  61 */   private static final int CONTINUE_CODE = HttpResponseStatus.CONTINUE.code();
/*     */   
/*  63 */   private final Queue<CharSequence> acceptEncodingQueue = new ArrayDeque<CharSequence>();
/*     */   private EmbeddedChannel encoder;
/*  65 */   private State state = State.AWAIT_HEADERS;
/*     */ 
/*     */   
/*     */   public boolean acceptOutboundMessage(Object msg) throws Exception {
/*  69 */     return (msg instanceof HttpContent || msg instanceof HttpResponse);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, HttpRequest msg, List<Object> out) throws Exception {
/*  75 */     CharSequence acceptedEncoding = msg.headers().get((CharSequence)HttpHeaderNames.ACCEPT_ENCODING);
/*  76 */     if (acceptedEncoding == null) {
/*  77 */       acceptedEncoding = HttpContentDecoder.IDENTITY;
/*     */     }
/*     */     
/*  80 */     HttpMethod meth = msg.method();
/*  81 */     if (meth == HttpMethod.HEAD) {
/*  82 */       acceptedEncoding = ZERO_LENGTH_HEAD;
/*  83 */     } else if (meth == HttpMethod.CONNECT) {
/*  84 */       acceptedEncoding = ZERO_LENGTH_CONNECT;
/*     */     } 
/*     */     
/*  87 */     this.acceptEncodingQueue.add(acceptedEncoding);
/*  88 */     out.add(ReferenceCountUtil.retain(msg)); } protected void encode(ChannelHandlerContext ctx, HttpObject msg, List<Object> out) throws Exception {
/*     */     HttpResponse res;
/*     */     int code;
/*     */     CharSequence acceptEncoding;
/*     */     Result result;
/*  93 */     boolean isFull = (msg instanceof HttpResponse && msg instanceof LastHttpContent);
/*  94 */     switch (this.state) {
/*     */       case AWAIT_HEADERS:
/*  96 */         ensureHeaders(msg);
/*  97 */         assert this.encoder == null;
/*     */         
/*  99 */         res = (HttpResponse)msg;
/* 100 */         code = res.status().code();
/*     */         
/* 102 */         if (code == CONTINUE_CODE) {
/*     */ 
/*     */           
/* 105 */           acceptEncoding = null;
/*     */         } else {
/*     */           
/* 108 */           acceptEncoding = this.acceptEncodingQueue.poll();
/* 109 */           if (acceptEncoding == null) {
/* 110 */             throw new IllegalStateException("cannot send more responses than requests");
/*     */           }
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 127 */         if (isPassthru(res.protocolVersion(), code, acceptEncoding)) {
/* 128 */           if (isFull) {
/* 129 */             out.add(ReferenceCountUtil.retain(res)); break;
/*     */           } 
/* 131 */           out.add(res);
/*     */           
/* 133 */           this.state = State.PASS_THROUGH;
/*     */           
/*     */           break;
/*     */         } 
/*     */         
/* 138 */         if (isFull)
/*     */         {
/* 140 */           if (!((ByteBufHolder)res).content().isReadable()) {
/* 141 */             out.add(ReferenceCountUtil.retain(res));
/*     */             
/*     */             break;
/*     */           } 
/*     */         }
/*     */         
/* 147 */         result = beginEncode(res, acceptEncoding.toString());
/*     */ 
/*     */         
/* 150 */         if (result == null) {
/* 151 */           if (isFull) {
/* 152 */             out.add(ReferenceCountUtil.retain(res)); break;
/*     */           } 
/* 154 */           out.add(res);
/*     */           
/* 156 */           this.state = State.PASS_THROUGH;
/*     */           
/*     */           break;
/*     */         } 
/*     */         
/* 161 */         this.encoder = result.contentEncoder();
/*     */ 
/*     */ 
/*     */         
/* 165 */         res.headers().set((CharSequence)HttpHeaderNames.CONTENT_ENCODING, result.targetContentEncoding());
/*     */ 
/*     */         
/* 168 */         if (isFull) {
/*     */           
/* 170 */           HttpResponse newRes = new DefaultHttpResponse(res.protocolVersion(), res.status());
/* 171 */           newRes.headers().set(res.headers());
/* 172 */           out.add(newRes);
/*     */           
/* 174 */           ensureContent(res);
/* 175 */           encodeFullResponse(newRes, (HttpContent)res, out);
/*     */           
/*     */           break;
/*     */         } 
/* 179 */         res.headers().remove((CharSequence)HttpHeaderNames.CONTENT_LENGTH);
/* 180 */         res.headers().set((CharSequence)HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
/*     */         
/* 182 */         out.add(res);
/* 183 */         this.state = State.AWAIT_CONTENT;
/* 184 */         if (!(msg instanceof HttpContent)) {
/*     */           break;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case AWAIT_CONTENT:
/* 193 */         ensureContent(msg);
/* 194 */         if (encodeContent((HttpContent)msg, out)) {
/* 195 */           this.state = State.AWAIT_HEADERS;
/*     */         }
/*     */         break;
/*     */       
/*     */       case PASS_THROUGH:
/* 200 */         ensureContent(msg);
/* 201 */         out.add(ReferenceCountUtil.retain(msg));
/*     */         
/* 203 */         if (msg instanceof LastHttpContent) {
/* 204 */           this.state = State.AWAIT_HEADERS;
/*     */         }
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void encodeFullResponse(HttpResponse newRes, HttpContent content, List<Object> out) {
/* 212 */     int existingMessages = out.size();
/* 213 */     encodeContent(content, out);
/*     */     
/* 215 */     if (HttpUtil.isContentLengthSet(newRes)) {
/*     */       
/* 217 */       int messageSize = 0;
/* 218 */       for (int i = existingMessages; i < out.size(); i++) {
/* 219 */         Object item = out.get(i);
/* 220 */         if (item instanceof HttpContent) {
/* 221 */           messageSize += ((HttpContent)item).content().readableBytes();
/*     */         }
/*     */       } 
/* 224 */       HttpUtil.setContentLength(newRes, messageSize);
/*     */     } else {
/* 226 */       newRes.headers().set((CharSequence)HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isPassthru(HttpVersion version, int code, CharSequence httpMethod) {
/* 231 */     return (code < 200 || code == 204 || code == 304 || httpMethod == ZERO_LENGTH_HEAD || (httpMethod == ZERO_LENGTH_CONNECT && code == 200) || version == HttpVersion.HTTP_1_0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void ensureHeaders(HttpObject msg) {
/* 237 */     if (!(msg instanceof HttpResponse)) {
/* 238 */       throw new IllegalStateException("unexpected message type: " + msg
/*     */           
/* 240 */           .getClass().getName() + " (expected: " + HttpResponse.class.getSimpleName() + ')');
/*     */     }
/*     */   }
/*     */   
/*     */   private static void ensureContent(HttpObject msg) {
/* 245 */     if (!(msg instanceof HttpContent)) {
/* 246 */       throw new IllegalStateException("unexpected message type: " + msg
/*     */           
/* 248 */           .getClass().getName() + " (expected: " + HttpContent.class.getSimpleName() + ')');
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean encodeContent(HttpContent c, List<Object> out) {
/* 253 */     ByteBuf content = c.content();
/*     */     
/* 255 */     encode(content, out);
/*     */     
/* 257 */     if (c instanceof LastHttpContent) {
/* 258 */       finishEncode(out);
/* 259 */       LastHttpContent last = (LastHttpContent)c;
/*     */ 
/*     */ 
/*     */       
/* 263 */       HttpHeaders headers = last.trailingHeaders();
/* 264 */       if (headers.isEmpty()) {
/* 265 */         out.add(LastHttpContent.EMPTY_LAST_CONTENT);
/*     */       } else {
/* 267 */         out.add(new ComposedLastHttpContent(headers));
/*     */       } 
/* 269 */       return true;
/*     */     } 
/* 271 */     return false;
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
/*     */   public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
/* 292 */     cleanupSafely(ctx);
/* 293 */     super.handlerRemoved(ctx);
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/* 298 */     cleanupSafely(ctx);
/* 299 */     super.channelInactive(ctx);
/*     */   }
/*     */   
/*     */   private void cleanup() {
/* 303 */     if (this.encoder != null) {
/*     */       
/* 305 */       this.encoder.finishAndReleaseAll();
/* 306 */       this.encoder = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void cleanupSafely(ChannelHandlerContext ctx) {
/*     */     try {
/* 312 */       cleanup();
/* 313 */     } catch (Throwable cause) {
/*     */ 
/*     */       
/* 316 */       ctx.fireExceptionCaught(cause);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void encode(ByteBuf in, List<Object> out) {
/* 322 */     this.encoder.writeOutbound(new Object[] { in.retain() });
/* 323 */     fetchEncoderOutput(out);
/*     */   }
/*     */   
/*     */   private void finishEncode(List<Object> out) {
/* 327 */     if (this.encoder.finish()) {
/* 328 */       fetchEncoderOutput(out);
/*     */     }
/* 330 */     this.encoder = null;
/*     */   }
/*     */   
/*     */   private void fetchEncoderOutput(List<Object> out) {
/*     */     while (true) {
/* 335 */       ByteBuf buf = (ByteBuf)this.encoder.readOutbound();
/* 336 */       if (buf == null) {
/*     */         break;
/*     */       }
/* 339 */       if (!buf.isReadable()) {
/* 340 */         buf.release();
/*     */         continue;
/*     */       } 
/* 343 */       out.add(new DefaultHttpContent(buf));
/*     */     } 
/*     */   }
/*     */   protected abstract Result beginEncode(HttpResponse paramHttpResponse, String paramString) throws Exception;
/*     */   
/*     */   public static final class Result { private final String targetContentEncoding;
/*     */     private final EmbeddedChannel contentEncoder;
/*     */     
/*     */     public Result(String targetContentEncoding, EmbeddedChannel contentEncoder) {
/* 352 */       if (targetContentEncoding == null) {
/* 353 */         throw new NullPointerException("targetContentEncoding");
/*     */       }
/* 355 */       if (contentEncoder == null) {
/* 356 */         throw new NullPointerException("contentEncoder");
/*     */       }
/*     */       
/* 359 */       this.targetContentEncoding = targetContentEncoding;
/* 360 */       this.contentEncoder = contentEncoder;
/*     */     }
/*     */     
/*     */     public String targetContentEncoding() {
/* 364 */       return this.targetContentEncoding;
/*     */     }
/*     */     
/*     */     public EmbeddedChannel contentEncoder() {
/* 368 */       return this.contentEncoder;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\HttpContentEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */