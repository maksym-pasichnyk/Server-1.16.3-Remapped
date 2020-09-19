/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.Tag;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.datafixers.util.Unit;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityBlockStateFix
/*     */   extends DataFix
/*     */ {
/*     */   private static final Map<String, Integer> MAP;
/*     */   
/*     */   public EntityBlockStateFix(Schema debug1, boolean debug2) {
/*  34 */     super(debug1, debug2);
/*     */   }
/*     */   static {
/*  37 */     MAP = (Map<String, Integer>)DataFixUtils.make(Maps.newHashMap(), debug0 -> {
/*     */           debug0.put("minecraft:air", Integer.valueOf(0));
/*     */           debug0.put("minecraft:stone", Integer.valueOf(1));
/*     */           debug0.put("minecraft:grass", Integer.valueOf(2));
/*     */           debug0.put("minecraft:dirt", Integer.valueOf(3));
/*     */           debug0.put("minecraft:cobblestone", Integer.valueOf(4));
/*     */           debug0.put("minecraft:planks", Integer.valueOf(5));
/*     */           debug0.put("minecraft:sapling", Integer.valueOf(6));
/*     */           debug0.put("minecraft:bedrock", Integer.valueOf(7));
/*     */           debug0.put("minecraft:flowing_water", Integer.valueOf(8));
/*     */           debug0.put("minecraft:water", Integer.valueOf(9));
/*     */           debug0.put("minecraft:flowing_lava", Integer.valueOf(10));
/*     */           debug0.put("minecraft:lava", Integer.valueOf(11));
/*     */           debug0.put("minecraft:sand", Integer.valueOf(12));
/*     */           debug0.put("minecraft:gravel", Integer.valueOf(13));
/*     */           debug0.put("minecraft:gold_ore", Integer.valueOf(14));
/*     */           debug0.put("minecraft:iron_ore", Integer.valueOf(15));
/*     */           debug0.put("minecraft:coal_ore", Integer.valueOf(16));
/*     */           debug0.put("minecraft:log", Integer.valueOf(17));
/*     */           debug0.put("minecraft:leaves", Integer.valueOf(18));
/*     */           debug0.put("minecraft:sponge", Integer.valueOf(19));
/*     */           debug0.put("minecraft:glass", Integer.valueOf(20));
/*     */           debug0.put("minecraft:lapis_ore", Integer.valueOf(21));
/*     */           debug0.put("minecraft:lapis_block", Integer.valueOf(22));
/*     */           debug0.put("minecraft:dispenser", Integer.valueOf(23));
/*     */           debug0.put("minecraft:sandstone", Integer.valueOf(24));
/*     */           debug0.put("minecraft:noteblock", Integer.valueOf(25));
/*     */           debug0.put("minecraft:bed", Integer.valueOf(26));
/*     */           debug0.put("minecraft:golden_rail", Integer.valueOf(27));
/*     */           debug0.put("minecraft:detector_rail", Integer.valueOf(28));
/*     */           debug0.put("minecraft:sticky_piston", Integer.valueOf(29));
/*     */           debug0.put("minecraft:web", Integer.valueOf(30));
/*     */           debug0.put("minecraft:tallgrass", Integer.valueOf(31));
/*     */           debug0.put("minecraft:deadbush", Integer.valueOf(32));
/*     */           debug0.put("minecraft:piston", Integer.valueOf(33));
/*     */           debug0.put("minecraft:piston_head", Integer.valueOf(34));
/*     */           debug0.put("minecraft:wool", Integer.valueOf(35));
/*     */           debug0.put("minecraft:piston_extension", Integer.valueOf(36));
/*     */           debug0.put("minecraft:yellow_flower", Integer.valueOf(37));
/*     */           debug0.put("minecraft:red_flower", Integer.valueOf(38));
/*     */           debug0.put("minecraft:brown_mushroom", Integer.valueOf(39));
/*     */           debug0.put("minecraft:red_mushroom", Integer.valueOf(40));
/*     */           debug0.put("minecraft:gold_block", Integer.valueOf(41));
/*     */           debug0.put("minecraft:iron_block", Integer.valueOf(42));
/*     */           debug0.put("minecraft:double_stone_slab", Integer.valueOf(43));
/*     */           debug0.put("minecraft:stone_slab", Integer.valueOf(44));
/*     */           debug0.put("minecraft:brick_block", Integer.valueOf(45));
/*     */           debug0.put("minecraft:tnt", Integer.valueOf(46));
/*     */           debug0.put("minecraft:bookshelf", Integer.valueOf(47));
/*     */           debug0.put("minecraft:mossy_cobblestone", Integer.valueOf(48));
/*     */           debug0.put("minecraft:obsidian", Integer.valueOf(49));
/*     */           debug0.put("minecraft:torch", Integer.valueOf(50));
/*     */           debug0.put("minecraft:fire", Integer.valueOf(51));
/*     */           debug0.put("minecraft:mob_spawner", Integer.valueOf(52));
/*     */           debug0.put("minecraft:oak_stairs", Integer.valueOf(53));
/*     */           debug0.put("minecraft:chest", Integer.valueOf(54));
/*     */           debug0.put("minecraft:redstone_wire", Integer.valueOf(55));
/*     */           debug0.put("minecraft:diamond_ore", Integer.valueOf(56));
/*     */           debug0.put("minecraft:diamond_block", Integer.valueOf(57));
/*     */           debug0.put("minecraft:crafting_table", Integer.valueOf(58));
/*     */           debug0.put("minecraft:wheat", Integer.valueOf(59));
/*     */           debug0.put("minecraft:farmland", Integer.valueOf(60));
/*     */           debug0.put("minecraft:furnace", Integer.valueOf(61));
/*     */           debug0.put("minecraft:lit_furnace", Integer.valueOf(62));
/*     */           debug0.put("minecraft:standing_sign", Integer.valueOf(63));
/*     */           debug0.put("minecraft:wooden_door", Integer.valueOf(64));
/*     */           debug0.put("minecraft:ladder", Integer.valueOf(65));
/*     */           debug0.put("minecraft:rail", Integer.valueOf(66));
/*     */           debug0.put("minecraft:stone_stairs", Integer.valueOf(67));
/*     */           debug0.put("minecraft:wall_sign", Integer.valueOf(68));
/*     */           debug0.put("minecraft:lever", Integer.valueOf(69));
/*     */           debug0.put("minecraft:stone_pressure_plate", Integer.valueOf(70));
/*     */           debug0.put("minecraft:iron_door", Integer.valueOf(71));
/*     */           debug0.put("minecraft:wooden_pressure_plate", Integer.valueOf(72));
/*     */           debug0.put("minecraft:redstone_ore", Integer.valueOf(73));
/*     */           debug0.put("minecraft:lit_redstone_ore", Integer.valueOf(74));
/*     */           debug0.put("minecraft:unlit_redstone_torch", Integer.valueOf(75));
/*     */           debug0.put("minecraft:redstone_torch", Integer.valueOf(76));
/*     */           debug0.put("minecraft:stone_button", Integer.valueOf(77));
/*     */           debug0.put("minecraft:snow_layer", Integer.valueOf(78));
/*     */           debug0.put("minecraft:ice", Integer.valueOf(79));
/*     */           debug0.put("minecraft:snow", Integer.valueOf(80));
/*     */           debug0.put("minecraft:cactus", Integer.valueOf(81));
/*     */           debug0.put("minecraft:clay", Integer.valueOf(82));
/*     */           debug0.put("minecraft:reeds", Integer.valueOf(83));
/*     */           debug0.put("minecraft:jukebox", Integer.valueOf(84));
/*     */           debug0.put("minecraft:fence", Integer.valueOf(85));
/*     */           debug0.put("minecraft:pumpkin", Integer.valueOf(86));
/*     */           debug0.put("minecraft:netherrack", Integer.valueOf(87));
/*     */           debug0.put("minecraft:soul_sand", Integer.valueOf(88));
/*     */           debug0.put("minecraft:glowstone", Integer.valueOf(89));
/*     */           debug0.put("minecraft:portal", Integer.valueOf(90));
/*     */           debug0.put("minecraft:lit_pumpkin", Integer.valueOf(91));
/*     */           debug0.put("minecraft:cake", Integer.valueOf(92));
/*     */           debug0.put("minecraft:unpowered_repeater", Integer.valueOf(93));
/*     */           debug0.put("minecraft:powered_repeater", Integer.valueOf(94));
/*     */           debug0.put("minecraft:stained_glass", Integer.valueOf(95));
/*     */           debug0.put("minecraft:trapdoor", Integer.valueOf(96));
/*     */           debug0.put("minecraft:monster_egg", Integer.valueOf(97));
/*     */           debug0.put("minecraft:stonebrick", Integer.valueOf(98));
/*     */           debug0.put("minecraft:brown_mushroom_block", Integer.valueOf(99));
/*     */           debug0.put("minecraft:red_mushroom_block", Integer.valueOf(100));
/*     */           debug0.put("minecraft:iron_bars", Integer.valueOf(101));
/*     */           debug0.put("minecraft:glass_pane", Integer.valueOf(102));
/*     */           debug0.put("minecraft:melon_block", Integer.valueOf(103));
/*     */           debug0.put("minecraft:pumpkin_stem", Integer.valueOf(104));
/*     */           debug0.put("minecraft:melon_stem", Integer.valueOf(105));
/*     */           debug0.put("minecraft:vine", Integer.valueOf(106));
/*     */           debug0.put("minecraft:fence_gate", Integer.valueOf(107));
/*     */           debug0.put("minecraft:brick_stairs", Integer.valueOf(108));
/*     */           debug0.put("minecraft:stone_brick_stairs", Integer.valueOf(109));
/*     */           debug0.put("minecraft:mycelium", Integer.valueOf(110));
/*     */           debug0.put("minecraft:waterlily", Integer.valueOf(111));
/*     */           debug0.put("minecraft:nether_brick", Integer.valueOf(112));
/*     */           debug0.put("minecraft:nether_brick_fence", Integer.valueOf(113));
/*     */           debug0.put("minecraft:nether_brick_stairs", Integer.valueOf(114));
/*     */           debug0.put("minecraft:nether_wart", Integer.valueOf(115));
/*     */           debug0.put("minecraft:enchanting_table", Integer.valueOf(116));
/*     */           debug0.put("minecraft:brewing_stand", Integer.valueOf(117));
/*     */           debug0.put("minecraft:cauldron", Integer.valueOf(118));
/*     */           debug0.put("minecraft:end_portal", Integer.valueOf(119));
/*     */           debug0.put("minecraft:end_portal_frame", Integer.valueOf(120));
/*     */           debug0.put("minecraft:end_stone", Integer.valueOf(121));
/*     */           debug0.put("minecraft:dragon_egg", Integer.valueOf(122));
/*     */           debug0.put("minecraft:redstone_lamp", Integer.valueOf(123));
/*     */           debug0.put("minecraft:lit_redstone_lamp", Integer.valueOf(124));
/*     */           debug0.put("minecraft:double_wooden_slab", Integer.valueOf(125));
/*     */           debug0.put("minecraft:wooden_slab", Integer.valueOf(126));
/*     */           debug0.put("minecraft:cocoa", Integer.valueOf(127));
/*     */           debug0.put("minecraft:sandstone_stairs", Integer.valueOf(128));
/*     */           debug0.put("minecraft:emerald_ore", Integer.valueOf(129));
/*     */           debug0.put("minecraft:ender_chest", Integer.valueOf(130));
/*     */           debug0.put("minecraft:tripwire_hook", Integer.valueOf(131));
/*     */           debug0.put("minecraft:tripwire", Integer.valueOf(132));
/*     */           debug0.put("minecraft:emerald_block", Integer.valueOf(133));
/*     */           debug0.put("minecraft:spruce_stairs", Integer.valueOf(134));
/*     */           debug0.put("minecraft:birch_stairs", Integer.valueOf(135));
/*     */           debug0.put("minecraft:jungle_stairs", Integer.valueOf(136));
/*     */           debug0.put("minecraft:command_block", Integer.valueOf(137));
/*     */           debug0.put("minecraft:beacon", Integer.valueOf(138));
/*     */           debug0.put("minecraft:cobblestone_wall", Integer.valueOf(139));
/*     */           debug0.put("minecraft:flower_pot", Integer.valueOf(140));
/*     */           debug0.put("minecraft:carrots", Integer.valueOf(141));
/*     */           debug0.put("minecraft:potatoes", Integer.valueOf(142));
/*     */           debug0.put("minecraft:wooden_button", Integer.valueOf(143));
/*     */           debug0.put("minecraft:skull", Integer.valueOf(144));
/*     */           debug0.put("minecraft:anvil", Integer.valueOf(145));
/*     */           debug0.put("minecraft:trapped_chest", Integer.valueOf(146));
/*     */           debug0.put("minecraft:light_weighted_pressure_plate", Integer.valueOf(147));
/*     */           debug0.put("minecraft:heavy_weighted_pressure_plate", Integer.valueOf(148));
/*     */           debug0.put("minecraft:unpowered_comparator", Integer.valueOf(149));
/*     */           debug0.put("minecraft:powered_comparator", Integer.valueOf(150));
/*     */           debug0.put("minecraft:daylight_detector", Integer.valueOf(151));
/*     */           debug0.put("minecraft:redstone_block", Integer.valueOf(152));
/*     */           debug0.put("minecraft:quartz_ore", Integer.valueOf(153));
/*     */           debug0.put("minecraft:hopper", Integer.valueOf(154));
/*     */           debug0.put("minecraft:quartz_block", Integer.valueOf(155));
/*     */           debug0.put("minecraft:quartz_stairs", Integer.valueOf(156));
/*     */           debug0.put("minecraft:activator_rail", Integer.valueOf(157));
/*     */           debug0.put("minecraft:dropper", Integer.valueOf(158));
/*     */           debug0.put("minecraft:stained_hardened_clay", Integer.valueOf(159));
/*     */           debug0.put("minecraft:stained_glass_pane", Integer.valueOf(160));
/*     */           debug0.put("minecraft:leaves2", Integer.valueOf(161));
/*     */           debug0.put("minecraft:log2", Integer.valueOf(162));
/*     */           debug0.put("minecraft:acacia_stairs", Integer.valueOf(163));
/*     */           debug0.put("minecraft:dark_oak_stairs", Integer.valueOf(164));
/*     */           debug0.put("minecraft:slime", Integer.valueOf(165));
/*     */           debug0.put("minecraft:barrier", Integer.valueOf(166));
/*     */           debug0.put("minecraft:iron_trapdoor", Integer.valueOf(167));
/*     */           debug0.put("minecraft:prismarine", Integer.valueOf(168));
/*     */           debug0.put("minecraft:sea_lantern", Integer.valueOf(169));
/*     */           debug0.put("minecraft:hay_block", Integer.valueOf(170));
/*     */           debug0.put("minecraft:carpet", Integer.valueOf(171));
/*     */           debug0.put("minecraft:hardened_clay", Integer.valueOf(172));
/*     */           debug0.put("minecraft:coal_block", Integer.valueOf(173));
/*     */           debug0.put("minecraft:packed_ice", Integer.valueOf(174));
/*     */           debug0.put("minecraft:double_plant", Integer.valueOf(175));
/*     */           debug0.put("minecraft:standing_banner", Integer.valueOf(176));
/*     */           debug0.put("minecraft:wall_banner", Integer.valueOf(177));
/*     */           debug0.put("minecraft:daylight_detector_inverted", Integer.valueOf(178));
/*     */           debug0.put("minecraft:red_sandstone", Integer.valueOf(179));
/*     */           debug0.put("minecraft:red_sandstone_stairs", Integer.valueOf(180));
/*     */           debug0.put("minecraft:double_stone_slab2", Integer.valueOf(181));
/*     */           debug0.put("minecraft:stone_slab2", Integer.valueOf(182));
/*     */           debug0.put("minecraft:spruce_fence_gate", Integer.valueOf(183));
/*     */           debug0.put("minecraft:birch_fence_gate", Integer.valueOf(184));
/*     */           debug0.put("minecraft:jungle_fence_gate", Integer.valueOf(185));
/*     */           debug0.put("minecraft:dark_oak_fence_gate", Integer.valueOf(186));
/*     */           debug0.put("minecraft:acacia_fence_gate", Integer.valueOf(187));
/*     */           debug0.put("minecraft:spruce_fence", Integer.valueOf(188));
/*     */           debug0.put("minecraft:birch_fence", Integer.valueOf(189));
/*     */           debug0.put("minecraft:jungle_fence", Integer.valueOf(190));
/*     */           debug0.put("minecraft:dark_oak_fence", Integer.valueOf(191));
/*     */           debug0.put("minecraft:acacia_fence", Integer.valueOf(192));
/*     */           debug0.put("minecraft:spruce_door", Integer.valueOf(193));
/*     */           debug0.put("minecraft:birch_door", Integer.valueOf(194));
/*     */           debug0.put("minecraft:jungle_door", Integer.valueOf(195));
/*     */           debug0.put("minecraft:acacia_door", Integer.valueOf(196));
/*     */           debug0.put("minecraft:dark_oak_door", Integer.valueOf(197));
/*     */           debug0.put("minecraft:end_rod", Integer.valueOf(198));
/*     */           debug0.put("minecraft:chorus_plant", Integer.valueOf(199));
/*     */           debug0.put("minecraft:chorus_flower", Integer.valueOf(200));
/*     */           debug0.put("minecraft:purpur_block", Integer.valueOf(201));
/*     */           debug0.put("minecraft:purpur_pillar", Integer.valueOf(202));
/*     */           debug0.put("minecraft:purpur_stairs", Integer.valueOf(203));
/*     */           debug0.put("minecraft:purpur_double_slab", Integer.valueOf(204));
/*     */           debug0.put("minecraft:purpur_slab", Integer.valueOf(205));
/*     */           debug0.put("minecraft:end_bricks", Integer.valueOf(206));
/*     */           debug0.put("minecraft:beetroots", Integer.valueOf(207));
/*     */           debug0.put("minecraft:grass_path", Integer.valueOf(208));
/*     */           debug0.put("minecraft:end_gateway", Integer.valueOf(209));
/*     */           debug0.put("minecraft:repeating_command_block", Integer.valueOf(210));
/*     */           debug0.put("minecraft:chain_command_block", Integer.valueOf(211));
/*     */           debug0.put("minecraft:frosted_ice", Integer.valueOf(212));
/*     */           debug0.put("minecraft:magma", Integer.valueOf(213));
/*     */           debug0.put("minecraft:nether_wart_block", Integer.valueOf(214));
/*     */           debug0.put("minecraft:red_nether_brick", Integer.valueOf(215));
/*     */           debug0.put("minecraft:bone_block", Integer.valueOf(216));
/*     */           debug0.put("minecraft:structure_void", Integer.valueOf(217));
/*     */           debug0.put("minecraft:observer", Integer.valueOf(218));
/*     */           debug0.put("minecraft:white_shulker_box", Integer.valueOf(219));
/*     */           debug0.put("minecraft:orange_shulker_box", Integer.valueOf(220));
/*     */           debug0.put("minecraft:magenta_shulker_box", Integer.valueOf(221));
/*     */           debug0.put("minecraft:light_blue_shulker_box", Integer.valueOf(222));
/*     */           debug0.put("minecraft:yellow_shulker_box", Integer.valueOf(223));
/*     */           debug0.put("minecraft:lime_shulker_box", Integer.valueOf(224));
/*     */           debug0.put("minecraft:pink_shulker_box", Integer.valueOf(225));
/*     */           debug0.put("minecraft:gray_shulker_box", Integer.valueOf(226));
/*     */           debug0.put("minecraft:silver_shulker_box", Integer.valueOf(227));
/*     */           debug0.put("minecraft:cyan_shulker_box", Integer.valueOf(228));
/*     */           debug0.put("minecraft:purple_shulker_box", Integer.valueOf(229));
/*     */           debug0.put("minecraft:blue_shulker_box", Integer.valueOf(230));
/*     */           debug0.put("minecraft:brown_shulker_box", Integer.valueOf(231));
/*     */           debug0.put("minecraft:green_shulker_box", Integer.valueOf(232));
/*     */           debug0.put("minecraft:red_shulker_box", Integer.valueOf(233));
/*     */           debug0.put("minecraft:black_shulker_box", Integer.valueOf(234));
/*     */           debug0.put("minecraft:white_glazed_terracotta", Integer.valueOf(235));
/*     */           debug0.put("minecraft:orange_glazed_terracotta", Integer.valueOf(236));
/*     */           debug0.put("minecraft:magenta_glazed_terracotta", Integer.valueOf(237));
/*     */           debug0.put("minecraft:light_blue_glazed_terracotta", Integer.valueOf(238));
/*     */           debug0.put("minecraft:yellow_glazed_terracotta", Integer.valueOf(239));
/*     */           debug0.put("minecraft:lime_glazed_terracotta", Integer.valueOf(240));
/*     */           debug0.put("minecraft:pink_glazed_terracotta", Integer.valueOf(241));
/*     */           debug0.put("minecraft:gray_glazed_terracotta", Integer.valueOf(242));
/*     */           debug0.put("minecraft:silver_glazed_terracotta", Integer.valueOf(243));
/*     */           debug0.put("minecraft:cyan_glazed_terracotta", Integer.valueOf(244));
/*     */           debug0.put("minecraft:purple_glazed_terracotta", Integer.valueOf(245));
/*     */           debug0.put("minecraft:blue_glazed_terracotta", Integer.valueOf(246));
/*     */           debug0.put("minecraft:brown_glazed_terracotta", Integer.valueOf(247));
/*     */           debug0.put("minecraft:green_glazed_terracotta", Integer.valueOf(248));
/*     */           debug0.put("minecraft:red_glazed_terracotta", Integer.valueOf(249));
/*     */           debug0.put("minecraft:black_glazed_terracotta", Integer.valueOf(250));
/*     */           debug0.put("minecraft:concrete", Integer.valueOf(251));
/*     */           debug0.put("minecraft:concrete_powder", Integer.valueOf(252));
/*     */           debug0.put("minecraft:structure_block", Integer.valueOf(255));
/*     */         });
/*     */   }
/*     */   public static int getBlockId(String debug0) {
/* 295 */     Integer debug1 = MAP.get(debug0);
/* 296 */     return (debug1 == null) ? 0 : debug1.intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/* 301 */     Schema debug1 = getInputSchema();
/* 302 */     Schema debug2 = getOutputSchema();
/*     */     
/* 304 */     Function<Typed<?>, Typed<?>> debug3 = debug1 -> updateBlockToBlockState(debug1, "DisplayTile", "DisplayData", "DisplayState");
/* 305 */     Function<Typed<?>, Typed<?>> debug4 = debug1 -> updateBlockToBlockState(debug1, "inTile", "inData", "inBlockState");
/*     */     
/* 307 */     Type<Pair<Either<Pair<String, Either<Integer, String>>, Unit>, Dynamic<?>>> debug5 = DSL.and(
/* 308 */         DSL.optional((Type)DSL.field("inTile", DSL.named(References.BLOCK_NAME.typeName(), DSL.or(DSL.intType(), NamespacedSchema.namespacedString())))), 
/* 309 */         DSL.remainderType());
/*     */ 
/*     */     
/* 312 */     Function<Typed<?>, Typed<?>> debug6 = debug1 -> debug1.update(debug0.finder(), DSL.remainderType(), Pair::getSecond);
/*     */     
/* 314 */     return fixTypeEverywhereTyped("EntityBlockStateFix", debug1.getType(References.ENTITY), debug2.getType(References.ENTITY), debug4 -> {
/*     */           debug4 = updateEntity(debug4, "minecraft:falling_block", this::updateFallingBlock);
/*     */           debug4 = updateEntity(debug4, "minecraft:enderman", ());
/*     */           debug4 = updateEntity(debug4, "minecraft:arrow", debug1);
/*     */           debug4 = updateEntity(debug4, "minecraft:spectral_arrow", debug1);
/*     */           debug4 = updateEntity(debug4, "minecraft:egg", debug2);
/*     */           debug4 = updateEntity(debug4, "minecraft:ender_pearl", debug2);
/*     */           debug4 = updateEntity(debug4, "minecraft:fireball", debug2);
/*     */           debug4 = updateEntity(debug4, "minecraft:potion", debug2);
/*     */           debug4 = updateEntity(debug4, "minecraft:small_fireball", debug2);
/*     */           debug4 = updateEntity(debug4, "minecraft:snowball", debug2);
/*     */           debug4 = updateEntity(debug4, "minecraft:wither_skull", debug2);
/*     */           debug4 = updateEntity(debug4, "minecraft:xp_bottle", debug2);
/*     */           debug4 = updateEntity(debug4, "minecraft:commandblock_minecart", debug3);
/*     */           debug4 = updateEntity(debug4, "minecraft:minecart", debug3);
/*     */           debug4 = updateEntity(debug4, "minecraft:chest_minecart", debug3);
/*     */           debug4 = updateEntity(debug4, "minecraft:furnace_minecart", debug3);
/*     */           debug4 = updateEntity(debug4, "minecraft:tnt_minecart", debug3);
/*     */           debug4 = updateEntity(debug4, "minecraft:hopper_minecart", debug3);
/*     */           return updateEntity(debug4, "minecraft:spawner_minecart", debug3);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private Typed<?> updateFallingBlock(Typed<?> debug1) {
/* 339 */     Type<Either<Pair<String, Either<Integer, String>>, Unit>> debug2 = DSL.optional((Type)DSL.field("Block", DSL.named(References.BLOCK_NAME.typeName(), DSL.or(DSL.intType(), NamespacedSchema.namespacedString()))));
/* 340 */     Type<Either<Pair<String, Dynamic<?>>, Unit>> debug3 = DSL.optional((Type)DSL.field("BlockState", DSL.named(References.BLOCK_STATE.typeName(), DSL.remainderType())));
/*     */     
/* 342 */     Dynamic<?> debug4 = (Dynamic)debug1.get(DSL.remainderFinder());
/*     */     
/* 344 */     return debug1.update(debug2.finder(), debug3, debug1 -> {
/*     */           int debug2 = ((Integer)debug1.map((), ())).intValue();
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           int debug3 = debug0.get("Data").asInt(0) & 0xF;
/*     */ 
/*     */ 
/*     */           
/*     */           return Either.left(Pair.of(References.BLOCK_STATE.typeName(), BlockStateData.getTag(debug2 << 4 | debug3)));
/* 355 */         }).set(DSL.remainderFinder(), debug4.remove("Data").remove("TileID").remove("Tile"));
/*     */   }
/*     */   
/*     */   private Typed<?> updateBlockToBlockState(Typed<?> debug1, String debug2, String debug3, String debug4) {
/* 359 */     Tag.TagType tagType1 = DSL.field(debug2, DSL.named(References.BLOCK_NAME.typeName(), DSL.or(DSL.intType(), NamespacedSchema.namespacedString())));
/* 360 */     Tag.TagType tagType2 = DSL.field(debug4, DSL.named(References.BLOCK_STATE.typeName(), DSL.remainderType()));
/*     */     
/* 362 */     Dynamic<?> debug7 = (Dynamic)debug1.getOrCreate(DSL.remainderFinder());
/*     */     
/* 364 */     return debug1.update(tagType1.finder(), (Type)tagType2, debug2 -> {
/*     */           int debug3 = ((Integer)((Either)debug2.getSecond()).map((), EntityBlockStateFix::getBlockId)).intValue();
/*     */           
/*     */           int debug4 = debug0.get(debug1).asInt(0) & 0xF;
/*     */           return Pair.of(References.BLOCK_STATE.typeName(), BlockStateData.getTag(debug3 << 4 | debug4));
/* 369 */         }).set(DSL.remainderFinder(), debug7.remove(debug3));
/*     */   }
/*     */   
/*     */   private Typed<?> updateEntity(Typed<?> debug1, String debug2, Function<Typed<?>, Typed<?>> debug3) {
/* 373 */     Type<?> debug4 = getInputSchema().getChoiceType(References.ENTITY, debug2);
/* 374 */     Type<?> debug5 = getOutputSchema().getChoiceType(References.ENTITY, debug2);
/* 375 */     return debug1.updateTyped(DSL.namedChoice(debug2, debug4), debug5, debug3);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityBlockStateFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */