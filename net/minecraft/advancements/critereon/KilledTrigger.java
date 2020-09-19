/*     */ package net.minecraft.advancements.critereon;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ 
/*     */ public class KilledTrigger extends SimpleCriterionTrigger<KilledTrigger.TriggerInstance> {
/*     */   private final ResourceLocation id;
/*     */   
/*     */   public KilledTrigger(ResourceLocation debug1) {
/*  15 */     this.id = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public ResourceLocation getId() {
/*  20 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/*  25 */     return new TriggerInstance(this.id, debug2, EntityPredicate.Composite.fromJson(debug1, "entity", debug3), DamageSourcePredicate.fromJson(debug1.get("killing_blow")));
/*     */   }
/*     */   
/*     */   public void trigger(ServerPlayer debug1, Entity debug2, DamageSource debug3) {
/*  29 */     LootContext debug4 = EntityPredicate.createContext(debug1, debug2);
/*  30 */     trigger(debug1, debug3 -> debug3.matches(debug0, debug1, debug2));
/*     */   }
/*     */   
/*     */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*     */     private final EntityPredicate.Composite entityPredicate;
/*     */     private final DamageSourcePredicate killingBlow;
/*     */     
/*     */     public TriggerInstance(ResourceLocation debug1, EntityPredicate.Composite debug2, EntityPredicate.Composite debug3, DamageSourcePredicate debug4) {
/*  38 */       super(debug1, debug2);
/*  39 */       this.entityPredicate = debug3;
/*  40 */       this.killingBlow = debug4;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static TriggerInstance playerKilledEntity(EntityPredicate.Builder debug0) {
/*  48 */       return new TriggerInstance(CriteriaTriggers.PLAYER_KILLED_ENTITY.id, EntityPredicate.Composite.ANY, EntityPredicate.Composite.wrap(debug0.build()), DamageSourcePredicate.ANY);
/*     */     }
/*     */     
/*     */     public static TriggerInstance playerKilledEntity() {
/*  52 */       return new TriggerInstance(CriteriaTriggers.PLAYER_KILLED_ENTITY.id, EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY, DamageSourcePredicate.ANY);
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
/*     */     public static TriggerInstance playerKilledEntity(EntityPredicate.Builder debug0, DamageSourcePredicate.Builder debug1) {
/*  68 */       return new TriggerInstance(CriteriaTriggers.PLAYER_KILLED_ENTITY.id, EntityPredicate.Composite.ANY, EntityPredicate.Composite.wrap(debug0.build()), debug1.build());
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
/*     */     public static TriggerInstance entityKilledPlayer() {
/*  80 */       return new TriggerInstance(CriteriaTriggers.ENTITY_KILLED_PLAYER.id, EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY, DamageSourcePredicate.ANY);
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
/*     */     public boolean matches(ServerPlayer debug1, LootContext debug2, DamageSource debug3) {
/* 100 */       if (!this.killingBlow.matches(debug1, debug3)) {
/* 101 */         return false;
/*     */       }
/* 103 */       return this.entityPredicate.matches(debug2);
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 108 */       JsonObject debug2 = super.serializeToJson(debug1);
/*     */       
/* 110 */       debug2.add("entity", this.entityPredicate.toJson(debug1));
/* 111 */       debug2.add("killing_blow", this.killingBlow.serializeToJson());
/*     */       
/* 113 */       return debug2;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\KilledTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */