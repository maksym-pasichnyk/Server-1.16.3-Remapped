/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerboundMovePlayerPacket
/*     */   implements Packet<ServerGamePacketListener>
/*     */ {
/*     */   protected double x;
/*     */   protected double y;
/*     */   protected double z;
/*     */   protected float yRot;
/*     */   protected float xRot;
/*     */   protected boolean onGround;
/*     */   protected boolean hasPos;
/*     */   protected boolean hasRot;
/*     */   
/*     */   public static class PosRot
/*     */     extends ServerboundMovePlayerPacket
/*     */   {
/*     */     public void read(FriendlyByteBuf debug1) throws IOException {
/*  37 */       this.x = debug1.readDouble();
/*  38 */       this.y = debug1.readDouble();
/*  39 */       this.z = debug1.readDouble();
/*  40 */       this.yRot = debug1.readFloat();
/*  41 */       this.xRot = debug1.readFloat();
/*  42 */       super.read(debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(FriendlyByteBuf debug1) throws IOException {
/*  47 */       debug1.writeDouble(this.x);
/*  48 */       debug1.writeDouble(this.y);
/*  49 */       debug1.writeDouble(this.z);
/*  50 */       debug1.writeFloat(this.yRot);
/*  51 */       debug1.writeFloat(this.xRot);
/*  52 */       super.write(debug1);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Pos
/*     */     extends ServerboundMovePlayerPacket
/*     */   {
/*     */     public void read(FriendlyByteBuf debug1) throws IOException {
/*  71 */       this.x = debug1.readDouble();
/*  72 */       this.y = debug1.readDouble();
/*  73 */       this.z = debug1.readDouble();
/*  74 */       super.read(debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(FriendlyByteBuf debug1) throws IOException {
/*  79 */       debug1.writeDouble(this.x);
/*  80 */       debug1.writeDouble(this.y);
/*  81 */       debug1.writeDouble(this.z);
/*  82 */       super.write(debug1);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Rot
/*     */     extends ServerboundMovePlayerPacket
/*     */   {
/*     */     public void read(FriendlyByteBuf debug1) throws IOException {
/* 100 */       this.yRot = debug1.readFloat();
/* 101 */       this.xRot = debug1.readFloat();
/* 102 */       super.read(debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(FriendlyByteBuf debug1) throws IOException {
/* 107 */       debug1.writeFloat(this.yRot);
/* 108 */       debug1.writeFloat(this.xRot);
/* 109 */       super.write(debug1);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handle(ServerGamePacketListener debug1) {
/* 122 */     debug1.handleMovePlayer(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 127 */     this.onGround = (debug1.readUnsignedByte() != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 132 */     debug1.writeByte(this.onGround ? 1 : 0);
/*     */   }
/*     */   
/*     */   public double getX(double debug1) {
/* 136 */     return this.hasPos ? this.x : debug1;
/*     */   }
/*     */   
/*     */   public double getY(double debug1) {
/* 140 */     return this.hasPos ? this.y : debug1;
/*     */   }
/*     */   
/*     */   public double getZ(double debug1) {
/* 144 */     return this.hasPos ? this.z : debug1;
/*     */   }
/*     */   
/*     */   public float getYRot(float debug1) {
/* 148 */     return this.hasRot ? this.yRot : debug1;
/*     */   }
/*     */   
/*     */   public float getXRot(float debug1) {
/* 152 */     return this.hasRot ? this.xRot : debug1;
/*     */   }
/*     */   
/*     */   public boolean isOnGround() {
/* 156 */     return this.onGround;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundMovePlayerPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */