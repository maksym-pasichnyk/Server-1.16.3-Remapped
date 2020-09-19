/*    */ package net.minecraft.data.worldgen;
/*    */ 
/*    */ import net.minecraft.data.BuiltinRegistries;
/*    */ import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
/*    */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*    */ import net.minecraft.world.level.levelgen.carver.WorldCarver;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*    */ 
/*    */ public class Carvers {
/* 10 */   public static final ConfiguredWorldCarver<ProbabilityFeatureConfiguration> CAVE = register("cave", WorldCarver.CAVE.configured((CarverConfiguration)new ProbabilityFeatureConfiguration(0.14285715F)));
/* 11 */   public static final ConfiguredWorldCarver<ProbabilityFeatureConfiguration> CANYON = register("canyon", WorldCarver.CANYON.configured((CarverConfiguration)new ProbabilityFeatureConfiguration(0.02F)));
/* 12 */   public static final ConfiguredWorldCarver<ProbabilityFeatureConfiguration> OCEAN_CAVE = register("ocean_cave", WorldCarver.CAVE.configured((CarverConfiguration)new ProbabilityFeatureConfiguration(0.06666667F)));
/* 13 */   public static final ConfiguredWorldCarver<ProbabilityFeatureConfiguration> UNDERWATER_CANYON = register("underwater_canyon", WorldCarver.UNDERWATER_CANYON.configured((CarverConfiguration)new ProbabilityFeatureConfiguration(0.02F)));
/* 14 */   public static final ConfiguredWorldCarver<ProbabilityFeatureConfiguration> UNDERWATER_CAVE = register("underwater_cave", WorldCarver.UNDERWATER_CAVE.configured((CarverConfiguration)new ProbabilityFeatureConfiguration(0.06666667F)));
/* 15 */   public static final ConfiguredWorldCarver<ProbabilityFeatureConfiguration> NETHER_CAVE = register("nether_cave", WorldCarver.NETHER_CAVE.configured((CarverConfiguration)new ProbabilityFeatureConfiguration(0.2F)));
/*    */   
/*    */   private static <WC extends CarverConfiguration> ConfiguredWorldCarver<WC> register(String debug0, ConfiguredWorldCarver<WC> debug1) {
/* 18 */     return (ConfiguredWorldCarver<WC>)BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_CARVER, debug0, debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\worldgen\Carvers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */