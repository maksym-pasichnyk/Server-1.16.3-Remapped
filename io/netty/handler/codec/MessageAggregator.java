/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.CompositeByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MessageAggregator<I, S, C extends ByteBufHolder, O extends ByteBufHolder>
/*     */   extends MessageToMessageDecoder<I>
/*     */ {
/*     */   private static final int DEFAULT_MAX_COMPOSITEBUFFER_COMPONENTS = 1024;
/*     */   private final int maxContentLength;
/*     */   private O currentMessage;
/*     */   private boolean handlingOversizedMessage;
/*  60 */   private int maxCumulationBufferComponents = 1024;
/*     */ 
/*     */ 
/*     */   
/*     */   private ChannelHandlerContext ctx;
/*     */ 
/*     */ 
/*     */   
/*     */   private ChannelFutureListener continueResponseWriteListener;
/*     */ 
/*     */ 
/*     */   
/*     */   protected MessageAggregator(int maxContentLength) {
/*  73 */     validateMaxContentLength(maxContentLength);
/*  74 */     this.maxContentLength = maxContentLength;
/*     */   }
/*     */   
/*     */   protected MessageAggregator(int maxContentLength, Class<? extends I> inboundMessageType) {
/*  78 */     super(inboundMessageType);
/*  79 */     validateMaxContentLength(maxContentLength);
/*  80 */     this.maxContentLength = maxContentLength;
/*     */   }
/*     */   
/*     */   private static void validateMaxContentLength(int maxContentLength) {
/*  84 */     if (maxContentLength < 0) {
/*  85 */       throw new IllegalArgumentException("maxContentLength: " + maxContentLength + " (expected: >= 0)");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean acceptInboundMessage(Object msg) throws Exception {
/*  92 */     if (!super.acceptInboundMessage(msg)) {
/*  93 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  97 */     I in = (I)msg;
/*     */     
/*  99 */     return ((isContentMessage(in) || isStartMessage(in)) && !isAggregated(in));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean isStartMessage(I paramI) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean isContentMessage(I paramI) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean isLastContentMessage(C paramC) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean isAggregated(I paramI) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int maxContentLength() {
/* 143 */     return this.maxContentLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int maxCumulationBufferComponents() {
/* 153 */     return this.maxCumulationBufferComponents;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setMaxCumulationBufferComponents(int maxCumulationBufferComponents) {
/* 164 */     if (maxCumulationBufferComponents < 2) {
/* 165 */       throw new IllegalArgumentException("maxCumulationBufferComponents: " + maxCumulationBufferComponents + " (expected: >= 2)");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 170 */     if (this.ctx == null) {
/* 171 */       this.maxCumulationBufferComponents = maxCumulationBufferComponents;
/*     */     } else {
/* 173 */       throw new IllegalStateException("decoder properties cannot be changed once the decoder is added to a pipeline.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final boolean isHandlingOversizedMessage() {
/* 183 */     return this.handlingOversizedMessage;
/*     */   }
/*     */   
/*     */   protected final ChannelHandlerContext ctx() {
/* 187 */     if (this.ctx == null) {
/* 188 */       throw new IllegalStateException("not added to a pipeline yet");
/*     */     }
/* 190 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void decode(final ChannelHandlerContext ctx, I msg, List<Object> out) throws Exception {
/* 195 */     if (isStartMessage(msg)) {
/* 196 */       this.handlingOversizedMessage = false;
/* 197 */       if (this.currentMessage != null) {
/* 198 */         this.currentMessage.release();
/* 199 */         this.currentMessage = null;
/* 200 */         throw new MessageAggregationException();
/*     */       } 
/*     */ 
/*     */       
/* 204 */       I i = msg;
/*     */ 
/*     */ 
/*     */       
/* 208 */       Object continueResponse = newContinueResponse((S)i, this.maxContentLength, ctx.pipeline());
/* 209 */       if (continueResponse != null) {
/*     */         
/* 211 */         ChannelFutureListener listener = this.continueResponseWriteListener;
/* 212 */         if (listener == null) {
/* 213 */           this.continueResponseWriteListener = listener = new ChannelFutureListener()
/*     */             {
/*     */               public void operationComplete(ChannelFuture future) throws Exception {
/* 216 */                 if (!future.isSuccess()) {
/* 217 */                   ctx.fireExceptionCaught(future.cause());
/*     */                 }
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */         
/* 224 */         boolean closeAfterWrite = closeAfterContinueResponse(continueResponse);
/* 225 */         this.handlingOversizedMessage = ignoreContentAfterContinueResponse(continueResponse);
/*     */         
/* 227 */         ChannelFuture future = ctx.writeAndFlush(continueResponse).addListener((GenericFutureListener)listener);
/*     */         
/* 229 */         if (closeAfterWrite) {
/* 230 */           future.addListener((GenericFutureListener)ChannelFutureListener.CLOSE);
/*     */           return;
/*     */         } 
/* 233 */         if (this.handlingOversizedMessage) {
/*     */           return;
/*     */         }
/* 236 */       } else if (isContentLengthInvalid((S)i, this.maxContentLength)) {
/*     */         
/* 238 */         invokeHandleOversizedMessage(ctx, (S)i);
/*     */         
/*     */         return;
/*     */       } 
/* 242 */       if (i instanceof DecoderResultProvider && !((DecoderResultProvider)i).decoderResult().isSuccess()) {
/*     */         O aggregated;
/* 244 */         if (i instanceof ByteBufHolder) {
/* 245 */           aggregated = beginAggregation((S)i, ((ByteBufHolder)i).content().retain());
/*     */         } else {
/* 247 */           aggregated = beginAggregation((S)i, Unpooled.EMPTY_BUFFER);
/*     */         } 
/* 249 */         finishAggregation(aggregated);
/* 250 */         out.add(aggregated);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 255 */       CompositeByteBuf content = ctx.alloc().compositeBuffer(this.maxCumulationBufferComponents);
/* 256 */       if (i instanceof ByteBufHolder) {
/* 257 */         appendPartialContent(content, ((ByteBufHolder)i).content());
/*     */       }
/* 259 */       this.currentMessage = beginAggregation((S)i, (ByteBuf)content);
/* 260 */     } else if (isContentMessage(msg)) {
/* 261 */       boolean last; if (this.currentMessage == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 268 */       CompositeByteBuf content = (CompositeByteBuf)this.currentMessage.content();
/*     */ 
/*     */       
/* 271 */       ByteBufHolder byteBufHolder = (ByteBufHolder)msg;
/*     */       
/* 273 */       if (content.readableBytes() > this.maxContentLength - byteBufHolder.content().readableBytes()) {
/*     */ 
/*     */         
/* 276 */         O o = this.currentMessage;
/* 277 */         invokeHandleOversizedMessage(ctx, (S)o);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 282 */       appendPartialContent(content, byteBufHolder.content());
/*     */ 
/*     */       
/* 285 */       aggregate(this.currentMessage, (C)byteBufHolder);
/*     */ 
/*     */       
/* 288 */       if (byteBufHolder instanceof DecoderResultProvider) {
/* 289 */         DecoderResult decoderResult = ((DecoderResultProvider)byteBufHolder).decoderResult();
/* 290 */         if (!decoderResult.isSuccess()) {
/* 291 */           if (this.currentMessage instanceof DecoderResultProvider) {
/* 292 */             ((DecoderResultProvider)this.currentMessage).setDecoderResult(
/* 293 */                 DecoderResult.failure(decoderResult.cause()));
/*     */           }
/* 295 */           last = true;
/*     */         } else {
/* 297 */           last = isLastContentMessage((C)byteBufHolder);
/*     */         } 
/*     */       } else {
/* 300 */         last = isLastContentMessage((C)byteBufHolder);
/*     */       } 
/*     */       
/* 303 */       if (last) {
/* 304 */         finishAggregation(this.currentMessage);
/*     */ 
/*     */         
/* 307 */         out.add(this.currentMessage);
/* 308 */         this.currentMessage = null;
/*     */       } 
/*     */     } else {
/* 311 */       throw new MessageAggregationException();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void appendPartialContent(CompositeByteBuf content, ByteBuf partialContent) {
/* 316 */     if (partialContent.isReadable()) {
/* 317 */       content.addComponent(true, partialContent.retain());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean isContentLengthInvalid(S paramS, int paramInt) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Object newContinueResponse(S paramS, int paramInt, ChannelPipeline paramChannelPipeline) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean closeAfterContinueResponse(Object paramObject) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean ignoreContentAfterContinueResponse(Object paramObject) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract O beginAggregation(S paramS, ByteBuf paramByteBuf) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void aggregate(O aggregated, C content) throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void finishAggregation(O aggregated) throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void invokeHandleOversizedMessage(ChannelHandlerContext ctx, S oversized) throws Exception {
/* 380 */     this.handlingOversizedMessage = true;
/* 381 */     this.currentMessage = null;
/*     */     try {
/* 383 */       handleOversizedMessage(ctx, oversized);
/*     */     } finally {
/*     */       
/* 386 */       ReferenceCountUtil.release(oversized);
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
/*     */   protected void handleOversizedMessage(ChannelHandlerContext ctx, S oversized) throws Exception {
/* 398 */     ctx.fireExceptionCaught(new TooLongFrameException("content length exceeded " + 
/* 399 */           maxContentLength() + " bytes."));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
/* 407 */     if (this.currentMessage != null && !ctx.channel().config().isAutoRead()) {
/* 408 */       ctx.read();
/*     */     }
/* 410 */     ctx.fireChannelReadComplete();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/*     */     try {
/* 417 */       super.channelInactive(ctx);
/*     */     } finally {
/* 419 */       releaseCurrentMessage();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
/* 425 */     this.ctx = ctx;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
/*     */     try {
/* 431 */       super.handlerRemoved(ctx);
/*     */     }
/*     */     finally {
/*     */       
/* 435 */       releaseCurrentMessage();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void releaseCurrentMessage() {
/* 440 */     if (this.currentMessage != null) {
/* 441 */       this.currentMessage.release();
/* 442 */       this.currentMessage = null;
/* 443 */       this.handlingOversizedMessage = false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\MessageAggregator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */