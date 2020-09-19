/*     */ package net.minecraft.server.dedicated;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.GameProfileRepository;
/*     */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Proxy;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.DefaultUncaughtExceptionHandler;
/*     */ import net.minecraft.DefaultUncaughtExceptionHandlerWithName;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.server.ConsoleInput;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.ServerInterface;
/*     */ import net.minecraft.server.ServerResources;
/*     */ import net.minecraft.server.gui.MinecraftServerGui;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.players.GameProfileCache;
/*     */ import net.minecraft.server.players.OldUsersConverter;
/*     */ import net.minecraft.server.players.PlayerList;
/*     */ import net.minecraft.server.rcon.RconConsoleSource;
/*     */ import net.minecraft.server.rcon.thread.QueryThreadGs4;
/*     */ import net.minecraft.server.rcon.thread.RconThread;
/*     */ import net.minecraft.util.Crypt;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.monitoring.jmx.MinecraftServerStatistics;
/*     */ import net.minecraft.world.Snooper;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.CreativeModeTab;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.SkullBlockEntity;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.WorldData;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class DedicatedServer extends MinecraftServer implements ServerInterface {
/*  62 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */   
/*  65 */   private static final Pattern SHA1 = Pattern.compile("^[a-fA-F0-9]{40}$");
/*     */   
/*  67 */   private final List<ConsoleInput> consoleInput = Collections.synchronizedList(Lists.newArrayList());
/*     */   private QueryThreadGs4 queryThreadGs4;
/*     */   private final RconConsoleSource rconConsoleSource;
/*     */   private RconThread rconThread;
/*     */   private final DedicatedServerSettings settings;
/*     */   @Nullable
/*     */   private MinecraftServerGui gui;
/*     */   
/*     */   public DedicatedServer(Thread debug1, RegistryAccess.RegistryHolder debug2, LevelStorageSource.LevelStorageAccess debug3, PackRepository debug4, ServerResources debug5, WorldData debug6, DedicatedServerSettings debug7, DataFixer debug8, MinecraftSessionService debug9, GameProfileRepository debug10, GameProfileCache debug11, ChunkProgressListenerFactory debug12) {
/*  76 */     super(debug1, debug2, debug3, debug6, debug4, Proxy.NO_PROXY, debug8, debug5, debug9, debug10, debug11, debug12);
/*  77 */     this.settings = debug7;
/*  78 */     this.rconConsoleSource = new RconConsoleSource(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean initServer() throws IOException {
/*  83 */     Thread debug1 = new Thread("Server console handler")
/*     */       {
/*     */         public void run() {
/*  86 */           BufferedReader debug1 = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
/*     */           try {
/*     */             String debug2;
/*  89 */             while (!DedicatedServer.this.isStopped() && DedicatedServer.this.isRunning() && (debug2 = debug1.readLine()) != null) {
/*  90 */               DedicatedServer.this.handleConsoleInput(debug2, DedicatedServer.this.createCommandSourceStack());
/*     */             }
/*  92 */           } catch (IOException debug3) {
/*  93 */             DedicatedServer.LOGGER.error("Exception handling console input", debug3);
/*     */           } 
/*     */         }
/*     */       };
/*  97 */     debug1.setDaemon(true);
/*  98 */     debug1.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(LOGGER));
/*  99 */     debug1.start();
/*     */     
/* 101 */     LOGGER.info("Starting minecraft server version " + SharedConstants.getCurrentVersion().getName());
/*     */     
/* 103 */     if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
/* 104 */       LOGGER.warn("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
/*     */     }
/*     */     
/* 107 */     LOGGER.info("Loading properties");
/* 108 */     DedicatedServerProperties debug2 = this.settings.getProperties();
/*     */     
/* 110 */     if (isSingleplayer()) {
/* 111 */       setLocalIp("127.0.0.1");
/*     */     } else {
/* 113 */       setUsesAuthentication(debug2.onlineMode);
/* 114 */       setPreventProxyConnections(debug2.preventProxyConnections);
/* 115 */       setLocalIp(debug2.serverIp);
/*     */     } 
/*     */     
/* 118 */     setPvpAllowed(debug2.pvp);
/* 119 */     setFlightAllowed(debug2.allowFlight);
/* 120 */     setResourcePack(debug2.resourcePack, getPackHash());
/* 121 */     setMotd(debug2.motd);
/* 122 */     setForceGameType(debug2.forceGameMode);
/* 123 */     super.setPlayerIdleTimeout(((Integer)debug2.playerIdleTimeout.get()).intValue());
/* 124 */     setEnforceWhitelist(debug2.enforceWhitelist);
/*     */     
/* 126 */     this.worldData.setGameType(debug2.gamemode);
/* 127 */     LOGGER.info("Default game type: {}", debug2.gamemode);
/*     */     
/* 129 */     InetAddress debug3 = null;
/* 130 */     if (!getLocalIp().isEmpty()) {
/* 131 */       debug3 = InetAddress.getByName(getLocalIp());
/*     */     }
/* 133 */     if (getPort() < 0) {
/* 134 */       setPort(debug2.serverPort);
/*     */     }
/*     */     
/* 137 */     LOGGER.info("Generating keypair");
/* 138 */     setKeyPair(Crypt.generateKeyPair());
/*     */     
/* 140 */     LOGGER.info("Starting Minecraft server on {}:{}", getLocalIp().isEmpty() ? "*" : getLocalIp(), Integer.valueOf(getPort()));
/*     */     try {
/* 142 */       getConnection().startTcpServerListener(debug3, getPort());
/* 143 */     } catch (IOException iOException) {
/* 144 */       LOGGER.warn("**** FAILED TO BIND TO PORT!");
/* 145 */       LOGGER.warn("The exception was: {}", iOException.toString());
/* 146 */       LOGGER.warn("Perhaps a server is already running on that port?");
/* 147 */       return false;
/*     */     } 
/*     */     
/* 150 */     if (!usesAuthentication()) {
/* 151 */       LOGGER.warn("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
/* 152 */       LOGGER.warn("The server will make no attempt to authenticate usernames. Beware.");
/* 153 */       LOGGER.warn("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
/* 154 */       LOGGER.warn("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
/*     */     } 
/*     */     
/* 157 */     if (convertOldUsers()) {
/* 158 */       getProfileCache().save();
/*     */     }
/* 160 */     if (!OldUsersConverter.serverReadyAfterUserconversion(this)) {
/* 161 */       return false;
/*     */     }
/*     */     
/* 164 */     setPlayerList(new DedicatedPlayerList(this, this.registryHolder, this.playerDataStorage));
/*     */     
/* 166 */     long debug4 = Util.getNanos();
/* 167 */     setMaxBuildHeight(debug2.maxBuildHeight);
/*     */     
/* 169 */     SkullBlockEntity.setProfileCache(getProfileCache());
/* 170 */     SkullBlockEntity.setSessionService(getSessionService());
/* 171 */     GameProfileCache.setUsesAuthentication(usesAuthentication());
/*     */     
/* 173 */     LOGGER.info("Preparing level \"{}\"", getLevelIdName());
/* 174 */     loadLevel();
/* 175 */     long debug6 = Util.getNanos() - debug4;
/* 176 */     String debug8 = String.format(Locale.ROOT, "%.3fs", new Object[] { Double.valueOf(debug6 / 1.0E9D) });
/* 177 */     LOGGER.info("Done ({})! For help, type \"help\"", debug8);
/*     */ 
/*     */     
/* 180 */     if (debug2.announcePlayerAchievements != null) {
/* 181 */       ((GameRules.BooleanValue)getGameRules().getRule(GameRules.RULE_ANNOUNCE_ADVANCEMENTS)).set(debug2.announcePlayerAchievements.booleanValue(), this);
/*     */     }
/*     */     
/* 184 */     if (debug2.enableQuery) {
/* 185 */       LOGGER.info("Starting GS4 status listener");
/* 186 */       this.queryThreadGs4 = QueryThreadGs4.create(this);
/*     */     } 
/* 188 */     if (debug2.enableRcon) {
/* 189 */       LOGGER.info("Starting remote control listener");
/* 190 */       this.rconThread = RconThread.create(this);
/*     */     } 
/*     */     
/* 193 */     if (getMaxTickLength() > 0L) {
/* 194 */       Thread debug9 = new Thread(new ServerWatchdog(this));
/* 195 */       debug9.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandlerWithName(LOGGER));
/* 196 */       debug9.setName("Server Watchdog");
/* 197 */       debug9.setDaemon(true);
/* 198 */       debug9.start();
/*     */     } 
/*     */ 
/*     */     
/* 202 */     Items.AIR.fillItemCategory(CreativeModeTab.TAB_SEARCH, NonNullList.create());
/*     */     
/* 204 */     if (debug2.enableJmxMonitoring) {
/* 205 */       MinecraftServerStatistics.registerJmxMonitoring(this);
/*     */     }
/*     */     
/* 208 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSpawningAnimals() {
/* 213 */     return ((getProperties()).spawnAnimals && super.isSpawningAnimals());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSpawningMonsters() {
/* 218 */     return ((this.settings.getProperties()).spawnMonsters && super.isSpawningMonsters());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean areNpcsEnabled() {
/* 223 */     return ((this.settings.getProperties()).spawnNpcs && super.areNpcsEnabled());
/*     */   }
/*     */   public String getPackHash() {
/*     */     String debug2;
/* 227 */     DedicatedServerProperties debug1 = this.settings.getProperties();
/*     */     
/* 229 */     if (!debug1.resourcePackSha1.isEmpty()) {
/* 230 */       debug2 = debug1.resourcePackSha1;
/* 231 */       if (!Strings.isNullOrEmpty(debug1.resourcePackHash)) {
/* 232 */         LOGGER.warn("resource-pack-hash is deprecated and found along side resource-pack-sha1. resource-pack-hash will be ignored.");
/*     */       }
/* 234 */     } else if (!Strings.isNullOrEmpty(debug1.resourcePackHash)) {
/* 235 */       LOGGER.warn("resource-pack-hash is deprecated. Please use resource-pack-sha1 instead.");
/* 236 */       debug2 = debug1.resourcePackHash;
/*     */     } else {
/* 238 */       debug2 = "";
/*     */     } 
/*     */     
/* 241 */     if (!debug2.isEmpty() && !SHA1.matcher(debug2).matches()) {
/* 242 */       LOGGER.warn("Invalid sha1 for ressource-pack-sha1");
/*     */     }
/* 244 */     if (!debug1.resourcePack.isEmpty() && debug2.isEmpty()) {
/* 245 */       LOGGER.warn("You specified a resource pack without providing a sha1 hash. Pack will be updated on the client only if you change the name of the pack.");
/*     */     }
/*     */     
/* 248 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public DedicatedServerProperties getProperties() {
/* 253 */     return this.settings.getProperties();
/*     */   }
/*     */ 
/*     */   
/*     */   public void forceDifficulty() {
/* 258 */     setDifficulty((getProperties()).difficulty, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isHardcore() {
/* 263 */     return (getProperties()).hardcore;
/*     */   }
/*     */ 
/*     */   
/*     */   public CrashReport fillReport(CrashReport debug1) {
/* 268 */     debug1 = super.fillReport(debug1);
/*     */     
/* 270 */     debug1.getSystemDetails().setDetail("Is Modded", () -> (String)getModdedStatus().orElse("Unknown (can't tell)"));
/* 271 */     debug1.getSystemDetails().setDetail("Type", () -> "Dedicated Server (map_server.txt)");
/*     */     
/* 273 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<String> getModdedStatus() {
/* 278 */     String debug1 = getServerModName();
/* 279 */     if (!"vanilla".equals(debug1)) {
/* 280 */       return Optional.of("Definitely; Server brand changed to '" + debug1 + "'");
/*     */     }
/*     */     
/* 283 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onServerExit() {
/* 288 */     if (this.gui != null) {
/* 289 */       this.gui.close();
/*     */     }
/*     */     
/* 292 */     if (this.rconThread != null) {
/* 293 */       this.rconThread.stop();
/*     */     }
/*     */     
/* 296 */     if (this.queryThreadGs4 != null) {
/* 297 */       this.queryThreadGs4.stop();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void tickChildren(BooleanSupplier debug1) {
/* 303 */     super.tickChildren(debug1);
/* 304 */     handleConsoleInputs();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNetherEnabled() {
/* 309 */     return (getProperties()).allowNether;
/*     */   }
/*     */ 
/*     */   
/*     */   public void populateSnooper(Snooper debug1) {
/* 314 */     debug1.setDynamicData("whitelist_enabled", Boolean.valueOf(getPlayerList().isUsingWhitelist()));
/* 315 */     debug1.setDynamicData("whitelist_count", Integer.valueOf((getPlayerList().getWhiteListNames()).length));
/* 316 */     super.populateSnooper(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleConsoleInput(String debug1, CommandSourceStack debug2) {
/* 325 */     this.consoleInput.add(new ConsoleInput(debug1, debug2));
/*     */   }
/*     */   
/*     */   public void handleConsoleInputs() {
/* 329 */     while (!this.consoleInput.isEmpty()) {
/* 330 */       ConsoleInput debug1 = this.consoleInput.remove(0);
/* 331 */       getCommands().performCommand(debug1.source, debug1.msg);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDedicatedServer() {
/* 337 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRateLimitPacketsPerSecond() {
/* 342 */     return (getProperties()).rateLimitPacketsPerSecond;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEpollEnabled() {
/* 347 */     return (getProperties()).useNativeTransport;
/*     */   }
/*     */ 
/*     */   
/*     */   public DedicatedPlayerList getPlayerList() {
/* 352 */     return (DedicatedPlayerList)super.getPlayerList();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPublished() {
/* 357 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getServerIp() {
/* 362 */     return getLocalIp();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getServerPort() {
/* 367 */     return getPort();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getServerName() {
/* 372 */     return getMotd();
/*     */   }
/*     */   
/*     */   public void showGui() {
/* 376 */     if (this.gui == null) {
/* 377 */       this.gui = MinecraftServerGui.showFrameFor(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasGui() {
/* 383 */     return (this.gui != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean publishServer(GameType debug1, boolean debug2, int debug3) {
/* 388 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCommandBlockEnabled() {
/* 393 */     return (getProperties()).enableCommandBlock;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSpawnProtectionRadius() {
/* 398 */     return (getProperties()).spawnProtection;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUnderSpawnProtection(ServerLevel debug1, BlockPos debug2, Player debug3) {
/* 403 */     if (debug1.dimension() != Level.OVERWORLD) {
/* 404 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 408 */     if (getPlayerList().getOps().isEmpty()) {
/* 409 */       return false;
/*     */     }
/* 411 */     if (getPlayerList().isOp(debug3.getGameProfile())) {
/* 412 */       return false;
/*     */     }
/* 414 */     if (getSpawnProtectionRadius() <= 0) {
/* 415 */       return false;
/*     */     }
/*     */     
/* 418 */     BlockPos debug4 = debug1.getSharedSpawnPos();
/* 419 */     int debug5 = Mth.abs(debug2.getX() - debug4.getX());
/* 420 */     int debug6 = Mth.abs(debug2.getZ() - debug4.getZ());
/* 421 */     int debug7 = Math.max(debug5, debug6);
/*     */     
/* 423 */     return (debug7 <= getSpawnProtectionRadius());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean repliesToStatus() {
/* 428 */     return (getProperties()).enableStatus;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOperatorUserPermissionLevel() {
/* 433 */     return (getProperties()).opPermissionLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFunctionCompilationLevel() {
/* 438 */     return (getProperties()).functionPermissionLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlayerIdleTimeout(int debug1) {
/* 443 */     super.setPlayerIdleTimeout(debug1);
/* 444 */     this.settings.update(debug2 -> (DedicatedServerProperties)debug2.playerIdleTimeout.update(registryAccess(), Integer.valueOf(debug1)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRconBroadcast() {
/* 449 */     return (getProperties()).broadcastRconToOps;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldInformAdmins() {
/* 454 */     return (getProperties()).broadcastConsoleToOps;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAbsoluteMaxWorldSize() {
/* 459 */     return (getProperties()).maxWorldSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCompressionThreshold() {
/* 464 */     return (getProperties()).networkCompressionThreshold;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean convertOldUsers() {
/* 470 */     boolean debug2 = false;
/* 471 */     int debug1 = 0;
/* 472 */     while (!debug2 && debug1 <= 2) {
/* 473 */       if (debug1 > 0) {
/* 474 */         LOGGER.warn("Encountered a problem while converting the user banlist, retrying in a few seconds");
/* 475 */         waitForRetry();
/*     */       } 
/* 477 */       debug2 = OldUsersConverter.convertUserBanlist(this);
/* 478 */       debug1++;
/*     */     } 
/*     */     
/* 481 */     boolean debug3 = false;
/* 482 */     debug1 = 0;
/* 483 */     while (!debug3 && debug1 <= 2) {
/* 484 */       if (debug1 > 0) {
/* 485 */         LOGGER.warn("Encountered a problem while converting the ip banlist, retrying in a few seconds");
/* 486 */         waitForRetry();
/*     */       } 
/* 488 */       debug3 = OldUsersConverter.convertIpBanlist(this);
/* 489 */       debug1++;
/*     */     } 
/*     */     
/* 492 */     boolean debug4 = false;
/* 493 */     debug1 = 0;
/* 494 */     while (!debug4 && debug1 <= 2) {
/* 495 */       if (debug1 > 0) {
/* 496 */         LOGGER.warn("Encountered a problem while converting the op list, retrying in a few seconds");
/* 497 */         waitForRetry();
/*     */       } 
/* 499 */       debug4 = OldUsersConverter.convertOpsList(this);
/* 500 */       debug1++;
/*     */     } 
/*     */     
/* 503 */     boolean debug5 = false;
/* 504 */     debug1 = 0;
/* 505 */     while (!debug5 && debug1 <= 2) {
/* 506 */       if (debug1 > 0) {
/* 507 */         LOGGER.warn("Encountered a problem while converting the whitelist, retrying in a few seconds");
/* 508 */         waitForRetry();
/*     */       } 
/* 510 */       debug5 = OldUsersConverter.convertWhiteList(this);
/* 511 */       debug1++;
/*     */     } 
/*     */     
/* 514 */     boolean debug6 = false;
/* 515 */     debug1 = 0;
/* 516 */     while (!debug6 && debug1 <= 2) {
/* 517 */       if (debug1 > 0) {
/* 518 */         LOGGER.warn("Encountered a problem while converting the player save files, retrying in a few seconds");
/* 519 */         waitForRetry();
/*     */       } 
/* 521 */       debug6 = OldUsersConverter.convertPlayers(this);
/* 522 */       debug1++;
/*     */     } 
/*     */     
/* 525 */     return (debug2 || debug3 || debug4 || debug5 || debug6);
/*     */   }
/*     */   
/*     */   private void waitForRetry() {
/*     */     try {
/* 530 */       Thread.sleep(5000L);
/* 531 */     } catch (InterruptedException debug1) {
/*     */       return;
/*     */     } 
/*     */   }
/*     */   
/*     */   public long getMaxTickLength() {
/* 537 */     return (getProperties()).maxTickTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPluginNames() {
/* 542 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public String runCommand(String debug1) {
/* 547 */     this.rconConsoleSource.prepareForCommand();
/* 548 */     executeBlocking(() -> getCommands().performCommand(this.rconConsoleSource.createCommandSourceStack(), debug1));
/* 549 */     return this.rconConsoleSource.getCommandResponse();
/*     */   }
/*     */   
/*     */   public void storeUsingWhiteList(boolean debug1) {
/* 553 */     this.settings.update(debug2 -> (DedicatedServerProperties)debug2.whiteList.update(registryAccess(), Boolean.valueOf(debug1)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void stopServer() {
/* 558 */     super.stopServer();
/* 559 */     Util.shutdownExecutors();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleplayerOwner(GameProfile debug1) {
/* 564 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getScaledTrackingDistance(int debug1) {
/* 569 */     return (getProperties()).entityBroadcastRangePercentage * debug1 / 100;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLevelIdName() {
/* 574 */     return this.storageSource.getLevelId();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean forceSynchronousWrites() {
/* 579 */     return (this.settings.getProperties()).syncChunkWrites;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\dedicated\DedicatedServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */