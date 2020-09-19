/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class ObserverBlock extends DirectionalBlock {
/*  19 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*     */   public ObserverBlock(BlockBehaviour.Properties debug1) {
/*  22 */     super(debug1);
/*     */     
/*  24 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.SOUTH)).setValue((Property)POWERED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/*  29 */     debug1.add(new Property[] { (Property)FACING, (Property)POWERED });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/*  34 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/*  39 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  44 */     if (((Boolean)debug1.getValue((Property)POWERED)).booleanValue()) {
/*  45 */       debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)POWERED, Boolean.valueOf(false)), 2);
/*     */     } else {
/*  47 */       debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)POWERED, Boolean.valueOf(true)), 2);
/*  48 */       debug2.getBlockTicks().scheduleTick(debug3, this, 2);
/*     */     } 
/*  50 */     updateNeighborsInFront((Level)debug2, debug3, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  55 */     if (debug1.getValue((Property)FACING) == debug2 && !((Boolean)debug1.getValue((Property)POWERED)).booleanValue()) {
/*  56 */       startSignal(debug4, debug5);
/*     */     }
/*     */     
/*  59 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */   
/*     */   private void startSignal(LevelAccessor debug1, BlockPos debug2) {
/*  63 */     if (!debug1.isClientSide() && !debug1.getBlockTicks().hasScheduledTick(debug2, this)) {
/*  64 */       debug1.getBlockTicks().scheduleTick(debug2, this, 2);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void updateNeighborsInFront(Level debug1, BlockPos debug2, BlockState debug3) {
/*  69 */     Direction debug4 = (Direction)debug3.getValue((Property)FACING);
/*  70 */     BlockPos debug5 = debug2.relative(debug4.getOpposite());
/*     */     
/*  72 */     debug1.neighborChanged(debug5, this, debug2);
/*  73 */     debug1.updateNeighborsAtExceptFromFacing(debug5, this, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSignalSource(BlockState debug1) {
/*  78 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDirectSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/*  83 */     return debug1.getSignal(debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/*  88 */     if (((Boolean)debug1.getValue((Property)POWERED)).booleanValue() && debug1.getValue((Property)FACING) == debug4) {
/*  89 */       return 15;
/*     */     }
/*  91 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/*  96 */     if (debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/*     */     
/* 100 */     if (!debug2.isClientSide() && ((Boolean)debug1.getValue((Property)POWERED)).booleanValue() && !debug2.getBlockTicks().hasScheduledTick(debug3, this)) {
/* 101 */       BlockState debug6 = (BlockState)debug1.setValue((Property)POWERED, Boolean.valueOf(false));
/*     */       
/* 103 */       debug2.setBlock(debug3, debug6, 18);
/* 104 */       updateNeighborsInFront(debug2, debug3, debug6);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 110 */     if (debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/* 113 */     if (!debug2.isClientSide && ((Boolean)debug1.getValue((Property)POWERED)).booleanValue() && debug2.getBlockTicks().hasScheduledTick(debug3, this))
/*     */     {
/* 115 */       updateNeighborsInFront(debug2, debug3, (BlockState)debug1.setValue((Property)POWERED, Boolean.valueOf(false)));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 121 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug1.getNearestLookingDirection().getOpposite().getOpposite());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\ObserverBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */