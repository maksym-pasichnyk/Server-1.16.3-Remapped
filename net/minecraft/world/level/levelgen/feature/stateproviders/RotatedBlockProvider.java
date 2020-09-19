/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ import java.util.Random;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.RotatedPillarBlock;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class RotatedBlockProvider extends BlockStateProvider {
/*    */   public static final Codec<RotatedBlockProvider> CODEC;
/*    */   
/*    */   static {
/* 16 */     CODEC = BlockState.CODEC.fieldOf("state").xmap(BlockBehaviour.BlockStateBase::getBlock, Block::defaultBlockState).xmap(RotatedBlockProvider::new, debug0 -> debug0.block).codec();
/*    */   }
/*    */   private final Block block;
/*    */   
/*    */   public RotatedBlockProvider(Block debug1) {
/* 21 */     this.block = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockStateProviderType<?> type() {
/* 26 */     return BlockStateProviderType.ROTATED_BLOCK_PROVIDER;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getState(Random debug1, BlockPos debug2) {
/* 31 */     Direction.Axis debug3 = Direction.Axis.getRandom(debug1);
/* 32 */     return (BlockState)this.block.defaultBlockState().setValue((Property)RotatedPillarBlock.AXIS, (Comparable)debug3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\RotatedBlockProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */