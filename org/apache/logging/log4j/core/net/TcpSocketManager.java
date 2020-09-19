/*     */ package org.apache.logging.log4j.core.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.net.ConnectException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.appender.OutputStreamManager;
/*     */ import org.apache.logging.log4j.core.util.Closer;
/*     */ import org.apache.logging.log4j.core.util.Log4jThread;
/*     */ import org.apache.logging.log4j.core.util.NullOutputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TcpSocketManager
/*     */   extends AbstractSocketManager
/*     */ {
/*     */   public static final int DEFAULT_RECONNECTION_DELAY_MILLIS = 30000;
/*     */   private static final int DEFAULT_PORT = 4560;
/*  54 */   private static final TcpSocketManagerFactory FACTORY = new TcpSocketManagerFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int reconnectionDelay;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Reconnector reconnector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Socket socket;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final SocketOptions socketOptions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean retry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean immediateFail;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int connectTimeoutMillis;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public TcpSocketManager(String name, OutputStream os, Socket socket, InetAddress inetAddress, String host, int port, int connectTimeoutMillis, int delay, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize) {
/* 103 */     this(name, os, socket, inetAddress, host, port, connectTimeoutMillis, delay, immediateFail, layout, bufferSize, (SocketOptions)null);
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
/*     */   public TcpSocketManager(String name, OutputStream os, Socket socket, InetAddress inetAddress, String host, int port, int connectTimeoutMillis, int delay, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
/* 137 */     super(name, os, inetAddress, host, port, layout, true, bufferSize);
/* 138 */     this.connectTimeoutMillis = connectTimeoutMillis;
/* 139 */     this.reconnectionDelay = delay;
/* 140 */     this.socket = socket;
/* 141 */     this.immediateFail = immediateFail;
/* 142 */     this.retry = (delay > 0);
/* 143 */     if (socket == null) {
/* 144 */       this.reconnector = createReconnector();
/* 145 */       this.reconnector.start();
/*     */     } 
/* 147 */     this.socketOptions = socketOptions;
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
/*     */   @Deprecated
/*     */   public static TcpSocketManager getSocketManager(String host, int port, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize) {
/* 170 */     return getSocketManager(host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, (SocketOptions)null);
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
/*     */   public static TcpSocketManager getSocketManager(String host, int port, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
/* 192 */     if (Strings.isEmpty(host)) {
/* 193 */       throw new IllegalArgumentException("A host name is required");
/*     */     }
/* 195 */     if (port <= 0) {
/* 196 */       port = 4560;
/*     */     }
/* 198 */     if (reconnectDelayMillis == 0) {
/* 199 */       reconnectDelayMillis = 30000;
/*     */     }
/* 201 */     return (TcpSocketManager)getManager("TCP:" + host + ':' + port, new FactoryData(host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, socketOptions), FACTORY);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void write(byte[] bytes, int offset, int length, boolean immediateFlush) {
/* 207 */     if (this.socket == null) {
/* 208 */       if (this.reconnector != null && !this.immediateFail) {
/* 209 */         this.reconnector.latch();
/*     */       }
/* 211 */       if (this.socket == null) {
/* 212 */         String msg = "Error writing to " + getName() + " socket not available";
/* 213 */         throw new AppenderLoggingException(msg);
/*     */       } 
/*     */     } 
/* 216 */     synchronized (this) {
/*     */       try {
/* 218 */         OutputStream outputStream = getOutputStream();
/* 219 */         outputStream.write(bytes, offset, length);
/* 220 */         if (immediateFlush) {
/* 221 */           outputStream.flush();
/*     */         }
/* 223 */       } catch (IOException ex) {
/* 224 */         if (this.retry && this.reconnector == null) {
/* 225 */           this.reconnector = createReconnector();
/* 226 */           this.reconnector.start();
/*     */         } 
/* 228 */         String msg = "Error writing to " + getName();
/* 229 */         throw new AppenderLoggingException(msg, ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected synchronized boolean closeOutputStream() {
/* 236 */     boolean closed = super.closeOutputStream();
/* 237 */     if (this.reconnector != null) {
/* 238 */       this.reconnector.shutdown();
/* 239 */       this.reconnector.interrupt();
/* 240 */       this.reconnector = null;
/*     */     } 
/* 242 */     Socket oldSocket = this.socket;
/* 243 */     this.socket = null;
/* 244 */     if (oldSocket != null) {
/*     */       try {
/* 246 */         oldSocket.close();
/* 247 */       } catch (IOException e) {
/* 248 */         LOGGER.error("Could not close socket {}", this.socket);
/* 249 */         return false;
/*     */       } 
/*     */     }
/* 252 */     return closed;
/*     */   }
/*     */   
/*     */   public int getConnectTimeoutMillis() {
/* 256 */     return this.connectTimeoutMillis;
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
/*     */   public Map<String, String> getContentFormat() {
/* 270 */     Map<String, String> result = new HashMap<>(super.getContentFormat());
/* 271 */     result.put("protocol", "tcp");
/* 272 */     result.put("direction", "out");
/* 273 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class Reconnector
/*     */     extends Log4jThread
/*     */   {
/* 281 */     private final CountDownLatch latch = new CountDownLatch(1);
/*     */     
/*     */     private boolean shutdown = false;
/*     */     
/*     */     private final Object owner;
/*     */     
/*     */     public Reconnector(OutputStreamManager owner) {
/* 288 */       super("TcpSocketManager-Reconnector");
/* 289 */       this.owner = owner;
/*     */     }
/*     */     
/*     */     public void latch() {
/*     */       try {
/* 294 */         this.latch.await();
/* 295 */       } catch (InterruptedException interruptedException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void shutdown() {
/* 301 */       this.shutdown = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 306 */       while (!this.shutdown) {
/*     */         try {
/* 308 */           sleep(TcpSocketManager.this.reconnectionDelay);
/* 309 */           Socket sock = TcpSocketManager.this.createSocket(TcpSocketManager.this.inetAddress, TcpSocketManager.this.port);
/* 310 */           OutputStream newOS = sock.getOutputStream();
/* 311 */           synchronized (this.owner) {
/*     */             try {
/* 313 */               TcpSocketManager.this.getOutputStream().close();
/* 314 */             } catch (IOException iOException) {}
/*     */ 
/*     */ 
/*     */             
/* 318 */             TcpSocketManager.this.setOutputStream(newOS);
/* 319 */             TcpSocketManager.this.socket = sock;
/* 320 */             TcpSocketManager.this.reconnector = null;
/* 321 */             this.shutdown = true;
/*     */           } 
/* 323 */           TcpSocketManager.LOGGER.debug("Connection to " + TcpSocketManager.this.host + ':' + TcpSocketManager.this.port + " reestablished.");
/* 324 */         } catch (InterruptedException ie) {
/* 325 */           TcpSocketManager.LOGGER.debug("Reconnection interrupted.");
/* 326 */         } catch (ConnectException ex) {
/* 327 */           TcpSocketManager.LOGGER.debug(TcpSocketManager.this.host + ':' + TcpSocketManager.this.port + " refused connection");
/* 328 */         } catch (IOException ioe) {
/* 329 */           TcpSocketManager.LOGGER.debug("Unable to reconnect to " + TcpSocketManager.this.host + ':' + TcpSocketManager.this.port);
/*     */         } finally {
/* 331 */           this.latch.countDown();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private Reconnector createReconnector() {
/* 338 */     Reconnector recon = new Reconnector(this);
/* 339 */     recon.setDaemon(true);
/* 340 */     recon.setPriority(1);
/* 341 */     return recon;
/*     */   }
/*     */   
/*     */   protected Socket createSocket(InetAddress host, int port) throws IOException {
/* 345 */     return createSocket(host.getHostName(), port);
/*     */   }
/*     */   
/*     */   protected Socket createSocket(String host, int port) throws IOException {
/* 349 */     Socket newSocket = new Socket();
/* 350 */     newSocket.connect(new InetSocketAddress(host, port), this.connectTimeoutMillis);
/* 351 */     if (this.socketOptions != null) {
/* 352 */       this.socketOptions.apply(newSocket);
/*     */     }
/* 354 */     return newSocket;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */   {
/*     */     private final String host;
/*     */     
/*     */     private final int port;
/*     */     
/*     */     private final int connectTimeoutMillis;
/*     */     
/*     */     private final int reconnectDelayMillis;
/*     */     private final boolean immediateFail;
/*     */     private final Layout<? extends Serializable> layout;
/*     */     private final int bufferSize;
/*     */     private final SocketOptions socketOptions;
/*     */     
/*     */     public FactoryData(String host, int port, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
/* 373 */       this.host = host;
/* 374 */       this.port = port;
/* 375 */       this.connectTimeoutMillis = connectTimeoutMillis;
/* 376 */       this.reconnectDelayMillis = reconnectDelayMillis;
/* 377 */       this.immediateFail = immediateFail;
/* 378 */       this.layout = layout;
/* 379 */       this.bufferSize = bufferSize;
/* 380 */       this.socketOptions = socketOptions;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class TcpSocketManagerFactory
/*     */     implements ManagerFactory<TcpSocketManager, FactoryData>
/*     */   {
/*     */     public TcpSocketManager createManager(String name, TcpSocketManager.FactoryData data) {
/*     */       InetAddress inetAddress;
/*     */       try {
/* 396 */         inetAddress = InetAddress.getByName(data.host);
/* 397 */       } catch (UnknownHostException ex) {
/* 398 */         TcpSocketManager.LOGGER.error("Could not find address of " + data.host, ex, ex);
/* 399 */         return null;
/*     */       } 
/* 401 */       Socket socket = null;
/*     */       
/*     */       try {
/* 404 */         socket = createSocket(data);
/* 405 */         OutputStream os = socket.getOutputStream();
/* 406 */         return new TcpSocketManager(name, os, socket, inetAddress, data.host, data.port, data.connectTimeoutMillis, data.reconnectDelayMillis, data.immediateFail, data.layout, data.bufferSize, data.socketOptions);
/*     */       
/*     */       }
/* 409 */       catch (IOException ex) {
/* 410 */         TcpSocketManager.LOGGER.error("TcpSocketManager (" + name + ") " + ex, ex);
/* 411 */         NullOutputStream nullOutputStream = NullOutputStream.getInstance();
/*     */         
/* 413 */         if (data.reconnectDelayMillis == 0) {
/* 414 */           Closer.closeSilently(socket);
/* 415 */           return null;
/*     */         } 
/* 417 */         return new TcpSocketManager(name, (OutputStream)nullOutputStream, null, inetAddress, data.host, data.port, data.connectTimeoutMillis, data.reconnectDelayMillis, data.immediateFail, data.layout, data.bufferSize, data.socketOptions);
/*     */       } 
/*     */     }
/*     */     
/*     */     static Socket createSocket(TcpSocketManager.FactoryData data) throws IOException, SocketException {
/* 422 */       Socket socket = new Socket();
/* 423 */       socket.connect(new InetSocketAddress(data.host, data.port), data.connectTimeoutMillis);
/* 424 */       SocketOptions socketOptions = data.socketOptions;
/* 425 */       if (socketOptions != null) {
/* 426 */         socketOptions.apply(socket);
/*     */       }
/* 428 */       return socket;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SocketOptions getSocketOptions() {
/* 437 */     return this.socketOptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket getSocket() {
/* 444 */     return this.socket;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\net\TcpSocketManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */