/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.GameRules;
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
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FarmBlock
/*     */   extends Block {
/*  28 */   public static final IntegerProperty MOISTURE = BlockStateProperties.MOISTURE;
/*  29 */   protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);
/*     */ 
/*     */ 
/*     */   
/*     */   protected FarmBlock(BlockBehaviour.Properties debug1) {
/*  34 */     super(debug1);
/*  35 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)MOISTURE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  40 */     if (debug2 == Direction.UP && !debug1.canSurvive((LevelReader)debug4, debug5)) {
/*  41 */       debug4.getBlockTicks().scheduleTick(debug5, this, 1);
/*     */     }
/*  43 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/*  48 */     BlockState debug4 = debug2.getBlockState(debug3.above());
/*  49 */     return (!debug4.getMaterial().isSolid() || debug4.getBlock() instanceof FenceGateBlock || debug4.getBlock() instanceof net.minecraft.world.level.block.piston.MovingPistonBlock);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/*  54 */     if (!defaultBlockState().canSurvive((LevelReader)debug1.getLevel(), debug1.getClickedPos())) {
/*  55 */       return Blocks.DIRT.defaultBlockState();
/*     */     }
/*  57 */     return super.getStateForPlacement(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean useShapeForLightOcclusion(BlockState debug1) {
/*  62 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  67 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  72 */     if (!debug1.canSurvive((LevelReader)debug2, debug3)) {
/*  73 */       turnToDirt(debug1, (Level)debug2, debug3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  79 */     int debug5 = ((Integer)debug1.getValue((Property)MOISTURE)).intValue();
/*  80 */     if (isNearWater((LevelReader)debug2, debug3) || debug2.isRainingAt(debug3.above())) {
/*  81 */       if (debug5 < 7) {
/*  82 */         debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)MOISTURE, Integer.valueOf(7)), 2);
/*     */       }
/*  84 */     } else if (debug5 > 0) {
/*  85 */       debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)MOISTURE, Integer.valueOf(debug5 - 1)), 2);
/*  86 */     } else if (!isUnderCrops((BlockGetter)debug2, debug3)) {
/*  87 */       turnToDirt(debug1, (Level)debug2, debug3);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void fallOn(Level debug1, BlockPos debug2, Entity debug3, float debug4) {
/*  93 */     if (!debug1.isClientSide && debug1.random.nextFloat() < debug4 - 0.5F && debug3 instanceof net.minecraft.world.entity.LivingEntity && (
/*  94 */       debug3 instanceof net.minecraft.world.entity.player.Player || debug1.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)))
/*     */     {
/*  96 */       if (debug3.getBbWidth() * debug3.getBbWidth() * debug3.getBbHeight() > 0.512F) {
/*  97 */         turnToDirt(debug1.getBlockState(debug2), debug1, debug2);
/*     */       }
/*     */     }
/*     */     
/* 101 */     super.fallOn(debug1, debug2, debug3, debug4);
/*     */   }
/*     */   
/*     */   public static void turnToDirt(BlockState debug0, Level debug1, BlockPos debug2) {
/* 105 */     debug1.setBlockAndUpdate(debug2, pushEntitiesUp(debug0, Blocks.DIRT.defaultBlockState(), debug1, debug2));
/*     */   }
/*     */   
/*     */   private static boolean isUnderCrops(BlockGetter debug0, BlockPos debug1) {
/* 109 */     Block debug2 = debug0.getBlockState(debug1.above()).getBlock();
/* 110 */     return (debug2 instanceof CropBlock || debug2 instanceof StemBlock || debug2 instanceof AttachedStemBlock);
/*     */   }
/*     */   
/*     */   private static boolean isNearWater(LevelReader debug0, BlockPos debug1) {
/* 114 */     for (BlockPos debug3 : BlockPos.betweenClosed(debug1.offset(-4, 0, -4), debug1.offset(4, 1, 4))) {
/* 115 */       if (debug0.getFluidState(debug3).is((Tag)FluidTags.WATER)) {
/* 116 */         return true;
/*     */       }
/*     */     } 
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 124 */     debug1.add(new Property[] { (Property)MOISTURE });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 129 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\FarmBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */