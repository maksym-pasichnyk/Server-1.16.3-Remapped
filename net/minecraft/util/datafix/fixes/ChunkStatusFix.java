/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class ChunkStatusFix extends DataFix {
/*    */   public ChunkStatusFix(Schema debug1, boolean debug2) {
/* 15 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 20 */     Type<?> debug1 = getInputSchema().getType(References.CHUNK);
/* 21 */     Type<?> debug2 = debug1.findFieldType("Level");
/*    */     
/* 23 */     OpticFinder<?> debug3 = DSL.fieldFinder("Level", debug2);
/*    */     
/* 25 */     return fixTypeEverywhereTyped("ChunkStatusFix", debug1, getOutputSchema().getType(References.CHUNK), debug1 -> debug1.updateTyped(debug0, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ChunkStatusFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */