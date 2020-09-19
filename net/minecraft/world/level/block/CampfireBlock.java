/*     */ package net.minecraft.world.level.block;
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.item.crafting.CampfireCookingRecipe;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.CampfireBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CampfireBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
/*  48 */   protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D);
/*  49 */   public static final BooleanProperty LIT = BlockStateProperties.LIT;
/*  50 */   public static final BooleanProperty SIGNAL_FIRE = BlockStateProperties.SIGNAL_FIRE;
/*  51 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  52 */   public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
/*     */ 
/*     */   
/*  55 */   private static final VoxelShape VIRTUAL_FENCE_POST = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
/*     */   
/*     */   private final boolean spawnParticles;
/*     */   
/*     */   private final int fireDamage;
/*     */   
/*     */   public CampfireBlock(boolean debug1, int debug2, BlockBehaviour.Properties debug3) {
/*  62 */     super(debug3);
/*  63 */     this.spawnParticles = debug1;
/*  64 */     this.fireDamage = debug2;
/*  65 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)LIT, Boolean.valueOf(true))).setValue((Property)SIGNAL_FIRE, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false))).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  70 */     BlockEntity debug7 = debug2.getBlockEntity(debug3);
/*  71 */     if (debug7 instanceof CampfireBlockEntity) {
/*  72 */       CampfireBlockEntity debug8 = (CampfireBlockEntity)debug7;
/*  73 */       ItemStack debug9 = debug4.getItemInHand(debug5);
/*  74 */       Optional<CampfireCookingRecipe> debug10 = debug8.getCookableRecipe(debug9);
/*  75 */       if (debug10.isPresent()) {
/*  76 */         if (!debug2.isClientSide && debug8.placeFood(debug4.abilities.instabuild ? debug9.copy() : debug9, ((CampfireCookingRecipe)debug10.get()).getCookingTime())) {
/*  77 */           debug4.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
/*  78 */           return InteractionResult.SUCCESS;
/*     */         } 
/*  80 */         return InteractionResult.CONSUME;
/*     */       } 
/*     */     } 
/*     */     
/*  84 */     return InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */   
/*     */   public void entityInside(BlockState debug1, Level debug2, BlockPos debug3, Entity debug4) {
/*  89 */     if (!debug4.fireImmune() && ((Boolean)debug1.getValue((Property)LIT)).booleanValue() && debug4 instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)debug4)) {
/*  90 */       debug4.hurt(DamageSource.IN_FIRE, this.fireDamage);
/*     */     }
/*     */     
/*  93 */     super.entityInside(debug1, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/*  98 */     if (debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/*     */     
/* 102 */     BlockEntity debug6 = debug2.getBlockEntity(debug3);
/* 103 */     if (debug6 instanceof CampfireBlockEntity) {
/* 104 */       Containers.dropContents(debug2, debug3, ((CampfireBlockEntity)debug6).getItems());
/*     */     }
/*     */     
/* 107 */     super.onRemove(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 113 */     Level level = debug1.getLevel();
/* 114 */     BlockPos debug3 = debug1.getClickedPos();
/* 115 */     boolean debug4 = (level.getFluidState(debug3).getType() == Fluids.WATER);
/* 116 */     return (BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState()
/* 117 */       .setValue((Property)WATERLOGGED, Boolean.valueOf(debug4)))
/* 118 */       .setValue((Property)SIGNAL_FIRE, Boolean.valueOf(isSmokeSource(level.getBlockState(debug3.below())))))
/* 119 */       .setValue((Property)LIT, Boolean.valueOf(!debug4)))
/* 120 */       .setValue((Property)FACING, (Comparable)debug1.getHorizontalDirection());
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 125 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 126 */       debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/*     */     }
/*     */     
/* 129 */     if (debug2 == Direction.DOWN) {
/* 130 */       return (BlockState)debug1.setValue((Property)SIGNAL_FIRE, Boolean.valueOf(isSmokeSource(debug3)));
/*     */     }
/* 132 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */   
/*     */   private boolean isSmokeSource(BlockState debug1) {
/* 136 */     return debug1.is(Blocks.HAY_BLOCK);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 141 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderShape getRenderShape(BlockState debug1) {
/* 146 */     return RenderShape.MODEL;
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
/*     */   public static void dowse(LevelAccessor debug0, BlockPos debug1, BlockState debug2) {
/* 167 */     if (debug0.isClientSide()) {
/* 168 */       for (int i = 0; i < 20; i++) {
/* 169 */         makeParticles((Level)debug0, debug1, ((Boolean)debug2.getValue((Property)SIGNAL_FIRE)).booleanValue(), true);
/*     */       }
/*     */     }
/*     */     
/* 173 */     BlockEntity debug3 = debug0.getBlockEntity(debug1);
/* 174 */     if (debug3 instanceof CampfireBlockEntity) {
/* 175 */       ((CampfireBlockEntity)debug3).dowse();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean placeLiquid(LevelAccessor debug1, BlockPos debug2, BlockState debug3, FluidState debug4) {
/* 181 */     if (!((Boolean)debug3.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue() && debug4.getType() == Fluids.WATER) {
/* 182 */       boolean debug5 = ((Boolean)debug3.getValue((Property)LIT)).booleanValue();
/* 183 */       if (debug5) {
/* 184 */         if (!debug1.isClientSide()) {
/* 185 */           debug1.playSound(null, debug2, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */         }
/*     */         
/* 188 */         dowse(debug1, debug2, debug3);
/*     */       } 
/*     */       
/* 191 */       debug1.setBlock(debug2, (BlockState)((BlockState)debug3.setValue((Property)WATERLOGGED, Boolean.valueOf(true))).setValue((Property)LIT, Boolean.valueOf(false)), 3);
/* 192 */       debug1.getLiquidTicks().scheduleTick(debug2, debug4.getType(), debug4.getType().getTickDelay((LevelReader)debug1));
/* 193 */       return true;
/*     */     } 
/* 195 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onProjectileHit(Level debug1, BlockState debug2, BlockHitResult debug3, Projectile debug4) {
/* 200 */     if (!debug1.isClientSide && debug4.isOnFire()) {
/* 201 */       Entity debug5 = debug4.getOwner();
/* 202 */       boolean debug6 = (debug5 == null || debug5 instanceof Player || debug1.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING));
/* 203 */       if (debug6 && !((Boolean)debug2.getValue((Property)LIT)).booleanValue() && !((Boolean)debug2.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 204 */         BlockPos debug7 = debug3.getBlockPos();
/* 205 */         debug1.setBlock(debug7, (BlockState)debug2.setValue((Property)BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void makeParticles(Level debug0, BlockPos debug1, boolean debug2, boolean debug3) {
/* 211 */     Random debug4 = debug0.getRandom();
/* 212 */     SimpleParticleType debug5 = debug2 ? ParticleTypes.CAMPFIRE_SIGNAL_SMOKE : ParticleTypes.CAMPFIRE_COSY_SMOKE;
/* 213 */     debug0.addAlwaysVisibleParticle((ParticleOptions)debug5, true, debug1
/*     */         
/* 215 */         .getX() + 0.5D + debug4.nextDouble() / 3.0D * (debug4.nextBoolean() ? true : -1), debug1
/* 216 */         .getY() + debug4.nextDouble() + debug4.nextDouble(), debug1
/* 217 */         .getZ() + 0.5D + debug4.nextDouble() / 3.0D * (debug4.nextBoolean() ? true : -1), 0.0D, 0.07D, 0.0D);
/*     */ 
/*     */     
/* 220 */     if (debug3) {
/* 221 */       debug0.addParticle((ParticleOptions)ParticleTypes.SMOKE, debug1
/* 222 */           .getX() + 0.25D + debug4.nextDouble() / 2.0D * (debug4.nextBoolean() ? true : -1), debug1
/* 223 */           .getY() + 0.4D, debug1
/* 224 */           .getZ() + 0.25D + debug4.nextDouble() / 2.0D * (debug4.nextBoolean() ? true : -1), 0.0D, 0.005D, 0.0D);
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
/*     */   public static boolean isSmokeyPos(Level debug0, BlockPos debug1) {
/* 237 */     for (int debug2 = 1; debug2 <= 5; debug2++) {
/* 238 */       BlockPos debug3 = debug1.below(debug2);
/* 239 */       BlockState debug4 = debug0.getBlockState(debug3);
/* 240 */       if (isLitCampfire(debug4)) {
/* 241 */         return true;
/*     */       }
/*     */       
/* 244 */       boolean debug5 = Shapes.joinIsNotEmpty(VIRTUAL_FENCE_POST, debug4.getCollisionShape((BlockGetter)debug0, debug1, CollisionContext.empty()), BooleanOp.AND);
/* 245 */       if (debug5) {
/*     */ 
/*     */         
/* 248 */         BlockState debug6 = debug0.getBlockState(debug3.below());
/* 249 */         return isLitCampfire(debug6);
/*     */       } 
/*     */     } 
/* 252 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isLitCampfire(BlockState debug0) {
/* 257 */     return (debug0.hasProperty((Property)LIT) && debug0.is((Tag)BlockTags.CAMPFIRES) && ((Boolean)debug0.getValue((Property)LIT)).booleanValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockState debug1) {
/* 262 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 263 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 265 */     return super.getFluidState(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 270 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 275 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 280 */     debug1.add(new Property[] { (Property)LIT, (Property)SIGNAL_FIRE, (Property)WATERLOGGED, (Property)FACING });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 285 */     return (BlockEntity)new CampfireBlockEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 290 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean canLight(BlockState debug0) {
/* 294 */     return (debug0.is((Tag)BlockTags.CAMPFIRES, debug0 -> (debug0.hasProperty((Property)BlockStateProperties.WATERLOGGED) && debug0.hasProperty((Property)BlockStateProperties.LIT))) && 
/* 295 */       !((Boolean)debug0.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue() && !((Boolean)debug0.getValue((Property)BlockStateProperties.LIT)).booleanValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\CampfireBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */