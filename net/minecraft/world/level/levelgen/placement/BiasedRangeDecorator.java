/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;
/*    */ 
/*    */ public class BiasedRangeDecorator extends SimpleFeatureDecorator<RangeDecoratorConfiguration> {
/*    */   public BiasedRangeDecorator(Codec<RangeDecoratorConfiguration> debug1) {
/* 12 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> place(Random debug1, RangeDecoratorConfiguration debug2, BlockPos debug3) {
/* 17 */     int debug4 = debug3.getX();
/* 18 */     int debug5 = debug3.getZ();
/* 19 */     int debug6 = debug1.nextInt(debug1.nextInt(debug2.maximum - debug2.topOffset) + debug2.bottomOffset);
/* 20 */     return Stream.of(new BlockPos(debug4, debug6, debug5));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\BiasedRangeDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */