/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.ChatType;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundChatPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private Component message;
/*    */   private ChatType type;
/*    */   private UUID sender;
/*    */   
/*    */   public ClientboundChatPacket() {}
/*    */   
/*    */   public ClientboundChatPacket(Component debug1, ChatType debug2, UUID debug3) {
/* 20 */     this.message = debug1;
/* 21 */     this.type = debug2;
/* 22 */     this.sender = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 27 */     this.message = debug1.readComponent();
/* 28 */     this.type = ChatType.getForIndex(debug1.readByte());
/* 29 */     this.sender = debug1.readUUID();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 34 */     debug1.writeComponent(this.message);
/* 35 */     debug1.writeByte(this.type.getIndex());
/* 36 */     debug1.writeUUID(this.sender);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 41 */     debug1.handleChat(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSystem() {
/* 49 */     return (this.type == ChatType.SYSTEM || this.type == ChatType.GAME_INFO);
/*    */   }
/*    */   
/*    */   public ChatType getType() {
/* 53 */     return this.type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSkippable() {
/* 62 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundChatPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */