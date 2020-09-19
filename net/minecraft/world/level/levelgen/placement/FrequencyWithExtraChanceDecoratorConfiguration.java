/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public class FrequencyWithExtraChanceDecoratorConfiguration implements DecoratorConfiguration {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.INT.fieldOf("count").forGetter(()), (App)Codec.FLOAT.fieldOf("extra_chance").forGetter(()), (App)Codec.INT.fieldOf("extra_count").forGetter(())).apply((Applicative)debug0, FrequencyWithExtraChanceDecoratorConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<FrequencyWithExtraChanceDecoratorConfiguration> CODEC;
/*    */   
/*    */   public final int count;
/*    */   public final float extraChance;
/*    */   public final int extraCount;
/*    */   
/*    */   public FrequencyWithExtraChanceDecoratorConfiguration(int debug1, float debug2, int debug3) {
/* 19 */     this.count = debug1;
/* 20 */     this.extraChance = debug2;
/* 21 */     this.extraCount = debug3;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\FrequencyWithExtraChanceDecoratorConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */