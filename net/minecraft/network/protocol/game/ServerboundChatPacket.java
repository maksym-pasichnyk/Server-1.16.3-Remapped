/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ 
/*    */ public class ServerboundChatPacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private String message;
/*    */   
/*    */   public ServerboundChatPacket() {}
/*    */   
/*    */   public ServerboundChatPacket(String debug1) {
/* 17 */     if (debug1.length() > 256) {
/* 18 */       debug1 = debug1.substring(0, 256);
/*    */     }
/*    */     
/* 21 */     this.message = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 26 */     this.message = debug1.readUtf(256);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 31 */     debug1.writeUtf(this.message);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 36 */     debug1.handleChat(this);
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 40 */     return this.message;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundChatPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */