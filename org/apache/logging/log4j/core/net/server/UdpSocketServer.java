/*     */ package org.apache.logging.log4j.core.net.server;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.OptionalDataException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationFactory;
/*     */ import org.apache.logging.log4j.core.util.BasicCommandLineArguments;
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
/*     */ public class UdpSocketServer<T extends InputStream>
/*     */   extends AbstractSocketServer<T>
/*     */ {
/*     */   private final DatagramSocket datagramSocket;
/*     */   
/*     */   public static UdpSocketServer<InputStream> createJsonSocketServer(int port) throws IOException {
/*  50 */     return new UdpSocketServer<>(port, new JsonInputStreamLogEventBridge());
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
/*     */   public static UdpSocketServer<ObjectInputStream> createSerializedSocketServer(int port) throws IOException {
/*  63 */     return new UdpSocketServer<>(port, new ObjectInputStreamLogEventBridge());
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
/*     */   public static UdpSocketServer<InputStream> createXmlSocketServer(int port) throws IOException {
/*  76 */     return new UdpSocketServer<>(port, new XmlInputStreamLogEventBridge());
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
/*  88 */     AbstractSocketServer.CommandLineArguments cla = (AbstractSocketServer.CommandLineArguments)BasicCommandLineArguments.parseCommandLine(args, UdpSocketServer.class, new AbstractSocketServer.CommandLineArguments());
/*  89 */     if (cla.isHelp()) {
/*     */       return;
/*     */     }
/*  92 */     if (cla.getConfigLocation() != null) {
/*  93 */       ConfigurationFactory.setConfigurationFactory((ConfigurationFactory)new AbstractSocketServer.ServerConfigurationFactory(cla.getConfigLocation()));
/*     */     }
/*  95 */     UdpSocketServer<ObjectInputStream> socketServer = createSerializedSocketServer(cla.getPort());
/*     */     
/*  97 */     Thread serverThread = socketServer.startNewThread();
/*  98 */     if (cla.isInteractive()) {
/*  99 */       socketServer.awaitTermination(serverThread);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   private final int maxBufferSize = 67584;
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
/*     */   public UdpSocketServer(int port, LogEventBridge<T> logEventInput) throws IOException {
/* 118 */     super(port, logEventInput);
/* 119 */     this.datagramSocket = new DatagramSocket(port);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 127 */     while (isActive()) {
/* 128 */       if (this.datagramSocket.isClosed()) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/* 133 */         byte[] buf = new byte[67584];
/* 134 */         DatagramPacket packet = new DatagramPacket(buf, buf.length);
/* 135 */         this.datagramSocket.receive(packet);
/* 136 */         ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), packet.getOffset(), packet.getLength());
/* 137 */         this.logEventInput.logEvents(this.logEventInput.wrapStream(bais), this);
/* 138 */       } catch (OptionalDataException e) {
/* 139 */         if (this.datagramSocket.isClosed()) {
/*     */           return;
/*     */         }
/*     */         
/* 143 */         this.logger.error("OptionalDataException eof=" + e.eof + " length=" + e.length, e);
/* 144 */       } catch (EOFException e) {
/* 145 */         if (this.datagramSocket.isClosed()) {
/*     */           return;
/*     */         }
/*     */         
/* 149 */         this.logger.info("EOF encountered");
/* 150 */       } catch (IOException e) {
/* 151 */         if (this.datagramSocket.isClosed()) {
/*     */           return;
/*     */         }
/*     */         
/* 155 */         this.logger.error("Exception encountered on accept. Ignoring. Stack Trace :", e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 165 */     setActive(false);
/* 166 */     Thread.currentThread().interrupt();
/* 167 */     this.datagramSocket.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\net\server\UdpSocketServer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */