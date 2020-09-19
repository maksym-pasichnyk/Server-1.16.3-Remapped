/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class ItemShulkerBoxColorFix
/*    */   extends DataFix
/*    */ {
/*    */   public ItemShulkerBoxColorFix(Schema debug1, boolean debug2) {
/* 20 */     super(debug1, debug2);
/*    */   }
/*    */   
/* 23 */   public static final String[] NAMES_BY_COLOR = new String[] { "minecraft:white_shulker_box", "minecraft:orange_shulker_box", "minecraft:magenta_shulker_box", "minecraft:light_blue_shulker_box", "minecraft:yellow_shulker_box", "minecraft:lime_shulker_box", "minecraft:pink_shulker_box", "minecraft:gray_shulker_box", "minecraft:silver_shulker_box", "minecraft:cyan_shulker_box", "minecraft:purple_shulker_box", "minecraft:blue_shulker_box", "minecraft:brown_shulker_box", "minecraft:green_shulker_box", "minecraft:red_shulker_box", "minecraft:black_shulker_box" };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 44 */     Type<?> debug1 = getInputSchema().getType(References.ITEM_STACK);
/*    */     
/* 46 */     OpticFinder<Pair<String, String>> debug2 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 47 */     OpticFinder<?> debug3 = debug1.findField("tag");
/* 48 */     OpticFinder<?> debug4 = debug3.type().findField("BlockEntityTag");
/*    */     
/* 50 */     return fixTypeEverywhereTyped("ItemShulkerBoxColorFix", debug1, debug3 -> {
/*    */           Optional<Pair<String, String>> debug4 = debug3.getOptional(debug0);
/*    */           if (debug4.isPresent() && Objects.equals(((Pair)debug4.get()).getSecond(), "minecraft:shulker_box")) {
/*    */             Optional<? extends Typed<?>> debug5 = debug3.getOptionalTyped(debug1);
/*    */             if (debug5.isPresent()) {
/*    */               Typed<?> debug6 = debug5.get();
/*    */               Optional<? extends Typed<?>> debug7 = debug6.getOptionalTyped(debug2);
/*    */               if (debug7.isPresent()) {
/*    */                 Typed<?> debug8 = debug7.get();
/*    */                 Dynamic<?> debug9 = (Dynamic)debug8.get(DSL.remainderFinder());
/*    */                 int debug10 = debug9.get("Color").asInt(0);
/*    */                 debug9.remove("Color");
/*    */                 return debug3.set(debug1, debug6.set(debug2, debug8.set(DSL.remainderFinder(), debug9))).set(debug0, Pair.of(References.ITEM_NAME.typeName(), NAMES_BY_COLOR[debug10 % 16]));
/*    */               } 
/*    */             } 
/*    */           } 
/*    */           return debug3;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemShulkerBoxColorFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */