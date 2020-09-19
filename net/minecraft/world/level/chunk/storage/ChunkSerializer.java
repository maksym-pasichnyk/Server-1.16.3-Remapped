/*     */ package net.minecraft.world.level.chunk.storage;
/*     */ import com.google.common.collect.Maps;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortList;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortListIterator;
/*     */ import java.util.Arrays;
/*     */ import java.util.BitSet;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.IdMap;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.LongArrayTag;
/*     */ import net.minecraft.nbt.ShortTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.server.level.ServerChunkCache;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ThreadedLevelLightEngine;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.ChunkTickList;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.TickList;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkBiomeContainer;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.ChunkStatus;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.ImposterProtoChunk;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.chunk.ProtoChunk;
/*     */ import net.minecraft.world.level.chunk.ProtoTickList;
/*     */ import net.minecraft.world.level.chunk.UpgradeData;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ChunkSerializer {
/*  60 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   public static ProtoChunk read(ServerLevel debug0, StructureManager debug1, PoiManager debug2, ChunkPos debug3, CompoundTag debug4) {
/*     */     ProtoChunk protoChunk1;
/*  64 */     ChunkGenerator debug5 = debug0.getChunkSource().getGenerator();
/*  65 */     BiomeSource debug6 = debug5.getBiomeSource();
/*     */     
/*  67 */     CompoundTag debug7 = debug4.getCompound("Level");
/*  68 */     ChunkPos debug8 = new ChunkPos(debug7.getInt("xPos"), debug7.getInt("zPos"));
/*  69 */     if (!Objects.equals(debug3, debug8)) {
/*  70 */       LOGGER.error("Chunk file at {} is in the wrong location; relocating. (Expected {}, got {})", debug3, debug3, debug8);
/*     */     }
/*     */     
/*  73 */     ChunkBiomeContainer debug9 = new ChunkBiomeContainer((IdMap)debug0.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY), debug3, debug6, debug7.contains("Biomes", 11) ? debug7.getIntArray("Biomes") : null);
/*     */     
/*  75 */     UpgradeData debug10 = debug7.contains("UpgradeData", 10) ? new UpgradeData(debug7.getCompound("UpgradeData")) : UpgradeData.EMPTY;
/*     */     
/*  77 */     ProtoTickList<Block> debug11 = new ProtoTickList(debug0 -> (debug0 == null || debug0.defaultBlockState().isAir()), debug3, debug7.getList("ToBeTicked", 9));
/*  78 */     ProtoTickList<Fluid> debug12 = new ProtoTickList(debug0 -> (debug0 == null || debug0 == Fluids.EMPTY), debug3, debug7.getList("LiquidsToBeTicked", 9));
/*     */     
/*  80 */     boolean debug13 = debug7.getBoolean("isLightOn");
/*     */     
/*  82 */     ListTag debug14 = debug7.getList("Sections", 10);
/*  83 */     int debug15 = 16;
/*  84 */     LevelChunkSection[] debug16 = new LevelChunkSection[16];
/*     */     
/*  86 */     boolean debug17 = debug0.dimensionType().hasSkyLight();
/*  87 */     ServerChunkCache serverChunkCache = debug0.getChunkSource();
/*     */     
/*  89 */     LevelLightEngine debug19 = serverChunkCache.getLightEngine();
/*  90 */     if (debug13) {
/*  91 */       debug19.retainData(debug3, true);
/*     */     }
/*     */     
/*  94 */     for (int i = 0; i < debug14.size(); i++) {
/*  95 */       CompoundTag debug21 = debug14.getCompound(i);
/*     */       
/*  97 */       int i1 = debug21.getByte("Y");
/*  98 */       if (debug21.contains("Palette", 9) && debug21.contains("BlockStates", 12)) {
/*  99 */         LevelChunkSection debug23 = new LevelChunkSection(i1 << 4);
/* 100 */         debug23.getStates().read(debug21.getList("Palette", 10), debug21.getLongArray("BlockStates"));
/* 101 */         debug23.recalcBlockCounts();
/* 102 */         if (!debug23.isEmpty()) {
/* 103 */           debug16[i1] = debug23;
/*     */         }
/* 105 */         debug2.checkConsistencyWithBlocks(debug3, debug23);
/*     */       } 
/* 107 */       if (debug13) {
/*     */ 
/*     */         
/* 110 */         if (debug21.contains("BlockLight", 7)) {
/* 111 */           debug19.queueSectionData(LightLayer.BLOCK, SectionPos.of(debug3, i1), new DataLayer(debug21.getByteArray("BlockLight")), true);
/*     */         }
/* 113 */         if (debug17 && debug21.contains("SkyLight", 7)) {
/* 114 */           debug19.queueSectionData(LightLayer.SKY, SectionPos.of(debug3, i1), new DataLayer(debug21.getByteArray("SkyLight")), true);
/*     */         }
/*     */       } 
/*     */     } 
/* 118 */     long debug20 = debug7.getLong("InhabitedTime");
/*     */     
/* 120 */     ChunkStatus.ChunkType debug22 = getChunkTypeFromTag(debug4);
/*     */ 
/*     */     
/* 123 */     if (debug22 == ChunkStatus.ChunkType.LEVELCHUNK) {
/*     */       ProtoTickList<Block> protoTickList; ProtoTickList<Fluid> protoTickList1;
/* 125 */       if (debug7.contains("TileTicks", 9)) {
/* 126 */         ChunkTickList chunkTickList = ChunkTickList.create(debug7.getList("TileTicks", 10), Registry.BLOCK::getKey, Registry.BLOCK::get);
/*     */       } else {
/* 128 */         protoTickList = debug11;
/*     */       } 
/*     */ 
/*     */       
/* 132 */       if (debug7.contains("LiquidTicks", 9)) {
/* 133 */         ChunkTickList chunkTickList = ChunkTickList.create(debug7.getList("LiquidTicks", 10), Registry.FLUID::getKey, Registry.FLUID::get);
/*     */       } else {
/* 135 */         protoTickList1 = debug12;
/*     */       } 
/*     */       
/* 138 */       LevelChunk levelChunk = new LevelChunk((Level)debug0.getLevel(), debug3, debug9, debug10, (TickList)protoTickList, (TickList)protoTickList1, debug20, debug16, debug1 -> postLoadChunk(debug0, debug1));
/*     */     } else {
/* 140 */       ProtoChunk protoChunk = new ProtoChunk(debug3, debug10, debug16, debug11, debug12);
/* 141 */       protoChunk.setBiomes(debug9);
/* 142 */       protoChunk1 = protoChunk;
/* 143 */       protoChunk1.setInhabitedTime(debug20);
/* 144 */       protoChunk.setStatus(ChunkStatus.byName(debug7.getString("Status")));
/* 145 */       if (protoChunk1.getStatus().isOrAfter(ChunkStatus.FEATURES)) {
/* 146 */         protoChunk.setLightEngine(debug19);
/*     */       }
/* 148 */       if (!debug13 && protoChunk1.getStatus().isOrAfter(ChunkStatus.LIGHT)) {
/* 149 */         for (BlockPos blockPos : BlockPos.betweenClosed(debug3.getMinBlockX(), 0, debug3.getMinBlockZ(), debug3.getMaxBlockX(), 255, debug3.getMaxBlockZ())) {
/* 150 */           if (protoChunk1.getBlockState(blockPos).getLightEmission() != 0) {
/* 151 */             protoChunk.addLight(blockPos);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 157 */     protoChunk1.setLightCorrect(debug13);
/*     */     
/* 159 */     CompoundTag debug24 = debug7.getCompound("Heightmaps");
/*     */     
/* 161 */     EnumSet<Heightmap.Types> debug25 = EnumSet.noneOf(Heightmap.Types.class);
/*     */     
/* 163 */     for (Heightmap.Types types : protoChunk1.getStatus().heightmapsAfter()) {
/* 164 */       String str = types.getSerializationKey();
/*     */       
/* 166 */       if (debug24.contains(str, 12)) {
/* 167 */         protoChunk1.setHeightmap(types, debug24.getLongArray(str)); continue;
/*     */       } 
/* 169 */       debug25.add(types);
/*     */     } 
/*     */ 
/*     */     
/* 173 */     Heightmap.primeHeightmaps((ChunkAccess)protoChunk1, debug25);
/*     */     
/* 175 */     CompoundTag debug26 = debug7.getCompound("Structures");
/* 176 */     protoChunk1.setAllStarts(unpackStructureStart(debug1, debug26, debug0.getSeed()));
/* 177 */     protoChunk1.setAllReferences(unpackStructureReferences(debug3, debug26));
/*     */     
/* 179 */     if (debug7.getBoolean("shouldSave")) {
/* 180 */       protoChunk1.setUnsaved(true);
/*     */     }
/*     */     
/* 183 */     ListTag debug27 = debug7.getList("PostProcessing", 9);
/* 184 */     for (int j = 0; j < debug27.size(); j++) {
/* 185 */       ListTag listTag = debug27.getList(j);
/* 186 */       for (int i1 = 0; i1 < listTag.size(); i1++) {
/* 187 */         protoChunk1.addPackedPostProcess(listTag.getShort(i1), j);
/*     */       }
/*     */     } 
/*     */     
/* 191 */     if (debug22 == ChunkStatus.ChunkType.LEVELCHUNK) {
/* 192 */       return (ProtoChunk)new ImposterProtoChunk((LevelChunk)protoChunk1);
/*     */     }
/*     */     
/* 195 */     ProtoChunk debug28 = protoChunk1;
/* 196 */     ListTag debug29 = debug7.getList("Entities", 10);
/* 197 */     for (int k = 0; k < debug29.size(); k++) {
/* 198 */       debug28.addEntity(debug29.getCompound(k));
/*     */     }
/*     */     
/* 201 */     ListTag debug30 = debug7.getList("TileEntities", 10);
/* 202 */     for (int m = 0; m < debug30.size(); m++) {
/* 203 */       CompoundTag compoundTag = debug30.getCompound(m);
/* 204 */       protoChunk1.setBlockEntityNbt(compoundTag);
/*     */     } 
/*     */     
/* 207 */     ListTag debug31 = debug7.getList("Lights", 9);
/* 208 */     for (int n = 0; n < debug31.size(); n++) {
/* 209 */       ListTag debug33 = debug31.getList(n);
/* 210 */       for (int debug34 = 0; debug34 < debug33.size(); debug34++) {
/* 211 */         debug28.addLight(debug33.getShort(debug34), n);
/*     */       }
/*     */     } 
/*     */     
/* 215 */     CompoundTag debug32 = debug7.getCompound("CarvingMasks");
/* 216 */     for (String debug34 : debug32.getAllKeys()) {
/* 217 */       GenerationStep.Carving debug35 = GenerationStep.Carving.valueOf(debug34);
/* 218 */       debug28.setCarvingMask(debug35, BitSet.valueOf(debug32.getByteArray(debug34)));
/*     */     } 
/*     */     
/* 221 */     return debug28;
/*     */   }
/*     */   
/*     */   public static CompoundTag write(ServerLevel debug0, ChunkAccess debug1) {
/* 225 */     ChunkPos debug2 = debug1.getPos();
/* 226 */     CompoundTag debug3 = new CompoundTag();
/* 227 */     CompoundTag debug4 = new CompoundTag();
/* 228 */     debug3.putInt("DataVersion", SharedConstants.getCurrentVersion().getWorldVersion());
/* 229 */     debug3.put("Level", (Tag)debug4);
/*     */     
/* 231 */     debug4.putInt("xPos", debug2.x);
/* 232 */     debug4.putInt("zPos", debug2.z);
/* 233 */     debug4.putLong("LastUpdate", debug0.getGameTime());
/* 234 */     debug4.putLong("InhabitedTime", debug1.getInhabitedTime());
/* 235 */     debug4.putString("Status", debug1.getStatus().getName());
/*     */     
/* 237 */     UpgradeData debug5 = debug1.getUpgradeData();
/* 238 */     if (!debug5.isEmpty()) {
/* 239 */       debug4.put("UpgradeData", (Tag)debug5.write());
/*     */     }
/*     */     
/* 242 */     LevelChunkSection[] debug6 = debug1.getSections();
/* 243 */     ListTag debug7 = new ListTag();
/*     */     
/* 245 */     ThreadedLevelLightEngine threadedLevelLightEngine = debug0.getChunkSource().getLightEngine();
/*     */     
/* 247 */     boolean debug9 = debug1.isLightCorrect();
/* 248 */     for (int i = -1; i < 17; i++) {
/* 249 */       int j = i;
/* 250 */       LevelChunkSection levelChunkSection = Arrays.<LevelChunkSection>stream(debug6).filter(debug1 -> (debug1 != null && debug1.bottomBlockY() >> 4 == debug0)).findFirst().orElse(LevelChunk.EMPTY_SECTION);
/* 251 */       DataLayer dataLayer1 = threadedLevelLightEngine.getLayerListener(LightLayer.BLOCK).getDataLayerData(SectionPos.of(debug2, j));
/* 252 */       DataLayer dataLayer2 = threadedLevelLightEngine.getLayerListener(LightLayer.SKY).getDataLayerData(SectionPos.of(debug2, j));
/* 253 */       if (levelChunkSection != LevelChunk.EMPTY_SECTION || dataLayer1 != null || dataLayer2 != null) {
/*     */ 
/*     */ 
/*     */         
/* 257 */         CompoundTag compoundTag = new CompoundTag();
/* 258 */         compoundTag.putByte("Y", (byte)(j & 0xFF));
/*     */         
/* 260 */         if (levelChunkSection != LevelChunk.EMPTY_SECTION) {
/* 261 */           levelChunkSection.getStates().write(compoundTag, "Palette", "BlockStates");
/*     */         }
/* 263 */         if (dataLayer1 != null && !dataLayer1.isEmpty()) {
/* 264 */           compoundTag.putByteArray("BlockLight", dataLayer1.getData());
/*     */         }
/* 266 */         if (dataLayer2 != null && !dataLayer2.isEmpty()) {
/* 267 */           compoundTag.putByteArray("SkyLight", dataLayer2.getData());
/*     */         }
/*     */         
/* 270 */         debug7.add(compoundTag);
/*     */       } 
/* 272 */     }  debug4.put("Sections", (Tag)debug7);
/*     */     
/* 274 */     if (debug9) {
/* 275 */       debug4.putBoolean("isLightOn", true);
/*     */     }
/*     */     
/* 278 */     ChunkBiomeContainer debug10 = debug1.getBiomes();
/* 279 */     if (debug10 != null) {
/* 280 */       debug4.putIntArray("Biomes", debug10.writeBiomes());
/*     */     }
/*     */ 
/*     */     
/* 284 */     ListTag debug11 = new ListTag();
/* 285 */     for (BlockPos blockPos : debug1.getBlockEntitiesPos()) {
/* 286 */       CompoundTag compoundTag = debug1.getBlockEntityNbtForSaving(blockPos);
/* 287 */       if (compoundTag != null) {
/* 288 */         debug11.add(compoundTag);
/*     */       }
/*     */     } 
/* 291 */     debug4.put("TileEntities", (Tag)debug11);
/*     */     
/* 293 */     ListTag debug12 = new ListTag();
/* 294 */     if (debug1.getStatus().getChunkType() == ChunkStatus.ChunkType.LEVELCHUNK) {
/*     */       
/* 296 */       LevelChunk levelChunk = (LevelChunk)debug1;
/* 297 */       levelChunk.setLastSaveHadEntities(false);
/* 298 */       for (int j = 0; j < (levelChunk.getEntitySections()).length; j++) {
/* 299 */         for (Entity debug16 : levelChunk.getEntitySections()[j]) {
/* 300 */           CompoundTag debug17 = new CompoundTag();
/* 301 */           if (debug16.save(debug17)) {
/* 302 */             levelChunk.setLastSaveHadEntities(true);
/* 303 */             debug12.add(debug17);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 309 */       ProtoChunk protoChunk = (ProtoChunk)debug1;
/* 310 */       debug12.addAll(protoChunk.getEntities());
/*     */ 
/*     */       
/* 313 */       debug4.put("Lights", (Tag)packOffsets(protoChunk.getPackedLights()));
/*     */ 
/*     */       
/* 316 */       CompoundTag compoundTag = new CompoundTag();
/* 317 */       for (GenerationStep.Carving debug18 : GenerationStep.Carving.values()) {
/* 318 */         BitSet debug19 = protoChunk.getCarvingMask(debug18);
/* 319 */         if (debug19 != null) {
/* 320 */           compoundTag.putByteArray(debug18.toString(), debug19.toByteArray());
/*     */         }
/*     */       } 
/* 323 */       debug4.put("CarvingMasks", (Tag)compoundTag);
/*     */     } 
/* 325 */     debug4.put("Entities", (Tag)debug12);
/*     */ 
/*     */     
/* 328 */     TickList<Block> debug13 = debug1.getBlockTicks();
/* 329 */     if (debug13 instanceof ProtoTickList) {
/* 330 */       debug4.put("ToBeTicked", (Tag)((ProtoTickList)debug13).save());
/* 331 */     } else if (debug13 instanceof ChunkTickList) {
/* 332 */       debug4.put("TileTicks", (Tag)((ChunkTickList)debug13).save());
/*     */     } else {
/*     */       
/* 335 */       debug4.put("TileTicks", (Tag)debug0.getBlockTicks().save(debug2));
/*     */     } 
/*     */     
/* 338 */     TickList<Fluid> debug14 = debug1.getLiquidTicks();
/* 339 */     if (debug14 instanceof ProtoTickList) {
/* 340 */       debug4.put("LiquidsToBeTicked", (Tag)((ProtoTickList)debug14).save());
/* 341 */     } else if (debug14 instanceof ChunkTickList) {
/* 342 */       debug4.put("LiquidTicks", (Tag)((ChunkTickList)debug14).save());
/*     */     } else {
/*     */       
/* 345 */       debug4.put("LiquidTicks", (Tag)debug0.getLiquidTicks().save(debug2));
/*     */     } 
/*     */ 
/*     */     
/* 349 */     debug4.put("PostProcessing", (Tag)packOffsets(debug1.getPostProcessing()));
/*     */     
/* 351 */     CompoundTag debug15 = new CompoundTag();
/* 352 */     for (Map.Entry<Heightmap.Types, Heightmap> debug17 : (Iterable<Map.Entry<Heightmap.Types, Heightmap>>)debug1.getHeightmaps()) {
/* 353 */       if (debug1.getStatus().heightmapsAfter().contains(debug17.getKey())) {
/* 354 */         debug15.put(((Heightmap.Types)debug17.getKey()).getSerializationKey(), (Tag)new LongArrayTag(((Heightmap)debug17.getValue()).getRawData()));
/*     */       }
/*     */     } 
/* 357 */     debug4.put("Heightmaps", (Tag)debug15);
/*     */     
/* 359 */     debug4.put("Structures", (Tag)packStructureData(debug2, debug1.getAllStarts(), debug1.getAllReferences()));
/* 360 */     return debug3;
/*     */   }
/*     */   
/*     */   public static ChunkStatus.ChunkType getChunkTypeFromTag(@Nullable CompoundTag debug0) {
/* 364 */     if (debug0 != null) {
/* 365 */       ChunkStatus debug1 = ChunkStatus.byName(debug0.getCompound("Level").getString("Status"));
/* 366 */       if (debug1 != null) {
/* 367 */         return debug1.getChunkType();
/*     */       }
/*     */     } 
/* 370 */     return ChunkStatus.ChunkType.PROTOCHUNK;
/*     */   }
/*     */   
/*     */   private static void postLoadChunk(CompoundTag debug0, LevelChunk debug1) {
/* 374 */     ListTag debug2 = debug0.getList("Entities", 10);
/* 375 */     Level debug3 = debug1.getLevel();
/* 376 */     for (int i = 0; i < debug2.size(); i++) {
/* 377 */       CompoundTag compoundTag = debug2.getCompound(i);
/* 378 */       EntityType.loadEntityRecursive(compoundTag, debug3, debug1 -> {
/*     */             debug0.addEntity(debug1);
/*     */             return debug1;
/*     */           });
/* 382 */       debug1.setLastSaveHadEntities(true);
/*     */     } 
/*     */     
/* 385 */     ListTag debug4 = debug0.getList("TileEntities", 10);
/* 386 */     for (int debug5 = 0; debug5 < debug4.size(); debug5++) {
/* 387 */       CompoundTag debug6 = debug4.getCompound(debug5);
/* 388 */       boolean debug7 = debug6.getBoolean("keepPacked");
/* 389 */       if (debug7) {
/* 390 */         debug1.setBlockEntityNbt(debug6);
/*     */       } else {
/*     */         
/* 393 */         BlockPos debug8 = new BlockPos(debug6.getInt("x"), debug6.getInt("y"), debug6.getInt("z"));
/* 394 */         BlockEntity debug9 = BlockEntity.loadStatic(debug1.getBlockState(debug8), debug6);
/* 395 */         if (debug9 != null) {
/* 396 */           debug1.addBlockEntity(debug9);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static CompoundTag packStructureData(ChunkPos debug0, Map<StructureFeature<?>, StructureStart<?>> debug1, Map<StructureFeature<?>, LongSet> debug2) {
/* 403 */     CompoundTag debug3 = new CompoundTag();
/*     */     
/* 405 */     CompoundTag debug4 = new CompoundTag();
/* 406 */     for (Map.Entry<StructureFeature<?>, StructureStart<?>> debug6 : debug1.entrySet()) {
/* 407 */       debug4.put(((StructureFeature)debug6.getKey()).getFeatureName(), (Tag)((StructureStart)debug6.getValue()).createTag(debug0.x, debug0.z));
/*     */     }
/* 409 */     debug3.put("Starts", (Tag)debug4);
/*     */     
/* 411 */     CompoundTag debug5 = new CompoundTag();
/* 412 */     for (Map.Entry<StructureFeature<?>, LongSet> debug7 : debug2.entrySet()) {
/* 413 */       debug5.put(((StructureFeature)debug7.getKey()).getFeatureName(), (Tag)new LongArrayTag(debug7.getValue()));
/*     */     }
/* 415 */     debug3.put("References", (Tag)debug5);
/*     */     
/* 417 */     return debug3;
/*     */   }
/*     */   
/*     */   private static Map<StructureFeature<?>, StructureStart<?>> unpackStructureStart(StructureManager debug0, CompoundTag debug1, long debug2) {
/* 421 */     Map<StructureFeature<?>, StructureStart<?>> debug4 = Maps.newHashMap();
/*     */     
/* 423 */     CompoundTag debug5 = debug1.getCompound("Starts");
/* 424 */     for (String debug7 : debug5.getAllKeys()) {
/* 425 */       String debug8 = debug7.toLowerCase(Locale.ROOT);
/* 426 */       StructureFeature<?> debug9 = (StructureFeature)StructureFeature.STRUCTURES_REGISTRY.get(debug8);
/* 427 */       if (debug9 == null) {
/* 428 */         LOGGER.error("Unknown structure start: {}", debug8);
/*     */         continue;
/*     */       } 
/* 431 */       StructureStart<?> debug10 = StructureFeature.loadStaticStart(debug0, debug5.getCompound(debug7), debug2);
/* 432 */       if (debug10 != null) {
/* 433 */         debug4.put(debug9, debug10);
/*     */       }
/*     */     } 
/*     */     
/* 437 */     return debug4;
/*     */   }
/*     */   
/*     */   private static Map<StructureFeature<?>, LongSet> unpackStructureReferences(ChunkPos debug0, CompoundTag debug1) {
/* 441 */     Map<StructureFeature<?>, LongSet> debug2 = Maps.newHashMap();
/*     */     
/* 443 */     CompoundTag debug3 = debug1.getCompound("References");
/* 444 */     for (Iterator<String> iterator = debug3.getAllKeys().iterator(); iterator.hasNext(); ) { String debug5 = iterator.next();
/*     */       
/* 446 */       debug2.put(StructureFeature.STRUCTURES_REGISTRY.get(debug5.toLowerCase(Locale.ROOT)), new LongOpenHashSet(Arrays.stream(debug3.getLongArray(debug5)).filter(debug2 -> {
/*     */                 ChunkPos debug4 = new ChunkPos(debug2);
/*     */                 
/*     */                 if (debug4.getChessboardDistance(debug0) > 8) {
/*     */                   LOGGER.warn("Found invalid structure reference [ {} @ {} ] for chunk {}.", debug1, debug4, debug0);
/*     */                   return false;
/*     */                 } 
/*     */                 return true;
/* 454 */               }).toArray())); }
/*     */ 
/*     */     
/* 457 */     return debug2;
/*     */   }
/*     */   
/*     */   public static ListTag packOffsets(ShortList[] debug0) {
/* 461 */     ListTag debug1 = new ListTag();
/* 462 */     for (ShortList debug5 : debug0) {
/* 463 */       ListTag debug6 = new ListTag();
/* 464 */       if (debug5 != null) {
/* 465 */         for (ShortListIterator<Short> shortListIterator = debug5.iterator(); shortListIterator.hasNext(); ) { Short debug8 = shortListIterator.next();
/* 466 */           debug6.add(ShortTag.valueOf(debug8.shortValue())); }
/*     */       
/*     */       }
/* 469 */       debug1.add(debug6);
/*     */     } 
/* 471 */     return debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\storage\ChunkSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */