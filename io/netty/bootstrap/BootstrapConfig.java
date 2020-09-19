/*    */ package io.netty.bootstrap;
/*    */ 
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.resolver.AddressResolverGroup;
/*    */ import java.net.SocketAddress;
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
/*    */ public final class BootstrapConfig
/*    */   extends AbstractBootstrapConfig<Bootstrap, Channel>
/*    */ {
/*    */   BootstrapConfig(Bootstrap bootstrap) {
/* 29 */     super(bootstrap);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SocketAddress remoteAddress() {
/* 36 */     return this.bootstrap.remoteAddress();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AddressResolverGroup<?> resolver() {
/* 43 */     return this.bootstrap.resolver();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 48 */     StringBuilder buf = new StringBuilder(super.toString());
/* 49 */     buf.setLength(buf.length() - 1);
/* 50 */     buf.append(", resolver: ").append(resolver());
/* 51 */     SocketAddress remoteAddress = remoteAddress();
/* 52 */     if (remoteAddress != null) {
/* 53 */       buf.append(", remoteAddress: ")
/* 54 */         .append(remoteAddress);
/*    */     }
/* 56 */     return buf.append(')').toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\bootstrap\BootstrapConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */