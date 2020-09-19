/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
/*    */ 
/*    */ public class EndGatewayPlacementDecorator extends FeatureDecorator<NoneDecoratorConfiguration> {
/*    */   public EndGatewayPlacementDecorator(Codec<NoneDecoratorConfiguration> debug1) {
/* 13 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(DecorationContext debug1, Random debug2, NoneDecoratorConfiguration debug3, BlockPos debug4) {
/* 18 */     if (debug2.nextInt(700) == 0) {
/* 19 */       int debug5 = debug2.nextInt(16) + debug4.getX();
/* 20 */       int debug6 = debug2.nextInt(16) + debug4.getZ();
/* 21 */       int debug7 = debug1.getHeight(Heightmap.Types.MOTION_BLOCKING, debug5, debug6);
/* 22 */       if (debug7 > 0) {
/* 23 */         int debug8 = debug7 + 3 + debug2.nextInt(7);
/* 24 */         return Stream.of(new BlockPos(debug5, debug8, debug6));
/*    */       } 
/*    */     } 
/*    */     
/* 28 */     return Stream.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\EndGatewayPlacementDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */