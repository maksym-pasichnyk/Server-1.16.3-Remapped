/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class LevelUUIDFix extends AbstractUUIDFix {
/*    */   public LevelUUIDFix(Schema debug1) {
/* 12 */     super(debug1, References.LEVEL);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     return fixTypeEverywhereTyped("LevelUUIDFix", getInputSchema().getType(this.typeReference), debug1 -> debug1.updateTyped(DSL.remainderFinder(), ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Dynamic<?> updateWanderingTrader(Dynamic<?> debug1) {
/* 28 */     return replaceUUIDString(debug1, "WanderingTraderId", "WanderingTraderId").orElse(debug1);
/*    */   }
/*    */   
/*    */   private Dynamic<?> updateDragonFight(Dynamic<?> debug1) {
/* 32 */     return debug1.update("DimensionData", debug0 -> debug0.updateMapValues(()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Dynamic<?> updateCustomBossEvents(Dynamic<?> debug1) {
/* 42 */     return debug1.update("CustomBossEvents", debug0 -> debug0.updateMapValues(()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\LevelUUIDFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */