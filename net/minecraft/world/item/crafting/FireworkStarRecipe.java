/*     */ package net.minecraft.world.item.crafting;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.inventory.CraftingContainer;
/*     */ import net.minecraft.world.item.DyeItem;
/*     */ import net.minecraft.world.item.FireworkRocketItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class FireworkStarRecipe extends CustomRecipe {
/*  20 */   private static final Ingredient SHAPE_INGREDIENT = Ingredient.of(new ItemLike[] { (ItemLike)Items.FIRE_CHARGE, (ItemLike)Items.FEATHER, (ItemLike)Items.GOLD_NUGGET, (ItemLike)Items.SKELETON_SKULL, (ItemLike)Items.WITHER_SKELETON_SKULL, (ItemLike)Items.CREEPER_HEAD, (ItemLike)Items.PLAYER_HEAD, (ItemLike)Items.DRAGON_HEAD, (ItemLike)Items.ZOMBIE_HEAD });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  31 */   private static final Ingredient TRAIL_INGREDIENT = Ingredient.of(new ItemLike[] { (ItemLike)Items.DIAMOND });
/*  32 */   private static final Ingredient FLICKER_INGREDIENT = Ingredient.of(new ItemLike[] { (ItemLike)Items.GLOWSTONE_DUST }); private static final Map<Item, FireworkRocketItem.Shape> SHAPE_BY_ITEM;
/*     */   static {
/*  34 */     SHAPE_BY_ITEM = (Map<Item, FireworkRocketItem.Shape>)Util.make(Maps.newHashMap(), debug0 -> {
/*     */           debug0.put(Items.FIRE_CHARGE, FireworkRocketItem.Shape.LARGE_BALL);
/*     */           debug0.put(Items.FEATHER, FireworkRocketItem.Shape.BURST);
/*     */           debug0.put(Items.GOLD_NUGGET, FireworkRocketItem.Shape.STAR);
/*     */           debug0.put(Items.SKELETON_SKULL, FireworkRocketItem.Shape.CREEPER);
/*     */           debug0.put(Items.WITHER_SKELETON_SKULL, FireworkRocketItem.Shape.CREEPER);
/*     */           debug0.put(Items.CREEPER_HEAD, FireworkRocketItem.Shape.CREEPER);
/*     */           debug0.put(Items.PLAYER_HEAD, FireworkRocketItem.Shape.CREEPER);
/*     */           debug0.put(Items.DRAGON_HEAD, FireworkRocketItem.Shape.CREEPER);
/*     */           debug0.put(Items.ZOMBIE_HEAD, FireworkRocketItem.Shape.CREEPER);
/*     */         });
/*     */   }
/*  46 */   private static final Ingredient GUNPOWDER_INGREDIENT = Ingredient.of(new ItemLike[] { (ItemLike)Items.GUNPOWDER });
/*     */   
/*     */   public FireworkStarRecipe(ResourceLocation debug1) {
/*  49 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(CraftingContainer debug1, Level debug2) {
/*  54 */     boolean debug3 = false;
/*  55 */     boolean debug4 = false;
/*  56 */     boolean debug5 = false;
/*  57 */     boolean debug6 = false;
/*  58 */     boolean debug7 = false;
/*     */     
/*  60 */     for (int debug8 = 0; debug8 < debug1.getContainerSize(); debug8++) {
/*  61 */       ItemStack debug9 = debug1.getItem(debug8);
/*  62 */       if (!debug9.isEmpty())
/*     */       {
/*     */ 
/*     */         
/*  66 */         if (SHAPE_INGREDIENT.test(debug9)) {
/*  67 */           if (debug5) {
/*  68 */             return false;
/*     */           }
/*  70 */           debug5 = true;
/*  71 */         } else if (FLICKER_INGREDIENT.test(debug9)) {
/*  72 */           if (debug7) {
/*  73 */             return false;
/*     */           }
/*  75 */           debug7 = true;
/*  76 */         } else if (TRAIL_INGREDIENT.test(debug9)) {
/*  77 */           if (debug6) {
/*  78 */             return false;
/*     */           }
/*  80 */           debug6 = true;
/*  81 */         } else if (GUNPOWDER_INGREDIENT.test(debug9)) {
/*  82 */           if (debug3) {
/*  83 */             return false;
/*     */           }
/*  85 */           debug3 = true;
/*  86 */         } else if (debug9.getItem() instanceof DyeItem) {
/*  87 */           debug4 = true;
/*     */         } else {
/*  89 */           return false;
/*     */         } 
/*     */       }
/*     */     } 
/*  93 */     return (debug3 && debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack assemble(CraftingContainer debug1) {
/*  98 */     ItemStack debug2 = new ItemStack((ItemLike)Items.FIREWORK_STAR);
/*  99 */     CompoundTag debug3 = debug2.getOrCreateTagElement("Explosion");
/*     */     
/* 101 */     FireworkRocketItem.Shape debug4 = FireworkRocketItem.Shape.SMALL_BALL;
/* 102 */     List<Integer> debug5 = Lists.newArrayList();
/*     */     
/* 104 */     for (int debug6 = 0; debug6 < debug1.getContainerSize(); debug6++) {
/* 105 */       ItemStack debug7 = debug1.getItem(debug6);
/* 106 */       if (!debug7.isEmpty())
/*     */       {
/*     */ 
/*     */         
/* 110 */         if (SHAPE_INGREDIENT.test(debug7)) {
/* 111 */           debug4 = SHAPE_BY_ITEM.get(debug7.getItem());
/* 112 */         } else if (FLICKER_INGREDIENT.test(debug7)) {
/* 113 */           debug3.putBoolean("Flicker", true);
/* 114 */         } else if (TRAIL_INGREDIENT.test(debug7)) {
/* 115 */           debug3.putBoolean("Trail", true);
/* 116 */         } else if (debug7.getItem() instanceof DyeItem) {
/* 117 */           debug5.add(Integer.valueOf(((DyeItem)debug7.getItem()).getDyeColor().getFireworkColor()));
/*     */         } 
/*     */       }
/*     */     } 
/* 121 */     debug3.putIntArray("Colors", debug5);
/* 122 */     debug3.putByte("Type", (byte)debug4.getId());
/*     */     
/* 124 */     return debug2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getResultItem() {
/* 134 */     return new ItemStack((ItemLike)Items.FIREWORK_STAR);
/*     */   }
/*     */ 
/*     */   
/*     */   public RecipeSerializer<?> getSerializer() {
/* 139 */     return RecipeSerializer.FIREWORK_STAR;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\FireworkStarRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */