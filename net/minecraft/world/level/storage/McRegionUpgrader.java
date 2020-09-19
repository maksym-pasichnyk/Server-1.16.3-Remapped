/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.WritableRegistry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.RegistryReadOps;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.util.ProgressListener;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.DataPackConfig;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.biome.FixedBiomeSource;
/*     */ import net.minecraft.world.level.biome.OverworldBiomeSource;
/*     */ import net.minecraft.world.level.chunk.storage.RegionFile;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class McRegionUpgrader
/*     */ {
/*  36 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   static boolean convertLevel(LevelStorageSource.LevelStorageAccess debug0, ProgressListener debug1) {
/*     */     OverworldBiomeSource overworldBiomeSource;
/*  40 */     debug1.progressStagePercentage(0);
/*     */     
/*  42 */     List<File> debug2 = Lists.newArrayList();
/*  43 */     List<File> debug3 = Lists.newArrayList();
/*  44 */     List<File> debug4 = Lists.newArrayList();
/*  45 */     File debug5 = debug0.getDimensionPath(Level.OVERWORLD);
/*  46 */     File debug6 = debug0.getDimensionPath(Level.NETHER);
/*  47 */     File debug7 = debug0.getDimensionPath(Level.END);
/*     */     
/*  49 */     LOGGER.info("Scanning folders...");
/*     */ 
/*     */     
/*  52 */     addRegionFiles(debug5, debug2);
/*     */ 
/*     */     
/*  55 */     if (debug6.exists()) {
/*  56 */       addRegionFiles(debug6, debug3);
/*     */     }
/*  58 */     if (debug7.exists()) {
/*  59 */       addRegionFiles(debug7, debug4);
/*     */     }
/*     */     
/*  62 */     int debug8 = debug2.size() + debug3.size() + debug4.size();
/*  63 */     LOGGER.info("Total conversion count is {}", Integer.valueOf(debug8));
/*     */     
/*  65 */     RegistryAccess.RegistryHolder debug9 = RegistryAccess.builtin();
/*  66 */     RegistryReadOps<Tag> debug10 = RegistryReadOps.create((DynamicOps)NbtOps.INSTANCE, (ResourceManager)ResourceManager.Empty.INSTANCE, debug9);
/*     */     
/*  68 */     WorldData debug11 = debug0.getDataTag((DynamicOps<Tag>)debug10, DataPackConfig.DEFAULT);
/*  69 */     long debug12 = (debug11 != null) ? debug11.worldGenSettings().seed() : 0L;
/*     */ 
/*     */ 
/*     */     
/*  73 */     WritableRegistry writableRegistry = debug9.registryOrThrow(Registry.BIOME_REGISTRY);
/*     */     
/*  75 */     if (debug11 != null && debug11.worldGenSettings().isFlatWorld()) {
/*  76 */       FixedBiomeSource fixedBiomeSource = new FixedBiomeSource((Biome)writableRegistry.getOrThrow(Biomes.PLAINS));
/*     */     } else {
/*  78 */       overworldBiomeSource = new OverworldBiomeSource(debug12, false, false, (Registry)writableRegistry);
/*     */     } 
/*     */ 
/*     */     
/*  82 */     convertRegions(debug9, new File(debug5, "region"), debug2, (BiomeSource)overworldBiomeSource, 0, debug8, debug1);
/*     */     
/*  84 */     convertRegions(debug9, new File(debug6, "region"), debug3, (BiomeSource)new FixedBiomeSource((Biome)writableRegistry.getOrThrow(Biomes.NETHER_WASTES)), debug2.size(), debug8, debug1);
/*     */     
/*  86 */     convertRegions(debug9, new File(debug7, "region"), debug4, (BiomeSource)new FixedBiomeSource((Biome)writableRegistry.getOrThrow(Biomes.THE_END)), debug2.size() + debug3.size(), debug8, debug1);
/*     */     
/*  88 */     makeMcrLevelDatBackup(debug0);
/*     */     
/*  90 */     debug0.saveDataTag((RegistryAccess)debug9, debug11);
/*  91 */     return true;
/*     */   }
/*     */   
/*     */   private static void makeMcrLevelDatBackup(LevelStorageSource.LevelStorageAccess debug0) {
/*  95 */     File debug1 = debug0.getLevelPath(LevelResource.LEVEL_DATA_FILE).toFile();
/*  96 */     if (!debug1.exists()) {
/*  97 */       LOGGER.warn("Unable to create level.dat_mcr backup");
/*     */       
/*     */       return;
/*     */     } 
/* 101 */     File debug2 = new File(debug1.getParent(), "level.dat_mcr");
/* 102 */     if (!debug1.renameTo(debug2)) {
/* 103 */       LOGGER.warn("Unable to create level.dat_mcr backup");
/*     */     }
/*     */   }
/*     */   
/*     */   private static void convertRegions(RegistryAccess.RegistryHolder debug0, File debug1, Iterable<File> debug2, BiomeSource debug3, int debug4, int debug5, ProgressListener debug6) {
/* 108 */     for (File debug8 : debug2) {
/* 109 */       convertRegion(debug0, debug1, debug8, debug3, debug4, debug5, debug6);
/*     */       
/* 111 */       debug4++;
/* 112 */       int debug9 = (int)Math.round(100.0D * debug4 / debug5);
/* 113 */       debug6.progressStagePercentage(debug9);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void convertRegion(RegistryAccess.RegistryHolder debug0, File debug1, File debug2, BiomeSource debug3, int debug4, int debug5, ProgressListener debug6) {
/* 118 */     String debug7 = debug2.getName();
/*     */ 
/*     */     
/* 121 */     try(RegionFile debug8 = new RegionFile(debug2, debug1, true); 
/* 122 */         RegionFile debug10 = new RegionFile(new File(debug1, debug7.substring(0, debug7.length() - ".mcr".length()) + ".mca"), debug1, true)) {
/*     */       
/* 124 */       for (int debug12 = 0; debug12 < 32; debug12++) {
/* 125 */         int debug13; for (debug13 = 0; debug13 < 32; debug13++) {
/* 126 */           ChunkPos chunkPos = new ChunkPos(debug12, debug13);
/* 127 */           if (debug8.hasChunk(chunkPos) && !debug10.hasChunk(chunkPos)) {
/*     */             try {
/* 129 */               CompoundTag debug16; DataInputStream dataInputStream = debug8.getChunkDataInputStream(chunkPos); Throwable throwable = null;
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             }
/* 135 */             catch (IOException debug16) {
/* 136 */               LOGGER.warn("Failed to read data for chunk {}", chunkPos, debug16);
/*     */             } 
/*     */           }
/*     */         } 
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
/* 153 */         debug13 = (int)Math.round(100.0D * (debug4 * 1024) / (debug5 * 1024));
/* 154 */         int debug14 = (int)Math.round(100.0D * ((debug12 + 1) * 32 + debug4 * 1024) / (debug5 * 1024));
/* 155 */         if (debug14 > debug13) {
/* 156 */           debug6.progressStagePercentage(debug14);
/*     */         }
/*     */       } 
/* 159 */     } catch (IOException debug8) {
/* 160 */       LOGGER.error("Failed to upgrade region file {}", debug2, debug8);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void addRegionFiles(File debug0, Collection<File> debug1) {
/* 165 */     File debug2 = new File(debug0, "region");
/* 166 */     File[] debug3 = debug2.listFiles((debug0, debug1) -> debug1.endsWith(".mcr"));
/*     */     
/* 168 */     if (debug3 != null)
/* 169 */       Collections.addAll(debug1, debug3); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\McRegionUpgrader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */