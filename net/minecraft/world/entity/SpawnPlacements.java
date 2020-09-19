/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.world.entity.ambient.Bat;
/*     */ import net.minecraft.world.entity.animal.AbstractFish;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.animal.Dolphin;
/*     */ import net.minecraft.world.entity.animal.MushroomCow;
/*     */ import net.minecraft.world.entity.animal.Ocelot;
/*     */ import net.minecraft.world.entity.animal.Parrot;
/*     */ import net.minecraft.world.entity.animal.PolarBear;
/*     */ import net.minecraft.world.entity.animal.Rabbit;
/*     */ import net.minecraft.world.entity.animal.Squid;
/*     */ import net.minecraft.world.entity.animal.Turtle;
/*     */ import net.minecraft.world.entity.monster.Drowned;
/*     */ import net.minecraft.world.entity.monster.Endermite;
/*     */ import net.minecraft.world.entity.monster.Ghast;
/*     */ import net.minecraft.world.entity.monster.Guardian;
/*     */ import net.minecraft.world.entity.monster.Husk;
/*     */ import net.minecraft.world.entity.monster.MagmaCube;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.PatrollingMonster;
/*     */ import net.minecraft.world.entity.monster.Silverfish;
/*     */ import net.minecraft.world.entity.monster.Slime;
/*     */ import net.minecraft.world.entity.monster.Stray;
/*     */ import net.minecraft.world.entity.monster.Strider;
/*     */ import net.minecraft.world.entity.monster.ZombifiedPiglin;
/*     */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*     */ import net.minecraft.world.entity.monster.piglin.Piglin;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ 
/*     */ public class SpawnPlacements
/*     */ {
/*     */   @FunctionalInterface
/*     */   public static interface SpawnPredicate<T extends Entity>
/*     */   {
/*     */     boolean test(EntityType<T> param1EntityType, ServerLevelAccessor param1ServerLevelAccessor, MobSpawnType param1MobSpawnType, BlockPos param1BlockPos, Random param1Random);
/*     */   }
/*     */   
/*     */   static class Data
/*     */   {
/*     */     private final Heightmap.Types heightMap;
/*     */     private final SpawnPlacements.Type placement;
/*     */     private final SpawnPlacements.SpawnPredicate<?> predicate;
/*     */     
/*     */     public Data(Heightmap.Types debug1, SpawnPlacements.Type debug2, SpawnPlacements.SpawnPredicate<?> debug3) {
/*  53 */       this.heightMap = debug1;
/*  54 */       this.placement = debug2;
/*  55 */       this.predicate = debug3;
/*     */     }
/*     */   }
/*     */   
/*  59 */   private static final Map<EntityType<?>, Data> DATA_BY_TYPE = Maps.newHashMap();
/*     */   
/*     */   private static <T extends Mob> void register(EntityType<T> debug0, Type debug1, Heightmap.Types debug2, SpawnPredicate<T> debug3) {
/*  62 */     Data debug4 = DATA_BY_TYPE.put(debug0, new Data(debug2, debug1, debug3));
/*  63 */     if (debug4 != null) {
/*  64 */       throw new IllegalStateException("Duplicate registration for type " + Registry.ENTITY_TYPE.getKey(debug0));
/*     */     }
/*     */   }
/*     */   
/*     */   static {
/*  69 */     register(EntityType.COD, Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractFish::checkFishSpawnRules);
/*  70 */     register(EntityType.DOLPHIN, Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Dolphin::checkDolphinSpawnRules);
/*  71 */     register(EntityType.DROWNED, Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Drowned::checkDrownedSpawnRules);
/*  72 */     register(EntityType.GUARDIAN, Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Guardian::checkGuardianSpawnRules);
/*  73 */     register(EntityType.PUFFERFISH, Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractFish::checkFishSpawnRules);
/*  74 */     register(EntityType.SALMON, Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractFish::checkFishSpawnRules);
/*  75 */     register(EntityType.SQUID, Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Squid::checkSquidSpawnRules);
/*  76 */     register(EntityType.TROPICAL_FISH, Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractFish::checkFishSpawnRules);
/*     */     
/*  78 */     register(EntityType.BAT, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Bat::checkBatSpawnRules);
/*  79 */     register(EntityType.BLAZE, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkAnyLightMonsterSpawnRules);
/*  80 */     register(EntityType.CAVE_SPIDER, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/*  81 */     register(EntityType.CHICKEN, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/*  82 */     register(EntityType.COW, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/*  83 */     register(EntityType.CREEPER, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/*  84 */     register(EntityType.DONKEY, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/*  85 */     register(EntityType.ENDERMAN, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/*  86 */     register(EntityType.ENDERMITE, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Endermite::checkEndermiteSpawnRules);
/*  87 */     register(EntityType.ENDER_DRAGON, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
/*  88 */     register(EntityType.GHAST, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Ghast::checkGhastSpawnRules);
/*  89 */     register(EntityType.GIANT, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/*  90 */     register(EntityType.HORSE, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/*  91 */     register(EntityType.HUSK, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Husk::checkHuskSpawnRules);
/*  92 */     register(EntityType.IRON_GOLEM, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
/*  93 */     register(EntityType.LLAMA, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/*  94 */     register(EntityType.MAGMA_CUBE, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MagmaCube::checkMagmaCubeSpawnRules);
/*  95 */     register(EntityType.MOOSHROOM, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MushroomCow::checkMushroomSpawnRules);
/*  96 */     register(EntityType.MULE, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/*  97 */     register(EntityType.OCELOT, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Ocelot::checkOcelotSpawnRules);
/*  98 */     register(EntityType.PARROT, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Parrot::checkParrotSpawnRules);
/*  99 */     register(EntityType.PIG, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/* 100 */     register(EntityType.HOGLIN, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Hoglin::checkHoglinSpawnRules);
/* 101 */     register(EntityType.PIGLIN, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Piglin::checkPiglinSpawnRules);
/* 102 */     register(EntityType.PILLAGER, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PatrollingMonster::checkPatrollingMonsterSpawnRules);
/* 103 */     register(EntityType.POLAR_BEAR, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PolarBear::checkPolarBearSpawnRules);
/* 104 */     register(EntityType.RABBIT, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Rabbit::checkRabbitSpawnRules);
/* 105 */     register(EntityType.SHEEP, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/* 106 */     register(EntityType.SILVERFISH, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Silverfish::checkSliverfishSpawnRules);
/* 107 */     register(EntityType.SKELETON, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 108 */     register(EntityType.SKELETON_HORSE, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/* 109 */     register(EntityType.SLIME, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Slime::checkSlimeSpawnRules);
/* 110 */     register(EntityType.SNOW_GOLEM, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
/* 111 */     register(EntityType.SPIDER, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 112 */     register(EntityType.STRAY, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Stray::checkStraySpawnRules);
/* 113 */     register(EntityType.STRIDER, Type.IN_LAVA, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Strider::checkStriderSpawnRules);
/* 114 */     register(EntityType.TURTLE, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Turtle::checkTurtleSpawnRules);
/* 115 */     register(EntityType.VILLAGER, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
/* 116 */     register(EntityType.WITCH, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 117 */     register(EntityType.WITHER, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 118 */     register(EntityType.WITHER_SKELETON, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 119 */     register(EntityType.WOLF, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/* 120 */     register(EntityType.ZOMBIE, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 121 */     register(EntityType.ZOMBIE_HORSE, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/* 122 */     register(EntityType.ZOMBIFIED_PIGLIN, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ZombifiedPiglin::checkZombifiedPiglinSpawnRules);
/* 123 */     register(EntityType.ZOMBIE_VILLAGER, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/*     */ 
/*     */     
/* 126 */     register(EntityType.CAT, Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/* 127 */     register(EntityType.ELDER_GUARDIAN, Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Guardian::checkGuardianSpawnRules);
/* 128 */     register(EntityType.EVOKER, Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 129 */     register(EntityType.FOX, Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/* 130 */     register(EntityType.ILLUSIONER, Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 131 */     register(EntityType.PANDA, Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/* 132 */     register(EntityType.PHANTOM, Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
/* 133 */     register(EntityType.RAVAGER, Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 134 */     register(EntityType.SHULKER, Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
/* 135 */     register(EntityType.TRADER_LLAMA, Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/* 136 */     register(EntityType.VEX, Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 137 */     register(EntityType.VINDICATOR, Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 138 */     register(EntityType.WANDERING_TRADER, Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
/*     */   }
/*     */   
/*     */   public static Type getPlacementType(EntityType<?> debug0) {
/* 142 */     Data debug1 = DATA_BY_TYPE.get(debug0);
/* 143 */     return (debug1 == null) ? Type.NO_RESTRICTIONS : debug1.placement;
/*     */   }
/*     */   
/*     */   public static Heightmap.Types getHeightmapType(@Nullable EntityType<?> debug0) {
/* 147 */     Data debug1 = DATA_BY_TYPE.get(debug0);
/* 148 */     return (debug1 == null) ? Heightmap.Types.MOTION_BLOCKING_NO_LEAVES : debug1.heightMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T extends Entity> boolean checkSpawnRules(EntityType<T> debug0, ServerLevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 153 */     Data debug5 = DATA_BY_TYPE.get(debug0);
/* 154 */     return (debug5 == null || debug5.predicate.test(debug0, debug1, debug2, debug3, debug4));
/*     */   }
/*     */   
/*     */   public enum Type {
/* 158 */     ON_GROUND,
/* 159 */     IN_WATER,
/* 160 */     NO_RESTRICTIONS,
/* 161 */     IN_LAVA;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\SpawnPlacements.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */