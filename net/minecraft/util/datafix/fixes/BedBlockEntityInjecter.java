/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.List;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class BedBlockEntityInjecter
/*    */   extends DataFix
/*    */ {
/*    */   public BedBlockEntityInjecter(Schema debug1, boolean debug2) {
/* 26 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 31 */     Type<?> debug1 = getOutputSchema().getType(References.CHUNK);
/* 32 */     Type<?> debug2 = debug1.findFieldType("Level");
/* 33 */     Type<?> debug3 = debug2.findFieldType("TileEntities");
/* 34 */     if (!(debug3 instanceof List.ListType)) {
/* 35 */       throw new IllegalStateException("Tile entity type is not a list type.");
/*    */     }
/* 37 */     List.ListType<?> debug4 = (List.ListType)debug3;
/*    */     
/* 39 */     return cap(debug2, debug4);
/*    */   }
/*    */   
/*    */   private <TE> TypeRewriteRule cap(Type<?> debug1, List.ListType<TE> debug2) {
/* 43 */     Type<TE> debug3 = debug2.getElement();
/* 44 */     OpticFinder<?> debug4 = DSL.fieldFinder("Level", debug1);
/* 45 */     OpticFinder<List<TE>> debug5 = DSL.fieldFinder("TileEntities", (Type)debug2);
/*    */ 
/*    */     
/* 48 */     int debug6 = 416;
/*    */     
/* 50 */     return TypeRewriteRule.seq(
/* 51 */         fixTypeEverywhere("InjectBedBlockEntityType", (Type)getInputSchema().findChoiceType(References.BLOCK_ENTITY), (Type)getOutputSchema().findChoiceType(References.BLOCK_ENTITY), debug0 -> ()), 
/* 52 */         fixTypeEverywhereTyped("BedBlockEntityInjecter", getOutputSchema().getType(References.CHUNK), debug3 -> {
/*    */             Typed<?> debug4 = debug3.getTyped(debug0);
/*    */             Dynamic<?> debug5 = (Dynamic)debug4.get(DSL.remainderFinder());
/*    */             int debug6 = debug5.get("xPos").asInt(0);
/*    */             int debug7 = debug5.get("zPos").asInt(0);
/*    */             List<TE> debug8 = Lists.newArrayList((Iterable)debug4.getOrCreate(debug1));
/*    */             List<? extends Dynamic<?>> debug9 = debug5.get("Sections").asList(Function.identity());
/*    */             for (int debug10 = 0; debug10 < debug9.size(); debug10++) {
/*    */               Dynamic<?> debug11 = debug9.get(debug10);
/*    */               int debug12 = debug11.get("Y").asInt(0);
/*    */               Stream<Integer> debug13 = debug11.get("Blocks").asStream().map(());
/*    */               int debug14 = 0;
/*    */               Iterator<?> iterator = debug13::iterator.iterator();
/*    */               while (iterator.hasNext()) {
/*    */                 int debug16 = ((Integer)iterator.next()).intValue();
/*    */                 if (416 == (debug16 & 0xFF) << 4) {
/*    */                   int debug17 = debug14 & 0xF;
/*    */                   int debug18 = debug14 >> 8 & 0xF;
/*    */                   int debug19 = debug14 >> 4 & 0xF;
/*    */                   Map<Dynamic<?>, Dynamic<?>> debug20 = Maps.newHashMap();
/*    */                   debug20.put(debug11.createString("id"), debug11.createString("minecraft:bed"));
/*    */                   debug20.put(debug11.createString("x"), debug11.createInt(debug17 + (debug6 << 4)));
/*    */                   debug20.put(debug11.createString("y"), debug11.createInt(debug18 + (debug12 << 4)));
/*    */                   debug20.put(debug11.createString("z"), debug11.createInt(debug19 + (debug7 << 4)));
/*    */                   debug20.put(debug11.createString("color"), debug11.createShort((short)14));
/*    */                   debug8.add((TE)((Pair)debug2.read(debug11.createMap(debug20)).result().orElseThrow(())).getFirst());
/*    */                 } 
/*    */                 debug14++;
/*    */               } 
/*    */             } 
/*    */             return !debug8.isEmpty() ? debug3.set(debug0, debug4.set(debug1, debug8)) : debug3;
/*    */           }));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\BedBlockEntityInjecter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */