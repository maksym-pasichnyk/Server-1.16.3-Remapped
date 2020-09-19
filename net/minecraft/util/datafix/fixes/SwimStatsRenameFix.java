/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class SwimStatsRenameFix extends DataFix {
/*    */   public SwimStatsRenameFix(Schema debug1, boolean debug2) {
/* 13 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 18 */     Type<?> debug1 = getOutputSchema().getType(References.STATS);
/* 19 */     Type<?> debug2 = getInputSchema().getType(References.STATS);
/* 20 */     OpticFinder<?> debug3 = debug2.findField("stats");
/* 21 */     OpticFinder<?> debug4 = debug3.type().findField("minecraft:custom");
/* 22 */     OpticFinder<String> debug5 = NamespacedSchema.namespacedString().finder();
/* 23 */     return fixTypeEverywhereTyped("SwimStatsRenameFix", debug2, debug1, debug3 -> debug3.updateTyped(debug0, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\SwimStatsRenameFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */