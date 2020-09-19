/*     */ package net.minecraft.server.rcon.thread;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.PortUnreachableException;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Date;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.server.ServerInterface;
/*     */ import net.minecraft.server.rcon.NetworkDataOutputStream;
/*     */ import net.minecraft.server.rcon.PktUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class QueryThreadGs4
/*     */   extends GenericThread {
/*  26 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private long lastChallengeCheck;
/*     */   
/*     */   private final int port;
/*     */   
/*     */   private final int serverPort;
/*     */   
/*     */   private final int maxPlayers;
/*     */   private final String serverName;
/*     */   private final String worldName;
/*     */   private DatagramSocket socket;
/*  38 */   private final byte[] buffer = new byte[1460];
/*     */   private String hostIp;
/*     */   private String serverIp;
/*     */   private final Map<SocketAddress, RequestChallenge> validChallenges;
/*     */   private final NetworkDataOutputStream rulesResponse;
/*     */   private long lastRulesResponse;
/*     */   private final ServerInterface serverInterface;
/*     */   
/*     */   private QueryThreadGs4(ServerInterface debug1, int debug2) {
/*  47 */     super("Query Listener");
/*  48 */     this.serverInterface = debug1;
/*     */     
/*  50 */     this.port = debug2;
/*  51 */     this.serverIp = debug1.getServerIp();
/*  52 */     this.serverPort = debug1.getServerPort();
/*  53 */     this.serverName = debug1.getServerName();
/*  54 */     this.maxPlayers = debug1.getMaxPlayers();
/*  55 */     this.worldName = debug1.getLevelIdName();
/*     */ 
/*     */     
/*  58 */     this.lastRulesResponse = 0L;
/*     */     
/*  60 */     this.hostIp = "0.0.0.0";
/*     */ 
/*     */     
/*  63 */     if (this.serverIp.isEmpty() || this.hostIp.equals(this.serverIp)) {
/*     */       
/*  65 */       this.serverIp = "0.0.0.0";
/*     */       try {
/*  67 */         InetAddress debug3 = InetAddress.getLocalHost();
/*  68 */         this.hostIp = debug3.getHostAddress();
/*  69 */       } catch (UnknownHostException debug3) {
/*  70 */         LOGGER.warn("Unable to determine local host IP, please set server-ip in server.properties", debug3);
/*     */       } 
/*     */     } else {
/*  73 */       this.hostIp = this.serverIp;
/*     */     } 
/*     */ 
/*     */     
/*  77 */     this.rulesResponse = new NetworkDataOutputStream(1460);
/*  78 */     this.validChallenges = Maps.newHashMap();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static QueryThreadGs4 create(ServerInterface debug0) {
/*  83 */     int debug1 = (debug0.getProperties()).queryPort;
/*  84 */     if (0 >= debug1 || 65535 < debug1) {
/*  85 */       LOGGER.warn("Invalid query port {} found in server.properties (queries disabled)", Integer.valueOf(debug1));
/*  86 */       return null;
/*     */     } 
/*     */     
/*  89 */     QueryThreadGs4 debug2 = new QueryThreadGs4(debug0, debug1);
/*  90 */     if (!debug2.start()) {
/*  91 */       return null;
/*     */     }
/*  93 */     return debug2;
/*     */   }
/*     */   
/*     */   private void sendTo(byte[] debug1, DatagramPacket debug2) throws IOException {
/*  97 */     this.socket.send(new DatagramPacket(debug1, debug1.length, debug2.getSocketAddress()));
/*     */   }
/*     */   private boolean processPacket(DatagramPacket debug1) throws IOException {
/*     */     NetworkDataOutputStream debug5;
/* 101 */     byte[] debug2 = debug1.getData();
/* 102 */     int debug3 = debug1.getLength();
/* 103 */     SocketAddress debug4 = debug1.getSocketAddress();
/* 104 */     LOGGER.debug("Packet len {} [{}]", Integer.valueOf(debug3), debug4);
/* 105 */     if (3 > debug3 || -2 != debug2[0] || -3 != debug2[1]) {
/*     */       
/* 107 */       LOGGER.debug("Invalid packet [{}]", debug4);
/* 108 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 112 */     LOGGER.debug("Packet '{}' [{}]", PktUtils.toHexString(debug2[2]), debug4);
/* 113 */     switch (debug2[2]) {
/*     */       
/*     */       case 9:
/* 116 */         sendChallenge(debug1);
/* 117 */         LOGGER.debug("Challenge [{}]", debug4);
/* 118 */         return true;
/*     */ 
/*     */       
/*     */       case 0:
/* 122 */         if (!validChallenge(debug1).booleanValue()) {
/* 123 */           LOGGER.debug("Invalid challenge [{}]", debug4);
/* 124 */           return false;
/*     */         } 
/*     */         
/* 127 */         if (15 == debug3) {
/*     */           
/* 129 */           sendTo(buildRuleResponse(debug1), debug1);
/* 130 */           LOGGER.debug("Rules [{}]", debug4);
/*     */           break;
/*     */         } 
/* 133 */         debug5 = new NetworkDataOutputStream(1460);
/* 134 */         debug5.write(0);
/* 135 */         debug5.writeBytes(getIdentBytes(debug1.getSocketAddress()));
/* 136 */         debug5.writeString(this.serverName);
/* 137 */         debug5.writeString("SMP");
/* 138 */         debug5.writeString(this.worldName);
/* 139 */         debug5.writeString(Integer.toString(this.serverInterface.getPlayerCount()));
/* 140 */         debug5.writeString(Integer.toString(this.maxPlayers));
/* 141 */         debug5.writeShort((short)this.serverPort);
/* 142 */         debug5.writeString(this.hostIp);
/*     */         
/* 144 */         sendTo(debug5.toByteArray(), debug1);
/* 145 */         LOGGER.debug("Status [{}]", debug4);
/*     */         break;
/*     */     } 
/*     */     
/* 149 */     return true;
/*     */   }
/*     */   
/*     */   private byte[] buildRuleResponse(DatagramPacket debug1) throws IOException {
/* 153 */     long debug2 = Util.getMillis();
/* 154 */     if (debug2 < this.lastRulesResponse + 5000L) {
/*     */       
/* 156 */       byte[] arrayOfByte1 = this.rulesResponse.toByteArray();
/* 157 */       byte[] debug5 = getIdentBytes(debug1.getSocketAddress());
/* 158 */       arrayOfByte1[1] = debug5[0];
/* 159 */       arrayOfByte1[2] = debug5[1];
/* 160 */       arrayOfByte1[3] = debug5[2];
/* 161 */       arrayOfByte1[4] = debug5[3];
/*     */       
/* 163 */       return arrayOfByte1;
/*     */     } 
/*     */     
/* 166 */     this.lastRulesResponse = debug2;
/*     */     
/* 168 */     this.rulesResponse.reset();
/* 169 */     this.rulesResponse.write(0);
/* 170 */     this.rulesResponse.writeBytes(getIdentBytes(debug1.getSocketAddress()));
/* 171 */     this.rulesResponse.writeString("splitnum");
/* 172 */     this.rulesResponse.write(128);
/* 173 */     this.rulesResponse.write(0);
/*     */ 
/*     */     
/* 176 */     this.rulesResponse.writeString("hostname");
/* 177 */     this.rulesResponse.writeString(this.serverName);
/* 178 */     this.rulesResponse.writeString("gametype");
/* 179 */     this.rulesResponse.writeString("SMP");
/* 180 */     this.rulesResponse.writeString("game_id");
/* 181 */     this.rulesResponse.writeString("MINECRAFT");
/* 182 */     this.rulesResponse.writeString("version");
/* 183 */     this.rulesResponse.writeString(this.serverInterface.getServerVersion());
/* 184 */     this.rulesResponse.writeString("plugins");
/* 185 */     this.rulesResponse.writeString(this.serverInterface.getPluginNames());
/* 186 */     this.rulesResponse.writeString("map");
/* 187 */     this.rulesResponse.writeString(this.worldName);
/* 188 */     this.rulesResponse.writeString("numplayers");
/* 189 */     this.rulesResponse.writeString("" + this.serverInterface.getPlayerCount());
/* 190 */     this.rulesResponse.writeString("maxplayers");
/* 191 */     this.rulesResponse.writeString("" + this.maxPlayers);
/* 192 */     this.rulesResponse.writeString("hostport");
/* 193 */     this.rulesResponse.writeString("" + this.serverPort);
/* 194 */     this.rulesResponse.writeString("hostip");
/* 195 */     this.rulesResponse.writeString(this.hostIp);
/* 196 */     this.rulesResponse.write(0);
/* 197 */     this.rulesResponse.write(1);
/*     */ 
/*     */ 
/*     */     
/* 201 */     this.rulesResponse.writeString("player_");
/* 202 */     this.rulesResponse.write(0);
/*     */     
/* 204 */     String[] debug4 = this.serverInterface.getPlayerNames();
/* 205 */     for (String debug8 : debug4) {
/* 206 */       this.rulesResponse.writeString(debug8);
/*     */     }
/* 208 */     this.rulesResponse.write(0);
/*     */     
/* 210 */     return this.rulesResponse.toByteArray();
/*     */   }
/*     */   
/*     */   private byte[] getIdentBytes(SocketAddress debug1) {
/* 214 */     return ((RequestChallenge)this.validChallenges.get(debug1)).getIdentBytes();
/*     */   }
/*     */   
/*     */   private Boolean validChallenge(DatagramPacket debug1) {
/* 218 */     SocketAddress debug2 = debug1.getSocketAddress();
/* 219 */     if (!this.validChallenges.containsKey(debug2))
/*     */     {
/* 221 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/* 224 */     byte[] debug3 = debug1.getData();
/* 225 */     return Boolean.valueOf((((RequestChallenge)this.validChallenges.get(debug2)).getChallenge() == PktUtils.intFromNetworkByteArray(debug3, 7, debug1.getLength())));
/*     */   }
/*     */   
/*     */   private void sendChallenge(DatagramPacket debug1) throws IOException {
/* 229 */     RequestChallenge debug2 = new RequestChallenge(debug1);
/* 230 */     this.validChallenges.put(debug1.getSocketAddress(), debug2);
/*     */     
/* 232 */     sendTo(debug2.getChallengeBytes(), debug1);
/*     */   }
/*     */   
/*     */   private void pruneChallenges() {
/* 236 */     if (!this.running) {
/*     */       return;
/*     */     }
/*     */     
/* 240 */     long debug1 = Util.getMillis();
/* 241 */     if (debug1 < this.lastChallengeCheck + 30000L) {
/*     */       return;
/*     */     }
/* 244 */     this.lastChallengeCheck = debug1;
/*     */     
/* 246 */     this.validChallenges.values().removeIf(debug2 -> debug2.before(debug0).booleanValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/* 251 */     LOGGER.info("Query running on {}:{}", this.serverIp, Integer.valueOf(this.port));
/* 252 */     this.lastChallengeCheck = Util.getMillis();
/* 253 */     DatagramPacket debug1 = new DatagramPacket(this.buffer, this.buffer.length);
/*     */     
/*     */     try {
/* 256 */       while (this.running) {
/*     */         try {
/* 258 */           this.socket.receive(debug1);
/*     */ 
/*     */           
/* 261 */           pruneChallenges();
/*     */ 
/*     */           
/* 264 */           processPacket(debug1);
/* 265 */         } catch (SocketTimeoutException debug2) {
/*     */           
/* 267 */           pruneChallenges();
/* 268 */         } catch (PortUnreachableException portUnreachableException) {
/*     */         
/* 270 */         } catch (IOException debug2) {
/*     */           
/* 272 */           recoverSocketError(debug2);
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 276 */       LOGGER.debug("closeSocket: {}:{}", this.serverIp, Integer.valueOf(this.port));
/* 277 */       this.socket.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean start() {
/* 283 */     if (this.running) {
/* 284 */       return true;
/*     */     }
/*     */     
/* 287 */     if (!initSocket()) {
/* 288 */       return false;
/*     */     }
/*     */     
/* 291 */     return super.start();
/*     */   }
/*     */ 
/*     */   
/*     */   private void recoverSocketError(Exception debug1) {
/* 296 */     if (!this.running) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 301 */     LOGGER.warn("Unexpected exception", debug1);
/*     */ 
/*     */     
/* 304 */     if (!initSocket()) {
/* 305 */       LOGGER.error("Failed to recover from exception, shutting down!");
/* 306 */       this.running = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean initSocket() {
/*     */     try {
/* 312 */       this.socket = new DatagramSocket(this.port, InetAddress.getByName(this.serverIp));
/* 313 */       this.socket.setSoTimeout(500);
/* 314 */       return true;
/* 315 */     } catch (Exception debug1) {
/* 316 */       LOGGER.warn("Unable to initialise query system on {}:{}", this.serverIp, Integer.valueOf(this.port), debug1);
/*     */       
/* 318 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class RequestChallenge
/*     */   {
/* 329 */     private final long time = (new Date()).getTime(); private final int challenge; private final byte[] identBytes; public RequestChallenge(DatagramPacket debug1) {
/* 330 */       byte[] debug2 = debug1.getData();
/* 331 */       this.identBytes = new byte[4];
/* 332 */       this.identBytes[0] = debug2[3];
/* 333 */       this.identBytes[1] = debug2[4];
/* 334 */       this.identBytes[2] = debug2[5];
/* 335 */       this.identBytes[3] = debug2[6];
/* 336 */       this.ident = new String(this.identBytes, StandardCharsets.UTF_8);
/* 337 */       this.challenge = (new Random()).nextInt(16777216);
/* 338 */       this.challengeBytes = String.format("\t%s%d\000", new Object[] { this.ident, Integer.valueOf(this.challenge) }).getBytes(StandardCharsets.UTF_8);
/*     */     }
/*     */     private final byte[] challengeBytes; private final String ident;
/*     */     public Boolean before(long debug1) {
/* 342 */       return Boolean.valueOf((this.time < debug1));
/*     */     }
/*     */     
/*     */     public int getChallenge() {
/* 346 */       return this.challenge;
/*     */     }
/*     */     
/*     */     public byte[] getChallengeBytes() {
/* 350 */       return this.challengeBytes;
/*     */     }
/*     */     
/*     */     public byte[] getIdentBytes() {
/* 354 */       return this.identBytes;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\rcon\thread\QueryThreadGs4.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */