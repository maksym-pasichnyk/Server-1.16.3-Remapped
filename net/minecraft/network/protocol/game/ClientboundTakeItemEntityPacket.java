/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundTakeItemEntityPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private int itemId;
/*    */   private int playerId;
/*    */   private int amount;
/*    */   
/*    */   public ClientboundTakeItemEntityPacket() {}
/*    */   
/*    */   public ClientboundTakeItemEntityPacket(int debug1, int debug2, int debug3) {
/* 17 */     this.itemId = debug1;
/* 18 */     this.playerId = debug2;
/* 19 */     this.amount = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 24 */     this.itemId = debug1.readVarInt();
/* 25 */     this.playerId = debug1.readVarInt();
/* 26 */     this.amount = debug1.readVarInt();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 31 */     debug1.writeVarInt(this.itemId);
/* 32 */     debug1.writeVarInt(this.playerId);
/* 33 */     debug1.writeVarInt(this.amount);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 38 */     debug1.handleTakeItemEntity(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundTakeItemEntityPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */