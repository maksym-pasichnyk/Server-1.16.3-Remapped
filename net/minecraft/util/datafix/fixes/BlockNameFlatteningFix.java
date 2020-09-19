/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class BlockNameFlatteningFix
/*    */   extends DataFix {
/*    */   public BlockNameFlatteningFix(Schema debug1, boolean debug2) {
/* 18 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 23 */     Type<?> debug1 = getInputSchema().getType(References.BLOCK_NAME);
/* 24 */     Type<?> debug2 = getOutputSchema().getType(References.BLOCK_NAME);
/*    */     
/* 26 */     Type<Pair<String, Either<Integer, String>>> debug3 = DSL.named(References.BLOCK_NAME.typeName(), DSL.or(DSL.intType(), NamespacedSchema.namespacedString()));
/* 27 */     Type<Pair<String, String>> debug4 = DSL.named(References.BLOCK_NAME.typeName(), NamespacedSchema.namespacedString());
/*    */     
/* 29 */     if (!Objects.equals(debug1, debug3) || !Objects.equals(debug2, debug4)) {
/* 30 */       throw new IllegalStateException("Expected and actual types don't match.");
/*    */     }
/* 32 */     return fixTypeEverywhere("BlockNameFlatteningFix", debug3, debug4, debug0 -> ());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockNameFlatteningFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */