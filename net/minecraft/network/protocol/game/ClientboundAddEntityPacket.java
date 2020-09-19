/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class ClientboundAddEntityPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private int id;
/*    */   private UUID uuid;
/*    */   private double x;
/*    */   private double y;
/*    */   private double z;
/*    */   private int xa;
/*    */   private int ya;
/*    */   private int za;
/*    */   private int xRot;
/*    */   private int yRot;
/*    */   private EntityType<?> type;
/*    */   private int data;
/*    */   
/*    */   public ClientboundAddEntityPacket() {}
/*    */   
/*    */   public ClientboundAddEntityPacket(int debug1, UUID debug2, double debug3, double debug5, double debug7, float debug9, float debug10, EntityType<?> debug11, int debug12, Vec3 debug13) {
/* 35 */     this.id = debug1;
/* 36 */     this.uuid = debug2;
/* 37 */     this.x = debug3;
/* 38 */     this.y = debug5;
/* 39 */     this.z = debug7;
/* 40 */     this.xRot = Mth.floor(debug9 * 256.0F / 360.0F);
/* 41 */     this.yRot = Mth.floor(debug10 * 256.0F / 360.0F);
/* 42 */     this.type = debug11;
/* 43 */     this.data = debug12;
/*    */     
/* 45 */     this.xa = (int)(Mth.clamp(debug13.x, -3.9D, 3.9D) * 8000.0D);
/* 46 */     this.ya = (int)(Mth.clamp(debug13.y, -3.9D, 3.9D) * 8000.0D);
/* 47 */     this.za = (int)(Mth.clamp(debug13.z, -3.9D, 3.9D) * 8000.0D);
/*    */   }
/*    */   
/*    */   public ClientboundAddEntityPacket(Entity debug1) {
/* 51 */     this(debug1, 0);
/*    */   }
/*    */   
/*    */   public ClientboundAddEntityPacket(Entity debug1, int debug2) {
/* 55 */     this(debug1.getId(), debug1.getUUID(), debug1.getX(), debug1.getY(), debug1.getZ(), debug1.xRot, debug1.yRot, debug1.getType(), debug2, debug1.getDeltaMovement());
/*    */   }
/*    */   
/*    */   public ClientboundAddEntityPacket(Entity debug1, EntityType<?> debug2, int debug3, BlockPos debug4) {
/* 59 */     this(debug1.getId(), debug1.getUUID(), debug4.getX(), debug4.getY(), debug4.getZ(), debug1.xRot, debug1.yRot, debug2, debug3, debug1.getDeltaMovement());
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 64 */     this.id = debug1.readVarInt();
/* 65 */     this.uuid = debug1.readUUID();
/* 66 */     this.type = (EntityType)Registry.ENTITY_TYPE.byId(debug1.readVarInt());
/* 67 */     this.x = debug1.readDouble();
/* 68 */     this.y = debug1.readDouble();
/* 69 */     this.z = debug1.readDouble();
/* 70 */     this.xRot = debug1.readByte();
/* 71 */     this.yRot = debug1.readByte();
/* 72 */     this.data = debug1.readInt();
/*    */     
/* 74 */     this.xa = debug1.readShort();
/* 75 */     this.ya = debug1.readShort();
/* 76 */     this.za = debug1.readShort();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 81 */     debug1.writeVarInt(this.id);
/* 82 */     debug1.writeUUID(this.uuid);
/* 83 */     debug1.writeVarInt(Registry.ENTITY_TYPE.getId(this.type));
/* 84 */     debug1.writeDouble(this.x);
/* 85 */     debug1.writeDouble(this.y);
/* 86 */     debug1.writeDouble(this.z);
/* 87 */     debug1.writeByte(this.xRot);
/* 88 */     debug1.writeByte(this.yRot);
/* 89 */     debug1.writeInt(this.data);
/*    */     
/* 91 */     debug1.writeShort(this.xa);
/* 92 */     debug1.writeShort(this.ya);
/* 93 */     debug1.writeShort(this.za);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 98 */     debug1.handleAddEntity(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundAddEntityPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */