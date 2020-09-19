/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class BlockEntityIdFix extends DataFix {
/*    */   public BlockEntityIdFix(Schema debug1, boolean debug2) {
/* 16 */     super(debug1, debug2);
/*    */   } private static final Map<String, String> ID_MAP;
/*    */   static {
/* 19 */     ID_MAP = (Map<String, String>)DataFixUtils.make(Maps.newHashMap(), debug0 -> {
/*    */           debug0.put("Airportal", "minecraft:end_portal");
/*    */           debug0.put("Banner", "minecraft:banner");
/*    */           debug0.put("Beacon", "minecraft:beacon");
/*    */           debug0.put("Cauldron", "minecraft:brewing_stand");
/*    */           debug0.put("Chest", "minecraft:chest");
/*    */           debug0.put("Comparator", "minecraft:comparator");
/*    */           debug0.put("Control", "minecraft:command_block");
/*    */           debug0.put("DLDetector", "minecraft:daylight_detector");
/*    */           debug0.put("Dropper", "minecraft:dropper");
/*    */           debug0.put("EnchantTable", "minecraft:enchanting_table");
/*    */           debug0.put("EndGateway", "minecraft:end_gateway");
/*    */           debug0.put("EnderChest", "minecraft:ender_chest");
/*    */           debug0.put("FlowerPot", "minecraft:flower_pot");
/*    */           debug0.put("Furnace", "minecraft:furnace");
/*    */           debug0.put("Hopper", "minecraft:hopper");
/*    */           debug0.put("MobSpawner", "minecraft:mob_spawner");
/*    */           debug0.put("Music", "minecraft:noteblock");
/*    */           debug0.put("Piston", "minecraft:piston");
/*    */           debug0.put("RecordPlayer", "minecraft:jukebox");
/*    */           debug0.put("Sign", "minecraft:sign");
/*    */           debug0.put("Skull", "minecraft:skull");
/*    */           debug0.put("Structure", "minecraft:structure_block");
/*    */           debug0.put("Trap", "minecraft:dispenser");
/*    */         });
/*    */   }
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 47 */     Type<?> debug1 = getInputSchema().getType(References.ITEM_STACK);
/* 48 */     Type<?> debug2 = getOutputSchema().getType(References.ITEM_STACK);
/*    */     
/* 50 */     TaggedChoice.TaggedChoiceType<String> debug3 = getInputSchema().findChoiceType(References.BLOCK_ENTITY);
/* 51 */     TaggedChoice.TaggedChoiceType<String> debug4 = getOutputSchema().findChoiceType(References.BLOCK_ENTITY);
/*    */     
/* 53 */     return TypeRewriteRule.seq(
/* 54 */         convertUnchecked("item stack block entity name hook converter", debug1, debug2), 
/* 55 */         fixTypeEverywhere("BlockEntityIdFix", (Type)debug3, (Type)debug4, debug0 -> ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockEntityIdFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */