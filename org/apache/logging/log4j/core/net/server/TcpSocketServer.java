/*     */ package org.apache.logging.log4j.core.net.server;
/*     */ 
/*     */ import com.beust.jcommander.Parameter;
/*     */ import com.beust.jcommander.validators.PositiveInteger;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.OptionalDataException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationFactory;
/*     */ import org.apache.logging.log4j.core.util.BasicCommandLineArguments;
/*     */ import org.apache.logging.log4j.core.util.Closer;
/*     */ import org.apache.logging.log4j.core.util.Log4jThread;
/*     */ import org.apache.logging.log4j.message.EntryMessage;
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
/*     */ public class TcpSocketServer<T extends InputStream>
/*     */   extends AbstractSocketServer<T>
/*     */ {
/*     */   protected static class CommandLineArguments
/*     */     extends AbstractSocketServer.CommandLineArguments
/*     */   {
/*     */     @Parameter(names = {"--backlog", "-b"}, validateWith = PositiveInteger.class, description = "Server socket backlog.")
/*  50 */     private int backlog = 50;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int getBacklog() {
/*  56 */       return this.backlog;
/*     */     }
/*     */     
/*     */     void setBacklog(int backlog) {
/*  60 */       this.backlog = backlog;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class SocketHandler
/*     */     extends Log4jThread
/*     */   {
/*     */     private final T inputStream;
/*     */     
/*     */     private volatile boolean shutdown = false;
/*     */ 
/*     */     
/*     */     public SocketHandler(Socket socket) throws IOException {
/*  75 */       this.inputStream = TcpSocketServer.this.logEventInput.wrapStream(socket.getInputStream());
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*  80 */       EntryMessage entry = TcpSocketServer.this.logger.traceEntry();
/*  81 */       boolean closed = false; try {
/*     */         while (true) {
/*     */           try {
/*  84 */             if (!this.shutdown) {
/*  85 */               TcpSocketServer.this.logEventInput.logEvents(this.inputStream, TcpSocketServer.this); continue;
/*     */             } 
/*  87 */           } catch (EOFException e) {
/*  88 */             closed = true;
/*  89 */           } catch (OptionalDataException e) {
/*  90 */             TcpSocketServer.this.logger.error("OptionalDataException eof=" + e.eof + " length=" + e.length, e);
/*  91 */           } catch (IOException e) {
/*  92 */             TcpSocketServer.this.logger.error("IOException encountered while reading from socket", e);
/*     */           }  break;
/*  94 */         }  if (!closed) {
/*  95 */           Closer.closeSilently((AutoCloseable)this.inputStream);
/*     */         }
/*     */       } finally {
/*  98 */         TcpSocketServer.access$000(TcpSocketServer.this).remove(Long.valueOf(getId()));
/*     */       } 
/* 100 */       TcpSocketServer.this.logger.traceExit(entry);
/*     */     }
/*     */     
/*     */     public void shutdown() {
/* 104 */       this.shutdown = true;
/* 105 */       interrupt();
/*     */     }
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
/*     */   public static TcpSocketServer<InputStream> createJsonSocketServer(int port) throws IOException {
/* 119 */     LOGGER.entry(new Object[] { "createJsonSocketServer", Integer.valueOf(port) });
/* 120 */     TcpSocketServer<InputStream> socketServer = new TcpSocketServer<>(port, new JsonInputStreamLogEventBridge());
/* 121 */     return (TcpSocketServer<InputStream>)LOGGER.exit(socketServer);
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
/*     */   public static TcpSocketServer<ObjectInputStream> createSerializedSocketServer(int port) throws IOException {
/* 134 */     LOGGER.entry(new Object[] { Integer.valueOf(port) });
/* 135 */     TcpSocketServer<ObjectInputStream> socketServer = new TcpSocketServer<>(port, new ObjectInputStreamLogEventBridge());
/* 136 */     return (TcpSocketServer<ObjectInputStream>)LOGGER.exit(socketServer);
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
/*     */   public static TcpSocketServer<ObjectInputStream> createSerializedSocketServer(int port, int backlog, InetAddress localBindAddress) throws IOException {
/* 151 */     LOGGER.entry(new Object[] { Integer.valueOf(port) });
/* 152 */     TcpSocketServer<ObjectInputStream> socketServer = new TcpSocketServer<>(port, backlog, localBindAddress, new ObjectInputStreamLogEventBridge());
/*     */     
/* 154 */     return (TcpSocketServer<ObjectInputStream>)LOGGER.exit(socketServer);
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
/*     */   public static TcpSocketServer<InputStream> createXmlSocketServer(int port) throws IOException {
/* 167 */     LOGGER.entry(new Object[] { Integer.valueOf(port) });
/* 168 */     TcpSocketServer<InputStream> socketServer = new TcpSocketServer<>(port, new XmlInputStreamLogEventBridge());
/* 169 */     return (TcpSocketServer<InputStream>)LOGGER.exit(socketServer);
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
/*     */   public static void main(String[] args) throws Exception {
/* 181 */     CommandLineArguments cla = (CommandLineArguments)BasicCommandLineArguments.parseCommandLine(args, TcpSocketServer.class, new CommandLineArguments());
/* 182 */     if (cla.isHelp()) {
/*     */       return;
/*     */     }
/* 185 */     if (cla.getConfigLocation() != null) {
/* 186 */       ConfigurationFactory.setConfigurationFactory((ConfigurationFactory)new AbstractSocketServer.ServerConfigurationFactory(cla.getConfigLocation()));
/*     */     }
/* 188 */     TcpSocketServer<ObjectInputStream> socketServer = createSerializedSocketServer(cla.getPort(), cla.getBacklog(), cla.getLocalBindAddress());
/*     */     
/* 190 */     Thread serverThread = socketServer.startNewThread();
/* 191 */     if (cla.isInteractive()) {
/* 192 */       socketServer.awaitTermination(serverThread);
/*     */     }
/*     */   }
/*     */   
/* 196 */   private final ConcurrentMap<Long, SocketHandler> handlers = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ServerSocket serverSocket;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TcpSocketServer(int port, int backlog, InetAddress localBindAddress, LogEventBridge<T> logEventInput) throws IOException {
/* 216 */     this(port, logEventInput, new ServerSocket(port, backlog, localBindAddress));
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
/*     */   public TcpSocketServer(int port, LogEventBridge<T> logEventInput) throws IOException {
/* 230 */     this(port, logEventInput, extracted(port));
/*     */   }
/*     */   
/*     */   private static ServerSocket extracted(int port) throws IOException {
/* 234 */     return new ServerSocket(port);
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
/*     */   public TcpSocketServer(int port, LogEventBridge<T> logEventInput, ServerSocket serverSocket) throws IOException {
/* 251 */     super(port, logEventInput);
/* 252 */     this.serverSocket = serverSocket;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 260 */     EntryMessage entry = this.logger.traceEntry();
/* 261 */     while (isActive()) {
/* 262 */       if (this.serverSocket.isClosed()) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/* 267 */         this.logger.debug("Listening for a connection {}...", this.serverSocket);
/* 268 */         Socket clientSocket = this.serverSocket.accept();
/* 269 */         this.logger.debug("Acepted connection on {}...", this.serverSocket);
/* 270 */         this.logger.debug("Socket accepted: {}", clientSocket);
/* 271 */         clientSocket.setSoLinger(true, 0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 277 */         SocketHandler handler = new SocketHandler(clientSocket);
/* 278 */         this.handlers.put(Long.valueOf(handler.getId()), handler);
/* 279 */         handler.start();
/* 280 */       } catch (IOException e) {
/* 281 */         if (this.serverSocket.isClosed()) {
/*     */           
/* 283 */           this.logger.traceExit(entry);
/*     */           return;
/*     */         } 
/* 286 */         this.logger.error("Exception encountered on accept. Ignoring. Stack trace :", e);
/*     */       } 
/*     */     } 
/* 289 */     for (Map.Entry<Long, SocketHandler> handlerEntry : this.handlers.entrySet()) {
/* 290 */       SocketHandler handler = handlerEntry.getValue();
/* 291 */       handler.shutdown();
/*     */       try {
/* 293 */         handler.join();
/* 294 */       } catch (InterruptedException interruptedException) {}
/*     */     } 
/*     */ 
/*     */     
/* 298 */     this.logger.traceExit(entry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() throws IOException {
/* 308 */     EntryMessage entry = this.logger.traceEntry();
/* 309 */     setActive(false);
/* 310 */     Thread.currentThread().interrupt();
/* 311 */     this.serverSocket.close();
/* 312 */     this.logger.traceExit(entry);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\net\server\TcpSocketServer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */