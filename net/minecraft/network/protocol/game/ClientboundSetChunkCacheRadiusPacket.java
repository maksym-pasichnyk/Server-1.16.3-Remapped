/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundSetChunkCacheRadiusPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private int radius;
/*    */   
/*    */   public ClientboundSetChunkCacheRadiusPacket() {}
/*    */   
/*    */   public ClientboundSetChunkCacheRadiusPacket(int debug1) {
/* 15 */     this.radius = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 20 */     this.radius = debug1.readVarInt();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 25 */     debug1.writeVarInt(this.radius);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 30 */     debug1.handleSetChunkCacheRadius(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetChunkCacheRadiusPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */