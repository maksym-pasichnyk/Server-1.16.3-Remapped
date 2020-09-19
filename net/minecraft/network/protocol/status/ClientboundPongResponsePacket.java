/*    */ package net.minecraft.network.protocol.status;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundPongResponsePacket
/*    */   implements Packet<ClientStatusPacketListener> {
/*    */   private long time;
/*    */   
/*    */   public ClientboundPongResponsePacket() {}
/*    */   
/*    */   public ClientboundPongResponsePacket(long debug1) {
/* 15 */     this.time = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 20 */     this.time = debug1.readLong();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 25 */     debug1.writeLong(this.time);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientStatusPacketListener debug1) {
/* 30 */     debug1.handlePongResponse(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\status\ClientboundPongResponsePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */