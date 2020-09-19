/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.UniformInt;
/*    */ import net.minecraft.world.level.LevelSimulatedRW;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ 
/*    */ public class MegaJungleFoliagePlacer extends FoliagePlacer {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.create(debug0 -> foliagePlacerParts(debug0).and((App)Codec.intRange(0, 16).fieldOf("height").forGetter(())).apply((Applicative)debug0, MegaJungleFoliagePlacer::new));
/*    */   }
/*    */   
/*    */   public static final Codec<MegaJungleFoliagePlacer> CODEC;
/*    */   protected final int height;
/*    */   
/*    */   public MegaJungleFoliagePlacer(UniformInt debug1, UniformInt debug2, int debug3) {
/* 23 */     super(debug1, debug2);
/* 24 */     this.height = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 29 */     return FoliagePlacerType.MEGA_JUNGLE_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedRW debug1, Random debug2, TreeConfiguration debug3, int debug4, FoliagePlacer.FoliageAttachment debug5, int debug6, int debug7, Set<BlockPos> debug8, int debug9, BoundingBox debug10) {
/* 35 */     int debug11 = debug5.doubleTrunk() ? debug6 : (1 + debug2.nextInt(2));
/*    */     
/* 37 */     for (int debug12 = debug9; debug12 >= debug9 - debug11; debug12--) {
/* 38 */       int debug13 = debug7 + debug5.radiusOffset() + 1 - debug12;
/* 39 */       placeLeavesRow(debug1, debug2, debug3, debug5.foliagePos(), debug13, debug8, debug12, debug5.doubleTrunk(), debug10);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int foliageHeight(Random debug1, int debug2, TreeConfiguration debug3) {
/* 45 */     return this.height;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(Random debug1, int debug2, int debug3, int debug4, int debug5, boolean debug6) {
/* 50 */     if (debug2 + debug4 >= 7) {
/* 51 */       return true;
/*    */     }
/* 53 */     return (debug2 * debug2 + debug4 * debug4 > debug5 * debug5);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\MegaJungleFoliagePlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */