/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public class NoiseCountFactorDecoratorConfiguration implements DecoratorConfiguration {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.INT.fieldOf("noise_to_count_ratio").forGetter(()), (App)Codec.DOUBLE.fieldOf("noise_factor").forGetter(()), (App)Codec.DOUBLE.fieldOf("noise_offset").orElse(Double.valueOf(0.0D)).forGetter(())).apply((Applicative)debug0, NoiseCountFactorDecoratorConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<NoiseCountFactorDecoratorConfiguration> CODEC;
/*    */   
/*    */   public final int noiseToCountRatio;
/*    */   public final double noiseFactor;
/*    */   public final double noiseOffset;
/*    */   
/*    */   public NoiseCountFactorDecoratorConfiguration(int debug1, double debug2, double debug4) {
/* 19 */     this.noiseToCountRatio = debug1;
/* 20 */     this.noiseFactor = debug2;
/* 21 */     this.noiseOffset = debug4;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\NoiseCountFactorDecoratorConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */