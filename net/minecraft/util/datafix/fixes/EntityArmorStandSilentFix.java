/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityArmorStandSilentFix extends NamedEntityFix {
/*    */   public EntityArmorStandSilentFix(Schema debug1, boolean debug2) {
/* 10 */     super(debug1, debug2, "EntityArmorStandSilentFix", References.ENTITY, "ArmorStand");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> debug1) {
/* 14 */     if (debug1.get("Silent").asBoolean(false) && !debug1.get("Marker").asBoolean(false)) {
/* 15 */       return debug1.remove("Silent");
/*    */     }
/* 17 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 22 */     return debug1.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityArmorStandSilentFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */