/*    */ package net.minecraft.advancements.critereon;
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonElement;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.storage.loot.Deserializers;
/*    */ import net.minecraft.world.level.storage.loot.PredicateManager;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class DeserializationContext {
/* 15 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   private final ResourceLocation id;
/*    */   private final PredicateManager predicateManager;
/* 18 */   private final Gson predicateGson = Deserializers.createConditionSerializer().create();
/*    */   
/*    */   public DeserializationContext(ResourceLocation debug1, PredicateManager debug2) {
/* 21 */     this.id = debug1;
/* 22 */     this.predicateManager = debug2;
/*    */   }
/*    */   
/*    */   public final LootItemCondition[] deserializeConditions(JsonArray debug1, String debug2, LootContextParamSet debug3) {
/* 26 */     LootItemCondition[] debug4 = (LootItemCondition[])this.predicateGson.fromJson((JsonElement)debug1, LootItemCondition[].class);
/* 27 */     ValidationContext debug5 = new ValidationContext(debug3, this.predicateManager::get, debug0 -> null);
/* 28 */     for (LootItemCondition debug9 : debug4) {
/* 29 */       debug9.validate(debug5);
/* 30 */       debug5.getProblems().forEach((debug1, debug2) -> LOGGER.warn("Found validation problem in advancement trigger {}/{}: {}", debug0, debug1, debug2));
/*    */     } 
/* 32 */     return debug4;
/*    */   }
/*    */   
/*    */   public ResourceLocation getAdvancementId() {
/* 36 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\DeserializationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */