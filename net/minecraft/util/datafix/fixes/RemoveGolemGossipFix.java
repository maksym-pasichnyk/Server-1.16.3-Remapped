/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class RemoveGolemGossipFix
/*    */   extends NamedEntityFix {
/*    */   public RemoveGolemGossipFix(Schema debug1, boolean debug2) {
/* 11 */     super(debug1, debug2, "Remove Golem Gossip Fix", References.ENTITY, "minecraft:villager");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 16 */     return debug1.update(DSL.remainderFinder(), RemoveGolemGossipFix::fixValue);
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixValue(Dynamic<?> debug0) {
/* 20 */     return debug0.update("Gossips", debug1 -> debug0.createList(debug1.asStream().filter(())));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\RemoveGolemGossipFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */