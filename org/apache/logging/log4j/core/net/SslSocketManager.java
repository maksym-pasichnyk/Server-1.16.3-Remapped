/*     */ package org.apache.logging.log4j.core.net;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.UnknownHostException;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
/*     */ import org.apache.logging.log4j.core.util.Closer;
/*     */ import org.apache.logging.log4j.util.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SslSocketManager
/*     */   extends TcpSocketManager
/*     */ {
/*     */   public static final int DEFAULT_PORT = 6514;
/*  43 */   private static final SslSocketManagerFactory FACTORY = new SslSocketManagerFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final SslConfiguration sslConfig;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslSocketManager(String name, OutputStream os, Socket sock, SslConfiguration sslConfig, InetAddress inetAddress, String host, int port, int connectTimeoutMillis, int delay, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize) {
/*  66 */     super(name, os, sock, inetAddress, host, port, connectTimeoutMillis, delay, immediateFail, layout, bufferSize, (SocketOptions)null);
/*  67 */     this.sslConfig = sslConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslSocketManager(String name, OutputStream os, Socket sock, SslConfiguration sslConfig, InetAddress inetAddress, String host, int port, int connectTimeoutMillis, int delay, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
/*  89 */     super(name, os, sock, inetAddress, host, port, connectTimeoutMillis, delay, immediateFail, layout, bufferSize, socketOptions);
/*  90 */     this.sslConfig = sslConfig;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class SslFactoryData
/*     */   {
/*     */     protected SslConfiguration sslConfiguration;
/*     */     private final String host;
/*     */     private final int port;
/*     */     private final int connectTimeoutMillis;
/*     */     private final int delayMillis;
/*     */     private final boolean immediateFail;
/*     */     private final Layout<? extends Serializable> layout;
/*     */     private final int bufferSize;
/*     */     private final SocketOptions socketOptions;
/*     */     
/*     */     public SslFactoryData(SslConfiguration sslConfiguration, String host, int port, int connectTimeoutMillis, int delayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
/* 107 */       this.host = host;
/* 108 */       this.port = port;
/* 109 */       this.connectTimeoutMillis = connectTimeoutMillis;
/* 110 */       this.delayMillis = delayMillis;
/* 111 */       this.immediateFail = immediateFail;
/* 112 */       this.layout = layout;
/* 113 */       this.sslConfiguration = sslConfiguration;
/* 114 */       this.bufferSize = bufferSize;
/* 115 */       this.socketOptions = socketOptions;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static SslSocketManager getSocketManager(SslConfiguration sslConfig, String host, int port, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize) {
/* 126 */     return getSocketManager(sslConfig, host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, (SocketOptions)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static SslSocketManager getSocketManager(SslConfiguration sslConfig, String host, int port, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
/* 132 */     if (Strings.isEmpty(host)) {
/* 133 */       throw new IllegalArgumentException("A host name is required");
/*     */     }
/* 135 */     if (port <= 0) {
/* 136 */       port = 6514;
/*     */     }
/* 138 */     if (reconnectDelayMillis == 0) {
/* 139 */       reconnectDelayMillis = 30000;
/*     */     }
/* 141 */     return (SslSocketManager)getManager("TLS:" + host + ':' + port, new SslFactoryData(sslConfig, host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, socketOptions), FACTORY);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Socket createSocket(String host, int port) throws IOException {
/* 147 */     SSLSocketFactory socketFactory = createSslSocketFactory(this.sslConfig);
/* 148 */     InetSocketAddress address = new InetSocketAddress(host, port);
/* 149 */     Socket newSocket = socketFactory.createSocket();
/* 150 */     newSocket.connect(address, getConnectTimeoutMillis());
/* 151 */     return newSocket;
/*     */   }
/*     */ 
/*     */   
/*     */   private static SSLSocketFactory createSslSocketFactory(SslConfiguration sslConf) {
/*     */     SSLSocketFactory socketFactory;
/* 157 */     if (sslConf != null) {
/* 158 */       socketFactory = sslConf.getSslSocketFactory();
/*     */     } else {
/* 160 */       socketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
/*     */     } 
/*     */     
/* 163 */     return socketFactory;
/*     */   }
/*     */   
/*     */   private static class SslSocketManagerFactory
/*     */     implements ManagerFactory<SslSocketManager, SslFactoryData> {
/*     */     private SslSocketManagerFactory() {}
/*     */     
/*     */     private static class TlsSocketManagerFactoryException extends Exception {
/*     */       private static final long serialVersionUID = 1L;
/*     */       
/*     */       private TlsSocketManagerFactoryException() {}
/*     */     }
/*     */     
/*     */     public SslSocketManager createManager(String name, SslSocketManager.SslFactoryData data) {
/* 177 */       InetAddress inetAddress = null;
/* 178 */       OutputStream os = null;
/* 179 */       Socket socket = null;
/*     */       try {
/* 181 */         inetAddress = resolveAddress(data.host);
/* 182 */         socket = createSocket(data);
/* 183 */         os = socket.getOutputStream();
/* 184 */         checkDelay(data.delayMillis, os);
/* 185 */       } catch (IOException e) {
/* 186 */         SslSocketManager.LOGGER.error("SslSocketManager ({})", name, e);
/* 187 */         os = new ByteArrayOutputStream();
/* 188 */       } catch (TlsSocketManagerFactoryException e) {
/* 189 */         SslSocketManager.LOGGER.catching(Level.DEBUG, e);
/* 190 */         Closer.closeSilently(socket);
/* 191 */         return null;
/*     */       } 
/* 193 */       return new SslSocketManager(name, os, socket, data.sslConfiguration, inetAddress, data.host, data.port, data.connectTimeoutMillis, data.delayMillis, data.immediateFail, data.layout, data.bufferSize, data.socketOptions);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private InetAddress resolveAddress(String hostName) throws TlsSocketManagerFactoryException {
/*     */       InetAddress address;
/*     */       try {
/* 202 */         address = InetAddress.getByName(hostName);
/* 203 */       } catch (UnknownHostException ex) {
/* 204 */         SslSocketManager.LOGGER.error("Could not find address of {}", hostName, ex);
/* 205 */         throw new TlsSocketManagerFactoryException();
/*     */       } 
/*     */       
/* 208 */       return address;
/*     */     }
/*     */     
/*     */     private void checkDelay(int delay, OutputStream os) throws TlsSocketManagerFactoryException {
/* 212 */       if (delay == 0 && os == null) {
/* 213 */         throw new TlsSocketManagerFactoryException();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Socket createSocket(SslSocketManager.SslFactoryData data) throws IOException {
/* 221 */       SSLSocketFactory socketFactory = SslSocketManager.createSslSocketFactory(data.sslConfiguration);
/* 222 */       SSLSocket socket = (SSLSocket)socketFactory.createSocket();
/* 223 */       SocketOptions socketOptions = data.socketOptions;
/* 224 */       if (socketOptions != null)
/*     */       {
/* 226 */         socketOptions.apply(socket);
/*     */       }
/* 228 */       socket.connect(new InetSocketAddress(data.host, data.port), data.connectTimeoutMillis);
/* 229 */       if (socketOptions != null)
/*     */       {
/* 231 */         socketOptions.apply(socket);
/*     */       }
/* 233 */       return socket;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\net\SslSocketManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */