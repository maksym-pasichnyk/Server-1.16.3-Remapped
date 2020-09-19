/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class ClientboundTeleportEntityPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private int id;
/*    */   private double x;
/*    */   private double y;
/*    */   private double z;
/*    */   private byte yRot;
/*    */   private byte xRot;
/*    */   private boolean onGround;
/*    */   
/*    */   public ClientboundTeleportEntityPacket() {}
/*    */   
/*    */   public ClientboundTeleportEntityPacket(Entity debug1) {
/* 22 */     this.id = debug1.getId();
/* 23 */     this.x = debug1.getX();
/* 24 */     this.y = debug1.getY();
/* 25 */     this.z = debug1.getZ();
/* 26 */     this.yRot = (byte)(int)(debug1.yRot * 256.0F / 360.0F);
/* 27 */     this.xRot = (byte)(int)(debug1.xRot * 256.0F / 360.0F);
/* 28 */     this.onGround = debug1.isOnGround();
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 33 */     this.id = debug1.readVarInt();
/* 34 */     this.x = debug1.readDouble();
/* 35 */     this.y = debug1.readDouble();
/* 36 */     this.z = debug1.readDouble();
/* 37 */     this.yRot = debug1.readByte();
/* 38 */     this.xRot = debug1.readByte();
/* 39 */     this.onGround = debug1.readBoolean();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 44 */     debug1.writeVarInt(this.id);
/* 45 */     debug1.writeDouble(this.x);
/* 46 */     debug1.writeDouble(this.y);
/* 47 */     debug1.writeDouble(this.z);
/* 48 */     debug1.writeByte(this.yRot);
/* 49 */     debug1.writeByte(this.xRot);
/* 50 */     debug1.writeBoolean(this.onGround);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 55 */     debug1.handleTeleportEntity(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundTeleportEntityPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */