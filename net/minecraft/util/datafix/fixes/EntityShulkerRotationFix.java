/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.List;
/*    */ 
/*    */ public class EntityShulkerRotationFix
/*    */   extends NamedEntityFix {
/*    */   public EntityShulkerRotationFix(Schema debug1) {
/* 12 */     super(debug1, false, "EntityShulkerRotationFix", References.ENTITY, "minecraft:shulker");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> debug1) {
/* 16 */     List<Double> debug2 = debug1.get("Rotation").asList(debug0 -> Double.valueOf(debug0.asDouble(180.0D)));
/* 17 */     if (!debug2.isEmpty()) {
/* 18 */       debug2.set(0, Double.valueOf(((Double)debug2.get(0)).doubleValue() - 180.0D));
/* 19 */       return debug1.set("Rotation", debug1.createList(debug2.stream().map(debug1::createDouble)));
/*    */     } 
/* 21 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 26 */     return debug1.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityShulkerRotationFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */