/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ 
/*    */ public class SensorType<U extends Sensor<?>>
/*    */ {
/*  9 */   public static final SensorType<DummySensor> DUMMY = register("dummy", DummySensor::new);
/* 10 */   public static final SensorType<NearestItemSensor> NEAREST_ITEMS = register("nearest_items", NearestItemSensor::new);
/* 11 */   public static final SensorType<NearestLivingEntitySensor> NEAREST_LIVING_ENTITIES = register("nearest_living_entities", NearestLivingEntitySensor::new);
/* 12 */   public static final SensorType<PlayerSensor> NEAREST_PLAYERS = register("nearest_players", PlayerSensor::new);
/* 13 */   public static final SensorType<NearestBedSensor> NEAREST_BED = register("nearest_bed", NearestBedSensor::new);
/* 14 */   public static final SensorType<HurtBySensor> HURT_BY = register("hurt_by", HurtBySensor::new);
/* 15 */   public static final SensorType<VillagerHostilesSensor> VILLAGER_HOSTILES = register("villager_hostiles", VillagerHostilesSensor::new);
/* 16 */   public static final SensorType<VillagerBabiesSensor> VILLAGER_BABIES = register("villager_babies", VillagerBabiesSensor::new);
/* 17 */   public static final SensorType<SecondaryPoiSensor> SECONDARY_POIS = register("secondary_pois", SecondaryPoiSensor::new);
/* 18 */   public static final SensorType<GolemSensor> GOLEM_DETECTED = register("golem_detected", GolemSensor::new);
/* 19 */   public static final SensorType<PiglinSpecificSensor> PIGLIN_SPECIFIC_SENSOR = register("piglin_specific_sensor", PiglinSpecificSensor::new);
/* 20 */   public static final SensorType<PiglinBruteSpecificSensor> PIGLIN_BRUTE_SPECIFIC_SENSOR = register("piglin_brute_specific_sensor", PiglinBruteSpecificSensor::new);
/* 21 */   public static final SensorType<HoglinSpecificSensor> HOGLIN_SPECIFIC_SENSOR = register("hoglin_specific_sensor", HoglinSpecificSensor::new);
/* 22 */   public static final SensorType<AdultSensor> NEAREST_ADULT = register("nearest_adult", AdultSensor::new);
/*    */   
/*    */   private final Supplier<U> factory;
/*    */   
/*    */   private SensorType(Supplier<U> debug1) {
/* 27 */     this.factory = debug1;
/*    */   }
/*    */   
/*    */   public U create() {
/* 31 */     return this.factory.get();
/*    */   }
/*    */   
/*    */   private static <U extends Sensor<?>> SensorType<U> register(String debug0, Supplier<U> debug1) {
/* 35 */     return (SensorType<U>)Registry.register((Registry)Registry.SENSOR_TYPE, new ResourceLocation(debug0), new SensorType<>(debug1));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\sensing\SensorType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */