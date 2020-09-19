/*    */ package net.minecraft.world.level.levelgen.feature.blockplacers;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public abstract class BlockPlacer
/*    */ {
/* 12 */   public static final Codec<BlockPlacer> CODEC = Registry.BLOCK_PLACER_TYPES.dispatch(BlockPlacer::type, BlockPlacerType::codec);
/*    */   
/*    */   public abstract void place(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState, Random paramRandom);
/*    */   
/*    */   protected abstract BlockPlacerType<?> type();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\blockplacers\BlockPlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */