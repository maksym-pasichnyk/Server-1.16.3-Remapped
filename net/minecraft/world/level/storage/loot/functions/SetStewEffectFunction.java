/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.SuspiciousStewItem;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.RandomValueBounds;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ public class SetStewEffectFunction
/*     */   extends LootItemConditionalFunction
/*     */ {
/*     */   private final Map<MobEffect, RandomValueBounds> effectDurationMap;
/*     */   
/*     */   private SetStewEffectFunction(LootItemCondition[] debug1, Map<MobEffect, RandomValueBounds> debug2) {
/*  32 */     super(debug1);
/*  33 */     this.effectDurationMap = (Map<MobEffect, RandomValueBounds>)ImmutableMap.copyOf(debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunctionType getType() {
/*  38 */     return LootItemFunctions.SET_STEW_EFFECT;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack debug1, LootContext debug2) {
/*  43 */     if (debug1.getItem() != Items.SUSPICIOUS_STEW || this.effectDurationMap.isEmpty()) {
/*  44 */       return debug1;
/*     */     }
/*     */     
/*  47 */     Random debug3 = debug2.getRandom();
/*  48 */     int debug4 = debug3.nextInt(this.effectDurationMap.size());
/*  49 */     Map.Entry<MobEffect, RandomValueBounds> debug5 = (Map.Entry<MobEffect, RandomValueBounds>)Iterables.get(this.effectDurationMap.entrySet(), debug4);
/*     */     
/*  51 */     MobEffect debug6 = debug5.getKey();
/*  52 */     int debug7 = ((RandomValueBounds)debug5.getValue()).getInt(debug3);
/*  53 */     if (!debug6.isInstantenous()) {
/*  54 */       debug7 *= 20;
/*     */     }
/*     */     
/*  57 */     SuspiciousStewItem.saveMobEffect(debug1, debug6, debug7);
/*  58 */     return debug1;
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*  62 */     private final Map<MobEffect, RandomValueBounds> effectDurationMap = Maps.newHashMap();
/*     */ 
/*     */     
/*     */     protected Builder getThis() {
/*  66 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withEffect(MobEffect debug1, RandomValueBounds debug2) {
/*  70 */       this.effectDurationMap.put(debug1, debug2);
/*  71 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LootItemFunction build() {
/*  76 */       return new SetStewEffectFunction(getConditions(), this.effectDurationMap);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder stewEffect() {
/*  81 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static class Serializer
/*     */     extends LootItemConditionalFunction.Serializer<SetStewEffectFunction> {
/*     */     public void serialize(JsonObject debug1, SetStewEffectFunction debug2, JsonSerializationContext debug3) {
/*  87 */       super.serialize(debug1, debug2, debug3);
/*     */       
/*  89 */       if (!debug2.effectDurationMap.isEmpty()) {
/*  90 */         JsonArray debug4 = new JsonArray();
/*  91 */         for (MobEffect debug6 : debug2.effectDurationMap.keySet()) {
/*  92 */           JsonObject debug7 = new JsonObject();
/*  93 */           ResourceLocation debug8 = Registry.MOB_EFFECT.getKey(debug6);
/*  94 */           if (debug8 == null) {
/*  95 */             throw new IllegalArgumentException("Don't know how to serialize mob effect " + debug6);
/*     */           }
/*  97 */           debug7.add("type", (JsonElement)new JsonPrimitive(debug8.toString()));
/*  98 */           debug7.add("duration", debug3.serialize(debug2.effectDurationMap.get(debug6)));
/*  99 */           debug4.add((JsonElement)debug7);
/*     */         } 
/* 101 */         debug1.add("effects", (JsonElement)debug4);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public SetStewEffectFunction deserialize(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/* 107 */       Map<MobEffect, RandomValueBounds> debug4 = Maps.newHashMap();
/* 108 */       if (debug1.has("effects")) {
/* 109 */         JsonArray debug5 = GsonHelper.getAsJsonArray(debug1, "effects");
/* 110 */         for (JsonElement debug7 : debug5) {
/* 111 */           String debug8 = GsonHelper.getAsString(debug7.getAsJsonObject(), "type");
/*     */           
/* 113 */           MobEffect debug9 = (MobEffect)Registry.MOB_EFFECT.getOptional(new ResourceLocation(debug8)).orElseThrow(() -> new JsonSyntaxException("Unknown mob effect '" + debug0 + "'"));
/* 114 */           RandomValueBounds debug10 = (RandomValueBounds)GsonHelper.getAsObject(debug7.getAsJsonObject(), "duration", debug2, RandomValueBounds.class);
/* 115 */           debug4.put(debug9, debug10);
/*     */         } 
/*     */       } 
/*     */       
/* 119 */       return new SetStewEffectFunction(debug3, debug4);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetStewEffectFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */