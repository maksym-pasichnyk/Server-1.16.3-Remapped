/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClientboundTabListPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private Component header;
/*    */   private Component footer;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 23 */     this.header = debug1.readComponent();
/* 24 */     this.footer = debug1.readComponent();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 29 */     debug1.writeComponent(this.header);
/* 30 */     debug1.writeComponent(this.footer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 35 */     debug1.handleTabListCustomisation(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundTabListPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */