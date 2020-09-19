/*    */ package io.netty.handler.codec.socksx.v5;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.DecoderException;
/*    */ import io.netty.handler.codec.DecoderResult;
/*    */ import io.netty.handler.codec.ReplayingDecoder;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Socks5PasswordAuthResponseDecoder
/*    */   extends ReplayingDecoder<Socks5PasswordAuthResponseDecoder.State>
/*    */ {
/*    */   enum State
/*    */   {
/* 37 */     INIT,
/* 38 */     SUCCESS,
/* 39 */     FAILURE;
/*    */   }
/*    */   
/*    */   public Socks5PasswordAuthResponseDecoder() {
/* 43 */     super(State.INIT);
/*    */   }
/*    */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/*    */     try {
/*    */       byte version;
/*    */       int readableBytes;
/* 49 */       switch ((State)state()) {
/*    */         case INIT:
/* 51 */           version = in.readByte();
/* 52 */           if (version != 1) {
/* 53 */             throw new DecoderException("unsupported subnegotiation version: " + version + " (expected: 1)");
/*    */           }
/*    */           
/* 56 */           out.add(new DefaultSocks5PasswordAuthResponse(Socks5PasswordAuthStatus.valueOf(in.readByte())));
/* 57 */           checkpoint(State.SUCCESS);
/*    */         
/*    */         case SUCCESS:
/* 60 */           readableBytes = actualReadableBytes();
/* 61 */           if (readableBytes > 0) {
/* 62 */             out.add(in.readRetainedSlice(readableBytes));
/*    */           }
/*    */           break;
/*    */         
/*    */         case FAILURE:
/* 67 */           in.skipBytes(actualReadableBytes());
/*    */           break;
/*    */       } 
/*    */     
/* 71 */     } catch (Exception e) {
/* 72 */       fail(out, e);
/*    */     } 
/*    */   }
/*    */   private void fail(List<Object> out, Exception cause) {
/*    */     DecoderException decoderException;
/* 77 */     if (!(cause instanceof DecoderException)) {
/* 78 */       decoderException = new DecoderException(cause);
/*    */     }
/*    */     
/* 81 */     checkpoint(State.FAILURE);
/*    */     
/* 83 */     Socks5Message m = new DefaultSocks5PasswordAuthResponse(Socks5PasswordAuthStatus.FAILURE);
/* 84 */     m.setDecoderResult(DecoderResult.failure((Throwable)decoderException));
/* 85 */     out.add(m);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\socksx\v5\Socks5PasswordAuthResponseDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */