/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ public class RuinedPortalConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<RuinedPortalConfiguration> CODEC;
/*    */   
/*    */   static {
/*  7 */     CODEC = RuinedPortalFeature.Type.CODEC.fieldOf("portal_type").xmap(RuinedPortalConfiguration::new, debug0 -> debug0.portalType).codec();
/*    */   }
/*    */   public final RuinedPortalFeature.Type portalType;
/*    */   
/*    */   public RuinedPortalConfiguration(RuinedPortalFeature.Type debug1) {
/* 12 */     this.portalType = debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\RuinedPortalConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */