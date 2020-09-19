/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Arrays;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.IntStream;
/*    */ 
/*    */ public class ChunkBiomeFix extends DataFix {
/*    */   public ChunkBiomeFix(Schema debug1, boolean debug2) {
/* 16 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 21 */     Type<?> debug1 = getInputSchema().getType(References.CHUNK);
/* 22 */     OpticFinder<?> debug2 = debug1.findField("Level");
/*    */     
/* 24 */     return fixTypeEverywhereTyped("Leaves fix", debug1, debug1 -> debug1.updateTyped(debug0, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ChunkBiomeFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */