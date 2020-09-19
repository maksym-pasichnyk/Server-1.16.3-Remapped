/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import java.math.BigInteger;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.security.PrivateKey;
/*     */ import java.util.Arrays;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.crypto.SecretKey;
/*     */ import net.minecraft.DefaultUncaughtExceptionHandler;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.login.ClientboundGameProfilePacket;
/*     */ import net.minecraft.network.protocol.login.ClientboundHelloPacket;
/*     */ import net.minecraft.network.protocol.login.ClientboundLoginCompressionPacket;
/*     */ import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
/*     */ import net.minecraft.network.protocol.login.ServerLoginPacketListener;
/*     */ import net.minecraft.network.protocol.login.ServerboundCustomQueryPacket;
/*     */ import net.minecraft.network.protocol.login.ServerboundHelloPacket;
/*     */ import net.minecraft.network.protocol.login.ServerboundKeyPacket;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Crypt;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ServerLoginPacketListenerImpl implements ServerLoginPacketListener {
/*  40 */   private static final AtomicInteger UNIQUE_THREAD_ID = new AtomicInteger(0);
/*  41 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  43 */   private static final Random RANDOM = new Random();
/*     */   
/*  45 */   private final byte[] nonce = new byte[4];
/*     */   private final MinecraftServer server;
/*     */   public final Connection connection;
/*  48 */   private State state = State.HELLO;
/*     */   private int tick;
/*     */   private GameProfile gameProfile;
/*  51 */   private final String serverId = "";
/*     */   private SecretKey secretKey;
/*     */   private ServerPlayer delayedAcceptPlayer;
/*     */   
/*     */   public ServerLoginPacketListenerImpl(MinecraftServer debug1, Connection debug2) {
/*  56 */     this.server = debug1;
/*  57 */     this.connection = debug2;
/*  58 */     RANDOM.nextBytes(this.nonce);
/*     */   }
/*     */   
/*     */   public void tick() {
/*  62 */     if (this.state == State.READY_TO_ACCEPT) {
/*  63 */       handleAcceptedLogin();
/*  64 */     } else if (this.state == State.DELAY_ACCEPT) {
/*  65 */       ServerPlayer debug1 = this.server.getPlayerList().getPlayer(this.gameProfile.getId());
/*  66 */       if (debug1 == null) {
/*  67 */         this.state = State.READY_TO_ACCEPT;
/*  68 */         this.server.getPlayerList().placeNewPlayer(this.connection, this.delayedAcceptPlayer);
/*  69 */         this.delayedAcceptPlayer = null;
/*     */       } 
/*     */     } 
/*  72 */     if (this.tick++ == 600) {
/*  73 */       disconnect((Component)new TranslatableComponent("multiplayer.disconnect.slow_login"));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Connection getConnection() {
/*  79 */     return this.connection;
/*     */   }
/*     */   
/*     */   public void disconnect(Component debug1) {
/*     */     try {
/*  84 */       LOGGER.info("Disconnecting {}: {}", getUserName(), debug1.getString());
/*  85 */       this.connection.send((Packet)new ClientboundLoginDisconnectPacket(debug1));
/*  86 */       this.connection.disconnect(debug1);
/*  87 */     } catch (Exception debug2) {
/*  88 */       LOGGER.error("Error whilst disconnecting player", debug2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handleAcceptedLogin() {
/*  93 */     if (!this.gameProfile.isComplete()) {
/*  94 */       this.gameProfile = createFakeProfile(this.gameProfile);
/*     */     }
/*     */     
/*  97 */     Component debug1 = this.server.getPlayerList().canPlayerLogin(this.connection.getRemoteAddress(), this.gameProfile);
/*  98 */     if (debug1 != null) {
/*  99 */       disconnect(debug1);
/*     */     } else {
/* 101 */       this.state = State.ACCEPTED;
/* 102 */       if (this.server.getCompressionThreshold() >= 0 && !this.connection.isMemoryConnection()) {
/* 103 */         this.connection.send((Packet)new ClientboundLoginCompressionPacket(this.server.getCompressionThreshold()), (GenericFutureListener)(debug1 -> this.connection.setupCompression(this.server.getCompressionThreshold())));
/*     */       }
/* 105 */       this.connection.send((Packet)new ClientboundGameProfilePacket(this.gameProfile));
/* 106 */       ServerPlayer debug2 = this.server.getPlayerList().getPlayer(this.gameProfile.getId());
/* 107 */       if (debug2 != null) {
/* 108 */         this.state = State.DELAY_ACCEPT;
/* 109 */         this.delayedAcceptPlayer = this.server.getPlayerList().getPlayerForLogin(this.gameProfile);
/*     */       } else {
/* 111 */         this.server.getPlayerList().placeNewPlayer(this.connection, this.server.getPlayerList().getPlayerForLogin(this.gameProfile));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisconnect(Component debug1) {
/* 118 */     LOGGER.info("{} lost connection: {}", getUserName(), debug1.getString());
/*     */   }
/*     */   
/*     */   public String getUserName() {
/* 122 */     if (this.gameProfile != null) {
/* 123 */       return this.gameProfile + " (" + this.connection.getRemoteAddress() + ")";
/*     */     }
/* 125 */     return String.valueOf(this.connection.getRemoteAddress());
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleHello(ServerboundHelloPacket debug1) {
/* 130 */     Validate.validState((this.state == State.HELLO), "Unexpected hello packet", new Object[0]);
/* 131 */     this.gameProfile = debug1.getGameProfile();
/*     */     
/* 133 */     if (this.server.usesAuthentication() && !this.connection.isMemoryConnection()) {
/* 134 */       this.state = State.KEY;
/* 135 */       this.connection.send((Packet)new ClientboundHelloPacket("", this.server.getKeyPair().getPublic(), this.nonce));
/*     */     } else {
/* 137 */       this.state = State.READY_TO_ACCEPT;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleKey(ServerboundKeyPacket debug1) {
/* 143 */     Validate.validState((this.state == State.KEY), "Unexpected key packet", new Object[0]);
/* 144 */     PrivateKey debug2 = this.server.getKeyPair().getPrivate();
/*     */     
/* 146 */     if (!Arrays.equals(this.nonce, debug1.getNonce(debug2))) {
/* 147 */       throw new IllegalStateException("Invalid nonce!");
/*     */     }
/*     */     
/* 150 */     this.secretKey = debug1.getSecretKey(debug2);
/* 151 */     this.state = State.AUTHENTICATING;
/*     */     
/* 153 */     this.connection.setEncryptionKey(this.secretKey);
/*     */     
/* 155 */     Thread debug3 = new Thread("User Authenticator #" + UNIQUE_THREAD_ID.incrementAndGet())
/*     */       {
/*     */         public void run() {
/* 158 */           GameProfile debug1 = ServerLoginPacketListenerImpl.this.gameProfile;
/*     */           
/*     */           try {
/* 161 */             String debug2 = (new BigInteger(Crypt.digestData("", ServerLoginPacketListenerImpl.this.server.getKeyPair().getPublic(), ServerLoginPacketListenerImpl.this.secretKey))).toString(16);
/* 162 */             ServerLoginPacketListenerImpl.this.gameProfile = ServerLoginPacketListenerImpl.this.server.getSessionService().hasJoinedServer(new GameProfile(null, debug1.getName()), debug2, getAddress());
/*     */             
/* 164 */             if (ServerLoginPacketListenerImpl.this.gameProfile != null) {
/* 165 */               ServerLoginPacketListenerImpl.LOGGER.info("UUID of player {} is {}", ServerLoginPacketListenerImpl.this.gameProfile.getName(), ServerLoginPacketListenerImpl.this.gameProfile.getId());
/* 166 */               ServerLoginPacketListenerImpl.this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
/* 167 */             } else if (ServerLoginPacketListenerImpl.this.server.isSingleplayer()) {
/* 168 */               ServerLoginPacketListenerImpl.LOGGER.warn("Failed to verify username but will let them in anyway!");
/* 169 */               ServerLoginPacketListenerImpl.this.gameProfile = ServerLoginPacketListenerImpl.this.createFakeProfile(debug1);
/* 170 */               ServerLoginPacketListenerImpl.this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
/*     */             } else {
/* 172 */               ServerLoginPacketListenerImpl.this.disconnect((Component)new TranslatableComponent("multiplayer.disconnect.unverified_username"));
/* 173 */               ServerLoginPacketListenerImpl.LOGGER.error("Username '{}' tried to join with an invalid session", debug1.getName());
/*     */             } 
/* 175 */           } catch (AuthenticationUnavailableException debug2) {
/* 176 */             if (ServerLoginPacketListenerImpl.this.server.isSingleplayer()) {
/* 177 */               ServerLoginPacketListenerImpl.LOGGER.warn("Authentication servers are down but will let them in anyway!");
/* 178 */               ServerLoginPacketListenerImpl.this.gameProfile = ServerLoginPacketListenerImpl.this.createFakeProfile(debug1);
/* 179 */               ServerLoginPacketListenerImpl.this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
/*     */             } else {
/* 181 */               ServerLoginPacketListenerImpl.this.disconnect((Component)new TranslatableComponent("multiplayer.disconnect.authservers_down"));
/* 182 */               ServerLoginPacketListenerImpl.LOGGER.error("Couldn't verify username because servers are unavailable");
/*     */             } 
/*     */           } 
/*     */         }
/*     */         
/*     */         @Nullable
/*     */         private InetAddress getAddress() {
/* 189 */           SocketAddress debug1 = ServerLoginPacketListenerImpl.this.connection.getRemoteAddress();
/* 190 */           return (ServerLoginPacketListenerImpl.this.server.getPreventProxyConnections() && debug1 instanceof InetSocketAddress) ? ((InetSocketAddress)debug1).getAddress() : null;
/*     */         }
/*     */       };
/* 193 */     debug3.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(LOGGER));
/* 194 */     debug3.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleCustomQueryPacket(ServerboundCustomQueryPacket debug1) {
/* 199 */     disconnect((Component)new TranslatableComponent("multiplayer.disconnect.unexpected_query_response"));
/*     */   }
/*     */   
/*     */   protected GameProfile createFakeProfile(GameProfile debug1) {
/* 203 */     UUID debug2 = Player.createPlayerUUID(debug1.getName());
/* 204 */     return new GameProfile(debug2, debug1.getName());
/*     */   }
/*     */   
/*     */   enum State {
/* 208 */     HELLO, KEY, AUTHENTICATING, NEGOTIATING, READY_TO_ACCEPT, DELAY_ACCEPT, ACCEPTED;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\network\ServerLoginPacketListenerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */