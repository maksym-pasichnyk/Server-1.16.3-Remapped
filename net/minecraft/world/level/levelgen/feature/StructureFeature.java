/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.HashBiMap;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.StructureFeatureManager;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.ChunkStatus;
/*     */ import net.minecraft.world.level.chunk.FeatureAccess;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.MineshaftConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.OceanRuinConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RuinedPortalConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ShipwreckConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.NetherFossilFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.OceanRuinFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public abstract class StructureFeature<C extends FeatureConfiguration>
/*     */ {
/*  52 */   public static final BiMap<String, StructureFeature<?>> STRUCTURES_REGISTRY = (BiMap<String, StructureFeature<?>>)HashBiMap.create();
/*  53 */   private static final Map<StructureFeature<?>, GenerationStep.Decoration> STEP = Maps.newHashMap();
/*     */   
/*  55 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  57 */   public static final StructureFeature<JigsawConfiguration> PILLAGER_OUTPOST = register("Pillager_Outpost", new PillagerOutpostFeature(JigsawConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
/*  58 */   public static final StructureFeature<MineshaftConfiguration> MINESHAFT = register("Mineshaft", new MineshaftFeature(MineshaftConfiguration.CODEC), GenerationStep.Decoration.UNDERGROUND_STRUCTURES);
/*  59 */   public static final StructureFeature<NoneFeatureConfiguration> WOODLAND_MANSION = register("Mansion", new WoodlandMansionFeature(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
/*  60 */   public static final StructureFeature<NoneFeatureConfiguration> JUNGLE_TEMPLE = register("Jungle_Pyramid", new JunglePyramidFeature(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
/*  61 */   public static final StructureFeature<NoneFeatureConfiguration> DESERT_PYRAMID = register("Desert_Pyramid", new DesertPyramidFeature(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
/*  62 */   public static final StructureFeature<NoneFeatureConfiguration> IGLOO = register("Igloo", new IglooFeature(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
/*  63 */   public static final StructureFeature<RuinedPortalConfiguration> RUINED_PORTAL = register("Ruined_Portal", new RuinedPortalFeature(RuinedPortalConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
/*  64 */   public static final StructureFeature<ShipwreckConfiguration> SHIPWRECK = register("Shipwreck", new ShipwreckFeature(ShipwreckConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
/*  65 */   public static final SwamplandHutFeature SWAMP_HUT = register("Swamp_Hut", new SwamplandHutFeature(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
/*  66 */   public static final StructureFeature<NoneFeatureConfiguration> STRONGHOLD = register("Stronghold", new StrongholdFeature(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.STRONGHOLDS);
/*  67 */   public static final StructureFeature<NoneFeatureConfiguration> OCEAN_MONUMENT = register("Monument", new OceanMonumentFeature(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
/*  68 */   public static final StructureFeature<OceanRuinConfiguration> OCEAN_RUIN = (StructureFeature<OceanRuinConfiguration>)register("Ocean_Ruin", new OceanRuinFeature(OceanRuinConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
/*  69 */   public static final StructureFeature<NoneFeatureConfiguration> NETHER_BRIDGE = register("Fortress", new NetherFortressFeature(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.UNDERGROUND_DECORATION);
/*  70 */   public static final StructureFeature<NoneFeatureConfiguration> END_CITY = register("EndCity", new EndCityFeature(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
/*  71 */   public static final StructureFeature<ProbabilityFeatureConfiguration> BURIED_TREASURE = register("Buried_Treasure", new BuriedTreasureFeature(ProbabilityFeatureConfiguration.CODEC), GenerationStep.Decoration.UNDERGROUND_STRUCTURES);
/*  72 */   public static final StructureFeature<JigsawConfiguration> VILLAGE = register("Village", new VillageFeature(JigsawConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
/*  73 */   public static final StructureFeature<NoneFeatureConfiguration> NETHER_FOSSIL = (StructureFeature<NoneFeatureConfiguration>)register("Nether_Fossil", new NetherFossilFeature(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.UNDERGROUND_DECORATION);
/*  74 */   public static final StructureFeature<JigsawConfiguration> BASTION_REMNANT = register("Bastion_Remnant", new BastionFeature(JigsawConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
/*     */   
/*  76 */   public static final List<StructureFeature<?>> NOISE_AFFECTING_FEATURES = (List<StructureFeature<?>>)ImmutableList.of(PILLAGER_OUTPOST, VILLAGE, NETHER_FOSSIL);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   private static final ResourceLocation JIGSAW_RENAME = new ResourceLocation("jigsaw");
/*  84 */   private static final Map<ResourceLocation, ResourceLocation> RENAMES = (Map<ResourceLocation, ResourceLocation>)ImmutableMap.builder()
/*  85 */     .put(new ResourceLocation("nvi"), JIGSAW_RENAME)
/*  86 */     .put(new ResourceLocation("pcp"), JIGSAW_RENAME)
/*  87 */     .put(new ResourceLocation("bastionremnant"), JIGSAW_RENAME)
/*  88 */     .put(new ResourceLocation("runtime"), JIGSAW_RENAME)
/*  89 */     .build();
/*     */   private final Codec<ConfiguredStructureFeature<C, StructureFeature<C>>> configuredStructureCodec;
/*     */   
/*     */   private static <F extends StructureFeature<?>> F register(String debug0, F debug1, GenerationStep.Decoration debug2) {
/*  93 */     STRUCTURES_REGISTRY.put(debug0.toLowerCase(Locale.ROOT), debug1);
/*  94 */     STEP.put((StructureFeature<?>)debug1, debug2);
/*  95 */     return (F)Registry.register(Registry.STRUCTURE_FEATURE, debug0.toLowerCase(Locale.ROOT), debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructureFeature(Codec<C> debug1) {
/* 103 */     this.configuredStructureCodec = debug1.fieldOf("config").xmap(debug1 -> new ConfiguredStructureFeature<>(this, debug1), debug0 -> debug0.config).codec();
/*     */   }
/*     */   
/*     */   public GenerationStep.Decoration step() {
/* 107 */     return STEP.get(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void bootstrap() {}
/*     */   
/*     */   @Nullable
/*     */   public static StructureStart<?> loadStaticStart(StructureManager debug0, CompoundTag debug1, long debug2) {
/* 115 */     String debug4 = debug1.getString("id");
/* 116 */     if ("INVALID".equals(debug4)) {
/* 117 */       return StructureStart.INVALID_START;
/*     */     }
/*     */ 
/*     */     
/* 121 */     StructureFeature<?> debug5 = (StructureFeature)Registry.STRUCTURE_FEATURE.get(new ResourceLocation(debug4.toLowerCase(Locale.ROOT)));
/* 122 */     if (debug5 == null) {
/* 123 */       LOGGER.error("Unknown feature id: {}", debug4);
/* 124 */       return null;
/*     */     } 
/*     */     
/* 127 */     int debug6 = debug1.getInt("ChunkX");
/* 128 */     int debug7 = debug1.getInt("ChunkZ");
/* 129 */     int debug8 = debug1.getInt("references");
/* 130 */     BoundingBox debug9 = debug1.contains("BB") ? new BoundingBox(debug1.getIntArray("BB")) : BoundingBox.getUnknownBox();
/* 131 */     ListTag debug10 = debug1.getList("Children", 10);
/*     */     
/*     */     try {
/* 134 */       StructureStart<?> debug11 = debug5.createStart(debug6, debug7, debug9, debug8, debug2);
/* 135 */       for (int debug12 = 0; debug12 < debug10.size(); debug12++) {
/* 136 */         CompoundTag debug13 = debug10.getCompound(debug12);
/* 137 */         String debug14 = debug13.getString("id").toLowerCase(Locale.ROOT);
/* 138 */         ResourceLocation debug15 = new ResourceLocation(debug14);
/* 139 */         ResourceLocation debug16 = RENAMES.getOrDefault(debug15, debug15);
/*     */         
/* 141 */         StructurePieceType debug17 = (StructurePieceType)Registry.STRUCTURE_PIECE.get(debug16);
/*     */         
/* 143 */         if (debug17 == null) {
/* 144 */           LOGGER.error("Unknown structure piece id: {}", debug16);
/*     */         } else {
/*     */ 
/*     */           
/*     */           try {
/* 149 */             StructurePiece debug18 = debug17.load(debug0, debug13);
/* 150 */             debug11.getPieces().add(debug18);
/* 151 */           } catch (Exception debug18) {
/* 152 */             LOGGER.error("Exception loading structure piece with id {}", debug16, debug18);
/*     */           } 
/*     */         } 
/* 155 */       }  return debug11;
/* 156 */     } catch (Exception debug11) {
/* 157 */       LOGGER.error("Failed Start with id {}", debug4, debug11);
/* 158 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Codec<ConfiguredStructureFeature<C, StructureFeature<C>>> configuredStructureCodec() {
/* 163 */     return this.configuredStructureCodec;
/*     */   }
/*     */   
/*     */   public ConfiguredStructureFeature<C, ? extends StructureFeature<C>> configured(C debug1) {
/* 167 */     return new ConfiguredStructureFeature<>(this, debug1);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public BlockPos getNearestGeneratedFeature(LevelReader debug1, StructureFeatureManager debug2, BlockPos debug3, int debug4, boolean debug5, long debug6, StructureFeatureConfiguration debug8) {
/* 172 */     int debug9 = debug8.spacing();
/*     */     
/* 174 */     int debug10 = debug3.getX() >> 4;
/* 175 */     int debug11 = debug3.getZ() >> 4;
/*     */     
/* 177 */     int debug12 = 0;
/* 178 */     WorldgenRandom debug13 = new WorldgenRandom();
/* 179 */     while (debug12 <= debug4) {
/* 180 */       for (int debug14 = -debug12; debug14 <= debug12; debug14++) {
/* 181 */         boolean debug15 = (debug14 == -debug12 || debug14 == debug12);
/* 182 */         for (int debug16 = -debug12; debug16 <= debug12; debug16++) {
/* 183 */           boolean debug17 = (debug16 == -debug12 || debug16 == debug12);
/* 184 */           if (debug15 || debug17) {
/*     */ 
/*     */ 
/*     */             
/* 188 */             int debug18 = debug10 + debug9 * debug14;
/* 189 */             int debug19 = debug11 + debug9 * debug16;
/*     */             
/* 191 */             ChunkPos debug20 = getPotentialFeatureChunk(debug8, debug6, debug13, debug18, debug19);
/* 192 */             ChunkAccess debug21 = debug1.getChunk(debug20.x, debug20.z, ChunkStatus.STRUCTURE_STARTS);
/* 193 */             StructureStart<?> debug22 = debug2.getStartForFeature(SectionPos.of(debug21.getPos(), 0), this, (FeatureAccess)debug21);
/* 194 */             if (debug22 != null && debug22.isValid()) {
/* 195 */               if (debug5 && debug22.canBeReferenced()) {
/* 196 */                 debug22.addReference();
/* 197 */                 return debug22.getLocatePos();
/* 198 */               }  if (!debug5) {
/* 199 */                 return debug22.getLocatePos();
/*     */               }
/*     */             } 
/*     */             
/* 203 */             if (debug12 == 0)
/*     */               break; 
/*     */           } 
/*     */         } 
/* 207 */         if (debug12 == 0) {
/*     */           break;
/*     */         }
/*     */       } 
/* 211 */       debug12++;
/*     */     } 
/*     */     
/* 214 */     return null;
/*     */   }
/*     */   
/*     */   protected boolean linearSeparation() {
/* 218 */     return true;
/*     */   }
/*     */   
/*     */   public final ChunkPos getPotentialFeatureChunk(StructureFeatureConfiguration debug1, long debug2, WorldgenRandom debug4, int debug5, int debug6) {
/* 222 */     int debug11, debug12, debug7 = debug1.spacing();
/* 223 */     int debug8 = debug1.separation();
/*     */     
/* 225 */     int debug9 = Math.floorDiv(debug5, debug7);
/* 226 */     int debug10 = Math.floorDiv(debug6, debug7);
/*     */     
/* 228 */     debug4.setLargeFeatureWithSalt(debug2, debug9, debug10, debug1.salt());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 233 */     if (linearSeparation()) {
/* 234 */       debug11 = debug4.nextInt(debug7 - debug8);
/* 235 */       debug12 = debug4.nextInt(debug7 - debug8);
/*     */     } else {
/* 237 */       debug11 = (debug4.nextInt(debug7 - debug8) + debug4.nextInt(debug7 - debug8)) / 2;
/* 238 */       debug12 = (debug4.nextInt(debug7 - debug8) + debug4.nextInt(debug7 - debug8)) / 2;
/*     */     } 
/*     */     
/* 241 */     return new ChunkPos(debug9 * debug7 + debug11, debug10 * debug7 + debug12);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isFeatureChunk(ChunkGenerator debug1, BiomeSource debug2, long debug3, WorldgenRandom debug5, int debug6, int debug7, Biome debug8, ChunkPos debug9, C debug10) {
/* 248 */     return true;
/*     */   }
/*     */   
/*     */   private StructureStart<C> createStart(int debug1, int debug2, BoundingBox debug3, int debug4, long debug5) {
/* 252 */     return getStartFactory().create(this, debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */   
/*     */   public StructureStart<?> generate(RegistryAccess debug1, ChunkGenerator debug2, BiomeSource debug3, StructureManager debug4, long debug5, ChunkPos debug7, Biome debug8, int debug9, WorldgenRandom debug10, StructureFeatureConfiguration debug11, C debug12) {
/* 256 */     ChunkPos debug13 = getPotentialFeatureChunk(debug11, debug5, debug10, debug7.x, debug7.z);
/* 257 */     if (debug7.x == debug13.x && debug7.z == debug13.z && 
/*     */       
/* 259 */       isFeatureChunk(debug2, debug3, debug5, debug10, debug7.x, debug7.z, debug8, debug13, debug12)) {
/* 260 */       StructureStart<C> debug14 = createStart(debug7.x, debug7.z, BoundingBox.getUnknownBox(), debug9, debug5);
/* 261 */       debug14.generatePieces(debug1, debug2, debug4, debug7.x, debug7.z, debug8, (FeatureConfiguration)debug12);
/* 262 */       if (debug14.isValid()) {
/* 263 */         return debug14;
/*     */       }
/*     */     } 
/* 266 */     return StructureStart.INVALID_START;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFeatureName() {
/* 272 */     return (String)STRUCTURES_REGISTRY.inverse().get(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MobSpawnSettings.SpawnerData> getSpecialEnemies() {
/* 280 */     return (List<MobSpawnSettings.SpawnerData>)ImmutableList.of();
/*     */   }
/*     */   
/*     */   public List<MobSpawnSettings.SpawnerData> getSpecialAnimals() {
/* 284 */     return (List<MobSpawnSettings.SpawnerData>)ImmutableList.of();
/*     */   }
/*     */   
/*     */   public abstract StructureStartFactory<C> getStartFactory();
/*     */   
/*     */   public static interface StructureStartFactory<C extends FeatureConfiguration> {
/*     */     StructureStart<C> create(StructureFeature<C> param1StructureFeature, int param1Int1, int param1Int2, BoundingBox param1BoundingBox, int param1Int3, long param1Long);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\StructureFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */