/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ 
/*    */ public class NoiseDependantDecoratorConfiguration implements DecoratorConfiguration {
/*    */   static {
/*  7 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.DOUBLE.fieldOf("noise_level").forGetter(()), (App)Codec.INT.fieldOf("below_noise").forGetter(()), (App)Codec.INT.fieldOf("above_noise").forGetter(())).apply((Applicative)debug0, NoiseDependantDecoratorConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<NoiseDependantDecoratorConfiguration> CODEC;
/*    */   
/*    */   public final double noiseLevel;
/*    */   public final int belowNoise;
/*    */   public final int aboveNoise;
/*    */   
/*    */   public NoiseDependantDecoratorConfiguration(double debug1, int debug3, int debug4) {
/* 18 */     this.noiseLevel = debug1;
/* 19 */     this.belowNoise = debug3;
/* 20 */     this.aboveNoise = debug4;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\NoiseDependantDecoratorConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */