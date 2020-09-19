/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
/*    */ 
/*    */ public class EndIslandPlacementDecorator extends SimpleFeatureDecorator<NoneDecoratorConfiguration> {
/*    */   public EndIslandPlacementDecorator(Codec<NoneDecoratorConfiguration> debug1) {
/* 12 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> place(Random debug1, NoneDecoratorConfiguration debug2, BlockPos debug3) {
/* 17 */     Stream<BlockPos> debug4 = Stream.empty();
/*    */     
/* 19 */     if (debug1.nextInt(14) == 0) {
/* 20 */       debug4 = Stream.concat(debug4, Stream.of(debug3.offset(debug1.nextInt(16), 55 + debug1.nextInt(16), debug1.nextInt(16))));
/*    */       
/* 22 */       if (debug1.nextInt(4) == 0) {
/* 23 */         debug4 = Stream.concat(debug4, Stream.of(debug3.offset(debug1.nextInt(16), 55 + debug1.nextInt(16), debug1.nextInt(16))));
/*    */       }
/* 25 */       return debug4;
/*    */     } 
/*    */     
/* 28 */     return Stream.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\EndIslandPlacementDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */