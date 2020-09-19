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
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class ItemWaterPotionFix
/*    */   extends DataFix
/*    */ {
/*    */   public ItemWaterPotionFix(Schema debug1, boolean debug2) {
/* 19 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 24 */     Type<?> debug1 = getInputSchema().getType(References.ITEM_STACK);
/*    */     
/* 26 */     OpticFinder<Pair<String, String>> debug2 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 27 */     OpticFinder<?> debug3 = debug1.findField("tag");
/*    */     
/* 29 */     return fixTypeEverywhereTyped("ItemWaterPotionFix", debug1, debug2 -> {
/*    */           Optional<Pair<String, String>> debug3 = debug2.getOptional(debug0);
/*    */           if (debug3.isPresent()) {
/*    */             String debug4 = (String)((Pair)debug3.get()).getSecond();
/*    */             if ("minecraft:potion".equals(debug4) || "minecraft:splash_potion".equals(debug4) || "minecraft:lingering_potion".equals(debug4) || "minecraft:tipped_arrow".equals(debug4)) {
/*    */               Typed<?> debug5 = debug2.getOrCreateTyped(debug1);
/*    */               Dynamic<?> debug6 = (Dynamic)debug5.get(DSL.remainderFinder());
/*    */               if (!debug6.get("Potion").asString().result().isPresent())
/*    */                 debug6 = debug6.set("Potion", debug6.createString("minecraft:water")); 
/*    */               return debug2.set(debug1, debug5.set(DSL.remainderFinder(), debug6));
/*    */             } 
/*    */           } 
/*    */           return debug2;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemWaterPotionFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */