/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.IntStream;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ 
/*    */ public class CountWithExtraChanceDecorator extends SimpleFeatureDecorator<FrequencyWithExtraChanceDecoratorConfiguration> {
/*    */   public CountWithExtraChanceDecorator(Codec<FrequencyWithExtraChanceDecoratorConfiguration> debug1) {
/* 12 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> place(Random debug1, FrequencyWithExtraChanceDecoratorConfiguration debug2, BlockPos debug3) {
/* 17 */     int debug4 = debug2.count + ((debug1.nextFloat() < debug2.extraChance) ? debug2.extraCount : 0);
/* 18 */     return IntStream.range(0, debug4).mapToObj(debug1 -> debug0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\CountWithExtraChanceDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */