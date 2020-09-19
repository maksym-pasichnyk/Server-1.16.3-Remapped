/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundContainerSetDataPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private int containerId;
/*    */   private int id;
/*    */   private int value;
/*    */   
/*    */   public ClientboundContainerSetDataPacket() {}
/*    */   
/*    */   public ClientboundContainerSetDataPacket(int debug1, int debug2, int debug3) {
/* 18 */     this.containerId = debug1;
/* 19 */     this.id = debug2;
/* 20 */     this.value = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 25 */     debug1.handleContainerSetData(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 30 */     this.containerId = debug1.readUnsignedByte();
/* 31 */     this.id = debug1.readShort();
/* 32 */     this.value = debug1.readShort();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 38 */     debug1.writeByte(this.containerId);
/* 39 */     debug1.writeShort(this.id);
/* 40 */     debug1.writeShort(this.value);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundContainerSetDataPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */