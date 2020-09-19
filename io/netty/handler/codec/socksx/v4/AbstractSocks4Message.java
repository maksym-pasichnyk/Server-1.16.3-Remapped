/*    */ package io.netty.handler.codec.socksx.v4;
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
/*    */ public abstract class AbstractSocks4Message
/*    */   extends AbstractSocksMessage
/*    */   implements Socks4Message
/*    */ {
/*    */   public final SocksVersion version() {
/* 28 */     return SocksVersion.SOCKS4a;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\socksx\v4\AbstractSocks4Message.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */