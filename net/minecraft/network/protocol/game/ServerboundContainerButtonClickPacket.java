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
/*    */ public class ServerboundContainerButtonClickPacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private int containerId;
/*    */   private int buttonId;
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 22 */     debug1.handleContainerButtonClick(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 27 */     this.containerId = debug1.readByte();
/* 28 */     this.buttonId = debug1.readByte();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 33 */     debug1.writeByte(this.containerId);
/* 34 */     debug1.writeByte(this.buttonId);
/*    */   }
/*    */   
/*    */   public int getContainerId() {
/* 38 */     return this.containerId;
/*    */   }
/*    */   
/*    */   public int getButtonId() {
/* 42 */     return this.buttonId;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundContainerButtonClickPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */