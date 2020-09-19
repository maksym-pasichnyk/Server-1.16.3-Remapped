/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.CocoaBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ 
/*    */ public class CocoaDecorator extends TreeDecorator {
/*    */   public static final Codec<CocoaDecorator> CODEC;
/*    */   
/*    */   static {
/* 18 */     CODEC = Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(CocoaDecorator::new, debug0 -> Float.valueOf(debug0.probability)).codec();
/*    */   }
/*    */   private final float probability;
/*    */   
/*    */   public CocoaDecorator(float debug1) {
/* 23 */     this.probability = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 28 */     return TreeDecoratorType.COCOA;
/*    */   }
/*    */ 
/*    */   
/*    */   public void place(WorldGenLevel debug1, Random debug2, List<BlockPos> debug3, List<BlockPos> debug4, Set<BlockPos> debug5, BoundingBox debug6) {
/* 33 */     if (debug2.nextFloat() >= this.probability) {
/*    */       return;
/*    */     }
/*    */     
/* 37 */     int debug7 = ((BlockPos)debug3.get(0)).getY();
/* 38 */     debug3.stream()
/* 39 */       .filter(debug1 -> (debug1.getY() - debug0 <= 2))
/* 40 */       .forEach(debug5 -> {
/*    */           for (Direction debug7 : Direction.Plane.HORIZONTAL) {
/*    */             if (debug1.nextFloat() <= 0.25F) {
/*    */               Direction debug8 = debug7.getOpposite();
/*    */               BlockPos debug9 = debug5.offset(debug8.getStepX(), 0, debug8.getStepZ());
/*    */               if (Feature.isAir((LevelSimulatedReader)debug2, debug9)) {
/*    */                 BlockState debug10 = (BlockState)((BlockState)Blocks.COCOA.defaultBlockState().setValue((Property)CocoaBlock.AGE, Integer.valueOf(debug1.nextInt(3)))).setValue((Property)CocoaBlock.FACING, (Comparable)debug7);
/*    */                 setBlock((LevelWriter)debug2, debug9, debug10, debug3, debug4);
/*    */               } 
/*    */             } 
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\CocoaDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */