/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class NetherWartBlock
/*    */   extends BushBlock
/*    */ {
/* 19 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
/*    */   
/* 21 */   private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] {
/* 22 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D), 
/* 23 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), 
/* 24 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 11.0D, 16.0D), 
/* 25 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D)
/*    */     };
/*    */   
/*    */   protected NetherWartBlock(BlockBehaviour.Properties debug1) {
/* 29 */     super(debug1);
/* 30 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0)));
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 35 */     return SHAPE_BY_AGE[((Integer)debug1.getValue((Property)AGE)).intValue()];
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/* 40 */     return debug1.is(Blocks.SOUL_SAND);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isRandomlyTicking(BlockState debug1) {
/* 45 */     return (((Integer)debug1.getValue((Property)AGE)).intValue() < 3);
/*    */   }
/*    */ 
/*    */   
/*    */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 50 */     int debug5 = ((Integer)debug1.getValue((Property)AGE)).intValue();
/* 51 */     if (debug5 < 3 && debug4.nextInt(10) == 0) {
/* 52 */       debug1 = (BlockState)debug1.setValue((Property)AGE, Integer.valueOf(debug5 + 1));
/* 53 */       debug2.setBlock(debug3, debug1, 2);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 64 */     debug1.add(new Property[] { (Property)AGE });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\NetherWartBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */