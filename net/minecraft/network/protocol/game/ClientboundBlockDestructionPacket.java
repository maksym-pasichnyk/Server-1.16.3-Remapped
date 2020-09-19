/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundBlockDestructionPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private int id;
/*    */   private BlockPos pos;
/*    */   private int progress;
/*    */   
/*    */   public ClientboundBlockDestructionPacket() {}
/*    */   
/*    */   public ClientboundBlockDestructionPacket(int debug1, BlockPos debug2, int debug3) {
/* 18 */     this.id = debug1;
/* 19 */     this.pos = debug2;
/* 20 */     this.progress = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 25 */     this.id = debug1.readVarInt();
/* 26 */     this.pos = debug1.readBlockPos();
/* 27 */     this.progress = debug1.readUnsignedByte();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 32 */     debug1.writeVarInt(this.id);
/* 33 */     debug1.writeBlockPos(this.pos);
/* 34 */     debug1.writeByte(this.progress);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 39 */     debug1.handleBlockDestruction(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundBlockDestructionPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */