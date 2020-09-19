/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public abstract class PoiTypeRename extends DataFix {
/*    */   public PoiTypeRename(Schema debug1, boolean debug2) {
/* 19 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 24 */     Type<Pair<String, Dynamic<?>>> debug1 = DSL.named(References.POI_CHUNK.typeName(), DSL.remainderType());
/*    */     
/* 26 */     if (!Objects.equals(debug1, getInputSchema().getType(References.POI_CHUNK))) {
/* 27 */       throw new IllegalStateException("Poi type is not what was expected.");
/*    */     }
/* 29 */     return fixTypeEverywhere("POI rename", debug1, debug1 -> ());
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> cap(Dynamic<T> debug1) {
/* 33 */     return debug1.update("Sections", debug1 -> debug1.updateMapValues(()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private <T> Optional<Dynamic<T>> renameRecords(Dynamic<T> debug1) {
/* 39 */     return debug1.asStreamOpt().map(debug2 -> debug1.createList(debug2.map(())))
/*    */ 
/*    */       
/* 42 */       .result();
/*    */   }
/*    */   
/*    */   protected abstract String rename(String paramString);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\PoiTypeRename.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */