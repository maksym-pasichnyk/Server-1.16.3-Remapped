/*    */ package net.minecraft.network.protocol.login;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.security.PrivateKey;
/*    */ import javax.crypto.SecretKey;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.util.Crypt;
/*    */ 
/*    */ public class ServerboundKeyPacket
/*    */   implements Packet<ServerLoginPacketListener> {
/* 13 */   private byte[] keybytes = new byte[0];
/* 14 */   private byte[] nonce = new byte[0];
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 26 */     this.keybytes = debug1.readByteArray();
/* 27 */     this.nonce = debug1.readByteArray();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 32 */     debug1.writeByteArray(this.keybytes);
/* 33 */     debug1.writeByteArray(this.nonce);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerLoginPacketListener debug1) {
/* 38 */     debug1.handleKey(this);
/*    */   }
/*    */   
/*    */   public SecretKey getSecretKey(PrivateKey debug1) {
/* 42 */     return Crypt.decryptByteToSecretKey(debug1, this.keybytes);
/*    */   }
/*    */   
/*    */   public byte[] getNonce(PrivateKey debug1) {
/* 46 */     if (debug1 == null) {
/* 47 */       return this.nonce;
/*    */     }
/* 49 */     return Crypt.decryptUsingKey(debug1, this.nonce);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\login\ServerboundKeyPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */