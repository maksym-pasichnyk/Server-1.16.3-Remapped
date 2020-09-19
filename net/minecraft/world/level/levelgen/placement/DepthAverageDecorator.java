/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ 
/*    */ public class DepthAverageDecorator extends SimpleFeatureDecorator<DepthAverageConfigation> {
/*    */   public DepthAverageDecorator(Codec<DepthAverageConfigation> debug1) {
/* 11 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> place(Random debug1, DepthAverageConfigation debug2, BlockPos debug3) {
/* 16 */     int debug4 = debug2.baseline;
/* 17 */     int debug5 = debug2.spread;
/*    */     
/* 19 */     int debug6 = debug3.getX();
/* 20 */     int debug7 = debug3.getZ();
/* 21 */     int debug8 = debug1.nextInt(debug5) + debug1.nextInt(debug5) - debug5 + debug4;
/* 22 */     return Stream.of(new BlockPos(debug6, debug8, debug7));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\DepthAverageDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */