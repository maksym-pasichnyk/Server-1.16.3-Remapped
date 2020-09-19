/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class BasePressurePlateBlock
/*     */   extends Block {
/*  22 */   protected static final VoxelShape PRESSED_AABB = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 0.5D, 15.0D);
/*  23 */   protected static final VoxelShape AABB = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D);
/*  24 */   protected static final AABB TOUCH_AABB = new AABB(0.125D, 0.0D, 0.125D, 0.875D, 0.25D, 0.875D);
/*     */   
/*     */   protected BasePressurePlateBlock(BlockBehaviour.Properties debug1) {
/*  27 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  32 */     return (getSignalForState(debug1) > 0) ? PRESSED_AABB : AABB;
/*     */   }
/*     */   
/*     */   protected int getPressedTime() {
/*  36 */     return 20;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPossibleToRespawnInThis() {
/*  41 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  46 */     if (debug2 == Direction.DOWN && !debug1.canSurvive((LevelReader)debug4, debug5)) {
/*  47 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  49 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/*  54 */     BlockPos debug4 = debug3.below();
/*  55 */     return (canSupportRigidBlock((BlockGetter)debug2, debug4) || canSupportCenter(debug2, debug4, Direction.UP));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  60 */     int debug5 = getSignalForState(debug1);
/*  61 */     if (debug5 > 0) {
/*  62 */       checkPressed((Level)debug2, debug3, debug1, debug5);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void entityInside(BlockState debug1, Level debug2, BlockPos debug3, Entity debug4) {
/*  68 */     if (debug2.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/*  72 */     int debug5 = getSignalForState(debug1);
/*  73 */     if (debug5 == 0) {
/*  74 */       checkPressed(debug2, debug3, debug1, debug5);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void checkPressed(Level debug1, BlockPos debug2, BlockState debug3, int debug4) {
/*  79 */     int debug5 = getSignalStrength(debug1, debug2);
/*  80 */     boolean debug6 = (debug4 > 0);
/*  81 */     boolean debug7 = (debug5 > 0);
/*     */     
/*  83 */     if (debug4 != debug5) {
/*  84 */       BlockState debug8 = setSignalForState(debug3, debug5);
/*  85 */       debug1.setBlock(debug2, debug8, 2);
/*  86 */       updateNeighbours(debug1, debug2);
/*  87 */       debug1.setBlocksDirty(debug2, debug3, debug8);
/*     */     } 
/*     */     
/*  90 */     if (!debug7 && debug6) {
/*  91 */       playOffSound((LevelAccessor)debug1, debug2);
/*  92 */     } else if (debug7 && !debug6) {
/*  93 */       playOnSound((LevelAccessor)debug1, debug2);
/*     */     } 
/*     */     
/*  96 */     if (debug7) {
/*  97 */       debug1.getBlockTicks().scheduleTick(new BlockPos((Vec3i)debug2), this, getPressedTime());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void playOnSound(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos);
/*     */   
/*     */   protected abstract void playOffSound(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos);
/*     */   
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 107 */     if (debug5 || debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/* 110 */     if (getSignalForState(debug1) > 0) {
/* 111 */       updateNeighbours(debug2, debug3);
/*     */     }
/*     */     
/* 114 */     super.onRemove(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */   
/*     */   protected void updateNeighbours(Level debug1, BlockPos debug2) {
/* 118 */     debug1.updateNeighborsAt(debug2, this);
/* 119 */     debug1.updateNeighborsAt(debug2.below(), this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/* 124 */     return getSignalForState(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDirectSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/* 129 */     if (debug4 == Direction.UP) {
/* 130 */       return getSignalForState(debug1);
/*     */     }
/*     */     
/* 133 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSignalSource(BlockState debug1) {
/* 138 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public PushReaction getPistonPushReaction(BlockState debug1) {
/* 143 */     return PushReaction.DESTROY;
/*     */   }
/*     */   
/*     */   protected abstract int getSignalStrength(Level paramLevel, BlockPos paramBlockPos);
/*     */   
/*     */   protected abstract int getSignalForState(BlockState paramBlockState);
/*     */   
/*     */   protected abstract BlockState setSignalForState(BlockState paramBlockState, int paramInt);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\BasePressurePlateBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */