/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class ClientboundBlockEventPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private BlockPos pos;
/*    */   private int b0;
/*    */   private int b1;
/*    */   private Block block;
/*    */   
/*    */   public ClientboundBlockEventPacket() {}
/*    */   
/*    */   public ClientboundBlockEventPacket(BlockPos debug1, Block debug2, int debug3, int debug4) {
/* 21 */     this.pos = debug1;
/* 22 */     this.block = debug2;
/* 23 */     this.b0 = debug3;
/* 24 */     this.b1 = debug4;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 29 */     this.pos = debug1.readBlockPos();
/* 30 */     this.b0 = debug1.readUnsignedByte();
/* 31 */     this.b1 = debug1.readUnsignedByte();
/*    */     
/* 33 */     this.block = (Block)Registry.BLOCK.byId(debug1.readVarInt());
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 38 */     debug1.writeBlockPos(this.pos);
/* 39 */     debug1.writeByte(this.b0);
/* 40 */     debug1.writeByte(this.b1);
/* 41 */     debug1.writeVarInt(Registry.BLOCK.getId(this.block));
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 46 */     debug1.handleBlockEvent(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundBlockEventPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */