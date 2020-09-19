/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundSetCarriedItemPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private int slot;
/*    */   
/*    */   public ClientboundSetCarriedItemPacket() {}
/*    */   
/*    */   public ClientboundSetCarriedItemPacket(int debug1) {
/* 16 */     this.slot = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 21 */     this.slot = debug1.readByte();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 26 */     debug1.writeByte(this.slot);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 31 */     debug1.handleSetCarriedItem(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetCarriedItemPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */