/*    */ package net.minecraft.server.network;
/*    */ import net.minecraft.network.Connection;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.status.ClientboundPongResponsePacket;
/*    */ import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
/*    */ import net.minecraft.network.protocol.status.ServerStatusPacketListener;
/*    */ import net.minecraft.network.protocol.status.ServerboundPingRequestPacket;
/*    */ import net.minecraft.network.protocol.status.ServerboundStatusRequestPacket;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ public class ServerStatusPacketListenerImpl implements ServerStatusPacketListener {
/* 14 */   private static final Component DISCONNECT_REASON = (Component)new TranslatableComponent("multiplayer.status.request_handled");
/*    */   
/*    */   private final MinecraftServer server;
/*    */   private final Connection connection;
/*    */   private boolean hasRequestedStatus;
/*    */   
/*    */   public ServerStatusPacketListenerImpl(MinecraftServer debug1, Connection debug2) {
/* 21 */     this.server = debug1;
/* 22 */     this.connection = debug2;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDisconnect(Component debug1) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public Connection getConnection() {
/* 32 */     return this.connection;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleStatusRequest(ServerboundStatusRequestPacket debug1) {
/* 37 */     if (this.hasRequestedStatus) {
/* 38 */       this.connection.disconnect(DISCONNECT_REASON);
/*    */       return;
/*    */     } 
/* 41 */     this.hasRequestedStatus = true;
/* 42 */     this.connection.send((Packet)new ClientboundStatusResponsePacket(this.server.getStatus()));
/*    */   }
/*    */ 
/*    */   
/*    */   public void handlePingRequest(ServerboundPingRequestPacket debug1) {
/* 47 */     this.connection.send((Packet)new ClientboundPongResponsePacket(debug1.getTime()));
/* 48 */     this.connection.disconnect(DISCONNECT_REASON);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\network\ServerStatusPacketListenerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */