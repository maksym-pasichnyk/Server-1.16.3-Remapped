/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class RecipeManager
/*     */   extends SimpleJsonResourceReloadListener {
/*  36 */   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
/*  37 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  39 */   private Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipes = (Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>>)ImmutableMap.of();
/*     */   private boolean hasErrors;
/*     */   
/*     */   public RecipeManager() {
/*  43 */     super(GSON, "recipes");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void apply(Map<ResourceLocation, JsonElement> debug1, ResourceManager debug2, ProfilerFiller debug3) {
/*  48 */     this.hasErrors = false;
/*  49 */     Map<RecipeType<?>, ImmutableMap.Builder<ResourceLocation, Recipe<?>>> debug4 = Maps.newHashMap();
/*     */     
/*  51 */     for (Map.Entry<ResourceLocation, JsonElement> debug6 : debug1.entrySet()) {
/*  52 */       ResourceLocation debug7 = debug6.getKey();
/*     */       try {
/*  54 */         Recipe<?> debug8 = fromJson(debug7, GsonHelper.convertToJsonObject(debug6.getValue(), "top element"));
/*  55 */         ((ImmutableMap.Builder)debug4.computeIfAbsent(debug8.getType(), debug0 -> ImmutableMap.builder())).put(debug7, debug8);
/*  56 */       } catch (JsonParseException|IllegalArgumentException debug8) {
/*  57 */         LOGGER.error("Parsing error loading recipe {}", debug7, debug8);
/*     */       } 
/*     */     } 
/*     */     
/*  61 */     this.recipes = (Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>>)debug4.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, debug0 -> ((ImmutableMap.Builder)debug0.getValue()).build()));
/*  62 */     LOGGER.info("Loaded {} recipes", Integer.valueOf(debug4.size()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <C extends Container, T extends Recipe<C>> Optional<T> getRecipeFor(RecipeType<T> debug1, C debug2, Level debug3) {
/*  70 */     return byType(debug1).values().stream().flatMap(debug3 -> Util.toStream(debug0.tryMatch(debug3, debug1, debug2))).findFirst();
/*     */   }
/*     */   
/*     */   public <C extends Container, T extends Recipe<C>> List<T> getAllRecipesFor(RecipeType<T> debug1) {
/*  74 */     return (List<T>)byType(debug1).values()
/*  75 */       .stream()
/*  76 */       .map(debug0 -> debug0)
/*  77 */       .collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public <C extends Container, T extends Recipe<C>> List<T> getRecipesFor(RecipeType<T> debug1, C debug2, Level debug3) {
/*  81 */     return (List<T>)byType(debug1).values()
/*  82 */       .stream()
/*  83 */       .flatMap(debug3 -> Util.toStream(debug0.tryMatch(debug3, debug1, debug2)))
/*  84 */       .sorted(Comparator.comparing(debug0 -> debug0.getResultItem().getDescriptionId()))
/*  85 */       .collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private <C extends Container, T extends Recipe<C>> Map<ResourceLocation, Recipe<C>> byType(RecipeType<T> debug1) {
/*  91 */     return (Map<ResourceLocation, Recipe<C>>)this.recipes.getOrDefault(debug1, Collections.emptyMap());
/*     */   }
/*     */   
/*     */   public <C extends Container, T extends Recipe<C>> NonNullList<ItemStack> getRemainingItemsFor(RecipeType<T> debug1, C debug2, Level debug3) {
/*  95 */     Optional<T> debug4 = getRecipeFor(debug1, debug2, debug3);
/*  96 */     if (debug4.isPresent()) {
/*  97 */       return ((Recipe<C>)debug4.get()).getRemainingItems(debug2);
/*     */     }
/*     */     
/* 100 */     NonNullList<ItemStack> debug5 = NonNullList.withSize(debug2.getContainerSize(), ItemStack.EMPTY);
/* 101 */     for (int debug6 = 0; debug6 < debug5.size(); debug6++) {
/* 102 */       debug5.set(debug6, debug2.getItem(debug6));
/*     */     }
/* 104 */     return debug5;
/*     */   }
/*     */   
/*     */   public Optional<? extends Recipe<?>> byKey(ResourceLocation debug1) {
/* 108 */     return this.recipes.values().stream().map(debug1 -> (Recipe)debug1.get(debug0)).filter(Objects::nonNull).findFirst();
/*     */   }
/*     */   
/*     */   public Collection<Recipe<?>> getRecipes() {
/* 112 */     return (Collection<Recipe<?>>)this.recipes.values().stream().flatMap(debug0 -> debug0.values().stream()).collect(Collectors.toSet());
/*     */   }
/*     */   
/*     */   public Stream<ResourceLocation> getRecipeIds() {
/* 116 */     return this.recipes.values().stream().flatMap(debug0 -> debug0.keySet().stream());
/*     */   }
/*     */   
/*     */   public static Recipe<?> fromJson(ResourceLocation debug0, JsonObject debug1) {
/* 120 */     String debug2 = GsonHelper.getAsString(debug1, "type");
/* 121 */     return ((RecipeSerializer<Recipe<?>>)Registry.RECIPE_SERIALIZER.getOptional(new ResourceLocation(debug2))
/* 122 */       .orElseThrow(() -> new JsonSyntaxException("Invalid or unsupported recipe type '" + debug0 + "'")))
/* 123 */       .fromJson(debug0, debug1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\RecipeManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */