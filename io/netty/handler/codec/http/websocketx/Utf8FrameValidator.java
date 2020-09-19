/*    */ package io.netty.handler.codec.http.websocketx;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import io.netty.channel.ChannelFutureListener;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*    */ import io.netty.handler.codec.CorruptedFrameException;
/*    */ import io.netty.util.concurrent.GenericFutureListener;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Utf8FrameValidator
/*    */   extends ChannelInboundHandlerAdapter
/*    */ {
/*    */   private int fragmentedFramesCount;
/*    */   private Utf8Validator utf8Validator;
/*    */   
/*    */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/* 35 */     if (msg instanceof WebSocketFrame) {
/* 36 */       WebSocketFrame frame = (WebSocketFrame)msg;
/*    */ 
/*    */ 
/*    */       
/* 40 */       if (((WebSocketFrame)msg).isFinalFragment()) {
/*    */ 
/*    */         
/* 43 */         if (!(frame instanceof PingWebSocketFrame)) {
/* 44 */           this.fragmentedFramesCount = 0;
/*    */ 
/*    */           
/* 47 */           if (frame instanceof TextWebSocketFrame || (this.utf8Validator != null && this.utf8Validator
/* 48 */             .isChecking()))
/*    */           {
/* 50 */             checkUTF8String(ctx, frame.content());
/*    */ 
/*    */ 
/*    */             
/* 54 */             this.utf8Validator.finish();
/*    */           }
/*    */         
/*    */         } 
/*    */       } else {
/*    */         
/* 60 */         if (this.fragmentedFramesCount == 0) {
/*    */           
/* 62 */           if (frame instanceof TextWebSocketFrame) {
/* 63 */             checkUTF8String(ctx, frame.content());
/*    */           
/*    */           }
/*    */         }
/* 67 */         else if (this.utf8Validator != null && this.utf8Validator.isChecking()) {
/* 68 */           checkUTF8String(ctx, frame.content());
/*    */         } 
/*    */ 
/*    */ 
/*    */         
/* 73 */         this.fragmentedFramesCount++;
/*    */       } 
/*    */     } 
/*    */     
/* 77 */     super.channelRead(ctx, msg);
/*    */   }
/*    */   
/*    */   private void checkUTF8String(ChannelHandlerContext ctx, ByteBuf buffer) {
/*    */     try {
/* 82 */       if (this.utf8Validator == null) {
/* 83 */         this.utf8Validator = new Utf8Validator();
/*    */       }
/* 85 */       this.utf8Validator.check(buffer);
/* 86 */     } catch (CorruptedFrameException ex) {
/* 87 */       if (ctx.channel().isActive())
/* 88 */         ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener((GenericFutureListener)ChannelFutureListener.CLOSE); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\websocketx\Utf8FrameValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */