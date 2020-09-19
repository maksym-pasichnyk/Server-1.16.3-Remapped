/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ 
/*    */ public abstract class SimpleFeatureDecorator<DC extends DecoratorConfiguration>
/*    */   extends FeatureDecorator<DC> {
/*    */   public SimpleFeatureDecorator(Codec<DC> debug1) {
/* 12 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public final Stream<BlockPos> getPositions(DecorationContext debug1, Random debug2, DC debug3, BlockPos debug4) {
/* 17 */     return place(debug2, debug3, debug4);
/*    */   }
/*    */   
/*    */   protected abstract Stream<BlockPos> place(Random paramRandom, DC paramDC, BlockPos paramBlockPos);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\SimpleFeatureDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */