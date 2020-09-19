/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.UniformInt;
/*    */ import net.minecraft.world.level.LevelSimulatedRW;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ 
/*    */ public class BushFoliagePlacer extends BlobFoliagePlacer {
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.create(debug0 -> blobParts(debug0).apply((Applicative)debug0, BushFoliagePlacer::new));
/*    */   } public static final Codec<BushFoliagePlacer> CODEC;
/*    */   public BushFoliagePlacer(UniformInt debug1, UniformInt debug2, int debug3) {
/* 18 */     super(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 23 */     return FoliagePlacerType.BUSH_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedRW debug1, Random debug2, TreeConfiguration debug3, int debug4, FoliagePlacer.FoliageAttachment debug5, int debug6, int debug7, Set<BlockPos> debug8, int debug9, BoundingBox debug10) {
/* 28 */     for (int debug11 = debug9; debug11 >= debug9 - debug6; debug11--) {
/* 29 */       int debug12 = debug7 + debug5.radiusOffset() - 1 - debug11;
/* 30 */       placeLeavesRow(debug1, debug2, debug3, debug5.foliagePos(), debug12, debug8, debug11, debug5.doubleTrunk(), debug10);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(Random debug1, int debug2, int debug3, int debug4, int debug5, boolean debug6) {
/* 36 */     return (debug2 == debug5 && debug4 == debug5 && debug1.nextInt(2) == 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\BushFoliagePlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */