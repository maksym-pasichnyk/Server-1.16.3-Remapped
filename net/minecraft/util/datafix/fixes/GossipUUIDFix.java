/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class GossipUUIDFix extends NamedEntityFix {
/*    */   public GossipUUIDFix(Schema debug1, String debug2) {
/* 10 */     super(debug1, false, "Gossip for for " + debug2, References.ENTITY, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 15 */     return debug1.update(DSL.remainderFinder(), debug0 -> debug0.update("Gossips", ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\GossipUUIDFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */