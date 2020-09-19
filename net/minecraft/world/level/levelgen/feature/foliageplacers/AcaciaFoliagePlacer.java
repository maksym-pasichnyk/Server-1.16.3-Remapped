/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.UniformInt;
/*    */ import net.minecraft.world.level.LevelSimulatedRW;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ 
/*    */ public class AcaciaFoliagePlacer extends FoliagePlacer {
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.create(debug0 -> foliagePlacerParts(debug0).apply((Applicative)debug0, AcaciaFoliagePlacer::new));
/*    */   } public static final Codec<AcaciaFoliagePlacer> CODEC;
/*    */   public AcaciaFoliagePlacer(UniformInt debug1, UniformInt debug2) {
/* 18 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 23 */     return FoliagePlacerType.ACACIA_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedRW debug1, Random debug2, TreeConfiguration debug3, int debug4, FoliagePlacer.FoliageAttachment debug5, int debug6, int debug7, Set<BlockPos> debug8, int debug9, BoundingBox debug10) {
/* 28 */     boolean debug11 = debug5.doubleTrunk();
/* 29 */     BlockPos debug12 = debug5.foliagePos().above(debug9);
/*    */     
/* 31 */     placeLeavesRow(debug1, debug2, debug3, debug12, debug7 + debug5.radiusOffset(), debug8, -1 - debug6, debug11, debug10);
/* 32 */     placeLeavesRow(debug1, debug2, debug3, debug12, debug7 - 1, debug8, -debug6, debug11, debug10);
/* 33 */     placeLeavesRow(debug1, debug2, debug3, debug12, debug7 + debug5.radiusOffset() - 1, debug8, 0, debug11, debug10);
/*    */   }
/*    */ 
/*    */   
/*    */   public int foliageHeight(Random debug1, int debug2, TreeConfiguration debug3) {
/* 38 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(Random debug1, int debug2, int debug3, int debug4, int debug5, boolean debug6) {
/* 43 */     if (debug3 == 0)
/*    */     {
/* 45 */       return ((debug2 > 1 || debug4 > 1) && debug2 != 0 && debug4 != 0);
/*    */     }
/* 47 */     return (debug2 == debug5 && debug4 == debug5 && debug5 > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\AcaciaFoliagePlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */