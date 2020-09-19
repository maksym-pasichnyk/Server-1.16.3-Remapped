/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ 
/*    */ public class LakeLavaPlacementDecorator extends FeatureDecorator<ChanceDecoratorConfiguration> {
/*    */   public LakeLavaPlacementDecorator(Codec<ChanceDecoratorConfiguration> debug1) {
/* 11 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(DecorationContext debug1, Random debug2, ChanceDecoratorConfiguration debug3, BlockPos debug4) {
/* 16 */     if (debug2.nextInt(debug3.chance / 10) == 0) {
/* 17 */       int debug5 = debug2.nextInt(16) + debug4.getX();
/* 18 */       int debug6 = debug2.nextInt(16) + debug4.getZ();
/* 19 */       int debug7 = debug2.nextInt(debug2.nextInt(debug1.getGenDepth() - 8) + 8);
/* 20 */       if (debug7 < debug1.getSeaLevel() || debug2.nextInt(debug3.chance / 8) == 0) {
/* 21 */         return Stream.of(new BlockPos(debug5, debug7, debug6));
/*    */       }
/*    */     } 
/*    */     
/* 25 */     return Stream.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\LakeLavaPlacementDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */