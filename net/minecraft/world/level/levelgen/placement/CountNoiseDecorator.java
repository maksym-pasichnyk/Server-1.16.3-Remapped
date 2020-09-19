/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.IntStream;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoiseDependantDecoratorConfiguration;
/*    */ 
/*    */ public class CountNoiseDecorator extends FeatureDecorator<NoiseDependantDecoratorConfiguration> {
/*    */   public CountNoiseDecorator(Codec<NoiseDependantDecoratorConfiguration> debug1) {
/* 14 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(DecorationContext debug1, Random debug2, NoiseDependantDecoratorConfiguration debug3, BlockPos debug4) {
/* 19 */     double debug5 = Biome.BIOME_INFO_NOISE.getValue(debug4.getX() / 200.0D, debug4.getZ() / 200.0D, false);
/* 20 */     int debug7 = (debug5 < debug3.noiseLevel) ? debug3.belowNoise : debug3.aboveNoise;
/* 21 */     return IntStream.range(0, debug7).mapToObj(debug1 -> debug0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\CountNoiseDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */