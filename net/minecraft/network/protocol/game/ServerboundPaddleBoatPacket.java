/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundPaddleBoatPacket
/*    */   implements Packet<ServerGamePacketListener> {
/*    */   private boolean left;
/*    */   private boolean right;
/*    */   
/*    */   public ServerboundPaddleBoatPacket() {}
/*    */   
/*    */   public ServerboundPaddleBoatPacket(boolean debug1, boolean debug2) {
/* 16 */     this.left = debug1;
/* 17 */     this.right = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 22 */     this.left = debug1.readBoolean();
/* 23 */     this.right = debug1.readBoolean();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 28 */     debug1.writeBoolean(this.left);
/* 29 */     debug1.writeBoolean(this.right);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 34 */     debug1.handlePaddleBoat(this);
/*    */   }
/*    */   
/*    */   public boolean getLeft() {
/* 38 */     return this.left;
/*    */   }
/*    */   
/*    */   public boolean getRight() {
/* 42 */     return this.right;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundPaddleBoatPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */