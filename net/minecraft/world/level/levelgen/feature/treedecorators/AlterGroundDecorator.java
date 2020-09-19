/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelSimulatedRW;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public class AlterGroundDecorator extends TreeDecorator {
/*    */   public static final Codec<AlterGroundDecorator> CODEC;
/*    */   
/*    */   static {
/* 17 */     CODEC = BlockStateProvider.CODEC.fieldOf("provider").xmap(AlterGroundDecorator::new, debug0 -> debug0.provider).codec();
/*    */   }
/*    */   private final BlockStateProvider provider;
/*    */   
/*    */   public AlterGroundDecorator(BlockStateProvider debug1) {
/* 22 */     this.provider = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 27 */     return TreeDecoratorType.ALTER_GROUND;
/*    */   }
/*    */ 
/*    */   
/*    */   public void place(WorldGenLevel debug1, Random debug2, List<BlockPos> debug3, List<BlockPos> debug4, Set<BlockPos> debug5, BoundingBox debug6) {
/* 32 */     int debug7 = ((BlockPos)debug3.get(0)).getY();
/* 33 */     debug3.stream().filter(debug1 -> (debug1.getY() == debug0)).forEach(debug3 -> {
/*    */           placeCircle((LevelSimulatedRW)debug1, debug2, debug3.west().north());
/*    */           placeCircle((LevelSimulatedRW)debug1, debug2, debug3.east(2).north());
/*    */           placeCircle((LevelSimulatedRW)debug1, debug2, debug3.west().south(2));
/*    */           placeCircle((LevelSimulatedRW)debug1, debug2, debug3.east(2).south(2));
/*    */           for (int debug4 = 0; debug4 < 5; debug4++) {
/*    */             int debug5 = debug2.nextInt(64);
/*    */             int debug6 = debug5 % 8;
/*    */             int debug7 = debug5 / 8;
/*    */             if (debug6 == 0 || debug6 == 7 || debug7 == 0 || debug7 == 7) {
/*    */               placeCircle((LevelSimulatedRW)debug1, debug2, debug3.offset(-3 + debug6, 0, -3 + debug7));
/*    */             }
/*    */           } 
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   private void placeCircle(LevelSimulatedRW debug1, Random debug2, BlockPos debug3) {
/* 51 */     for (int debug4 = -2; debug4 <= 2; debug4++) {
/* 52 */       for (int debug5 = -2; debug5 <= 2; debug5++) {
/* 53 */         if (Math.abs(debug4) != 2 || Math.abs(debug5) != 2) {
/* 54 */           placeBlockAt(debug1, debug2, debug3.offset(debug4, 0, debug5));
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private void placeBlockAt(LevelSimulatedRW debug1, Random debug2, BlockPos debug3) {
/* 61 */     for (int debug4 = 2; debug4 >= -3; debug4--) {
/* 62 */       BlockPos debug5 = debug3.above(debug4);
/* 63 */       if (Feature.isGrassOrDirt((LevelSimulatedReader)debug1, debug5)) {
/* 64 */         debug1.setBlock(debug5, this.provider.getState(debug2, debug3), 19); break;
/*    */       } 
/* 66 */       if (!Feature.isAir((LevelSimulatedReader)debug1, debug5) && debug4 < 0)
/*    */         break; 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\AlterGroundDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */