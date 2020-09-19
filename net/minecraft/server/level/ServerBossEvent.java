/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Set;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.BossEvent;
/*     */ 
/*     */ public class ServerBossEvent extends BossEvent {
/*  16 */   private final Set<ServerPlayer> players = Sets.newHashSet();
/*  17 */   private final Set<ServerPlayer> unmodifiablePlayers = Collections.unmodifiableSet(this.players);
/*     */   private boolean visible = true;
/*     */   
/*     */   public ServerBossEvent(Component debug1, BossEvent.BossBarColor debug2, BossEvent.BossBarOverlay debug3) {
/*  21 */     super(Mth.createInsecureUUID(), debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPercent(float debug1) {
/*  26 */     if (debug1 != this.percent) {
/*  27 */       super.setPercent(debug1);
/*  28 */       broadcast(ClientboundBossEventPacket.Operation.UPDATE_PCT);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setColor(BossEvent.BossBarColor debug1) {
/*  34 */     if (debug1 != this.color) {
/*  35 */       super.setColor(debug1);
/*  36 */       broadcast(ClientboundBossEventPacket.Operation.UPDATE_STYLE);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOverlay(BossEvent.BossBarOverlay debug1) {
/*  42 */     if (debug1 != this.overlay) {
/*  43 */       super.setOverlay(debug1);
/*  44 */       broadcast(ClientboundBossEventPacket.Operation.UPDATE_STYLE);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BossEvent setDarkenScreen(boolean debug1) {
/*  50 */     if (debug1 != this.darkenScreen) {
/*  51 */       super.setDarkenScreen(debug1);
/*  52 */       broadcast(ClientboundBossEventPacket.Operation.UPDATE_PROPERTIES);
/*     */     } 
/*  54 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BossEvent setPlayBossMusic(boolean debug1) {
/*  59 */     if (debug1 != this.playBossMusic) {
/*  60 */       super.setPlayBossMusic(debug1);
/*  61 */       broadcast(ClientboundBossEventPacket.Operation.UPDATE_PROPERTIES);
/*     */     } 
/*  63 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BossEvent setCreateWorldFog(boolean debug1) {
/*  68 */     if (debug1 != this.createWorldFog) {
/*  69 */       super.setCreateWorldFog(debug1);
/*  70 */       broadcast(ClientboundBossEventPacket.Operation.UPDATE_PROPERTIES);
/*     */     } 
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setName(Component debug1) {
/*  77 */     if (!Objects.equal(debug1, this.name)) {
/*  78 */       super.setName(debug1);
/*  79 */       broadcast(ClientboundBossEventPacket.Operation.UPDATE_NAME);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void broadcast(ClientboundBossEventPacket.Operation debug1) {
/*  84 */     if (this.visible) {
/*  85 */       ClientboundBossEventPacket debug2 = new ClientboundBossEventPacket(debug1, this);
/*  86 */       for (ServerPlayer debug4 : this.players) {
/*  87 */         debug4.connection.send((Packet)debug2);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addPlayer(ServerPlayer debug1) {
/*  93 */     if (this.players.add(debug1) && this.visible) {
/*  94 */       debug1.connection.send((Packet)new ClientboundBossEventPacket(ClientboundBossEventPacket.Operation.ADD, this));
/*     */     }
/*     */   }
/*     */   
/*     */   public void removePlayer(ServerPlayer debug1) {
/*  99 */     if (this.players.remove(debug1) && this.visible) {
/* 100 */       debug1.connection.send((Packet)new ClientboundBossEventPacket(ClientboundBossEventPacket.Operation.REMOVE, this));
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeAllPlayers() {
/* 105 */     if (!this.players.isEmpty()) {
/* 106 */       for (ServerPlayer debug2 : Lists.newArrayList(this.players)) {
/* 107 */         removePlayer(debug2);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isVisible() {
/* 113 */     return this.visible;
/*     */   }
/*     */   
/*     */   public void setVisible(boolean debug1) {
/* 117 */     if (debug1 != this.visible) {
/* 118 */       this.visible = debug1;
/*     */       
/* 120 */       for (ServerPlayer debug3 : this.players) {
/* 121 */         debug3.connection.send((Packet)new ClientboundBossEventPacket(debug1 ? ClientboundBossEventPacket.Operation.ADD : ClientboundBossEventPacket.Operation.REMOVE, this));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public Collection<ServerPlayer> getPlayers() {
/* 127 */     return this.unmodifiablePlayers;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\ServerBossEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */