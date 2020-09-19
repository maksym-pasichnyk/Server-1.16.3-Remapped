/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratedFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.DecorationContext;
/*    */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*    */ 
/*    */ public class DecoratedFeature extends Feature<DecoratedFeatureConfiguration> {
/*    */   public DecoratedFeature(Codec<DecoratedFeatureConfiguration> debug1) {
/* 16 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, DecoratedFeatureConfiguration debug5) {
/* 21 */     MutableBoolean debug6 = new MutableBoolean();
/* 22 */     debug5.decorator.getPositions(new DecorationContext(debug1, debug2), debug3, debug4).forEach(debug5 -> {
/*    */           if (((ConfiguredFeature)debug0.feature.get()).place(debug1, debug2, debug3, debug5)) {
/*    */             debug4.setTrue();
/*    */           }
/*    */         });
/* 27 */     return debug6.isTrue();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 32 */     return String.format("< %s [%s] >", new Object[] { getClass().getSimpleName(), Registry.FEATURE.getKey(this) });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\DecoratedFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */