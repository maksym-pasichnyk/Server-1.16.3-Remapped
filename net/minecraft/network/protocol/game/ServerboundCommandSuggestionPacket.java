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
/*    */ public class ServerboundCommandSuggestionPacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private int id;
/*    */   private String command;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 23 */     this.id = debug1.readVarInt();
/* 24 */     this.command = debug1.readUtf(32500);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 29 */     debug1.writeVarInt(this.id);
/* 30 */     debug1.writeUtf(this.command, 32500);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 35 */     debug1.handleCustomCommandSuggestions(this);
/*    */   }
/*    */   
/*    */   public int getId() {
/* 39 */     return this.id;
/*    */   }
/*    */   
/*    */   public String getCommand() {
/* 43 */     return this.command;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundCommandSuggestionPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */