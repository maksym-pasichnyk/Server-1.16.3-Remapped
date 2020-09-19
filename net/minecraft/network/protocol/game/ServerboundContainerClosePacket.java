/*    */ package net.minecraft.network.protocol.game;
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
/*    */ public class ServerboundContainerClosePacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private int containerId;
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 20 */     debug1.handleContainerClose(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 25 */     this.containerId = debug1.readByte();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 30 */     debug1.writeByte(this.containerId);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundContainerClosePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */