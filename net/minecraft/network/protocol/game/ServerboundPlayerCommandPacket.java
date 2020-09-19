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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServerboundPlayerCommandPacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private int id;
/*    */   private Action action;
/*    */   private int data;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 29 */     this.id = debug1.readVarInt();
/* 30 */     this.action = (Action)debug1.readEnum(Action.class);
/* 31 */     this.data = debug1.readVarInt();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 36 */     debug1.writeVarInt(this.id);
/* 37 */     debug1.writeEnum(this.action);
/* 38 */     debug1.writeVarInt(this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 43 */     debug1.handlePlayerCommand(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Action getAction() {
/* 51 */     return this.action;
/*    */   }
/*    */   
/*    */   public int getData() {
/* 55 */     return this.data;
/*    */   }
/*    */   
/*    */   public enum Action {
/* 59 */     PRESS_SHIFT_KEY,
/* 60 */     RELEASE_SHIFT_KEY,
/* 61 */     STOP_SLEEPING,
/* 62 */     START_SPRINTING,
/* 63 */     STOP_SPRINTING,
/* 64 */     START_RIDING_JUMP,
/* 65 */     STOP_RIDING_JUMP,
/* 66 */     OPEN_INVENTORY,
/* 67 */     START_FALL_FLYING;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundPlayerCommandPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */