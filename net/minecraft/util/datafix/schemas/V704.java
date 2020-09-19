/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
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
/*     */ public class V704
/*     */   extends Schema
/*     */ {
/*     */   protected static final Map<String, String> ITEM_TO_BLOCKENTITY;
/*     */   
/*     */   public V704(int debug1, Schema debug2) {
/*  35 */     super(debug1, debug2);
/*     */   }
/*     */   
/*     */   protected static void registerInventory(Schema debug0, Map<String, Supplier<TypeTemplate>> debug1, String debug2) {
/*  39 */     debug0.register(debug1, debug2, () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(debug0))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type<?> getChoiceType(DSL.TypeReference debug1, String debug2) {
/*  46 */     if (Objects.equals(debug1.typeName(), References.BLOCK_ENTITY.typeName())) {
/*  47 */       return super.getChoiceType(debug1, NamespacedSchema.ensureNamespaced(debug2));
/*     */     }
/*  49 */     return super.getChoiceType(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema debug1) {
/*  54 */     Map<String, Supplier<TypeTemplate>> debug2 = Maps.newHashMap();
/*     */     
/*  56 */     registerInventory(debug1, debug2, "minecraft:furnace");
/*  57 */     registerInventory(debug1, debug2, "minecraft:chest");
/*  58 */     debug1.registerSimple(debug2, "minecraft:ender_chest");
/*  59 */     debug1.register(debug2, "minecraft:jukebox", debug1 -> DSL.optionalFields("RecordItem", References.ITEM_STACK.in(debug0)));
/*     */ 
/*     */     
/*  62 */     registerInventory(debug1, debug2, "minecraft:dispenser");
/*  63 */     registerInventory(debug1, debug2, "minecraft:dropper");
/*  64 */     debug1.registerSimple(debug2, "minecraft:sign");
/*  65 */     debug1.register(debug2, "minecraft:mob_spawner", debug1 -> References.UNTAGGED_SPAWNER.in(debug0));
/*  66 */     debug1.registerSimple(debug2, "minecraft:noteblock");
/*  67 */     debug1.registerSimple(debug2, "minecraft:piston");
/*  68 */     registerInventory(debug1, debug2, "minecraft:brewing_stand");
/*  69 */     debug1.registerSimple(debug2, "minecraft:enchanting_table");
/*  70 */     debug1.registerSimple(debug2, "minecraft:end_portal");
/*  71 */     debug1.registerSimple(debug2, "minecraft:beacon");
/*  72 */     debug1.registerSimple(debug2, "minecraft:skull");
/*  73 */     debug1.registerSimple(debug2, "minecraft:daylight_detector");
/*  74 */     registerInventory(debug1, debug2, "minecraft:hopper");
/*  75 */     debug1.registerSimple(debug2, "minecraft:comparator");
/*  76 */     debug1.register(debug2, "minecraft:flower_pot", debug1 -> DSL.optionalFields("Item", DSL.or(DSL.constType(DSL.intType()), References.ITEM_NAME.in(debug0))));
/*     */ 
/*     */     
/*  79 */     debug1.registerSimple(debug2, "minecraft:banner");
/*  80 */     debug1.registerSimple(debug2, "minecraft:structure_block");
/*  81 */     debug1.registerSimple(debug2, "minecraft:end_gateway");
/*  82 */     debug1.registerSimple(debug2, "minecraft:command_block");
/*     */     
/*  84 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerTypes(Schema debug1, Map<String, Supplier<TypeTemplate>> debug2, Map<String, Supplier<TypeTemplate>> debug3) {
/*  89 */     super.registerTypes(debug1, debug2, debug3);
/*     */     
/*  91 */     debug1.registerType(false, References.BLOCK_ENTITY, () -> DSL.taggedChoiceLazy("id", NamespacedSchema.namespacedString(), debug0));
/*     */     
/*  93 */     debug1.registerType(true, References.ITEM_STACK, () -> DSL.hook(DSL.optionalFields("id", References.ITEM_NAME.in(debug0), "tag", DSL.optionalFields("EntityTag", References.ENTITY_TREE.in(debug0), "BlockEntityTag", References.BLOCK_ENTITY.in(debug0), "CanDestroy", DSL.list(References.BLOCK_NAME.in(debug0)), "CanPlaceOn", DSL.list(References.BLOCK_NAME.in(debug0)))), ADD_NAMES, Hook.HookFunction.IDENTITY));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 104 */     ITEM_TO_BLOCKENTITY = (Map<String, String>)DataFixUtils.make(Maps.newHashMap(), debug0 -> {
/*     */           debug0.put("minecraft:furnace", "minecraft:furnace");
/*     */           debug0.put("minecraft:lit_furnace", "minecraft:furnace");
/*     */           debug0.put("minecraft:chest", "minecraft:chest");
/*     */           debug0.put("minecraft:trapped_chest", "minecraft:chest");
/*     */           debug0.put("minecraft:ender_chest", "minecraft:ender_chest");
/*     */           debug0.put("minecraft:jukebox", "minecraft:jukebox");
/*     */           debug0.put("minecraft:dispenser", "minecraft:dispenser");
/*     */           debug0.put("minecraft:dropper", "minecraft:dropper");
/*     */           debug0.put("minecraft:sign", "minecraft:sign");
/*     */           debug0.put("minecraft:mob_spawner", "minecraft:mob_spawner");
/*     */           debug0.put("minecraft:noteblock", "minecraft:noteblock");
/*     */           debug0.put("minecraft:brewing_stand", "minecraft:brewing_stand");
/*     */           debug0.put("minecraft:enhanting_table", "minecraft:enchanting_table");
/*     */           debug0.put("minecraft:command_block", "minecraft:command_block");
/*     */           debug0.put("minecraft:beacon", "minecraft:beacon");
/*     */           debug0.put("minecraft:skull", "minecraft:skull");
/*     */           debug0.put("minecraft:daylight_detector", "minecraft:daylight_detector");
/*     */           debug0.put("minecraft:hopper", "minecraft:hopper");
/*     */           debug0.put("minecraft:banner", "minecraft:banner");
/*     */           debug0.put("minecraft:flower_pot", "minecraft:flower_pot");
/*     */           debug0.put("minecraft:repeating_command_block", "minecraft:command_block");
/*     */           debug0.put("minecraft:chain_command_block", "minecraft:command_block");
/*     */           debug0.put("minecraft:shulker_box", "minecraft:shulker_box");
/*     */           debug0.put("minecraft:white_shulker_box", "minecraft:shulker_box");
/*     */           debug0.put("minecraft:orange_shulker_box", "minecraft:shulker_box");
/*     */           debug0.put("minecraft:magenta_shulker_box", "minecraft:shulker_box");
/*     */           debug0.put("minecraft:light_blue_shulker_box", "minecraft:shulker_box");
/*     */           debug0.put("minecraft:yellow_shulker_box", "minecraft:shulker_box");
/*     */           debug0.put("minecraft:lime_shulker_box", "minecraft:shulker_box");
/*     */           debug0.put("minecraft:pink_shulker_box", "minecraft:shulker_box");
/*     */           debug0.put("minecraft:gray_shulker_box", "minecraft:shulker_box");
/*     */           debug0.put("minecraft:silver_shulker_box", "minecraft:shulker_box");
/*     */           debug0.put("minecraft:cyan_shulker_box", "minecraft:shulker_box");
/*     */           debug0.put("minecraft:purple_shulker_box", "minecraft:shulker_box");
/*     */           debug0.put("minecraft:blue_shulker_box", "minecraft:shulker_box");
/*     */           debug0.put("minecraft:brown_shulker_box", "minecraft:shulker_box");
/*     */           debug0.put("minecraft:green_shulker_box", "minecraft:shulker_box");
/*     */           debug0.put("minecraft:red_shulker_box", "minecraft:shulker_box");
/*     */           debug0.put("minecraft:black_shulker_box", "minecraft:shulker_box");
/*     */           debug0.put("minecraft:bed", "minecraft:bed");
/*     */           debug0.put("minecraft:light_gray_shulker_box", "minecraft:shulker_box");
/*     */           debug0.put("minecraft:banner", "minecraft:banner");
/*     */           debug0.put("minecraft:white_banner", "minecraft:banner");
/*     */           debug0.put("minecraft:orange_banner", "minecraft:banner");
/*     */           debug0.put("minecraft:magenta_banner", "minecraft:banner");
/*     */           debug0.put("minecraft:light_blue_banner", "minecraft:banner");
/*     */           debug0.put("minecraft:yellow_banner", "minecraft:banner");
/*     */           debug0.put("minecraft:lime_banner", "minecraft:banner");
/*     */           debug0.put("minecraft:pink_banner", "minecraft:banner");
/*     */           debug0.put("minecraft:gray_banner", "minecraft:banner");
/*     */           debug0.put("minecraft:silver_banner", "minecraft:banner");
/*     */           debug0.put("minecraft:cyan_banner", "minecraft:banner");
/*     */           debug0.put("minecraft:purple_banner", "minecraft:banner");
/*     */           debug0.put("minecraft:blue_banner", "minecraft:banner");
/*     */           debug0.put("minecraft:brown_banner", "minecraft:banner");
/*     */           debug0.put("minecraft:green_banner", "minecraft:banner");
/*     */           debug0.put("minecraft:red_banner", "minecraft:banner");
/*     */           debug0.put("minecraft:black_banner", "minecraft:banner");
/*     */           debug0.put("minecraft:standing_sign", "minecraft:sign");
/*     */           debug0.put("minecraft:wall_sign", "minecraft:sign");
/*     */           debug0.put("minecraft:piston_head", "minecraft:piston");
/*     */           debug0.put("minecraft:daylight_detector_inverted", "minecraft:daylight_detector");
/*     */           debug0.put("minecraft:unpowered_comparator", "minecraft:comparator");
/*     */           debug0.put("minecraft:powered_comparator", "minecraft:comparator");
/*     */           debug0.put("minecraft:wall_banner", "minecraft:banner");
/*     */           debug0.put("minecraft:standing_banner", "minecraft:banner");
/*     */           debug0.put("minecraft:structure_block", "minecraft:structure_block");
/*     */           debug0.put("minecraft:end_portal", "minecraft:end_portal");
/*     */           debug0.put("minecraft:end_gateway", "minecraft:end_gateway");
/*     */           debug0.put("minecraft:sign", "minecraft:sign");
/*     */           debug0.put("minecraft:shield", "minecraft:banner");
/*     */         });
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
/*     */   
/* 188 */   protected static final Hook.HookFunction ADD_NAMES = new Hook.HookFunction()
/*     */     {
/*     */       public <T> T apply(DynamicOps<T> debug1, T debug2) {
/* 191 */         return V99.addNames(new Dynamic(debug1, debug2), V704.ITEM_TO_BLOCKENTITY, "ArmorStand");
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V704.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */