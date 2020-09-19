/*    */ package net.minecraft.world.level.levelgen.placement.nether;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.IntStream;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.SimpleFeatureDecorator;
/*    */ 
/*    */ public class GlowstoneDecorator
/*    */   extends SimpleFeatureDecorator<CountConfiguration>
/*    */ {
/*    */   public GlowstoneDecorator(Codec<CountConfiguration> debug1) {
/* 16 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> place(Random debug1, CountConfiguration debug2, BlockPos debug3) {
/* 21 */     return IntStream.range(0, debug1.nextInt(debug1.nextInt(debug2.count().sample(debug1)) + 1)).mapToObj(debug2 -> {
/*    */           int debug3 = debug0.nextInt(16) + debug1.getX();
/*    */           int debug4 = debug0.nextInt(16) + debug1.getZ();
/*    */           int debug5 = debug0.nextInt(120) + 4;
/*    */           return new BlockPos(debug3, debug5, debug4);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\nether\GlowstoneDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */