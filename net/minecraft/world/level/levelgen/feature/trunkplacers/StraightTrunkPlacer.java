/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelSimulatedRW;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ 
/*    */ public class StraightTrunkPlacer extends TrunkPlacer {
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.create(debug0 -> trunkPlacerParts(debug0).apply((Applicative)debug0, StraightTrunkPlacer::new));
/*    */   } public static final Codec<StraightTrunkPlacer> CODEC;
/*    */   public StraightTrunkPlacer(int debug1, int debug2, int debug3) {
/* 20 */     super(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TrunkPlacerType<?> type() {
/* 25 */     return TrunkPlacerType.STRAIGHT_TRUNK_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedRW debug1, Random debug2, int debug3, BlockPos debug4, Set<BlockPos> debug5, BoundingBox debug6, TreeConfiguration debug7) {
/* 30 */     setDirtAt(debug1, debug4.below());
/*    */     
/* 32 */     for (int debug8 = 0; debug8 < debug3; debug8++) {
/* 33 */       placeLog(debug1, debug2, debug4.above(debug8), debug5, debug6, debug7);
/*    */     }
/* 35 */     return (List<FoliagePlacer.FoliageAttachment>)ImmutableList.of(new FoliagePlacer.FoliageAttachment(debug4.above(debug3), 0, false));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\StraightTrunkPlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */