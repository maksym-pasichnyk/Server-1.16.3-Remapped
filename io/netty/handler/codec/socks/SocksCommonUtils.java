/*    */ package io.netty.handler.codec.socks;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.util.CharsetUtil;
/*    */ import io.netty.util.internal.StringUtil;
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
/*    */ final class SocksCommonUtils
/*    */ {
/* 23 */   public static final SocksRequest UNKNOWN_SOCKS_REQUEST = new UnknownSocksRequest();
/* 24 */   public static final SocksResponse UNKNOWN_SOCKS_RESPONSE = new UnknownSocksResponse();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final char ipv6hextetSeparator = ':';
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String ipv6toStr(byte[] src) {
/* 39 */     assert src.length == 16;
/* 40 */     StringBuilder sb = new StringBuilder(39);
/* 41 */     ipv6toStr(sb, src, 0, 8);
/* 42 */     return sb.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   private static void ipv6toStr(StringBuilder sb, byte[] src, int fromHextet, int toHextet) {
/* 47 */     toHextet--; int i;
/* 48 */     for (i = fromHextet; i < toHextet; i++) {
/* 49 */       appendHextet(sb, src, i);
/* 50 */       sb.append(':');
/*    */     } 
/*    */     
/* 53 */     appendHextet(sb, src, i);
/*    */   }
/*    */   
/*    */   private static void appendHextet(StringBuilder sb, byte[] src, int i) {
/* 57 */     StringUtil.toHexString(sb, src, i << 1, 2);
/*    */   }
/*    */   
/*    */   static String readUsAscii(ByteBuf buffer, int length) {
/* 61 */     String s = buffer.toString(buffer.readerIndex(), length, CharsetUtil.US_ASCII);
/* 62 */     buffer.skipBytes(length);
/* 63 */     return s;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\socks\SocksCommonUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */