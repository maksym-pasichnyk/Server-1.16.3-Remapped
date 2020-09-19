/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class ChunkStatusFix2 extends DataFix {
/* 16 */   private static final Map<String, String> RENAMES_AND_DOWNGRADES = (Map<String, String>)ImmutableMap.builder()
/* 17 */     .put("structure_references", "empty")
/* 18 */     .put("biomes", "empty")
/* 19 */     .put("base", "surface")
/* 20 */     .put("carved", "carvers")
/* 21 */     .put("liquid_carved", "liquid_carvers")
/* 22 */     .put("decorated", "features")
/* 23 */     .put("lighted", "light")
/* 24 */     .put("mobs_spawned", "spawn")
/* 25 */     .put("finalized", "heightmaps")
/* 26 */     .put("fullchunk", "full")
/* 27 */     .build();
/*    */   
/*    */   public ChunkStatusFix2(Schema debug1, boolean debug2) {
/* 30 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 35 */     Type<?> debug1 = getInputSchema().getType(References.CHUNK);
/* 36 */     Type<?> debug2 = debug1.findFieldType("Level");
/*    */     
/* 38 */     OpticFinder<?> debug3 = DSL.fieldFinder("Level", debug2);
/*    */     
/* 40 */     return fixTypeEverywhereTyped("ChunkStatusFix2", debug1, getOutputSchema().getType(References.CHUNK), debug1 -> debug1.updateTyped(debug0, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ChunkStatusFix2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */