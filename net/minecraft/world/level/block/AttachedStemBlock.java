/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class AttachedStemBlock
/*    */   extends BushBlock
/*    */ {
/* 21 */   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
/*    */ 
/*    */   
/*    */   private final StemGrownBlock fruit;
/*    */   
/* 26 */   private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap((Map)ImmutableMap.of(Direction.SOUTH, 
/* 27 */         Block.box(6.0D, 0.0D, 6.0D, 10.0D, 10.0D, 16.0D), Direction.WEST, 
/* 28 */         Block.box(0.0D, 0.0D, 6.0D, 10.0D, 10.0D, 10.0D), Direction.NORTH, 
/* 29 */         Block.box(6.0D, 0.0D, 0.0D, 10.0D, 10.0D, 10.0D), Direction.EAST, 
/* 30 */         Block.box(6.0D, 0.0D, 6.0D, 16.0D, 10.0D, 10.0D)));
/*    */ 
/*    */   
/*    */   protected AttachedStemBlock(StemGrownBlock debug1, BlockBehaviour.Properties debug2) {
/* 34 */     super(debug2);
/* 35 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH));
/* 36 */     this.fruit = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 41 */     return AABBS.get(debug1.getValue((Property)FACING));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 46 */     if (!debug3.is(this.fruit) && debug2 == debug1.getValue((Property)FACING)) {
/* 47 */       return (BlockState)this.fruit.getStem().defaultBlockState().setValue((Property)StemBlock.AGE, Integer.valueOf(7));
/*    */     }
/* 49 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/* 54 */     return debug1.is(Blocks.FARMLAND);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 76 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 81 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 86 */     debug1.add(new Property[] { (Property)FACING });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\AttachedStemBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */