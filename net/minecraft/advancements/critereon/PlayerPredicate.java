/*     */ package net.minecraft.advancements.critereon;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.Map;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.advancements.CriterionProgress;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.PlayerAdvancements;
/*     */ import net.minecraft.server.ServerAdvancementManager;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.stats.ServerRecipeBook;
/*     */ import net.minecraft.stats.ServerStatsCounter;
/*     */ import net.minecraft.stats.Stat;
/*     */ import net.minecraft.stats.StatType;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.GameType;
/*     */ 
/*     */ public class PlayerPredicate {
/*     */   private final MinMaxBounds.Ints level;
/*     */   private final GameType gameType;
/*     */   private final Map<Stat<?>, MinMaxBounds.Ints> stats;
/*  33 */   public static final PlayerPredicate ANY = (new Builder()).build();
/*     */   private final Object2BooleanMap<ResourceLocation> recipes;
/*     */   private final Map<ResourceLocation, AdvancementPredicate> advancements;
/*     */   
/*     */   static interface AdvancementPredicate extends Predicate<AdvancementProgress> {
/*     */     JsonElement toJson();
/*     */   }
/*     */   
/*     */   static class AdvancementDonePredicate implements AdvancementPredicate {
/*     */     public AdvancementDonePredicate(boolean debug1) {
/*  43 */       this.state = debug1;
/*     */     }
/*     */     private final boolean state;
/*     */     
/*     */     public JsonElement toJson() {
/*  48 */       return (JsonElement)new JsonPrimitive(Boolean.valueOf(this.state));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(AdvancementProgress debug1) {
/*  53 */       return (debug1.isDone() == this.state);
/*     */     }
/*     */   }
/*     */   
/*     */   static class AdvancementCriterionsPredicate implements AdvancementPredicate {
/*     */     private final Object2BooleanMap<String> criterions;
/*     */     
/*     */     public AdvancementCriterionsPredicate(Object2BooleanMap<String> debug1) {
/*  61 */       this.criterions = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonElement toJson() {
/*  66 */       JsonObject debug1 = new JsonObject();
/*  67 */       this.criterions.forEach(debug1::addProperty);
/*  68 */       return (JsonElement)debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(AdvancementProgress debug1) {
/*  73 */       for (ObjectIterator<Object2BooleanMap.Entry<String>> objectIterator = this.criterions.object2BooleanEntrySet().iterator(); objectIterator.hasNext(); ) { Object2BooleanMap.Entry<String> debug3 = objectIterator.next();
/*  74 */         CriterionProgress debug4 = debug1.getCriterion((String)debug3.getKey());
/*  75 */         if (debug4 == null || debug4.isDone() != debug3.getBooleanValue()) {
/*  76 */           return false;
/*     */         } }
/*     */       
/*  79 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   private static AdvancementPredicate advancementPredicateFromJson(JsonElement debug0) {
/*  84 */     if (debug0.isJsonPrimitive()) {
/*  85 */       boolean debug1 = debug0.getAsBoolean();
/*  86 */       return new AdvancementDonePredicate(debug1);
/*     */     } 
/*     */     
/*  89 */     Object2BooleanOpenHashMap object2BooleanOpenHashMap = new Object2BooleanOpenHashMap();
/*  90 */     JsonObject debug2 = GsonHelper.convertToJsonObject(debug0, "criterion data");
/*  91 */     debug2.entrySet().forEach(debug1 -> {
/*     */           boolean debug2 = GsonHelper.convertToBoolean((JsonElement)debug1.getValue(), "criterion test");
/*     */           debug0.put(debug1.getKey(), debug2);
/*     */         });
/*  95 */     return new AdvancementCriterionsPredicate((Object2BooleanMap<String>)object2BooleanOpenHashMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PlayerPredicate(MinMaxBounds.Ints debug1, GameType debug2, Map<Stat<?>, MinMaxBounds.Ints> debug3, Object2BooleanMap<ResourceLocation> debug4, Map<ResourceLocation, AdvancementPredicate> debug5) {
/* 105 */     this.level = debug1;
/* 106 */     this.gameType = debug2;
/* 107 */     this.stats = debug3;
/* 108 */     this.recipes = debug4;
/* 109 */     this.advancements = debug5;
/*     */   }
/*     */   
/*     */   public boolean matches(Entity debug1) {
/* 113 */     if (this == ANY) {
/* 114 */       return true;
/*     */     }
/*     */     
/* 117 */     if (!(debug1 instanceof ServerPlayer)) {
/* 118 */       return false;
/*     */     }
/*     */     
/* 121 */     ServerPlayer debug2 = (ServerPlayer)debug1;
/*     */     
/* 123 */     if (!this.level.matches(debug2.experienceLevel)) {
/* 124 */       return false;
/*     */     }
/*     */     
/* 127 */     if (this.gameType != GameType.NOT_SET && this.gameType != debug2.gameMode.getGameModeForPlayer()) {
/* 128 */       return false;
/*     */     }
/*     */     
/* 131 */     ServerStatsCounter serverStatsCounter = debug2.getStats();
/* 132 */     for (Map.Entry<Stat<?>, MinMaxBounds.Ints> debug5 : this.stats.entrySet()) {
/* 133 */       int debug6 = serverStatsCounter.getValue(debug5.getKey());
/* 134 */       if (!((MinMaxBounds.Ints)debug5.getValue()).matches(debug6)) {
/* 135 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 139 */     ServerRecipeBook serverRecipeBook = debug2.getRecipeBook();
/* 140 */     for (ObjectIterator<Object2BooleanMap.Entry<ResourceLocation>> objectIterator = this.recipes.object2BooleanEntrySet().iterator(); objectIterator.hasNext(); ) { Object2BooleanMap.Entry<ResourceLocation> debug6 = objectIterator.next();
/* 141 */       if (serverRecipeBook.contains((ResourceLocation)debug6.getKey()) != debug6.getBooleanValue()) {
/* 142 */         return false;
/*     */       } }
/*     */ 
/*     */     
/* 146 */     if (!this.advancements.isEmpty()) {
/* 147 */       PlayerAdvancements debug5 = debug2.getAdvancements();
/* 148 */       ServerAdvancementManager debug6 = debug2.getServer().getAdvancements();
/*     */       
/* 150 */       for (Map.Entry<ResourceLocation, AdvancementPredicate> debug8 : this.advancements.entrySet()) {
/* 151 */         Advancement debug9 = debug6.getAdvancement(debug8.getKey());
/* 152 */         if (debug9 == null || !((AdvancementPredicate)debug8.getValue()).test(debug5.getOrStartProgress(debug9))) {
/* 153 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 158 */     return true;
/*     */   }
/*     */   
/*     */   public static PlayerPredicate fromJson(@Nullable JsonElement debug0) {
/* 162 */     if (debug0 == null || debug0.isJsonNull()) {
/* 163 */       return ANY;
/*     */     }
/*     */     
/* 166 */     JsonObject debug1 = GsonHelper.convertToJsonObject(debug0, "player");
/* 167 */     MinMaxBounds.Ints debug2 = MinMaxBounds.Ints.fromJson(debug1.get("level"));
/*     */     
/* 169 */     String debug3 = GsonHelper.getAsString(debug1, "gamemode", "");
/* 170 */     GameType debug4 = GameType.byName(debug3, GameType.NOT_SET);
/*     */     
/* 172 */     Map<Stat<?>, MinMaxBounds.Ints> debug5 = Maps.newHashMap();
/* 173 */     JsonArray debug6 = GsonHelper.getAsJsonArray(debug1, "stats", null);
/* 174 */     if (debug6 != null) {
/* 175 */       for (JsonElement jsonElement : debug6) {
/* 176 */         JsonObject jsonObject = GsonHelper.convertToJsonObject(jsonElement, "stats entry");
/* 177 */         ResourceLocation resourceLocation1 = new ResourceLocation(GsonHelper.getAsString(jsonObject, "type"));
/*     */         
/* 179 */         StatType<?> debug11 = (StatType)Registry.STAT_TYPE.get(resourceLocation1);
/* 180 */         if (debug11 == null) {
/* 181 */           throw new JsonParseException("Invalid stat type: " + resourceLocation1);
/*     */         }
/*     */         
/* 184 */         ResourceLocation debug12 = new ResourceLocation(GsonHelper.getAsString(jsonObject, "stat"));
/* 185 */         Stat<?> debug13 = getStat(debug11, debug12);
/*     */         
/* 187 */         MinMaxBounds.Ints debug14 = MinMaxBounds.Ints.fromJson(jsonObject.get("value"));
/* 188 */         debug5.put(debug13, debug14);
/*     */       } 
/*     */     }
/*     */     
/* 192 */     Object2BooleanOpenHashMap object2BooleanOpenHashMap = new Object2BooleanOpenHashMap();
/* 193 */     JsonObject debug8 = GsonHelper.getAsJsonObject(debug1, "recipes", new JsonObject());
/* 194 */     for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)debug8.entrySet()) {
/* 195 */       ResourceLocation debug11 = new ResourceLocation(entry.getKey());
/* 196 */       boolean debug12 = GsonHelper.convertToBoolean(entry.getValue(), "recipe present");
/* 197 */       object2BooleanOpenHashMap.put(debug11, debug12);
/*     */     } 
/*     */     
/* 200 */     Map<ResourceLocation, AdvancementPredicate> debug9 = Maps.newHashMap();
/* 201 */     JsonObject debug10 = GsonHelper.getAsJsonObject(debug1, "advancements", new JsonObject());
/* 202 */     for (Map.Entry<String, JsonElement> debug12 : (Iterable<Map.Entry<String, JsonElement>>)debug10.entrySet()) {
/* 203 */       ResourceLocation debug13 = new ResourceLocation(debug12.getKey());
/* 204 */       AdvancementPredicate debug14 = advancementPredicateFromJson(debug12.getValue());
/* 205 */       debug9.put(debug13, debug14);
/*     */     } 
/*     */     
/* 208 */     return new PlayerPredicate(debug2, debug4, debug5, (Object2BooleanMap<ResourceLocation>)object2BooleanOpenHashMap, debug9);
/*     */   }
/*     */   
/*     */   private static <T> Stat<T> getStat(StatType<T> debug0, ResourceLocation debug1) {
/* 212 */     Registry<T> debug2 = debug0.getRegistry();
/* 213 */     T debug3 = (T)debug2.get(debug1);
/* 214 */     if (debug3 == null) {
/* 215 */       throw new JsonParseException("Unknown object " + debug1 + " for stat type " + Registry.STAT_TYPE.getKey(debug0));
/*     */     }
/*     */     
/* 218 */     return debug0.get(debug3);
/*     */   }
/*     */   
/*     */   private static <T> ResourceLocation getStatValueId(Stat<T> debug0) {
/* 222 */     return debug0.getType().getRegistry().getKey(debug0.getValue());
/*     */   }
/*     */   
/*     */   public JsonElement serializeToJson() {
/* 226 */     if (this == ANY) {
/* 227 */       return (JsonElement)JsonNull.INSTANCE;
/*     */     }
/*     */     
/* 230 */     JsonObject debug1 = new JsonObject();
/* 231 */     debug1.add("level", this.level.serializeToJson());
/*     */     
/* 233 */     if (this.gameType != GameType.NOT_SET) {
/* 234 */       debug1.addProperty("gamemode", this.gameType.getName());
/*     */     }
/*     */     
/* 237 */     if (!this.stats.isEmpty()) {
/* 238 */       JsonArray debug2 = new JsonArray();
/* 239 */       this.stats.forEach((debug1, debug2) -> {
/*     */             JsonObject debug3 = new JsonObject();
/*     */             debug3.addProperty("type", Registry.STAT_TYPE.getKey(debug1.getType()).toString());
/*     */             debug3.addProperty("stat", getStatValueId(debug1).toString());
/*     */             debug3.add("value", debug2.serializeToJson());
/*     */             debug0.add((JsonElement)debug3);
/*     */           });
/* 246 */       debug1.add("stats", (JsonElement)debug2);
/*     */     } 
/*     */     
/* 249 */     if (!this.recipes.isEmpty()) {
/* 250 */       JsonObject debug2 = new JsonObject();
/* 251 */       this.recipes.forEach((debug1, debug2) -> debug0.addProperty(debug1.toString(), debug2));
/* 252 */       debug1.add("recipes", (JsonElement)debug2);
/*     */     } 
/*     */     
/* 255 */     if (!this.advancements.isEmpty()) {
/* 256 */       JsonObject debug2 = new JsonObject();
/* 257 */       this.advancements.forEach((debug1, debug2) -> debug0.add(debug1.toString(), debug2.toJson()));
/* 258 */       debug1.add("advancements", (JsonElement)debug2);
/*     */     } 
/*     */     
/* 261 */     return (JsonElement)debug1;
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 265 */     private MinMaxBounds.Ints level = MinMaxBounds.Ints.ANY;
/* 266 */     private GameType gameType = GameType.NOT_SET;
/* 267 */     private final Map<Stat<?>, MinMaxBounds.Ints> stats = Maps.newHashMap();
/* 268 */     private final Object2BooleanMap<ResourceLocation> recipes = (Object2BooleanMap<ResourceLocation>)new Object2BooleanOpenHashMap();
/* 269 */     private final Map<ResourceLocation, PlayerPredicate.AdvancementPredicate> advancements = Maps.newHashMap();
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
/*     */     public PlayerPredicate build() {
/* 306 */       return new PlayerPredicate(this.level, this.gameType, this.stats, this.recipes, this.advancements);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\PlayerPredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */