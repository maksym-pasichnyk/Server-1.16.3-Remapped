/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
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
/*     */ public class V705
/*     */   extends NamespacedSchema
/*     */ {
/*     */   public V705(int debug1, Schema debug2) {
/*  27 */     super(debug1, debug2);
/*     */   }
/*     */   
/*     */   protected static void registerMob(Schema debug0, Map<String, Supplier<TypeTemplate>> debug1, String debug2) {
/*  31 */     debug0.register(debug1, debug2, () -> V100.equipment(debug0));
/*     */   }
/*     */   
/*     */   protected static void registerThrowableProjectile(Schema debug0, Map<String, Supplier<TypeTemplate>> debug1, String debug2) {
/*  35 */     debug0.register(debug1, debug2, () -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(debug0)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema debug1) {
/*  42 */     Map<String, Supplier<TypeTemplate>> debug2 = Maps.newHashMap();
/*     */     
/*  44 */     debug1.registerSimple(debug2, "minecraft:area_effect_cloud");
/*  45 */     registerMob(debug1, debug2, "minecraft:armor_stand");
/*  46 */     debug1.register(debug2, "minecraft:arrow", debug1 -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(debug0)));
/*     */ 
/*     */     
/*  49 */     registerMob(debug1, debug2, "minecraft:bat");
/*  50 */     registerMob(debug1, debug2, "minecraft:blaze");
/*  51 */     debug1.registerSimple(debug2, "minecraft:boat");
/*  52 */     registerMob(debug1, debug2, "minecraft:cave_spider");
/*  53 */     debug1.register(debug2, "minecraft:chest_minecart", debug1 -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(debug0), "Items", DSL.list(References.ITEM_STACK.in(debug0))));
/*     */ 
/*     */ 
/*     */     
/*  57 */     registerMob(debug1, debug2, "minecraft:chicken");
/*  58 */     debug1.register(debug2, "minecraft:commandblock_minecart", debug1 -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(debug0)));
/*     */ 
/*     */     
/*  61 */     registerMob(debug1, debug2, "minecraft:cow");
/*  62 */     registerMob(debug1, debug2, "minecraft:creeper");
/*  63 */     debug1.register(debug2, "minecraft:donkey", debug1 -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(debug0)), "SaddleItem", References.ITEM_STACK.in(debug0), V100.equipment(debug0)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  68 */     debug1.registerSimple(debug2, "minecraft:dragon_fireball");
/*  69 */     registerThrowableProjectile(debug1, debug2, "minecraft:egg");
/*  70 */     registerMob(debug1, debug2, "minecraft:elder_guardian");
/*  71 */     debug1.registerSimple(debug2, "minecraft:ender_crystal");
/*  72 */     registerMob(debug1, debug2, "minecraft:ender_dragon");
/*  73 */     debug1.register(debug2, "minecraft:enderman", debug1 -> DSL.optionalFields("carried", References.BLOCK_NAME.in(debug0), V100.equipment(debug0)));
/*     */ 
/*     */ 
/*     */     
/*  77 */     registerMob(debug1, debug2, "minecraft:endermite");
/*  78 */     registerThrowableProjectile(debug1, debug2, "minecraft:ender_pearl");
/*  79 */     debug1.registerSimple(debug2, "minecraft:eye_of_ender_signal");
/*  80 */     debug1.register(debug2, "minecraft:falling_block", debug1 -> DSL.optionalFields("Block", References.BLOCK_NAME.in(debug0), "TileEntityData", References.BLOCK_ENTITY.in(debug0)));
/*     */ 
/*     */ 
/*     */     
/*  84 */     registerThrowableProjectile(debug1, debug2, "minecraft:fireball");
/*  85 */     debug1.register(debug2, "minecraft:fireworks_rocket", debug1 -> DSL.optionalFields("FireworksItem", References.ITEM_STACK.in(debug0)));
/*     */ 
/*     */     
/*  88 */     debug1.register(debug2, "minecraft:furnace_minecart", debug1 -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(debug0)));
/*     */ 
/*     */     
/*  91 */     registerMob(debug1, debug2, "minecraft:ghast");
/*  92 */     registerMob(debug1, debug2, "minecraft:giant");
/*  93 */     registerMob(debug1, debug2, "minecraft:guardian");
/*  94 */     debug1.register(debug2, "minecraft:hopper_minecart", debug1 -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(debug0), "Items", DSL.list(References.ITEM_STACK.in(debug0))));
/*     */ 
/*     */ 
/*     */     
/*  98 */     debug1.register(debug2, "minecraft:horse", debug1 -> DSL.optionalFields("ArmorItem", References.ITEM_STACK.in(debug0), "SaddleItem", References.ITEM_STACK.in(debug0), V100.equipment(debug0)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     registerMob(debug1, debug2, "minecraft:husk");
/* 105 */     debug1.register(debug2, "minecraft:item", debug1 -> DSL.optionalFields("Item", References.ITEM_STACK.in(debug0)));
/*     */ 
/*     */     
/* 108 */     debug1.register(debug2, "minecraft:item_frame", debug1 -> DSL.optionalFields("Item", References.ITEM_STACK.in(debug0)));
/*     */ 
/*     */     
/* 111 */     debug1.registerSimple(debug2, "minecraft:leash_knot");
/* 112 */     registerMob(debug1, debug2, "minecraft:magma_cube");
/* 113 */     debug1.register(debug2, "minecraft:minecart", debug1 -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(debug0)));
/*     */ 
/*     */     
/* 116 */     registerMob(debug1, debug2, "minecraft:mooshroom");
/* 117 */     debug1.register(debug2, "minecraft:mule", debug1 -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(debug0)), "SaddleItem", References.ITEM_STACK.in(debug0), V100.equipment(debug0)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     registerMob(debug1, debug2, "minecraft:ocelot");
/* 123 */     debug1.registerSimple(debug2, "minecraft:painting");
/* 124 */     debug1.registerSimple(debug2, "minecraft:parrot");
/* 125 */     registerMob(debug1, debug2, "minecraft:pig");
/* 126 */     registerMob(debug1, debug2, "minecraft:polar_bear");
/* 127 */     debug1.register(debug2, "minecraft:potion", debug1 -> DSL.optionalFields("Potion", References.ITEM_STACK.in(debug0), "inTile", References.BLOCK_NAME.in(debug0)));
/*     */ 
/*     */ 
/*     */     
/* 131 */     registerMob(debug1, debug2, "minecraft:rabbit");
/* 132 */     registerMob(debug1, debug2, "minecraft:sheep");
/* 133 */     registerMob(debug1, debug2, "minecraft:shulker");
/* 134 */     debug1.registerSimple(debug2, "minecraft:shulker_bullet");
/* 135 */     registerMob(debug1, debug2, "minecraft:silverfish");
/* 136 */     registerMob(debug1, debug2, "minecraft:skeleton");
/* 137 */     debug1.register(debug2, "minecraft:skeleton_horse", debug1 -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(debug0), V100.equipment(debug0)));
/*     */ 
/*     */ 
/*     */     
/* 141 */     registerMob(debug1, debug2, "minecraft:slime");
/* 142 */     registerThrowableProjectile(debug1, debug2, "minecraft:small_fireball");
/* 143 */     registerThrowableProjectile(debug1, debug2, "minecraft:snowball");
/* 144 */     registerMob(debug1, debug2, "minecraft:snowman");
/* 145 */     debug1.register(debug2, "minecraft:spawner_minecart", debug1 -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(debug0), References.UNTAGGED_SPAWNER.in(debug0)));
/*     */ 
/*     */ 
/*     */     
/* 149 */     debug1.register(debug2, "minecraft:spectral_arrow", debug1 -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(debug0)));
/*     */ 
/*     */     
/* 152 */     registerMob(debug1, debug2, "minecraft:spider");
/* 153 */     registerMob(debug1, debug2, "minecraft:squid");
/* 154 */     registerMob(debug1, debug2, "minecraft:stray");
/* 155 */     debug1.registerSimple(debug2, "minecraft:tnt");
/* 156 */     debug1.register(debug2, "minecraft:tnt_minecart", debug1 -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(debug0)));
/*     */ 
/*     */     
/* 159 */     debug1.register(debug2, "minecraft:villager", debug1 -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(debug0)), "Offers", DSL.optionalFields("Recipes", DSL.list(DSL.optionalFields("buy", References.ITEM_STACK.in(debug0), "buyB", References.ITEM_STACK.in(debug0), "sell", References.ITEM_STACK.in(debug0)))), V100.equipment(debug0)));
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
/* 172 */     registerMob(debug1, debug2, "minecraft:villager_golem");
/* 173 */     registerMob(debug1, debug2, "minecraft:witch");
/* 174 */     registerMob(debug1, debug2, "minecraft:wither");
/* 175 */     registerMob(debug1, debug2, "minecraft:wither_skeleton");
/* 176 */     registerThrowableProjectile(debug1, debug2, "minecraft:wither_skull");
/* 177 */     registerMob(debug1, debug2, "minecraft:wolf");
/* 178 */     registerThrowableProjectile(debug1, debug2, "minecraft:xp_bottle");
/* 179 */     debug1.registerSimple(debug2, "minecraft:xp_orb");
/* 180 */     registerMob(debug1, debug2, "minecraft:zombie");
/* 181 */     debug1.register(debug2, "minecraft:zombie_horse", debug1 -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(debug0), V100.equipment(debug0)));
/*     */ 
/*     */ 
/*     */     
/* 185 */     registerMob(debug1, debug2, "minecraft:zombie_pigman");
/* 186 */     registerMob(debug1, debug2, "minecraft:zombie_villager");
/*     */ 
/*     */     
/* 189 */     debug1.registerSimple(debug2, "minecraft:evocation_fangs");
/* 190 */     registerMob(debug1, debug2, "minecraft:evocation_illager");
/* 191 */     debug1.registerSimple(debug2, "minecraft:illusion_illager");
/* 192 */     debug1.register(debug2, "minecraft:llama", debug1 -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(debug0)), "SaddleItem", References.ITEM_STACK.in(debug0), "DecorItem", References.ITEM_STACK.in(debug0), V100.equipment(debug0)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 198 */     debug1.registerSimple(debug2, "minecraft:llama_spit");
/* 199 */     registerMob(debug1, debug2, "minecraft:vex");
/* 200 */     registerMob(debug1, debug2, "minecraft:vindication_illager");
/*     */     
/* 202 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerTypes(Schema debug1, Map<String, Supplier<TypeTemplate>> debug2, Map<String, Supplier<TypeTemplate>> debug3) {
/* 207 */     super.registerTypes(debug1, debug2, debug3);
/* 208 */     debug1.registerType(true, References.ENTITY, () -> DSL.taggedChoiceLazy("id", namespacedString(), debug0));
/* 209 */     debug1.registerType(true, References.ITEM_STACK, () -> DSL.hook(DSL.optionalFields("id", References.ITEM_NAME.in(debug0), "tag", DSL.optionalFields("EntityTag", References.ENTITY_TREE.in(debug0), "BlockEntityTag", References.BLOCK_ENTITY.in(debug0), "CanDestroy", DSL.list(References.BLOCK_NAME.in(debug0)), "CanPlaceOn", DSL.list(References.BLOCK_NAME.in(debug0)))), ADD_NAMES, Hook.HookFunction.IDENTITY));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 220 */   protected static final Hook.HookFunction ADD_NAMES = new Hook.HookFunction()
/*     */     {
/*     */       public <T> T apply(DynamicOps<T> debug1, T debug2) {
/* 223 */         return V99.addNames(new Dynamic(debug1, debug2), V704.ITEM_TO_BLOCKENTITY, "minecraft:armor_stand");
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V705.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */