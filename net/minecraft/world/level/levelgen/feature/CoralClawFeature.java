/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class CoralClawFeature
/*    */   extends CoralFeature {
/*    */   public CoralClawFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 18 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean placeFeature(LevelAccessor debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 23 */     if (!placeCoralBlock(debug1, debug2, debug3, debug4)) {
/* 24 */       return false;
/*    */     }
/*    */     
/* 27 */     Direction debug5 = Direction.Plane.HORIZONTAL.getRandomDirection(debug2);
/* 28 */     int debug6 = debug2.nextInt(2) + 2;
/*    */     
/* 30 */     List<Direction> debug7 = Lists.newArrayList((Object[])new Direction[] { debug5, debug5.getClockWise(), debug5.getCounterClockWise() });
/* 31 */     Collections.shuffle(debug7, debug2);
/* 32 */     List<Direction> debug8 = debug7.subList(0, debug6);
/*    */     
/* 34 */     for (Direction debug10 : debug8) {
/* 35 */       int debug13; Direction debug14; BlockPos.MutableBlockPos debug11 = debug3.mutable();
/* 36 */       int debug12 = debug2.nextInt(2) + 1;
/*    */ 
/*    */ 
/*    */       
/* 40 */       debug11.move(debug10);
/* 41 */       if (debug10 == debug5) {
/* 42 */         debug14 = debug5;
/* 43 */         debug13 = debug2.nextInt(3) + 2;
/*    */       } else {
/* 45 */         debug11.move(Direction.UP);
/*    */ 
/*    */         
/* 48 */         Direction[] arrayOfDirection = { debug10, Direction.UP };
/* 49 */         debug14 = (Direction)Util.getRandom((Object[])arrayOfDirection, debug2);
/* 50 */         debug13 = debug2.nextInt(3) + 3;
/*    */       } 
/*    */       int debug15;
/* 53 */       for (debug15 = 0; debug15 < debug12 && 
/* 54 */         placeCoralBlock(debug1, debug2, (BlockPos)debug11, debug4); debug15++)
/*    */       {
/*    */         
/* 57 */         debug11.move(debug14);
/*    */       }
/* 59 */       debug11.move(debug14.getOpposite());
/* 60 */       debug11.move(Direction.UP);
/*    */       
/* 62 */       for (debug15 = 0; debug15 < debug13; debug15++) {
/* 63 */         debug11.move(debug5);
/* 64 */         if (!placeCoralBlock(debug1, debug2, (BlockPos)debug11, debug4)) {
/*    */           break;
/*    */         }
/*    */         
/* 68 */         if (debug2.nextFloat() < 0.25F) {
/* 69 */           debug11.move(Direction.UP);
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 74 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\CoralClawFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */