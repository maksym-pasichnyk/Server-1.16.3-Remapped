/*     */ package net.minecraft.world.level.block.piston;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DirectionalBlock;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.PistonType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class PistonHeadBlock extends DirectionalBlock {
/*  30 */   public static final EnumProperty<PistonType> TYPE = BlockStateProperties.PISTON_TYPE;
/*  31 */   public static final BooleanProperty SHORT = BlockStateProperties.SHORT;
/*     */ 
/*     */ 
/*     */   
/*  35 */   protected static final VoxelShape EAST_AABB = Block.box(12.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
/*  36 */   protected static final VoxelShape WEST_AABB = Block.box(0.0D, 0.0D, 0.0D, 4.0D, 16.0D, 16.0D);
/*  37 */   protected static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 12.0D, 16.0D, 16.0D, 16.0D);
/*  38 */   protected static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 4.0D);
/*  39 */   protected static final VoxelShape UP_AABB = Block.box(0.0D, 12.0D, 0.0D, 16.0D, 16.0D, 16.0D);
/*  40 */   protected static final VoxelShape DOWN_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   protected static final VoxelShape UP_ARM_AABB = Block.box(6.0D, -4.0D, 6.0D, 10.0D, 12.0D, 10.0D);
/*  47 */   protected static final VoxelShape DOWN_ARM_AABB = Block.box(6.0D, 4.0D, 6.0D, 10.0D, 20.0D, 10.0D);
/*  48 */   protected static final VoxelShape SOUTH_ARM_AABB = Block.box(6.0D, 6.0D, -4.0D, 10.0D, 10.0D, 12.0D);
/*  49 */   protected static final VoxelShape NORTH_ARM_AABB = Block.box(6.0D, 6.0D, 4.0D, 10.0D, 10.0D, 20.0D);
/*  50 */   protected static final VoxelShape EAST_ARM_AABB = Block.box(-4.0D, 6.0D, 6.0D, 12.0D, 10.0D, 10.0D);
/*  51 */   protected static final VoxelShape WEST_ARM_AABB = Block.box(4.0D, 6.0D, 6.0D, 20.0D, 10.0D, 10.0D);
/*     */   
/*  53 */   protected static final VoxelShape SHORT_UP_ARM_AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 12.0D, 10.0D);
/*  54 */   protected static final VoxelShape SHORT_DOWN_ARM_AABB = Block.box(6.0D, 4.0D, 6.0D, 10.0D, 16.0D, 10.0D);
/*  55 */   protected static final VoxelShape SHORT_SOUTH_ARM_AABB = Block.box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 12.0D);
/*  56 */   protected static final VoxelShape SHORT_NORTH_ARM_AABB = Block.box(6.0D, 6.0D, 4.0D, 10.0D, 10.0D, 16.0D);
/*  57 */   protected static final VoxelShape SHORT_EAST_ARM_AABB = Block.box(0.0D, 6.0D, 6.0D, 12.0D, 10.0D, 10.0D);
/*  58 */   protected static final VoxelShape SHORT_WEST_ARM_AABB = Block.box(4.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D);
/*     */   
/*  60 */   private static final VoxelShape[] SHAPES_SHORT = makeShapes(true);
/*  61 */   private static final VoxelShape[] SHAPES_LONG = makeShapes(false);
/*     */   
/*     */   private static VoxelShape[] makeShapes(boolean debug0) {
/*  64 */     return (VoxelShape[])Arrays.<Direction>stream(Direction.values()).map(debug1 -> calculateShape(debug1, debug0)).toArray(debug0 -> new VoxelShape[debug0]);
/*     */   }
/*     */   
/*     */   private static VoxelShape calculateShape(Direction debug0, boolean debug1) {
/*  68 */     switch (debug0)
/*     */     
/*     */     { default:
/*  71 */         return Shapes.or(DOWN_AABB, debug1 ? SHORT_DOWN_ARM_AABB : DOWN_ARM_AABB);
/*     */       case UP:
/*  73 */         return Shapes.or(UP_AABB, debug1 ? SHORT_UP_ARM_AABB : UP_ARM_AABB);
/*     */       case NORTH:
/*  75 */         return Shapes.or(NORTH_AABB, debug1 ? SHORT_NORTH_ARM_AABB : NORTH_ARM_AABB);
/*     */       case SOUTH:
/*  77 */         return Shapes.or(SOUTH_AABB, debug1 ? SHORT_SOUTH_ARM_AABB : SOUTH_ARM_AABB);
/*     */       case WEST:
/*  79 */         return Shapes.or(WEST_AABB, debug1 ? SHORT_WEST_ARM_AABB : WEST_ARM_AABB);
/*     */       case EAST:
/*  81 */         break; }  return Shapes.or(EAST_AABB, debug1 ? SHORT_EAST_ARM_AABB : EAST_ARM_AABB);
/*     */   }
/*     */ 
/*     */   
/*     */   public PistonHeadBlock(BlockBehaviour.Properties debug1) {
/*  86 */     super(debug1);
/*  87 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)TYPE, (Comparable)PistonType.DEFAULT)).setValue((Property)SHORT, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean useShapeForLightOcclusion(BlockState debug1) {
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  97 */     return (((Boolean)debug1.getValue((Property)SHORT)).booleanValue() ? SHAPES_SHORT : SHAPES_LONG)[((Direction)debug1.getValue((Property)FACING)).ordinal()];
/*     */   }
/*     */   
/*     */   private boolean isFittingBase(BlockState debug1, BlockState debug2) {
/* 101 */     Block debug3 = (debug1.getValue((Property)TYPE) == PistonType.DEFAULT) ? Blocks.PISTON : Blocks.STICKY_PISTON;
/* 102 */     return (debug2.is(debug3) && ((Boolean)debug2.getValue((Property)PistonBaseBlock.EXTENDED)).booleanValue() && debug2.getValue((Property)FACING) == debug1.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerWillDestroy(Level debug1, BlockPos debug2, BlockState debug3, Player debug4) {
/* 107 */     if (!debug1.isClientSide && debug4.abilities.instabuild) {
/* 108 */       BlockPos debug5 = debug2.relative(((Direction)debug3.getValue((Property)FACING)).getOpposite());
/* 109 */       if (isFittingBase(debug3, debug1.getBlockState(debug5))) {
/* 110 */         debug1.destroyBlock(debug5, false);
/*     */       }
/*     */     } 
/* 113 */     super.playerWillDestroy(debug1, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 118 */     if (debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/* 121 */     super.onRemove(debug1, debug2, debug3, debug4, debug5);
/*     */ 
/*     */     
/* 124 */     BlockPos debug6 = debug3.relative(((Direction)debug1.getValue((Property)FACING)).getOpposite());
/* 125 */     if (isFittingBase(debug1, debug2.getBlockState(debug6))) {
/* 126 */       debug2.destroyBlock(debug6, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 132 */     if (debug2.getOpposite() == debug1.getValue((Property)FACING) && 
/* 133 */       !debug1.canSurvive((LevelReader)debug4, debug5)) {
/* 134 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 137 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 142 */     BlockState debug4 = debug2.getBlockState(debug3.relative(((Direction)debug1.getValue((Property)FACING)).getOpposite()));
/*     */     
/* 144 */     return (isFittingBase(debug1, debug4) || (debug4.is(Blocks.MOVING_PISTON) && debug4.getValue((Property)FACING) == debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void neighborChanged(BlockState debug1, Level debug2, BlockPos debug3, Block debug4, BlockPos debug5, boolean debug6) {
/* 149 */     if (debug1.canSurvive((LevelReader)debug2, debug3)) {
/* 150 */       BlockPos debug7 = debug3.relative(((Direction)debug1.getValue((Property)FACING)).getOpposite());
/* 151 */       debug2.getBlockState(debug7).neighborChanged(debug2, debug7, debug4, debug5, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 162 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 167 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 172 */     debug1.add(new Property[] { (Property)FACING, (Property)TYPE, (Property)SHORT });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 177 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\piston\PistonHeadBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */