/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundClientCommandPacket
/*    */   implements Packet<ServerGamePacketListener> {
/*    */   private Action action;
/*    */   
/*    */   public ServerboundClientCommandPacket() {}
/*    */   
/*    */   public ServerboundClientCommandPacket(Action debug1) {
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
/* 30 */     debug1.handleClientCommand(this);
/*    */   }
/*    */   
/*    */   public Action getAction() {
/* 34 */     return this.action;
/*    */   }
/*    */   
/*    */   public enum Action {
/* 38 */     PERFORM_RESPAWN,
/* 39 */     REQUEST_STATS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundClientCommandPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */