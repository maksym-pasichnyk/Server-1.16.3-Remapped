/*    */ package net.minecraft.world.item.crafting;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ 
/*    */ public interface RecipeSerializer<T extends Recipe<?>> {
/*  9 */   public static final RecipeSerializer<ShapedRecipe> SHAPED_RECIPE = register("crafting_shaped", new ShapedRecipe.Serializer());
/* 10 */   public static final RecipeSerializer<ShapelessRecipe> SHAPELESS_RECIPE = register("crafting_shapeless", new ShapelessRecipe.Serializer());
/* 11 */   public static final SimpleRecipeSerializer<ArmorDyeRecipe> ARMOR_DYE = register("crafting_special_armordye", new SimpleRecipeSerializer<>(ArmorDyeRecipe::new));
/* 12 */   public static final SimpleRecipeSerializer<BookCloningRecipe> BOOK_CLONING = register("crafting_special_bookcloning", new SimpleRecipeSerializer<>(BookCloningRecipe::new));
/* 13 */   public static final SimpleRecipeSerializer<MapCloningRecipe> MAP_CLONING = register("crafting_special_mapcloning", new SimpleRecipeSerializer<>(MapCloningRecipe::new));
/* 14 */   public static final SimpleRecipeSerializer<MapExtendingRecipe> MAP_EXTENDING = register("crafting_special_mapextending", new SimpleRecipeSerializer<>(MapExtendingRecipe::new));
/* 15 */   public static final SimpleRecipeSerializer<FireworkRocketRecipe> FIREWORK_ROCKET = register("crafting_special_firework_rocket", new SimpleRecipeSerializer<>(FireworkRocketRecipe::new));
/* 16 */   public static final SimpleRecipeSerializer<FireworkStarRecipe> FIREWORK_STAR = register("crafting_special_firework_star", new SimpleRecipeSerializer<>(FireworkStarRecipe::new));
/* 17 */   public static final SimpleRecipeSerializer<FireworkStarFadeRecipe> FIREWORK_STAR_FADE = register("crafting_special_firework_star_fade", new SimpleRecipeSerializer<>(FireworkStarFadeRecipe::new));
/* 18 */   public static final SimpleRecipeSerializer<TippedArrowRecipe> TIPPED_ARROW = register("crafting_special_tippedarrow", new SimpleRecipeSerializer<>(TippedArrowRecipe::new));
/* 19 */   public static final SimpleRecipeSerializer<BannerDuplicateRecipe> BANNER_DUPLICATE = register("crafting_special_bannerduplicate", new SimpleRecipeSerializer<>(BannerDuplicateRecipe::new));
/* 20 */   public static final SimpleRecipeSerializer<ShieldDecorationRecipe> SHIELD_DECORATION = register("crafting_special_shielddecoration", new SimpleRecipeSerializer<>(ShieldDecorationRecipe::new));
/* 21 */   public static final SimpleRecipeSerializer<ShulkerBoxColoring> SHULKER_BOX_COLORING = register("crafting_special_shulkerboxcoloring", new SimpleRecipeSerializer<>(ShulkerBoxColoring::new));
/* 22 */   public static final SimpleRecipeSerializer<SuspiciousStewRecipe> SUSPICIOUS_STEW = register("crafting_special_suspiciousstew", new SimpleRecipeSerializer<>(SuspiciousStewRecipe::new));
/* 23 */   public static final SimpleRecipeSerializer<RepairItemRecipe> REPAIR_ITEM = register("crafting_special_repairitem", new SimpleRecipeSerializer<>(RepairItemRecipe::new));
/* 24 */   public static final SimpleCookingSerializer<SmeltingRecipe> SMELTING_RECIPE = register("smelting", new SimpleCookingSerializer<>(SmeltingRecipe::new, 200));
/* 25 */   public static final SimpleCookingSerializer<BlastingRecipe> BLASTING_RECIPE = register("blasting", new SimpleCookingSerializer<>(BlastingRecipe::new, 100));
/* 26 */   public static final SimpleCookingSerializer<SmokingRecipe> SMOKING_RECIPE = register("smoking", new SimpleCookingSerializer<>(SmokingRecipe::new, 100));
/* 27 */   public static final SimpleCookingSerializer<CampfireCookingRecipe> CAMPFIRE_COOKING_RECIPE = register("campfire_cooking", new SimpleCookingSerializer<>(CampfireCookingRecipe::new, 100));
/* 28 */   public static final RecipeSerializer<StonecutterRecipe> STONECUTTER = register("stonecutting", new SingleItemRecipe.Serializer<>(StonecutterRecipe::new));
/* 29 */   public static final RecipeSerializer<UpgradeRecipe> SMITHING = register("smithing", new UpgradeRecipe.Serializer());
/*    */   
/*    */   T fromJson(ResourceLocation paramResourceLocation, JsonObject paramJsonObject);
/*    */   
/*    */   T fromNetwork(ResourceLocation paramResourceLocation, FriendlyByteBuf paramFriendlyByteBuf);
/*    */   
/*    */   void toNetwork(FriendlyByteBuf paramFriendlyByteBuf, T paramT);
/*    */   
/*    */   static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String debug0, S debug1) {
/* 38 */     return (S)Registry.register(Registry.RECIPE_SERIALIZER, debug0, debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\RecipeSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */