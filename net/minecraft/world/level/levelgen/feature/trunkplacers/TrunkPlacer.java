/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.LevelSimulatedRW;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.LevelWriter;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.feature.TreeFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ 
/*    */ public abstract class TrunkPlacer {
/* 25 */   public static final Codec<TrunkPlacer> CODEC = Registry.TRUNK_PLACER_TYPES.dispatch(TrunkPlacer::type, TrunkPlacerType::codec);
/*    */   
/*    */   protected final int baseHeight;
/*    */   protected final int heightRandA;
/*    */   protected final int heightRandB;
/*    */   
/*    */   protected static <P extends TrunkPlacer> Products.P3<RecordCodecBuilder.Mu<P>, Integer, Integer, Integer> trunkPlacerParts(RecordCodecBuilder.Instance<P> debug0) {
/* 32 */     return debug0.group(
/* 33 */         (App)Codec.intRange(0, 32).fieldOf("base_height").forGetter(debug0 -> Integer.valueOf(debug0.baseHeight)), 
/* 34 */         (App)Codec.intRange(0, 24).fieldOf("height_rand_a").forGetter(debug0 -> Integer.valueOf(debug0.heightRandA)), 
/* 35 */         (App)Codec.intRange(0, 24).fieldOf("height_rand_b").forGetter(debug0 -> Integer.valueOf(debug0.heightRandB)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TrunkPlacer(int debug1, int debug2, int debug3) {
/* 43 */     this.baseHeight = debug1;
/* 44 */     this.heightRandA = debug2;
/* 45 */     this.heightRandB = debug3;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getTreeHeight(Random debug1) {
/* 53 */     return this.baseHeight + debug1.nextInt(this.heightRandA + 1) + debug1.nextInt(this.heightRandB + 1);
/*    */   }
/*    */   
/*    */   protected static void setBlock(LevelWriter debug0, BlockPos debug1, BlockState debug2, BoundingBox debug3) {
/* 57 */     TreeFeature.setBlockKnownShape(debug0, debug1, debug2);
/* 58 */     debug3.expand(new BoundingBox((Vec3i)debug1, (Vec3i)debug1));
/*    */   }
/*    */   
/*    */   private static boolean isDirt(LevelSimulatedReader debug0, BlockPos debug1) {
/* 62 */     return debug0.isStateAtPosition(debug1, debug0 -> {
/*    */           Block debug1 = debug0.getBlock();
/* 64 */           return (Feature.isDirt(debug1) && !debug0.is(Blocks.GRASS_BLOCK) && !debug0.is(Blocks.MYCELIUM));
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected static void setDirtAt(LevelSimulatedRW debug0, BlockPos debug1) {
/* 71 */     if (!isDirt((LevelSimulatedReader)debug0, debug1)) {
/* 72 */       TreeFeature.setBlockKnownShape((LevelWriter)debug0, debug1, Blocks.DIRT.defaultBlockState());
/*    */     }
/*    */   }
/*    */   
/*    */   protected static boolean placeLog(LevelSimulatedRW debug0, Random debug1, BlockPos debug2, Set<BlockPos> debug3, BoundingBox debug4, TreeConfiguration debug5) {
/* 77 */     if (TreeFeature.validTreePos((LevelSimulatedReader)debug0, debug2)) {
/* 78 */       setBlock((LevelWriter)debug0, debug2, debug5.trunkProvider.getState(debug1, debug2), debug4);
/* 79 */       debug3.add(debug2.immutable());
/* 80 */       return true;
/*    */     } 
/* 82 */     return false;
/*    */   }
/*    */   
/*    */   protected static void placeLogIfFree(LevelSimulatedRW debug0, Random debug1, BlockPos.MutableBlockPos debug2, Set<BlockPos> debug3, BoundingBox debug4, TreeConfiguration debug5) {
/* 86 */     if (TreeFeature.isFree((LevelSimulatedReader)debug0, (BlockPos)debug2))
/* 87 */       placeLog(debug0, debug1, (BlockPos)debug2, debug3, debug4, debug5); 
/*    */   }
/*    */   
/*    */   protected abstract TrunkPlacerType<?> type();
/*    */   
/*    */   public abstract List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedRW paramLevelSimulatedRW, Random paramRandom, int paramInt, BlockPos paramBlockPos, Set<BlockPos> paramSet, BoundingBox paramBoundingBox, TreeConfiguration paramTreeConfiguration);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\TrunkPlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */