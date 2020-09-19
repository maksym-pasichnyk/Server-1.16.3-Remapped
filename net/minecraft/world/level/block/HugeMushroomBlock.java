/*    */ package net.minecraft.world.level.block;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class HugeMushroomBlock extends Block {
/* 15 */   public static final BooleanProperty NORTH = PipeBlock.NORTH;
/* 16 */   public static final BooleanProperty EAST = PipeBlock.EAST;
/* 17 */   public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
/* 18 */   public static final BooleanProperty WEST = PipeBlock.WEST;
/* 19 */   public static final BooleanProperty UP = PipeBlock.UP;
/* 20 */   public static final BooleanProperty DOWN = PipeBlock.DOWN;
/*    */   
/* 22 */   private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
/*    */   
/*    */   public HugeMushroomBlock(BlockBehaviour.Properties debug1) {
/* 25 */     super(debug1);
/* 26 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)NORTH, Boolean.valueOf(true))).setValue((Property)EAST, Boolean.valueOf(true))).setValue((Property)SOUTH, Boolean.valueOf(true))).setValue((Property)WEST, Boolean.valueOf(true))).setValue((Property)UP, Boolean.valueOf(true))).setValue((Property)DOWN, Boolean.valueOf(true)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 31 */     Level level = debug1.getLevel();
/* 32 */     BlockPos debug3 = debug1.getClickedPos();
/*    */     
/* 34 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState()
/* 35 */       .setValue((Property)DOWN, Boolean.valueOf((this != level.getBlockState(debug3.below()).getBlock()))))
/* 36 */       .setValue((Property)UP, Boolean.valueOf((this != level.getBlockState(debug3.above()).getBlock()))))
/* 37 */       .setValue((Property)NORTH, Boolean.valueOf((this != level.getBlockState(debug3.north()).getBlock()))))
/* 38 */       .setValue((Property)EAST, Boolean.valueOf((this != level.getBlockState(debug3.east()).getBlock()))))
/* 39 */       .setValue((Property)SOUTH, Boolean.valueOf((this != level.getBlockState(debug3.south()).getBlock()))))
/* 40 */       .setValue((Property)WEST, Boolean.valueOf((this != level.getBlockState(debug3.west()).getBlock())));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 46 */     if (debug3.is(this)) {
/* 47 */       return (BlockState)debug1.setValue((Property)PROPERTY_BY_DIRECTION.get(debug2), Boolean.valueOf(false));
/*    */     }
/* 49 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 54 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)debug1
/* 55 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(debug2.rotate(Direction.NORTH)), debug1.getValue((Property)NORTH)))
/* 56 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(debug2.rotate(Direction.SOUTH)), debug1.getValue((Property)SOUTH)))
/* 57 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(debug2.rotate(Direction.EAST)), debug1.getValue((Property)EAST)))
/* 58 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(debug2.rotate(Direction.WEST)), debug1.getValue((Property)WEST)))
/* 59 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(debug2.rotate(Direction.UP)), debug1.getValue((Property)UP)))
/* 60 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(debug2.rotate(Direction.DOWN)), debug1.getValue((Property)DOWN));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 66 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)debug1
/* 67 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(debug2.mirror(Direction.NORTH)), debug1.getValue((Property)NORTH)))
/* 68 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(debug2.mirror(Direction.SOUTH)), debug1.getValue((Property)SOUTH)))
/* 69 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(debug2.mirror(Direction.EAST)), debug1.getValue((Property)EAST)))
/* 70 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(debug2.mirror(Direction.WEST)), debug1.getValue((Property)WEST)))
/* 71 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(debug2.mirror(Direction.UP)), debug1.getValue((Property)UP)))
/* 72 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(debug2.mirror(Direction.DOWN)), debug1.getValue((Property)DOWN));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 78 */     debug1.add(new Property[] { (Property)UP, (Property)DOWN, (Property)NORTH, (Property)EAST, (Property)SOUTH, (Property)WEST });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\HugeMushroomBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */