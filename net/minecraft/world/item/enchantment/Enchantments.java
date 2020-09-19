/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ 
/*    */ public class Enchantments {
/*  7 */   private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   public static final Enchantment ALL_DAMAGE_PROTECTION = register("protection", new ProtectionEnchantment(Enchantment.Rarity.COMMON, ProtectionEnchantment.Type.ALL, ARMOR_SLOTS));
/* 16 */   public static final Enchantment FIRE_PROTECTION = register("fire_protection", new ProtectionEnchantment(Enchantment.Rarity.UNCOMMON, ProtectionEnchantment.Type.FIRE, ARMOR_SLOTS));
/* 17 */   public static final Enchantment FALL_PROTECTION = register("feather_falling", new ProtectionEnchantment(Enchantment.Rarity.UNCOMMON, ProtectionEnchantment.Type.FALL, ARMOR_SLOTS));
/* 18 */   public static final Enchantment BLAST_PROTECTION = register("blast_protection", new ProtectionEnchantment(Enchantment.Rarity.RARE, ProtectionEnchantment.Type.EXPLOSION, ARMOR_SLOTS));
/* 19 */   public static final Enchantment PROJECTILE_PROTECTION = register("projectile_protection", new ProtectionEnchantment(Enchantment.Rarity.UNCOMMON, ProtectionEnchantment.Type.PROJECTILE, ARMOR_SLOTS));
/* 20 */   public static final Enchantment RESPIRATION = register("respiration", new OxygenEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
/* 21 */   public static final Enchantment AQUA_AFFINITY = register("aqua_affinity", new WaterWorkerEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
/* 22 */   public static final Enchantment THORNS = register("thorns", new ThornsEnchantment(Enchantment.Rarity.VERY_RARE, ARMOR_SLOTS));
/* 23 */   public static final Enchantment DEPTH_STRIDER = register("depth_strider", new WaterWalkerEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
/* 24 */   public static final Enchantment FROST_WALKER = register("frost_walker", new FrostWalkerEnchantment(Enchantment.Rarity.RARE, new EquipmentSlot[] { EquipmentSlot.FEET }));
/* 25 */   public static final Enchantment BINDING_CURSE = register("binding_curse", new BindingCurseEnchantment(Enchantment.Rarity.VERY_RARE, ARMOR_SLOTS));
/* 26 */   public static final Enchantment SOUL_SPEED = register("soul_speed", new SoulSpeedEnchantment(Enchantment.Rarity.VERY_RARE, new EquipmentSlot[] { EquipmentSlot.FEET }));
/*    */ 
/*    */   
/* 29 */   public static final Enchantment SHARPNESS = register("sharpness", new DamageEnchantment(Enchantment.Rarity.COMMON, 0, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/* 30 */   public static final Enchantment SMITE = register("smite", new DamageEnchantment(Enchantment.Rarity.UNCOMMON, 1, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/* 31 */   public static final Enchantment BANE_OF_ARTHROPODS = register("bane_of_arthropods", new DamageEnchantment(Enchantment.Rarity.UNCOMMON, 2, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/* 32 */   public static final Enchantment KNOCKBACK = register("knockback", new KnockbackEnchantment(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/* 33 */   public static final Enchantment FIRE_ASPECT = register("fire_aspect", new FireAspectEnchantment(Enchantment.Rarity.RARE, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/* 34 */   public static final Enchantment MOB_LOOTING = register("looting", new LootBonusEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/* 35 */   public static final Enchantment SWEEPING_EDGE = register("sweeping", new SweepingEdgeEnchantment(Enchantment.Rarity.RARE, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/*    */ 
/*    */   
/* 38 */   public static final Enchantment BLOCK_EFFICIENCY = register("efficiency", new DiggingEnchantment(Enchantment.Rarity.COMMON, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/* 39 */   public static final Enchantment SILK_TOUCH = register("silk_touch", new UntouchingEnchantment(Enchantment.Rarity.VERY_RARE, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/* 40 */   public static final Enchantment UNBREAKING = register("unbreaking", new DigDurabilityEnchantment(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/* 41 */   public static final Enchantment BLOCK_FORTUNE = register("fortune", new LootBonusEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.DIGGER, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/*    */ 
/*    */   
/* 44 */   public static final Enchantment POWER_ARROWS = register("power", new ArrowDamageEnchantment(Enchantment.Rarity.COMMON, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/* 45 */   public static final Enchantment PUNCH_ARROWS = register("punch", new ArrowKnockbackEnchantment(Enchantment.Rarity.RARE, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/* 46 */   public static final Enchantment FLAMING_ARROWS = register("flame", new ArrowFireEnchantment(Enchantment.Rarity.RARE, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/* 47 */   public static final Enchantment INFINITY_ARROWS = register("infinity", new ArrowInfiniteEnchantment(Enchantment.Rarity.VERY_RARE, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/*    */ 
/*    */   
/* 50 */   public static final Enchantment FISHING_LUCK = register("luck_of_the_sea", new LootBonusEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.FISHING_ROD, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/* 51 */   public static final Enchantment FISHING_SPEED = register("lure", new FishingSpeedEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.FISHING_ROD, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/*    */ 
/*    */   
/* 54 */   public static final Enchantment LOYALTY = register("loyalty", new TridentLoyaltyEnchantment(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/* 55 */   public static final Enchantment IMPALING = register("impaling", new TridentImpalerEnchantment(Enchantment.Rarity.RARE, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/* 56 */   public static final Enchantment RIPTIDE = register("riptide", new TridentRiptideEnchantment(Enchantment.Rarity.RARE, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/* 57 */   public static final Enchantment CHANNELING = register("channeling", new TridentChannelingEnchantment(Enchantment.Rarity.VERY_RARE, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/*    */ 
/*    */   
/* 60 */   public static final Enchantment MULTISHOT = register("multishot", new MultiShotEnchantment(Enchantment.Rarity.RARE, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/* 61 */   public static final Enchantment QUICK_CHARGE = register("quick_charge", new QuickChargeEnchantment(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/* 62 */   public static final Enchantment PIERCING = register("piercing", new ArrowPiercingEnchantment(Enchantment.Rarity.COMMON, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));
/*    */ 
/*    */   
/* 65 */   public static final Enchantment MENDING = register("mending", new MendingEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.values()));
/* 66 */   public static final Enchantment VANISHING_CURSE = register("vanishing_curse", new VanishingCurseEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.values()));
/*    */   
/*    */   private static Enchantment register(String debug0, Enchantment debug1) {
/* 69 */     return (Enchantment)Registry.register(Registry.ENCHANTMENT, debug0, debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\enchantment\Enchantments.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */