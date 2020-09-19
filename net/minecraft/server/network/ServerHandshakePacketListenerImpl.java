/*    */ package net.minecraft.server.network;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.network.Connection;
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TextComponent;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
/*    */ import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ public class ServerHandshakePacketListenerImpl implements ServerHandshakePacketListener {
/* 15 */   private static final Component IGNORE_STATUS_REASON = (Component)new TextComponent("Ignoring status request");
/*    */   
/*    */   private final MinecraftServer server;
/*    */   private final Connection connection;
/*    */   
/*    */   public ServerHandshakePacketListenerImpl(MinecraftServer debug1, Connection debug2) {
/* 21 */     this.server = debug1;
/* 22 */     this.connection = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleIntention(ClientIntentionPacket debug1) {
/* 27 */     switch (debug1.getIntention()) {
/*    */       case LOGIN:
/* 29 */         this.connection.setProtocol(ConnectionProtocol.LOGIN);
/*    */         
/* 31 */         if (debug1.getProtocolVersion() > SharedConstants.getCurrentVersion().getProtocolVersion()) {
/* 32 */           TranslatableComponent translatableComponent = new TranslatableComponent("multiplayer.disconnect.outdated_server", new Object[] { SharedConstants.getCurrentVersion().getName() });
/* 33 */           this.connection.send((Packet)new ClientboundLoginDisconnectPacket((Component)translatableComponent));
/* 34 */           this.connection.disconnect((Component)translatableComponent);
/* 35 */         } else if (debug1.getProtocolVersion() < SharedConstants.getCurrentVersion().getProtocolVersion()) {
/* 36 */           TranslatableComponent translatableComponent = new TranslatableComponent("multiplayer.disconnect.outdated_client", new Object[] { SharedConstants.getCurrentVersion().getName() });
/* 37 */           this.connection.send((Packet)new ClientboundLoginDisconnectPacket((Component)translatableComponent));
/* 38 */           this.connection.disconnect((Component)translatableComponent);
/*    */         } else {
/* 40 */           this.connection.setListener((PacketListener)new ServerLoginPacketListenerImpl(this.server, this.connection));
/*    */         } 
/*    */         return;
/*    */       case STATUS:
/* 44 */         if (this.server.repliesToStatus()) {
/* 45 */           this.connection.setProtocol(ConnectionProtocol.STATUS);
/* 46 */           this.connection.setListener((PacketListener)new ServerStatusPacketListenerImpl(this.server, this.connection));
/*    */         } else {
/* 48 */           this.connection.disconnect(IGNORE_STATUS_REASON);
/*    */         } 
/*    */         return;
/*    */     } 
/* 52 */     throw new UnsupportedOperationException("Invalid intention " + debug1.getIntention());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDisconnect(Component debug1) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public Connection getConnection() {
/* 63 */     return this.connection;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\network\ServerHandshakePacketListenerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */