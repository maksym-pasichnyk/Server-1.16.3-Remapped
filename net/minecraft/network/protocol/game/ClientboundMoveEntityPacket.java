/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClientboundMoveEntityPacket
/*     */   implements Packet<ClientGamePacketListener>
/*     */ {
/*     */   protected int entityId;
/*     */   protected short xa;
/*     */   protected short ya;
/*     */   protected short za;
/*     */   protected byte yRot;
/*     */   protected byte xRot;
/*     */   protected boolean onGround;
/*     */   protected boolean hasRot;
/*     */   protected boolean hasPos;
/*     */   
/*     */   public static long entityToPacket(double debug0) {
/*  29 */     return Mth.lfloor(debug0 * 4096.0D);
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
/*     */   public static Vec3 packetToEntity(long debug0, long debug2, long debug4) {
/*  44 */     return (new Vec3(debug0, debug2, debug4)).scale(2.44140625E-4D);
/*     */   }
/*     */   
/*     */   public static class PosRot extends ClientboundMoveEntityPacket {
/*     */     public PosRot() {
/*  49 */       this.hasRot = true;
/*  50 */       this.hasPos = true;
/*     */     }
/*     */     
/*     */     public PosRot(int debug1, short debug2, short debug3, short debug4, byte debug5, byte debug6, boolean debug7) {
/*  54 */       super(debug1);
/*     */       
/*  56 */       this.xa = debug2;
/*  57 */       this.ya = debug3;
/*  58 */       this.za = debug4;
/*  59 */       this.yRot = debug5;
/*  60 */       this.xRot = debug6;
/*  61 */       this.onGround = debug7;
/*  62 */       this.hasRot = true;
/*  63 */       this.hasPos = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void read(FriendlyByteBuf debug1) throws IOException {
/*  68 */       super.read(debug1);
/*  69 */       this.xa = debug1.readShort();
/*  70 */       this.ya = debug1.readShort();
/*  71 */       this.za = debug1.readShort();
/*  72 */       this.yRot = debug1.readByte();
/*  73 */       this.xRot = debug1.readByte();
/*  74 */       this.onGround = debug1.readBoolean();
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(FriendlyByteBuf debug1) throws IOException {
/*  79 */       super.write(debug1);
/*  80 */       debug1.writeShort(this.xa);
/*  81 */       debug1.writeShort(this.ya);
/*  82 */       debug1.writeShort(this.za);
/*  83 */       debug1.writeByte(this.yRot);
/*  84 */       debug1.writeByte(this.xRot);
/*  85 */       debug1.writeBoolean(this.onGround);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Pos extends ClientboundMoveEntityPacket {
/*     */     public Pos() {
/*  91 */       this.hasPos = true;
/*     */     }
/*     */     
/*     */     public Pos(int debug1, short debug2, short debug3, short debug4, boolean debug5) {
/*  95 */       super(debug1);
/*     */       
/*  97 */       this.xa = debug2;
/*  98 */       this.ya = debug3;
/*  99 */       this.za = debug4;
/* 100 */       this.onGround = debug5;
/* 101 */       this.hasPos = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void read(FriendlyByteBuf debug1) throws IOException {
/* 106 */       super.read(debug1);
/* 107 */       this.xa = debug1.readShort();
/* 108 */       this.ya = debug1.readShort();
/* 109 */       this.za = debug1.readShort();
/* 110 */       this.onGround = debug1.readBoolean();
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(FriendlyByteBuf debug1) throws IOException {
/* 115 */       super.write(debug1);
/* 116 */       debug1.writeShort(this.xa);
/* 117 */       debug1.writeShort(this.ya);
/* 118 */       debug1.writeShort(this.za);
/* 119 */       debug1.writeBoolean(this.onGround);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Rot extends ClientboundMoveEntityPacket {
/*     */     public Rot() {
/* 125 */       this.hasRot = true;
/*     */     }
/*     */     
/*     */     public Rot(int debug1, byte debug2, byte debug3, boolean debug4) {
/* 129 */       super(debug1);
/* 130 */       this.yRot = debug2;
/* 131 */       this.xRot = debug3;
/* 132 */       this.hasRot = true;
/* 133 */       this.onGround = debug4;
/*     */     }
/*     */ 
/*     */     
/*     */     public void read(FriendlyByteBuf debug1) throws IOException {
/* 138 */       super.read(debug1);
/* 139 */       this.yRot = debug1.readByte();
/* 140 */       this.xRot = debug1.readByte();
/* 141 */       this.onGround = debug1.readBoolean();
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(FriendlyByteBuf debug1) throws IOException {
/* 146 */       super.write(debug1);
/* 147 */       debug1.writeByte(this.yRot);
/* 148 */       debug1.writeByte(this.xRot);
/* 149 */       debug1.writeBoolean(this.onGround);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientboundMoveEntityPacket() {}
/*     */   
/*     */   public ClientboundMoveEntityPacket(int debug1) {
/* 157 */     this.entityId = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 162 */     this.entityId = debug1.readVarInt();
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 167 */     debug1.writeVarInt(this.entityId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(ClientGamePacketListener debug1) {
/* 172 */     debug1.handleMoveEntity(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 177 */     return "Entity_" + super.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundMoveEntityPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */