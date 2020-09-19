/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class ReorganizePoi
/*    */   extends DataFix {
/*    */   public ReorganizePoi(Schema debug1, boolean debug2) {
/* 21 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 26 */     Type<Pair<String, Dynamic<?>>> debug1 = DSL.named(References.POI_CHUNK.typeName(), DSL.remainderType());
/*    */     
/* 28 */     if (!Objects.equals(debug1, getInputSchema().getType(References.POI_CHUNK))) {
/* 29 */       throw new IllegalStateException("Poi type is not what was expected.");
/*    */     }
/* 31 */     return fixTypeEverywhere("POI reorganization", debug1, debug0 -> ());
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> cap(Dynamic<T> debug0) {
/* 35 */     Map<Dynamic<T>, Dynamic<T>> debug1 = Maps.newHashMap();
/* 36 */     for (int debug2 = 0; debug2 < 16; debug2++) {
/* 37 */       String debug3 = String.valueOf(debug2);
/* 38 */       Optional<Dynamic<T>> debug4 = debug0.get(debug3).result();
/* 39 */       if (debug4.isPresent()) {
/* 40 */         Dynamic<T> debug5 = debug4.get();
/* 41 */         Dynamic<T> debug6 = debug0.createMap((Map)ImmutableMap.of(debug0.createString("Records"), debug5));
/* 42 */         debug1.put(debug0.createInt(debug2), debug6);
/* 43 */         debug0 = debug0.remove(debug3);
/*    */       } 
/*    */     } 
/*    */     
/* 47 */     return debug0.set("Sections", debug0.createMap(debug1));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ReorganizePoi.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */