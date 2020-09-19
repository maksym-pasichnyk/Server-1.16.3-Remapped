/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public class ServerboundContainerAckPacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private int containerId;
/*    */   private short uid;
/*    */   private boolean accepted;
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 24 */     debug1.handleContainerAck(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 29 */     this.containerId = debug1.readByte();
/* 30 */     this.uid = debug1.readShort();
/* 31 */     this.accepted = (debug1.readByte() != 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 36 */     debug1.writeByte(this.containerId);
/* 37 */     debug1.writeShort(this.uid);
/* 38 */     debug1.writeByte(this.accepted ? 1 : 0);
/*    */   }
/*    */   
/*    */   public int getContainerId() {
/* 42 */     return this.containerId;
/*    */   }
/*    */   
/*    */   public short getUid() {
/* 46 */     return this.uid;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundContainerAckPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */