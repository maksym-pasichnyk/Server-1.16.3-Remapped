/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.CompoundList;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class NewVillageFix extends DataFix {
/*    */   public NewVillageFix(Schema debug1, boolean debug2) {
/* 22 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     CompoundList.CompoundListType<String, ?> debug1 = DSL.compoundList(DSL.string(), getInputSchema().getType(References.STRUCTURE_FEATURE));
/* 28 */     OpticFinder<? extends List<? extends Pair<String, ?>>> debug2 = debug1.finder();
/*    */     
/* 30 */     return cap(debug1);
/*    */   }
/*    */   
/*    */   private <SF> TypeRewriteRule cap(CompoundList.CompoundListType<String, SF> debug1) {
/* 34 */     Type<?> debug2 = getInputSchema().getType(References.CHUNK);
/* 35 */     Type<?> debug3 = getInputSchema().getType(References.STRUCTURE_FEATURE);
/* 36 */     OpticFinder<?> debug4 = debug2.findField("Level");
/* 37 */     OpticFinder<?> debug5 = debug4.type().findField("Structures");
/* 38 */     OpticFinder<?> debug6 = debug5.type().findField("Starts");
/* 39 */     OpticFinder<List<Pair<String, SF>>> debug7 = debug1.finder();
/* 40 */     return TypeRewriteRule.seq(
/* 41 */         fixTypeEverywhereTyped("NewVillageFix", debug2, debug4 -> debug4.updateTyped(debug0, ())), 
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
/* 57 */         fixTypeEverywhereTyped("NewVillageStartFix", debug3, debug0 -> debug0.update(DSL.remainderFinder(), ())));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\NewVillageFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */