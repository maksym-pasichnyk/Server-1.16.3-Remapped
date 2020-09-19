/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class ConditionReference implements LootItemCondition {
/* 14 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   private final ResourceLocation name;
/*    */   
/*    */   private ConditionReference(ResourceLocation debug1) {
/* 19 */     this.name = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 24 */     return LootItemConditions.REFERENCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext debug1) {
/* 29 */     if (debug1.hasVisitedCondition(this.name)) {
/* 30 */       debug1.reportProblem("Condition " + this.name + " is recursively called");
/*    */       
/*    */       return;
/*    */     } 
/* 34 */     super.validate(debug1);
/*    */     
/* 36 */     LootItemCondition debug2 = debug1.resolveCondition(this.name);
/* 37 */     if (debug2 == null) {
/* 38 */       debug1.reportProblem("Unknown condition table called " + this.name);
/*    */     } else {
/* 40 */       debug2.validate(debug1.enterTable(".{" + this.name + "}", this.name));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(LootContext debug1) {
/* 46 */     LootItemCondition debug2 = debug1.getCondition(this.name);
/* 47 */     if (debug1.addVisitedCondition(debug2)) {
/*    */       try {
/* 49 */         return debug2.test(debug1);
/*    */       } finally {
/* 51 */         debug1.removeVisitedCondition(debug2);
/*    */       } 
/*    */     }
/* 54 */     LOGGER.warn("Detected infinite loop in loot tables");
/* 55 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static class Serializer
/*    */     implements net.minecraft.world.level.storage.loot.Serializer<ConditionReference>
/*    */   {
/*    */     public void serialize(JsonObject debug1, ConditionReference debug2, JsonSerializationContext debug3) {
/* 66 */       debug1.addProperty("name", debug2.name.toString());
/*    */     }
/*    */ 
/*    */     
/*    */     public ConditionReference deserialize(JsonObject debug1, JsonDeserializationContext debug2) {
/* 71 */       ResourceLocation debug3 = new ResourceLocation(GsonHelper.getAsString(debug1, "name"));
/* 72 */       return new ConditionReference(debug3);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\ConditionReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */