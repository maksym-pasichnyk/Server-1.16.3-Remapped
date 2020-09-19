/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FurnaceRecipeFix
/*    */   extends DataFix
/*    */ {
/*    */   public FurnaceRecipeFix(Schema debug1, boolean debug2) {
/* 29 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 34 */     return cap(getOutputSchema().getTypeRaw(References.RECIPE));
/*    */   }
/*    */   
/*    */   private <R> TypeRewriteRule cap(Type<R> debug1) {
/* 38 */     Type<Pair<Either<Pair<List<Pair<R, Integer>>, Dynamic<?>>, Unit>, Dynamic<?>>> debug2 = DSL.and(
/* 39 */         DSL.optional((Type)DSL.field("RecipesUsed", DSL.and((Type)DSL.compoundList(debug1, DSL.intType()), DSL.remainderType()))), 
/* 40 */         DSL.remainderType());
/*    */ 
/*    */     
/* 43 */     OpticFinder<?> debug3 = DSL.namedChoice("minecraft:furnace", getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:furnace"));
/* 44 */     OpticFinder<?> debug4 = DSL.namedChoice("minecraft:blast_furnace", getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:blast_furnace"));
/* 45 */     OpticFinder<?> debug5 = DSL.namedChoice("minecraft:smoker", getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:smoker"));
/*    */     
/* 47 */     Type<?> debug6 = getOutputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:furnace");
/* 48 */     Type<?> debug7 = getOutputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:blast_furnace");
/* 49 */     Type<?> debug8 = getOutputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:smoker");
/*    */     
/* 51 */     Type<?> debug9 = getInputSchema().getType(References.BLOCK_ENTITY);
/* 52 */     Type<?> debug10 = getOutputSchema().getType(References.BLOCK_ENTITY);
/* 53 */     return fixTypeEverywhereTyped("FurnaceRecipesFix", debug9, debug10, debug9 -> debug9.updateTyped(debug1, debug2, ()).updateTyped(debug5, debug6, ()).updateTyped(debug7, debug8, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private <R> Typed<?> updateFurnaceContents(Type<R> debug1, Type<Pair<Either<Pair<List<Pair<R, Integer>>, Dynamic<?>>, Unit>, Dynamic<?>>> debug2, Typed<?> debug3) {
/* 62 */     Dynamic<?> debug4 = (Dynamic)debug3.getOrCreate(DSL.remainderFinder());
/*    */     
/* 64 */     int debug5 = debug4.get("RecipesUsedSize").asInt(0);
/* 65 */     debug4 = debug4.remove("RecipesUsedSize");
/*    */     
/* 67 */     List<Pair<R, Integer>> debug6 = Lists.newArrayList();
/* 68 */     for (int debug7 = 0; debug7 < debug5; debug7++) {
/* 69 */       String debug8 = "RecipeLocation" + debug7;
/* 70 */       String debug9 = "RecipeAmount" + debug7;
/*    */       
/* 72 */       Optional<? extends Dynamic<?>> debug10 = debug4.get(debug8).result();
/* 73 */       int debug11 = debug4.get(debug9).asInt(0);
/* 74 */       if (debug11 > 0) {
/* 75 */         debug10.ifPresent(debug3 -> {
/*    */               Optional<? extends Pair<R, ? extends Dynamic<?>>> debug4 = debug0.read(debug3).result();
/*    */               
/*    */               debug4.ifPresent(());
/*    */             });
/*    */       }
/* 81 */       debug4 = debug4.remove(debug8).remove(debug9);
/*    */     } 
/*    */     
/* 84 */     return debug3.set(DSL.remainderFinder(), debug2, Pair.of(Either.left(Pair.of(debug6, debug4.emptyMap())), debug4));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\FurnaceRecipeFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */