/*     */ package net.minecraft.world.level.storage.loot.predicates;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.RandomValueBounds;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
/*     */ import net.minecraft.world.scores.Objective;
/*     */ import net.minecraft.world.scores.Scoreboard;
/*     */ 
/*     */ public class EntityHasScoreCondition
/*     */   implements LootItemCondition {
/*     */   private final Map<String, RandomValueBounds> scores;
/*     */   private final LootContext.EntityTarget entityTarget;
/*     */   
/*     */   private EntityHasScoreCondition(Map<String, RandomValueBounds> debug1, LootContext.EntityTarget debug2) {
/*  26 */     this.scores = (Map<String, RandomValueBounds>)ImmutableMap.copyOf(debug1);
/*  27 */     this.entityTarget = debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemConditionType getType() {
/*  32 */     return LootItemConditions.ENTITY_SCORES;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<LootContextParam<?>> getReferencedContextParams() {
/*  37 */     return (Set<LootContextParam<?>>)ImmutableSet.of(this.entityTarget.getParam());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean test(LootContext debug1) {
/*  42 */     Entity debug2 = (Entity)debug1.getParamOrNull(this.entityTarget.getParam());
/*     */     
/*  44 */     if (debug2 == null) {
/*  45 */       return false;
/*     */     }
/*     */     
/*  48 */     Scoreboard debug3 = debug2.level.getScoreboard();
/*  49 */     for (Map.Entry<String, RandomValueBounds> debug5 : this.scores.entrySet()) {
/*  50 */       if (!hasScore(debug2, debug3, debug5.getKey(), debug5.getValue())) {
/*  51 */         return false;
/*     */       }
/*     */     } 
/*  54 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean hasScore(Entity debug1, Scoreboard debug2, String debug3, RandomValueBounds debug4) {
/*  58 */     Objective debug5 = debug2.getObjective(debug3);
/*  59 */     if (debug5 == null) {
/*  60 */       return false;
/*     */     }
/*  62 */     String debug6 = debug1.getScoreboardName();
/*  63 */     if (!debug2.hasPlayerScore(debug6, debug5)) {
/*  64 */       return false;
/*     */     }
/*  66 */     return debug4.matchesValue(debug2.getOrCreatePlayerScore(debug6, debug5).getScore());
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
/*     */   public static class Serializer
/*     */     implements net.minecraft.world.level.storage.loot.Serializer<EntityHasScoreCondition>
/*     */   {
/*     */     public void serialize(JsonObject debug1, EntityHasScoreCondition debug2, JsonSerializationContext debug3) {
/*  95 */       JsonObject debug4 = new JsonObject();
/*  96 */       for (Map.Entry<String, RandomValueBounds> debug6 : (Iterable<Map.Entry<String, RandomValueBounds>>)debug2.scores.entrySet()) {
/*  97 */         debug4.add(debug6.getKey(), debug3.serialize(debug6.getValue()));
/*     */       }
/*  99 */       debug1.add("scores", (JsonElement)debug4);
/* 100 */       debug1.add("entity", debug3.serialize(debug2.entityTarget));
/*     */     }
/*     */ 
/*     */     
/*     */     public EntityHasScoreCondition deserialize(JsonObject debug1, JsonDeserializationContext debug2) {
/* 105 */       Set<Map.Entry<String, JsonElement>> debug3 = GsonHelper.getAsJsonObject(debug1, "scores").entrySet();
/* 106 */       Map<String, RandomValueBounds> debug4 = Maps.newLinkedHashMap();
/* 107 */       for (Map.Entry<String, JsonElement> debug6 : debug3) {
/* 108 */         debug4.put(debug6.getKey(), GsonHelper.convertToObject(debug6.getValue(), "score", debug2, RandomValueBounds.class));
/*     */       }
/* 110 */       return new EntityHasScoreCondition(debug4, (LootContext.EntityTarget)GsonHelper.getAsObject(debug1, "entity", debug2, LootContext.EntityTarget.class));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\EntityHasScoreCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */