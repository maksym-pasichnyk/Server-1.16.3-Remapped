/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ 
/*    */ public class HeightmapDoubleDecorator<DC extends DecoratorConfiguration>
/*    */   extends EdgeDecorator<DC> {
/*    */   public HeightmapDoubleDecorator(Codec<DC> debug1) {
/* 13 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Heightmap.Types type(DC debug1) {
/* 18 */     return Heightmap.Types.MOTION_BLOCKING;
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(DecorationContext debug1, Random debug2, DC debug3, BlockPos debug4) {
/* 23 */     int debug5 = debug4.getX();
/* 24 */     int debug6 = debug4.getZ();
/* 25 */     int debug7 = debug1.getHeight(type(debug3), debug5, debug6);
/* 26 */     if (debug7 == 0) {
/* 27 */       return Stream.of(new BlockPos[0]);
/*    */     }
/* 29 */     return Stream.of(new BlockPos(debug5, debug2.nextInt(debug7 * 2), debug6));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\HeightmapDoubleDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */