/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ 
/*    */ public class ChanceDecorator extends SimpleFeatureDecorator<ChanceDecoratorConfiguration> {
/*    */   public ChanceDecorator(Codec<ChanceDecoratorConfiguration> debug1) {
/* 11 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> place(Random debug1, ChanceDecoratorConfiguration debug2, BlockPos debug3) {
/* 16 */     if (debug1.nextFloat() < 1.0F / debug2.chance) {
/* 17 */       return Stream.of(debug3);
/*    */     }
/*    */     
/* 20 */     return Stream.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\ChanceDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */