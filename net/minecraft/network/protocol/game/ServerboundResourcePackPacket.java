/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundResourcePackPacket
/*    */   implements Packet<ServerGamePacketListener> {
/*    */   private Action action;
/*    */   
/*    */   public ServerboundResourcePackPacket() {}
/*    */   
/*    */   public ServerboundResourcePackPacket(Action debug1) {
/* 15 */     this.action = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 20 */     this.action = (Action)debug1.readEnum(Action.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 25 */     debug1.writeEnum(this.action);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 30 */     debug1.handleResourcePackResponse(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public enum Action
/*    */   {
/* 38 */     SUCCESSFULLY_LOADED,
/* 39 */     DECLINED,
/* 40 */     FAILED_DOWNLOAD,
/* 41 */     ACCEPTED;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundResourcePackPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */