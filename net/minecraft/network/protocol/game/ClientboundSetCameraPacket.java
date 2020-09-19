/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ 
/*    */ public class ClientboundSetCameraPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   public int cameraId;
/*    */   
/*    */   public ClientboundSetCameraPacket() {}
/*    */   
/*    */   public ClientboundSetCameraPacket(Entity debug1) {
/* 18 */     this.cameraId = debug1.getId();
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 23 */     this.cameraId = debug1.readVarInt();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 28 */     debug1.writeVarInt(this.cameraId);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 33 */     debug1.handleSetCamera(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetCameraPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */