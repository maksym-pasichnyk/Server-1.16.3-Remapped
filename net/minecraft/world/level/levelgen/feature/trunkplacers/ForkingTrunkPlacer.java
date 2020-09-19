/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.LevelSimulatedRW;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ 
/*    */ public class ForkingTrunkPlacer extends TrunkPlacer {
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.create(debug0 -> trunkPlacerParts(debug0).apply((Applicative)debug0, ForkingTrunkPlacer::new));
/*    */   } public static final Codec<ForkingTrunkPlacer> CODEC;
/*    */   public ForkingTrunkPlacer(int debug1, int debug2, int debug3) {
/* 21 */     super(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TrunkPlacerType<?> type() {
/* 26 */     return TrunkPlacerType.FORKING_TRUNK_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedRW debug1, Random debug2, int debug3, BlockPos debug4, Set<BlockPos> debug5, BoundingBox debug6, TreeConfiguration debug7) {
/* 31 */     setDirtAt(debug1, debug4.below());
/*    */     
/* 33 */     List<FoliagePlacer.FoliageAttachment> debug8 = Lists.newArrayList();
/*    */     
/* 35 */     Direction debug9 = Direction.Plane.HORIZONTAL.getRandomDirection(debug2);
/* 36 */     int debug10 = debug3 - debug2.nextInt(4) - 1;
/* 37 */     int debug11 = 3 - debug2.nextInt(3);
/*    */     
/* 39 */     BlockPos.MutableBlockPos debug12 = new BlockPos.MutableBlockPos();
/* 40 */     int debug13 = debug4.getX();
/* 41 */     int debug14 = debug4.getZ();
/* 42 */     int debug15 = 0;
/* 43 */     for (int i = 0; i < debug3; i++) {
/* 44 */       int debug17 = debug4.getY() + i;
/* 45 */       if (i >= debug10 && debug11 > 0) {
/* 46 */         debug13 += debug9.getStepX();
/* 47 */         debug14 += debug9.getStepZ();
/* 48 */         debug11--;
/*    */       } 
/* 50 */       if (placeLog(debug1, debug2, (BlockPos)debug12.set(debug13, debug17, debug14), debug5, debug6, debug7)) {
/* 51 */         debug15 = debug17 + 1;
/*    */       }
/*    */     } 
/* 54 */     debug8.add(new FoliagePlacer.FoliageAttachment(new BlockPos(debug13, debug15, debug14), 1, false));
/*    */     
/* 56 */     debug13 = debug4.getX();
/* 57 */     debug14 = debug4.getZ();
/* 58 */     Direction debug16 = Direction.Plane.HORIZONTAL.getRandomDirection(debug2);
/* 59 */     if (debug16 != debug9) {
/* 60 */       int debug17 = debug10 - debug2.nextInt(2) - 1;
/* 61 */       int debug18 = 1 + debug2.nextInt(3);
/*    */       
/* 63 */       debug15 = 0;
/* 64 */       for (int debug19 = debug17; debug19 < debug3 && debug18 > 0; debug19++, debug18--) {
/* 65 */         if (debug19 >= 1) {
/*    */ 
/*    */           
/* 68 */           int debug20 = debug4.getY() + debug19;
/* 69 */           debug13 += debug16.getStepX();
/* 70 */           debug14 += debug16.getStepZ();
/* 71 */           if (placeLog(debug1, debug2, (BlockPos)debug12.set(debug13, debug20, debug14), debug5, debug6, debug7))
/* 72 */             debug15 = debug20 + 1; 
/*    */         } 
/*    */       } 
/* 75 */       if (debug15 > 1) {
/* 76 */         debug8.add(new FoliagePlacer.FoliageAttachment(new BlockPos(debug13, debug15, debug14), 0, false));
/*    */       }
/*    */     } 
/*    */     
/* 80 */     return debug8;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\ForkingTrunkPlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */