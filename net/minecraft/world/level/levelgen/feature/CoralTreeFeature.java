/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class CoralTreeFeature extends CoralFeature {
/*    */   public CoralTreeFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 17 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean placeFeature(LevelAccessor debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 22 */     BlockPos.MutableBlockPos debug5 = debug3.mutable();
/*    */     
/* 24 */     int debug6 = debug2.nextInt(3) + 1;
/* 25 */     for (int i = 0; i < debug6; i++) {
/* 26 */       if (!placeCoralBlock(debug1, debug2, (BlockPos)debug5, debug4)) {
/* 27 */         return true;
/*    */       }
/* 29 */       debug5.move(Direction.UP);
/*    */     } 
/* 31 */     BlockPos debug7 = debug5.immutable();
/*    */     
/* 33 */     int debug8 = debug2.nextInt(3) + 2;
/* 34 */     List<Direction> debug9 = Lists.newArrayList((Iterable)Direction.Plane.HORIZONTAL);
/* 35 */     Collections.shuffle(debug9, debug2);
/* 36 */     List<Direction> debug10 = debug9.subList(0, debug8);
/*    */     
/* 38 */     for (Direction debug12 : debug10) {
/* 39 */       debug5.set((Vec3i)debug7);
/* 40 */       debug5.move(debug12);
/*    */       
/* 42 */       int debug13 = debug2.nextInt(5) + 2;
/* 43 */       int debug14 = 0;
/* 44 */       for (int debug15 = 0; debug15 < debug13 && 
/* 45 */         placeCoralBlock(debug1, debug2, (BlockPos)debug5, debug4); debug15++) {
/*    */ 
/*    */         
/* 48 */         debug14++;
/* 49 */         debug5.move(Direction.UP);
/*    */         
/* 51 */         if (debug15 == 0 || (debug14 >= 2 && debug2.nextFloat() < 0.25F)) {
/* 52 */           debug5.move(debug12);
/* 53 */           debug14 = 0;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 58 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\CoralTreeFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */