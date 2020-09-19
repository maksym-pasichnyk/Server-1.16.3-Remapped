/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class SavedDataVillageCropFix
/*    */   extends DataFix {
/*    */   public SavedDataVillageCropFix(Schema debug1, boolean debug2) {
/* 12 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 17 */     return writeFixAndRead("SavedDataVillageCropFix", getInputSchema().getType(References.STRUCTURE_FEATURE), getOutputSchema().getType(References.STRUCTURE_FEATURE), this::fixTag);
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> fixTag(Dynamic<T> debug1) {
/* 21 */     return debug1.update("Children", SavedDataVillageCropFix::updateChildren);
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> updateChildren(Dynamic<T> debug0) {
/* 25 */     return debug0.asStreamOpt().map(SavedDataVillageCropFix::updateChildren).map(debug0::createList).result().orElse(debug0);
/*    */   }
/*    */   
/*    */   private static Stream<? extends Dynamic<?>> updateChildren(Stream<? extends Dynamic<?>> debug0) {
/* 29 */     return debug0.map(debug0 -> {
/*    */           String debug1 = debug0.get("id").asString("");
/*    */           return "ViF".equals(debug1) ? updateSingleField(debug0) : ("ViDF".equals(debug1) ? updateDoubleField(debug0) : debug0);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static <T> Dynamic<T> updateSingleField(Dynamic<T> debug0) {
/* 43 */     debug0 = updateCrop(debug0, "CA");
/* 44 */     return updateCrop(debug0, "CB");
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> updateDoubleField(Dynamic<T> debug0) {
/* 48 */     debug0 = updateCrop(debug0, "CA");
/* 49 */     debug0 = updateCrop(debug0, "CB");
/* 50 */     debug0 = updateCrop(debug0, "CC");
/* 51 */     return updateCrop(debug0, "CD");
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> updateCrop(Dynamic<T> debug0, String debug1) {
/* 55 */     if (debug0.get(debug1).asNumber().result().isPresent()) {
/* 56 */       return debug0.set(debug1, BlockStateData.getTag(debug0.get(debug1).asInt(0) << 4));
/*    */     }
/* 58 */     return debug0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\SavedDataVillageCropFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */