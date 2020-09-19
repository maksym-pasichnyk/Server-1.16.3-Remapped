/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class StructureReferenceCountFix extends DataFix {
/*    */   public StructureReferenceCountFix(Schema debug1, boolean debug2) {
/* 12 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     Type<?> debug1 = getInputSchema().getType(References.STRUCTURE_FEATURE);
/* 18 */     return fixTypeEverywhereTyped("Structure Reference Fix", debug1, debug0 -> debug0.update(DSL.remainderFinder(), StructureReferenceCountFix::setCountToAtLeastOne));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static <T> Dynamic<T> setCountToAtLeastOne(Dynamic<T> debug0) {
/* 24 */     return debug0.update("references", debug0 -> debug0.createInt(((Integer)debug0.asNumber().map(Number::intValue).result().filter(()).orElse(Integer.valueOf(1))).intValue()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\StructureReferenceCountFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */