/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ClientboundSetEntityMotionPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private int id;
/*    */   private int xa;
/*    */   private int ya;
/*    */   private int za;
/*    */   
/*    */   public ClientboundSetEntityMotionPacket() {}
/*    */   
/*    */   public ClientboundSetEntityMotionPacket(Entity debug1) {
/* 21 */     this(debug1.getId(), debug1.getDeltaMovement());
/*    */   }
/*    */   
/*    */   public ClientboundSetEntityMotionPacket(int debug1, Vec3 debug2) {
/* 25 */     this.id = debug1;
/* 26 */     double debug3 = 3.9D;
/* 27 */     double debug5 = Mth.clamp(debug2.x, -3.9D, 3.9D);
/* 28 */     double debug7 = Mth.clamp(debug2.y, -3.9D, 3.9D);
/* 29 */     double debug9 = Mth.clamp(debug2.z, -3.9D, 3.9D);
/* 30 */     this.xa = (int)(debug5 * 8000.0D);
/* 31 */     this.ya = (int)(debug7 * 8000.0D);
/* 32 */     this.za = (int)(debug9 * 8000.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 37 */     this.id = debug1.readVarInt();
/* 38 */     this.xa = debug1.readShort();
/* 39 */     this.ya = debug1.readShort();
/* 40 */     this.za = debug1.readShort();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 45 */     debug1.writeVarInt(this.id);
/* 46 */     debug1.writeShort(this.xa);
/* 47 */     debug1.writeShort(this.ya);
/* 48 */     debug1.writeShort(this.za);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 53 */     debug1.handleSetEntityMotion(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetEntityMotionPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */