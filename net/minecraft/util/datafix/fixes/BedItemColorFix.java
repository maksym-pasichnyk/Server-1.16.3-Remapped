/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class BedItemColorFix
/*    */   extends DataFix {
/*    */   public BedItemColorFix(Schema debug1, boolean debug2) {
/* 18 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 23 */     OpticFinder<Pair<String, String>> debug1 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/*    */     
/* 25 */     return fixTypeEverywhereTyped("BedItemColorFix", getInputSchema().getType(References.ITEM_STACK), debug1 -> {
/*    */           Optional<Pair<String, String>> debug2 = debug1.getOptional(debug0);
/*    */           if (debug2.isPresent() && Objects.equals(((Pair)debug2.get()).getSecond(), "minecraft:bed")) {
/*    */             Dynamic<?> debug3 = (Dynamic)debug1.get(DSL.remainderFinder());
/*    */             if (debug3.get("Damage").asInt(0) == 0)
/*    */               return debug1.set(DSL.remainderFinder(), debug3.set("Damage", debug3.createShort((short)14))); 
/*    */           } 
/*    */           return debug1;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\BedItemColorFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */