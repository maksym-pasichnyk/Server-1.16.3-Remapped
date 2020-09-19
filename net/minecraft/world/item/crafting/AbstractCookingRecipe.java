/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class AbstractCookingRecipe
/*    */   implements Recipe<Container> {
/*    */   protected final RecipeType<?> type;
/*    */   protected final ResourceLocation id;
/*    */   protected final String group;
/*    */   protected final Ingredient ingredient;
/*    */   protected final ItemStack result;
/*    */   protected final float experience;
/*    */   protected final int cookingTime;
/*    */   
/*    */   public AbstractCookingRecipe(RecipeType<?> debug1, ResourceLocation debug2, String debug3, Ingredient debug4, ItemStack debug5, float debug6, int debug7) {
/* 20 */     this.type = debug1;
/* 21 */     this.id = debug2;
/* 22 */     this.group = debug3;
/* 23 */     this.ingredient = debug4;
/* 24 */     this.result = debug5;
/* 25 */     this.experience = debug6;
/* 26 */     this.cookingTime = debug7;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(Container debug1, Level debug2) {
/* 31 */     return this.ingredient.test(debug1.getItem(0));
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack assemble(Container debug1) {
/* 36 */     return this.result.copy();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NonNullList<Ingredient> getIngredients() {
/* 46 */     NonNullList<Ingredient> debug1 = NonNullList.create();
/* 47 */     debug1.add(this.ingredient);
/* 48 */     return debug1;
/*    */   }
/*    */   
/*    */   public float getExperience() {
/* 52 */     return this.experience;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack getResultItem() {
/* 57 */     return this.result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getCookingTime() {
/* 66 */     return this.cookingTime;
/*    */   }
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 71 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeType<?> getType() {
/* 76 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\AbstractCookingRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */