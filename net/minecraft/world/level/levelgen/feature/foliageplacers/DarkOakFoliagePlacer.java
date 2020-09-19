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
/*    */ public class DarkOakFoliagePlacer extends FoliagePlacer {
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.create(debug0 -> foliagePlacerParts(debug0).apply((Applicative)debug0, DarkOakFoliagePlacer::new));
/*    */   } public static final Codec<DarkOakFoliagePlacer> CODEC;
/*    */   public DarkOakFoliagePlacer(UniformInt debug1, UniformInt debug2) {
/* 18 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 23 */     return FoliagePlacerType.DARK_OAK_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedRW debug1, Random debug2, TreeConfiguration debug3, int debug4, FoliagePlacer.FoliageAttachment debug5, int debug6, int debug7, Set<BlockPos> debug8, int debug9, BoundingBox debug10) {
/* 28 */     BlockPos debug11 = debug5.foliagePos().above(debug9);
/* 29 */     boolean debug12 = debug5.doubleTrunk();
/*    */     
/* 31 */     if (debug12) {
/* 32 */       placeLeavesRow(debug1, debug2, debug3, debug11, debug7 + 2, debug8, -1, debug12, debug10);
/* 33 */       placeLeavesRow(debug1, debug2, debug3, debug11, debug7 + 3, debug8, 0, debug12, debug10);
/* 34 */       placeLeavesRow(debug1, debug2, debug3, debug11, debug7 + 2, debug8, 1, debug12, debug10);
/* 35 */       if (debug2.nextBoolean()) {
/* 36 */         placeLeavesRow(debug1, debug2, debug3, debug11, debug7, debug8, 2, debug12, debug10);
/*    */       }
/*    */     } else {
/* 39 */       placeLeavesRow(debug1, debug2, debug3, debug11, debug7 + 2, debug8, -1, debug12, debug10);
/* 40 */       placeLeavesRow(debug1, debug2, debug3, debug11, debug7 + 1, debug8, 0, debug12, debug10);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int foliageHeight(Random debug1, int debug2, TreeConfiguration debug3) {
/* 46 */     return 4;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocationSigned(Random debug1, int debug2, int debug3, int debug4, int debug5, boolean debug6) {
/* 51 */     if (debug3 == 0 && debug6 && (
/* 52 */       debug2 == -debug5 || debug2 >= debug5) && (debug4 == -debug5 || debug4 >= debug5)) {
/* 53 */       return true;
/*    */     }
/*    */     
/* 56 */     return super.shouldSkipLocationSigned(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(Random debug1, int debug2, int debug3, int debug4, int debug5, boolean debug6) {
/* 61 */     if (debug3 == -1 && !debug6) {
/* 62 */       return (debug2 == debug5 && debug4 == debug5);
/*    */     }
/* 64 */     if (debug3 == 1) {
/* 65 */       return (debug2 + debug4 > debug5 * 2 - 2);
/*    */     }
/* 67 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\DarkOakFoliagePlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */