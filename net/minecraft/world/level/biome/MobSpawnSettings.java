/*     */ package net.minecraft.world.level.biome;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Keyable;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ 
/*     */ public class MobSpawnSettings {
/*  25 */   public static final Logger LOGGER = LogManager.getLogger(); public static final MobSpawnSettings EMPTY; public static final MapCodec<MobSpawnSettings> CODEC; private final float creatureGenerationProbability;
/*     */   static {
/*  27 */     EMPTY = new MobSpawnSettings(0.1F, (Map<MobCategory, List<SpawnerData>>)Stream.<MobCategory>of(MobCategory.values()).collect(ImmutableMap.toImmutableMap(debug0 -> debug0, debug0 -> ImmutableList.of())), (Map<EntityType<?>, MobSpawnCost>)ImmutableMap.of(), false);
/*     */     
/*  29 */     CODEC = RecordCodecBuilder.mapCodec(debug0 -> debug0.group((App)Codec.FLOAT.optionalFieldOf("creature_spawn_probability", Float.valueOf(0.1F)).forGetter(()), (App)Codec.simpleMap(MobCategory.CODEC, SpawnerData.CODEC.listOf().promotePartial(Util.prefix("Spawn data: ", LOGGER::error)), StringRepresentable.keys((StringRepresentable[])MobCategory.values())).fieldOf("spawners").forGetter(()), (App)Codec.simpleMap((Codec)Registry.ENTITY_TYPE, MobSpawnCost.CODEC, (Keyable)Registry.ENTITY_TYPE).fieldOf("spawn_costs").forGetter(()), (App)Codec.BOOL.fieldOf("player_spawn_friendly").orElse(Boolean.valueOf(false)).forGetter(MobSpawnSettings::playerSpawnFriendly)).apply((Applicative)debug0, MobSpawnSettings::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<MobCategory, List<SpawnerData>> spawners;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<EntityType<?>, MobSpawnCost> mobSpawnCosts;
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean playerSpawnFriendly;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MobSpawnSettings(float debug1, Map<MobCategory, List<SpawnerData>> debug2, Map<EntityType<?>, MobSpawnCost> debug3, boolean debug4) {
/*  50 */     this.creatureGenerationProbability = debug1;
/*  51 */     this.spawners = debug2;
/*  52 */     this.mobSpawnCosts = debug3;
/*  53 */     this.playerSpawnFriendly = debug4;
/*     */   }
/*     */   
/*     */   public List<SpawnerData> getMobs(MobCategory debug1) {
/*  57 */     return (List<SpawnerData>)this.spawners.getOrDefault(debug1, ImmutableList.of());
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public MobSpawnCost getMobSpawnCost(EntityType<?> debug1) {
/*  62 */     return this.mobSpawnCosts.get(debug1);
/*     */   }
/*     */   
/*     */   public float getCreatureProbability() {
/*  66 */     return this.creatureGenerationProbability;
/*     */   }
/*     */   
/*     */   public boolean playerSpawnFriendly() {
/*  70 */     return this.playerSpawnFriendly;
/*     */   }
/*     */   public static class SpawnerData extends WeighedRandom.WeighedRandomItem { public static final Codec<SpawnerData> CODEC; public final EntityType<?> type;
/*     */     static {
/*  74 */       CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Registry.ENTITY_TYPE.fieldOf("type").forGetter(()), (App)Codec.INT.fieldOf("weight").forGetter(()), (App)Codec.INT.fieldOf("minCount").forGetter(()), (App)Codec.INT.fieldOf("maxCount").forGetter(())).apply((Applicative)debug0, SpawnerData::new));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final int minCount;
/*     */ 
/*     */     
/*     */     public final int maxCount;
/*     */ 
/*     */     
/*     */     public SpawnerData(EntityType<?> debug1, int debug2, int debug3, int debug4) {
/*  86 */       super(debug2);
/*  87 */       this.type = (debug1.getCategory() == MobCategory.MISC) ? EntityType.PIG : debug1;
/*  88 */       this.minCount = debug3;
/*  89 */       this.maxCount = debug4;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  94 */       return EntityType.getKey(this.type) + "*(" + this.minCount + "-" + this.maxCount + "):" + this.weight;
/*     */     } }
/*     */   public static class MobSpawnCost { public static final Codec<MobSpawnCost> CODEC; private final double energyBudget; private final double charge;
/*     */     
/*     */     static {
/*  99 */       CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.DOUBLE.fieldOf("energy_budget").forGetter(()), (App)Codec.DOUBLE.fieldOf("charge").forGetter(())).apply((Applicative)debug0, MobSpawnCost::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private MobSpawnCost(double debug1, double debug3) {
/* 108 */       this.energyBudget = debug1;
/* 109 */       this.charge = debug3;
/*     */     }
/*     */     
/*     */     public double getEnergyBudget() {
/* 113 */       return this.energyBudget;
/*     */     }
/*     */     
/*     */     public double getCharge() {
/* 117 */       return this.charge;
/*     */     } }
/*     */   public static class Builder { private final Map<MobCategory, List<MobSpawnSettings.SpawnerData>> spawners; private final Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> mobSpawnCosts;
/*     */     
/*     */     public Builder() {
/* 122 */       this.spawners = (Map<MobCategory, List<MobSpawnSettings.SpawnerData>>)Stream.<MobCategory>of(MobCategory.values()).collect(ImmutableMap.toImmutableMap(debug0 -> debug0, debug0 -> Lists.newArrayList()));
/* 123 */       this.mobSpawnCosts = Maps.newLinkedHashMap();
/* 124 */       this.creatureGenerationProbability = 0.1F;
/*     */     }
/*     */     private float creatureGenerationProbability; private boolean playerCanSpawn;
/*     */     public Builder addSpawn(MobCategory debug1, MobSpawnSettings.SpawnerData debug2) {
/* 128 */       ((List<MobSpawnSettings.SpawnerData>)this.spawners.get(debug1)).add(debug2);
/* 129 */       return this;
/*     */     }
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
/*     */     public Builder addMobCharge(EntityType<?> debug1, double debug2, double debug4) {
/* 155 */       this.mobSpawnCosts.put(debug1, new MobSpawnSettings.MobSpawnCost(debug4, debug2));
/* 156 */       return this;
/*     */     }
/*     */     
/*     */     public Builder creatureGenerationProbability(float debug1) {
/* 160 */       this.creatureGenerationProbability = debug1;
/* 161 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setPlayerCanSpawn() {
/* 165 */       this.playerCanSpawn = true;
/* 166 */       return this;
/*     */     }
/*     */     
/*     */     public MobSpawnSettings build() {
/* 170 */       return new MobSpawnSettings(this.creatureGenerationProbability, (Map)this.spawners
/*     */           
/* 172 */           .entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, debug0 -> ImmutableList.copyOf((Collection)debug0.getValue()))), 
/* 173 */           (Map)ImmutableMap.copyOf(this.mobSpawnCosts), this.playerCanSpawn);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\biome\MobSpawnSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */