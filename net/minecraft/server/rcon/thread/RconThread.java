/*     */ package net.minecraft.server.rcon.thread;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.server.ServerInterface;
/*     */ import net.minecraft.server.dedicated.DedicatedServerProperties;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class RconThread
/*     */   extends GenericThread {
/*  18 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final ServerSocket socket;
/*     */   private final String rconPassword;
/*  22 */   private final List<RconClient> clients = Lists.newArrayList();
/*     */   private final ServerInterface serverInterface;
/*     */   
/*     */   private RconThread(ServerInterface debug1, ServerSocket debug2, String debug3) {
/*  26 */     super("RCON Listener");
/*  27 */     this.serverInterface = debug1;
/*  28 */     this.socket = debug2;
/*  29 */     this.rconPassword = debug3;
/*     */   }
/*     */   
/*     */   private void clearClients() {
/*  33 */     this.clients.removeIf(debug0 -> !debug0.isRunning());
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     try {
/*  39 */       while (this.running) {
/*     */         
/*     */         try {
/*  42 */           Socket debug1 = this.socket.accept();
/*  43 */           RconClient debug2 = new RconClient(this.serverInterface, this.rconPassword, debug1);
/*  44 */           debug2.start();
/*  45 */           this.clients.add(debug2);
/*     */ 
/*     */           
/*  48 */           clearClients();
/*  49 */         } catch (SocketTimeoutException debug1) {
/*     */           
/*  51 */           clearClients();
/*  52 */         } catch (IOException debug1) {
/*  53 */           if (this.running) {
/*  54 */             LOGGER.info("IO exception: ", debug1);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } finally {
/*  59 */       closeSocket(this.socket);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static RconThread create(ServerInterface debug0) {
/*  65 */     DedicatedServerProperties debug1 = debug0.getProperties();
/*     */     
/*  67 */     String debug2 = debug0.getServerIp();
/*  68 */     if (debug2.isEmpty()) {
/*  69 */       debug2 = "0.0.0.0";
/*     */     }
/*     */     
/*  72 */     int debug3 = debug1.rconPort;
/*  73 */     if (0 >= debug3 || 65535 < debug3) {
/*  74 */       LOGGER.warn("Invalid rcon port {} found in server.properties, rcon disabled!", Integer.valueOf(debug3));
/*  75 */       return null;
/*     */     } 
/*     */     
/*  78 */     String debug4 = debug1.rconPassword;
/*  79 */     if (debug4.isEmpty()) {
/*  80 */       LOGGER.warn("No rcon password set in server.properties, rcon disabled!");
/*  81 */       return null;
/*     */     } 
/*     */     
/*     */     try {
/*  85 */       ServerSocket debug5 = new ServerSocket(debug3, 0, InetAddress.getByName(debug2));
/*  86 */       debug5.setSoTimeout(500);
/*     */       
/*  88 */       RconThread debug6 = new RconThread(debug0, debug5, debug4);
/*  89 */       if (!debug6.start()) {
/*  90 */         return null;
/*     */       }
/*  92 */       LOGGER.info("RCON running on {}:{}", debug2, Integer.valueOf(debug3));
/*  93 */       return debug6;
/*  94 */     } catch (IOException debug5) {
/*  95 */       LOGGER.warn("Unable to initialise RCON on {}:{}", debug2, Integer.valueOf(debug3), debug5);
/*     */ 
/*     */       
/*  98 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stop() {
/* 103 */     this.running = false;
/* 104 */     closeSocket(this.socket);
/* 105 */     super.stop();
/*     */     
/* 107 */     for (RconClient debug2 : this.clients) {
/* 108 */       if (debug2.isRunning()) {
/* 109 */         debug2.stop();
/*     */       }
/*     */     } 
/* 112 */     this.clients.clear();
/*     */   }
/*     */   
/*     */   private void closeSocket(ServerSocket debug1) {
/* 116 */     LOGGER.debug("closeSocket: {}", debug1);
/*     */     
/*     */     try {
/* 119 */       debug1.close();
/* 120 */     } catch (IOException debug2) {
/* 121 */       LOGGER.warn("Failed to close socket", debug2);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\rcon\thread\RconThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */