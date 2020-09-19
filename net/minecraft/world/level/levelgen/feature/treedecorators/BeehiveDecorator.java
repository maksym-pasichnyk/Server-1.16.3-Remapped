/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.entity.animal.Bee;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.BeehiveBlock;
/*    */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ 
/*    */ public class BeehiveDecorator extends TreeDecorator {
/*    */   public static final Codec<BeehiveDecorator> CODEC;
/*    */   
/*    */   static {
/* 23 */     CODEC = Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(BeehiveDecorator::new, debug0 -> Float.valueOf(debug0.probability)).codec();
/*    */   }
/*    */   private final float probability;
/*    */   
/*    */   public BeehiveDecorator(float debug1) {
/* 28 */     this.probability = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 33 */     return TreeDecoratorType.BEEHIVE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void place(WorldGenLevel debug1, Random debug2, List<BlockPos> debug3, List<BlockPos> debug4, Set<BlockPos> debug5, BoundingBox debug6) {
/* 38 */     if (debug2.nextFloat() >= this.probability) {
/*    */       return;
/*    */     }
/*    */     
/* 42 */     Direction debug7 = BeehiveBlock.getRandomOffset(debug2);
/*    */ 
/*    */ 
/*    */     
/* 46 */     int debug8 = !debug4.isEmpty() ? Math.max(((BlockPos)debug4.get(0)).getY() - 1, ((BlockPos)debug3.get(0)).getY()) : Math.min(((BlockPos)debug3.get(0)).getY() + 1 + debug2.nextInt(3), ((BlockPos)debug3.get(debug3.size() - 1)).getY());
/*    */     
/* 48 */     List<BlockPos> debug9 = (List<BlockPos>)debug3.stream().filter(debug1 -> (debug1.getY() == debug0)).collect(Collectors.toList());
/* 49 */     if (debug9.isEmpty()) {
/*    */       return;
/*    */     }
/* 52 */     BlockPos debug10 = debug9.get(debug2.nextInt(debug9.size()));
/* 53 */     BlockPos debug11 = debug10.relative(debug7);
/* 54 */     if (!Feature.isAir((LevelSimulatedReader)debug1, debug11) || !Feature.isAir((LevelSimulatedReader)debug1, debug11.relative(Direction.SOUTH))) {
/*    */       return;
/*    */     }
/*    */     
/* 58 */     BlockState debug12 = (BlockState)Blocks.BEE_NEST.defaultBlockState().setValue((Property)BeehiveBlock.FACING, (Comparable)Direction.SOUTH);
/* 59 */     setBlock((LevelWriter)debug1, debug11, debug12, debug5, debug6);
/* 60 */     BlockEntity debug13 = debug1.getBlockEntity(debug11);
/* 61 */     if (debug13 instanceof BeehiveBlockEntity) {
/* 62 */       BeehiveBlockEntity debug14 = (BeehiveBlockEntity)debug13;
/* 63 */       int debug15 = 2 + debug2.nextInt(2);
/* 64 */       for (int debug16 = 0; debug16 < debug15; debug16++) {
/* 65 */         Bee debug17 = new Bee(EntityType.BEE, (Level)debug1.getLevel());
/* 66 */         debug14.addOccupantWithPresetTicks((Entity)debug17, false, debug2.nextInt(599));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\BeehiveDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */