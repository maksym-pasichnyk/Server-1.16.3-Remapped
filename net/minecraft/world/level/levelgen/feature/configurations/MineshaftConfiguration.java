/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.levelgen.feature.MineshaftFeature;
/*    */ 
/*    */ public class MineshaftConfiguration implements FeatureConfiguration {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(()), (App)MineshaftFeature.Type.CODEC.fieldOf("type").forGetter(())).apply((Applicative)debug0, MineshaftConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<MineshaftConfiguration> CODEC;
/*    */   public final float probability;
/*    */   public final MineshaftFeature.Type type;
/*    */   
/*    */   public MineshaftConfiguration(float debug1, MineshaftFeature.Type debug2) {
/* 17 */     this.probability = debug1;
/* 18 */     this.type = debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\MineshaftConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */