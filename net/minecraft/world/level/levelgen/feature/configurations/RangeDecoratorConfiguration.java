/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ 
/*    */ public class RangeDecoratorConfiguration implements DecoratorConfiguration {
/*    */   static {
/*  7 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.INT.fieldOf("bottom_offset").orElse(Integer.valueOf(0)).forGetter(()), (App)Codec.INT.fieldOf("top_offset").orElse(Integer.valueOf(0)).forGetter(()), (App)Codec.INT.fieldOf("maximum").orElse(Integer.valueOf(0)).forGetter(())).apply((Applicative)debug0, RangeDecoratorConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<RangeDecoratorConfiguration> CODEC;
/*    */   
/*    */   public final int bottomOffset;
/*    */   public final int topOffset;
/*    */   public final int maximum;
/*    */   
/*    */   public RangeDecoratorConfiguration(int debug1, int debug2, int debug3) {
/* 18 */     this.bottomOffset = debug1;
/* 19 */     this.topOffset = debug2;
/* 20 */     this.maximum = debug3;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\RangeDecoratorConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */