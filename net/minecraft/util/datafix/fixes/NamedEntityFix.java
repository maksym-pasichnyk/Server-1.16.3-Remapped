/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ 
/*    */ public abstract class NamedEntityFix extends DataFix {
/*    */   private final String name;
/*    */   private final String entityName;
/*    */   private final DSL.TypeReference type;
/*    */   
/*    */   public NamedEntityFix(Schema debug1, boolean debug2, String debug3, DSL.TypeReference debug4, String debug5) {
/* 16 */     super(debug1, debug2);
/* 17 */     this.name = debug3;
/* 18 */     this.type = debug4;
/* 19 */     this.entityName = debug5;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 24 */     OpticFinder<?> debug1 = DSL.namedChoice(this.entityName, getInputSchema().getChoiceType(this.type, this.entityName));
/*    */     
/* 26 */     return fixTypeEverywhereTyped(this.name, getInputSchema().getType(this.type), getOutputSchema().getType(this.type), debug2 -> debug2.updateTyped(debug1, getOutputSchema().getChoiceType(this.type, this.entityName), this::fix));
/*    */   }
/*    */   
/*    */   protected abstract Typed<?> fix(Typed<?> paramTyped);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\NamedEntityFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */