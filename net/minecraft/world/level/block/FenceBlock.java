/*     */ package net.minecraft.world.level.block;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FenceBlock extends CrossCollisionBlock {
/*     */   public FenceBlock(BlockBehaviour.Properties debug1) {
/*  29 */     super(2.0F, 2.0F, 16.0F, 16.0F, 24.0F, debug1);
/*  30 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)NORTH, Boolean.valueOf(false))).setValue((Property)EAST, Boolean.valueOf(false))).setValue((Property)SOUTH, Boolean.valueOf(false))).setValue((Property)WEST, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */     
/*  32 */     this.occlusionByIndex = makeShapes(2.0F, 1.0F, 16.0F, 6.0F, 15.0F);
/*     */   }
/*     */   private final VoxelShape[] occlusionByIndex;
/*     */   
/*     */   public VoxelShape getOcclusionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/*  37 */     return this.occlusionByIndex[getAABBIndex(debug1)];
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getVisualShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  42 */     return getShape(debug1, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/*  47 */     return false;
/*     */   }
/*     */   
/*     */   public boolean connectsTo(BlockState debug1, boolean debug2, Direction debug3) {
/*  51 */     Block debug4 = debug1.getBlock();
/*     */     
/*  53 */     boolean debug5 = isSameFence(debug4);
/*  54 */     boolean debug6 = (debug4 instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(debug1, debug3));
/*  55 */     return ((!isExceptionForConnection(debug4) && debug2) || debug5 || debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isSameFence(Block debug1) {
/*  60 */     return (debug1.is((Tag<Block>)BlockTags.FENCES) && debug1.is((Tag<Block>)BlockTags.WOODEN_FENCES) == defaultBlockState().is((Tag)BlockTags.WOODEN_FENCES));
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  65 */     if (debug2.isClientSide) {
/*  66 */       ItemStack debug7 = debug4.getItemInHand(debug5);
/*  67 */       if (debug7.getItem() == Items.LEAD) {
/*  68 */         return InteractionResult.SUCCESS;
/*     */       }
/*  70 */       return InteractionResult.PASS;
/*     */     } 
/*     */ 
/*     */     
/*  74 */     return LeadItem.bindPlayerMobs(debug4, debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/*  79 */     Level level = debug1.getLevel();
/*  80 */     BlockPos debug3 = debug1.getClickedPos();
/*  81 */     FluidState debug4 = debug1.getLevel().getFluidState(debug1.getClickedPos());
/*     */ 
/*     */     
/*  84 */     BlockPos debug5 = debug3.north();
/*  85 */     BlockPos debug6 = debug3.east();
/*  86 */     BlockPos debug7 = debug3.south();
/*  87 */     BlockPos debug8 = debug3.west();
/*     */     
/*  89 */     BlockState debug9 = level.getBlockState(debug5);
/*  90 */     BlockState debug10 = level.getBlockState(debug6);
/*  91 */     BlockState debug11 = level.getBlockState(debug7);
/*  92 */     BlockState debug12 = level.getBlockState(debug8);
/*     */     
/*  94 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)super.getStateForPlacement(debug1)
/*  95 */       .setValue((Property)NORTH, Boolean.valueOf(connectsTo(debug9, debug9.isFaceSturdy((BlockGetter)level, debug5, Direction.SOUTH), Direction.SOUTH))))
/*  96 */       .setValue((Property)EAST, Boolean.valueOf(connectsTo(debug10, debug10.isFaceSturdy((BlockGetter)level, debug6, Direction.WEST), Direction.WEST))))
/*  97 */       .setValue((Property)SOUTH, Boolean.valueOf(connectsTo(debug11, debug11.isFaceSturdy((BlockGetter)level, debug7, Direction.NORTH), Direction.NORTH))))
/*  98 */       .setValue((Property)WEST, Boolean.valueOf(connectsTo(debug12, debug12.isFaceSturdy((BlockGetter)level, debug8, Direction.EAST), Direction.EAST))))
/*  99 */       .setValue((Property)WATERLOGGED, Boolean.valueOf((debug4.getType() == Fluids.WATER)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 104 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 105 */       debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/*     */     }
/* 107 */     if (debug2.getAxis().getPlane() == Direction.Plane.HORIZONTAL) {
/* 108 */       return (BlockState)debug1.setValue((Property)PROPERTY_BY_DIRECTION.get(debug2), Boolean.valueOf(connectsTo(debug3, debug3.isFaceSturdy((BlockGetter)debug4, debug6, debug2.getOpposite()), debug2.getOpposite())));
/*     */     }
/* 110 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 115 */     debug1.add(new Property[] { (Property)NORTH, (Property)EAST, (Property)WEST, (Property)SOUTH, (Property)WATERLOGGED });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\FenceBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */