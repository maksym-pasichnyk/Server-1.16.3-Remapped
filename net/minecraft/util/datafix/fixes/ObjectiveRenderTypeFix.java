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
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*    */ 
/*    */ public class ObjectiveRenderTypeFix
/*    */   extends DataFix {
/*    */   public ObjectiveRenderTypeFix(Schema debug1, boolean debug2) {
/* 19 */     super(debug1, debug2);
/*    */   }
/*    */   
/*    */   private static ObjectiveCriteria.RenderType getRenderType(String debug0) {
/* 23 */     return debug0.equals("health") ? ObjectiveCriteria.RenderType.HEARTS : ObjectiveCriteria.RenderType.INTEGER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 28 */     Type<Pair<String, Dynamic<?>>> debug1 = DSL.named(References.OBJECTIVE.typeName(), DSL.remainderType());
/*    */     
/* 30 */     if (!Objects.equals(debug1, getInputSchema().getType(References.OBJECTIVE))) {
/* 31 */       throw new IllegalStateException("Objective type is not what was expected.");
/*    */     }
/*    */     
/* 34 */     return fixTypeEverywhere("ObjectiveRenderTypeFix", debug1, debug0 -> ());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ObjectiveRenderTypeFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */