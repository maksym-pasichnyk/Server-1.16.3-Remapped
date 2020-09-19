/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.entity.decoration.Painting;
/*    */ 
/*    */ 
/*    */ public class ClientboundAddPaintingPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private int id;
/*    */   private UUID uuid;
/*    */   private BlockPos pos;
/*    */   private Direction direction;
/*    */   private int motive;
/*    */   
/*    */   public ClientboundAddPaintingPacket() {}
/*    */   
/*    */   public ClientboundAddPaintingPacket(Painting debug1) {
/* 26 */     this.id = debug1.getId();
/* 27 */     this.uuid = debug1.getUUID();
/* 28 */     this.pos = debug1.getPos();
/* 29 */     this.direction = debug1.getDirection();
/* 30 */     this.motive = Registry.MOTIVE.getId(debug1.motive);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 35 */     this.id = debug1.readVarInt();
/* 36 */     this.uuid = debug1.readUUID();
/* 37 */     this.motive = debug1.readVarInt();
/* 38 */     this.pos = debug1.readBlockPos();
/* 39 */     this.direction = Direction.from2DDataValue(debug1.readUnsignedByte());
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 44 */     debug1.writeVarInt(this.id);
/* 45 */     debug1.writeUUID(this.uuid);
/* 46 */     debug1.writeVarInt(this.motive);
/* 47 */     debug1.writeBlockPos(this.pos);
/* 48 */     debug1.writeByte(this.direction.get2DDataValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 53 */     debug1.handleAddPainting(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundAddPaintingPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */