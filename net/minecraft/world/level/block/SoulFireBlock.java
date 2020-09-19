/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SoulFireBlock extends BaseFireBlock {
/*    */   public SoulFireBlock(BlockBehaviour.Properties debug1) {
/* 12 */     super(debug1, 2.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 17 */     if (canSurvive(debug1, (LevelReader)debug4, debug5)) {
/* 18 */       return defaultBlockState();
/*    */     }
/*    */     
/* 21 */     return Blocks.AIR.defaultBlockState();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 26 */     return canSurviveOnBlock(debug2.getBlockState(debug3.below()).getBlock());
/*    */   }
/*    */   
/*    */   public static boolean canSurviveOnBlock(Block debug0) {
/* 30 */     return debug0.is((Tag<Block>)BlockTags.SOUL_FIRE_BASE_BLOCKS);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canBurn(BlockState debug1) {
/* 35 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\SoulFireBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */