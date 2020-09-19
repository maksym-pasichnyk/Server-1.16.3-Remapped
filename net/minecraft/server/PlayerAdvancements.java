/*     */ package net.minecraft.server;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.io.Files;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.internal.Streams;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import com.google.gson.stream.JsonReader;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.StringReader;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.advancements.Criterion;
/*     */ import net.minecraft.advancements.CriterionProgress;
/*     */ import net.minecraft.advancements.CriterionTrigger;
/*     */ import net.minecraft.advancements.CriterionTriggerInstance;
/*     */ import net.minecraft.network.chat.ChatType;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundSelectAdvancementsTabPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.players.PlayerList;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class PlayerAdvancements {
/*  56 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  58 */   private static final Gson GSON = (new GsonBuilder())
/*  59 */     .registerTypeAdapter(AdvancementProgress.class, new AdvancementProgress.Serializer())
/*  60 */     .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
/*  61 */     .setPrettyPrinting()
/*  62 */     .create();
/*  63 */   private static final TypeToken<Map<ResourceLocation, AdvancementProgress>> TYPE_TOKEN = new TypeToken<Map<ResourceLocation, AdvancementProgress>>() {  }
/*     */   ;
/*     */   private final DataFixer dataFixer;
/*     */   private final PlayerList playerList;
/*     */   private final File file;
/*  68 */   private final Map<Advancement, AdvancementProgress> advancements = Maps.newLinkedHashMap();
/*  69 */   private final Set<Advancement> visible = Sets.newLinkedHashSet();
/*  70 */   private final Set<Advancement> visibilityChanged = Sets.newLinkedHashSet();
/*  71 */   private final Set<Advancement> progressChanged = Sets.newLinkedHashSet();
/*     */   private ServerPlayer player;
/*     */   @Nullable
/*     */   private Advancement lastSelectedTab;
/*     */   private boolean isFirstPacket = true;
/*     */   
/*     */   public PlayerAdvancements(DataFixer debug1, PlayerList debug2, ServerAdvancementManager debug3, File debug4, ServerPlayer debug5) {
/*  78 */     this.dataFixer = debug1;
/*  79 */     this.playerList = debug2;
/*  80 */     this.file = debug4;
/*  81 */     this.player = debug5;
/*  82 */     load(debug3);
/*     */   }
/*     */   
/*     */   public void setPlayer(ServerPlayer debug1) {
/*  86 */     this.player = debug1;
/*     */   }
/*     */   
/*     */   public void stopListening() {
/*  90 */     for (CriterionTrigger<?> debug2 : (Iterable<CriterionTrigger<?>>)CriteriaTriggers.all()) {
/*  91 */       debug2.removePlayerListeners(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void reload(ServerAdvancementManager debug1) {
/*  96 */     stopListening();
/*  97 */     this.advancements.clear();
/*  98 */     this.visible.clear();
/*  99 */     this.visibilityChanged.clear();
/* 100 */     this.progressChanged.clear();
/* 101 */     this.isFirstPacket = true;
/* 102 */     this.lastSelectedTab = null;
/* 103 */     load(debug1);
/*     */   }
/*     */   
/*     */   private void registerListeners(ServerAdvancementManager debug1) {
/* 107 */     for (Advancement debug3 : debug1.getAllAdvancements()) {
/* 108 */       registerListeners(debug3);
/*     */     }
/*     */   }
/*     */   
/*     */   private void ensureAllVisible() {
/* 113 */     List<Advancement> debug1 = Lists.newArrayList();
/* 114 */     for (Map.Entry<Advancement, AdvancementProgress> debug3 : this.advancements.entrySet()) {
/* 115 */       if (((AdvancementProgress)debug3.getValue()).isDone()) {
/* 116 */         debug1.add(debug3.getKey());
/* 117 */         this.progressChanged.add(debug3.getKey());
/*     */       } 
/*     */     } 
/* 120 */     for (Advancement debug3 : debug1) {
/* 121 */       ensureVisibility(debug3);
/*     */     }
/*     */   }
/*     */   
/*     */   private void checkForAutomaticTriggers(ServerAdvancementManager debug1) {
/* 126 */     for (Advancement debug3 : debug1.getAllAdvancements()) {
/* 127 */       if (debug3.getCriteria().isEmpty()) {
/* 128 */         award(debug3, "");
/* 129 */         debug3.getRewards().grant(this.player);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void load(ServerAdvancementManager debug1) {
/* 135 */     if (this.file.isFile()) {
/* 136 */       try (JsonReader debug2 = new JsonReader(new StringReader(Files.toString(this.file, StandardCharsets.UTF_8)))) {
/* 137 */         debug2.setLenient(false);
/* 138 */         Dynamic<JsonElement> debug4 = new Dynamic((DynamicOps)JsonOps.INSTANCE, Streams.parse(debug2));
/*     */ 
/*     */         
/* 141 */         if (!debug4.get("DataVersion").asNumber().result().isPresent()) {
/* 142 */           debug4 = debug4.set("DataVersion", debug4.createInt(1343));
/*     */         }
/* 144 */         debug4 = this.dataFixer.update(DataFixTypes.ADVANCEMENTS.getType(), debug4, debug4.get("DataVersion").asInt(0), SharedConstants.getCurrentVersion().getWorldVersion());
/*     */         
/* 146 */         debug4 = debug4.remove("DataVersion");
/*     */         
/* 148 */         Map<ResourceLocation, AdvancementProgress> debug5 = (Map<ResourceLocation, AdvancementProgress>)GSON.getAdapter(TYPE_TOKEN).fromJsonTree((JsonElement)debug4.getValue());
/*     */         
/* 150 */         if (debug5 == null) {
/* 151 */           throw new JsonParseException("Found null for advancements");
/*     */         }
/* 153 */         Stream<Map.Entry<ResourceLocation, AdvancementProgress>> debug6 = debug5.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue));
/* 154 */         for (Map.Entry<ResourceLocation, AdvancementProgress> debug8 : (Iterable<Map.Entry<ResourceLocation, AdvancementProgress>>)debug6.collect((Collector)Collectors.toList())) {
/* 155 */           Advancement debug9 = debug1.getAdvancement(debug8.getKey());
/* 156 */           if (debug9 == null) {
/* 157 */             LOGGER.warn("Ignored advancement '{}' in progress file {} - it doesn't exist anymore?", debug8.getKey(), this.file);
/*     */             continue;
/*     */           } 
/* 160 */           startProgress(debug9, debug8.getValue());
/*     */         } 
/* 162 */       } catch (JsonParseException debug2) {
/* 163 */         LOGGER.error("Couldn't parse player advancements in {}", this.file, debug2);
/* 164 */       } catch (IOException debug2) {
/* 165 */         LOGGER.error("Couldn't access player advancements in {}", this.file, debug2);
/*     */       } 
/*     */     }
/*     */     
/* 169 */     checkForAutomaticTriggers(debug1);
/* 170 */     ensureAllVisible();
/* 171 */     registerListeners(debug1);
/*     */   }
/*     */   
/*     */   public void save() {
/* 175 */     Map<ResourceLocation, AdvancementProgress> debug1 = Maps.newHashMap();
/* 176 */     for (Map.Entry<Advancement, AdvancementProgress> debug3 : this.advancements.entrySet()) {
/* 177 */       AdvancementProgress debug4 = debug3.getValue();
/* 178 */       if (debug4.hasProgress()) {
/* 179 */         debug1.put(((Advancement)debug3.getKey()).getId(), debug4);
/*     */       }
/*     */     } 
/* 182 */     if (this.file.getParentFile() != null) {
/* 183 */       this.file.getParentFile().mkdirs();
/*     */     }
/*     */     
/* 186 */     JsonElement debug2 = GSON.toJsonTree(debug1);
/* 187 */     debug2.getAsJsonObject().addProperty("DataVersion", Integer.valueOf(SharedConstants.getCurrentVersion().getWorldVersion()));
/* 188 */     try(OutputStream debug3 = new FileOutputStream(this.file); Writer debug5 = new OutputStreamWriter(debug3, Charsets.UTF_8.newEncoder())) {
/* 189 */       GSON.toJson(debug2, debug5);
/* 190 */     } catch (IOException debug3) {
/* 191 */       LOGGER.error("Couldn't save player advancements to {}", this.file, debug3);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean award(Advancement debug1, String debug2) {
/* 196 */     boolean debug3 = false;
/*     */     
/* 198 */     AdvancementProgress debug4 = getOrStartProgress(debug1);
/* 199 */     boolean debug5 = debug4.isDone();
/*     */     
/* 201 */     if (debug4.grantProgress(debug2)) {
/* 202 */       unregisterListeners(debug1);
/* 203 */       this.progressChanged.add(debug1);
/* 204 */       debug3 = true;
/*     */       
/* 206 */       if (!debug5 && debug4.isDone()) {
/* 207 */         debug1.getRewards().grant(this.player);
/* 208 */         if (debug1.getDisplay() != null && debug1.getDisplay().shouldAnnounceChat() && this.player.level.getGameRules().getBoolean(GameRules.RULE_ANNOUNCE_ADVANCEMENTS)) {
/* 209 */           this.playerList.broadcastMessage((Component)new TranslatableComponent("chat.type.advancement." + debug1.getDisplay().getFrame().getName(), new Object[] { this.player.getDisplayName(), debug1.getChatComponent() }), ChatType.SYSTEM, Util.NIL_UUID);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 214 */     if (debug4.isDone()) {
/* 215 */       ensureVisibility(debug1);
/*     */     }
/*     */     
/* 218 */     return debug3;
/*     */   }
/*     */   
/*     */   public boolean revoke(Advancement debug1, String debug2) {
/* 222 */     boolean debug3 = false;
/*     */     
/* 224 */     AdvancementProgress debug4 = getOrStartProgress(debug1);
/* 225 */     if (debug4.revokeProgress(debug2)) {
/* 226 */       registerListeners(debug1);
/* 227 */       this.progressChanged.add(debug1);
/* 228 */       debug3 = true;
/*     */     } 
/*     */     
/* 231 */     if (!debug4.hasProgress()) {
/* 232 */       ensureVisibility(debug1);
/*     */     }
/*     */     
/* 235 */     return debug3;
/*     */   }
/*     */   
/*     */   private void registerListeners(Advancement debug1) {
/* 239 */     AdvancementProgress debug2 = getOrStartProgress(debug1);
/* 240 */     if (debug2.isDone()) {
/*     */       return;
/*     */     }
/* 243 */     for (Map.Entry<String, Criterion> debug4 : (Iterable<Map.Entry<String, Criterion>>)debug1.getCriteria().entrySet()) {
/* 244 */       CriterionProgress debug5 = debug2.getCriterion(debug4.getKey());
/* 245 */       if (debug5 == null || debug5.isDone()) {
/*     */         continue;
/*     */       }
/* 248 */       CriterionTriggerInstance debug6 = ((Criterion)debug4.getValue()).getTrigger();
/* 249 */       if (debug6 != null) {
/* 250 */         CriterionTrigger<CriterionTriggerInstance> debug7 = CriteriaTriggers.getCriterion(debug6.getCriterion());
/* 251 */         if (debug7 != null) {
/* 252 */           debug7.addPlayerListener(this, new CriterionTrigger.Listener(debug6, debug1, debug4.getKey()));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void unregisterListeners(Advancement debug1) {
/* 259 */     AdvancementProgress debug2 = getOrStartProgress(debug1);
/* 260 */     for (Map.Entry<String, Criterion> debug4 : (Iterable<Map.Entry<String, Criterion>>)debug1.getCriteria().entrySet()) {
/* 261 */       CriterionProgress debug5 = debug2.getCriterion(debug4.getKey());
/* 262 */       if (debug5 == null || (!debug5.isDone() && !debug2.isDone())) {
/*     */         continue;
/*     */       }
/* 265 */       CriterionTriggerInstance debug6 = ((Criterion)debug4.getValue()).getTrigger();
/* 266 */       if (debug6 != null) {
/* 267 */         CriterionTrigger<CriterionTriggerInstance> debug7 = CriteriaTriggers.getCriterion(debug6.getCriterion());
/* 268 */         if (debug7 != null) {
/* 269 */           debug7.removePlayerListener(this, new CriterionTrigger.Listener(debug6, debug1, debug4.getKey()));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void flushDirty(ServerPlayer debug1) {
/* 276 */     if (this.isFirstPacket || !this.visibilityChanged.isEmpty() || !this.progressChanged.isEmpty()) {
/* 277 */       Map<ResourceLocation, AdvancementProgress> debug2 = Maps.newHashMap();
/* 278 */       Set<Advancement> debug3 = Sets.newLinkedHashSet();
/* 279 */       Set<ResourceLocation> debug4 = Sets.newLinkedHashSet();
/* 280 */       for (Advancement debug6 : this.progressChanged) {
/* 281 */         if (this.visible.contains(debug6)) {
/* 282 */           debug2.put(debug6.getId(), this.advancements.get(debug6));
/*     */         }
/*     */       } 
/* 285 */       for (Advancement debug6 : this.visibilityChanged) {
/* 286 */         if (this.visible.contains(debug6)) {
/* 287 */           debug3.add(debug6); continue;
/*     */         } 
/* 289 */         debug4.add(debug6.getId());
/*     */       } 
/*     */       
/* 292 */       if (this.isFirstPacket || !debug2.isEmpty() || !debug3.isEmpty() || !debug4.isEmpty()) {
/* 293 */         debug1.connection.send((Packet)new ClientboundUpdateAdvancementsPacket(this.isFirstPacket, debug3, debug4, debug2));
/* 294 */         this.visibilityChanged.clear();
/* 295 */         this.progressChanged.clear();
/*     */       } 
/*     */     } 
/* 298 */     this.isFirstPacket = false;
/*     */   }
/*     */   
/*     */   public void setSelectedTab(@Nullable Advancement debug1) {
/* 302 */     Advancement debug2 = this.lastSelectedTab;
/* 303 */     if (debug1 != null && debug1.getParent() == null && debug1.getDisplay() != null) {
/* 304 */       this.lastSelectedTab = debug1;
/*     */     } else {
/* 306 */       this.lastSelectedTab = null;
/*     */     } 
/* 308 */     if (debug2 != this.lastSelectedTab) {
/* 309 */       this.player.connection.send((Packet)new ClientboundSelectAdvancementsTabPacket((this.lastSelectedTab == null) ? null : this.lastSelectedTab.getId()));
/*     */     }
/*     */   }
/*     */   
/*     */   public AdvancementProgress getOrStartProgress(Advancement debug1) {
/* 314 */     AdvancementProgress debug2 = this.advancements.get(debug1);
/* 315 */     if (debug2 == null) {
/* 316 */       debug2 = new AdvancementProgress();
/* 317 */       startProgress(debug1, debug2);
/*     */     } 
/* 319 */     return debug2;
/*     */   }
/*     */   
/*     */   private void startProgress(Advancement debug1, AdvancementProgress debug2) {
/* 323 */     debug2.update(debug1.getCriteria(), debug1.getRequirements());
/* 324 */     this.advancements.put(debug1, debug2);
/*     */   }
/*     */   
/*     */   private void ensureVisibility(Advancement debug1) {
/* 328 */     boolean debug2 = shouldBeVisible(debug1);
/* 329 */     boolean debug3 = this.visible.contains(debug1);
/*     */     
/* 331 */     if (debug2 && !debug3) {
/* 332 */       this.visible.add(debug1);
/* 333 */       this.visibilityChanged.add(debug1);
/* 334 */       if (this.advancements.containsKey(debug1)) {
/* 335 */         this.progressChanged.add(debug1);
/*     */       }
/* 337 */     } else if (!debug2 && debug3) {
/* 338 */       this.visible.remove(debug1);
/* 339 */       this.visibilityChanged.add(debug1);
/*     */     } 
/*     */     
/* 342 */     if (debug2 != debug3 && 
/* 343 */       debug1.getParent() != null) {
/* 344 */       ensureVisibility(debug1.getParent());
/*     */     }
/*     */     
/* 347 */     for (Advancement debug5 : debug1.getChildren()) {
/* 348 */       ensureVisibility(debug5);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean shouldBeVisible(Advancement debug1) {
/* 353 */     for (int debug2 = 0; debug1 != null && debug2 <= 2; debug2++) {
/* 354 */       if (debug2 == 0 && 
/* 355 */         hasCompletedChildrenOrSelf(debug1)) {
/* 356 */         return true;
/*     */       }
/*     */ 
/*     */       
/* 360 */       if (debug1.getDisplay() == null) {
/* 361 */         return false;
/*     */       }
/* 363 */       AdvancementProgress debug3 = getOrStartProgress(debug1);
/* 364 */       if (debug3.isDone())
/* 365 */         return true; 
/* 366 */       if (debug1.getDisplay().isHidden()) {
/* 367 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 371 */       debug1 = debug1.getParent();
/*     */     } 
/*     */     
/* 374 */     return false;
/*     */   }
/*     */   
/*     */   private boolean hasCompletedChildrenOrSelf(Advancement debug1) {
/* 378 */     AdvancementProgress debug2 = getOrStartProgress(debug1);
/* 379 */     if (debug2.isDone()) {
/* 380 */       return true;
/*     */     }
/*     */     
/* 383 */     for (Advancement debug4 : debug1.getChildren()) {
/* 384 */       if (hasCompletedChildrenOrSelf(debug4)) {
/* 385 */         return true;
/*     */       }
/*     */     } 
/* 388 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\PlayerAdvancements.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */