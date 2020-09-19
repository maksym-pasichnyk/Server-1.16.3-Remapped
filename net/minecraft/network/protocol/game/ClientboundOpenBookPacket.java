/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ 
/*    */ public class ClientboundOpenBookPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private InteractionHand hand;
/*    */   
/*    */   public ClientboundOpenBookPacket() {}
/*    */   
/*    */   public ClientboundOpenBookPacket(InteractionHand debug1) {
/* 16 */     this.hand = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 21 */     this.hand = (InteractionHand)debug1.readEnum(InteractionHand.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 26 */     debug1.writeEnum((Enum)this.hand);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 31 */     debug1.handleOpenBook(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundOpenBookPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */