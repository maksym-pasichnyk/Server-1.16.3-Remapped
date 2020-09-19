/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.JsonElement;
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.function.Predicate;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class PredicateManager
/*    */   extends SimpleJsonResourceReloadListener {
/* 24 */   private static final Logger LOGGER = LogManager.getLogger(); private Map<ResourceLocation, LootItemCondition> conditions;
/* 25 */   private static final Gson GSON = Deserializers.createConditionSerializer().create();
/*    */   
/*    */   public PredicateManager() {
/* 28 */     super(GSON, "predicates");
/*    */ 
/*    */     
/* 31 */     this.conditions = (Map<ResourceLocation, LootItemCondition>)ImmutableMap.of();
/*    */   }
/*    */   @Nullable
/*    */   public LootItemCondition get(ResourceLocation debug1) {
/* 35 */     return this.conditions.get(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void apply(Map<ResourceLocation, JsonElement> debug1, ResourceManager debug2, ProfilerFiller debug3) {
/* 40 */     ImmutableMap.Builder<ResourceLocation, LootItemCondition> debug4 = ImmutableMap.builder();
/* 41 */     debug1.forEach((debug1, debug2) -> {
/*    */           try {
/*    */             if (debug2.isJsonArray()) {
/*    */               LootItemCondition[] debug3 = (LootItemCondition[])GSON.fromJson(debug2, LootItemCondition[].class);
/*    */               debug0.put(debug1, new CompositePredicate(debug3));
/*    */             } else {
/*    */               LootItemCondition debug3 = (LootItemCondition)GSON.fromJson(debug2, LootItemCondition.class);
/*    */               debug0.put(debug1, debug3);
/*    */             } 
/* 50 */           } catch (Exception debug3) {
/*    */             LOGGER.error("Couldn't parse loot table {}", debug1, debug3);
/*    */           } 
/*    */         });
/*    */     
/* 55 */     ImmutableMap immutableMap = debug4.build();
/* 56 */     ValidationContext debug6 = new ValidationContext(LootContextParamSets.ALL_PARAMS, immutableMap::get, debug0 -> null);
/* 57 */     immutableMap.forEach((debug1, debug2) -> debug2.validate(debug0.enterCondition("{" + debug1 + "}", debug1)));
/* 58 */     debug6.getProblems().forEach((debug0, debug1) -> LOGGER.warn("Found validation problem in " + debug0 + ": " + debug1));
/*    */     
/* 60 */     this.conditions = (Map<ResourceLocation, LootItemCondition>)immutableMap;
/*    */   }
/*    */   
/*    */   public Set<ResourceLocation> getKeys() {
/* 64 */     return Collections.unmodifiableSet(this.conditions.keySet());
/*    */   }
/*    */   
/*    */   static class CompositePredicate implements LootItemCondition {
/*    */     private final LootItemCondition[] terms;
/*    */     private final Predicate<LootContext> composedPredicate;
/*    */     
/*    */     private CompositePredicate(LootItemCondition[] debug1) {
/* 72 */       this.terms = debug1;
/* 73 */       this.composedPredicate = LootItemConditions.andConditions((Predicate[])debug1);
/*    */     }
/*    */ 
/*    */     
/*    */     public final boolean test(LootContext debug1) {
/* 78 */       return this.composedPredicate.test(debug1);
/*    */     }
/*    */ 
/*    */     
/*    */     public void validate(ValidationContext debug1) {
/* 83 */       super.validate(debug1);
/*    */       
/* 85 */       for (int debug2 = 0; debug2 < this.terms.length; debug2++) {
/* 86 */         this.terms[debug2].validate(debug1.forChild(".term[" + debug2 + "]"));
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemConditionType getType() {
/* 92 */       throw new UnsupportedOperationException();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\PredicateManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */