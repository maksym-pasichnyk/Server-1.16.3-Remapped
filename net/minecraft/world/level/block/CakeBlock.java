/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CakeBlock extends Block {
/*  26 */   public static final IntegerProperty BITES = BlockStateProperties.BITES;
/*     */ 
/*     */ 
/*     */   
/*  30 */   protected static final VoxelShape[] SHAPE_BY_BITE = new VoxelShape[] {
/*  31 */       Block.box(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), 
/*  32 */       Block.box(3.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), 
/*  33 */       Block.box(5.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), 
/*  34 */       Block.box(7.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), 
/*  35 */       Block.box(9.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), 
/*  36 */       Block.box(11.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), 
/*  37 */       Block.box(13.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D)
/*     */     };
/*     */   
/*     */   protected CakeBlock(BlockBehaviour.Properties debug1) {
/*  41 */     super(debug1);
/*  42 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)BITES, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  47 */     return SHAPE_BY_BITE[((Integer)debug1.getValue((Property)BITES)).intValue()];
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  52 */     if (debug2.isClientSide) {
/*  53 */       ItemStack debug7 = debug4.getItemInHand(debug5);
/*  54 */       if (eat((LevelAccessor)debug2, debug3, debug1, debug4).consumesAction())
/*  55 */         return InteractionResult.SUCCESS; 
/*  56 */       if (debug7.isEmpty()) {
/*  57 */         return InteractionResult.CONSUME;
/*     */       }
/*     */     } 
/*     */     
/*  61 */     return eat((LevelAccessor)debug2, debug3, debug1, debug4);
/*     */   }
/*     */   
/*     */   private InteractionResult eat(LevelAccessor debug1, BlockPos debug2, BlockState debug3, Player debug4) {
/*  65 */     if (!debug4.canEat(false)) {
/*  66 */       return InteractionResult.PASS;
/*     */     }
/*  68 */     debug4.awardStat(Stats.EAT_CAKE_SLICE);
/*     */     
/*  70 */     debug4.getFoodData().eat(2, 0.1F);
/*  71 */     int debug5 = ((Integer)debug3.getValue((Property)BITES)).intValue();
/*  72 */     if (debug5 < 6) {
/*  73 */       debug1.setBlock(debug2, (BlockState)debug3.setValue((Property)BITES, Integer.valueOf(debug5 + 1)), 3);
/*     */     } else {
/*  75 */       debug1.removeBlock(debug2, false);
/*     */     } 
/*     */     
/*  78 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  83 */     if (debug2 == Direction.DOWN && !debug1.canSurvive((LevelReader)debug4, debug5)) {
/*  84 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  87 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/*  92 */     return debug2.getBlockState(debug3.below()).getMaterial().isSolid();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/*  97 */     debug1.add(new Property[] { (Property)BITES });
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAnalogOutputSignal(BlockState debug1, Level debug2, BlockPos debug3) {
/* 102 */     return (7 - ((Integer)debug1.getValue((Property)BITES)).intValue()) * 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnalogOutputSignal(BlockState debug1) {
/* 107 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 112 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\CakeBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */