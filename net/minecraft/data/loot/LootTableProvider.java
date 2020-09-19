/*    */ package net.minecraft.data.loot;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.common.collect.Multimap;
/*    */ import com.google.common.collect.Sets;
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.data.DataGenerator;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.HashCache;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ import net.minecraft.world.level.storage.loot.LootTables;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class LootTableProvider
/*    */   implements DataProvider {
/* 33 */   private static final Logger LOGGER = LogManager.getLogger();
/* 34 */   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
/*    */   
/*    */   private final DataGenerator generator;
/* 37 */   private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> subProviders = (List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>>)ImmutableList.of(
/* 38 */       Pair.of(FishingLoot::new, LootContextParamSets.FISHING), 
/* 39 */       Pair.of(ChestLoot::new, LootContextParamSets.CHEST), 
/* 40 */       Pair.of(EntityLoot::new, LootContextParamSets.ENTITY), 
/* 41 */       Pair.of(BlockLoot::new, LootContextParamSets.BLOCK), 
/* 42 */       Pair.of(PiglinBarterLoot::new, LootContextParamSets.PIGLIN_BARTER), 
/* 43 */       Pair.of(GiftLoot::new, LootContextParamSets.GIFT));
/*    */ 
/*    */   
/*    */   public LootTableProvider(DataGenerator debug1) {
/* 47 */     this.generator = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run(HashCache debug1) {
/* 52 */     Path debug2 = this.generator.getOutputFolder();
/*    */     
/* 54 */     Map<ResourceLocation, LootTable> debug3 = Maps.newHashMap();
/*    */     
/* 56 */     this.subProviders.forEach(debug1 -> ((Consumer<BiConsumer>)((Supplier<Consumer<BiConsumer>>)debug1.getFirst()).get()).accept(()));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 64 */     ValidationContext debug4 = new ValidationContext(LootContextParamSets.ALL_PARAMS, debug0 -> null, debug3::get);
/*    */     
/* 66 */     Sets.SetView setView = Sets.difference(BuiltInLootTables.all(), debug3.keySet());
/*    */     
/* 68 */     for (ResourceLocation debug7 : setView) {
/* 69 */       debug4.reportProblem("Missing built-in table: " + debug7);
/*    */     }
/*    */     
/* 72 */     debug3.forEach((debug1, debug2) -> LootTables.validate(debug0, debug1, debug2));
/*    */     
/* 74 */     Multimap<String, String> debug6 = debug4.getProblems();
/* 75 */     if (!debug6.isEmpty()) {
/* 76 */       debug6.forEach((debug0, debug1) -> LOGGER.warn("Found validation problem in " + debug0 + ": " + debug1));
/* 77 */       throw new IllegalStateException("Failed to validate loot tables, see logs");
/*    */     } 
/*    */     
/* 80 */     debug3.forEach((debug2, debug3) -> {
/*    */           Path debug4 = createPath(debug0, debug2);
/*    */           try {
/*    */             DataProvider.save(GSON, debug1, LootTables.serialize(debug3), debug4);
/* 84 */           } catch (IOException debug5) {
/*    */             LOGGER.error("Couldn't save loot table {}", debug4, debug5);
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   private static Path createPath(Path debug0, ResourceLocation debug1) {
/* 91 */     return debug0.resolve("data/" + debug1.getNamespace() + "/loot_tables/" + debug1.getPath() + ".json");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 96 */     return "LootTables";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\loot\LootTableProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */