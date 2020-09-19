/*    */ package io.netty.handler.codec.socks;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandler;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.ReplayingDecoder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
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
/*    */ public class SocksInitRequestDecoder
/*    */   extends ReplayingDecoder<SocksInitRequestDecoder.State>
/*    */ {
/*    */   public SocksInitRequestDecoder() {
/* 34 */     super(State.CHECK_PROTOCOL_VERSION);
/*    */   }
/*    */   protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
/*    */     byte authSchemeNum;
/*    */     List<SocksAuthScheme> authSchemes;
/* 39 */     switch ((State)state()) {
/*    */       case CHECK_PROTOCOL_VERSION:
/* 41 */         if (byteBuf.readByte() != SocksProtocolVersion.SOCKS5.byteValue()) {
/* 42 */           out.add(SocksCommonUtils.UNKNOWN_SOCKS_REQUEST);
/*    */           break;
/*    */         } 
/* 45 */         checkpoint(State.READ_AUTH_SCHEMES);
/*    */       
/*    */       case READ_AUTH_SCHEMES:
/* 48 */         authSchemeNum = byteBuf.readByte();
/*    */         
/* 50 */         if (authSchemeNum > 0) {
/* 51 */           authSchemes = new ArrayList<SocksAuthScheme>(authSchemeNum);
/* 52 */           for (int i = 0; i < authSchemeNum; i++) {
/* 53 */             authSchemes.add(SocksAuthScheme.valueOf(byteBuf.readByte()));
/*    */           }
/*    */         } else {
/* 56 */           authSchemes = Collections.emptyList();
/*    */         } 
/* 58 */         out.add(new SocksInitRequest(authSchemes));
/*    */         break;
/*    */       
/*    */       default:
/* 62 */         throw new Error();
/*    */     } 
/*    */     
/* 65 */     ctx.pipeline().remove((ChannelHandler)this);
/*    */   }
/*    */   
/*    */   enum State {
/* 69 */     CHECK_PROTOCOL_VERSION,
/* 70 */     READ_AUTH_SCHEMES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\socks\SocksInitRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */