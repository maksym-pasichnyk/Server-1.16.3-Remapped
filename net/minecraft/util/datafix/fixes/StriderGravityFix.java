/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class StriderGravityFix extends NamedEntityFix {
/*    */   public StriderGravityFix(Schema debug1, boolean debug2) {
/* 10 */     super(debug1, debug2, "StriderGravityFix", References.ENTITY, "minecraft:strider");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> debug1) {
/* 14 */     if (debug1.get("NoGravity").asBoolean(false)) {
/* 15 */       return debug1.set("NoGravity", debug1.createBoolean(false));
/*    */     }
/* 17 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 22 */     return debug1.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\StriderGravityFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */