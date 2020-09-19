/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.DismountHelper;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.ExplosionDamageCalculator;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class RespawnAnchorBlock
/*     */   extends Block
/*     */ {
/*  42 */   public static final IntegerProperty CHARGE = BlockStateProperties.RESPAWN_ANCHOR_CHARGES;
/*     */   
/*  44 */   private static final ImmutableList<Vec3i> RESPAWN_HORIZONTAL_OFFSETS = ImmutableList.of(new Vec3i(0, 0, -1), new Vec3i(-1, 0, 0), new Vec3i(0, 0, 1), new Vec3i(1, 0, 0), new Vec3i(-1, 0, -1), new Vec3i(1, 0, -1), new Vec3i(-1, 0, 1), new Vec3i(1, 0, 1));
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
/*  55 */   private static final ImmutableList<Vec3i> RESPAWN_OFFSETS = (new ImmutableList.Builder())
/*  56 */     .addAll((Iterable)RESPAWN_HORIZONTAL_OFFSETS)
/*  57 */     .addAll(RESPAWN_HORIZONTAL_OFFSETS.stream().map(Vec3i::below).iterator())
/*  58 */     .addAll(RESPAWN_HORIZONTAL_OFFSETS.stream().map(Vec3i::above).iterator())
/*  59 */     .add(new Vec3i(0, 1, 0))
/*  60 */     .build();
/*     */   
/*     */   public RespawnAnchorBlock(BlockBehaviour.Properties debug1) {
/*  63 */     super(debug1);
/*  64 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)CHARGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  69 */     ItemStack debug7 = debug4.getItemInHand(debug5);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     if (debug5 == InteractionHand.MAIN_HAND && 
/*  75 */       !isRespawnFuel(debug7) && 
/*  76 */       isRespawnFuel(debug4.getItemInHand(InteractionHand.OFF_HAND))) {
/*  77 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/*  80 */     if (isRespawnFuel(debug7) && 
/*  81 */       canBeCharged(debug1)) {
/*  82 */       charge(debug2, debug3, debug1);
/*  83 */       if (!debug4.abilities.instabuild) {
/*  84 */         debug7.shrink(1);
/*     */       }
/*     */       
/*  87 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */     } 
/*     */ 
/*     */     
/*  91 */     if (((Integer)debug1.getValue((Property)CHARGE)).intValue() == 0) {
/*  92 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/*  95 */     if (canSetSpawn(debug2)) {
/*  96 */       if (!debug2.isClientSide) {
/*  97 */         ServerPlayer debug8 = (ServerPlayer)debug4;
/*  98 */         if (debug8.getRespawnDimension() != debug2.dimension() || !debug8.getRespawnPosition().equals(debug3)) {
/*  99 */           debug8.setRespawnPosition(debug2.dimension(), debug3, 0.0F, false, true);
/* 100 */           debug2.playSound(null, debug3.getX() + 0.5D, debug3.getY() + 0.5D, debug3.getZ() + 0.5D, SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 101 */           return InteractionResult.SUCCESS;
/*     */         } 
/*     */       } 
/*     */       
/* 105 */       return InteractionResult.CONSUME;
/*     */     } 
/* 107 */     if (!debug2.isClientSide) {
/* 108 */       explode(debug1, debug2, debug3);
/*     */     }
/* 110 */     return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isRespawnFuel(ItemStack debug0) {
/* 115 */     return (debug0.getItem() == Items.GLOWSTONE);
/*     */   }
/*     */   
/*     */   private static boolean canBeCharged(BlockState debug0) {
/* 119 */     return (((Integer)debug0.getValue((Property)CHARGE)).intValue() < 4);
/*     */   }
/*     */   
/*     */   private static boolean isWaterThatWouldFlow(BlockPos debug0, Level debug1) {
/* 123 */     FluidState debug2 = debug1.getFluidState(debug0);
/* 124 */     if (!debug2.is((Tag)FluidTags.WATER)) {
/* 125 */       return false;
/*     */     }
/* 127 */     if (debug2.isSource()) {
/* 128 */       return true;
/*     */     }
/* 130 */     float debug3 = debug2.getAmount();
/* 131 */     if (debug3 < 2.0F) {
/* 132 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 136 */     FluidState debug4 = debug1.getFluidState(debug0.below());
/* 137 */     return !debug4.is((Tag)FluidTags.WATER);
/*     */   }
/*     */   
/*     */   private void explode(BlockState debug1, Level debug2, final BlockPos pos) {
/* 141 */     debug2.removeBlock(pos, false);
/*     */     
/* 143 */     boolean debug4 = Direction.Plane.HORIZONTAL.stream().map(pos::relative).anyMatch(debug1 -> isWaterThatWouldFlow(debug1, debug0));
/* 144 */     final boolean inWater = (debug4 || debug2.getFluidState(pos.above()).is((Tag)FluidTags.WATER));
/* 145 */     ExplosionDamageCalculator debug6 = new ExplosionDamageCalculator()
/*     */       {
/*     */         public Optional<Float> getBlockExplosionResistance(Explosion debug1, BlockGetter debug2, BlockPos debug3, BlockState debug4, FluidState debug5) {
/* 148 */           if (debug3.equals(pos) && inWater)
/*     */           {
/* 150 */             return Optional.of(Float.valueOf(Blocks.WATER.getExplosionResistance()));
/*     */           }
/* 152 */           return super.getBlockExplosionResistance(debug1, debug2, debug3, debug4, debug5);
/*     */         }
/*     */       };
/* 155 */     debug2.explode(null, DamageSource.badRespawnPointExplosion(), debug6, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 5.0F, true, Explosion.BlockInteraction.DESTROY);
/*     */   }
/*     */   
/*     */   public static boolean canSetSpawn(Level debug0) {
/* 159 */     return debug0.dimensionType().respawnAnchorWorks();
/*     */   }
/*     */   
/*     */   public static void charge(Level debug0, BlockPos debug1, BlockState debug2) {
/* 163 */     debug0.setBlock(debug1, (BlockState)debug2.setValue((Property)CHARGE, Integer.valueOf(((Integer)debug2.getValue((Property)CHARGE)).intValue() + 1)), 3);
/* 164 */     debug0.playSound(null, debug1.getX() + 0.5D, debug1.getY() + 0.5D, debug1.getZ() + 0.5D, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 1.0F, 1.0F);
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
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 187 */     debug1.add(new Property[] { (Property)CHARGE });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnalogOutputSignal(BlockState debug1) {
/* 192 */     return true;
/*     */   }
/*     */   
/*     */   public static int getScaledChargeLevel(BlockState debug0, int debug1) {
/* 196 */     return Mth.floor((((Integer)debug0.getValue((Property)CHARGE)).intValue() - 0) / 4.0F * debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAnalogOutputSignal(BlockState debug1, Level debug2, BlockPos debug3) {
/* 201 */     return getScaledChargeLevel(debug1, 15);
/*     */   }
/*     */   
/*     */   public static Optional<Vec3> findStandUpPosition(EntityType<?> debug0, CollisionGetter debug1, BlockPos debug2) {
/* 205 */     Optional<Vec3> debug3 = findStandUpPosition(debug0, debug1, debug2, true);
/* 206 */     if (debug3.isPresent()) {
/* 207 */       return debug3;
/*     */     }
/* 209 */     return findStandUpPosition(debug0, debug1, debug2, false);
/*     */   }
/*     */   
/*     */   private static Optional<Vec3> findStandUpPosition(EntityType<?> debug0, CollisionGetter debug1, BlockPos debug2, boolean debug3) {
/* 213 */     BlockPos.MutableBlockPos debug4 = new BlockPos.MutableBlockPos();
/* 214 */     for (UnmodifiableIterator<Vec3i> unmodifiableIterator = RESPAWN_OFFSETS.iterator(); unmodifiableIterator.hasNext(); ) { Vec3i debug6 = unmodifiableIterator.next();
/* 215 */       debug4.set((Vec3i)debug2).move(debug6);
/*     */       
/* 217 */       Vec3 debug7 = DismountHelper.findSafeDismountLocation(debug0, debug1, (BlockPos)debug4, debug3);
/* 218 */       if (debug7 != null) {
/* 219 */         return Optional.of(debug7);
/*     */       } }
/*     */     
/* 222 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 227 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\RespawnAnchorBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */