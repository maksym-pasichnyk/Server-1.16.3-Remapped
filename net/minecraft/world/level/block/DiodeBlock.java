/*     */ package net.minecraft.world.level.block;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.TickPriority;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class DiodeBlock extends HorizontalDirectionalBlock {
/*  24 */   protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
/*     */   
/*  26 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*     */   protected DiodeBlock(BlockBehaviour.Properties debug1) {
/*  29 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  34 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/*  39 */     return canSupportRigidBlock((BlockGetter)debug2, debug3.below());
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  44 */     if (isLocked((LevelReader)debug2, debug3, debug1)) {
/*     */       return;
/*     */     }
/*     */     
/*  48 */     boolean debug5 = ((Boolean)debug1.getValue((Property)POWERED)).booleanValue();
/*  49 */     boolean debug6 = shouldTurnOn((Level)debug2, debug3, debug1);
/*  50 */     if (debug5 && !debug6) {
/*  51 */       debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)POWERED, Boolean.valueOf(false)), 2);
/*  52 */     } else if (!debug5) {
/*     */ 
/*     */       
/*  55 */       debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)POWERED, Boolean.valueOf(true)), 2);
/*  56 */       if (!debug6) {
/*  57 */         debug2.getBlockTicks().scheduleTick(debug3, this, getDelay(debug1), TickPriority.VERY_HIGH);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDirectSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/*  64 */     return debug1.getSignal(debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/*  69 */     if (!((Boolean)debug1.getValue((Property)POWERED)).booleanValue()) {
/*  70 */       return 0;
/*     */     }
/*     */     
/*  73 */     if (debug1.getValue((Property)FACING) == debug4) {
/*  74 */       return getOutputSignal(debug2, debug3, debug1);
/*     */     }
/*     */     
/*  77 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void neighborChanged(BlockState debug1, Level debug2, BlockPos debug3, Block debug4, BlockPos debug5, boolean debug6) {
/*  82 */     if (debug1.canSurvive((LevelReader)debug2, debug3)) {
/*  83 */       checkTickOnNeighbor(debug2, debug3, debug1);
/*     */       
/*     */       return;
/*     */     } 
/*  87 */     BlockEntity debug7 = isEntityBlock() ? debug2.getBlockEntity(debug3) : null;
/*  88 */     dropResources(debug1, (LevelAccessor)debug2, debug3, debug7);
/*  89 */     debug2.removeBlock(debug3, false);
/*  90 */     for (Direction debug11 : Direction.values()) {
/*  91 */       debug2.updateNeighborsAt(debug3.relative(debug11), this);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void checkTickOnNeighbor(Level debug1, BlockPos debug2, BlockState debug3) {
/*  96 */     if (isLocked((LevelReader)debug1, debug2, debug3)) {
/*     */       return;
/*     */     }
/*     */     
/* 100 */     boolean debug4 = ((Boolean)debug3.getValue((Property)POWERED)).booleanValue();
/* 101 */     boolean debug5 = shouldTurnOn(debug1, debug2, debug3);
/* 102 */     if (debug4 != debug5 && !debug1.getBlockTicks().willTickThisTick(debug2, this)) {
/* 103 */       TickPriority debug6 = TickPriority.HIGH;
/*     */ 
/*     */       
/* 106 */       if (shouldPrioritize((BlockGetter)debug1, debug2, debug3)) {
/* 107 */         debug6 = TickPriority.EXTREMELY_HIGH;
/* 108 */       } else if (debug4) {
/* 109 */         debug6 = TickPriority.VERY_HIGH;
/*     */       } 
/*     */       
/* 112 */       debug1.getBlockTicks().scheduleTick(debug2, this, getDelay(debug3), debug6);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isLocked(LevelReader debug1, BlockPos debug2, BlockState debug3) {
/* 117 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean shouldTurnOn(Level debug1, BlockPos debug2, BlockState debug3) {
/* 121 */     return (getInputSignal(debug1, debug2, debug3) > 0);
/*     */   }
/*     */   
/*     */   protected int getInputSignal(Level debug1, BlockPos debug2, BlockState debug3) {
/* 125 */     Direction debug4 = (Direction)debug3.getValue((Property)FACING);
/*     */     
/* 127 */     BlockPos debug5 = debug2.relative(debug4);
/* 128 */     int debug6 = debug1.getSignal(debug5, debug4);
/* 129 */     if (debug6 >= 15) {
/* 130 */       return debug6;
/*     */     }
/*     */     
/* 133 */     BlockState debug7 = debug1.getBlockState(debug5);
/* 134 */     return Math.max(debug6, debug7.is(Blocks.REDSTONE_WIRE) ? ((Integer)debug7.getValue((Property)RedStoneWireBlock.POWER)).intValue() : 0);
/*     */   }
/*     */   
/*     */   protected int getAlternateSignal(LevelReader debug1, BlockPos debug2, BlockState debug3) {
/* 138 */     Direction debug4 = (Direction)debug3.getValue((Property)FACING);
/* 139 */     Direction debug5 = debug4.getClockWise();
/* 140 */     Direction debug6 = debug4.getCounterClockWise();
/* 141 */     return Math.max(getAlternateSignalAt(debug1, debug2.relative(debug5), debug5), getAlternateSignalAt(debug1, debug2.relative(debug6), debug6));
/*     */   }
/*     */   
/*     */   protected int getAlternateSignalAt(LevelReader debug1, BlockPos debug2, Direction debug3) {
/* 145 */     BlockState debug4 = debug1.getBlockState(debug2);
/* 146 */     if (isAlternateInput(debug4)) {
/* 147 */       if (debug4.is(Blocks.REDSTONE_BLOCK)) {
/* 148 */         return 15;
/*     */       }
/* 150 */       if (debug4.is(Blocks.REDSTONE_WIRE)) {
/* 151 */         return ((Integer)debug4.getValue((Property)RedStoneWireBlock.POWER)).intValue();
/*     */       }
/* 153 */       return debug1.getDirectSignal(debug2, debug3);
/*     */     } 
/*     */     
/* 156 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSignalSource(BlockState debug1) {
/* 161 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 166 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug1.getHorizontalDirection().getOpposite());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, LivingEntity debug4, ItemStack debug5) {
/* 171 */     if (shouldTurnOn(debug1, debug2, debug3)) {
/* 172 */       debug1.getBlockTicks().scheduleTick(debug2, this, 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 178 */     updateNeighborsInFront(debug2, debug3, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 183 */     if (debug5 || debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/*     */     
/* 187 */     super.onRemove(debug1, debug2, debug3, debug4, debug5);
/* 188 */     updateNeighborsInFront(debug2, debug3, debug1);
/*     */   }
/*     */   
/*     */   protected void updateNeighborsInFront(Level debug1, BlockPos debug2, BlockState debug3) {
/* 192 */     Direction debug4 = (Direction)debug3.getValue((Property)FACING);
/* 193 */     BlockPos debug5 = debug2.relative(debug4.getOpposite());
/*     */     
/* 195 */     debug1.neighborChanged(debug5, this, debug2);
/* 196 */     debug1.updateNeighborsAtExceptFromFacing(debug5, this, debug4);
/*     */   }
/*     */   
/*     */   protected boolean isAlternateInput(BlockState debug1) {
/* 200 */     return debug1.isSignalSource();
/*     */   }
/*     */   
/*     */   protected int getOutputSignal(BlockGetter debug1, BlockPos debug2, BlockState debug3) {
/* 204 */     return 15;
/*     */   }
/*     */   
/*     */   public static boolean isDiode(BlockState debug0) {
/* 208 */     return debug0.getBlock() instanceof DiodeBlock;
/*     */   }
/*     */   
/*     */   public boolean shouldPrioritize(BlockGetter debug1, BlockPos debug2, BlockState debug3) {
/* 212 */     Direction debug4 = ((Direction)debug3.getValue((Property)FACING)).getOpposite();
/* 213 */     BlockState debug5 = debug1.getBlockState(debug2.relative(debug4));
/*     */     
/* 215 */     return (isDiode(debug5) && debug5.getValue((Property)FACING) != debug4);
/*     */   }
/*     */   
/*     */   protected abstract int getDelay(BlockState paramBlockState);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\DiodeBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */