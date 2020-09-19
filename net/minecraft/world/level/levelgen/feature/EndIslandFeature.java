/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.LevelWriter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class EndIslandFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public EndIslandFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 15 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, NoneFeatureConfiguration debug5) {
/* 20 */     float debug6 = (debug3.nextInt(3) + 4);
/* 21 */     int debug7 = 0;
/* 22 */     while (debug6 > 0.5F) {
/* 23 */       for (int debug8 = Mth.floor(-debug6); debug8 <= Mth.ceil(debug6); debug8++) {
/* 24 */         for (int debug9 = Mth.floor(-debug6); debug9 <= Mth.ceil(debug6); debug9++) {
/* 25 */           if ((debug8 * debug8 + debug9 * debug9) <= (debug6 + 1.0F) * (debug6 + 1.0F)) {
/* 26 */             setBlock((LevelWriter)debug1, debug4.offset(debug8, debug7, debug9), Blocks.END_STONE.defaultBlockState());
/*    */           }
/*    */         } 
/*    */       } 
/* 30 */       debug6 = (float)(debug6 - debug3.nextInt(2) + 0.5D);
/* 31 */       debug7--;
/*    */     } 
/*    */     
/* 34 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\EndIslandFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */