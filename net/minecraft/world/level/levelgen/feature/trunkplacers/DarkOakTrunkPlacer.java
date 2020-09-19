/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ import com.google.common.collect.Lists;
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
/*    */ public class DarkOakTrunkPlacer extends TrunkPlacer {
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.create(debug0 -> trunkPlacerParts(debug0).apply((Applicative)debug0, DarkOakTrunkPlacer::new));
/*    */   } public static final Codec<DarkOakTrunkPlacer> CODEC;
/*    */   public DarkOakTrunkPlacer(int debug1, int debug2, int debug3) {
/* 22 */     super(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TrunkPlacerType<?> type() {
/* 27 */     return TrunkPlacerType.DARK_OAK_TRUNK_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedRW debug1, Random debug2, int debug3, BlockPos debug4, Set<BlockPos> debug5, BoundingBox debug6, TreeConfiguration debug7) {
/* 32 */     List<FoliagePlacer.FoliageAttachment> debug8 = Lists.newArrayList();
/*    */     
/* 34 */     BlockPos debug9 = debug4.below();
/* 35 */     setDirtAt(debug1, debug9);
/* 36 */     setDirtAt(debug1, debug9.east());
/* 37 */     setDirtAt(debug1, debug9.south());
/* 38 */     setDirtAt(debug1, debug9.south().east());
/*    */     
/* 40 */     Direction debug10 = Direction.Plane.HORIZONTAL.getRandomDirection(debug2);
/* 41 */     int debug11 = debug3 - debug2.nextInt(4);
/* 42 */     int debug12 = 2 - debug2.nextInt(3);
/*    */     
/* 44 */     int debug13 = debug4.getX();
/* 45 */     int debug14 = debug4.getY();
/* 46 */     int debug15 = debug4.getZ();
/*    */     
/* 48 */     int debug16 = debug13;
/* 49 */     int debug17 = debug15;
/* 50 */     int debug18 = debug14 + debug3 - 1;
/*    */     
/*    */     int debug19;
/* 53 */     for (debug19 = 0; debug19 < debug3; debug19++) {
/* 54 */       if (debug19 >= debug11 && debug12 > 0) {
/* 55 */         debug16 += debug10.getStepX();
/* 56 */         debug17 += debug10.getStepZ();
/* 57 */         debug12--;
/*    */       } 
/*    */       
/* 60 */       int debug20 = debug14 + debug19;
/* 61 */       BlockPos debug21 = new BlockPos(debug16, debug20, debug17);
/* 62 */       if (TreeFeature.isAirOrLeaves((LevelSimulatedReader)debug1, debug21)) {
/* 63 */         placeLog(debug1, debug2, debug21, debug5, debug6, debug7);
/* 64 */         placeLog(debug1, debug2, debug21.east(), debug5, debug6, debug7);
/* 65 */         placeLog(debug1, debug2, debug21.south(), debug5, debug6, debug7);
/* 66 */         placeLog(debug1, debug2, debug21.east().south(), debug5, debug6, debug7);
/*    */       } 
/*    */     } 
/*    */     
/* 70 */     debug8.add(new FoliagePlacer.FoliageAttachment(new BlockPos(debug16, debug18, debug17), 0, true));
/*    */ 
/*    */     
/* 73 */     for (debug19 = -1; debug19 <= 2; debug19++) {
/* 74 */       for (int debug20 = -1; debug20 <= 2; debug20++) {
/* 75 */         if (debug19 < 0 || debug19 > 1 || debug20 < 0 || debug20 > 1)
/*    */         {
/*    */           
/* 78 */           if (debug2.nextInt(3) <= 0) {
/*    */ 
/*    */             
/* 81 */             int debug21 = debug2.nextInt(3) + 2;
/* 82 */             for (int debug22 = 0; debug22 < debug21; debug22++) {
/* 83 */               placeLog(debug1, debug2, new BlockPos(debug13 + debug19, debug18 - debug22 - 1, debug15 + debug20), debug5, debug6, debug7);
/*    */             }
/*    */             
/* 86 */             debug8.add(new FoliagePlacer.FoliageAttachment(new BlockPos(debug16 + debug19, debug18, debug17 + debug20), 0, false));
/*    */           }  } 
/*    */       } 
/*    */     } 
/* 90 */     return debug8;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\DarkOakTrunkPlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */