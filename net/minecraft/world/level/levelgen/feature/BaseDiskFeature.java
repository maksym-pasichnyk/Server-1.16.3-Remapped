/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ 
/*    */ public class BaseDiskFeature extends Feature<DiskConfiguration> {
/*    */   public BaseDiskFeature(Codec<DiskConfiguration> debug1) {
/* 15 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, DiskConfiguration debug5) {
/* 20 */     boolean debug6 = false;
/*    */     
/* 22 */     int debug7 = debug5.radius.sample(debug3);
/* 23 */     for (int debug8 = debug4.getX() - debug7; debug8 <= debug4.getX() + debug7; debug8++) {
/* 24 */       for (int debug9 = debug4.getZ() - debug7; debug9 <= debug4.getZ() + debug7; debug9++) {
/* 25 */         int debug10 = debug8 - debug4.getX();
/* 26 */         int debug11 = debug9 - debug4.getZ();
/* 27 */         if (debug10 * debug10 + debug11 * debug11 <= debug7 * debug7)
/*    */         {
/*    */           
/* 30 */           for (int debug12 = debug4.getY() - debug5.halfHeight; debug12 <= debug4.getY() + debug5.halfHeight; debug12++) {
/* 31 */             BlockPos debug13 = new BlockPos(debug8, debug12, debug9);
/* 32 */             Block debug14 = debug1.getBlockState(debug13).getBlock();
/*    */             
/* 34 */             for (BlockState debug16 : debug5.targets) {
/* 35 */               if (debug16.is(debug14)) {
/* 36 */                 debug1.setBlock(debug13, debug5.state, 2);
/* 37 */                 debug6 = true;
/*    */                 break;
/*    */               } 
/*    */             } 
/*    */           } 
/*    */         }
/*    */       } 
/*    */     } 
/* 45 */     return debug6;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\BaseDiskFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */