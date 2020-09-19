/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.LazyLoadedValue;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.item.crafting.Ingredient;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public enum ArmorMaterials implements ArmorMaterial {
/* 12 */   LEATHER("leather", 5, new int[] { 1, 2, 3, 1 }, 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> Ingredient.of(new ItemLike[] { Items.LEATHER })),
/* 13 */   CHAIN("chainmail", 15, new int[] { 1, 4, 5, 2 }, 12, SoundEvents.ARMOR_EQUIP_CHAIN, 0.0F, 0.0F, () -> Ingredient.of(new ItemLike[] { Items.IRON_INGOT })),
/* 14 */   IRON("iron", 15, new int[] { 2, 5, 6, 2 }, 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> Ingredient.of(new ItemLike[] { Items.IRON_INGOT })),
/* 15 */   GOLD("gold", 7, new int[] { 1, 3, 5, 2 }, 25, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, 0.0F, () -> Ingredient.of(new ItemLike[] { Items.GOLD_INGOT })),
/* 16 */   DIAMOND("diamond", 33, new int[] { 3, 6, 8, 3 }, 10, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, 0.0F, () -> Ingredient.of(new ItemLike[] { Items.DIAMOND })),
/* 17 */   TURTLE("turtle", 25, new int[] { 2, 5, 6, 2 }, 9, SoundEvents.ARMOR_EQUIP_TURTLE, 0.0F, 0.0F, () -> Ingredient.of(new ItemLike[] { Items.SCUTE })),
/* 18 */   NETHERITE("netherite", 37, new int[] { 3, 6, 8, 3 }, 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, () -> Ingredient.of(new ItemLike[] { Items.NETHERITE_INGOT }));
/*    */   private static final int[] HEALTH_PER_SLOT;
/*    */   
/*    */   static {
/* 22 */     HEALTH_PER_SLOT = new int[] { 13, 15, 16, 11 };
/*    */   }
/*    */ 
/*    */   
/*    */   private final String name;
/*    */   private final int durabilityMultiplier;
/*    */   private final int[] slotProtections;
/*    */   private final int enchantmentValue;
/*    */   private final SoundEvent sound;
/*    */   private final float toughness;
/*    */   private final float knockbackResistance;
/*    */   private final LazyLoadedValue<Ingredient> repairIngredient;
/*    */   
/*    */   ArmorMaterials(String debug3, int debug4, int[] debug5, int debug6, SoundEvent debug7, float debug8, float debug9, Supplier<Ingredient> debug10) {
/* 36 */     this.name = debug3;
/* 37 */     this.durabilityMultiplier = debug4;
/* 38 */     this.slotProtections = debug5;
/* 39 */     this.enchantmentValue = debug6;
/* 40 */     this.sound = debug7;
/* 41 */     this.toughness = debug8;
/* 42 */     this.knockbackResistance = debug9;
/* 43 */     this.repairIngredient = new LazyLoadedValue(debug10);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDurabilityForSlot(EquipmentSlot debug1) {
/* 48 */     return HEALTH_PER_SLOT[debug1.getIndex()] * this.durabilityMultiplier;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDefenseForSlot(EquipmentSlot debug1) {
/* 53 */     return this.slotProtections[debug1.getIndex()];
/*    */   }
/*    */ 
/*    */   
/*    */   public int getEnchantmentValue() {
/* 58 */     return this.enchantmentValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public SoundEvent getEquipSound() {
/* 63 */     return this.sound;
/*    */   }
/*    */ 
/*    */   
/*    */   public Ingredient getRepairIngredient() {
/* 68 */     return (Ingredient)this.repairIngredient.get();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float getToughness() {
/* 78 */     return this.toughness;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getKnockbackResistance() {
/* 83 */     return this.knockbackResistance;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\ArmorMaterials.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */