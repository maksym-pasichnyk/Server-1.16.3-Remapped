/*    */ package net.minecraft.world.level.levelgen.placement.nether;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.SimpleFeatureDecorator;
/*    */ 
/*    */ public class FireDecorator
/*    */   extends SimpleFeatureDecorator<CountConfiguration> {
/*    */   public FireDecorator(Codec<CountConfiguration> debug1) {
/* 16 */     super(debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> place(Random debug1, CountConfiguration debug2, BlockPos debug3) {
/* 23 */     List<BlockPos> debug4 = Lists.newArrayList();
/*    */     
/* 25 */     for (int debug5 = 0; debug5 < debug1.nextInt(debug1.nextInt(debug2.count().sample(debug1)) + 1) + 1; debug5++) {
/* 26 */       int debug6 = debug1.nextInt(16) + debug3.getX();
/* 27 */       int debug7 = debug1.nextInt(16) + debug3.getZ();
/* 28 */       int debug8 = debug1.nextInt(120) + 4;
/* 29 */       debug4.add(new BlockPos(debug6, debug8, debug7));
/*    */     } 
/*    */     
/* 32 */     return debug4.stream();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\nether\FireDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */