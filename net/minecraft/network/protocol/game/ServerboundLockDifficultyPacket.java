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
/*    */ public class ServerboundLockDifficultyPacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private boolean locked;
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 20 */     debug1.handleLockDifficulty(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 25 */     this.locked = debug1.readBoolean();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 30 */     debug1.writeBoolean(this.locked);
/*    */   }
/*    */   
/*    */   public boolean isLocked() {
/* 34 */     return this.locked;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundLockDifficultyPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */