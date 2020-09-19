/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class OptionsRenameFieldFix extends DataFix {
/*    */   private final String fixName;
/*    */   
/*    */   public OptionsRenameFieldFix(Schema debug1, boolean debug2, String debug3, String debug4, String debug5) {
/* 15 */     super(debug1, debug2);
/* 16 */     this.fixName = debug3;
/* 17 */     this.fieldFrom = debug4;
/* 18 */     this.fieldTo = debug5;
/*    */   }
/*    */   private final String fieldFrom; private final String fieldTo;
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 23 */     return fixTypeEverywhereTyped(this.fixName, getInputSchema().getType(References.OPTIONS), debug1 -> debug1.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\OptionsRenameFieldFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */