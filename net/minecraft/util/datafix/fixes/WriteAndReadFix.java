/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ 
/*    */ public class WriteAndReadFix extends DataFix {
/*    */   private final String name;
/*    */   private final DSL.TypeReference type;
/*    */   
/*    */   public WriteAndReadFix(Schema debug1, String debug2, DSL.TypeReference debug3) {
/* 13 */     super(debug1, true);
/* 14 */     this.name = debug2;
/* 15 */     this.type = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 20 */     return writeAndRead(this.name, getInputSchema().getType(this.type), getOutputSchema().getType(this.type));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\WriteAndReadFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */