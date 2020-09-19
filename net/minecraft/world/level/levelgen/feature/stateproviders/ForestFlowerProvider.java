/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ForestFlowerProvider
/*    */   extends BlockStateProvider {
/* 13 */   public static final Codec<ForestFlowerProvider> CODEC = Codec.unit(() -> INSTANCE);
/*    */   
/* 15 */   private static final BlockState[] FLOWERS = new BlockState[] { Blocks.DANDELION
/* 16 */       .defaultBlockState(), Blocks.POPPY
/* 17 */       .defaultBlockState(), Blocks.ALLIUM
/* 18 */       .defaultBlockState(), Blocks.AZURE_BLUET
/* 19 */       .defaultBlockState(), Blocks.RED_TULIP
/* 20 */       .defaultBlockState(), Blocks.ORANGE_TULIP
/* 21 */       .defaultBlockState(), Blocks.WHITE_TULIP
/* 22 */       .defaultBlockState(), Blocks.PINK_TULIP
/* 23 */       .defaultBlockState(), Blocks.OXEYE_DAISY
/* 24 */       .defaultBlockState(), Blocks.CORNFLOWER
/* 25 */       .defaultBlockState(), Blocks.LILY_OF_THE_VALLEY
/* 26 */       .defaultBlockState() };
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockStateProviderType<?> type() {
/* 31 */     return BlockStateProviderType.FOREST_FLOWER_PROVIDER;
/*    */   }
/*    */   
/* 34 */   public static final ForestFlowerProvider INSTANCE = new ForestFlowerProvider();
/*    */ 
/*    */   
/*    */   public BlockState getState(Random debug1, BlockPos debug2) {
/* 38 */     double debug3 = Mth.clamp((1.0D + Biome.BIOME_INFO_NOISE.getValue(debug2.getX() / 48.0D, debug2.getZ() / 48.0D, false)) / 2.0D, 0.0D, 0.9999D);
/* 39 */     return FLOWERS[(int)(debug3 * FLOWERS.length)];
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\ForestFlowerProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */