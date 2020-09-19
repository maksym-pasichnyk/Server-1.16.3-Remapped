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
/*    */ public class ServerboundSelectTradePacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private int item;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 20 */     this.item = debug1.readVarInt();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 25 */     debug1.writeVarInt(this.item);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 30 */     debug1.handleSelectTrade(this);
/*    */   }
/*    */   
/*    */   public int getItem() {
/* 34 */     return this.item;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundSelectTradePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */