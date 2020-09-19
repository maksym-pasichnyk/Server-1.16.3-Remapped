/*    */ package net.minecraft.world.level.block;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class WallSkullBlock extends AbstractSkullBlock {
/* 18 */   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
/*    */   
/* 20 */   private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap((Map)ImmutableMap.of(Direction.NORTH, 
/* 21 */         Block.box(4.0D, 4.0D, 8.0D, 12.0D, 12.0D, 16.0D), Direction.SOUTH, 
/* 22 */         Block.box(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 8.0D), Direction.EAST, 
/* 23 */         Block.box(0.0D, 4.0D, 4.0D, 8.0D, 12.0D, 12.0D), Direction.WEST, 
/* 24 */         Block.box(8.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D)));
/*    */ 
/*    */   
/*    */   protected WallSkullBlock(SkullBlock.Type debug1, BlockBehaviour.Properties debug2) {
/* 28 */     super(debug1, debug2);
/* 29 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescriptionId() {
/* 34 */     return asItem().getDescriptionId();
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 39 */     return AABBS.get(debug1.getValue((Property)FACING));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 44 */     BlockState debug2 = defaultBlockState();
/*    */     
/* 46 */     Level level = debug1.getLevel();
/* 47 */     BlockPos debug4 = debug1.getClickedPos();
/*    */     
/* 49 */     Direction[] debug5 = debug1.getNearestLookingDirections();
/* 50 */     for (Direction debug9 : debug5) {
/* 51 */       if (debug9.getAxis().isHorizontal()) {
/*    */ 
/*    */ 
/*    */         
/* 55 */         Direction debug10 = debug9.getOpposite();
/*    */         
/* 57 */         debug2 = (BlockState)debug2.setValue((Property)FACING, (Comparable)debug10);
/* 58 */         if (!level.getBlockState(debug4.relative(debug9)).canBeReplaced(debug1)) {
/* 59 */           return debug2;
/*    */         }
/*    */       } 
/*    */     } 
/* 63 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 68 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 73 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 78 */     debug1.add(new Property[] { (Property)FACING });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\WallSkullBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */