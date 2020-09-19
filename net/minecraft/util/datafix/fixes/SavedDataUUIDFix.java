/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class SavedDataUUIDFix extends AbstractUUIDFix {
/*    */   public SavedDataUUIDFix(Schema debug1) {
/* 10 */     super(debug1, References.SAVED_DATA);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 15 */     return fixTypeEverywhereTyped("SavedDataUUIDFix", getInputSchema().getType(this.typeReference), debug0 -> debug0.updateTyped(debug0.getType().findField("data"), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\SavedDataUUIDFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */