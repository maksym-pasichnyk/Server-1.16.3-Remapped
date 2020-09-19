/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class RedstoneWireConnectionsFix extends DataFix {
/*    */   public RedstoneWireConnectionsFix(Schema debug1) {
/* 11 */     super(debug1, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 16 */     Schema debug1 = getInputSchema();
/* 17 */     return fixTypeEverywhereTyped("RedstoneConnectionsFix", debug1.getType(References.BLOCK_STATE), debug1 -> debug1.update(DSL.remainderFinder(), this::updateRedstoneConnections));
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> updateRedstoneConnections(Dynamic<T> debug1) {
/* 21 */     boolean debug2 = debug1.get("Name").asString().result().filter("minecraft:redstone_wire"::equals).isPresent();
/* 22 */     if (!debug2) {
/* 23 */       return debug1;
/*    */     }
/*    */     
/* 26 */     return debug1.update("Properties", debug0 -> {
/*    */           String debug1 = debug0.get("east").asString("none");
/*    */           String debug2 = debug0.get("west").asString("none");
/*    */           String debug3 = debug0.get("north").asString("none");
/*    */           String debug4 = debug0.get("south").asString("none");
/* 31 */           boolean debug5 = (isConnected(debug1) || isConnected(debug2));
/* 32 */           boolean debug6 = (isConnected(debug3) || isConnected(debug4));
/*    */           
/* 34 */           String debug7 = (!isConnected(debug1) && !debug6) ? "side" : debug1;
/* 35 */           String debug8 = (!isConnected(debug2) && !debug6) ? "side" : debug2;
/* 36 */           String debug9 = (!isConnected(debug3) && !debug5) ? "side" : debug3;
/* 37 */           String debug10 = (!isConnected(debug4) && !debug5) ? "side" : debug4;
/*    */           return debug0.update("east", ()).update("west", ()).update("north", ()).update("south", ());
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean isConnected(String debug0) {
/* 48 */     return !"none".equals(debug0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\RedstoneWireConnectionsFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */