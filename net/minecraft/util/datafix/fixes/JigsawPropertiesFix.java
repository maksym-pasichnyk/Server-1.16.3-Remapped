/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class JigsawPropertiesFix extends NamedEntityFix {
/*    */   public JigsawPropertiesFix(Schema debug1, boolean debug2) {
/* 10 */     super(debug1, debug2, "JigsawPropertiesFix", References.BLOCK_ENTITY, "minecraft:jigsaw");
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixTag(Dynamic<?> debug0) {
/* 14 */     String debug1 = debug0.get("attachement_type").asString("minecraft:empty");
/* 15 */     String debug2 = debug0.get("target_pool").asString("minecraft:empty");
/* 16 */     return debug0
/* 17 */       .set("name", debug0.createString(debug1))
/* 18 */       .set("target", debug0.createString(debug1))
/* 19 */       .remove("attachement_type")
/* 20 */       .set("pool", debug0.createString(debug2))
/* 21 */       .remove("target_pool");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 26 */     return debug1.update(DSL.remainderFinder(), JigsawPropertiesFix::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\JigsawPropertiesFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */