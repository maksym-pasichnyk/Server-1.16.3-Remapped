/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
/*    */ 
/*    */ public class Spread32Decorator extends FeatureDecorator<NoneDecoratorConfiguration> {
/*    */   public Spread32Decorator(Codec<NoneDecoratorConfiguration> debug1) {
/* 12 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(DecorationContext debug1, Random debug2, NoneDecoratorConfiguration debug3, BlockPos debug4) {
/* 17 */     int debug5 = debug2.nextInt(debug4.getY() + 32);
/* 18 */     return Stream.of(new BlockPos(debug4.getX(), debug5, debug4.getZ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\Spread32Decorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */