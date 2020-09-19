/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundSetHealthPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private float health;
/*    */   private int food;
/*    */   private float saturation;
/*    */   
/*    */   public ClientboundSetHealthPacket() {}
/*    */   
/*    */   public ClientboundSetHealthPacket(float debug1, int debug2, float debug3) {
/* 17 */     this.health = debug1;
/* 18 */     this.food = debug2;
/* 19 */     this.saturation = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 24 */     this.health = debug1.readFloat();
/* 25 */     this.food = debug1.readVarInt();
/* 26 */     this.saturation = debug1.readFloat();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 31 */     debug1.writeFloat(this.health);
/* 32 */     debug1.writeVarInt(this.food);
/* 33 */     debug1.writeFloat(this.saturation);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 38 */     debug1.handleSetHealth(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetHealthPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */