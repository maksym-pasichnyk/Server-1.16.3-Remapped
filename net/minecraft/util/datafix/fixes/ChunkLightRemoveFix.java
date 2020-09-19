/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class ChunkLightRemoveFix extends DataFix {
/*    */   public ChunkLightRemoveFix(Schema debug1, boolean debug2) {
/* 12 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     Type<?> debug1 = getInputSchema().getType(References.CHUNK);
/* 18 */     Type<?> debug2 = debug1.findFieldType("Level");
/*    */     
/* 20 */     OpticFinder<?> debug3 = DSL.fieldFinder("Level", debug2);
/*    */     
/* 22 */     return fixTypeEverywhereTyped("ChunkLightRemoveFix", debug1, getOutputSchema().getType(References.CHUNK), debug1 -> debug1.updateTyped(debug0, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ChunkLightRemoveFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */