/*   */ package net.minecraft.world.level.levelgen.feature.configurations;
/*   */ 
/*   */ import com.mojang.serialization.Codec;
/*   */ 
/*   */ public class NoneFeatureConfiguration implements FeatureConfiguration {
/* 6 */   public static final Codec<NoneFeatureConfiguration> CODEC = Codec.unit(() -> INSTANCE);
/*   */   
/* 8 */   public static final NoneFeatureConfiguration INSTANCE = new NoneFeatureConfiguration();
/*   */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\NoneFeatureConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */