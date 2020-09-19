/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class ItemStackEnchantmentNamesFix extends DataFix {
/*    */   static {
/* 17 */     MAP = (Int2ObjectMap<String>)DataFixUtils.make(new Int2ObjectOpenHashMap(), debug0 -> {
/*    */           debug0.put(0, "minecraft:protection");
/*    */           debug0.put(1, "minecraft:fire_protection");
/*    */           debug0.put(2, "minecraft:feather_falling");
/*    */           debug0.put(3, "minecraft:blast_protection");
/*    */           debug0.put(4, "minecraft:projectile_protection");
/*    */           debug0.put(5, "minecraft:respiration");
/*    */           debug0.put(6, "minecraft:aqua_affinity");
/*    */           debug0.put(7, "minecraft:thorns");
/*    */           debug0.put(8, "minecraft:depth_strider");
/*    */           debug0.put(9, "minecraft:frost_walker");
/*    */           debug0.put(10, "minecraft:binding_curse");
/*    */           debug0.put(16, "minecraft:sharpness");
/*    */           debug0.put(17, "minecraft:smite");
/*    */           debug0.put(18, "minecraft:bane_of_arthropods");
/*    */           debug0.put(19, "minecraft:knockback");
/*    */           debug0.put(20, "minecraft:fire_aspect");
/*    */           debug0.put(21, "minecraft:looting");
/*    */           debug0.put(22, "minecraft:sweeping");
/*    */           debug0.put(32, "minecraft:efficiency");
/*    */           debug0.put(33, "minecraft:silk_touch");
/*    */           debug0.put(34, "minecraft:unbreaking");
/*    */           debug0.put(35, "minecraft:fortune");
/*    */           debug0.put(48, "minecraft:power");
/*    */           debug0.put(49, "minecraft:punch");
/*    */           debug0.put(50, "minecraft:flame");
/*    */           debug0.put(51, "minecraft:infinity");
/*    */           debug0.put(61, "minecraft:luck_of_the_sea");
/*    */           debug0.put(62, "minecraft:lure");
/*    */           debug0.put(65, "minecraft:loyalty");
/*    */           debug0.put(66, "minecraft:impaling");
/*    */           debug0.put(67, "minecraft:riptide");
/*    */           debug0.put(68, "minecraft:channeling");
/*    */           debug0.put(70, "minecraft:mending");
/*    */           debug0.put(71, "minecraft:vanishing_curse");
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static final Int2ObjectMap<String> MAP;
/*    */ 
/*    */   
/*    */   public ItemStackEnchantmentNamesFix(Schema debug1, boolean debug2) {
/* 61 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 66 */     Type<?> debug1 = getInputSchema().getType(References.ITEM_STACK);
/* 67 */     OpticFinder<?> debug2 = debug1.findField("tag");
/* 68 */     return fixTypeEverywhereTyped("ItemStackEnchantmentFix", debug1, debug2 -> debug2.updateTyped(debug1, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private Dynamic<?> fixTag(Dynamic<?> debug1) {
/* 74 */     Optional<? extends Dynamic<?>> debug2 = debug1.get("ench").asStreamOpt().map(debug0 -> debug0.map(())).map(debug1::createList).result();
/*    */     
/* 76 */     if (debug2.isPresent()) {
/* 77 */       debug1 = debug1.remove("ench").set("Enchantments", debug2.get());
/*    */     }
/*    */     
/* 80 */     return debug1.update("StoredEnchantments", debug0 -> (Dynamic)DataFixUtils.orElse(debug0.asStreamOpt().map(()).map(debug0::createList).result(), debug0));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemStackEnchantmentNamesFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */