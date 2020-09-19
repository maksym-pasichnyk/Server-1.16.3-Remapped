/*    */ package io.netty.handler.codec.socks;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandler;
/*    */ import io.netty.channel.ChannelHandlerContext;
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
/*    */ public class SocksAuthResponseDecoder
/*    */   extends ReplayingDecoder<SocksAuthResponseDecoder.State>
/*    */ {
/*    */   public SocksAuthResponseDecoder() {
/* 32 */     super(State.CHECK_PROTOCOL_VERSION);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
/*    */     SocksAuthStatus authStatus;
/* 38 */     switch ((State)state()) {
/*    */       case CHECK_PROTOCOL_VERSION:
/* 40 */         if (byteBuf.readByte() != SocksSubnegotiationVersion.AUTH_PASSWORD.byteValue()) {
/* 41 */           out.add(SocksCommonUtils.UNKNOWN_SOCKS_RESPONSE);
/*    */           break;
/*    */         } 
/* 44 */         checkpoint(State.READ_AUTH_RESPONSE);
/*    */       
/*    */       case READ_AUTH_RESPONSE:
/* 47 */         authStatus = SocksAuthStatus.valueOf(byteBuf.readByte());
/* 48 */         out.add(new SocksAuthResponse(authStatus));
/*    */         break;
/*    */       
/*    */       default:
/* 52 */         throw new Error();
/*    */     } 
/*    */     
/* 55 */     channelHandlerContext.pipeline().remove((ChannelHandler)this);
/*    */   }
/*    */   
/*    */   enum State {
/* 59 */     CHECK_PROTOCOL_VERSION,
/* 60 */     READ_AUTH_RESPONSE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\socks\SocksAuthResponseDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */