/*     */ package net.minecraft.world.level.block.piston;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.TickableBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.PistonType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ public class PistonMovingBlockEntity
/*     */   extends BlockEntity
/*     */   implements TickableBlockEntity
/*     */ {
/*     */   private BlockState movedState;
/*     */   private Direction direction;
/*     */   private boolean extending;
/*     */   private boolean isSourcePiston;
/*  38 */   private static final ThreadLocal<Direction> NOCLIP = ThreadLocal.withInitial(() -> null);
/*     */   
/*     */   private float progress;
/*     */   
/*     */   private float progressO;
/*     */   private long lastTicked;
/*     */   private int deathTicks;
/*     */   
/*     */   public PistonMovingBlockEntity() {
/*  47 */     super(BlockEntityType.PISTON);
/*     */   }
/*     */   
/*     */   public PistonMovingBlockEntity(BlockState debug1, Direction debug2, boolean debug3, boolean debug4) {
/*  51 */     this();
/*  52 */     this.movedState = debug1;
/*  53 */     this.direction = debug2;
/*  54 */     this.extending = debug3;
/*  55 */     this.isSourcePiston = debug4;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag() {
/*  60 */     return save(new CompoundTag());
/*     */   }
/*     */   
/*     */   public boolean isExtending() {
/*  64 */     return this.extending;
/*     */   }
/*     */   
/*     */   public Direction getDirection() {
/*  68 */     return this.direction;
/*     */   }
/*     */   
/*     */   public boolean isSourcePiston() {
/*  72 */     return this.isSourcePiston;
/*     */   }
/*     */   
/*     */   public float getProgress(float debug1) {
/*  76 */     if (debug1 > 1.0F) {
/*  77 */       debug1 = 1.0F;
/*     */     }
/*  79 */     return Mth.lerp(debug1, this.progressO, this.progress);
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
/*     */   
/*     */   private float getExtendedProgress(float debug1) {
/*  95 */     return this.extending ? (debug1 - 1.0F) : (1.0F - debug1);
/*     */   }
/*     */   
/*     */   private BlockState getCollisionRelatedBlockState() {
/*  99 */     if (!isExtending() && isSourcePiston() && this.movedState.getBlock() instanceof PistonBaseBlock) {
/* 100 */       return (BlockState)((BlockState)((BlockState)Blocks.PISTON_HEAD.defaultBlockState()
/* 101 */         .setValue((Property)PistonHeadBlock.SHORT, Boolean.valueOf((this.progress > 0.25F))))
/* 102 */         .setValue((Property)PistonHeadBlock.TYPE, this.movedState.is(Blocks.STICKY_PISTON) ? (Comparable)PistonType.STICKY : (Comparable)PistonType.DEFAULT))
/* 103 */         .setValue((Property)PistonHeadBlock.FACING, this.movedState.getValue((Property)PistonBaseBlock.FACING));
/*     */     }
/* 105 */     return this.movedState;
/*     */   }
/*     */   
/*     */   private void moveCollidedEntities(float debug1) {
/* 109 */     Direction debug2 = getMovementDirection();
/*     */     
/* 111 */     double debug3 = (debug1 - this.progress);
/*     */     
/* 113 */     VoxelShape debug5 = getCollisionRelatedBlockState().getCollisionShape((BlockGetter)this.level, getBlockPos());
/* 114 */     if (debug5.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 118 */     AABB debug6 = moveByPositionAndProgress(debug5.bounds());
/* 119 */     List<Entity> debug7 = this.level.getEntities(null, PistonMath.getMovementArea(debug6, debug2, debug3).minmax(debug6));
/* 120 */     if (debug7.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 124 */     List<AABB> debug8 = debug5.toAabbs();
/* 125 */     boolean debug9 = this.movedState.is(Blocks.SLIME_BLOCK);
/* 126 */     for (Entity debug11 : debug7) {
/* 127 */       if (debug11.getPistonPushReaction() == PushReaction.IGNORE) {
/*     */         continue;
/*     */       }
/*     */       
/* 131 */       if (debug9) {
/* 132 */         if (debug11 instanceof net.minecraft.server.level.ServerPlayer) {
/*     */           continue;
/*     */         }
/*     */         
/* 136 */         Vec3 vec3 = debug11.getDeltaMovement();
/* 137 */         double debug13 = vec3.x;
/* 138 */         double debug15 = vec3.y;
/* 139 */         double debug17 = vec3.z;
/* 140 */         switch (debug2.getAxis()) {
/*     */           case EAST:
/* 142 */             debug13 = debug2.getStepX();
/*     */             break;
/*     */           case WEST:
/* 145 */             debug15 = debug2.getStepY();
/*     */             break;
/*     */           case UP:
/* 148 */             debug17 = debug2.getStepZ();
/*     */             break;
/*     */         } 
/*     */         
/* 152 */         debug11.setDeltaMovement(debug13, debug15, debug17);
/*     */       } 
/*     */       
/* 155 */       double debug12 = 0.0D;
/* 156 */       for (AABB debug15 : debug8) {
/*     */ 
/*     */         
/* 159 */         AABB debug16 = PistonMath.getMovementArea(moveByPositionAndProgress(debug15), debug2, debug3);
/*     */         
/* 161 */         AABB debug17 = debug11.getBoundingBox();
/* 162 */         if (!debug16.intersects(debug17)) {
/*     */           continue;
/*     */         }
/*     */         
/* 166 */         debug12 = Math.max(debug12, getMovement(debug16, debug2, debug17));
/*     */ 
/*     */         
/* 169 */         if (debug12 >= debug3) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 174 */       if (debug12 <= 0.0D) {
/*     */         continue;
/*     */       }
/*     */       
/* 178 */       debug12 = Math.min(debug12, debug3) + 0.01D;
/* 179 */       moveEntityByPiston(debug2, debug11, debug12, debug2);
/*     */       
/* 181 */       if (!this.extending && this.isSourcePiston) {
/* 182 */         fixEntityWithinPistonBase(debug11, debug2, debug3);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void moveEntityByPiston(Direction debug0, Entity debug1, double debug2, Direction debug4) {
/* 189 */     NOCLIP.set(debug0);
/* 190 */     debug1.move(MoverType.PISTON, new Vec3(debug2 * debug4.getStepX(), debug2 * debug4.getStepY(), debug2 * debug4.getStepZ()));
/* 191 */     NOCLIP.set(null);
/*     */   }
/*     */   
/*     */   private void moveStuckEntities(float debug1) {
/* 195 */     if (!isStickyForEntities()) {
/*     */       return;
/*     */     }
/*     */     
/* 199 */     Direction debug2 = getMovementDirection();
/* 200 */     if (!debug2.getAxis().isHorizontal()) {
/*     */       return;
/*     */     }
/*     */     
/* 204 */     double debug3 = this.movedState.getCollisionShape((BlockGetter)this.level, this.worldPosition).max(Direction.Axis.Y);
/* 205 */     AABB debug5 = moveByPositionAndProgress(new AABB(0.0D, debug3, 0.0D, 1.0D, 1.5000000999999998D, 1.0D));
/*     */     
/* 207 */     double debug6 = (debug1 - this.progress);
/*     */     
/* 209 */     List<Entity> debug8 = this.level.getEntities((Entity)null, debug5, debug1 -> matchesStickyCritera(debug0, debug1));
/* 210 */     for (Entity debug10 : debug8) {
/* 211 */       moveEntityByPiston(debug2, debug10, debug6, debug2);
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean matchesStickyCritera(AABB debug0, Entity debug1) {
/* 216 */     return (debug1.getPistonPushReaction() == PushReaction.NORMAL && debug1
/* 217 */       .isOnGround() && debug1
/* 218 */       .getX() >= debug0.minX && debug1
/* 219 */       .getX() <= debug0.maxX && debug1
/* 220 */       .getZ() >= debug0.minZ && debug1
/* 221 */       .getZ() <= debug0.maxZ);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isStickyForEntities() {
/* 226 */     return this.movedState.is(Blocks.HONEY_BLOCK);
/*     */   }
/*     */   
/*     */   public Direction getMovementDirection() {
/* 230 */     return this.extending ? this.direction : this.direction.getOpposite();
/*     */   }
/*     */   
/*     */   private static double getMovement(AABB debug0, Direction debug1, AABB debug2) {
/* 234 */     switch (debug1)
/*     */     { case EAST:
/* 236 */         return debug0.maxX - debug2.minX;
/*     */       case WEST:
/* 238 */         return debug2.maxX - debug0.minX;
/*     */       
/*     */       default:
/* 241 */         return debug0.maxY - debug2.minY;
/*     */       case DOWN:
/* 243 */         return debug2.maxY - debug0.minY;
/*     */       case SOUTH:
/* 245 */         return debug0.maxZ - debug2.minZ;
/*     */       case NORTH:
/* 247 */         break; }  return debug2.maxZ - debug0.minZ;
/*     */   }
/*     */ 
/*     */   
/*     */   private AABB moveByPositionAndProgress(AABB debug1) {
/* 252 */     double debug2 = getExtendedProgress(this.progress);
/* 253 */     return debug1.move(this.worldPosition
/* 254 */         .getX() + debug2 * this.direction.getStepX(), this.worldPosition
/* 255 */         .getY() + debug2 * this.direction.getStepY(), this.worldPosition
/* 256 */         .getZ() + debug2 * this.direction.getStepZ());
/*     */   }
/*     */ 
/*     */   
/*     */   private void fixEntityWithinPistonBase(Entity debug1, Direction debug2, double debug3) {
/* 261 */     AABB debug5 = debug1.getBoundingBox();
/* 262 */     AABB debug6 = Shapes.block().bounds().move(this.worldPosition);
/* 263 */     if (debug5.intersects(debug6)) {
/* 264 */       Direction debug7 = debug2.getOpposite();
/*     */ 
/*     */       
/* 267 */       double debug8 = getMovement(debug6, debug7, debug5) + 0.01D;
/* 268 */       double debug10 = getMovement(debug6, debug7, debug5.intersect(debug6)) + 0.01D;
/*     */       
/* 270 */       if (Math.abs(debug8 - debug10) < 0.01D) {
/* 271 */         debug8 = Math.min(debug8, debug3) + 0.01D;
/* 272 */         moveEntityByPiston(debug2, debug1, debug8, debug7);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public BlockState getMovedState() {
/* 278 */     return this.movedState;
/*     */   }
/*     */   
/*     */   public void finalTick() {
/* 282 */     if (this.level != null && (this.progressO < 1.0F || this.level.isClientSide)) {
/* 283 */       this.progress = 1.0F;
/* 284 */       this.progressO = this.progress;
/* 285 */       this.level.removeBlockEntity(this.worldPosition);
/* 286 */       setRemoved();
/* 287 */       if (this.level.getBlockState(this.worldPosition).is(Blocks.MOVING_PISTON)) {
/*     */         BlockState debug1;
/* 289 */         if (this.isSourcePiston) {
/* 290 */           debug1 = Blocks.AIR.defaultBlockState();
/*     */         } else {
/* 292 */           debug1 = Block.updateFromNeighbourShapes(this.movedState, (LevelAccessor)this.level, this.worldPosition);
/*     */         } 
/* 294 */         this.level.setBlock(this.worldPosition, debug1, 3);
/* 295 */         this.level.neighborChanged(this.worldPosition, debug1.getBlock(), this.worldPosition);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 302 */     this.lastTicked = this.level.getGameTime();
/* 303 */     this.progressO = this.progress;
/*     */     
/* 305 */     if (this.progressO >= 1.0F) {
/* 306 */       if (this.level.isClientSide && this.deathTicks < 5) {
/* 307 */         this.deathTicks++;
/*     */         return;
/*     */       } 
/* 310 */       this.level.removeBlockEntity(this.worldPosition);
/* 311 */       setRemoved();
/* 312 */       if (this.movedState != null && this.level.getBlockState(this.worldPosition).is(Blocks.MOVING_PISTON)) {
/* 313 */         BlockState blockState = Block.updateFromNeighbourShapes(this.movedState, (LevelAccessor)this.level, this.worldPosition);
/* 314 */         if (blockState.isAir()) {
/* 315 */           this.level.setBlock(this.worldPosition, this.movedState, 84);
/* 316 */           Block.updateOrDestroy(this.movedState, blockState, (LevelAccessor)this.level, this.worldPosition, 3);
/*     */         } else {
/* 318 */           if (blockState.hasProperty((Property)BlockStateProperties.WATERLOGGED) && ((Boolean)blockState.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue()) {
/* 319 */             blockState = (BlockState)blockState.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(false));
/*     */           }
/* 321 */           this.level.setBlock(this.worldPosition, blockState, 67);
/* 322 */           this.level.neighborChanged(this.worldPosition, blockState.getBlock(), this.worldPosition);
/*     */         } 
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 328 */     float debug1 = this.progress + 0.5F;
/* 329 */     moveCollidedEntities(debug1);
/* 330 */     moveStuckEntities(debug1);
/* 331 */     this.progress = debug1;
/* 332 */     if (this.progress >= 1.0F) {
/* 333 */       this.progress = 1.0F;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(BlockState debug1, CompoundTag debug2) {
/* 339 */     super.load(debug1, debug2);
/*     */     
/* 341 */     this.movedState = NbtUtils.readBlockState(debug2.getCompound("blockState"));
/* 342 */     this.direction = Direction.from3DDataValue(debug2.getInt("facing"));
/* 343 */     this.progress = debug2.getFloat("progress");
/* 344 */     this.progressO = this.progress;
/* 345 */     this.extending = debug2.getBoolean("extending");
/* 346 */     this.isSourcePiston = debug2.getBoolean("source");
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/* 351 */     super.save(debug1);
/*     */     
/* 353 */     debug1.put("blockState", (Tag)NbtUtils.writeBlockState(this.movedState));
/* 354 */     debug1.putInt("facing", this.direction.get3DDataValue());
/* 355 */     debug1.putFloat("progress", this.progressO);
/* 356 */     debug1.putBoolean("extending", this.extending);
/* 357 */     debug1.putBoolean("source", this.isSourcePiston);
/*     */     
/* 359 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getCollisionShape(BlockGetter debug1, BlockPos debug2) {
/*     */     VoxelShape debug3;
/*     */     BlockState debug5;
/* 366 */     if (!this.extending && this.isSourcePiston) {
/* 367 */       debug3 = ((BlockState)this.movedState.setValue((Property)PistonBaseBlock.EXTENDED, Boolean.valueOf(true))).getCollisionShape(debug1, debug2);
/*     */     } else {
/* 369 */       debug3 = Shapes.empty();
/*     */     } 
/*     */     
/* 372 */     Direction debug4 = NOCLIP.get();
/* 373 */     if (this.progress < 1.0D && debug4 == getMovementDirection()) {
/* 374 */       return debug3;
/*     */     }
/*     */ 
/*     */     
/* 378 */     if (isSourcePiston()) {
/* 379 */       debug5 = (BlockState)((BlockState)Blocks.PISTON_HEAD.defaultBlockState().setValue((Property)PistonHeadBlock.FACING, (Comparable)this.direction)).setValue((Property)PistonHeadBlock.SHORT, Boolean.valueOf((this.extending != ((1.0F - this.progress < 0.25F)))));
/*     */     } else {
/* 381 */       debug5 = this.movedState;
/*     */     } 
/* 383 */     float debug6 = getExtendedProgress(this.progress);
/* 384 */     double debug7 = (this.direction.getStepX() * debug6);
/* 385 */     double debug9 = (this.direction.getStepY() * debug6);
/* 386 */     double debug11 = (this.direction.getStepZ() * debug6);
/* 387 */     return Shapes.or(debug3, debug5.getCollisionShape(debug1, debug2).move(debug7, debug9, debug11));
/*     */   }
/*     */   
/*     */   public long getLastTicked() {
/* 391 */     return this.lastTicked;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\piston\PistonMovingBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */