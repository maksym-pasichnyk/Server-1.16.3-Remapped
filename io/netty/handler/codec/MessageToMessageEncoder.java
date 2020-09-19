/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelOutboundHandlerAdapter;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.internal.StringUtil;
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
/*     */ public abstract class MessageToMessageEncoder<I>
/*     */   extends ChannelOutboundHandlerAdapter
/*     */ {
/*     */   private final TypeParameterMatcher matcher;
/*     */   
/*     */   protected MessageToMessageEncoder() {
/*  59 */     this.matcher = TypeParameterMatcher.find(this, MessageToMessageEncoder.class, "I");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MessageToMessageEncoder(Class<? extends I> outboundMessageType) {
/*  68 */     this.matcher = TypeParameterMatcher.get(outboundMessageType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean acceptOutboundMessage(Object msg) throws Exception {
/*  76 */     return this.matcher.match(msg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/*  81 */     CodecOutputList out = null;
/*     */     try {
/*  83 */       if (acceptOutboundMessage(msg)) {
/*  84 */         out = CodecOutputList.newInstance();
/*     */         
/*  86 */         I cast = (I)msg;
/*     */         try {
/*  88 */           encode(ctx, cast, out);
/*     */         } finally {
/*  90 */           ReferenceCountUtil.release(cast);
/*     */         } 
/*     */         
/*  93 */         if (out.isEmpty()) {
/*  94 */           out.recycle();
/*  95 */           out = null;
/*     */           
/*  97 */           throw new EncoderException(
/*  98 */               StringUtil.simpleClassName(this) + " must produce at least one message.");
/*     */         } 
/*     */       } else {
/* 101 */         ctx.write(msg, promise);
/*     */       } 
/* 103 */     } catch (EncoderException e) {
/* 104 */       throw e;
/* 105 */     } catch (Throwable t) {
/* 106 */       throw new EncoderException(t);
/*     */     } finally {
/* 108 */       if (out != null) {
/* 109 */         int sizeMinusOne = out.size() - 1;
/* 110 */         if (sizeMinusOne == 0) {
/* 111 */           ctx.write(out.get(0), promise);
/* 112 */         } else if (sizeMinusOne > 0) {
/*     */ 
/*     */           
/* 115 */           ChannelPromise voidPromise = ctx.voidPromise();
/* 116 */           boolean isVoidPromise = (promise == voidPromise);
/* 117 */           for (int i = 0; i < sizeMinusOne; i++) {
/*     */             ChannelPromise p;
/* 119 */             if (isVoidPromise) {
/* 120 */               p = voidPromise;
/*     */             } else {
/* 122 */               p = ctx.newPromise();
/*     */             } 
/* 124 */             ctx.write(out.getUnsafe(i), p);
/*     */           } 
/* 126 */           ctx.write(out.getUnsafe(sizeMinusOne), promise);
/*     */         } 
/* 128 */         out.recycle();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract void encode(ChannelHandlerContext paramChannelHandlerContext, I paramI, List<Object> paramList) throws Exception;
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\MessageToMessageEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */