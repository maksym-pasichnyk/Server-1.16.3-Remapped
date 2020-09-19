/*    */ package net.minecraft.world.level.block;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.grower.AbstractTreeGrower;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class SaplingBlock extends BushBlock implements BonemealableBlock {
/* 18 */   public static final IntegerProperty STAGE = BlockStateProperties.STAGE;
/*    */ 
/*    */   
/* 21 */   protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
/*    */   
/*    */   private final AbstractTreeGrower treeGrower;
/*    */   
/*    */   protected SaplingBlock(AbstractTreeGrower debug1, BlockBehaviour.Properties debug2) {
/* 26 */     super(debug2);
/* 27 */     this.treeGrower = debug1;
/* 28 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)STAGE, Integer.valueOf(0)));
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 33 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 38 */     if (debug2.getMaxLocalRawBrightness(debug3.above()) >= 9 && 
/* 39 */       debug4.nextInt(7) == 0) {
/* 40 */       advanceTree(debug2, debug3, debug1, debug4);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void advanceTree(ServerLevel debug1, BlockPos debug2, BlockState debug3, Random debug4) {
/* 46 */     if (((Integer)debug3.getValue((Property)STAGE)).intValue() == 0) {
/* 47 */       debug1.setBlock(debug2, (BlockState)debug3.cycle((Property)STAGE), 4);
/*    */     } else {
/* 49 */       this.treeGrower.growTree(debug1, debug1.getChunkSource().getGenerator(), debug2, debug3, debug4);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(BlockGetter debug1, BlockPos debug2, BlockState debug3, boolean debug4) {
/* 55 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 60 */     return (debug1.random.nextFloat() < 0.45D);
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 65 */     advanceTree(debug1, debug3, debug4, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 70 */     debug1.add(new Property[] { (Property)STAGE });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\SaplingBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */