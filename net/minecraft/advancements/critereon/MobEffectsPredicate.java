/*     */ package net.minecraft.advancements.critereon;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ 
/*     */ public class MobEffectsPredicate
/*     */ {
/*  21 */   public static final MobEffectsPredicate ANY = new MobEffectsPredicate(Collections.emptyMap());
/*     */   
/*     */   private final Map<MobEffect, MobEffectInstancePredicate> effects;
/*     */   
/*     */   public MobEffectsPredicate(Map<MobEffect, MobEffectInstancePredicate> debug1) {
/*  26 */     this.effects = debug1;
/*     */   }
/*     */   
/*     */   public static MobEffectsPredicate effects() {
/*  30 */     return new MobEffectsPredicate(Maps.newLinkedHashMap());
/*     */   }
/*     */   
/*     */   public MobEffectsPredicate and(MobEffect debug1) {
/*  34 */     this.effects.put(debug1, new MobEffectInstancePredicate());
/*  35 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(Entity debug1) {
/*  44 */     if (this == ANY) {
/*  45 */       return true;
/*     */     }
/*  47 */     if (debug1 instanceof LivingEntity) {
/*  48 */       return matches(((LivingEntity)debug1).getActiveEffectsMap());
/*     */     }
/*  50 */     return false;
/*     */   }
/*     */   
/*     */   public boolean matches(LivingEntity debug1) {
/*  54 */     if (this == ANY) {
/*  55 */       return true;
/*     */     }
/*  57 */     return matches(debug1.getActiveEffectsMap());
/*     */   }
/*     */   
/*     */   public boolean matches(Map<MobEffect, MobEffectInstance> debug1) {
/*  61 */     if (this == ANY) {
/*  62 */       return true;
/*     */     }
/*     */     
/*  65 */     for (Map.Entry<MobEffect, MobEffectInstancePredicate> debug3 : this.effects.entrySet()) {
/*  66 */       MobEffectInstance debug4 = debug1.get(debug3.getKey());
/*  67 */       if (!((MobEffectInstancePredicate)debug3.getValue()).matches(debug4)) {
/*  68 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  72 */     return true;
/*     */   }
/*     */   
/*     */   public static MobEffectsPredicate fromJson(@Nullable JsonElement debug0) {
/*  76 */     if (debug0 == null || debug0.isJsonNull()) {
/*  77 */       return ANY;
/*     */     }
/*  79 */     JsonObject debug1 = GsonHelper.convertToJsonObject(debug0, "effects");
/*  80 */     Map<MobEffect, MobEffectInstancePredicate> debug2 = Maps.newLinkedHashMap();
/*     */     
/*  82 */     for (Map.Entry<String, JsonElement> debug4 : (Iterable<Map.Entry<String, JsonElement>>)debug1.entrySet()) {
/*  83 */       ResourceLocation debug5 = new ResourceLocation(debug4.getKey());
/*  84 */       MobEffect debug6 = (MobEffect)Registry.MOB_EFFECT.getOptional(debug5).orElseThrow(() -> new JsonSyntaxException("Unknown effect '" + debug0 + "'"));
/*  85 */       MobEffectInstancePredicate debug7 = MobEffectInstancePredicate.fromJson(GsonHelper.convertToJsonObject(debug4.getValue(), debug4.getKey()));
/*  86 */       debug2.put(debug6, debug7);
/*     */     } 
/*     */     
/*  89 */     return new MobEffectsPredicate(debug2);
/*     */   }
/*     */   
/*     */   public JsonElement serializeToJson() {
/*  93 */     if (this == ANY) {
/*  94 */       return (JsonElement)JsonNull.INSTANCE;
/*     */     }
/*     */     
/*  97 */     JsonObject debug1 = new JsonObject();
/*     */     
/*  99 */     for (Map.Entry<MobEffect, MobEffectInstancePredicate> debug3 : this.effects.entrySet()) {
/* 100 */       debug1.add(Registry.MOB_EFFECT.getKey(debug3.getKey()).toString(), ((MobEffectInstancePredicate)debug3.getValue()).serializeToJson());
/*     */     }
/*     */     
/* 103 */     return (JsonElement)debug1;
/*     */   }
/*     */   
/*     */   public static class MobEffectInstancePredicate {
/*     */     private final MinMaxBounds.Ints amplifier;
/*     */     private final MinMaxBounds.Ints duration;
/*     */     @Nullable
/*     */     private final Boolean ambient;
/*     */     @Nullable
/*     */     private final Boolean visible;
/*     */     
/*     */     public MobEffectInstancePredicate(MinMaxBounds.Ints debug1, MinMaxBounds.Ints debug2, @Nullable Boolean debug3, @Nullable Boolean debug4) {
/* 115 */       this.amplifier = debug1;
/* 116 */       this.duration = debug2;
/* 117 */       this.ambient = debug3;
/* 118 */       this.visible = debug4;
/*     */     }
/*     */     
/*     */     public MobEffectInstancePredicate() {
/* 122 */       this(MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, null, null);
/*     */     }
/*     */     
/*     */     public boolean matches(@Nullable MobEffectInstance debug1) {
/* 126 */       if (debug1 == null) {
/* 127 */         return false;
/*     */       }
/* 129 */       if (!this.amplifier.matches(debug1.getAmplifier())) {
/* 130 */         return false;
/*     */       }
/* 132 */       if (!this.duration.matches(debug1.getDuration())) {
/* 133 */         return false;
/*     */       }
/* 135 */       if (this.ambient != null && this.ambient.booleanValue() != debug1.isAmbient()) {
/* 136 */         return false;
/*     */       }
/* 138 */       if (this.visible != null && this.visible.booleanValue() != debug1.isVisible()) {
/* 139 */         return false;
/*     */       }
/* 141 */       return true;
/*     */     }
/*     */     
/*     */     public JsonElement serializeToJson() {
/* 145 */       JsonObject debug1 = new JsonObject();
/*     */       
/* 147 */       debug1.add("amplifier", this.amplifier.serializeToJson());
/* 148 */       debug1.add("duration", this.duration.serializeToJson());
/* 149 */       debug1.addProperty("ambient", this.ambient);
/* 150 */       debug1.addProperty("visible", this.visible);
/*     */       
/* 152 */       return (JsonElement)debug1;
/*     */     }
/*     */     
/*     */     public static MobEffectInstancePredicate fromJson(JsonObject debug0) {
/* 156 */       MinMaxBounds.Ints debug1 = MinMaxBounds.Ints.fromJson(debug0.get("amplifier"));
/* 157 */       MinMaxBounds.Ints debug2 = MinMaxBounds.Ints.fromJson(debug0.get("duration"));
/* 158 */       Boolean debug3 = debug0.has("ambient") ? Boolean.valueOf(GsonHelper.getAsBoolean(debug0, "ambient")) : null;
/* 159 */       Boolean debug4 = debug0.has("visible") ? Boolean.valueOf(GsonHelper.getAsBoolean(debug0, "visible")) : null;
/* 160 */       return new MobEffectInstancePredicate(debug1, debug2, debug3, debug4);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\MobEffectsPredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */