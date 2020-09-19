/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.level.GameRules;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public interface RecipeHolder
/*    */ {
/*    */   void setRecipeUsed(@Nullable Recipe<?> paramRecipe);
/*    */   
/*    */   @Nullable
/*    */   Recipe<?> getRecipeUsed();
/*    */   
/*    */   default void awardUsedRecipes(Player debug1) {
/* 19 */     Recipe<?> debug2 = getRecipeUsed();
/* 20 */     if (debug2 != null && !debug2.isSpecial()) {
/* 21 */       debug1.awardRecipes(Collections.singleton(debug2));
/* 22 */       setRecipeUsed(null);
/*    */     } 
/*    */   }
/*    */   
/*    */   default boolean setRecipeUsed(Level debug1, ServerPlayer debug2, Recipe<?> debug3) {
/* 27 */     if (debug3.isSpecial() || !debug1.getGameRules().getBoolean(GameRules.RULE_LIMITED_CRAFTING) || debug2.getRecipeBook().contains(debug3)) {
/* 28 */       setRecipeUsed(debug3);
/* 29 */       return true;
/*    */     } 
/*    */     
/* 32 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\RecipeHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */