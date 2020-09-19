/*    */ package net.minecraft.world.item;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.tags.ItemTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.util.LazyLoadedValue;
/*    */ import net.minecraft.world.item.crafting.Ingredient;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public enum Tiers implements Tier {
/* 10 */   WOOD(0, 59, 2.0F, 0.0F, 15, () -> Ingredient.of((Tag)ItemTags.PLANKS)),
/* 11 */   STONE(1, 131, 4.0F, 1.0F, 5, () -> Ingredient.of((Tag)ItemTags.STONE_TOOL_MATERIALS)),
/* 12 */   IRON(2, 250, 6.0F, 2.0F, 14, () -> Ingredient.of(new ItemLike[] { Items.IRON_INGOT })),
/* 13 */   DIAMOND(3, 1561, 8.0F, 3.0F, 10, () -> Ingredient.of(new ItemLike[] { Items.DIAMOND })),
/* 14 */   GOLD(0, 32, 12.0F, 0.0F, 22, () -> Ingredient.of(new ItemLike[] { Items.GOLD_INGOT })),
/* 15 */   NETHERITE(4, 2031, 9.0F, 4.0F, 15, () -> Ingredient.of(new ItemLike[] { Items.NETHERITE_INGOT }));
/*    */   
/*    */   private final int level;
/*    */   
/*    */   private final int uses;
/*    */   private final float speed;
/*    */   private final float damage;
/*    */   private final int enchantmentValue;
/*    */   private final LazyLoadedValue<Ingredient> repairIngredient;
/*    */   
/*    */   Tiers(int debug3, int debug4, float debug5, float debug6, int debug7, Supplier<Ingredient> debug8) {
/* 26 */     this.level = debug3;
/* 27 */     this.uses = debug4;
/* 28 */     this.speed = debug5;
/* 29 */     this.damage = debug6;
/* 30 */     this.enchantmentValue = debug7;
/* 31 */     this.repairIngredient = new LazyLoadedValue(debug8);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getUses() {
/* 36 */     return this.uses;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getSpeed() {
/* 41 */     return this.speed;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getAttackDamageBonus() {
/* 46 */     return this.damage;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLevel() {
/* 51 */     return this.level;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getEnchantmentValue() {
/* 56 */     return this.enchantmentValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public Ingredient getRepairIngredient() {
/* 61 */     return (Ingredient)this.repairIngredient.get();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\Tiers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */