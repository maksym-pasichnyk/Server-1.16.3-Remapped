/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class EntityHorseSaddleFix
/*    */   extends NamedEntityFix {
/*    */   public EntityHorseSaddleFix(Schema debug1, boolean debug2) {
/* 17 */     super(debug1, debug2, "EntityHorseSaddleFix", References.ENTITY, "EntityHorse");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 24 */     OpticFinder<Pair<String, String>> debug2 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 25 */     Type<?> debug3 = getInputSchema().getTypeRaw(References.ITEM_STACK);
/* 26 */     OpticFinder<?> debug4 = DSL.fieldFinder("SaddleItem", debug3);
/*    */     
/* 28 */     Optional<? extends Typed<?>> debug5 = debug1.getOptionalTyped(debug4);
/* 29 */     Dynamic<?> debug6 = (Dynamic)debug1.get(DSL.remainderFinder());
/* 30 */     if (!debug5.isPresent() && debug6.get("Saddle").asBoolean(false)) {
/* 31 */       Typed<?> debug7 = (Typed)debug3.pointTyped(debug1.getOps()).orElseThrow(IllegalStateException::new);
/* 32 */       debug7 = debug7.set(debug2, Pair.of(References.ITEM_NAME.typeName(), "minecraft:saddle"));
/*    */       
/* 34 */       Dynamic<?> debug8 = debug6.emptyMap();
/* 35 */       debug8 = debug8.set("Count", debug8.createByte((byte)1));
/* 36 */       debug8 = debug8.set("Damage", debug8.createShort((short)0));
/*    */       
/* 38 */       debug7 = debug7.set(DSL.remainderFinder(), debug8);
/* 39 */       debug6.remove("Saddle");
/*    */       
/* 41 */       return debug1.set(debug4, debug7).set(DSL.remainderFinder(), debug6);
/*    */     } 
/* 43 */     return debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityHorseSaddleFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */