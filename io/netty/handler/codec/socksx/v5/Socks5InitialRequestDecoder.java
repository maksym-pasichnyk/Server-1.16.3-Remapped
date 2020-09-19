/*    */ package io.netty.handler.codec.socksx.v5;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.DecoderException;
/*    */ import io.netty.handler.codec.DecoderResult;
/*    */ import io.netty.handler.codec.ReplayingDecoder;
/*    */ import io.netty.handler.codec.socksx.SocksVersion;
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
/*    */ public class Socks5InitialRequestDecoder
/*    */   extends ReplayingDecoder<Socks5InitialRequestDecoder.State>
/*    */ {
/*    */   enum State
/*    */   {
/* 38 */     INIT,
/* 39 */     SUCCESS,
/* 40 */     FAILURE;
/*    */   }
/*    */   
/*    */   public Socks5InitialRequestDecoder() {
/* 44 */     super(State.INIT); } protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception { try {
/*    */       byte version;
/*    */       int readableBytes;
/*    */       int authMethodCnt;
/*    */       Socks5AuthMethod[] authMethods;
/*    */       int i;
/* 50 */       switch ((State)state()) {
/*    */         case INIT:
/* 52 */           version = in.readByte();
/* 53 */           if (version != SocksVersion.SOCKS5.byteValue()) {
/* 54 */             throw new DecoderException("unsupported version: " + version + " (expected: " + SocksVersion.SOCKS5
/* 55 */                 .byteValue() + ')');
/*    */           }
/*    */           
/* 58 */           authMethodCnt = in.readUnsignedByte();
/* 59 */           if (actualReadableBytes() < authMethodCnt) {
/*    */             break;
/*    */           }
/*    */           
/* 63 */           authMethods = new Socks5AuthMethod[authMethodCnt];
/* 64 */           for (i = 0; i < authMethodCnt; i++) {
/* 65 */             authMethods[i] = Socks5AuthMethod.valueOf(in.readByte());
/*    */           }
/*    */           
/* 68 */           out.add(new DefaultSocks5InitialRequest(authMethods));
/* 69 */           checkpoint(State.SUCCESS);
/*    */         
/*    */         case SUCCESS:
/* 72 */           readableBytes = actualReadableBytes();
/* 73 */           if (readableBytes > 0) {
/* 74 */             out.add(in.readRetainedSlice(readableBytes));
/*    */           }
/*    */           break;
/*    */         
/*    */         case FAILURE:
/* 79 */           in.skipBytes(actualReadableBytes());
/*    */           break;
/*    */       } 
/*    */     
/* 83 */     } catch (Exception e) {
/* 84 */       fail(out, e);
/*    */     }  }
/*    */   
/*    */   private void fail(List<Object> out, Exception cause) {
/*    */     DecoderException decoderException;
/* 89 */     if (!(cause instanceof DecoderException)) {
/* 90 */       decoderException = new DecoderException(cause);
/*    */     }
/*    */     
/* 93 */     checkpoint(State.FAILURE);
/*    */     
/* 95 */     Socks5Message m = new DefaultSocks5InitialRequest(new Socks5AuthMethod[] { Socks5AuthMethod.NO_AUTH });
/* 96 */     m.setDecoderResult(DecoderResult.failure((Throwable)decoderException));
/* 97 */     out.add(m);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\socksx\v5\Socks5InitialRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */