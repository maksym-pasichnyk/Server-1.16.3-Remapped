/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class PlayerUUIDFix extends AbstractUUIDFix {
/*    */   public PlayerUUIDFix(Schema debug1) {
/* 12 */     super(debug1, References.PLAYER);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     return fixTypeEverywhereTyped("PlayerUUIDFix", getInputSchema().getType(this.typeReference), debug0 -> {
/*    */           OpticFinder<?> debug1 = debug0.getType().findField("RootVehicle");
/*    */           return debug0.updateTyped(debug1, debug1.type(), ()).update(DSL.remainderFinder(), ());
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\PlayerUUIDFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */