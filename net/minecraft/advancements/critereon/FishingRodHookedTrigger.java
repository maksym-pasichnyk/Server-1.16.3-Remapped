/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.entity.projectile.FishingHook;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public class FishingRodHookedTrigger
/*    */   extends SimpleCriterionTrigger<FishingRodHookedTrigger.TriggerInstance> {
/* 16 */   private static final ResourceLocation ID = new ResourceLocation("fishing_rod_hooked");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 20 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 25 */     ItemPredicate debug4 = ItemPredicate.fromJson(debug1.get("rod"));
/* 26 */     EntityPredicate.Composite debug5 = EntityPredicate.Composite.fromJson(debug1, "entity", debug3);
/* 27 */     ItemPredicate debug6 = ItemPredicate.fromJson(debug1.get("item"));
/* 28 */     return new TriggerInstance(debug2, debug4, debug5, debug6);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, ItemStack debug2, FishingHook debug3, Collection<ItemStack> debug4) {
/* 32 */     LootContext debug5 = EntityPredicate.createContext(debug1, (debug3.getHookedIn() != null) ? debug3.getHookedIn() : (Entity)debug3);
/* 33 */     trigger(debug1, debug3 -> debug3.matches(debug0, debug1, debug2));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final ItemPredicate rod;
/*    */     private final EntityPredicate.Composite entity;
/*    */     private final ItemPredicate item;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, ItemPredicate debug2, EntityPredicate.Composite debug3, ItemPredicate debug4) {
/* 42 */       super(FishingRodHookedTrigger.ID, debug1);
/* 43 */       this.rod = debug2;
/* 44 */       this.entity = debug3;
/* 45 */       this.item = debug4;
/*    */     }
/*    */     
/*    */     public static TriggerInstance fishedItem(ItemPredicate debug0, EntityPredicate debug1, ItemPredicate debug2) {
/* 49 */       return new TriggerInstance(EntityPredicate.Composite.ANY, debug0, EntityPredicate.Composite.wrap(debug1), debug2);
/*    */     }
/*    */     
/*    */     public boolean matches(ItemStack debug1, LootContext debug2, Collection<ItemStack> debug3) {
/* 53 */       if (!this.rod.matches(debug1)) {
/* 54 */         return false;
/*    */       }
/* 56 */       if (!this.entity.matches(debug2)) {
/* 57 */         return false;
/*    */       }
/* 59 */       if (this.item != ItemPredicate.ANY) {
/* 60 */         boolean debug4 = false;
/*    */         
/* 62 */         Entity debug5 = (Entity)debug2.getParamOrNull(LootContextParams.THIS_ENTITY);
/* 63 */         if (debug5 instanceof ItemEntity) {
/* 64 */           ItemEntity debug6 = (ItemEntity)debug5;
/* 65 */           if (this.item.matches(debug6.getItem())) {
/* 66 */             debug4 = true;
/*    */           }
/*    */         } 
/* 69 */         for (ItemStack debug7 : debug3) {
/* 70 */           if (this.item.matches(debug7)) {
/* 71 */             debug4 = true;
/*    */             break;
/*    */           } 
/*    */         } 
/* 75 */         if (!debug4) {
/* 76 */           return false;
/*    */         }
/*    */       } 
/* 79 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 84 */       JsonObject debug2 = super.serializeToJson(debug1);
/*    */       
/* 86 */       debug2.add("rod", this.rod.serializeToJson());
/* 87 */       debug2.add("entity", this.entity.toJson(debug1));
/* 88 */       debug2.add("item", this.item.serializeToJson());
/*    */       
/* 90 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\FishingRodHookedTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */