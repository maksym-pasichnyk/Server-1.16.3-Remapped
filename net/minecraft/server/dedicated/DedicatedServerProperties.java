/*    */ package net.minecraft.server.dedicated;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.util.Properties;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.level.GameType;
/*    */ import net.minecraft.world.level.levelgen.WorldGenSettings;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DedicatedServerProperties
/*    */   extends Settings<DedicatedServerProperties>
/*    */ {
/* 17 */   public final boolean onlineMode = get("online-mode", true);
/* 18 */   public final boolean preventProxyConnections = get("prevent-proxy-connections", false);
/* 19 */   public final String serverIp = get("server-ip", "");
/* 20 */   public final boolean spawnAnimals = get("spawn-animals", true);
/* 21 */   public final boolean spawnNpcs = get("spawn-npcs", true);
/* 22 */   public final boolean pvp = get("pvp", true);
/* 23 */   public final boolean allowFlight = get("allow-flight", false);
/* 24 */   public final String resourcePack = get("resource-pack", "");
/* 25 */   public final String motd = get("motd", "A Minecraft Server");
/* 26 */   public final boolean forceGameMode = get("force-gamemode", false);
/* 27 */   public final boolean enforceWhitelist = get("enforce-whitelist", false);
/* 28 */   public final Difficulty difficulty = (Difficulty)get("difficulty", dispatchNumberOrString(Difficulty::byId, Difficulty::byName), Difficulty::getKey, Difficulty.EASY);
/* 29 */   public final GameType gamemode = (GameType)get("gamemode", dispatchNumberOrString(GameType::byId, GameType::byName), GameType::getName, GameType.SURVIVAL);
/* 30 */   public final String levelName = get("level-name", "world");
/* 31 */   public final int serverPort = get("server-port", 25565);
/*    */   
/*    */   public final int maxBuildHeight;
/*    */   
/*    */   public final Boolean announcePlayerAchievements;
/*    */   
/*    */   public final boolean enableQuery;
/*    */   public final int queryPort;
/*    */   public final boolean enableRcon;
/*    */   public final int rconPort;
/*    */   public final String rconPassword;
/*    */   public final String resourcePackHash;
/*    */   public final String resourcePackSha1;
/*    */   public final boolean hardcore;
/*    */   public final boolean allowNether;
/*    */   public final boolean spawnMonsters;
/*    */   public final boolean snooperEnabled;
/*    */   public final boolean useNativeTransport;
/*    */   public final boolean enableCommandBlock;
/*    */   public final int spawnProtection;
/*    */   public final int opPermissionLevel;
/*    */   public final int functionPermissionLevel;
/*    */   public final long maxTickTime;
/*    */   public final int rateLimitPacketsPerSecond;
/*    */   public final int viewDistance;
/*    */   public final int maxPlayers;
/*    */   public final int networkCompressionThreshold;
/*    */   public final boolean broadcastRconToOps;
/*    */   public final boolean broadcastConsoleToOps;
/*    */   public final int maxWorldSize;
/*    */   public final boolean syncChunkWrites;
/*    */   public final boolean enableJmxMonitoring;
/*    */   public final boolean enableStatus;
/*    */   public final int entityBroadcastRangePercentage;
/*    */   public final Settings<DedicatedServerProperties>.MutableValue<Integer> playerIdleTimeout;
/*    */   public final Settings<DedicatedServerProperties>.MutableValue<Boolean> whiteList;
/*    */   public final WorldGenSettings worldGenSettings;
/*    */   
/*    */   public DedicatedServerProperties(Properties debug1, RegistryAccess debug2) {
/* 70 */     super(debug1); this.maxBuildHeight = get("max-build-height", debug0 -> Integer.valueOf(Mth.clamp((debug0.intValue() + 8) / 16 * 16, 64, 256)), 256); this.announcePlayerAchievements = getLegacyBoolean("announce-player-achievements"); this.enableQuery = get("enable-query", false); this.queryPort = get("query.port", 25565); this.enableRcon = get("enable-rcon", false); this.rconPort = get("rcon.port", 25575); this.rconPassword = get("rcon.password", ""); this.resourcePackHash = getLegacyString("resource-pack-hash"); this.resourcePackSha1 = get("resource-pack-sha1", ""); this.hardcore = get("hardcore", false); this.allowNether = get("allow-nether", true); this.spawnMonsters = get("spawn-monsters", true); if (get("snooper-enabled", true)); this.snooperEnabled = false; this.useNativeTransport = get("use-native-transport", true); this.enableCommandBlock = get("enable-command-block", false); this.spawnProtection = get("spawn-protection", 16); this.opPermissionLevel = get("op-permission-level", 4); this.functionPermissionLevel = get("function-permission-level", 2); this.maxTickTime = get("max-tick-time", TimeUnit.MINUTES.toMillis(1L)); this.rateLimitPacketsPerSecond = get("rate-limit", 0); this.viewDistance = get("view-distance", 10); this.maxPlayers = get("max-players", 20); this.networkCompressionThreshold = get("network-compression-threshold", 256); this.broadcastRconToOps = get("broadcast-rcon-to-ops", true); this.broadcastConsoleToOps = get("broadcast-console-to-ops", true); this.maxWorldSize = get("max-world-size", debug0 -> Integer.valueOf(Mth.clamp(debug0.intValue(), 1, 29999984)), 29999984); this.syncChunkWrites = get("sync-chunk-writes", true); this.enableJmxMonitoring = get("enable-jmx-monitoring", false); this.enableStatus = get("enable-status", true); this.entityBroadcastRangePercentage = get("entity-broadcast-range-percentage", debug0 -> Integer.valueOf(Mth.clamp(debug0.intValue(), 10, 1000)), 100); this.playerIdleTimeout = getMutable("player-idle-timeout", 0); this.whiteList = getMutable("white-list", false);
/* 71 */     this.worldGenSettings = WorldGenSettings.create(debug2, debug1);
/*    */   }
/*    */   
/*    */   public static DedicatedServerProperties fromFile(RegistryAccess debug0, Path debug1) {
/* 75 */     return new DedicatedServerProperties(loadFromFile(debug1), debug0);
/*    */   }
/*    */ 
/*    */   
/*    */   protected DedicatedServerProperties reload(RegistryAccess debug1, Properties debug2) {
/* 80 */     return new DedicatedServerProperties(debug2, debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\dedicated\DedicatedServerProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */