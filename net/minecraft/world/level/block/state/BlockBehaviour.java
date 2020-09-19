/*      */ package net.minecraft.world.level.block.state;
/*      */ 
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.mojang.serialization.MapCodec;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.ToIntFunction;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.network.protocol.game.DebugPackets;
/*      */ import net.minecraft.resources.ResourceLocation;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.tags.Tag;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.MenuProvider;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.projectile.Projectile;
/*      */ import net.minecraft.world.item.DyeColor;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.context.BlockPlaceContext;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.EmptyBlockGetter;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelAccessor;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Mirror;
/*      */ import net.minecraft.world.level.block.RenderShape;
/*      */ import net.minecraft.world.level.block.Rotation;
/*      */ import net.minecraft.world.level.block.SoundType;
/*      */ import net.minecraft.world.level.block.SupportType;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.material.FluidState;
/*      */ import net.minecraft.world.level.material.Fluids;
/*      */ import net.minecraft.world.level.material.Material;
/*      */ import net.minecraft.world.level.material.MaterialColor;
/*      */ import net.minecraft.world.level.material.PushReaction;
/*      */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*      */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*      */ import net.minecraft.world.level.storage.loot.LootContext;
/*      */ import net.minecraft.world.level.storage.loot.LootTable;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.CollisionContext;
/*      */ import net.minecraft.world.phys.shapes.Shapes;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ 
/*      */ 
/*      */ public abstract class BlockBehaviour
/*      */ {
/*   67 */   protected static final Direction[] UPDATE_SHAPE_ORDER = new Direction[] { Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP };
/*      */   
/*      */   protected final Material material;
/*      */   
/*      */   protected final boolean hasCollision;
/*      */   protected final float explosionResistance;
/*      */   protected final boolean isRandomlyTicking;
/*      */   protected final SoundType soundType;
/*      */   protected final float friction;
/*      */   protected final float speedFactor;
/*      */   protected final float jumpFactor;
/*      */   protected final boolean dynamicShape;
/*      */   protected final Properties properties;
/*      */   @Nullable
/*      */   protected ResourceLocation drops;
/*      */   
/*      */   public BlockBehaviour(Properties debug1) {
/*   84 */     this.material = debug1.material;
/*   85 */     this.hasCollision = debug1.hasCollision;
/*   86 */     this.drops = debug1.drops;
/*   87 */     this.explosionResistance = debug1.explosionResistance;
/*   88 */     this.isRandomlyTicking = debug1.isRandomlyTicking;
/*   89 */     this.soundType = debug1.soundType;
/*   90 */     this.friction = debug1.friction;
/*   91 */     this.speedFactor = debug1.speedFactor;
/*   92 */     this.jumpFactor = debug1.jumpFactor;
/*   93 */     this.dynamicShape = debug1.dynamicShape;
/*   94 */     this.properties = debug1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void updateIndirectNeighbourShapes(BlockState debug1, LevelAccessor debug2, BlockPos debug3, int debug4, int debug5) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/*  108 */     switch (debug4) {
/*      */       case LAND:
/*  110 */         return !debug1.isCollisionShapeFullBlock(debug2, debug3);
/*      */       case WATER:
/*  112 */         return debug2.getFluidState(debug3).is((Tag)FluidTags.WATER);
/*      */       case AIR:
/*  114 */         return !debug1.isCollisionShapeFullBlock(debug2, debug3);
/*      */     } 
/*  116 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  128 */     return debug1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void neighborChanged(BlockState debug1, Level debug2, BlockPos debug3, Block debug4, BlockPos debug5, boolean debug6) {
/*  143 */     DebugPackets.sendNeighborsUpdatePacket(debug2, debug3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {}
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/*  154 */     if (isEntityBlock() && !debug1.is(debug4.getBlock())) {
/*  155 */       debug2.removeBlockEntity(debug3);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  162 */     return InteractionResult.PASS;
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean triggerEvent(BlockState debug1, Level debug2, BlockPos debug3, int debug4, int debug5) {
/*  168 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public RenderShape getRenderShape(BlockState debug1) {
/*  176 */     return RenderShape.MODEL;
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean useShapeForLightOcclusion(BlockState debug1) {
/*  182 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean isSignalSource(BlockState debug1) {
/*  188 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public PushReaction getPistonPushReaction(BlockState debug1) {
/*  194 */     return this.material.getPushReaction();
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public FluidState getFluidState(BlockState debug1) {
/*  200 */     return Fluids.EMPTY.defaultFluidState();
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean hasAnalogOutputSignal(BlockState debug1) {
/*  206 */     return false;
/*      */   }
/*      */   
/*      */   public OffsetType getOffsetType() {
/*  210 */     return OffsetType.NONE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/*  218 */     return debug1;
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/*  224 */     return debug1;
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean canBeReplaced(BlockState debug1, BlockPlaceContext debug2) {
/*  230 */     return (this.material.isReplaceable() && (debug2.getItemInHand().isEmpty() || debug2.getItemInHand().getItem() != asItem()));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean canBeReplaced(BlockState debug1, Fluid debug2) {
/*  236 */     return (this.material.isReplaceable() || !this.material.isSolid());
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public List<ItemStack> getDrops(BlockState debug1, LootContext.Builder debug2) {
/*  242 */     ResourceLocation debug3 = getLootTable();
/*  243 */     if (debug3 == BuiltInLootTables.EMPTY) {
/*  244 */       return Collections.emptyList();
/*      */     }
/*  246 */     LootContext debug4 = debug2.withParameter(LootContextParams.BLOCK_STATE, debug1).create(LootContextParamSets.BLOCK);
/*  247 */     ServerLevel debug5 = debug4.getLevel();
/*  248 */     LootTable debug6 = debug5.getServer().getLootTables().get(debug3);
/*  249 */     return debug6.getRandomItems(debug4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public VoxelShape getOcclusionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/*  264 */     return debug1.getShape(debug2, debug3);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public VoxelShape getBlockSupportShape(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/*  270 */     return getCollisionShape(debug1, debug2, debug3, CollisionContext.empty());
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public VoxelShape getInteractionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/*  276 */     return Shapes.empty();
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public int getLightBlock(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/*  282 */     if (debug1.isSolidRender(debug2, debug3)) {
/*  283 */       return debug2.getMaxLightLevel();
/*      */     }
/*  285 */     return debug1.propagatesSkylightDown(debug2, debug3) ? 0 : 1;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   @Deprecated
/*      */   public MenuProvider getMenuProvider(BlockState debug1, Level debug2, BlockPos debug3) {
/*  292 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/*  298 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public int getAnalogOutputSignal(BlockState debug1, Level debug2, BlockPos debug3) {
/*  310 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  318 */     return Shapes.block();
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public VoxelShape getCollisionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  324 */     return this.hasCollision ? debug1.getShape(debug2, debug3) : Shapes.empty();
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public VoxelShape getVisualShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  330 */     return getCollisionShape(debug1, debug2, debug3, debug4);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  336 */     tick(debug1, debug2, debug3, debug4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {}
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public float getDestroyProgress(BlockState debug1, Player debug2, BlockGetter debug3, BlockPos debug4) {
/*  347 */     float debug5 = debug1.getDestroySpeed(debug3, debug4);
/*  348 */     if (debug5 == -1.0F) {
/*  349 */       return 0.0F;
/*      */     }
/*  351 */     int debug6 = debug2.hasCorrectToolForDrops(debug1) ? 30 : 100;
/*  352 */     return debug2.getDestroySpeed(debug1) / debug5 / debug6;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void spawnAfterBreak(BlockState debug1, ServerLevel debug2, BlockPos debug3, ItemStack debug4) {}
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void attack(BlockState debug1, Level debug2, BlockPos debug3, Player debug4) {}
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public int getSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/*  368 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void entityInside(BlockState debug1, Level debug2, BlockPos debug3, Entity debug4) {}
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public int getDirectSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/*  379 */     return 0;
/*      */   }
/*      */   
/*      */   public final boolean isEntityBlock() {
/*  383 */     return this instanceof net.minecraft.world.level.block.EntityBlock;
/*      */   }
/*      */   
/*      */   public final ResourceLocation getLootTable() {
/*  387 */     if (this.drops == null) {
/*  388 */       ResourceLocation debug1 = Registry.BLOCK.getKey(asBlock());
/*      */       
/*  390 */       this.drops = new ResourceLocation(debug1.getNamespace(), "blocks/" + debug1.getPath());
/*      */     } 
/*  392 */     return this.drops;
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void onProjectileHit(Level debug1, BlockState debug2, BlockHitResult debug3, Projectile debug4) {}
/*      */   
/*      */   public abstract Item asItem();
/*      */   
/*      */   protected abstract Block asBlock();
/*      */   
/*      */   public enum OffsetType
/*      */   {
/*  405 */     NONE,
/*  406 */     XZ,
/*  407 */     XYZ;
/*      */   }
/*      */   
/*      */   public MaterialColor defaultMaterialColor() {
/*  411 */     return this.properties.materialColor.apply(asBlock().defaultBlockState());
/*      */   }
/*      */   
/*      */   public static class Properties
/*      */   {
/*      */     private Material material;
/*      */     private Function<BlockState, MaterialColor> materialColor;
/*      */     private boolean hasCollision = true;
/*  419 */     private SoundType soundType = SoundType.STONE;
/*      */     private ToIntFunction<BlockState> lightEmission = debug0 -> 0;
/*      */     private float explosionResistance;
/*      */     private float destroyTime;
/*      */     private boolean requiresCorrectToolForDrops;
/*      */     private boolean isRandomlyTicking;
/*  425 */     private float friction = 0.6F;
/*  426 */     private float speedFactor = 1.0F; private ResourceLocation drops; private boolean canOcclude = true; private boolean isAir; private BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn; private BlockBehaviour.StatePredicate isRedstoneConductor;
/*  427 */     private float jumpFactor = 1.0F; private BlockBehaviour.StatePredicate isSuffocating; private BlockBehaviour.StatePredicate isViewBlocking; private BlockBehaviour.StatePredicate hasPostProcess; private BlockBehaviour.StatePredicate emissiveRendering;
/*      */     private boolean dynamicShape;
/*      */     
/*      */     private Properties(Material debug1, Function<BlockState, MaterialColor> debug2) {
/*  431 */       this.isValidSpawn = ((debug0, debug1, debug2, debug3) -> 
/*  432 */         (debug0.isFaceSturdy(debug1, debug2, Direction.UP) && debug0.getLightEmission() < 14));
/*      */       
/*  434 */       this.isRedstoneConductor = ((debug0, debug1, debug2) -> 
/*  435 */         (debug0.getMaterial().isSolidBlocking() && debug0.isCollisionShapeFullBlock(debug1, debug2)));
/*      */       
/*  437 */       this.isSuffocating = ((debug1, debug2, debug3) -> 
/*  438 */         (this.material.blocksMotion() && debug1.isCollisionShapeFullBlock(debug2, debug3)));
/*      */       
/*  440 */       this.isViewBlocking = this.isSuffocating;
/*  441 */       this.hasPostProcess = ((debug0, debug1, debug2) -> false);
/*  442 */       this.emissiveRendering = ((debug0, debug1, debug2) -> false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  451 */       this.material = debug1;
/*  452 */       this.materialColor = debug2;
/*      */     } private Properties(Material debug1, MaterialColor debug2) {
/*      */       this(debug1, debug1 -> debug0);
/*      */     } public static Properties of(Material debug0) {
/*  456 */       return of(debug0, debug0.getColor());
/*      */     }
/*      */     
/*      */     public static Properties of(Material debug0, DyeColor debug1) {
/*  460 */       return of(debug0, debug1.getMaterialColor());
/*      */     }
/*      */     
/*      */     public static Properties of(Material debug0, MaterialColor debug1) {
/*  464 */       return new Properties(debug0, debug1);
/*      */     }
/*      */     
/*      */     public static Properties of(Material debug0, Function<BlockState, MaterialColor> debug1) {
/*  468 */       return new Properties(debug0, debug1);
/*      */     }
/*      */     
/*      */     public static Properties copy(BlockBehaviour debug0) {
/*  472 */       Properties debug1 = new Properties(debug0.material, debug0.properties.materialColor);
/*      */       
/*  474 */       debug1.material = debug0.properties.material;
/*  475 */       debug1.destroyTime = debug0.properties.destroyTime;
/*  476 */       debug1.explosionResistance = debug0.properties.explosionResistance;
/*  477 */       debug1.hasCollision = debug0.properties.hasCollision;
/*  478 */       debug1.isRandomlyTicking = debug0.properties.isRandomlyTicking;
/*  479 */       debug1.lightEmission = debug0.properties.lightEmission;
/*  480 */       debug1.materialColor = debug0.properties.materialColor;
/*  481 */       debug1.soundType = debug0.properties.soundType;
/*  482 */       debug1.friction = debug0.properties.friction;
/*  483 */       debug1.speedFactor = debug0.properties.speedFactor;
/*  484 */       debug1.dynamicShape = debug0.properties.dynamicShape;
/*  485 */       debug1.canOcclude = debug0.properties.canOcclude;
/*  486 */       debug1.isAir = debug0.properties.isAir;
/*  487 */       debug1.requiresCorrectToolForDrops = debug0.properties.requiresCorrectToolForDrops;
/*      */       
/*  489 */       return debug1;
/*      */     }
/*      */     
/*      */     public Properties noCollission() {
/*  493 */       this.hasCollision = false;
/*  494 */       this.canOcclude = false;
/*  495 */       return this;
/*      */     }
/*      */     
/*      */     public Properties noOcclusion() {
/*  499 */       this.canOcclude = false;
/*  500 */       return this;
/*      */     }
/*      */     
/*      */     public Properties friction(float debug1) {
/*  504 */       this.friction = debug1;
/*  505 */       return this;
/*      */     }
/*      */     
/*      */     public Properties speedFactor(float debug1) {
/*  509 */       this.speedFactor = debug1;
/*  510 */       return this;
/*      */     }
/*      */     
/*      */     public Properties jumpFactor(float debug1) {
/*  514 */       this.jumpFactor = debug1;
/*  515 */       return this;
/*      */     }
/*      */     
/*      */     public Properties sound(SoundType debug1) {
/*  519 */       this.soundType = debug1;
/*  520 */       return this;
/*      */     }
/*      */     
/*      */     public Properties lightLevel(ToIntFunction<BlockState> debug1) {
/*  524 */       this.lightEmission = debug1;
/*  525 */       return this;
/*      */     }
/*      */     
/*      */     public Properties strength(float debug1, float debug2) {
/*  529 */       this.destroyTime = debug1;
/*  530 */       this.explosionResistance = Math.max(0.0F, debug2);
/*  531 */       return this;
/*      */     }
/*      */     
/*      */     public Properties instabreak() {
/*  535 */       return strength(0.0F);
/*      */     }
/*      */     
/*      */     public Properties strength(float debug1) {
/*  539 */       strength(debug1, debug1);
/*  540 */       return this;
/*      */     }
/*      */     
/*      */     public Properties randomTicks() {
/*  544 */       this.isRandomlyTicking = true;
/*  545 */       return this;
/*      */     }
/*      */     
/*      */     public Properties dynamicShape() {
/*  549 */       this.dynamicShape = true;
/*  550 */       return this;
/*      */     }
/*      */     
/*      */     public Properties noDrops() {
/*  554 */       this.drops = BuiltInLootTables.EMPTY;
/*  555 */       return this;
/*      */     }
/*      */     
/*      */     public Properties dropsLike(Block debug1) {
/*  559 */       this.drops = debug1.getLootTable();
/*  560 */       return this;
/*      */     }
/*      */     
/*      */     public Properties air() {
/*  564 */       this.isAir = true;
/*  565 */       return this;
/*      */     }
/*      */     
/*      */     public Properties isValidSpawn(BlockBehaviour.StateArgumentPredicate<EntityType<?>> debug1) {
/*  569 */       this.isValidSpawn = debug1;
/*  570 */       return this;
/*      */     }
/*      */     
/*      */     public Properties isRedstoneConductor(BlockBehaviour.StatePredicate debug1) {
/*  574 */       this.isRedstoneConductor = debug1;
/*  575 */       return this;
/*      */     }
/*      */     
/*      */     public Properties isSuffocating(BlockBehaviour.StatePredicate debug1) {
/*  579 */       this.isSuffocating = debug1;
/*  580 */       return this;
/*      */     }
/*      */     
/*      */     public Properties isViewBlocking(BlockBehaviour.StatePredicate debug1) {
/*  584 */       this.isViewBlocking = debug1;
/*  585 */       return this;
/*      */     }
/*      */     
/*      */     public Properties hasPostProcess(BlockBehaviour.StatePredicate debug1) {
/*  589 */       this.hasPostProcess = debug1;
/*  590 */       return this;
/*      */     }
/*      */     
/*      */     public Properties emissiveRendering(BlockBehaviour.StatePredicate debug1) {
/*  594 */       this.emissiveRendering = debug1;
/*  595 */       return this;
/*      */     }
/*      */     
/*      */     public Properties requiresCorrectToolForDrops() {
/*  599 */       this.requiresCorrectToolForDrops = true;
/*  600 */       return this;
/*      */     }
/*      */   }
/*      */   
/*      */   public static abstract class BlockStateBase
/*      */     extends StateHolder<Block, BlockState> {
/*      */     private final int lightEmission;
/*      */     private final boolean useShapeForLightOcclusion;
/*      */     private final boolean isAir;
/*      */     private final Material material;
/*      */     private final MaterialColor materialColor;
/*      */     private final float destroySpeed;
/*      */     private final boolean requiresCorrectToolForDrops;
/*      */     private final boolean canOcclude;
/*      */     private final BlockBehaviour.StatePredicate isRedstoneConductor;
/*      */     private final BlockBehaviour.StatePredicate isSuffocating;
/*      */     private final BlockBehaviour.StatePredicate isViewBlocking;
/*      */     private final BlockBehaviour.StatePredicate hasPostProcess;
/*      */     private final BlockBehaviour.StatePredicate emissiveRendering;
/*      */     @Nullable
/*      */     protected Cache cache;
/*      */     
/*      */     protected BlockStateBase(Block debug1, ImmutableMap<Property<?>, Comparable<?>> debug2, MapCodec<BlockState> debug3) {
/*  623 */       super(debug1, debug2, debug3);
/*  624 */       BlockBehaviour.Properties debug4 = debug1.properties;
/*      */       
/*  626 */       this.lightEmission = debug4.lightEmission.applyAsInt(asState());
/*  627 */       this.useShapeForLightOcclusion = debug1.useShapeForLightOcclusion(asState());
/*  628 */       this.isAir = debug4.isAir;
/*  629 */       this.material = debug4.material;
/*  630 */       this.materialColor = debug4.materialColor.apply(asState());
/*  631 */       this.destroySpeed = debug4.destroyTime;
/*  632 */       this.requiresCorrectToolForDrops = debug4.requiresCorrectToolForDrops;
/*  633 */       this.canOcclude = debug4.canOcclude;
/*  634 */       this.isRedstoneConductor = debug4.isRedstoneConductor;
/*  635 */       this.isSuffocating = debug4.isSuffocating;
/*  636 */       this.isViewBlocking = debug4.isViewBlocking;
/*  637 */       this.hasPostProcess = debug4.hasPostProcess;
/*  638 */       this.emissiveRendering = debug4.emissiveRendering;
/*      */     }
/*      */     
/*      */     public void initCache() {
/*  642 */       if (!getBlock().hasDynamicShape()) {
/*  643 */         this.cache = new Cache(asState());
/*      */       }
/*      */     }
/*      */     
/*      */     public Block getBlock() {
/*  648 */       return this.owner;
/*      */     }
/*      */     
/*      */     public Material getMaterial() {
/*  652 */       return this.material;
/*      */     }
/*      */     
/*      */     public boolean isValidSpawn(BlockGetter debug1, BlockPos debug2, EntityType<?> debug3) {
/*  656 */       return (getBlock()).properties.isValidSpawn.test(asState(), debug1, debug2, debug3);
/*      */     }
/*      */     
/*      */     public boolean propagatesSkylightDown(BlockGetter debug1, BlockPos debug2) {
/*  660 */       if (this.cache != null) {
/*  661 */         return this.cache.propagatesSkylightDown;
/*      */       }
/*  663 */       return getBlock().propagatesSkylightDown(asState(), debug1, debug2);
/*      */     }
/*      */     
/*      */     public int getLightBlock(BlockGetter debug1, BlockPos debug2) {
/*  667 */       if (this.cache != null) {
/*  668 */         return this.cache.lightBlock;
/*      */       }
/*  670 */       return getBlock().getLightBlock(asState(), debug1, debug2);
/*      */     }
/*      */     
/*      */     public VoxelShape getFaceOcclusionShape(BlockGetter debug1, BlockPos debug2, Direction debug3) {
/*  674 */       if (this.cache != null && this.cache.occlusionShapes != null) {
/*  675 */         return this.cache.occlusionShapes[debug3.ordinal()];
/*      */       }
/*      */       
/*  678 */       return Shapes.getFaceShape(getOcclusionShape(debug1, debug2), debug3);
/*      */     }
/*      */     
/*      */     public VoxelShape getOcclusionShape(BlockGetter debug1, BlockPos debug2) {
/*  682 */       return getBlock().getOcclusionShape(asState(), debug1, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasLargeCollisionShape() {
/*  687 */       return (this.cache == null || this.cache.largeCollisionShape);
/*      */     }
/*      */     
/*      */     public boolean useShapeForLightOcclusion() {
/*  691 */       return this.useShapeForLightOcclusion;
/*      */     }
/*      */     
/*      */     public int getLightEmission() {
/*  695 */       return this.lightEmission;
/*      */     }
/*      */     
/*      */     public boolean isAir() {
/*  699 */       return this.isAir;
/*      */     }
/*      */     
/*      */     public MaterialColor getMapColor(BlockGetter debug1, BlockPos debug2) {
/*  703 */       return this.materialColor;
/*      */     }
/*      */     
/*      */     public BlockState rotate(Rotation debug1) {
/*  707 */       return getBlock().rotate(asState(), debug1);
/*      */     }
/*      */     
/*      */     public BlockState mirror(Mirror debug1) {
/*  711 */       return getBlock().mirror(asState(), debug1);
/*      */     }
/*      */     
/*      */     public RenderShape getRenderShape() {
/*  715 */       return getBlock().getRenderShape(asState());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isRedstoneConductor(BlockGetter debug1, BlockPos debug2) {
/*  727 */       return this.isRedstoneConductor.test(asState(), debug1, debug2);
/*      */     }
/*      */     
/*      */     public boolean isSignalSource() {
/*  731 */       return getBlock().isSignalSource(asState());
/*      */     }
/*      */     
/*      */     public int getSignal(BlockGetter debug1, BlockPos debug2, Direction debug3) {
/*  735 */       return getBlock().getSignal(asState(), debug1, debug2, debug3);
/*      */     }
/*      */     
/*      */     public boolean hasAnalogOutputSignal() {
/*  739 */       return getBlock().hasAnalogOutputSignal(asState());
/*      */     }
/*      */     
/*      */     public int getAnalogOutputSignal(Level debug1, BlockPos debug2) {
/*  743 */       return getBlock().getAnalogOutputSignal(asState(), debug1, debug2);
/*      */     }
/*      */     
/*      */     public float getDestroySpeed(BlockGetter debug1, BlockPos debug2) {
/*  747 */       return this.destroySpeed;
/*      */     }
/*      */     
/*      */     public float getDestroyProgress(Player debug1, BlockGetter debug2, BlockPos debug3) {
/*  751 */       return getBlock().getDestroyProgress(asState(), debug1, debug2, debug3);
/*      */     }
/*      */     
/*      */     public int getDirectSignal(BlockGetter debug1, BlockPos debug2, Direction debug3) {
/*  755 */       return getBlock().getDirectSignal(asState(), debug1, debug2, debug3);
/*      */     }
/*      */     
/*      */     public PushReaction getPistonPushReaction() {
/*  759 */       return getBlock().getPistonPushReaction(asState());
/*      */     }
/*      */     
/*      */     public boolean isSolidRender(BlockGetter debug1, BlockPos debug2) {
/*  763 */       if (this.cache != null) {
/*  764 */         return this.cache.solidRender;
/*      */       }
/*  766 */       BlockState debug3 = asState();
/*  767 */       if (debug3.canOcclude()) {
/*  768 */         return Block.isShapeFullBlock(debug3.getOcclusionShape(debug1, debug2));
/*      */       }
/*  770 */       return false;
/*      */     }
/*      */     
/*      */     public boolean canOcclude() {
/*  774 */       return this.canOcclude;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public VoxelShape getShape(BlockGetter debug1, BlockPos debug2) {
/*  782 */       return getShape(debug1, debug2, CollisionContext.empty());
/*      */     }
/*      */     
/*      */     public VoxelShape getShape(BlockGetter debug1, BlockPos debug2, CollisionContext debug3) {
/*  786 */       return getBlock().getShape(asState(), debug1, debug2, debug3);
/*      */     }
/*      */     
/*      */     public VoxelShape getCollisionShape(BlockGetter debug1, BlockPos debug2) {
/*  790 */       if (this.cache != null) {
/*  791 */         return this.cache.collisionShape;
/*      */       }
/*  793 */       return getCollisionShape(debug1, debug2, CollisionContext.empty());
/*      */     }
/*      */     
/*      */     public VoxelShape getCollisionShape(BlockGetter debug1, BlockPos debug2, CollisionContext debug3) {
/*  797 */       return getBlock().getCollisionShape(asState(), debug1, debug2, debug3);
/*      */     }
/*      */     
/*      */     public VoxelShape getBlockSupportShape(BlockGetter debug1, BlockPos debug2) {
/*  801 */       return getBlock().getBlockSupportShape(asState(), debug1, debug2);
/*      */     }
/*      */     
/*      */     public VoxelShape getVisualShape(BlockGetter debug1, BlockPos debug2, CollisionContext debug3) {
/*  805 */       return getBlock().getVisualShape(asState(), debug1, debug2, debug3);
/*      */     }
/*      */     
/*      */     public VoxelShape getInteractionShape(BlockGetter debug1, BlockPos debug2) {
/*  809 */       return getBlock().getInteractionShape(asState(), debug1, debug2);
/*      */     }
/*      */     
/*      */     public final boolean entityCanStandOn(BlockGetter debug1, BlockPos debug2, Entity debug3) {
/*  813 */       return entityCanStandOnFace(debug1, debug2, debug3, Direction.UP);
/*      */     }
/*      */     
/*      */     public final boolean entityCanStandOnFace(BlockGetter debug1, BlockPos debug2, Entity debug3, Direction debug4) {
/*  817 */       return Block.isFaceFull(getCollisionShape(debug1, debug2, CollisionContext.of(debug3)), debug4);
/*      */     }
/*      */     
/*      */     public Vec3 getOffset(BlockGetter debug1, BlockPos debug2) {
/*  821 */       BlockBehaviour.OffsetType debug3 = getBlock().getOffsetType();
/*  822 */       if (debug3 == BlockBehaviour.OffsetType.NONE) {
/*  823 */         return Vec3.ZERO;
/*      */       }
/*      */       
/*  826 */       long debug4 = Mth.getSeed(debug2.getX(), 0, debug2.getZ());
/*  827 */       return new Vec3((((float)(debug4 & 0xFL) / 15.0F) - 0.5D) * 0.5D, (debug3 == BlockBehaviour.OffsetType.XYZ) ? ((((float)(debug4 >> 4L & 0xFL) / 15.0F) - 1.0D) * 0.2D) : 0.0D, (((float)(debug4 >> 8L & 0xFL) / 15.0F) - 0.5D) * 0.5D);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean triggerEvent(Level debug1, BlockPos debug2, int debug3, int debug4) {
/*  835 */       return getBlock().triggerEvent(asState(), debug1, debug2, debug3, debug4);
/*      */     }
/*      */     
/*      */     public void neighborChanged(Level debug1, BlockPos debug2, Block debug3, BlockPos debug4, boolean debug5) {
/*  839 */       getBlock().neighborChanged(asState(), debug1, debug2, debug3, debug4, debug5);
/*      */     }
/*      */     
/*      */     public final void updateNeighbourShapes(LevelAccessor debug1, BlockPos debug2, int debug3) {
/*  843 */       updateNeighbourShapes(debug1, debug2, debug3, 512);
/*      */     }
/*      */     
/*      */     public final void updateNeighbourShapes(LevelAccessor debug1, BlockPos debug2, int debug3, int debug4) {
/*  847 */       getBlock();
/*  848 */       BlockPos.MutableBlockPos debug5 = new BlockPos.MutableBlockPos();
/*  849 */       for (Direction debug9 : BlockBehaviour.UPDATE_SHAPE_ORDER) {
/*  850 */         debug5.setWithOffset((Vec3i)debug2, debug9);
/*  851 */         BlockState debug10 = debug1.getBlockState((BlockPos)debug5);
/*  852 */         BlockState debug11 = debug10.updateShape(debug9.getOpposite(), asState(), debug1, (BlockPos)debug5, debug2);
/*  853 */         Block.updateOrDestroy(debug10, debug11, debug1, (BlockPos)debug5, debug3, debug4);
/*      */       } 
/*      */     }
/*      */     
/*      */     public final void updateIndirectNeighbourShapes(LevelAccessor debug1, BlockPos debug2, int debug3) {
/*  858 */       updateIndirectNeighbourShapes(debug1, debug2, debug3, 512);
/*      */     }
/*      */     
/*      */     public void updateIndirectNeighbourShapes(LevelAccessor debug1, BlockPos debug2, int debug3, int debug4) {
/*  862 */       getBlock().updateIndirectNeighbourShapes(asState(), debug1, debug2, debug3, debug4);
/*      */     }
/*      */     
/*      */     public void onPlace(Level debug1, BlockPos debug2, BlockState debug3, boolean debug4) {
/*  866 */       getBlock().onPlace(asState(), debug1, debug2, debug3, debug4);
/*      */     }
/*      */     
/*      */     public void onRemove(Level debug1, BlockPos debug2, BlockState debug3, boolean debug4) {
/*  870 */       getBlock().onRemove(asState(), debug1, debug2, debug3, debug4);
/*      */     }
/*      */     
/*      */     public void tick(ServerLevel debug1, BlockPos debug2, Random debug3) {
/*  874 */       getBlock().tick(asState(), debug1, debug2, debug3);
/*      */     }
/*      */     
/*      */     public void randomTick(ServerLevel debug1, BlockPos debug2, Random debug3) {
/*  878 */       getBlock().randomTick(asState(), debug1, debug2, debug3);
/*      */     }
/*      */     
/*      */     public void entityInside(Level debug1, BlockPos debug2, Entity debug3) {
/*  882 */       getBlock().entityInside(asState(), debug1, debug2, debug3);
/*      */     }
/*      */     
/*      */     public void spawnAfterBreak(ServerLevel debug1, BlockPos debug2, ItemStack debug3) {
/*  886 */       getBlock().spawnAfterBreak(asState(), debug1, debug2, debug3);
/*      */     }
/*      */     
/*      */     public List<ItemStack> getDrops(LootContext.Builder debug1) {
/*  890 */       return getBlock().getDrops(asState(), debug1);
/*      */     }
/*      */     
/*      */     public InteractionResult use(Level debug1, Player debug2, InteractionHand debug3, BlockHitResult debug4) {
/*  894 */       return getBlock().use(asState(), debug1, debug4.getBlockPos(), debug2, debug3, debug4);
/*      */     }
/*      */     
/*      */     public void attack(Level debug1, BlockPos debug2, Player debug3) {
/*  898 */       getBlock().attack(asState(), debug1, debug2, debug3);
/*      */     }
/*      */     
/*      */     public boolean isSuffocating(BlockGetter debug1, BlockPos debug2) {
/*  902 */       return this.isSuffocating.test(asState(), debug1, debug2);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public BlockState updateShape(Direction debug1, BlockState debug2, LevelAccessor debug3, BlockPos debug4, BlockPos debug5) {
/*  910 */       return getBlock().updateShape(asState(), debug1, debug2, debug3, debug4, debug5);
/*      */     }
/*      */     
/*      */     public boolean isPathfindable(BlockGetter debug1, BlockPos debug2, PathComputationType debug3) {
/*  914 */       return getBlock().isPathfindable(asState(), debug1, debug2, debug3);
/*      */     }
/*      */     
/*      */     public boolean canBeReplaced(BlockPlaceContext debug1) {
/*  918 */       return getBlock().canBeReplaced(asState(), debug1);
/*      */     }
/*      */     
/*      */     public boolean canBeReplaced(Fluid debug1) {
/*  922 */       return getBlock().canBeReplaced(asState(), debug1);
/*      */     }
/*      */     
/*      */     public boolean canSurvive(LevelReader debug1, BlockPos debug2) {
/*  926 */       return getBlock().canSurvive(asState(), debug1, debug2);
/*      */     }
/*      */     
/*      */     public boolean hasPostProcess(BlockGetter debug1, BlockPos debug2) {
/*  930 */       return this.hasPostProcess.test(asState(), debug1, debug2);
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     public MenuProvider getMenuProvider(Level debug1, BlockPos debug2) {
/*  935 */       return getBlock().getMenuProvider(asState(), debug1, debug2);
/*      */     }
/*      */     
/*      */     public boolean is(Tag<Block> debug1) {
/*  939 */       return getBlock().is(debug1);
/*      */     }
/*      */     
/*      */     public boolean is(Tag<Block> debug1, Predicate<BlockStateBase> debug2) {
/*  943 */       return (getBlock().is(debug1) && debug2.test(this));
/*      */     }
/*      */     
/*      */     public boolean is(Block debug1) {
/*  947 */       return getBlock().is(debug1);
/*      */     }
/*      */     
/*      */     public FluidState getFluidState() {
/*  951 */       return getBlock().getFluidState(asState());
/*      */     }
/*      */     
/*      */     public boolean isRandomlyTicking() {
/*  955 */       return getBlock().isRandomlyTicking(asState());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SoundType getSoundType() {
/*  963 */       return getBlock().getSoundType(asState());
/*      */     }
/*      */     
/*      */     public void onProjectileHit(Level debug1, BlockState debug2, BlockHitResult debug3, Projectile debug4) {
/*  967 */       getBlock().onProjectileHit(debug1, debug2, debug3, debug4);
/*      */     }
/*      */     
/*      */     public boolean isFaceSturdy(BlockGetter debug1, BlockPos debug2, Direction debug3) {
/*  971 */       return isFaceSturdy(debug1, debug2, debug3, SupportType.FULL);
/*      */     }
/*      */     
/*      */     public boolean isFaceSturdy(BlockGetter debug1, BlockPos debug2, Direction debug3, SupportType debug4) {
/*  975 */       if (this.cache != null) {
/*  976 */         return this.cache.isFaceSturdy(debug3, debug4);
/*      */       }
/*  978 */       return debug4.isSupporting(asState(), debug1, debug2, debug3);
/*      */     }
/*      */     
/*      */     public boolean isCollisionShapeFullBlock(BlockGetter debug1, BlockPos debug2) {
/*  982 */       if (this.cache != null) {
/*  983 */         return this.cache.isCollisionShapeFullBlock;
/*      */       }
/*  985 */       return Block.isShapeFullBlock(getCollisionShape(debug1, debug2));
/*      */     }
/*      */     
/*      */     protected abstract BlockState asState();
/*      */     
/*      */     public boolean requiresCorrectToolForDrops() {
/*  991 */       return this.requiresCorrectToolForDrops;
/*      */     }
/*      */     
/*      */     static final class Cache {
/*  995 */       private static final Direction[] DIRECTIONS = Direction.values();
/*  996 */       private static final int SUPPORT_TYPE_COUNT = (SupportType.values()).length;
/*      */       protected final boolean solidRender;
/*      */       private final boolean propagatesSkylightDown;
/*      */       private final int lightBlock;
/*      */       @Nullable
/*      */       private final VoxelShape[] occlusionShapes;
/*      */       protected final VoxelShape collisionShape;
/*      */       protected final boolean largeCollisionShape;
/*      */       private final boolean[] faceSturdy;
/*      */       protected final boolean isCollisionShapeFullBlock;
/*      */       
/*      */       private Cache(BlockState debug1) {
/* 1008 */         Block debug2 = debug1.getBlock();
/* 1009 */         this.solidRender = debug1.isSolidRender((BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
/* 1010 */         this.propagatesSkylightDown = debug2.propagatesSkylightDown(debug1, (BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
/* 1011 */         this.lightBlock = debug2.getLightBlock(debug1, (BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
/* 1012 */         if (!debug1.canOcclude()) {
/* 1013 */           this.occlusionShapes = null;
/*      */         } else {
/* 1015 */           this.occlusionShapes = new VoxelShape[DIRECTIONS.length];
/* 1016 */           VoxelShape debug3 = debug2.getOcclusionShape(debug1, (BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
/* 1017 */           for (Direction debug7 : DIRECTIONS) {
/* 1018 */             this.occlusionShapes[debug7.ordinal()] = Shapes.getFaceShape(debug3, debug7);
/*      */           }
/*      */         } 
/*      */         
/* 1022 */         this.collisionShape = debug2.getCollisionShape(debug1, (BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO, CollisionContext.empty());
/* 1023 */         this.largeCollisionShape = Arrays.<Direction.Axis>stream(Direction.Axis.values()).anyMatch(debug1 -> (this.collisionShape.min(debug1) < 0.0D || this.collisionShape.max(debug1) > 1.0D));
/* 1024 */         this.faceSturdy = new boolean[DIRECTIONS.length * SUPPORT_TYPE_COUNT];
/* 1025 */         for (Direction debug6 : DIRECTIONS) {
/* 1026 */           for (SupportType debug10 : SupportType.values()) {
/* 1027 */             this.faceSturdy[getFaceSupportIndex(debug6, debug10)] = debug10.isSupporting(debug1, (BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO, debug6);
/*      */           }
/*      */         } 
/* 1030 */         this.isCollisionShapeFullBlock = Block.isShapeFullBlock(debug1.getCollisionShape((BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
/*      */       }
/*      */       
/*      */       public boolean isFaceSturdy(Direction debug1, SupportType debug2) {
/* 1034 */         return this.faceSturdy[getFaceSupportIndex(debug1, debug2)];
/*      */       }
/*      */       
/*      */       private static int getFaceSupportIndex(Direction debug0, SupportType debug1) {
/* 1038 */         return debug0.ordinal() * SUPPORT_TYPE_COUNT + debug1.ordinal();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static interface StateArgumentPredicate<A> {
/*      */     boolean test(BlockState param1BlockState, BlockGetter param1BlockGetter, BlockPos param1BlockPos, A param1A);
/*      */   }
/*      */   
/*      */   public static interface StatePredicate {
/*      */     boolean test(BlockState param1BlockState, BlockGetter param1BlockGetter, BlockPos param1BlockPos);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\BlockBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */