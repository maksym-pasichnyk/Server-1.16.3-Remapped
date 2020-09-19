/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClientboundRemoveEntitiesPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private int[] entityIds;
/*    */   
/*    */   public ClientboundRemoveEntitiesPacket() {}
/*    */   
/*    */   public ClientboundRemoveEntitiesPacket(int... debug1) {
/* 18 */     this.entityIds = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 23 */     this.entityIds = new int[debug1.readVarInt()];
/*    */     
/* 25 */     for (int debug2 = 0; debug2 < this.entityIds.length; debug2++) {
/* 26 */       this.entityIds[debug2] = debug1.readVarInt();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 32 */     debug1.writeVarInt(this.entityIds.length);
/*    */     
/* 34 */     for (int debug5 : this.entityIds) {
/* 35 */       debug1.writeVarInt(debug5);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 41 */     debug1.handleRemoveEntity(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundRemoveEntitiesPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */