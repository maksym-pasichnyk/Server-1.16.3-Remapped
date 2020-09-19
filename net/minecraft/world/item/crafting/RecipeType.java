/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ public interface RecipeType<T extends Recipe<?>>
/*    */ {
/* 11 */   public static final RecipeType<CraftingRecipe> CRAFTING = register("crafting");
/* 12 */   public static final RecipeType<SmeltingRecipe> SMELTING = register("smelting");
/* 13 */   public static final RecipeType<BlastingRecipe> BLASTING = register("blasting");
/* 14 */   public static final RecipeType<SmokingRecipe> SMOKING = register("smoking");
/* 15 */   public static final RecipeType<CampfireCookingRecipe> CAMPFIRE_COOKING = register("campfire_cooking");
/* 16 */   public static final RecipeType<StonecutterRecipe> STONECUTTING = register("stonecutting");
/* 17 */   public static final RecipeType<UpgradeRecipe> SMITHING = register("smithing");
/*    */   
/*    */   static <T extends Recipe<?>> RecipeType<T> register(final String name) {
/* 20 */     return (RecipeType<T>)Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(name), new RecipeType<T>()
/*    */         {
/*    */           public String toString() {
/* 23 */             return name;
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   default <C extends net.minecraft.world.Container> Optional<T> tryMatch(Recipe<C> debug1, Level debug2, C debug3) {
/* 30 */     return debug1.matches(debug3, debug2) ? Optional.<T>of((T)debug1) : Optional.<T>empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\RecipeType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */