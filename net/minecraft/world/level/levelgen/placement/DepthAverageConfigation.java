/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public class DepthAverageConfigation implements DecoratorConfiguration {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.INT.fieldOf("baseline").forGetter(()), (App)Codec.INT.fieldOf("spread").forGetter(())).apply((Applicative)debug0, DepthAverageConfigation::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<DepthAverageConfigation> CODEC;
/*    */   public final int baseline;
/*    */   public final int spread;
/*    */   
/*    */   public DepthAverageConfigation(int debug1, int debug2) {
/* 17 */     this.baseline = debug1;
/* 18 */     this.spread = debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\DepthAverageConfigation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */