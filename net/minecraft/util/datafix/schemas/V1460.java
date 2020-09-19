/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.datafix.fixes.References;
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
/*     */ public class V1460
/*     */   extends NamespacedSchema
/*     */ {
/*     */   public V1460(int debug1, Schema debug2) {
/*  50 */     super(debug1, debug2);
/*     */   }
/*     */   
/*     */   protected static void registerMob(Schema debug0, Map<String, Supplier<TypeTemplate>> debug1, String debug2) {
/*  54 */     debug0.register(debug1, debug2, () -> V100.equipment(debug0));
/*     */   }
/*     */   
/*     */   protected static void registerInventory(Schema debug0, Map<String, Supplier<TypeTemplate>> debug1, String debug2) {
/*  58 */     debug0.register(debug1, debug2, () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(debug0))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema debug1) {
/*  65 */     Map<String, Supplier<TypeTemplate>> debug2 = Maps.newHashMap();
/*     */     
/*  67 */     debug1.registerSimple(debug2, "minecraft:area_effect_cloud");
/*  68 */     registerMob(debug1, debug2, "minecraft:armor_stand");
/*  69 */     debug1.register(debug2, "minecraft:arrow", debug1 -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(debug0)));
/*     */ 
/*     */     
/*  72 */     registerMob(debug1, debug2, "minecraft:bat");
/*  73 */     registerMob(debug1, debug2, "minecraft:blaze");
/*  74 */     debug1.registerSimple(debug2, "minecraft:boat");
/*  75 */     registerMob(debug1, debug2, "minecraft:cave_spider");
/*  76 */     debug1.register(debug2, "minecraft:chest_minecart", debug1 -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(debug0), "Items", DSL.list(References.ITEM_STACK.in(debug0))));
/*     */ 
/*     */ 
/*     */     
/*  80 */     registerMob(debug1, debug2, "minecraft:chicken");
/*  81 */     debug1.register(debug2, "minecraft:commandblock_minecart", debug1 -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(debug0)));
/*     */ 
/*     */     
/*  84 */     registerMob(debug1, debug2, "minecraft:cow");
/*  85 */     registerMob(debug1, debug2, "minecraft:creeper");
/*  86 */     debug1.register(debug2, "minecraft:donkey", debug1 -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(debug0)), "SaddleItem", References.ITEM_STACK.in(debug0), V100.equipment(debug0)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     debug1.registerSimple(debug2, "minecraft:dragon_fireball");
/*  92 */     debug1.registerSimple(debug2, "minecraft:egg");
/*  93 */     registerMob(debug1, debug2, "minecraft:elder_guardian");
/*  94 */     debug1.registerSimple(debug2, "minecraft:ender_crystal");
/*  95 */     registerMob(debug1, debug2, "minecraft:ender_dragon");
/*  96 */     debug1.register(debug2, "minecraft:enderman", debug1 -> DSL.optionalFields("carriedBlockState", References.BLOCK_STATE.in(debug0), V100.equipment(debug0)));
/*     */ 
/*     */ 
/*     */     
/* 100 */     registerMob(debug1, debug2, "minecraft:endermite");
/* 101 */     debug1.registerSimple(debug2, "minecraft:ender_pearl");
/* 102 */     debug1.registerSimple(debug2, "minecraft:evocation_fangs");
/* 103 */     registerMob(debug1, debug2, "minecraft:evocation_illager");
/* 104 */     debug1.registerSimple(debug2, "minecraft:eye_of_ender_signal");
/* 105 */     debug1.register(debug2, "minecraft:falling_block", debug1 -> DSL.optionalFields("BlockState", References.BLOCK_STATE.in(debug0), "TileEntityData", References.BLOCK_ENTITY.in(debug0)));
/*     */ 
/*     */ 
/*     */     
/* 109 */     debug1.registerSimple(debug2, "minecraft:fireball");
/* 110 */     debug1.register(debug2, "minecraft:fireworks_rocket", debug1 -> DSL.optionalFields("FireworksItem", References.ITEM_STACK.in(debug0)));
/*     */ 
/*     */     
/* 113 */     debug1.register(debug2, "minecraft:furnace_minecart", debug1 -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(debug0)));
/*     */ 
/*     */     
/* 116 */     registerMob(debug1, debug2, "minecraft:ghast");
/* 117 */     registerMob(debug1, debug2, "minecraft:giant");
/* 118 */     registerMob(debug1, debug2, "minecraft:guardian");
/* 119 */     debug1.register(debug2, "minecraft:hopper_minecart", debug1 -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(debug0), "Items", DSL.list(References.ITEM_STACK.in(debug0))));
/*     */ 
/*     */ 
/*     */     
/* 123 */     debug1.register(debug2, "minecraft:horse", debug1 -> DSL.optionalFields("ArmorItem", References.ITEM_STACK.in(debug0), "SaddleItem", References.ITEM_STACK.in(debug0), V100.equipment(debug0)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     registerMob(debug1, debug2, "minecraft:husk");
/* 130 */     debug1.registerSimple(debug2, "minecraft:illusion_illager");
/* 131 */     debug1.register(debug2, "minecraft:item", debug1 -> DSL.optionalFields("Item", References.ITEM_STACK.in(debug0)));
/*     */ 
/*     */     
/* 134 */     debug1.register(debug2, "minecraft:item_frame", debug1 -> DSL.optionalFields("Item", References.ITEM_STACK.in(debug0)));
/*     */ 
/*     */     
/* 137 */     debug1.registerSimple(debug2, "minecraft:leash_knot");
/* 138 */     debug1.register(debug2, "minecraft:llama", debug1 -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(debug0)), "SaddleItem", References.ITEM_STACK.in(debug0), "DecorItem", References.ITEM_STACK.in(debug0), V100.equipment(debug0)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     debug1.registerSimple(debug2, "minecraft:llama_spit");
/* 145 */     registerMob(debug1, debug2, "minecraft:magma_cube");
/* 146 */     debug1.register(debug2, "minecraft:minecart", debug1 -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(debug0)));
/*     */ 
/*     */     
/* 149 */     registerMob(debug1, debug2, "minecraft:mooshroom");
/* 150 */     debug1.register(debug2, "minecraft:mule", debug1 -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(debug0)), "SaddleItem", References.ITEM_STACK.in(debug0), V100.equipment(debug0)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 155 */     registerMob(debug1, debug2, "minecraft:ocelot");
/* 156 */     debug1.registerSimple(debug2, "minecraft:painting");
/* 157 */     debug1.registerSimple(debug2, "minecraft:parrot");
/* 158 */     registerMob(debug1, debug2, "minecraft:pig");
/* 159 */     registerMob(debug1, debug2, "minecraft:polar_bear");
/* 160 */     debug1.register(debug2, "minecraft:potion", debug1 -> DSL.optionalFields("Potion", References.ITEM_STACK.in(debug0)));
/*     */ 
/*     */     
/* 163 */     registerMob(debug1, debug2, "minecraft:rabbit");
/* 164 */     registerMob(debug1, debug2, "minecraft:sheep");
/* 165 */     registerMob(debug1, debug2, "minecraft:shulker");
/* 166 */     debug1.registerSimple(debug2, "minecraft:shulker_bullet");
/* 167 */     registerMob(debug1, debug2, "minecraft:silverfish");
/* 168 */     registerMob(debug1, debug2, "minecraft:skeleton");
/* 169 */     debug1.register(debug2, "minecraft:skeleton_horse", debug1 -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(debug0), V100.equipment(debug0)));
/*     */ 
/*     */ 
/*     */     
/* 173 */     registerMob(debug1, debug2, "minecraft:slime");
/* 174 */     debug1.registerSimple(debug2, "minecraft:small_fireball");
/* 175 */     debug1.registerSimple(debug2, "minecraft:snowball");
/* 176 */     registerMob(debug1, debug2, "minecraft:snowman");
/* 177 */     debug1.register(debug2, "minecraft:spawner_minecart", debug1 -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(debug0), References.UNTAGGED_SPAWNER.in(debug0)));
/*     */ 
/*     */ 
/*     */     
/* 181 */     debug1.register(debug2, "minecraft:spectral_arrow", debug1 -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(debug0)));
/*     */ 
/*     */     
/* 184 */     registerMob(debug1, debug2, "minecraft:spider");
/* 185 */     registerMob(debug1, debug2, "minecraft:squid");
/* 186 */     registerMob(debug1, debug2, "minecraft:stray");
/* 187 */     debug1.registerSimple(debug2, "minecraft:tnt");
/* 188 */     debug1.register(debug2, "minecraft:tnt_minecart", debug1 -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(debug0)));
/*     */ 
/*     */     
/* 191 */     registerMob(debug1, debug2, "minecraft:vex");
/* 192 */     debug1.register(debug2, "minecraft:villager", debug1 -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(debug0)), "Offers", DSL.optionalFields("Recipes", DSL.list(DSL.optionalFields("buy", References.ITEM_STACK.in(debug0), "buyB", References.ITEM_STACK.in(debug0), "sell", References.ITEM_STACK.in(debug0)))), V100.equipment(debug0)));
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
/* 205 */     registerMob(debug1, debug2, "minecraft:villager_golem");
/* 206 */     registerMob(debug1, debug2, "minecraft:vindication_illager");
/* 207 */     registerMob(debug1, debug2, "minecraft:witch");
/* 208 */     registerMob(debug1, debug2, "minecraft:wither");
/* 209 */     registerMob(debug1, debug2, "minecraft:wither_skeleton");
/* 210 */     debug1.registerSimple(debug2, "minecraft:wither_skull");
/* 211 */     registerMob(debug1, debug2, "minecraft:wolf");
/* 212 */     debug1.registerSimple(debug2, "minecraft:xp_bottle");
/* 213 */     debug1.registerSimple(debug2, "minecraft:xp_orb");
/* 214 */     registerMob(debug1, debug2, "minecraft:zombie");
/* 215 */     debug1.register(debug2, "minecraft:zombie_horse", debug1 -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(debug0), V100.equipment(debug0)));
/*     */ 
/*     */ 
/*     */     
/* 219 */     registerMob(debug1, debug2, "minecraft:zombie_pigman");
/* 220 */     registerMob(debug1, debug2, "minecraft:zombie_villager");
/*     */     
/* 222 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema debug1) {
/* 227 */     Map<String, Supplier<TypeTemplate>> debug2 = Maps.newHashMap();
/*     */     
/* 229 */     registerInventory(debug1, debug2, "minecraft:furnace");
/* 230 */     registerInventory(debug1, debug2, "minecraft:chest");
/* 231 */     registerInventory(debug1, debug2, "minecraft:trapped_chest");
/* 232 */     debug1.registerSimple(debug2, "minecraft:ender_chest");
/* 233 */     debug1.register(debug2, "minecraft:jukebox", debug1 -> DSL.optionalFields("RecordItem", References.ITEM_STACK.in(debug0)));
/*     */ 
/*     */     
/* 236 */     registerInventory(debug1, debug2, "minecraft:dispenser");
/* 237 */     registerInventory(debug1, debug2, "minecraft:dropper");
/* 238 */     debug1.registerSimple(debug2, "minecraft:sign");
/* 239 */     debug1.register(debug2, "minecraft:mob_spawner", debug1 -> References.UNTAGGED_SPAWNER.in(debug0));
/* 240 */     debug1.register(debug2, "minecraft:piston", debug1 -> DSL.optionalFields("blockState", References.BLOCK_STATE.in(debug0)));
/*     */ 
/*     */     
/* 243 */     registerInventory(debug1, debug2, "minecraft:brewing_stand");
/* 244 */     debug1.registerSimple(debug2, "minecraft:enchanting_table");
/* 245 */     debug1.registerSimple(debug2, "minecraft:end_portal");
/* 246 */     debug1.registerSimple(debug2, "minecraft:beacon");
/* 247 */     debug1.registerSimple(debug2, "minecraft:skull");
/* 248 */     debug1.registerSimple(debug2, "minecraft:daylight_detector");
/* 249 */     registerInventory(debug1, debug2, "minecraft:hopper");
/* 250 */     debug1.registerSimple(debug2, "minecraft:comparator");
/* 251 */     debug1.registerSimple(debug2, "minecraft:banner");
/* 252 */     debug1.registerSimple(debug2, "minecraft:structure_block");
/* 253 */     debug1.registerSimple(debug2, "minecraft:end_gateway");
/* 254 */     debug1.registerSimple(debug2, "minecraft:command_block");
/* 255 */     registerInventory(debug1, debug2, "minecraft:shulker_box");
/* 256 */     debug1.registerSimple(debug2, "minecraft:bed");
/*     */     
/* 258 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerTypes(Schema debug1, Map<String, Supplier<TypeTemplate>> debug2, Map<String, Supplier<TypeTemplate>> debug3) {
/* 263 */     debug1.registerType(false, References.LEVEL, DSL::remainder);
/* 264 */     debug1.registerType(false, References.RECIPE, () -> DSL.constType(namespacedString()));
/* 265 */     debug1.registerType(false, References.PLAYER, () -> DSL.optionalFields("RootVehicle", DSL.optionalFields("Entity", References.ENTITY_TREE.in(debug0)), "Inventory", DSL.list(References.ITEM_STACK.in(debug0)), "EnderItems", DSL.list(References.ITEM_STACK.in(debug0)), DSL.optionalFields("ShoulderEntityLeft", References.ENTITY_TREE.in(debug0), "ShoulderEntityRight", References.ENTITY_TREE.in(debug0), "recipeBook", DSL.optionalFields("recipes", DSL.list(References.RECIPE.in(debug0)), "toBeDisplayed", DSL.list(References.RECIPE.in(debug0))))));
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
/* 282 */     debug1.registerType(false, References.CHUNK, () -> DSL.fields("Level", DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(debug0)), "TileEntities", DSL.list(References.BLOCK_ENTITY.in(debug0)), "TileTicks", DSL.list(DSL.fields("i", References.BLOCK_NAME.in(debug0))), "Sections", DSL.list(DSL.optionalFields("Palette", DSL.list(References.BLOCK_STATE.in(debug0)))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 292 */     debug1.registerType(true, References.BLOCK_ENTITY, () -> DSL.taggedChoiceLazy("id", namespacedString(), debug0));
/* 293 */     debug1.registerType(true, References.ENTITY_TREE, () -> DSL.optionalFields("Passengers", DSL.list(References.ENTITY_TREE.in(debug0)), References.ENTITY.in(debug0)));
/*     */ 
/*     */ 
/*     */     
/* 297 */     debug1.registerType(true, References.ENTITY, () -> DSL.taggedChoiceLazy("id", namespacedString(), debug0));
/* 298 */     debug1.registerType(true, References.ITEM_STACK, () -> DSL.hook(DSL.optionalFields("id", References.ITEM_NAME.in(debug0), "tag", DSL.optionalFields("EntityTag", References.ENTITY_TREE.in(debug0), "BlockEntityTag", References.BLOCK_ENTITY.in(debug0), "CanDestroy", DSL.list(References.BLOCK_NAME.in(debug0)), "CanPlaceOn", DSL.list(References.BLOCK_NAME.in(debug0)))), V705.ADD_NAMES, Hook.HookFunction.IDENTITY));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 307 */     debug1.registerType(false, References.HOTBAR, () -> DSL.compoundList(DSL.list(References.ITEM_STACK.in(debug0))));
/* 308 */     debug1.registerType(false, References.OPTIONS, DSL::remainder);
/* 309 */     debug1.registerType(false, References.STRUCTURE, () -> DSL.optionalFields("entities", DSL.list(DSL.optionalFields("nbt", References.ENTITY_TREE.in(debug0))), "blocks", DSL.list(DSL.optionalFields("nbt", References.BLOCK_ENTITY.in(debug0))), "palette", DSL.list(References.BLOCK_STATE.in(debug0))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 314 */     debug1.registerType(false, References.BLOCK_NAME, () -> DSL.constType(namespacedString()));
/* 315 */     debug1.registerType(false, References.ITEM_NAME, () -> DSL.constType(namespacedString()));
/* 316 */     debug1.registerType(false, References.BLOCK_STATE, DSL::remainder);
/* 317 */     Supplier<TypeTemplate> debug4 = () -> DSL.compoundList(References.ITEM_NAME.in(debug0), DSL.constType(DSL.intType()));
/*     */     
/* 319 */     debug1.registerType(false, References.STATS, () -> DSL.optionalFields("stats", DSL.optionalFields("minecraft:mined", DSL.compoundList(References.BLOCK_NAME.in(debug0), DSL.constType(DSL.intType())), "minecraft:crafted", debug1.get(), "minecraft:used", debug1.get(), "minecraft:broken", debug1.get(), "minecraft:picked_up", debug1.get(), DSL.optionalFields("minecraft:dropped", debug1.get(), "minecraft:killed", DSL.compoundList(References.ENTITY_NAME.in(debug0), DSL.constType(DSL.intType())), "minecraft:killed_by", DSL.compoundList(References.ENTITY_NAME.in(debug0), DSL.constType(DSL.intType())), "minecraft:custom", DSL.compoundList(DSL.constType(namespacedString()), DSL.constType(DSL.intType()))))));
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
/* 334 */     debug1.registerType(false, References.SAVED_DATA, () -> DSL.optionalFields("data", DSL.optionalFields("Features", DSL.compoundList(References.STRUCTURE_FEATURE.in(debug0)), "Objectives", DSL.list(References.OBJECTIVE.in(debug0)), "Teams", DSL.list(References.TEAM.in(debug0)))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 341 */     debug1.registerType(false, References.STRUCTURE_FEATURE, () -> DSL.optionalFields("Children", DSL.list(DSL.optionalFields("CA", References.BLOCK_STATE.in(debug0), "CB", References.BLOCK_STATE.in(debug0), "CC", References.BLOCK_STATE.in(debug0), "CD", References.BLOCK_STATE.in(debug0)))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 349 */     debug1.registerType(false, References.OBJECTIVE, DSL::remainder);
/* 350 */     debug1.registerType(false, References.TEAM, DSL::remainder);
/* 351 */     debug1.registerType(true, References.UNTAGGED_SPAWNER, () -> DSL.optionalFields("SpawnPotentials", DSL.list(DSL.fields("Entity", References.ENTITY_TREE.in(debug0))), "SpawnData", References.ENTITY_TREE.in(debug0)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 357 */     debug1.registerType(false, References.ADVANCEMENTS, () -> DSL.optionalFields("minecraft:adventure/adventuring_time", DSL.optionalFields("criteria", DSL.compoundList(References.BIOME.in(debug0), DSL.constType(DSL.string()))), "minecraft:adventure/kill_a_mob", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(debug0), DSL.constType(DSL.string()))), "minecraft:adventure/kill_all_mobs", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(debug0), DSL.constType(DSL.string()))), "minecraft:husbandry/bred_all_animals", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(debug0), DSL.constType(DSL.string())))));
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
/* 371 */     debug1.registerType(false, References.BIOME, () -> DSL.constType(namespacedString()));
/* 372 */     debug1.registerType(false, References.ENTITY_NAME, () -> DSL.constType(namespacedString()));
/* 373 */     debug1.registerType(false, References.POI_CHUNK, DSL::remainder);
/* 374 */     debug1.registerType(true, References.WORLD_GEN_SETTINGS, DSL::remainder);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1460.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */