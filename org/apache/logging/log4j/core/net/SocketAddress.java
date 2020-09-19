/*    */ package org.apache.logging.log4j.core.net;
/*    */ 
/*    */ import java.net.InetAddress;
/*    */ import java.net.InetSocketAddress;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*    */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidHost;
/*    */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidPort;
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
/*    */ @Plugin(name = "SocketAddress", category = "Core", printObject = true)
/*    */ public class SocketAddress
/*    */ {
/*    */   private final InetSocketAddress socketAddress;
/*    */   
/*    */   public static SocketAddress getLoopback() {
/* 43 */     return new SocketAddress(InetAddress.getLoopbackAddress(), 0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private SocketAddress(InetAddress host, int port) {
/* 49 */     this.socketAddress = new InetSocketAddress(host, port);
/*    */   }
/*    */   
/*    */   public InetSocketAddress getSocketAddress() {
/* 53 */     return this.socketAddress;
/*    */   }
/*    */   
/*    */   public int getPort() {
/* 57 */     return this.socketAddress.getPort();
/*    */   }
/*    */   
/*    */   public InetAddress getAddress() {
/* 61 */     return this.socketAddress.getAddress();
/*    */   }
/*    */   
/*    */   public String getHostName() {
/* 65 */     return this.socketAddress.getHostName();
/*    */   }
/*    */   
/*    */   @PluginBuilderFactory
/*    */   public static Builder newBuilder() {
/* 70 */     return new Builder();
/*    */   }
/*    */   
/*    */   public static class Builder
/*    */     implements org.apache.logging.log4j.core.util.Builder<SocketAddress>
/*    */   {
/*    */     @PluginBuilderAttribute
/*    */     @ValidHost
/*    */     private InetAddress host;
/*    */     @PluginBuilderAttribute
/*    */     @ValidPort
/*    */     private int port;
/*    */     
/*    */     public Builder setHost(InetAddress host) {
/* 84 */       this.host = host;
/* 85 */       return this;
/*    */     }
/*    */     
/*    */     public Builder setPort(int port) {
/* 89 */       this.port = port;
/* 90 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public SocketAddress build() {
/* 95 */       return new SocketAddress(this.host, this.port);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\net\SocketAddress.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */