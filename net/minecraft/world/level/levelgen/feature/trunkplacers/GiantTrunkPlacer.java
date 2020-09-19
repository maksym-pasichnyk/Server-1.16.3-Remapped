/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.LevelSimulatedRW;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ 
/*    */ public class GiantTrunkPlacer extends TrunkPlacer {
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.create(debug0 -> trunkPlacerParts(debug0).apply((Applicative)debug0, GiantTrunkPlacer::new));
/*    */   } public static final Codec<GiantTrunkPlacer> CODEC;
/*    */   public GiantTrunkPlacer(int debug1, int debug2, int debug3) {
/* 20 */     super(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TrunkPlacerType<?> type() {
/* 25 */     return TrunkPlacerType.GIANT_TRUNK_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedRW debug1, Random debug2, int debug3, BlockPos debug4, Set<BlockPos> debug5, BoundingBox debug6, TreeConfiguration debug7) {
/* 30 */     BlockPos debug8 = debug4.below();
/* 31 */     setDirtAt(debug1, debug8);
/* 32 */     setDirtAt(debug1, debug8.east());
/* 33 */     setDirtAt(debug1, debug8.south());
/* 34 */     setDirtAt(debug1, debug8.south().east());
/*    */     
/* 36 */     BlockPos.MutableBlockPos debug9 = new BlockPos.MutableBlockPos();
/*    */     
/* 38 */     for (int debug10 = 0; debug10 < debug3; debug10++) {
/* 39 */       placeLogIfFreeWithOffset(debug1, debug2, debug9, debug5, debug6, debug7, debug4, 0, debug10, 0);
/*    */       
/* 41 */       if (debug10 < debug3 - 1) {
/* 42 */         placeLogIfFreeWithOffset(debug1, debug2, debug9, debug5, debug6, debug7, debug4, 1, debug10, 0);
/*    */         
/* 44 */         placeLogIfFreeWithOffset(debug1, debug2, debug9, debug5, debug6, debug7, debug4, 1, debug10, 1);
/*    */         
/* 46 */         placeLogIfFreeWithOffset(debug1, debug2, debug9, debug5, debug6, debug7, debug4, 0, debug10, 1);
/*    */       } 
/*    */     } 
/*    */     
/* 50 */     return (List<FoliagePlacer.FoliageAttachment>)ImmutableList.of(new FoliagePlacer.FoliageAttachment(debug4.above(debug3), 0, true));
/*    */   }
/*    */   
/*    */   private static void placeLogIfFreeWithOffset(LevelSimulatedRW debug0, Random debug1, BlockPos.MutableBlockPos debug2, Set<BlockPos> debug3, BoundingBox debug4, TreeConfiguration debug5, BlockPos debug6, int debug7, int debug8, int debug9) {
/* 54 */     debug2.setWithOffset((Vec3i)debug6, debug7, debug8, debug9);
/* 55 */     placeLogIfFree(debug0, debug1, debug2, debug3, debug4, debug5);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\GiantTrunkPlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */