/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public abstract class EntityRenameFix
/*    */   extends DataFix {
/*    */   public EntityRenameFix(String debug1, Schema debug2, boolean debug3) {
/* 16 */     super(debug2, debug3);
/* 17 */     this.name = debug1;
/*    */   }
/*    */   
/*    */   protected final String name;
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 23 */     TaggedChoice.TaggedChoiceType<String> debug1 = getInputSchema().findChoiceType(References.ENTITY);
/* 24 */     TaggedChoice.TaggedChoiceType<String> debug2 = getOutputSchema().findChoiceType(References.ENTITY);
/*    */     
/* 26 */     return fixTypeEverywhere(this.name, (Type)debug1, (Type)debug2, debug3 -> ());
/*    */   }
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
/*    */ 
/*    */ 
/*    */   
/*    */   private <A> Typed<A> getEntity(Object debug1, DynamicOps<?> debug2, Type<A> debug3) {
/* 43 */     return new Typed(debug3, debug2, debug1);
/*    */   }
/*    */   
/*    */   protected abstract Pair<String, Typed<?>> fix(String paramString, Typed<?> paramTyped);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityRenameFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */