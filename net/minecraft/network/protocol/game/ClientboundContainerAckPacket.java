/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundContainerAckPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private int containerId;
/*    */   private short uid;
/*    */   private boolean accepted;
/*    */   
/*    */   public ClientboundContainerAckPacket() {}
/*    */   
/*    */   public ClientboundContainerAckPacket(int debug1, short debug2, boolean debug3) {
/* 18 */     this.containerId = debug1;
/* 19 */     this.uid = debug2;
/* 20 */     this.accepted = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 25 */     debug1.handleContainerAck(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 30 */     this.containerId = debug1.readUnsignedByte();
/* 31 */     this.uid = debug1.readShort();
/* 32 */     this.accepted = debug1.readBoolean();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 37 */     debug1.writeByte(this.containerId);
/* 38 */     debug1.writeShort(this.uid);
/* 39 */     debug1.writeBoolean(this.accepted);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundContainerAckPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */