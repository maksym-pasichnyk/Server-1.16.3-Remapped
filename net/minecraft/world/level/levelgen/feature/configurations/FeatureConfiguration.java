/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ 
/*    */ public interface FeatureConfiguration
/*    */ {
/*  8 */   public static final NoneFeatureConfiguration NONE = NoneFeatureConfiguration.INSTANCE;
/*    */   
/*    */   default Stream<ConfiguredFeature<?, ?>> getFeatures() {
/* 11 */     return Stream.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\FeatureConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */