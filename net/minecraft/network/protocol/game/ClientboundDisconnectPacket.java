/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundDisconnectPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private Component reason;
/*    */   
/*    */   public ClientboundDisconnectPacket() {}
/*    */   
/*    */   public ClientboundDisconnectPacket(Component debug1) {
/* 16 */     this.reason = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 21 */     this.reason = debug1.readComponent();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 26 */     debug1.writeComponent(this.reason);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 31 */     debug1.handleDisconnect(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundDisconnectPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */