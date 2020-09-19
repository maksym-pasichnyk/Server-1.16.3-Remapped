/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class ServerboundMoveVehiclePacket
/*    */   implements Packet<ServerGamePacketListener> {
/*    */   private double x;
/*    */   private double y;
/*    */   private double z;
/*    */   private float yRot;
/*    */   private float xRot;
/*    */   
/*    */   public ServerboundMoveVehiclePacket() {}
/*    */   
/*    */   public ServerboundMoveVehiclePacket(Entity debug1) {
/* 20 */     this.x = debug1.getX();
/* 21 */     this.y = debug1.getY();
/* 22 */     this.z = debug1.getZ();
/* 23 */     this.yRot = debug1.yRot;
/* 24 */     this.xRot = debug1.xRot;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 29 */     this.x = debug1.readDouble();
/* 30 */     this.y = debug1.readDouble();
/* 31 */     this.z = debug1.readDouble();
/* 32 */     this.yRot = debug1.readFloat();
/* 33 */     this.xRot = debug1.readFloat();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 38 */     debug1.writeDouble(this.x);
/* 39 */     debug1.writeDouble(this.y);
/* 40 */     debug1.writeDouble(this.z);
/* 41 */     debug1.writeFloat(this.yRot);
/* 42 */     debug1.writeFloat(this.xRot);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 47 */     debug1.handleMoveVehicle(this);
/*    */   }
/*    */   
/*    */   public double getX() {
/* 51 */     return this.x;
/*    */   }
/*    */   
/*    */   public double getY() {
/* 55 */     return this.y;
/*    */   }
/*    */   
/*    */   public double getZ() {
/* 59 */     return this.z;
/*    */   }
/*    */   
/*    */   public float getYRot() {
/* 63 */     return this.yRot;
/*    */   }
/*    */   
/*    */   public float getXRot() {
/* 67 */     return this.xRot;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundMoveVehiclePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */