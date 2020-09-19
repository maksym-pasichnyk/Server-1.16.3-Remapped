/*    */ package net.minecraft.network.protocol.handshake;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClientIntentionPacket
/*    */   implements Packet<ServerHandshakePacketListener>
/*    */ {
/*    */   private int protocolVersion;
/*    */   private String hostName;
/*    */   private int port;
/*    */   private ConnectionProtocol intention;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 32 */     this.protocolVersion = debug1.readVarInt();
/* 33 */     this.hostName = debug1.readUtf(255);
/* 34 */     this.port = debug1.readUnsignedShort();
/* 35 */     this.intention = ConnectionProtocol.getById(debug1.readVarInt());
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 40 */     debug1.writeVarInt(this.protocolVersion);
/* 41 */     debug1.writeUtf(this.hostName);
/* 42 */     debug1.writeShort(this.port);
/* 43 */     debug1.writeVarInt(this.intention.getId());
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerHandshakePacketListener debug1) {
/* 48 */     debug1.handleIntention(this);
/*    */   }
/*    */   
/*    */   public ConnectionProtocol getIntention() {
/* 52 */     return this.intention;
/*    */   }
/*    */   
/*    */   public int getProtocolVersion() {
/* 56 */     return this.protocolVersion;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\handshake\ClientIntentionPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */