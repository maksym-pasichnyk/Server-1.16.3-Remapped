/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.levelgen.structure.OceanRuinFeature;
/*    */ 
/*    */ public class OceanRuinConfiguration implements FeatureConfiguration {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)OceanRuinFeature.Type.CODEC.fieldOf("biome_temp").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("large_probability").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("cluster_probability").forGetter(())).apply((Applicative)debug0, OceanRuinConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<OceanRuinConfiguration> CODEC;
/*    */   
/*    */   public final OceanRuinFeature.Type biomeTemp;
/*    */   public final float largeProbability;
/*    */   public final float clusterProbability;
/*    */   
/*    */   public OceanRuinConfiguration(OceanRuinFeature.Type debug1, float debug2, float debug3) {
/* 19 */     this.biomeTemp = debug1;
/* 20 */     this.largeProbability = debug2;
/* 21 */     this.clusterProbability = debug3;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\OceanRuinConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */