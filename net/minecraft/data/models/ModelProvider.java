/*     */ package net.minecraft.data.models;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonElement;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.data.DataGenerator;
/*     */ import net.minecraft.data.DataProvider;
/*     */ import net.minecraft.data.HashCache;
/*     */ import net.minecraft.data.models.blockstates.BlockStateGenerator;
/*     */ import net.minecraft.data.models.model.DelegatedModel;
/*     */ import net.minecraft.data.models.model.ModelLocationUtils;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ModelProvider
/*     */   implements DataProvider {
/*  32 */   private static final Logger LOGGER = LogManager.getLogger();
/*  33 */   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
/*     */   
/*     */   private final DataGenerator generator;
/*     */   
/*     */   public ModelProvider(DataGenerator debug1) {
/*  38 */     this.generator = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void run(HashCache debug1) {
/*  43 */     Path debug2 = this.generator.getOutputFolder();
/*     */     
/*  45 */     Map<Block, BlockStateGenerator> debug3 = Maps.newHashMap();
/*  46 */     Consumer<BlockStateGenerator> debug4 = debug1 -> {
/*     */         Block debug2 = debug1.getBlock();
/*     */         
/*     */         BlockStateGenerator debug3 = debug0.put(debug2, debug1);
/*     */         if (debug3 != null) {
/*     */           throw new IllegalStateException("Duplicate blockstate definition for " + debug2);
/*     */         }
/*     */       };
/*  54 */     Map<ResourceLocation, Supplier<JsonElement>> debug5 = Maps.newHashMap();
/*  55 */     Set<Item> debug6 = Sets.newHashSet();
/*     */     
/*  57 */     BiConsumer<ResourceLocation, Supplier<JsonElement>> debug7 = (debug1, debug2) -> {
/*     */         Supplier<JsonElement> debug3 = debug0.put(debug1, debug2);
/*     */         
/*     */         if (debug3 != null) {
/*     */           throw new IllegalStateException("Duplicate model definition for " + debug1);
/*     */         }
/*     */       };
/*  64 */     Consumer<Item> debug8 = debug6::add;
/*     */     
/*  66 */     (new BlockModelGenerators(debug4, debug7, debug8)).run();
/*  67 */     (new ItemModelGenerators(debug7)).run();
/*     */     
/*  69 */     List<Block> debug9 = (List<Block>)Registry.BLOCK.stream().filter(debug1 -> !debug0.containsKey(debug1)).collect(Collectors.toList());
/*  70 */     if (!debug9.isEmpty()) {
/*  71 */       throw new IllegalStateException("Missing blockstate definitions for: " + debug9);
/*     */     }
/*     */     
/*  74 */     Registry.BLOCK.forEach(debug2 -> {
/*     */           Item debug3 = (Item)Item.BY_BLOCK.get(debug2);
/*     */           
/*     */           if (debug3 != null) {
/*     */             if (debug0.contains(debug3)) {
/*     */               return;
/*     */             }
/*     */             ResourceLocation debug4 = ModelLocationUtils.getModelLocation(debug3);
/*     */             if (!debug1.containsKey(debug4)) {
/*     */               debug1.put(debug4, new DelegatedModel(ModelLocationUtils.getModelLocation(debug2)));
/*     */             }
/*     */           } 
/*     */         });
/*  87 */     saveCollection(debug1, debug2, (Map)debug3, ModelProvider::createBlockStatePath);
/*  88 */     saveCollection(debug1, debug2, debug5, ModelProvider::createModelPath);
/*     */   }
/*     */   
/*     */   private <T> void saveCollection(HashCache debug1, Path debug2, Map<T, ? extends Supplier<JsonElement>> debug3, BiFunction<Path, T, Path> debug4) {
/*  92 */     debug3.forEach((debug3, debug4) -> {
/*     */           Path debug5 = debug0.apply(debug1, debug3);
/*     */           try {
/*     */             DataProvider.save(GSON, debug2, debug4.get(), debug5);
/*  96 */           } catch (Exception debug6) {
/*     */             LOGGER.error("Couldn't save {}", debug5, debug6);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private static Path createBlockStatePath(Path debug0, Block debug1) {
/* 103 */     ResourceLocation debug2 = Registry.BLOCK.getKey(debug1);
/* 104 */     return debug0.resolve("assets/" + debug2.getNamespace() + "/blockstates/" + debug2.getPath() + ".json");
/*     */   }
/*     */   
/*     */   private static Path createModelPath(Path debug0, ResourceLocation debug1) {
/* 108 */     return debug0.resolve("assets/" + debug1.getNamespace() + "/models/" + debug1.getPath() + ".json");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 113 */     return "Block State Definitions";
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\models\ModelProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */