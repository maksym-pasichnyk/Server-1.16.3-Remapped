/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
/*    */ 
/*    */ public class NopePlacementDecorator extends SimpleFeatureDecorator<NoneDecoratorConfiguration> {
/*    */   public NopePlacementDecorator(Codec<NoneDecoratorConfiguration> debug1) {
/* 12 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> place(Random debug1, NoneDecoratorConfiguration debug2, BlockPos debug3) {
/* 17 */     return Stream.of(debug3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\NopePlacementDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */