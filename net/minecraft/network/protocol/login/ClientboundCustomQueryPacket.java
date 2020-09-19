/*    */ package net.minecraft.network.protocol.login;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.resources.ResourceLocation;
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
/*    */ public class ClientboundCustomQueryPacket
/*    */   implements Packet<ClientLoginPacketListener>
/*    */ {
/*    */   private int transactionId;
/*    */   private ResourceLocation identifier;
/*    */   private FriendlyByteBuf data;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 27 */     this.transactionId = debug1.readVarInt();
/* 28 */     this.identifier = debug1.readResourceLocation();
/* 29 */     int debug2 = debug1.readableBytes();
/* 30 */     if (debug2 < 0 || debug2 > 1048576) {
/* 31 */       throw new IOException("Payload may not be larger than 1048576 bytes");
/*    */     }
/* 33 */     this.data = new FriendlyByteBuf(debug1.readBytes(debug2));
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 38 */     debug1.writeVarInt(this.transactionId);
/* 39 */     debug1.writeResourceLocation(this.identifier);
/* 40 */     debug1.writeBytes(this.data.copy());
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientLoginPacketListener debug1) {
/* 45 */     debug1.handleCustomQuery(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\login\ClientboundCustomQueryPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */