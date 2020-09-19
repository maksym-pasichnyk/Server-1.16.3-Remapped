/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.List;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class VillagerTradeFix
/*    */   extends NamedEntityFix
/*    */ {
/*    */   public VillagerTradeFix(Schema debug1, boolean debug2) {
/* 18 */     super(debug1, debug2, "Villager trade fix", References.ENTITY, "minecraft:villager");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 23 */     OpticFinder<?> debug2 = debug1.getType().findField("Offers");
/* 24 */     OpticFinder<?> debug3 = debug2.type().findField("Recipes");
/* 25 */     Type<?> debug4 = debug3.type();
/* 26 */     if (!(debug4 instanceof List.ListType)) {
/* 27 */       throw new IllegalStateException("Recipes are expected to be a list.");
/*    */     }
/* 29 */     List.ListType<?> debug5 = (List.ListType)debug4;
/* 30 */     Type<?> debug6 = debug5.getElement();
/* 31 */     OpticFinder<?> debug7 = DSL.typeFinder(debug6);
/* 32 */     OpticFinder<?> debug8 = debug6.findField("buy");
/* 33 */     OpticFinder<?> debug9 = debug6.findField("buyB");
/* 34 */     OpticFinder<?> debug10 = debug6.findField("sell");
/*    */     
/* 36 */     OpticFinder<Pair<String, String>> debug11 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 37 */     Function<Typed<?>, Typed<?>> debug12 = debug2 -> updateItemStack(debug1, debug2);
/*    */     
/* 39 */     return debug1.updateTyped(debug2, debug6 -> debug6.updateTyped(debug0, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Typed<?> updateItemStack(OpticFinder<Pair<String, String>> debug1, Typed<?> debug2) {
/* 48 */     return debug2.update(debug1, debug0 -> debug0.mapSecond(()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\VillagerTradeFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */