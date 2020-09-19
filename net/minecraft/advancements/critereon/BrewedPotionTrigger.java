/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSyntaxException;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.item.alchemy.Potion;
/*    */ 
/*    */ public class BrewedPotionTrigger
/*    */   extends SimpleCriterionTrigger<BrewedPotionTrigger.TriggerInstance> {
/* 14 */   private static final ResourceLocation ID = new ResourceLocation("brewed_potion");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 18 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 23 */     Potion debug4 = null;
/* 24 */     if (debug1.has("potion")) {
/* 25 */       ResourceLocation debug5 = new ResourceLocation(GsonHelper.getAsString(debug1, "potion"));
/* 26 */       debug4 = (Potion)Registry.POTION.getOptional(debug5).orElseThrow(() -> new JsonSyntaxException("Unknown potion '" + debug0 + "'"));
/*    */     } 
/* 28 */     return new TriggerInstance(debug2, debug4);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, Potion debug2) {
/* 32 */     trigger(debug1, debug1 -> debug1.matches(debug0));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final Potion potion;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, @Nullable Potion debug2) {
/* 39 */       super(BrewedPotionTrigger.ID, debug1);
/* 40 */       this.potion = debug2;
/*    */     }
/*    */     
/*    */     public static TriggerInstance brewedPotion() {
/* 44 */       return new TriggerInstance(EntityPredicate.Composite.ANY, null);
/*    */     }
/*    */     
/*    */     public boolean matches(Potion debug1) {
/* 48 */       if (this.potion != null && this.potion != debug1) {
/* 49 */         return false;
/*    */       }
/* 51 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 56 */       JsonObject debug2 = super.serializeToJson(debug1);
/*    */       
/* 58 */       if (this.potion != null) {
/* 59 */         debug2.addProperty("potion", Registry.POTION.getKey(this.potion).toString());
/*    */       }
/*    */       
/* 62 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\BrewedPotionTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */