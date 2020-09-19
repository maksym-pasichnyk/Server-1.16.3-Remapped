/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class OptionsAddTextBackgroundFix extends DataFix {
/*    */   public OptionsAddTextBackgroundFix(Schema debug1, boolean debug2) {
/* 11 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 16 */     return fixTypeEverywhereTyped("OptionsAddTextBackgroundFix", getInputSchema().getType(References.OPTIONS), debug1 -> debug1.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private double calculateBackground(String debug1) {
/*    */     try {
/* 23 */       double debug2 = 0.9D * Double.parseDouble(debug1) + 0.1D;
/* 24 */       return debug2 / 2.0D;
/* 25 */     } catch (NumberFormatException debug2) {
/* 26 */       return 0.5D;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\OptionsAddTextBackgroundFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */