/*    */ package net.minecraft.network.protocol.status;
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
/*    */ public class ServerboundPingRequestPacket
/*    */   implements Packet<ServerStatusPacketListener>
/*    */ {
/*    */   private long time;
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
/*    */   public void handle(ServerStatusPacketListener debug1) {
/* 30 */     debug1.handlePingRequest(this);
/*    */   }
/*    */   
/*    */   public long getTime() {
/* 34 */     return this.time;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\status\ServerboundPingRequestPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */