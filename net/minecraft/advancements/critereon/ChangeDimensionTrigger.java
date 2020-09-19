/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ChangeDimensionTrigger
/*    */   extends SimpleCriterionTrigger<ChangeDimensionTrigger.TriggerInstance> {
/* 14 */   private static final ResourceLocation ID = new ResourceLocation("changed_dimension");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 18 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 23 */     ResourceKey<Level> debug4 = debug1.has("from") ? ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(GsonHelper.getAsString(debug1, "from"))) : null;
/* 24 */     ResourceKey<Level> debug5 = debug1.has("to") ? ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(GsonHelper.getAsString(debug1, "to"))) : null;
/* 25 */     return new TriggerInstance(debug2, debug4, debug5);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, ResourceKey<Level> debug2, ResourceKey<Level> debug3) {
/* 29 */     trigger(debug1, debug2 -> debug2.matches(debug0, debug1));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     @Nullable
/*    */     private final ResourceKey<Level> from;
/*    */     @Nullable
/*    */     private final ResourceKey<Level> to;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, @Nullable ResourceKey<Level> debug2, @Nullable ResourceKey<Level> debug3) {
/* 39 */       super(ChangeDimensionTrigger.ID, debug1);
/* 40 */       this.from = debug2;
/* 41 */       this.to = debug3;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public static TriggerInstance changedDimensionTo(ResourceKey<Level> debug0) {
/* 53 */       return new TriggerInstance(EntityPredicate.Composite.ANY, null, debug0);
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public boolean matches(ResourceKey<Level> debug1, ResourceKey<Level> debug2) {
/* 61 */       if (this.from != null && this.from != debug1) {
/* 62 */         return false;
/*    */       }
/* 64 */       if (this.to != null && this.to != debug2) {
/* 65 */         return false;
/*    */       }
/* 67 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 72 */       JsonObject debug2 = super.serializeToJson(debug1);
/* 73 */       if (this.from != null) {
/* 74 */         debug2.addProperty("from", this.from.location().toString());
/*    */       }
/* 76 */       if (this.to != null) {
/* 77 */         debug2.addProperty("to", this.to.location().toString());
/*    */       }
/* 79 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\ChangeDimensionTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */