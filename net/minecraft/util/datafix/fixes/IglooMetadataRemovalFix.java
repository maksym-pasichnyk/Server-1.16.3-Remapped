/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class IglooMetadataRemovalFix extends DataFix {
/*    */   public IglooMetadataRemovalFix(Schema debug1, boolean debug2) {
/* 11 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 16 */     Type<?> debug1 = getInputSchema().getType(References.STRUCTURE_FEATURE);
/* 17 */     Type<?> debug2 = getOutputSchema().getType(References.STRUCTURE_FEATURE);
/*    */     
/* 19 */     return writeFixAndRead("IglooMetadataRemovalFix", debug1, debug2, IglooMetadataRemovalFix::fixTag);
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> fixTag(Dynamic<T> debug0) {
/* 23 */     boolean debug1 = ((Boolean)debug0.get("Children").asStreamOpt().map(debug0 -> Boolean.valueOf(debug0.allMatch(IglooMetadataRemovalFix::isIglooPiece))).result().orElse(Boolean.valueOf(false))).booleanValue();
/*    */     
/* 25 */     if (debug1) {
/* 26 */       return debug0.set("id", debug0.createString("Igloo")).remove("Children");
/*    */     }
/* 28 */     return debug0.update("Children", IglooMetadataRemovalFix::removeIglooPieces);
/*    */   }
/*    */ 
/*    */   
/*    */   private static <T> Dynamic<T> removeIglooPieces(Dynamic<T> debug0) {
/* 33 */     return debug0.asStreamOpt().map(debug0 -> debug0.filter(())).map(debug0::createList).result().orElse(debug0);
/*    */   }
/*    */   
/*    */   private static boolean isIglooPiece(Dynamic<?> debug0) {
/* 37 */     return debug0.get("id").asString("").equals("Iglu");
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\IglooMetadataRemovalFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */