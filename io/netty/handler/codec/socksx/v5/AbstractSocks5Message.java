/*    */ package io.netty.handler.codec.socksx.v5;
/*    */ 
/*    */ import io.netty.handler.codec.socksx.AbstractSocksMessage;
/*    */ import io.netty.handler.codec.socksx.SocksVersion;
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
/*    */ public abstract class AbstractSocks5Message
/*    */   extends AbstractSocksMessage
/*    */   implements Socks5Message
/*    */ {
/*    */   public final SocksVersion version() {
/* 28 */     return SocksVersion.SOCKS5;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\socksx\v5\AbstractSocks5Message.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */