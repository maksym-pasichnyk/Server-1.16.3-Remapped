/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class BlockEntityJukeboxFix extends NamedEntityFix {
/*    */   public BlockEntityJukeboxFix(Schema debug1, boolean debug2) {
/* 12 */     super(debug1, debug2, "BlockEntityJukeboxFix", References.BLOCK_ENTITY, "minecraft:jukebox");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 17 */     Type<?> debug2 = getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:jukebox");
/* 18 */     Type<?> debug3 = debug2.findFieldType("RecordItem");
/* 19 */     OpticFinder<?> debug4 = DSL.fieldFinder("RecordItem", debug3);
/* 20 */     Dynamic<?> debug5 = (Dynamic)debug1.get(DSL.remainderFinder());
/* 21 */     int debug6 = debug5.get("Record").asInt(0);
/* 22 */     if (debug6 > 0) {
/* 23 */       debug5.remove("Record");
/*    */       
/* 25 */       String debug7 = ItemStackTheFlatteningFix.updateItem(ItemIdFix.getItem(debug6), 0);
/* 26 */       if (debug7 != null) {
/* 27 */         Dynamic<?> debug8 = debug5.emptyMap();
/* 28 */         debug8 = debug8.set("id", debug8.createString(debug7));
/* 29 */         debug8 = debug8.set("Count", debug8.createByte((byte)1));
/* 30 */         return debug1.set(debug4, (Typed)((Pair)debug3.readTyped(debug8).result().orElseThrow(() -> new IllegalStateException("Could not create record item stack."))).getFirst()).set(DSL.remainderFinder(), debug5);
/*    */       } 
/*    */     } 
/* 33 */     return debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockEntityJukeboxFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */