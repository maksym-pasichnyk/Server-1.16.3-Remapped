/*    */ package net.minecraft.world.level.levelgen.placement.nether;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.DecorationContext;
/*    */ import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
/*    */ 
/*    */ public class MagmaDecorator extends FeatureDecorator<NoneDecoratorConfiguration> {
/*    */   public MagmaDecorator(Codec<NoneDecoratorConfiguration> debug1) {
/* 14 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(DecorationContext debug1, Random debug2, NoneDecoratorConfiguration debug3, BlockPos debug4) {
/* 19 */     int debug5 = debug1.getSeaLevel();
/* 20 */     int debug6 = debug5 - 5 + debug2.nextInt(10);
/* 21 */     return Stream.of(new BlockPos(debug4.getX(), debug6, debug4.getZ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\nether\MagmaDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */