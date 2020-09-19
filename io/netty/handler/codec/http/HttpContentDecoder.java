/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.embedded.EmbeddedChannel;
/*     */ import io.netty.handler.codec.CodecException;
/*     */ import io.netty.handler.codec.MessageToMessageDecoder;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class HttpContentDecoder
/*     */   extends MessageToMessageDecoder<HttpObject>
/*     */ {
/*  48 */   static final String IDENTITY = HttpHeaderValues.IDENTITY.toString();
/*     */   
/*     */   protected ChannelHandlerContext ctx;
/*     */   
/*     */   private EmbeddedChannel decoder;
/*     */   private boolean continueResponse;
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, HttpObject msg, List<Object> out) throws Exception {
/*  56 */     if (msg instanceof HttpResponse && ((HttpResponse)msg).status().code() == 100) {
/*     */       
/*  58 */       if (!(msg instanceof LastHttpContent)) {
/*  59 */         this.continueResponse = true;
/*     */       }
/*     */       
/*  62 */       out.add(ReferenceCountUtil.retain(msg));
/*     */       
/*     */       return;
/*     */     } 
/*  66 */     if (this.continueResponse) {
/*  67 */       if (msg instanceof LastHttpContent) {
/*  68 */         this.continueResponse = false;
/*     */       }
/*     */       
/*  71 */       out.add(ReferenceCountUtil.retain(msg));
/*     */       
/*     */       return;
/*     */     } 
/*  75 */     if (msg instanceof HttpMessage) {
/*  76 */       cleanup();
/*  77 */       HttpMessage message = (HttpMessage)msg;
/*  78 */       HttpHeaders headers = message.headers();
/*     */ 
/*     */       
/*  81 */       String contentEncoding = headers.get((CharSequence)HttpHeaderNames.CONTENT_ENCODING);
/*  82 */       if (contentEncoding != null) {
/*  83 */         contentEncoding = contentEncoding.trim();
/*     */       } else {
/*  85 */         contentEncoding = IDENTITY;
/*     */       } 
/*  87 */       this.decoder = newContentDecoder(contentEncoding);
/*     */       
/*  89 */       if (this.decoder == null) {
/*  90 */         if (message instanceof HttpContent) {
/*  91 */           ((HttpContent)message).retain();
/*     */         }
/*  93 */         out.add(message);
/*     */ 
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */ 
/*     */       
/* 101 */       if (headers.contains((CharSequence)HttpHeaderNames.CONTENT_LENGTH)) {
/* 102 */         headers.remove((CharSequence)HttpHeaderNames.CONTENT_LENGTH);
/* 103 */         headers.set((CharSequence)HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 109 */       CharSequence targetContentEncoding = getTargetContentEncoding(contentEncoding);
/* 110 */       if (HttpHeaderValues.IDENTITY.contentEquals(targetContentEncoding)) {
/*     */ 
/*     */         
/* 113 */         headers.remove((CharSequence)HttpHeaderNames.CONTENT_ENCODING);
/*     */       } else {
/* 115 */         headers.set((CharSequence)HttpHeaderNames.CONTENT_ENCODING, targetContentEncoding);
/*     */       } 
/*     */       
/* 118 */       if (message instanceof HttpContent) {
/*     */         HttpMessage copy;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 124 */         if (message instanceof HttpRequest) {
/* 125 */           HttpRequest r = (HttpRequest)message;
/* 126 */           copy = new DefaultHttpRequest(r.protocolVersion(), r.method(), r.uri());
/* 127 */         } else if (message instanceof HttpResponse) {
/* 128 */           HttpResponse r = (HttpResponse)message;
/* 129 */           copy = new DefaultHttpResponse(r.protocolVersion(), r.status());
/*     */         } else {
/* 131 */           throw new CodecException("Object of class " + message.getClass().getName() + " is not a HttpRequest or HttpResponse");
/*     */         } 
/*     */         
/* 134 */         copy.headers().set(message.headers());
/* 135 */         copy.setDecoderResult(message.decoderResult());
/* 136 */         out.add(copy);
/*     */       } else {
/* 138 */         out.add(message);
/*     */       } 
/*     */     } 
/*     */     
/* 142 */     if (msg instanceof HttpContent) {
/* 143 */       HttpContent c = (HttpContent)msg;
/* 144 */       if (this.decoder == null) {
/* 145 */         out.add(c.retain());
/*     */       } else {
/* 147 */         decodeContent(c, out);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void decodeContent(HttpContent c, List<Object> out) {
/* 153 */     ByteBuf content = c.content();
/*     */     
/* 155 */     decode(content, out);
/*     */     
/* 157 */     if (c instanceof LastHttpContent) {
/* 158 */       finishDecode(out);
/*     */       
/* 160 */       LastHttpContent last = (LastHttpContent)c;
/*     */ 
/*     */       
/* 163 */       HttpHeaders headers = last.trailingHeaders();
/* 164 */       if (headers.isEmpty()) {
/* 165 */         out.add(LastHttpContent.EMPTY_LAST_CONTENT);
/*     */       } else {
/* 167 */         out.add(new ComposedLastHttpContent(headers));
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getTargetContentEncoding(String contentEncoding) throws Exception {
/* 193 */     return IDENTITY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
/* 198 */     cleanupSafely(ctx);
/* 199 */     super.handlerRemoved(ctx);
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/* 204 */     cleanupSafely(ctx);
/* 205 */     super.channelInactive(ctx);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
/* 210 */     this.ctx = ctx;
/* 211 */     super.handlerAdded(ctx);
/*     */   }
/*     */   
/*     */   private void cleanup() {
/* 215 */     if (this.decoder != null) {
/*     */       
/* 217 */       this.decoder.finishAndReleaseAll();
/* 218 */       this.decoder = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void cleanupSafely(ChannelHandlerContext ctx) {
/*     */     try {
/* 224 */       cleanup();
/* 225 */     } catch (Throwable cause) {
/*     */ 
/*     */       
/* 228 */       ctx.fireExceptionCaught(cause);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void decode(ByteBuf in, List<Object> out) {
/* 234 */     this.decoder.writeInbound(new Object[] { in.retain() });
/* 235 */     fetchDecoderOutput(out);
/*     */   }
/*     */   
/*     */   private void finishDecode(List<Object> out) {
/* 239 */     if (this.decoder.finish()) {
/* 240 */       fetchDecoderOutput(out);
/*     */     }
/* 242 */     this.decoder = null;
/*     */   }
/*     */   
/*     */   private void fetchDecoderOutput(List<Object> out) {
/*     */     while (true) {
/* 247 */       ByteBuf buf = (ByteBuf)this.decoder.readInbound();
/* 248 */       if (buf == null) {
/*     */         break;
/*     */       }
/* 251 */       if (!buf.isReadable()) {
/* 252 */         buf.release();
/*     */         continue;
/*     */       } 
/* 255 */       out.add(new DefaultHttpContent(buf));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract EmbeddedChannel newContentDecoder(String paramString) throws Exception;
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\HttpContentDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */