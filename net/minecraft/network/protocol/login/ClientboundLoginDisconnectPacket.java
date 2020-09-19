/*    */ package net.minecraft.network.protocol.login;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundLoginDisconnectPacket
/*    */   implements Packet<ClientLoginPacketListener> {
/*    */   private Component reason;
/*    */   
/*    */   public ClientboundLoginDisconnectPacket() {}
/*    */   
/*    */   public ClientboundLoginDisconnectPacket(Component debug1) {
/* 16 */     this.reason = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 21 */     this.reason = (Component)Component.Serializer.fromJsonLenient(debug1.readUtf(262144));
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 26 */     debug1.writeComponent(this.reason);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientLoginPacketListener debug1) {
/* 31 */     debug1.handleDisconnect(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\login\ClientboundLoginDisconnectPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */