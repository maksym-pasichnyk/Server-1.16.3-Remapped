/*    */ package io.netty.handler.codec.socksx;
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
/*    */ public enum SocksVersion
/*    */ {
/* 26 */   SOCKS4a((byte)4),
/*    */ 
/*    */ 
/*    */   
/* 30 */   SOCKS5((byte)5),
/*    */ 
/*    */ 
/*    */   
/* 34 */   UNKNOWN((byte)-1);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final byte b;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   SocksVersion(byte b) {
/* 55 */     this.b = b;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte byteValue() {
/* 62 */     return this.b;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\socksx\SocksVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */