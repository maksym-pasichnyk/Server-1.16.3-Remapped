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
/*    */ public class ServerboundStatusRequestPacket
/*    */   implements Packet<ServerStatusPacketListener>
/*    */ {
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {}
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {}
/*    */   
/*    */   public void handle(ServerStatusPacketListener debug1) {
/* 22 */     debug1.handleStatusRequest(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\status\ServerboundStatusRequestPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */