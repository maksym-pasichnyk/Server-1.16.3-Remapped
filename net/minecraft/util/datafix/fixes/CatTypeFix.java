/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class CatTypeFix extends NamedEntityFix {
/*    */   public CatTypeFix(Schema debug1, boolean debug2) {
/* 10 */     super(debug1, debug2, "CatTypeFix", References.ENTITY, "minecraft:cat");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> debug1) {
/* 14 */     if (debug1.get("CatType").asInt(0) == 9) {
/* 15 */       return debug1.set("CatType", debug1.createInt(10));
/*    */     }
/* 17 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 22 */     return debug1.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\CatTypeFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */