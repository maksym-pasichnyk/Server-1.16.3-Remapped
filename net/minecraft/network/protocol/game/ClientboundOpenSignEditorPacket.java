/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundOpenSignEditorPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private BlockPos pos;
/*    */   
/*    */   public ClientboundOpenSignEditorPacket() {}
/*    */   
/*    */   public ClientboundOpenSignEditorPacket(BlockPos debug1) {
/* 16 */     this.pos = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 21 */     debug1.handleOpenSignEditor(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 26 */     this.pos = debug1.readBlockPos();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 31 */     debug1.writeBlockPos(this.pos);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundOpenSignEditorPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */