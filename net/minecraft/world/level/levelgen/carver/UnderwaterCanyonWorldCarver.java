/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.BitSet;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*    */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*    */ 
/*    */ public class UnderwaterCanyonWorldCarver extends CanyonWorldCarver {
/*    */   public UnderwaterCanyonWorldCarver(Codec<ProbabilityFeatureConfiguration> debug1) {
/* 18 */     super(debug1);
/* 19 */     this.replaceableBlocks = (Set<Block>)ImmutableSet.of(Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.DIRT, Blocks.COARSE_DIRT, (Object[])new Block[] { Blocks.PODZOL, Blocks.GRASS_BLOCK, Blocks.TERRACOTTA, Blocks.WHITE_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA, Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA, Blocks.LIME_TERRACOTTA, Blocks.PINK_TERRACOTTA, Blocks.GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA, Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.BLUE_TERRACOTTA, Blocks.BROWN_TERRACOTTA, Blocks.GREEN_TERRACOTTA, Blocks.RED_TERRACOTTA, Blocks.BLACK_TERRACOTTA, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.MYCELIUM, Blocks.SNOW, Blocks.SAND, Blocks.GRAVEL, Blocks.WATER, Blocks.LAVA, Blocks.OBSIDIAN, Blocks.AIR, Blocks.CAVE_AIR });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean hasWater(ChunkAccess debug1, int debug2, int debug3, int debug4, int debug5, int debug6, int debug7, int debug8, int debug9) {
/* 61 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean carveBlock(ChunkAccess debug1, Function<BlockPos, Biome> debug2, BitSet debug3, Random debug4, BlockPos.MutableBlockPos debug5, BlockPos.MutableBlockPos debug6, BlockPos.MutableBlockPos debug7, int debug8, int debug9, int debug10, int debug11, int debug12, int debug13, int debug14, int debug15, MutableBoolean debug16) {
/* 66 */     return UnderwaterCaveWorldCarver.carveBlock(this, debug1, debug3, debug4, debug5, debug8, debug9, debug10, debug11, debug12, debug13, debug14, debug15);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\carver\UnderwaterCanyonWorldCarver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */