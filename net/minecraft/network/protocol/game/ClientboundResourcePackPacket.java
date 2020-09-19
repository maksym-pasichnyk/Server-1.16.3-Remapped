/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClientboundResourcePackPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private String url;
/*    */   private String hash;
/*    */   
/*    */   public ClientboundResourcePackPacket() {}
/*    */   
/*    */   public ClientboundResourcePackPacket(String debug1, String debug2) {
/* 19 */     this.url = debug1;
/* 20 */     this.hash = debug2;
/*    */     
/* 22 */     if (debug2.length() > 40) {
/* 23 */       throw new IllegalArgumentException("Hash is too long (max 40, was " + debug2.length() + ")");
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 29 */     this.url = debug1.readUtf(32767);
/* 30 */     this.hash = debug1.readUtf(40);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 35 */     debug1.writeUtf(this.url);
/* 36 */     debug1.writeUtf(this.hash);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 41 */     debug1.handleResourcePack(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundResourcePackPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */