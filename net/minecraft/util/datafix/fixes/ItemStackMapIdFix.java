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
/*    */ public class ItemStackMapIdFix
/*    */   extends DataFix
/*    */ {
/*    */   public ItemStackMapIdFix(Schema debug1, boolean debug2) {
/* 20 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 25 */     Type<?> debug1 = getInputSchema().getType(References.ITEM_STACK);
/*    */     
/* 27 */     OpticFinder<Pair<String, String>> debug2 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 28 */     OpticFinder<?> debug3 = debug1.findField("tag");
/*    */     
/* 30 */     return fixTypeEverywhereTyped("ItemInstanceMapIdFix", debug1, debug2 -> {
/*    */           Optional<Pair<String, String>> debug3 = debug2.getOptional(debug0);
/*    */           if (debug3.isPresent() && Objects.equals(((Pair)debug3.get()).getSecond(), "minecraft:filled_map")) {
/*    */             Dynamic<?> debug4 = (Dynamic)debug2.get(DSL.remainderFinder());
/*    */             Typed<?> debug5 = debug2.getOrCreateTyped(debug1);
/*    */             Dynamic<?> debug6 = (Dynamic)debug5.get(DSL.remainderFinder());
/*    */             debug6 = debug6.set("map", debug6.createInt(debug4.get("Damage").asInt(0)));
/*    */             return debug2.set(debug1, debug5.set(DSL.remainderFinder(), debug6));
/*    */           } 
/*    */           return debug2;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemStackMapIdFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */