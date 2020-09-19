/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.handler.codec.dns.DnsQuestion;
/*    */ import java.net.InetSocketAddress;
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
/*    */ public final class DnsNameResolverTimeoutException
/*    */   extends DnsNameResolverException
/*    */ {
/*    */   private static final long serialVersionUID = -8826717969627131854L;
/*    */   
/*    */   public DnsNameResolverTimeoutException(InetSocketAddress remoteAddress, DnsQuestion question, String message) {
/* 33 */     super(remoteAddress, question, message);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\DnsNameResolverTimeoutException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */