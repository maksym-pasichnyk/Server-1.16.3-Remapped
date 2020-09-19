/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
/*    */ 
/*    */ public class SquareDecorator extends SimpleFeatureDecorator<NoneDecoratorConfiguration> {
/*    */   public SquareDecorator(Codec<NoneDecoratorConfiguration> debug1) {
/* 12 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> place(Random debug1, NoneDecoratorConfiguration debug2, BlockPos debug3) {
/* 17 */     int debug4 = debug1.nextInt(16) + debug3.getX();
/* 18 */     int debug5 = debug1.nextInt(16) + debug3.getZ();
/* 19 */     int debug6 = debug3.getY();
/*    */     
/* 21 */     return Stream.of(new BlockPos(debug4, debug6, debug5));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\SquareDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */