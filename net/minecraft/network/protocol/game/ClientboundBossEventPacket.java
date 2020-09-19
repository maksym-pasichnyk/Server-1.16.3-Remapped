/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.world.BossEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClientboundBossEventPacket
/*     */   implements Packet<ClientGamePacketListener>
/*     */ {
/*     */   private UUID id;
/*     */   private Operation operation;
/*     */   private Component name;
/*     */   private float pct;
/*     */   private BossEvent.BossBarColor color;
/*     */   private BossEvent.BossBarOverlay overlay;
/*     */   private boolean darkenScreen;
/*     */   private boolean playMusic;
/*     */   private boolean createWorldFog;
/*     */   
/*     */   public ClientboundBossEventPacket() {}
/*     */   
/*     */   public ClientboundBossEventPacket(Operation debug1, BossEvent debug2) {
/*  30 */     this.operation = debug1;
/*  31 */     this.id = debug2.getId();
/*  32 */     this.name = debug2.getName();
/*  33 */     this.pct = debug2.getPercent();
/*  34 */     this.color = debug2.getColor();
/*  35 */     this.overlay = debug2.getOverlay();
/*  36 */     this.darkenScreen = debug2.shouldDarkenScreen();
/*  37 */     this.playMusic = debug2.shouldPlayBossMusic();
/*  38 */     this.createWorldFog = debug2.shouldCreateWorldFog();
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(FriendlyByteBuf debug1) throws IOException {
/*  43 */     this.id = debug1.readUUID();
/*  44 */     this.operation = (Operation)debug1.readEnum(Operation.class);
/*     */     
/*  46 */     switch (this.operation) {
/*     */       case ADD:
/*  48 */         this.name = debug1.readComponent();
/*  49 */         this.pct = debug1.readFloat();
/*  50 */         this.color = (BossEvent.BossBarColor)debug1.readEnum(BossEvent.BossBarColor.class);
/*  51 */         this.overlay = (BossEvent.BossBarOverlay)debug1.readEnum(BossEvent.BossBarOverlay.class);
/*  52 */         decodeProperties(debug1.readUnsignedByte());
/*     */         break;
/*     */ 
/*     */       
/*     */       case UPDATE_PCT:
/*  57 */         this.pct = debug1.readFloat();
/*     */         break;
/*     */       case UPDATE_NAME:
/*  60 */         this.name = debug1.readComponent();
/*     */         break;
/*     */       case UPDATE_STYLE:
/*  63 */         this.color = (BossEvent.BossBarColor)debug1.readEnum(BossEvent.BossBarColor.class);
/*  64 */         this.overlay = (BossEvent.BossBarOverlay)debug1.readEnum(BossEvent.BossBarOverlay.class);
/*     */         break;
/*     */       case UPDATE_PROPERTIES:
/*  67 */         decodeProperties(debug1.readUnsignedByte());
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void decodeProperties(int debug1) {
/*  73 */     this.darkenScreen = ((debug1 & 0x1) > 0);
/*  74 */     this.playMusic = ((debug1 & 0x2) > 0);
/*  75 */     this.createWorldFog = ((debug1 & 0x4) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf debug1) throws IOException {
/*  80 */     debug1.writeUUID(this.id);
/*  81 */     debug1.writeEnum(this.operation);
/*     */     
/*  83 */     switch (this.operation) {
/*     */       case ADD:
/*  85 */         debug1.writeComponent(this.name);
/*  86 */         debug1.writeFloat(this.pct);
/*  87 */         debug1.writeEnum((Enum)this.color);
/*  88 */         debug1.writeEnum((Enum)this.overlay);
/*  89 */         debug1.writeByte(encodeProperties());
/*     */         break;
/*     */ 
/*     */       
/*     */       case UPDATE_PCT:
/*  94 */         debug1.writeFloat(this.pct);
/*     */         break;
/*     */       case UPDATE_NAME:
/*  97 */         debug1.writeComponent(this.name);
/*     */         break;
/*     */       case UPDATE_STYLE:
/* 100 */         debug1.writeEnum((Enum)this.color);
/* 101 */         debug1.writeEnum((Enum)this.overlay);
/*     */         break;
/*     */       case UPDATE_PROPERTIES:
/* 104 */         debug1.writeByte(encodeProperties());
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private int encodeProperties() {
/* 110 */     int debug1 = 0;
/* 111 */     if (this.darkenScreen) {
/* 112 */       debug1 |= 0x1;
/*     */     }
/* 114 */     if (this.playMusic) {
/* 115 */       debug1 |= 0x2;
/*     */     }
/* 117 */     if (this.createWorldFog) {
/* 118 */       debug1 |= 0x4;
/*     */     }
/* 120 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(ClientGamePacketListener debug1) {
/* 125 */     debug1.handleBossUpdate(this);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Operation
/*     */   {
/* 165 */     ADD,
/* 166 */     REMOVE,
/* 167 */     UPDATE_PCT,
/* 168 */     UPDATE_NAME,
/* 169 */     UPDATE_STYLE,
/* 170 */     UPDATE_PROPERTIES;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundBossEventPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */