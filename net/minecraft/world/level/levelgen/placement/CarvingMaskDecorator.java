/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.BitSet;
/*    */ import java.util.Random;
/*    */ import java.util.stream.IntStream;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ 
/*    */ public class CarvingMaskDecorator extends FeatureDecorator<CarvingMaskDecoratorConfiguration> {
/*    */   public CarvingMaskDecorator(Codec<CarvingMaskDecoratorConfiguration> debug1) {
/* 14 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(DecorationContext debug1, Random debug2, CarvingMaskDecoratorConfiguration debug3, BlockPos debug4) {
/* 19 */     ChunkPos debug5 = new ChunkPos(debug4);
/* 20 */     BitSet debug6 = debug1.getCarvingMask(debug5, debug3.step);
/*    */     
/* 22 */     return IntStream.range(0, debug6.length()).filter(debug3 -> (debug0.get(debug3) && debug1.nextFloat() < debug2.probability)).mapToObj(debug1 -> {
/*    */           int debug2 = debug1 & 0xF;
/*    */           int debug3 = debug1 >> 4 & 0xF;
/*    */           int debug4 = debug1 >> 8;
/*    */           return new BlockPos(debug0.getMinBlockX() + debug2, debug4, debug0.getMinBlockZ() + debug3);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\CarvingMaskDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */