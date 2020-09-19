/*    */ package net.minecraft.network.protocol.login;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundLoginCompressionPacket
/*    */   implements Packet<ClientLoginPacketListener> {
/*    */   private int compressionThreshold;
/*    */   
/*    */   public ClientboundLoginCompressionPacket() {}
/*    */   
/*    */   public ClientboundLoginCompressionPacket(int debug1) {
/* 15 */     this.compressionThreshold = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 20 */     this.compressionThreshold = debug1.readVarInt();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 25 */     debug1.writeVarInt(this.compressionThreshold);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientLoginPacketListener debug1) {
/* 30 */     debug1.handleCompression(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\login\ClientboundLoginCompressionPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */