/*     */ package net.minecraft.server.players;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import java.io.File;
/*     */ import java.net.SocketAddress;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.chat.ChatType;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundChatPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundLoginPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetBorderPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetDefaultSpawnPositionPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSoundPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundUpdateTagsPacket;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.PlayerAdvancements;
/*     */ import net.minecraft.server.ServerScoreboard;
/*     */ import net.minecraft.server.level.DemoMode;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.level.ServerPlayerGameMode;
/*     */ import net.minecraft.server.network.ServerGamePacketListenerImpl;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.ServerStatsCounter;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.border.BorderChangeListener;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.storage.LevelData;
/*     */ import net.minecraft.world.level.storage.LevelResource;
/*     */ import net.minecraft.world.level.storage.PlayerDataStorage;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.scores.Objective;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.Team;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PlayerList
/*     */ {
/*  97 */   public static final File USERBANLIST_FILE = new File("banned-players.json");
/*  98 */   public static final File IPBANLIST_FILE = new File("banned-ips.json");
/*  99 */   public static final File OPLIST_FILE = new File("ops.json");
/* 100 */   public static final File WHITELIST_FILE = new File("whitelist.json");
/* 101 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/* 103 */   private static final SimpleDateFormat BAN_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
/*     */   
/*     */   private final MinecraftServer server;
/* 106 */   private final List<ServerPlayer> players = Lists.newArrayList();
/* 107 */   private final Map<UUID, ServerPlayer> playersByUUID = Maps.newHashMap();
/* 108 */   private final UserBanList bans = new UserBanList(USERBANLIST_FILE);
/* 109 */   private final IpBanList ipBans = new IpBanList(IPBANLIST_FILE);
/* 110 */   private final ServerOpList ops = new ServerOpList(OPLIST_FILE);
/* 111 */   private final UserWhiteList whitelist = new UserWhiteList(WHITELIST_FILE);
/* 112 */   private final Map<UUID, ServerStatsCounter> stats = Maps.newHashMap();
/* 113 */   private final Map<UUID, PlayerAdvancements> advancements = Maps.newHashMap();
/*     */   private final PlayerDataStorage playerIo;
/*     */   private boolean doWhiteList;
/*     */   private final RegistryAccess.RegistryHolder registryHolder;
/*     */   protected final int maxPlayers;
/*     */   private int viewDistance;
/*     */   private GameType overrideGameMode;
/*     */   private boolean allowCheatsForAllPlayers;
/*     */   private int sendAllPlayerInfoIn;
/*     */   
/*     */   public PlayerList(MinecraftServer debug1, RegistryAccess.RegistryHolder debug2, PlayerDataStorage debug3, int debug4) {
/* 124 */     this.server = debug1;
/* 125 */     this.registryHolder = debug2;
/* 126 */     this.maxPlayers = debug4;
/* 127 */     this.playerIo = debug3;
/*     */   } public void placeNewPlayer(Connection debug1, ServerPlayer debug2) {
/*     */     ServerLevel debug10;
/*     */     TranslatableComponent translatableComponent;
/* 131 */     GameProfile debug3 = debug2.getGameProfile();
/*     */     
/* 133 */     GameProfileCache debug4 = this.server.getProfileCache();
/* 134 */     GameProfile debug5 = debug4.get(debug3.getId());
/* 135 */     String debug6 = (debug5 == null) ? debug3.getName() : debug5.getName();
/* 136 */     debug4.add(debug3);
/*     */     
/* 138 */     CompoundTag debug7 = load(debug2);
/*     */     
/* 140 */     ResourceKey<Level> debug8 = (debug7 != null) ? DimensionType.parseLegacy(new Dynamic((DynamicOps)NbtOps.INSTANCE, debug7.get("Dimension"))).resultOrPartial(LOGGER::error).orElse(Level.OVERWORLD) : Level.OVERWORLD;
/* 141 */     ServerLevel debug9 = this.server.getLevel(debug8);
/*     */     
/* 143 */     if (debug9 == null) {
/* 144 */       LOGGER.warn("Unknown respawn dimension {}, defaulting to overworld", debug8);
/* 145 */       debug10 = this.server.overworld();
/*     */     } else {
/* 147 */       debug10 = debug9;
/*     */     } 
/* 149 */     debug2.setLevel((Level)debug10);
/* 150 */     debug2.gameMode.setLevel((ServerLevel)debug2.level);
/*     */     
/* 152 */     String debug11 = "local";
/*     */     
/* 154 */     if (debug1.getRemoteAddress() != null) {
/* 155 */       debug11 = debug1.getRemoteAddress().toString();
/*     */     }
/*     */     
/* 158 */     LOGGER.info("{}[{}] logged in with entity id {} at ({}, {}, {})", debug2.getName().getString(), debug11, Integer.valueOf(debug2.getId()), Double.valueOf(debug2.getX()), Double.valueOf(debug2.getY()), Double.valueOf(debug2.getZ()));
/*     */     
/* 160 */     LevelData debug12 = debug10.getLevelData();
/*     */     
/* 162 */     updatePlayerGameMode(debug2, null, debug10);
/*     */     
/* 164 */     ServerGamePacketListenerImpl debug13 = new ServerGamePacketListenerImpl(this.server, debug1, debug2);
/* 165 */     GameRules debug14 = debug10.getGameRules();
/* 166 */     boolean debug15 = debug14.getBoolean(GameRules.RULE_DO_IMMEDIATE_RESPAWN);
/* 167 */     boolean debug16 = debug14.getBoolean(GameRules.RULE_REDUCEDDEBUGINFO);
/* 168 */     debug13.send((Packet)new ClientboundLoginPacket(debug2.getId(), debug2.gameMode
/* 169 */           .getGameModeForPlayer(), debug2.gameMode
/* 170 */           .getPreviousGameModeForPlayer(), 
/* 171 */           BiomeManager.obfuscateSeed(debug10.getSeed()), debug12
/* 172 */           .isHardcore(), this.server
/* 173 */           .levelKeys(), this.registryHolder, debug10
/*     */           
/* 175 */           .dimensionType(), debug10
/* 176 */           .dimension(), 
/* 177 */           getMaxPlayers(), this.viewDistance, debug16, !debug15, debug10
/*     */ 
/*     */ 
/*     */           
/* 181 */           .isDebug(), debug10
/* 182 */           .isFlat()));
/* 183 */     debug13.send((Packet)new ClientboundCustomPayloadPacket(ClientboundCustomPayloadPacket.BRAND, (new FriendlyByteBuf(Unpooled.buffer())).writeUtf(getServer().getServerModName())));
/* 184 */     debug13.send((Packet)new ClientboundChangeDifficultyPacket(debug12.getDifficulty(), debug12.isDifficultyLocked()));
/* 185 */     debug13.send((Packet)new ClientboundPlayerAbilitiesPacket(debug2.abilities));
/* 186 */     debug13.send((Packet)new ClientboundSetCarriedItemPacket(debug2.inventory.selected));
/* 187 */     debug13.send((Packet)new ClientboundUpdateRecipesPacket(this.server.getRecipeManager().getRecipes()));
/* 188 */     debug13.send((Packet)new ClientboundUpdateTagsPacket(this.server.getTags()));
/* 189 */     sendPlayerPermissionLevel(debug2);
/*     */     
/* 191 */     debug2.getStats().markAllDirty();
/*     */     
/* 193 */     debug2.getRecipeBook().sendInitialRecipeBook(debug2);
/*     */     
/* 195 */     updateEntireScoreboard(debug10.getScoreboard(), debug2);
/*     */     
/* 197 */     this.server.invalidateStatus();
/*     */     
/* 199 */     if (debug2.getGameProfile().getName().equalsIgnoreCase(debug6)) {
/* 200 */       translatableComponent = new TranslatableComponent("multiplayer.player.joined", new Object[] { debug2.getDisplayName() });
/*     */     } else {
/* 202 */       translatableComponent = new TranslatableComponent("multiplayer.player.joined.renamed", new Object[] { debug2.getDisplayName(), debug6 });
/*     */     } 
/* 204 */     broadcastMessage((Component)translatableComponent.withStyle(ChatFormatting.YELLOW), ChatType.SYSTEM, Util.NIL_UUID);
/* 205 */     debug13.teleport(debug2.getX(), debug2.getY(), debug2.getZ(), debug2.yRot, debug2.xRot);
/*     */     
/* 207 */     this.players.add(debug2);
/* 208 */     this.playersByUUID.put(debug2.getUUID(), debug2);
/*     */     
/* 210 */     broadcastAll((Packet<?>)new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, new ServerPlayer[] { debug2 }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 220 */     for (int debug18 = 0; debug18 < this.players.size(); debug18++) {
/* 221 */       debug2.connection.send((Packet)new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, new ServerPlayer[] { this.players.get(debug18) }));
/*     */     } 
/*     */     
/* 224 */     debug10.addNewPlayer(debug2);
/*     */     
/* 226 */     this.server.getCustomBossEvents().onPlayerConnect(debug2);
/* 227 */     sendLevelInfo(debug2, debug10);
/*     */     
/* 229 */     if (!this.server.getResourcePack().isEmpty()) {
/* 230 */       debug2.sendTexturePack(this.server.getResourcePack(), this.server.getResourcePackHash());
/*     */     }
/*     */     
/* 233 */     for (MobEffectInstance debug19 : debug2.getActiveEffects()) {
/* 234 */       debug13.send((Packet)new ClientboundUpdateMobEffectPacket(debug2.getId(), debug19));
/*     */     }
/*     */     
/* 237 */     if (debug7 != null && debug7.contains("RootVehicle", 10)) {
/* 238 */       CompoundTag compoundTag = debug7.getCompound("RootVehicle");
/* 239 */       Entity debug19 = EntityType.loadEntityRecursive(compoundTag.getCompound("Entity"), (Level)debug10, debug1 -> !debug0.addWithUUID(debug1) ? null : debug1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 245 */       if (debug19 != null) {
/*     */         UUID debug20;
/* 247 */         if (compoundTag.hasUUID("Attach")) {
/* 248 */           debug20 = compoundTag.getUUID("Attach");
/*     */         } else {
/* 250 */           debug20 = null;
/*     */         } 
/* 252 */         if (debug19.getUUID().equals(debug20)) {
/* 253 */           debug2.startRiding(debug19, true);
/*     */         } else {
/* 255 */           for (Entity debug22 : debug19.getIndirectPassengers()) {
/* 256 */             if (debug22.getUUID().equals(debug20)) {
/* 257 */               debug2.startRiding(debug22, true);
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/* 262 */         if (!debug2.isPassenger()) {
/* 263 */           LOGGER.warn("Couldn't reattach entity to player");
/* 264 */           debug10.despawn(debug19);
/* 265 */           for (Entity debug22 : debug19.getIndirectPassengers()) {
/* 266 */             debug10.despawn(debug22);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 272 */     debug2.initMenu();
/*     */   }
/*     */   
/*     */   protected void updateEntireScoreboard(ServerScoreboard debug1, ServerPlayer debug2) {
/* 276 */     Set<Objective> debug3 = Sets.newHashSet();
/*     */     
/* 278 */     for (PlayerTeam debug5 : debug1.getPlayerTeams()) {
/* 279 */       debug2.connection.send((Packet)new ClientboundSetPlayerTeamPacket(debug5, 0));
/*     */     }
/*     */     
/* 282 */     for (int debug4 = 0; debug4 < 19; debug4++) {
/* 283 */       Objective debug5 = debug1.getDisplayObjective(debug4);
/*     */       
/* 285 */       if (debug5 != null && !debug3.contains(debug5)) {
/* 286 */         List<Packet<?>> debug6 = debug1.getStartTrackingPackets(debug5);
/*     */         
/* 288 */         for (Packet<?> debug8 : debug6) {
/* 289 */           debug2.connection.send(debug8);
/*     */         }
/*     */         
/* 292 */         debug3.add(debug5);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setLevel(ServerLevel debug1) {
/* 298 */     debug1.getWorldBorder().addListener(new BorderChangeListener()
/*     */         {
/*     */           public void onBorderSizeSet(WorldBorder debug1, double debug2) {
/* 301 */             PlayerList.this.broadcastAll((Packet<?>)new ClientboundSetBorderPacket(debug1, ClientboundSetBorderPacket.Type.SET_SIZE));
/*     */           }
/*     */ 
/*     */           
/*     */           public void onBorderSizeLerping(WorldBorder debug1, double debug2, double debug4, long debug6) {
/* 306 */             PlayerList.this.broadcastAll((Packet<?>)new ClientboundSetBorderPacket(debug1, ClientboundSetBorderPacket.Type.LERP_SIZE));
/*     */           }
/*     */ 
/*     */           
/*     */           public void onBorderCenterSet(WorldBorder debug1, double debug2, double debug4) {
/* 311 */             PlayerList.this.broadcastAll((Packet<?>)new ClientboundSetBorderPacket(debug1, ClientboundSetBorderPacket.Type.SET_CENTER));
/*     */           }
/*     */ 
/*     */           
/*     */           public void onBorderSetWarningTime(WorldBorder debug1, int debug2) {
/* 316 */             PlayerList.this.broadcastAll((Packet<?>)new ClientboundSetBorderPacket(debug1, ClientboundSetBorderPacket.Type.SET_WARNING_TIME));
/*     */           }
/*     */ 
/*     */           
/*     */           public void onBorderSetWarningBlocks(WorldBorder debug1, int debug2) {
/* 321 */             PlayerList.this.broadcastAll((Packet<?>)new ClientboundSetBorderPacket(debug1, ClientboundSetBorderPacket.Type.SET_WARNING_BLOCKS));
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void onBorderSetDamagePerBlock(WorldBorder debug1, double debug2) {}
/*     */ 
/*     */           
/*     */           public void onBorderSetDamageSafeZOne(WorldBorder debug1, double debug2) {}
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public CompoundTag load(ServerPlayer debug1) {
/* 336 */     CompoundTag debug3, debug2 = this.server.getWorldData().getLoadedPlayerTag();
/*     */ 
/*     */     
/* 339 */     if (debug1.getName().getString().equals(this.server.getSingleplayerName()) && debug2 != null) {
/* 340 */       debug3 = debug2;
/* 341 */       debug1.load(debug3);
/* 342 */       LOGGER.debug("loading single player");
/*     */     } else {
/* 344 */       debug3 = this.playerIo.load((Player)debug1);
/*     */     } 
/* 346 */     return debug3;
/*     */   }
/*     */   
/*     */   protected void save(ServerPlayer debug1) {
/* 350 */     this.playerIo.save((Player)debug1);
/* 351 */     ServerStatsCounter debug2 = this.stats.get(debug1.getUUID());
/* 352 */     if (debug2 != null) {
/* 353 */       debug2.save();
/*     */     }
/* 355 */     PlayerAdvancements debug3 = this.advancements.get(debug1.getUUID());
/* 356 */     if (debug3 != null) {
/* 357 */       debug3.save();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(ServerPlayer debug1) {
/* 367 */     ServerLevel debug2 = debug1.getLevel();
/* 368 */     debug1.awardStat(Stats.LEAVE_GAME);
/* 369 */     save(debug1);
/* 370 */     if (debug1.isPassenger()) {
/* 371 */       Entity entity = debug1.getRootVehicle();
/* 372 */       if (entity.hasOnePlayerPassenger()) {
/* 373 */         LOGGER.debug("Removing player mount");
/* 374 */         debug1.stopRiding();
/* 375 */         debug2.despawn(entity);
/* 376 */         entity.removed = true;
/* 377 */         for (Entity debug5 : entity.getIndirectPassengers()) {
/* 378 */           debug2.despawn(debug5);
/* 379 */           debug5.removed = true;
/*     */         } 
/* 381 */         debug2.getChunk(debug1.xChunk, debug1.zChunk).markUnsaved();
/*     */       } 
/*     */     } 
/* 384 */     debug1.unRide();
/* 385 */     debug2.removePlayerImmediately(debug1);
/*     */     
/* 387 */     debug1.getAdvancements().stopListening();
/* 388 */     this.players.remove(debug1);
/* 389 */     this.server.getCustomBossEvents().onPlayerDisconnect(debug1);
/* 390 */     UUID debug3 = debug1.getUUID();
/* 391 */     ServerPlayer debug4 = this.playersByUUID.get(debug3);
/* 392 */     if (debug4 == debug1) {
/* 393 */       this.playersByUUID.remove(debug3);
/* 394 */       this.stats.remove(debug3);
/* 395 */       this.advancements.remove(debug3);
/*     */     } 
/* 397 */     broadcastAll((Packet<?>)new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, new ServerPlayer[] { debug1 }));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Component canPlayerLogin(SocketAddress debug1, GameProfile debug2) {
/* 402 */     if (this.bans.isBanned(debug2)) {
/* 403 */       UserBanListEntry debug3 = this.bans.get(debug2);
/* 404 */       TranslatableComponent translatableComponent = new TranslatableComponent("multiplayer.disconnect.banned.reason", new Object[] { debug3.getReason() });
/*     */       
/* 406 */       if (debug3.getExpires() != null) {
/* 407 */         translatableComponent.append((Component)new TranslatableComponent("multiplayer.disconnect.banned.expiration", new Object[] { BAN_DATE_FORMAT.format(debug3.getExpires()) }));
/*     */       }
/*     */       
/* 410 */       return (Component)translatableComponent;
/*     */     } 
/*     */     
/* 413 */     if (!isWhiteListed(debug2)) {
/* 414 */       return (Component)new TranslatableComponent("multiplayer.disconnect.not_whitelisted");
/*     */     }
/*     */     
/* 417 */     if (this.ipBans.isBanned(debug1)) {
/* 418 */       IpBanListEntry debug3 = this.ipBans.get(debug1);
/* 419 */       TranslatableComponent translatableComponent = new TranslatableComponent("multiplayer.disconnect.banned_ip.reason", new Object[] { debug3.getReason() });
/*     */       
/* 421 */       if (debug3.getExpires() != null) {
/* 422 */         translatableComponent.append((Component)new TranslatableComponent("multiplayer.disconnect.banned_ip.expiration", new Object[] { BAN_DATE_FORMAT.format(debug3.getExpires()) }));
/*     */       }
/*     */       
/* 425 */       return (Component)translatableComponent;
/*     */     } 
/*     */     
/* 428 */     if (this.players.size() >= this.maxPlayers && !canBypassPlayerLimit(debug2)) {
/* 429 */       return (Component)new TranslatableComponent("multiplayer.disconnect.server_full");
/*     */     }
/*     */     
/* 432 */     return null;
/*     */   }
/*     */   public ServerPlayer getPlayerForLogin(GameProfile debug1) {
/*     */     ServerPlayerGameMode debug5;
/* 436 */     UUID debug2 = Player.createPlayerUUID(debug1);
/* 437 */     List<ServerPlayer> debug3 = Lists.newArrayList();
/* 438 */     for (int i = 0; i < this.players.size(); i++) {
/* 439 */       ServerPlayer serverPlayer = this.players.get(i);
/* 440 */       if (serverPlayer.getUUID().equals(debug2)) {
/* 441 */         debug3.add(serverPlayer);
/*     */       }
/*     */     } 
/* 444 */     ServerPlayer debug4 = this.playersByUUID.get(debug1.getId());
/* 445 */     if (debug4 != null && !debug3.contains(debug4)) {
/* 446 */       debug3.add(debug4);
/*     */     }
/* 448 */     for (ServerPlayer serverPlayer : debug3) {
/* 449 */       serverPlayer.connection.disconnect((Component)new TranslatableComponent("multiplayer.disconnect.duplicate_login"));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 454 */     ServerLevel debug6 = this.server.overworld();
/* 455 */     if (this.server.isDemo()) {
/* 456 */       DemoMode demoMode = new DemoMode(debug6);
/*     */     } else {
/* 458 */       debug5 = new ServerPlayerGameMode(debug6);
/*     */     } 
/*     */     
/* 461 */     return new ServerPlayer(this.server, debug6, debug1, debug5);
/*     */   } public ServerPlayer respawn(ServerPlayer debug1, boolean debug2) {
/*     */     Optional<Vec3> debug7;
/*     */     ServerPlayerGameMode debug8;
/* 465 */     this.players.remove(debug1);
/* 466 */     debug1.getLevel().removePlayerImmediately(debug1);
/*     */     
/* 468 */     BlockPos debug3 = debug1.getRespawnPosition();
/* 469 */     float debug4 = debug1.getRespawnAngle();
/* 470 */     boolean debug5 = debug1.isRespawnForced();
/*     */     
/* 472 */     ServerLevel debug6 = this.server.getLevel(debug1.getRespawnDimension());
/*     */ 
/*     */     
/* 475 */     if (debug6 != null && debug3 != null) {
/* 476 */       debug7 = Player.findRespawnPositionAndUseSpawnBlock(debug6, debug3, debug4, debug5, debug2);
/*     */     } else {
/* 478 */       debug7 = Optional.empty();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 483 */     ServerLevel debug9 = (debug6 != null && debug7.isPresent()) ? debug6 : this.server.overworld();
/* 484 */     if (this.server.isDemo()) {
/* 485 */       DemoMode demoMode = new DemoMode(debug9);
/*     */     } else {
/* 487 */       debug8 = new ServerPlayerGameMode(debug9);
/*     */     } 
/*     */     
/* 490 */     ServerPlayer debug10 = new ServerPlayer(this.server, debug9, debug1.getGameProfile(), debug8);
/* 491 */     debug10.connection = debug1.connection;
/* 492 */     debug10.restoreFrom(debug1, debug2);
/* 493 */     debug10.setId(debug1.getId());
/* 494 */     debug10.setMainArm(debug1.getMainArm());
/* 495 */     for (String str : debug1.getTags()) {
/* 496 */       debug10.addTag(str);
/*     */     }
/*     */     
/* 499 */     updatePlayerGameMode(debug10, debug1, debug9);
/*     */     
/* 501 */     boolean debug11 = false;
/* 502 */     if (debug7.isPresent()) {
/* 503 */       float debug15; BlockState blockState = debug9.getBlockState(debug3);
/* 504 */       boolean debug13 = blockState.is(Blocks.RESPAWN_ANCHOR);
/*     */       
/* 506 */       Vec3 debug14 = debug7.get();
/*     */       
/* 508 */       if (blockState.is((Tag)BlockTags.BEDS) || debug13) {
/* 509 */         Vec3 debug16 = Vec3.atBottomCenterOf((Vec3i)debug3).subtract(debug14).normalize();
/* 510 */         debug15 = (float)Mth.wrapDegrees(Mth.atan2(debug16.z, debug16.x) * 57.2957763671875D - 90.0D);
/*     */       } else {
/* 512 */         debug15 = debug4;
/*     */       } 
/* 514 */       debug10.moveTo(debug14.x, debug14.y, debug14.z, debug15, 0.0F);
/*     */       
/* 516 */       debug10.setRespawnPosition(debug9.dimension(), debug3, debug4, debug5, false);
/* 517 */       debug11 = (!debug2 && debug13);
/* 518 */     } else if (debug3 != null) {
/* 519 */       debug10.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.NO_RESPAWN_BLOCK_AVAILABLE, 0.0F));
/*     */     } 
/*     */     
/* 522 */     while (!debug9.noCollision((Entity)debug10) && debug10.getY() < 256.0D) {
/* 523 */       debug10.setPos(debug10.getX(), debug10.getY() + 1.0D, debug10.getZ());
/*     */     }
/*     */     
/* 526 */     LevelData debug12 = debug10.level.getLevelData();
/* 527 */     debug10.connection.send((Packet)new ClientboundRespawnPacket(debug10.level.dimensionType(), debug10.level.dimension(), BiomeManager.obfuscateSeed(debug10.getLevel().getSeed()), debug10.gameMode.getGameModeForPlayer(), debug10.gameMode.getPreviousGameModeForPlayer(), debug10.getLevel().isDebug(), debug10.getLevel().isFlat(), debug2));
/* 528 */     debug10.connection.teleport(debug10.getX(), debug10.getY(), debug10.getZ(), debug10.yRot, debug10.xRot);
/* 529 */     debug10.connection.send((Packet)new ClientboundSetDefaultSpawnPositionPacket(debug9.getSharedSpawnPos(), debug9.getSharedSpawnAngle()));
/* 530 */     debug10.connection.send((Packet)new ClientboundChangeDifficultyPacket(debug12.getDifficulty(), debug12.isDifficultyLocked()));
/* 531 */     debug10.connection.send((Packet)new ClientboundSetExperiencePacket(debug10.experienceProgress, debug10.totalExperience, debug10.experienceLevel));
/* 532 */     sendLevelInfo(debug10, debug9);
/* 533 */     sendPlayerPermissionLevel(debug10);
/*     */     
/* 535 */     debug9.addRespawnedPlayer(debug10);
/* 536 */     this.players.add(debug10);
/* 537 */     this.playersByUUID.put(debug10.getUUID(), debug10);
/*     */     
/* 539 */     debug10.initMenu();
/* 540 */     debug10.setHealth(debug10.getHealth());
/*     */     
/* 542 */     if (debug11)
/*     */     {
/* 544 */       debug10.connection.send((Packet)new ClientboundSoundPacket(SoundEvents.RESPAWN_ANCHOR_DEPLETE, SoundSource.BLOCKS, debug3.getX(), debug3.getY(), debug3.getZ(), 1.0F, 1.0F));
/*     */     }
/*     */     
/* 547 */     return debug10;
/*     */   }
/*     */   
/*     */   public void sendPlayerPermissionLevel(ServerPlayer debug1) {
/* 551 */     GameProfile debug2 = debug1.getGameProfile();
/* 552 */     int debug3 = this.server.getProfilePermissions(debug2);
/* 553 */     sendPlayerPermissionLevel(debug1, debug3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 559 */     if (++this.sendAllPlayerInfoIn > 600) {
/* 560 */       broadcastAll((Packet<?>)new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.UPDATE_LATENCY, this.players));
/* 561 */       this.sendAllPlayerInfoIn = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void broadcastAll(Packet<?> debug1) {
/* 566 */     for (int debug2 = 0; debug2 < this.players.size(); debug2++) {
/* 567 */       ((ServerPlayer)this.players.get(debug2)).connection.send(debug1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void broadcastAll(Packet<?> debug1, ResourceKey<Level> debug2) {
/* 572 */     for (int debug3 = 0; debug3 < this.players.size(); debug3++) {
/* 573 */       ServerPlayer debug4 = this.players.get(debug3);
/* 574 */       if (debug4.level.dimension() == debug2) {
/* 575 */         debug4.connection.send(debug1);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void broadcastToTeam(Player debug1, Component debug2) {
/* 581 */     Team debug3 = debug1.getTeam();
/* 582 */     if (debug3 == null) {
/*     */       return;
/*     */     }
/* 585 */     Collection<String> debug4 = debug3.getPlayers();
/* 586 */     for (String debug6 : debug4) {
/* 587 */       ServerPlayer debug7 = getPlayerByName(debug6);
/* 588 */       if (debug7 == null || debug7 == debug1) {
/*     */         continue;
/*     */       }
/* 591 */       debug7.sendMessage(debug2, debug1.getUUID());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void broadcastToAllExceptTeam(Player debug1, Component debug2) {
/* 596 */     Team debug3 = debug1.getTeam();
/* 597 */     if (debug3 == null) {
/* 598 */       broadcastMessage(debug2, ChatType.SYSTEM, debug1.getUUID());
/*     */       return;
/*     */     } 
/* 601 */     for (int debug4 = 0; debug4 < this.players.size(); debug4++) {
/* 602 */       ServerPlayer debug5 = this.players.get(debug4);
/* 603 */       if (debug5.getTeam() != debug3) {
/* 604 */         debug5.sendMessage(debug2, debug1.getUUID());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public String[] getPlayerNamesArray() {
/* 610 */     String[] debug1 = new String[this.players.size()];
/* 611 */     for (int debug2 = 0; debug2 < this.players.size(); debug2++) {
/* 612 */       debug1[debug2] = ((ServerPlayer)this.players.get(debug2)).getGameProfile().getName();
/*     */     }
/* 614 */     return debug1;
/*     */   }
/*     */   
/*     */   public UserBanList getBans() {
/* 618 */     return this.bans;
/*     */   }
/*     */   
/*     */   public IpBanList getIpBans() {
/* 622 */     return this.ipBans;
/*     */   }
/*     */   
/*     */   public void op(GameProfile debug1) {
/* 626 */     this.ops.add(new ServerOpListEntry(debug1, this.server.getOperatorUserPermissionLevel(), this.ops.canBypassPlayerLimit(debug1)));
/* 627 */     ServerPlayer debug2 = getPlayer(debug1.getId());
/* 628 */     if (debug2 != null) {
/* 629 */       sendPlayerPermissionLevel(debug2);
/*     */     }
/*     */   }
/*     */   
/*     */   public void deop(GameProfile debug1) {
/* 634 */     this.ops.remove(debug1);
/* 635 */     ServerPlayer debug2 = getPlayer(debug1.getId());
/* 636 */     if (debug2 != null) {
/* 637 */       sendPlayerPermissionLevel(debug2);
/*     */     }
/*     */   }
/*     */   
/*     */   private void sendPlayerPermissionLevel(ServerPlayer debug1, int debug2) {
/* 642 */     if (debug1.connection != null) {
/*     */       byte debug3;
/* 644 */       if (debug2 <= 0) {
/* 645 */         debug3 = 24;
/* 646 */       } else if (debug2 >= 4) {
/* 647 */         debug3 = 28;
/*     */       } else {
/* 649 */         debug3 = (byte)(24 + debug2);
/*     */       } 
/* 651 */       debug1.connection.send((Packet)new ClientboundEntityEventPacket((Entity)debug1, debug3));
/*     */     } 
/* 653 */     this.server.getCommands().sendCommands(debug1);
/*     */   }
/*     */   
/*     */   public boolean isWhiteListed(GameProfile debug1) {
/* 657 */     return (!this.doWhiteList || this.ops.contains(debug1) || this.whitelist.contains(debug1));
/*     */   }
/*     */   
/*     */   public boolean isOp(GameProfile debug1) {
/* 661 */     return (this.ops.contains(debug1) || (this.server.isSingleplayerOwner(debug1) && this.server.getWorldData().getAllowCommands()) || this.allowCheatsForAllPlayers);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public ServerPlayer getPlayerByName(String debug1) {
/* 666 */     for (ServerPlayer debug3 : this.players) {
/* 667 */       if (debug3.getGameProfile().getName().equalsIgnoreCase(debug1)) {
/* 668 */         return debug3;
/*     */       }
/*     */     } 
/* 671 */     return null;
/*     */   }
/*     */   
/*     */   public void broadcast(@Nullable Player debug1, double debug2, double debug4, double debug6, double debug8, ResourceKey<Level> debug10, Packet<?> debug11) {
/* 675 */     for (int debug12 = 0; debug12 < this.players.size(); debug12++) {
/* 676 */       ServerPlayer debug13 = this.players.get(debug12);
/* 677 */       if (debug13 != debug1)
/*     */       {
/*     */         
/* 680 */         if (debug13.level.dimension() == debug10) {
/*     */ 
/*     */           
/* 683 */           double debug14 = debug2 - debug13.getX();
/* 684 */           double debug16 = debug4 - debug13.getY();
/* 685 */           double debug18 = debug6 - debug13.getZ();
/* 686 */           if (debug14 * debug14 + debug16 * debug16 + debug18 * debug18 < debug8 * debug8)
/* 687 */             debug13.connection.send(debug11); 
/*     */         }  } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void saveAll() {
/* 693 */     for (int debug1 = 0; debug1 < this.players.size(); debug1++) {
/* 694 */       save(this.players.get(debug1));
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
/*     */   public UserWhiteList getWhiteList() {
/* 707 */     return this.whitelist;
/*     */   }
/*     */   
/*     */   public String[] getWhiteListNames() {
/* 711 */     return this.whitelist.getUserList();
/*     */   }
/*     */   
/*     */   public ServerOpList getOps() {
/* 715 */     return this.ops;
/*     */   }
/*     */   
/*     */   public String[] getOpNames() {
/* 719 */     return this.ops.getUserList();
/*     */   }
/*     */ 
/*     */   
/*     */   public void reloadWhiteList() {}
/*     */ 
/*     */   
/*     */   public void sendLevelInfo(ServerPlayer debug1, ServerLevel debug2) {
/* 727 */     WorldBorder debug3 = this.server.overworld().getWorldBorder();
/* 728 */     debug1.connection.send((Packet)new ClientboundSetBorderPacket(debug3, ClientboundSetBorderPacket.Type.INITIALIZE));
/* 729 */     debug1.connection.send((Packet)new ClientboundSetTimePacket(debug2.getGameTime(), debug2.getDayTime(), debug2.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)));
/* 730 */     debug1.connection.send((Packet)new ClientboundSetDefaultSpawnPositionPacket(debug2.getSharedSpawnPos(), debug2.getSharedSpawnAngle()));
/*     */     
/* 732 */     if (debug2.isRaining()) {
/* 733 */       debug1.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.START_RAINING, 0.0F));
/* 734 */       debug1.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.RAIN_LEVEL_CHANGE, debug2.getRainLevel(1.0F)));
/* 735 */       debug1.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE, debug2.getThunderLevel(1.0F)));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void sendAllPlayerInfo(ServerPlayer debug1) {
/* 740 */     debug1.refreshContainer((AbstractContainerMenu)debug1.inventoryMenu);
/* 741 */     debug1.resetSentInfo();
/* 742 */     debug1.connection.send((Packet)new ClientboundSetCarriedItemPacket(debug1.inventory.selected));
/*     */   }
/*     */   
/*     */   public int getPlayerCount() {
/* 746 */     return this.players.size();
/*     */   }
/*     */   
/*     */   public int getMaxPlayers() {
/* 750 */     return this.maxPlayers;
/*     */   }
/*     */   
/*     */   public boolean isUsingWhitelist() {
/* 754 */     return this.doWhiteList;
/*     */   }
/*     */   
/*     */   public void setUsingWhiteList(boolean debug1) {
/* 758 */     this.doWhiteList = debug1;
/*     */   }
/*     */   
/*     */   public List<ServerPlayer> getPlayersWithAddress(String debug1) {
/* 762 */     List<ServerPlayer> debug2 = Lists.newArrayList();
/*     */     
/* 764 */     for (ServerPlayer debug4 : this.players) {
/* 765 */       if (debug4.getIpAddress().equals(debug1)) {
/* 766 */         debug2.add(debug4);
/*     */       }
/*     */     } 
/*     */     
/* 770 */     return debug2;
/*     */   }
/*     */   
/*     */   public int getViewDistance() {
/* 774 */     return this.viewDistance;
/*     */   }
/*     */   
/*     */   public MinecraftServer getServer() {
/* 778 */     return this.server;
/*     */   }
/*     */   
/*     */   public CompoundTag getSingleplayerData() {
/* 782 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updatePlayerGameMode(ServerPlayer debug1, @Nullable ServerPlayer debug2, ServerLevel debug3) {
/* 791 */     if (debug2 != null) {
/* 792 */       debug1.gameMode.setGameModeForPlayer(debug2.gameMode.getGameModeForPlayer(), debug2.gameMode.getPreviousGameModeForPlayer());
/* 793 */     } else if (this.overrideGameMode != null) {
/* 794 */       debug1.gameMode.setGameModeForPlayer(this.overrideGameMode, GameType.NOT_SET);
/*     */     } 
/* 796 */     debug1.gameMode.updateGameMode(debug3.getServer().getWorldData().getGameType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAll() {
/* 804 */     for (int debug1 = 0; debug1 < this.players.size(); debug1++) {
/* 805 */       ((ServerPlayer)this.players.get(debug1)).connection.disconnect((Component)new TranslatableComponent("multiplayer.disconnect.server_shutdown"));
/*     */     }
/*     */   }
/*     */   
/*     */   public void broadcastMessage(Component debug1, ChatType debug2, UUID debug3) {
/* 810 */     this.server.sendMessage(debug1, debug3);
/* 811 */     broadcastAll((Packet<?>)new ClientboundChatPacket(debug1, debug2, debug3));
/*     */   }
/*     */   
/*     */   public ServerStatsCounter getPlayerStats(Player debug1) {
/* 815 */     UUID debug2 = debug1.getUUID();
/* 816 */     ServerStatsCounter debug3 = (debug2 == null) ? null : this.stats.get(debug2);
/*     */     
/* 818 */     if (debug3 == null) {
/* 819 */       File debug4 = this.server.getWorldPath(LevelResource.PLAYER_STATS_DIR).toFile();
/* 820 */       File debug5 = new File(debug4, debug2 + ".json");
/*     */       
/* 822 */       if (!debug5.exists()) {
/*     */         
/* 824 */         File debug6 = new File(debug4, debug1.getName().getString() + ".json");
/* 825 */         if (debug6.exists() && debug6.isFile()) {
/* 826 */           debug6.renameTo(debug5);
/*     */         }
/*     */       } 
/*     */       
/* 830 */       debug3 = new ServerStatsCounter(this.server, debug5);
/* 831 */       this.stats.put(debug2, debug3);
/*     */     } 
/*     */     
/* 834 */     return debug3;
/*     */   }
/*     */   
/*     */   public PlayerAdvancements getPlayerAdvancements(ServerPlayer debug1) {
/* 838 */     UUID debug2 = debug1.getUUID();
/* 839 */     PlayerAdvancements debug3 = this.advancements.get(debug2);
/* 840 */     if (debug3 == null) {
/* 841 */       File debug4 = this.server.getWorldPath(LevelResource.PLAYER_ADVANCEMENTS_DIR).toFile();
/* 842 */       File debug5 = new File(debug4, debug2 + ".json");
/* 843 */       debug3 = new PlayerAdvancements(this.server.getFixerUpper(), this, this.server.getAdvancements(), debug5, debug1);
/* 844 */       this.advancements.put(debug2, debug3);
/*     */     } 
/*     */     
/* 847 */     debug3.setPlayer(debug1);
/*     */     
/* 849 */     return debug3;
/*     */   }
/*     */   
/*     */   public void setViewDistance(int debug1) {
/* 853 */     this.viewDistance = debug1;
/* 854 */     broadcastAll((Packet<?>)new ClientboundSetChunkCacheRadiusPacket(debug1));
/*     */     
/* 856 */     for (ServerLevel debug3 : this.server.getAllLevels()) {
/* 857 */       if (debug3 != null) {
/* 858 */         debug3.getChunkSource().setViewDistance(debug1);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<ServerPlayer> getPlayers() {
/* 864 */     return this.players;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public ServerPlayer getPlayer(UUID debug1) {
/* 869 */     return this.playersByUUID.get(debug1);
/*     */   }
/*     */   
/*     */   public boolean canBypassPlayerLimit(GameProfile debug1) {
/* 873 */     return false;
/*     */   }
/*     */   
/*     */   public void reloadResources() {
/* 877 */     for (PlayerAdvancements debug2 : this.advancements.values()) {
/* 878 */       debug2.reload(this.server.getAdvancements());
/*     */     }
/* 880 */     broadcastAll((Packet<?>)new ClientboundUpdateTagsPacket(this.server.getTags()));
/* 881 */     ClientboundUpdateRecipesPacket debug1 = new ClientboundUpdateRecipesPacket(this.server.getRecipeManager().getRecipes());
/* 882 */     for (ServerPlayer debug3 : this.players) {
/* 883 */       debug3.connection.send((Packet)debug1);
/* 884 */       debug3.getRecipeBook().sendInitialRecipeBook(debug3);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isAllowCheatsForAllPlayers() {
/* 889 */     return this.allowCheatsForAllPlayers;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\players\PlayerList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */