/*    */ package net.minecraft.network.protocol.login;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.security.PublicKey;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.util.Crypt;
/*    */ 
/*    */ public class ClientboundHelloPacket
/*    */   implements Packet<ClientLoginPacketListener> {
/*    */   private String serverId;
/*    */   private PublicKey publicKey;
/*    */   private byte[] nonce;
/*    */   
/*    */   public ClientboundHelloPacket() {}
/*    */   
/*    */   public ClientboundHelloPacket(String debug1, PublicKey debug2, byte[] debug3) {
/* 19 */     this.serverId = debug1;
/* 20 */     this.publicKey = debug2;
/* 21 */     this.nonce = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 26 */     this.serverId = debug1.readUtf(20);
/* 27 */     this.publicKey = Crypt.byteToPublicKey(debug1.readByteArray());
/* 28 */     this.nonce = debug1.readByteArray();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 33 */     debug1.writeUtf(this.serverId);
/* 34 */     debug1.writeByteArray(this.publicKey.getEncoded());
/* 35 */     debug1.writeByteArray(this.nonce);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientLoginPacketListener debug1) {
/* 40 */     debug1.handleHello(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\login\ClientboundHelloPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */