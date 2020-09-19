/*     */ package net.minecraft.util.datafix.fixes;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicLike;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.OptionalDynamic;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.commons.lang3.math.NumberUtils;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ public class WorldGenSettingsFix extends DataFix {
/*     */   public WorldGenSettingsFix(Schema debug1) {
/*  28 */     super(debug1, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  33 */     return fixTypeEverywhereTyped("WorldGenSettings building", getInputSchema().getType(References.WORLD_GEN_SETTINGS), debug0 -> debug0.update(DSL.remainderFinder(), WorldGenSettingsFix::fix));
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> noise(long debug0, DynamicLike<T> debug2, Dynamic<T> debug3, Dynamic<T> debug4) {
/*  37 */     return debug2.createMap((Map)ImmutableMap.of(debug2
/*  38 */           .createString("type"), debug2.createString("minecraft:noise"), debug2
/*  39 */           .createString("biome_source"), debug4, debug2
/*  40 */           .createString("seed"), debug2.createLong(debug0), debug2
/*  41 */           .createString("settings"), debug3));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Dynamic<T> vanillaBiomeSource(Dynamic<T> debug0, long debug1, boolean debug3, boolean debug4) {
/*  49 */     ImmutableMap.Builder<Dynamic<T>, Dynamic<T>> debug5 = ImmutableMap.builder().put(debug0.createString("type"), debug0.createString("minecraft:vanilla_layered")).put(debug0.createString("seed"), debug0.createLong(debug1)).put(debug0.createString("large_biomes"), debug0.createBoolean(debug4));
/*     */     
/*  51 */     if (debug3) {
/*  52 */       debug5.put(debug0.createString("legacy_biome_init_layer"), debug0.createBoolean(debug3));
/*     */     }
/*     */     
/*  55 */     return debug0.createMap((Map)debug5.build());
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
/*     */ 
/*     */   
/*  68 */   private static final ImmutableMap<String, StructureFeatureConfiguration> DEFAULTS = ImmutableMap.builder()
/*  69 */     .put("minecraft:village", new StructureFeatureConfiguration(32, 8, 10387312))
/*  70 */     .put("minecraft:desert_pyramid", new StructureFeatureConfiguration(32, 8, 14357617))
/*  71 */     .put("minecraft:igloo", new StructureFeatureConfiguration(32, 8, 14357618))
/*  72 */     .put("minecraft:jungle_pyramid", new StructureFeatureConfiguration(32, 8, 14357619))
/*  73 */     .put("minecraft:swamp_hut", new StructureFeatureConfiguration(32, 8, 14357620))
/*  74 */     .put("minecraft:pillager_outpost", new StructureFeatureConfiguration(32, 8, 165745296))
/*  75 */     .put("minecraft:monument", new StructureFeatureConfiguration(32, 5, 10387313))
/*  76 */     .put("minecraft:endcity", new StructureFeatureConfiguration(20, 11, 10387313))
/*  77 */     .put("minecraft:mansion", new StructureFeatureConfiguration(80, 20, 10387319))
/*  78 */     .build();
/*     */   static final class StructureFeatureConfiguration { public static final Codec<StructureFeatureConfiguration> CODEC; private final int spacing; private final int separation; private final int salt;
/*     */     static {
/*  81 */       CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.INT.fieldOf("spacing").forGetter(()), (App)Codec.INT.fieldOf("separation").forGetter(()), (App)Codec.INT.fieldOf("salt").forGetter(())).apply((Applicative)debug0, StructureFeatureConfiguration::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public StructureFeatureConfiguration(int debug1, int debug2, int debug3) {
/*  92 */       this.spacing = debug1;
/*  93 */       this.separation = debug2;
/*  94 */       this.salt = debug3;
/*     */     }
/*     */     
/*     */     public <T> Dynamic<T> serialize(DynamicOps<T> debug1) {
/*  98 */       return new Dynamic(debug1, CODEC.encodeStart(debug1, this).result().orElse(debug1.emptyMap()));
/*     */     } }
/*     */   
/*     */   private static <T> Dynamic<T> fix(Dynamic<T> debug0) {
/*     */     Dynamic<T> debug4;
/* 103 */     DynamicOps<T> debug1 = debug0.getOps();
/*     */     
/* 105 */     long debug2 = debug0.get("RandomSeed").asLong(0L);
/*     */     
/* 107 */     Optional<String> debug5 = debug0.get("generatorName").asString().map(debug0 -> debug0.toLowerCase(Locale.ROOT)).result();
/*     */     
/* 109 */     Optional<String> debug6 = debug0.get("legacy_custom_options").asString().result().map(Optional::of).orElseGet(() -> debug0.equals(Optional.of("customized")) ? debug1.get("generatorOptions").asString().result() : Optional.empty());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     boolean debug7 = false;
/* 117 */     if (debug5.equals(Optional.of("customized")))
/* 118 */     { debug4 = defaultOverworld(debug0, debug2); }
/* 119 */     else if (!debug5.isPresent())
/* 120 */     { debug4 = defaultOverworld(debug0, debug2); }
/*     */     else
/* 122 */     { String str; boolean debug8; byte b; boolean debug9; OptionalDynamic<T> optionalDynamic1; ImmutableMap.Builder<T, T> debug10; Map<Dynamic<T>, Dynamic<T>> debug11; OptionalDynamic<T> debug12; OptionalDynamic<?> debug13; Optional<String> debug14; Dynamic<T> debug15, debug16, debug17; switch ((String)debug5.get())
/*     */       { case "flat":
/* 124 */           optionalDynamic1 = debug0.get("generatorOptions");
/* 125 */           debug11 = fixFlatStructures(debug1, optionalDynamic1);
/*     */           
/* 127 */           debug4 = debug0.createMap((Map)ImmutableMap.of(debug0
/* 128 */                 .createString("type"), debug0.createString("minecraft:flat"), debug0
/* 129 */                 .createString("settings"), debug0.createMap((Map)ImmutableMap.of(debug0
/* 130 */                     .createString("structures"), debug0.createMap(debug11), debug0
/* 131 */                     .createString("layers"), optionalDynamic1.get("layers").result().orElseGet(() -> debug0.createList(Stream.of(new Dynamic[] { debug0.createMap((Map)ImmutableMap.of(debug0.createString("height"), debug0.createInt(1), debug0.createString("block"), debug0.createString("minecraft:bedrock"))), debug0.createMap((Map)ImmutableMap.of(debug0.createString("height"), debug0.createInt(2), debug0.createString("block"), debug0.createString("minecraft:dirt"))), debug0.createMap((Map)ImmutableMap.of(debug0.createString("height"), debug0.createInt(1), debug0.createString("block"), debug0.createString("minecraft:grass_block"))) }))), debug0
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
/* 145 */                     .createString("biome"), debug0.createString(optionalDynamic1.get("biome").asString("minecraft:plains"))))));
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
/*     */ 
/*     */ 
/*     */           
/* 201 */           debug8 = debug0.get("MapFeatures").asBoolean(true);
/* 202 */           debug9 = debug0.get("BonusChest").asBoolean(false);
/*     */           
/* 204 */           debug10 = ImmutableMap.builder();
/* 205 */           debug10.put(debug1.createString("seed"), debug1.createLong(debug2));
/* 206 */           debug10.put(debug1.createString("generate_features"), debug1.createBoolean(debug8));
/* 207 */           debug10.put(debug1.createString("bonus_chest"), debug1.createBoolean(debug9));
/* 208 */           debug10.put(debug1.createString("dimensions"), vanillaLevels(debug0, debug2, debug4, debug7));
/* 209 */           debug6.ifPresent(debug2 -> debug0.put(debug1.createString("legacy_custom_options"), debug1.createString(debug2)));
/*     */           
/* 211 */           return new Dynamic(debug1, debug1.createMap((Map)debug10.build()));case "debug_all_block_states": debug4 = debug0.createMap((Map)ImmutableMap.of(debug0.createString("type"), debug0.createString("minecraft:debug"))); debug8 = debug0.get("MapFeatures").asBoolean(true); debug9 = debug0.get("BonusChest").asBoolean(false); debug10 = ImmutableMap.builder(); debug10.put(debug1.createString("seed"), debug1.createLong(debug2)); debug10.put(debug1.createString("generate_features"), debug1.createBoolean(debug8)); debug10.put(debug1.createString("bonus_chest"), debug1.createBoolean(debug9)); debug10.put(debug1.createString("dimensions"), vanillaLevels(debug0, debug2, debug4, debug7)); debug6.ifPresent(debug2 -> debug0.put(debug1.createString("legacy_custom_options"), debug1.createString(debug2))); return new Dynamic(debug1, debug1.createMap((Map)debug10.build()));case "buffet": debug12 = debug0.get("generatorOptions"); debug13 = debug12.get("chunk_generator"); debug14 = debug13.get("type").asString().result(); if (Objects.equals(debug14, Optional.of("minecraft:caves"))) { debug15 = debug0.createString("minecraft:caves"); debug7 = true; } else if (Objects.equals(debug14, Optional.of("minecraft:floating_islands"))) { debug15 = debug0.createString("minecraft:floating_islands"); } else { debug15 = debug0.createString("minecraft:overworld"); }  debug16 = debug12.get("biome_source").result().orElseGet(() -> debug0.createMap((Map)ImmutableMap.of(debug0.createString("type"), debug0.createString("minecraft:fixed")))); if (debug16.get("type").asString().result().equals(Optional.of("minecraft:fixed"))) { String str1 = debug16.get("options").get("biomes").asStream().findFirst().flatMap(debug0 -> debug0.asString().result()).orElse("minecraft:ocean"); debug17 = debug16.remove("options").set("biome", debug0.createString(str1)); } else { debug17 = debug16; }  debug4 = noise(debug2, (DynamicLike<T>)debug0, debug15, debug17); debug8 = debug0.get("MapFeatures").asBoolean(true); debug9 = debug0.get("BonusChest").asBoolean(false); debug10 = ImmutableMap.builder(); debug10.put(debug1.createString("seed"), debug1.createLong(debug2)); debug10.put(debug1.createString("generate_features"), debug1.createBoolean(debug8)); debug10.put(debug1.createString("bonus_chest"), debug1.createBoolean(debug9)); debug10.put(debug1.createString("dimensions"), vanillaLevels(debug0, debug2, debug4, debug7)); debug6.ifPresent(debug2 -> debug0.put(debug1.createString("legacy_custom_options"), debug1.createString(debug2))); return new Dynamic(debug1, debug1.createMap((Map)debug10.build())); }  boolean debug18 = ((String)debug5.get()).equals("default"); boolean debug19 = (((String)debug5.get()).equals("default_1_1") || (debug18 && debug0.get("generatorVersion").asInt(0) == 0)); boolean debug20 = ((String)debug5.get()).equals("amplified"); boolean debug21 = ((String)debug5.get()).equals("largebiomes"); debug4 = noise(debug2, (DynamicLike<T>)debug0, debug0.createString(debug20 ? "minecraft:amplified" : "minecraft:overworld"), vanillaBiomeSource(debug0, debug2, debug19, debug21)); }  boolean bool1 = debug0.get("MapFeatures").asBoolean(true); boolean bool2 = debug0.get("BonusChest").asBoolean(false); ImmutableMap.Builder builder = ImmutableMap.builder(); builder.put(debug1.createString("seed"), debug1.createLong(debug2)); builder.put(debug1.createString("generate_features"), debug1.createBoolean(bool1)); builder.put(debug1.createString("bonus_chest"), debug1.createBoolean(bool2)); builder.put(debug1.createString("dimensions"), vanillaLevels(debug0, debug2, debug4, debug7)); debug6.ifPresent(debug2 -> debug0.put(debug1.createString("legacy_custom_options"), debug1.createString(debug2))); return new Dynamic(debug1, debug1.createMap((Map)builder.build()));
/*     */   }
/*     */   
/*     */   protected static <T> Dynamic<T> defaultOverworld(Dynamic<T> debug0, long debug1) {
/* 215 */     return noise(debug1, (DynamicLike<T>)debug0, debug0.createString("minecraft:overworld"), vanillaBiomeSource(debug0, debug1, false, false));
/*     */   }
/*     */   
/*     */   protected static <T> T vanillaLevels(Dynamic<T> debug0, long debug1, Dynamic<T> debug3, boolean debug4) {
/* 219 */     DynamicOps<T> debug5 = debug0.getOps();
/* 220 */     return (T)debug5.createMap((Map)ImmutableMap.of(debug5
/* 221 */           .createString("minecraft:overworld"), debug5.createMap((Map)ImmutableMap.of(debug5
/* 222 */               .createString("type"), debug5.createString("minecraft:overworld" + (debug4 ? "_caves" : "")), debug5
/* 223 */               .createString("generator"), debug3.getValue())), debug5
/*     */           
/* 225 */           .createString("minecraft:the_nether"), debug5.createMap((Map)ImmutableMap.of(debug5
/* 226 */               .createString("type"), debug5.createString("minecraft:the_nether"), debug5
/* 227 */               .createString("generator"), noise(debug1, (DynamicLike<T>)debug0, debug0.createString("minecraft:nether"), debug0.createMap((Map)ImmutableMap.of(debug0
/* 228 */                     .createString("type"), debug0.createString("minecraft:multi_noise"), debug0
/* 229 */                     .createString("seed"), debug0.createLong(debug1), debug0
/* 230 */                     .createString("preset"), debug0.createString("minecraft:nether"))))
/* 231 */               .getValue())), debug5
/*     */           
/* 233 */           .createString("minecraft:the_end"), debug5.createMap((Map)ImmutableMap.of(debug5
/* 234 */               .createString("type"), debug5.createString("minecraft:the_end"), debug5
/* 235 */               .createString("generator"), noise(debug1, (DynamicLike<T>)debug0, debug0.createString("minecraft:end"), debug0.createMap((Map)ImmutableMap.of(debug0
/* 236 */                     .createString("type"), debug0.createString("minecraft:the_end"), debug0
/* 237 */                     .createString("seed"), debug0.createLong(debug1))))
/* 238 */               .getValue()))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Map<Dynamic<T>, Dynamic<T>> fixFlatStructures(DynamicOps<T> debug0, OptionalDynamic<T> debug1) {
/* 244 */     MutableInt debug2 = new MutableInt(32);
/* 245 */     MutableInt debug3 = new MutableInt(3);
/* 246 */     MutableInt debug4 = new MutableInt(128);
/* 247 */     MutableBoolean debug5 = new MutableBoolean(false);
/* 248 */     Map<String, StructureFeatureConfiguration> debug6 = Maps.newHashMap();
/*     */     
/* 250 */     if (!debug1.result().isPresent()) {
/* 251 */       debug5.setTrue();
/* 252 */       debug6.put("minecraft:village", DEFAULTS.get("minecraft:village"));
/*     */     } 
/*     */     
/* 255 */     debug1.get("structures").flatMap(Dynamic::getMapValues).result().ifPresent(debug5 -> debug5.forEach(()));
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
/* 318 */     ImmutableMap.Builder<Dynamic<T>, Dynamic<T>> debug7 = ImmutableMap.builder();
/* 319 */     debug7.put(debug1.createString("structures"), debug1.createMap((Map)debug6.entrySet().stream().collect(Collectors.toMap(debug1 -> debug0.createString((String)debug1.getKey()), debug1 -> ((StructureFeatureConfiguration)debug1.getValue()).serialize(debug0)))));
/*     */ 
/*     */ 
/*     */     
/* 323 */     if (debug5.isTrue()) {
/* 324 */       debug7.put(debug1.createString("stronghold"), debug1.createMap((Map)ImmutableMap.of(debug1
/* 325 */               .createString("distance"), debug1.createInt(debug2.getValue().intValue()), debug1
/* 326 */               .createString("spread"), debug1.createInt(debug3.getValue().intValue()), debug1
/* 327 */               .createString("count"), debug1.createInt(debug4.getValue().intValue()))));
/*     */     }
/*     */     
/* 330 */     return (Map<Dynamic<T>, Dynamic<T>>)debug7.build();
/*     */   }
/*     */   
/*     */   private static int getInt(String debug0, int debug1) {
/* 334 */     return NumberUtils.toInt(debug0, debug1);
/*     */   }
/*     */   
/*     */   private static int getInt(String debug0, int debug1, int debug2) {
/* 338 */     return Math.max(debug2, getInt(debug0, debug1));
/*     */   }
/*     */   
/*     */   private static void setSpacing(Map<String, StructureFeatureConfiguration> debug0, String debug1, String debug2, int debug3) {
/* 342 */     StructureFeatureConfiguration debug4 = (StructureFeatureConfiguration)debug0.getOrDefault(debug1, DEFAULTS.get(debug1));
/* 343 */     int debug5 = getInt(debug2, debug4.spacing, debug3);
/* 344 */     debug0.put(debug1, new StructureFeatureConfiguration(debug5, debug4.separation, debug4.salt));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\WorldGenSettingsFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */