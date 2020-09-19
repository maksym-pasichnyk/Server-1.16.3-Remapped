/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.Keyable;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.StrongholdConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
/*    */ 
/*    */ public class StructureSettings
/*    */ {
/*    */   public static final Codec<StructureSettings> CODEC;
/*    */   
/*    */   static {
/* 24 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)StrongholdConfiguration.CODEC.optionalFieldOf("stronghold").forGetter(()), (App)Codec.simpleMap((Codec)Registry.STRUCTURE_FEATURE, StructureFeatureConfiguration.CODEC, (Keyable)Registry.STRUCTURE_FEATURE).fieldOf("structures").forGetter(())).apply((Applicative)debug0, StructureSettings::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public static final ImmutableMap<StructureFeature<?>, StructureFeatureConfiguration> DEFAULTS = ImmutableMap.builder()
/* 32 */     .put(StructureFeature.VILLAGE, new StructureFeatureConfiguration(32, 8, 10387312))
/* 33 */     .put(StructureFeature.DESERT_PYRAMID, new StructureFeatureConfiguration(32, 8, 14357617))
/* 34 */     .put(StructureFeature.IGLOO, new StructureFeatureConfiguration(32, 8, 14357618))
/* 35 */     .put(StructureFeature.JUNGLE_TEMPLE, new StructureFeatureConfiguration(32, 8, 14357619))
/* 36 */     .put(StructureFeature.SWAMP_HUT, new StructureFeatureConfiguration(32, 8, 14357620))
/* 37 */     .put(StructureFeature.PILLAGER_OUTPOST, new StructureFeatureConfiguration(32, 8, 165745296))
/* 38 */     .put(StructureFeature.STRONGHOLD, new StructureFeatureConfiguration(1, 0, 0))
/* 39 */     .put(StructureFeature.OCEAN_MONUMENT, new StructureFeatureConfiguration(32, 5, 10387313))
/* 40 */     .put(StructureFeature.END_CITY, new StructureFeatureConfiguration(20, 11, 10387313))
/* 41 */     .put(StructureFeature.WOODLAND_MANSION, new StructureFeatureConfiguration(80, 20, 10387319))
/* 42 */     .put(StructureFeature.BURIED_TREASURE, new StructureFeatureConfiguration(1, 0, 0))
/* 43 */     .put(StructureFeature.MINESHAFT, new StructureFeatureConfiguration(1, 0, 0))
/* 44 */     .put(StructureFeature.RUINED_PORTAL, new StructureFeatureConfiguration(40, 15, 34222645))
/* 45 */     .put(StructureFeature.SHIPWRECK, new StructureFeatureConfiguration(24, 4, 165745295))
/* 46 */     .put(StructureFeature.OCEAN_RUIN, new StructureFeatureConfiguration(20, 8, 14357621))
/*    */     
/* 48 */     .put(StructureFeature.BASTION_REMNANT, new StructureFeatureConfiguration(27, 4, 30084232))
/* 49 */     .put(StructureFeature.NETHER_BRIDGE, new StructureFeatureConfiguration(27, 4, 30084232))
/* 50 */     .put(StructureFeature.NETHER_FOSSIL, new StructureFeatureConfiguration(2, 1, 14357921))
/* 51 */     .build();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 61 */   public static final StrongholdConfiguration DEFAULT_STRONGHOLD = new StrongholdConfiguration(32, 3, 128);
/*    */   
/*    */   private final Map<StructureFeature<?>, StructureFeatureConfiguration> structureConfig;
/*    */   
/*    */   @Nullable
/*    */   private final StrongholdConfiguration stronghold;
/*    */   
/*    */   public StructureSettings(Optional<StrongholdConfiguration> debug1, Map<StructureFeature<?>, StructureFeatureConfiguration> debug2) {
/* 69 */     this.stronghold = debug1.orElse(null);
/* 70 */     this.structureConfig = debug2;
/*    */   }
/*    */   
/*    */   public StructureSettings(boolean debug1) {
/* 74 */     this.structureConfig = Maps.newHashMap((Map)DEFAULTS);
/* 75 */     this.stronghold = debug1 ? DEFAULT_STRONGHOLD : null;
/*    */   }
/*    */   
/*    */   public Map<StructureFeature<?>, StructureFeatureConfiguration> structureConfig() {
/* 79 */     return this.structureConfig;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public StructureFeatureConfiguration getConfig(StructureFeature<?> debug1) {
/* 84 */     return this.structureConfig.get(debug1);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public StrongholdConfiguration stronghold() {
/* 89 */     return this.stronghold;
/*    */   }
/*    */   
/*    */   static {
/*    */     for (StructureFeature<?> debug1 : (Iterable<StructureFeature<?>>)Registry.STRUCTURE_FEATURE) {
/*    */       if (!DEFAULTS.containsKey(debug1))
/*    */         throw new IllegalStateException("Structure feature without default settings: " + Registry.STRUCTURE_FEATURE.getKey(debug1)); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\StructureSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */