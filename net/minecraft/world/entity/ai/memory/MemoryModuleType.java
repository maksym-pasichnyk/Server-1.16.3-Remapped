/*     */ package net.minecraft.world.entity.ai.memory;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.SerializableUUID;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.behavior.PositionTracker;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*     */ import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ 
/*     */ 
/*     */ public class MemoryModuleType<U>
/*     */ {
/*  28 */   public static final MemoryModuleType<Void> DUMMY = register("dummy");
/*  29 */   public static final MemoryModuleType<GlobalPos> HOME = register("home", GlobalPos.CODEC);
/*  30 */   public static final MemoryModuleType<GlobalPos> JOB_SITE = register("job_site", GlobalPos.CODEC);
/*  31 */   public static final MemoryModuleType<GlobalPos> POTENTIAL_JOB_SITE = register("potential_job_site", GlobalPos.CODEC);
/*  32 */   public static final MemoryModuleType<GlobalPos> MEETING_POINT = register("meeting_point", GlobalPos.CODEC);
/*  33 */   public static final MemoryModuleType<List<GlobalPos>> SECONDARY_JOB_SITE = register("secondary_job_site");
/*  34 */   public static final MemoryModuleType<List<LivingEntity>> LIVING_ENTITIES = register("mobs");
/*  35 */   public static final MemoryModuleType<List<LivingEntity>> VISIBLE_LIVING_ENTITIES = register("visible_mobs");
/*  36 */   public static final MemoryModuleType<List<LivingEntity>> VISIBLE_VILLAGER_BABIES = register("visible_villager_babies");
/*  37 */   public static final MemoryModuleType<List<Player>> NEAREST_PLAYERS = register("nearest_players");
/*  38 */   public static final MemoryModuleType<Player> NEAREST_VISIBLE_PLAYER = register("nearest_visible_player");
/*  39 */   public static final MemoryModuleType<Player> NEAREST_VISIBLE_TARGETABLE_PLAYER = register("nearest_visible_targetable_player");
/*  40 */   public static final MemoryModuleType<WalkTarget> WALK_TARGET = register("walk_target");
/*  41 */   public static final MemoryModuleType<PositionTracker> LOOK_TARGET = register("look_target");
/*  42 */   public static final MemoryModuleType<LivingEntity> ATTACK_TARGET = register("attack_target");
/*  43 */   public static final MemoryModuleType<Boolean> ATTACK_COOLING_DOWN = register("attack_cooling_down");
/*  44 */   public static final MemoryModuleType<LivingEntity> INTERACTION_TARGET = register("interaction_target");
/*  45 */   public static final MemoryModuleType<AgableMob> BREED_TARGET = register("breed_target");
/*  46 */   public static final MemoryModuleType<Entity> RIDE_TARGET = register("ride_target");
/*  47 */   public static final MemoryModuleType<Path> PATH = register("path");
/*  48 */   public static final MemoryModuleType<List<GlobalPos>> INTERACTABLE_DOORS = register("interactable_doors");
/*  49 */   public static final MemoryModuleType<Set<GlobalPos>> DOORS_TO_CLOSE = register("doors_to_close");
/*  50 */   public static final MemoryModuleType<BlockPos> NEAREST_BED = register("nearest_bed");
/*  51 */   public static final MemoryModuleType<DamageSource> HURT_BY = register("hurt_by");
/*  52 */   public static final MemoryModuleType<LivingEntity> HURT_BY_ENTITY = register("hurt_by_entity");
/*  53 */   public static final MemoryModuleType<LivingEntity> AVOID_TARGET = register("avoid_target");
/*  54 */   public static final MemoryModuleType<LivingEntity> NEAREST_HOSTILE = register("nearest_hostile");
/*  55 */   public static final MemoryModuleType<GlobalPos> HIDING_PLACE = register("hiding_place");
/*  56 */   public static final MemoryModuleType<Long> HEARD_BELL_TIME = register("heard_bell_time");
/*  57 */   public static final MemoryModuleType<Long> CANT_REACH_WALK_TARGET_SINCE = register("cant_reach_walk_target_since");
/*  58 */   public static final MemoryModuleType<Boolean> GOLEM_DETECTED_RECENTLY = register("golem_detected_recently", (Codec<Boolean>)Codec.BOOL);
/*  59 */   public static final MemoryModuleType<Long> LAST_SLEPT = register("last_slept", (Codec<Long>)Codec.LONG);
/*  60 */   public static final MemoryModuleType<Long> LAST_WOKEN = register("last_woken", (Codec<Long>)Codec.LONG);
/*  61 */   public static final MemoryModuleType<Long> LAST_WORKED_AT_POI = register("last_worked_at_poi", (Codec<Long>)Codec.LONG);
/*  62 */   public static final MemoryModuleType<AgableMob> NEAREST_VISIBLE_ADULT = register("nearest_visible_adult");
/*  63 */   public static final MemoryModuleType<ItemEntity> NEAREST_VISIBLE_WANTED_ITEM = register("nearest_visible_wanted_item");
/*  64 */   public static final MemoryModuleType<Mob> NEAREST_VISIBLE_NEMESIS = register("nearest_visible_nemesis");
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
/*  75 */   public static final MemoryModuleType<UUID> ANGRY_AT = register("angry_at", SerializableUUID.CODEC);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   public static final MemoryModuleType<Boolean> UNIVERSAL_ANGER = register("universal_anger", (Codec<Boolean>)Codec.BOOL);
/*  81 */   public static final MemoryModuleType<Boolean> ADMIRING_ITEM = register("admiring_item", (Codec<Boolean>)Codec.BOOL);
/*  82 */   public static final MemoryModuleType<Integer> TIME_TRYING_TO_REACH_ADMIRE_ITEM = register("time_trying_to_reach_admire_item");
/*  83 */   public static final MemoryModuleType<Boolean> DISABLE_WALK_TO_ADMIRE_ITEM = register("disable_walk_to_admire_item");
/*  84 */   public static final MemoryModuleType<Boolean> ADMIRING_DISABLED = register("admiring_disabled", (Codec<Boolean>)Codec.BOOL);
/*  85 */   public static final MemoryModuleType<Boolean> HUNTED_RECENTLY = register("hunted_recently", (Codec<Boolean>)Codec.BOOL);
/*     */   
/*  87 */   public static final MemoryModuleType<BlockPos> CELEBRATE_LOCATION = register("celebrate_location");
/*  88 */   public static final MemoryModuleType<Boolean> DANCING = register("dancing");
/*  89 */   public static final MemoryModuleType<Hoglin> NEAREST_VISIBLE_HUNTABLE_HOGLIN = register("nearest_visible_huntable_hoglin");
/*  90 */   public static final MemoryModuleType<Hoglin> NEAREST_VISIBLE_BABY_HOGLIN = register("nearest_visible_baby_hoglin");
/*  91 */   public static final MemoryModuleType<Player> NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD = register("nearest_targetable_player_not_wearing_gold");
/*  92 */   public static final MemoryModuleType<List<AbstractPiglin>> NEARBY_ADULT_PIGLINS = register("nearby_adult_piglins");
/*  93 */   public static final MemoryModuleType<List<AbstractPiglin>> NEAREST_VISIBLE_ADULT_PIGLINS = register("nearest_visible_adult_piglins");
/*  94 */   public static final MemoryModuleType<List<Hoglin>> NEAREST_VISIBLE_ADULT_HOGLINS = register("nearest_visible_adult_hoglins");
/*     */   
/*  96 */   public static final MemoryModuleType<AbstractPiglin> NEAREST_VISIBLE_ADULT_PIGLIN = register("nearest_visible_adult_piglin");
/*  97 */   public static final MemoryModuleType<LivingEntity> NEAREST_VISIBLE_ZOMBIFIED = register("nearest_visible_zombified");
/*  98 */   public static final MemoryModuleType<Integer> VISIBLE_ADULT_PIGLIN_COUNT = register("visible_adult_piglin_count");
/*  99 */   public static final MemoryModuleType<Integer> VISIBLE_ADULT_HOGLIN_COUNT = register("visible_adult_hoglin_count");
/* 100 */   public static final MemoryModuleType<Player> NEAREST_PLAYER_HOLDING_WANTED_ITEM = register("nearest_player_holding_wanted_item");
/* 101 */   public static final MemoryModuleType<Boolean> ATE_RECENTLY = register("ate_recently");
/* 102 */   public static final MemoryModuleType<BlockPos> NEAREST_REPELLENT = register("nearest_repellent");
/* 103 */   public static final MemoryModuleType<Boolean> PACIFIED = register("pacified");
/*     */   
/*     */   private final Optional<Codec<ExpirableValue<U>>> codec;
/*     */   
/*     */   private MemoryModuleType(Optional<Codec<U>> debug1) {
/* 108 */     this.codec = debug1.map(ExpirableValue::codec);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 113 */     return Registry.MEMORY_MODULE_TYPE.getKey(this).toString();
/*     */   }
/*     */   
/*     */   public Optional<Codec<ExpirableValue<U>>> getCodec() {
/* 117 */     return this.codec;
/*     */   }
/*     */   
/*     */   private static <U> MemoryModuleType<U> register(String debug0, Codec<U> debug1) {
/* 121 */     return (MemoryModuleType<U>)Registry.register((Registry)Registry.MEMORY_MODULE_TYPE, new ResourceLocation(debug0), new MemoryModuleType(Optional.of(debug1)));
/*     */   }
/*     */   
/*     */   private static <U> MemoryModuleType<U> register(String debug0) {
/* 125 */     return (MemoryModuleType<U>)Registry.register((Registry)Registry.MEMORY_MODULE_TYPE, new ResourceLocation(debug0), new MemoryModuleType(Optional.empty()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\memory\MemoryModuleType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */