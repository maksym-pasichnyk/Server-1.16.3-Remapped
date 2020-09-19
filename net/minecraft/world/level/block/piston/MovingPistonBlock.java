/*     */ package net.minecraft.world.level.block.piston;
/*     */ import java.util.Collections;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.BaseEntityBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.PistonType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class MovingPistonBlock extends BaseEntityBlock {
/*  35 */   public static final DirectionProperty FACING = PistonHeadBlock.FACING;
/*  36 */   public static final EnumProperty<PistonType> TYPE = PistonHeadBlock.TYPE;
/*     */   
/*     */   public MovingPistonBlock(BlockBehaviour.Properties debug1) {
/*  39 */     super(debug1);
/*  40 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)TYPE, (Comparable)PistonType.DEFAULT));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/*  46 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BlockEntity newMovingBlockEntity(BlockState debug0, Direction debug1, boolean debug2, boolean debug3) {
/*  51 */     return new PistonMovingBlockEntity(debug0, debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/*  56 */     if (debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/*  59 */     BlockEntity debug6 = debug2.getBlockEntity(debug3);
/*  60 */     if (debug6 instanceof PistonMovingBlockEntity) {
/*  61 */       ((PistonMovingBlockEntity)debug6).finalTick();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy(LevelAccessor debug1, BlockPos debug2, BlockState debug3) {
/*  68 */     BlockPos debug4 = debug2.relative(((Direction)debug3.getValue((Property)FACING)).getOpposite());
/*  69 */     BlockState debug5 = debug1.getBlockState(debug4);
/*  70 */     if (debug5.getBlock() instanceof PistonBaseBlock && ((Boolean)debug5.getValue((Property)PistonBaseBlock.EXTENDED)).booleanValue()) {
/*  71 */       debug1.removeBlock(debug4, false);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  78 */     if (!debug2.isClientSide && debug2.getBlockEntity(debug3) == null) {
/*     */       
/*  80 */       debug2.removeBlock(debug3, false);
/*  81 */       return InteractionResult.CONSUME;
/*     */     } 
/*  83 */     return InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ItemStack> getDrops(BlockState debug1, LootContext.Builder debug2) {
/*  89 */     PistonMovingBlockEntity debug3 = getBlockEntity((BlockGetter)debug2.getLevel(), new BlockPos((Vec3)debug2.getParameter(LootContextParams.ORIGIN)));
/*  90 */     if (debug3 == null) {
/*  91 */       return Collections.emptyList();
/*     */     }
/*     */     
/*  94 */     return debug3.getMovedState().getDrops(debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 100 */     return Shapes.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getCollisionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 105 */     PistonMovingBlockEntity debug5 = getBlockEntity(debug2, debug3);
/* 106 */     if (debug5 != null) {
/* 107 */       return debug5.getCollisionShape(debug2, debug3);
/*     */     }
/* 109 */     return Shapes.empty();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private PistonMovingBlockEntity getBlockEntity(BlockGetter debug1, BlockPos debug2) {
/* 114 */     BlockEntity debug3 = debug1.getBlockEntity(debug2);
/* 115 */     if (debug3 instanceof PistonMovingBlockEntity) {
/* 116 */       return (PistonMovingBlockEntity)debug3;
/*     */     }
/* 118 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 128 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 133 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 138 */     debug1.add(new Property[] { (Property)FACING, (Property)TYPE });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 143 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\piston\MovingPistonBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */