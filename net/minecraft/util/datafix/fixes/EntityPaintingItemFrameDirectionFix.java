/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityPaintingItemFrameDirectionFix extends DataFix {
/* 12 */   private static final int[][] DIRECTIONS = new int[][] { { 0, 0, 1 }, { -1, 0, 0 }, { 0, 0, -1 }, { 1, 0, 0 } };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public EntityPaintingItemFrameDirectionFix(Schema debug1, boolean debug2) {
/* 20 */     super(debug1, debug2);
/*    */   }
/*    */   
/*    */   private Dynamic<?> doFix(Dynamic<?> debug1, boolean debug2, boolean debug3) {
/* 24 */     if ((debug2 || debug3) && !debug1.get("Facing").asNumber().result().isPresent()) {
/*    */       int debug4;
/* 26 */       if (debug1.get("Direction").asNumber().result().isPresent()) {
/* 27 */         debug4 = debug1.get("Direction").asByte((byte)0) % DIRECTIONS.length;
/* 28 */         int[] debug5 = DIRECTIONS[debug4];
/*    */         
/* 30 */         debug1 = debug1.set("TileX", debug1.createInt(debug1.get("TileX").asInt(0) + debug5[0]));
/* 31 */         debug1 = debug1.set("TileY", debug1.createInt(debug1.get("TileY").asInt(0) + debug5[1]));
/* 32 */         debug1 = debug1.set("TileZ", debug1.createInt(debug1.get("TileZ").asInt(0) + debug5[2]));
/*    */         
/* 34 */         debug1 = debug1.remove("Direction");
/*    */         
/* 36 */         if (debug3 && debug1.get("ItemRotation").asNumber().result().isPresent()) {
/* 37 */           debug1 = debug1.set("ItemRotation", debug1.createByte((byte)(debug1.get("ItemRotation").asByte((byte)0) * 2)));
/*    */         }
/*    */       } else {
/* 40 */         debug4 = debug1.get("Dir").asByte((byte)0) % DIRECTIONS.length;
/* 41 */         debug1 = debug1.remove("Dir");
/*    */       } 
/* 43 */       debug1 = debug1.set("Facing", debug1.createByte((byte)debug4));
/*    */     } 
/*    */     
/* 46 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 51 */     Type<?> debug1 = getInputSchema().getChoiceType(References.ENTITY, "Painting");
/* 52 */     OpticFinder<?> debug2 = DSL.namedChoice("Painting", debug1);
/*    */     
/* 54 */     Type<?> debug3 = getInputSchema().getChoiceType(References.ENTITY, "ItemFrame");
/* 55 */     OpticFinder<?> debug4 = DSL.namedChoice("ItemFrame", debug3);
/*    */     
/* 57 */     Type<?> debug5 = getInputSchema().getType(References.ENTITY);
/*    */     
/* 59 */     TypeRewriteRule debug6 = fixTypeEverywhereTyped("EntityPaintingFix", debug5, debug3 -> debug3.updateTyped(debug1, debug2, ()));
/*    */ 
/*    */     
/* 62 */     TypeRewriteRule debug7 = fixTypeEverywhereTyped("EntityItemFrameFix", debug5, debug3 -> debug3.updateTyped(debug1, debug2, ()));
/*    */ 
/*    */ 
/*    */     
/* 66 */     return TypeRewriteRule.seq(debug6, debug7);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityPaintingItemFrameDirectionFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */