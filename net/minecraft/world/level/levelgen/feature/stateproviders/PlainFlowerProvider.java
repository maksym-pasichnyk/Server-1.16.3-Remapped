/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class PlainFlowerProvider
/*    */   extends BlockStateProvider {
/* 13 */   public static final Codec<PlainFlowerProvider> CODEC = Codec.unit(() -> INSTANCE);
/*    */   
/* 15 */   public static final PlainFlowerProvider INSTANCE = new PlainFlowerProvider();
/*    */   
/* 17 */   private static final BlockState[] LOW_NOISE_FLOWERS = new BlockState[] { Blocks.ORANGE_TULIP
/* 18 */       .defaultBlockState(), Blocks.RED_TULIP
/* 19 */       .defaultBlockState(), Blocks.PINK_TULIP
/* 20 */       .defaultBlockState(), Blocks.WHITE_TULIP
/* 21 */       .defaultBlockState() };
/*    */   
/* 23 */   private static final BlockState[] HIGH_NOISE_FLOWERS = new BlockState[] { Blocks.POPPY
/* 24 */       .defaultBlockState(), Blocks.AZURE_BLUET
/* 25 */       .defaultBlockState(), Blocks.OXEYE_DAISY
/* 26 */       .defaultBlockState(), Blocks.CORNFLOWER
/* 27 */       .defaultBlockState() };
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockStateProviderType<?> type() {
/* 32 */     return BlockStateProviderType.PLAIN_FLOWER_PROVIDER;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getState(Random debug1, BlockPos debug2) {
/* 37 */     double debug3 = Biome.BIOME_INFO_NOISE.getValue(debug2.getX() / 200.0D, debug2.getZ() / 200.0D, false);
/* 38 */     if (debug3 < -0.8D) {
/* 39 */       return (BlockState)Util.getRandom((Object[])LOW_NOISE_FLOWERS, debug1);
/*    */     }
/*    */     
/* 42 */     if (debug1.nextInt(3) > 0) {
/* 43 */       return (BlockState)Util.getRandom((Object[])HIGH_NOISE_FLOWERS, debug1);
/*    */     }
/*    */     
/* 46 */     return Blocks.DANDELION.defaultBlockState();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\PlainFlowerProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */