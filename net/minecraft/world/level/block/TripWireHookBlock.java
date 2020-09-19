/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class TripWireHookBlock extends Block {
/*  29 */   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
/*  30 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*  31 */   public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   protected static final VoxelShape NORTH_AABB = Block.box(5.0D, 0.0D, 10.0D, 11.0D, 10.0D, 16.0D);
/*  39 */   protected static final VoxelShape SOUTH_AABB = Block.box(5.0D, 0.0D, 0.0D, 11.0D, 10.0D, 6.0D);
/*  40 */   protected static final VoxelShape WEST_AABB = Block.box(10.0D, 0.0D, 5.0D, 16.0D, 10.0D, 11.0D);
/*  41 */   protected static final VoxelShape EAST_AABB = Block.box(0.0D, 0.0D, 5.0D, 6.0D, 10.0D, 11.0D);
/*     */   
/*     */   public TripWireHookBlock(BlockBehaviour.Properties debug1) {
/*  44 */     super(debug1);
/*  45 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)ATTACHED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  50 */     switch ((Direction)debug1.getValue((Property)FACING))
/*     */     
/*     */     { default:
/*  53 */         return EAST_AABB;
/*     */       case WEST:
/*  55 */         return WEST_AABB;
/*     */       case SOUTH:
/*  57 */         return SOUTH_AABB;
/*     */       case NORTH:
/*  59 */         break; }  return NORTH_AABB;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/*  65 */     Direction debug4 = (Direction)debug1.getValue((Property)FACING);
/*  66 */     BlockPos debug5 = debug3.relative(debug4.getOpposite());
/*  67 */     BlockState debug6 = debug2.getBlockState(debug5);
/*  68 */     return (debug4.getAxis().isHorizontal() && debug6.isFaceSturdy((BlockGetter)debug2, debug5, debug4));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  73 */     if (debug2.getOpposite() == debug1.getValue((Property)FACING) && !debug1.canSurvive((LevelReader)debug4, debug5)) {
/*  74 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  76 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/*  82 */     BlockState debug2 = (BlockState)((BlockState)defaultBlockState().setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)ATTACHED, Boolean.valueOf(false));
/*     */     
/*  84 */     Level level = debug1.getLevel();
/*  85 */     BlockPos debug4 = debug1.getClickedPos();
/*     */     
/*  87 */     Direction[] debug5 = debug1.getNearestLookingDirections();
/*  88 */     for (Direction debug9 : debug5) {
/*  89 */       if (debug9.getAxis().isHorizontal()) {
/*     */ 
/*     */ 
/*     */         
/*  93 */         Direction debug10 = debug9.getOpposite();
/*     */         
/*  95 */         debug2 = (BlockState)debug2.setValue((Property)FACING, (Comparable)debug10);
/*  96 */         if (debug2.canSurvive((LevelReader)level, debug4)) {
/*  97 */           return debug2;
/*     */         }
/*     */       } 
/*     */     } 
/* 101 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, LivingEntity debug4, ItemStack debug5) {
/* 106 */     calculateState(debug1, debug2, debug3, false, false, -1, (BlockState)null);
/*     */   }
/*     */   public void calculateState(Level debug1, BlockPos debug2, BlockState debug3, boolean debug4, boolean debug5, int debug6, @Nullable BlockState debug7) {
/*     */     int j;
/* 110 */     Direction debug8 = (Direction)debug3.getValue((Property)FACING);
/* 111 */     int debug9 = ((Boolean)debug3.getValue((Property)ATTACHED)).booleanValue();
/* 112 */     boolean debug10 = ((Boolean)debug3.getValue((Property)POWERED)).booleanValue();
/*     */     
/* 114 */     boolean debug11 = !debug4;
/* 115 */     boolean debug12 = false;
/* 116 */     int debug13 = 0;
/*     */     
/* 118 */     BlockState[] debug14 = new BlockState[42];
/* 119 */     for (int k = 1; k < 42; k++) {
/* 120 */       BlockPos debug16 = debug2.relative(debug8, k);
/* 121 */       BlockState debug17 = debug1.getBlockState(debug16);
/*     */       
/* 123 */       if (debug17.is(Blocks.TRIPWIRE_HOOK)) {
/* 124 */         if (debug17.getValue((Property)FACING) == debug8.getOpposite()) {
/* 125 */           debug13 = k;
/*     */         }
/*     */         break;
/*     */       } 
/* 129 */       if (debug17.is(Blocks.TRIPWIRE) || k == debug6) {
/* 130 */         if (k == debug6) {
/* 131 */           debug17 = (BlockState)MoreObjects.firstNonNull(debug7, debug17);
/*     */         }
/* 133 */         boolean debug18 = !((Boolean)debug17.getValue((Property)TripWireBlock.DISARMED)).booleanValue();
/* 134 */         boolean debug19 = ((Boolean)debug17.getValue((Property)TripWireBlock.POWERED)).booleanValue();
/* 135 */         j = debug12 | ((debug18 && debug19) ? 1 : 0);
/*     */         
/* 137 */         debug14[k] = debug17;
/*     */         
/* 139 */         if (k == debug6) {
/* 140 */           debug1.getBlockTicks().scheduleTick(debug2, this, 10);
/* 141 */           debug11 &= debug18;
/*     */         } 
/*     */       } else {
/* 144 */         debug14[k] = null;
/* 145 */         debug11 = false;
/*     */       } 
/*     */     } 
/*     */     
/* 149 */     int i = debug11 & ((debug13 > 1) ? 1 : 0);
/* 150 */     j &= i;
/* 151 */     BlockState debug15 = (BlockState)((BlockState)defaultBlockState().setValue((Property)ATTACHED, Boolean.valueOf(i))).setValue((Property)POWERED, Boolean.valueOf(j));
/*     */     
/* 153 */     if (debug13 > 0) {
/* 154 */       BlockPos debug16 = debug2.relative(debug8, debug13);
/* 155 */       Direction debug17 = debug8.getOpposite();
/* 156 */       debug1.setBlock(debug16, (BlockState)debug15.setValue((Property)FACING, (Comparable)debug17), 3);
/* 157 */       notifyNeighbors(debug1, debug16, debug17);
/*     */       
/* 159 */       playSound(debug1, debug16, i, j, debug9, debug10);
/*     */     } 
/*     */     
/* 162 */     playSound(debug1, debug2, i, j, debug9, debug10);
/*     */     
/* 164 */     if (!debug4) {
/* 165 */       debug1.setBlock(debug2, (BlockState)debug15.setValue((Property)FACING, (Comparable)debug8), 3);
/* 166 */       if (debug5) {
/* 167 */         notifyNeighbors(debug1, debug2, debug8);
/*     */       }
/*     */     } 
/*     */     
/* 171 */     if (debug9 != i) {
/* 172 */       for (int debug16 = 1; debug16 < debug13; debug16++) {
/* 173 */         BlockPos debug17 = debug2.relative(debug8, debug16);
/* 174 */         BlockState debug18 = debug14[debug16];
/* 175 */         if (debug18 != null) {
/*     */ 
/*     */ 
/*     */           
/* 179 */           debug1.setBlock(debug17, (BlockState)debug18.setValue((Property)ATTACHED, Boolean.valueOf(i)), 3);
/* 180 */           if (!debug1.getBlockState(debug17).isAir());
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 188 */     calculateState((Level)debug2, debug3, debug1, false, true, -1, (BlockState)null);
/*     */   }
/*     */   
/*     */   private void playSound(Level debug1, BlockPos debug2, boolean debug3, boolean debug4, boolean debug5, boolean debug6) {
/* 192 */     if (debug4 && !debug6) {
/* 193 */       debug1.playSound(null, debug2, SoundEvents.TRIPWIRE_CLICK_ON, SoundSource.BLOCKS, 0.4F, 0.6F);
/* 194 */     } else if (!debug4 && debug6) {
/* 195 */       debug1.playSound(null, debug2, SoundEvents.TRIPWIRE_CLICK_OFF, SoundSource.BLOCKS, 0.4F, 0.5F);
/* 196 */     } else if (debug3 && !debug5) {
/* 197 */       debug1.playSound(null, debug2, SoundEvents.TRIPWIRE_ATTACH, SoundSource.BLOCKS, 0.4F, 0.7F);
/* 198 */     } else if (!debug3 && debug5) {
/* 199 */       debug1.playSound(null, debug2, SoundEvents.TRIPWIRE_DETACH, SoundSource.BLOCKS, 0.4F, 1.2F / (debug1.random.nextFloat() * 0.2F + 0.9F));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void notifyNeighbors(Level debug1, BlockPos debug2, Direction debug3) {
/* 204 */     debug1.updateNeighborsAt(debug2, this);
/* 205 */     debug1.updateNeighborsAt(debug2.relative(debug3.getOpposite()), this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 210 */     if (debug5 || debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/* 213 */     boolean debug6 = ((Boolean)debug1.getValue((Property)ATTACHED)).booleanValue();
/* 214 */     boolean debug7 = ((Boolean)debug1.getValue((Property)POWERED)).booleanValue();
/*     */     
/* 216 */     if (debug6 || debug7) {
/* 217 */       calculateState(debug2, debug3, debug1, true, false, -1, (BlockState)null);
/*     */     }
/*     */     
/* 220 */     if (debug7) {
/* 221 */       debug2.updateNeighborsAt(debug3, this);
/* 222 */       debug2.updateNeighborsAt(debug3.relative(((Direction)debug1.getValue((Property)FACING)).getOpposite()), this);
/*     */     } 
/*     */     
/* 225 */     super.onRemove(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/* 230 */     return ((Boolean)debug1.getValue((Property)POWERED)).booleanValue() ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDirectSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/* 235 */     if (!((Boolean)debug1.getValue((Property)POWERED)).booleanValue()) {
/* 236 */       return 0;
/*     */     }
/*     */     
/* 239 */     if (debug1.getValue((Property)FACING) == debug4) {
/* 240 */       return 15;
/*     */     }
/*     */     
/* 243 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSignalSource(BlockState debug1) {
/* 248 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 253 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 258 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 263 */     debug1.add(new Property[] { (Property)FACING, (Property)POWERED, (Property)ATTACHED });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\TripWireHookBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */