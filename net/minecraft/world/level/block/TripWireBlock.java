/*     */ package net.minecraft.world.level.block;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
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
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class TripWireBlock extends Block {
/*  25 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*  26 */   public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;
/*  27 */   public static final BooleanProperty DISARMED = BlockStateProperties.DISARMED;
/*  28 */   public static final BooleanProperty NORTH = PipeBlock.NORTH;
/*  29 */   public static final BooleanProperty EAST = PipeBlock.EAST;
/*  30 */   public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
/*  31 */   public static final BooleanProperty WEST = PipeBlock.WEST;
/*     */   
/*  33 */   private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = CrossCollisionBlock.PROPERTY_BY_DIRECTION;
/*     */   
/*  35 */   protected static final VoxelShape AABB = Block.box(0.0D, 1.0D, 0.0D, 16.0D, 2.5D, 16.0D);
/*  36 */   protected static final VoxelShape NOT_ATTACHED_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
/*     */   
/*     */   private final TripWireHookBlock hook;
/*     */ 
/*     */   
/*     */   public TripWireBlock(TripWireHookBlock debug1, BlockBehaviour.Properties debug2) {
/*  42 */     super(debug2);
/*  43 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)ATTACHED, Boolean.valueOf(false))).setValue((Property)DISARMED, Boolean.valueOf(false))).setValue((Property)NORTH, Boolean.valueOf(false))).setValue((Property)EAST, Boolean.valueOf(false))).setValue((Property)SOUTH, Boolean.valueOf(false))).setValue((Property)WEST, Boolean.valueOf(false)));
/*  44 */     this.hook = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  49 */     return ((Boolean)debug1.getValue((Property)ATTACHED)).booleanValue() ? AABB : NOT_ATTACHED_AABB;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/*  54 */     Level level = debug1.getLevel();
/*  55 */     BlockPos debug3 = debug1.getClickedPos();
/*     */     
/*  57 */     return (BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState()
/*  58 */       .setValue((Property)NORTH, Boolean.valueOf(shouldConnectTo(level.getBlockState(debug3.north()), Direction.NORTH))))
/*  59 */       .setValue((Property)EAST, Boolean.valueOf(shouldConnectTo(level.getBlockState(debug3.east()), Direction.EAST))))
/*  60 */       .setValue((Property)SOUTH, Boolean.valueOf(shouldConnectTo(level.getBlockState(debug3.south()), Direction.SOUTH))))
/*  61 */       .setValue((Property)WEST, Boolean.valueOf(shouldConnectTo(level.getBlockState(debug3.west()), Direction.WEST)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  66 */     if (debug2.getAxis().isHorizontal()) {
/*  67 */       return (BlockState)debug1.setValue((Property)PROPERTY_BY_DIRECTION.get(debug2), Boolean.valueOf(shouldConnectTo(debug3, debug2)));
/*     */     }
/*  69 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/*  74 */     if (debug4.is(debug1.getBlock())) {
/*     */       return;
/*     */     }
/*  77 */     updateSource(debug2, debug3, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/*  82 */     if (debug5 || debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/*  85 */     updateSource(debug2, debug3, (BlockState)debug1.setValue((Property)POWERED, Boolean.valueOf(true)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerWillDestroy(Level debug1, BlockPos debug2, BlockState debug3, Player debug4) {
/*  90 */     if (!debug1.isClientSide && !debug4.getMainHandItem().isEmpty() && debug4.getMainHandItem().getItem() == Items.SHEARS) {
/*  91 */       debug1.setBlock(debug2, (BlockState)debug3.setValue((Property)DISARMED, Boolean.valueOf(true)), 4);
/*     */     }
/*  93 */     super.playerWillDestroy(debug1, debug2, debug3, debug4);
/*     */   }
/*     */   
/*     */   private void updateSource(Level debug1, BlockPos debug2, BlockState debug3) {
/*  97 */     for (Direction debug7 : new Direction[] { Direction.SOUTH, Direction.WEST }) {
/*  98 */       for (int debug8 = 1; debug8 < 42; debug8++) {
/*  99 */         BlockPos debug9 = debug2.relative(debug7, debug8);
/* 100 */         BlockState debug10 = debug1.getBlockState(debug9);
/*     */         
/* 102 */         if (debug10.is(this.hook)) {
/* 103 */           if (debug10.getValue((Property)TripWireHookBlock.FACING) == debug7.getOpposite()) {
/* 104 */             this.hook.calculateState(debug1, debug9, debug10, false, true, debug8, debug3);
/*     */           }
/*     */           break;
/*     */         } 
/* 108 */         if (!debug10.is(this)) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void entityInside(BlockState debug1, Level debug2, BlockPos debug3, Entity debug4) {
/* 117 */     if (debug2.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/* 121 */     if (((Boolean)debug1.getValue((Property)POWERED)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 125 */     checkPressed(debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 130 */     if (!((Boolean)debug2.getBlockState(debug3).getValue((Property)POWERED)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 134 */     checkPressed((Level)debug2, debug3);
/*     */   }
/*     */   
/*     */   private void checkPressed(Level debug1, BlockPos debug2) {
/* 138 */     BlockState debug3 = debug1.getBlockState(debug2);
/* 139 */     boolean debug4 = ((Boolean)debug3.getValue((Property)POWERED)).booleanValue();
/* 140 */     boolean debug5 = false;
/*     */     
/* 142 */     List<? extends Entity> debug6 = debug1.getEntities(null, debug3.getShape((BlockGetter)debug1, debug2).bounds().move(debug2));
/* 143 */     if (!debug6.isEmpty()) {
/* 144 */       for (Entity debug8 : debug6) {
/* 145 */         if (!debug8.isIgnoringBlockTriggers()) {
/* 146 */           debug5 = true;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 152 */     if (debug5 != debug4) {
/* 153 */       debug3 = (BlockState)debug3.setValue((Property)POWERED, Boolean.valueOf(debug5));
/* 154 */       debug1.setBlock(debug2, debug3, 3);
/* 155 */       updateSource(debug1, debug2, debug3);
/*     */     } 
/*     */     
/* 158 */     if (debug5) {
/* 159 */       debug1.getBlockTicks().scheduleTick(new BlockPos((Vec3i)debug2), this, 10);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean shouldConnectTo(BlockState debug1, Direction debug2) {
/* 164 */     Block debug3 = debug1.getBlock();
/*     */     
/* 166 */     if (debug3 == this.hook) {
/* 167 */       return (debug1.getValue((Property)TripWireHookBlock.FACING) == debug2.getOpposite());
/*     */     }
/*     */     
/* 170 */     return (debug3 == this);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 175 */     switch (debug2) {
/*     */       case LEFT_RIGHT:
/* 177 */         return (BlockState)((BlockState)((BlockState)((BlockState)debug1.setValue((Property)NORTH, debug1.getValue((Property)SOUTH))).setValue((Property)EAST, debug1.getValue((Property)WEST))).setValue((Property)SOUTH, debug1.getValue((Property)NORTH))).setValue((Property)WEST, debug1.getValue((Property)EAST));
/*     */       case FRONT_BACK:
/* 179 */         return (BlockState)((BlockState)((BlockState)((BlockState)debug1.setValue((Property)NORTH, debug1.getValue((Property)EAST))).setValue((Property)EAST, debug1.getValue((Property)SOUTH))).setValue((Property)SOUTH, debug1.getValue((Property)WEST))).setValue((Property)WEST, debug1.getValue((Property)NORTH));
/*     */       case null:
/* 181 */         return (BlockState)((BlockState)((BlockState)((BlockState)debug1.setValue((Property)NORTH, debug1.getValue((Property)WEST))).setValue((Property)EAST, debug1.getValue((Property)NORTH))).setValue((Property)SOUTH, debug1.getValue((Property)EAST))).setValue((Property)WEST, debug1.getValue((Property)SOUTH));
/*     */     } 
/* 183 */     return debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 189 */     switch (debug2) {
/*     */       case LEFT_RIGHT:
/* 191 */         return (BlockState)((BlockState)debug1.setValue((Property)NORTH, debug1.getValue((Property)SOUTH))).setValue((Property)SOUTH, debug1.getValue((Property)NORTH));
/*     */       case FRONT_BACK:
/* 193 */         return (BlockState)((BlockState)debug1.setValue((Property)EAST, debug1.getValue((Property)WEST))).setValue((Property)WEST, debug1.getValue((Property)EAST));
/*     */     } 
/*     */ 
/*     */     
/* 197 */     return super.mirror(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 202 */     debug1.add(new Property[] { (Property)POWERED, (Property)ATTACHED, (Property)DISARMED, (Property)NORTH, (Property)EAST, (Property)WEST, (Property)SOUTH });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\TripWireBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */