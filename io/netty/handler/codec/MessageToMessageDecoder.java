/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.internal.TypeParameterMatcher;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MessageToMessageDecoder<I>
/*     */   extends ChannelInboundHandlerAdapter
/*     */ {
/*     */   private final TypeParameterMatcher matcher;
/*     */   
/*     */   protected MessageToMessageDecoder() {
/*  60 */     this.matcher = TypeParameterMatcher.find(this, MessageToMessageDecoder.class, "I");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MessageToMessageDecoder(Class<? extends I> inboundMessageType) {
/*  69 */     this.matcher = TypeParameterMatcher.get(inboundMessageType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean acceptInboundMessage(Object msg) throws Exception {
/*  77 */     return this.matcher.match(msg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/*  82 */     CodecOutputList out = CodecOutputList.newInstance();
/*     */     try {
/*  84 */       if (acceptInboundMessage(msg)) {
/*     */         
/*  86 */         I cast = (I)msg;
/*     */         try {
/*  88 */           decode(ctx, cast, out);
/*     */         } finally {
/*  90 */           ReferenceCountUtil.release(cast);
/*     */         } 
/*     */       } else {
/*  93 */         out.add(msg);
/*     */       } 
/*  95 */     } catch (DecoderException e) {
/*  96 */       throw e;
/*  97 */     } catch (Exception e) {
/*  98 */       throw new DecoderException(e);
/*     */     } finally {
/* 100 */       int size = out.size();
/* 101 */       for (int i = 0; i < size; i++) {
/* 102 */         ctx.fireChannelRead(out.getUnsafe(i));
/*     */       }
/* 104 */       out.recycle();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract void decode(ChannelHandlerContext paramChannelHandlerContext, I paramI, List<Object> paramList) throws Exception;
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\MessageToMessageDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */