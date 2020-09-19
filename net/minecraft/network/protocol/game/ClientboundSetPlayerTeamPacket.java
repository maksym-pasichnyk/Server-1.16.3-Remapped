/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.Team;
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
/*     */ public class ClientboundSetPlayerTeamPacket
/*     */   implements Packet<ClientGamePacketListener>
/*     */ {
/*  27 */   private String name = "";
/*  28 */   private Component displayName = TextComponent.EMPTY;
/*  29 */   private Component playerPrefix = TextComponent.EMPTY;
/*  30 */   private Component playerSuffix = TextComponent.EMPTY;
/*  31 */   private String nametagVisibility = Team.Visibility.ALWAYS.name;
/*  32 */   private String collisionRule = Team.CollisionRule.ALWAYS.name;
/*  33 */   private ChatFormatting color = ChatFormatting.RESET;
/*  34 */   private final Collection<String> players = Lists.newArrayList();
/*     */   
/*     */   private int method;
/*     */   
/*     */   private int options;
/*     */ 
/*     */   
/*     */   public ClientboundSetPlayerTeamPacket(PlayerTeam debug1, int debug2) {
/*  42 */     this.name = debug1.getName();
/*  43 */     this.method = debug2;
/*     */     
/*  45 */     if (debug2 == 0 || debug2 == 2) {
/*  46 */       this.displayName = debug1.getDisplayName();
/*  47 */       this.options = debug1.packOptions();
/*  48 */       this.nametagVisibility = (debug1.getNameTagVisibility()).name;
/*  49 */       this.collisionRule = (debug1.getCollisionRule()).name;
/*  50 */       this.color = debug1.getColor();
/*  51 */       this.playerPrefix = debug1.getPlayerPrefix();
/*  52 */       this.playerSuffix = debug1.getPlayerSuffix();
/*     */     } 
/*  54 */     if (debug2 == 0) {
/*  55 */       this.players.addAll(debug1.getPlayers());
/*     */     }
/*     */   }
/*     */   
/*     */   public ClientboundSetPlayerTeamPacket(PlayerTeam debug1, Collection<String> debug2, int debug3) {
/*  60 */     if (debug3 != 3 && debug3 != 4) {
/*  61 */       throw new IllegalArgumentException("Method must be join or leave for player constructor");
/*     */     }
/*  63 */     if (debug2 == null || debug2.isEmpty()) {
/*  64 */       throw new IllegalArgumentException("Players cannot be null/empty");
/*     */     }
/*     */     
/*  67 */     this.method = debug3;
/*  68 */     this.name = debug1.getName();
/*  69 */     this.players.addAll(debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(FriendlyByteBuf debug1) throws IOException {
/*  74 */     this.name = debug1.readUtf(16);
/*  75 */     this.method = debug1.readByte();
/*     */     
/*  77 */     if (this.method == 0 || this.method == 2) {
/*  78 */       this.displayName = debug1.readComponent();
/*  79 */       this.options = debug1.readByte();
/*  80 */       this.nametagVisibility = debug1.readUtf(40);
/*  81 */       this.collisionRule = debug1.readUtf(40);
/*  82 */       this.color = (ChatFormatting)debug1.readEnum(ChatFormatting.class);
/*  83 */       this.playerPrefix = debug1.readComponent();
/*  84 */       this.playerSuffix = debug1.readComponent();
/*     */     } 
/*     */     
/*  87 */     if (this.method == 0 || this.method == 3 || this.method == 4) {
/*  88 */       int debug2 = debug1.readVarInt();
/*     */       
/*  90 */       for (int debug3 = 0; debug3 < debug2; debug3++) {
/*  91 */         this.players.add(debug1.readUtf(40));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf debug1) throws IOException {
/*  98 */     debug1.writeUtf(this.name);
/*  99 */     debug1.writeByte(this.method);
/*     */     
/* 101 */     if (this.method == 0 || this.method == 2) {
/* 102 */       debug1.writeComponent(this.displayName);
/* 103 */       debug1.writeByte(this.options);
/* 104 */       debug1.writeUtf(this.nametagVisibility);
/* 105 */       debug1.writeUtf(this.collisionRule);
/* 106 */       debug1.writeEnum((Enum)this.color);
/* 107 */       debug1.writeComponent(this.playerPrefix);
/* 108 */       debug1.writeComponent(this.playerSuffix);
/*     */     } 
/*     */     
/* 111 */     if (this.method == 0 || this.method == 3 || this.method == 4) {
/* 112 */       debug1.writeVarInt(this.players.size());
/*     */       
/* 114 */       for (String debug3 : this.players) {
/* 115 */         debug1.writeUtf(debug3);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(ClientGamePacketListener debug1) {
/* 122 */     debug1.handleSetPlayerTeamPacket(this);
/*     */   }
/*     */   
/*     */   public ClientboundSetPlayerTeamPacket() {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetPlayerTeamPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */