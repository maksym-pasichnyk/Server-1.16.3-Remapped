/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class BlockEntityKeepPacked extends NamedEntityFix {
/*    */   public BlockEntityKeepPacked(Schema debug1, boolean debug2) {
/* 10 */     super(debug1, debug2, "BlockEntityKeepPacked", References.BLOCK_ENTITY, "DUMMY");
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixTag(Dynamic<?> debug0) {
/* 14 */     return debug0.set("keepPacked", debug0.createBoolean(true));
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 19 */     return debug1.update(DSL.remainderFinder(), BlockEntityKeepPacked::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockEntityKeepPacked.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */