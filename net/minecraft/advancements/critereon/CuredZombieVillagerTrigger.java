/*    */ package net.minecraft.advancements.critereon;
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.monster.Zombie;
/*    */ import net.minecraft.world.entity.npc.Villager;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public class CuredZombieVillagerTrigger extends SimpleCriterionTrigger<CuredZombieVillagerTrigger.TriggerInstance> {
/* 11 */   private static final ResourceLocation ID = new ResourceLocation("cured_zombie_villager");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 15 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 20 */     EntityPredicate.Composite debug4 = EntityPredicate.Composite.fromJson(debug1, "zombie", debug3);
/* 21 */     EntityPredicate.Composite debug5 = EntityPredicate.Composite.fromJson(debug1, "villager", debug3);
/* 22 */     return new TriggerInstance(debug2, debug4, debug5);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, Zombie debug2, Villager debug3) {
/* 26 */     LootContext debug4 = EntityPredicate.createContext(debug1, (Entity)debug2);
/* 27 */     LootContext debug5 = EntityPredicate.createContext(debug1, (Entity)debug3);
/*    */     
/* 29 */     trigger(debug1, debug2 -> debug2.matches(debug0, debug1));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final EntityPredicate.Composite zombie;
/*    */     private final EntityPredicate.Composite villager;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, EntityPredicate.Composite debug2, EntityPredicate.Composite debug3) {
/* 37 */       super(CuredZombieVillagerTrigger.ID, debug1);
/* 38 */       this.zombie = debug2;
/* 39 */       this.villager = debug3;
/*    */     }
/*    */     
/*    */     public static TriggerInstance curedZombieVillager() {
/* 43 */       return new TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY);
/*    */     }
/*    */     
/*    */     public boolean matches(LootContext debug1, LootContext debug2) {
/* 47 */       if (!this.zombie.matches(debug1)) {
/* 48 */         return false;
/*    */       }
/* 50 */       if (!this.villager.matches(debug2)) {
/* 51 */         return false;
/*    */       }
/* 53 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 58 */       JsonObject debug2 = super.serializeToJson(debug1);
/*    */       
/* 60 */       debug2.add("zombie", this.zombie.toJson(debug1));
/* 61 */       debug2.add("villager", this.villager.toJson(debug1));
/*    */       
/* 63 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\CuredZombieVillagerTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */