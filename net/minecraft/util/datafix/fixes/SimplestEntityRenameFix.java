/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public abstract class SimplestEntityRenameFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public SimplestEntityRenameFix(String debug1, Schema debug2, boolean debug3) {
/* 19 */     super(debug2, debug3);
/* 20 */     this.name = debug1;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 26 */     TaggedChoice.TaggedChoiceType<String> debug1 = getInputSchema().findChoiceType(References.ENTITY);
/* 27 */     TaggedChoice.TaggedChoiceType<String> debug2 = getOutputSchema().findChoiceType(References.ENTITY);
/*    */     
/* 29 */     Type<Pair<String, String>> debug3 = DSL.named(References.ENTITY_NAME.typeName(), NamespacedSchema.namespacedString());
/* 30 */     if (!Objects.equals(getOutputSchema().getType(References.ENTITY_NAME), debug3)) {
/* 31 */       throw new IllegalStateException("Entity name type is not what was expected.");
/*    */     }
/*    */     
/* 34 */     return TypeRewriteRule.seq(
/* 35 */         fixTypeEverywhere(this.name, (Type)debug1, (Type)debug2, debug3 -> ()), 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 47 */         fixTypeEverywhere(this.name + " for entity name", debug3, debug1 -> ()));
/*    */   }
/*    */   
/*    */   protected abstract String rename(String paramString);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\SimplestEntityRenameFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */