/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Random;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ 
/*    */ public class WeightedConfiguredFeature {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)ConfiguredFeature.CODEC.fieldOf("feature").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("chance").forGetter(())).apply((Applicative)debug0, WeightedConfiguredFeature::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<WeightedConfiguredFeature> CODEC;
/*    */   public final Supplier<ConfiguredFeature<?, ?>> feature;
/*    */   public final float chance;
/*    */   
/*    */   public WeightedConfiguredFeature(ConfiguredFeature<?, ?> debug1, float debug2) {
/* 22 */     this(() -> debug0, debug2);
/*    */   }
/*    */   
/*    */   private WeightedConfiguredFeature(Supplier<ConfiguredFeature<?, ?>> debug1, float debug2) {
/* 26 */     this.feature = debug1;
/* 27 */     this.chance = debug2;
/*    */   }
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4) {
/* 31 */     return ((ConfiguredFeature)this.feature.get()).place(debug1, debug2, debug3, debug4);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\WeightedConfiguredFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */