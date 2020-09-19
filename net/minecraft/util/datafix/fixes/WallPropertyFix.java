/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class WallPropertyFix extends DataFix {
/* 14 */   private static final Set<String> WALL_BLOCKS = (Set<String>)ImmutableSet.of("minecraft:andesite_wall", "minecraft:brick_wall", "minecraft:cobblestone_wall", "minecraft:diorite_wall", "minecraft:end_stone_brick_wall", "minecraft:granite_wall", (Object[])new String[] { "minecraft:mossy_cobblestone_wall", "minecraft:mossy_stone_brick_wall", "minecraft:nether_brick_wall", "minecraft:prismarine_wall", "minecraft:red_nether_brick_wall", "minecraft:red_sandstone_wall", "minecraft:sandstone_wall", "minecraft:stone_brick_wall" });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WallPropertyFix(Schema debug1, boolean debug2) {
/* 33 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 38 */     return fixTypeEverywhereTyped("WallPropertyFix", getInputSchema().getType(References.BLOCK_STATE), debug0 -> debug0.update(DSL.remainderFinder(), WallPropertyFix::upgradeBlockStateTag));
/*    */   }
/*    */   
/*    */   private static String mapProperty(String debug0) {
/* 42 */     return "true".equals(debug0) ? "low" : "none";
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> fixWallProperty(Dynamic<T> debug0, String debug1) {
/* 46 */     return debug0.update(debug1, debug0 -> (Dynamic)DataFixUtils.orElse(debug0.asString().result().map(WallPropertyFix::mapProperty).map(debug0::createString), debug0));
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> upgradeBlockStateTag(Dynamic<T> debug0) {
/* 50 */     boolean debug1 = debug0.get("Name").asString().result().filter(WALL_BLOCKS::contains).isPresent();
/* 51 */     if (!debug1) {
/* 52 */       return debug0;
/*    */     }
/*    */     
/* 55 */     return debug0.update("Properties", debug0 -> {
/*    */           Dynamic<?> debug1 = fixWallProperty(debug0, "east");
/*    */           debug1 = fixWallProperty(debug1, "west");
/*    */           debug1 = fixWallProperty(debug1, "north");
/*    */           return fixWallProperty(debug1, "south");
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\WallPropertyFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */