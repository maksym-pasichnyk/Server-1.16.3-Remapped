/*     */ package net.minecraft.server.bossevents;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerBossEvent;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.BossEvent;
/*     */ 
/*     */ public class CustomBossEvent extends ServerBossEvent {
/*  23 */   private final Set<UUID> players = Sets.newHashSet(); private final ResourceLocation id;
/*     */   private int value;
/*  25 */   private int max = 100;
/*     */   
/*     */   public CustomBossEvent(ResourceLocation debug1, Component debug2) {
/*  28 */     super(debug2, BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS);
/*  29 */     this.id = debug1;
/*  30 */     setPercent(0.0F);
/*     */   }
/*     */   
/*     */   public ResourceLocation getTextId() {
/*  34 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPlayer(ServerPlayer debug1) {
/*  39 */     super.addPlayer(debug1);
/*  40 */     this.players.add(debug1.getUUID());
/*     */   }
/*     */   
/*     */   public void addOfflinePlayer(UUID debug1) {
/*  44 */     this.players.add(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removePlayer(ServerPlayer debug1) {
/*  49 */     super.removePlayer(debug1);
/*  50 */     this.players.remove(debug1.getUUID());
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAllPlayers() {
/*  55 */     super.removeAllPlayers();
/*  56 */     this.players.clear();
/*     */   }
/*     */   
/*     */   public int getValue() {
/*  60 */     return this.value;
/*     */   }
/*     */   
/*     */   public int getMax() {
/*  64 */     return this.max;
/*     */   }
/*     */   
/*     */   public void setValue(int debug1) {
/*  68 */     this.value = debug1;
/*  69 */     setPercent(Mth.clamp(debug1 / this.max, 0.0F, 1.0F));
/*     */   }
/*     */   
/*     */   public void setMax(int debug1) {
/*  73 */     this.max = debug1;
/*  74 */     setPercent(Mth.clamp(this.value / debug1, 0.0F, 1.0F));
/*     */   }
/*     */   
/*     */   public final Component getDisplayName() {
/*  78 */     return (Component)ComponentUtils.wrapInSquareBrackets(getName()).withStyle(debug1 -> debug1.withColor(getColor().getFormatting()).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent(getTextId().toString()))).withInsertion(getTextId().toString()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setPlayers(Collection<ServerPlayer> debug1) {
/*  86 */     Set<UUID> debug2 = Sets.newHashSet();
/*  87 */     Set<ServerPlayer> debug3 = Sets.newHashSet();
/*     */     
/*  89 */     for (UUID debug5 : this.players) {
/*  90 */       boolean debug6 = false;
/*  91 */       for (ServerPlayer debug8 : debug1) {
/*  92 */         if (debug8.getUUID().equals(debug5)) {
/*  93 */           debug6 = true;
/*     */           break;
/*     */         } 
/*     */       } 
/*  97 */       if (!debug6) {
/*  98 */         debug2.add(debug5);
/*     */       }
/*     */     } 
/*     */     
/* 102 */     for (ServerPlayer debug5 : debug1) {
/* 103 */       boolean debug6 = false;
/* 104 */       for (UUID debug8 : this.players) {
/* 105 */         if (debug5.getUUID().equals(debug8)) {
/* 106 */           debug6 = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 110 */       if (!debug6) {
/* 111 */         debug3.add(debug5);
/*     */       }
/*     */     } 
/*     */     
/* 115 */     for (UUID debug5 : debug2) {
/* 116 */       for (ServerPlayer debug7 : getPlayers()) {
/* 117 */         if (debug7.getUUID().equals(debug5)) {
/* 118 */           removePlayer(debug7);
/*     */           break;
/*     */         } 
/*     */       } 
/* 122 */       this.players.remove(debug5);
/*     */     } 
/*     */     
/* 125 */     for (ServerPlayer debug5 : debug3) {
/* 126 */       addPlayer(debug5);
/*     */     }
/*     */     
/* 129 */     return (!debug2.isEmpty() || !debug3.isEmpty());
/*     */   }
/*     */   
/*     */   public CompoundTag save() {
/* 133 */     CompoundTag debug1 = new CompoundTag();
/*     */     
/* 135 */     debug1.putString("Name", Component.Serializer.toJson(this.name));
/* 136 */     debug1.putBoolean("Visible", isVisible());
/* 137 */     debug1.putInt("Value", this.value);
/* 138 */     debug1.putInt("Max", this.max);
/* 139 */     debug1.putString("Color", getColor().getName());
/* 140 */     debug1.putString("Overlay", getOverlay().getName());
/* 141 */     debug1.putBoolean("DarkenScreen", shouldDarkenScreen());
/* 142 */     debug1.putBoolean("PlayBossMusic", shouldPlayBossMusic());
/* 143 */     debug1.putBoolean("CreateWorldFog", shouldCreateWorldFog());
/*     */     
/* 145 */     ListTag debug2 = new ListTag();
/* 146 */     for (UUID debug4 : this.players) {
/* 147 */       debug2.add(NbtUtils.createUUID(debug4));
/*     */     }
/* 149 */     debug1.put("Players", (Tag)debug2);
/*     */     
/* 151 */     return debug1;
/*     */   }
/*     */   
/*     */   public static CustomBossEvent load(CompoundTag debug0, ResourceLocation debug1) {
/* 155 */     CustomBossEvent debug2 = new CustomBossEvent(debug1, (Component)Component.Serializer.fromJson(debug0.getString("Name")));
/* 156 */     debug2.setVisible(debug0.getBoolean("Visible"));
/* 157 */     debug2.setValue(debug0.getInt("Value"));
/* 158 */     debug2.setMax(debug0.getInt("Max"));
/* 159 */     debug2.setColor(BossEvent.BossBarColor.byName(debug0.getString("Color")));
/* 160 */     debug2.setOverlay(BossEvent.BossBarOverlay.byName(debug0.getString("Overlay")));
/* 161 */     debug2.setDarkenScreen(debug0.getBoolean("DarkenScreen"));
/* 162 */     debug2.setPlayBossMusic(debug0.getBoolean("PlayBossMusic"));
/* 163 */     debug2.setCreateWorldFog(debug0.getBoolean("CreateWorldFog"));
/*     */     
/* 165 */     ListTag debug3 = debug0.getList("Players", 11);
/* 166 */     for (int debug4 = 0; debug4 < debug3.size(); debug4++) {
/* 167 */       debug2.addOfflinePlayer(NbtUtils.loadUUID(debug3.get(debug4)));
/*     */     }
/*     */     
/* 170 */     return debug2;
/*     */   }
/*     */   
/*     */   public void onPlayerConnect(ServerPlayer debug1) {
/* 174 */     if (this.players.contains(debug1.getUUID())) {
/* 175 */       addPlayer(debug1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onPlayerDisconnect(ServerPlayer debug1) {
/* 180 */     super.removePlayer(debug1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\bossevents\CustomBossEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */