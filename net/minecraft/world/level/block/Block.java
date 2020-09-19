/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.IdMapper;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.BlockItem;
/*     */ import net.minecraft.world.item.CreativeModeTab;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Block
/*     */   extends BlockBehaviour
/*     */   implements ItemLike
/*     */ {
/*  60 */   protected static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  62 */   public static final IdMapper<BlockState> BLOCK_STATE_REGISTRY = new IdMapper();
/*     */   
/*  64 */   private static final LoadingCache<VoxelShape, Boolean> SHAPE_FULL_BLOCK_CACHE = CacheBuilder.newBuilder()
/*  65 */     .maximumSize(512L)
/*  66 */     .weakKeys()
/*  67 */     .build(new CacheLoader<VoxelShape, Boolean>()
/*     */       {
/*     */         public Boolean load(VoxelShape debug1) {
/*  70 */           return Boolean.valueOf(!Shapes.joinIsNotEmpty(Shapes.block(), debug1, BooleanOp.NOT_SAME));
/*     */         }
/*     */       });
/*     */ 
/*     */ 
/*     */   
/*     */   protected final StateDefinition<Block, BlockState> stateDefinition;
/*     */ 
/*     */   
/*     */   private BlockState defaultBlockState;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String descriptionId;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Item item;
/*     */   
/*     */   private static final ThreadLocal<Object2ByteLinkedOpenHashMap<BlockStatePairKey>> OCCLUSION_CACHE;
/*     */ 
/*     */   
/*     */   public static int getId(@Nullable BlockState debug0) {
/*  93 */     if (debug0 == null) {
/*  94 */       return 0;
/*     */     }
/*  96 */     int debug1 = BLOCK_STATE_REGISTRY.getId(debug0);
/*  97 */     return (debug1 == -1) ? 0 : debug1;
/*     */   }
/*     */   
/*     */   public static BlockState stateById(int debug0) {
/* 101 */     BlockState debug1 = (BlockState)BLOCK_STATE_REGISTRY.byId(debug0);
/* 102 */     return (debug1 == null) ? Blocks.AIR.defaultBlockState() : debug1;
/*     */   }
/*     */   
/*     */   public static Block byItem(@Nullable Item debug0) {
/* 106 */     if (debug0 instanceof BlockItem) {
/* 107 */       return ((BlockItem)debug0).getBlock();
/*     */     }
/*     */     
/* 110 */     return Blocks.AIR;
/*     */   }
/*     */   
/*     */   public static BlockState pushEntitiesUp(BlockState debug0, BlockState debug1, Level debug2, BlockPos debug3) {
/* 114 */     VoxelShape debug4 = Shapes.joinUnoptimized(debug0.getCollisionShape((BlockGetter)debug2, debug3), debug1.getCollisionShape((BlockGetter)debug2, debug3), BooleanOp.ONLY_SECOND).move(debug3.getX(), debug3.getY(), debug3.getZ());
/* 115 */     List<Entity> debug5 = debug2.getEntities(null, debug4.bounds());
/* 116 */     for (Entity debug7 : debug5) {
/*     */       
/* 118 */       double debug8 = Shapes.collide(Direction.Axis.Y, debug7.getBoundingBox().move(0.0D, 1.0D, 0.0D), Stream.of(debug4), -1.0D);
/* 119 */       debug7.teleportTo(debug7.getX(), debug7.getY() + 1.0D + debug8, debug7.getZ());
/*     */     } 
/* 121 */     return debug1;
/*     */   }
/*     */   
/*     */   public static VoxelShape box(double debug0, double debug2, double debug4, double debug6, double debug8, double debug10) {
/* 125 */     return Shapes.box(debug0 / 16.0D, debug2 / 16.0D, debug4 / 16.0D, debug6 / 16.0D, debug8 / 16.0D, debug10 / 16.0D);
/*     */   }
/*     */   
/*     */   public boolean is(Tag<Block> debug1) {
/* 129 */     return debug1.contains(this);
/*     */   }
/*     */   
/*     */   public boolean is(Block debug1) {
/* 133 */     return (this == debug1);
/*     */   }
/*     */   
/*     */   public static BlockState updateFromNeighbourShapes(BlockState debug0, LevelAccessor debug1, BlockPos debug2) {
/* 137 */     BlockState debug3 = debug0;
/*     */     
/* 139 */     BlockPos.MutableBlockPos debug4 = new BlockPos.MutableBlockPos();
/* 140 */     for (Direction debug8 : UPDATE_SHAPE_ORDER) {
/* 141 */       debug4.setWithOffset((Vec3i)debug2, debug8);
/* 142 */       debug3 = debug3.updateShape(debug8, debug1.getBlockState((BlockPos)debug4), debug1, debug2, (BlockPos)debug4);
/*     */     } 
/*     */     
/* 145 */     return debug3;
/*     */   }
/*     */   
/*     */   public static void updateOrDestroy(BlockState debug0, BlockState debug1, LevelAccessor debug2, BlockPos debug3, int debug4) {
/* 149 */     updateOrDestroy(debug0, debug1, debug2, debug3, debug4, 512);
/*     */   }
/*     */   
/*     */   public static void updateOrDestroy(BlockState debug0, BlockState debug1, LevelAccessor debug2, BlockPos debug3, int debug4, int debug5) {
/* 153 */     if (debug1 != debug0) {
/* 154 */       if (debug1.isAir()) {
/* 155 */         if (!debug2.isClientSide()) {
/* 156 */           debug2.destroyBlock(debug3, ((debug4 & 0x20) == 0), null, debug5);
/*     */         }
/*     */       } else {
/* 159 */         debug2.setBlock(debug3, debug1, debug4 & 0xFFFFFFDF, debug5);
/*     */       } 
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
/*     */   public Block(BlockBehaviour.Properties debug1) {
/* 174 */     super(debug1);
/* 175 */     StateDefinition.Builder<Block, BlockState> debug2 = new StateDefinition.Builder(this);
/* 176 */     createBlockStateDefinition(debug2);
/*     */     
/* 178 */     this.stateDefinition = debug2.create(Block::defaultBlockState, BlockState::new);
/* 179 */     registerDefaultState((BlockState)this.stateDefinition.any());
/*     */   }
/*     */   
/*     */   public static boolean isExceptionForConnection(Block debug0) {
/* 183 */     return (debug0 instanceof LeavesBlock || debug0 == Blocks.BARRIER || debug0 == Blocks.CARVED_PUMPKIN || debug0 == Blocks.JACK_O_LANTERN || debug0 == Blocks.MELON || debug0 == Blocks.PUMPKIN || debug0
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 189 */       .is((Tag<Block>)BlockTags.SHULKER_BOXES));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRandomlyTicking(BlockState debug1) {
/* 194 */     return this.isRandomlyTicking;
/*     */   }
/*     */   
/*     */   public static final class BlockStatePairKey {
/*     */     private final BlockState first;
/*     */     private final BlockState second;
/*     */     private final Direction direction;
/*     */     
/*     */     public BlockStatePairKey(BlockState debug1, BlockState debug2, Direction debug3) {
/* 203 */       this.first = debug1;
/* 204 */       this.second = debug2;
/* 205 */       this.direction = debug3;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object debug1) {
/* 210 */       if (this == debug1) {
/* 211 */         return true;
/*     */       }
/* 213 */       if (!(debug1 instanceof BlockStatePairKey)) {
/* 214 */         return false;
/*     */       }
/* 216 */       BlockStatePairKey debug2 = (BlockStatePairKey)debug1;
/* 217 */       return (this.first == debug2.first && this.second == debug2.second && this.direction == debug2.direction);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 222 */       int debug1 = this.first.hashCode();
/* 223 */       debug1 = 31 * debug1 + this.second.hashCode();
/* 224 */       debug1 = 31 * debug1 + this.direction.hashCode();
/* 225 */       return debug1;
/*     */     }
/*     */   }
/*     */   
/*     */   static {
/* 230 */     OCCLUSION_CACHE = ThreadLocal.withInitial(() -> {
/*     */           Object2ByteLinkedOpenHashMap<BlockStatePairKey> debug0 = new Object2ByteLinkedOpenHashMap<BlockStatePairKey>(2048, 0.25F)
/*     */             {
/*     */               protected void rehash(int debug1) {}
/*     */             };
/*     */           debug0.defaultReturnValue(127);
/*     */           return debug0;
/*     */         });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean canSupportRigidBlock(BlockGetter debug0, BlockPos debug1) {
/* 272 */     return debug0.getBlockState(debug1).isFaceSturdy(debug0, debug1, Direction.UP, SupportType.RIGID);
/*     */   }
/*     */   
/*     */   public static boolean canSupportCenter(LevelReader debug0, BlockPos debug1, Direction debug2) {
/* 276 */     BlockState debug3 = debug0.getBlockState(debug1);
/*     */     
/* 278 */     if (debug2 == Direction.DOWN && debug3.is((Tag)BlockTags.UNSTABLE_BOTTOM_CENTER)) {
/* 279 */       return false;
/*     */     }
/*     */     
/* 282 */     return debug3.isFaceSturdy((BlockGetter)debug0, debug1, debug2, SupportType.CENTER);
/*     */   }
/*     */   
/*     */   public static boolean isFaceFull(VoxelShape debug0, Direction debug1) {
/* 286 */     VoxelShape debug2 = debug0.getFaceShape(debug1);
/* 287 */     return isShapeFullBlock(debug2);
/*     */   }
/*     */   
/*     */   public static boolean isShapeFullBlock(VoxelShape debug0) {
/* 291 */     return ((Boolean)SHAPE_FULL_BLOCK_CACHE.getUnchecked(debug0)).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean propagatesSkylightDown(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/* 295 */     return (!isShapeFullBlock(debug1.getShape(debug2, debug3)) && debug1.getFluidState().isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy(LevelAccessor debug1, BlockPos debug2, BlockState debug3) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<ItemStack> getDrops(BlockState debug0, ServerLevel debug1, BlockPos debug2, @Nullable BlockEntity debug3) {
/* 314 */     LootContext.Builder debug4 = (new LootContext.Builder(debug1)).withRandom(debug1.random).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)debug2)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, debug3);
/* 315 */     return debug0.getDrops(debug4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<ItemStack> getDrops(BlockState debug0, ServerLevel debug1, BlockPos debug2, @Nullable BlockEntity debug3, @Nullable Entity debug4, ItemStack debug5) {
/* 324 */     LootContext.Builder debug6 = (new LootContext.Builder(debug1)).withRandom(debug1.random).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)debug2)).withParameter(LootContextParams.TOOL, debug5).withOptionalParameter(LootContextParams.THIS_ENTITY, debug4).withOptionalParameter(LootContextParams.BLOCK_ENTITY, debug3);
/* 325 */     return debug0.getDrops(debug6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void dropResources(BlockState debug0, Level debug1, BlockPos debug2) {
/* 336 */     if (debug1 instanceof ServerLevel) {
/* 337 */       getDrops(debug0, (ServerLevel)debug1, debug2, (BlockEntity)null).forEach(debug2 -> popResource(debug0, debug1, debug2));
/* 338 */       debug0.spawnAfterBreak((ServerLevel)debug1, debug2, ItemStack.EMPTY);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void dropResources(BlockState debug0, LevelAccessor debug1, BlockPos debug2, @Nullable BlockEntity debug3) {
/* 343 */     if (debug1 instanceof ServerLevel) {
/* 344 */       getDrops(debug0, (ServerLevel)debug1, debug2, debug3).forEach(debug2 -> popResource((Level)debug0, debug1, debug2));
/* 345 */       debug0.spawnAfterBreak((ServerLevel)debug1, debug2, ItemStack.EMPTY);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void dropResources(BlockState debug0, Level debug1, BlockPos debug2, @Nullable BlockEntity debug3, Entity debug4, ItemStack debug5) {
/* 350 */     if (debug1 instanceof ServerLevel) {
/* 351 */       getDrops(debug0, (ServerLevel)debug1, debug2, debug3, debug4, debug5).forEach(debug2 -> popResource(debug0, debug1, debug2));
/* 352 */       debug0.spawnAfterBreak((ServerLevel)debug1, debug2, debug5);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void popResource(Level debug0, BlockPos debug1, ItemStack debug2) {
/* 357 */     if (debug0.isClientSide || debug2.isEmpty() || !debug0.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
/*     */       return;
/*     */     }
/*     */     
/* 361 */     float debug3 = 0.5F;
/* 362 */     double debug4 = (debug0.random.nextFloat() * 0.5F) + 0.25D;
/* 363 */     double debug6 = (debug0.random.nextFloat() * 0.5F) + 0.25D;
/* 364 */     double debug8 = (debug0.random.nextFloat() * 0.5F) + 0.25D;
/* 365 */     ItemEntity debug10 = new ItemEntity(debug0, debug1.getX() + debug4, debug1.getY() + debug6, debug1.getZ() + debug8, debug2);
/* 366 */     debug10.setDefaultPickUpDelay();
/* 367 */     debug0.addFreshEntity((Entity)debug10);
/*     */   }
/*     */   
/*     */   protected void popExperience(ServerLevel debug1, BlockPos debug2, int debug3) {
/* 371 */     if (debug1.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
/* 372 */       while (debug3 > 0) {
/* 373 */         int debug4 = ExperienceOrb.getExperienceValue(debug3);
/* 374 */         debug3 -= debug4;
/* 375 */         debug1.addFreshEntity((Entity)new ExperienceOrb((Level)debug1, debug2.getX() + 0.5D, debug2.getY() + 0.5D, debug2.getZ() + 0.5D, debug4));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public float getExplosionResistance() {
/* 381 */     return this.explosionResistance;
/*     */   }
/*     */ 
/*     */   
/*     */   public void wasExploded(Level debug1, BlockPos debug2, Explosion debug3) {}
/*     */ 
/*     */   
/*     */   public void stepOn(Level debug1, BlockPos debug2, Entity debug3) {}
/*     */   
/*     */   @Nullable
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 392 */     return defaultBlockState();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void playerDestroy(Level debug1, Player debug2, BlockPos debug3, BlockState debug4, @Nullable BlockEntity debug5, ItemStack debug6) {
/* 400 */     debug2.awardStat(Stats.BLOCK_MINED.get(this));
/* 401 */     debug2.causeFoodExhaustion(0.005F);
/* 402 */     dropResources(debug4, debug1, debug3, debug5, (Entity)debug2, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, @Nullable LivingEntity debug4, ItemStack debug5) {}
/*     */   
/*     */   public boolean isPossibleToRespawnInThis() {
/* 409 */     return (!this.material.isSolid() && !this.material.isLiquid());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescriptionId() {
/* 417 */     if (this.descriptionId == null) {
/* 418 */       this.descriptionId = Util.makeDescriptionId("block", Registry.BLOCK.getKey(this));
/*     */     }
/* 420 */     return this.descriptionId;
/*     */   }
/*     */   
/*     */   public void fallOn(Level debug1, BlockPos debug2, Entity debug3, float debug4) {
/* 424 */     debug3.causeFallDamage(debug4, 1.0F);
/*     */   }
/*     */   
/*     */   public void updateEntityAfterFallOn(BlockGetter debug1, Entity debug2) {
/* 428 */     debug2.setDeltaMovement(debug2.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fillItemCategory(CreativeModeTab debug1, NonNullList<ItemStack> debug2) {
/* 436 */     debug2.add(new ItemStack(this));
/*     */   }
/*     */   
/*     */   public float getFriction() {
/* 440 */     return this.friction;
/*     */   }
/*     */   
/*     */   public float getSpeedFactor() {
/* 444 */     return this.speedFactor;
/*     */   }
/*     */   
/*     */   public float getJumpFactor() {
/* 448 */     return this.jumpFactor;
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerWillDestroy(Level debug1, BlockPos debug2, BlockState debug3, Player debug4) {
/* 453 */     debug1.levelEvent(debug4, 2001, debug2, getId(debug3));
/*     */     
/* 455 */     if (is((Tag<Block>)BlockTags.GUARDED_BY_PIGLINS)) {
/* 456 */       PiglinAi.angerNearbyPiglins(debug4, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleRain(Level debug1, BlockPos debug2) {}
/*     */   
/*     */   public boolean dropFromExplosion(Explosion debug1) {
/* 464 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {}
/*     */   
/*     */   public StateDefinition<Block, BlockState> getStateDefinition() {
/* 471 */     return this.stateDefinition;
/*     */   }
/*     */   
/*     */   protected final void registerDefaultState(BlockState debug1) {
/* 475 */     this.defaultBlockState = debug1;
/*     */   }
/*     */   
/*     */   public final BlockState defaultBlockState() {
/* 479 */     return this.defaultBlockState;
/*     */   }
/*     */   
/*     */   public SoundType getSoundType(BlockState debug1) {
/* 483 */     return this.soundType;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item asItem() {
/* 488 */     if (this.item == null) {
/* 489 */       this.item = Item.byBlock(this);
/*     */     }
/* 491 */     return this.item;
/*     */   }
/*     */   
/*     */   public boolean hasDynamicShape() {
/* 495 */     return this.dynamicShape;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 500 */     return "Block{" + Registry.BLOCK.getKey(this) + "}";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Block asBlock() {
/* 508 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\Block.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */