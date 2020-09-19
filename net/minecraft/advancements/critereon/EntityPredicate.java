/*     */ package net.minecraft.advancements.critereon;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.animal.Cat;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.scores.Team;
/*     */ 
/*     */ public class EntityPredicate
/*     */ {
/*  29 */   public static final EntityPredicate ANY = new EntityPredicate(EntityTypePredicate.ANY, DistancePredicate.ANY, LocationPredicate.ANY, MobEffectsPredicate.ANY, NbtPredicate.ANY, EntityFlagsPredicate.ANY, EntityEquipmentPredicate.ANY, PlayerPredicate.ANY, FishingHookPredicate.ANY, null, null);
/*     */   
/*     */   private final EntityTypePredicate entityType;
/*     */   
/*     */   private final DistancePredicate distanceToPlayer;
/*     */   
/*     */   private final LocationPredicate location;
/*     */   
/*     */   private final MobEffectsPredicate effects;
/*     */   private final NbtPredicate nbt;
/*     */   private final EntityFlagsPredicate flags;
/*     */   private final EntityEquipmentPredicate equipment;
/*     */   private final PlayerPredicate player;
/*     */   private final FishingHookPredicate fishingHook;
/*     */   private final EntityPredicate vehicle;
/*     */   private final EntityPredicate targetedEntity;
/*     */   @Nullable
/*     */   private final String team;
/*     */   @Nullable
/*     */   private final ResourceLocation catType;
/*     */   
/*     */   private EntityPredicate(EntityTypePredicate debug1, DistancePredicate debug2, LocationPredicate debug3, MobEffectsPredicate debug4, NbtPredicate debug5, EntityFlagsPredicate debug6, EntityEquipmentPredicate debug7, PlayerPredicate debug8, FishingHookPredicate debug9, @Nullable String debug10, @Nullable ResourceLocation debug11) {
/*  51 */     this.entityType = debug1;
/*  52 */     this.distanceToPlayer = debug2;
/*  53 */     this.location = debug3;
/*  54 */     this.effects = debug4;
/*  55 */     this.nbt = debug5;
/*  56 */     this.flags = debug6;
/*  57 */     this.equipment = debug7;
/*  58 */     this.player = debug8;
/*  59 */     this.fishingHook = debug9;
/*  60 */     this.vehicle = this;
/*  61 */     this.targetedEntity = this;
/*  62 */     this.team = debug10;
/*  63 */     this.catType = debug11;
/*     */   }
/*     */   
/*     */   private EntityPredicate(EntityTypePredicate debug1, DistancePredicate debug2, LocationPredicate debug3, MobEffectsPredicate debug4, NbtPredicate debug5, EntityFlagsPredicate debug6, EntityEquipmentPredicate debug7, PlayerPredicate debug8, FishingHookPredicate debug9, EntityPredicate debug10, EntityPredicate debug11, @Nullable String debug12, @Nullable ResourceLocation debug13) {
/*  67 */     this.entityType = debug1;
/*  68 */     this.distanceToPlayer = debug2;
/*  69 */     this.location = debug3;
/*  70 */     this.effects = debug4;
/*  71 */     this.nbt = debug5;
/*  72 */     this.flags = debug6;
/*  73 */     this.equipment = debug7;
/*  74 */     this.player = debug8;
/*  75 */     this.fishingHook = debug9;
/*  76 */     this.vehicle = debug10;
/*  77 */     this.targetedEntity = debug11;
/*  78 */     this.team = debug12;
/*  79 */     this.catType = debug13;
/*     */   }
/*     */   
/*     */   public boolean matches(ServerPlayer debug1, @Nullable Entity debug2) {
/*  83 */     return matches(debug1.getLevel(), debug1.position(), debug2);
/*     */   }
/*     */   
/*     */   public boolean matches(ServerLevel debug1, @Nullable Vec3 debug2, @Nullable Entity debug3) {
/*  87 */     if (this == ANY) {
/*  88 */       return true;
/*     */     }
/*  90 */     if (debug3 == null) {
/*  91 */       return false;
/*     */     }
/*  93 */     if (!this.entityType.matches(debug3.getType())) {
/*  94 */       return false;
/*     */     }
/*  96 */     if (debug2 == null) {
/*  97 */       if (this.distanceToPlayer != DistancePredicate.ANY) {
/*  98 */         return false;
/*     */       }
/*     */     }
/* 101 */     else if (!this.distanceToPlayer.matches(debug2.x, debug2.y, debug2.z, debug3.getX(), debug3.getY(), debug3.getZ())) {
/* 102 */       return false;
/*     */     } 
/*     */     
/* 105 */     if (!this.location.matches(debug1, debug3.getX(), debug3.getY(), debug3.getZ())) {
/* 106 */       return false;
/*     */     }
/* 108 */     if (!this.effects.matches(debug3)) {
/* 109 */       return false;
/*     */     }
/* 111 */     if (!this.nbt.matches(debug3)) {
/* 112 */       return false;
/*     */     }
/* 114 */     if (!this.flags.matches(debug3)) {
/* 115 */       return false;
/*     */     }
/*     */     
/* 118 */     if (!this.equipment.matches(debug3)) {
/* 119 */       return false;
/*     */     }
/*     */     
/* 122 */     if (!this.player.matches(debug3)) {
/* 123 */       return false;
/*     */     }
/*     */     
/* 126 */     if (!this.fishingHook.matches(debug3)) {
/* 127 */       return false;
/*     */     }
/*     */     
/* 130 */     if (!this.vehicle.matches(debug1, debug2, debug3.getVehicle())) {
/* 131 */       return false;
/*     */     }
/*     */     
/* 134 */     if (!this.targetedEntity.matches(debug1, debug2, (debug3 instanceof Mob) ? (Entity)((Mob)debug3).getTarget() : null)) {
/* 135 */       return false;
/*     */     }
/*     */     
/* 138 */     if (this.team != null) {
/* 139 */       Team debug4 = debug3.getTeam();
/* 140 */       if (debug4 == null || !this.team.equals(debug4.getName())) {
/* 141 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 145 */     if (this.catType != null && (
/* 146 */       !(debug3 instanceof Cat) || !((Cat)debug3).getResourceLocation().equals(this.catType))) {
/* 147 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 151 */     return true;
/*     */   }
/*     */   
/*     */   public static EntityPredicate fromJson(@Nullable JsonElement debug0) {
/* 155 */     if (debug0 == null || debug0.isJsonNull()) {
/* 156 */       return ANY;
/*     */     }
/*     */     
/* 159 */     JsonObject debug1 = GsonHelper.convertToJsonObject(debug0, "entity");
/*     */     
/* 161 */     EntityTypePredicate debug2 = EntityTypePredicate.fromJson(debug1.get("type"));
/* 162 */     DistancePredicate debug3 = DistancePredicate.fromJson(debug1.get("distance"));
/* 163 */     LocationPredicate debug4 = LocationPredicate.fromJson(debug1.get("location"));
/* 164 */     MobEffectsPredicate debug5 = MobEffectsPredicate.fromJson(debug1.get("effects"));
/* 165 */     NbtPredicate debug6 = NbtPredicate.fromJson(debug1.get("nbt"));
/* 166 */     EntityFlagsPredicate debug7 = EntityFlagsPredicate.fromJson(debug1.get("flags"));
/* 167 */     EntityEquipmentPredicate debug8 = EntityEquipmentPredicate.fromJson(debug1.get("equipment"));
/* 168 */     PlayerPredicate debug9 = PlayerPredicate.fromJson(debug1.get("player"));
/* 169 */     FishingHookPredicate debug10 = FishingHookPredicate.fromJson(debug1.get("fishing_hook"));
/* 170 */     EntityPredicate debug11 = fromJson(debug1.get("vehicle"));
/* 171 */     EntityPredicate debug12 = fromJson(debug1.get("targeted_entity"));
/* 172 */     String debug13 = GsonHelper.getAsString(debug1, "team", null);
/* 173 */     ResourceLocation debug14 = debug1.has("catType") ? new ResourceLocation(GsonHelper.getAsString(debug1, "catType")) : null;
/*     */     
/* 175 */     return (new Builder())
/* 176 */       .entityType(debug2)
/* 177 */       .distance(debug3)
/* 178 */       .located(debug4)
/* 179 */       .effects(debug5)
/* 180 */       .nbt(debug6)
/* 181 */       .flags(debug7)
/* 182 */       .equipment(debug8)
/* 183 */       .player(debug9)
/* 184 */       .fishingHook(debug10)
/* 185 */       .team(debug13)
/* 186 */       .vehicle(debug11)
/* 187 */       .targetedEntity(debug12)
/* 188 */       .catType(debug14)
/* 189 */       .build();
/*     */   }
/*     */   
/*     */   public JsonElement serializeToJson() {
/* 193 */     if (this == ANY) {
/* 194 */       return (JsonElement)JsonNull.INSTANCE;
/*     */     }
/*     */     
/* 197 */     JsonObject debug1 = new JsonObject();
/*     */     
/* 199 */     debug1.add("type", this.entityType.serializeToJson());
/* 200 */     debug1.add("distance", this.distanceToPlayer.serializeToJson());
/* 201 */     debug1.add("location", this.location.serializeToJson());
/* 202 */     debug1.add("effects", this.effects.serializeToJson());
/* 203 */     debug1.add("nbt", this.nbt.serializeToJson());
/* 204 */     debug1.add("flags", this.flags.serializeToJson());
/* 205 */     debug1.add("equipment", this.equipment.serializeToJson());
/* 206 */     debug1.add("player", this.player.serializeToJson());
/* 207 */     debug1.add("fishing_hook", this.fishingHook.serializeToJson());
/* 208 */     debug1.add("vehicle", this.vehicle.serializeToJson());
/* 209 */     debug1.add("targeted_entity", this.targetedEntity.serializeToJson());
/* 210 */     debug1.addProperty("team", this.team);
/* 211 */     if (this.catType != null) {
/* 212 */       debug1.addProperty("catType", this.catType.toString());
/*     */     }
/*     */     
/* 215 */     return (JsonElement)debug1;
/*     */   }
/*     */   
/*     */   public static LootContext createContext(ServerPlayer debug0, Entity debug1) {
/* 219 */     return (new LootContext.Builder(debug0.getLevel()))
/* 220 */       .withParameter(LootContextParams.THIS_ENTITY, debug1)
/* 221 */       .withParameter(LootContextParams.ORIGIN, debug0.position())
/* 222 */       .withRandom(debug0.getRandom())
/* 223 */       .create(LootContextParamSets.ADVANCEMENT_ENTITY);
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 227 */     private EntityTypePredicate entityType = EntityTypePredicate.ANY;
/* 228 */     private DistancePredicate distanceToPlayer = DistancePredicate.ANY;
/* 229 */     private LocationPredicate location = LocationPredicate.ANY;
/* 230 */     private MobEffectsPredicate effects = MobEffectsPredicate.ANY;
/* 231 */     private NbtPredicate nbt = NbtPredicate.ANY;
/* 232 */     private EntityFlagsPredicate flags = EntityFlagsPredicate.ANY;
/* 233 */     private EntityEquipmentPredicate equipment = EntityEquipmentPredicate.ANY;
/* 234 */     private PlayerPredicate player = PlayerPredicate.ANY;
/* 235 */     private FishingHookPredicate fishingHook = FishingHookPredicate.ANY;
/* 236 */     private EntityPredicate vehicle = EntityPredicate.ANY;
/* 237 */     private EntityPredicate targetedEntity = EntityPredicate.ANY;
/*     */     private String team;
/*     */     private ResourceLocation catType;
/*     */     
/*     */     public static Builder entity() {
/* 242 */       return new Builder();
/*     */     }
/*     */     
/*     */     public Builder of(EntityType<?> debug1) {
/* 246 */       this.entityType = EntityTypePredicate.of(debug1);
/* 247 */       return this;
/*     */     }
/*     */     
/*     */     public Builder of(Tag<EntityType<?>> debug1) {
/* 251 */       this.entityType = EntityTypePredicate.of(debug1);
/* 252 */       return this;
/*     */     }
/*     */     
/*     */     public Builder of(ResourceLocation debug1) {
/* 256 */       this.catType = debug1;
/* 257 */       return this;
/*     */     }
/*     */     
/*     */     public Builder entityType(EntityTypePredicate debug1) {
/* 261 */       this.entityType = debug1;
/* 262 */       return this;
/*     */     }
/*     */     
/*     */     public Builder distance(DistancePredicate debug1) {
/* 266 */       this.distanceToPlayer = debug1;
/* 267 */       return this;
/*     */     }
/*     */     
/*     */     public Builder located(LocationPredicate debug1) {
/* 271 */       this.location = debug1;
/* 272 */       return this;
/*     */     }
/*     */     
/*     */     public Builder effects(MobEffectsPredicate debug1) {
/* 276 */       this.effects = debug1;
/* 277 */       return this;
/*     */     }
/*     */     
/*     */     public Builder nbt(NbtPredicate debug1) {
/* 281 */       this.nbt = debug1;
/* 282 */       return this;
/*     */     }
/*     */     
/*     */     public Builder flags(EntityFlagsPredicate debug1) {
/* 286 */       this.flags = debug1;
/* 287 */       return this;
/*     */     }
/*     */     
/*     */     public Builder equipment(EntityEquipmentPredicate debug1) {
/* 291 */       this.equipment = debug1;
/* 292 */       return this;
/*     */     }
/*     */     
/*     */     public Builder player(PlayerPredicate debug1) {
/* 296 */       this.player = debug1;
/* 297 */       return this;
/*     */     }
/*     */     
/*     */     public Builder fishingHook(FishingHookPredicate debug1) {
/* 301 */       this.fishingHook = debug1;
/* 302 */       return this;
/*     */     }
/*     */     
/*     */     public Builder vehicle(EntityPredicate debug1) {
/* 306 */       this.vehicle = debug1;
/* 307 */       return this;
/*     */     }
/*     */     
/*     */     public Builder targetedEntity(EntityPredicate debug1) {
/* 311 */       this.targetedEntity = debug1;
/* 312 */       return this;
/*     */     }
/*     */     
/*     */     public Builder team(@Nullable String debug1) {
/* 316 */       this.team = debug1;
/* 317 */       return this;
/*     */     }
/*     */     
/*     */     public Builder catType(@Nullable ResourceLocation debug1) {
/* 321 */       this.catType = debug1;
/* 322 */       return this;
/*     */     }
/*     */     
/*     */     public EntityPredicate build() {
/* 326 */       return new EntityPredicate(this.entityType, this.distanceToPlayer, this.location, this.effects, this.nbt, this.flags, this.equipment, this.player, this.fishingHook, this.vehicle, this.targetedEntity, this.team, this.catType);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Composite {
/* 331 */     public static final Composite ANY = new Composite(new LootItemCondition[0]);
/*     */     
/*     */     private final LootItemCondition[] conditions;
/*     */     private final Predicate<LootContext> compositePredicates;
/*     */     
/*     */     private Composite(LootItemCondition[] debug1) {
/* 337 */       this.conditions = debug1;
/* 338 */       this.compositePredicates = LootItemConditions.andConditions((Predicate[])debug1);
/*     */     }
/*     */     
/*     */     public static Composite create(LootItemCondition... debug0) {
/* 342 */       return new Composite(debug0);
/*     */     }
/*     */     
/*     */     public static Composite fromJson(JsonObject debug0, String debug1, DeserializationContext debug2) {
/* 346 */       JsonElement debug3 = debug0.get(debug1);
/* 347 */       return fromElement(debug1, debug2, debug3);
/*     */     }
/*     */     
/*     */     public static Composite[] fromJsonArray(JsonObject debug0, String debug1, DeserializationContext debug2) {
/* 351 */       JsonElement debug3 = debug0.get(debug1);
/* 352 */       if (debug3 == null || debug3.isJsonNull()) {
/* 353 */         return new Composite[0];
/*     */       }
/* 355 */       JsonArray debug4 = GsonHelper.convertToJsonArray(debug3, debug1);
/* 356 */       Composite[] debug5 = new Composite[debug4.size()];
/*     */       
/* 358 */       for (int debug6 = 0; debug6 < debug4.size(); debug6++) {
/* 359 */         debug5[debug6] = fromElement(debug1 + "[" + debug6 + "]", debug2, debug4.get(debug6));
/*     */       }
/*     */       
/* 362 */       return debug5;
/*     */     }
/*     */     
/*     */     private static Composite fromElement(String debug0, DeserializationContext debug1, @Nullable JsonElement debug2) {
/* 366 */       if (debug2 != null && debug2.isJsonArray()) {
/* 367 */         LootItemCondition[] arrayOfLootItemCondition = debug1.deserializeConditions(debug2.getAsJsonArray(), debug1.getAdvancementId().toString() + "/" + debug0, LootContextParamSets.ADVANCEMENT_ENTITY);
/* 368 */         return new Composite(arrayOfLootItemCondition);
/*     */       } 
/*     */ 
/*     */       
/* 372 */       EntityPredicate debug3 = EntityPredicate.fromJson(debug2);
/* 373 */       return wrap(debug3);
/*     */     }
/*     */     
/*     */     public static Composite wrap(EntityPredicate debug0) {
/* 377 */       if (debug0 == EntityPredicate.ANY) {
/* 378 */         return ANY;
/*     */       }
/* 380 */       LootItemCondition debug1 = LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, debug0).build();
/* 381 */       return new Composite(new LootItemCondition[] { debug1 });
/*     */     }
/*     */     
/*     */     public boolean matches(LootContext debug1) {
/* 385 */       return this.compositePredicates.test(debug1);
/*     */     }
/*     */     
/*     */     public JsonElement toJson(SerializationContext debug1) {
/* 389 */       if (this.conditions.length == 0) {
/* 390 */         return (JsonElement)JsonNull.INSTANCE;
/*     */       }
/*     */       
/* 393 */       return debug1.serializeConditions(this.conditions);
/*     */     }
/*     */     
/*     */     public static JsonElement toJson(Composite[] debug0, SerializationContext debug1) {
/* 397 */       if (debug0.length == 0) {
/* 398 */         return (JsonElement)JsonNull.INSTANCE;
/*     */       }
/*     */       
/* 401 */       JsonArray debug2 = new JsonArray();
/* 402 */       for (Composite debug6 : debug0) {
/* 403 */         debug2.add(debug6.toJson(debug1));
/*     */       }
/* 405 */       return (JsonElement)debug2;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\EntityPredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */