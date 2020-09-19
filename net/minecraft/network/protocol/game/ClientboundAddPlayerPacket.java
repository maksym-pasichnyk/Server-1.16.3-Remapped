/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class ClientboundAddPlayerPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private int entityId;
/*    */   private UUID playerId;
/*    */   private double x;
/*    */   private double y;
/*    */   private double z;
/*    */   private byte yRot;
/*    */   private byte xRot;
/*    */   
/*    */   public ClientboundAddPlayerPacket() {}
/*    */   
/*    */   public ClientboundAddPlayerPacket(Player debug1) {
/* 24 */     this.entityId = debug1.getId();
/* 25 */     this.playerId = debug1.getGameProfile().getId();
/* 26 */     this.x = debug1.getX();
/* 27 */     this.y = debug1.getY();
/* 28 */     this.z = debug1.getZ();
/* 29 */     this.yRot = (byte)(int)(debug1.yRot * 256.0F / 360.0F);
/* 30 */     this.xRot = (byte)(int)(debug1.xRot * 256.0F / 360.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 35 */     this.entityId = debug1.readVarInt();
/* 36 */     this.playerId = debug1.readUUID();
/* 37 */     this.x = debug1.readDouble();
/* 38 */     this.y = debug1.readDouble();
/* 39 */     this.z = debug1.readDouble();
/* 40 */     this.yRot = debug1.readByte();
/* 41 */     this.xRot = debug1.readByte();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 46 */     debug1.writeVarInt(this.entityId);
/* 47 */     debug1.writeUUID(this.playerId);
/* 48 */     debug1.writeDouble(this.x);
/* 49 */     debug1.writeDouble(this.y);
/* 50 */     debug1.writeDouble(this.z);
/* 51 */     debug1.writeByte(this.yRot);
/* 52 */     debug1.writeByte(this.xRot);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 57 */     debug1.handleAddPlayer(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundAddPlayerPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */