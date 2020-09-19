/*    */ package net.minecraft.world.food;
/*    */ 
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ 
/*    */ 
/*    */ public class Foods
/*    */ {
/*  9 */   public static final FoodProperties APPLE = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.3F).build();
/* 10 */   public static final FoodProperties BAKED_POTATO = (new FoodProperties.Builder()).nutrition(5).saturationMod(0.6F).build();
/* 11 */   public static final FoodProperties BEEF = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.3F).meat().build();
/* 12 */   public static final FoodProperties BEETROOT = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.6F).build();
/* 13 */   public static final FoodProperties BEETROOT_SOUP = stew(6);
/* 14 */   public static final FoodProperties BREAD = (new FoodProperties.Builder()).nutrition(5).saturationMod(0.6F).build();
/* 15 */   public static final FoodProperties CARROT = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.6F).build();
/* 16 */   public static final FoodProperties CHICKEN = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F)
/* 17 */     .effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F)
/* 18 */     .meat().build();
/* 19 */   public static final FoodProperties CHORUS_FRUIT = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.3F).alwaysEat().build();
/* 20 */   public static final FoodProperties COD = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.1F).build();
/* 21 */   public static final FoodProperties COOKED_BEEF = (new FoodProperties.Builder()).nutrition(8).saturationMod(0.8F).meat().build();
/* 22 */   public static final FoodProperties COOKED_CHICKEN = (new FoodProperties.Builder()).nutrition(6).saturationMod(0.6F).meat().build();
/* 23 */   public static final FoodProperties COOKED_COD = (new FoodProperties.Builder()).nutrition(5).saturationMod(0.6F).build();
/* 24 */   public static final FoodProperties COOKED_MUTTON = (new FoodProperties.Builder()).nutrition(6).saturationMod(0.8F).meat().build();
/* 25 */   public static final FoodProperties COOKED_PORKCHOP = (new FoodProperties.Builder()).nutrition(8).saturationMod(0.8F).meat().build();
/* 26 */   public static final FoodProperties COOKED_RABBIT = (new FoodProperties.Builder()).nutrition(5).saturationMod(0.6F).meat().build();
/* 27 */   public static final FoodProperties COOKED_SALMON = (new FoodProperties.Builder()).nutrition(6).saturationMod(0.8F).build();
/* 28 */   public static final FoodProperties COOKIE = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.1F).build();
/* 29 */   public static final FoodProperties DRIED_KELP = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.3F).fast().build();
/* 30 */   public static final FoodProperties ENCHANTED_GOLDEN_APPLE = (new FoodProperties.Builder()).nutrition(4).saturationMod(1.2F)
/* 31 */     .effect(new MobEffectInstance(MobEffects.REGENERATION, 400, 1), 1.0F)
/* 32 */     .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0), 1.0F)
/* 33 */     .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0), 1.0F)
/* 34 */     .effect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3), 1.0F)
/* 35 */     .alwaysEat().build();
/* 36 */   public static final FoodProperties GOLDEN_APPLE = (new FoodProperties.Builder()).nutrition(4).saturationMod(1.2F)
/* 37 */     .effect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1), 1.0F)
/* 38 */     .effect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0), 1.0F)
/* 39 */     .alwaysEat().build();
/* 40 */   public static final FoodProperties GOLDEN_CARROT = (new FoodProperties.Builder()).nutrition(6).saturationMod(1.2F).build();
/* 41 */   public static final FoodProperties HONEY_BOTTLE = (new FoodProperties.Builder()).nutrition(6).saturationMod(0.1F).build();
/* 42 */   public static final FoodProperties MELON_SLICE = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F).build();
/* 43 */   public static final FoodProperties MUSHROOM_STEW = stew(6);
/* 44 */   public static final FoodProperties MUTTON = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F).meat().build();
/* 45 */   public static final FoodProperties POISONOUS_POTATO = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F)
/* 46 */     .effect(new MobEffectInstance(MobEffects.POISON, 100, 0), 0.6F)
/* 47 */     .build();
/* 48 */   public static final FoodProperties PORKCHOP = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.3F).meat().build();
/* 49 */   public static final FoodProperties POTATO = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.3F).build();
/* 50 */   public static final FoodProperties PUFFERFISH = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.1F)
/* 51 */     .effect(new MobEffectInstance(MobEffects.POISON, 1200, 3), 1.0F)
/* 52 */     .effect(new MobEffectInstance(MobEffects.HUNGER, 300, 2), 1.0F)
/* 53 */     .effect(new MobEffectInstance(MobEffects.CONFUSION, 300, 0), 1.0F)
/* 54 */     .build();
/* 55 */   public static final FoodProperties PUMPKIN_PIE = (new FoodProperties.Builder()).nutrition(8).saturationMod(0.3F).build();
/* 56 */   public static final FoodProperties RABBIT = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.3F).meat().build();
/* 57 */   public static final FoodProperties RABBIT_STEW = stew(10);
/* 58 */   public static final FoodProperties ROTTEN_FLESH = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.1F)
/* 59 */     .effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.8F)
/* 60 */     .meat().build();
/* 61 */   public static final FoodProperties SALMON = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.1F).build();
/* 62 */   public static final FoodProperties SPIDER_EYE = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.8F)
/* 63 */     .effect(new MobEffectInstance(MobEffects.POISON, 100, 0), 1.0F)
/* 64 */     .build();
/* 65 */   public static final FoodProperties SUSPICIOUS_STEW = stew(6);
/* 66 */   public static final FoodProperties SWEET_BERRIES = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.1F).build();
/* 67 */   public static final FoodProperties TROPICAL_FISH = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.1F).build();
/*    */   
/*    */   private static FoodProperties stew(int debug0) {
/* 70 */     return (new FoodProperties.Builder()).nutrition(debug0).saturationMod(0.6F).build();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\food\Foods.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */