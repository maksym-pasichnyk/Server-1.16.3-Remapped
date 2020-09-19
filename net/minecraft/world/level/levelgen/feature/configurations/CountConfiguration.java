/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.UniformInt;
/*    */ 
/*    */ public class CountConfiguration implements DecoratorConfiguration, FeatureConfiguration {
/*  7 */   public static final Codec<CountConfiguration> CODEC = UniformInt.codec(-10, 128, 128).fieldOf("count")
/*  8 */     .xmap(CountConfiguration::new, CountConfiguration::count).codec();
/*    */   
/*    */   private final UniformInt count;
/*    */   
/*    */   public CountConfiguration(int debug1) {
/* 13 */     this.count = UniformInt.fixed(debug1);
/*    */   }
/*    */   
/*    */   public CountConfiguration(UniformInt debug1) {
/* 17 */     this.count = debug1;
/*    */   }
/*    */   
/*    */   public UniformInt count() {
/* 21 */     return this.count;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\CountConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */