/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.IntStream;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
/*    */ 
/*    */ public class EmeraldPlacementDecorator extends SimpleFeatureDecorator<NoneDecoratorConfiguration> {
/*    */   public EmeraldPlacementDecorator(Codec<NoneDecoratorConfiguration> debug1) {
/* 13 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> place(Random debug1, NoneDecoratorConfiguration debug2, BlockPos debug3) {
/* 18 */     int debug4 = 3 + debug1.nextInt(6);
/* 19 */     return IntStream.range(0, debug4).mapToObj(debug2 -> {
/*    */           int debug3 = debug0.nextInt(16) + debug1.getX();
/*    */           int debug4 = debug0.nextInt(16) + debug1.getZ();
/*    */           int debug5 = debug0.nextInt(28) + 4;
/*    */           return new BlockPos(debug3, debug5, debug4);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\EmeraldPlacementDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */