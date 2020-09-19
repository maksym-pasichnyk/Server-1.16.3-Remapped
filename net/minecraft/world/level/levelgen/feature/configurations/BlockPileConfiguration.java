/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ public class BlockPileConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<BlockPileConfiguration> CODEC;
/*    */   
/*    */   static {
/*  7 */     CODEC = BlockStateProvider.CODEC.fieldOf("state_provider").xmap(BlockPileConfiguration::new, debug0 -> debug0.stateProvider).codec();
/*    */   }
/*    */   public final BlockStateProvider stateProvider;
/*    */   
/*    */   public BlockPileConfiguration(BlockStateProvider debug1) {
/* 12 */     this.stateProvider = debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\BlockPileConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */