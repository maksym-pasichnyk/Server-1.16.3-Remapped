/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockIgnoreProcessor
/*    */   extends StructureProcessor
/*    */ {
/*    */   public static final Codec<BlockIgnoreProcessor> CODEC;
/*    */   
/*    */   static {
/* 21 */     CODEC = BlockState.CODEC.xmap(BlockBehaviour.BlockStateBase::getBlock, Block::defaultBlockState).listOf().fieldOf("blocks").xmap(BlockIgnoreProcessor::new, debug0 -> debug0.toIgnore).codec();
/*    */   }
/* 23 */   public static final BlockIgnoreProcessor STRUCTURE_BLOCK = new BlockIgnoreProcessor((List<Block>)ImmutableList.of(Blocks.STRUCTURE_BLOCK));
/* 24 */   public static final BlockIgnoreProcessor AIR = new BlockIgnoreProcessor((List<Block>)ImmutableList.of(Blocks.AIR));
/* 25 */   public static final BlockIgnoreProcessor STRUCTURE_AND_AIR = new BlockIgnoreProcessor((List<Block>)ImmutableList.of(Blocks.AIR, Blocks.STRUCTURE_BLOCK));
/*    */   
/*    */   private final ImmutableList<Block> toIgnore;
/*    */   
/*    */   public BlockIgnoreProcessor(List<Block> debug1) {
/* 30 */     this.toIgnore = ImmutableList.copyOf(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader debug1, BlockPos debug2, BlockPos debug3, StructureTemplate.StructureBlockInfo debug4, StructureTemplate.StructureBlockInfo debug5, StructurePlaceSettings debug6) {
/* 36 */     if (this.toIgnore.contains(debug5.state.getBlock())) {
/* 37 */       return null;
/*    */     }
/* 39 */     return debug5;
/*    */   }
/*    */ 
/*    */   
/*    */   protected StructureProcessorType<?> getType() {
/* 44 */     return StructureProcessorType.BLOCK_IGNORE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlockIgnoreProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */