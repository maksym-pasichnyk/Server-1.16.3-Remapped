/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.SnowyDirtBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class SnowAndFreezeFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public SnowAndFreezeFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 20 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, NoneFeatureConfiguration debug5) {
/* 25 */     BlockPos.MutableBlockPos debug6 = new BlockPos.MutableBlockPos();
/* 26 */     BlockPos.MutableBlockPos debug7 = new BlockPos.MutableBlockPos();
/*    */     
/* 28 */     for (int debug8 = 0; debug8 < 16; debug8++) {
/* 29 */       for (int debug9 = 0; debug9 < 16; debug9++) {
/* 30 */         int debug10 = debug4.getX() + debug8;
/* 31 */         int debug11 = debug4.getZ() + debug9;
/* 32 */         int debug12 = debug1.getHeight(Heightmap.Types.MOTION_BLOCKING, debug10, debug11);
/*    */         
/* 34 */         debug6.set(debug10, debug12, debug11);
/* 35 */         debug7.set((Vec3i)debug6).move(Direction.DOWN, 1);
/*    */         
/* 37 */         Biome debug13 = debug1.getBiome((BlockPos)debug6);
/*    */         
/* 39 */         if (debug13.shouldFreeze((LevelReader)debug1, (BlockPos)debug7, false)) {
/* 40 */           debug1.setBlock((BlockPos)debug7, Blocks.ICE.defaultBlockState(), 2);
/*    */         }
/* 42 */         if (debug13.shouldSnow((LevelReader)debug1, (BlockPos)debug6)) {
/* 43 */           debug1.setBlock((BlockPos)debug6, Blocks.SNOW.defaultBlockState(), 2);
/*    */           
/* 45 */           BlockState debug14 = debug1.getBlockState((BlockPos)debug7);
/* 46 */           if (debug14.hasProperty((Property)SnowyDirtBlock.SNOWY)) {
/* 47 */             debug1.setBlock((BlockPos)debug7, (BlockState)debug14.setValue((Property)SnowyDirtBlock.SNOWY, Boolean.valueOf(true)), 2);
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/* 52 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\SnowAndFreezeFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */