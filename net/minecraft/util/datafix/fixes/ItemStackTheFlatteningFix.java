/*     */ package net.minecraft.util.datafix.fixes;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ public class ItemStackTheFlatteningFix extends DataFix {
/*     */   private static final Map<String, String> MAP;
/*     */   
/*     */   public ItemStackTheFlatteningFix(Schema debug1, boolean debug2) {
/*  26 */     super(debug1, debug2);
/*     */   } private static final Set<String> IDS;
/*     */   static {
/*  29 */     MAP = (Map<String, String>)DataFixUtils.make(Maps.newHashMap(), debug0 -> {
/*     */           debug0.put("minecraft:stone.0", "minecraft:stone");
/*     */           
/*     */           debug0.put("minecraft:stone.1", "minecraft:granite");
/*     */           
/*     */           debug0.put("minecraft:stone.2", "minecraft:polished_granite");
/*     */           debug0.put("minecraft:stone.3", "minecraft:diorite");
/*     */           debug0.put("minecraft:stone.4", "minecraft:polished_diorite");
/*     */           debug0.put("minecraft:stone.5", "minecraft:andesite");
/*     */           debug0.put("minecraft:stone.6", "minecraft:polished_andesite");
/*     */           debug0.put("minecraft:dirt.0", "minecraft:dirt");
/*     */           debug0.put("minecraft:dirt.1", "minecraft:coarse_dirt");
/*     */           debug0.put("minecraft:dirt.2", "minecraft:podzol");
/*     */           debug0.put("minecraft:leaves.0", "minecraft:oak_leaves");
/*     */           debug0.put("minecraft:leaves.1", "minecraft:spruce_leaves");
/*     */           debug0.put("minecraft:leaves.2", "minecraft:birch_leaves");
/*     */           debug0.put("minecraft:leaves.3", "minecraft:jungle_leaves");
/*     */           debug0.put("minecraft:leaves2.0", "minecraft:acacia_leaves");
/*     */           debug0.put("minecraft:leaves2.1", "minecraft:dark_oak_leaves");
/*     */           debug0.put("minecraft:log.0", "minecraft:oak_log");
/*     */           debug0.put("minecraft:log.1", "minecraft:spruce_log");
/*     */           debug0.put("minecraft:log.2", "minecraft:birch_log");
/*     */           debug0.put("minecraft:log.3", "minecraft:jungle_log");
/*     */           debug0.put("minecraft:log2.0", "minecraft:acacia_log");
/*     */           debug0.put("minecraft:log2.1", "minecraft:dark_oak_log");
/*     */           debug0.put("minecraft:sapling.0", "minecraft:oak_sapling");
/*     */           debug0.put("minecraft:sapling.1", "minecraft:spruce_sapling");
/*     */           debug0.put("minecraft:sapling.2", "minecraft:birch_sapling");
/*     */           debug0.put("minecraft:sapling.3", "minecraft:jungle_sapling");
/*     */           debug0.put("minecraft:sapling.4", "minecraft:acacia_sapling");
/*     */           debug0.put("minecraft:sapling.5", "minecraft:dark_oak_sapling");
/*     */           debug0.put("minecraft:planks.0", "minecraft:oak_planks");
/*     */           debug0.put("minecraft:planks.1", "minecraft:spruce_planks");
/*     */           debug0.put("minecraft:planks.2", "minecraft:birch_planks");
/*     */           debug0.put("minecraft:planks.3", "minecraft:jungle_planks");
/*     */           debug0.put("minecraft:planks.4", "minecraft:acacia_planks");
/*     */           debug0.put("minecraft:planks.5", "minecraft:dark_oak_planks");
/*     */           debug0.put("minecraft:sand.0", "minecraft:sand");
/*     */           debug0.put("minecraft:sand.1", "minecraft:red_sand");
/*     */           debug0.put("minecraft:quartz_block.0", "minecraft:quartz_block");
/*     */           debug0.put("minecraft:quartz_block.1", "minecraft:chiseled_quartz_block");
/*     */           debug0.put("minecraft:quartz_block.2", "minecraft:quartz_pillar");
/*     */           debug0.put("minecraft:anvil.0", "minecraft:anvil");
/*     */           debug0.put("minecraft:anvil.1", "minecraft:chipped_anvil");
/*     */           debug0.put("minecraft:anvil.2", "minecraft:damaged_anvil");
/*     */           debug0.put("minecraft:wool.0", "minecraft:white_wool");
/*     */           debug0.put("minecraft:wool.1", "minecraft:orange_wool");
/*     */           debug0.put("minecraft:wool.2", "minecraft:magenta_wool");
/*     */           debug0.put("minecraft:wool.3", "minecraft:light_blue_wool");
/*     */           debug0.put("minecraft:wool.4", "minecraft:yellow_wool");
/*     */           debug0.put("minecraft:wool.5", "minecraft:lime_wool");
/*     */           debug0.put("minecraft:wool.6", "minecraft:pink_wool");
/*     */           debug0.put("minecraft:wool.7", "minecraft:gray_wool");
/*     */           debug0.put("minecraft:wool.8", "minecraft:light_gray_wool");
/*     */           debug0.put("minecraft:wool.9", "minecraft:cyan_wool");
/*     */           debug0.put("minecraft:wool.10", "minecraft:purple_wool");
/*     */           debug0.put("minecraft:wool.11", "minecraft:blue_wool");
/*     */           debug0.put("minecraft:wool.12", "minecraft:brown_wool");
/*     */           debug0.put("minecraft:wool.13", "minecraft:green_wool");
/*     */           debug0.put("minecraft:wool.14", "minecraft:red_wool");
/*     */           debug0.put("minecraft:wool.15", "minecraft:black_wool");
/*     */           debug0.put("minecraft:carpet.0", "minecraft:white_carpet");
/*     */           debug0.put("minecraft:carpet.1", "minecraft:orange_carpet");
/*     */           debug0.put("minecraft:carpet.2", "minecraft:magenta_carpet");
/*     */           debug0.put("minecraft:carpet.3", "minecraft:light_blue_carpet");
/*     */           debug0.put("minecraft:carpet.4", "minecraft:yellow_carpet");
/*     */           debug0.put("minecraft:carpet.5", "minecraft:lime_carpet");
/*     */           debug0.put("minecraft:carpet.6", "minecraft:pink_carpet");
/*     */           debug0.put("minecraft:carpet.7", "minecraft:gray_carpet");
/*     */           debug0.put("minecraft:carpet.8", "minecraft:light_gray_carpet");
/*     */           debug0.put("minecraft:carpet.9", "minecraft:cyan_carpet");
/*     */           debug0.put("minecraft:carpet.10", "minecraft:purple_carpet");
/*     */           debug0.put("minecraft:carpet.11", "minecraft:blue_carpet");
/*     */           debug0.put("minecraft:carpet.12", "minecraft:brown_carpet");
/*     */           debug0.put("minecraft:carpet.13", "minecraft:green_carpet");
/*     */           debug0.put("minecraft:carpet.14", "minecraft:red_carpet");
/*     */           debug0.put("minecraft:carpet.15", "minecraft:black_carpet");
/*     */           debug0.put("minecraft:hardened_clay.0", "minecraft:terracotta");
/*     */           debug0.put("minecraft:stained_hardened_clay.0", "minecraft:white_terracotta");
/*     */           debug0.put("minecraft:stained_hardened_clay.1", "minecraft:orange_terracotta");
/*     */           debug0.put("minecraft:stained_hardened_clay.2", "minecraft:magenta_terracotta");
/*     */           debug0.put("minecraft:stained_hardened_clay.3", "minecraft:light_blue_terracotta");
/*     */           debug0.put("minecraft:stained_hardened_clay.4", "minecraft:yellow_terracotta");
/*     */           debug0.put("minecraft:stained_hardened_clay.5", "minecraft:lime_terracotta");
/*     */           debug0.put("minecraft:stained_hardened_clay.6", "minecraft:pink_terracotta");
/*     */           debug0.put("minecraft:stained_hardened_clay.7", "minecraft:gray_terracotta");
/*     */           debug0.put("minecraft:stained_hardened_clay.8", "minecraft:light_gray_terracotta");
/*     */           debug0.put("minecraft:stained_hardened_clay.9", "minecraft:cyan_terracotta");
/*     */           debug0.put("minecraft:stained_hardened_clay.10", "minecraft:purple_terracotta");
/*     */           debug0.put("minecraft:stained_hardened_clay.11", "minecraft:blue_terracotta");
/*     */           debug0.put("minecraft:stained_hardened_clay.12", "minecraft:brown_terracotta");
/*     */           debug0.put("minecraft:stained_hardened_clay.13", "minecraft:green_terracotta");
/*     */           debug0.put("minecraft:stained_hardened_clay.14", "minecraft:red_terracotta");
/*     */           debug0.put("minecraft:stained_hardened_clay.15", "minecraft:black_terracotta");
/*     */           debug0.put("minecraft:silver_glazed_terracotta.0", "minecraft:light_gray_glazed_terracotta");
/*     */           debug0.put("minecraft:stained_glass.0", "minecraft:white_stained_glass");
/*     */           debug0.put("minecraft:stained_glass.1", "minecraft:orange_stained_glass");
/*     */           debug0.put("minecraft:stained_glass.2", "minecraft:magenta_stained_glass");
/*     */           debug0.put("minecraft:stained_glass.3", "minecraft:light_blue_stained_glass");
/*     */           debug0.put("minecraft:stained_glass.4", "minecraft:yellow_stained_glass");
/*     */           debug0.put("minecraft:stained_glass.5", "minecraft:lime_stained_glass");
/*     */           debug0.put("minecraft:stained_glass.6", "minecraft:pink_stained_glass");
/*     */           debug0.put("minecraft:stained_glass.7", "minecraft:gray_stained_glass");
/*     */           debug0.put("minecraft:stained_glass.8", "minecraft:light_gray_stained_glass");
/*     */           debug0.put("minecraft:stained_glass.9", "minecraft:cyan_stained_glass");
/*     */           debug0.put("minecraft:stained_glass.10", "minecraft:purple_stained_glass");
/*     */           debug0.put("minecraft:stained_glass.11", "minecraft:blue_stained_glass");
/*     */           debug0.put("minecraft:stained_glass.12", "minecraft:brown_stained_glass");
/*     */           debug0.put("minecraft:stained_glass.13", "minecraft:green_stained_glass");
/*     */           debug0.put("minecraft:stained_glass.14", "minecraft:red_stained_glass");
/*     */           debug0.put("minecraft:stained_glass.15", "minecraft:black_stained_glass");
/*     */           debug0.put("minecraft:stained_glass_pane.0", "minecraft:white_stained_glass_pane");
/*     */           debug0.put("minecraft:stained_glass_pane.1", "minecraft:orange_stained_glass_pane");
/*     */           debug0.put("minecraft:stained_glass_pane.2", "minecraft:magenta_stained_glass_pane");
/*     */           debug0.put("minecraft:stained_glass_pane.3", "minecraft:light_blue_stained_glass_pane");
/*     */           debug0.put("minecraft:stained_glass_pane.4", "minecraft:yellow_stained_glass_pane");
/*     */           debug0.put("minecraft:stained_glass_pane.5", "minecraft:lime_stained_glass_pane");
/*     */           debug0.put("minecraft:stained_glass_pane.6", "minecraft:pink_stained_glass_pane");
/*     */           debug0.put("minecraft:stained_glass_pane.7", "minecraft:gray_stained_glass_pane");
/*     */           debug0.put("minecraft:stained_glass_pane.8", "minecraft:light_gray_stained_glass_pane");
/*     */           debug0.put("minecraft:stained_glass_pane.9", "minecraft:cyan_stained_glass_pane");
/*     */           debug0.put("minecraft:stained_glass_pane.10", "minecraft:purple_stained_glass_pane");
/*     */           debug0.put("minecraft:stained_glass_pane.11", "minecraft:blue_stained_glass_pane");
/*     */           debug0.put("minecraft:stained_glass_pane.12", "minecraft:brown_stained_glass_pane");
/*     */           debug0.put("minecraft:stained_glass_pane.13", "minecraft:green_stained_glass_pane");
/*     */           debug0.put("minecraft:stained_glass_pane.14", "minecraft:red_stained_glass_pane");
/*     */           debug0.put("minecraft:stained_glass_pane.15", "minecraft:black_stained_glass_pane");
/*     */           debug0.put("minecraft:prismarine.0", "minecraft:prismarine");
/*     */           debug0.put("minecraft:prismarine.1", "minecraft:prismarine_bricks");
/*     */           debug0.put("minecraft:prismarine.2", "minecraft:dark_prismarine");
/*     */           debug0.put("minecraft:concrete.0", "minecraft:white_concrete");
/*     */           debug0.put("minecraft:concrete.1", "minecraft:orange_concrete");
/*     */           debug0.put("minecraft:concrete.2", "minecraft:magenta_concrete");
/*     */           debug0.put("minecraft:concrete.3", "minecraft:light_blue_concrete");
/*     */           debug0.put("minecraft:concrete.4", "minecraft:yellow_concrete");
/*     */           debug0.put("minecraft:concrete.5", "minecraft:lime_concrete");
/*     */           debug0.put("minecraft:concrete.6", "minecraft:pink_concrete");
/*     */           debug0.put("minecraft:concrete.7", "minecraft:gray_concrete");
/*     */           debug0.put("minecraft:concrete.8", "minecraft:light_gray_concrete");
/*     */           debug0.put("minecraft:concrete.9", "minecraft:cyan_concrete");
/*     */           debug0.put("minecraft:concrete.10", "minecraft:purple_concrete");
/*     */           debug0.put("minecraft:concrete.11", "minecraft:blue_concrete");
/*     */           debug0.put("minecraft:concrete.12", "minecraft:brown_concrete");
/*     */           debug0.put("minecraft:concrete.13", "minecraft:green_concrete");
/*     */           debug0.put("minecraft:concrete.14", "minecraft:red_concrete");
/*     */           debug0.put("minecraft:concrete.15", "minecraft:black_concrete");
/*     */           debug0.put("minecraft:concrete_powder.0", "minecraft:white_concrete_powder");
/*     */           debug0.put("minecraft:concrete_powder.1", "minecraft:orange_concrete_powder");
/*     */           debug0.put("minecraft:concrete_powder.2", "minecraft:magenta_concrete_powder");
/*     */           debug0.put("minecraft:concrete_powder.3", "minecraft:light_blue_concrete_powder");
/*     */           debug0.put("minecraft:concrete_powder.4", "minecraft:yellow_concrete_powder");
/*     */           debug0.put("minecraft:concrete_powder.5", "minecraft:lime_concrete_powder");
/*     */           debug0.put("minecraft:concrete_powder.6", "minecraft:pink_concrete_powder");
/*     */           debug0.put("minecraft:concrete_powder.7", "minecraft:gray_concrete_powder");
/*     */           debug0.put("minecraft:concrete_powder.8", "minecraft:light_gray_concrete_powder");
/*     */           debug0.put("minecraft:concrete_powder.9", "minecraft:cyan_concrete_powder");
/*     */           debug0.put("minecraft:concrete_powder.10", "minecraft:purple_concrete_powder");
/*     */           debug0.put("minecraft:concrete_powder.11", "minecraft:blue_concrete_powder");
/*     */           debug0.put("minecraft:concrete_powder.12", "minecraft:brown_concrete_powder");
/*     */           debug0.put("minecraft:concrete_powder.13", "minecraft:green_concrete_powder");
/*     */           debug0.put("minecraft:concrete_powder.14", "minecraft:red_concrete_powder");
/*     */           debug0.put("minecraft:concrete_powder.15", "minecraft:black_concrete_powder");
/*     */           debug0.put("minecraft:cobblestone_wall.0", "minecraft:cobblestone_wall");
/*     */           debug0.put("minecraft:cobblestone_wall.1", "minecraft:mossy_cobblestone_wall");
/*     */           debug0.put("minecraft:sandstone.0", "minecraft:sandstone");
/*     */           debug0.put("minecraft:sandstone.1", "minecraft:chiseled_sandstone");
/*     */           debug0.put("minecraft:sandstone.2", "minecraft:cut_sandstone");
/*     */           debug0.put("minecraft:red_sandstone.0", "minecraft:red_sandstone");
/*     */           debug0.put("minecraft:red_sandstone.1", "minecraft:chiseled_red_sandstone");
/*     */           debug0.put("minecraft:red_sandstone.2", "minecraft:cut_red_sandstone");
/*     */           debug0.put("minecraft:stonebrick.0", "minecraft:stone_bricks");
/*     */           debug0.put("minecraft:stonebrick.1", "minecraft:mossy_stone_bricks");
/*     */           debug0.put("minecraft:stonebrick.2", "minecraft:cracked_stone_bricks");
/*     */           debug0.put("minecraft:stonebrick.3", "minecraft:chiseled_stone_bricks");
/*     */           debug0.put("minecraft:monster_egg.0", "minecraft:infested_stone");
/*     */           debug0.put("minecraft:monster_egg.1", "minecraft:infested_cobblestone");
/*     */           debug0.put("minecraft:monster_egg.2", "minecraft:infested_stone_bricks");
/*     */           debug0.put("minecraft:monster_egg.3", "minecraft:infested_mossy_stone_bricks");
/*     */           debug0.put("minecraft:monster_egg.4", "minecraft:infested_cracked_stone_bricks");
/*     */           debug0.put("minecraft:monster_egg.5", "minecraft:infested_chiseled_stone_bricks");
/*     */           debug0.put("minecraft:yellow_flower.0", "minecraft:dandelion");
/*     */           debug0.put("minecraft:red_flower.0", "minecraft:poppy");
/*     */           debug0.put("minecraft:red_flower.1", "minecraft:blue_orchid");
/*     */           debug0.put("minecraft:red_flower.2", "minecraft:allium");
/*     */           debug0.put("minecraft:red_flower.3", "minecraft:azure_bluet");
/*     */           debug0.put("minecraft:red_flower.4", "minecraft:red_tulip");
/*     */           debug0.put("minecraft:red_flower.5", "minecraft:orange_tulip");
/*     */           debug0.put("minecraft:red_flower.6", "minecraft:white_tulip");
/*     */           debug0.put("minecraft:red_flower.7", "minecraft:pink_tulip");
/*     */           debug0.put("minecraft:red_flower.8", "minecraft:oxeye_daisy");
/*     */           debug0.put("minecraft:double_plant.0", "minecraft:sunflower");
/*     */           debug0.put("minecraft:double_plant.1", "minecraft:lilac");
/*     */           debug0.put("minecraft:double_plant.2", "minecraft:tall_grass");
/*     */           debug0.put("minecraft:double_plant.3", "minecraft:large_fern");
/*     */           debug0.put("minecraft:double_plant.4", "minecraft:rose_bush");
/*     */           debug0.put("minecraft:double_plant.5", "minecraft:peony");
/*     */           debug0.put("minecraft:deadbush.0", "minecraft:dead_bush");
/*     */           debug0.put("minecraft:tallgrass.0", "minecraft:dead_bush");
/*     */           debug0.put("minecraft:tallgrass.1", "minecraft:grass");
/*     */           debug0.put("minecraft:tallgrass.2", "minecraft:fern");
/*     */           debug0.put("minecraft:sponge.0", "minecraft:sponge");
/*     */           debug0.put("minecraft:sponge.1", "minecraft:wet_sponge");
/*     */           debug0.put("minecraft:purpur_slab.0", "minecraft:purpur_slab");
/*     */           debug0.put("minecraft:stone_slab.0", "minecraft:stone_slab");
/*     */           debug0.put("minecraft:stone_slab.1", "minecraft:sandstone_slab");
/*     */           debug0.put("minecraft:stone_slab.2", "minecraft:petrified_oak_slab");
/*     */           debug0.put("minecraft:stone_slab.3", "minecraft:cobblestone_slab");
/*     */           debug0.put("minecraft:stone_slab.4", "minecraft:brick_slab");
/*     */           debug0.put("minecraft:stone_slab.5", "minecraft:stone_brick_slab");
/*     */           debug0.put("minecraft:stone_slab.6", "minecraft:nether_brick_slab");
/*     */           debug0.put("minecraft:stone_slab.7", "minecraft:quartz_slab");
/*     */           debug0.put("minecraft:stone_slab2.0", "minecraft:red_sandstone_slab");
/*     */           debug0.put("minecraft:wooden_slab.0", "minecraft:oak_slab");
/*     */           debug0.put("minecraft:wooden_slab.1", "minecraft:spruce_slab");
/*     */           debug0.put("minecraft:wooden_slab.2", "minecraft:birch_slab");
/*     */           debug0.put("minecraft:wooden_slab.3", "minecraft:jungle_slab");
/*     */           debug0.put("minecraft:wooden_slab.4", "minecraft:acacia_slab");
/*     */           debug0.put("minecraft:wooden_slab.5", "minecraft:dark_oak_slab");
/*     */           debug0.put("minecraft:coal.0", "minecraft:coal");
/*     */           debug0.put("minecraft:coal.1", "minecraft:charcoal");
/*     */           debug0.put("minecraft:fish.0", "minecraft:cod");
/*     */           debug0.put("minecraft:fish.1", "minecraft:salmon");
/*     */           debug0.put("minecraft:fish.2", "minecraft:clownfish");
/*     */           debug0.put("minecraft:fish.3", "minecraft:pufferfish");
/*     */           debug0.put("minecraft:cooked_fish.0", "minecraft:cooked_cod");
/*     */           debug0.put("minecraft:cooked_fish.1", "minecraft:cooked_salmon");
/*     */           debug0.put("minecraft:skull.0", "minecraft:skeleton_skull");
/*     */           debug0.put("minecraft:skull.1", "minecraft:wither_skeleton_skull");
/*     */           debug0.put("minecraft:skull.2", "minecraft:zombie_head");
/*     */           debug0.put("minecraft:skull.3", "minecraft:player_head");
/*     */           debug0.put("minecraft:skull.4", "minecraft:creeper_head");
/*     */           debug0.put("minecraft:skull.5", "minecraft:dragon_head");
/*     */           debug0.put("minecraft:golden_apple.0", "minecraft:golden_apple");
/*     */           debug0.put("minecraft:golden_apple.1", "minecraft:enchanted_golden_apple");
/*     */           debug0.put("minecraft:fireworks.0", "minecraft:firework_rocket");
/*     */           debug0.put("minecraft:firework_charge.0", "minecraft:firework_star");
/*     */           debug0.put("minecraft:dye.0", "minecraft:ink_sac");
/*     */           debug0.put("minecraft:dye.1", "minecraft:rose_red");
/*     */           debug0.put("minecraft:dye.2", "minecraft:cactus_green");
/*     */           debug0.put("minecraft:dye.3", "minecraft:cocoa_beans");
/*     */           debug0.put("minecraft:dye.4", "minecraft:lapis_lazuli");
/*     */           debug0.put("minecraft:dye.5", "minecraft:purple_dye");
/*     */           debug0.put("minecraft:dye.6", "minecraft:cyan_dye");
/*     */           debug0.put("minecraft:dye.7", "minecraft:light_gray_dye");
/*     */           debug0.put("minecraft:dye.8", "minecraft:gray_dye");
/*     */           debug0.put("minecraft:dye.9", "minecraft:pink_dye");
/*     */           debug0.put("minecraft:dye.10", "minecraft:lime_dye");
/*     */           debug0.put("minecraft:dye.11", "minecraft:dandelion_yellow");
/*     */           debug0.put("minecraft:dye.12", "minecraft:light_blue_dye");
/*     */           debug0.put("minecraft:dye.13", "minecraft:magenta_dye");
/*     */           debug0.put("minecraft:dye.14", "minecraft:orange_dye");
/*     */           debug0.put("minecraft:dye.15", "minecraft:bone_meal");
/*     */           debug0.put("minecraft:silver_shulker_box.0", "minecraft:light_gray_shulker_box");
/*     */           debug0.put("minecraft:fence.0", "minecraft:oak_fence");
/*     */           debug0.put("minecraft:fence_gate.0", "minecraft:oak_fence_gate");
/*     */           debug0.put("minecraft:wooden_door.0", "minecraft:oak_door");
/*     */           debug0.put("minecraft:boat.0", "minecraft:oak_boat");
/*     */           debug0.put("minecraft:lit_pumpkin.0", "minecraft:jack_o_lantern");
/*     */           debug0.put("minecraft:pumpkin.0", "minecraft:carved_pumpkin");
/*     */           debug0.put("minecraft:trapdoor.0", "minecraft:oak_trapdoor");
/*     */           debug0.put("minecraft:nether_brick.0", "minecraft:nether_bricks");
/*     */           debug0.put("minecraft:red_nether_brick.0", "minecraft:red_nether_bricks");
/*     */           debug0.put("minecraft:netherbrick.0", "minecraft:nether_brick");
/*     */           debug0.put("minecraft:wooden_button.0", "minecraft:oak_button");
/*     */           debug0.put("minecraft:wooden_pressure_plate.0", "minecraft:oak_pressure_plate");
/*     */           debug0.put("minecraft:noteblock.0", "minecraft:note_block");
/*     */           debug0.put("minecraft:bed.0", "minecraft:white_bed");
/*     */           debug0.put("minecraft:bed.1", "minecraft:orange_bed");
/*     */           debug0.put("minecraft:bed.2", "minecraft:magenta_bed");
/*     */           debug0.put("minecraft:bed.3", "minecraft:light_blue_bed");
/*     */           debug0.put("minecraft:bed.4", "minecraft:yellow_bed");
/*     */           debug0.put("minecraft:bed.5", "minecraft:lime_bed");
/*     */           debug0.put("minecraft:bed.6", "minecraft:pink_bed");
/*     */           debug0.put("minecraft:bed.7", "minecraft:gray_bed");
/*     */           debug0.put("minecraft:bed.8", "minecraft:light_gray_bed");
/*     */           debug0.put("minecraft:bed.9", "minecraft:cyan_bed");
/*     */           debug0.put("minecraft:bed.10", "minecraft:purple_bed");
/*     */           debug0.put("minecraft:bed.11", "minecraft:blue_bed");
/*     */           debug0.put("minecraft:bed.12", "minecraft:brown_bed");
/*     */           debug0.put("minecraft:bed.13", "minecraft:green_bed");
/*     */           debug0.put("minecraft:bed.14", "minecraft:red_bed");
/*     */           debug0.put("minecraft:bed.15", "minecraft:black_bed");
/*     */           debug0.put("minecraft:banner.15", "minecraft:white_banner");
/*     */           debug0.put("minecraft:banner.14", "minecraft:orange_banner");
/*     */           debug0.put("minecraft:banner.13", "minecraft:magenta_banner");
/*     */           debug0.put("minecraft:banner.12", "minecraft:light_blue_banner");
/*     */           debug0.put("minecraft:banner.11", "minecraft:yellow_banner");
/*     */           debug0.put("minecraft:banner.10", "minecraft:lime_banner");
/*     */           debug0.put("minecraft:banner.9", "minecraft:pink_banner");
/*     */           debug0.put("minecraft:banner.8", "minecraft:gray_banner");
/*     */           debug0.put("minecraft:banner.7", "minecraft:light_gray_banner");
/*     */           debug0.put("minecraft:banner.6", "minecraft:cyan_banner");
/*     */           debug0.put("minecraft:banner.5", "minecraft:purple_banner");
/*     */           debug0.put("minecraft:banner.4", "minecraft:blue_banner");
/*     */           debug0.put("minecraft:banner.3", "minecraft:brown_banner");
/*     */           debug0.put("minecraft:banner.2", "minecraft:green_banner");
/*     */           debug0.put("minecraft:banner.1", "minecraft:red_banner");
/*     */           debug0.put("minecraft:banner.0", "minecraft:black_banner");
/*     */           debug0.put("minecraft:grass.0", "minecraft:grass_block");
/*     */           debug0.put("minecraft:brick_block.0", "minecraft:bricks");
/*     */           debug0.put("minecraft:end_bricks.0", "minecraft:end_stone_bricks");
/*     */           debug0.put("minecraft:golden_rail.0", "minecraft:powered_rail");
/*     */           debug0.put("minecraft:magma.0", "minecraft:magma_block");
/*     */           debug0.put("minecraft:quartz_ore.0", "minecraft:nether_quartz_ore");
/*     */           debug0.put("minecraft:reeds.0", "minecraft:sugar_cane");
/*     */           debug0.put("minecraft:slime.0", "minecraft:slime_block");
/*     */           debug0.put("minecraft:stone_stairs.0", "minecraft:cobblestone_stairs");
/*     */           debug0.put("minecraft:waterlily.0", "minecraft:lily_pad");
/*     */           debug0.put("minecraft:web.0", "minecraft:cobweb");
/*     */           debug0.put("minecraft:snow.0", "minecraft:snow_block");
/*     */           debug0.put("minecraft:snow_layer.0", "minecraft:snow");
/*     */           debug0.put("minecraft:record_11.0", "minecraft:music_disc_11");
/*     */           debug0.put("minecraft:record_13.0", "minecraft:music_disc_13");
/*     */           debug0.put("minecraft:record_blocks.0", "minecraft:music_disc_blocks");
/*     */           debug0.put("minecraft:record_cat.0", "minecraft:music_disc_cat");
/*     */           debug0.put("minecraft:record_chirp.0", "minecraft:music_disc_chirp");
/*     */           debug0.put("minecraft:record_far.0", "minecraft:music_disc_far");
/*     */           debug0.put("minecraft:record_mall.0", "minecraft:music_disc_mall");
/*     */           debug0.put("minecraft:record_mellohi.0", "minecraft:music_disc_mellohi");
/*     */           debug0.put("minecraft:record_stal.0", "minecraft:music_disc_stal");
/*     */           debug0.put("minecraft:record_strad.0", "minecraft:music_disc_strad");
/*     */           debug0.put("minecraft:record_wait.0", "minecraft:music_disc_wait");
/*     */           debug0.put("minecraft:record_ward.0", "minecraft:music_disc_ward");
/*     */         });
/* 353 */     IDS = (Set<String>)MAP.keySet().stream().map(debug0 -> debug0.substring(0, debug0.indexOf('.'))).collect(Collectors.toSet());
/*     */   }
/* 355 */   private static final Set<String> DAMAGE_IDS = Sets.newHashSet((Object[])new String[] { "minecraft:bow", "minecraft:carrot_on_a_stick", "minecraft:chainmail_boots", "minecraft:chainmail_chestplate", "minecraft:chainmail_helmet", "minecraft:chainmail_leggings", "minecraft:diamond_axe", "minecraft:diamond_boots", "minecraft:diamond_chestplate", "minecraft:diamond_helmet", "minecraft:diamond_hoe", "minecraft:diamond_leggings", "minecraft:diamond_pickaxe", "minecraft:diamond_shovel", "minecraft:diamond_sword", "minecraft:elytra", "minecraft:fishing_rod", "minecraft:flint_and_steel", "minecraft:golden_axe", "minecraft:golden_boots", "minecraft:golden_chestplate", "minecraft:golden_helmet", "minecraft:golden_hoe", "minecraft:golden_leggings", "minecraft:golden_pickaxe", "minecraft:golden_shovel", "minecraft:golden_sword", "minecraft:iron_axe", "minecraft:iron_boots", "minecraft:iron_chestplate", "minecraft:iron_helmet", "minecraft:iron_hoe", "minecraft:iron_leggings", "minecraft:iron_pickaxe", "minecraft:iron_shovel", "minecraft:iron_sword", "minecraft:leather_boots", "minecraft:leather_chestplate", "minecraft:leather_helmet", "minecraft:leather_leggings", "minecraft:shears", "minecraft:shield", "minecraft:stone_axe", "minecraft:stone_hoe", "minecraft:stone_pickaxe", "minecraft:stone_shovel", "minecraft:stone_sword", "minecraft:wooden_axe", "minecraft:wooden_hoe", "minecraft:wooden_pickaxe", "minecraft:wooden_shovel", "minecraft:wooden_sword" });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/* 412 */     Type<?> debug1 = getInputSchema().getType(References.ITEM_STACK);
/*     */     
/* 414 */     OpticFinder<Pair<String, String>> debug2 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 415 */     OpticFinder<?> debug3 = debug1.findField("tag");
/*     */     
/* 417 */     return fixTypeEverywhereTyped("ItemInstanceTheFlatteningFix", debug1, debug2 -> {
/*     */           Optional<Pair<String, String>> debug3 = debug2.getOptional(debug0);
/*     */           if (!debug3.isPresent()) {
/*     */             return debug2;
/*     */           }
/*     */           debug4 = debug2;
/*     */           Dynamic<?> debug5 = (Dynamic)debug2.get(DSL.remainderFinder());
/*     */           int debug6 = debug5.get("Damage").asInt(0);
/*     */           String debug7 = updateItem((String)((Pair)debug3.get()).getSecond(), debug6);
/*     */           if (debug7 != null) {
/*     */             debug4 = debug4.set(debug0, Pair.of(References.ITEM_NAME.typeName(), debug7));
/*     */           }
/*     */           if (DAMAGE_IDS.contains(((Pair)debug3.get()).getSecond())) {
/*     */             Typed<?> debug8 = debug2.getOrCreateTyped(debug1);
/*     */             Dynamic<?> debug9 = (Dynamic)debug8.get(DSL.remainderFinder());
/*     */             debug9 = debug9.set("Damage", debug9.createInt(debug6));
/*     */             debug4 = debug4.set(debug1, debug8.set(DSL.remainderFinder(), debug9));
/*     */           } 
/*     */           return debug4.set(DSL.remainderFinder(), debug5.remove("Damage"));
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static String updateItem(@Nullable String debug0, int debug1) {
/* 446 */     if (IDS.contains(debug0)) {
/* 447 */       String debug2 = MAP.get(debug0 + '.' + debug1);
/* 448 */       return (debug2 == null) ? MAP.get(debug0 + ".0") : debug2;
/*     */     } 
/* 450 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemStackTheFlatteningFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */