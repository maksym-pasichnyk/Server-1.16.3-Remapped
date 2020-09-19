/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public abstract class BlockStateProvider
/*    */ {
/* 11 */   public static final Codec<BlockStateProvider> CODEC = Registry.BLOCKSTATE_PROVIDER_TYPES.dispatch(BlockStateProvider::type, BlockStateProviderType::codec);
/*    */   
/*    */   protected abstract BlockStateProviderType<?> type();
/*    */   
/*    */   public abstract BlockState getState(Random paramRandom, BlockPos paramBlockPos);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\BlockStateProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */