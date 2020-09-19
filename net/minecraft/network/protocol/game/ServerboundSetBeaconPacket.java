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
/*    */ 
/*    */ public class ServerboundSetBeaconPacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private int primary;
/*    */   private int secondary;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 22 */     this.primary = debug1.readVarInt();
/* 23 */     this.secondary = debug1.readVarInt();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 28 */     debug1.writeVarInt(this.primary);
/* 29 */     debug1.writeVarInt(this.secondary);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 34 */     debug1.handleSetBeaconPacket(this);
/*    */   }
/*    */   
/*    */   public int getPrimary() {
/* 38 */     return this.primary;
/*    */   }
/*    */   
/*    */   public int getSecondary() {
/* 42 */     return this.secondary;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundSetBeaconPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */