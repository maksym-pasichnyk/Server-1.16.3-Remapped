/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.DustParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LeverBlock extends FaceAttachedHorizontalDirectionalBlock {
/*  27 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  33 */   protected static final VoxelShape NORTH_AABB = Block.box(5.0D, 4.0D, 10.0D, 11.0D, 12.0D, 16.0D);
/*  34 */   protected static final VoxelShape SOUTH_AABB = Block.box(5.0D, 4.0D, 0.0D, 11.0D, 12.0D, 6.0D);
/*  35 */   protected static final VoxelShape WEST_AABB = Block.box(10.0D, 4.0D, 5.0D, 16.0D, 12.0D, 11.0D);
/*  36 */   protected static final VoxelShape EAST_AABB = Block.box(0.0D, 4.0D, 5.0D, 6.0D, 12.0D, 11.0D);
/*     */   
/*  38 */   protected static final VoxelShape UP_AABB_Z = Block.box(5.0D, 0.0D, 4.0D, 11.0D, 6.0D, 12.0D);
/*  39 */   protected static final VoxelShape UP_AABB_X = Block.box(4.0D, 0.0D, 5.0D, 12.0D, 6.0D, 11.0D);
/*     */   
/*  41 */   protected static final VoxelShape DOWN_AABB_Z = Block.box(5.0D, 10.0D, 4.0D, 11.0D, 16.0D, 12.0D);
/*  42 */   protected static final VoxelShape DOWN_AABB_X = Block.box(4.0D, 10.0D, 5.0D, 12.0D, 16.0D, 11.0D);
/*     */   
/*     */   protected LeverBlock(BlockBehaviour.Properties debug1) {
/*  45 */     super(debug1);
/*  46 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)FACE, (Comparable)AttachFace.WALL));
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  51 */     switch ((AttachFace)debug1.getValue((Property)FACE)) {
/*     */       case FLOOR:
/*  53 */         switch (((Direction)debug1.getValue((Property)FACING)).getAxis()) {
/*     */           case FLOOR:
/*  55 */             return UP_AABB_X;
/*     */         } 
/*     */         
/*  58 */         return UP_AABB_Z;
/*     */       
/*     */       case WALL:
/*  61 */         switch ((Direction)debug1.getValue((Property)FACING)) {
/*     */           case FLOOR:
/*  63 */             return EAST_AABB;
/*     */           case WALL:
/*  65 */             return WEST_AABB;
/*     */           case CEILING:
/*  67 */             return SOUTH_AABB;
/*     */         } 
/*     */         
/*  70 */         return NORTH_AABB;
/*     */     } 
/*     */ 
/*     */     
/*  74 */     switch (((Direction)debug1.getValue((Property)FACING)).getAxis()) {
/*     */       case FLOOR:
/*  76 */         return DOWN_AABB_X;
/*     */     } 
/*     */     
/*  79 */     return DOWN_AABB_Z;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  87 */     if (debug2.isClientSide) {
/*  88 */       BlockState blockState = (BlockState)debug1.cycle((Property)POWERED);
/*  89 */       if (((Boolean)blockState.getValue((Property)POWERED)).booleanValue()) {
/*  90 */         makeParticle(blockState, (LevelAccessor)debug2, debug3, 1.0F);
/*     */       }
/*  92 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/*  95 */     BlockState debug7 = pull(debug1, debug2, debug3);
/*     */     
/*  97 */     float debug8 = ((Boolean)debug7.getValue((Property)POWERED)).booleanValue() ? 0.6F : 0.5F;
/*  98 */     debug2.playSound(null, debug3, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, debug8);
/*     */     
/* 100 */     return InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState pull(BlockState debug1, Level debug2, BlockPos debug3) {
/* 107 */     debug1 = (BlockState)debug1.cycle((Property)POWERED);
/* 108 */     debug2.setBlock(debug3, debug1, 3);
/* 109 */     updateNeighbours(debug1, debug2, debug3);
/* 110 */     return debug1;
/*     */   }
/*     */   
/*     */   private static void makeParticle(BlockState debug0, LevelAccessor debug1, BlockPos debug2, float debug3) {
/* 114 */     Direction debug4 = ((Direction)debug0.getValue((Property)FACING)).getOpposite();
/* 115 */     Direction debug5 = getConnectedDirection(debug0).getOpposite();
/* 116 */     double debug6 = debug2.getX() + 0.5D + 0.1D * debug4.getStepX() + 0.2D * debug5.getStepX();
/* 117 */     double debug8 = debug2.getY() + 0.5D + 0.1D * debug4.getStepY() + 0.2D * debug5.getStepY();
/* 118 */     double debug10 = debug2.getZ() + 0.5D + 0.1D * debug4.getStepZ() + 0.2D * debug5.getStepZ();
/*     */     
/* 120 */     debug1.addParticle((ParticleOptions)new DustParticleOptions(1.0F, 0.0F, 0.0F, debug3), debug6, debug8, debug10, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 132 */     if (debug5 || debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/* 135 */     if (((Boolean)debug1.getValue((Property)POWERED)).booleanValue()) {
/* 136 */       updateNeighbours(debug1, debug2, debug3);
/*     */     }
/* 138 */     super.onRemove(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/* 143 */     return ((Boolean)debug1.getValue((Property)POWERED)).booleanValue() ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDirectSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/* 148 */     if (((Boolean)debug1.getValue((Property)POWERED)).booleanValue() && getConnectedDirection(debug1) == debug4) {
/* 149 */       return 15;
/*     */     }
/* 151 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSignalSource(BlockState debug1) {
/* 156 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateNeighbours(BlockState debug1, Level debug2, BlockPos debug3) {
/* 161 */     debug2.updateNeighborsAt(debug3, this);
/* 162 */     debug2.updateNeighborsAt(debug3.relative(getConnectedDirection(debug1).getOpposite()), this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 167 */     debug1.add(new Property[] { (Property)FACE, (Property)FACING, (Property)POWERED });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\LeverBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */