/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ 
/*    */ public class BlockStateStructureTemplateFix extends DataFix {
/*    */   public BlockStateStructureTemplateFix(Schema debug1, boolean debug2) {
/* 10 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 15 */     return fixTypeEverywhereTyped("BlockStateStructureTemplateFix", getInputSchema().getType(References.BLOCK_STATE), debug0 -> debug0.update(DSL.remainderFinder(), BlockStateData::upgradeBlockStateTag));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockStateStructureTemplateFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */