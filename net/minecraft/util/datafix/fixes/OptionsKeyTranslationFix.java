/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ public class OptionsKeyTranslationFix extends DataFix {
/*    */   public OptionsKeyTranslationFix(Schema debug1, boolean debug2) {
/* 14 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 19 */     return fixTypeEverywhereTyped("OptionsKeyTranslationFix", getInputSchema().getType(References.OPTIONS), debug0 -> debug0.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\OptionsKeyTranslationFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */