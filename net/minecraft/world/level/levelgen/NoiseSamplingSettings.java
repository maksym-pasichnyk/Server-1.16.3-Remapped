/*    */ package net.minecraft.world.level.levelgen;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public class NoiseSamplingSettings {
/*  7 */   private static final Codec<Double> SCALE_RANGE = Codec.doubleRange(0.001D, 1000.0D); public static final Codec<NoiseSamplingSettings> CODEC; private final double xzScale;
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)SCALE_RANGE.fieldOf("xz_scale").forGetter(NoiseSamplingSettings::xzScale), (App)SCALE_RANGE.fieldOf("y_scale").forGetter(NoiseSamplingSettings::yScale), (App)SCALE_RANGE.fieldOf("xz_factor").forGetter(NoiseSamplingSettings::xzFactor), (App)SCALE_RANGE.fieldOf("y_factor").forGetter(NoiseSamplingSettings::yFactor)).apply((Applicative)debug0, NoiseSamplingSettings::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private final double yScale;
/*    */   
/*    */   private final double xzFactor;
/*    */   
/*    */   private final double yFactor;
/*    */ 
/*    */   
/*    */   public NoiseSamplingSettings(double debug1, double debug3, double debug5, double debug7) {
/* 22 */     this.xzScale = debug1;
/* 23 */     this.yScale = debug3;
/* 24 */     this.xzFactor = debug5;
/* 25 */     this.yFactor = debug7;
/*    */   }
/*    */   
/*    */   public double xzScale() {
/* 29 */     return this.xzScale;
/*    */   }
/*    */   
/*    */   public double yScale() {
/* 33 */     return this.yScale;
/*    */   }
/*    */   
/*    */   public double xzFactor() {
/* 37 */     return this.xzFactor;
/*    */   }
/*    */   
/*    */   public double yFactor() {
/* 41 */     return this.yFactor;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\NoiseSamplingSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */