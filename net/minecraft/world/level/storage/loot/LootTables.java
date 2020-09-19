/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.JsonElement;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class LootTables
/*    */   extends SimpleJsonResourceReloadListener {
/* 18 */   private static final Logger LOGGER = LogManager.getLogger();
/* 19 */   private static final Gson GSON = Deserializers.createLootTableSerializer().create();
/*    */   
/* 21 */   private Map<ResourceLocation, LootTable> tables = (Map<ResourceLocation, LootTable>)ImmutableMap.of();
/*    */   private final PredicateManager predicateManager;
/*    */   
/*    */   public LootTables(PredicateManager debug1) {
/* 25 */     super(GSON, "loot_tables");
/* 26 */     this.predicateManager = debug1;
/*    */   }
/*    */   
/*    */   public LootTable get(ResourceLocation debug1) {
/* 30 */     return this.tables.getOrDefault(debug1, LootTable.EMPTY);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void apply(Map<ResourceLocation, JsonElement> debug1, ResourceManager debug2, ProfilerFiller debug3) {
/* 35 */     ImmutableMap.Builder<ResourceLocation, LootTable> debug4 = ImmutableMap.builder();
/*    */     
/* 37 */     JsonElement debug5 = debug1.remove(BuiltInLootTables.EMPTY);
/* 38 */     if (debug5 != null) {
/* 39 */       LOGGER.warn("Datapack tried to redefine {} loot table, ignoring", BuiltInLootTables.EMPTY);
/*    */     }
/*    */     
/* 42 */     debug1.forEach((debug1, debug2) -> {
/*    */           try {
/*    */             LootTable debug3 = (LootTable)GSON.fromJson(debug2, LootTable.class);
/*    */             debug0.put(debug1, debug3);
/* 46 */           } catch (Exception debug3) {
/*    */             LOGGER.error("Couldn't parse loot table {}", debug1, debug3);
/*    */           } 
/*    */         });
/*    */     
/* 51 */     debug4.put(BuiltInLootTables.EMPTY, LootTable.EMPTY);
/*    */     
/* 53 */     ImmutableMap<ResourceLocation, LootTable> debug6 = debug4.build();
/* 54 */     ValidationContext debug7 = new ValidationContext(LootContextParamSets.ALL_PARAMS, this.predicateManager::get, debug6::get);
/* 55 */     debug6.forEach((debug1, debug2) -> validate(debug0, debug1, debug2));
/* 56 */     debug7.getProblems().forEach((debug0, debug1) -> LOGGER.warn("Found validation problem in " + debug0 + ": " + debug1));
/*    */     
/* 58 */     this.tables = (Map<ResourceLocation, LootTable>)debug6;
/*    */   }
/*    */   
/*    */   public static void validate(ValidationContext debug0, ResourceLocation debug1, LootTable debug2) {
/* 62 */     debug2.validate(debug0.setParams(debug2.getParamSet()).enterTable("{" + debug1 + "}", debug1));
/*    */   }
/*    */   
/*    */   public static JsonElement serialize(LootTable debug0) {
/* 66 */     return GSON.toJsonTree(debug0);
/*    */   }
/*    */   
/*    */   public Set<ResourceLocation> getIds() {
/* 70 */     return this.tables.keySet();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\LootTables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */