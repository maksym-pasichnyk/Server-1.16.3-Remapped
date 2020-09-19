/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ 
/*    */ public class DecoratedDecorator extends FeatureDecorator<DecoratedDecoratorConfiguration> {
/*    */   public DecoratedDecorator(Codec<DecoratedDecoratorConfiguration> debug1) {
/* 11 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(DecorationContext debug1, Random debug2, DecoratedDecoratorConfiguration debug3, BlockPos debug4) {
/* 16 */     return debug3.outer().getPositions(debug1, debug2, debug4).flatMap(debug3 -> debug0.inner().getPositions(debug1, debug2, debug3));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\DecoratedDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */