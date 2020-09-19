/*     */ package net.minecraft.server.rcon.thread;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.net.Socket;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import net.minecraft.server.ServerInterface;
/*     */ import net.minecraft.server.rcon.PktUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class RconClient
/*     */   extends GenericThread {
/*  16 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */   
/*     */   private boolean authed;
/*     */ 
/*     */   
/*     */   private final Socket client;
/*     */   
/*  24 */   private final byte[] buf = new byte[1460];
/*     */   private final String rconPassword;
/*     */   private final ServerInterface serverInterface;
/*     */   
/*     */   RconClient(ServerInterface debug1, String debug2, Socket debug3) {
/*  29 */     super("RCON Client " + debug3.getInetAddress());
/*  30 */     this.serverInterface = debug1;
/*  31 */     this.client = debug3;
/*     */     
/*     */     try {
/*  34 */       this.client.setSoTimeout(0);
/*  35 */     } catch (Exception debug4) {
/*  36 */       this.running = false;
/*     */     } 
/*     */     
/*  39 */     this.rconPassword = debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     
/*  45 */     try { while (this.running) {
/*  46 */         String debug7; BufferedInputStream debug1 = new BufferedInputStream(this.client.getInputStream());
/*  47 */         int debug2 = debug1.read(this.buf, 0, 1460);
/*     */         
/*  49 */         if (10 > debug2) {
/*     */           return;
/*     */         }
/*     */         
/*  53 */         int debug3 = 0;
/*  54 */         int debug4 = PktUtils.intFromByteArray(this.buf, 0, debug2);
/*  55 */         if (debug4 != debug2 - 4) {
/*     */           return;
/*     */         }
/*     */         
/*  59 */         debug3 += 4;
/*  60 */         int debug5 = PktUtils.intFromByteArray(this.buf, debug3, debug2);
/*  61 */         debug3 += 4;
/*     */         
/*  63 */         int debug6 = PktUtils.intFromByteArray(this.buf, debug3);
/*  64 */         debug3 += 4;
/*  65 */         switch (debug6) {
/*     */           case 3:
/*  67 */             debug7 = PktUtils.stringFromByteArray(this.buf, debug3, debug2);
/*  68 */             debug3 += debug7.length();
/*  69 */             if (!debug7.isEmpty() && debug7.equals(this.rconPassword)) {
/*  70 */               this.authed = true;
/*  71 */               send(debug5, 2, ""); continue;
/*     */             } 
/*  73 */             this.authed = false;
/*  74 */             sendAuthFailure();
/*     */             continue;
/*     */           
/*     */           case 2:
/*  78 */             if (this.authed) {
/*  79 */               String debug8 = PktUtils.stringFromByteArray(this.buf, debug3, debug2);
/*     */               try {
/*  81 */                 sendCmdResponse(debug5, this.serverInterface.runCommand(debug8));
/*  82 */               } catch (Exception debug9) {
/*  83 */                 sendCmdResponse(debug5, "Error executing: " + debug8 + " (" + debug9.getMessage() + ")");
/*     */               }  continue;
/*     */             } 
/*  86 */             sendAuthFailure();
/*     */             continue;
/*     */         } 
/*     */         
/*  90 */         sendCmdResponse(debug5, String.format("Unknown request %s", new Object[] { Integer.toHexString(debug6) }));
/*     */       }
/*     */        }
/*  93 */     catch (IOException iOException) {  }
/*  94 */     catch (Exception debug1)
/*  95 */     { LOGGER.error("Exception whilst parsing RCON input", debug1); }
/*     */     finally
/*  97 */     { closeSocket();
/*  98 */       LOGGER.info("Thread {} shutting down", this.name);
/*  99 */       this.running = false; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void send(int debug1, int debug2, String debug3) throws IOException {
/* 106 */     ByteArrayOutputStream debug4 = new ByteArrayOutputStream(1248);
/* 107 */     DataOutputStream debug5 = new DataOutputStream(debug4);
/* 108 */     byte[] debug6 = debug3.getBytes(StandardCharsets.UTF_8);
/* 109 */     debug5.writeInt(Integer.reverseBytes(debug6.length + 10));
/* 110 */     debug5.writeInt(Integer.reverseBytes(debug1));
/* 111 */     debug5.writeInt(Integer.reverseBytes(debug2));
/* 112 */     debug5.write(debug6);
/* 113 */     debug5.write(0);
/* 114 */     debug5.write(0);
/* 115 */     this.client.getOutputStream().write(debug4.toByteArray());
/*     */   }
/*     */   
/*     */   private void sendAuthFailure() throws IOException {
/* 119 */     send(-1, 2, "");
/*     */   }
/*     */   
/*     */   private void sendCmdResponse(int debug1, String debug2) throws IOException {
/* 123 */     int debug3 = debug2.length();
/*     */     
/*     */     do {
/* 126 */       int debug4 = (4096 <= debug3) ? 4096 : debug3;
/* 127 */       send(debug1, 0, debug2.substring(0, debug4));
/* 128 */       debug2 = debug2.substring(debug4);
/* 129 */       debug3 = debug2.length();
/* 130 */     } while (0 != debug3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 138 */     this.running = false;
/* 139 */     closeSocket();
/* 140 */     super.stop();
/*     */   }
/*     */   
/*     */   private void closeSocket() {
/*     */     try {
/* 145 */       this.client.close();
/* 146 */     } catch (IOException debug1) {
/* 147 */       LOGGER.warn("Failed to close socket", debug1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\rcon\thread\RconClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */