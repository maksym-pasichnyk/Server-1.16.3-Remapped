/*     */ package net.minecraft.world.item.enchantment;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.WeighedRandom;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MobType;
/*     */ import net.minecraft.world.item.EnchantedBookItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import org.apache.commons.lang3.mutable.MutableFloat;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ public class EnchantmentHelper {
/*     */   public static int getItemEnchantmentLevel(Enchantment debug0, ItemStack debug1) {
/*  35 */     if (debug1.isEmpty()) {
/*  36 */       return 0;
/*     */     }
/*     */     
/*  39 */     ResourceLocation debug2 = Registry.ENCHANTMENT.getKey(debug0);
/*     */     
/*  41 */     ListTag debug3 = debug1.getEnchantmentTags();
/*  42 */     for (int debug4 = 0; debug4 < debug3.size(); debug4++) {
/*  43 */       CompoundTag debug5 = debug3.getCompound(debug4);
/*     */       
/*  45 */       ResourceLocation debug6 = ResourceLocation.tryParse(debug5.getString("id"));
/*  46 */       if (debug6 != null && debug6.equals(debug2)) {
/*  47 */         return Mth.clamp(debug5.getInt("lvl"), 0, 255);
/*     */       }
/*     */     } 
/*  50 */     return 0;
/*     */   }
/*     */   
/*     */   public static Map<Enchantment, Integer> getEnchantments(ItemStack debug0) {
/*  54 */     ListTag debug1 = (debug0.getItem() == Items.ENCHANTED_BOOK) ? EnchantedBookItem.getEnchantments(debug0) : debug0.getEnchantmentTags();
/*  55 */     return deserializeEnchantments(debug1);
/*     */   }
/*     */   
/*     */   public static Map<Enchantment, Integer> deserializeEnchantments(ListTag debug0) {
/*  59 */     Map<Enchantment, Integer> debug1 = Maps.newLinkedHashMap();
/*  60 */     for (int debug2 = 0; debug2 < debug0.size(); debug2++) {
/*  61 */       CompoundTag debug3 = debug0.getCompound(debug2);
/*     */       
/*  63 */       Registry.ENCHANTMENT.getOptional(ResourceLocation.tryParse(debug3.getString("id")))
/*  64 */         .ifPresent(debug2 -> (Integer)debug0.put(debug2, Integer.valueOf(debug1.getInt("lvl"))));
/*     */     } 
/*     */     
/*  67 */     return debug1;
/*     */   }
/*     */   
/*     */   public static void setEnchantments(Map<Enchantment, Integer> debug0, ItemStack debug1) {
/*  71 */     ListTag debug2 = new ListTag();
/*     */     
/*  73 */     for (Map.Entry<Enchantment, Integer> debug4 : debug0.entrySet()) {
/*  74 */       Enchantment debug5 = debug4.getKey();
/*  75 */       if (debug5 == null) {
/*     */         continue;
/*     */       }
/*  78 */       int debug6 = ((Integer)debug4.getValue()).intValue();
/*     */       
/*  80 */       CompoundTag debug7 = new CompoundTag();
/*  81 */       debug7.putString("id", String.valueOf(Registry.ENCHANTMENT.getKey(debug5)));
/*  82 */       debug7.putShort("lvl", (short)debug6);
/*     */       
/*  84 */       debug2.add(debug7);
/*     */       
/*  86 */       if (debug1.getItem() == Items.ENCHANTED_BOOK) {
/*  87 */         EnchantedBookItem.addEnchantment(debug1, new EnchantmentInstance(debug5, debug6));
/*     */       }
/*     */     } 
/*     */     
/*  91 */     if (debug2.isEmpty()) {
/*  92 */       debug1.removeTagKey("Enchantments");
/*  93 */     } else if (debug1.getItem() != Items.ENCHANTED_BOOK) {
/*  94 */       debug1.addTagElement("Enchantments", (Tag)debug2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void runIterationOnItem(EnchantmentVisitor debug0, ItemStack debug1) {
/* 104 */     if (debug1.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 108 */     ListTag debug2 = debug1.getEnchantmentTags();
/* 109 */     for (int debug3 = 0; debug3 < debug2.size(); debug3++) {
/* 110 */       String debug4 = debug2.getCompound(debug3).getString("id");
/* 111 */       int debug5 = debug2.getCompound(debug3).getInt("lvl");
/*     */       
/* 113 */       Registry.ENCHANTMENT.getOptional(ResourceLocation.tryParse(debug4))
/* 114 */         .ifPresent(debug2 -> debug0.accept(debug2, debug1));
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void runIterationOnInventory(EnchantmentVisitor debug0, Iterable<ItemStack> debug1) {
/* 119 */     for (ItemStack debug3 : debug1) {
/* 120 */       runIterationOnItem(debug0, debug3);
/*     */     }
/*     */   }
/*     */   
/*     */   public static int getDamageProtection(Iterable<ItemStack> debug0, DamageSource debug1) {
/* 125 */     MutableInt debug2 = new MutableInt();
/* 126 */     runIterationOnInventory((debug2, debug3) -> debug0.add(debug2.getDamageProtection(debug3, debug1)), debug0);
/* 127 */     return debug2.intValue();
/*     */   }
/*     */   
/*     */   public static float getDamageBonus(ItemStack debug0, MobType debug1) {
/* 131 */     MutableFloat debug2 = new MutableFloat();
/* 132 */     runIterationOnItem((debug2, debug3) -> debug0.add(debug2.getDamageBonus(debug3, debug1)), debug0);
/* 133 */     return debug2.floatValue();
/*     */   }
/*     */   
/*     */   public static float getSweepingDamageRatio(LivingEntity debug0) {
/* 137 */     int debug1 = getEnchantmentLevel(Enchantments.SWEEPING_EDGE, debug0);
/* 138 */     if (debug1 > 0) {
/* 139 */       return SweepingEdgeEnchantment.getSweepingDamageRatio(debug1);
/*     */     }
/* 141 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public static void doPostHurtEffects(LivingEntity debug0, Entity debug1) {
/* 145 */     EnchantmentVisitor debug2 = (debug2, debug3) -> debug2.doPostHurt(debug0, debug1, debug3);
/* 146 */     if (debug0 != null) {
/* 147 */       runIterationOnInventory(debug2, debug0.getAllSlots());
/*     */     }
/* 149 */     if (debug1 instanceof net.minecraft.world.entity.player.Player) {
/* 150 */       runIterationOnItem(debug2, debug0.getMainHandItem());
/*     */     }
/*     */   }
/*     */   
/*     */   public static void doPostDamageEffects(LivingEntity debug0, Entity debug1) {
/* 155 */     EnchantmentVisitor debug2 = (debug2, debug3) -> debug2.doPostAttack(debug0, debug1, debug3);
/* 156 */     if (debug0 != null) {
/* 157 */       runIterationOnInventory(debug2, debug0.getAllSlots());
/*     */     }
/* 159 */     if (debug0 instanceof net.minecraft.world.entity.player.Player) {
/* 160 */       runIterationOnItem(debug2, debug0.getMainHandItem());
/*     */     }
/*     */   }
/*     */   
/*     */   public static int getEnchantmentLevel(Enchantment debug0, LivingEntity debug1) {
/* 165 */     Iterable<ItemStack> debug2 = debug0.getSlotItems(debug1).values();
/* 166 */     if (debug2 == null) {
/* 167 */       return 0;
/*     */     }
/* 169 */     int debug3 = 0;
/* 170 */     for (ItemStack debug5 : debug2) {
/* 171 */       int debug6 = getItemEnchantmentLevel(debug0, debug5);
/* 172 */       if (debug6 > debug3) {
/* 173 */         debug3 = debug6;
/*     */       }
/*     */     } 
/* 176 */     return debug3;
/*     */   }
/*     */   
/*     */   public static int getKnockbackBonus(LivingEntity debug0) {
/* 180 */     return getEnchantmentLevel(Enchantments.KNOCKBACK, debug0);
/*     */   }
/*     */   
/*     */   public static int getFireAspect(LivingEntity debug0) {
/* 184 */     return getEnchantmentLevel(Enchantments.FIRE_ASPECT, debug0);
/*     */   }
/*     */   
/*     */   public static int getRespiration(LivingEntity debug0) {
/* 188 */     return getEnchantmentLevel(Enchantments.RESPIRATION, debug0);
/*     */   }
/*     */   
/*     */   public static int getDepthStrider(LivingEntity debug0) {
/* 192 */     return getEnchantmentLevel(Enchantments.DEPTH_STRIDER, debug0);
/*     */   }
/*     */   
/*     */   public static int getBlockEfficiency(LivingEntity debug0) {
/* 196 */     return getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, debug0);
/*     */   }
/*     */   
/*     */   public static int getFishingLuckBonus(ItemStack debug0) {
/* 200 */     return getItemEnchantmentLevel(Enchantments.FISHING_LUCK, debug0);
/*     */   }
/*     */   
/*     */   public static int getFishingSpeedBonus(ItemStack debug0) {
/* 204 */     return getItemEnchantmentLevel(Enchantments.FISHING_SPEED, debug0);
/*     */   }
/*     */   
/*     */   public static int getMobLooting(LivingEntity debug0) {
/* 208 */     return getEnchantmentLevel(Enchantments.MOB_LOOTING, debug0);
/*     */   }
/*     */   
/*     */   public static boolean hasAquaAffinity(LivingEntity debug0) {
/* 212 */     return (getEnchantmentLevel(Enchantments.AQUA_AFFINITY, debug0) > 0);
/*     */   }
/*     */   
/*     */   public static boolean hasFrostWalker(LivingEntity debug0) {
/* 216 */     return (getEnchantmentLevel(Enchantments.FROST_WALKER, debug0) > 0);
/*     */   }
/*     */   
/*     */   public static boolean hasSoulSpeed(LivingEntity debug0) {
/* 220 */     return (getEnchantmentLevel(Enchantments.SOUL_SPEED, debug0) > 0);
/*     */   }
/*     */   
/*     */   public static boolean hasBindingCurse(ItemStack debug0) {
/* 224 */     return (getItemEnchantmentLevel(Enchantments.BINDING_CURSE, debug0) > 0);
/*     */   }
/*     */   
/*     */   public static boolean hasVanishingCurse(ItemStack debug0) {
/* 228 */     return (getItemEnchantmentLevel(Enchantments.VANISHING_CURSE, debug0) > 0);
/*     */   }
/*     */   
/*     */   public static int getLoyalty(ItemStack debug0) {
/* 232 */     return getItemEnchantmentLevel(Enchantments.LOYALTY, debug0);
/*     */   }
/*     */   
/*     */   public static int getRiptide(ItemStack debug0) {
/* 236 */     return getItemEnchantmentLevel(Enchantments.RIPTIDE, debug0);
/*     */   }
/*     */   
/*     */   public static boolean hasChanneling(ItemStack debug0) {
/* 240 */     return (getItemEnchantmentLevel(Enchantments.CHANNELING, debug0) > 0);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static Map.Entry<EquipmentSlot, ItemStack> getRandomItemWith(Enchantment debug0, LivingEntity debug1) {
/* 245 */     return getRandomItemWith(debug0, debug1, debug0 -> true);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static Map.Entry<EquipmentSlot, ItemStack> getRandomItemWith(Enchantment debug0, LivingEntity debug1, Predicate<ItemStack> debug2) {
/* 250 */     Map<EquipmentSlot, ItemStack> debug3 = debug0.getSlotItems(debug1);
/* 251 */     if (debug3.isEmpty()) {
/* 252 */       return null;
/*     */     }
/* 254 */     List<Map.Entry<EquipmentSlot, ItemStack>> debug4 = Lists.newArrayList();
/* 255 */     for (Map.Entry<EquipmentSlot, ItemStack> debug6 : debug3.entrySet()) {
/* 256 */       ItemStack debug7 = debug6.getValue();
/* 257 */       if (!debug7.isEmpty() && getItemEnchantmentLevel(debug0, debug7) > 0 && debug2.test(debug7)) {
/* 258 */         debug4.add(debug6);
/*     */       }
/*     */     } 
/*     */     
/* 262 */     return debug4.isEmpty() ? null : debug4.get(debug1.getRandom().nextInt(debug4.size()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getEnchantmentCost(Random debug0, int debug1, int debug2, ItemStack debug3) {
/* 273 */     Item debug4 = debug3.getItem();
/* 274 */     int debug5 = debug4.getEnchantmentValue();
/*     */     
/* 276 */     if (debug5 <= 0)
/*     */     {
/* 278 */       return 0;
/*     */     }
/*     */     
/* 281 */     if (debug2 > 15) {
/* 282 */       debug2 = 15;
/*     */     }
/* 284 */     int debug6 = debug0.nextInt(8) + 1 + (debug2 >> 1) + debug0.nextInt(debug2 + 1);
/* 285 */     if (debug1 == 0) {
/* 286 */       return Math.max(debug6 / 3, 1);
/*     */     }
/* 288 */     if (debug1 == 1) {
/* 289 */       return debug6 * 2 / 3 + 1;
/*     */     }
/* 291 */     return Math.max(debug6, debug2 * 2);
/*     */   }
/*     */   
/*     */   public static ItemStack enchantItem(Random debug0, ItemStack debug1, int debug2, boolean debug3) {
/* 295 */     List<EnchantmentInstance> debug4 = selectEnchantment(debug0, debug1, debug2, debug3);
/*     */     
/* 297 */     boolean debug5 = (debug1.getItem() == Items.BOOK);
/* 298 */     if (debug5) {
/* 299 */       debug1 = new ItemStack((ItemLike)Items.ENCHANTED_BOOK);
/*     */     }
/*     */     
/* 302 */     for (EnchantmentInstance debug7 : debug4) {
/* 303 */       if (debug5) {
/* 304 */         EnchantedBookItem.addEnchantment(debug1, debug7); continue;
/*     */       } 
/* 306 */       debug1.enchant(debug7.enchantment, debug7.level);
/*     */     } 
/*     */ 
/*     */     
/* 310 */     return debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<EnchantmentInstance> selectEnchantment(Random debug0, ItemStack debug1, int debug2, boolean debug3) {
/* 321 */     List<EnchantmentInstance> debug4 = Lists.newArrayList();
/*     */ 
/*     */     
/* 324 */     Item debug5 = debug1.getItem();
/* 325 */     int debug6 = debug5.getEnchantmentValue();
/*     */     
/* 327 */     if (debug6 <= 0) {
/* 328 */       return debug4;
/*     */     }
/*     */     
/* 331 */     debug2 += 1 + debug0.nextInt(debug6 / 4 + 1) + debug0.nextInt(debug6 / 4 + 1);
/*     */ 
/*     */     
/* 334 */     float debug7 = (debug0.nextFloat() + debug0.nextFloat() - 1.0F) * 0.15F;
/* 335 */     debug2 = Mth.clamp(Math.round(debug2 + debug2 * debug7), 1, 2147483647);
/*     */     
/* 337 */     List<EnchantmentInstance> debug8 = getAvailableEnchantmentResults(debug2, debug1, debug3);
/* 338 */     if (!debug8.isEmpty()) {
/* 339 */       debug4.add(WeighedRandom.getRandomItem(debug0, debug8));
/*     */       
/* 341 */       while (debug0.nextInt(50) <= debug2) {
/* 342 */         filterCompatibleEnchantments(debug8, (EnchantmentInstance)Util.lastOf(debug4));
/*     */         
/* 344 */         if (debug8.isEmpty()) {
/*     */           break;
/*     */         }
/*     */         
/* 348 */         debug4.add(WeighedRandom.getRandomItem(debug0, debug8));
/* 349 */         debug2 /= 2;
/*     */       } 
/*     */     } 
/* 352 */     return debug4;
/*     */   }
/*     */   
/*     */   public static void filterCompatibleEnchantments(List<EnchantmentInstance> debug0, EnchantmentInstance debug1) {
/* 356 */     Iterator<EnchantmentInstance> debug2 = debug0.iterator();
/* 357 */     while (debug2.hasNext()) {
/* 358 */       if (!debug1.enchantment.isCompatibleWith(((EnchantmentInstance)debug2.next()).enchantment)) {
/* 359 */         debug2.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isEnchantmentCompatible(Collection<Enchantment> debug0, Enchantment debug1) {
/* 365 */     for (Enchantment debug3 : debug0) {
/* 366 */       if (!debug3.isCompatibleWith(debug1)) {
/* 367 */         return false;
/*     */       }
/*     */     } 
/* 370 */     return true;
/*     */   }
/*     */   
/*     */   public static List<EnchantmentInstance> getAvailableEnchantmentResults(int debug0, ItemStack debug1, boolean debug2) {
/* 374 */     List<EnchantmentInstance> debug3 = Lists.newArrayList();
/*     */     
/* 376 */     Item debug4 = debug1.getItem();
/* 377 */     boolean debug5 = (debug1.getItem() == Items.BOOK);
/* 378 */     for (Enchantment debug7 : Registry.ENCHANTMENT) {
/* 379 */       if (debug7.isTreasureOnly() && !debug2) {
/*     */         continue;
/*     */       }
/* 382 */       if (!debug7.isDiscoverable()) {
/*     */         continue;
/*     */       }
/*     */       
/* 386 */       if (!debug7.category.canEnchant(debug4) && !debug5) {
/*     */         continue;
/*     */       }
/*     */       
/* 390 */       for (int debug8 = debug7.getMaxLevel(); debug8 > debug7.getMinLevel() - 1; debug8--) {
/* 391 */         if (debug0 >= debug7.getMinCost(debug8) && debug0 <= debug7.getMaxCost(debug8)) {
/* 392 */           debug3.add(new EnchantmentInstance(debug7, debug8));
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 398 */     return debug3;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   static interface EnchantmentVisitor {
/*     */     void accept(Enchantment param1Enchantment, int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\enchantment\EnchantmentHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */