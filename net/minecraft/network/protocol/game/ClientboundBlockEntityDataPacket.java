/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.CompoundTag;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClientboundBlockEntityDataPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private BlockPos pos;
/*    */   private int type;
/*    */   private CompoundTag tag;
/*    */   
/*    */   public ClientboundBlockEntityDataPacket() {}
/*    */   
/*    */   public ClientboundBlockEntityDataPacket(BlockPos debug1, int debug2, CompoundTag debug3) {
/* 34 */     this.pos = debug1;
/* 35 */     this.type = debug2;
/* 36 */     this.tag = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 41 */     this.pos = debug1.readBlockPos();
/* 42 */     this.type = debug1.readUnsignedByte();
/* 43 */     this.tag = debug1.readNbt();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 48 */     debug1.writeBlockPos(this.pos);
/* 49 */     debug1.writeByte((byte)this.type);
/* 50 */     debug1.writeNbt(this.tag);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 55 */     debug1.handleBlockEntityData(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundBlockEntityDataPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */