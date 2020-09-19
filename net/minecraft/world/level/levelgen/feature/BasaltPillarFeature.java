/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class BasaltPillarFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public BasaltPillarFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 19 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, NoneFeatureConfiguration debug5) {
/* 24 */     if (!debug1.isEmptyBlock(debug4) || debug1.isEmptyBlock(debug4.above())) {
/* 25 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 29 */     BlockPos.MutableBlockPos debug6 = debug4.mutable();
/* 30 */     BlockPos.MutableBlockPos debug7 = debug4.mutable();
/* 31 */     boolean debug8 = true;
/* 32 */     boolean debug9 = true;
/* 33 */     boolean debug10 = true;
/* 34 */     boolean debug11 = true;
/*    */     
/* 36 */     while (debug1.isEmptyBlock((BlockPos)debug6)) {
/* 37 */       if (Level.isOutsideBuildHeight((BlockPos)debug6)) {
/* 38 */         return true;
/*    */       }
/*    */       
/* 41 */       debug1.setBlock((BlockPos)debug6, Blocks.BASALT.defaultBlockState(), 2);
/*    */       
/* 43 */       debug8 = (debug8 && placeHangOff((LevelAccessor)debug1, debug3, (BlockPos)debug7.setWithOffset((Vec3i)debug6, Direction.NORTH)));
/* 44 */       debug9 = (debug9 && placeHangOff((LevelAccessor)debug1, debug3, (BlockPos)debug7.setWithOffset((Vec3i)debug6, Direction.SOUTH)));
/* 45 */       debug10 = (debug10 && placeHangOff((LevelAccessor)debug1, debug3, (BlockPos)debug7.setWithOffset((Vec3i)debug6, Direction.WEST)));
/* 46 */       debug11 = (debug11 && placeHangOff((LevelAccessor)debug1, debug3, (BlockPos)debug7.setWithOffset((Vec3i)debug6, Direction.EAST)));
/*    */       
/* 48 */       debug6.move(Direction.DOWN);
/*    */     } 
/*    */ 
/*    */     
/* 52 */     debug6.move(Direction.UP);
/* 53 */     placeBaseHangOff((LevelAccessor)debug1, debug3, (BlockPos)debug7.setWithOffset((Vec3i)debug6, Direction.NORTH));
/* 54 */     placeBaseHangOff((LevelAccessor)debug1, debug3, (BlockPos)debug7.setWithOffset((Vec3i)debug6, Direction.SOUTH));
/* 55 */     placeBaseHangOff((LevelAccessor)debug1, debug3, (BlockPos)debug7.setWithOffset((Vec3i)debug6, Direction.WEST));
/* 56 */     placeBaseHangOff((LevelAccessor)debug1, debug3, (BlockPos)debug7.setWithOffset((Vec3i)debug6, Direction.EAST));
/* 57 */     debug6.move(Direction.DOWN);
/*    */     
/* 59 */     BlockPos.MutableBlockPos debug12 = new BlockPos.MutableBlockPos();
/* 60 */     for (int debug13 = -3; debug13 < 4; debug13++) {
/* 61 */       for (int debug14 = -3; debug14 < 4; debug14++) {
/* 62 */         int debug15 = Mth.abs(debug13) * Mth.abs(debug14);
/* 63 */         if (debug3.nextInt(10) < 10 - debug15) {
/*    */ 
/*    */ 
/*    */           
/* 67 */           debug12.set((Vec3i)debug6.offset(debug13, 0, debug14));
/* 68 */           int debug16 = 3;
/* 69 */           while (debug1.isEmptyBlock((BlockPos)debug7.setWithOffset((Vec3i)debug12, Direction.DOWN))) {
/* 70 */             debug12.move(Direction.DOWN);
/* 71 */             debug16--;
/* 72 */             if (debug16 <= 0) {
/*    */               break;
/*    */             }
/*    */           } 
/*    */           
/* 77 */           if (!debug1.isEmptyBlock((BlockPos)debug7.setWithOffset((Vec3i)debug12, Direction.DOWN))) {
/* 78 */             debug1.setBlock((BlockPos)debug12, Blocks.BASALT.defaultBlockState(), 2);
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/* 83 */     return true;
/*    */   }
/*    */   
/*    */   private void placeBaseHangOff(LevelAccessor debug1, Random debug2, BlockPos debug3) {
/* 87 */     if (debug2.nextBoolean()) {
/* 88 */       debug1.setBlock(debug3, Blocks.BASALT.defaultBlockState(), 2);
/*    */     }
/*    */   }
/*    */   
/*    */   private boolean placeHangOff(LevelAccessor debug1, Random debug2, BlockPos debug3) {
/* 93 */     if (debug2.nextInt(10) != 0) {
/* 94 */       debug1.setBlock(debug3, Blocks.BASALT.defaultBlockState(), 2);
/* 95 */       return true;
/*    */     } 
/*    */     
/* 98 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\BasaltPillarFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */