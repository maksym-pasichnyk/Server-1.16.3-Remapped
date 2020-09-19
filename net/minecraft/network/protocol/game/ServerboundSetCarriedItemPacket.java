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
/*    */ public class ServerboundSetCarriedItemPacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private int slot;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 20 */     this.slot = debug1.readShort();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 25 */     debug1.writeShort(this.slot);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 30 */     debug1.handleSetCarriedItem(this);
/*    */   }
/*    */   
/*    */   public int getSlot() {
/* 34 */     return this.slot;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundSetCarriedItemPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */