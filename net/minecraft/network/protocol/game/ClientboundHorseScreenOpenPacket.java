/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundHorseScreenOpenPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private int containerId;
/*    */   private int size;
/*    */   private int entityId;
/*    */   
/*    */   public ClientboundHorseScreenOpenPacket() {}
/*    */   
/*    */   public ClientboundHorseScreenOpenPacket(int debug1, int debug2, int debug3) {
/* 17 */     this.containerId = debug1;
/* 18 */     this.size = debug2;
/* 19 */     this.entityId = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 24 */     debug1.handleHorseScreenOpen(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 29 */     this.containerId = debug1.readUnsignedByte();
/* 30 */     this.size = debug1.readVarInt();
/* 31 */     this.entityId = debug1.readInt();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 36 */     debug1.writeByte(this.containerId);
/* 37 */     debug1.writeVarInt(this.size);
/* 38 */     debug1.writeInt(this.entityId);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundHorseScreenOpenPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */