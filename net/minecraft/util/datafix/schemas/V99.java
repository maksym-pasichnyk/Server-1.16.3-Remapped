/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.datafix.fixes.References;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
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
/*     */ public class V99
/*     */   extends Schema
/*     */ {
/*  51 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   public V99(int debug1, Schema debug2) {
/*  54 */     super(debug1, debug2);
/*     */   }
/*     */   private static final Map<String, String> ITEM_TO_BLOCKENTITY;
/*     */   protected static TypeTemplate equipment(Schema debug0) {
/*  58 */     return DSL.optionalFields("Equipment", 
/*  59 */         DSL.list(References.ITEM_STACK.in(debug0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void registerMob(Schema debug0, Map<String, Supplier<TypeTemplate>> debug1, String debug2) {
/*  64 */     debug0.register(debug1, debug2, () -> equipment(debug0));
/*     */   }
/*     */   
/*     */   protected static void registerThrowableProjectile(Schema debug0, Map<String, Supplier<TypeTemplate>> debug1, String debug2) {
/*  68 */     debug0.register(debug1, debug2, () -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(debug0)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void registerMinecart(Schema debug0, Map<String, Supplier<TypeTemplate>> debug1, String debug2) {
/*  75 */     debug0.register(debug1, debug2, () -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(debug0)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void registerInventory(Schema debug0, Map<String, Supplier<TypeTemplate>> debug1, String debug2) {
/*  81 */     debug0.register(debug1, debug2, () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(debug0))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema debug1) {
/*  88 */     Map<String, Supplier<TypeTemplate>> debug2 = Maps.newHashMap();
/*     */     
/*  90 */     debug1.register(debug2, "Item", debug1 -> DSL.optionalFields("Item", References.ITEM_STACK.in(debug0)));
/*     */ 
/*     */     
/*  93 */     debug1.registerSimple(debug2, "XPOrb");
/*  94 */     registerThrowableProjectile(debug1, debug2, "ThrownEgg");
/*  95 */     debug1.registerSimple(debug2, "LeashKnot");
/*  96 */     debug1.registerSimple(debug2, "Painting");
/*  97 */     debug1.register(debug2, "Arrow", debug1 -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(debug0)));
/*     */ 
/*     */     
/* 100 */     debug1.register(debug2, "TippedArrow", debug1 -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(debug0)));
/*     */ 
/*     */     
/* 103 */     debug1.register(debug2, "SpectralArrow", debug1 -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(debug0)));
/*     */ 
/*     */     
/* 106 */     registerThrowableProjectile(debug1, debug2, "Snowball");
/* 107 */     registerThrowableProjectile(debug1, debug2, "Fireball");
/* 108 */     registerThrowableProjectile(debug1, debug2, "SmallFireball");
/* 109 */     registerThrowableProjectile(debug1, debug2, "ThrownEnderpearl");
/* 110 */     debug1.registerSimple(debug2, "EyeOfEnderSignal");
/* 111 */     debug1.register(debug2, "ThrownPotion", debug1 -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(debug0), "Potion", References.ITEM_STACK.in(debug0)));
/*     */ 
/*     */ 
/*     */     
/* 115 */     registerThrowableProjectile(debug1, debug2, "ThrownExpBottle");
/* 116 */     debug1.register(debug2, "ItemFrame", debug1 -> DSL.optionalFields("Item", References.ITEM_STACK.in(debug0)));
/*     */ 
/*     */     
/* 119 */     registerThrowableProjectile(debug1, debug2, "WitherSkull");
/* 120 */     debug1.registerSimple(debug2, "PrimedTnt");
/* 121 */     debug1.register(debug2, "FallingSand", debug1 -> DSL.optionalFields("Block", References.BLOCK_NAME.in(debug0), "TileEntityData", References.BLOCK_ENTITY.in(debug0)));
/*     */ 
/*     */ 
/*     */     
/* 125 */     debug1.register(debug2, "FireworksRocketEntity", debug1 -> DSL.optionalFields("FireworksItem", References.ITEM_STACK.in(debug0)));
/*     */ 
/*     */     
/* 128 */     debug1.registerSimple(debug2, "Boat");
/*     */ 
/*     */     
/* 131 */     debug1.register(debug2, "Minecart", () -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(debug0), "Items", DSL.list(References.ITEM_STACK.in(debug0))));
/*     */ 
/*     */ 
/*     */     
/* 135 */     registerMinecart(debug1, debug2, "MinecartRideable");
/* 136 */     debug1.register(debug2, "MinecartChest", debug1 -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(debug0), "Items", DSL.list(References.ITEM_STACK.in(debug0))));
/*     */ 
/*     */ 
/*     */     
/* 140 */     registerMinecart(debug1, debug2, "MinecartFurnace");
/* 141 */     registerMinecart(debug1, debug2, "MinecartTNT");
/* 142 */     debug1.register(debug2, "MinecartSpawner", () -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(debug0), References.UNTAGGED_SPAWNER.in(debug0)));
/*     */ 
/*     */ 
/*     */     
/* 146 */     debug1.register(debug2, "MinecartHopper", debug1 -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(debug0), "Items", DSL.list(References.ITEM_STACK.in(debug0))));
/*     */ 
/*     */ 
/*     */     
/* 150 */     registerMinecart(debug1, debug2, "MinecartCommandBlock");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 155 */     registerMob(debug1, debug2, "ArmorStand");
/* 156 */     registerMob(debug1, debug2, "Creeper");
/* 157 */     registerMob(debug1, debug2, "Skeleton");
/* 158 */     registerMob(debug1, debug2, "Spider");
/* 159 */     registerMob(debug1, debug2, "Giant");
/* 160 */     registerMob(debug1, debug2, "Zombie");
/* 161 */     registerMob(debug1, debug2, "Slime");
/* 162 */     registerMob(debug1, debug2, "Ghast");
/* 163 */     registerMob(debug1, debug2, "PigZombie");
/* 164 */     debug1.register(debug2, "Enderman", debug1 -> DSL.optionalFields("carried", References.BLOCK_NAME.in(debug0), equipment(debug0)));
/*     */ 
/*     */ 
/*     */     
/* 168 */     registerMob(debug1, debug2, "CaveSpider");
/* 169 */     registerMob(debug1, debug2, "Silverfish");
/* 170 */     registerMob(debug1, debug2, "Blaze");
/* 171 */     registerMob(debug1, debug2, "LavaSlime");
/* 172 */     registerMob(debug1, debug2, "EnderDragon");
/* 173 */     registerMob(debug1, debug2, "WitherBoss");
/* 174 */     registerMob(debug1, debug2, "Bat");
/* 175 */     registerMob(debug1, debug2, "Witch");
/* 176 */     registerMob(debug1, debug2, "Endermite");
/* 177 */     registerMob(debug1, debug2, "Guardian");
/* 178 */     registerMob(debug1, debug2, "Pig");
/* 179 */     registerMob(debug1, debug2, "Sheep");
/* 180 */     registerMob(debug1, debug2, "Cow");
/* 181 */     registerMob(debug1, debug2, "Chicken");
/* 182 */     registerMob(debug1, debug2, "Squid");
/* 183 */     registerMob(debug1, debug2, "Wolf");
/* 184 */     registerMob(debug1, debug2, "MushroomCow");
/* 185 */     registerMob(debug1, debug2, "SnowMan");
/* 186 */     registerMob(debug1, debug2, "Ozelot");
/* 187 */     registerMob(debug1, debug2, "VillagerGolem");
/* 188 */     debug1.register(debug2, "EntityHorse", debug1 -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(debug0)), "ArmorItem", References.ITEM_STACK.in(debug0), "SaddleItem", References.ITEM_STACK.in(debug0), equipment(debug0)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     registerMob(debug1, debug2, "Rabbit");
/* 195 */     debug1.register(debug2, "Villager", debug1 -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(debug0)), "Offers", DSL.optionalFields("Recipes", DSL.list(DSL.optionalFields("buy", References.ITEM_STACK.in(debug0), "buyB", References.ITEM_STACK.in(debug0), "sell", References.ITEM_STACK.in(debug0)))), equipment(debug0)));
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
/* 208 */     debug1.registerSimple(debug2, "EnderCrystal");
/*     */ 
/*     */ 
/*     */     
/* 212 */     debug1.registerSimple(debug2, "AreaEffectCloud");
/* 213 */     debug1.registerSimple(debug2, "ShulkerBullet");
/* 214 */     registerMob(debug1, debug2, "Shulker");
/*     */     
/* 216 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema debug1) {
/* 221 */     Map<String, Supplier<TypeTemplate>> debug2 = Maps.newHashMap();
/*     */     
/* 223 */     registerInventory(debug1, debug2, "Furnace");
/* 224 */     registerInventory(debug1, debug2, "Chest");
/* 225 */     debug1.registerSimple(debug2, "EnderChest");
/* 226 */     debug1.register(debug2, "RecordPlayer", debug1 -> DSL.optionalFields("RecordItem", References.ITEM_STACK.in(debug0)));
/*     */ 
/*     */     
/* 229 */     registerInventory(debug1, debug2, "Trap");
/* 230 */     registerInventory(debug1, debug2, "Dropper");
/* 231 */     debug1.registerSimple(debug2, "Sign");
/* 232 */     debug1.register(debug2, "MobSpawner", debug1 -> References.UNTAGGED_SPAWNER.in(debug0));
/* 233 */     debug1.registerSimple(debug2, "Music");
/* 234 */     debug1.registerSimple(debug2, "Piston");
/* 235 */     registerInventory(debug1, debug2, "Cauldron");
/* 236 */     debug1.registerSimple(debug2, "EnchantTable");
/* 237 */     debug1.registerSimple(debug2, "Airportal");
/* 238 */     debug1.registerSimple(debug2, "Control");
/* 239 */     debug1.registerSimple(debug2, "Beacon");
/* 240 */     debug1.registerSimple(debug2, "Skull");
/* 241 */     debug1.registerSimple(debug2, "DLDetector");
/* 242 */     registerInventory(debug1, debug2, "Hopper");
/* 243 */     debug1.registerSimple(debug2, "Comparator");
/* 244 */     debug1.register(debug2, "FlowerPot", debug1 -> DSL.optionalFields("Item", DSL.or(DSL.constType(DSL.intType()), References.ITEM_NAME.in(debug0))));
/*     */ 
/*     */     
/* 247 */     debug1.registerSimple(debug2, "Banner");
/*     */ 
/*     */ 
/*     */     
/* 251 */     debug1.registerSimple(debug2, "Structure");
/* 252 */     debug1.registerSimple(debug2, "EndGateway");
/* 253 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerTypes(Schema debug1, Map<String, Supplier<TypeTemplate>> debug2, Map<String, Supplier<TypeTemplate>> debug3) {
/* 258 */     debug1.registerType(false, References.LEVEL, DSL::remainder);
/* 259 */     debug1.registerType(false, References.PLAYER, () -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(debug0)), "EnderItems", DSL.list(References.ITEM_STACK.in(debug0))));
/*     */ 
/*     */ 
/*     */     
/* 263 */     debug1.registerType(false, References.CHUNK, () -> DSL.fields("Level", DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(debug0)), "TileEntities", DSL.list(References.BLOCK_ENTITY.in(debug0)), "TileTicks", DSL.list(DSL.fields("i", References.BLOCK_NAME.in(debug0))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 270 */     debug1.registerType(true, References.BLOCK_ENTITY, () -> DSL.taggedChoiceLazy("id", DSL.string(), debug0));
/* 271 */     debug1.registerType(true, References.ENTITY_TREE, () -> DSL.optionalFields("Riding", References.ENTITY_TREE.in(debug0), References.ENTITY.in(debug0)));
/*     */ 
/*     */ 
/*     */     
/* 275 */     debug1.registerType(false, References.ENTITY_NAME, () -> DSL.constType(NamespacedSchema.namespacedString()));
/* 276 */     debug1.registerType(true, References.ENTITY, () -> DSL.taggedChoiceLazy("id", DSL.string(), debug0));
/* 277 */     debug1.registerType(true, References.ITEM_STACK, () -> DSL.hook(DSL.optionalFields("id", DSL.or(DSL.constType(DSL.intType()), References.ITEM_NAME.in(debug0)), "tag", DSL.optionalFields("EntityTag", References.ENTITY_TREE.in(debug0), "BlockEntityTag", References.BLOCK_ENTITY.in(debug0), "CanDestroy", DSL.list(References.BLOCK_NAME.in(debug0)), "CanPlaceOn", DSL.list(References.BLOCK_NAME.in(debug0)))), ADD_NAMES, Hook.HookFunction.IDENTITY));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 286 */     debug1.registerType(false, References.OPTIONS, DSL::remainder);
/* 287 */     debug1.registerType(false, References.BLOCK_NAME, () -> DSL.or(DSL.constType(DSL.intType()), DSL.constType(NamespacedSchema.namespacedString())));
/* 288 */     debug1.registerType(false, References.ITEM_NAME, () -> DSL.constType(NamespacedSchema.namespacedString()));
/* 289 */     debug1.registerType(false, References.STATS, DSL::remainder);
/* 290 */     debug1.registerType(false, References.SAVED_DATA, () -> DSL.optionalFields("data", DSL.optionalFields("Features", DSL.compoundList(References.STRUCTURE_FEATURE.in(debug0)), "Objectives", DSL.list(References.OBJECTIVE.in(debug0)), "Teams", DSL.list(References.TEAM.in(debug0)))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 297 */     debug1.registerType(false, References.STRUCTURE_FEATURE, DSL::remainder);
/* 298 */     debug1.registerType(false, References.OBJECTIVE, DSL::remainder);
/* 299 */     debug1.registerType(false, References.TEAM, DSL::remainder);
/*     */     
/* 301 */     debug1.registerType(true, References.UNTAGGED_SPAWNER, DSL::remainder);
/* 302 */     debug1.registerType(false, References.POI_CHUNK, DSL::remainder);
/* 303 */     debug1.registerType(true, References.WORLD_GEN_SETTINGS, DSL::remainder);
/*     */   }
/*     */   static {
/* 306 */     ITEM_TO_BLOCKENTITY = (Map<String, String>)DataFixUtils.make(Maps.newHashMap(), debug0 -> {
/*     */           debug0.put("minecraft:furnace", "Furnace");
/*     */           debug0.put("minecraft:lit_furnace", "Furnace");
/*     */           debug0.put("minecraft:chest", "Chest");
/*     */           debug0.put("minecraft:trapped_chest", "Chest");
/*     */           debug0.put("minecraft:ender_chest", "EnderChest");
/*     */           debug0.put("minecraft:jukebox", "RecordPlayer");
/*     */           debug0.put("minecraft:dispenser", "Trap");
/*     */           debug0.put("minecraft:dropper", "Dropper");
/*     */           debug0.put("minecraft:sign", "Sign");
/*     */           debug0.put("minecraft:mob_spawner", "MobSpawner");
/*     */           debug0.put("minecraft:noteblock", "Music");
/*     */           debug0.put("minecraft:brewing_stand", "Cauldron");
/*     */           debug0.put("minecraft:enhanting_table", "EnchantTable");
/*     */           debug0.put("minecraft:command_block", "CommandBlock");
/*     */           debug0.put("minecraft:beacon", "Beacon");
/*     */           debug0.put("minecraft:skull", "Skull");
/*     */           debug0.put("minecraft:daylight_detector", "DLDetector");
/*     */           debug0.put("minecraft:hopper", "Hopper");
/*     */           debug0.put("minecraft:banner", "Banner");
/*     */           debug0.put("minecraft:flower_pot", "FlowerPot");
/*     */           debug0.put("minecraft:repeating_command_block", "CommandBlock");
/*     */           debug0.put("minecraft:chain_command_block", "CommandBlock");
/*     */           debug0.put("minecraft:standing_sign", "Sign");
/*     */           debug0.put("minecraft:wall_sign", "Sign");
/*     */           debug0.put("minecraft:piston_head", "Piston");
/*     */           debug0.put("minecraft:daylight_detector_inverted", "DLDetector");
/*     */           debug0.put("minecraft:unpowered_comparator", "Comparator");
/*     */           debug0.put("minecraft:powered_comparator", "Comparator");
/*     */           debug0.put("minecraft:wall_banner", "Banner");
/*     */           debug0.put("minecraft:standing_banner", "Banner");
/*     */           debug0.put("minecraft:structure_block", "Structure");
/*     */           debug0.put("minecraft:end_portal", "Airportal");
/*     */           debug0.put("minecraft:end_gateway", "EndGateway");
/*     */           debug0.put("minecraft:shield", "Banner");
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 348 */   protected static final Hook.HookFunction ADD_NAMES = new Hook.HookFunction()
/*     */     {
/*     */       public <T> T apply(DynamicOps<T> debug1, T debug2) {
/* 351 */         return V99.addNames(new Dynamic(debug1, debug2), V99.ITEM_TO_BLOCKENTITY, "ArmorStand");
/*     */       }
/*     */     };
/*     */   
/*     */   protected static <T> T addNames(Dynamic<T> debug0, Map<String, String> debug1, String debug2) {
/* 356 */     return (T)debug0.update("tag", debug3 -> debug3.update("BlockEntityTag", ()).update("EntityTag", ()))
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
/* 372 */       .getValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V99.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */