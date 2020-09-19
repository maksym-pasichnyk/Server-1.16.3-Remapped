/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ public class ShipwreckConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<ShipwreckConfiguration> CODEC;
/*    */   
/*    */   static {
/*  6 */     CODEC = Codec.BOOL.fieldOf("is_beached").orElse(Boolean.valueOf(false)).xmap(ShipwreckConfiguration::new, debug0 -> Boolean.valueOf(debug0.isBeached)).codec();
/*    */   }
/*    */   public final boolean isBeached;
/*    */   
/*    */   public ShipwreckConfiguration(boolean debug1) {
/* 11 */     this.isBeached = debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\ShipwreckConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */