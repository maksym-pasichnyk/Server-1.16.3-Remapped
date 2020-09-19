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
/*    */ public class SocksInitResponseDecoder
/*    */   extends ReplayingDecoder<SocksInitResponseDecoder.State>
/*    */ {
/*    */   public SocksInitResponseDecoder() {
/* 32 */     super(State.CHECK_PROTOCOL_VERSION);
/*    */   }
/*    */   
/*    */   protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
/*    */     SocksAuthScheme authScheme;
/* 37 */     switch ((State)state()) {
/*    */       case CHECK_PROTOCOL_VERSION:
/* 39 */         if (byteBuf.readByte() != SocksProtocolVersion.SOCKS5.byteValue()) {
/* 40 */           out.add(SocksCommonUtils.UNKNOWN_SOCKS_RESPONSE);
/*    */           break;
/*    */         } 
/* 43 */         checkpoint(State.READ_PREFERRED_AUTH_TYPE);
/*    */       
/*    */       case READ_PREFERRED_AUTH_TYPE:
/* 46 */         authScheme = SocksAuthScheme.valueOf(byteBuf.readByte());
/* 47 */         out.add(new SocksInitResponse(authScheme));
/*    */         break;
/*    */       
/*    */       default:
/* 51 */         throw new Error();
/*    */     } 
/*    */     
/* 54 */     ctx.pipeline().remove((ChannelHandler)this);
/*    */   }
/*    */   
/*    */   enum State {
/* 58 */     CHECK_PROTOCOL_VERSION,
/* 59 */     READ_PREFERRED_AUTH_TYPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\socks\SocksInitResponseDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */