/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.LevelSimulatedRW;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ 
/*    */ public class MegaJungleTrunkPlacer extends GiantTrunkPlacer {
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.create(debug0 -> trunkPlacerParts(debug0).apply((Applicative)debug0, MegaJungleTrunkPlacer::new));
/*    */   } public static final Codec<MegaJungleTrunkPlacer> CODEC;
/*    */   public MegaJungleTrunkPlacer(int debug1, int debug2, int debug3) {
/* 21 */     super(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TrunkPlacerType<?> type() {
/* 26 */     return TrunkPlacerType.MEGA_JUNGLE_TRUNK_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedRW debug1, Random debug2, int debug3, BlockPos debug4, Set<BlockPos> debug5, BoundingBox debug6, TreeConfiguration debug7) {
/* 31 */     List<FoliagePlacer.FoliageAttachment> debug8 = Lists.newArrayList();
/* 32 */     debug8.addAll(super.placeTrunk(debug1, debug2, debug3, debug4, debug5, debug6, debug7));
/*    */     
/*    */     int debug9;
/* 35 */     for (debug9 = debug3 - 2 - debug2.nextInt(4); debug9 > debug3 / 2; debug9 -= 2 + debug2.nextInt(4)) {
/* 36 */       float debug10 = debug2.nextFloat() * 6.2831855F;
/* 37 */       int debug11 = 0;
/* 38 */       int debug12 = 0;
/*    */       
/* 40 */       for (int debug13 = 0; debug13 < 5; debug13++) {
/* 41 */         debug11 = (int)(1.5F + Mth.cos(debug10) * debug13);
/* 42 */         debug12 = (int)(1.5F + Mth.sin(debug10) * debug13);
/* 43 */         BlockPos debug14 = debug4.offset(debug11, debug9 - 3 + debug13 / 2, debug12);
/* 44 */         placeLog(debug1, debug2, debug14, debug5, debug6, debug7);
/*    */       } 
/*    */       
/* 47 */       debug8.add(new FoliagePlacer.FoliageAttachment(debug4.offset(debug11, debug9, debug12), -2, false));
/*    */     } 
/*    */     
/* 50 */     return debug8;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\MegaJungleTrunkPlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */