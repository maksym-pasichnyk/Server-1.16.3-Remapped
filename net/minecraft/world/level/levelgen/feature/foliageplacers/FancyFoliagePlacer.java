/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.UniformInt;
/*    */ import net.minecraft.world.level.LevelSimulatedRW;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ 
/*    */ public class FancyFoliagePlacer extends BlobFoliagePlacer {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.create(debug0 -> blobParts(debug0).apply((Applicative)debug0, FancyFoliagePlacer::new));
/*    */   } public static final Codec<FancyFoliagePlacer> CODEC;
/*    */   public FancyFoliagePlacer(UniformInt debug1, UniformInt debug2, int debug3) {
/* 19 */     super(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 24 */     return FoliagePlacerType.FANCY_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedRW debug1, Random debug2, TreeConfiguration debug3, int debug4, FoliagePlacer.FoliageAttachment debug5, int debug6, int debug7, Set<BlockPos> debug8, int debug9, BoundingBox debug10) {
/* 29 */     for (int debug11 = debug9; debug11 >= debug9 - debug6; debug11--) {
/* 30 */       int debug12 = debug7 + ((debug11 == debug9 || debug11 == debug9 - debug6) ? 0 : 1);
/* 31 */       placeLeavesRow(debug1, debug2, debug3, debug5.foliagePos(), debug12, debug8, debug11, debug5.doubleTrunk(), debug10);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(Random debug1, int debug2, int debug3, int debug4, int debug5, boolean debug6) {
/* 37 */     return (Mth.square(debug2 + 0.5F) + Mth.square(debug4 + 0.5F) > (debug5 * debug5));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\FancyFoliagePlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */