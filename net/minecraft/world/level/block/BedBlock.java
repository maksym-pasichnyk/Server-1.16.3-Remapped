/*     */ package net.minecraft.world.level.block;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.npc.Villager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.DismountHelper;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.entity.BedBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BedPart;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ 
/*     */ public class BedBlock extends HorizontalDirectionalBlock implements EntityBlock {
/*  47 */   public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
/*  48 */   public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;
/*     */ 
/*     */   
/*  51 */   protected static final VoxelShape BASE = Block.box(0.0D, 3.0D, 0.0D, 16.0D, 9.0D, 16.0D);
/*     */ 
/*     */ 
/*     */   
/*  55 */   protected static final VoxelShape LEG_NORTH_WEST = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 3.0D, 3.0D);
/*  56 */   protected static final VoxelShape LEG_SOUTH_WEST = Block.box(0.0D, 0.0D, 13.0D, 3.0D, 3.0D, 16.0D);
/*  57 */   protected static final VoxelShape LEG_NORTH_EAST = Block.box(13.0D, 0.0D, 0.0D, 16.0D, 3.0D, 3.0D);
/*  58 */   protected static final VoxelShape LEG_SOUTH_EAST = Block.box(13.0D, 0.0D, 13.0D, 16.0D, 3.0D, 16.0D);
/*     */   
/*  60 */   protected static final VoxelShape NORTH_SHAPE = Shapes.or(BASE, new VoxelShape[] { LEG_NORTH_WEST, LEG_NORTH_EAST });
/*  61 */   protected static final VoxelShape SOUTH_SHAPE = Shapes.or(BASE, new VoxelShape[] { LEG_SOUTH_WEST, LEG_SOUTH_EAST });
/*  62 */   protected static final VoxelShape WEST_SHAPE = Shapes.or(BASE, new VoxelShape[] { LEG_NORTH_WEST, LEG_SOUTH_WEST });
/*  63 */   protected static final VoxelShape EAST_SHAPE = Shapes.or(BASE, new VoxelShape[] { LEG_NORTH_EAST, LEG_SOUTH_EAST });
/*     */   
/*     */   private final DyeColor color;
/*     */   
/*     */   public BedBlock(DyeColor debug1, BlockBehaviour.Properties debug2) {
/*  68 */     super(debug2);
/*  69 */     this.color = debug1;
/*  70 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)PART, (Comparable)BedPart.FOOT)).setValue((Property)OCCUPIED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  81 */     if (debug2.isClientSide) {
/*  82 */       return InteractionResult.CONSUME;
/*     */     }
/*     */     
/*  85 */     if (debug1.getValue((Property)PART) != BedPart.HEAD) {
/*     */       
/*  87 */       debug3 = debug3.relative((Direction)debug1.getValue((Property)FACING));
/*  88 */       debug1 = debug2.getBlockState(debug3);
/*  89 */       if (!debug1.is(this)) {
/*  90 */         return InteractionResult.CONSUME;
/*     */       }
/*     */     } 
/*     */     
/*  94 */     if (!canSetSpawn(debug2)) {
/*     */       
/*  96 */       debug2.removeBlock(debug3, false);
/*     */ 
/*     */       
/*  99 */       BlockPos debug7 = debug3.relative(((Direction)debug1.getValue((Property)FACING)).getOpposite());
/* 100 */       if (debug2.getBlockState(debug7).is(this)) {
/* 101 */         debug2.removeBlock(debug7, false);
/*     */       }
/*     */       
/* 104 */       debug2.explode(null, DamageSource.badRespawnPointExplosion(), null, debug3.getX() + 0.5D, debug3.getY() + 0.5D, debug3.getZ() + 0.5D, 5.0F, true, Explosion.BlockInteraction.DESTROY);
/* 105 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 108 */     if (((Boolean)debug1.getValue((Property)OCCUPIED)).booleanValue()) {
/* 109 */       if (!kickVillagerOutOfBed(debug2, debug3)) {
/* 110 */         debug4.displayClientMessage((Component)new TranslatableComponent("block.minecraft.bed.occupied"), true);
/*     */       }
/* 112 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 115 */     debug4.startSleepInBed(debug3)
/* 116 */       .ifLeft(debug1 -> {
/*     */           if (debug1 != null) {
/*     */             debug0.displayClientMessage(debug1.getMessage(), true);
/*     */           }
/*     */         });
/* 121 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   public static boolean canSetSpawn(Level debug0) {
/* 125 */     return debug0.dimensionType().bedWorks();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean kickVillagerOutOfBed(Level debug1, BlockPos debug2) {
/* 132 */     List<Villager> debug3 = debug1.getEntitiesOfClass(Villager.class, new AABB(debug2), LivingEntity::isSleeping);
/* 133 */     if (debug3.isEmpty()) {
/* 134 */       return false;
/*     */     }
/* 136 */     ((Villager)debug3.get(0)).stopSleeping();
/* 137 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void fallOn(Level debug1, BlockPos debug2, Entity debug3, float debug4) {
/* 142 */     super.fallOn(debug1, debug2, debug3, debug4 * 0.5F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateEntityAfterFallOn(BlockGetter debug1, Entity debug2) {
/* 147 */     if (debug2.isSuppressingBounce()) {
/* 148 */       super.updateEntityAfterFallOn(debug1, debug2);
/*     */     } else {
/* 150 */       bounceUp(debug2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void bounceUp(Entity debug1) {
/* 155 */     Vec3 debug2 = debug1.getDeltaMovement();
/* 156 */     if (debug2.y < 0.0D) {
/*     */       
/* 158 */       double debug3 = (debug1 instanceof LivingEntity) ? 1.0D : 0.8D;
/* 159 */       debug1.setDeltaMovement(debug2.x, -debug2.y * 0.6600000262260437D * debug3, debug2.z);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 169 */     if (debug2 == getNeighbourDirection((BedPart)debug1.getValue((Property)PART), (Direction)debug1.getValue((Property)FACING))) {
/* 170 */       if (debug3.is(this) && debug3.getValue((Property)PART) != debug1.getValue((Property)PART)) {
/* 171 */         return (BlockState)debug1.setValue((Property)OCCUPIED, debug3.getValue((Property)OCCUPIED));
/*     */       }
/* 173 */       return Blocks.AIR.defaultBlockState();
/*     */     } 
/*     */ 
/*     */     
/* 177 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */   
/*     */   private static Direction getNeighbourDirection(BedPart debug0, Direction debug1) {
/* 181 */     return (debug0 == BedPart.FOOT) ? debug1 : debug1.getOpposite();
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerWillDestroy(Level debug1, BlockPos debug2, BlockState debug3, Player debug4) {
/* 186 */     if (!debug1.isClientSide && debug4.isCreative()) {
/* 187 */       BedPart debug5 = (BedPart)debug3.getValue((Property)PART);
/* 188 */       if (debug5 == BedPart.FOOT) {
/* 189 */         BlockPos debug6 = debug2.relative(getNeighbourDirection(debug5, (Direction)debug3.getValue((Property)FACING)));
/* 190 */         BlockState debug7 = debug1.getBlockState(debug6);
/* 191 */         if (debug7.getBlock() == this && debug7.getValue((Property)PART) == BedPart.HEAD) {
/*     */           
/* 193 */           debug1.setBlock(debug6, Blocks.AIR.defaultBlockState(), 35);
/* 194 */           debug1.levelEvent(debug4, 2001, debug6, Block.getId(debug7));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 199 */     super.playerWillDestroy(debug1, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 205 */     Direction debug2 = debug1.getHorizontalDirection();
/*     */     
/* 207 */     BlockPos debug3 = debug1.getClickedPos();
/* 208 */     BlockPos debug4 = debug3.relative(debug2);
/* 209 */     if (debug1.getLevel().getBlockState(debug4).canBeReplaced(debug1)) {
/* 210 */       return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug2);
/*     */     }
/*     */     
/* 213 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 218 */     Direction debug5 = getConnectedDirection(debug1).getOpposite();
/* 219 */     switch (debug5) {
/*     */       case NORTH:
/* 221 */         return NORTH_SHAPE;
/*     */       case SOUTH:
/* 223 */         return SOUTH_SHAPE;
/*     */       case WEST:
/* 225 */         return WEST_SHAPE;
/*     */     } 
/* 227 */     return EAST_SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Direction getConnectedDirection(BlockState debug0) {
/* 232 */     Direction debug1 = (Direction)debug0.getValue((Property)FACING);
/* 233 */     return (debug0.getValue((Property)PART) == BedPart.HEAD) ? debug1.getOpposite() : debug1;
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
/*     */   private static boolean isBunkBed(BlockGetter debug0, BlockPos debug1) {
/* 245 */     return debug0.getBlockState(debug1.below()).getBlock() instanceof BedBlock;
/*     */   }
/*     */   
/*     */   public static Optional<Vec3> findStandUpPosition(EntityType<?> debug0, CollisionGetter debug1, BlockPos debug2, float debug3) {
/* 249 */     Direction debug4 = (Direction)debug1.getBlockState(debug2).getValue((Property)FACING);
/* 250 */     Direction debug5 = debug4.getClockWise();
/* 251 */     Direction debug6 = debug5.isFacingAngle(debug3) ? debug5.getOpposite() : debug5;
/*     */     
/* 253 */     if (isBunkBed((BlockGetter)debug1, debug2)) {
/* 254 */       return findBunkBedStandUpPosition(debug0, debug1, debug2, debug4, debug6);
/*     */     }
/*     */     
/* 257 */     int[][] debug7 = bedStandUpOffsets(debug4, debug6);
/*     */     
/* 259 */     Optional<Vec3> debug8 = findStandUpPositionAtOffset(debug0, debug1, debug2, debug7, true);
/* 260 */     if (debug8.isPresent()) {
/* 261 */       return debug8;
/*     */     }
/* 263 */     return findStandUpPositionAtOffset(debug0, debug1, debug2, debug7, false);
/*     */   }
/*     */   
/*     */   private static Optional<Vec3> findBunkBedStandUpPosition(EntityType<?> debug0, CollisionGetter debug1, BlockPos debug2, Direction debug3, Direction debug4) {
/* 267 */     int[][] debug5 = bedSurroundStandUpOffsets(debug3, debug4);
/*     */     
/* 269 */     Optional<Vec3> debug6 = findStandUpPositionAtOffset(debug0, debug1, debug2, debug5, true);
/* 270 */     if (debug6.isPresent()) {
/* 271 */       return debug6;
/*     */     }
/*     */     
/* 274 */     BlockPos debug7 = debug2.below();
/*     */     
/* 276 */     Optional<Vec3> debug8 = findStandUpPositionAtOffset(debug0, debug1, debug7, debug5, true);
/* 277 */     if (debug8.isPresent()) {
/* 278 */       return debug8;
/*     */     }
/*     */     
/* 281 */     int[][] debug9 = bedAboveStandUpOffsets(debug3);
/*     */     
/* 283 */     Optional<Vec3> debug10 = findStandUpPositionAtOffset(debug0, debug1, debug2, debug9, true);
/* 284 */     if (debug10.isPresent()) {
/* 285 */       return debug10;
/*     */     }
/*     */     
/* 288 */     Optional<Vec3> debug11 = findStandUpPositionAtOffset(debug0, debug1, debug2, debug5, false);
/* 289 */     if (debug11.isPresent()) {
/* 290 */       return debug11;
/*     */     }
/*     */     
/* 293 */     Optional<Vec3> debug12 = findStandUpPositionAtOffset(debug0, debug1, debug7, debug5, false);
/* 294 */     if (debug12.isPresent()) {
/* 295 */       return debug12;
/*     */     }
/*     */     
/* 298 */     return findStandUpPositionAtOffset(debug0, debug1, debug2, debug9, false);
/*     */   }
/*     */   
/*     */   private static Optional<Vec3> findStandUpPositionAtOffset(EntityType<?> debug0, CollisionGetter debug1, BlockPos debug2, int[][] debug3, boolean debug4) {
/* 302 */     BlockPos.MutableBlockPos debug5 = new BlockPos.MutableBlockPos();
/* 303 */     for (int[] debug9 : debug3) {
/* 304 */       debug5.set(debug2.getX() + debug9[0], debug2.getY(), debug2.getZ() + debug9[1]);
/*     */       
/* 306 */       Vec3 debug10 = DismountHelper.findSafeDismountLocation(debug0, debug1, (BlockPos)debug5, debug4);
/* 307 */       if (debug10 != null) {
/* 308 */         return Optional.of(debug10);
/*     */       }
/*     */     } 
/* 311 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public PushReaction getPistonPushReaction(BlockState debug1) {
/* 316 */     return PushReaction.DESTROY;
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderShape getRenderShape(BlockState debug1) {
/* 321 */     return RenderShape.ENTITYBLOCK_ANIMATED;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 326 */     debug1.add(new Property[] { (Property)FACING, (Property)PART, (Property)OCCUPIED });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 331 */     return (BlockEntity)new BedBlockEntity(this.color);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, @Nullable LivingEntity debug4, ItemStack debug5) {
/* 336 */     super.setPlacedBy(debug1, debug2, debug3, debug4, debug5);
/*     */ 
/*     */     
/* 339 */     if (!debug1.isClientSide) {
/* 340 */       BlockPos debug6 = debug2.relative((Direction)debug3.getValue((Property)FACING));
/* 341 */       debug1.setBlock(debug6, (BlockState)debug3.setValue((Property)PART, (Comparable)BedPart.HEAD), 3);
/*     */       
/* 343 */       debug1.blockUpdated(debug2, Blocks.AIR);
/* 344 */       debug3.updateNeighbourShapes((LevelAccessor)debug1, debug2, 3);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 360 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[][] bedStandUpOffsets(Direction debug0, Direction debug1) {
/* 367 */     return (int[][])ArrayUtils.addAll((Object[])bedSurroundStandUpOffsets(debug0, debug1), (Object[])bedAboveStandUpOffsets(debug0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[][] bedSurroundStandUpOffsets(Direction debug0, Direction debug1) {
/* 375 */     return new int[][] { { debug1
/* 376 */           .getStepX(), debug1.getStepZ() }, { debug1
/* 377 */           .getStepX() - debug0.getStepX(), debug1.getStepZ() - debug0.getStepZ() }, { debug1
/* 378 */           .getStepX() - debug0.getStepX() * 2, debug1.getStepZ() - debug0.getStepZ() * 2
/* 379 */         }, { -debug0.getStepX() * 2, -debug0.getStepZ() * 2
/* 380 */         }, { -debug1.getStepX() - debug0.getStepX() * 2, -debug1.getStepZ() - debug0.getStepZ() * 2
/* 381 */         }, { -debug1.getStepX() - debug0.getStepX(), -debug1.getStepZ() - debug0.getStepZ()
/* 382 */         }, { -debug1.getStepX(), -debug1.getStepZ()
/* 383 */         }, { -debug1.getStepX() + debug0.getStepX(), -debug1.getStepZ() + debug0.getStepZ() }, { debug0
/* 384 */           .getStepX(), debug0.getStepZ() }, { debug1
/* 385 */           .getStepX() + debug0.getStepX(), debug1.getStepZ() + debug0.getStepZ() } };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[][] bedAboveStandUpOffsets(Direction debug0) {
/* 391 */     return new int[][] { { 0, 0
/*     */         },
/* 393 */         { -debug0.getStepX(), -debug0.getStepZ() } };
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\BedBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */