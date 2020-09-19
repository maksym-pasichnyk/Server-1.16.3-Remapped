/*    */ package net.minecraft.network.protocol.login;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServerboundCustomQueryPacket
/*    */   implements Packet<ServerLoginPacketListener>
/*    */ {
/*    */   private int transactionId;
/*    */   private FriendlyByteBuf data;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 25 */     this.transactionId = debug1.readVarInt();
/* 26 */     if (debug1.readBoolean()) {
/* 27 */       int debug2 = debug1.readableBytes();
/* 28 */       if (debug2 < 0 || debug2 > 1048576) {
/* 29 */         throw new IOException("Payload may not be larger than 1048576 bytes");
/*    */       }
/* 31 */       this.data = new FriendlyByteBuf(debug1.readBytes(debug2));
/*    */     } else {
/* 33 */       this.data = null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 39 */     debug1.writeVarInt(this.transactionId);
/* 40 */     if (this.data != null) {
/* 41 */       debug1.writeBoolean(true);
/* 42 */       debug1.writeBytes(this.data.copy());
/*    */     } else {
/* 44 */       debug1.writeBoolean(false);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerLoginPacketListener debug1) {
/* 50 */     debug1.handleCustomQueryPacket(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\login\ServerboundCustomQueryPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */