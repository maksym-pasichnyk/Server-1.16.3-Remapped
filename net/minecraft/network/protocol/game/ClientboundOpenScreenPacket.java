/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.inventory.MenuType;
/*    */ 
/*    */ public class ClientboundOpenScreenPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private int containerId;
/*    */   private int type;
/*    */   private Component title;
/*    */   
/*    */   public ClientboundOpenScreenPacket() {}
/*    */   
/*    */   public ClientboundOpenScreenPacket(int debug1, MenuType<?> debug2, Component debug3) {
/* 21 */     this.containerId = debug1;
/* 22 */     this.type = Registry.MENU.getId(debug2);
/* 23 */     this.title = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 28 */     this.containerId = debug1.readVarInt();
/* 29 */     this.type = debug1.readVarInt();
/* 30 */     this.title = debug1.readComponent();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 35 */     debug1.writeVarInt(this.containerId);
/* 36 */     debug1.writeVarInt(this.type);
/* 37 */     debug1.writeComponent(this.title);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 42 */     debug1.handleOpenScreen(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundOpenScreenPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */