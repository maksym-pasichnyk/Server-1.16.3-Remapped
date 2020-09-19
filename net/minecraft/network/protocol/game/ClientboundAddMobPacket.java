/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ClientboundAddMobPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private int id;
/*    */   private UUID uuid;
/*    */   private int type;
/*    */   private double x;
/*    */   private double y;
/*    */   private double z;
/*    */   private int xd;
/*    */   private int yd;
/*    */   private int zd;
/*    */   private byte yRot;
/*    */   private byte xRot;
/*    */   private byte yHeadRot;
/*    */   
/*    */   public ClientboundAddMobPacket() {}
/*    */   
/*    */   public ClientboundAddMobPacket(LivingEntity debug1) {
/* 32 */     this.id = debug1.getId();
/* 33 */     this.uuid = debug1.getUUID();
/*    */     
/* 35 */     this.type = Registry.ENTITY_TYPE.getId(debug1.getType());
/* 36 */     this.x = debug1.getX();
/* 37 */     this.y = debug1.getY();
/* 38 */     this.z = debug1.getZ();
/* 39 */     this.yRot = (byte)(int)(debug1.yRot * 256.0F / 360.0F);
/* 40 */     this.xRot = (byte)(int)(debug1.xRot * 256.0F / 360.0F);
/* 41 */     this.yHeadRot = (byte)(int)(debug1.yHeadRot * 256.0F / 360.0F);
/*    */ 
/*    */     
/* 44 */     double debug2 = 3.9D;
/*    */     
/* 46 */     Vec3 debug4 = debug1.getDeltaMovement();
/* 47 */     double debug5 = Mth.clamp(debug4.x, -3.9D, 3.9D);
/* 48 */     double debug7 = Mth.clamp(debug4.y, -3.9D, 3.9D);
/* 49 */     double debug9 = Mth.clamp(debug4.z, -3.9D, 3.9D);
/*    */     
/* 51 */     this.xd = (int)(debug5 * 8000.0D);
/* 52 */     this.yd = (int)(debug7 * 8000.0D);
/* 53 */     this.zd = (int)(debug9 * 8000.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 58 */     this.id = debug1.readVarInt();
/* 59 */     this.uuid = debug1.readUUID();
/* 60 */     this.type = debug1.readVarInt();
/* 61 */     this.x = debug1.readDouble();
/* 62 */     this.y = debug1.readDouble();
/* 63 */     this.z = debug1.readDouble();
/* 64 */     this.yRot = debug1.readByte();
/* 65 */     this.xRot = debug1.readByte();
/* 66 */     this.yHeadRot = debug1.readByte();
/* 67 */     this.xd = debug1.readShort();
/* 68 */     this.yd = debug1.readShort();
/* 69 */     this.zd = debug1.readShort();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 74 */     debug1.writeVarInt(this.id);
/* 75 */     debug1.writeUUID(this.uuid);
/* 76 */     debug1.writeVarInt(this.type);
/* 77 */     debug1.writeDouble(this.x);
/* 78 */     debug1.writeDouble(this.y);
/* 79 */     debug1.writeDouble(this.z);
/* 80 */     debug1.writeByte(this.yRot);
/* 81 */     debug1.writeByte(this.xRot);
/* 82 */     debug1.writeByte(this.yHeadRot);
/* 83 */     debug1.writeShort(this.xd);
/* 84 */     debug1.writeShort(this.yd);
/* 85 */     debug1.writeShort(this.zd);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 90 */     debug1.handleAddMob(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundAddMobPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */