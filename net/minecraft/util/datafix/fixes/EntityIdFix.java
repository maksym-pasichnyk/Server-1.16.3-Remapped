/*     */ package net.minecraft.util.datafix.fixes;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ public class EntityIdFix extends DataFix {
/*     */   public EntityIdFix(Schema debug1, boolean debug2) {
/*  15 */     super(debug1, debug2);
/*     */   } private static final Map<String, String> ID_MAP;
/*     */   static {
/*  18 */     ID_MAP = (Map<String, String>)DataFixUtils.make(Maps.newHashMap(), debug0 -> {
/*     */           debug0.put("AreaEffectCloud", "minecraft:area_effect_cloud");
/*     */           debug0.put("ArmorStand", "minecraft:armor_stand");
/*     */           debug0.put("Arrow", "minecraft:arrow");
/*     */           debug0.put("Bat", "minecraft:bat");
/*     */           debug0.put("Blaze", "minecraft:blaze");
/*     */           debug0.put("Boat", "minecraft:boat");
/*     */           debug0.put("CaveSpider", "minecraft:cave_spider");
/*     */           debug0.put("Chicken", "minecraft:chicken");
/*     */           debug0.put("Cow", "minecraft:cow");
/*     */           debug0.put("Creeper", "minecraft:creeper");
/*     */           debug0.put("Donkey", "minecraft:donkey");
/*     */           debug0.put("DragonFireball", "minecraft:dragon_fireball");
/*     */           debug0.put("ElderGuardian", "minecraft:elder_guardian");
/*     */           debug0.put("EnderCrystal", "minecraft:ender_crystal");
/*     */           debug0.put("EnderDragon", "minecraft:ender_dragon");
/*     */           debug0.put("Enderman", "minecraft:enderman");
/*     */           debug0.put("Endermite", "minecraft:endermite");
/*     */           debug0.put("EyeOfEnderSignal", "minecraft:eye_of_ender_signal");
/*     */           debug0.put("FallingSand", "minecraft:falling_block");
/*     */           debug0.put("Fireball", "minecraft:fireball");
/*     */           debug0.put("FireworksRocketEntity", "minecraft:fireworks_rocket");
/*     */           debug0.put("Ghast", "minecraft:ghast");
/*     */           debug0.put("Giant", "minecraft:giant");
/*     */           debug0.put("Guardian", "minecraft:guardian");
/*     */           debug0.put("Horse", "minecraft:horse");
/*     */           debug0.put("Husk", "minecraft:husk");
/*     */           debug0.put("Item", "minecraft:item");
/*     */           debug0.put("ItemFrame", "minecraft:item_frame");
/*     */           debug0.put("LavaSlime", "minecraft:magma_cube");
/*     */           debug0.put("LeashKnot", "minecraft:leash_knot");
/*     */           debug0.put("MinecartChest", "minecraft:chest_minecart");
/*     */           debug0.put("MinecartCommandBlock", "minecraft:commandblock_minecart");
/*     */           debug0.put("MinecartFurnace", "minecraft:furnace_minecart");
/*     */           debug0.put("MinecartHopper", "minecraft:hopper_minecart");
/*     */           debug0.put("MinecartRideable", "minecraft:minecart");
/*     */           debug0.put("MinecartSpawner", "minecraft:spawner_minecart");
/*     */           debug0.put("MinecartTNT", "minecraft:tnt_minecart");
/*     */           debug0.put("Mule", "minecraft:mule");
/*     */           debug0.put("MushroomCow", "minecraft:mooshroom");
/*     */           debug0.put("Ozelot", "minecraft:ocelot");
/*     */           debug0.put("Painting", "minecraft:painting");
/*     */           debug0.put("Pig", "minecraft:pig");
/*     */           debug0.put("PigZombie", "minecraft:zombie_pigman");
/*     */           debug0.put("PolarBear", "minecraft:polar_bear");
/*     */           debug0.put("PrimedTnt", "minecraft:tnt");
/*     */           debug0.put("Rabbit", "minecraft:rabbit");
/*     */           debug0.put("Sheep", "minecraft:sheep");
/*     */           debug0.put("Shulker", "minecraft:shulker");
/*     */           debug0.put("ShulkerBullet", "minecraft:shulker_bullet");
/*     */           debug0.put("Silverfish", "minecraft:silverfish");
/*     */           debug0.put("Skeleton", "minecraft:skeleton");
/*     */           debug0.put("SkeletonHorse", "minecraft:skeleton_horse");
/*     */           debug0.put("Slime", "minecraft:slime");
/*     */           debug0.put("SmallFireball", "minecraft:small_fireball");
/*     */           debug0.put("SnowMan", "minecraft:snowman");
/*     */           debug0.put("Snowball", "minecraft:snowball");
/*     */           debug0.put("SpectralArrow", "minecraft:spectral_arrow");
/*     */           debug0.put("Spider", "minecraft:spider");
/*     */           debug0.put("Squid", "minecraft:squid");
/*     */           debug0.put("Stray", "minecraft:stray");
/*     */           debug0.put("ThrownEgg", "minecraft:egg");
/*     */           debug0.put("ThrownEnderpearl", "minecraft:ender_pearl");
/*     */           debug0.put("ThrownExpBottle", "minecraft:xp_bottle");
/*     */           debug0.put("ThrownPotion", "minecraft:potion");
/*     */           debug0.put("Villager", "minecraft:villager");
/*     */           debug0.put("VillagerGolem", "minecraft:villager_golem");
/*     */           debug0.put("Witch", "minecraft:witch");
/*     */           debug0.put("WitherBoss", "minecraft:wither");
/*     */           debug0.put("WitherSkeleton", "minecraft:wither_skeleton");
/*     */           debug0.put("WitherSkull", "minecraft:wither_skull");
/*     */           debug0.put("Wolf", "minecraft:wolf");
/*     */           debug0.put("XPOrb", "minecraft:xp_orb");
/*     */           debug0.put("Zombie", "minecraft:zombie");
/*     */           debug0.put("ZombieHorse", "minecraft:zombie_horse");
/*     */           debug0.put("ZombieVillager", "minecraft:zombie_villager");
/*     */         });
/*     */   }
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/*  98 */     TaggedChoice.TaggedChoiceType<String> debug1 = getInputSchema().findChoiceType(References.ENTITY);
/*  99 */     TaggedChoice.TaggedChoiceType<String> debug2 = getOutputSchema().findChoiceType(References.ENTITY);
/*     */     
/* 101 */     Type<?> debug3 = getInputSchema().getType(References.ITEM_STACK);
/* 102 */     Type<?> debug4 = getOutputSchema().getType(References.ITEM_STACK);
/*     */     
/* 104 */     return TypeRewriteRule.seq(
/* 105 */         convertUnchecked("item stack entity name hook converter", debug3, debug4), 
/* 106 */         fixTypeEverywhere("EntityIdFix", (Type)debug1, (Type)debug2, debug0 -> ()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityIdFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */