/*    */ package org.apache.logging.log4j.core.net.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
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
/*    */ public class SecureTcpSocketServer<T extends InputStream>
/*    */   extends TcpSocketServer<T>
/*    */ {
/*    */   public SecureTcpSocketServer(int port, LogEventBridge<T> logEventInput, SslConfiguration sslConfig) throws IOException {
/* 34 */     super(port, logEventInput, sslConfig.getSslServerSocketFactory().createServerSocket(port));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\net\server\SecureTcpSocketServer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */