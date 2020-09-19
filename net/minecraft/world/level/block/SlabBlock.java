/*     */ package net.minecraft.world.level.block;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.SlabType;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SlabBlock extends Block implements SimpleWaterloggedBlock {
/*  27 */   public static final EnumProperty<SlabType> TYPE = BlockStateProperties.SLAB_TYPE;
/*  28 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  30 */   protected static final VoxelShape BOTTOM_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
/*  31 */   protected static final VoxelShape TOP_AABB = Block.box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);
/*     */   
/*     */   public SlabBlock(BlockBehaviour.Properties debug1) {
/*  34 */     super(debug1);
/*     */     
/*  36 */     registerDefaultState((BlockState)((BlockState)defaultBlockState().setValue((Property)TYPE, (Comparable)SlabType.BOTTOM)).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean useShapeForLightOcclusion(BlockState debug1) {
/*  41 */     return (debug1.getValue((Property)TYPE) != SlabType.DOUBLE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/*  46 */     debug1.add(new Property[] { (Property)TYPE, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  51 */     SlabType debug5 = (SlabType)debug1.getValue((Property)TYPE);
/*  52 */     switch (debug5) {
/*     */       case LAND:
/*  54 */         return Shapes.block();
/*     */       case WATER:
/*  56 */         return TOP_AABB;
/*     */     } 
/*  58 */     return BOTTOM_AABB;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/*  65 */     BlockPos debug2 = debug1.getClickedPos();
/*  66 */     BlockState debug3 = debug1.getLevel().getBlockState(debug2);
/*  67 */     if (debug3.is(this)) {
/*  68 */       return (BlockState)((BlockState)debug3.setValue((Property)TYPE, (Comparable)SlabType.DOUBLE)).setValue((Property)WATERLOGGED, Boolean.valueOf(false));
/*     */     }
/*     */     
/*  71 */     FluidState debug4 = debug1.getLevel().getFluidState(debug2);
/*  72 */     BlockState debug5 = (BlockState)((BlockState)defaultBlockState().setValue((Property)TYPE, (Comparable)SlabType.BOTTOM)).setValue((Property)WATERLOGGED, Boolean.valueOf((debug4.getType() == Fluids.WATER)));
/*     */     
/*  74 */     Direction debug6 = debug1.getClickedFace();
/*  75 */     if (debug6 == Direction.DOWN || (debug6 != Direction.UP && (debug1.getClickLocation()).y - debug2.getY() > 0.5D)) {
/*  76 */       return (BlockState)debug5.setValue((Property)TYPE, (Comparable)SlabType.TOP);
/*     */     }
/*  78 */     return debug5;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeReplaced(BlockState debug1, BlockPlaceContext debug2) {
/*  83 */     ItemStack debug3 = debug2.getItemInHand();
/*     */     
/*  85 */     SlabType debug4 = (SlabType)debug1.getValue((Property)TYPE);
/*  86 */     if (debug4 == SlabType.DOUBLE || debug3.getItem() != asItem()) {
/*  87 */       return false;
/*     */     }
/*     */     
/*  90 */     if (debug2.replacingClickedOnBlock()) {
/*  91 */       boolean debug5 = ((debug2.getClickLocation()).y - debug2.getClickedPos().getY() > 0.5D);
/*  92 */       Direction debug6 = debug2.getClickedFace();
/*  93 */       if (debug4 == SlabType.BOTTOM) {
/*  94 */         return (debug6 == Direction.UP || (debug5 && debug6.getAxis().isHorizontal()));
/*     */       }
/*  96 */       return (debug6 == Direction.DOWN || (!debug5 && debug6.getAxis().isHorizontal()));
/*     */     } 
/*     */     
/*  99 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockState debug1) {
/* 104 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 105 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 107 */     return super.getFluidState(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean placeLiquid(LevelAccessor debug1, BlockPos debug2, BlockState debug3, FluidState debug4) {
/* 112 */     if (debug3.getValue((Property)TYPE) != SlabType.DOUBLE) {
/* 113 */       return super.placeLiquid(debug1, debug2, debug3, debug4);
/*     */     }
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceLiquid(BlockGetter debug1, BlockPos debug2, BlockState debug3, Fluid debug4) {
/* 120 */     if (debug3.getValue((Property)TYPE) != SlabType.DOUBLE) {
/* 121 */       return super.canPlaceLiquid(debug1, debug2, debug3, debug4);
/*     */     }
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 128 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 129 */       debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/*     */     }
/* 131 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 136 */     switch (debug4) {
/*     */       case LAND:
/* 138 */         return false;
/*     */       case WATER:
/* 140 */         return debug2.getFluidState(debug3).is((Tag)FluidTags.WATER);
/*     */       case AIR:
/* 142 */         return false;
/*     */     } 
/* 144 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\SlabBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */