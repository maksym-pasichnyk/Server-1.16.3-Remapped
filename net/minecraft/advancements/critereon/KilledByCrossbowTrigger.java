/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Sets;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public class KilledByCrossbowTrigger
/*    */   extends SimpleCriterionTrigger<KilledByCrossbowTrigger.TriggerInstance> {
/* 18 */   private static final ResourceLocation ID = new ResourceLocation("killed_by_crossbow");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 22 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 27 */     EntityPredicate.Composite[] debug4 = EntityPredicate.Composite.fromJsonArray(debug1, "victims", debug3);
/* 28 */     MinMaxBounds.Ints debug5 = MinMaxBounds.Ints.fromJson(debug1.get("unique_entity_types"));
/* 29 */     return new TriggerInstance(debug2, debug4, debug5);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, Collection<Entity> debug2) {
/* 33 */     List<LootContext> debug3 = Lists.newArrayList();
/* 34 */     Set<EntityType<?>> debug4 = Sets.newHashSet();
/* 35 */     for (Entity debug6 : debug2) {
/* 36 */       debug4.add(debug6.getType());
/* 37 */       debug3.add(EntityPredicate.createContext(debug1, debug6));
/*    */     } 
/*    */     
/* 40 */     trigger(debug1, debug2 -> debug2.matches(debug0, debug1.size()));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final EntityPredicate.Composite[] victims;
/*    */     private final MinMaxBounds.Ints uniqueEntityTypes;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, EntityPredicate.Composite[] debug2, MinMaxBounds.Ints debug3) {
/* 48 */       super(KilledByCrossbowTrigger.ID, debug1);
/* 49 */       this.victims = debug2;
/* 50 */       this.uniqueEntityTypes = debug3;
/*    */     }
/*    */     
/*    */     public static TriggerInstance crossbowKilled(EntityPredicate.Builder... debug0) {
/* 54 */       EntityPredicate.Composite[] debug1 = new EntityPredicate.Composite[debug0.length];
/* 55 */       for (int debug2 = 0; debug2 < debug0.length; debug2++) {
/* 56 */         EntityPredicate.Builder debug3 = debug0[debug2];
/* 57 */         debug1[debug2] = EntityPredicate.Composite.wrap(debug3.build());
/*    */       } 
/* 59 */       return new TriggerInstance(EntityPredicate.Composite.ANY, debug1, MinMaxBounds.Ints.ANY);
/*    */     }
/*    */     
/*    */     public static TriggerInstance crossbowKilled(MinMaxBounds.Ints debug0) {
/* 63 */       EntityPredicate.Composite[] debug1 = new EntityPredicate.Composite[0];
/* 64 */       return new TriggerInstance(EntityPredicate.Composite.ANY, debug1, debug0);
/*    */     }
/*    */     
/*    */     public boolean matches(Collection<LootContext> debug1, int debug2) {
/* 68 */       if (this.victims.length > 0) {
/* 69 */         List<LootContext> debug3 = Lists.newArrayList(debug1);
/* 70 */         for (EntityPredicate.Composite debug7 : this.victims) {
/* 71 */           boolean debug8 = false;
/* 72 */           for (Iterator<LootContext> debug9 = debug3.iterator(); debug9.hasNext(); ) {
/* 73 */             LootContext debug10 = debug9.next();
/* 74 */             if (debug7.matches(debug10)) {
/* 75 */               debug9.remove();
/* 76 */               debug8 = true;
/*    */               
/*    */               break;
/*    */             } 
/*    */           } 
/* 81 */           if (!debug8) {
/* 82 */             return false;
/*    */           }
/*    */         } 
/*    */       } 
/*    */       
/* 87 */       return this.uniqueEntityTypes.matches(debug2);
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 92 */       JsonObject debug2 = super.serializeToJson(debug1);
/*    */       
/* 94 */       debug2.add("victims", EntityPredicate.Composite.toJson(this.victims, debug1));
/* 95 */       debug2.add("unique_entity_types", this.uniqueEntityTypes.serializeToJson());
/*    */       
/* 97 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\KilledByCrossbowTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */