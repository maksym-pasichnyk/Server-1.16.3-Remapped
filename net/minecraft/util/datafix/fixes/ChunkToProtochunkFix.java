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
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import it.unimi.dsi.fastutil.shorts.ShortArrayList;
/*    */ import it.unimi.dsi.fastutil.shorts.ShortList;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.IntStream;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class ChunkToProtochunkFix
/*    */   extends DataFix {
/*    */   public ChunkToProtochunkFix(Schema debug1, boolean debug2) {
/* 26 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 31 */     Type<?> debug1 = getInputSchema().getType(References.CHUNK);
/* 32 */     Type<?> debug2 = getOutputSchema().getType(References.CHUNK);
/* 33 */     Type<?> debug3 = debug1.findFieldType("Level");
/* 34 */     Type<?> debug4 = debug2.findFieldType("Level");
/* 35 */     Type<?> debug5 = debug3.findFieldType("TileTicks");
/*    */     
/* 37 */     OpticFinder<?> debug6 = DSL.fieldFinder("Level", debug3);
/* 38 */     OpticFinder<?> debug7 = DSL.fieldFinder("TileTicks", debug5);
/*    */     
/* 40 */     return TypeRewriteRule.seq(
/* 41 */         fixTypeEverywhereTyped("ChunkToProtoChunkFix", debug1, getOutputSchema().getType(References.CHUNK), debug3 -> debug3.updateTyped(debug0, debug1, ())), 
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
/* 84 */         writeAndRead("Structure biome inject", getInputSchema().getType(References.STRUCTURE_FEATURE), getOutputSchema().getType(References.STRUCTURE_FEATURE)));
/*    */   }
/*    */ 
/*    */   
/*    */   private static short packOffsetCoordinates(int debug0, int debug1, int debug2) {
/* 89 */     return (short)(debug0 & 0xF | (debug1 & 0xF) << 4 | (debug2 & 0xF) << 8);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ChunkToProtochunkFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */