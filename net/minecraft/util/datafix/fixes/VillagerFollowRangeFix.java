/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillagerFollowRangeFix
/*    */   extends NamedEntityFix
/*    */ {
/*    */   public VillagerFollowRangeFix(Schema debug1) {
/* 17 */     super(debug1, false, "Villager Follow Range Fix", References.ENTITY, "minecraft:villager");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 22 */     return debug1.update(DSL.remainderFinder(), VillagerFollowRangeFix::fixValue);
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixValue(Dynamic<?> debug0) {
/* 26 */     return debug0.update("Attributes", debug1 -> debug0.createList(debug1.asStream().map(())));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\VillagerFollowRangeFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */