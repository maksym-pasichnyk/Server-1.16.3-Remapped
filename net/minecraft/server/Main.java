/*     */ package net.minecraft.server;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.authlib.GameProfileRepository;
/*     */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*     */ import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.io.File;
/*     */ import java.net.Proxy;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import joptsimple.AbstractOptionSpec;
/*     */ import joptsimple.ArgumentAcceptingOptionSpec;
/*     */ import joptsimple.OptionParser;
/*     */ import joptsimple.OptionSet;
/*     */ import joptsimple.OptionSpec;
/*     */ import joptsimple.OptionSpecBuilder;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.DefaultUncaughtExceptionHandler;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.RegistryReadOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.dedicated.DedicatedServer;
/*     */ import net.minecraft.server.dedicated.DedicatedServerProperties;
/*     */ import net.minecraft.server.dedicated.DedicatedServerSettings;
/*     */ import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
/*     */ import net.minecraft.server.packs.repository.FolderRepositorySource;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.packs.repository.PackSource;
/*     */ import net.minecraft.server.packs.repository.RepositorySource;
/*     */ import net.minecraft.server.packs.repository.ServerPacksSource;
/*     */ import net.minecraft.server.players.GameProfileCache;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.datafix.DataFixers;
/*     */ import net.minecraft.util.worldupdate.WorldUpgrader;
/*     */ import net.minecraft.world.level.DataPackConfig;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import net.minecraft.world.level.levelgen.WorldGenSettings;
/*     */ import net.minecraft.world.level.storage.LevelResource;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.PrimaryLevelData;
/*     */ import net.minecraft.world.level.storage.WorldData;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class Main {
/*  58 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */   
/*     */   public static void main(String[] debug0) {
/*  62 */     OptionParser debug1 = new OptionParser();
/*  63 */     OptionSpecBuilder optionSpecBuilder1 = debug1.accepts("nogui");
/*  64 */     OptionSpecBuilder optionSpecBuilder2 = debug1.accepts("initSettings", "Initializes 'server.properties' and 'eula.txt', then quits");
/*  65 */     OptionSpecBuilder optionSpecBuilder3 = debug1.accepts("demo");
/*  66 */     OptionSpecBuilder optionSpecBuilder4 = debug1.accepts("bonusChest");
/*  67 */     OptionSpecBuilder optionSpecBuilder5 = debug1.accepts("forceUpgrade");
/*  68 */     OptionSpecBuilder optionSpecBuilder6 = debug1.accepts("eraseCache");
/*  69 */     OptionSpecBuilder optionSpecBuilder7 = debug1.accepts("safeMode", "Loads level with vanilla datapack only");
/*  70 */     AbstractOptionSpec abstractOptionSpec = debug1.accepts("help").forHelp();
/*  71 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec1 = debug1.accepts("singleplayer").withRequiredArg();
/*  72 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec2 = debug1.accepts("universe").withRequiredArg().defaultsTo(".", (Object[])new String[0]);
/*  73 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec3 = debug1.accepts("world").withRequiredArg();
/*  74 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec4 = debug1.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(-1), (Object[])new Integer[0]);
/*  75 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec5 = debug1.accepts("serverId").withRequiredArg();
/*  76 */     NonOptionArgumentSpec nonOptionArgumentSpec = debug1.nonOptions(); try {
/*     */       ServerResources debug35;
/*     */       PrimaryLevelData primaryLevelData1;
/*  79 */       OptionSet debug16 = debug1.parse(debug0);
/*     */       
/*  81 */       if (debug16.has((OptionSpec)abstractOptionSpec)) {
/*  82 */         debug1.printHelpOn(System.err);
/*     */         
/*     */         return;
/*     */       } 
/*  86 */       CrashReport.preload();
/*     */       
/*  88 */       Bootstrap.bootStrap();
/*  89 */       Bootstrap.validate();
/*     */       
/*  91 */       Util.startTimerHackThread();
/*     */       
/*  93 */       RegistryAccess.RegistryHolder debug17 = RegistryAccess.builtin();
/*     */       
/*  95 */       Path debug18 = Paths.get("server.properties", new String[0]);
/*  96 */       DedicatedServerSettings debug19 = new DedicatedServerSettings((RegistryAccess)debug17, debug18);
/*  97 */       debug19.forceSave();
/*     */       
/*  99 */       Path debug20 = Paths.get("eula.txt", new String[0]);
/* 100 */       Eula debug21 = new Eula(debug20);
/*     */       
/* 102 */       if (debug16.has((OptionSpec)optionSpecBuilder2)) {
/* 103 */         LOGGER.info("Initialized '{}' and '{}'", debug18.toAbsolutePath(), debug20.toAbsolutePath());
/*     */         
/*     */         return;
/*     */       } 
/* 107 */       if (!debug21.hasAgreedToEULA()) {
/* 108 */         LOGGER.info("You need to agree to the EULA in order to run the server. Go to eula.txt for more info.");
/*     */         
/*     */         return;
/*     */       } 
/* 112 */       File debug22 = new File((String)debug16.valueOf((OptionSpec)argumentAcceptingOptionSpec2));
/* 113 */       YggdrasilAuthenticationService debug23 = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
/* 114 */       MinecraftSessionService debug24 = debug23.createMinecraftSessionService();
/* 115 */       GameProfileRepository debug25 = debug23.createProfileRepository();
/* 116 */       GameProfileCache debug26 = new GameProfileCache(debug25, new File(debug22, MinecraftServer.USERID_CACHE_FILE.getName()));
/* 117 */       String debug27 = Optional.<Object>ofNullable(debug16.valueOf((OptionSpec)argumentAcceptingOptionSpec3)).orElse((debug19.getProperties()).levelName);
/* 118 */       LevelStorageSource debug28 = LevelStorageSource.createDefault(debug22.toPath());
/* 119 */       LevelStorageSource.LevelStorageAccess debug29 = debug28.createAccess(debug27);
/*     */       
/* 121 */       MinecraftServer.convertFromRegionFormatIfNeeded(debug29);
/*     */       
/* 123 */       DataPackConfig debug30 = debug29.getDataPacks();
/*     */       
/* 125 */       boolean debug31 = debug16.has((OptionSpec)optionSpecBuilder7);
/* 126 */       if (debug31) {
/* 127 */         LOGGER.warn("Safe mode active, only vanilla datapack will be loaded");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 132 */       PackRepository debug32 = new PackRepository(new RepositorySource[] { (RepositorySource)new ServerPacksSource(), (RepositorySource)new FolderRepositorySource(debug29.getLevelPath(LevelResource.DATAPACK_DIR).toFile(), PackSource.WORLD) });
/*     */       
/* 134 */       DataPackConfig debug33 = MinecraftServer.configurePackRepository(debug32, (debug30 == null) ? DataPackConfig.DEFAULT : debug30, debug31);
/* 135 */       CompletableFuture<ServerResources> debug34 = ServerResources.loadResources(debug32.openAllSelected(), Commands.CommandSelection.DEDICATED, (debug19.getProperties()).functionPermissionLevel, Util.backgroundExecutor(), Runnable::run);
/*     */ 
/*     */       
/*     */       try {
/* 139 */         debug35 = debug34.get();
/* 140 */       } catch (Exception exception) {
/* 141 */         LOGGER.warn("Failed to load datapacks, can't proceed with server load. You can either fix your datapacks or reset to vanilla with --safeMode", exception);
/* 142 */         debug32.close();
/*     */         return;
/*     */       } 
/* 145 */       debug35.updateGlobals();
/*     */       
/* 147 */       RegistryReadOps<Tag> debug36 = RegistryReadOps.create((DynamicOps)NbtOps.INSTANCE, debug35.getResourceManager(), debug17);
/*     */       
/* 149 */       WorldData debug37 = debug29.getDataTag((DynamicOps)debug36, debug33);
/*     */       
/* 151 */       if (debug37 == null) {
/*     */         LevelSettings debug38;
/*     */         WorldGenSettings worldGenSettings;
/* 154 */         if (debug16.has((OptionSpec)optionSpecBuilder3)) {
/* 155 */           debug38 = MinecraftServer.DEMO_SETTINGS;
/* 156 */           worldGenSettings = WorldGenSettings.demoSettings((RegistryAccess)debug17);
/*     */         } else {
/* 158 */           DedicatedServerProperties dedicatedServerProperties = debug19.getProperties();
/* 159 */           debug38 = new LevelSettings(dedicatedServerProperties.levelName, dedicatedServerProperties.gamemode, dedicatedServerProperties.hardcore, dedicatedServerProperties.difficulty, false, new GameRules(), debug33);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 168 */           worldGenSettings = debug16.has((OptionSpec)optionSpecBuilder4) ? dedicatedServerProperties.worldGenSettings.withBonusChest() : dedicatedServerProperties.worldGenSettings;
/*     */         } 
/* 170 */         primaryLevelData1 = new PrimaryLevelData(debug38, worldGenSettings, Lifecycle.stable());
/*     */       } 
/*     */       
/* 173 */       if (debug16.has((OptionSpec)optionSpecBuilder5)) {
/* 174 */         forceUpgrade(debug29, DataFixers.getDataFixer(), debug16.has((OptionSpec)optionSpecBuilder6), () -> true, primaryLevelData1.worldGenSettings().levels());
/*     */       }
/*     */       
/* 177 */       debug29.saveDataTag((RegistryAccess)debug17, (WorldData)primaryLevelData1);
/*     */       
/* 179 */       PrimaryLevelData primaryLevelData2 = primaryLevelData1;
/* 180 */       final DedicatedServer dedicatedServer = MinecraftServer.<DedicatedServer>spin(debug16 -> {
/*     */             DedicatedServer debug17 = new DedicatedServer(debug16, debug0, debug1, debug2, debug3, debug4, debug5, DataFixers.getDataFixer(), debug6, debug7, debug8, net.minecraft.server.level.progress.LoggerChunkProgressListener::new);
/*     */             
/*     */             debug17.setSingleplayerName((String)debug9.valueOf(debug10));
/*     */             
/*     */             debug17.setPort(((Integer)debug9.valueOf(debug11)).intValue());
/*     */             debug17.setDemo(debug9.has(debug12));
/*     */             debug17.setId((String)debug9.valueOf(debug13));
/* 188 */             boolean debug18 = (!debug9.has(debug14) && !debug9.valuesOf(debug15).contains("nogui"));
/*     */             
/*     */             if (debug18 && !GraphicsEnvironment.isHeadless()) {
/*     */               debug17.showGui();
/*     */             }
/*     */             return debug17;
/*     */           });
/* 195 */       Thread debug40 = new Thread("Server Shutdown Thread")
/*     */         {
/*     */           public void run() {
/* 198 */             dedicatedServer.halt(true);
/*     */           }
/*     */         };
/* 201 */       debug40.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(LOGGER));
/* 202 */       Runtime.getRuntime().addShutdownHook(debug40);
/* 203 */     } catch (Exception debug16) {
/* 204 */       LOGGER.fatal("Failed to start the minecraft server", debug16);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void forceUpgrade(LevelStorageSource.LevelStorageAccess debug0, DataFixer debug1, boolean debug2, BooleanSupplier debug3, ImmutableSet<ResourceKey<Level>> debug4) {
/* 209 */     LOGGER.info("Forcing world upgrade!");
/*     */     
/* 211 */     WorldUpgrader debug5 = new WorldUpgrader(debug0, debug1, debug4, debug2);
/* 212 */     Component debug6 = null;
/* 213 */     while (!debug5.isFinished()) {
/* 214 */       Component debug7 = debug5.getStatus();
/* 215 */       if (debug6 != debug7) {
/* 216 */         debug6 = debug7;
/* 217 */         LOGGER.info(debug5.getStatus().getString());
/*     */       } 
/* 219 */       int debug8 = debug5.getTotalChunks();
/* 220 */       if (debug8 > 0) {
/* 221 */         int debug9 = debug5.getConverted() + debug5.getSkipped();
/* 222 */         LOGGER.info("{}% completed ({} / {} chunks)...", Integer.valueOf(Mth.floor(debug9 / debug8 * 100.0F)), Integer.valueOf(debug9), Integer.valueOf(debug8));
/*     */       } 
/*     */       
/* 225 */       if (!debug3.getAsBoolean()) {
/* 226 */         debug5.cancel(); continue;
/*     */       } 
/*     */       try {
/* 229 */         Thread.sleep(1000L);
/* 230 */       } catch (InterruptedException interruptedException) {}
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */