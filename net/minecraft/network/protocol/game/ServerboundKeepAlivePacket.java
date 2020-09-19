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
/*    */ public class ServerboundKeepAlivePacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private long id;
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 20 */     debug1.handleKeepAlive(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 25 */     this.id = debug1.readLong();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 30 */     debug1.writeLong(this.id);
/*    */   }
/*    */   
/*    */   public long getId() {
/* 34 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundKeepAlivePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */