/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class AddNewChoices extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public AddNewChoices(Schema debug1, String debug2, DSL.TypeReference debug3) {
/* 14 */     super(debug1, true);
/* 15 */     this.name = debug2;
/* 16 */     this.type = debug3;
/*    */   }
/*    */   private final DSL.TypeReference type;
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 21 */     TaggedChoice.TaggedChoiceType<?> debug1 = getInputSchema().findChoiceType(this.type);
/* 22 */     TaggedChoice.TaggedChoiceType<?> debug2 = getOutputSchema().findChoiceType(this.type);
/* 23 */     return cap(this.name, debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected final <K> TypeRewriteRule cap(String debug1, TaggedChoice.TaggedChoiceType<K> debug2, TaggedChoice.TaggedChoiceType<?> debug3) {
/* 28 */     if (debug2.getKeyType() != debug3.getKeyType()) {
/* 29 */       throw new IllegalStateException("Could not inject: key type is not the same");
/*    */     }
/* 31 */     TaggedChoice.TaggedChoiceType<K> debug4 = (TaggedChoice.TaggedChoiceType)debug3;
/* 32 */     return fixTypeEverywhere(debug1, (Type)debug2, (Type)debug4, debug2 -> ());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\AddNewChoices.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */