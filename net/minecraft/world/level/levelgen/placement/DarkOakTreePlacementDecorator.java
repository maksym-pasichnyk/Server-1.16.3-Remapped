/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.IntStream;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
/*    */ 
/*    */ public class DarkOakTreePlacementDecorator extends EdgeDecorator<NoneDecoratorConfiguration> {
/*    */   public DarkOakTreePlacementDecorator(Codec<NoneDecoratorConfiguration> debug1) {
/* 14 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Heightmap.Types type(NoneDecoratorConfiguration debug1) {
/* 19 */     return Heightmap.Types.MOTION_BLOCKING;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(DecorationContext debug1, Random debug2, NoneDecoratorConfiguration debug3, BlockPos debug4) {
/* 26 */     return IntStream.range(0, 16).mapToObj(debug5 -> {
/*    */           int debug6 = debug5 / 4;
/*    */           int debug7 = debug5 % 4;
/*    */           int debug8 = debug6 * 4 + 1 + debug1.nextInt(3) + debug2.getX();
/*    */           int debug9 = debug7 * 4 + 1 + debug1.nextInt(3) + debug2.getZ();
/*    */           int debug10 = debug3.getHeight(type(debug4), debug8, debug9);
/*    */           return new BlockPos(debug8, debug10, debug9);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\DarkOakTreePlacementDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */