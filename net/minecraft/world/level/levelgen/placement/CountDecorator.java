/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.IntStream;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ 
/*    */ public class CountDecorator extends SimpleFeatureDecorator<CountConfiguration> {
/*    */   public CountDecorator(Codec<CountConfiguration> debug1) {
/* 13 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> place(Random debug1, CountConfiguration debug2, BlockPos debug3) {
/* 18 */     return IntStream.range(0, debug2.count().sample(debug1)).mapToObj(debug1 -> debug0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\CountDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */