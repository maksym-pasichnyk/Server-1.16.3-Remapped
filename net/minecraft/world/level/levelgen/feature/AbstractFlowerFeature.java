/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ 
/*    */ public abstract class AbstractFlowerFeature<U extends FeatureConfiguration>
/*    */   extends Feature<U>
/*    */ {
/*    */   public AbstractFlowerFeature(Codec<U> debug1) {
/* 17 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, U debug5) {
/* 22 */     BlockState debug6 = getRandomFlower(debug3, debug4, debug5);
/* 23 */     int debug7 = 0;
/* 24 */     for (int debug8 = 0; debug8 < getCount(debug5); debug8++) {
/* 25 */       BlockPos debug9 = getPos(debug3, debug4, debug5);
/*    */       
/* 27 */       if (debug1.isEmptyBlock(debug9) && debug9.getY() < 255 && debug6.canSurvive((LevelReader)debug1, debug9) && isValid((LevelAccessor)debug1, debug9, debug5)) {
/* 28 */         debug1.setBlock(debug9, debug6, 2);
/* 29 */         debug7++;
/*    */       } 
/*    */     } 
/*    */     
/* 33 */     return (debug7 > 0);
/*    */   }
/*    */   
/*    */   public abstract boolean isValid(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, U paramU);
/*    */   
/*    */   public abstract int getCount(U paramU);
/*    */   
/*    */   public abstract BlockPos getPos(Random paramRandom, BlockPos paramBlockPos, U paramU);
/*    */   
/*    */   public abstract BlockState getRandomFlower(Random paramRandom, BlockPos paramBlockPos, U paramU);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\AbstractFlowerFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */