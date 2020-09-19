/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class JigsawRotationFix extends DataFix {
/* 14 */   private static final Map<String, String> renames = (Map<String, String>)ImmutableMap.builder()
/* 15 */     .put("down", "down_south")
/* 16 */     .put("up", "up_north")
/* 17 */     .put("north", "north_up")
/* 18 */     .put("south", "south_up")
/* 19 */     .put("west", "west_up")
/* 20 */     .put("east", "east_up")
/* 21 */     .build();
/*    */   
/*    */   public JigsawRotationFix(Schema debug1, boolean debug2) {
/* 24 */     super(debug1, debug2);
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fix(Dynamic<?> debug0) {
/* 28 */     Optional<String> debug1 = debug0.get("Name").asString().result();
/* 29 */     if (debug1.equals(Optional.of("minecraft:jigsaw"))) {
/* 30 */       return debug0.update("Properties", debug0 -> {
/*    */             String debug1 = debug0.get("facing").asString("north");
/*    */             
/*    */             return debug0.remove("facing").set("orientation", debug0.createString(renames.getOrDefault(debug1, debug1)));
/*    */           });
/*    */     }
/*    */     
/* 37 */     return debug0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 42 */     return fixTypeEverywhereTyped("jigsaw_rotation_fix", getInputSchema().getType(References.BLOCK_STATE), debug0 -> debug0.update(DSL.remainderFinder(), JigsawRotationFix::fix));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\JigsawRotationFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */