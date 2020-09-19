/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public class ProbabilityFeatureConfiguration implements CarverConfiguration, FeatureConfiguration {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(())).apply((Applicative)debug0, ProbabilityFeatureConfiguration::new));
/*    */   }
/*    */   
/*    */   public static final Codec<ProbabilityFeatureConfiguration> CODEC;
/*    */   public final float probability;
/*    */   
/*    */   public ProbabilityFeatureConfiguration(float debug1) {
/* 15 */     this.probability = debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\ProbabilityFeatureConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */