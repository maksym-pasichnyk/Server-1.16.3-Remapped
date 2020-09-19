/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Locale;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class OptionsLowerCaseLanguageFix extends DataFix {
/*    */   public OptionsLowerCaseLanguageFix(Schema debug1, boolean debug2) {
/* 13 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 18 */     return fixTypeEverywhereTyped("OptionsLowerCaseLanguageFix", getInputSchema().getType(References.OPTIONS), debug0 -> debug0.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\OptionsLowerCaseLanguageFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */