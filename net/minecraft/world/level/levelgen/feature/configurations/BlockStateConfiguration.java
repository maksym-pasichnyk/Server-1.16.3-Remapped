/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ public class BlockStateConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<BlockStateConfiguration> CODEC;
/*    */   
/*    */   static {
/*  7 */     CODEC = BlockState.CODEC.fieldOf("state").xmap(BlockStateConfiguration::new, debug0 -> debug0.state).codec();
/*    */   }
/*    */   public final BlockState state;
/*    */   
/*    */   public BlockStateConfiguration(BlockState debug1) {
/* 12 */     this.state = debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\BlockStateConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */