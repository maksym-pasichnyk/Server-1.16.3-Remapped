/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ 
/*     */ public class ClientboundSetBorderPacket
/*     */   implements Packet<ClientGamePacketListener> {
/*     */   private Type type;
/*     */   private int newAbsoluteMaxSize;
/*     */   private double newCenterX;
/*     */   private double newCenterZ;
/*     */   private double newSize;
/*     */   private double oldSize;
/*     */   private long lerpTime;
/*     */   private int warningTime;
/*     */   private int warningBlocks;
/*     */   
/*     */   public ClientboundSetBorderPacket() {}
/*     */   
/*     */   public ClientboundSetBorderPacket(WorldBorder debug1, Type debug2) {
/*  24 */     this.type = debug2;
/*  25 */     this.newCenterX = debug1.getCenterX();
/*  26 */     this.newCenterZ = debug1.getCenterZ();
/*  27 */     this.oldSize = debug1.getSize();
/*  28 */     this.newSize = debug1.getLerpTarget();
/*  29 */     this.lerpTime = debug1.getLerpRemainingTime();
/*  30 */     this.newAbsoluteMaxSize = debug1.getAbsoluteMaxSize();
/*  31 */     this.warningBlocks = debug1.getWarningBlocks();
/*  32 */     this.warningTime = debug1.getWarningTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(FriendlyByteBuf debug1) throws IOException {
/*  37 */     this.type = (Type)debug1.readEnum(Type.class);
/*     */     
/*  39 */     switch (this.type) {
/*     */       case SET_SIZE:
/*  41 */         this.newSize = debug1.readDouble();
/*     */         break;
/*     */       case LERP_SIZE:
/*  44 */         this.oldSize = debug1.readDouble();
/*  45 */         this.newSize = debug1.readDouble();
/*  46 */         this.lerpTime = debug1.readVarLong();
/*     */         break;
/*     */       case SET_CENTER:
/*  49 */         this.newCenterX = debug1.readDouble();
/*  50 */         this.newCenterZ = debug1.readDouble();
/*     */         break;
/*     */       case SET_WARNING_BLOCKS:
/*  53 */         this.warningBlocks = debug1.readVarInt();
/*     */         break;
/*     */       case SET_WARNING_TIME:
/*  56 */         this.warningTime = debug1.readVarInt();
/*     */         break;
/*     */       case INITIALIZE:
/*  59 */         this.newCenterX = debug1.readDouble();
/*  60 */         this.newCenterZ = debug1.readDouble();
/*  61 */         this.oldSize = debug1.readDouble();
/*  62 */         this.newSize = debug1.readDouble();
/*  63 */         this.lerpTime = debug1.readVarLong();
/*  64 */         this.newAbsoluteMaxSize = debug1.readVarInt();
/*  65 */         this.warningBlocks = debug1.readVarInt();
/*  66 */         this.warningTime = debug1.readVarInt();
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf debug1) throws IOException {
/*  73 */     debug1.writeEnum(this.type);
/*     */     
/*  75 */     switch (this.type) {
/*     */       case SET_SIZE:
/*  77 */         debug1.writeDouble(this.newSize);
/*     */         break;
/*     */       case LERP_SIZE:
/*  80 */         debug1.writeDouble(this.oldSize);
/*  81 */         debug1.writeDouble(this.newSize);
/*  82 */         debug1.writeVarLong(this.lerpTime);
/*     */         break;
/*     */       case SET_CENTER:
/*  85 */         debug1.writeDouble(this.newCenterX);
/*  86 */         debug1.writeDouble(this.newCenterZ);
/*     */         break;
/*     */       case SET_WARNING_TIME:
/*  89 */         debug1.writeVarInt(this.warningTime);
/*     */         break;
/*     */       case SET_WARNING_BLOCKS:
/*  92 */         debug1.writeVarInt(this.warningBlocks);
/*     */         break;
/*     */       case INITIALIZE:
/*  95 */         debug1.writeDouble(this.newCenterX);
/*  96 */         debug1.writeDouble(this.newCenterZ);
/*  97 */         debug1.writeDouble(this.oldSize);
/*  98 */         debug1.writeDouble(this.newSize);
/*  99 */         debug1.writeVarLong(this.lerpTime);
/* 100 */         debug1.writeVarInt(this.newAbsoluteMaxSize);
/* 101 */         debug1.writeVarInt(this.warningBlocks);
/* 102 */         debug1.writeVarInt(this.warningTime);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(ClientGamePacketListener debug1) {
/* 109 */     debug1.handleSetBorder(this);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Type
/*     */   {
/* 146 */     SET_SIZE,
/* 147 */     LERP_SIZE,
/* 148 */     SET_CENTER,
/* 149 */     INITIALIZE,
/* 150 */     SET_WARNING_TIME,
/* 151 */     SET_WARNING_BLOCKS;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetBorderPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */