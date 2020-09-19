/*    */ package io.netty.handler.proxy;
/*    */ 
/*    */ import java.net.ConnectException;
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
/*    */ public class ProxyConnectException
/*    */   extends ConnectException
/*    */ {
/*    */   private static final long serialVersionUID = 5211364632246265538L;
/*    */   
/*    */   public ProxyConnectException() {}
/*    */   
/*    */   public ProxyConnectException(String msg) {
/* 27 */     super(msg);
/*    */   }
/*    */   
/*    */   public ProxyConnectException(Throwable cause) {
/* 31 */     initCause(cause);
/*    */   }
/*    */   
/*    */   public ProxyConnectException(String msg, Throwable cause) {
/* 35 */     super(msg);
/* 36 */     initCause(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\proxy\ProxyConnectException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */