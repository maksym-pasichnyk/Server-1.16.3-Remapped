/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityItemFrameDirectionFix extends NamedEntityFix {
/*    */   public EntityItemFrameDirectionFix(Schema debug1, boolean debug2) {
/* 10 */     super(debug1, debug2, "EntityItemFrameDirectionFix", References.ENTITY, "minecraft:item_frame");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> debug1) {
/* 14 */     return debug1.set("Facing", debug1.createByte(direction2dTo3d(debug1.get("Facing").asByte((byte)0))));
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 19 */     return debug1.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */   
/*    */   private static byte direction2dTo3d(byte debug0) {
/* 23 */     switch (debug0)
/*    */     
/*    */     { default:
/* 26 */         return 2;
/*    */       case 0:
/* 28 */         return 3;
/*    */       case 1:
/* 30 */         return 4;
/*    */       case 3:
/* 32 */         break; }  return 5;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityItemFrameDirectionFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */