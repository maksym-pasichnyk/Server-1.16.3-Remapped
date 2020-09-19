/*    */ package io.netty.handler.codec.socks;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandler;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.ReplayingDecoder;
/*    */ import io.netty.util.NetUtil;
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
/*    */ public class SocksCmdResponseDecoder
/*    */   extends ReplayingDecoder<SocksCmdResponseDecoder.State>
/*    */ {
/*    */   private SocksCmdStatus cmdStatus;
/*    */   private SocksAddressType addressType;
/*    */   
/*    */   public SocksCmdResponseDecoder() {
/* 36 */     super(State.CHECK_PROTOCOL_VERSION); } protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception { String host; int fieldLength;
/*    */     byte[] bytes;
/*    */     int port;
/*    */     String str1;
/*    */     int i;
/* 41 */     switch ((State)state()) {
/*    */       case CHECK_PROTOCOL_VERSION:
/* 43 */         if (byteBuf.readByte() != SocksProtocolVersion.SOCKS5.byteValue()) {
/* 44 */           out.add(SocksCommonUtils.UNKNOWN_SOCKS_RESPONSE);
/*    */           break;
/*    */         } 
/* 47 */         checkpoint(State.READ_CMD_HEADER);
/*    */       
/*    */       case READ_CMD_HEADER:
/* 50 */         this.cmdStatus = SocksCmdStatus.valueOf(byteBuf.readByte());
/* 51 */         byteBuf.skipBytes(1);
/* 52 */         this.addressType = SocksAddressType.valueOf(byteBuf.readByte());
/* 53 */         checkpoint(State.READ_CMD_ADDRESS);
/*    */       
/*    */       case READ_CMD_ADDRESS:
/* 56 */         switch (this.addressType) {
/*    */           case CHECK_PROTOCOL_VERSION:
/* 58 */             host = NetUtil.intToIpAddress(byteBuf.readInt());
/* 59 */             port = byteBuf.readUnsignedShort();
/* 60 */             out.add(new SocksCmdResponse(this.cmdStatus, this.addressType, host, port));
/*    */             break;
/*    */           
/*    */           case READ_CMD_HEADER:
/* 64 */             fieldLength = byteBuf.readByte();
/* 65 */             str1 = SocksCommonUtils.readUsAscii(byteBuf, fieldLength);
/* 66 */             i = byteBuf.readUnsignedShort();
/* 67 */             out.add(new SocksCmdResponse(this.cmdStatus, this.addressType, str1, i));
/*    */             break;
/*    */           
/*    */           case READ_CMD_ADDRESS:
/* 71 */             bytes = new byte[16];
/* 72 */             byteBuf.readBytes(bytes);
/* 73 */             str1 = SocksCommonUtils.ipv6toStr(bytes);
/* 74 */             i = byteBuf.readUnsignedShort();
/* 75 */             out.add(new SocksCmdResponse(this.cmdStatus, this.addressType, str1, i));
/*    */             break;
/*    */           
/*    */           case null:
/* 79 */             out.add(SocksCommonUtils.UNKNOWN_SOCKS_RESPONSE);
/*    */             break;
/*    */         } 
/*    */         
/* 83 */         throw new Error();
/*    */ 
/*    */ 
/*    */ 
/*    */       
/*    */       default:
/* 89 */         throw new Error();
/*    */     } 
/*    */     
/* 92 */     ctx.pipeline().remove((ChannelHandler)this); }
/*    */ 
/*    */   
/*    */   enum State {
/* 96 */     CHECK_PROTOCOL_VERSION,
/* 97 */     READ_CMD_HEADER,
/* 98 */     READ_CMD_ADDRESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\socks\SocksCmdResponseDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */