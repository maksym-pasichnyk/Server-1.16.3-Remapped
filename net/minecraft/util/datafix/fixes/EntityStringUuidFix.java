/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class EntityStringUuidFix extends DataFix {
/*    */   public EntityStringUuidFix(Schema debug1, boolean debug2) {
/* 13 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 18 */     return fixTypeEverywhereTyped("EntityStringUuidFix", getInputSchema().getType(References.ENTITY), debug0 -> debug0.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityStringUuidFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */