/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.properties.Property;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.level.GameType;
/*     */ 
/*     */ public class ClientboundPlayerInfoPacket
/*     */   implements Packet<ClientGamePacketListener> {
/*     */   private Action action;
/*  20 */   private final List<PlayerUpdate> entries = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientboundPlayerInfoPacket(Action debug1, ServerPlayer... debug2) {
/*  26 */     this.action = debug1;
/*     */     
/*  28 */     for (ServerPlayer debug6 : debug2) {
/*  29 */       this.entries.add(new PlayerUpdate(debug6.getGameProfile(), debug6.latency, debug6.gameMode.getGameModeForPlayer(), debug6.getTabListDisplayName()));
/*     */     }
/*     */   }
/*     */   
/*     */   public ClientboundPlayerInfoPacket(Action debug1, Iterable<ServerPlayer> debug2) {
/*  34 */     this.action = debug1;
/*     */     
/*  36 */     for (ServerPlayer debug4 : debug2) {
/*  37 */       this.entries.add(new PlayerUpdate(debug4.getGameProfile(), debug4.latency, debug4.gameMode.getGameModeForPlayer(), debug4.getTabListDisplayName()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(FriendlyByteBuf debug1) throws IOException {
/*  43 */     this.action = (Action)debug1.readEnum(Action.class);
/*     */     
/*  45 */     int debug2 = debug1.readVarInt();
/*  46 */     for (int debug3 = 0; debug3 < debug2; debug3++) {
/*  47 */       int debug8, debug9; GameProfile debug4 = null;
/*  48 */       int debug5 = 0;
/*  49 */       GameType debug6 = null;
/*  50 */       Component debug7 = null;
/*     */       
/*  52 */       switch (this.action) {
/*     */         case ADD_PLAYER:
/*  54 */           debug4 = new GameProfile(debug1.readUUID(), debug1.readUtf(16));
/*  55 */           debug8 = debug1.readVarInt();
/*  56 */           for (debug9 = 0; debug9 < debug8; debug9++) {
/*  57 */             String debug10 = debug1.readUtf(32767);
/*  58 */             String debug11 = debug1.readUtf(32767);
/*     */             
/*  60 */             if (debug1.readBoolean()) {
/*  61 */               debug4.getProperties().put(debug10, new Property(debug10, debug11, debug1.readUtf(32767)));
/*     */             } else {
/*  63 */               debug4.getProperties().put(debug10, new Property(debug10, debug11));
/*     */             } 
/*     */           } 
/*  66 */           debug6 = GameType.byId(debug1.readVarInt());
/*  67 */           debug5 = debug1.readVarInt();
/*  68 */           if (debug1.readBoolean()) {
/*  69 */             debug7 = debug1.readComponent();
/*     */           }
/*     */           break;
/*     */         case UPDATE_GAME_MODE:
/*  73 */           debug4 = new GameProfile(debug1.readUUID(), null);
/*  74 */           debug6 = GameType.byId(debug1.readVarInt());
/*     */           break;
/*     */         case UPDATE_LATENCY:
/*  77 */           debug4 = new GameProfile(debug1.readUUID(), null);
/*  78 */           debug5 = debug1.readVarInt();
/*     */           break;
/*     */         case UPDATE_DISPLAY_NAME:
/*  81 */           debug4 = new GameProfile(debug1.readUUID(), null);
/*  82 */           if (debug1.readBoolean()) {
/*  83 */             debug7 = debug1.readComponent();
/*     */           }
/*     */           break;
/*     */         case REMOVE_PLAYER:
/*  87 */           debug4 = new GameProfile(debug1.readUUID(), null);
/*     */           break;
/*     */       } 
/*     */       
/*  91 */       this.entries.add(new PlayerUpdate(debug4, debug5, debug6, debug7));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf debug1) throws IOException {
/*  97 */     debug1.writeEnum(this.action);
/*     */     
/*  99 */     debug1.writeVarInt(this.entries.size());
/* 100 */     for (PlayerUpdate debug3 : this.entries) {
/* 101 */       switch (this.action) {
/*     */         case ADD_PLAYER:
/* 103 */           debug1.writeUUID(debug3.getProfile().getId());
/* 104 */           debug1.writeUtf(debug3.getProfile().getName());
/* 105 */           debug1.writeVarInt(debug3.getProfile().getProperties().size());
/* 106 */           for (Property debug5 : debug3.getProfile().getProperties().values()) {
/* 107 */             debug1.writeUtf(debug5.getName());
/* 108 */             debug1.writeUtf(debug5.getValue());
/* 109 */             if (debug5.hasSignature()) {
/* 110 */               debug1.writeBoolean(true);
/* 111 */               debug1.writeUtf(debug5.getSignature()); continue;
/*     */             } 
/* 113 */             debug1.writeBoolean(false);
/*     */           } 
/*     */           
/* 116 */           debug1.writeVarInt(debug3.getGameMode().getId());
/* 117 */           debug1.writeVarInt(debug3.getLatency());
/*     */           
/* 119 */           if (debug3.getDisplayName() == null) {
/* 120 */             debug1.writeBoolean(false); continue;
/*     */           } 
/* 122 */           debug1.writeBoolean(true);
/* 123 */           debug1.writeComponent(debug3.getDisplayName());
/*     */ 
/*     */         
/*     */         case UPDATE_GAME_MODE:
/* 127 */           debug1.writeUUID(debug3.getProfile().getId());
/* 128 */           debug1.writeVarInt(debug3.getGameMode().getId());
/*     */         
/*     */         case UPDATE_LATENCY:
/* 131 */           debug1.writeUUID(debug3.getProfile().getId());
/* 132 */           debug1.writeVarInt(debug3.getLatency());
/*     */         
/*     */         case UPDATE_DISPLAY_NAME:
/* 135 */           debug1.writeUUID(debug3.getProfile().getId());
/* 136 */           if (debug3.getDisplayName() == null) {
/* 137 */             debug1.writeBoolean(false); continue;
/*     */           } 
/* 139 */           debug1.writeBoolean(true);
/* 140 */           debug1.writeComponent(debug3.getDisplayName());
/*     */ 
/*     */         
/*     */         case REMOVE_PLAYER:
/* 144 */           debug1.writeUUID(debug3.getProfile().getId());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handle(ClientGamePacketListener debug1) {
/* 152 */     debug1.handlePlayerInfo(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Action
/*     */   {
/* 164 */     ADD_PLAYER,
/* 165 */     UPDATE_GAME_MODE,
/* 166 */     UPDATE_LATENCY,
/* 167 */     UPDATE_DISPLAY_NAME,
/* 168 */     REMOVE_PLAYER;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 174 */     return MoreObjects.toStringHelper(this)
/* 175 */       .add("action", this.action)
/* 176 */       .add("entries", this.entries)
/* 177 */       .toString();
/*     */   }
/*     */   
/*     */   public ClientboundPlayerInfoPacket() {}
/*     */   
/*     */   public class PlayerUpdate {
/*     */     private final int latency;
/*     */     private final GameType gameMode;
/*     */     
/*     */     public PlayerUpdate(GameProfile debug2, @Nullable int debug3, @Nullable GameType debug4, Component debug5) {
/* 187 */       this.profile = debug2;
/* 188 */       this.latency = debug3;
/* 189 */       this.gameMode = debug4;
/* 190 */       this.displayName = debug5;
/*     */     }
/*     */     private final GameProfile profile; private final Component displayName;
/*     */     public GameProfile getProfile() {
/* 194 */       return this.profile;
/*     */     }
/*     */     
/*     */     public int getLatency() {
/* 198 */       return this.latency;
/*     */     }
/*     */     
/*     */     public GameType getGameMode() {
/* 202 */       return this.gameMode;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public Component getDisplayName() {
/* 207 */       return this.displayName;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 212 */       return MoreObjects.toStringHelper(this)
/* 213 */         .add("latency", this.latency)
/* 214 */         .add("gameMode", this.gameMode)
/* 215 */         .add("profile", this.profile)
/* 216 */         .add("displayName", (this.displayName == null) ? null : Component.Serializer.toJson(this.displayName))
/* 217 */         .toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundPlayerInfoPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */