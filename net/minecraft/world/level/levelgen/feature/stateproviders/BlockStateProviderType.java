/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class BlockStateProviderType<P extends BlockStateProvider> {
/*  7 */   public static final BlockStateProviderType<SimpleStateProvider> SIMPLE_STATE_PROVIDER = register("simple_state_provider", SimpleStateProvider.CODEC);
/*  8 */   public static final BlockStateProviderType<WeightedStateProvider> WEIGHTED_STATE_PROVIDER = register("weighted_state_provider", WeightedStateProvider.CODEC);
/*  9 */   public static final BlockStateProviderType<PlainFlowerProvider> PLAIN_FLOWER_PROVIDER = register("plain_flower_provider", PlainFlowerProvider.CODEC);
/* 10 */   public static final BlockStateProviderType<ForestFlowerProvider> FOREST_FLOWER_PROVIDER = register("forest_flower_provider", ForestFlowerProvider.CODEC);
/* 11 */   public static final BlockStateProviderType<RotatedBlockProvider> ROTATED_BLOCK_PROVIDER = register("rotated_block_provider", RotatedBlockProvider.CODEC);
/*    */   
/*    */   private static <P extends BlockStateProvider> BlockStateProviderType<P> register(String debug0, Codec<P> debug1) {
/* 14 */     return (BlockStateProviderType<P>)Registry.register(Registry.BLOCKSTATE_PROVIDER_TYPES, debug0, new BlockStateProviderType<>(debug1));
/*    */   }
/*    */   
/*    */   private final Codec<P> codec;
/*    */   
/*    */   private BlockStateProviderType(Codec<P> debug1) {
/* 20 */     this.codec = debug1;
/*    */   }
/*    */   
/*    */   public Codec<P> codec() {
/* 24 */     return this.codec;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\BlockStateProviderType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */