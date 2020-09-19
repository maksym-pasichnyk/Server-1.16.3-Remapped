/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public class ServerboundEntityTagQuery
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private int transactionId;
/*    */   private int entityId;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 23 */     this.transactionId = debug1.readVarInt();
/* 24 */     this.entityId = debug1.readVarInt();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 29 */     debug1.writeVarInt(this.transactionId);
/* 30 */     debug1.writeVarInt(this.entityId);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 35 */     debug1.handleEntityTagQuery(this);
/*    */   }
/*    */   
/*    */   public int getTransactionId() {
/* 39 */     return this.transactionId;
/*    */   }
/*    */   
/*    */   public int getEntityId() {
/* 43 */     return this.entityId;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundEntityTagQuery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */