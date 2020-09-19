/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundForgetLevelChunkPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private int x;
/*    */   private int z;
/*    */   
/*    */   public ClientboundForgetLevelChunkPacket() {}
/*    */   
/*    */   public ClientboundForgetLevelChunkPacket(int debug1, int debug2) {
/* 16 */     this.x = debug1;
/* 17 */     this.z = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 22 */     this.x = debug1.readInt();
/* 23 */     this.z = debug1.readInt();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 28 */     debug1.writeInt(this.x);
/* 29 */     debug1.writeInt(this.z);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 34 */     debug1.handleForgetLevelChunk(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundForgetLevelChunkPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */