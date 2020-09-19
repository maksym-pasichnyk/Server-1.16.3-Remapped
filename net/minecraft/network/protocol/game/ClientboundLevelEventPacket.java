/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundLevelEventPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private int type;
/*    */   private BlockPos pos;
/*    */   private int data;
/*    */   private boolean globalEvent;
/*    */   
/*    */   public ClientboundLevelEventPacket() {}
/*    */   
/*    */   public ClientboundLevelEventPacket(int debug1, BlockPos debug2, int debug3, boolean debug4) {
/* 20 */     this.type = debug1;
/* 21 */     this.pos = debug2.immutable();
/* 22 */     this.data = debug3;
/* 23 */     this.globalEvent = debug4;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 28 */     this.type = debug1.readInt();
/* 29 */     this.pos = debug1.readBlockPos();
/* 30 */     this.data = debug1.readInt();
/* 31 */     this.globalEvent = debug1.readBoolean();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 36 */     debug1.writeInt(this.type);
/* 37 */     debug1.writeBlockPos(this.pos);
/* 38 */     debug1.writeInt(this.data);
/* 39 */     debug1.writeBoolean(this.globalEvent);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 44 */     debug1.handleLevelEvent(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundLevelEventPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */