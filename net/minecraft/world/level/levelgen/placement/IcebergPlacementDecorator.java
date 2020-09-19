/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
/*    */ 
/*    */ public class IcebergPlacementDecorator extends SimpleFeatureDecorator<NoneDecoratorConfiguration> {
/*    */   public IcebergPlacementDecorator(Codec<NoneDecoratorConfiguration> debug1) {
/* 12 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> place(Random debug1, NoneDecoratorConfiguration debug2, BlockPos debug3) {
/* 17 */     int debug4 = debug1.nextInt(8) + 4 + debug3.getX();
/* 18 */     int debug5 = debug1.nextInt(8) + 4 + debug3.getZ();
/* 19 */     return Stream.of(new BlockPos(debug4, debug3.getY(), debug5));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\IcebergPlacementDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */