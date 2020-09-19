/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufHolder;
/*    */ import io.netty.handler.codec.dns.DnsRecord;
/*    */ import java.net.IDN;
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class DnsAddressDecoder
/*    */ {
/*    */   private static final int INADDRSZ4 = 4;
/*    */   private static final int INADDRSZ6 = 16;
/*    */   
/*    */   static InetAddress decodeAddress(DnsRecord record, String name, boolean decodeIdn) {
/* 46 */     if (!(record instanceof io.netty.handler.codec.dns.DnsRawRecord)) {
/* 47 */       return null;
/*    */     }
/* 49 */     ByteBuf content = ((ByteBufHolder)record).content();
/* 50 */     int contentLen = content.readableBytes();
/* 51 */     if (contentLen != 4 && contentLen != 16) {
/* 52 */       return null;
/*    */     }
/*    */     
/* 55 */     byte[] addrBytes = new byte[contentLen];
/* 56 */     content.getBytes(content.readerIndex(), addrBytes);
/*    */     
/*    */     try {
/* 59 */       return InetAddress.getByAddress(decodeIdn ? IDN.toUnicode(name) : name, addrBytes);
/* 60 */     } catch (UnknownHostException e) {
/*    */       
/* 62 */       throw new Error(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\DnsAddressDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */