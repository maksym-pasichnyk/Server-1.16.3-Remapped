/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.IntStream;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ 
/*    */ public class NoiseBasedDecorator extends SimpleFeatureDecorator<NoiseCountFactorDecoratorConfiguration> {
/*    */   public NoiseBasedDecorator(Codec<NoiseCountFactorDecoratorConfiguration> debug1) {
/* 13 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> place(Random debug1, NoiseCountFactorDecoratorConfiguration debug2, BlockPos debug3) {
/* 18 */     double debug4 = Biome.BIOME_INFO_NOISE.getValue(debug3.getX() / debug2.noiseFactor, debug3.getZ() / debug2.noiseFactor, false);
/* 19 */     int debug6 = (int)Math.ceil((debug4 + debug2.noiseOffset) * debug2.noiseToCountRatio);
/* 20 */     return IntStream.range(0, debug6).mapToObj(debug1 -> debug0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\NoiseBasedDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */