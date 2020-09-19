/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.google.common.base.Splitter;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.StreamSupport;
/*    */ import org.apache.commons.lang3.math.NumberUtils;
/*    */ 
/*    */ public class LevelFlatGeneratorInfoFix extends DataFix {
/*    */   public LevelFlatGeneratorInfoFix(Schema debug1, boolean debug2) {
/* 20 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   private static final Splitter SPLITTER = Splitter.on(';').limit(5);
/* 28 */   private static final Splitter LAYER_SPLITTER = Splitter.on(',');
/* 29 */   private static final Splitter OLD_AMOUNT_SPLITTER = Splitter.on('x').limit(2);
/* 30 */   private static final Splitter AMOUNT_SPLITTER = Splitter.on('*').limit(2);
/* 31 */   private static final Splitter BLOCK_SPLITTER = Splitter.on(':').limit(3);
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 35 */     return fixTypeEverywhereTyped("LevelFlatGeneratorInfoFix", getInputSchema().getType(References.LEVEL), debug1 -> debug1.update(DSL.remainderFinder(), this::fix));
/*    */   }
/*    */   
/*    */   private Dynamic<?> fix(Dynamic<?> debug1) {
/* 39 */     if (debug1.get("generatorName").asString("").equalsIgnoreCase("flat")) {
/* 40 */       return debug1.update("generatorOptions", debug1 -> (Dynamic)DataFixUtils.orElse(debug1.asString().map(this::fixString).map(debug1::createString).result(), debug1));
/*    */     }
/* 42 */     return debug1;
/*    */   } @VisibleForTesting
/*    */   String fixString(String debug1) {
/*    */     int debug4;
/*    */     String debug5;
/* 47 */     if (debug1.isEmpty()) {
/* 48 */       return "minecraft:bedrock,2*minecraft:dirt,minecraft:grass_block;1;village";
/*    */     }
/*    */     
/* 51 */     Iterator<String> debug2 = SPLITTER.split(debug1).iterator();
/*    */     
/* 53 */     String debug3 = debug2.next();
/*    */ 
/*    */     
/* 56 */     if (debug2.hasNext()) {
/* 57 */       debug4 = NumberUtils.toInt(debug3, 0);
/* 58 */       debug5 = debug2.next();
/*    */     } else {
/* 60 */       debug4 = 0;
/* 61 */       debug5 = debug3;
/*    */     } 
/*    */     
/* 64 */     if (debug4 < 0 || debug4 > 3) {
/* 65 */       return "minecraft:bedrock,2*minecraft:dirt,minecraft:grass_block;1;village";
/*    */     }
/*    */     
/* 68 */     StringBuilder debug6 = new StringBuilder();
/*    */     
/* 70 */     Splitter debug7 = (debug4 < 3) ? OLD_AMOUNT_SPLITTER : AMOUNT_SPLITTER;
/*    */     
/* 72 */     debug6.append(StreamSupport.stream(LAYER_SPLITTER.split(debug5).spliterator(), false).map(debug2 -> {
/*    */             int debug3;
/*    */             
/*    */             String debug4;
/*    */             
/*    */             List<String> debug5 = debug0.splitToList(debug2);
/*    */             
/*    */             if (debug5.size() == 2) {
/*    */               debug3 = NumberUtils.toInt(debug5.get(0));
/*    */               debug4 = debug5.get(1);
/*    */             } else {
/*    */               debug3 = 1;
/*    */               debug4 = debug5.get(0);
/*    */             } 
/*    */             List<String> debug6 = BLOCK_SPLITTER.splitToList(debug4);
/*    */             int debug7 = ((String)debug6.get(0)).equals("minecraft") ? 1 : 0;
/*    */             String debug8 = debug6.get(debug7);
/*    */             int debug9 = (debug1 == 3) ? EntityBlockStateFix.getBlockId("minecraft:" + debug8) : NumberUtils.toInt(debug8, 0);
/*    */             int debug10 = debug7 + 1;
/*    */             int debug11 = (debug6.size() > debug10) ? NumberUtils.toInt(debug6.get(debug10), 0) : 0;
/*    */             return ((debug3 == 1) ? "" : (debug3 + "*")) + BlockStateData.getTag(debug9 << 4 | debug11).get("Name").asString("");
/* 93 */           }).collect(Collectors.joining(",")));
/*    */     
/* 95 */     while (debug2.hasNext()) {
/* 96 */       debug6.append(';').append(debug2.next());
/*    */     }
/*    */     
/* 99 */     return debug6.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\LevelFlatGeneratorInfoFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */