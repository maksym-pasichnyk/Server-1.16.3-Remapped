/*     */ package net.minecraft.world.item.alchemy;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PotionBrewing
/*     */ {
/*  17 */   private static final List<Mix<Potion>> POTION_MIXES = Lists.newArrayList();
/*  18 */   private static final List<Mix<Item>> CONTAINER_MIXES = Lists.newArrayList();
/*     */   
/*  20 */   private static final List<Ingredient> ALLOWED_CONTAINERS = Lists.newArrayList(); static {
/*  21 */     ALLOWED_CONTAINER = (debug0 -> {
/*     */         for (Ingredient debug2 : ALLOWED_CONTAINERS) {
/*     */           if (debug2.test(debug0))
/*     */             return true; 
/*     */         } 
/*     */         return false;
/*     */       });
/*     */   }
/*     */   private static final Predicate<ItemStack> ALLOWED_CONTAINER;
/*     */   public static boolean isIngredient(ItemStack debug0) {
/*  31 */     return (isContainerIngredient(debug0) || isPotionIngredient(debug0));
/*     */   }
/*     */   
/*     */   protected static boolean isContainerIngredient(ItemStack debug0) {
/*  35 */     for (int debug1 = 0, debug2 = CONTAINER_MIXES.size(); debug1 < debug2; debug1++) {
/*  36 */       if ((CONTAINER_MIXES.get(debug1)).ingredient.test(debug0)) {
/*  37 */         return true;
/*     */       }
/*     */     } 
/*  40 */     return false;
/*     */   }
/*     */   
/*     */   protected static boolean isPotionIngredient(ItemStack debug0) {
/*  44 */     for (int debug1 = 0, debug2 = POTION_MIXES.size(); debug1 < debug2; debug1++) {
/*  45 */       if ((POTION_MIXES.get(debug1)).ingredient.test(debug0)) {
/*  46 */         return true;
/*     */       }
/*     */     } 
/*  49 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isBrewablePotion(Potion debug0) {
/*  53 */     for (int debug1 = 0, debug2 = POTION_MIXES.size(); debug1 < debug2; debug1++) {
/*  54 */       if ((POTION_MIXES.get(debug1)).to == debug0) {
/*  55 */         return true;
/*     */       }
/*     */     } 
/*  58 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean hasMix(ItemStack debug0, ItemStack debug1) {
/*  63 */     if (!ALLOWED_CONTAINER.test(debug0)) {
/*  64 */       return false;
/*     */     }
/*     */     
/*  67 */     return (hasContainerMix(debug0, debug1) || hasPotionMix(debug0, debug1));
/*     */   }
/*     */   
/*     */   protected static boolean hasContainerMix(ItemStack debug0, ItemStack debug1) {
/*  71 */     Item debug2 = debug0.getItem();
/*  72 */     for (int debug3 = 0, debug4 = CONTAINER_MIXES.size(); debug3 < debug4; debug3++) {
/*  73 */       Mix<Item> debug5 = CONTAINER_MIXES.get(debug3);
/*  74 */       if (debug5.from == debug2 && debug5.ingredient.test(debug1)) {
/*  75 */         return true;
/*     */       }
/*     */     } 
/*  78 */     return false;
/*     */   }
/*     */   
/*     */   protected static boolean hasPotionMix(ItemStack debug0, ItemStack debug1) {
/*  82 */     Potion debug2 = PotionUtils.getPotion(debug0);
/*  83 */     for (int debug3 = 0, debug4 = POTION_MIXES.size(); debug3 < debug4; debug3++) {
/*  84 */       Mix<Potion> debug5 = POTION_MIXES.get(debug3);
/*  85 */       if (debug5.from == debug2 && debug5.ingredient.test(debug1)) {
/*  86 */         return true;
/*     */       }
/*     */     } 
/*  89 */     return false;
/*     */   }
/*     */   
/*     */   public static ItemStack mix(ItemStack debug0, ItemStack debug1) {
/*  93 */     if (!debug1.isEmpty()) {
/*  94 */       Potion debug2 = PotionUtils.getPotion(debug1);
/*  95 */       Item debug3 = debug1.getItem(); int debug4, debug5;
/*  96 */       for (debug4 = 0, debug5 = CONTAINER_MIXES.size(); debug4 < debug5; debug4++) {
/*  97 */         Mix<Item> debug6 = CONTAINER_MIXES.get(debug4);
/*  98 */         if (debug6.from == debug3 && debug6.ingredient.test(debug0)) {
/*  99 */           return PotionUtils.setPotion(new ItemStack((ItemLike)debug6.to), debug2);
/*     */         }
/*     */       } 
/*     */       
/* 103 */       for (debug4 = 0, debug5 = POTION_MIXES.size(); debug4 < debug5; debug4++) {
/* 104 */         Mix<Potion> debug6 = POTION_MIXES.get(debug4);
/* 105 */         if (debug6.from == debug2 && debug6.ingredient.test(debug0)) {
/* 106 */           return PotionUtils.setPotion(new ItemStack((ItemLike)debug3), (Potion)debug6.to);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 111 */     return debug1;
/*     */   }
/*     */   
/*     */   public static void bootStrap() {
/* 115 */     addContainer(Items.POTION);
/* 116 */     addContainer(Items.SPLASH_POTION);
/* 117 */     addContainer(Items.LINGERING_POTION);
/*     */     
/* 119 */     addContainerRecipe(Items.POTION, Items.GUNPOWDER, Items.SPLASH_POTION);
/* 120 */     addContainerRecipe(Items.SPLASH_POTION, Items.DRAGON_BREATH, Items.LINGERING_POTION);
/*     */     
/* 122 */     addMix(Potions.WATER, Items.GLISTERING_MELON_SLICE, Potions.MUNDANE);
/* 123 */     addMix(Potions.WATER, Items.GHAST_TEAR, Potions.MUNDANE);
/* 124 */     addMix(Potions.WATER, Items.RABBIT_FOOT, Potions.MUNDANE);
/* 125 */     addMix(Potions.WATER, Items.BLAZE_POWDER, Potions.MUNDANE);
/* 126 */     addMix(Potions.WATER, Items.SPIDER_EYE, Potions.MUNDANE);
/* 127 */     addMix(Potions.WATER, Items.SUGAR, Potions.MUNDANE);
/* 128 */     addMix(Potions.WATER, Items.MAGMA_CREAM, Potions.MUNDANE);
/*     */     
/* 130 */     addMix(Potions.WATER, Items.GLOWSTONE_DUST, Potions.THICK);
/*     */     
/* 132 */     addMix(Potions.WATER, Items.REDSTONE, Potions.MUNDANE);
/*     */     
/* 134 */     addMix(Potions.WATER, Items.NETHER_WART, Potions.AWKWARD);
/*     */     
/* 136 */     addMix(Potions.AWKWARD, Items.GOLDEN_CARROT, Potions.NIGHT_VISION);
/* 137 */     addMix(Potions.NIGHT_VISION, Items.REDSTONE, Potions.LONG_NIGHT_VISION);
/*     */     
/* 139 */     addMix(Potions.NIGHT_VISION, Items.FERMENTED_SPIDER_EYE, Potions.INVISIBILITY);
/* 140 */     addMix(Potions.LONG_NIGHT_VISION, Items.FERMENTED_SPIDER_EYE, Potions.LONG_INVISIBILITY);
/*     */     
/* 142 */     addMix(Potions.INVISIBILITY, Items.REDSTONE, Potions.LONG_INVISIBILITY);
/*     */     
/* 144 */     addMix(Potions.AWKWARD, Items.MAGMA_CREAM, Potions.FIRE_RESISTANCE);
/* 145 */     addMix(Potions.FIRE_RESISTANCE, Items.REDSTONE, Potions.LONG_FIRE_RESISTANCE);
/*     */     
/* 147 */     addMix(Potions.AWKWARD, Items.RABBIT_FOOT, Potions.LEAPING);
/* 148 */     addMix(Potions.LEAPING, Items.REDSTONE, Potions.LONG_LEAPING);
/* 149 */     addMix(Potions.LEAPING, Items.GLOWSTONE_DUST, Potions.STRONG_LEAPING);
/*     */     
/* 151 */     addMix(Potions.LEAPING, Items.FERMENTED_SPIDER_EYE, Potions.SLOWNESS);
/* 152 */     addMix(Potions.LONG_LEAPING, Items.FERMENTED_SPIDER_EYE, Potions.LONG_SLOWNESS);
/*     */     
/* 154 */     addMix(Potions.SLOWNESS, Items.REDSTONE, Potions.LONG_SLOWNESS);
/*     */     
/* 156 */     addMix(Potions.SLOWNESS, Items.GLOWSTONE_DUST, Potions.STRONG_SLOWNESS);
/* 157 */     addMix(Potions.AWKWARD, Items.TURTLE_HELMET, Potions.TURTLE_MASTER);
/* 158 */     addMix(Potions.TURTLE_MASTER, Items.REDSTONE, Potions.LONG_TURTLE_MASTER);
/* 159 */     addMix(Potions.TURTLE_MASTER, Items.GLOWSTONE_DUST, Potions.STRONG_TURTLE_MASTER);
/*     */     
/* 161 */     addMix(Potions.SWIFTNESS, Items.FERMENTED_SPIDER_EYE, Potions.SLOWNESS);
/* 162 */     addMix(Potions.LONG_SWIFTNESS, Items.FERMENTED_SPIDER_EYE, Potions.LONG_SLOWNESS);
/*     */     
/* 164 */     addMix(Potions.AWKWARD, Items.SUGAR, Potions.SWIFTNESS);
/* 165 */     addMix(Potions.SWIFTNESS, Items.REDSTONE, Potions.LONG_SWIFTNESS);
/* 166 */     addMix(Potions.SWIFTNESS, Items.GLOWSTONE_DUST, Potions.STRONG_SWIFTNESS);
/*     */     
/* 168 */     addMix(Potions.AWKWARD, Items.PUFFERFISH, Potions.WATER_BREATHING);
/* 169 */     addMix(Potions.WATER_BREATHING, Items.REDSTONE, Potions.LONG_WATER_BREATHING);
/*     */     
/* 171 */     addMix(Potions.AWKWARD, Items.GLISTERING_MELON_SLICE, Potions.HEALING);
/* 172 */     addMix(Potions.HEALING, Items.GLOWSTONE_DUST, Potions.STRONG_HEALING);
/*     */     
/* 174 */     addMix(Potions.HEALING, Items.FERMENTED_SPIDER_EYE, Potions.HARMING);
/* 175 */     addMix(Potions.STRONG_HEALING, Items.FERMENTED_SPIDER_EYE, Potions.STRONG_HARMING);
/*     */     
/* 177 */     addMix(Potions.HARMING, Items.GLOWSTONE_DUST, Potions.STRONG_HARMING);
/*     */     
/* 179 */     addMix(Potions.POISON, Items.FERMENTED_SPIDER_EYE, Potions.HARMING);
/* 180 */     addMix(Potions.LONG_POISON, Items.FERMENTED_SPIDER_EYE, Potions.HARMING);
/* 181 */     addMix(Potions.STRONG_POISON, Items.FERMENTED_SPIDER_EYE, Potions.STRONG_HARMING);
/*     */     
/* 183 */     addMix(Potions.AWKWARD, Items.SPIDER_EYE, Potions.POISON);
/* 184 */     addMix(Potions.POISON, Items.REDSTONE, Potions.LONG_POISON);
/* 185 */     addMix(Potions.POISON, Items.GLOWSTONE_DUST, Potions.STRONG_POISON);
/*     */     
/* 187 */     addMix(Potions.AWKWARD, Items.GHAST_TEAR, Potions.REGENERATION);
/* 188 */     addMix(Potions.REGENERATION, Items.REDSTONE, Potions.LONG_REGENERATION);
/* 189 */     addMix(Potions.REGENERATION, Items.GLOWSTONE_DUST, Potions.STRONG_REGENERATION);
/*     */     
/* 191 */     addMix(Potions.AWKWARD, Items.BLAZE_POWDER, Potions.STRENGTH);
/* 192 */     addMix(Potions.STRENGTH, Items.REDSTONE, Potions.LONG_STRENGTH);
/* 193 */     addMix(Potions.STRENGTH, Items.GLOWSTONE_DUST, Potions.STRONG_STRENGTH);
/*     */     
/* 195 */     addMix(Potions.WATER, Items.FERMENTED_SPIDER_EYE, Potions.WEAKNESS);
/* 196 */     addMix(Potions.WEAKNESS, Items.REDSTONE, Potions.LONG_WEAKNESS);
/*     */     
/* 198 */     addMix(Potions.AWKWARD, Items.PHANTOM_MEMBRANE, Potions.SLOW_FALLING);
/* 199 */     addMix(Potions.SLOW_FALLING, Items.REDSTONE, Potions.LONG_SLOW_FALLING);
/*     */   }
/*     */   
/*     */   private static void addContainerRecipe(Item debug0, Item debug1, Item debug2) {
/* 203 */     if (!(debug0 instanceof net.minecraft.world.item.PotionItem)) {
/* 204 */       throw new IllegalArgumentException("Expected a potion, got: " + Registry.ITEM.getKey(debug0));
/*     */     }
/* 206 */     if (!(debug2 instanceof net.minecraft.world.item.PotionItem)) {
/* 207 */       throw new IllegalArgumentException("Expected a potion, got: " + Registry.ITEM.getKey(debug2));
/*     */     }
/* 209 */     CONTAINER_MIXES.add(new Mix<>(debug0, Ingredient.of(new ItemLike[] { (ItemLike)debug1 }, ), debug2));
/*     */   }
/*     */   
/*     */   private static void addContainer(Item debug0) {
/* 213 */     if (!(debug0 instanceof net.minecraft.world.item.PotionItem)) {
/* 214 */       throw new IllegalArgumentException("Expected a potion, got: " + Registry.ITEM.getKey(debug0));
/*     */     }
/* 216 */     ALLOWED_CONTAINERS.add(Ingredient.of(new ItemLike[] { (ItemLike)debug0 }));
/*     */   }
/*     */   
/*     */   private static void addMix(Potion debug0, Item debug1, Potion debug2) {
/* 220 */     POTION_MIXES.add(new Mix<>(debug0, Ingredient.of(new ItemLike[] { (ItemLike)debug1 }, ), debug2));
/*     */   }
/*     */   
/*     */   static class Mix<T> {
/*     */     private final T from;
/*     */     private final Ingredient ingredient;
/*     */     private final T to;
/*     */     
/*     */     public Mix(T debug1, Ingredient debug2, T debug3) {
/* 229 */       this.from = debug1;
/* 230 */       this.ingredient = debug2;
/* 231 */       this.to = debug3;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\alchemy\PotionBrewing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */