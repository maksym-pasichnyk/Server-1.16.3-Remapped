/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class BlockEntityBlockStateFix extends NamedEntityFix {
/*    */   public BlockEntityBlockStateFix(Schema debug1, boolean debug2) {
/* 12 */     super(debug1, debug2, "BlockEntityBlockStateFix", References.BLOCK_ENTITY, "minecraft:piston");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 17 */     Type<?> debug2 = getOutputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:piston");
/*    */     
/* 19 */     Type<?> debug3 = debug2.findFieldType("blockState");
/* 20 */     OpticFinder<?> debug4 = DSL.fieldFinder("blockState", debug3);
/* 21 */     Dynamic<?> debug5 = (Dynamic)debug1.get(DSL.remainderFinder());
/*    */     
/* 23 */     int debug6 = debug5.get("blockId").asInt(0);
/* 24 */     debug5 = debug5.remove("blockId");
/* 25 */     int debug7 = debug5.get("blockData").asInt(0) & 0xF;
/* 26 */     debug5 = debug5.remove("blockData");
/*    */     
/* 28 */     Dynamic<?> debug8 = BlockStateData.getTag(debug6 << 4 | debug7);
/* 29 */     Typed<?> debug9 = (Typed)debug2.pointTyped(debug1.getOps()).orElseThrow(() -> new IllegalStateException("Could not create new piston block entity."));
/* 30 */     return debug9.set(DSL.remainderFinder(), debug5).set(debug4, (Typed)((Pair)debug3.readTyped(debug8).result().orElseThrow(() -> new IllegalStateException("Could not parse newly created block state tag."))).getFirst());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockEntityBlockStateFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */