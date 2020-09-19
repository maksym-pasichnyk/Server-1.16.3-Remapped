/*      */ package net.minecraft.data.recipes;
/*      */ 
/*      */ import com.google.common.collect.Sets;
/*      */ import com.google.gson.Gson;
/*      */ import com.google.gson.GsonBuilder;
/*      */ import com.google.gson.JsonElement;
/*      */ import com.google.gson.JsonObject;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.IOException;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.attribute.FileAttribute;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.function.Consumer;
/*      */ import net.minecraft.advancements.Advancement;
/*      */ import net.minecraft.advancements.CriterionTriggerInstance;
/*      */ import net.minecraft.advancements.critereon.EnterBlockTrigger;
/*      */ import net.minecraft.advancements.critereon.EntityPredicate;
/*      */ import net.minecraft.advancements.critereon.ImpossibleTrigger;
/*      */ import net.minecraft.advancements.critereon.InventoryChangeTrigger;
/*      */ import net.minecraft.advancements.critereon.ItemPredicate;
/*      */ import net.minecraft.advancements.critereon.MinMaxBounds;
/*      */ import net.minecraft.advancements.critereon.StatePropertiesPredicate;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.data.DataGenerator;
/*      */ import net.minecraft.data.DataProvider;
/*      */ import net.minecraft.data.HashCache;
/*      */ import net.minecraft.resources.ResourceLocation;
/*      */ import net.minecraft.tags.ItemTags;
/*      */ import net.minecraft.tags.Tag;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.crafting.Ingredient;
/*      */ import net.minecraft.world.item.crafting.RecipeSerializer;
/*      */ import net.minecraft.world.item.crafting.SimpleCookingSerializer;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class RecipeProvider
/*      */   implements DataProvider
/*      */ {
/*   53 */   private static final Logger LOGGER = LogManager.getLogger();
/*   54 */   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
/*      */   
/*      */   private final DataGenerator generator;
/*      */   
/*      */   public RecipeProvider(DataGenerator debug1) {
/*   59 */     this.generator = debug1;
/*      */   }
/*      */ 
/*      */   
/*      */   public void run(HashCache debug1) throws IOException {
/*   64 */     Path debug2 = this.generator.getOutputFolder();
/*   65 */     Set<ResourceLocation> debug3 = Sets.newHashSet();
/*   66 */     buildShapelessRecipes(debug3 -> {
/*      */           if (!debug0.add(debug3.getId())) {
/*      */             throw new IllegalStateException("Duplicate recipe " + debug3.getId());
/*      */           }
/*      */           
/*      */           saveRecipe(debug1, debug3.serializeRecipe(), debug2.resolve("data/" + debug3.getId().getNamespace() + "/recipes/" + debug3.getId().getPath() + ".json"));
/*      */           JsonObject debug4 = debug3.serializeAdvancement();
/*      */           if (debug4 != null) {
/*      */             saveAdvancement(debug1, debug4, debug2.resolve("data/" + debug3.getId().getNamespace() + "/advancements/" + debug3.getAdvancementId().getPath() + ".json"));
/*      */           }
/*      */         });
/*   77 */     saveAdvancement(debug1, Advancement.Builder.advancement().addCriterion("impossible", (CriterionTriggerInstance)new ImpossibleTrigger.TriggerInstance()).serializeToJson(), debug2.resolve("data/minecraft/advancements/recipes/root.json"));
/*      */   }
/*      */   
/*      */   private static void saveRecipe(HashCache debug0, JsonObject debug1, Path debug2) {
/*      */     try {
/*   82 */       String debug3 = GSON.toJson((JsonElement)debug1);
/*   83 */       String debug4 = SHA1.hashUnencodedChars(debug3).toString();
/*      */       
/*   85 */       if (!Objects.equals(debug0.getHash(debug2), debug4) || !Files.exists(debug2, new java.nio.file.LinkOption[0])) {
/*   86 */         Files.createDirectories(debug2.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
/*   87 */         try (BufferedWriter debug5 = Files.newBufferedWriter(debug2, new java.nio.file.OpenOption[0])) {
/*   88 */           debug5.write(debug3);
/*      */         } 
/*      */       } 
/*   91 */       debug0.putNew(debug2, debug4);
/*   92 */     } catch (IOException debug3) {
/*   93 */       LOGGER.error("Couldn't save recipe {}", debug2, debug3);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void saveAdvancement(HashCache debug0, JsonObject debug1, Path debug2) {
/*      */     try {
/*   99 */       String debug3 = GSON.toJson((JsonElement)debug1);
/*  100 */       String debug4 = SHA1.hashUnencodedChars(debug3).toString();
/*      */       
/*  102 */       if (!Objects.equals(debug0.getHash(debug2), debug4) || !Files.exists(debug2, new java.nio.file.LinkOption[0])) {
/*  103 */         Files.createDirectories(debug2.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
/*  104 */         try (BufferedWriter debug5 = Files.newBufferedWriter(debug2, new java.nio.file.OpenOption[0])) {
/*  105 */           debug5.write(debug3);
/*      */         } 
/*      */       } 
/*  108 */       debug0.putNew(debug2, debug4);
/*  109 */     } catch (IOException debug3) {
/*  110 */       LOGGER.error("Couldn't save recipe advancement {}", debug2, debug3);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void buildShapelessRecipes(Consumer<FinishedRecipe> debug0) {
/*  115 */     planksFromLog(debug0, (ItemLike)Blocks.ACACIA_PLANKS, (Tag<Item>)ItemTags.ACACIA_LOGS);
/*  116 */     planksFromLogs(debug0, (ItemLike)Blocks.BIRCH_PLANKS, (Tag<Item>)ItemTags.BIRCH_LOGS);
/*  117 */     planksFromLogs(debug0, (ItemLike)Blocks.CRIMSON_PLANKS, (Tag<Item>)ItemTags.CRIMSON_STEMS);
/*  118 */     planksFromLog(debug0, (ItemLike)Blocks.DARK_OAK_PLANKS, (Tag<Item>)ItemTags.DARK_OAK_LOGS);
/*  119 */     planksFromLogs(debug0, (ItemLike)Blocks.JUNGLE_PLANKS, (Tag<Item>)ItemTags.JUNGLE_LOGS);
/*  120 */     planksFromLogs(debug0, (ItemLike)Blocks.OAK_PLANKS, (Tag<Item>)ItemTags.OAK_LOGS);
/*  121 */     planksFromLogs(debug0, (ItemLike)Blocks.SPRUCE_PLANKS, (Tag<Item>)ItemTags.SPRUCE_LOGS);
/*  122 */     planksFromLogs(debug0, (ItemLike)Blocks.WARPED_PLANKS, (Tag<Item>)ItemTags.WARPED_STEMS);
/*      */     
/*  124 */     woodFromLogs(debug0, (ItemLike)Blocks.ACACIA_WOOD, (ItemLike)Blocks.ACACIA_LOG);
/*  125 */     woodFromLogs(debug0, (ItemLike)Blocks.BIRCH_WOOD, (ItemLike)Blocks.BIRCH_LOG);
/*  126 */     woodFromLogs(debug0, (ItemLike)Blocks.DARK_OAK_WOOD, (ItemLike)Blocks.DARK_OAK_LOG);
/*  127 */     woodFromLogs(debug0, (ItemLike)Blocks.JUNGLE_WOOD, (ItemLike)Blocks.JUNGLE_LOG);
/*  128 */     woodFromLogs(debug0, (ItemLike)Blocks.OAK_WOOD, (ItemLike)Blocks.OAK_LOG);
/*  129 */     woodFromLogs(debug0, (ItemLike)Blocks.SPRUCE_WOOD, (ItemLike)Blocks.SPRUCE_LOG);
/*  130 */     woodFromLogs(debug0, (ItemLike)Blocks.CRIMSON_HYPHAE, (ItemLike)Blocks.CRIMSON_STEM);
/*  131 */     woodFromLogs(debug0, (ItemLike)Blocks.WARPED_HYPHAE, (ItemLike)Blocks.WARPED_STEM);
/*      */     
/*  133 */     woodFromLogs(debug0, (ItemLike)Blocks.STRIPPED_ACACIA_WOOD, (ItemLike)Blocks.STRIPPED_ACACIA_LOG);
/*  134 */     woodFromLogs(debug0, (ItemLike)Blocks.STRIPPED_BIRCH_WOOD, (ItemLike)Blocks.STRIPPED_BIRCH_LOG);
/*  135 */     woodFromLogs(debug0, (ItemLike)Blocks.STRIPPED_DARK_OAK_WOOD, (ItemLike)Blocks.STRIPPED_DARK_OAK_LOG);
/*  136 */     woodFromLogs(debug0, (ItemLike)Blocks.STRIPPED_JUNGLE_WOOD, (ItemLike)Blocks.STRIPPED_JUNGLE_LOG);
/*  137 */     woodFromLogs(debug0, (ItemLike)Blocks.STRIPPED_OAK_WOOD, (ItemLike)Blocks.STRIPPED_OAK_LOG);
/*  138 */     woodFromLogs(debug0, (ItemLike)Blocks.STRIPPED_SPRUCE_WOOD, (ItemLike)Blocks.STRIPPED_SPRUCE_LOG);
/*  139 */     woodFromLogs(debug0, (ItemLike)Blocks.STRIPPED_CRIMSON_HYPHAE, (ItemLike)Blocks.STRIPPED_CRIMSON_STEM);
/*  140 */     woodFromLogs(debug0, (ItemLike)Blocks.STRIPPED_WARPED_HYPHAE, (ItemLike)Blocks.STRIPPED_WARPED_STEM);
/*      */     
/*  142 */     woodenBoat(debug0, (ItemLike)Items.ACACIA_BOAT, (ItemLike)Blocks.ACACIA_PLANKS);
/*  143 */     woodenBoat(debug0, (ItemLike)Items.BIRCH_BOAT, (ItemLike)Blocks.BIRCH_PLANKS);
/*  144 */     woodenBoat(debug0, (ItemLike)Items.DARK_OAK_BOAT, (ItemLike)Blocks.DARK_OAK_PLANKS);
/*  145 */     woodenBoat(debug0, (ItemLike)Items.JUNGLE_BOAT, (ItemLike)Blocks.JUNGLE_PLANKS);
/*  146 */     woodenBoat(debug0, (ItemLike)Items.OAK_BOAT, (ItemLike)Blocks.OAK_PLANKS);
/*  147 */     woodenBoat(debug0, (ItemLike)Items.SPRUCE_BOAT, (ItemLike)Blocks.SPRUCE_PLANKS);
/*      */     
/*  149 */     woodenButton(debug0, (ItemLike)Blocks.ACACIA_BUTTON, (ItemLike)Blocks.ACACIA_PLANKS);
/*  150 */     woodenDoor(debug0, (ItemLike)Blocks.ACACIA_DOOR, (ItemLike)Blocks.ACACIA_PLANKS);
/*  151 */     woodenFence(debug0, (ItemLike)Blocks.ACACIA_FENCE, (ItemLike)Blocks.ACACIA_PLANKS);
/*  152 */     woodenFenceGate(debug0, (ItemLike)Blocks.ACACIA_FENCE_GATE, (ItemLike)Blocks.ACACIA_PLANKS);
/*  153 */     woodenPressurePlate(debug0, (ItemLike)Blocks.ACACIA_PRESSURE_PLATE, (ItemLike)Blocks.ACACIA_PLANKS);
/*  154 */     woodenSlab(debug0, (ItemLike)Blocks.ACACIA_SLAB, (ItemLike)Blocks.ACACIA_PLANKS);
/*  155 */     woodenStairs(debug0, (ItemLike)Blocks.ACACIA_STAIRS, (ItemLike)Blocks.ACACIA_PLANKS);
/*  156 */     woodenTrapdoor(debug0, (ItemLike)Blocks.ACACIA_TRAPDOOR, (ItemLike)Blocks.ACACIA_PLANKS);
/*  157 */     woodenSign(debug0, (ItemLike)Blocks.ACACIA_SIGN, (ItemLike)Blocks.ACACIA_PLANKS);
/*      */     
/*  159 */     woodenButton(debug0, (ItemLike)Blocks.BIRCH_BUTTON, (ItemLike)Blocks.BIRCH_PLANKS);
/*  160 */     woodenDoor(debug0, (ItemLike)Blocks.BIRCH_DOOR, (ItemLike)Blocks.BIRCH_PLANKS);
/*  161 */     woodenFence(debug0, (ItemLike)Blocks.BIRCH_FENCE, (ItemLike)Blocks.BIRCH_PLANKS);
/*  162 */     woodenFenceGate(debug0, (ItemLike)Blocks.BIRCH_FENCE_GATE, (ItemLike)Blocks.BIRCH_PLANKS);
/*  163 */     woodenPressurePlate(debug0, (ItemLike)Blocks.BIRCH_PRESSURE_PLATE, (ItemLike)Blocks.BIRCH_PLANKS);
/*  164 */     woodenSlab(debug0, (ItemLike)Blocks.BIRCH_SLAB, (ItemLike)Blocks.BIRCH_PLANKS);
/*  165 */     woodenStairs(debug0, (ItemLike)Blocks.BIRCH_STAIRS, (ItemLike)Blocks.BIRCH_PLANKS);
/*  166 */     woodenTrapdoor(debug0, (ItemLike)Blocks.BIRCH_TRAPDOOR, (ItemLike)Blocks.BIRCH_PLANKS);
/*  167 */     woodenSign(debug0, (ItemLike)Blocks.BIRCH_SIGN, (ItemLike)Blocks.BIRCH_PLANKS);
/*      */     
/*  169 */     woodenButton(debug0, (ItemLike)Blocks.CRIMSON_BUTTON, (ItemLike)Blocks.CRIMSON_PLANKS);
/*  170 */     woodenDoor(debug0, (ItemLike)Blocks.CRIMSON_DOOR, (ItemLike)Blocks.CRIMSON_PLANKS);
/*  171 */     woodenFence(debug0, (ItemLike)Blocks.CRIMSON_FENCE, (ItemLike)Blocks.CRIMSON_PLANKS);
/*  172 */     woodenFenceGate(debug0, (ItemLike)Blocks.CRIMSON_FENCE_GATE, (ItemLike)Blocks.CRIMSON_PLANKS);
/*  173 */     woodenPressurePlate(debug0, (ItemLike)Blocks.CRIMSON_PRESSURE_PLATE, (ItemLike)Blocks.CRIMSON_PLANKS);
/*  174 */     woodenSlab(debug0, (ItemLike)Blocks.CRIMSON_SLAB, (ItemLike)Blocks.CRIMSON_PLANKS);
/*  175 */     woodenStairs(debug0, (ItemLike)Blocks.CRIMSON_STAIRS, (ItemLike)Blocks.CRIMSON_PLANKS);
/*  176 */     woodenTrapdoor(debug0, (ItemLike)Blocks.CRIMSON_TRAPDOOR, (ItemLike)Blocks.CRIMSON_PLANKS);
/*  177 */     woodenSign(debug0, (ItemLike)Blocks.CRIMSON_SIGN, (ItemLike)Blocks.CRIMSON_PLANKS);
/*      */     
/*  179 */     woodenButton(debug0, (ItemLike)Blocks.DARK_OAK_BUTTON, (ItemLike)Blocks.DARK_OAK_PLANKS);
/*  180 */     woodenDoor(debug0, (ItemLike)Blocks.DARK_OAK_DOOR, (ItemLike)Blocks.DARK_OAK_PLANKS);
/*  181 */     woodenFence(debug0, (ItemLike)Blocks.DARK_OAK_FENCE, (ItemLike)Blocks.DARK_OAK_PLANKS);
/*  182 */     woodenFenceGate(debug0, (ItemLike)Blocks.DARK_OAK_FENCE_GATE, (ItemLike)Blocks.DARK_OAK_PLANKS);
/*  183 */     woodenPressurePlate(debug0, (ItemLike)Blocks.DARK_OAK_PRESSURE_PLATE, (ItemLike)Blocks.DARK_OAK_PLANKS);
/*  184 */     woodenSlab(debug0, (ItemLike)Blocks.DARK_OAK_SLAB, (ItemLike)Blocks.DARK_OAK_PLANKS);
/*  185 */     woodenStairs(debug0, (ItemLike)Blocks.DARK_OAK_STAIRS, (ItemLike)Blocks.DARK_OAK_PLANKS);
/*  186 */     woodenTrapdoor(debug0, (ItemLike)Blocks.DARK_OAK_TRAPDOOR, (ItemLike)Blocks.DARK_OAK_PLANKS);
/*  187 */     woodenSign(debug0, (ItemLike)Blocks.DARK_OAK_SIGN, (ItemLike)Blocks.DARK_OAK_PLANKS);
/*      */     
/*  189 */     woodenButton(debug0, (ItemLike)Blocks.JUNGLE_BUTTON, (ItemLike)Blocks.JUNGLE_PLANKS);
/*  190 */     woodenDoor(debug0, (ItemLike)Blocks.JUNGLE_DOOR, (ItemLike)Blocks.JUNGLE_PLANKS);
/*  191 */     woodenFence(debug0, (ItemLike)Blocks.JUNGLE_FENCE, (ItemLike)Blocks.JUNGLE_PLANKS);
/*  192 */     woodenFenceGate(debug0, (ItemLike)Blocks.JUNGLE_FENCE_GATE, (ItemLike)Blocks.JUNGLE_PLANKS);
/*  193 */     woodenPressurePlate(debug0, (ItemLike)Blocks.JUNGLE_PRESSURE_PLATE, (ItemLike)Blocks.JUNGLE_PLANKS);
/*  194 */     woodenSlab(debug0, (ItemLike)Blocks.JUNGLE_SLAB, (ItemLike)Blocks.JUNGLE_PLANKS);
/*  195 */     woodenStairs(debug0, (ItemLike)Blocks.JUNGLE_STAIRS, (ItemLike)Blocks.JUNGLE_PLANKS);
/*  196 */     woodenTrapdoor(debug0, (ItemLike)Blocks.JUNGLE_TRAPDOOR, (ItemLike)Blocks.JUNGLE_PLANKS);
/*  197 */     woodenSign(debug0, (ItemLike)Blocks.JUNGLE_SIGN, (ItemLike)Blocks.JUNGLE_PLANKS);
/*      */     
/*  199 */     woodenButton(debug0, (ItemLike)Blocks.OAK_BUTTON, (ItemLike)Blocks.OAK_PLANKS);
/*  200 */     woodenDoor(debug0, (ItemLike)Blocks.OAK_DOOR, (ItemLike)Blocks.OAK_PLANKS);
/*  201 */     woodenFence(debug0, (ItemLike)Blocks.OAK_FENCE, (ItemLike)Blocks.OAK_PLANKS);
/*  202 */     woodenFenceGate(debug0, (ItemLike)Blocks.OAK_FENCE_GATE, (ItemLike)Blocks.OAK_PLANKS);
/*  203 */     woodenPressurePlate(debug0, (ItemLike)Blocks.OAK_PRESSURE_PLATE, (ItemLike)Blocks.OAK_PLANKS);
/*  204 */     woodenSlab(debug0, (ItemLike)Blocks.OAK_SLAB, (ItemLike)Blocks.OAK_PLANKS);
/*  205 */     woodenStairs(debug0, (ItemLike)Blocks.OAK_STAIRS, (ItemLike)Blocks.OAK_PLANKS);
/*  206 */     woodenTrapdoor(debug0, (ItemLike)Blocks.OAK_TRAPDOOR, (ItemLike)Blocks.OAK_PLANKS);
/*  207 */     woodenSign(debug0, (ItemLike)Blocks.OAK_SIGN, (ItemLike)Blocks.OAK_PLANKS);
/*      */     
/*  209 */     woodenButton(debug0, (ItemLike)Blocks.SPRUCE_BUTTON, (ItemLike)Blocks.SPRUCE_PLANKS);
/*  210 */     woodenDoor(debug0, (ItemLike)Blocks.SPRUCE_DOOR, (ItemLike)Blocks.SPRUCE_PLANKS);
/*  211 */     woodenFence(debug0, (ItemLike)Blocks.SPRUCE_FENCE, (ItemLike)Blocks.SPRUCE_PLANKS);
/*  212 */     woodenFenceGate(debug0, (ItemLike)Blocks.SPRUCE_FENCE_GATE, (ItemLike)Blocks.SPRUCE_PLANKS);
/*  213 */     woodenPressurePlate(debug0, (ItemLike)Blocks.SPRUCE_PRESSURE_PLATE, (ItemLike)Blocks.SPRUCE_PLANKS);
/*  214 */     woodenSlab(debug0, (ItemLike)Blocks.SPRUCE_SLAB, (ItemLike)Blocks.SPRUCE_PLANKS);
/*  215 */     woodenStairs(debug0, (ItemLike)Blocks.SPRUCE_STAIRS, (ItemLike)Blocks.SPRUCE_PLANKS);
/*  216 */     woodenTrapdoor(debug0, (ItemLike)Blocks.SPRUCE_TRAPDOOR, (ItemLike)Blocks.SPRUCE_PLANKS);
/*  217 */     woodenSign(debug0, (ItemLike)Blocks.SPRUCE_SIGN, (ItemLike)Blocks.SPRUCE_PLANKS);
/*      */     
/*  219 */     woodenButton(debug0, (ItemLike)Blocks.WARPED_BUTTON, (ItemLike)Blocks.WARPED_PLANKS);
/*  220 */     woodenDoor(debug0, (ItemLike)Blocks.WARPED_DOOR, (ItemLike)Blocks.WARPED_PLANKS);
/*  221 */     woodenFence(debug0, (ItemLike)Blocks.WARPED_FENCE, (ItemLike)Blocks.WARPED_PLANKS);
/*  222 */     woodenFenceGate(debug0, (ItemLike)Blocks.WARPED_FENCE_GATE, (ItemLike)Blocks.WARPED_PLANKS);
/*  223 */     woodenPressurePlate(debug0, (ItemLike)Blocks.WARPED_PRESSURE_PLATE, (ItemLike)Blocks.WARPED_PLANKS);
/*  224 */     woodenSlab(debug0, (ItemLike)Blocks.WARPED_SLAB, (ItemLike)Blocks.WARPED_PLANKS);
/*  225 */     woodenStairs(debug0, (ItemLike)Blocks.WARPED_STAIRS, (ItemLike)Blocks.WARPED_PLANKS);
/*  226 */     woodenTrapdoor(debug0, (ItemLike)Blocks.WARPED_TRAPDOOR, (ItemLike)Blocks.WARPED_PLANKS);
/*  227 */     woodenSign(debug0, (ItemLike)Blocks.WARPED_SIGN, (ItemLike)Blocks.WARPED_PLANKS);
/*      */ 
/*      */     
/*  230 */     coloredWoolFromWhiteWoolAndDye(debug0, (ItemLike)Blocks.BLACK_WOOL, (ItemLike)Items.BLACK_DYE);
/*  231 */     carpetFromWool(debug0, (ItemLike)Blocks.BLACK_CARPET, (ItemLike)Blocks.BLACK_WOOL);
/*  232 */     coloredCarpetFromWhiteCarpetAndDye(debug0, (ItemLike)Blocks.BLACK_CARPET, (ItemLike)Items.BLACK_DYE);
/*  233 */     bedFromPlanksAndWool(debug0, (ItemLike)Items.BLACK_BED, (ItemLike)Blocks.BLACK_WOOL);
/*  234 */     bedFromWhiteBedAndDye(debug0, (ItemLike)Items.BLACK_BED, (ItemLike)Items.BLACK_DYE);
/*  235 */     banner(debug0, (ItemLike)Items.BLACK_BANNER, (ItemLike)Blocks.BLACK_WOOL);
/*      */     
/*  237 */     coloredWoolFromWhiteWoolAndDye(debug0, (ItemLike)Blocks.BLUE_WOOL, (ItemLike)Items.BLUE_DYE);
/*  238 */     carpetFromWool(debug0, (ItemLike)Blocks.BLUE_CARPET, (ItemLike)Blocks.BLUE_WOOL);
/*  239 */     coloredCarpetFromWhiteCarpetAndDye(debug0, (ItemLike)Blocks.BLUE_CARPET, (ItemLike)Items.BLUE_DYE);
/*  240 */     bedFromPlanksAndWool(debug0, (ItemLike)Items.BLUE_BED, (ItemLike)Blocks.BLUE_WOOL);
/*  241 */     bedFromWhiteBedAndDye(debug0, (ItemLike)Items.BLUE_BED, (ItemLike)Items.BLUE_DYE);
/*  242 */     banner(debug0, (ItemLike)Items.BLUE_BANNER, (ItemLike)Blocks.BLUE_WOOL);
/*      */     
/*  244 */     coloredWoolFromWhiteWoolAndDye(debug0, (ItemLike)Blocks.BROWN_WOOL, (ItemLike)Items.BROWN_DYE);
/*  245 */     carpetFromWool(debug0, (ItemLike)Blocks.BROWN_CARPET, (ItemLike)Blocks.BROWN_WOOL);
/*  246 */     coloredCarpetFromWhiteCarpetAndDye(debug0, (ItemLike)Blocks.BROWN_CARPET, (ItemLike)Items.BROWN_DYE);
/*  247 */     bedFromPlanksAndWool(debug0, (ItemLike)Items.BROWN_BED, (ItemLike)Blocks.BROWN_WOOL);
/*  248 */     bedFromWhiteBedAndDye(debug0, (ItemLike)Items.BROWN_BED, (ItemLike)Items.BROWN_DYE);
/*  249 */     banner(debug0, (ItemLike)Items.BROWN_BANNER, (ItemLike)Blocks.BROWN_WOOL);
/*      */     
/*  251 */     coloredWoolFromWhiteWoolAndDye(debug0, (ItemLike)Blocks.CYAN_WOOL, (ItemLike)Items.CYAN_DYE);
/*  252 */     carpetFromWool(debug0, (ItemLike)Blocks.CYAN_CARPET, (ItemLike)Blocks.CYAN_WOOL);
/*  253 */     coloredCarpetFromWhiteCarpetAndDye(debug0, (ItemLike)Blocks.CYAN_CARPET, (ItemLike)Items.CYAN_DYE);
/*  254 */     bedFromPlanksAndWool(debug0, (ItemLike)Items.CYAN_BED, (ItemLike)Blocks.CYAN_WOOL);
/*  255 */     bedFromWhiteBedAndDye(debug0, (ItemLike)Items.CYAN_BED, (ItemLike)Items.CYAN_DYE);
/*  256 */     banner(debug0, (ItemLike)Items.CYAN_BANNER, (ItemLike)Blocks.CYAN_WOOL);
/*      */     
/*  258 */     coloredWoolFromWhiteWoolAndDye(debug0, (ItemLike)Blocks.GRAY_WOOL, (ItemLike)Items.GRAY_DYE);
/*  259 */     carpetFromWool(debug0, (ItemLike)Blocks.GRAY_CARPET, (ItemLike)Blocks.GRAY_WOOL);
/*  260 */     coloredCarpetFromWhiteCarpetAndDye(debug0, (ItemLike)Blocks.GRAY_CARPET, (ItemLike)Items.GRAY_DYE);
/*  261 */     bedFromPlanksAndWool(debug0, (ItemLike)Items.GRAY_BED, (ItemLike)Blocks.GRAY_WOOL);
/*  262 */     bedFromWhiteBedAndDye(debug0, (ItemLike)Items.GRAY_BED, (ItemLike)Items.GRAY_DYE);
/*  263 */     banner(debug0, (ItemLike)Items.GRAY_BANNER, (ItemLike)Blocks.GRAY_WOOL);
/*      */     
/*  265 */     coloredWoolFromWhiteWoolAndDye(debug0, (ItemLike)Blocks.GREEN_WOOL, (ItemLike)Items.GREEN_DYE);
/*  266 */     carpetFromWool(debug0, (ItemLike)Blocks.GREEN_CARPET, (ItemLike)Blocks.GREEN_WOOL);
/*  267 */     coloredCarpetFromWhiteCarpetAndDye(debug0, (ItemLike)Blocks.GREEN_CARPET, (ItemLike)Items.GREEN_DYE);
/*  268 */     bedFromPlanksAndWool(debug0, (ItemLike)Items.GREEN_BED, (ItemLike)Blocks.GREEN_WOOL);
/*  269 */     bedFromWhiteBedAndDye(debug0, (ItemLike)Items.GREEN_BED, (ItemLike)Items.GREEN_DYE);
/*  270 */     banner(debug0, (ItemLike)Items.GREEN_BANNER, (ItemLike)Blocks.GREEN_WOOL);
/*      */     
/*  272 */     coloredWoolFromWhiteWoolAndDye(debug0, (ItemLike)Blocks.LIGHT_BLUE_WOOL, (ItemLike)Items.LIGHT_BLUE_DYE);
/*  273 */     carpetFromWool(debug0, (ItemLike)Blocks.LIGHT_BLUE_CARPET, (ItemLike)Blocks.LIGHT_BLUE_WOOL);
/*  274 */     coloredCarpetFromWhiteCarpetAndDye(debug0, (ItemLike)Blocks.LIGHT_BLUE_CARPET, (ItemLike)Items.LIGHT_BLUE_DYE);
/*  275 */     bedFromPlanksAndWool(debug0, (ItemLike)Items.LIGHT_BLUE_BED, (ItemLike)Blocks.LIGHT_BLUE_WOOL);
/*  276 */     bedFromWhiteBedAndDye(debug0, (ItemLike)Items.LIGHT_BLUE_BED, (ItemLike)Items.LIGHT_BLUE_DYE);
/*  277 */     banner(debug0, (ItemLike)Items.LIGHT_BLUE_BANNER, (ItemLike)Blocks.LIGHT_BLUE_WOOL);
/*      */     
/*  279 */     coloredWoolFromWhiteWoolAndDye(debug0, (ItemLike)Blocks.LIGHT_GRAY_WOOL, (ItemLike)Items.LIGHT_GRAY_DYE);
/*  280 */     carpetFromWool(debug0, (ItemLike)Blocks.LIGHT_GRAY_CARPET, (ItemLike)Blocks.LIGHT_GRAY_WOOL);
/*  281 */     coloredCarpetFromWhiteCarpetAndDye(debug0, (ItemLike)Blocks.LIGHT_GRAY_CARPET, (ItemLike)Items.LIGHT_GRAY_DYE);
/*  282 */     bedFromPlanksAndWool(debug0, (ItemLike)Items.LIGHT_GRAY_BED, (ItemLike)Blocks.LIGHT_GRAY_WOOL);
/*  283 */     bedFromWhiteBedAndDye(debug0, (ItemLike)Items.LIGHT_GRAY_BED, (ItemLike)Items.LIGHT_GRAY_DYE);
/*  284 */     banner(debug0, (ItemLike)Items.LIGHT_GRAY_BANNER, (ItemLike)Blocks.LIGHT_GRAY_WOOL);
/*      */     
/*  286 */     coloredWoolFromWhiteWoolAndDye(debug0, (ItemLike)Blocks.LIME_WOOL, (ItemLike)Items.LIME_DYE);
/*  287 */     carpetFromWool(debug0, (ItemLike)Blocks.LIME_CARPET, (ItemLike)Blocks.LIME_WOOL);
/*  288 */     coloredCarpetFromWhiteCarpetAndDye(debug0, (ItemLike)Blocks.LIME_CARPET, (ItemLike)Items.LIME_DYE);
/*  289 */     bedFromPlanksAndWool(debug0, (ItemLike)Items.LIME_BED, (ItemLike)Blocks.LIME_WOOL);
/*  290 */     bedFromWhiteBedAndDye(debug0, (ItemLike)Items.LIME_BED, (ItemLike)Items.LIME_DYE);
/*  291 */     banner(debug0, (ItemLike)Items.LIME_BANNER, (ItemLike)Blocks.LIME_WOOL);
/*      */     
/*  293 */     coloredWoolFromWhiteWoolAndDye(debug0, (ItemLike)Blocks.MAGENTA_WOOL, (ItemLike)Items.MAGENTA_DYE);
/*  294 */     carpetFromWool(debug0, (ItemLike)Blocks.MAGENTA_CARPET, (ItemLike)Blocks.MAGENTA_WOOL);
/*  295 */     coloredCarpetFromWhiteCarpetAndDye(debug0, (ItemLike)Blocks.MAGENTA_CARPET, (ItemLike)Items.MAGENTA_DYE);
/*  296 */     bedFromPlanksAndWool(debug0, (ItemLike)Items.MAGENTA_BED, (ItemLike)Blocks.MAGENTA_WOOL);
/*  297 */     bedFromWhiteBedAndDye(debug0, (ItemLike)Items.MAGENTA_BED, (ItemLike)Items.MAGENTA_DYE);
/*  298 */     banner(debug0, (ItemLike)Items.MAGENTA_BANNER, (ItemLike)Blocks.MAGENTA_WOOL);
/*      */     
/*  300 */     coloredWoolFromWhiteWoolAndDye(debug0, (ItemLike)Blocks.ORANGE_WOOL, (ItemLike)Items.ORANGE_DYE);
/*  301 */     carpetFromWool(debug0, (ItemLike)Blocks.ORANGE_CARPET, (ItemLike)Blocks.ORANGE_WOOL);
/*  302 */     coloredCarpetFromWhiteCarpetAndDye(debug0, (ItemLike)Blocks.ORANGE_CARPET, (ItemLike)Items.ORANGE_DYE);
/*  303 */     bedFromPlanksAndWool(debug0, (ItemLike)Items.ORANGE_BED, (ItemLike)Blocks.ORANGE_WOOL);
/*  304 */     bedFromWhiteBedAndDye(debug0, (ItemLike)Items.ORANGE_BED, (ItemLike)Items.ORANGE_DYE);
/*  305 */     banner(debug0, (ItemLike)Items.ORANGE_BANNER, (ItemLike)Blocks.ORANGE_WOOL);
/*      */     
/*  307 */     coloredWoolFromWhiteWoolAndDye(debug0, (ItemLike)Blocks.PINK_WOOL, (ItemLike)Items.PINK_DYE);
/*  308 */     carpetFromWool(debug0, (ItemLike)Blocks.PINK_CARPET, (ItemLike)Blocks.PINK_WOOL);
/*  309 */     coloredCarpetFromWhiteCarpetAndDye(debug0, (ItemLike)Blocks.PINK_CARPET, (ItemLike)Items.PINK_DYE);
/*  310 */     bedFromPlanksAndWool(debug0, (ItemLike)Items.PINK_BED, (ItemLike)Blocks.PINK_WOOL);
/*  311 */     bedFromWhiteBedAndDye(debug0, (ItemLike)Items.PINK_BED, (ItemLike)Items.PINK_DYE);
/*  312 */     banner(debug0, (ItemLike)Items.PINK_BANNER, (ItemLike)Blocks.PINK_WOOL);
/*      */     
/*  314 */     coloredWoolFromWhiteWoolAndDye(debug0, (ItemLike)Blocks.PURPLE_WOOL, (ItemLike)Items.PURPLE_DYE);
/*  315 */     carpetFromWool(debug0, (ItemLike)Blocks.PURPLE_CARPET, (ItemLike)Blocks.PURPLE_WOOL);
/*  316 */     coloredCarpetFromWhiteCarpetAndDye(debug0, (ItemLike)Blocks.PURPLE_CARPET, (ItemLike)Items.PURPLE_DYE);
/*  317 */     bedFromPlanksAndWool(debug0, (ItemLike)Items.PURPLE_BED, (ItemLike)Blocks.PURPLE_WOOL);
/*  318 */     bedFromWhiteBedAndDye(debug0, (ItemLike)Items.PURPLE_BED, (ItemLike)Items.PURPLE_DYE);
/*  319 */     banner(debug0, (ItemLike)Items.PURPLE_BANNER, (ItemLike)Blocks.PURPLE_WOOL);
/*      */     
/*  321 */     coloredWoolFromWhiteWoolAndDye(debug0, (ItemLike)Blocks.RED_WOOL, (ItemLike)Items.RED_DYE);
/*  322 */     carpetFromWool(debug0, (ItemLike)Blocks.RED_CARPET, (ItemLike)Blocks.RED_WOOL);
/*  323 */     coloredCarpetFromWhiteCarpetAndDye(debug0, (ItemLike)Blocks.RED_CARPET, (ItemLike)Items.RED_DYE);
/*  324 */     bedFromPlanksAndWool(debug0, (ItemLike)Items.RED_BED, (ItemLike)Blocks.RED_WOOL);
/*  325 */     bedFromWhiteBedAndDye(debug0, (ItemLike)Items.RED_BED, (ItemLike)Items.RED_DYE);
/*  326 */     banner(debug0, (ItemLike)Items.RED_BANNER, (ItemLike)Blocks.RED_WOOL);
/*      */     
/*  328 */     carpetFromWool(debug0, (ItemLike)Blocks.WHITE_CARPET, (ItemLike)Blocks.WHITE_WOOL);
/*  329 */     bedFromPlanksAndWool(debug0, (ItemLike)Items.WHITE_BED, (ItemLike)Blocks.WHITE_WOOL);
/*  330 */     banner(debug0, (ItemLike)Items.WHITE_BANNER, (ItemLike)Blocks.WHITE_WOOL);
/*      */     
/*  332 */     coloredWoolFromWhiteWoolAndDye(debug0, (ItemLike)Blocks.YELLOW_WOOL, (ItemLike)Items.YELLOW_DYE);
/*  333 */     carpetFromWool(debug0, (ItemLike)Blocks.YELLOW_CARPET, (ItemLike)Blocks.YELLOW_WOOL);
/*  334 */     coloredCarpetFromWhiteCarpetAndDye(debug0, (ItemLike)Blocks.YELLOW_CARPET, (ItemLike)Items.YELLOW_DYE);
/*  335 */     bedFromPlanksAndWool(debug0, (ItemLike)Items.YELLOW_BED, (ItemLike)Blocks.YELLOW_WOOL);
/*  336 */     bedFromWhiteBedAndDye(debug0, (ItemLike)Items.YELLOW_BED, (ItemLike)Items.YELLOW_DYE);
/*  337 */     banner(debug0, (ItemLike)Items.YELLOW_BANNER, (ItemLike)Blocks.YELLOW_WOOL);
/*      */ 
/*      */     
/*  340 */     stainedGlassFromGlassAndDye(debug0, (ItemLike)Blocks.BLACK_STAINED_GLASS, (ItemLike)Items.BLACK_DYE);
/*  341 */     stainedGlassPaneFromStainedGlass(debug0, (ItemLike)Blocks.BLACK_STAINED_GLASS_PANE, (ItemLike)Blocks.BLACK_STAINED_GLASS);
/*  342 */     stainedGlassPaneFromGlassPaneAndDye(debug0, (ItemLike)Blocks.BLACK_STAINED_GLASS_PANE, (ItemLike)Items.BLACK_DYE);
/*      */     
/*  344 */     stainedGlassFromGlassAndDye(debug0, (ItemLike)Blocks.BLUE_STAINED_GLASS, (ItemLike)Items.BLUE_DYE);
/*  345 */     stainedGlassPaneFromStainedGlass(debug0, (ItemLike)Blocks.BLUE_STAINED_GLASS_PANE, (ItemLike)Blocks.BLUE_STAINED_GLASS);
/*  346 */     stainedGlassPaneFromGlassPaneAndDye(debug0, (ItemLike)Blocks.BLUE_STAINED_GLASS_PANE, (ItemLike)Items.BLUE_DYE);
/*      */     
/*  348 */     stainedGlassFromGlassAndDye(debug0, (ItemLike)Blocks.BROWN_STAINED_GLASS, (ItemLike)Items.BROWN_DYE);
/*  349 */     stainedGlassPaneFromStainedGlass(debug0, (ItemLike)Blocks.BROWN_STAINED_GLASS_PANE, (ItemLike)Blocks.BROWN_STAINED_GLASS);
/*  350 */     stainedGlassPaneFromGlassPaneAndDye(debug0, (ItemLike)Blocks.BROWN_STAINED_GLASS_PANE, (ItemLike)Items.BROWN_DYE);
/*      */     
/*  352 */     stainedGlassFromGlassAndDye(debug0, (ItemLike)Blocks.CYAN_STAINED_GLASS, (ItemLike)Items.CYAN_DYE);
/*  353 */     stainedGlassPaneFromStainedGlass(debug0, (ItemLike)Blocks.CYAN_STAINED_GLASS_PANE, (ItemLike)Blocks.CYAN_STAINED_GLASS);
/*  354 */     stainedGlassPaneFromGlassPaneAndDye(debug0, (ItemLike)Blocks.CYAN_STAINED_GLASS_PANE, (ItemLike)Items.CYAN_DYE);
/*      */     
/*  356 */     stainedGlassFromGlassAndDye(debug0, (ItemLike)Blocks.GRAY_STAINED_GLASS, (ItemLike)Items.GRAY_DYE);
/*  357 */     stainedGlassPaneFromStainedGlass(debug0, (ItemLike)Blocks.GRAY_STAINED_GLASS_PANE, (ItemLike)Blocks.GRAY_STAINED_GLASS);
/*  358 */     stainedGlassPaneFromGlassPaneAndDye(debug0, (ItemLike)Blocks.GRAY_STAINED_GLASS_PANE, (ItemLike)Items.GRAY_DYE);
/*      */     
/*  360 */     stainedGlassFromGlassAndDye(debug0, (ItemLike)Blocks.GREEN_STAINED_GLASS, (ItemLike)Items.GREEN_DYE);
/*  361 */     stainedGlassPaneFromStainedGlass(debug0, (ItemLike)Blocks.GREEN_STAINED_GLASS_PANE, (ItemLike)Blocks.GREEN_STAINED_GLASS);
/*  362 */     stainedGlassPaneFromGlassPaneAndDye(debug0, (ItemLike)Blocks.GREEN_STAINED_GLASS_PANE, (ItemLike)Items.GREEN_DYE);
/*      */     
/*  364 */     stainedGlassFromGlassAndDye(debug0, (ItemLike)Blocks.LIGHT_BLUE_STAINED_GLASS, (ItemLike)Items.LIGHT_BLUE_DYE);
/*  365 */     stainedGlassPaneFromStainedGlass(debug0, (ItemLike)Blocks.LIGHT_BLUE_STAINED_GLASS_PANE, (ItemLike)Blocks.LIGHT_BLUE_STAINED_GLASS);
/*  366 */     stainedGlassPaneFromGlassPaneAndDye(debug0, (ItemLike)Blocks.LIGHT_BLUE_STAINED_GLASS_PANE, (ItemLike)Items.LIGHT_BLUE_DYE);
/*      */     
/*  368 */     stainedGlassFromGlassAndDye(debug0, (ItemLike)Blocks.LIGHT_GRAY_STAINED_GLASS, (ItemLike)Items.LIGHT_GRAY_DYE);
/*  369 */     stainedGlassPaneFromStainedGlass(debug0, (ItemLike)Blocks.LIGHT_GRAY_STAINED_GLASS_PANE, (ItemLike)Blocks.LIGHT_GRAY_STAINED_GLASS);
/*  370 */     stainedGlassPaneFromGlassPaneAndDye(debug0, (ItemLike)Blocks.LIGHT_GRAY_STAINED_GLASS_PANE, (ItemLike)Items.LIGHT_GRAY_DYE);
/*      */     
/*  372 */     stainedGlassFromGlassAndDye(debug0, (ItemLike)Blocks.LIME_STAINED_GLASS, (ItemLike)Items.LIME_DYE);
/*  373 */     stainedGlassPaneFromStainedGlass(debug0, (ItemLike)Blocks.LIME_STAINED_GLASS_PANE, (ItemLike)Blocks.LIME_STAINED_GLASS);
/*  374 */     stainedGlassPaneFromGlassPaneAndDye(debug0, (ItemLike)Blocks.LIME_STAINED_GLASS_PANE, (ItemLike)Items.LIME_DYE);
/*      */     
/*  376 */     stainedGlassFromGlassAndDye(debug0, (ItemLike)Blocks.MAGENTA_STAINED_GLASS, (ItemLike)Items.MAGENTA_DYE);
/*  377 */     stainedGlassPaneFromStainedGlass(debug0, (ItemLike)Blocks.MAGENTA_STAINED_GLASS_PANE, (ItemLike)Blocks.MAGENTA_STAINED_GLASS);
/*  378 */     stainedGlassPaneFromGlassPaneAndDye(debug0, (ItemLike)Blocks.MAGENTA_STAINED_GLASS_PANE, (ItemLike)Items.MAGENTA_DYE);
/*      */     
/*  380 */     stainedGlassFromGlassAndDye(debug0, (ItemLike)Blocks.ORANGE_STAINED_GLASS, (ItemLike)Items.ORANGE_DYE);
/*  381 */     stainedGlassPaneFromStainedGlass(debug0, (ItemLike)Blocks.ORANGE_STAINED_GLASS_PANE, (ItemLike)Blocks.ORANGE_STAINED_GLASS);
/*  382 */     stainedGlassPaneFromGlassPaneAndDye(debug0, (ItemLike)Blocks.ORANGE_STAINED_GLASS_PANE, (ItemLike)Items.ORANGE_DYE);
/*      */     
/*  384 */     stainedGlassFromGlassAndDye(debug0, (ItemLike)Blocks.PINK_STAINED_GLASS, (ItemLike)Items.PINK_DYE);
/*  385 */     stainedGlassPaneFromStainedGlass(debug0, (ItemLike)Blocks.PINK_STAINED_GLASS_PANE, (ItemLike)Blocks.PINK_STAINED_GLASS);
/*  386 */     stainedGlassPaneFromGlassPaneAndDye(debug0, (ItemLike)Blocks.PINK_STAINED_GLASS_PANE, (ItemLike)Items.PINK_DYE);
/*      */     
/*  388 */     stainedGlassFromGlassAndDye(debug0, (ItemLike)Blocks.PURPLE_STAINED_GLASS, (ItemLike)Items.PURPLE_DYE);
/*  389 */     stainedGlassPaneFromStainedGlass(debug0, (ItemLike)Blocks.PURPLE_STAINED_GLASS_PANE, (ItemLike)Blocks.PURPLE_STAINED_GLASS);
/*  390 */     stainedGlassPaneFromGlassPaneAndDye(debug0, (ItemLike)Blocks.PURPLE_STAINED_GLASS_PANE, (ItemLike)Items.PURPLE_DYE);
/*      */     
/*  392 */     stainedGlassFromGlassAndDye(debug0, (ItemLike)Blocks.RED_STAINED_GLASS, (ItemLike)Items.RED_DYE);
/*  393 */     stainedGlassPaneFromStainedGlass(debug0, (ItemLike)Blocks.RED_STAINED_GLASS_PANE, (ItemLike)Blocks.RED_STAINED_GLASS);
/*  394 */     stainedGlassPaneFromGlassPaneAndDye(debug0, (ItemLike)Blocks.RED_STAINED_GLASS_PANE, (ItemLike)Items.RED_DYE);
/*      */     
/*  396 */     stainedGlassFromGlassAndDye(debug0, (ItemLike)Blocks.WHITE_STAINED_GLASS, (ItemLike)Items.WHITE_DYE);
/*  397 */     stainedGlassPaneFromStainedGlass(debug0, (ItemLike)Blocks.WHITE_STAINED_GLASS_PANE, (ItemLike)Blocks.WHITE_STAINED_GLASS);
/*  398 */     stainedGlassPaneFromGlassPaneAndDye(debug0, (ItemLike)Blocks.WHITE_STAINED_GLASS_PANE, (ItemLike)Items.WHITE_DYE);
/*      */     
/*  400 */     stainedGlassFromGlassAndDye(debug0, (ItemLike)Blocks.YELLOW_STAINED_GLASS, (ItemLike)Items.YELLOW_DYE);
/*  401 */     stainedGlassPaneFromStainedGlass(debug0, (ItemLike)Blocks.YELLOW_STAINED_GLASS_PANE, (ItemLike)Blocks.YELLOW_STAINED_GLASS);
/*  402 */     stainedGlassPaneFromGlassPaneAndDye(debug0, (ItemLike)Blocks.YELLOW_STAINED_GLASS_PANE, (ItemLike)Items.YELLOW_DYE);
/*      */ 
/*      */     
/*  405 */     coloredTerracottaFromTerracottaAndDye(debug0, (ItemLike)Blocks.BLACK_TERRACOTTA, (ItemLike)Items.BLACK_DYE);
/*  406 */     coloredTerracottaFromTerracottaAndDye(debug0, (ItemLike)Blocks.BLUE_TERRACOTTA, (ItemLike)Items.BLUE_DYE);
/*  407 */     coloredTerracottaFromTerracottaAndDye(debug0, (ItemLike)Blocks.BROWN_TERRACOTTA, (ItemLike)Items.BROWN_DYE);
/*  408 */     coloredTerracottaFromTerracottaAndDye(debug0, (ItemLike)Blocks.CYAN_TERRACOTTA, (ItemLike)Items.CYAN_DYE);
/*  409 */     coloredTerracottaFromTerracottaAndDye(debug0, (ItemLike)Blocks.GRAY_TERRACOTTA, (ItemLike)Items.GRAY_DYE);
/*  410 */     coloredTerracottaFromTerracottaAndDye(debug0, (ItemLike)Blocks.GREEN_TERRACOTTA, (ItemLike)Items.GREEN_DYE);
/*  411 */     coloredTerracottaFromTerracottaAndDye(debug0, (ItemLike)Blocks.LIGHT_BLUE_TERRACOTTA, (ItemLike)Items.LIGHT_BLUE_DYE);
/*  412 */     coloredTerracottaFromTerracottaAndDye(debug0, (ItemLike)Blocks.LIGHT_GRAY_TERRACOTTA, (ItemLike)Items.LIGHT_GRAY_DYE);
/*  413 */     coloredTerracottaFromTerracottaAndDye(debug0, (ItemLike)Blocks.LIME_TERRACOTTA, (ItemLike)Items.LIME_DYE);
/*  414 */     coloredTerracottaFromTerracottaAndDye(debug0, (ItemLike)Blocks.MAGENTA_TERRACOTTA, (ItemLike)Items.MAGENTA_DYE);
/*  415 */     coloredTerracottaFromTerracottaAndDye(debug0, (ItemLike)Blocks.ORANGE_TERRACOTTA, (ItemLike)Items.ORANGE_DYE);
/*  416 */     coloredTerracottaFromTerracottaAndDye(debug0, (ItemLike)Blocks.PINK_TERRACOTTA, (ItemLike)Items.PINK_DYE);
/*  417 */     coloredTerracottaFromTerracottaAndDye(debug0, (ItemLike)Blocks.PURPLE_TERRACOTTA, (ItemLike)Items.PURPLE_DYE);
/*  418 */     coloredTerracottaFromTerracottaAndDye(debug0, (ItemLike)Blocks.RED_TERRACOTTA, (ItemLike)Items.RED_DYE);
/*  419 */     coloredTerracottaFromTerracottaAndDye(debug0, (ItemLike)Blocks.WHITE_TERRACOTTA, (ItemLike)Items.WHITE_DYE);
/*  420 */     coloredTerracottaFromTerracottaAndDye(debug0, (ItemLike)Blocks.YELLOW_TERRACOTTA, (ItemLike)Items.YELLOW_DYE);
/*      */     
/*  422 */     concretePowder(debug0, (ItemLike)Blocks.BLACK_CONCRETE_POWDER, (ItemLike)Items.BLACK_DYE);
/*  423 */     concretePowder(debug0, (ItemLike)Blocks.BLUE_CONCRETE_POWDER, (ItemLike)Items.BLUE_DYE);
/*  424 */     concretePowder(debug0, (ItemLike)Blocks.BROWN_CONCRETE_POWDER, (ItemLike)Items.BROWN_DYE);
/*  425 */     concretePowder(debug0, (ItemLike)Blocks.CYAN_CONCRETE_POWDER, (ItemLike)Items.CYAN_DYE);
/*  426 */     concretePowder(debug0, (ItemLike)Blocks.GRAY_CONCRETE_POWDER, (ItemLike)Items.GRAY_DYE);
/*  427 */     concretePowder(debug0, (ItemLike)Blocks.GREEN_CONCRETE_POWDER, (ItemLike)Items.GREEN_DYE);
/*  428 */     concretePowder(debug0, (ItemLike)Blocks.LIGHT_BLUE_CONCRETE_POWDER, (ItemLike)Items.LIGHT_BLUE_DYE);
/*  429 */     concretePowder(debug0, (ItemLike)Blocks.LIGHT_GRAY_CONCRETE_POWDER, (ItemLike)Items.LIGHT_GRAY_DYE);
/*  430 */     concretePowder(debug0, (ItemLike)Blocks.LIME_CONCRETE_POWDER, (ItemLike)Items.LIME_DYE);
/*  431 */     concretePowder(debug0, (ItemLike)Blocks.MAGENTA_CONCRETE_POWDER, (ItemLike)Items.MAGENTA_DYE);
/*  432 */     concretePowder(debug0, (ItemLike)Blocks.ORANGE_CONCRETE_POWDER, (ItemLike)Items.ORANGE_DYE);
/*  433 */     concretePowder(debug0, (ItemLike)Blocks.PINK_CONCRETE_POWDER, (ItemLike)Items.PINK_DYE);
/*  434 */     concretePowder(debug0, (ItemLike)Blocks.PURPLE_CONCRETE_POWDER, (ItemLike)Items.PURPLE_DYE);
/*  435 */     concretePowder(debug0, (ItemLike)Blocks.RED_CONCRETE_POWDER, (ItemLike)Items.RED_DYE);
/*  436 */     concretePowder(debug0, (ItemLike)Blocks.WHITE_CONCRETE_POWDER, (ItemLike)Items.WHITE_DYE);
/*  437 */     concretePowder(debug0, (ItemLike)Blocks.YELLOW_CONCRETE_POWDER, (ItemLike)Items.YELLOW_DYE);
/*      */ 
/*      */     
/*  440 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.ACTIVATOR_RAIL, 6)
/*  441 */       .define(Character.valueOf('#'), (ItemLike)Blocks.REDSTONE_TORCH)
/*  442 */       .define(Character.valueOf('S'), (ItemLike)Items.STICK)
/*  443 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_INGOT)
/*  444 */       .pattern("XSX")
/*  445 */       .pattern("X#X")
/*  446 */       .pattern("XSX")
/*  447 */       .unlockedBy("has_rail", (CriterionTriggerInstance)has((ItemLike)Blocks.RAIL))
/*  448 */       .save(debug0);
/*      */     
/*  450 */     ShapelessRecipeBuilder.shapeless((ItemLike)Blocks.ANDESITE, 2)
/*  451 */       .requires((ItemLike)Blocks.DIORITE)
/*  452 */       .requires((ItemLike)Blocks.COBBLESTONE)
/*  453 */       .unlockedBy("has_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.DIORITE))
/*  454 */       .save(debug0);
/*      */     
/*  456 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.ANVIL)
/*  457 */       .define(Character.valueOf('I'), (ItemLike)Blocks.IRON_BLOCK)
/*  458 */       .define(Character.valueOf('i'), (ItemLike)Items.IRON_INGOT)
/*  459 */       .pattern("III")
/*  460 */       .pattern(" i ")
/*  461 */       .pattern("iii")
/*  462 */       .unlockedBy("has_iron_block", (CriterionTriggerInstance)has((ItemLike)Blocks.IRON_BLOCK))
/*  463 */       .save(debug0);
/*      */     
/*  465 */     ShapedRecipeBuilder.shaped((ItemLike)Items.ARMOR_STAND)
/*  466 */       .define(Character.valueOf('/'), (ItemLike)Items.STICK)
/*  467 */       .define(Character.valueOf('_'), (ItemLike)Blocks.SMOOTH_STONE_SLAB)
/*  468 */       .pattern("///")
/*  469 */       .pattern(" / ")
/*  470 */       .pattern("/_/")
/*  471 */       .unlockedBy("has_stone_slab", (CriterionTriggerInstance)has((ItemLike)Blocks.SMOOTH_STONE_SLAB))
/*  472 */       .save(debug0);
/*      */     
/*  474 */     ShapedRecipeBuilder.shaped((ItemLike)Items.ARROW, 4)
/*  475 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  476 */       .define(Character.valueOf('X'), (ItemLike)Items.FLINT)
/*  477 */       .define(Character.valueOf('Y'), (ItemLike)Items.FEATHER)
/*  478 */       .pattern("X")
/*  479 */       .pattern("#")
/*  480 */       .pattern("Y")
/*  481 */       .unlockedBy("has_feather", (CriterionTriggerInstance)has((ItemLike)Items.FEATHER))
/*  482 */       .unlockedBy("has_flint", (CriterionTriggerInstance)has((ItemLike)Items.FLINT))
/*  483 */       .save(debug0);
/*      */     
/*  485 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.BARREL, 1)
/*  486 */       .define(Character.valueOf('P'), (Tag<Item>)ItemTags.PLANKS)
/*  487 */       .define(Character.valueOf('S'), (Tag<Item>)ItemTags.WOODEN_SLABS)
/*  488 */       .pattern("PSP")
/*  489 */       .pattern("P P")
/*  490 */       .pattern("PSP")
/*  491 */       .unlockedBy("has_planks", (CriterionTriggerInstance)has((Tag<Item>)ItemTags.PLANKS))
/*  492 */       .unlockedBy("has_wood_slab", (CriterionTriggerInstance)has((Tag<Item>)ItemTags.WOODEN_SLABS))
/*  493 */       .save(debug0);
/*      */     
/*  495 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.BEACON)
/*  496 */       .define(Character.valueOf('S'), (ItemLike)Items.NETHER_STAR)
/*  497 */       .define(Character.valueOf('G'), (ItemLike)Blocks.GLASS)
/*  498 */       .define(Character.valueOf('O'), (ItemLike)Blocks.OBSIDIAN)
/*  499 */       .pattern("GGG")
/*  500 */       .pattern("GSG")
/*  501 */       .pattern("OOO")
/*  502 */       .unlockedBy("has_nether_star", (CriterionTriggerInstance)has((ItemLike)Items.NETHER_STAR))
/*  503 */       .save(debug0);
/*      */     
/*  505 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.BEEHIVE)
/*  506 */       .define(Character.valueOf('P'), (Tag<Item>)ItemTags.PLANKS)
/*  507 */       .define(Character.valueOf('H'), (ItemLike)Items.HONEYCOMB)
/*  508 */       .pattern("PPP")
/*  509 */       .pattern("HHH")
/*  510 */       .pattern("PPP")
/*  511 */       .unlockedBy("has_honeycomb", (CriterionTriggerInstance)has((ItemLike)Items.HONEYCOMB))
/*  512 */       .save(debug0);
/*      */     
/*  514 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.BEETROOT_SOUP)
/*  515 */       .requires((ItemLike)Items.BOWL)
/*  516 */       .requires((ItemLike)Items.BEETROOT, 6)
/*  517 */       .unlockedBy("has_beetroot", (CriterionTriggerInstance)has((ItemLike)Items.BEETROOT))
/*  518 */       .save(debug0);
/*      */     
/*  520 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.BLACK_DYE)
/*  521 */       .requires((ItemLike)Items.INK_SAC)
/*  522 */       .group("black_dye")
/*  523 */       .unlockedBy("has_ink_sac", (CriterionTriggerInstance)has((ItemLike)Items.INK_SAC))
/*  524 */       .save(debug0);
/*      */     
/*  526 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.BLACK_DYE)
/*  527 */       .requires((ItemLike)Blocks.WITHER_ROSE)
/*  528 */       .group("black_dye")
/*  529 */       .unlockedBy("has_black_flower", (CriterionTriggerInstance)has((ItemLike)Blocks.WITHER_ROSE))
/*  530 */       .save(debug0, "black_dye_from_wither_rose");
/*      */     
/*  532 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.BLAZE_POWDER, 2)
/*  533 */       .requires((ItemLike)Items.BLAZE_ROD)
/*  534 */       .unlockedBy("has_blaze_rod", (CriterionTriggerInstance)has((ItemLike)Items.BLAZE_ROD))
/*  535 */       .save(debug0);
/*      */     
/*  537 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.BLUE_DYE)
/*  538 */       .requires((ItemLike)Items.LAPIS_LAZULI)
/*  539 */       .group("blue_dye")
/*  540 */       .unlockedBy("has_lapis_lazuli", (CriterionTriggerInstance)has((ItemLike)Items.LAPIS_LAZULI))
/*  541 */       .save(debug0);
/*      */     
/*  543 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.BLUE_DYE)
/*  544 */       .requires((ItemLike)Blocks.CORNFLOWER)
/*  545 */       .group("blue_dye")
/*  546 */       .unlockedBy("has_blue_flower", (CriterionTriggerInstance)has((ItemLike)Blocks.CORNFLOWER))
/*  547 */       .save(debug0, "blue_dye_from_cornflower");
/*      */     
/*  549 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.BLUE_ICE)
/*  550 */       .define(Character.valueOf('#'), (ItemLike)Blocks.PACKED_ICE)
/*  551 */       .pattern("###")
/*  552 */       .pattern("###")
/*  553 */       .pattern("###")
/*  554 */       .unlockedBy("has_packed_ice", (CriterionTriggerInstance)has((ItemLike)Blocks.PACKED_ICE))
/*  555 */       .save(debug0);
/*      */     
/*  557 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.BONE_BLOCK)
/*  558 */       .define(Character.valueOf('X'), (ItemLike)Items.BONE_MEAL)
/*  559 */       .pattern("XXX")
/*  560 */       .pattern("XXX")
/*  561 */       .pattern("XXX")
/*  562 */       .unlockedBy("has_bonemeal", (CriterionTriggerInstance)has((ItemLike)Items.BONE_MEAL))
/*  563 */       .save(debug0);
/*      */     
/*  565 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.BONE_MEAL, 3)
/*  566 */       .requires((ItemLike)Items.BONE)
/*  567 */       .group("bonemeal")
/*  568 */       .unlockedBy("has_bone", (CriterionTriggerInstance)has((ItemLike)Items.BONE))
/*  569 */       .save(debug0);
/*      */     
/*  571 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.BONE_MEAL, 9)
/*  572 */       .requires((ItemLike)Blocks.BONE_BLOCK)
/*  573 */       .group("bonemeal")
/*  574 */       .unlockedBy("has_bone_block", (CriterionTriggerInstance)has((ItemLike)Blocks.BONE_BLOCK))
/*  575 */       .save(debug0, "bone_meal_from_bone_block");
/*      */     
/*  577 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.BOOK)
/*  578 */       .requires((ItemLike)Items.PAPER, 3)
/*  579 */       .requires((ItemLike)Items.LEATHER)
/*  580 */       .unlockedBy("has_paper", (CriterionTriggerInstance)has((ItemLike)Items.PAPER))
/*  581 */       .save(debug0);
/*      */     
/*  583 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.BOOKSHELF)
/*  584 */       .define(Character.valueOf('#'), (Tag<Item>)ItemTags.PLANKS)
/*  585 */       .define(Character.valueOf('X'), (ItemLike)Items.BOOK)
/*  586 */       .pattern("###")
/*  587 */       .pattern("XXX")
/*  588 */       .pattern("###")
/*  589 */       .unlockedBy("has_book", (CriterionTriggerInstance)has((ItemLike)Items.BOOK))
/*  590 */       .save(debug0);
/*      */     
/*  592 */     ShapedRecipeBuilder.shaped((ItemLike)Items.BOW)
/*  593 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  594 */       .define(Character.valueOf('X'), (ItemLike)Items.STRING)
/*  595 */       .pattern(" #X")
/*  596 */       .pattern("# X")
/*  597 */       .pattern(" #X")
/*  598 */       .unlockedBy("has_string", (CriterionTriggerInstance)has((ItemLike)Items.STRING))
/*  599 */       .save(debug0);
/*      */     
/*  601 */     ShapedRecipeBuilder.shaped((ItemLike)Items.BOWL, 4)
/*  602 */       .define(Character.valueOf('#'), (Tag<Item>)ItemTags.PLANKS)
/*  603 */       .pattern("# #")
/*  604 */       .pattern(" # ")
/*  605 */       .unlockedBy("has_brown_mushroom", (CriterionTriggerInstance)has((ItemLike)Blocks.BROWN_MUSHROOM))
/*  606 */       .unlockedBy("has_red_mushroom", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_MUSHROOM))
/*  607 */       .unlockedBy("has_mushroom_stew", (CriterionTriggerInstance)has((ItemLike)Items.MUSHROOM_STEW))
/*  608 */       .save(debug0);
/*      */     
/*  610 */     ShapedRecipeBuilder.shaped((ItemLike)Items.BREAD)
/*  611 */       .define(Character.valueOf('#'), (ItemLike)Items.WHEAT)
/*  612 */       .pattern("###")
/*  613 */       .unlockedBy("has_wheat", (CriterionTriggerInstance)has((ItemLike)Items.WHEAT))
/*  614 */       .save(debug0);
/*      */     
/*  616 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.BREWING_STAND)
/*  617 */       .define(Character.valueOf('B'), (ItemLike)Items.BLAZE_ROD)
/*  618 */       .define(Character.valueOf('#'), (Tag<Item>)ItemTags.STONE_CRAFTING_MATERIALS)
/*  619 */       .pattern(" B ")
/*  620 */       .pattern("###")
/*  621 */       .unlockedBy("has_blaze_rod", (CriterionTriggerInstance)has((ItemLike)Items.BLAZE_ROD))
/*  622 */       .save(debug0);
/*      */     
/*  624 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.BRICKS)
/*  625 */       .define(Character.valueOf('#'), (ItemLike)Items.BRICK)
/*  626 */       .pattern("##")
/*  627 */       .pattern("##")
/*  628 */       .unlockedBy("has_brick", (CriterionTriggerInstance)has((ItemLike)Items.BRICK))
/*  629 */       .save(debug0);
/*      */     
/*  631 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.BRICK_SLAB, 6)
/*  632 */       .define(Character.valueOf('#'), (ItemLike)Blocks.BRICKS)
/*  633 */       .pattern("###")
/*  634 */       .unlockedBy("has_brick_block", (CriterionTriggerInstance)has((ItemLike)Blocks.BRICKS))
/*  635 */       .save(debug0);
/*      */     
/*  637 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.BRICK_STAIRS, 4)
/*  638 */       .define(Character.valueOf('#'), (ItemLike)Blocks.BRICKS)
/*  639 */       .pattern("#  ")
/*  640 */       .pattern("## ")
/*  641 */       .pattern("###")
/*  642 */       .unlockedBy("has_brick_block", (CriterionTriggerInstance)has((ItemLike)Blocks.BRICKS))
/*  643 */       .save(debug0);
/*      */     
/*  645 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.BROWN_DYE)
/*  646 */       .requires((ItemLike)Items.COCOA_BEANS)
/*  647 */       .group("brown_dye")
/*  648 */       .unlockedBy("has_cocoa_beans", (CriterionTriggerInstance)has((ItemLike)Items.COCOA_BEANS))
/*  649 */       .save(debug0);
/*      */     
/*  651 */     ShapedRecipeBuilder.shaped((ItemLike)Items.BUCKET)
/*  652 */       .define(Character.valueOf('#'), (ItemLike)Items.IRON_INGOT)
/*  653 */       .pattern("# #")
/*  654 */       .pattern(" # ")
/*  655 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/*  656 */       .save(debug0);
/*      */     
/*  658 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.CAKE)
/*  659 */       .define(Character.valueOf('A'), (ItemLike)Items.MILK_BUCKET)
/*  660 */       .define(Character.valueOf('B'), (ItemLike)Items.SUGAR)
/*  661 */       .define(Character.valueOf('C'), (ItemLike)Items.WHEAT)
/*  662 */       .define(Character.valueOf('E'), (ItemLike)Items.EGG)
/*  663 */       .pattern("AAA")
/*  664 */       .pattern("BEB")
/*  665 */       .pattern("CCC")
/*  666 */       .unlockedBy("has_egg", (CriterionTriggerInstance)has((ItemLike)Items.EGG))
/*  667 */       .save(debug0);
/*      */     
/*  669 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.CAMPFIRE)
/*  670 */       .define(Character.valueOf('L'), (Tag<Item>)ItemTags.LOGS)
/*  671 */       .define(Character.valueOf('S'), (ItemLike)Items.STICK)
/*  672 */       .define(Character.valueOf('C'), (Tag<Item>)ItemTags.COALS)
/*  673 */       .pattern(" S ")
/*  674 */       .pattern("SCS")
/*  675 */       .pattern("LLL")
/*  676 */       .unlockedBy("has_stick", (CriterionTriggerInstance)has((ItemLike)Items.STICK))
/*  677 */       .unlockedBy("has_coal", (CriterionTriggerInstance)has((Tag<Item>)ItemTags.COALS))
/*  678 */       .save(debug0);
/*      */     
/*  680 */     ShapedRecipeBuilder.shaped((ItemLike)Items.CARROT_ON_A_STICK)
/*  681 */       .define(Character.valueOf('#'), (ItemLike)Items.FISHING_ROD)
/*  682 */       .define(Character.valueOf('X'), (ItemLike)Items.CARROT)
/*  683 */       .pattern("# ")
/*  684 */       .pattern(" X")
/*  685 */       .unlockedBy("has_carrot", (CriterionTriggerInstance)has((ItemLike)Items.CARROT))
/*  686 */       .save(debug0);
/*      */     
/*  688 */     ShapedRecipeBuilder.shaped((ItemLike)Items.WARPED_FUNGUS_ON_A_STICK)
/*  689 */       .define(Character.valueOf('#'), (ItemLike)Items.FISHING_ROD)
/*  690 */       .define(Character.valueOf('X'), (ItemLike)Items.WARPED_FUNGUS)
/*  691 */       .pattern("# ")
/*  692 */       .pattern(" X")
/*  693 */       .unlockedBy("has_warped_fungus", (CriterionTriggerInstance)has((ItemLike)Items.WARPED_FUNGUS))
/*  694 */       .save(debug0);
/*      */     
/*  696 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.CAULDRON)
/*  697 */       .define(Character.valueOf('#'), (ItemLike)Items.IRON_INGOT)
/*  698 */       .pattern("# #")
/*  699 */       .pattern("# #")
/*  700 */       .pattern("###")
/*  701 */       .unlockedBy("has_water_bucket", (CriterionTriggerInstance)has((ItemLike)Items.WATER_BUCKET))
/*  702 */       .save(debug0);
/*      */     
/*  704 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.COMPOSTER)
/*  705 */       .define(Character.valueOf('#'), (Tag<Item>)ItemTags.WOODEN_SLABS)
/*  706 */       .pattern("# #")
/*  707 */       .pattern("# #")
/*  708 */       .pattern("###")
/*  709 */       .unlockedBy("has_wood_slab", (CriterionTriggerInstance)has((Tag<Item>)ItemTags.WOODEN_SLABS))
/*  710 */       .save(debug0);
/*      */     
/*  712 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.CHEST)
/*  713 */       .define(Character.valueOf('#'), (Tag<Item>)ItemTags.PLANKS)
/*  714 */       .pattern("###")
/*  715 */       .pattern("# #")
/*  716 */       .pattern("###")
/*  717 */       .unlockedBy("has_lots_of_items", (CriterionTriggerInstance)new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.atLeast(10), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, new ItemPredicate[0]))
/*  718 */       .save(debug0);
/*      */     
/*  720 */     ShapedRecipeBuilder.shaped((ItemLike)Items.CHEST_MINECART)
/*  721 */       .define(Character.valueOf('A'), (ItemLike)Blocks.CHEST)
/*  722 */       .define(Character.valueOf('B'), (ItemLike)Items.MINECART)
/*  723 */       .pattern("A")
/*  724 */       .pattern("B")
/*  725 */       .unlockedBy("has_minecart", (CriterionTriggerInstance)has((ItemLike)Items.MINECART))
/*  726 */       .save(debug0);
/*      */     
/*  728 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.CHISELED_NETHER_BRICKS)
/*  729 */       .define(Character.valueOf('#'), (ItemLike)Blocks.NETHER_BRICK_SLAB)
/*  730 */       .pattern("#")
/*  731 */       .pattern("#")
/*  732 */       .unlockedBy("has_nether_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.NETHER_BRICKS))
/*  733 */       .save(debug0);
/*      */     
/*  735 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.CHISELED_QUARTZ_BLOCK)
/*  736 */       .define(Character.valueOf('#'), (ItemLike)Blocks.QUARTZ_SLAB)
/*  737 */       .pattern("#")
/*  738 */       .pattern("#")
/*  739 */       .unlockedBy("has_chiseled_quartz_block", (CriterionTriggerInstance)has((ItemLike)Blocks.CHISELED_QUARTZ_BLOCK))
/*  740 */       .unlockedBy("has_quartz_block", (CriterionTriggerInstance)has((ItemLike)Blocks.QUARTZ_BLOCK))
/*  741 */       .unlockedBy("has_quartz_pillar", (CriterionTriggerInstance)has((ItemLike)Blocks.QUARTZ_PILLAR))
/*  742 */       .save(debug0);
/*      */     
/*  744 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.CHISELED_STONE_BRICKS)
/*  745 */       .define(Character.valueOf('#'), (ItemLike)Blocks.STONE_BRICK_SLAB)
/*  746 */       .pattern("#")
/*  747 */       .pattern("#")
/*  748 */       .unlockedBy("has_stone_bricks", (CriterionTriggerInstance)has((Tag<Item>)ItemTags.STONE_BRICKS))
/*  749 */       .save(debug0);
/*      */     
/*  751 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.CLAY)
/*  752 */       .define(Character.valueOf('#'), (ItemLike)Items.CLAY_BALL)
/*  753 */       .pattern("##")
/*  754 */       .pattern("##")
/*  755 */       .unlockedBy("has_clay_ball", (CriterionTriggerInstance)has((ItemLike)Items.CLAY_BALL))
/*  756 */       .save(debug0);
/*      */     
/*  758 */     ShapedRecipeBuilder.shaped((ItemLike)Items.CLOCK)
/*  759 */       .define(Character.valueOf('#'), (ItemLike)Items.GOLD_INGOT)
/*  760 */       .define(Character.valueOf('X'), (ItemLike)Items.REDSTONE)
/*  761 */       .pattern(" # ")
/*  762 */       .pattern("#X#")
/*  763 */       .pattern(" # ")
/*  764 */       .unlockedBy("has_redstone", (CriterionTriggerInstance)has((ItemLike)Items.REDSTONE))
/*  765 */       .save(debug0);
/*      */     
/*  767 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.COAL, 9)
/*  768 */       .requires((ItemLike)Blocks.COAL_BLOCK)
/*  769 */       .unlockedBy("has_coal_block", (CriterionTriggerInstance)has((ItemLike)Blocks.COAL_BLOCK))
/*  770 */       .save(debug0);
/*      */     
/*  772 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.COAL_BLOCK)
/*  773 */       .define(Character.valueOf('#'), (ItemLike)Items.COAL)
/*  774 */       .pattern("###")
/*  775 */       .pattern("###")
/*  776 */       .pattern("###")
/*  777 */       .unlockedBy("has_coal", (CriterionTriggerInstance)has((ItemLike)Items.COAL))
/*  778 */       .save(debug0);
/*      */     
/*  780 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.COARSE_DIRT, 4)
/*  781 */       .define(Character.valueOf('D'), (ItemLike)Blocks.DIRT)
/*  782 */       .define(Character.valueOf('G'), (ItemLike)Blocks.GRAVEL)
/*  783 */       .pattern("DG")
/*  784 */       .pattern("GD")
/*  785 */       .unlockedBy("has_gravel", (CriterionTriggerInstance)has((ItemLike)Blocks.GRAVEL))
/*  786 */       .save(debug0);
/*      */     
/*  788 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.COBBLESTONE_SLAB, 6)
/*  789 */       .define(Character.valueOf('#'), (ItemLike)Blocks.COBBLESTONE)
/*  790 */       .pattern("###")
/*  791 */       .unlockedBy("has_cobblestone", (CriterionTriggerInstance)has((ItemLike)Blocks.COBBLESTONE))
/*  792 */       .save(debug0);
/*      */     
/*  794 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.COBBLESTONE_WALL, 6)
/*  795 */       .define(Character.valueOf('#'), (ItemLike)Blocks.COBBLESTONE)
/*  796 */       .pattern("###")
/*  797 */       .pattern("###")
/*  798 */       .unlockedBy("has_cobblestone", (CriterionTriggerInstance)has((ItemLike)Blocks.COBBLESTONE))
/*  799 */       .save(debug0);
/*      */     
/*  801 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.COMPARATOR)
/*  802 */       .define(Character.valueOf('#'), (ItemLike)Blocks.REDSTONE_TORCH)
/*  803 */       .define(Character.valueOf('X'), (ItemLike)Items.QUARTZ)
/*  804 */       .define(Character.valueOf('I'), (ItemLike)Blocks.STONE)
/*  805 */       .pattern(" # ")
/*  806 */       .pattern("#X#")
/*  807 */       .pattern("III")
/*  808 */       .unlockedBy("has_quartz", (CriterionTriggerInstance)has((ItemLike)Items.QUARTZ))
/*  809 */       .save(debug0);
/*      */     
/*  811 */     ShapedRecipeBuilder.shaped((ItemLike)Items.COMPASS)
/*  812 */       .define(Character.valueOf('#'), (ItemLike)Items.IRON_INGOT)
/*  813 */       .define(Character.valueOf('X'), (ItemLike)Items.REDSTONE)
/*  814 */       .pattern(" # ")
/*  815 */       .pattern("#X#")
/*  816 */       .pattern(" # ")
/*  817 */       .unlockedBy("has_redstone", (CriterionTriggerInstance)has((ItemLike)Items.REDSTONE))
/*  818 */       .save(debug0);
/*      */     
/*  820 */     ShapedRecipeBuilder.shaped((ItemLike)Items.COOKIE, 8)
/*  821 */       .define(Character.valueOf('#'), (ItemLike)Items.WHEAT)
/*  822 */       .define(Character.valueOf('X'), (ItemLike)Items.COCOA_BEANS)
/*  823 */       .pattern("#X#")
/*  824 */       .unlockedBy("has_cocoa", (CriterionTriggerInstance)has((ItemLike)Items.COCOA_BEANS))
/*  825 */       .save(debug0);
/*      */     
/*  827 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.CRAFTING_TABLE)
/*  828 */       .define(Character.valueOf('#'), (Tag<Item>)ItemTags.PLANKS)
/*  829 */       .pattern("##")
/*  830 */       .pattern("##")
/*  831 */       .unlockedBy("has_planks", (CriterionTriggerInstance)has((Tag<Item>)ItemTags.PLANKS))
/*  832 */       .save(debug0);
/*      */     
/*  834 */     ShapedRecipeBuilder.shaped((ItemLike)Items.CROSSBOW)
/*  835 */       .define(Character.valueOf('~'), (ItemLike)Items.STRING)
/*  836 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  837 */       .define(Character.valueOf('&'), (ItemLike)Items.IRON_INGOT)
/*  838 */       .define(Character.valueOf('$'), (ItemLike)Blocks.TRIPWIRE_HOOK)
/*  839 */       .pattern("#&#")
/*  840 */       .pattern("~$~")
/*  841 */       .pattern(" # ")
/*  842 */       .unlockedBy("has_string", (CriterionTriggerInstance)has((ItemLike)Items.STRING))
/*  843 */       .unlockedBy("has_stick", (CriterionTriggerInstance)has((ItemLike)Items.STICK))
/*  844 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/*  845 */       .unlockedBy("has_tripwire_hook", (CriterionTriggerInstance)has((ItemLike)Blocks.TRIPWIRE_HOOK))
/*  846 */       .save(debug0);
/*      */     
/*  848 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.LOOM)
/*  849 */       .define(Character.valueOf('#'), (Tag<Item>)ItemTags.PLANKS)
/*  850 */       .define(Character.valueOf('@'), (ItemLike)Items.STRING)
/*  851 */       .pattern("@@")
/*  852 */       .pattern("##")
/*  853 */       .unlockedBy("has_string", (CriterionTriggerInstance)has((ItemLike)Items.STRING))
/*  854 */       .save(debug0);
/*      */     
/*  856 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.CHISELED_RED_SANDSTONE)
/*  857 */       .define(Character.valueOf('#'), (ItemLike)Blocks.RED_SANDSTONE_SLAB)
/*  858 */       .pattern("#")
/*  859 */       .pattern("#")
/*  860 */       .unlockedBy("has_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_SANDSTONE))
/*  861 */       .unlockedBy("has_chiseled_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.CHISELED_RED_SANDSTONE))
/*  862 */       .unlockedBy("has_cut_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.CUT_RED_SANDSTONE))
/*  863 */       .save(debug0);
/*      */     
/*  865 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.CHISELED_SANDSTONE)
/*  866 */       .define(Character.valueOf('#'), (ItemLike)Blocks.SANDSTONE_SLAB)
/*  867 */       .pattern("#")
/*  868 */       .pattern("#")
/*  869 */       .unlockedBy("has_stone_slab", (CriterionTriggerInstance)has((ItemLike)Blocks.SANDSTONE_SLAB))
/*  870 */       .save(debug0);
/*      */     
/*  872 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.CYAN_DYE, 2)
/*  873 */       .requires((ItemLike)Items.BLUE_DYE)
/*  874 */       .requires((ItemLike)Items.GREEN_DYE)
/*  875 */       .unlockedBy("has_green_dye", (CriterionTriggerInstance)has((ItemLike)Items.GREEN_DYE))
/*  876 */       .unlockedBy("has_blue_dye", (CriterionTriggerInstance)has((ItemLike)Items.BLUE_DYE))
/*  877 */       .save(debug0);
/*      */     
/*  879 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.DARK_PRISMARINE)
/*  880 */       .define(Character.valueOf('S'), (ItemLike)Items.PRISMARINE_SHARD)
/*  881 */       .define(Character.valueOf('I'), (ItemLike)Items.BLACK_DYE)
/*  882 */       .pattern("SSS")
/*  883 */       .pattern("SIS")
/*  884 */       .pattern("SSS")
/*  885 */       .unlockedBy("has_prismarine_shard", (CriterionTriggerInstance)has((ItemLike)Items.PRISMARINE_SHARD))
/*  886 */       .save(debug0);
/*      */     
/*  888 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.PRISMARINE_STAIRS, 4)
/*  889 */       .define(Character.valueOf('#'), (ItemLike)Blocks.PRISMARINE)
/*  890 */       .pattern("#  ")
/*  891 */       .pattern("## ")
/*  892 */       .pattern("###")
/*  893 */       .unlockedBy("has_prismarine", (CriterionTriggerInstance)has((ItemLike)Blocks.PRISMARINE))
/*  894 */       .save(debug0);
/*      */     
/*  896 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.PRISMARINE_BRICK_STAIRS, 4)
/*  897 */       .define(Character.valueOf('#'), (ItemLike)Blocks.PRISMARINE_BRICKS)
/*  898 */       .pattern("#  ")
/*  899 */       .pattern("## ")
/*  900 */       .pattern("###")
/*  901 */       .unlockedBy("has_prismarine_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.PRISMARINE_BRICKS))
/*  902 */       .save(debug0);
/*      */     
/*  904 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.DARK_PRISMARINE_STAIRS, 4)
/*  905 */       .define(Character.valueOf('#'), (ItemLike)Blocks.DARK_PRISMARINE)
/*  906 */       .pattern("#  ")
/*  907 */       .pattern("## ")
/*  908 */       .pattern("###")
/*  909 */       .unlockedBy("has_dark_prismarine", (CriterionTriggerInstance)has((ItemLike)Blocks.DARK_PRISMARINE))
/*  910 */       .save(debug0);
/*      */     
/*  912 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.DAYLIGHT_DETECTOR)
/*  913 */       .define(Character.valueOf('Q'), (ItemLike)Items.QUARTZ)
/*  914 */       .define(Character.valueOf('G'), (ItemLike)Blocks.GLASS)
/*  915 */       .define(Character.valueOf('W'), Ingredient.of((Tag)ItemTags.WOODEN_SLABS))
/*  916 */       .pattern("GGG")
/*  917 */       .pattern("QQQ")
/*  918 */       .pattern("WWW")
/*  919 */       .unlockedBy("has_quartz", (CriterionTriggerInstance)has((ItemLike)Items.QUARTZ))
/*  920 */       .save(debug0);
/*      */     
/*  922 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.DETECTOR_RAIL, 6)
/*  923 */       .define(Character.valueOf('R'), (ItemLike)Items.REDSTONE)
/*  924 */       .define(Character.valueOf('#'), (ItemLike)Blocks.STONE_PRESSURE_PLATE)
/*  925 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_INGOT)
/*  926 */       .pattern("X X")
/*  927 */       .pattern("X#X")
/*  928 */       .pattern("XRX")
/*  929 */       .unlockedBy("has_rail", (CriterionTriggerInstance)has((ItemLike)Blocks.RAIL))
/*  930 */       .save(debug0);
/*      */     
/*  932 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.DIAMOND, 9)
/*  933 */       .requires((ItemLike)Blocks.DIAMOND_BLOCK)
/*  934 */       .unlockedBy("has_diamond_block", (CriterionTriggerInstance)has((ItemLike)Blocks.DIAMOND_BLOCK))
/*  935 */       .save(debug0);
/*      */     
/*  937 */     ShapedRecipeBuilder.shaped((ItemLike)Items.DIAMOND_AXE)
/*  938 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  939 */       .define(Character.valueOf('X'), (ItemLike)Items.DIAMOND)
/*  940 */       .pattern("XX")
/*  941 */       .pattern("X#")
/*  942 */       .pattern(" #")
/*  943 */       .unlockedBy("has_diamond", (CriterionTriggerInstance)has((ItemLike)Items.DIAMOND))
/*  944 */       .save(debug0);
/*      */     
/*  946 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.DIAMOND_BLOCK)
/*  947 */       .define(Character.valueOf('#'), (ItemLike)Items.DIAMOND)
/*  948 */       .pattern("###")
/*  949 */       .pattern("###")
/*  950 */       .pattern("###")
/*  951 */       .unlockedBy("has_diamond", (CriterionTriggerInstance)has((ItemLike)Items.DIAMOND))
/*  952 */       .save(debug0);
/*      */     
/*  954 */     ShapedRecipeBuilder.shaped((ItemLike)Items.DIAMOND_BOOTS)
/*  955 */       .define(Character.valueOf('X'), (ItemLike)Items.DIAMOND)
/*  956 */       .pattern("X X")
/*  957 */       .pattern("X X")
/*  958 */       .unlockedBy("has_diamond", (CriterionTriggerInstance)has((ItemLike)Items.DIAMOND))
/*  959 */       .save(debug0);
/*      */     
/*  961 */     ShapedRecipeBuilder.shaped((ItemLike)Items.DIAMOND_CHESTPLATE)
/*  962 */       .define(Character.valueOf('X'), (ItemLike)Items.DIAMOND)
/*  963 */       .pattern("X X")
/*  964 */       .pattern("XXX")
/*  965 */       .pattern("XXX")
/*  966 */       .unlockedBy("has_diamond", (CriterionTriggerInstance)has((ItemLike)Items.DIAMOND))
/*  967 */       .save(debug0);
/*      */     
/*  969 */     ShapedRecipeBuilder.shaped((ItemLike)Items.DIAMOND_HELMET)
/*  970 */       .define(Character.valueOf('X'), (ItemLike)Items.DIAMOND)
/*  971 */       .pattern("XXX")
/*  972 */       .pattern("X X")
/*  973 */       .unlockedBy("has_diamond", (CriterionTriggerInstance)has((ItemLike)Items.DIAMOND))
/*  974 */       .save(debug0);
/*      */     
/*  976 */     ShapedRecipeBuilder.shaped((ItemLike)Items.DIAMOND_HOE)
/*  977 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  978 */       .define(Character.valueOf('X'), (ItemLike)Items.DIAMOND)
/*  979 */       .pattern("XX")
/*  980 */       .pattern(" #")
/*  981 */       .pattern(" #")
/*  982 */       .unlockedBy("has_diamond", (CriterionTriggerInstance)has((ItemLike)Items.DIAMOND))
/*  983 */       .save(debug0);
/*      */     
/*  985 */     ShapedRecipeBuilder.shaped((ItemLike)Items.DIAMOND_LEGGINGS)
/*  986 */       .define(Character.valueOf('X'), (ItemLike)Items.DIAMOND)
/*  987 */       .pattern("XXX")
/*  988 */       .pattern("X X")
/*  989 */       .pattern("X X")
/*  990 */       .unlockedBy("has_diamond", (CriterionTriggerInstance)has((ItemLike)Items.DIAMOND))
/*  991 */       .save(debug0);
/*      */     
/*  993 */     ShapedRecipeBuilder.shaped((ItemLike)Items.DIAMOND_PICKAXE)
/*  994 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/*  995 */       .define(Character.valueOf('X'), (ItemLike)Items.DIAMOND)
/*  996 */       .pattern("XXX")
/*  997 */       .pattern(" # ")
/*  998 */       .pattern(" # ")
/*  999 */       .unlockedBy("has_diamond", (CriterionTriggerInstance)has((ItemLike)Items.DIAMOND))
/* 1000 */       .save(debug0);
/*      */     
/* 1002 */     ShapedRecipeBuilder.shaped((ItemLike)Items.DIAMOND_SHOVEL)
/* 1003 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1004 */       .define(Character.valueOf('X'), (ItemLike)Items.DIAMOND)
/* 1005 */       .pattern("X")
/* 1006 */       .pattern("#")
/* 1007 */       .pattern("#")
/* 1008 */       .unlockedBy("has_diamond", (CriterionTriggerInstance)has((ItemLike)Items.DIAMOND))
/* 1009 */       .save(debug0);
/*      */     
/* 1011 */     ShapedRecipeBuilder.shaped((ItemLike)Items.DIAMOND_SWORD)
/* 1012 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1013 */       .define(Character.valueOf('X'), (ItemLike)Items.DIAMOND)
/* 1014 */       .pattern("X")
/* 1015 */       .pattern("X")
/* 1016 */       .pattern("#")
/* 1017 */       .unlockedBy("has_diamond", (CriterionTriggerInstance)has((ItemLike)Items.DIAMOND))
/* 1018 */       .save(debug0);
/*      */     
/* 1020 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.DIORITE, 2)
/* 1021 */       .define(Character.valueOf('Q'), (ItemLike)Items.QUARTZ)
/* 1022 */       .define(Character.valueOf('C'), (ItemLike)Blocks.COBBLESTONE)
/* 1023 */       .pattern("CQ")
/* 1024 */       .pattern("QC")
/* 1025 */       .unlockedBy("has_quartz", (CriterionTriggerInstance)has((ItemLike)Items.QUARTZ))
/* 1026 */       .save(debug0);
/*      */     
/* 1028 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.DISPENSER)
/* 1029 */       .define(Character.valueOf('R'), (ItemLike)Items.REDSTONE)
/* 1030 */       .define(Character.valueOf('#'), (ItemLike)Blocks.COBBLESTONE)
/* 1031 */       .define(Character.valueOf('X'), (ItemLike)Items.BOW)
/* 1032 */       .pattern("###")
/* 1033 */       .pattern("#X#")
/* 1034 */       .pattern("#R#")
/* 1035 */       .unlockedBy("has_bow", (CriterionTriggerInstance)has((ItemLike)Items.BOW))
/* 1036 */       .save(debug0);
/*      */     
/* 1038 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.DROPPER)
/* 1039 */       .define(Character.valueOf('R'), (ItemLike)Items.REDSTONE)
/* 1040 */       .define(Character.valueOf('#'), (ItemLike)Blocks.COBBLESTONE)
/* 1041 */       .pattern("###")
/* 1042 */       .pattern("# #")
/* 1043 */       .pattern("#R#")
/* 1044 */       .unlockedBy("has_redstone", (CriterionTriggerInstance)has((ItemLike)Items.REDSTONE))
/* 1045 */       .save(debug0);
/*      */     
/* 1047 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.EMERALD, 9)
/* 1048 */       .requires((ItemLike)Blocks.EMERALD_BLOCK)
/* 1049 */       .unlockedBy("has_emerald_block", (CriterionTriggerInstance)has((ItemLike)Blocks.EMERALD_BLOCK))
/* 1050 */       .save(debug0);
/*      */     
/* 1052 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.EMERALD_BLOCK)
/* 1053 */       .define(Character.valueOf('#'), (ItemLike)Items.EMERALD)
/* 1054 */       .pattern("###")
/* 1055 */       .pattern("###")
/* 1056 */       .pattern("###")
/* 1057 */       .unlockedBy("has_emerald", (CriterionTriggerInstance)has((ItemLike)Items.EMERALD))
/* 1058 */       .save(debug0);
/*      */     
/* 1060 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.ENCHANTING_TABLE)
/* 1061 */       .define(Character.valueOf('B'), (ItemLike)Items.BOOK)
/* 1062 */       .define(Character.valueOf('#'), (ItemLike)Blocks.OBSIDIAN)
/* 1063 */       .define(Character.valueOf('D'), (ItemLike)Items.DIAMOND)
/* 1064 */       .pattern(" B ")
/* 1065 */       .pattern("D#D")
/* 1066 */       .pattern("###")
/* 1067 */       .unlockedBy("has_obsidian", (CriterionTriggerInstance)has((ItemLike)Blocks.OBSIDIAN))
/* 1068 */       .save(debug0);
/*      */     
/* 1070 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.ENDER_CHEST)
/* 1071 */       .define(Character.valueOf('#'), (ItemLike)Blocks.OBSIDIAN)
/* 1072 */       .define(Character.valueOf('E'), (ItemLike)Items.ENDER_EYE)
/* 1073 */       .pattern("###")
/* 1074 */       .pattern("#E#")
/* 1075 */       .pattern("###")
/* 1076 */       .unlockedBy("has_ender_eye", (CriterionTriggerInstance)has((ItemLike)Items.ENDER_EYE))
/* 1077 */       .save(debug0);
/*      */     
/* 1079 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.ENDER_EYE)
/* 1080 */       .requires((ItemLike)Items.ENDER_PEARL)
/* 1081 */       .requires((ItemLike)Items.BLAZE_POWDER)
/* 1082 */       .unlockedBy("has_blaze_powder", (CriterionTriggerInstance)has((ItemLike)Items.BLAZE_POWDER))
/* 1083 */       .save(debug0);
/*      */     
/* 1085 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.END_STONE_BRICKS, 4)
/* 1086 */       .define(Character.valueOf('#'), (ItemLike)Blocks.END_STONE)
/* 1087 */       .pattern("##")
/* 1088 */       .pattern("##")
/* 1089 */       .unlockedBy("has_end_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.END_STONE))
/* 1090 */       .save(debug0);
/*      */     
/* 1092 */     ShapedRecipeBuilder.shaped((ItemLike)Items.END_CRYSTAL)
/* 1093 */       .define(Character.valueOf('T'), (ItemLike)Items.GHAST_TEAR)
/* 1094 */       .define(Character.valueOf('E'), (ItemLike)Items.ENDER_EYE)
/* 1095 */       .define(Character.valueOf('G'), (ItemLike)Blocks.GLASS)
/* 1096 */       .pattern("GGG")
/* 1097 */       .pattern("GEG")
/* 1098 */       .pattern("GTG")
/* 1099 */       .unlockedBy("has_ender_eye", (CriterionTriggerInstance)has((ItemLike)Items.ENDER_EYE))
/* 1100 */       .save(debug0);
/*      */     
/* 1102 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.END_ROD, 4)
/* 1103 */       .define(Character.valueOf('#'), (ItemLike)Items.POPPED_CHORUS_FRUIT)
/* 1104 */       .define(Character.valueOf('/'), (ItemLike)Items.BLAZE_ROD)
/* 1105 */       .pattern("/")
/* 1106 */       .pattern("#")
/* 1107 */       .unlockedBy("has_chorus_fruit_popped", (CriterionTriggerInstance)has((ItemLike)Items.POPPED_CHORUS_FRUIT))
/* 1108 */       .save(debug0);
/*      */     
/* 1110 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.FERMENTED_SPIDER_EYE)
/* 1111 */       .requires((ItemLike)Items.SPIDER_EYE)
/* 1112 */       .requires((ItemLike)Blocks.BROWN_MUSHROOM)
/* 1113 */       .requires((ItemLike)Items.SUGAR)
/* 1114 */       .unlockedBy("has_spider_eye", (CriterionTriggerInstance)has((ItemLike)Items.SPIDER_EYE))
/* 1115 */       .save(debug0);
/*      */     
/* 1117 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.FIRE_CHARGE, 3)
/* 1118 */       .requires((ItemLike)Items.GUNPOWDER)
/* 1119 */       .requires((ItemLike)Items.BLAZE_POWDER)
/* 1120 */       .requires(Ingredient.of(new ItemLike[] { (ItemLike)Items.COAL, (ItemLike)Items.CHARCOAL
/* 1121 */           })).unlockedBy("has_blaze_powder", (CriterionTriggerInstance)has((ItemLike)Items.BLAZE_POWDER))
/* 1122 */       .save(debug0);
/*      */     
/* 1124 */     ShapedRecipeBuilder.shaped((ItemLike)Items.FISHING_ROD)
/* 1125 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1126 */       .define(Character.valueOf('X'), (ItemLike)Items.STRING)
/* 1127 */       .pattern("  #")
/* 1128 */       .pattern(" #X")
/* 1129 */       .pattern("# X")
/* 1130 */       .unlockedBy("has_string", (CriterionTriggerInstance)has((ItemLike)Items.STRING))
/* 1131 */       .save(debug0);
/*      */     
/* 1133 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.FLINT_AND_STEEL)
/* 1134 */       .requires((ItemLike)Items.IRON_INGOT)
/* 1135 */       .requires((ItemLike)Items.FLINT)
/* 1136 */       .unlockedBy("has_flint", (CriterionTriggerInstance)has((ItemLike)Items.FLINT))
/* 1137 */       .unlockedBy("has_obsidian", (CriterionTriggerInstance)has((ItemLike)Blocks.OBSIDIAN))
/* 1138 */       .save(debug0);
/*      */     
/* 1140 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.FLOWER_POT)
/* 1141 */       .define(Character.valueOf('#'), (ItemLike)Items.BRICK)
/* 1142 */       .pattern("# #")
/* 1143 */       .pattern(" # ")
/* 1144 */       .unlockedBy("has_brick", (CriterionTriggerInstance)has((ItemLike)Items.BRICK))
/* 1145 */       .save(debug0);
/*      */     
/* 1147 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.FURNACE)
/* 1148 */       .define(Character.valueOf('#'), (Tag<Item>)ItemTags.STONE_CRAFTING_MATERIALS)
/* 1149 */       .pattern("###")
/* 1150 */       .pattern("# #")
/* 1151 */       .pattern("###")
/* 1152 */       .unlockedBy("has_cobblestone", (CriterionTriggerInstance)has((Tag<Item>)ItemTags.STONE_CRAFTING_MATERIALS))
/* 1153 */       .save(debug0);
/*      */     
/* 1155 */     ShapedRecipeBuilder.shaped((ItemLike)Items.FURNACE_MINECART)
/* 1156 */       .define(Character.valueOf('A'), (ItemLike)Blocks.FURNACE)
/* 1157 */       .define(Character.valueOf('B'), (ItemLike)Items.MINECART)
/* 1158 */       .pattern("A")
/* 1159 */       .pattern("B")
/* 1160 */       .unlockedBy("has_minecart", (CriterionTriggerInstance)has((ItemLike)Items.MINECART))
/* 1161 */       .save(debug0);
/*      */     
/* 1163 */     ShapedRecipeBuilder.shaped((ItemLike)Items.GLASS_BOTTLE, 3)
/* 1164 */       .define(Character.valueOf('#'), (ItemLike)Blocks.GLASS)
/* 1165 */       .pattern("# #")
/* 1166 */       .pattern(" # ")
/* 1167 */       .unlockedBy("has_glass", (CriterionTriggerInstance)has((ItemLike)Blocks.GLASS))
/* 1168 */       .save(debug0);
/*      */     
/* 1170 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.GLASS_PANE, 16)
/* 1171 */       .define(Character.valueOf('#'), (ItemLike)Blocks.GLASS)
/* 1172 */       .pattern("###")
/* 1173 */       .pattern("###")
/* 1174 */       .unlockedBy("has_glass", (CriterionTriggerInstance)has((ItemLike)Blocks.GLASS))
/* 1175 */       .save(debug0);
/*      */     
/* 1177 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.GLOWSTONE)
/* 1178 */       .define(Character.valueOf('#'), (ItemLike)Items.GLOWSTONE_DUST)
/* 1179 */       .pattern("##")
/* 1180 */       .pattern("##")
/* 1181 */       .unlockedBy("has_glowstone_dust", (CriterionTriggerInstance)has((ItemLike)Items.GLOWSTONE_DUST))
/* 1182 */       .save(debug0);
/*      */     
/* 1184 */     ShapedRecipeBuilder.shaped((ItemLike)Items.GOLDEN_APPLE)
/* 1185 */       .define(Character.valueOf('#'), (ItemLike)Items.GOLD_INGOT)
/* 1186 */       .define(Character.valueOf('X'), (ItemLike)Items.APPLE)
/* 1187 */       .pattern("###")
/* 1188 */       .pattern("#X#")
/* 1189 */       .pattern("###")
/* 1190 */       .unlockedBy("has_gold_ingot", (CriterionTriggerInstance)has((ItemLike)Items.GOLD_INGOT))
/* 1191 */       .save(debug0);
/*      */     
/* 1193 */     ShapedRecipeBuilder.shaped((ItemLike)Items.GOLDEN_AXE)
/* 1194 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1195 */       .define(Character.valueOf('X'), (ItemLike)Items.GOLD_INGOT)
/* 1196 */       .pattern("XX")
/* 1197 */       .pattern("X#")
/* 1198 */       .pattern(" #")
/* 1199 */       .unlockedBy("has_gold_ingot", (CriterionTriggerInstance)has((ItemLike)Items.GOLD_INGOT))
/* 1200 */       .save(debug0);
/*      */     
/* 1202 */     ShapedRecipeBuilder.shaped((ItemLike)Items.GOLDEN_BOOTS)
/* 1203 */       .define(Character.valueOf('X'), (ItemLike)Items.GOLD_INGOT)
/* 1204 */       .pattern("X X")
/* 1205 */       .pattern("X X")
/* 1206 */       .unlockedBy("has_gold_ingot", (CriterionTriggerInstance)has((ItemLike)Items.GOLD_INGOT))
/* 1207 */       .save(debug0);
/*      */     
/* 1209 */     ShapedRecipeBuilder.shaped((ItemLike)Items.GOLDEN_CARROT)
/* 1210 */       .define(Character.valueOf('#'), (ItemLike)Items.GOLD_NUGGET)
/* 1211 */       .define(Character.valueOf('X'), (ItemLike)Items.CARROT)
/* 1212 */       .pattern("###")
/* 1213 */       .pattern("#X#")
/* 1214 */       .pattern("###")
/* 1215 */       .unlockedBy("has_gold_nugget", (CriterionTriggerInstance)has((ItemLike)Items.GOLD_NUGGET))
/* 1216 */       .save(debug0);
/*      */     
/* 1218 */     ShapedRecipeBuilder.shaped((ItemLike)Items.GOLDEN_CHESTPLATE)
/* 1219 */       .define(Character.valueOf('X'), (ItemLike)Items.GOLD_INGOT)
/* 1220 */       .pattern("X X")
/* 1221 */       .pattern("XXX")
/* 1222 */       .pattern("XXX")
/* 1223 */       .unlockedBy("has_gold_ingot", (CriterionTriggerInstance)has((ItemLike)Items.GOLD_INGOT))
/* 1224 */       .save(debug0);
/*      */     
/* 1226 */     ShapedRecipeBuilder.shaped((ItemLike)Items.GOLDEN_HELMET)
/* 1227 */       .define(Character.valueOf('X'), (ItemLike)Items.GOLD_INGOT)
/* 1228 */       .pattern("XXX")
/* 1229 */       .pattern("X X")
/* 1230 */       .unlockedBy("has_gold_ingot", (CriterionTriggerInstance)has((ItemLike)Items.GOLD_INGOT))
/* 1231 */       .save(debug0);
/*      */     
/* 1233 */     ShapedRecipeBuilder.shaped((ItemLike)Items.GOLDEN_HOE)
/* 1234 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1235 */       .define(Character.valueOf('X'), (ItemLike)Items.GOLD_INGOT)
/* 1236 */       .pattern("XX")
/* 1237 */       .pattern(" #")
/* 1238 */       .pattern(" #")
/* 1239 */       .unlockedBy("has_gold_ingot", (CriterionTriggerInstance)has((ItemLike)Items.GOLD_INGOT))
/* 1240 */       .save(debug0);
/*      */     
/* 1242 */     ShapedRecipeBuilder.shaped((ItemLike)Items.GOLDEN_LEGGINGS)
/* 1243 */       .define(Character.valueOf('X'), (ItemLike)Items.GOLD_INGOT)
/* 1244 */       .pattern("XXX")
/* 1245 */       .pattern("X X")
/* 1246 */       .pattern("X X")
/* 1247 */       .unlockedBy("has_gold_ingot", (CriterionTriggerInstance)has((ItemLike)Items.GOLD_INGOT))
/* 1248 */       .save(debug0);
/*      */     
/* 1250 */     ShapedRecipeBuilder.shaped((ItemLike)Items.GOLDEN_PICKAXE)
/* 1251 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1252 */       .define(Character.valueOf('X'), (ItemLike)Items.GOLD_INGOT)
/* 1253 */       .pattern("XXX")
/* 1254 */       .pattern(" # ")
/* 1255 */       .pattern(" # ")
/* 1256 */       .unlockedBy("has_gold_ingot", (CriterionTriggerInstance)has((ItemLike)Items.GOLD_INGOT))
/* 1257 */       .save(debug0);
/*      */     
/* 1259 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.POWERED_RAIL, 6)
/* 1260 */       .define(Character.valueOf('R'), (ItemLike)Items.REDSTONE)
/* 1261 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1262 */       .define(Character.valueOf('X'), (ItemLike)Items.GOLD_INGOT)
/* 1263 */       .pattern("X X")
/* 1264 */       .pattern("X#X")
/* 1265 */       .pattern("XRX")
/* 1266 */       .unlockedBy("has_rail", (CriterionTriggerInstance)has((ItemLike)Blocks.RAIL))
/* 1267 */       .save(debug0);
/*      */     
/* 1269 */     ShapedRecipeBuilder.shaped((ItemLike)Items.GOLDEN_SHOVEL)
/* 1270 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1271 */       .define(Character.valueOf('X'), (ItemLike)Items.GOLD_INGOT)
/* 1272 */       .pattern("X")
/* 1273 */       .pattern("#")
/* 1274 */       .pattern("#")
/* 1275 */       .unlockedBy("has_gold_ingot", (CriterionTriggerInstance)has((ItemLike)Items.GOLD_INGOT))
/* 1276 */       .save(debug0);
/*      */     
/* 1278 */     ShapedRecipeBuilder.shaped((ItemLike)Items.GOLDEN_SWORD)
/* 1279 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1280 */       .define(Character.valueOf('X'), (ItemLike)Items.GOLD_INGOT)
/* 1281 */       .pattern("X")
/* 1282 */       .pattern("X")
/* 1283 */       .pattern("#")
/* 1284 */       .unlockedBy("has_gold_ingot", (CriterionTriggerInstance)has((ItemLike)Items.GOLD_INGOT))
/* 1285 */       .save(debug0);
/*      */     
/* 1287 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.GOLD_BLOCK)
/* 1288 */       .define(Character.valueOf('#'), (ItemLike)Items.GOLD_INGOT)
/* 1289 */       .pattern("###")
/* 1290 */       .pattern("###")
/* 1291 */       .pattern("###")
/* 1292 */       .unlockedBy("has_gold_ingot", (CriterionTriggerInstance)has((ItemLike)Items.GOLD_INGOT))
/* 1293 */       .save(debug0);
/*      */     
/* 1295 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.GOLD_INGOT, 9)
/* 1296 */       .requires((ItemLike)Blocks.GOLD_BLOCK)
/* 1297 */       .group("gold_ingot")
/* 1298 */       .unlockedBy("has_gold_block", (CriterionTriggerInstance)has((ItemLike)Blocks.GOLD_BLOCK))
/* 1299 */       .save(debug0, "gold_ingot_from_gold_block");
/*      */     
/* 1301 */     ShapedRecipeBuilder.shaped((ItemLike)Items.GOLD_INGOT)
/* 1302 */       .define(Character.valueOf('#'), (ItemLike)Items.GOLD_NUGGET)
/* 1303 */       .pattern("###")
/* 1304 */       .pattern("###")
/* 1305 */       .pattern("###")
/* 1306 */       .group("gold_ingot")
/* 1307 */       .unlockedBy("has_gold_nugget", (CriterionTriggerInstance)has((ItemLike)Items.GOLD_NUGGET))
/* 1308 */       .save(debug0, "gold_ingot_from_nuggets");
/*      */     
/* 1310 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.GOLD_NUGGET, 9)
/* 1311 */       .requires((ItemLike)Items.GOLD_INGOT)
/* 1312 */       .unlockedBy("has_gold_ingot", (CriterionTriggerInstance)has((ItemLike)Items.GOLD_INGOT))
/* 1313 */       .save(debug0);
/*      */     
/* 1315 */     ShapelessRecipeBuilder.shapeless((ItemLike)Blocks.GRANITE)
/* 1316 */       .requires((ItemLike)Blocks.DIORITE)
/* 1317 */       .requires((ItemLike)Items.QUARTZ)
/* 1318 */       .unlockedBy("has_quartz", (CriterionTriggerInstance)has((ItemLike)Items.QUARTZ))
/* 1319 */       .save(debug0);
/*      */     
/* 1321 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.GRAY_DYE, 2)
/* 1322 */       .requires((ItemLike)Items.BLACK_DYE)
/* 1323 */       .requires((ItemLike)Items.WHITE_DYE)
/* 1324 */       .unlockedBy("has_white_dye", (CriterionTriggerInstance)has((ItemLike)Items.WHITE_DYE))
/* 1325 */       .unlockedBy("has_black_dye", (CriterionTriggerInstance)has((ItemLike)Items.BLACK_DYE))
/* 1326 */       .save(debug0);
/*      */     
/* 1328 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.HAY_BLOCK)
/* 1329 */       .define(Character.valueOf('#'), (ItemLike)Items.WHEAT)
/* 1330 */       .pattern("###")
/* 1331 */       .pattern("###")
/* 1332 */       .pattern("###")
/* 1333 */       .unlockedBy("has_wheat", (CriterionTriggerInstance)has((ItemLike)Items.WHEAT))
/* 1334 */       .save(debug0);
/*      */     
/* 1336 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE)
/* 1337 */       .define(Character.valueOf('#'), (ItemLike)Items.IRON_INGOT)
/* 1338 */       .pattern("##")
/* 1339 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/* 1340 */       .save(debug0);
/*      */     
/* 1342 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.HONEY_BOTTLE, 4)
/* 1343 */       .requires((ItemLike)Items.HONEY_BLOCK)
/* 1344 */       .requires((ItemLike)Items.GLASS_BOTTLE, 4)
/* 1345 */       .unlockedBy("has_honey_block", (CriterionTriggerInstance)has((ItemLike)Blocks.HONEY_BLOCK))
/* 1346 */       .save(debug0);
/*      */     
/* 1348 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.HONEY_BLOCK, 1)
/* 1349 */       .define(Character.valueOf('S'), (ItemLike)Items.HONEY_BOTTLE)
/* 1350 */       .pattern("SS")
/* 1351 */       .pattern("SS")
/* 1352 */       .unlockedBy("has_honey_bottle", (CriterionTriggerInstance)has((ItemLike)Items.HONEY_BOTTLE))
/* 1353 */       .save(debug0);
/*      */     
/* 1355 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.HONEYCOMB_BLOCK)
/* 1356 */       .define(Character.valueOf('H'), (ItemLike)Items.HONEYCOMB)
/* 1357 */       .pattern("HH")
/* 1358 */       .pattern("HH")
/* 1359 */       .unlockedBy("has_honeycomb", (CriterionTriggerInstance)has((ItemLike)Items.HONEYCOMB))
/* 1360 */       .save(debug0);
/*      */     
/* 1362 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.HOPPER)
/* 1363 */       .define(Character.valueOf('C'), (ItemLike)Blocks.CHEST)
/* 1364 */       .define(Character.valueOf('I'), (ItemLike)Items.IRON_INGOT)
/* 1365 */       .pattern("I I")
/* 1366 */       .pattern("ICI")
/* 1367 */       .pattern(" I ")
/* 1368 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/* 1369 */       .save(debug0);
/*      */     
/* 1371 */     ShapedRecipeBuilder.shaped((ItemLike)Items.HOPPER_MINECART)
/* 1372 */       .define(Character.valueOf('A'), (ItemLike)Blocks.HOPPER)
/* 1373 */       .define(Character.valueOf('B'), (ItemLike)Items.MINECART)
/* 1374 */       .pattern("A")
/* 1375 */       .pattern("B")
/* 1376 */       .unlockedBy("has_minecart", (CriterionTriggerInstance)has((ItemLike)Items.MINECART))
/* 1377 */       .save(debug0);
/*      */     
/* 1379 */     ShapedRecipeBuilder.shaped((ItemLike)Items.IRON_AXE)
/* 1380 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1381 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_INGOT)
/* 1382 */       .pattern("XX")
/* 1383 */       .pattern("X#")
/* 1384 */       .pattern(" #")
/* 1385 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/* 1386 */       .save(debug0);
/*      */     
/* 1388 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.IRON_BARS, 16)
/* 1389 */       .define(Character.valueOf('#'), (ItemLike)Items.IRON_INGOT)
/* 1390 */       .pattern("###")
/* 1391 */       .pattern("###")
/* 1392 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/* 1393 */       .save(debug0);
/*      */     
/* 1395 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.IRON_BLOCK)
/* 1396 */       .define(Character.valueOf('#'), (ItemLike)Items.IRON_INGOT)
/* 1397 */       .pattern("###")
/* 1398 */       .pattern("###")
/* 1399 */       .pattern("###")
/* 1400 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/* 1401 */       .save(debug0);
/*      */     
/* 1403 */     ShapedRecipeBuilder.shaped((ItemLike)Items.IRON_BOOTS)
/* 1404 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_INGOT)
/* 1405 */       .pattern("X X")
/* 1406 */       .pattern("X X")
/* 1407 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/* 1408 */       .save(debug0);
/*      */     
/* 1410 */     ShapedRecipeBuilder.shaped((ItemLike)Items.IRON_CHESTPLATE)
/* 1411 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_INGOT)
/* 1412 */       .pattern("X X")
/* 1413 */       .pattern("XXX")
/* 1414 */       .pattern("XXX")
/* 1415 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/* 1416 */       .save(debug0);
/*      */     
/* 1418 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.IRON_DOOR, 3)
/* 1419 */       .define(Character.valueOf('#'), (ItemLike)Items.IRON_INGOT)
/* 1420 */       .pattern("##")
/* 1421 */       .pattern("##")
/* 1422 */       .pattern("##")
/* 1423 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/* 1424 */       .save(debug0);
/*      */     
/* 1426 */     ShapedRecipeBuilder.shaped((ItemLike)Items.IRON_HELMET)
/* 1427 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_INGOT)
/* 1428 */       .pattern("XXX")
/* 1429 */       .pattern("X X")
/* 1430 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/* 1431 */       .save(debug0);
/*      */     
/* 1433 */     ShapedRecipeBuilder.shaped((ItemLike)Items.IRON_HOE)
/* 1434 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1435 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_INGOT)
/* 1436 */       .pattern("XX")
/* 1437 */       .pattern(" #")
/* 1438 */       .pattern(" #")
/* 1439 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/* 1440 */       .save(debug0);
/*      */     
/* 1442 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.IRON_INGOT, 9)
/* 1443 */       .requires((ItemLike)Blocks.IRON_BLOCK)
/* 1444 */       .group("iron_ingot")
/* 1445 */       .unlockedBy("has_iron_block", (CriterionTriggerInstance)has((ItemLike)Blocks.IRON_BLOCK))
/* 1446 */       .save(debug0, "iron_ingot_from_iron_block");
/*      */     
/* 1448 */     ShapedRecipeBuilder.shaped((ItemLike)Items.IRON_INGOT)
/* 1449 */       .define(Character.valueOf('#'), (ItemLike)Items.IRON_NUGGET)
/* 1450 */       .pattern("###")
/* 1451 */       .pattern("###")
/* 1452 */       .pattern("###")
/* 1453 */       .group("iron_ingot")
/* 1454 */       .unlockedBy("has_iron_nugget", (CriterionTriggerInstance)has((ItemLike)Items.IRON_NUGGET))
/* 1455 */       .save(debug0, "iron_ingot_from_nuggets");
/*      */     
/* 1457 */     ShapedRecipeBuilder.shaped((ItemLike)Items.IRON_LEGGINGS)
/* 1458 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_INGOT)
/* 1459 */       .pattern("XXX")
/* 1460 */       .pattern("X X")
/* 1461 */       .pattern("X X")
/* 1462 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/* 1463 */       .save(debug0);
/*      */     
/* 1465 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.IRON_NUGGET, 9)
/* 1466 */       .requires((ItemLike)Items.IRON_INGOT)
/* 1467 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/* 1468 */       .save(debug0);
/*      */     
/* 1470 */     ShapedRecipeBuilder.shaped((ItemLike)Items.IRON_PICKAXE)
/* 1471 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1472 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_INGOT)
/* 1473 */       .pattern("XXX")
/* 1474 */       .pattern(" # ")
/* 1475 */       .pattern(" # ")
/* 1476 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/* 1477 */       .save(debug0);
/*      */     
/* 1479 */     ShapedRecipeBuilder.shaped((ItemLike)Items.IRON_SHOVEL)
/* 1480 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1481 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_INGOT)
/* 1482 */       .pattern("X")
/* 1483 */       .pattern("#")
/* 1484 */       .pattern("#")
/* 1485 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/* 1486 */       .save(debug0);
/*      */     
/* 1488 */     ShapedRecipeBuilder.shaped((ItemLike)Items.IRON_SWORD)
/* 1489 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1490 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_INGOT)
/* 1491 */       .pattern("X")
/* 1492 */       .pattern("X")
/* 1493 */       .pattern("#")
/* 1494 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/* 1495 */       .save(debug0);
/*      */     
/* 1497 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.IRON_TRAPDOOR)
/* 1498 */       .define(Character.valueOf('#'), (ItemLike)Items.IRON_INGOT)
/* 1499 */       .pattern("##")
/* 1500 */       .pattern("##")
/* 1501 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/* 1502 */       .save(debug0);
/*      */     
/* 1504 */     ShapedRecipeBuilder.shaped((ItemLike)Items.ITEM_FRAME)
/* 1505 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1506 */       .define(Character.valueOf('X'), (ItemLike)Items.LEATHER)
/* 1507 */       .pattern("###")
/* 1508 */       .pattern("#X#")
/* 1509 */       .pattern("###")
/* 1510 */       .unlockedBy("has_leather", (CriterionTriggerInstance)has((ItemLike)Items.LEATHER))
/* 1511 */       .save(debug0);
/*      */     
/* 1513 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.JUKEBOX)
/* 1514 */       .define(Character.valueOf('#'), (Tag<Item>)ItemTags.PLANKS)
/* 1515 */       .define(Character.valueOf('X'), (ItemLike)Items.DIAMOND)
/* 1516 */       .pattern("###")
/* 1517 */       .pattern("#X#")
/* 1518 */       .pattern("###")
/* 1519 */       .unlockedBy("has_diamond", (CriterionTriggerInstance)has((ItemLike)Items.DIAMOND))
/* 1520 */       .save(debug0);
/*      */     
/* 1522 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.LADDER, 3)
/* 1523 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1524 */       .pattern("# #")
/* 1525 */       .pattern("###")
/* 1526 */       .pattern("# #")
/* 1527 */       .unlockedBy("has_stick", (CriterionTriggerInstance)has((ItemLike)Items.STICK))
/* 1528 */       .save(debug0);
/*      */     
/* 1530 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.LAPIS_BLOCK)
/* 1531 */       .define(Character.valueOf('#'), (ItemLike)Items.LAPIS_LAZULI)
/* 1532 */       .pattern("###")
/* 1533 */       .pattern("###")
/* 1534 */       .pattern("###")
/* 1535 */       .unlockedBy("has_lapis", (CriterionTriggerInstance)has((ItemLike)Items.LAPIS_LAZULI))
/* 1536 */       .save(debug0);
/*      */     
/* 1538 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.LAPIS_LAZULI, 9)
/* 1539 */       .requires((ItemLike)Blocks.LAPIS_BLOCK)
/* 1540 */       .unlockedBy("has_lapis_block", (CriterionTriggerInstance)has((ItemLike)Blocks.LAPIS_BLOCK))
/* 1541 */       .save(debug0);
/*      */     
/* 1543 */     ShapedRecipeBuilder.shaped((ItemLike)Items.LEAD, 2)
/* 1544 */       .define(Character.valueOf('~'), (ItemLike)Items.STRING)
/* 1545 */       .define(Character.valueOf('O'), (ItemLike)Items.SLIME_BALL)
/* 1546 */       .pattern("~~ ")
/* 1547 */       .pattern("~O ")
/* 1548 */       .pattern("  ~")
/* 1549 */       .unlockedBy("has_slime_ball", (CriterionTriggerInstance)has((ItemLike)Items.SLIME_BALL))
/* 1550 */       .save(debug0);
/*      */     
/* 1552 */     ShapedRecipeBuilder.shaped((ItemLike)Items.LEATHER)
/* 1553 */       .define(Character.valueOf('#'), (ItemLike)Items.RABBIT_HIDE)
/* 1554 */       .pattern("##")
/* 1555 */       .pattern("##")
/* 1556 */       .unlockedBy("has_rabbit_hide", (CriterionTriggerInstance)has((ItemLike)Items.RABBIT_HIDE))
/* 1557 */       .save(debug0);
/*      */     
/* 1559 */     ShapedRecipeBuilder.shaped((ItemLike)Items.LEATHER_BOOTS)
/* 1560 */       .define(Character.valueOf('X'), (ItemLike)Items.LEATHER)
/* 1561 */       .pattern("X X")
/* 1562 */       .pattern("X X")
/* 1563 */       .unlockedBy("has_leather", (CriterionTriggerInstance)has((ItemLike)Items.LEATHER))
/* 1564 */       .save(debug0);
/*      */     
/* 1566 */     ShapedRecipeBuilder.shaped((ItemLike)Items.LEATHER_CHESTPLATE)
/* 1567 */       .define(Character.valueOf('X'), (ItemLike)Items.LEATHER)
/* 1568 */       .pattern("X X")
/* 1569 */       .pattern("XXX")
/* 1570 */       .pattern("XXX")
/* 1571 */       .unlockedBy("has_leather", (CriterionTriggerInstance)has((ItemLike)Items.LEATHER))
/* 1572 */       .save(debug0);
/*      */     
/* 1574 */     ShapedRecipeBuilder.shaped((ItemLike)Items.LEATHER_HELMET)
/* 1575 */       .define(Character.valueOf('X'), (ItemLike)Items.LEATHER)
/* 1576 */       .pattern("XXX")
/* 1577 */       .pattern("X X")
/* 1578 */       .unlockedBy("has_leather", (CriterionTriggerInstance)has((ItemLike)Items.LEATHER))
/* 1579 */       .save(debug0);
/*      */     
/* 1581 */     ShapedRecipeBuilder.shaped((ItemLike)Items.LEATHER_LEGGINGS)
/* 1582 */       .define(Character.valueOf('X'), (ItemLike)Items.LEATHER)
/* 1583 */       .pattern("XXX")
/* 1584 */       .pattern("X X")
/* 1585 */       .pattern("X X")
/* 1586 */       .unlockedBy("has_leather", (CriterionTriggerInstance)has((ItemLike)Items.LEATHER))
/* 1587 */       .save(debug0);
/*      */     
/* 1589 */     ShapedRecipeBuilder.shaped((ItemLike)Items.LEATHER_HORSE_ARMOR)
/* 1590 */       .define(Character.valueOf('X'), (ItemLike)Items.LEATHER)
/* 1591 */       .pattern("X X")
/* 1592 */       .pattern("XXX")
/* 1593 */       .pattern("X X")
/* 1594 */       .unlockedBy("has_leather", (CriterionTriggerInstance)has((ItemLike)Items.LEATHER))
/* 1595 */       .save(debug0);
/*      */     
/* 1597 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.LECTERN)
/* 1598 */       .define(Character.valueOf('S'), (Tag<Item>)ItemTags.WOODEN_SLABS)
/* 1599 */       .define(Character.valueOf('B'), (ItemLike)Blocks.BOOKSHELF)
/* 1600 */       .pattern("SSS")
/* 1601 */       .pattern(" B ")
/* 1602 */       .pattern(" S ")
/* 1603 */       .unlockedBy("has_book", (CriterionTriggerInstance)has((ItemLike)Items.BOOK))
/* 1604 */       .save(debug0);
/*      */     
/* 1606 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.LEVER)
/* 1607 */       .define(Character.valueOf('#'), (ItemLike)Blocks.COBBLESTONE)
/* 1608 */       .define(Character.valueOf('X'), (ItemLike)Items.STICK)
/* 1609 */       .pattern("X")
/* 1610 */       .pattern("#")
/* 1611 */       .unlockedBy("has_cobblestone", (CriterionTriggerInstance)has((ItemLike)Blocks.COBBLESTONE))
/* 1612 */       .save(debug0);
/*      */     
/* 1614 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.LIGHT_BLUE_DYE)
/* 1615 */       .requires((ItemLike)Blocks.BLUE_ORCHID)
/* 1616 */       .group("light_blue_dye")
/* 1617 */       .unlockedBy("has_red_flower", (CriterionTriggerInstance)has((ItemLike)Blocks.BLUE_ORCHID))
/* 1618 */       .save(debug0, "light_blue_dye_from_blue_orchid");
/*      */     
/* 1620 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.LIGHT_BLUE_DYE, 2)
/* 1621 */       .requires((ItemLike)Items.BLUE_DYE)
/* 1622 */       .requires((ItemLike)Items.WHITE_DYE)
/* 1623 */       .group("light_blue_dye")
/* 1624 */       .unlockedBy("has_blue_dye", (CriterionTriggerInstance)has((ItemLike)Items.BLUE_DYE))
/* 1625 */       .unlockedBy("has_white_dye", (CriterionTriggerInstance)has((ItemLike)Items.WHITE_DYE))
/* 1626 */       .save(debug0, "light_blue_dye_from_blue_white_dye");
/*      */     
/* 1628 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.LIGHT_GRAY_DYE)
/* 1629 */       .requires((ItemLike)Blocks.AZURE_BLUET)
/* 1630 */       .group("light_gray_dye")
/* 1631 */       .unlockedBy("has_red_flower", (CriterionTriggerInstance)has((ItemLike)Blocks.AZURE_BLUET))
/* 1632 */       .save(debug0, "light_gray_dye_from_azure_bluet");
/*      */     
/* 1634 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.LIGHT_GRAY_DYE, 2)
/* 1635 */       .requires((ItemLike)Items.GRAY_DYE)
/* 1636 */       .requires((ItemLike)Items.WHITE_DYE)
/* 1637 */       .group("light_gray_dye")
/* 1638 */       .unlockedBy("has_gray_dye", (CriterionTriggerInstance)has((ItemLike)Items.GRAY_DYE))
/* 1639 */       .unlockedBy("has_white_dye", (CriterionTriggerInstance)has((ItemLike)Items.WHITE_DYE))
/* 1640 */       .save(debug0, "light_gray_dye_from_gray_white_dye");
/*      */     
/* 1642 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.LIGHT_GRAY_DYE, 3)
/* 1643 */       .requires((ItemLike)Items.BLACK_DYE)
/* 1644 */       .requires((ItemLike)Items.WHITE_DYE, 2)
/* 1645 */       .group("light_gray_dye")
/* 1646 */       .unlockedBy("has_white_dye", (CriterionTriggerInstance)has((ItemLike)Items.WHITE_DYE))
/* 1647 */       .unlockedBy("has_black_dye", (CriterionTriggerInstance)has((ItemLike)Items.BLACK_DYE))
/* 1648 */       .save(debug0, "light_gray_dye_from_black_white_dye");
/*      */     
/* 1650 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.LIGHT_GRAY_DYE)
/* 1651 */       .requires((ItemLike)Blocks.OXEYE_DAISY)
/* 1652 */       .group("light_gray_dye")
/* 1653 */       .unlockedBy("has_red_flower", (CriterionTriggerInstance)has((ItemLike)Blocks.OXEYE_DAISY))
/* 1654 */       .save(debug0, "light_gray_dye_from_oxeye_daisy");
/*      */     
/* 1656 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.LIGHT_GRAY_DYE)
/* 1657 */       .requires((ItemLike)Blocks.WHITE_TULIP)
/* 1658 */       .group("light_gray_dye")
/* 1659 */       .unlockedBy("has_red_flower", (CriterionTriggerInstance)has((ItemLike)Blocks.WHITE_TULIP))
/* 1660 */       .save(debug0, "light_gray_dye_from_white_tulip");
/*      */     
/* 1662 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE)
/* 1663 */       .define(Character.valueOf('#'), (ItemLike)Items.GOLD_INGOT)
/* 1664 */       .pattern("##")
/* 1665 */       .unlockedBy("has_gold_ingot", (CriterionTriggerInstance)has((ItemLike)Items.GOLD_INGOT))
/* 1666 */       .save(debug0);
/*      */     
/* 1668 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.LIME_DYE, 2)
/* 1669 */       .requires((ItemLike)Items.GREEN_DYE)
/* 1670 */       .requires((ItemLike)Items.WHITE_DYE)
/* 1671 */       .unlockedBy("has_green_dye", (CriterionTriggerInstance)has((ItemLike)Items.GREEN_DYE))
/* 1672 */       .unlockedBy("has_white_dye", (CriterionTriggerInstance)has((ItemLike)Items.WHITE_DYE))
/* 1673 */       .save(debug0);
/*      */     
/* 1675 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.JACK_O_LANTERN)
/* 1676 */       .define(Character.valueOf('A'), (ItemLike)Blocks.CARVED_PUMPKIN)
/* 1677 */       .define(Character.valueOf('B'), (ItemLike)Blocks.TORCH)
/* 1678 */       .pattern("A")
/* 1679 */       .pattern("B")
/* 1680 */       .unlockedBy("has_carved_pumpkin", (CriterionTriggerInstance)has((ItemLike)Blocks.CARVED_PUMPKIN))
/* 1681 */       .save(debug0);
/*      */     
/* 1683 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.MAGENTA_DYE)
/* 1684 */       .requires((ItemLike)Blocks.ALLIUM)
/* 1685 */       .group("magenta_dye")
/* 1686 */       .unlockedBy("has_red_flower", (CriterionTriggerInstance)has((ItemLike)Blocks.ALLIUM))
/* 1687 */       .save(debug0, "magenta_dye_from_allium");
/*      */     
/* 1689 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.MAGENTA_DYE, 4)
/* 1690 */       .requires((ItemLike)Items.BLUE_DYE)
/* 1691 */       .requires((ItemLike)Items.RED_DYE, 2)
/* 1692 */       .requires((ItemLike)Items.WHITE_DYE)
/* 1693 */       .group("magenta_dye")
/* 1694 */       .unlockedBy("has_blue_dye", (CriterionTriggerInstance)has((ItemLike)Items.BLUE_DYE))
/* 1695 */       .unlockedBy("has_rose_red", (CriterionTriggerInstance)has((ItemLike)Items.RED_DYE))
/* 1696 */       .unlockedBy("has_white_dye", (CriterionTriggerInstance)has((ItemLike)Items.WHITE_DYE))
/* 1697 */       .save(debug0, "magenta_dye_from_blue_red_white_dye");
/*      */     
/* 1699 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.MAGENTA_DYE, 3)
/* 1700 */       .requires((ItemLike)Items.BLUE_DYE)
/* 1701 */       .requires((ItemLike)Items.RED_DYE)
/* 1702 */       .requires((ItemLike)Items.PINK_DYE)
/* 1703 */       .group("magenta_dye")
/* 1704 */       .unlockedBy("has_pink_dye", (CriterionTriggerInstance)has((ItemLike)Items.PINK_DYE))
/* 1705 */       .unlockedBy("has_blue_dye", (CriterionTriggerInstance)has((ItemLike)Items.BLUE_DYE))
/* 1706 */       .unlockedBy("has_red_dye", (CriterionTriggerInstance)has((ItemLike)Items.RED_DYE))
/* 1707 */       .save(debug0, "magenta_dye_from_blue_red_pink");
/*      */     
/* 1709 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.MAGENTA_DYE, 2)
/* 1710 */       .requires((ItemLike)Blocks.LILAC)
/* 1711 */       .group("magenta_dye")
/* 1712 */       .unlockedBy("has_double_plant", (CriterionTriggerInstance)has((ItemLike)Blocks.LILAC))
/* 1713 */       .save(debug0, "magenta_dye_from_lilac");
/*      */     
/* 1715 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.MAGENTA_DYE, 2)
/* 1716 */       .requires((ItemLike)Items.PURPLE_DYE)
/* 1717 */       .requires((ItemLike)Items.PINK_DYE)
/* 1718 */       .group("magenta_dye")
/* 1719 */       .unlockedBy("has_pink_dye", (CriterionTriggerInstance)has((ItemLike)Items.PINK_DYE))
/* 1720 */       .unlockedBy("has_purple_dye", (CriterionTriggerInstance)has((ItemLike)Items.PURPLE_DYE))
/* 1721 */       .save(debug0, "magenta_dye_from_purple_and_pink");
/*      */     
/* 1723 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.MAGMA_BLOCK)
/* 1724 */       .define(Character.valueOf('#'), (ItemLike)Items.MAGMA_CREAM)
/* 1725 */       .pattern("##")
/* 1726 */       .pattern("##")
/* 1727 */       .unlockedBy("has_magma_cream", (CriterionTriggerInstance)has((ItemLike)Items.MAGMA_CREAM))
/* 1728 */       .save(debug0);
/*      */     
/* 1730 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.MAGMA_CREAM)
/* 1731 */       .requires((ItemLike)Items.BLAZE_POWDER)
/* 1732 */       .requires((ItemLike)Items.SLIME_BALL)
/* 1733 */       .unlockedBy("has_blaze_powder", (CriterionTriggerInstance)has((ItemLike)Items.BLAZE_POWDER))
/* 1734 */       .save(debug0);
/*      */     
/* 1736 */     ShapedRecipeBuilder.shaped((ItemLike)Items.MAP)
/* 1737 */       .define(Character.valueOf('#'), (ItemLike)Items.PAPER)
/* 1738 */       .define(Character.valueOf('X'), (ItemLike)Items.COMPASS)
/* 1739 */       .pattern("###")
/* 1740 */       .pattern("#X#")
/* 1741 */       .pattern("###")
/* 1742 */       .unlockedBy("has_compass", (CriterionTriggerInstance)has((ItemLike)Items.COMPASS))
/* 1743 */       .save(debug0);
/*      */     
/* 1745 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.MELON)
/* 1746 */       .define(Character.valueOf('M'), (ItemLike)Items.MELON_SLICE)
/* 1747 */       .pattern("MMM")
/* 1748 */       .pattern("MMM")
/* 1749 */       .pattern("MMM")
/* 1750 */       .unlockedBy("has_melon", (CriterionTriggerInstance)has((ItemLike)Items.MELON_SLICE))
/* 1751 */       .save(debug0);
/*      */     
/* 1753 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.MELON_SEEDS)
/* 1754 */       .requires((ItemLike)Items.MELON_SLICE)
/* 1755 */       .unlockedBy("has_melon", (CriterionTriggerInstance)has((ItemLike)Items.MELON_SLICE))
/* 1756 */       .save(debug0);
/*      */     
/* 1758 */     ShapedRecipeBuilder.shaped((ItemLike)Items.MINECART)
/* 1759 */       .define(Character.valueOf('#'), (ItemLike)Items.IRON_INGOT)
/* 1760 */       .pattern("# #")
/* 1761 */       .pattern("###")
/* 1762 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/* 1763 */       .save(debug0);
/*      */     
/* 1765 */     ShapelessRecipeBuilder.shapeless((ItemLike)Blocks.MOSSY_COBBLESTONE)
/* 1766 */       .requires((ItemLike)Blocks.COBBLESTONE)
/* 1767 */       .requires((ItemLike)Blocks.VINE)
/* 1768 */       .unlockedBy("has_vine", (CriterionTriggerInstance)has((ItemLike)Blocks.VINE))
/* 1769 */       .save(debug0);
/*      */     
/* 1771 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.MOSSY_COBBLESTONE_WALL, 6)
/* 1772 */       .define(Character.valueOf('#'), (ItemLike)Blocks.MOSSY_COBBLESTONE)
/* 1773 */       .pattern("###")
/* 1774 */       .pattern("###")
/* 1775 */       .unlockedBy("has_mossy_cobblestone", (CriterionTriggerInstance)has((ItemLike)Blocks.MOSSY_COBBLESTONE))
/* 1776 */       .save(debug0);
/*      */     
/* 1778 */     ShapelessRecipeBuilder.shapeless((ItemLike)Blocks.MOSSY_STONE_BRICKS)
/* 1779 */       .requires((ItemLike)Blocks.STONE_BRICKS)
/* 1780 */       .requires((ItemLike)Blocks.VINE)
/* 1781 */       .unlockedBy("has_mossy_cobblestone", (CriterionTriggerInstance)has((ItemLike)Blocks.MOSSY_COBBLESTONE))
/* 1782 */       .save(debug0);
/*      */     
/* 1784 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.MUSHROOM_STEW)
/* 1785 */       .requires((ItemLike)Blocks.BROWN_MUSHROOM)
/* 1786 */       .requires((ItemLike)Blocks.RED_MUSHROOM)
/* 1787 */       .requires((ItemLike)Items.BOWL)
/* 1788 */       .unlockedBy("has_mushroom_stew", (CriterionTriggerInstance)has((ItemLike)Items.MUSHROOM_STEW))
/* 1789 */       .unlockedBy("has_bowl", (CriterionTriggerInstance)has((ItemLike)Items.BOWL))
/* 1790 */       .unlockedBy("has_brown_mushroom", (CriterionTriggerInstance)has((ItemLike)Blocks.BROWN_MUSHROOM))
/* 1791 */       .unlockedBy("has_red_mushroom", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_MUSHROOM))
/* 1792 */       .save(debug0);
/*      */     
/* 1794 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.NETHER_BRICKS)
/* 1795 */       .define(Character.valueOf('N'), (ItemLike)Items.NETHER_BRICK)
/* 1796 */       .pattern("NN")
/* 1797 */       .pattern("NN")
/* 1798 */       .unlockedBy("has_netherbrick", (CriterionTriggerInstance)has((ItemLike)Items.NETHER_BRICK))
/* 1799 */       .save(debug0);
/*      */     
/* 1801 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.NETHER_BRICK_FENCE, 6)
/* 1802 */       .define(Character.valueOf('#'), (ItemLike)Blocks.NETHER_BRICKS)
/* 1803 */       .define(Character.valueOf('-'), (ItemLike)Items.NETHER_BRICK)
/* 1804 */       .pattern("#-#")
/* 1805 */       .pattern("#-#")
/* 1806 */       .unlockedBy("has_nether_brick", (CriterionTriggerInstance)has((ItemLike)Blocks.NETHER_BRICKS))
/* 1807 */       .save(debug0);
/*      */     
/* 1809 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.NETHER_BRICK_SLAB, 6)
/* 1810 */       .define(Character.valueOf('#'), (ItemLike)Blocks.NETHER_BRICKS)
/* 1811 */       .pattern("###")
/* 1812 */       .unlockedBy("has_nether_brick", (CriterionTriggerInstance)has((ItemLike)Blocks.NETHER_BRICKS))
/* 1813 */       .save(debug0);
/*      */     
/* 1815 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.NETHER_BRICK_STAIRS, 4)
/* 1816 */       .define(Character.valueOf('#'), (ItemLike)Blocks.NETHER_BRICKS)
/* 1817 */       .pattern("#  ")
/* 1818 */       .pattern("## ")
/* 1819 */       .pattern("###")
/* 1820 */       .unlockedBy("has_nether_brick", (CriterionTriggerInstance)has((ItemLike)Blocks.NETHER_BRICKS))
/* 1821 */       .save(debug0);
/*      */     
/* 1823 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.NETHER_WART_BLOCK)
/* 1824 */       .define(Character.valueOf('#'), (ItemLike)Items.NETHER_WART)
/* 1825 */       .pattern("###")
/* 1826 */       .pattern("###")
/* 1827 */       .pattern("###")
/* 1828 */       .unlockedBy("has_nether_wart", (CriterionTriggerInstance)has((ItemLike)Items.NETHER_WART))
/* 1829 */       .save(debug0);
/*      */     
/* 1831 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.NOTE_BLOCK)
/* 1832 */       .define(Character.valueOf('#'), (Tag<Item>)ItemTags.PLANKS)
/* 1833 */       .define(Character.valueOf('X'), (ItemLike)Items.REDSTONE)
/* 1834 */       .pattern("###")
/* 1835 */       .pattern("#X#")
/* 1836 */       .pattern("###")
/* 1837 */       .unlockedBy("has_redstone", (CriterionTriggerInstance)has((ItemLike)Items.REDSTONE))
/* 1838 */       .save(debug0);
/*      */     
/* 1840 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.OBSERVER)
/* 1841 */       .define(Character.valueOf('Q'), (ItemLike)Items.QUARTZ)
/* 1842 */       .define(Character.valueOf('R'), (ItemLike)Items.REDSTONE)
/* 1843 */       .define(Character.valueOf('#'), (ItemLike)Blocks.COBBLESTONE)
/* 1844 */       .pattern("###")
/* 1845 */       .pattern("RRQ")
/* 1846 */       .pattern("###")
/* 1847 */       .unlockedBy("has_quartz", (CriterionTriggerInstance)has((ItemLike)Items.QUARTZ))
/* 1848 */       .save(debug0);
/*      */     
/* 1850 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.ORANGE_DYE)
/* 1851 */       .requires((ItemLike)Blocks.ORANGE_TULIP)
/* 1852 */       .group("orange_dye")
/* 1853 */       .unlockedBy("has_red_flower", (CriterionTriggerInstance)has((ItemLike)Blocks.ORANGE_TULIP))
/* 1854 */       .save(debug0, "orange_dye_from_orange_tulip");
/*      */     
/* 1856 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.ORANGE_DYE, 2)
/* 1857 */       .requires((ItemLike)Items.RED_DYE)
/* 1858 */       .requires((ItemLike)Items.YELLOW_DYE)
/* 1859 */       .group("orange_dye")
/* 1860 */       .unlockedBy("has_red_dye", (CriterionTriggerInstance)has((ItemLike)Items.RED_DYE))
/* 1861 */       .unlockedBy("has_yellow_dye", (CriterionTriggerInstance)has((ItemLike)Items.YELLOW_DYE))
/* 1862 */       .save(debug0, "orange_dye_from_red_yellow");
/*      */     
/* 1864 */     ShapedRecipeBuilder.shaped((ItemLike)Items.PAINTING)
/* 1865 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 1866 */       .define(Character.valueOf('X'), Ingredient.of((Tag)ItemTags.WOOL))
/* 1867 */       .pattern("###")
/* 1868 */       .pattern("#X#")
/* 1869 */       .pattern("###")
/* 1870 */       .unlockedBy("has_wool", (CriterionTriggerInstance)has((Tag<Item>)ItemTags.WOOL))
/* 1871 */       .save(debug0);
/*      */     
/* 1873 */     ShapedRecipeBuilder.shaped((ItemLike)Items.PAPER, 3)
/* 1874 */       .define(Character.valueOf('#'), (ItemLike)Blocks.SUGAR_CANE)
/* 1875 */       .pattern("###")
/* 1876 */       .unlockedBy("has_reeds", (CriterionTriggerInstance)has((ItemLike)Blocks.SUGAR_CANE))
/* 1877 */       .save(debug0);
/*      */     
/* 1879 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.QUARTZ_PILLAR, 2)
/* 1880 */       .define(Character.valueOf('#'), (ItemLike)Blocks.QUARTZ_BLOCK)
/* 1881 */       .pattern("#")
/* 1882 */       .pattern("#")
/* 1883 */       .unlockedBy("has_chiseled_quartz_block", (CriterionTriggerInstance)has((ItemLike)Blocks.CHISELED_QUARTZ_BLOCK))
/* 1884 */       .unlockedBy("has_quartz_block", (CriterionTriggerInstance)has((ItemLike)Blocks.QUARTZ_BLOCK))
/* 1885 */       .unlockedBy("has_quartz_pillar", (CriterionTriggerInstance)has((ItemLike)Blocks.QUARTZ_PILLAR))
/* 1886 */       .save(debug0);
/*      */     
/* 1888 */     ShapelessRecipeBuilder.shapeless((ItemLike)Blocks.PACKED_ICE)
/* 1889 */       .requires((ItemLike)Blocks.ICE, 9)
/* 1890 */       .unlockedBy("has_ice", (CriterionTriggerInstance)has((ItemLike)Blocks.ICE))
/* 1891 */       .save(debug0);
/*      */     
/* 1893 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.PINK_DYE, 2)
/* 1894 */       .requires((ItemLike)Blocks.PEONY)
/* 1895 */       .group("pink_dye")
/* 1896 */       .unlockedBy("has_double_plant", (CriterionTriggerInstance)has((ItemLike)Blocks.PEONY))
/* 1897 */       .save(debug0, "pink_dye_from_peony");
/*      */     
/* 1899 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.PINK_DYE)
/* 1900 */       .requires((ItemLike)Blocks.PINK_TULIP)
/* 1901 */       .group("pink_dye")
/* 1902 */       .unlockedBy("has_red_flower", (CriterionTriggerInstance)has((ItemLike)Blocks.PINK_TULIP))
/* 1903 */       .save(debug0, "pink_dye_from_pink_tulip");
/*      */     
/* 1905 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.PINK_DYE, 2)
/* 1906 */       .requires((ItemLike)Items.RED_DYE)
/* 1907 */       .requires((ItemLike)Items.WHITE_DYE)
/* 1908 */       .group("pink_dye")
/* 1909 */       .unlockedBy("has_white_dye", (CriterionTriggerInstance)has((ItemLike)Items.WHITE_DYE))
/* 1910 */       .unlockedBy("has_red_dye", (CriterionTriggerInstance)has((ItemLike)Items.RED_DYE))
/* 1911 */       .save(debug0, "pink_dye_from_red_white_dye");
/*      */     
/* 1913 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.PISTON)
/* 1914 */       .define(Character.valueOf('R'), (ItemLike)Items.REDSTONE)
/* 1915 */       .define(Character.valueOf('#'), (ItemLike)Blocks.COBBLESTONE)
/* 1916 */       .define(Character.valueOf('T'), (Tag<Item>)ItemTags.PLANKS)
/* 1917 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_INGOT)
/* 1918 */       .pattern("TTT")
/* 1919 */       .pattern("#X#")
/* 1920 */       .pattern("#R#")
/* 1921 */       .unlockedBy("has_redstone", (CriterionTriggerInstance)has((ItemLike)Items.REDSTONE))
/* 1922 */       .save(debug0);
/*      */     
/* 1924 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.POLISHED_BASALT, 4)
/* 1925 */       .define(Character.valueOf('S'), (ItemLike)Blocks.BASALT)
/* 1926 */       .pattern("SS")
/* 1927 */       .pattern("SS")
/* 1928 */       .unlockedBy("has_basalt", (CriterionTriggerInstance)has((ItemLike)Blocks.BASALT))
/* 1929 */       .save(debug0);
/*      */ 
/*      */     
/* 1932 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.POLISHED_GRANITE, 4)
/* 1933 */       .define(Character.valueOf('S'), (ItemLike)Blocks.GRANITE)
/* 1934 */       .pattern("SS")
/* 1935 */       .pattern("SS")
/* 1936 */       .unlockedBy("has_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.GRANITE))
/* 1937 */       .save(debug0);
/*      */     
/* 1939 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.POLISHED_DIORITE, 4)
/* 1940 */       .define(Character.valueOf('S'), (ItemLike)Blocks.DIORITE)
/* 1941 */       .pattern("SS")
/* 1942 */       .pattern("SS")
/* 1943 */       .unlockedBy("has_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.DIORITE))
/* 1944 */       .save(debug0);
/*      */     
/* 1946 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.POLISHED_ANDESITE, 4)
/* 1947 */       .define(Character.valueOf('S'), (ItemLike)Blocks.ANDESITE)
/* 1948 */       .pattern("SS")
/* 1949 */       .pattern("SS")
/* 1950 */       .unlockedBy("has_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.ANDESITE))
/* 1951 */       .save(debug0);
/*      */     
/* 1953 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.PRISMARINE)
/* 1954 */       .define(Character.valueOf('S'), (ItemLike)Items.PRISMARINE_SHARD)
/* 1955 */       .pattern("SS")
/* 1956 */       .pattern("SS")
/* 1957 */       .unlockedBy("has_prismarine_shard", (CriterionTriggerInstance)has((ItemLike)Items.PRISMARINE_SHARD))
/* 1958 */       .save(debug0);
/*      */     
/* 1960 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.PRISMARINE_BRICKS)
/* 1961 */       .define(Character.valueOf('S'), (ItemLike)Items.PRISMARINE_SHARD)
/* 1962 */       .pattern("SSS")
/* 1963 */       .pattern("SSS")
/* 1964 */       .pattern("SSS")
/* 1965 */       .unlockedBy("has_prismarine_shard", (CriterionTriggerInstance)has((ItemLike)Items.PRISMARINE_SHARD))
/* 1966 */       .save(debug0);
/*      */     
/* 1968 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.PRISMARINE_SLAB, 6)
/* 1969 */       .define(Character.valueOf('#'), (ItemLike)Blocks.PRISMARINE)
/* 1970 */       .pattern("###")
/* 1971 */       .unlockedBy("has_prismarine", (CriterionTriggerInstance)has((ItemLike)Blocks.PRISMARINE))
/* 1972 */       .save(debug0);
/*      */     
/* 1974 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.PRISMARINE_BRICK_SLAB, 6)
/* 1975 */       .define(Character.valueOf('#'), (ItemLike)Blocks.PRISMARINE_BRICKS)
/* 1976 */       .pattern("###")
/* 1977 */       .unlockedBy("has_prismarine_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.PRISMARINE_BRICKS))
/* 1978 */       .save(debug0);
/*      */     
/* 1980 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.DARK_PRISMARINE_SLAB, 6)
/* 1981 */       .define(Character.valueOf('#'), (ItemLike)Blocks.DARK_PRISMARINE)
/* 1982 */       .pattern("###")
/* 1983 */       .unlockedBy("has_dark_prismarine", (CriterionTriggerInstance)has((ItemLike)Blocks.DARK_PRISMARINE))
/* 1984 */       .save(debug0);
/*      */     
/* 1986 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.PUMPKIN_PIE)
/* 1987 */       .requires((ItemLike)Blocks.PUMPKIN)
/* 1988 */       .requires((ItemLike)Items.SUGAR)
/* 1989 */       .requires((ItemLike)Items.EGG)
/* 1990 */       .unlockedBy("has_carved_pumpkin", (CriterionTriggerInstance)has((ItemLike)Blocks.CARVED_PUMPKIN))
/* 1991 */       .unlockedBy("has_pumpkin", (CriterionTriggerInstance)has((ItemLike)Blocks.PUMPKIN))
/* 1992 */       .save(debug0);
/*      */     
/* 1994 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.PUMPKIN_SEEDS, 4)
/* 1995 */       .requires((ItemLike)Blocks.PUMPKIN)
/* 1996 */       .unlockedBy("has_pumpkin", (CriterionTriggerInstance)has((ItemLike)Blocks.PUMPKIN))
/* 1997 */       .save(debug0);
/*      */     
/* 1999 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.PURPLE_DYE, 2)
/* 2000 */       .requires((ItemLike)Items.BLUE_DYE)
/* 2001 */       .requires((ItemLike)Items.RED_DYE)
/* 2002 */       .unlockedBy("has_blue_dye", (CriterionTriggerInstance)has((ItemLike)Items.BLUE_DYE))
/* 2003 */       .unlockedBy("has_red_dye", (CriterionTriggerInstance)has((ItemLike)Items.RED_DYE))
/* 2004 */       .save(debug0);
/*      */     
/* 2006 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.SHULKER_BOX)
/* 2007 */       .define(Character.valueOf('#'), (ItemLike)Blocks.CHEST)
/* 2008 */       .define(Character.valueOf('-'), (ItemLike)Items.SHULKER_SHELL)
/* 2009 */       .pattern("-")
/* 2010 */       .pattern("#")
/* 2011 */       .pattern("-")
/* 2012 */       .unlockedBy("has_shulker_shell", (CriterionTriggerInstance)has((ItemLike)Items.SHULKER_SHELL))
/* 2013 */       .save(debug0);
/*      */     
/* 2015 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.PURPUR_BLOCK, 4)
/* 2016 */       .define(Character.valueOf('F'), (ItemLike)Items.POPPED_CHORUS_FRUIT)
/* 2017 */       .pattern("FF")
/* 2018 */       .pattern("FF")
/* 2019 */       .unlockedBy("has_chorus_fruit_popped", (CriterionTriggerInstance)has((ItemLike)Items.POPPED_CHORUS_FRUIT))
/* 2020 */       .save(debug0);
/*      */     
/* 2022 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.PURPUR_PILLAR)
/* 2023 */       .define(Character.valueOf('#'), (ItemLike)Blocks.PURPUR_SLAB)
/* 2024 */       .pattern("#")
/* 2025 */       .pattern("#")
/* 2026 */       .unlockedBy("has_purpur_block", (CriterionTriggerInstance)has((ItemLike)Blocks.PURPUR_BLOCK))
/* 2027 */       .save(debug0);
/*      */     
/* 2029 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.PURPUR_SLAB, 6)
/* 2030 */       .define(Character.valueOf('#'), Ingredient.of(new ItemLike[] { (ItemLike)Blocks.PURPUR_BLOCK, (ItemLike)Blocks.PURPUR_PILLAR
/* 2031 */           })).pattern("###")
/* 2032 */       .unlockedBy("has_purpur_block", (CriterionTriggerInstance)has((ItemLike)Blocks.PURPUR_BLOCK))
/* 2033 */       .save(debug0);
/*      */     
/* 2035 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.PURPUR_STAIRS, 4)
/* 2036 */       .define(Character.valueOf('#'), Ingredient.of(new ItemLike[] { (ItemLike)Blocks.PURPUR_BLOCK, (ItemLike)Blocks.PURPUR_PILLAR
/* 2037 */           })).pattern("#  ")
/* 2038 */       .pattern("## ")
/* 2039 */       .pattern("###")
/* 2040 */       .unlockedBy("has_purpur_block", (CriterionTriggerInstance)has((ItemLike)Blocks.PURPUR_BLOCK))
/* 2041 */       .save(debug0);
/*      */     
/* 2043 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.QUARTZ_BLOCK)
/* 2044 */       .define(Character.valueOf('#'), (ItemLike)Items.QUARTZ)
/* 2045 */       .pattern("##")
/* 2046 */       .pattern("##")
/* 2047 */       .unlockedBy("has_quartz", (CriterionTriggerInstance)has((ItemLike)Items.QUARTZ))
/* 2048 */       .save(debug0);
/*      */     
/* 2050 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.QUARTZ_BRICKS, 4)
/* 2051 */       .define(Character.valueOf('#'), (ItemLike)Blocks.QUARTZ_BLOCK)
/* 2052 */       .pattern("##")
/* 2053 */       .pattern("##")
/* 2054 */       .unlockedBy("has_quartz_block", (CriterionTriggerInstance)has((ItemLike)Blocks.QUARTZ_BLOCK))
/* 2055 */       .save(debug0);
/*      */     
/* 2057 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.QUARTZ_SLAB, 6)
/* 2058 */       .define(Character.valueOf('#'), Ingredient.of(new ItemLike[] { (ItemLike)Blocks.CHISELED_QUARTZ_BLOCK, (ItemLike)Blocks.QUARTZ_BLOCK, (ItemLike)Blocks.QUARTZ_PILLAR
/* 2059 */           })).pattern("###")
/* 2060 */       .unlockedBy("has_chiseled_quartz_block", (CriterionTriggerInstance)has((ItemLike)Blocks.CHISELED_QUARTZ_BLOCK))
/* 2061 */       .unlockedBy("has_quartz_block", (CriterionTriggerInstance)has((ItemLike)Blocks.QUARTZ_BLOCK))
/* 2062 */       .unlockedBy("has_quartz_pillar", (CriterionTriggerInstance)has((ItemLike)Blocks.QUARTZ_PILLAR))
/* 2063 */       .save(debug0);
/*      */     
/* 2065 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.QUARTZ_STAIRS, 4)
/* 2066 */       .define(Character.valueOf('#'), Ingredient.of(new ItemLike[] { (ItemLike)Blocks.CHISELED_QUARTZ_BLOCK, (ItemLike)Blocks.QUARTZ_BLOCK, (ItemLike)Blocks.QUARTZ_PILLAR
/* 2067 */           })).pattern("#  ")
/* 2068 */       .pattern("## ")
/* 2069 */       .pattern("###")
/* 2070 */       .unlockedBy("has_chiseled_quartz_block", (CriterionTriggerInstance)has((ItemLike)Blocks.CHISELED_QUARTZ_BLOCK))
/* 2071 */       .unlockedBy("has_quartz_block", (CriterionTriggerInstance)has((ItemLike)Blocks.QUARTZ_BLOCK))
/* 2072 */       .unlockedBy("has_quartz_pillar", (CriterionTriggerInstance)has((ItemLike)Blocks.QUARTZ_PILLAR))
/* 2073 */       .save(debug0);
/*      */     
/* 2075 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.RABBIT_STEW)
/* 2076 */       .requires((ItemLike)Items.BAKED_POTATO)
/* 2077 */       .requires((ItemLike)Items.COOKED_RABBIT)
/* 2078 */       .requires((ItemLike)Items.BOWL)
/* 2079 */       .requires((ItemLike)Items.CARROT)
/* 2080 */       .requires((ItemLike)Blocks.BROWN_MUSHROOM)
/* 2081 */       .group("rabbit_stew")
/* 2082 */       .unlockedBy("has_cooked_rabbit", (CriterionTriggerInstance)has((ItemLike)Items.COOKED_RABBIT))
/* 2083 */       .save(debug0, "rabbit_stew_from_brown_mushroom");
/*      */     
/* 2085 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.RABBIT_STEW)
/* 2086 */       .requires((ItemLike)Items.BAKED_POTATO)
/* 2087 */       .requires((ItemLike)Items.COOKED_RABBIT)
/* 2088 */       .requires((ItemLike)Items.BOWL)
/* 2089 */       .requires((ItemLike)Items.CARROT)
/* 2090 */       .requires((ItemLike)Blocks.RED_MUSHROOM)
/* 2091 */       .group("rabbit_stew")
/* 2092 */       .unlockedBy("has_cooked_rabbit", (CriterionTriggerInstance)has((ItemLike)Items.COOKED_RABBIT))
/* 2093 */       .save(debug0, "rabbit_stew_from_red_mushroom");
/*      */     
/* 2095 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.RAIL, 16)
/* 2096 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 2097 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_INGOT)
/* 2098 */       .pattern("X X")
/* 2099 */       .pattern("X#X")
/* 2100 */       .pattern("X X")
/* 2101 */       .unlockedBy("has_minecart", (CriterionTriggerInstance)has((ItemLike)Items.MINECART))
/* 2102 */       .save(debug0);
/*      */     
/* 2104 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.REDSTONE, 9)
/* 2105 */       .requires((ItemLike)Blocks.REDSTONE_BLOCK)
/* 2106 */       .unlockedBy("has_redstone_block", (CriterionTriggerInstance)has((ItemLike)Blocks.REDSTONE_BLOCK))
/* 2107 */       .save(debug0);
/*      */     
/* 2109 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.REDSTONE_BLOCK)
/* 2110 */       .define(Character.valueOf('#'), (ItemLike)Items.REDSTONE)
/* 2111 */       .pattern("###")
/* 2112 */       .pattern("###")
/* 2113 */       .pattern("###")
/* 2114 */       .unlockedBy("has_redstone", (CriterionTriggerInstance)has((ItemLike)Items.REDSTONE))
/* 2115 */       .save(debug0);
/*      */     
/* 2117 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.REDSTONE_LAMP)
/* 2118 */       .define(Character.valueOf('R'), (ItemLike)Items.REDSTONE)
/* 2119 */       .define(Character.valueOf('G'), (ItemLike)Blocks.GLOWSTONE)
/* 2120 */       .pattern(" R ")
/* 2121 */       .pattern("RGR")
/* 2122 */       .pattern(" R ")
/* 2123 */       .unlockedBy("has_glowstone", (CriterionTriggerInstance)has((ItemLike)Blocks.GLOWSTONE))
/* 2124 */       .save(debug0);
/*      */     
/* 2126 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.REDSTONE_TORCH)
/* 2127 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 2128 */       .define(Character.valueOf('X'), (ItemLike)Items.REDSTONE)
/* 2129 */       .pattern("X")
/* 2130 */       .pattern("#")
/* 2131 */       .unlockedBy("has_redstone", (CriterionTriggerInstance)has((ItemLike)Items.REDSTONE))
/* 2132 */       .save(debug0);
/*      */     
/* 2134 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.RED_DYE)
/* 2135 */       .requires((ItemLike)Items.BEETROOT)
/* 2136 */       .group("red_dye")
/* 2137 */       .unlockedBy("has_beetroot", (CriterionTriggerInstance)has((ItemLike)Items.BEETROOT))
/* 2138 */       .save(debug0, "red_dye_from_beetroot");
/*      */     
/* 2140 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.RED_DYE)
/* 2141 */       .requires((ItemLike)Blocks.POPPY)
/* 2142 */       .group("red_dye")
/* 2143 */       .unlockedBy("has_red_flower", (CriterionTriggerInstance)has((ItemLike)Blocks.POPPY))
/* 2144 */       .save(debug0, "red_dye_from_poppy");
/*      */     
/* 2146 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.RED_DYE, 2)
/* 2147 */       .requires((ItemLike)Blocks.ROSE_BUSH)
/* 2148 */       .group("red_dye")
/* 2149 */       .unlockedBy("has_double_plant", (CriterionTriggerInstance)has((ItemLike)Blocks.ROSE_BUSH))
/* 2150 */       .save(debug0, "red_dye_from_rose_bush");
/*      */     
/* 2152 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.RED_DYE)
/* 2153 */       .requires((ItemLike)Blocks.RED_TULIP)
/* 2154 */       .group("red_dye")
/* 2155 */       .unlockedBy("has_red_flower", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_TULIP))
/* 2156 */       .save(debug0, "red_dye_from_tulip");
/*      */     
/* 2158 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.RED_NETHER_BRICKS)
/* 2159 */       .define(Character.valueOf('W'), (ItemLike)Items.NETHER_WART)
/* 2160 */       .define(Character.valueOf('N'), (ItemLike)Items.NETHER_BRICK)
/* 2161 */       .pattern("NW")
/* 2162 */       .pattern("WN")
/* 2163 */       .unlockedBy("has_nether_wart", (CriterionTriggerInstance)has((ItemLike)Items.NETHER_WART))
/* 2164 */       .save(debug0);
/*      */     
/* 2166 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.RED_SANDSTONE)
/* 2167 */       .define(Character.valueOf('#'), (ItemLike)Blocks.RED_SAND)
/* 2168 */       .pattern("##")
/* 2169 */       .pattern("##")
/* 2170 */       .unlockedBy("has_sand", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_SAND))
/* 2171 */       .save(debug0);
/*      */     
/* 2173 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.RED_SANDSTONE_SLAB, 6)
/* 2174 */       .define(Character.valueOf('#'), Ingredient.of(new ItemLike[] { (ItemLike)Blocks.RED_SANDSTONE, (ItemLike)Blocks.CHISELED_RED_SANDSTONE
/* 2175 */           })).pattern("###")
/* 2176 */       .unlockedBy("has_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_SANDSTONE))
/* 2177 */       .unlockedBy("has_chiseled_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.CHISELED_RED_SANDSTONE))
/* 2178 */       .save(debug0);
/*      */     
/* 2180 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.CUT_RED_SANDSTONE_SLAB, 6)
/* 2181 */       .define(Character.valueOf('#'), (ItemLike)Blocks.CUT_RED_SANDSTONE)
/* 2182 */       .pattern("###")
/* 2183 */       .unlockedBy("has_cut_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.CUT_RED_SANDSTONE))
/* 2184 */       .save(debug0);
/*      */     
/* 2186 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.RED_SANDSTONE_STAIRS, 4)
/* 2187 */       .define(Character.valueOf('#'), Ingredient.of(new ItemLike[] { (ItemLike)Blocks.RED_SANDSTONE, (ItemLike)Blocks.CHISELED_RED_SANDSTONE, (ItemLike)Blocks.CUT_RED_SANDSTONE
/* 2188 */           })).pattern("#  ")
/* 2189 */       .pattern("## ")
/* 2190 */       .pattern("###")
/* 2191 */       .unlockedBy("has_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_SANDSTONE))
/* 2192 */       .unlockedBy("has_chiseled_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.CHISELED_RED_SANDSTONE))
/* 2193 */       .unlockedBy("has_cut_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.CUT_RED_SANDSTONE))
/* 2194 */       .save(debug0);
/*      */     
/* 2196 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.REPEATER)
/* 2197 */       .define(Character.valueOf('#'), (ItemLike)Blocks.REDSTONE_TORCH)
/* 2198 */       .define(Character.valueOf('X'), (ItemLike)Items.REDSTONE)
/* 2199 */       .define(Character.valueOf('I'), (ItemLike)Blocks.STONE)
/* 2200 */       .pattern("#X#")
/* 2201 */       .pattern("III")
/* 2202 */       .unlockedBy("has_redstone_torch", (CriterionTriggerInstance)has((ItemLike)Blocks.REDSTONE_TORCH))
/* 2203 */       .save(debug0);
/*      */     
/* 2205 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.SANDSTONE)
/* 2206 */       .define(Character.valueOf('#'), (ItemLike)Blocks.SAND)
/* 2207 */       .pattern("##")
/* 2208 */       .pattern("##")
/* 2209 */       .unlockedBy("has_sand", (CriterionTriggerInstance)has((ItemLike)Blocks.SAND))
/* 2210 */       .save(debug0);
/*      */     
/* 2212 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.SANDSTONE_SLAB, 6)
/* 2213 */       .define(Character.valueOf('#'), Ingredient.of(new ItemLike[] { (ItemLike)Blocks.SANDSTONE, (ItemLike)Blocks.CHISELED_SANDSTONE
/* 2214 */           })).pattern("###")
/* 2215 */       .unlockedBy("has_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.SANDSTONE))
/* 2216 */       .unlockedBy("has_chiseled_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.CHISELED_SANDSTONE))
/* 2217 */       .save(debug0);
/*      */     
/* 2219 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.CUT_SANDSTONE_SLAB, 6)
/* 2220 */       .define(Character.valueOf('#'), (ItemLike)Blocks.CUT_SANDSTONE)
/* 2221 */       .pattern("###")
/* 2222 */       .unlockedBy("has_cut_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.CUT_SANDSTONE))
/* 2223 */       .save(debug0);
/*      */     
/* 2225 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.SANDSTONE_STAIRS, 4)
/* 2226 */       .define(Character.valueOf('#'), Ingredient.of(new ItemLike[] { (ItemLike)Blocks.SANDSTONE, (ItemLike)Blocks.CHISELED_SANDSTONE, (ItemLike)Blocks.CUT_SANDSTONE
/* 2227 */           })).pattern("#  ")
/* 2228 */       .pattern("## ")
/* 2229 */       .pattern("###")
/* 2230 */       .unlockedBy("has_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.SANDSTONE))
/* 2231 */       .unlockedBy("has_chiseled_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.CHISELED_SANDSTONE))
/* 2232 */       .unlockedBy("has_cut_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.CUT_SANDSTONE))
/* 2233 */       .save(debug0);
/*      */     
/* 2235 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.SEA_LANTERN)
/* 2236 */       .define(Character.valueOf('S'), (ItemLike)Items.PRISMARINE_SHARD)
/* 2237 */       .define(Character.valueOf('C'), (ItemLike)Items.PRISMARINE_CRYSTALS)
/* 2238 */       .pattern("SCS")
/* 2239 */       .pattern("CCC")
/* 2240 */       .pattern("SCS")
/* 2241 */       .unlockedBy("has_prismarine_crystals", (CriterionTriggerInstance)has((ItemLike)Items.PRISMARINE_CRYSTALS))
/* 2242 */       .save(debug0);
/*      */     
/* 2244 */     ShapedRecipeBuilder.shaped((ItemLike)Items.SHEARS)
/* 2245 */       .define(Character.valueOf('#'), (ItemLike)Items.IRON_INGOT)
/* 2246 */       .pattern(" #")
/* 2247 */       .pattern("# ")
/* 2248 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/* 2249 */       .save(debug0);
/*      */     
/* 2251 */     ShapedRecipeBuilder.shaped((ItemLike)Items.SHIELD)
/* 2252 */       .define(Character.valueOf('W'), (Tag<Item>)ItemTags.PLANKS)
/* 2253 */       .define(Character.valueOf('o'), (ItemLike)Items.IRON_INGOT)
/* 2254 */       .pattern("WoW")
/* 2255 */       .pattern("WWW")
/* 2256 */       .pattern(" W ")
/* 2257 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/* 2258 */       .save(debug0);
/*      */     
/* 2260 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.SLIME_BLOCK)
/* 2261 */       .define(Character.valueOf('#'), (ItemLike)Items.SLIME_BALL)
/* 2262 */       .pattern("###")
/* 2263 */       .pattern("###")
/* 2264 */       .pattern("###")
/* 2265 */       .unlockedBy("has_slime_ball", (CriterionTriggerInstance)has((ItemLike)Items.SLIME_BALL))
/* 2266 */       .save(debug0);
/*      */     
/* 2268 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.SLIME_BALL, 9)
/* 2269 */       .requires((ItemLike)Blocks.SLIME_BLOCK)
/* 2270 */       .unlockedBy("has_slime", (CriterionTriggerInstance)has((ItemLike)Blocks.SLIME_BLOCK))
/* 2271 */       .save(debug0);
/*      */     
/* 2273 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.CUT_RED_SANDSTONE, 4)
/* 2274 */       .define(Character.valueOf('#'), (ItemLike)Blocks.RED_SANDSTONE)
/* 2275 */       .pattern("##")
/* 2276 */       .pattern("##")
/* 2277 */       .unlockedBy("has_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_SANDSTONE))
/* 2278 */       .save(debug0);
/*      */     
/* 2280 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.CUT_SANDSTONE, 4)
/* 2281 */       .define(Character.valueOf('#'), (ItemLike)Blocks.SANDSTONE)
/* 2282 */       .pattern("##")
/* 2283 */       .pattern("##")
/* 2284 */       .unlockedBy("has_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.SANDSTONE))
/* 2285 */       .save(debug0);
/*      */     
/* 2287 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.SNOW_BLOCK)
/* 2288 */       .define(Character.valueOf('#'), (ItemLike)Items.SNOWBALL)
/* 2289 */       .pattern("##")
/* 2290 */       .pattern("##")
/* 2291 */       .unlockedBy("has_snowball", (CriterionTriggerInstance)has((ItemLike)Items.SNOWBALL))
/* 2292 */       .save(debug0);
/*      */     
/* 2294 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.SNOW, 6)
/* 2295 */       .define(Character.valueOf('#'), (ItemLike)Blocks.SNOW_BLOCK)
/* 2296 */       .pattern("###")
/* 2297 */       .unlockedBy("has_snowball", (CriterionTriggerInstance)has((ItemLike)Items.SNOWBALL))
/* 2298 */       .save(debug0);
/*      */     
/* 2300 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.SOUL_CAMPFIRE)
/* 2301 */       .define(Character.valueOf('L'), (Tag<Item>)ItemTags.LOGS)
/* 2302 */       .define(Character.valueOf('S'), (ItemLike)Items.STICK)
/* 2303 */       .define(Character.valueOf('#'), (Tag<Item>)ItemTags.SOUL_FIRE_BASE_BLOCKS)
/* 2304 */       .pattern(" S ")
/* 2305 */       .pattern("S#S")
/* 2306 */       .pattern("LLL")
/* 2307 */       .unlockedBy("has_stick", (CriterionTriggerInstance)has((ItemLike)Items.STICK))
/* 2308 */       .unlockedBy("has_soul_sand", (CriterionTriggerInstance)has((Tag<Item>)ItemTags.SOUL_FIRE_BASE_BLOCKS))
/* 2309 */       .save(debug0);
/*      */     
/* 2311 */     ShapedRecipeBuilder.shaped((ItemLike)Items.GLISTERING_MELON_SLICE)
/* 2312 */       .define(Character.valueOf('#'), (ItemLike)Items.GOLD_NUGGET)
/* 2313 */       .define(Character.valueOf('X'), (ItemLike)Items.MELON_SLICE)
/* 2314 */       .pattern("###")
/* 2315 */       .pattern("#X#")
/* 2316 */       .pattern("###")
/* 2317 */       .unlockedBy("has_melon", (CriterionTriggerInstance)has((ItemLike)Items.MELON_SLICE))
/* 2318 */       .save(debug0);
/*      */     
/* 2320 */     ShapedRecipeBuilder.shaped((ItemLike)Items.SPECTRAL_ARROW, 2)
/* 2321 */       .define(Character.valueOf('#'), (ItemLike)Items.GLOWSTONE_DUST)
/* 2322 */       .define(Character.valueOf('X'), (ItemLike)Items.ARROW)
/* 2323 */       .pattern(" # ")
/* 2324 */       .pattern("#X#")
/* 2325 */       .pattern(" # ")
/* 2326 */       .unlockedBy("has_glowstone_dust", (CriterionTriggerInstance)has((ItemLike)Items.GLOWSTONE_DUST))
/* 2327 */       .save(debug0);
/*      */     
/* 2329 */     ShapedRecipeBuilder.shaped((ItemLike)Items.STICK, 4)
/* 2330 */       .define(Character.valueOf('#'), (Tag<Item>)ItemTags.PLANKS)
/* 2331 */       .pattern("#")
/* 2332 */       .pattern("#")
/* 2333 */       .group("sticks")
/* 2334 */       .unlockedBy("has_planks", (CriterionTriggerInstance)has((Tag<Item>)ItemTags.PLANKS))
/* 2335 */       .save(debug0);
/*      */     
/* 2337 */     ShapedRecipeBuilder.shaped((ItemLike)Items.STICK, 1)
/* 2338 */       .define(Character.valueOf('#'), (ItemLike)Blocks.BAMBOO)
/* 2339 */       .pattern("#")
/* 2340 */       .pattern("#")
/* 2341 */       .group("sticks")
/* 2342 */       .unlockedBy("has_bamboo", (CriterionTriggerInstance)has((ItemLike)Blocks.BAMBOO))
/* 2343 */       .save(debug0, "stick_from_bamboo_item");
/*      */     
/* 2345 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.STICKY_PISTON)
/* 2346 */       .define(Character.valueOf('P'), (ItemLike)Blocks.PISTON)
/* 2347 */       .define(Character.valueOf('S'), (ItemLike)Items.SLIME_BALL)
/* 2348 */       .pattern("S")
/* 2349 */       .pattern("P")
/* 2350 */       .unlockedBy("has_slime_ball", (CriterionTriggerInstance)has((ItemLike)Items.SLIME_BALL))
/* 2351 */       .save(debug0);
/*      */     
/* 2353 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.STONE_BRICKS, 4)
/* 2354 */       .define(Character.valueOf('#'), (ItemLike)Blocks.STONE)
/* 2355 */       .pattern("##")
/* 2356 */       .pattern("##")
/* 2357 */       .unlockedBy("has_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.STONE))
/* 2358 */       .save(debug0);
/*      */     
/* 2360 */     ShapedRecipeBuilder.shaped((ItemLike)Items.STONE_AXE)
/* 2361 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 2362 */       .define(Character.valueOf('X'), (Tag<Item>)ItemTags.STONE_TOOL_MATERIALS)
/* 2363 */       .pattern("XX")
/* 2364 */       .pattern("X#")
/* 2365 */       .pattern(" #")
/* 2366 */       .unlockedBy("has_cobblestone", (CriterionTriggerInstance)has((Tag<Item>)ItemTags.STONE_TOOL_MATERIALS))
/* 2367 */       .save(debug0);
/*      */     
/* 2369 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.STONE_BRICK_SLAB, 6)
/* 2370 */       .define(Character.valueOf('#'), (ItemLike)Blocks.STONE_BRICKS)
/* 2371 */       .pattern("###")
/* 2372 */       .unlockedBy("has_stone_bricks", (CriterionTriggerInstance)has((Tag<Item>)ItemTags.STONE_BRICKS))
/* 2373 */       .save(debug0);
/*      */     
/* 2375 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.STONE_BRICK_STAIRS, 4)
/* 2376 */       .define(Character.valueOf('#'), (ItemLike)Blocks.STONE_BRICKS)
/* 2377 */       .pattern("#  ")
/* 2378 */       .pattern("## ")
/* 2379 */       .pattern("###")
/* 2380 */       .unlockedBy("has_stone_bricks", (CriterionTriggerInstance)has((Tag<Item>)ItemTags.STONE_BRICKS))
/* 2381 */       .save(debug0);
/*      */     
/* 2383 */     ShapelessRecipeBuilder.shapeless((ItemLike)Blocks.STONE_BUTTON)
/* 2384 */       .requires((ItemLike)Blocks.STONE)
/* 2385 */       .unlockedBy("has_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.STONE))
/* 2386 */       .save(debug0);
/*      */     
/* 2388 */     ShapedRecipeBuilder.shaped((ItemLike)Items.STONE_HOE)
/* 2389 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 2390 */       .define(Character.valueOf('X'), (Tag<Item>)ItemTags.STONE_TOOL_MATERIALS)
/* 2391 */       .pattern("XX")
/* 2392 */       .pattern(" #")
/* 2393 */       .pattern(" #")
/* 2394 */       .unlockedBy("has_cobblestone", (CriterionTriggerInstance)has((Tag<Item>)ItemTags.STONE_TOOL_MATERIALS))
/* 2395 */       .save(debug0);
/*      */     
/* 2397 */     ShapedRecipeBuilder.shaped((ItemLike)Items.STONE_PICKAXE)
/* 2398 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 2399 */       .define(Character.valueOf('X'), (Tag<Item>)ItemTags.STONE_TOOL_MATERIALS)
/* 2400 */       .pattern("XXX")
/* 2401 */       .pattern(" # ")
/* 2402 */       .pattern(" # ")
/* 2403 */       .unlockedBy("has_cobblestone", (CriterionTriggerInstance)has((Tag<Item>)ItemTags.STONE_TOOL_MATERIALS))
/* 2404 */       .save(debug0);
/*      */     
/* 2406 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.STONE_PRESSURE_PLATE)
/* 2407 */       .define(Character.valueOf('#'), (ItemLike)Blocks.STONE)
/* 2408 */       .pattern("##")
/* 2409 */       .unlockedBy("has_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.STONE))
/* 2410 */       .save(debug0);
/*      */     
/* 2412 */     ShapedRecipeBuilder.shaped((ItemLike)Items.STONE_SHOVEL)
/* 2413 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 2414 */       .define(Character.valueOf('X'), (Tag<Item>)ItemTags.STONE_TOOL_MATERIALS)
/* 2415 */       .pattern("X")
/* 2416 */       .pattern("#")
/* 2417 */       .pattern("#")
/* 2418 */       .unlockedBy("has_cobblestone", (CriterionTriggerInstance)has((Tag<Item>)ItemTags.STONE_TOOL_MATERIALS))
/* 2419 */       .save(debug0);
/*      */     
/* 2421 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.STONE_SLAB, 6)
/* 2422 */       .define(Character.valueOf('#'), (ItemLike)Blocks.STONE)
/* 2423 */       .pattern("###")
/* 2424 */       .unlockedBy("has_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.STONE))
/* 2425 */       .save(debug0);
/*      */     
/* 2427 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.SMOOTH_STONE_SLAB, 6)
/* 2428 */       .define(Character.valueOf('#'), (ItemLike)Blocks.SMOOTH_STONE)
/* 2429 */       .pattern("###")
/* 2430 */       .unlockedBy("has_smooth_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.SMOOTH_STONE))
/* 2431 */       .save(debug0);
/*      */     
/* 2433 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.COBBLESTONE_STAIRS, 4)
/* 2434 */       .define(Character.valueOf('#'), (ItemLike)Blocks.COBBLESTONE)
/* 2435 */       .pattern("#  ")
/* 2436 */       .pattern("## ")
/* 2437 */       .pattern("###")
/* 2438 */       .unlockedBy("has_cobblestone", (CriterionTriggerInstance)has((ItemLike)Blocks.COBBLESTONE))
/* 2439 */       .save(debug0);
/*      */     
/* 2441 */     ShapedRecipeBuilder.shaped((ItemLike)Items.STONE_SWORD)
/* 2442 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 2443 */       .define(Character.valueOf('X'), (Tag<Item>)ItemTags.STONE_TOOL_MATERIALS)
/* 2444 */       .pattern("X")
/* 2445 */       .pattern("X")
/* 2446 */       .pattern("#")
/* 2447 */       .unlockedBy("has_cobblestone", (CriterionTriggerInstance)has((Tag<Item>)ItemTags.STONE_TOOL_MATERIALS))
/* 2448 */       .save(debug0);
/*      */     
/* 2450 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.WHITE_WOOL)
/* 2451 */       .define(Character.valueOf('#'), (ItemLike)Items.STRING)
/* 2452 */       .pattern("##")
/* 2453 */       .pattern("##")
/* 2454 */       .unlockedBy("has_string", (CriterionTriggerInstance)has((ItemLike)Items.STRING))
/* 2455 */       .save(debug0, "white_wool_from_string");
/*      */     
/* 2457 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.SUGAR)
/* 2458 */       .requires((ItemLike)Blocks.SUGAR_CANE)
/* 2459 */       .group("sugar")
/* 2460 */       .unlockedBy("has_reeds", (CriterionTriggerInstance)has((ItemLike)Blocks.SUGAR_CANE))
/* 2461 */       .save(debug0, "sugar_from_sugar_cane");
/*      */     
/* 2463 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.SUGAR, 3)
/* 2464 */       .requires((ItemLike)Items.HONEY_BOTTLE)
/* 2465 */       .group("sugar")
/* 2466 */       .unlockedBy("has_honey_bottle", (CriterionTriggerInstance)has((ItemLike)Items.HONEY_BOTTLE))
/* 2467 */       .save(debug0, "sugar_from_honey_bottle");
/*      */     
/* 2469 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.TARGET)
/* 2470 */       .define(Character.valueOf('H'), (ItemLike)Items.HAY_BLOCK)
/* 2471 */       .define(Character.valueOf('R'), (ItemLike)Items.REDSTONE)
/* 2472 */       .pattern(" R ")
/* 2473 */       .pattern("RHR")
/* 2474 */       .pattern(" R ")
/* 2475 */       .unlockedBy("has_redstone", (CriterionTriggerInstance)has((ItemLike)Items.REDSTONE))
/* 2476 */       .unlockedBy("has_hay_block", (CriterionTriggerInstance)has((ItemLike)Blocks.HAY_BLOCK))
/* 2477 */       .save(debug0);
/*      */     
/* 2479 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.TNT)
/* 2480 */       .define(Character.valueOf('#'), Ingredient.of(new ItemLike[] { (ItemLike)Blocks.SAND, (ItemLike)Blocks.RED_SAND
/* 2481 */           })).define(Character.valueOf('X'), (ItemLike)Items.GUNPOWDER)
/* 2482 */       .pattern("X#X")
/* 2483 */       .pattern("#X#")
/* 2484 */       .pattern("X#X")
/* 2485 */       .unlockedBy("has_gunpowder", (CriterionTriggerInstance)has((ItemLike)Items.GUNPOWDER))
/* 2486 */       .save(debug0);
/*      */     
/* 2488 */     ShapedRecipeBuilder.shaped((ItemLike)Items.TNT_MINECART)
/* 2489 */       .define(Character.valueOf('A'), (ItemLike)Blocks.TNT)
/* 2490 */       .define(Character.valueOf('B'), (ItemLike)Items.MINECART)
/* 2491 */       .pattern("A")
/* 2492 */       .pattern("B")
/* 2493 */       .unlockedBy("has_minecart", (CriterionTriggerInstance)has((ItemLike)Items.MINECART))
/* 2494 */       .save(debug0);
/*      */     
/* 2496 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.TORCH, 4)
/* 2497 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 2498 */       .define(Character.valueOf('X'), Ingredient.of(new ItemLike[] { (ItemLike)Items.COAL, (ItemLike)Items.CHARCOAL
/* 2499 */           })).pattern("X")
/* 2500 */       .pattern("#")
/* 2501 */       .unlockedBy("has_stone_pickaxe", (CriterionTriggerInstance)has((ItemLike)Items.STONE_PICKAXE))
/* 2502 */       .save(debug0);
/*      */     
/* 2504 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.SOUL_TORCH, 4)
/* 2505 */       .define(Character.valueOf('X'), Ingredient.of(new ItemLike[] { (ItemLike)Items.COAL, (ItemLike)Items.CHARCOAL
/* 2506 */           })).define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 2507 */       .define(Character.valueOf('S'), (Tag<Item>)ItemTags.SOUL_FIRE_BASE_BLOCKS)
/* 2508 */       .pattern("X")
/* 2509 */       .pattern("#")
/* 2510 */       .pattern("S")
/* 2511 */       .unlockedBy("has_soul_sand", (CriterionTriggerInstance)has((Tag<Item>)ItemTags.SOUL_FIRE_BASE_BLOCKS))
/* 2512 */       .save(debug0);
/*      */     
/* 2514 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.LANTERN)
/* 2515 */       .define(Character.valueOf('#'), (ItemLike)Items.TORCH)
/* 2516 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_NUGGET)
/* 2517 */       .pattern("XXX")
/* 2518 */       .pattern("X#X")
/* 2519 */       .pattern("XXX")
/* 2520 */       .unlockedBy("has_iron_nugget", (CriterionTriggerInstance)has((ItemLike)Items.IRON_NUGGET))
/* 2521 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/* 2522 */       .save(debug0);
/*      */     
/* 2524 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.SOUL_LANTERN)
/* 2525 */       .define(Character.valueOf('#'), (ItemLike)Items.SOUL_TORCH)
/* 2526 */       .define(Character.valueOf('X'), (ItemLike)Items.IRON_NUGGET)
/* 2527 */       .pattern("XXX")
/* 2528 */       .pattern("X#X")
/* 2529 */       .pattern("XXX")
/* 2530 */       .unlockedBy("has_soul_torch", (CriterionTriggerInstance)has((ItemLike)Items.SOUL_TORCH))
/* 2531 */       .save(debug0);
/*      */     
/* 2533 */     ShapelessRecipeBuilder.shapeless((ItemLike)Blocks.TRAPPED_CHEST)
/* 2534 */       .requires((ItemLike)Blocks.CHEST)
/* 2535 */       .requires((ItemLike)Blocks.TRIPWIRE_HOOK)
/* 2536 */       .unlockedBy("has_tripwire_hook", (CriterionTriggerInstance)has((ItemLike)Blocks.TRIPWIRE_HOOK))
/* 2537 */       .save(debug0);
/*      */     
/* 2539 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.TRIPWIRE_HOOK, 2)
/* 2540 */       .define(Character.valueOf('#'), (Tag<Item>)ItemTags.PLANKS)
/* 2541 */       .define(Character.valueOf('S'), (ItemLike)Items.STICK)
/* 2542 */       .define(Character.valueOf('I'), (ItemLike)Items.IRON_INGOT)
/* 2543 */       .pattern("I")
/* 2544 */       .pattern("S")
/* 2545 */       .pattern("#")
/* 2546 */       .unlockedBy("has_string", (CriterionTriggerInstance)has((ItemLike)Items.STRING))
/* 2547 */       .save(debug0);
/*      */     
/* 2549 */     ShapedRecipeBuilder.shaped((ItemLike)Items.TURTLE_HELMET)
/* 2550 */       .define(Character.valueOf('X'), (ItemLike)Items.SCUTE)
/* 2551 */       .pattern("XXX")
/* 2552 */       .pattern("X X")
/* 2553 */       .unlockedBy("has_scute", (CriterionTriggerInstance)has((ItemLike)Items.SCUTE))
/* 2554 */       .save(debug0);
/*      */     
/* 2556 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.WHEAT, 9)
/* 2557 */       .requires((ItemLike)Blocks.HAY_BLOCK)
/* 2558 */       .unlockedBy("has_hay_block", (CriterionTriggerInstance)has((ItemLike)Blocks.HAY_BLOCK))
/* 2559 */       .save(debug0);
/*      */     
/* 2561 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.WHITE_DYE)
/* 2562 */       .requires((ItemLike)Items.BONE_MEAL)
/* 2563 */       .group("white_dye")
/* 2564 */       .unlockedBy("has_bone_meal", (CriterionTriggerInstance)has((ItemLike)Items.BONE_MEAL))
/* 2565 */       .save(debug0);
/*      */     
/* 2567 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.WHITE_DYE)
/* 2568 */       .requires((ItemLike)Blocks.LILY_OF_THE_VALLEY)
/* 2569 */       .group("white_dye")
/* 2570 */       .unlockedBy("has_white_flower", (CriterionTriggerInstance)has((ItemLike)Blocks.LILY_OF_THE_VALLEY))
/* 2571 */       .save(debug0, "white_dye_from_lily_of_the_valley");
/*      */     
/* 2573 */     ShapedRecipeBuilder.shaped((ItemLike)Items.WOODEN_AXE)
/* 2574 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 2575 */       .define(Character.valueOf('X'), (Tag<Item>)ItemTags.PLANKS)
/* 2576 */       .pattern("XX")
/* 2577 */       .pattern("X#")
/* 2578 */       .pattern(" #")
/* 2579 */       .unlockedBy("has_stick", (CriterionTriggerInstance)has((ItemLike)Items.STICK))
/* 2580 */       .save(debug0);
/*      */     
/* 2582 */     ShapedRecipeBuilder.shaped((ItemLike)Items.WOODEN_HOE)
/* 2583 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 2584 */       .define(Character.valueOf('X'), (Tag<Item>)ItemTags.PLANKS)
/* 2585 */       .pattern("XX")
/* 2586 */       .pattern(" #")
/* 2587 */       .pattern(" #")
/* 2588 */       .unlockedBy("has_stick", (CriterionTriggerInstance)has((ItemLike)Items.STICK))
/* 2589 */       .save(debug0);
/*      */     
/* 2591 */     ShapedRecipeBuilder.shaped((ItemLike)Items.WOODEN_PICKAXE)
/* 2592 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 2593 */       .define(Character.valueOf('X'), (Tag<Item>)ItemTags.PLANKS)
/* 2594 */       .pattern("XXX")
/* 2595 */       .pattern(" # ")
/* 2596 */       .pattern(" # ")
/* 2597 */       .unlockedBy("has_stick", (CriterionTriggerInstance)has((ItemLike)Items.STICK))
/* 2598 */       .save(debug0);
/*      */     
/* 2600 */     ShapedRecipeBuilder.shaped((ItemLike)Items.WOODEN_SHOVEL)
/* 2601 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 2602 */       .define(Character.valueOf('X'), (Tag<Item>)ItemTags.PLANKS)
/* 2603 */       .pattern("X")
/* 2604 */       .pattern("#")
/* 2605 */       .pattern("#")
/* 2606 */       .unlockedBy("has_stick", (CriterionTriggerInstance)has((ItemLike)Items.STICK))
/* 2607 */       .save(debug0);
/*      */     
/* 2609 */     ShapedRecipeBuilder.shaped((ItemLike)Items.WOODEN_SWORD)
/* 2610 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 2611 */       .define(Character.valueOf('X'), (Tag<Item>)ItemTags.PLANKS)
/* 2612 */       .pattern("X")
/* 2613 */       .pattern("X")
/* 2614 */       .pattern("#")
/* 2615 */       .unlockedBy("has_stick", (CriterionTriggerInstance)has((ItemLike)Items.STICK))
/* 2616 */       .save(debug0);
/*      */     
/* 2618 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.WRITABLE_BOOK)
/* 2619 */       .requires((ItemLike)Items.BOOK)
/* 2620 */       .requires((ItemLike)Items.INK_SAC)
/* 2621 */       .requires((ItemLike)Items.FEATHER)
/* 2622 */       .unlockedBy("has_book", (CriterionTriggerInstance)has((ItemLike)Items.BOOK))
/* 2623 */       .save(debug0);
/*      */     
/* 2625 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.YELLOW_DYE)
/* 2626 */       .requires((ItemLike)Blocks.DANDELION)
/* 2627 */       .group("yellow_dye")
/* 2628 */       .unlockedBy("has_yellow_flower", (CriterionTriggerInstance)has((ItemLike)Blocks.DANDELION))
/* 2629 */       .save(debug0, "yellow_dye_from_dandelion");
/*      */     
/* 2631 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.YELLOW_DYE, 2)
/* 2632 */       .requires((ItemLike)Blocks.SUNFLOWER)
/* 2633 */       .group("yellow_dye")
/* 2634 */       .unlockedBy("has_double_plant", (CriterionTriggerInstance)has((ItemLike)Blocks.SUNFLOWER))
/* 2635 */       .save(debug0, "yellow_dye_from_sunflower");
/*      */     
/* 2637 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.DRIED_KELP, 9)
/* 2638 */       .requires((ItemLike)Blocks.DRIED_KELP_BLOCK)
/* 2639 */       .unlockedBy("has_dried_kelp_block", (CriterionTriggerInstance)has((ItemLike)Blocks.DRIED_KELP_BLOCK))
/* 2640 */       .save(debug0);
/*      */     
/* 2642 */     ShapelessRecipeBuilder.shapeless((ItemLike)Blocks.DRIED_KELP_BLOCK)
/* 2643 */       .requires((ItemLike)Items.DRIED_KELP, 9)
/* 2644 */       .unlockedBy("has_dried_kelp", (CriterionTriggerInstance)has((ItemLike)Items.DRIED_KELP))
/* 2645 */       .save(debug0);
/*      */     
/* 2647 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.CONDUIT)
/* 2648 */       .define(Character.valueOf('#'), (ItemLike)Items.NAUTILUS_SHELL)
/* 2649 */       .define(Character.valueOf('X'), (ItemLike)Items.HEART_OF_THE_SEA)
/* 2650 */       .pattern("###")
/* 2651 */       .pattern("#X#")
/* 2652 */       .pattern("###")
/* 2653 */       .unlockedBy("has_nautilus_core", (CriterionTriggerInstance)has((ItemLike)Items.HEART_OF_THE_SEA))
/* 2654 */       .unlockedBy("has_nautilus_shell", (CriterionTriggerInstance)has((ItemLike)Items.NAUTILUS_SHELL))
/* 2655 */       .save(debug0);
/*      */     
/* 2657 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.POLISHED_GRANITE_STAIRS, 4)
/* 2658 */       .define(Character.valueOf('#'), (ItemLike)Blocks.POLISHED_GRANITE)
/* 2659 */       .pattern("#  ")
/* 2660 */       .pattern("## ")
/* 2661 */       .pattern("###")
/* 2662 */       .unlockedBy("has_polished_granite", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_GRANITE))
/* 2663 */       .save(debug0);
/*      */     
/* 2665 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.SMOOTH_RED_SANDSTONE_STAIRS, 4)
/* 2666 */       .define(Character.valueOf('#'), (ItemLike)Blocks.SMOOTH_RED_SANDSTONE)
/* 2667 */       .pattern("#  ")
/* 2668 */       .pattern("## ")
/* 2669 */       .pattern("###")
/* 2670 */       .unlockedBy("has_smooth_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.SMOOTH_RED_SANDSTONE))
/* 2671 */       .save(debug0);
/*      */     
/* 2673 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.MOSSY_STONE_BRICK_STAIRS, 4)
/* 2674 */       .define(Character.valueOf('#'), (ItemLike)Blocks.MOSSY_STONE_BRICKS)
/* 2675 */       .pattern("#  ")
/* 2676 */       .pattern("## ")
/* 2677 */       .pattern("###")
/* 2678 */       .unlockedBy("has_mossy_stone_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.MOSSY_STONE_BRICKS))
/* 2679 */       .save(debug0);
/*      */     
/* 2681 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.POLISHED_DIORITE_STAIRS, 4)
/* 2682 */       .define(Character.valueOf('#'), (ItemLike)Blocks.POLISHED_DIORITE)
/* 2683 */       .pattern("#  ")
/* 2684 */       .pattern("## ")
/* 2685 */       .pattern("###")
/* 2686 */       .unlockedBy("has_polished_diorite", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_DIORITE))
/* 2687 */       .save(debug0);
/*      */     
/* 2689 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.MOSSY_COBBLESTONE_STAIRS, 4)
/* 2690 */       .define(Character.valueOf('#'), (ItemLike)Blocks.MOSSY_COBBLESTONE)
/* 2691 */       .pattern("#  ")
/* 2692 */       .pattern("## ")
/* 2693 */       .pattern("###")
/* 2694 */       .unlockedBy("has_mossy_cobblestone", (CriterionTriggerInstance)has((ItemLike)Blocks.MOSSY_COBBLESTONE))
/* 2695 */       .save(debug0);
/*      */     
/* 2697 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.END_STONE_BRICK_STAIRS, 4)
/* 2698 */       .define(Character.valueOf('#'), (ItemLike)Blocks.END_STONE_BRICKS)
/* 2699 */       .pattern("#  ")
/* 2700 */       .pattern("## ")
/* 2701 */       .pattern("###")
/* 2702 */       .unlockedBy("has_end_stone_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.END_STONE_BRICKS))
/* 2703 */       .save(debug0);
/*      */     
/* 2705 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.STONE_STAIRS, 4)
/* 2706 */       .define(Character.valueOf('#'), (ItemLike)Blocks.STONE)
/* 2707 */       .pattern("#  ")
/* 2708 */       .pattern("## ")
/* 2709 */       .pattern("###")
/* 2710 */       .unlockedBy("has_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.STONE))
/* 2711 */       .save(debug0);
/*      */     
/* 2713 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.SMOOTH_SANDSTONE_STAIRS, 4)
/* 2714 */       .define(Character.valueOf('#'), (ItemLike)Blocks.SMOOTH_SANDSTONE)
/* 2715 */       .pattern("#  ")
/* 2716 */       .pattern("## ")
/* 2717 */       .pattern("###")
/* 2718 */       .unlockedBy("has_smooth_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.SMOOTH_SANDSTONE))
/* 2719 */       .save(debug0);
/*      */     
/* 2721 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.SMOOTH_QUARTZ_STAIRS, 4)
/* 2722 */       .define(Character.valueOf('#'), (ItemLike)Blocks.SMOOTH_QUARTZ)
/* 2723 */       .pattern("#  ")
/* 2724 */       .pattern("## ")
/* 2725 */       .pattern("###")
/* 2726 */       .unlockedBy("has_smooth_quartz", (CriterionTriggerInstance)has((ItemLike)Blocks.SMOOTH_QUARTZ))
/* 2727 */       .save(debug0);
/*      */     
/* 2729 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.GRANITE_STAIRS, 4)
/* 2730 */       .define(Character.valueOf('#'), (ItemLike)Blocks.GRANITE)
/* 2731 */       .pattern("#  ")
/* 2732 */       .pattern("## ")
/* 2733 */       .pattern("###")
/* 2734 */       .unlockedBy("has_granite", (CriterionTriggerInstance)has((ItemLike)Blocks.GRANITE))
/* 2735 */       .save(debug0);
/*      */     
/* 2737 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.ANDESITE_STAIRS, 4)
/* 2738 */       .define(Character.valueOf('#'), (ItemLike)Blocks.ANDESITE)
/* 2739 */       .pattern("#  ")
/* 2740 */       .pattern("## ")
/* 2741 */       .pattern("###")
/* 2742 */       .unlockedBy("has_andesite", (CriterionTriggerInstance)has((ItemLike)Blocks.ANDESITE))
/* 2743 */       .save(debug0);
/*      */     
/* 2745 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.RED_NETHER_BRICK_STAIRS, 4)
/* 2746 */       .define(Character.valueOf('#'), (ItemLike)Blocks.RED_NETHER_BRICKS)
/* 2747 */       .pattern("#  ")
/* 2748 */       .pattern("## ")
/* 2749 */       .pattern("###")
/* 2750 */       .unlockedBy("has_red_nether_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_NETHER_BRICKS))
/* 2751 */       .save(debug0);
/*      */     
/* 2753 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.POLISHED_ANDESITE_STAIRS, 4)
/* 2754 */       .define(Character.valueOf('#'), (ItemLike)Blocks.POLISHED_ANDESITE)
/* 2755 */       .pattern("#  ")
/* 2756 */       .pattern("## ")
/* 2757 */       .pattern("###")
/* 2758 */       .unlockedBy("has_polished_andesite", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_ANDESITE))
/* 2759 */       .save(debug0);
/*      */     
/* 2761 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.DIORITE_STAIRS, 4)
/* 2762 */       .define(Character.valueOf('#'), (ItemLike)Blocks.DIORITE)
/* 2763 */       .pattern("#  ")
/* 2764 */       .pattern("## ")
/* 2765 */       .pattern("###")
/* 2766 */       .unlockedBy("has_diorite", (CriterionTriggerInstance)has((ItemLike)Blocks.DIORITE))
/* 2767 */       .save(debug0);
/*      */     
/* 2769 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.POLISHED_GRANITE_SLAB, 6)
/* 2770 */       .define(Character.valueOf('#'), (ItemLike)Blocks.POLISHED_GRANITE)
/* 2771 */       .pattern("###")
/* 2772 */       .unlockedBy("has_polished_granite", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_GRANITE))
/* 2773 */       .save(debug0);
/*      */     
/* 2775 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.SMOOTH_RED_SANDSTONE_SLAB, 6)
/* 2776 */       .define(Character.valueOf('#'), (ItemLike)Blocks.SMOOTH_RED_SANDSTONE)
/* 2777 */       .pattern("###")
/* 2778 */       .unlockedBy("has_smooth_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.SMOOTH_RED_SANDSTONE))
/* 2779 */       .save(debug0);
/*      */     
/* 2781 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.MOSSY_STONE_BRICK_SLAB, 6)
/* 2782 */       .define(Character.valueOf('#'), (ItemLike)Blocks.MOSSY_STONE_BRICKS)
/* 2783 */       .pattern("###")
/* 2784 */       .unlockedBy("has_mossy_stone_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.MOSSY_STONE_BRICKS))
/* 2785 */       .save(debug0);
/*      */     
/* 2787 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.POLISHED_DIORITE_SLAB, 6)
/* 2788 */       .define(Character.valueOf('#'), (ItemLike)Blocks.POLISHED_DIORITE)
/* 2789 */       .pattern("###")
/* 2790 */       .unlockedBy("has_polished_diorite", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_DIORITE))
/* 2791 */       .save(debug0);
/*      */     
/* 2793 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.MOSSY_COBBLESTONE_SLAB, 6)
/* 2794 */       .define(Character.valueOf('#'), (ItemLike)Blocks.MOSSY_COBBLESTONE)
/* 2795 */       .pattern("###")
/* 2796 */       .unlockedBy("has_mossy_cobblestone", (CriterionTriggerInstance)has((ItemLike)Blocks.MOSSY_COBBLESTONE))
/* 2797 */       .save(debug0);
/*      */     
/* 2799 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.END_STONE_BRICK_SLAB, 6)
/* 2800 */       .define(Character.valueOf('#'), (ItemLike)Blocks.END_STONE_BRICKS)
/* 2801 */       .pattern("###")
/* 2802 */       .unlockedBy("has_end_stone_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.END_STONE_BRICKS))
/* 2803 */       .save(debug0);
/*      */     
/* 2805 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.SMOOTH_SANDSTONE_SLAB, 6)
/* 2806 */       .define(Character.valueOf('#'), (ItemLike)Blocks.SMOOTH_SANDSTONE)
/* 2807 */       .pattern("###")
/* 2808 */       .unlockedBy("has_smooth_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.SMOOTH_SANDSTONE))
/* 2809 */       .save(debug0);
/*      */     
/* 2811 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.SMOOTH_QUARTZ_SLAB, 6)
/* 2812 */       .define(Character.valueOf('#'), (ItemLike)Blocks.SMOOTH_QUARTZ)
/* 2813 */       .pattern("###")
/* 2814 */       .unlockedBy("has_smooth_quartz", (CriterionTriggerInstance)has((ItemLike)Blocks.SMOOTH_QUARTZ))
/* 2815 */       .save(debug0);
/*      */     
/* 2817 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.GRANITE_SLAB, 6)
/* 2818 */       .define(Character.valueOf('#'), (ItemLike)Blocks.GRANITE)
/* 2819 */       .pattern("###")
/* 2820 */       .unlockedBy("has_granite", (CriterionTriggerInstance)has((ItemLike)Blocks.GRANITE))
/* 2821 */       .save(debug0);
/*      */     
/* 2823 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.ANDESITE_SLAB, 6)
/* 2824 */       .define(Character.valueOf('#'), (ItemLike)Blocks.ANDESITE)
/* 2825 */       .pattern("###")
/* 2826 */       .unlockedBy("has_andesite", (CriterionTriggerInstance)has((ItemLike)Blocks.ANDESITE))
/* 2827 */       .save(debug0);
/*      */     
/* 2829 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.RED_NETHER_BRICK_SLAB, 6)
/* 2830 */       .define(Character.valueOf('#'), (ItemLike)Blocks.RED_NETHER_BRICKS)
/* 2831 */       .pattern("###")
/* 2832 */       .unlockedBy("has_red_nether_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_NETHER_BRICKS))
/* 2833 */       .save(debug0);
/*      */     
/* 2835 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.POLISHED_ANDESITE_SLAB, 6)
/* 2836 */       .define(Character.valueOf('#'), (ItemLike)Blocks.POLISHED_ANDESITE)
/* 2837 */       .pattern("###")
/* 2838 */       .unlockedBy("has_polished_andesite", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_ANDESITE))
/* 2839 */       .save(debug0);
/*      */     
/* 2841 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.DIORITE_SLAB, 6)
/* 2842 */       .define(Character.valueOf('#'), (ItemLike)Blocks.DIORITE)
/* 2843 */       .pattern("###")
/* 2844 */       .unlockedBy("has_diorite", (CriterionTriggerInstance)has((ItemLike)Blocks.DIORITE))
/* 2845 */       .save(debug0);
/*      */     
/* 2847 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.BRICK_WALL, 6)
/* 2848 */       .define(Character.valueOf('#'), (ItemLike)Blocks.BRICKS)
/* 2849 */       .pattern("###")
/* 2850 */       .pattern("###")
/* 2851 */       .unlockedBy("has_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.BRICKS))
/* 2852 */       .save(debug0);
/*      */     
/* 2854 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.PRISMARINE_WALL, 6)
/* 2855 */       .define(Character.valueOf('#'), (ItemLike)Blocks.PRISMARINE)
/* 2856 */       .pattern("###")
/* 2857 */       .pattern("###")
/* 2858 */       .unlockedBy("has_prismarine", (CriterionTriggerInstance)has((ItemLike)Blocks.PRISMARINE))
/* 2859 */       .save(debug0);
/*      */     
/* 2861 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.RED_SANDSTONE_WALL, 6)
/* 2862 */       .define(Character.valueOf('#'), (ItemLike)Blocks.RED_SANDSTONE)
/* 2863 */       .pattern("###")
/* 2864 */       .pattern("###")
/* 2865 */       .unlockedBy("has_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_SANDSTONE))
/* 2866 */       .save(debug0);
/*      */     
/* 2868 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.MOSSY_STONE_BRICK_WALL, 6)
/* 2869 */       .define(Character.valueOf('#'), (ItemLike)Blocks.MOSSY_STONE_BRICKS)
/* 2870 */       .pattern("###")
/* 2871 */       .pattern("###")
/* 2872 */       .unlockedBy("has_mossy_stone_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.MOSSY_STONE_BRICKS))
/* 2873 */       .save(debug0);
/*      */     
/* 2875 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.GRANITE_WALL, 6)
/* 2876 */       .define(Character.valueOf('#'), (ItemLike)Blocks.GRANITE)
/* 2877 */       .pattern("###")
/* 2878 */       .pattern("###")
/* 2879 */       .unlockedBy("has_granite", (CriterionTriggerInstance)has((ItemLike)Blocks.GRANITE))
/* 2880 */       .save(debug0);
/*      */     
/* 2882 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.STONE_BRICK_WALL, 6)
/* 2883 */       .define(Character.valueOf('#'), (ItemLike)Blocks.STONE_BRICKS)
/* 2884 */       .pattern("###")
/* 2885 */       .pattern("###")
/* 2886 */       .unlockedBy("has_stone_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.STONE_BRICKS))
/* 2887 */       .save(debug0);
/*      */     
/* 2889 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.NETHER_BRICK_WALL, 6)
/* 2890 */       .define(Character.valueOf('#'), (ItemLike)Blocks.NETHER_BRICKS)
/* 2891 */       .pattern("###")
/* 2892 */       .pattern("###")
/* 2893 */       .unlockedBy("has_nether_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.NETHER_BRICKS))
/* 2894 */       .save(debug0);
/*      */     
/* 2896 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.ANDESITE_WALL, 6)
/* 2897 */       .define(Character.valueOf('#'), (ItemLike)Blocks.ANDESITE)
/* 2898 */       .pattern("###")
/* 2899 */       .pattern("###")
/* 2900 */       .unlockedBy("has_andesite", (CriterionTriggerInstance)has((ItemLike)Blocks.ANDESITE))
/* 2901 */       .save(debug0);
/*      */     
/* 2903 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.RED_NETHER_BRICK_WALL, 6)
/* 2904 */       .define(Character.valueOf('#'), (ItemLike)Blocks.RED_NETHER_BRICKS)
/* 2905 */       .pattern("###")
/* 2906 */       .pattern("###")
/* 2907 */       .unlockedBy("has_red_nether_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_NETHER_BRICKS))
/* 2908 */       .save(debug0);
/*      */     
/* 2910 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.SANDSTONE_WALL, 6)
/* 2911 */       .define(Character.valueOf('#'), (ItemLike)Blocks.SANDSTONE)
/* 2912 */       .pattern("###")
/* 2913 */       .pattern("###")
/* 2914 */       .unlockedBy("has_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.SANDSTONE))
/* 2915 */       .save(debug0);
/*      */     
/* 2917 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.END_STONE_BRICK_WALL, 6)
/* 2918 */       .define(Character.valueOf('#'), (ItemLike)Blocks.END_STONE_BRICKS)
/* 2919 */       .pattern("###")
/* 2920 */       .pattern("###")
/* 2921 */       .unlockedBy("has_end_stone_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.END_STONE_BRICKS))
/* 2922 */       .save(debug0);
/*      */     
/* 2924 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.DIORITE_WALL, 6)
/* 2925 */       .define(Character.valueOf('#'), (ItemLike)Blocks.DIORITE)
/* 2926 */       .pattern("###")
/* 2927 */       .pattern("###")
/* 2928 */       .unlockedBy("has_diorite", (CriterionTriggerInstance)has((ItemLike)Blocks.DIORITE))
/* 2929 */       .save(debug0);
/*      */     
/* 2931 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.CREEPER_BANNER_PATTERN)
/* 2932 */       .requires((ItemLike)Items.PAPER)
/* 2933 */       .requires((ItemLike)Items.CREEPER_HEAD)
/* 2934 */       .unlockedBy("has_creeper_head", (CriterionTriggerInstance)has((ItemLike)Items.CREEPER_HEAD))
/* 2935 */       .save(debug0);
/*      */     
/* 2937 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.SKULL_BANNER_PATTERN)
/* 2938 */       .requires((ItemLike)Items.PAPER)
/* 2939 */       .requires((ItemLike)Items.WITHER_SKELETON_SKULL)
/* 2940 */       .unlockedBy("has_wither_skeleton_skull", (CriterionTriggerInstance)has((ItemLike)Items.WITHER_SKELETON_SKULL))
/* 2941 */       .save(debug0);
/*      */     
/* 2943 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.FLOWER_BANNER_PATTERN)
/* 2944 */       .requires((ItemLike)Items.PAPER)
/* 2945 */       .requires((ItemLike)Blocks.OXEYE_DAISY)
/* 2946 */       .unlockedBy("has_oxeye_daisy", (CriterionTriggerInstance)has((ItemLike)Blocks.OXEYE_DAISY))
/* 2947 */       .save(debug0);
/*      */     
/* 2949 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.MOJANG_BANNER_PATTERN)
/* 2950 */       .requires((ItemLike)Items.PAPER)
/* 2951 */       .requires((ItemLike)Items.ENCHANTED_GOLDEN_APPLE)
/* 2952 */       .unlockedBy("has_enchanted_golden_apple", (CriterionTriggerInstance)has((ItemLike)Items.ENCHANTED_GOLDEN_APPLE))
/* 2953 */       .save(debug0);
/*      */     
/* 2955 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.SCAFFOLDING, 6)
/* 2956 */       .define(Character.valueOf('~'), (ItemLike)Items.STRING)
/* 2957 */       .define(Character.valueOf('I'), (ItemLike)Blocks.BAMBOO)
/* 2958 */       .pattern("I~I")
/* 2959 */       .pattern("I I")
/* 2960 */       .pattern("I I")
/* 2961 */       .unlockedBy("has_bamboo", (CriterionTriggerInstance)has((ItemLike)Blocks.BAMBOO))
/* 2962 */       .save(debug0);
/*      */     
/* 2964 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.GRINDSTONE)
/* 2965 */       .define(Character.valueOf('I'), (ItemLike)Items.STICK)
/* 2966 */       .define(Character.valueOf('-'), (ItemLike)Blocks.STONE_SLAB)
/* 2967 */       .define(Character.valueOf('#'), (Tag<Item>)ItemTags.PLANKS)
/* 2968 */       .pattern("I-I")
/* 2969 */       .pattern("# #")
/* 2970 */       .unlockedBy("has_stone_slab", (CriterionTriggerInstance)has((ItemLike)Blocks.STONE_SLAB))
/* 2971 */       .save(debug0);
/*      */     
/* 2973 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.BLAST_FURNACE)
/* 2974 */       .define(Character.valueOf('#'), (ItemLike)Blocks.SMOOTH_STONE)
/* 2975 */       .define(Character.valueOf('X'), (ItemLike)Blocks.FURNACE)
/* 2976 */       .define(Character.valueOf('I'), (ItemLike)Items.IRON_INGOT)
/* 2977 */       .pattern("III")
/* 2978 */       .pattern("IXI")
/* 2979 */       .pattern("###")
/* 2980 */       .unlockedBy("has_smooth_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.SMOOTH_STONE))
/* 2981 */       .save(debug0);
/*      */     
/* 2983 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.SMOKER)
/* 2984 */       .define(Character.valueOf('#'), (Tag<Item>)ItemTags.LOGS)
/* 2985 */       .define(Character.valueOf('X'), (ItemLike)Blocks.FURNACE)
/* 2986 */       .pattern(" # ")
/* 2987 */       .pattern("#X#")
/* 2988 */       .pattern(" # ")
/* 2989 */       .unlockedBy("has_furnace", (CriterionTriggerInstance)has((ItemLike)Blocks.FURNACE))
/* 2990 */       .save(debug0);
/*      */     
/* 2992 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.CARTOGRAPHY_TABLE)
/* 2993 */       .define(Character.valueOf('#'), (Tag<Item>)ItemTags.PLANKS)
/* 2994 */       .define(Character.valueOf('@'), (ItemLike)Items.PAPER)
/* 2995 */       .pattern("@@")
/* 2996 */       .pattern("##")
/* 2997 */       .pattern("##")
/* 2998 */       .unlockedBy("has_paper", (CriterionTriggerInstance)has((ItemLike)Items.PAPER))
/* 2999 */       .save(debug0);
/*      */     
/* 3001 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.SMITHING_TABLE)
/* 3002 */       .define(Character.valueOf('#'), (Tag<Item>)ItemTags.PLANKS)
/* 3003 */       .define(Character.valueOf('@'), (ItemLike)Items.IRON_INGOT)
/* 3004 */       .pattern("@@")
/* 3005 */       .pattern("##")
/* 3006 */       .pattern("##")
/* 3007 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/* 3008 */       .save(debug0);
/*      */     
/* 3010 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.FLETCHING_TABLE)
/* 3011 */       .define(Character.valueOf('#'), (Tag<Item>)ItemTags.PLANKS)
/* 3012 */       .define(Character.valueOf('@'), (ItemLike)Items.FLINT)
/* 3013 */       .pattern("@@")
/* 3014 */       .pattern("##")
/* 3015 */       .pattern("##")
/* 3016 */       .unlockedBy("has_flint", (CriterionTriggerInstance)has((ItemLike)Items.FLINT))
/* 3017 */       .save(debug0);
/*      */     
/* 3019 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.STONECUTTER)
/* 3020 */       .define(Character.valueOf('I'), (ItemLike)Items.IRON_INGOT)
/* 3021 */       .define(Character.valueOf('#'), (ItemLike)Blocks.STONE)
/* 3022 */       .pattern(" I ")
/* 3023 */       .pattern("###")
/* 3024 */       .unlockedBy("has_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.STONE))
/* 3025 */       .save(debug0);
/*      */     
/* 3027 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.LODESTONE)
/* 3028 */       .define(Character.valueOf('S'), (ItemLike)Items.CHISELED_STONE_BRICKS)
/* 3029 */       .define(Character.valueOf('#'), (ItemLike)Items.NETHERITE_INGOT)
/* 3030 */       .pattern("SSS")
/* 3031 */       .pattern("S#S")
/* 3032 */       .pattern("SSS")
/* 3033 */       .unlockedBy("has_netherite_ingot", (CriterionTriggerInstance)has((ItemLike)Items.NETHERITE_INGOT))
/* 3034 */       .save(debug0);
/*      */     
/* 3036 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.NETHERITE_BLOCK)
/* 3037 */       .define(Character.valueOf('#'), (ItemLike)Items.NETHERITE_INGOT)
/* 3038 */       .pattern("###")
/* 3039 */       .pattern("###")
/* 3040 */       .pattern("###")
/* 3041 */       .unlockedBy("has_netherite_ingot", (CriterionTriggerInstance)has((ItemLike)Items.NETHERITE_INGOT))
/* 3042 */       .save(debug0);
/*      */     
/* 3044 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.NETHERITE_INGOT, 9)
/* 3045 */       .requires((ItemLike)Blocks.NETHERITE_BLOCK)
/* 3046 */       .group("netherite_ingot")
/* 3047 */       .unlockedBy("has_netherite_block", (CriterionTriggerInstance)has((ItemLike)Blocks.NETHERITE_BLOCK))
/* 3048 */       .save(debug0, "netherite_ingot_from_netherite_block");
/*      */     
/* 3050 */     ShapelessRecipeBuilder.shapeless((ItemLike)Items.NETHERITE_INGOT)
/* 3051 */       .requires((ItemLike)Items.NETHERITE_SCRAP, 4)
/* 3052 */       .requires((ItemLike)Items.GOLD_INGOT, 4)
/* 3053 */       .group("netherite_ingot")
/* 3054 */       .unlockedBy("has_netherite_scrap", (CriterionTriggerInstance)has((ItemLike)Items.NETHERITE_SCRAP))
/* 3055 */       .save(debug0);
/*      */     
/* 3057 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.RESPAWN_ANCHOR)
/* 3058 */       .define(Character.valueOf('O'), (ItemLike)Blocks.CRYING_OBSIDIAN)
/* 3059 */       .define(Character.valueOf('G'), (ItemLike)Blocks.GLOWSTONE)
/* 3060 */       .pattern("OOO")
/* 3061 */       .pattern("GGG")
/* 3062 */       .pattern("OOO")
/* 3063 */       .unlockedBy("has_obsidian", (CriterionTriggerInstance)has((ItemLike)Blocks.CRYING_OBSIDIAN))
/* 3064 */       .save(debug0);
/*      */     
/* 3066 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.BLACKSTONE_STAIRS, 4)
/* 3067 */       .define(Character.valueOf('#'), (ItemLike)Blocks.BLACKSTONE)
/* 3068 */       .pattern("#  ")
/* 3069 */       .pattern("## ")
/* 3070 */       .pattern("###")
/* 3071 */       .unlockedBy("has_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.BLACKSTONE))
/* 3072 */       .save(debug0);
/*      */     
/* 3074 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.POLISHED_BLACKSTONE_STAIRS, 4)
/* 3075 */       .define(Character.valueOf('#'), (ItemLike)Blocks.POLISHED_BLACKSTONE)
/* 3076 */       .pattern("#  ")
/* 3077 */       .pattern("## ")
/* 3078 */       .pattern("###")
/* 3079 */       .unlockedBy("has_polished_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_BLACKSTONE))
/* 3080 */       .save(debug0);
/*      */     
/* 3082 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS, 4)
/* 3083 */       .define(Character.valueOf('#'), (ItemLike)Blocks.POLISHED_BLACKSTONE_BRICKS)
/* 3084 */       .pattern("#  ")
/* 3085 */       .pattern("## ")
/* 3086 */       .pattern("###")
/* 3087 */       .unlockedBy("has_polished_blackstone_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_BLACKSTONE_BRICKS))
/* 3088 */       .save(debug0);
/*      */     
/* 3090 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.BLACKSTONE_SLAB, 6)
/* 3091 */       .define(Character.valueOf('#'), (ItemLike)Blocks.BLACKSTONE)
/* 3092 */       .pattern("###")
/* 3093 */       .unlockedBy("has_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.BLACKSTONE))
/* 3094 */       .save(debug0);
/*      */     
/* 3096 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.POLISHED_BLACKSTONE_SLAB, 6)
/* 3097 */       .define(Character.valueOf('#'), (ItemLike)Blocks.POLISHED_BLACKSTONE)
/* 3098 */       .pattern("###")
/* 3099 */       .unlockedBy("has_polished_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_BLACKSTONE))
/* 3100 */       .save(debug0);
/*      */     
/* 3102 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, 6)
/* 3103 */       .define(Character.valueOf('#'), (ItemLike)Blocks.POLISHED_BLACKSTONE_BRICKS)
/* 3104 */       .pattern("###")
/* 3105 */       .unlockedBy("has_polished_blackstone_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_BLACKSTONE_BRICKS))
/* 3106 */       .save(debug0);
/*      */     
/* 3108 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.POLISHED_BLACKSTONE, 4)
/* 3109 */       .define(Character.valueOf('S'), (ItemLike)Blocks.BLACKSTONE)
/* 3110 */       .pattern("SS")
/* 3111 */       .pattern("SS")
/* 3112 */       .unlockedBy("has_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.BLACKSTONE))
/* 3113 */       .save(debug0);
/*      */     
/* 3115 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.POLISHED_BLACKSTONE_BRICKS, 4)
/* 3116 */       .define(Character.valueOf('#'), (ItemLike)Blocks.POLISHED_BLACKSTONE)
/* 3117 */       .pattern("##")
/* 3118 */       .pattern("##")
/* 3119 */       .unlockedBy("has_polished_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_BLACKSTONE))
/* 3120 */       .save(debug0);
/*      */     
/* 3122 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.CHISELED_POLISHED_BLACKSTONE)
/* 3123 */       .define(Character.valueOf('#'), (ItemLike)Blocks.POLISHED_BLACKSTONE_SLAB)
/* 3124 */       .pattern("#")
/* 3125 */       .pattern("#")
/* 3126 */       .unlockedBy("has_polished_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_BLACKSTONE))
/* 3127 */       .save(debug0);
/*      */     
/* 3129 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.BLACKSTONE_WALL, 6)
/* 3130 */       .define(Character.valueOf('#'), (ItemLike)Blocks.BLACKSTONE)
/* 3131 */       .pattern("###")
/* 3132 */       .pattern("###")
/* 3133 */       .unlockedBy("has_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.BLACKSTONE))
/* 3134 */       .save(debug0);
/*      */     
/* 3136 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.POLISHED_BLACKSTONE_WALL, 6)
/* 3137 */       .define(Character.valueOf('#'), (ItemLike)Blocks.POLISHED_BLACKSTONE)
/* 3138 */       .pattern("###")
/* 3139 */       .pattern("###")
/* 3140 */       .unlockedBy("has_polished_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_BLACKSTONE))
/* 3141 */       .save(debug0);
/*      */     
/* 3143 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.POLISHED_BLACKSTONE_BRICK_WALL, 6)
/* 3144 */       .define(Character.valueOf('#'), (ItemLike)Blocks.POLISHED_BLACKSTONE_BRICKS)
/* 3145 */       .pattern("###")
/* 3146 */       .pattern("###")
/* 3147 */       .unlockedBy("has_polished_blackstone_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_BLACKSTONE_BRICKS))
/* 3148 */       .save(debug0);
/*      */     
/* 3150 */     ShapelessRecipeBuilder.shapeless((ItemLike)Blocks.POLISHED_BLACKSTONE_BUTTON)
/* 3151 */       .requires((ItemLike)Blocks.POLISHED_BLACKSTONE)
/* 3152 */       .unlockedBy("has_polished_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_BLACKSTONE))
/* 3153 */       .save(debug0);
/*      */     
/* 3155 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE)
/* 3156 */       .define(Character.valueOf('#'), (ItemLike)Blocks.POLISHED_BLACKSTONE)
/* 3157 */       .pattern("##")
/* 3158 */       .unlockedBy("has_polished_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_BLACKSTONE))
/* 3159 */       .save(debug0);
/*      */     
/* 3161 */     ShapedRecipeBuilder.shaped((ItemLike)Blocks.CHAIN)
/* 3162 */       .define(Character.valueOf('I'), (ItemLike)Items.IRON_INGOT)
/* 3163 */       .define(Character.valueOf('N'), (ItemLike)Items.IRON_NUGGET)
/* 3164 */       .pattern("N")
/* 3165 */       .pattern("I")
/* 3166 */       .pattern("N")
/* 3167 */       .unlockedBy("has_iron_nugget", (CriterionTriggerInstance)has((ItemLike)Items.IRON_NUGGET))
/* 3168 */       .unlockedBy("has_iron_ingot", (CriterionTriggerInstance)has((ItemLike)Items.IRON_INGOT))
/* 3169 */       .save(debug0);
/*      */     
/* 3171 */     SpecialRecipeBuilder.special(RecipeSerializer.ARMOR_DYE)
/* 3172 */       .save(debug0, "armor_dye");
/*      */     
/* 3174 */     SpecialRecipeBuilder.special(RecipeSerializer.BANNER_DUPLICATE)
/* 3175 */       .save(debug0, "banner_duplicate");
/*      */     
/* 3177 */     SpecialRecipeBuilder.special(RecipeSerializer.BOOK_CLONING)
/* 3178 */       .save(debug0, "book_cloning");
/*      */     
/* 3180 */     SpecialRecipeBuilder.special(RecipeSerializer.FIREWORK_ROCKET)
/* 3181 */       .save(debug0, "firework_rocket");
/*      */     
/* 3183 */     SpecialRecipeBuilder.special(RecipeSerializer.FIREWORK_STAR)
/* 3184 */       .save(debug0, "firework_star");
/*      */     
/* 3186 */     SpecialRecipeBuilder.special(RecipeSerializer.FIREWORK_STAR_FADE)
/* 3187 */       .save(debug0, "firework_star_fade");
/*      */     
/* 3189 */     SpecialRecipeBuilder.special(RecipeSerializer.MAP_CLONING)
/* 3190 */       .save(debug0, "map_cloning");
/*      */     
/* 3192 */     SpecialRecipeBuilder.special(RecipeSerializer.MAP_EXTENDING)
/* 3193 */       .save(debug0, "map_extending");
/*      */     
/* 3195 */     SpecialRecipeBuilder.special(RecipeSerializer.REPAIR_ITEM)
/* 3196 */       .save(debug0, "repair_item");
/*      */     
/* 3198 */     SpecialRecipeBuilder.special(RecipeSerializer.SHIELD_DECORATION)
/* 3199 */       .save(debug0, "shield_decoration");
/*      */     
/* 3201 */     SpecialRecipeBuilder.special(RecipeSerializer.SHULKER_BOX_COLORING)
/* 3202 */       .save(debug0, "shulker_box_coloring");
/*      */     
/* 3204 */     SpecialRecipeBuilder.special(RecipeSerializer.TIPPED_ARROW)
/* 3205 */       .save(debug0, "tipped_arrow");
/*      */     
/* 3207 */     SpecialRecipeBuilder.special(RecipeSerializer.SUSPICIOUS_STEW)
/* 3208 */       .save(debug0, "suspicious_stew");
/*      */ 
/*      */     
/* 3211 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Items.POTATO }, ), (ItemLike)Items.BAKED_POTATO, 0.35F, 200)
/* 3212 */       .unlockedBy("has_potato", (CriterionTriggerInstance)has((ItemLike)Items.POTATO))
/* 3213 */       .save(debug0);
/*      */     
/* 3215 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Items.CLAY_BALL }, ), (ItemLike)Items.BRICK, 0.3F, 200)
/* 3216 */       .unlockedBy("has_clay_ball", (CriterionTriggerInstance)has((ItemLike)Items.CLAY_BALL))
/* 3217 */       .save(debug0);
/*      */     
/* 3219 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((Tag)ItemTags.LOGS_THAT_BURN), (ItemLike)Items.CHARCOAL, 0.15F, 200)
/* 3220 */       .unlockedBy("has_log", (CriterionTriggerInstance)has((Tag<Item>)ItemTags.LOGS_THAT_BURN))
/* 3221 */       .save(debug0);
/*      */     
/* 3223 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Items.CHORUS_FRUIT }, ), (ItemLike)Items.POPPED_CHORUS_FRUIT, 0.1F, 200)
/* 3224 */       .unlockedBy("has_chorus_fruit", (CriterionTriggerInstance)has((ItemLike)Items.CHORUS_FRUIT))
/* 3225 */       .save(debug0);
/*      */     
/* 3227 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.COAL_ORE.asItem() }, ), (ItemLike)Items.COAL, 0.1F, 200)
/* 3228 */       .unlockedBy("has_coal_ore", (CriterionTriggerInstance)has((ItemLike)Blocks.COAL_ORE))
/* 3229 */       .save(debug0, "coal_from_smelting");
/*      */     
/* 3231 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Items.BEEF }, ), (ItemLike)Items.COOKED_BEEF, 0.35F, 200)
/* 3232 */       .unlockedBy("has_beef", (CriterionTriggerInstance)has((ItemLike)Items.BEEF))
/* 3233 */       .save(debug0);
/*      */     
/* 3235 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Items.CHICKEN }, ), (ItemLike)Items.COOKED_CHICKEN, 0.35F, 200)
/* 3236 */       .unlockedBy("has_chicken", (CriterionTriggerInstance)has((ItemLike)Items.CHICKEN))
/* 3237 */       .save(debug0);
/*      */     
/* 3239 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Items.COD }, ), (ItemLike)Items.COOKED_COD, 0.35F, 200)
/* 3240 */       .unlockedBy("has_cod", (CriterionTriggerInstance)has((ItemLike)Items.COD))
/* 3241 */       .save(debug0);
/*      */     
/* 3243 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.KELP }, ), (ItemLike)Items.DRIED_KELP, 0.1F, 200)
/* 3244 */       .unlockedBy("has_kelp", (CriterionTriggerInstance)has((ItemLike)Blocks.KELP))
/* 3245 */       .save(debug0, "dried_kelp_from_smelting");
/*      */     
/* 3247 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Items.SALMON }, ), (ItemLike)Items.COOKED_SALMON, 0.35F, 200)
/* 3248 */       .unlockedBy("has_salmon", (CriterionTriggerInstance)has((ItemLike)Items.SALMON))
/* 3249 */       .save(debug0);
/*      */     
/* 3251 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Items.MUTTON }, ), (ItemLike)Items.COOKED_MUTTON, 0.35F, 200)
/* 3252 */       .unlockedBy("has_mutton", (CriterionTriggerInstance)has((ItemLike)Items.MUTTON))
/* 3253 */       .save(debug0);
/*      */     
/* 3255 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Items.PORKCHOP }, ), (ItemLike)Items.COOKED_PORKCHOP, 0.35F, 200)
/* 3256 */       .unlockedBy("has_porkchop", (CriterionTriggerInstance)has((ItemLike)Items.PORKCHOP))
/* 3257 */       .save(debug0);
/*      */     
/* 3259 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Items.RABBIT }, ), (ItemLike)Items.COOKED_RABBIT, 0.35F, 200)
/* 3260 */       .unlockedBy("has_rabbit", (CriterionTriggerInstance)has((ItemLike)Items.RABBIT))
/* 3261 */       .save(debug0);
/*      */     
/* 3263 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.DIAMOND_ORE.asItem() }, ), (ItemLike)Items.DIAMOND, 1.0F, 200)
/* 3264 */       .unlockedBy("has_diamond_ore", (CriterionTriggerInstance)has((ItemLike)Blocks.DIAMOND_ORE))
/* 3265 */       .save(debug0, "diamond_from_smelting");
/*      */     
/* 3267 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.LAPIS_ORE.asItem() }, ), (ItemLike)Items.LAPIS_LAZULI, 0.2F, 200)
/* 3268 */       .unlockedBy("has_lapis_ore", (CriterionTriggerInstance)has((ItemLike)Blocks.LAPIS_ORE))
/* 3269 */       .save(debug0, "lapis_from_smelting");
/*      */     
/* 3271 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.EMERALD_ORE.asItem() }, ), (ItemLike)Items.EMERALD, 1.0F, 200)
/* 3272 */       .unlockedBy("has_emerald_ore", (CriterionTriggerInstance)has((ItemLike)Blocks.EMERALD_ORE))
/* 3273 */       .save(debug0, "emerald_from_smelting");
/*      */     
/* 3275 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((Tag)ItemTags.SAND), (ItemLike)Blocks.GLASS.asItem(), 0.1F, 200)
/* 3276 */       .unlockedBy("has_sand", (CriterionTriggerInstance)has((Tag<Item>)ItemTags.SAND))
/* 3277 */       .save(debug0);
/*      */     
/* 3279 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of((Tag)ItemTags.GOLD_ORES), (ItemLike)Items.GOLD_INGOT, 1.0F, 200)
/* 3280 */       .unlockedBy("has_gold_ore", (CriterionTriggerInstance)has((Tag<Item>)ItemTags.GOLD_ORES))
/* 3281 */       .save(debug0);
/*      */     
/* 3283 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.SEA_PICKLE.asItem() }, ), (ItemLike)Items.LIME_DYE, 0.1F, 200)
/* 3284 */       .unlockedBy("has_sea_pickle", (CriterionTriggerInstance)has((ItemLike)Blocks.SEA_PICKLE))
/* 3285 */       .save(debug0, "lime_dye_from_smelting");
/*      */     
/* 3287 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.CACTUS.asItem() }, ), (ItemLike)Items.GREEN_DYE, 1.0F, 200)
/* 3288 */       .unlockedBy("has_cactus", (CriterionTriggerInstance)has((ItemLike)Blocks.CACTUS))
/* 3289 */       .save(debug0);
/*      */     
/* 3291 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Items.GOLDEN_PICKAXE, (ItemLike)Items.GOLDEN_SHOVEL, (ItemLike)Items.GOLDEN_AXE, (ItemLike)Items.GOLDEN_HOE, (ItemLike)Items.GOLDEN_SWORD, (ItemLike)Items.GOLDEN_HELMET, (ItemLike)Items.GOLDEN_CHESTPLATE, (ItemLike)Items.GOLDEN_LEGGINGS, (ItemLike)Items.GOLDEN_BOOTS, (ItemLike)Items.GOLDEN_HORSE_ARMOR }, ), (ItemLike)Items.GOLD_NUGGET, 0.1F, 200)
/* 3292 */       .unlockedBy("has_golden_pickaxe", (CriterionTriggerInstance)has((ItemLike)Items.GOLDEN_PICKAXE))
/* 3293 */       .unlockedBy("has_golden_shovel", (CriterionTriggerInstance)has((ItemLike)Items.GOLDEN_SHOVEL))
/* 3294 */       .unlockedBy("has_golden_axe", (CriterionTriggerInstance)has((ItemLike)Items.GOLDEN_AXE))
/* 3295 */       .unlockedBy("has_golden_hoe", (CriterionTriggerInstance)has((ItemLike)Items.GOLDEN_HOE))
/* 3296 */       .unlockedBy("has_golden_sword", (CriterionTriggerInstance)has((ItemLike)Items.GOLDEN_SWORD))
/* 3297 */       .unlockedBy("has_golden_helmet", (CriterionTriggerInstance)has((ItemLike)Items.GOLDEN_HELMET))
/* 3298 */       .unlockedBy("has_golden_chestplate", (CriterionTriggerInstance)has((ItemLike)Items.GOLDEN_CHESTPLATE))
/* 3299 */       .unlockedBy("has_golden_leggings", (CriterionTriggerInstance)has((ItemLike)Items.GOLDEN_LEGGINGS))
/* 3300 */       .unlockedBy("has_golden_boots", (CriterionTriggerInstance)has((ItemLike)Items.GOLDEN_BOOTS))
/* 3301 */       .unlockedBy("has_golden_horse_armor", (CriterionTriggerInstance)has((ItemLike)Items.GOLDEN_HORSE_ARMOR))
/* 3302 */       .save(debug0, "gold_nugget_from_smelting");
/*      */     
/* 3304 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Items.IRON_PICKAXE, (ItemLike)Items.IRON_SHOVEL, (ItemLike)Items.IRON_AXE, (ItemLike)Items.IRON_HOE, (ItemLike)Items.IRON_SWORD, (ItemLike)Items.IRON_HELMET, (ItemLike)Items.IRON_CHESTPLATE, (ItemLike)Items.IRON_LEGGINGS, (ItemLike)Items.IRON_BOOTS, (ItemLike)Items.IRON_HORSE_ARMOR, (ItemLike)Items.CHAINMAIL_HELMET, (ItemLike)Items.CHAINMAIL_CHESTPLATE, (ItemLike)Items.CHAINMAIL_LEGGINGS, (ItemLike)Items.CHAINMAIL_BOOTS }, ), (ItemLike)Items.IRON_NUGGET, 0.1F, 200)
/* 3305 */       .unlockedBy("has_iron_pickaxe", (CriterionTriggerInstance)has((ItemLike)Items.IRON_PICKAXE))
/* 3306 */       .unlockedBy("has_iron_shovel", (CriterionTriggerInstance)has((ItemLike)Items.IRON_SHOVEL))
/* 3307 */       .unlockedBy("has_iron_axe", (CriterionTriggerInstance)has((ItemLike)Items.IRON_AXE))
/* 3308 */       .unlockedBy("has_iron_hoe", (CriterionTriggerInstance)has((ItemLike)Items.IRON_HOE))
/* 3309 */       .unlockedBy("has_iron_sword", (CriterionTriggerInstance)has((ItemLike)Items.IRON_SWORD))
/* 3310 */       .unlockedBy("has_iron_helmet", (CriterionTriggerInstance)has((ItemLike)Items.IRON_HELMET))
/* 3311 */       .unlockedBy("has_iron_chestplate", (CriterionTriggerInstance)has((ItemLike)Items.IRON_CHESTPLATE))
/* 3312 */       .unlockedBy("has_iron_leggings", (CriterionTriggerInstance)has((ItemLike)Items.IRON_LEGGINGS))
/* 3313 */       .unlockedBy("has_iron_boots", (CriterionTriggerInstance)has((ItemLike)Items.IRON_BOOTS))
/* 3314 */       .unlockedBy("has_iron_horse_armor", (CriterionTriggerInstance)has((ItemLike)Items.IRON_HORSE_ARMOR))
/* 3315 */       .unlockedBy("has_chainmail_helmet", (CriterionTriggerInstance)has((ItemLike)Items.CHAINMAIL_HELMET))
/* 3316 */       .unlockedBy("has_chainmail_chestplate", (CriterionTriggerInstance)has((ItemLike)Items.CHAINMAIL_CHESTPLATE))
/* 3317 */       .unlockedBy("has_chainmail_leggings", (CriterionTriggerInstance)has((ItemLike)Items.CHAINMAIL_LEGGINGS))
/* 3318 */       .unlockedBy("has_chainmail_boots", (CriterionTriggerInstance)has((ItemLike)Items.CHAINMAIL_BOOTS))
/* 3319 */       .save(debug0, "iron_nugget_from_smelting");
/*      */     
/* 3321 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.IRON_ORE.asItem() }, ), (ItemLike)Items.IRON_INGOT, 0.7F, 200)
/* 3322 */       .unlockedBy("has_iron_ore", (CriterionTriggerInstance)has((ItemLike)Blocks.IRON_ORE.asItem()))
/* 3323 */       .save(debug0);
/*      */     
/* 3325 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.CLAY }, ), (ItemLike)Blocks.TERRACOTTA.asItem(), 0.35F, 200)
/* 3326 */       .unlockedBy("has_clay_block", (CriterionTriggerInstance)has((ItemLike)Blocks.CLAY))
/* 3327 */       .save(debug0);
/*      */     
/* 3329 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.NETHERRACK }, ), (ItemLike)Items.NETHER_BRICK, 0.1F, 200)
/* 3330 */       .unlockedBy("has_netherrack", (CriterionTriggerInstance)has((ItemLike)Blocks.NETHERRACK))
/* 3331 */       .save(debug0);
/*      */     
/* 3333 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.NETHER_QUARTZ_ORE }, ), (ItemLike)Items.QUARTZ, 0.2F, 200)
/* 3334 */       .unlockedBy("has_nether_quartz_ore", (CriterionTriggerInstance)has((ItemLike)Blocks.NETHER_QUARTZ_ORE))
/* 3335 */       .save(debug0);
/*      */     
/* 3337 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.REDSTONE_ORE }, ), (ItemLike)Items.REDSTONE, 0.7F, 200)
/* 3338 */       .unlockedBy("has_redstone_ore", (CriterionTriggerInstance)has((ItemLike)Blocks.REDSTONE_ORE))
/* 3339 */       .save(debug0, "redstone_from_smelting");
/*      */     
/* 3341 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.WET_SPONGE }, ), (ItemLike)Blocks.SPONGE.asItem(), 0.15F, 200)
/* 3342 */       .unlockedBy("has_wet_sponge", (CriterionTriggerInstance)has((ItemLike)Blocks.WET_SPONGE))
/* 3343 */       .save(debug0);
/*      */     
/* 3345 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.COBBLESTONE }, ), (ItemLike)Blocks.STONE.asItem(), 0.1F, 200)
/* 3346 */       .unlockedBy("has_cobblestone", (CriterionTriggerInstance)has((ItemLike)Blocks.COBBLESTONE))
/* 3347 */       .save(debug0);
/*      */     
/* 3349 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.STONE }, ), (ItemLike)Blocks.SMOOTH_STONE.asItem(), 0.1F, 200)
/* 3350 */       .unlockedBy("has_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.STONE))
/* 3351 */       .save(debug0);
/*      */     
/* 3353 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.SANDSTONE }, ), (ItemLike)Blocks.SMOOTH_SANDSTONE.asItem(), 0.1F, 200)
/* 3354 */       .unlockedBy("has_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.SANDSTONE))
/* 3355 */       .save(debug0);
/*      */     
/* 3357 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.RED_SANDSTONE }, ), (ItemLike)Blocks.SMOOTH_RED_SANDSTONE.asItem(), 0.1F, 200)
/* 3358 */       .unlockedBy("has_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_SANDSTONE))
/* 3359 */       .save(debug0);
/*      */     
/* 3361 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.QUARTZ_BLOCK }, ), (ItemLike)Blocks.SMOOTH_QUARTZ.asItem(), 0.1F, 200)
/* 3362 */       .unlockedBy("has_quartz_block", (CriterionTriggerInstance)has((ItemLike)Blocks.QUARTZ_BLOCK))
/* 3363 */       .save(debug0);
/*      */     
/* 3365 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.STONE_BRICKS }, ), (ItemLike)Blocks.CRACKED_STONE_BRICKS.asItem(), 0.1F, 200)
/* 3366 */       .unlockedBy("has_stone_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.STONE_BRICKS))
/* 3367 */       .save(debug0);
/*      */     
/* 3369 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.BLACK_TERRACOTTA }, ), (ItemLike)Blocks.BLACK_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 3370 */       .unlockedBy("has_black_terracotta", (CriterionTriggerInstance)has((ItemLike)Blocks.BLACK_TERRACOTTA))
/* 3371 */       .save(debug0);
/*      */     
/* 3373 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.BLUE_TERRACOTTA }, ), (ItemLike)Blocks.BLUE_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 3374 */       .unlockedBy("has_blue_terracotta", (CriterionTriggerInstance)has((ItemLike)Blocks.BLUE_TERRACOTTA))
/* 3375 */       .save(debug0);
/*      */     
/* 3377 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.BROWN_TERRACOTTA }, ), (ItemLike)Blocks.BROWN_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 3378 */       .unlockedBy("has_brown_terracotta", (CriterionTriggerInstance)has((ItemLike)Blocks.BROWN_TERRACOTTA))
/* 3379 */       .save(debug0);
/*      */     
/* 3381 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.CYAN_TERRACOTTA }, ), (ItemLike)Blocks.CYAN_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 3382 */       .unlockedBy("has_cyan_terracotta", (CriterionTriggerInstance)has((ItemLike)Blocks.CYAN_TERRACOTTA))
/* 3383 */       .save(debug0);
/*      */     
/* 3385 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.GRAY_TERRACOTTA }, ), (ItemLike)Blocks.GRAY_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 3386 */       .unlockedBy("has_gray_terracotta", (CriterionTriggerInstance)has((ItemLike)Blocks.GRAY_TERRACOTTA))
/* 3387 */       .save(debug0);
/*      */     
/* 3389 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.GREEN_TERRACOTTA }, ), (ItemLike)Blocks.GREEN_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 3390 */       .unlockedBy("has_green_terracotta", (CriterionTriggerInstance)has((ItemLike)Blocks.GREEN_TERRACOTTA))
/* 3391 */       .save(debug0);
/*      */     
/* 3393 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.LIGHT_BLUE_TERRACOTTA }, ), (ItemLike)Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 3394 */       .unlockedBy("has_light_blue_terracotta", (CriterionTriggerInstance)has((ItemLike)Blocks.LIGHT_BLUE_TERRACOTTA))
/* 3395 */       .save(debug0);
/*      */     
/* 3397 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.LIGHT_GRAY_TERRACOTTA }, ), (ItemLike)Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 3398 */       .unlockedBy("has_light_gray_terracotta", (CriterionTriggerInstance)has((ItemLike)Blocks.LIGHT_GRAY_TERRACOTTA))
/* 3399 */       .save(debug0);
/*      */     
/* 3401 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.LIME_TERRACOTTA }, ), (ItemLike)Blocks.LIME_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 3402 */       .unlockedBy("has_lime_terracotta", (CriterionTriggerInstance)has((ItemLike)Blocks.LIME_TERRACOTTA))
/* 3403 */       .save(debug0);
/*      */     
/* 3405 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.MAGENTA_TERRACOTTA }, ), (ItemLike)Blocks.MAGENTA_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 3406 */       .unlockedBy("has_magenta_terracotta", (CriterionTriggerInstance)has((ItemLike)Blocks.MAGENTA_TERRACOTTA))
/* 3407 */       .save(debug0);
/*      */     
/* 3409 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.ORANGE_TERRACOTTA }, ), (ItemLike)Blocks.ORANGE_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 3410 */       .unlockedBy("has_orange_terracotta", (CriterionTriggerInstance)has((ItemLike)Blocks.ORANGE_TERRACOTTA))
/* 3411 */       .save(debug0);
/*      */     
/* 3413 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.PINK_TERRACOTTA }, ), (ItemLike)Blocks.PINK_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 3414 */       .unlockedBy("has_pink_terracotta", (CriterionTriggerInstance)has((ItemLike)Blocks.PINK_TERRACOTTA))
/* 3415 */       .save(debug0);
/*      */     
/* 3417 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.PURPLE_TERRACOTTA }, ), (ItemLike)Blocks.PURPLE_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 3418 */       .unlockedBy("has_purple_terracotta", (CriterionTriggerInstance)has((ItemLike)Blocks.PURPLE_TERRACOTTA))
/* 3419 */       .save(debug0);
/*      */     
/* 3421 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.RED_TERRACOTTA }, ), (ItemLike)Blocks.RED_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 3422 */       .unlockedBy("has_red_terracotta", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_TERRACOTTA))
/* 3423 */       .save(debug0);
/*      */     
/* 3425 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.WHITE_TERRACOTTA }, ), (ItemLike)Blocks.WHITE_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 3426 */       .unlockedBy("has_white_terracotta", (CriterionTriggerInstance)has((ItemLike)Blocks.WHITE_TERRACOTTA))
/* 3427 */       .save(debug0);
/*      */     
/* 3429 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.YELLOW_TERRACOTTA }, ), (ItemLike)Blocks.YELLOW_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 3430 */       .unlockedBy("has_yellow_terracotta", (CriterionTriggerInstance)has((ItemLike)Blocks.YELLOW_TERRACOTTA))
/* 3431 */       .save(debug0);
/*      */     
/* 3433 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.ANCIENT_DEBRIS }, ), (ItemLike)Items.NETHERITE_SCRAP, 2.0F, 200)
/* 3434 */       .unlockedBy("has_ancient_debris", (CriterionTriggerInstance)has((ItemLike)Blocks.ANCIENT_DEBRIS))
/* 3435 */       .save(debug0);
/*      */     
/* 3437 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.POLISHED_BLACKSTONE_BRICKS }, ), (ItemLike)Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.asItem(), 0.1F, 200)
/* 3438 */       .unlockedBy("has_blackstone_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_BLACKSTONE_BRICKS))
/* 3439 */       .save(debug0);
/*      */     
/* 3441 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.NETHER_BRICKS }, ), (ItemLike)Blocks.CRACKED_NETHER_BRICKS.asItem(), 0.1F, 200)
/* 3442 */       .unlockedBy("has_nether_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.NETHER_BRICKS))
/* 3443 */       .save(debug0);
/*      */ 
/*      */     
/* 3446 */     SimpleCookingRecipeBuilder.blasting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.IRON_ORE.asItem() }, ), (ItemLike)Items.IRON_INGOT, 0.7F, 100)
/* 3447 */       .unlockedBy("has_iron_ore", (CriterionTriggerInstance)has((ItemLike)Blocks.IRON_ORE.asItem()))
/* 3448 */       .save(debug0, "iron_ingot_from_blasting");
/*      */     
/* 3450 */     SimpleCookingRecipeBuilder.blasting(Ingredient.of((Tag)ItemTags.GOLD_ORES), (ItemLike)Items.GOLD_INGOT, 1.0F, 100)
/* 3451 */       .unlockedBy("has_gold_ore", (CriterionTriggerInstance)has((Tag<Item>)ItemTags.GOLD_ORES))
/* 3452 */       .save(debug0, "gold_ingot_from_blasting");
/*      */     
/* 3454 */     SimpleCookingRecipeBuilder.blasting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.DIAMOND_ORE.asItem() }, ), (ItemLike)Items.DIAMOND, 1.0F, 100)
/* 3455 */       .unlockedBy("has_diamond_ore", (CriterionTriggerInstance)has((ItemLike)Blocks.DIAMOND_ORE))
/* 3456 */       .save(debug0, "diamond_from_blasting");
/*      */     
/* 3458 */     SimpleCookingRecipeBuilder.blasting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.LAPIS_ORE.asItem() }, ), (ItemLike)Items.LAPIS_LAZULI, 0.2F, 100)
/* 3459 */       .unlockedBy("has_lapis_ore", (CriterionTriggerInstance)has((ItemLike)Blocks.LAPIS_ORE))
/* 3460 */       .save(debug0, "lapis_from_blasting");
/*      */     
/* 3462 */     SimpleCookingRecipeBuilder.blasting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.REDSTONE_ORE }, ), (ItemLike)Items.REDSTONE, 0.7F, 100)
/* 3463 */       .unlockedBy("has_redstone_ore", (CriterionTriggerInstance)has((ItemLike)Blocks.REDSTONE_ORE))
/* 3464 */       .save(debug0, "redstone_from_blasting");
/*      */     
/* 3466 */     SimpleCookingRecipeBuilder.blasting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.COAL_ORE.asItem() }, ), (ItemLike)Items.COAL, 0.1F, 100)
/* 3467 */       .unlockedBy("has_coal_ore", (CriterionTriggerInstance)has((ItemLike)Blocks.COAL_ORE))
/* 3468 */       .save(debug0, "coal_from_blasting");
/*      */     
/* 3470 */     SimpleCookingRecipeBuilder.blasting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.EMERALD_ORE.asItem() }, ), (ItemLike)Items.EMERALD, 1.0F, 100)
/* 3471 */       .unlockedBy("has_emerald_ore", (CriterionTriggerInstance)has((ItemLike)Blocks.EMERALD_ORE))
/* 3472 */       .save(debug0, "emerald_from_blasting");
/*      */     
/* 3474 */     SimpleCookingRecipeBuilder.blasting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.NETHER_QUARTZ_ORE }, ), (ItemLike)Items.QUARTZ, 0.2F, 100)
/* 3475 */       .unlockedBy("has_nether_quartz_ore", (CriterionTriggerInstance)has((ItemLike)Blocks.NETHER_QUARTZ_ORE))
/* 3476 */       .save(debug0, "quartz_from_blasting");
/*      */     
/* 3478 */     SimpleCookingRecipeBuilder.blasting(Ingredient.of(new ItemLike[] { (ItemLike)Items.GOLDEN_PICKAXE, (ItemLike)Items.GOLDEN_SHOVEL, (ItemLike)Items.GOLDEN_AXE, (ItemLike)Items.GOLDEN_HOE, (ItemLike)Items.GOLDEN_SWORD, (ItemLike)Items.GOLDEN_HELMET, (ItemLike)Items.GOLDEN_CHESTPLATE, (ItemLike)Items.GOLDEN_LEGGINGS, (ItemLike)Items.GOLDEN_BOOTS, (ItemLike)Items.GOLDEN_HORSE_ARMOR }, ), (ItemLike)Items.GOLD_NUGGET, 0.1F, 100)
/* 3479 */       .unlockedBy("has_golden_pickaxe", (CriterionTriggerInstance)has((ItemLike)Items.GOLDEN_PICKAXE))
/* 3480 */       .unlockedBy("has_golden_shovel", (CriterionTriggerInstance)has((ItemLike)Items.GOLDEN_SHOVEL))
/* 3481 */       .unlockedBy("has_golden_axe", (CriterionTriggerInstance)has((ItemLike)Items.GOLDEN_AXE))
/* 3482 */       .unlockedBy("has_golden_hoe", (CriterionTriggerInstance)has((ItemLike)Items.GOLDEN_HOE))
/* 3483 */       .unlockedBy("has_golden_sword", (CriterionTriggerInstance)has((ItemLike)Items.GOLDEN_SWORD))
/* 3484 */       .unlockedBy("has_golden_helmet", (CriterionTriggerInstance)has((ItemLike)Items.GOLDEN_HELMET))
/* 3485 */       .unlockedBy("has_golden_chestplate", (CriterionTriggerInstance)has((ItemLike)Items.GOLDEN_CHESTPLATE))
/* 3486 */       .unlockedBy("has_golden_leggings", (CriterionTriggerInstance)has((ItemLike)Items.GOLDEN_LEGGINGS))
/* 3487 */       .unlockedBy("has_golden_boots", (CriterionTriggerInstance)has((ItemLike)Items.GOLDEN_BOOTS))
/* 3488 */       .unlockedBy("has_golden_horse_armor", (CriterionTriggerInstance)has((ItemLike)Items.GOLDEN_HORSE_ARMOR))
/* 3489 */       .save(debug0, "gold_nugget_from_blasting");
/*      */     
/* 3491 */     SimpleCookingRecipeBuilder.blasting(Ingredient.of(new ItemLike[] { (ItemLike)Items.IRON_PICKAXE, (ItemLike)Items.IRON_SHOVEL, (ItemLike)Items.IRON_AXE, (ItemLike)Items.IRON_HOE, (ItemLike)Items.IRON_SWORD, (ItemLike)Items.IRON_HELMET, (ItemLike)Items.IRON_CHESTPLATE, (ItemLike)Items.IRON_LEGGINGS, (ItemLike)Items.IRON_BOOTS, (ItemLike)Items.IRON_HORSE_ARMOR, (ItemLike)Items.CHAINMAIL_HELMET, (ItemLike)Items.CHAINMAIL_CHESTPLATE, (ItemLike)Items.CHAINMAIL_LEGGINGS, (ItemLike)Items.CHAINMAIL_BOOTS }, ), (ItemLike)Items.IRON_NUGGET, 0.1F, 100)
/* 3492 */       .unlockedBy("has_iron_pickaxe", (CriterionTriggerInstance)has((ItemLike)Items.IRON_PICKAXE))
/* 3493 */       .unlockedBy("has_iron_shovel", (CriterionTriggerInstance)has((ItemLike)Items.IRON_SHOVEL))
/* 3494 */       .unlockedBy("has_iron_axe", (CriterionTriggerInstance)has((ItemLike)Items.IRON_AXE))
/* 3495 */       .unlockedBy("has_iron_hoe", (CriterionTriggerInstance)has((ItemLike)Items.IRON_HOE))
/* 3496 */       .unlockedBy("has_iron_sword", (CriterionTriggerInstance)has((ItemLike)Items.IRON_SWORD))
/* 3497 */       .unlockedBy("has_iron_helmet", (CriterionTriggerInstance)has((ItemLike)Items.IRON_HELMET))
/* 3498 */       .unlockedBy("has_iron_chestplate", (CriterionTriggerInstance)has((ItemLike)Items.IRON_CHESTPLATE))
/* 3499 */       .unlockedBy("has_iron_leggings", (CriterionTriggerInstance)has((ItemLike)Items.IRON_LEGGINGS))
/* 3500 */       .unlockedBy("has_iron_boots", (CriterionTriggerInstance)has((ItemLike)Items.IRON_BOOTS))
/* 3501 */       .unlockedBy("has_iron_horse_armor", (CriterionTriggerInstance)has((ItemLike)Items.IRON_HORSE_ARMOR))
/* 3502 */       .unlockedBy("has_chainmail_helmet", (CriterionTriggerInstance)has((ItemLike)Items.CHAINMAIL_HELMET))
/* 3503 */       .unlockedBy("has_chainmail_chestplate", (CriterionTriggerInstance)has((ItemLike)Items.CHAINMAIL_CHESTPLATE))
/* 3504 */       .unlockedBy("has_chainmail_leggings", (CriterionTriggerInstance)has((ItemLike)Items.CHAINMAIL_LEGGINGS))
/* 3505 */       .unlockedBy("has_chainmail_boots", (CriterionTriggerInstance)has((ItemLike)Items.CHAINMAIL_BOOTS))
/* 3506 */       .save(debug0, "iron_nugget_from_blasting");
/*      */     
/* 3508 */     SimpleCookingRecipeBuilder.blasting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.ANCIENT_DEBRIS }, ), (ItemLike)Items.NETHERITE_SCRAP, 2.0F, 100)
/* 3509 */       .unlockedBy("has_ancient_debris", (CriterionTriggerInstance)has((ItemLike)Blocks.ANCIENT_DEBRIS))
/* 3510 */       .save(debug0, "netherite_scrap_from_blasting");
/*      */     
/* 3512 */     cookRecipes(debug0, "smoking", RecipeSerializer.SMOKING_RECIPE, 100);
/* 3513 */     cookRecipes(debug0, "campfire_cooking", RecipeSerializer.CAMPFIRE_COOKING_RECIPE, 600);
/*      */ 
/*      */     
/* 3516 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.STONE }, ), (ItemLike)Blocks.STONE_SLAB, 2)
/* 3517 */       .unlocks("has_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.STONE))
/* 3518 */       .save(debug0, "stone_slab_from_stone_stonecutting");
/*      */     
/* 3520 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.STONE }, ), (ItemLike)Blocks.STONE_STAIRS)
/* 3521 */       .unlocks("has_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.STONE))
/* 3522 */       .save(debug0, "stone_stairs_from_stone_stonecutting");
/*      */     
/* 3524 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.STONE }, ), (ItemLike)Blocks.STONE_BRICKS)
/* 3525 */       .unlocks("has_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.STONE))
/* 3526 */       .save(debug0, "stone_bricks_from_stone_stonecutting");
/*      */     
/* 3528 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.STONE }, ), (ItemLike)Blocks.STONE_BRICK_SLAB, 2)
/* 3529 */       .unlocks("has_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.STONE))
/* 3530 */       .save(debug0, "stone_brick_slab_from_stone_stonecutting");
/*      */     
/* 3532 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.STONE }, ), (ItemLike)Blocks.STONE_BRICK_STAIRS)
/* 3533 */       .unlocks("has_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.STONE))
/* 3534 */       .save(debug0, "stone_brick_stairs_from_stone_stonecutting");
/*      */     
/* 3536 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.STONE }, ), (ItemLike)Blocks.CHISELED_STONE_BRICKS)
/* 3537 */       .unlocks("has_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.STONE))
/* 3538 */       .save(debug0, "chiseled_stone_bricks_stone_from_stonecutting");
/*      */     
/* 3540 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.STONE }, ), (ItemLike)Blocks.STONE_BRICK_WALL)
/* 3541 */       .unlocks("has_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.STONE))
/* 3542 */       .save(debug0, "stone_brick_walls_from_stone_stonecutting");
/*      */     
/* 3544 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.SANDSTONE }, ), (ItemLike)Blocks.CUT_SANDSTONE)
/* 3545 */       .unlocks("has_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.SANDSTONE))
/* 3546 */       .save(debug0, "cut_sandstone_from_sandstone_stonecutting");
/*      */     
/* 3548 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.SANDSTONE }, ), (ItemLike)Blocks.SANDSTONE_SLAB, 2)
/* 3549 */       .unlocks("has_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.SANDSTONE))
/* 3550 */       .save(debug0, "sandstone_slab_from_sandstone_stonecutting");
/*      */     
/* 3552 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.SANDSTONE }, ), (ItemLike)Blocks.CUT_SANDSTONE_SLAB, 2)
/* 3553 */       .unlocks("has_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.SANDSTONE))
/* 3554 */       .save(debug0, "cut_sandstone_slab_from_sandstone_stonecutting");
/*      */     
/* 3556 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.CUT_SANDSTONE }, ), (ItemLike)Blocks.CUT_SANDSTONE_SLAB, 2)
/* 3557 */       .unlocks("has_cut_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.SANDSTONE))
/* 3558 */       .save(debug0, "cut_sandstone_slab_from_cut_sandstone_stonecutting");
/*      */     
/* 3560 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.SANDSTONE }, ), (ItemLike)Blocks.SANDSTONE_STAIRS)
/* 3561 */       .unlocks("has_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.SANDSTONE))
/* 3562 */       .save(debug0, "sandstone_stairs_from_sandstone_stonecutting");
/*      */     
/* 3564 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.SANDSTONE }, ), (ItemLike)Blocks.SANDSTONE_WALL)
/* 3565 */       .unlocks("has_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.SANDSTONE))
/* 3566 */       .save(debug0, "sandstone_wall_from_sandstone_stonecutting");
/*      */     
/* 3568 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.SANDSTONE }, ), (ItemLike)Blocks.CHISELED_SANDSTONE)
/* 3569 */       .unlocks("has_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.SANDSTONE))
/* 3570 */       .save(debug0, "chiseled_sandstone_from_sandstone_stonecutting");
/*      */     
/* 3572 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.RED_SANDSTONE }, ), (ItemLike)Blocks.CUT_RED_SANDSTONE)
/* 3573 */       .unlocks("has_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_SANDSTONE))
/* 3574 */       .save(debug0, "cut_red_sandstone_from_red_sandstone_stonecutting");
/*      */     
/* 3576 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.RED_SANDSTONE }, ), (ItemLike)Blocks.RED_SANDSTONE_SLAB, 2)
/* 3577 */       .unlocks("has_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_SANDSTONE))
/* 3578 */       .save(debug0, "red_sandstone_slab_from_red_sandstone_stonecutting");
/*      */     
/* 3580 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.RED_SANDSTONE }, ), (ItemLike)Blocks.CUT_RED_SANDSTONE_SLAB, 2)
/* 3581 */       .unlocks("has_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_SANDSTONE))
/* 3582 */       .save(debug0, "cut_red_sandstone_slab_from_red_sandstone_stonecutting");
/*      */     
/* 3584 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.CUT_RED_SANDSTONE }, ), (ItemLike)Blocks.CUT_RED_SANDSTONE_SLAB, 2)
/* 3585 */       .unlocks("has_cut_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_SANDSTONE))
/* 3586 */       .save(debug0, "cut_red_sandstone_slab_from_cut_red_sandstone_stonecutting");
/*      */     
/* 3588 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.RED_SANDSTONE }, ), (ItemLike)Blocks.RED_SANDSTONE_STAIRS)
/* 3589 */       .unlocks("has_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_SANDSTONE))
/* 3590 */       .save(debug0, "red_sandstone_stairs_from_red_sandstone_stonecutting");
/*      */     
/* 3592 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.RED_SANDSTONE }, ), (ItemLike)Blocks.RED_SANDSTONE_WALL)
/* 3593 */       .unlocks("has_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_SANDSTONE))
/* 3594 */       .save(debug0, "red_sandstone_wall_from_red_sandstone_stonecutting");
/*      */     
/* 3596 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.RED_SANDSTONE }, ), (ItemLike)Blocks.CHISELED_RED_SANDSTONE)
/* 3597 */       .unlocks("has_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_SANDSTONE))
/* 3598 */       .save(debug0, "chiseled_red_sandstone_from_red_sandstone_stonecutting");
/*      */     
/* 3600 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.QUARTZ_BLOCK }, ), (ItemLike)Blocks.QUARTZ_SLAB, 2)
/* 3601 */       .unlocks("has_quartz_block", (CriterionTriggerInstance)has((ItemLike)Blocks.QUARTZ_BLOCK))
/* 3602 */       .save(debug0, "quartz_slab_from_stonecutting");
/*      */     
/* 3604 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.QUARTZ_BLOCK }, ), (ItemLike)Blocks.QUARTZ_STAIRS)
/* 3605 */       .unlocks("has_quartz_block", (CriterionTriggerInstance)has((ItemLike)Blocks.QUARTZ_BLOCK))
/* 3606 */       .save(debug0, "quartz_stairs_from_quartz_block_stonecutting");
/*      */     
/* 3608 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.QUARTZ_BLOCK }, ), (ItemLike)Blocks.QUARTZ_PILLAR)
/* 3609 */       .unlocks("has_quartz_block", (CriterionTriggerInstance)has((ItemLike)Blocks.QUARTZ_BLOCK))
/* 3610 */       .save(debug0, "quartz_pillar_from_quartz_block_stonecutting");
/*      */     
/* 3612 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.QUARTZ_BLOCK }, ), (ItemLike)Blocks.CHISELED_QUARTZ_BLOCK)
/* 3613 */       .unlocks("has_quartz_block", (CriterionTriggerInstance)has((ItemLike)Blocks.QUARTZ_BLOCK))
/* 3614 */       .save(debug0, "chiseled_quartz_block_from_quartz_block_stonecutting");
/*      */     
/* 3616 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.QUARTZ_BLOCK }, ), (ItemLike)Blocks.QUARTZ_BRICKS)
/* 3617 */       .unlocks("has_quartz_block", (CriterionTriggerInstance)has((ItemLike)Blocks.QUARTZ_BLOCK))
/* 3618 */       .save(debug0, "quartz_bricks_from_quartz_block_stonecutting");
/*      */     
/* 3620 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.COBBLESTONE }, ), (ItemLike)Blocks.COBBLESTONE_STAIRS)
/* 3621 */       .unlocks("has_cobblestone", (CriterionTriggerInstance)has((ItemLike)Blocks.COBBLESTONE))
/* 3622 */       .save(debug0, "cobblestone_stairs_from_cobblestone_stonecutting");
/*      */     
/* 3624 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.COBBLESTONE }, ), (ItemLike)Blocks.COBBLESTONE_SLAB, 2)
/* 3625 */       .unlocks("has_cobblestone", (CriterionTriggerInstance)has((ItemLike)Blocks.COBBLESTONE))
/* 3626 */       .save(debug0, "cobblestone_slab_from_cobblestone_stonecutting");
/*      */     
/* 3628 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.COBBLESTONE }, ), (ItemLike)Blocks.COBBLESTONE_WALL)
/* 3629 */       .unlocks("has_cobblestone", (CriterionTriggerInstance)has((ItemLike)Blocks.COBBLESTONE))
/* 3630 */       .save(debug0, "cobblestone_wall_from_cobblestone_stonecutting");
/*      */     
/* 3632 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.STONE_BRICKS }, ), (ItemLike)Blocks.STONE_BRICK_SLAB, 2)
/* 3633 */       .unlocks("has_stone_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.STONE_BRICKS))
/* 3634 */       .save(debug0, "stone_brick_slab_from_stone_bricks_stonecutting");
/*      */     
/* 3636 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.STONE_BRICKS }, ), (ItemLike)Blocks.STONE_BRICK_STAIRS)
/* 3637 */       .unlocks("has_stone_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.STONE_BRICKS))
/* 3638 */       .save(debug0, "stone_brick_stairs_from_stone_bricks_stonecutting");
/*      */     
/* 3640 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.STONE_BRICKS }, ), (ItemLike)Blocks.STONE_BRICK_WALL)
/* 3641 */       .unlocks("has_stone_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.STONE_BRICKS))
/* 3642 */       .save(debug0, "stone_brick_wall_from_stone_bricks_stonecutting");
/*      */     
/* 3644 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.STONE_BRICKS }, ), (ItemLike)Blocks.CHISELED_STONE_BRICKS)
/* 3645 */       .unlocks("has_stone_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.STONE_BRICKS))
/* 3646 */       .save(debug0, "chiseled_stone_bricks_from_stone_bricks_stonecutting");
/*      */     
/* 3648 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.BRICKS }, ), (ItemLike)Blocks.BRICK_SLAB, 2)
/* 3649 */       .unlocks("has_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.BRICKS))
/* 3650 */       .save(debug0, "brick_slab_from_bricks_stonecutting");
/*      */     
/* 3652 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.BRICKS }, ), (ItemLike)Blocks.BRICK_STAIRS)
/* 3653 */       .unlocks("has_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.BRICKS))
/* 3654 */       .save(debug0, "brick_stairs_from_bricks_stonecutting");
/*      */     
/* 3656 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.BRICKS }, ), (ItemLike)Blocks.BRICK_WALL)
/* 3657 */       .unlocks("has_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.BRICKS))
/* 3658 */       .save(debug0, "brick_wall_from_bricks_stonecutting");
/*      */     
/* 3660 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.NETHER_BRICKS }, ), (ItemLike)Blocks.NETHER_BRICK_SLAB, 2)
/* 3661 */       .unlocks("has_nether_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.NETHER_BRICKS))
/* 3662 */       .save(debug0, "nether_brick_slab_from_nether_bricks_stonecutting");
/*      */     
/* 3664 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.NETHER_BRICKS }, ), (ItemLike)Blocks.NETHER_BRICK_STAIRS)
/* 3665 */       .unlocks("has_nether_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.NETHER_BRICKS))
/* 3666 */       .save(debug0, "nether_brick_stairs_from_nether_bricks_stonecutting");
/*      */     
/* 3668 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.NETHER_BRICKS }, ), (ItemLike)Blocks.NETHER_BRICK_WALL)
/* 3669 */       .unlocks("has_nether_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.NETHER_BRICKS))
/* 3670 */       .save(debug0, "nether_brick_wall_from_nether_bricks_stonecutting");
/*      */     
/* 3672 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.NETHER_BRICKS }, ), (ItemLike)Blocks.CHISELED_NETHER_BRICKS)
/* 3673 */       .unlocks("has_nether_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.NETHER_BRICKS))
/* 3674 */       .save(debug0, "chiseled_nether_bricks_from_nether_bricks_stonecutting");
/*      */     
/* 3676 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.RED_NETHER_BRICKS }, ), (ItemLike)Blocks.RED_NETHER_BRICK_SLAB, 2)
/* 3677 */       .unlocks("has_nether_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_NETHER_BRICKS))
/* 3678 */       .save(debug0, "red_nether_brick_slab_from_red_nether_bricks_stonecutting");
/*      */     
/* 3680 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.RED_NETHER_BRICKS }, ), (ItemLike)Blocks.RED_NETHER_BRICK_STAIRS)
/* 3681 */       .unlocks("has_nether_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_NETHER_BRICKS))
/* 3682 */       .save(debug0, "red_nether_brick_stairs_from_red_nether_bricks_stonecutting");
/*      */     
/* 3684 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.RED_NETHER_BRICKS }, ), (ItemLike)Blocks.RED_NETHER_BRICK_WALL)
/* 3685 */       .unlocks("has_nether_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.RED_NETHER_BRICKS))
/* 3686 */       .save(debug0, "red_nether_brick_wall_from_red_nether_bricks_stonecutting");
/*      */     
/* 3688 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.PURPUR_BLOCK }, ), (ItemLike)Blocks.PURPUR_SLAB, 2)
/* 3689 */       .unlocks("has_purpur_block", (CriterionTriggerInstance)has((ItemLike)Blocks.PURPUR_BLOCK))
/* 3690 */       .save(debug0, "purpur_slab_from_purpur_block_stonecutting");
/*      */     
/* 3692 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.PURPUR_BLOCK }, ), (ItemLike)Blocks.PURPUR_STAIRS)
/* 3693 */       .unlocks("has_purpur_block", (CriterionTriggerInstance)has((ItemLike)Blocks.PURPUR_BLOCK))
/* 3694 */       .save(debug0, "purpur_stairs_from_purpur_block_stonecutting");
/*      */     
/* 3696 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.PURPUR_BLOCK }, ), (ItemLike)Blocks.PURPUR_PILLAR)
/* 3697 */       .unlocks("has_purpur_block", (CriterionTriggerInstance)has((ItemLike)Blocks.PURPUR_BLOCK))
/* 3698 */       .save(debug0, "purpur_pillar_from_purpur_block_stonecutting");
/*      */     
/* 3700 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.PRISMARINE }, ), (ItemLike)Blocks.PRISMARINE_SLAB, 2)
/* 3701 */       .unlocks("has_prismarine", (CriterionTriggerInstance)has((ItemLike)Blocks.PRISMARINE))
/* 3702 */       .save(debug0, "prismarine_slab_from_prismarine_stonecutting");
/*      */     
/* 3704 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.PRISMARINE }, ), (ItemLike)Blocks.PRISMARINE_STAIRS)
/* 3705 */       .unlocks("has_prismarine", (CriterionTriggerInstance)has((ItemLike)Blocks.PRISMARINE))
/* 3706 */       .save(debug0, "prismarine_stairs_from_prismarine_stonecutting");
/*      */     
/* 3708 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.PRISMARINE }, ), (ItemLike)Blocks.PRISMARINE_WALL)
/* 3709 */       .unlocks("has_prismarine", (CriterionTriggerInstance)has((ItemLike)Blocks.PRISMARINE))
/* 3710 */       .save(debug0, "prismarine_wall_from_prismarine_stonecutting");
/*      */     
/* 3712 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.PRISMARINE_BRICKS }, ), (ItemLike)Blocks.PRISMARINE_BRICK_SLAB, 2)
/* 3713 */       .unlocks("has_prismarine_brick", (CriterionTriggerInstance)has((ItemLike)Blocks.PRISMARINE_BRICKS))
/* 3714 */       .save(debug0, "prismarine_brick_slab_from_prismarine_stonecutting");
/*      */     
/* 3716 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.PRISMARINE_BRICKS }, ), (ItemLike)Blocks.PRISMARINE_BRICK_STAIRS)
/* 3717 */       .unlocks("has_prismarine_brick", (CriterionTriggerInstance)has((ItemLike)Blocks.PRISMARINE_BRICKS))
/* 3718 */       .save(debug0, "prismarine_brick_stairs_from_prismarine_stonecutting");
/*      */     
/* 3720 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.DARK_PRISMARINE }, ), (ItemLike)Blocks.DARK_PRISMARINE_SLAB, 2)
/* 3721 */       .unlocks("has_dark_prismarine", (CriterionTriggerInstance)has((ItemLike)Blocks.DARK_PRISMARINE))
/* 3722 */       .save(debug0, "dark_prismarine_slab_from_dark_prismarine_stonecutting");
/*      */     
/* 3724 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.DARK_PRISMARINE }, ), (ItemLike)Blocks.DARK_PRISMARINE_STAIRS)
/* 3725 */       .unlocks("has_dark_prismarine", (CriterionTriggerInstance)has((ItemLike)Blocks.DARK_PRISMARINE))
/* 3726 */       .save(debug0, "dark_prismarine_stairs_from_dark_prismarine_stonecutting");
/*      */     
/* 3728 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.ANDESITE }, ), (ItemLike)Blocks.ANDESITE_SLAB, 2)
/* 3729 */       .unlocks("has_andesite", (CriterionTriggerInstance)has((ItemLike)Blocks.ANDESITE))
/* 3730 */       .save(debug0, "andesite_slab_from_andesite_stonecutting");
/*      */     
/* 3732 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.ANDESITE }, ), (ItemLike)Blocks.ANDESITE_STAIRS)
/* 3733 */       .unlocks("has_andesite", (CriterionTriggerInstance)has((ItemLike)Blocks.ANDESITE))
/* 3734 */       .save(debug0, "andesite_stairs_from_andesite_stonecutting");
/*      */     
/* 3736 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.ANDESITE }, ), (ItemLike)Blocks.ANDESITE_WALL)
/* 3737 */       .unlocks("has_andesite", (CriterionTriggerInstance)has((ItemLike)Blocks.ANDESITE))
/* 3738 */       .save(debug0, "andesite_wall_from_andesite_stonecutting");
/*      */     
/* 3740 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.ANDESITE }, ), (ItemLike)Blocks.POLISHED_ANDESITE)
/* 3741 */       .unlocks("has_andesite", (CriterionTriggerInstance)has((ItemLike)Blocks.ANDESITE))
/* 3742 */       .save(debug0, "polished_andesite_from_andesite_stonecutting");
/*      */     
/* 3744 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.ANDESITE }, ), (ItemLike)Blocks.POLISHED_ANDESITE_SLAB, 2)
/* 3745 */       .unlocks("has_andesite", (CriterionTriggerInstance)has((ItemLike)Blocks.ANDESITE))
/* 3746 */       .save(debug0, "polished_andesite_slab_from_andesite_stonecutting");
/*      */     
/* 3748 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.ANDESITE }, ), (ItemLike)Blocks.POLISHED_ANDESITE_STAIRS)
/* 3749 */       .unlocks("has_andesite", (CriterionTriggerInstance)has((ItemLike)Blocks.ANDESITE))
/* 3750 */       .save(debug0, "polished_andesite_stairs_from_andesite_stonecutting");
/*      */     
/* 3752 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.POLISHED_ANDESITE }, ), (ItemLike)Blocks.POLISHED_ANDESITE_SLAB, 2)
/* 3753 */       .unlocks("has_polished_andesite", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_ANDESITE))
/* 3754 */       .save(debug0, "polished_andesite_slab_from_polished_andesite_stonecutting");
/*      */     
/* 3756 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.POLISHED_ANDESITE }, ), (ItemLike)Blocks.POLISHED_ANDESITE_STAIRS)
/* 3757 */       .unlocks("has_polished_andesite", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_ANDESITE))
/* 3758 */       .save(debug0, "polished_andesite_stairs_from_polished_andesite_stonecutting");
/*      */     
/* 3760 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.BASALT }, ), (ItemLike)Blocks.POLISHED_BASALT)
/* 3761 */       .unlocks("has_basalt", (CriterionTriggerInstance)has((ItemLike)Blocks.BASALT))
/* 3762 */       .save(debug0, "polished_basalt_from_basalt_stonecutting");
/*      */     
/* 3764 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.GRANITE }, ), (ItemLike)Blocks.GRANITE_SLAB, 2)
/* 3765 */       .unlocks("has_granite", (CriterionTriggerInstance)has((ItemLike)Blocks.GRANITE))
/* 3766 */       .save(debug0, "granite_slab_from_granite_stonecutting");
/*      */     
/* 3768 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.GRANITE }, ), (ItemLike)Blocks.GRANITE_STAIRS)
/* 3769 */       .unlocks("has_granite", (CriterionTriggerInstance)has((ItemLike)Blocks.GRANITE))
/* 3770 */       .save(debug0, "granite_stairs_from_granite_stonecutting");
/*      */     
/* 3772 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.GRANITE }, ), (ItemLike)Blocks.GRANITE_WALL)
/* 3773 */       .unlocks("has_granite", (CriterionTriggerInstance)has((ItemLike)Blocks.GRANITE))
/* 3774 */       .save(debug0, "granite_wall_from_granite_stonecutting");
/*      */     
/* 3776 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.GRANITE }, ), (ItemLike)Blocks.POLISHED_GRANITE)
/* 3777 */       .unlocks("has_granite", (CriterionTriggerInstance)has((ItemLike)Blocks.GRANITE))
/* 3778 */       .save(debug0, "polished_granite_from_granite_stonecutting");
/*      */     
/* 3780 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.GRANITE }, ), (ItemLike)Blocks.POLISHED_GRANITE_SLAB, 2)
/* 3781 */       .unlocks("has_granite", (CriterionTriggerInstance)has((ItemLike)Blocks.GRANITE))
/* 3782 */       .save(debug0, "polished_granite_slab_from_granite_stonecutting");
/*      */     
/* 3784 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.GRANITE }, ), (ItemLike)Blocks.POLISHED_GRANITE_STAIRS)
/* 3785 */       .unlocks("has_granite", (CriterionTriggerInstance)has((ItemLike)Blocks.GRANITE))
/* 3786 */       .save(debug0, "polished_granite_stairs_from_granite_stonecutting");
/*      */     
/* 3788 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.POLISHED_GRANITE }, ), (ItemLike)Blocks.POLISHED_GRANITE_SLAB, 2)
/* 3789 */       .unlocks("has_polished_granite", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_GRANITE))
/* 3790 */       .save(debug0, "polished_granite_slab_from_polished_granite_stonecutting");
/*      */     
/* 3792 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.POLISHED_GRANITE }, ), (ItemLike)Blocks.POLISHED_GRANITE_STAIRS)
/* 3793 */       .unlocks("has_polished_granite", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_GRANITE))
/* 3794 */       .save(debug0, "polished_granite_stairs_from_polished_granite_stonecutting");
/*      */     
/* 3796 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.DIORITE }, ), (ItemLike)Blocks.DIORITE_SLAB, 2)
/* 3797 */       .unlocks("has_diorite", (CriterionTriggerInstance)has((ItemLike)Blocks.DIORITE))
/* 3798 */       .save(debug0, "diorite_slab_from_diorite_stonecutting");
/*      */     
/* 3800 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.DIORITE }, ), (ItemLike)Blocks.DIORITE_STAIRS)
/* 3801 */       .unlocks("has_diorite", (CriterionTriggerInstance)has((ItemLike)Blocks.DIORITE))
/* 3802 */       .save(debug0, "diorite_stairs_from_diorite_stonecutting");
/*      */     
/* 3804 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.DIORITE }, ), (ItemLike)Blocks.DIORITE_WALL)
/* 3805 */       .unlocks("has_diorite", (CriterionTriggerInstance)has((ItemLike)Blocks.DIORITE))
/* 3806 */       .save(debug0, "diorite_wall_from_diorite_stonecutting");
/*      */     
/* 3808 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.DIORITE }, ), (ItemLike)Blocks.POLISHED_DIORITE)
/* 3809 */       .unlocks("has_diorite", (CriterionTriggerInstance)has((ItemLike)Blocks.DIORITE))
/* 3810 */       .save(debug0, "polished_diorite_from_diorite_stonecutting");
/*      */     
/* 3812 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.DIORITE }, ), (ItemLike)Blocks.POLISHED_DIORITE_SLAB, 2)
/* 3813 */       .unlocks("has_diorite", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_DIORITE))
/* 3814 */       .save(debug0, "polished_diorite_slab_from_diorite_stonecutting");
/*      */     
/* 3816 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.DIORITE }, ), (ItemLike)Blocks.POLISHED_DIORITE_STAIRS)
/* 3817 */       .unlocks("has_diorite", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_DIORITE))
/* 3818 */       .save(debug0, "polished_diorite_stairs_from_diorite_stonecutting");
/*      */     
/* 3820 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.POLISHED_DIORITE }, ), (ItemLike)Blocks.POLISHED_DIORITE_SLAB, 2)
/* 3821 */       .unlocks("has_polished_diorite", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_DIORITE))
/* 3822 */       .save(debug0, "polished_diorite_slab_from_polished_diorite_stonecutting");
/*      */     
/* 3824 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.POLISHED_DIORITE }, ), (ItemLike)Blocks.POLISHED_DIORITE_STAIRS)
/* 3825 */       .unlocks("has_polished_diorite", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_DIORITE))
/* 3826 */       .save(debug0, "polished_diorite_stairs_from_polished_diorite_stonecutting");
/*      */     
/* 3828 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.MOSSY_STONE_BRICKS }, ), (ItemLike)Blocks.MOSSY_STONE_BRICK_SLAB, 2)
/* 3829 */       .unlocks("has_mossy_stone_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.MOSSY_STONE_BRICKS))
/* 3830 */       .save(debug0, "mossy_stone_brick_slab_from_mossy_stone_brick_stonecutting");
/*      */     
/* 3832 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.MOSSY_STONE_BRICKS }, ), (ItemLike)Blocks.MOSSY_STONE_BRICK_STAIRS)
/* 3833 */       .unlocks("has_mossy_stone_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.MOSSY_STONE_BRICKS))
/* 3834 */       .save(debug0, "mossy_stone_brick_stairs_from_mossy_stone_brick_stonecutting");
/*      */     
/* 3836 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.MOSSY_STONE_BRICKS }, ), (ItemLike)Blocks.MOSSY_STONE_BRICK_WALL)
/* 3837 */       .unlocks("has_mossy_stone_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.MOSSY_STONE_BRICKS))
/* 3838 */       .save(debug0, "mossy_stone_brick_wall_from_mossy_stone_brick_stonecutting");
/*      */     
/* 3840 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.MOSSY_COBBLESTONE }, ), (ItemLike)Blocks.MOSSY_COBBLESTONE_SLAB, 2)
/* 3841 */       .unlocks("has_mossy_cobblestone", (CriterionTriggerInstance)has((ItemLike)Blocks.MOSSY_COBBLESTONE))
/* 3842 */       .save(debug0, "mossy_cobblestone_slab_from_mossy_cobblestone_stonecutting");
/*      */     
/* 3844 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.MOSSY_COBBLESTONE }, ), (ItemLike)Blocks.MOSSY_COBBLESTONE_STAIRS)
/* 3845 */       .unlocks("has_mossy_cobblestone", (CriterionTriggerInstance)has((ItemLike)Blocks.MOSSY_COBBLESTONE))
/* 3846 */       .save(debug0, "mossy_cobblestone_stairs_from_mossy_cobblestone_stonecutting");
/*      */     
/* 3848 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.MOSSY_COBBLESTONE }, ), (ItemLike)Blocks.MOSSY_COBBLESTONE_WALL)
/* 3849 */       .unlocks("has_mossy_cobblestone", (CriterionTriggerInstance)has((ItemLike)Blocks.MOSSY_COBBLESTONE))
/* 3850 */       .save(debug0, "mossy_cobblestone_wall_from_mossy_cobblestone_stonecutting");
/*      */     
/* 3852 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.SMOOTH_SANDSTONE }, ), (ItemLike)Blocks.SMOOTH_SANDSTONE_SLAB, 2)
/* 3853 */       .unlocks("has_smooth_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.SMOOTH_SANDSTONE))
/* 3854 */       .save(debug0, "smooth_sandstone_slab_from_smooth_sandstone_stonecutting");
/*      */     
/* 3856 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.SMOOTH_SANDSTONE }, ), (ItemLike)Blocks.SMOOTH_SANDSTONE_STAIRS)
/* 3857 */       .unlocks("has_mossy_cobblestone", (CriterionTriggerInstance)has((ItemLike)Blocks.SMOOTH_SANDSTONE))
/* 3858 */       .save(debug0, "smooth_sandstone_stairs_from_smooth_sandstone_stonecutting");
/*      */     
/* 3860 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.SMOOTH_RED_SANDSTONE }, ), (ItemLike)Blocks.SMOOTH_RED_SANDSTONE_SLAB, 2)
/* 3861 */       .unlocks("has_smooth_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.SMOOTH_RED_SANDSTONE))
/* 3862 */       .save(debug0, "smooth_red_sandstone_slab_from_smooth_red_sandstone_stonecutting");
/*      */     
/* 3864 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.SMOOTH_RED_SANDSTONE }, ), (ItemLike)Blocks.SMOOTH_RED_SANDSTONE_STAIRS)
/* 3865 */       .unlocks("has_smooth_red_sandstone", (CriterionTriggerInstance)has((ItemLike)Blocks.SMOOTH_RED_SANDSTONE))
/* 3866 */       .save(debug0, "smooth_red_sandstone_stairs_from_smooth_red_sandstone_stonecutting");
/*      */     
/* 3868 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.SMOOTH_QUARTZ }, ), (ItemLike)Blocks.SMOOTH_QUARTZ_SLAB, 2)
/* 3869 */       .unlocks("has_smooth_quartz", (CriterionTriggerInstance)has((ItemLike)Blocks.SMOOTH_QUARTZ))
/* 3870 */       .save(debug0, "smooth_quartz_slab_from_smooth_quartz_stonecutting");
/*      */     
/* 3872 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.SMOOTH_QUARTZ }, ), (ItemLike)Blocks.SMOOTH_QUARTZ_STAIRS)
/* 3873 */       .unlocks("has_smooth_quartz", (CriterionTriggerInstance)has((ItemLike)Blocks.SMOOTH_QUARTZ))
/* 3874 */       .save(debug0, "smooth_quartz_stairs_from_smooth_quartz_stonecutting");
/*      */     
/* 3876 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.END_STONE_BRICKS }, ), (ItemLike)Blocks.END_STONE_BRICK_SLAB, 2)
/* 3877 */       .unlocks("has_end_stone_brick", (CriterionTriggerInstance)has((ItemLike)Blocks.END_STONE_BRICKS))
/* 3878 */       .save(debug0, "end_stone_brick_slab_from_end_stone_brick_stonecutting");
/*      */     
/* 3880 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.END_STONE_BRICKS }, ), (ItemLike)Blocks.END_STONE_BRICK_STAIRS)
/* 3881 */       .unlocks("has_end_stone_brick", (CriterionTriggerInstance)has((ItemLike)Blocks.END_STONE_BRICKS))
/* 3882 */       .save(debug0, "end_stone_brick_stairs_from_end_stone_brick_stonecutting");
/*      */     
/* 3884 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.END_STONE_BRICKS }, ), (ItemLike)Blocks.END_STONE_BRICK_WALL)
/* 3885 */       .unlocks("has_end_stone_brick", (CriterionTriggerInstance)has((ItemLike)Blocks.END_STONE_BRICKS))
/* 3886 */       .save(debug0, "end_stone_brick_wall_from_end_stone_brick_stonecutting");
/*      */     
/* 3888 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.END_STONE }, ), (ItemLike)Blocks.END_STONE_BRICKS)
/* 3889 */       .unlocks("has_end_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.END_STONE))
/* 3890 */       .save(debug0, "end_stone_bricks_from_end_stone_stonecutting");
/*      */     
/* 3892 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.END_STONE }, ), (ItemLike)Blocks.END_STONE_BRICK_SLAB, 2)
/* 3893 */       .unlocks("has_end_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.END_STONE))
/* 3894 */       .save(debug0, "end_stone_brick_slab_from_end_stone_stonecutting");
/*      */     
/* 3896 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.END_STONE }, ), (ItemLike)Blocks.END_STONE_BRICK_STAIRS)
/* 3897 */       .unlocks("has_end_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.END_STONE))
/* 3898 */       .save(debug0, "end_stone_brick_stairs_from_end_stone_stonecutting");
/*      */     
/* 3900 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.END_STONE }, ), (ItemLike)Blocks.END_STONE_BRICK_WALL)
/* 3901 */       .unlocks("has_end_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.END_STONE))
/* 3902 */       .save(debug0, "end_stone_brick_wall_from_end_stone_stonecutting");
/*      */     
/* 3904 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.SMOOTH_STONE }, ), (ItemLike)Blocks.SMOOTH_STONE_SLAB, 2)
/* 3905 */       .unlocks("has_smooth_stone", (CriterionTriggerInstance)has((ItemLike)Blocks.SMOOTH_STONE))
/* 3906 */       .save(debug0, "smooth_stone_slab_from_smooth_stone_stonecutting");
/*      */     
/* 3908 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.BLACKSTONE }, ), (ItemLike)Blocks.BLACKSTONE_SLAB, 2)
/* 3909 */       .unlocks("has_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.BLACKSTONE))
/* 3910 */       .save(debug0, "blackstone_slab_from_blackstone_stonecutting");
/*      */     
/* 3912 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.BLACKSTONE }, ), (ItemLike)Blocks.BLACKSTONE_STAIRS)
/* 3913 */       .unlocks("has_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.BLACKSTONE))
/* 3914 */       .save(debug0, "blackstone_stairs_from_blackstone_stonecutting");
/*      */     
/* 3916 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.BLACKSTONE }, ), (ItemLike)Blocks.BLACKSTONE_WALL)
/* 3917 */       .unlocks("has_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.BLACKSTONE))
/* 3918 */       .save(debug0, "blackstone_wall_from_blackstone_stonecutting");
/*      */     
/* 3920 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.BLACKSTONE }, ), (ItemLike)Blocks.POLISHED_BLACKSTONE)
/* 3921 */       .unlocks("has_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.BLACKSTONE))
/* 3922 */       .save(debug0, "polished_blackstone_from_blackstone_stonecutting");
/*      */     
/* 3924 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.BLACKSTONE }, ), (ItemLike)Blocks.POLISHED_BLACKSTONE_WALL)
/* 3925 */       .unlocks("has_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.BLACKSTONE))
/* 3926 */       .save(debug0, "polished_blackstone_wall_from_blackstone_stonecutting");
/*      */     
/* 3928 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.BLACKSTONE }, ), (ItemLike)Blocks.POLISHED_BLACKSTONE_SLAB, 2)
/* 3929 */       .unlocks("has_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.BLACKSTONE))
/* 3930 */       .save(debug0, "polished_blackstone_slab_from_blackstone_stonecutting");
/*      */     
/* 3932 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.BLACKSTONE }, ), (ItemLike)Blocks.POLISHED_BLACKSTONE_STAIRS)
/* 3933 */       .unlocks("has_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.BLACKSTONE))
/* 3934 */       .save(debug0, "polished_blackstone_stairs_from_blackstone_stonecutting");
/*      */     
/* 3936 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.BLACKSTONE }, ), (ItemLike)Blocks.CHISELED_POLISHED_BLACKSTONE)
/* 3937 */       .unlocks("has_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.BLACKSTONE))
/* 3938 */       .save(debug0, "chiseled_polished_blackstone_from_blackstone_stonecutting");
/*      */     
/* 3940 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.BLACKSTONE }, ), (ItemLike)Blocks.POLISHED_BLACKSTONE_BRICKS)
/* 3941 */       .unlocks("has_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.BLACKSTONE))
/* 3942 */       .save(debug0, "polished_blackstone_bricks_from_blackstone_stonecutting");
/*      */     
/* 3944 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.BLACKSTONE }, ), (ItemLike)Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, 2)
/* 3945 */       .unlocks("has_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.BLACKSTONE))
/* 3946 */       .save(debug0, "polished_blackstone_brick_slab_from_blackstone_stonecutting");
/*      */     
/* 3948 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.BLACKSTONE }, ), (ItemLike)Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS)
/* 3949 */       .unlocks("has_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.BLACKSTONE))
/* 3950 */       .save(debug0, "polished_blackstone_brick_stairs_from_blackstone_stonecutting");
/*      */     
/* 3952 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.BLACKSTONE }, ), (ItemLike)Blocks.POLISHED_BLACKSTONE_BRICK_WALL)
/* 3953 */       .unlocks("has_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.BLACKSTONE))
/* 3954 */       .save(debug0, "polished_blackstone_brick_wall_from_blackstone_stonecutting");
/*      */     
/* 3956 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.POLISHED_BLACKSTONE }, ), (ItemLike)Blocks.POLISHED_BLACKSTONE_SLAB, 2)
/* 3957 */       .unlocks("has_polished_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_BLACKSTONE))
/* 3958 */       .save(debug0, "polished_blackstone_slab_from_polished_blackstone_stonecutting");
/*      */     
/* 3960 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.POLISHED_BLACKSTONE }, ), (ItemLike)Blocks.POLISHED_BLACKSTONE_STAIRS)
/* 3961 */       .unlocks("has_polished_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_BLACKSTONE))
/* 3962 */       .save(debug0, "polished_blackstone_stairs_from_polished_blackstone_stonecutting");
/*      */     
/* 3964 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.POLISHED_BLACKSTONE }, ), (ItemLike)Blocks.POLISHED_BLACKSTONE_BRICKS)
/* 3965 */       .unlocks("has_polished_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_BLACKSTONE))
/* 3966 */       .save(debug0, "polished_blackstone_bricks_from_polished_blackstone_stonecutting");
/*      */     
/* 3968 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.POLISHED_BLACKSTONE }, ), (ItemLike)Blocks.POLISHED_BLACKSTONE_WALL)
/* 3969 */       .unlocks("has_polished_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_BLACKSTONE))
/* 3970 */       .save(debug0, "polished_blackstone_wall_from_polished_blackstone_stonecutting");
/*      */     
/* 3972 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.POLISHED_BLACKSTONE }, ), (ItemLike)Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, 2)
/* 3973 */       .unlocks("has_polished_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_BLACKSTONE))
/* 3974 */       .save(debug0, "polished_blackstone_brick_slab_from_polished_blackstone_stonecutting");
/*      */     
/* 3976 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.POLISHED_BLACKSTONE }, ), (ItemLike)Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS)
/* 3977 */       .unlocks("has_polished_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_BLACKSTONE))
/* 3978 */       .save(debug0, "polished_blackstone_brick_stairs_from_polished_blackstone_stonecutting");
/*      */     
/* 3980 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.POLISHED_BLACKSTONE }, ), (ItemLike)Blocks.POLISHED_BLACKSTONE_BRICK_WALL)
/* 3981 */       .unlocks("has_polished_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_BLACKSTONE))
/* 3982 */       .save(debug0, "polished_blackstone_brick_wall_from_polished_blackstone_stonecutting");
/*      */     
/* 3984 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.POLISHED_BLACKSTONE }, ), (ItemLike)Blocks.CHISELED_POLISHED_BLACKSTONE)
/* 3985 */       .unlocks("has_polished_blackstone", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_BLACKSTONE))
/* 3986 */       .save(debug0, "chiseled_polished_blackstone_from_polished_blackstone_stonecutting");
/*      */     
/* 3988 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.POLISHED_BLACKSTONE_BRICKS }, ), (ItemLike)Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, 2)
/* 3989 */       .unlocks("has_polished_blackstone_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_BLACKSTONE_BRICKS))
/* 3990 */       .save(debug0, "polished_blackstone_brick_slab_from_polished_blackstone_bricks_stonecutting");
/*      */     
/* 3992 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.POLISHED_BLACKSTONE_BRICKS }, ), (ItemLike)Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS)
/* 3993 */       .unlocks("has_polished_blackstone_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_BLACKSTONE_BRICKS))
/* 3994 */       .save(debug0, "polished_blackstone_brick_stairs_from_polished_blackstone_bricks_stonecutting");
/*      */     
/* 3996 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.POLISHED_BLACKSTONE_BRICKS }, ), (ItemLike)Blocks.POLISHED_BLACKSTONE_BRICK_WALL)
/* 3997 */       .unlocks("has_polished_blackstone_bricks", (CriterionTriggerInstance)has((ItemLike)Blocks.POLISHED_BLACKSTONE_BRICKS))
/* 3998 */       .save(debug0, "polished_blackstone_brick_wall_from_polished_blackstone_bricks_stonecutting");
/*      */ 
/*      */     
/* 4001 */     netheriteSmithing(debug0, Items.DIAMOND_CHESTPLATE, Items.NETHERITE_CHESTPLATE);
/* 4002 */     netheriteSmithing(debug0, Items.DIAMOND_LEGGINGS, Items.NETHERITE_LEGGINGS);
/* 4003 */     netheriteSmithing(debug0, Items.DIAMOND_HELMET, Items.NETHERITE_HELMET);
/* 4004 */     netheriteSmithing(debug0, Items.DIAMOND_BOOTS, Items.NETHERITE_BOOTS);
/* 4005 */     netheriteSmithing(debug0, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD);
/* 4006 */     netheriteSmithing(debug0, Items.DIAMOND_AXE, Items.NETHERITE_AXE);
/* 4007 */     netheriteSmithing(debug0, Items.DIAMOND_PICKAXE, Items.NETHERITE_PICKAXE);
/* 4008 */     netheriteSmithing(debug0, Items.DIAMOND_HOE, Items.NETHERITE_HOE);
/* 4009 */     netheriteSmithing(debug0, Items.DIAMOND_SHOVEL, Items.NETHERITE_SHOVEL);
/*      */   }
/*      */   
/*      */   private static void netheriteSmithing(Consumer<FinishedRecipe> debug0, Item debug1, Item debug2) {
/* 4013 */     UpgradeRecipeBuilder.smithing(Ingredient.of(new ItemLike[] { (ItemLike)debug1 }, ), Ingredient.of(new ItemLike[] { (ItemLike)Items.NETHERITE_INGOT }, ), debug2)
/* 4014 */       .unlocks("has_netherite_ingot", (CriterionTriggerInstance)has((ItemLike)Items.NETHERITE_INGOT))
/* 4015 */       .save(debug0, Registry.ITEM.getKey(debug2.asItem()).getPath() + "_smithing");
/*      */   }
/*      */ 
/*      */   
/*      */   private static void planksFromLog(Consumer<FinishedRecipe> debug0, ItemLike debug1, Tag<Item> debug2) {
/* 4020 */     ShapelessRecipeBuilder.shapeless(debug1, 4)
/* 4021 */       .requires(debug2)
/* 4022 */       .group("planks")
/* 4023 */       .unlockedBy("has_log", (CriterionTriggerInstance)has(debug2))
/* 4024 */       .save(debug0);
/*      */   }
/*      */   
/*      */   private static void planksFromLogs(Consumer<FinishedRecipe> debug0, ItemLike debug1, Tag<Item> debug2) {
/* 4028 */     ShapelessRecipeBuilder.shapeless(debug1, 4)
/* 4029 */       .requires(debug2)
/* 4030 */       .group("planks")
/* 4031 */       .unlockedBy("has_logs", (CriterionTriggerInstance)has(debug2))
/* 4032 */       .save(debug0);
/*      */   }
/*      */   
/*      */   private static void woodFromLogs(Consumer<FinishedRecipe> debug0, ItemLike debug1, ItemLike debug2) {
/* 4036 */     ShapedRecipeBuilder.shaped(debug1, 3)
/* 4037 */       .define(Character.valueOf('#'), debug2)
/* 4038 */       .pattern("##")
/* 4039 */       .pattern("##")
/* 4040 */       .group("bark")
/* 4041 */       .unlockedBy("has_log", (CriterionTriggerInstance)has(debug2))
/* 4042 */       .save(debug0);
/*      */   }
/*      */   
/*      */   private static void woodenBoat(Consumer<FinishedRecipe> debug0, ItemLike debug1, ItemLike debug2) {
/* 4046 */     ShapedRecipeBuilder.shaped(debug1)
/* 4047 */       .define(Character.valueOf('#'), debug2)
/* 4048 */       .pattern("# #")
/* 4049 */       .pattern("###")
/* 4050 */       .group("boat")
/* 4051 */       .unlockedBy("in_water", (CriterionTriggerInstance)insideOf(Blocks.WATER))
/* 4052 */       .save(debug0);
/*      */   }
/*      */   
/*      */   private static void woodenButton(Consumer<FinishedRecipe> debug0, ItemLike debug1, ItemLike debug2) {
/* 4056 */     ShapelessRecipeBuilder.shapeless(debug1)
/* 4057 */       .requires(debug2)
/* 4058 */       .group("wooden_button")
/* 4059 */       .unlockedBy("has_planks", (CriterionTriggerInstance)has(debug2))
/* 4060 */       .save(debug0);
/*      */   }
/*      */   
/*      */   private static void woodenDoor(Consumer<FinishedRecipe> debug0, ItemLike debug1, ItemLike debug2) {
/* 4064 */     ShapedRecipeBuilder.shaped(debug1, 3)
/* 4065 */       .define(Character.valueOf('#'), debug2)
/* 4066 */       .pattern("##")
/* 4067 */       .pattern("##")
/* 4068 */       .pattern("##")
/* 4069 */       .group("wooden_door")
/* 4070 */       .unlockedBy("has_planks", (CriterionTriggerInstance)has(debug2))
/* 4071 */       .save(debug0);
/*      */   }
/*      */   
/*      */   private static void woodenFence(Consumer<FinishedRecipe> debug0, ItemLike debug1, ItemLike debug2) {
/* 4075 */     ShapedRecipeBuilder.shaped(debug1, 3)
/* 4076 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 4077 */       .define(Character.valueOf('W'), debug2)
/* 4078 */       .pattern("W#W")
/* 4079 */       .pattern("W#W")
/* 4080 */       .group("wooden_fence")
/* 4081 */       .unlockedBy("has_planks", (CriterionTriggerInstance)has(debug2))
/* 4082 */       .save(debug0);
/*      */   }
/*      */   
/*      */   private static void woodenFenceGate(Consumer<FinishedRecipe> debug0, ItemLike debug1, ItemLike debug2) {
/* 4086 */     ShapedRecipeBuilder.shaped(debug1)
/* 4087 */       .define(Character.valueOf('#'), (ItemLike)Items.STICK)
/* 4088 */       .define(Character.valueOf('W'), debug2)
/* 4089 */       .pattern("#W#")
/* 4090 */       .pattern("#W#")
/* 4091 */       .group("wooden_fence_gate")
/* 4092 */       .unlockedBy("has_planks", (CriterionTriggerInstance)has(debug2))
/* 4093 */       .save(debug0);
/*      */   }
/*      */   
/*      */   private static void woodenPressurePlate(Consumer<FinishedRecipe> debug0, ItemLike debug1, ItemLike debug2) {
/* 4097 */     ShapedRecipeBuilder.shaped(debug1)
/* 4098 */       .define(Character.valueOf('#'), debug2)
/* 4099 */       .pattern("##")
/* 4100 */       .group("wooden_pressure_plate")
/* 4101 */       .unlockedBy("has_planks", (CriterionTriggerInstance)has(debug2))
/* 4102 */       .save(debug0);
/*      */   }
/*      */   
/*      */   private static void woodenSlab(Consumer<FinishedRecipe> debug0, ItemLike debug1, ItemLike debug2) {
/* 4106 */     ShapedRecipeBuilder.shaped(debug1, 6)
/* 4107 */       .define(Character.valueOf('#'), debug2)
/* 4108 */       .pattern("###")
/* 4109 */       .group("wooden_slab")
/* 4110 */       .unlockedBy("has_planks", (CriterionTriggerInstance)has(debug2))
/* 4111 */       .save(debug0);
/*      */   }
/*      */   
/*      */   private static void woodenStairs(Consumer<FinishedRecipe> debug0, ItemLike debug1, ItemLike debug2) {
/* 4115 */     ShapedRecipeBuilder.shaped(debug1, 4)
/* 4116 */       .define(Character.valueOf('#'), debug2)
/* 4117 */       .pattern("#  ")
/* 4118 */       .pattern("## ")
/* 4119 */       .pattern("###")
/* 4120 */       .group("wooden_stairs")
/* 4121 */       .unlockedBy("has_planks", (CriterionTriggerInstance)has(debug2))
/* 4122 */       .save(debug0);
/*      */   }
/*      */   
/*      */   private static void woodenTrapdoor(Consumer<FinishedRecipe> debug0, ItemLike debug1, ItemLike debug2) {
/* 4126 */     ShapedRecipeBuilder.shaped(debug1, 2)
/* 4127 */       .define(Character.valueOf('#'), debug2)
/* 4128 */       .pattern("###")
/* 4129 */       .pattern("###")
/* 4130 */       .group("wooden_trapdoor")
/* 4131 */       .unlockedBy("has_planks", (CriterionTriggerInstance)has(debug2))
/* 4132 */       .save(debug0);
/*      */   }
/*      */   
/*      */   private static void woodenSign(Consumer<FinishedRecipe> debug0, ItemLike debug1, ItemLike debug2) {
/* 4136 */     String debug3 = Registry.ITEM.getKey(debug2.asItem()).getPath();
/* 4137 */     ShapedRecipeBuilder.shaped(debug1, 3)
/* 4138 */       .group("sign")
/* 4139 */       .define(Character.valueOf('#'), debug2)
/* 4140 */       .define(Character.valueOf('X'), (ItemLike)Items.STICK)
/* 4141 */       .pattern("###")
/* 4142 */       .pattern("###")
/* 4143 */       .pattern(" X ")
/* 4144 */       .unlockedBy("has_" + debug3, (CriterionTriggerInstance)has(debug2))
/* 4145 */       .save(debug0);
/*      */   }
/*      */   
/*      */   private static void coloredWoolFromWhiteWoolAndDye(Consumer<FinishedRecipe> debug0, ItemLike debug1, ItemLike debug2) {
/* 4149 */     ShapelessRecipeBuilder.shapeless(debug1)
/* 4150 */       .requires(debug2)
/* 4151 */       .requires((ItemLike)Blocks.WHITE_WOOL)
/* 4152 */       .group("wool")
/* 4153 */       .unlockedBy("has_white_wool", (CriterionTriggerInstance)has((ItemLike)Blocks.WHITE_WOOL))
/* 4154 */       .save(debug0);
/*      */   }
/*      */   
/*      */   private static void carpetFromWool(Consumer<FinishedRecipe> debug0, ItemLike debug1, ItemLike debug2) {
/* 4158 */     String debug3 = Registry.ITEM.getKey(debug2.asItem()).getPath();
/* 4159 */     ShapedRecipeBuilder.shaped(debug1, 3)
/* 4160 */       .define(Character.valueOf('#'), debug2)
/* 4161 */       .pattern("##")
/* 4162 */       .group("carpet")
/* 4163 */       .unlockedBy("has_" + debug3, (CriterionTriggerInstance)has(debug2))
/* 4164 */       .save(debug0);
/*      */   }
/*      */   
/*      */   private static void coloredCarpetFromWhiteCarpetAndDye(Consumer<FinishedRecipe> debug0, ItemLike debug1, ItemLike debug2) {
/* 4168 */     String debug3 = Registry.ITEM.getKey(debug1.asItem()).getPath();
/* 4169 */     String debug4 = Registry.ITEM.getKey(debug2.asItem()).getPath();
/* 4170 */     ShapedRecipeBuilder.shaped(debug1, 8)
/* 4171 */       .define(Character.valueOf('#'), (ItemLike)Blocks.WHITE_CARPET)
/* 4172 */       .define(Character.valueOf('$'), debug2)
/* 4173 */       .pattern("###")
/* 4174 */       .pattern("#$#")
/* 4175 */       .pattern("###")
/* 4176 */       .group("carpet")
/* 4177 */       .unlockedBy("has_white_carpet", (CriterionTriggerInstance)has((ItemLike)Blocks.WHITE_CARPET))
/* 4178 */       .unlockedBy("has_" + debug4, (CriterionTriggerInstance)has(debug2))
/* 4179 */       .save(debug0, debug3 + "_from_white_carpet");
/*      */   }
/*      */   
/*      */   private static void bedFromPlanksAndWool(Consumer<FinishedRecipe> debug0, ItemLike debug1, ItemLike debug2) {
/* 4183 */     String debug3 = Registry.ITEM.getKey(debug2.asItem()).getPath();
/* 4184 */     ShapedRecipeBuilder.shaped(debug1)
/* 4185 */       .define(Character.valueOf('#'), debug2)
/* 4186 */       .define(Character.valueOf('X'), (Tag<Item>)ItemTags.PLANKS)
/* 4187 */       .pattern("###")
/* 4188 */       .pattern("XXX")
/* 4189 */       .group("bed")
/* 4190 */       .unlockedBy("has_" + debug3, (CriterionTriggerInstance)has(debug2))
/* 4191 */       .save(debug0);
/*      */   }
/*      */   
/*      */   private static void bedFromWhiteBedAndDye(Consumer<FinishedRecipe> debug0, ItemLike debug1, ItemLike debug2) {
/* 4195 */     String debug3 = Registry.ITEM.getKey(debug1.asItem()).getPath();
/* 4196 */     ShapelessRecipeBuilder.shapeless(debug1)
/* 4197 */       .requires((ItemLike)Items.WHITE_BED)
/* 4198 */       .requires(debug2)
/* 4199 */       .group("dyed_bed")
/* 4200 */       .unlockedBy("has_bed", (CriterionTriggerInstance)has((ItemLike)Items.WHITE_BED))
/* 4201 */       .save(debug0, debug3 + "_from_white_bed");
/*      */   }
/*      */   
/*      */   private static void banner(Consumer<FinishedRecipe> debug0, ItemLike debug1, ItemLike debug2) {
/* 4205 */     String debug3 = Registry.ITEM.getKey(debug2.asItem()).getPath();
/* 4206 */     ShapedRecipeBuilder.shaped(debug1)
/* 4207 */       .define(Character.valueOf('#'), debug2)
/* 4208 */       .define(Character.valueOf('|'), (ItemLike)Items.STICK)
/* 4209 */       .pattern("###")
/* 4210 */       .pattern("###")
/* 4211 */       .pattern(" | ")
/* 4212 */       .group("banner")
/* 4213 */       .unlockedBy("has_" + debug3, (CriterionTriggerInstance)has(debug2))
/* 4214 */       .save(debug0);
/*      */   }
/*      */   
/*      */   private static void stainedGlassFromGlassAndDye(Consumer<FinishedRecipe> debug0, ItemLike debug1, ItemLike debug2) {
/* 4218 */     ShapedRecipeBuilder.shaped(debug1, 8)
/* 4219 */       .define(Character.valueOf('#'), (ItemLike)Blocks.GLASS)
/* 4220 */       .define(Character.valueOf('X'), debug2)
/* 4221 */       .pattern("###")
/* 4222 */       .pattern("#X#")
/* 4223 */       .pattern("###")
/* 4224 */       .group("stained_glass")
/* 4225 */       .unlockedBy("has_glass", (CriterionTriggerInstance)has((ItemLike)Blocks.GLASS))
/* 4226 */       .save(debug0);
/*      */   }
/*      */   
/*      */   private static void stainedGlassPaneFromStainedGlass(Consumer<FinishedRecipe> debug0, ItemLike debug1, ItemLike debug2) {
/* 4230 */     ShapedRecipeBuilder.shaped(debug1, 16)
/* 4231 */       .define(Character.valueOf('#'), debug2)
/* 4232 */       .pattern("###")
/* 4233 */       .pattern("###")
/* 4234 */       .group("stained_glass_pane")
/* 4235 */       .unlockedBy("has_glass", (CriterionTriggerInstance)has(debug2))
/* 4236 */       .save(debug0);
/*      */   }
/*      */   
/*      */   private static void stainedGlassPaneFromGlassPaneAndDye(Consumer<FinishedRecipe> debug0, ItemLike debug1, ItemLike debug2) {
/* 4240 */     String debug3 = Registry.ITEM.getKey(debug1.asItem()).getPath();
/* 4241 */     String debug4 = Registry.ITEM.getKey(debug2.asItem()).getPath();
/* 4242 */     ShapedRecipeBuilder.shaped(debug1, 8)
/* 4243 */       .define(Character.valueOf('#'), (ItemLike)Blocks.GLASS_PANE)
/* 4244 */       .define(Character.valueOf('$'), debug2)
/* 4245 */       .pattern("###")
/* 4246 */       .pattern("#$#")
/* 4247 */       .pattern("###")
/* 4248 */       .group("stained_glass_pane")
/* 4249 */       .unlockedBy("has_glass_pane", (CriterionTriggerInstance)has((ItemLike)Blocks.GLASS_PANE))
/* 4250 */       .unlockedBy("has_" + debug4, (CriterionTriggerInstance)has(debug2))
/* 4251 */       .save(debug0, debug3 + "_from_glass_pane");
/*      */   }
/*      */   
/*      */   private static void coloredTerracottaFromTerracottaAndDye(Consumer<FinishedRecipe> debug0, ItemLike debug1, ItemLike debug2) {
/* 4255 */     ShapedRecipeBuilder.shaped(debug1, 8)
/* 4256 */       .define(Character.valueOf('#'), (ItemLike)Blocks.TERRACOTTA)
/* 4257 */       .define(Character.valueOf('X'), debug2)
/* 4258 */       .pattern("###")
/* 4259 */       .pattern("#X#")
/* 4260 */       .pattern("###")
/* 4261 */       .group("stained_terracotta")
/* 4262 */       .unlockedBy("has_terracotta", (CriterionTriggerInstance)has((ItemLike)Blocks.TERRACOTTA))
/* 4263 */       .save(debug0);
/*      */   }
/*      */   
/*      */   private static void concretePowder(Consumer<FinishedRecipe> debug0, ItemLike debug1, ItemLike debug2) {
/* 4267 */     ShapelessRecipeBuilder.shapeless(debug1, 8)
/* 4268 */       .requires(debug2)
/* 4269 */       .requires((ItemLike)Blocks.SAND, 4)
/* 4270 */       .requires((ItemLike)Blocks.GRAVEL, 4)
/* 4271 */       .group("concrete_powder")
/* 4272 */       .unlockedBy("has_sand", (CriterionTriggerInstance)has((ItemLike)Blocks.SAND))
/* 4273 */       .unlockedBy("has_gravel", (CriterionTriggerInstance)has((ItemLike)Blocks.GRAVEL))
/* 4274 */       .save(debug0);
/*      */   }
/*      */   
/*      */   private static void cookRecipes(Consumer<FinishedRecipe> debug0, String debug1, SimpleCookingSerializer<?> debug2, int debug3) {
/* 4278 */     SimpleCookingRecipeBuilder.cooking(Ingredient.of(new ItemLike[] { (ItemLike)Items.BEEF }, ), (ItemLike)Items.COOKED_BEEF, 0.35F, debug3, debug2)
/* 4279 */       .unlockedBy("has_beef", (CriterionTriggerInstance)has((ItemLike)Items.BEEF))
/* 4280 */       .save(debug0, "cooked_beef_from_" + debug1);
/*      */     
/* 4282 */     SimpleCookingRecipeBuilder.cooking(Ingredient.of(new ItemLike[] { (ItemLike)Items.CHICKEN }, ), (ItemLike)Items.COOKED_CHICKEN, 0.35F, debug3, debug2)
/* 4283 */       .unlockedBy("has_chicken", (CriterionTriggerInstance)has((ItemLike)Items.CHICKEN))
/* 4284 */       .save(debug0, "cooked_chicken_from_" + debug1);
/*      */     
/* 4286 */     SimpleCookingRecipeBuilder.cooking(Ingredient.of(new ItemLike[] { (ItemLike)Items.COD }, ), (ItemLike)Items.COOKED_COD, 0.35F, debug3, debug2)
/* 4287 */       .unlockedBy("has_cod", (CriterionTriggerInstance)has((ItemLike)Items.COD))
/* 4288 */       .save(debug0, "cooked_cod_from_" + debug1);
/*      */     
/* 4290 */     SimpleCookingRecipeBuilder.cooking(Ingredient.of(new ItemLike[] { (ItemLike)Blocks.KELP }, ), (ItemLike)Items.DRIED_KELP, 0.1F, debug3, debug2)
/* 4291 */       .unlockedBy("has_kelp", (CriterionTriggerInstance)has((ItemLike)Blocks.KELP))
/* 4292 */       .save(debug0, "dried_kelp_from_" + debug1);
/*      */     
/* 4294 */     SimpleCookingRecipeBuilder.cooking(Ingredient.of(new ItemLike[] { (ItemLike)Items.SALMON }, ), (ItemLike)Items.COOKED_SALMON, 0.35F, debug3, debug2)
/* 4295 */       .unlockedBy("has_salmon", (CriterionTriggerInstance)has((ItemLike)Items.SALMON))
/* 4296 */       .save(debug0, "cooked_salmon_from_" + debug1);
/*      */     
/* 4298 */     SimpleCookingRecipeBuilder.cooking(Ingredient.of(new ItemLike[] { (ItemLike)Items.MUTTON }, ), (ItemLike)Items.COOKED_MUTTON, 0.35F, debug3, debug2)
/* 4299 */       .unlockedBy("has_mutton", (CriterionTriggerInstance)has((ItemLike)Items.MUTTON))
/* 4300 */       .save(debug0, "cooked_mutton_from_" + debug1);
/*      */     
/* 4302 */     SimpleCookingRecipeBuilder.cooking(Ingredient.of(new ItemLike[] { (ItemLike)Items.PORKCHOP }, ), (ItemLike)Items.COOKED_PORKCHOP, 0.35F, debug3, debug2)
/* 4303 */       .unlockedBy("has_porkchop", (CriterionTriggerInstance)has((ItemLike)Items.PORKCHOP))
/* 4304 */       .save(debug0, "cooked_porkchop_from_" + debug1);
/*      */     
/* 4306 */     SimpleCookingRecipeBuilder.cooking(Ingredient.of(new ItemLike[] { (ItemLike)Items.POTATO }, ), (ItemLike)Items.BAKED_POTATO, 0.35F, debug3, debug2)
/* 4307 */       .unlockedBy("has_potato", (CriterionTriggerInstance)has((ItemLike)Items.POTATO))
/* 4308 */       .save(debug0, "baked_potato_from_" + debug1);
/*      */     
/* 4310 */     SimpleCookingRecipeBuilder.cooking(Ingredient.of(new ItemLike[] { (ItemLike)Items.RABBIT }, ), (ItemLike)Items.COOKED_RABBIT, 0.35F, debug3, debug2)
/* 4311 */       .unlockedBy("has_rabbit", (CriterionTriggerInstance)has((ItemLike)Items.RABBIT))
/* 4312 */       .save(debug0, "cooked_rabbit_from_" + debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   private static EnterBlockTrigger.TriggerInstance insideOf(Block debug0) {
/* 4317 */     return new EnterBlockTrigger.TriggerInstance(EntityPredicate.Composite.ANY, debug0, StatePropertiesPredicate.ANY);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static InventoryChangeTrigger.TriggerInstance has(ItemLike debug0) {
/* 4325 */     return inventoryTrigger(new ItemPredicate[] { ItemPredicate.Builder.item().of(debug0).build() });
/*      */   }
/*      */   
/*      */   private static InventoryChangeTrigger.TriggerInstance has(Tag<Item> debug0) {
/* 4329 */     return inventoryTrigger(new ItemPredicate[] { ItemPredicate.Builder.item().of(debug0).build() });
/*      */   }
/*      */   
/*      */   private static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... debug0) {
/* 4333 */     return new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, debug0);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getName() {
/* 4338 */     return "Recipes";
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\recipes\RecipeProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */