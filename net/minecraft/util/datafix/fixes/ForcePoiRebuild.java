/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class ForcePoiRebuild
/*    */   extends DataFix
/*    */ {
/*    */   public ForcePoiRebuild(Schema debug1, boolean debug2) {
/* 18 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 23 */     Type<Pair<String, Dynamic<?>>> debug1 = DSL.named(References.POI_CHUNK.typeName(), DSL.remainderType());
/*    */     
/* 25 */     if (!Objects.equals(debug1, getInputSchema().getType(References.POI_CHUNK))) {
/* 26 */       throw new IllegalStateException("Poi type is not what was expected.");
/*    */     }
/* 28 */     return fixTypeEverywhere("POI rebuild", debug1, debug0 -> ());
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> cap(Dynamic<T> debug0) {
/* 32 */     return debug0.update("Sections", debug0 -> debug0.updateMapValues(()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ForcePoiRebuild.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */