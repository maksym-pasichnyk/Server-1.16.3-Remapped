/*     */ package net.minecraft.world.level.block;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.AbstractArrow;
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
/*     */ public abstract class ButtonBlock extends FaceAttachedHorizontalDirectionalBlock {
/*  31 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  39 */   protected static final VoxelShape CEILING_AABB_X = Block.box(6.0D, 14.0D, 5.0D, 10.0D, 16.0D, 11.0D);
/*  40 */   protected static final VoxelShape CEILING_AABB_Z = Block.box(5.0D, 14.0D, 6.0D, 11.0D, 16.0D, 10.0D);
/*  41 */   protected static final VoxelShape FLOOR_AABB_X = Block.box(6.0D, 0.0D, 5.0D, 10.0D, 2.0D, 11.0D);
/*  42 */   protected static final VoxelShape FLOOR_AABB_Z = Block.box(5.0D, 0.0D, 6.0D, 11.0D, 2.0D, 10.0D);
/*  43 */   protected static final VoxelShape NORTH_AABB = Block.box(5.0D, 6.0D, 14.0D, 11.0D, 10.0D, 16.0D);
/*  44 */   protected static final VoxelShape SOUTH_AABB = Block.box(5.0D, 6.0D, 0.0D, 11.0D, 10.0D, 2.0D);
/*  45 */   protected static final VoxelShape WEST_AABB = Block.box(14.0D, 6.0D, 5.0D, 16.0D, 10.0D, 11.0D);
/*  46 */   protected static final VoxelShape EAST_AABB = Block.box(0.0D, 6.0D, 5.0D, 2.0D, 10.0D, 11.0D);
/*     */   
/*  48 */   protected static final VoxelShape PRESSED_CEILING_AABB_X = Block.box(6.0D, 15.0D, 5.0D, 10.0D, 16.0D, 11.0D);
/*  49 */   protected static final VoxelShape PRESSED_CEILING_AABB_Z = Block.box(5.0D, 15.0D, 6.0D, 11.0D, 16.0D, 10.0D);
/*  50 */   protected static final VoxelShape PRESSED_FLOOR_AABB_X = Block.box(6.0D, 0.0D, 5.0D, 10.0D, 1.0D, 11.0D);
/*  51 */   protected static final VoxelShape PRESSED_FLOOR_AABB_Z = Block.box(5.0D, 0.0D, 6.0D, 11.0D, 1.0D, 10.0D);
/*  52 */   protected static final VoxelShape PRESSED_NORTH_AABB = Block.box(5.0D, 6.0D, 15.0D, 11.0D, 10.0D, 16.0D);
/*  53 */   protected static final VoxelShape PRESSED_SOUTH_AABB = Block.box(5.0D, 6.0D, 0.0D, 11.0D, 10.0D, 1.0D);
/*  54 */   protected static final VoxelShape PRESSED_WEST_AABB = Block.box(15.0D, 6.0D, 5.0D, 16.0D, 10.0D, 11.0D);
/*  55 */   protected static final VoxelShape PRESSED_EAST_AABB = Block.box(0.0D, 6.0D, 5.0D, 1.0D, 10.0D, 11.0D);
/*     */   
/*     */   private final boolean sensitive;
/*     */ 
/*     */   
/*     */   protected ButtonBlock(boolean debug1, BlockBehaviour.Properties debug2) {
/*  61 */     super(debug2);
/*  62 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)FACE, (Comparable)AttachFace.WALL));
/*  63 */     this.sensitive = debug1;
/*     */   }
/*     */   
/*     */   private int getPressDuration() {
/*  67 */     return this.sensitive ? 30 : 20;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  72 */     Direction debug5 = (Direction)debug1.getValue((Property)FACING);
/*  73 */     boolean debug6 = ((Boolean)debug1.getValue((Property)POWERED)).booleanValue();
/*     */     
/*  75 */     switch ((AttachFace)debug1.getValue((Property)FACE)) {
/*     */       case FLOOR:
/*  77 */         if (debug5.getAxis() == Direction.Axis.X) {
/*  78 */           return debug6 ? PRESSED_FLOOR_AABB_X : FLOOR_AABB_X;
/*     */         }
/*  80 */         return debug6 ? PRESSED_FLOOR_AABB_Z : FLOOR_AABB_Z;
/*     */       
/*     */       case WALL:
/*  83 */         switch (debug5) {
/*     */           case FLOOR:
/*  85 */             return debug6 ? PRESSED_EAST_AABB : EAST_AABB;
/*     */           case WALL:
/*  87 */             return debug6 ? PRESSED_WEST_AABB : WEST_AABB;
/*     */           case CEILING:
/*  89 */             return debug6 ? PRESSED_SOUTH_AABB : SOUTH_AABB;
/*     */         } 
/*     */         
/*  92 */         return debug6 ? PRESSED_NORTH_AABB : NORTH_AABB;
/*     */     } 
/*     */ 
/*     */     
/*  96 */     if (debug5.getAxis() == Direction.Axis.X) {
/*  97 */       return debug6 ? PRESSED_CEILING_AABB_X : CEILING_AABB_X;
/*     */     }
/*  99 */     return debug6 ? PRESSED_CEILING_AABB_Z : CEILING_AABB_Z;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 106 */     if (((Boolean)debug1.getValue((Property)POWERED)).booleanValue()) {
/* 107 */       return InteractionResult.CONSUME;
/*     */     }
/* 109 */     press(debug1, debug2, debug3);
/* 110 */     playSound(debug4, (LevelAccessor)debug2, debug3, true);
/* 111 */     return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */   }
/*     */   
/*     */   public void press(BlockState debug1, Level debug2, BlockPos debug3) {
/* 115 */     debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)POWERED, Boolean.valueOf(true)), 3);
/* 116 */     updateNeighbours(debug1, debug2, debug3);
/* 117 */     debug2.getBlockTicks().scheduleTick(debug3, this, getPressDuration());
/*     */   }
/*     */   
/*     */   protected void playSound(@Nullable Player debug1, LevelAccessor debug2, BlockPos debug3, boolean debug4) {
/* 121 */     debug2.playSound(debug4 ? debug1 : null, debug3, getSound(debug4), SoundSource.BLOCKS, 0.3F, debug4 ? 0.6F : 0.5F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract SoundEvent getSound(boolean paramBoolean);
/*     */   
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 128 */     if (debug5 || debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/* 131 */     if (((Boolean)debug1.getValue((Property)POWERED)).booleanValue()) {
/* 132 */       updateNeighbours(debug1, debug2, debug3);
/*     */     }
/* 134 */     super.onRemove(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/* 139 */     return ((Boolean)debug1.getValue((Property)POWERED)).booleanValue() ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDirectSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/* 144 */     if (((Boolean)debug1.getValue((Property)POWERED)).booleanValue() && getConnectedDirection(debug1) == debug4) {
/* 145 */       return 15;
/*     */     }
/* 147 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSignalSource(BlockState debug1) {
/* 152 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 157 */     if (!((Boolean)debug1.getValue((Property)POWERED)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 161 */     if (this.sensitive) {
/* 162 */       checkPressed(debug1, (Level)debug2, debug3);
/*     */     } else {
/* 164 */       debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)POWERED, Boolean.valueOf(false)), 3);
/*     */       
/* 166 */       updateNeighbours(debug1, (Level)debug2, debug3);
/*     */       
/* 168 */       playSound((Player)null, (LevelAccessor)debug2, debug3, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void entityInside(BlockState debug1, Level debug2, BlockPos debug3, Entity debug4) {
/* 174 */     if (debug2.isClientSide || !this.sensitive || ((Boolean)debug1.getValue((Property)POWERED)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 178 */     checkPressed(debug1, debug2, debug3);
/*     */   }
/*     */   
/*     */   private void checkPressed(BlockState debug1, Level debug2, BlockPos debug3) {
/* 182 */     List<? extends Entity> debug4 = debug2.getEntitiesOfClass(AbstractArrow.class, debug1.getShape((BlockGetter)debug2, debug3).bounds().move(debug3));
/* 183 */     boolean debug5 = !debug4.isEmpty();
/* 184 */     boolean debug6 = ((Boolean)debug1.getValue((Property)POWERED)).booleanValue();
/*     */     
/* 186 */     if (debug5 != debug6) {
/* 187 */       debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)POWERED, Boolean.valueOf(debug5)), 3);
/* 188 */       updateNeighbours(debug1, debug2, debug3);
/* 189 */       playSound((Player)null, (LevelAccessor)debug2, debug3, debug5);
/*     */     } 
/*     */     
/* 192 */     if (debug5) {
/* 193 */       debug2.getBlockTicks().scheduleTick(new BlockPos((Vec3i)debug3), this, getPressDuration());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateNeighbours(BlockState debug1, Level debug2, BlockPos debug3) {
/* 199 */     debug2.updateNeighborsAt(debug3, this);
/* 200 */     debug2.updateNeighborsAt(debug3.relative(getConnectedDirection(debug1).getOpposite()), this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 205 */     debug1.add(new Property[] { (Property)FACING, (Property)POWERED, (Property)FACE });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\ButtonBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */