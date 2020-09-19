/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.core.BlockPos;
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
/*    */ public class ServerboundBlockEntityTagQuery
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private int transactionId;
/*    */   private BlockPos pos;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 24 */     this.transactionId = debug1.readVarInt();
/* 25 */     this.pos = debug1.readBlockPos();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 30 */     debug1.writeVarInt(this.transactionId);
/* 31 */     debug1.writeBlockPos(this.pos);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 36 */     debug1.handleBlockEntityTagQuery(this);
/*    */   }
/*    */   
/*    */   public int getTransactionId() {
/* 40 */     return this.transactionId;
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 44 */     return this.pos;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundBlockEntityTagQuery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */