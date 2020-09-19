/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BambooLeaves;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class BambooSaplingBlock
/*    */   extends Block
/*    */   implements BonemealableBlock {
/* 25 */   protected static final VoxelShape SAPLING_SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 12.0D, 12.0D);
/*    */   
/*    */   public BambooSaplingBlock(BlockBehaviour.Properties debug1) {
/* 28 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockBehaviour.OffsetType getOffsetType() {
/* 33 */     return BlockBehaviour.OffsetType.XZ;
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 38 */     Vec3 debug5 = debug1.getOffset(debug2, debug3);
/* 39 */     return SAPLING_SHAPE.move(debug5.x, debug5.y, debug5.z);
/*    */   }
/*    */ 
/*    */   
/*    */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 44 */     if (debug4.nextInt(3) == 0 && debug2.isEmptyBlock(debug3.above()) && debug2.getRawBrightness(debug3.above(), 0) >= 9) {
/* 45 */       growBamboo((Level)debug2, debug3);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 51 */     return debug2.getBlockState(debug3.below()).is((Tag)BlockTags.BAMBOO_PLANTABLE_ON);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 56 */     if (!debug1.canSurvive((LevelReader)debug4, debug5)) {
/* 57 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/*    */     
/* 60 */     if (debug2 == Direction.UP && debug3.is(Blocks.BAMBOO)) {
/* 61 */       debug4.setBlock(debug5, Blocks.BAMBOO.defaultBlockState(), 2);
/*    */     }
/*    */     
/* 64 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(BlockGetter debug1, BlockPos debug2, BlockState debug3, boolean debug4) {
/* 74 */     return debug1.getBlockState(debug2.above()).isAir();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 79 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 84 */     growBamboo((Level)debug1, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   public float getDestroyProgress(BlockState debug1, Player debug2, BlockGetter debug3, BlockPos debug4) {
/* 89 */     if (debug2.getMainHandItem().getItem() instanceof net.minecraft.world.item.SwordItem) {
/* 90 */       return 1.0F;
/*    */     }
/*    */     
/* 93 */     return super.getDestroyProgress(debug1, debug2, debug3, debug4);
/*    */   }
/*    */   
/*    */   protected void growBamboo(Level debug1, BlockPos debug2) {
/* 97 */     debug1.setBlock(debug2.above(), (BlockState)Blocks.BAMBOO.defaultBlockState().setValue((Property)BambooBlock.LEAVES, (Comparable)BambooLeaves.SMALL), 3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\BambooSaplingBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */