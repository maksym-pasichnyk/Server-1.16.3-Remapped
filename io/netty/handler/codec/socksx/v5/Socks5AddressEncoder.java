/*    */ package io.netty.handler.codec.socksx.v5;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.handler.codec.EncoderException;
/*    */ import io.netty.util.CharsetUtil;
/*    */ import io.netty.util.NetUtil;
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
/*    */ public interface Socks5AddressEncoder
/*    */ {
/* 32 */   public static final Socks5AddressEncoder DEFAULT = new Socks5AddressEncoder()
/*    */     {
/*    */       public void encodeAddress(Socks5AddressType addrType, String addrValue, ByteBuf out) throws Exception {
/* 35 */         byte typeVal = addrType.byteValue();
/* 36 */         if (typeVal == Socks5AddressType.IPv4.byteValue()) {
/* 37 */           if (addrValue != null) {
/* 38 */             out.writeBytes(NetUtil.createByteArrayFromIpAddressString(addrValue));
/*    */           } else {
/* 40 */             out.writeInt(0);
/*    */           } 
/* 42 */         } else if (typeVal == Socks5AddressType.DOMAIN.byteValue()) {
/* 43 */           if (addrValue != null) {
/* 44 */             out.writeByte(addrValue.length());
/* 45 */             out.writeCharSequence(addrValue, CharsetUtil.US_ASCII);
/*    */           } else {
/* 47 */             out.writeByte(1);
/* 48 */             out.writeByte(0);
/*    */           } 
/* 50 */         } else if (typeVal == Socks5AddressType.IPv6.byteValue()) {
/* 51 */           if (addrValue != null) {
/* 52 */             out.writeBytes(NetUtil.createByteArrayFromIpAddressString(addrValue));
/*    */           } else {
/* 54 */             out.writeLong(0L);
/* 55 */             out.writeLong(0L);
/*    */           } 
/*    */         } else {
/* 58 */           throw new EncoderException("unsupported addrType: " + (addrType.byteValue() & 0xFF));
/*    */         } 
/*    */       }
/*    */     };
/*    */   
/*    */   void encodeAddress(Socks5AddressType paramSocks5AddressType, String paramString, ByteBuf paramByteBuf) throws Exception;
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\socksx\v5\Socks5AddressEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */