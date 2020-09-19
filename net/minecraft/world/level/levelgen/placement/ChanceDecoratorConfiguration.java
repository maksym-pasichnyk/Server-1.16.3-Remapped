/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ public class ChanceDecoratorConfiguration implements DecoratorConfiguration {
/*    */   public static final Codec<ChanceDecoratorConfiguration> CODEC;
/*    */   
/*    */   static {
/*  7 */     CODEC = Codec.INT.fieldOf("chance").xmap(ChanceDecoratorConfiguration::new, debug0 -> Integer.valueOf(debug0.chance)).codec();
/*    */   }
/*    */   public final int chance;
/*    */   
/*    */   public ChanceDecoratorConfiguration(int debug1) {
/* 12 */     this.chance = debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\ChanceDecoratorConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */