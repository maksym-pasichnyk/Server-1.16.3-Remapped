/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClientboundAnimatePacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private int id;
/*    */   private int action;
/*    */   
/*    */   public ClientboundAnimatePacket() {}
/*    */   
/*    */   public ClientboundAnimatePacket(Entity debug1, int debug2) {
/* 25 */     this.id = debug1.getId();
/* 26 */     this.action = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 31 */     this.id = debug1.readVarInt();
/* 32 */     this.action = debug1.readUnsignedByte();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 37 */     debug1.writeVarInt(this.id);
/* 38 */     debug1.writeByte(this.action);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 43 */     debug1.handleAnimate(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundAnimatePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */