/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.UniformInt;
/*    */ 
/*    */ public class ColumnFeatureConfiguration implements FeatureConfiguration {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)UniformInt.codec(0, 2, 1).fieldOf("reach").forGetter(()), (App)UniformInt.codec(1, 5, 5).fieldOf("height").forGetter(())).apply((Applicative)debug0, ColumnFeatureConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<ColumnFeatureConfiguration> CODEC;
/*    */   private final UniformInt reach;
/*    */   private final UniformInt height;
/*    */   
/*    */   public ColumnFeatureConfiguration(UniformInt debug1, UniformInt debug2) {
/* 17 */     this.reach = debug1;
/* 18 */     this.height = debug2;
/*    */   }
/*    */   
/*    */   public UniformInt reach() {
/* 22 */     return this.reach;
/*    */   }
/*    */   
/*    */   public UniformInt height() {
/* 26 */     return this.height;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\ColumnFeatureConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */