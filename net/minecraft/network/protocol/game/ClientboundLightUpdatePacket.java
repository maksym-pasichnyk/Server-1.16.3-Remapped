/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ 
/*     */ public class ClientboundLightUpdatePacket
/*     */   implements Packet<ClientGamePacketListener>
/*     */ {
/*     */   private int x;
/*     */   private int z;
/*     */   private int skyYMask;
/*     */   private int blockYMask;
/*     */   private int emptySkyYMask;
/*     */   private int emptyBlockYMask;
/*     */   private List<byte[]> skyUpdates;
/*     */   private List<byte[]> blockUpdates;
/*     */   private boolean trustEdges;
/*     */   
/*     */   public ClientboundLightUpdatePacket() {}
/*     */   
/*     */   public ClientboundLightUpdatePacket(ChunkPos debug1, LevelLightEngine debug2, boolean debug3) {
/*  31 */     this.x = debug1.x;
/*  32 */     this.z = debug1.z;
/*  33 */     this.trustEdges = debug3;
/*  34 */     this.skyUpdates = Lists.newArrayList();
/*  35 */     this.blockUpdates = Lists.newArrayList();
/*  36 */     for (int debug4 = 0; debug4 < 18; debug4++) {
/*  37 */       DataLayer debug5 = debug2.getLayerListener(LightLayer.SKY).getDataLayerData(SectionPos.of(debug1, -1 + debug4));
/*  38 */       DataLayer debug6 = debug2.getLayerListener(LightLayer.BLOCK).getDataLayerData(SectionPos.of(debug1, -1 + debug4));
/*  39 */       if (debug5 != null) {
/*  40 */         if (debug5.isEmpty()) {
/*  41 */           this.emptySkyYMask |= 1 << debug4;
/*     */         } else {
/*  43 */           this.skyYMask |= 1 << debug4;
/*  44 */           this.skyUpdates.add(debug5.getData().clone());
/*     */         } 
/*     */       }
/*  47 */       if (debug6 != null) {
/*  48 */         if (debug6.isEmpty()) {
/*  49 */           this.emptyBlockYMask |= 1 << debug4;
/*     */         } else {
/*  51 */           this.blockYMask |= 1 << debug4;
/*  52 */           this.blockUpdates.add(debug6.getData().clone());
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public ClientboundLightUpdatePacket(ChunkPos debug1, LevelLightEngine debug2, int debug3, int debug4, boolean debug5) {
/*  59 */     this.x = debug1.x;
/*  60 */     this.z = debug1.z;
/*  61 */     this.trustEdges = debug5;
/*  62 */     this.skyYMask = debug3;
/*  63 */     this.blockYMask = debug4;
/*  64 */     this.skyUpdates = Lists.newArrayList();
/*  65 */     this.blockUpdates = Lists.newArrayList();
/*  66 */     for (int debug6 = 0; debug6 < 18; debug6++) {
/*  67 */       if ((this.skyYMask & 1 << debug6) != 0) {
/*  68 */         DataLayer debug7 = debug2.getLayerListener(LightLayer.SKY).getDataLayerData(SectionPos.of(debug1, -1 + debug6));
/*  69 */         if (debug7 == null || debug7.isEmpty()) {
/*  70 */           this.skyYMask &= 1 << debug6 ^ 0xFFFFFFFF;
/*  71 */           if (debug7 != null) {
/*  72 */             this.emptySkyYMask |= 1 << debug6;
/*     */           }
/*     */         } else {
/*  75 */           this.skyUpdates.add(debug7.getData().clone());
/*     */         } 
/*     */       } 
/*  78 */       if ((this.blockYMask & 1 << debug6) != 0) {
/*  79 */         DataLayer debug7 = debug2.getLayerListener(LightLayer.BLOCK).getDataLayerData(SectionPos.of(debug1, -1 + debug6));
/*  80 */         if (debug7 == null || debug7.isEmpty()) {
/*  81 */           this.blockYMask &= 1 << debug6 ^ 0xFFFFFFFF;
/*  82 */           if (debug7 != null) {
/*  83 */             this.emptyBlockYMask |= 1 << debug6;
/*     */           }
/*     */         } else {
/*  86 */           this.blockUpdates.add(debug7.getData().clone());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(FriendlyByteBuf debug1) throws IOException {
/*  94 */     this.x = debug1.readVarInt();
/*  95 */     this.z = debug1.readVarInt();
/*  96 */     this.trustEdges = debug1.readBoolean();
/*  97 */     this.skyYMask = debug1.readVarInt();
/*  98 */     this.blockYMask = debug1.readVarInt();
/*  99 */     this.emptySkyYMask = debug1.readVarInt();
/* 100 */     this.emptyBlockYMask = debug1.readVarInt();
/* 101 */     this.skyUpdates = Lists.newArrayList(); int debug2;
/* 102 */     for (debug2 = 0; debug2 < 18; debug2++) {
/* 103 */       if ((this.skyYMask & 1 << debug2) != 0) {
/* 104 */         this.skyUpdates.add(debug1.readByteArray(2048));
/*     */       }
/*     */     } 
/* 107 */     this.blockUpdates = Lists.newArrayList();
/* 108 */     for (debug2 = 0; debug2 < 18; debug2++) {
/* 109 */       if ((this.blockYMask & 1 << debug2) != 0) {
/* 110 */         this.blockUpdates.add(debug1.readByteArray(2048));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 117 */     debug1.writeVarInt(this.x);
/* 118 */     debug1.writeVarInt(this.z);
/* 119 */     debug1.writeBoolean(this.trustEdges);
/* 120 */     debug1.writeVarInt(this.skyYMask);
/* 121 */     debug1.writeVarInt(this.blockYMask);
/* 122 */     debug1.writeVarInt(this.emptySkyYMask);
/* 123 */     debug1.writeVarInt(this.emptyBlockYMask);
/* 124 */     for (byte[] debug3 : this.skyUpdates) {
/* 125 */       debug1.writeByteArray(debug3);
/*     */     }
/* 127 */     for (byte[] debug3 : this.blockUpdates) {
/* 128 */       debug1.writeByteArray(debug3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(ClientGamePacketListener debug1) {
/* 134 */     debug1.handleLightUpdatePacked(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundLightUpdatePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */