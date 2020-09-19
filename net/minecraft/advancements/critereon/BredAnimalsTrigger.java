/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.AgableMob;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.animal.Animal;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public class BredAnimalsTrigger extends SimpleCriterionTrigger<BredAnimalsTrigger.TriggerInstance> {
/* 13 */   private static final ResourceLocation ID = new ResourceLocation("bred_animals");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 17 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 22 */     EntityPredicate.Composite debug4 = EntityPredicate.Composite.fromJson(debug1, "parent", debug3);
/* 23 */     EntityPredicate.Composite debug5 = EntityPredicate.Composite.fromJson(debug1, "partner", debug3);
/* 24 */     EntityPredicate.Composite debug6 = EntityPredicate.Composite.fromJson(debug1, "child", debug3);
/* 25 */     return new TriggerInstance(debug2, debug4, debug5, debug6);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, Animal debug2, Animal debug3, @Nullable AgableMob debug4) {
/* 29 */     LootContext debug5 = EntityPredicate.createContext(debug1, (Entity)debug2);
/* 30 */     LootContext debug6 = EntityPredicate.createContext(debug1, (Entity)debug3);
/* 31 */     LootContext debug7 = (debug4 != null) ? EntityPredicate.createContext(debug1, (Entity)debug4) : null;
/*    */     
/* 33 */     trigger(debug1, debug3 -> debug3.matches(debug0, debug1, debug2));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final EntityPredicate.Composite parent;
/*    */     private final EntityPredicate.Composite partner;
/*    */     private final EntityPredicate.Composite child;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, EntityPredicate.Composite debug2, EntityPredicate.Composite debug3, EntityPredicate.Composite debug4) {
/* 42 */       super(BredAnimalsTrigger.ID, debug1);
/* 43 */       this.parent = debug2;
/* 44 */       this.partner = debug3;
/* 45 */       this.child = debug4;
/*    */     }
/*    */     
/*    */     public static TriggerInstance bredAnimals() {
/* 49 */       return new TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY);
/*    */     }
/*    */     
/*    */     public static TriggerInstance bredAnimals(EntityPredicate.Builder debug0) {
/* 53 */       return new TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY, EntityPredicate.Composite.wrap(debug0.build()));
/*    */     }
/*    */     
/*    */     public static TriggerInstance bredAnimals(EntityPredicate debug0, EntityPredicate debug1, EntityPredicate debug2) {
/* 57 */       return new TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.wrap(debug0), EntityPredicate.Composite.wrap(debug1), EntityPredicate.Composite.wrap(debug2));
/*    */     }
/*    */     
/*    */     public boolean matches(LootContext debug1, LootContext debug2, @Nullable LootContext debug3) {
/* 61 */       if (this.child != EntityPredicate.Composite.ANY && (debug3 == null || !this.child.matches(debug3))) {
/* 62 */         return false;
/*    */       }
/*    */       
/* 65 */       return ((this.parent.matches(debug1) && this.partner.matches(debug2)) || (this.parent.matches(debug2) && this.partner.matches(debug1)));
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 70 */       JsonObject debug2 = super.serializeToJson(debug1);
/*    */       
/* 72 */       debug2.add("parent", this.parent.toJson(debug1));
/* 73 */       debug2.add("partner", this.partner.toJson(debug1));
/* 74 */       debug2.add("child", this.child.toJson(debug1));
/*    */       
/* 76 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\BredAnimalsTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */