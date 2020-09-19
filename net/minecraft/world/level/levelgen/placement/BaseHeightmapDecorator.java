/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ 
/*    */ public abstract class BaseHeightmapDecorator<DC extends DecoratorConfiguration>
/*    */   extends EdgeDecorator<DC> {
/*    */   public BaseHeightmapDecorator(Codec<DC> debug1) {
/* 12 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(DecorationContext debug1, Random debug2, DC debug3, BlockPos debug4) {
/* 17 */     int debug5 = debug4.getX();
/* 18 */     int debug6 = debug4.getZ();
/* 19 */     int debug7 = debug1.getHeight(type(debug3), debug5, debug6);
/* 20 */     if (debug7 > 0) {
/* 21 */       return Stream.of(new BlockPos(debug5, debug7, debug6));
/*    */     }
/* 23 */     return Stream.of(new BlockPos[0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\BaseHeightmapDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */