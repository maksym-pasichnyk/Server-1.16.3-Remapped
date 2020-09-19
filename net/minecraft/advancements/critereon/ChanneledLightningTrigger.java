/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public class ChanneledLightningTrigger
/*    */   extends SimpleCriterionTrigger<ChanneledLightningTrigger.TriggerInstance> {
/* 15 */   private static final ResourceLocation ID = new ResourceLocation("channeled_lightning");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 19 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 24 */     EntityPredicate.Composite[] debug4 = EntityPredicate.Composite.fromJsonArray(debug1, "victims", debug3);
/* 25 */     return new TriggerInstance(debug2, debug4);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, Collection<? extends Entity> debug2) {
/* 29 */     List<LootContext> debug3 = (List<LootContext>)debug2.stream().map(debug1 -> EntityPredicate.createContext(debug0, debug1)).collect(Collectors.toList());
/* 30 */     trigger(debug1, debug1 -> debug1.matches(debug0));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final EntityPredicate.Composite[] victims;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, EntityPredicate.Composite[] debug2) {
/* 37 */       super(ChanneledLightningTrigger.ID, debug1);
/* 38 */       this.victims = debug2;
/*    */     }
/*    */     
/*    */     public static TriggerInstance channeledLightning(EntityPredicate... debug0) {
/* 42 */       return new TriggerInstance(EntityPredicate.Composite.ANY, (EntityPredicate.Composite[])Stream.<EntityPredicate>of(debug0).map(EntityPredicate.Composite::wrap).toArray(debug0 -> new EntityPredicate.Composite[debug0]));
/*    */     }
/*    */     
/*    */     public boolean matches(Collection<? extends LootContext> debug1) {
/* 46 */       for (EntityPredicate.Composite debug5 : this.victims) {
/* 47 */         boolean debug6 = false;
/* 48 */         for (LootContext debug8 : debug1) {
/* 49 */           if (debug5.matches(debug8)) {
/* 50 */             debug6 = true;
/*    */             break;
/*    */           } 
/*    */         } 
/* 54 */         if (!debug6) {
/* 55 */           return false;
/*    */         }
/*    */       } 
/* 58 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 63 */       JsonObject debug2 = super.serializeToJson(debug1);
/*    */       
/* 65 */       debug2.add("victims", EntityPredicate.Composite.toJson(this.victims, debug1));
/*    */       
/* 67 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\ChanneledLightningTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */