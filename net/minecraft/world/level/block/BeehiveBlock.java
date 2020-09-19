/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.animal.Bee;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeehiveBlock
/*     */   extends BaseEntityBlock
/*     */ {
/*  55 */   private static final Direction[] SPAWN_DIRECTIONS = new Direction[] { Direction.WEST, Direction.EAST, Direction.SOUTH };
/*     */   
/*  57 */   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
/*  58 */   public static final IntegerProperty HONEY_LEVEL = BlockStateProperties.LEVEL_HONEY;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeehiveBlock(BlockBehaviour.Properties debug1) {
/*  64 */     super(debug1);
/*  65 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HONEY_LEVEL, Integer.valueOf(0))).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnalogOutputSignal(BlockState debug1) {
/*  70 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAnalogOutputSignal(BlockState debug1, Level debug2, BlockPos debug3) {
/*  75 */     return ((Integer)debug1.getValue((Property)HONEY_LEVEL)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerDestroy(Level debug1, Player debug2, BlockPos debug3, BlockState debug4, @Nullable BlockEntity debug5, ItemStack debug6) {
/*  80 */     super.playerDestroy(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */     
/*  82 */     if (!debug1.isClientSide && 
/*  83 */       debug5 instanceof BeehiveBlockEntity) {
/*  84 */       BeehiveBlockEntity debug7 = (BeehiveBlockEntity)debug5;
/*     */       
/*  86 */       if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, debug6) == 0) {
/*  87 */         debug7.emptyAllLivingFromHive(debug2, debug4, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
/*     */         
/*  89 */         debug1.updateNeighbourForOutputSignal(debug3, this);
/*     */         
/*  91 */         angerNearbyBees(debug1, debug3);
/*     */       } 
/*     */       
/*  94 */       CriteriaTriggers.BEE_NEST_DESTROYED.trigger((ServerPlayer)debug2, debug4.getBlock(), debug6, debug7.getOccupantCount());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void angerNearbyBees(Level debug1, BlockPos debug2) {
/* 100 */     List<Bee> debug3 = debug1.getEntitiesOfClass(Bee.class, (new AABB(debug2)).inflate(8.0D, 6.0D, 8.0D));
/* 101 */     if (!debug3.isEmpty()) {
/* 102 */       List<Player> debug4 = debug1.getEntitiesOfClass(Player.class, (new AABB(debug2)).inflate(8.0D, 6.0D, 8.0D));
/* 103 */       int debug5 = debug4.size();
/* 104 */       for (Bee debug7 : debug3) {
/* 105 */         if (debug7.getTarget() == null) {
/* 106 */           debug7.setTarget((LivingEntity)debug4.get(debug1.random.nextInt(debug5)));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void dropHoneycomb(Level debug0, BlockPos debug1) {
/* 113 */     popResource(debug0, debug1, new ItemStack((ItemLike)Items.HONEYCOMB, 3));
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 118 */     ItemStack debug7 = debug4.getItemInHand(debug5);
/* 119 */     int debug8 = ((Integer)debug1.getValue((Property)HONEY_LEVEL)).intValue();
/* 120 */     boolean debug9 = false;
/*     */     
/* 122 */     if (debug8 >= 5) {
/* 123 */       if (debug7.getItem() == Items.SHEARS) {
/* 124 */         debug2.playSound(debug4, debug4.getX(), debug4.getY(), debug4.getZ(), SoundEvents.BEEHIVE_SHEAR, SoundSource.NEUTRAL, 1.0F, 1.0F);
/* 125 */         dropHoneycomb(debug2, debug3);
/* 126 */         debug7.hurtAndBreak(1, (LivingEntity)debug4, debug1 -> debug1.broadcastBreakEvent(debug0));
/* 127 */         debug9 = true;
/* 128 */       } else if (debug7.getItem() == Items.GLASS_BOTTLE) {
/* 129 */         debug7.shrink(1);
/* 130 */         debug2.playSound(debug4, debug4.getX(), debug4.getY(), debug4.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
/* 131 */         if (debug7.isEmpty()) {
/* 132 */           debug4.setItemInHand(debug5, new ItemStack((ItemLike)Items.HONEY_BOTTLE));
/* 133 */         } else if (!debug4.inventory.add(new ItemStack((ItemLike)Items.HONEY_BOTTLE))) {
/* 134 */           debug4.drop(new ItemStack((ItemLike)Items.HONEY_BOTTLE), false);
/*     */         } 
/* 136 */         debug9 = true;
/*     */       } 
/*     */     }
/*     */     
/* 140 */     if (debug9) {
/* 141 */       if (!CampfireBlock.isSmokeyPos(debug2, debug3)) {
/*     */         
/* 143 */         if (hiveContainsBees(debug2, debug3)) {
/* 144 */           angerNearbyBees(debug2, debug3);
/*     */         }
/* 146 */         releaseBeesAndResetHoneyLevel(debug2, debug1, debug3, debug4, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
/*     */       } else {
/* 148 */         resetHoneyLevel(debug2, debug1, debug3);
/*     */       } 
/* 150 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */     } 
/*     */     
/* 153 */     return super.use(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */   
/*     */   private boolean hiveContainsBees(Level debug1, BlockPos debug2) {
/* 157 */     BlockEntity debug3 = debug1.getBlockEntity(debug2);
/* 158 */     if (debug3 instanceof BeehiveBlockEntity) {
/* 159 */       BeehiveBlockEntity debug4 = (BeehiveBlockEntity)debug3;
/* 160 */       return !debug4.isEmpty();
/*     */     } 
/*     */     
/* 163 */     return false;
/*     */   }
/*     */   
/*     */   public void releaseBeesAndResetHoneyLevel(Level debug1, BlockState debug2, BlockPos debug3, @Nullable Player debug4, BeehiveBlockEntity.BeeReleaseStatus debug5) {
/* 167 */     resetHoneyLevel(debug1, debug2, debug3);
/*     */     
/* 169 */     BlockEntity debug6 = debug1.getBlockEntity(debug3);
/* 170 */     if (debug6 instanceof BeehiveBlockEntity) {
/* 171 */       BeehiveBlockEntity debug7 = (BeehiveBlockEntity)debug6;
/* 172 */       debug7.emptyAllLivingFromHive(debug4, debug2, debug5);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void resetHoneyLevel(Level debug1, BlockState debug2, BlockPos debug3) {
/* 177 */     debug1.setBlock(debug3, (BlockState)debug2.setValue((Property)HONEY_LEVEL, Integer.valueOf(0)), 3);
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
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 226 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug1.getHorizontalDirection().getOpposite());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 231 */     debug1.add(new Property[] { (Property)HONEY_LEVEL, (Property)FACING });
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderShape getRenderShape(BlockState debug1) {
/* 236 */     return RenderShape.MODEL;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 242 */     return (BlockEntity)new BeehiveBlockEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerWillDestroy(Level debug1, BlockPos debug2, BlockState debug3, Player debug4) {
/* 247 */     if (!debug1.isClientSide && debug4.isCreative() && debug1.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
/* 248 */       BlockEntity debug5 = debug1.getBlockEntity(debug2);
/* 249 */       if (debug5 instanceof BeehiveBlockEntity) {
/* 250 */         BeehiveBlockEntity debug6 = (BeehiveBlockEntity)debug5;
/* 251 */         ItemStack debug7 = new ItemStack(this);
/* 252 */         int debug8 = ((Integer)debug3.getValue((Property)HONEY_LEVEL)).intValue();
/* 253 */         boolean debug9 = !debug6.isEmpty();
/*     */ 
/*     */         
/* 256 */         if (!debug9 && debug8 == 0) {
/*     */           return;
/*     */         }
/*     */         
/* 260 */         if (debug9) {
/* 261 */           CompoundTag compoundTag = new CompoundTag();
/* 262 */           compoundTag.put("Bees", (Tag)debug6.writeBees());
/* 263 */           debug7.addTagElement("BlockEntityTag", (Tag)compoundTag);
/*     */         } 
/*     */ 
/*     */         
/* 267 */         CompoundTag debug10 = new CompoundTag();
/* 268 */         debug10.putInt("honey_level", debug8);
/* 269 */         debug7.addTagElement("BlockStateTag", (Tag)debug10);
/*     */         
/* 271 */         ItemEntity debug11 = new ItemEntity(debug1, debug2.getX(), debug2.getY(), debug2.getZ(), debug7);
/* 272 */         debug11.setDefaultPickUpDelay();
/* 273 */         debug1.addFreshEntity((Entity)debug11);
/*     */       } 
/*     */     } 
/*     */     
/* 277 */     super.playerWillDestroy(debug1, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ItemStack> getDrops(BlockState debug1, LootContext.Builder debug2) {
/* 282 */     Entity debug3 = (Entity)debug2.getOptionalParameter(LootContextParams.THIS_ENTITY);
/*     */ 
/*     */     
/* 285 */     if (debug3 instanceof net.minecraft.world.entity.item.PrimedTnt || debug3 instanceof net.minecraft.world.entity.monster.Creeper || debug3 instanceof net.minecraft.world.entity.projectile.WitherSkull || debug3 instanceof net.minecraft.world.entity.boss.wither.WitherBoss || debug3 instanceof net.minecraft.world.entity.vehicle.MinecartTNT) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 290 */       BlockEntity debug4 = (BlockEntity)debug2.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
/* 291 */       if (debug4 instanceof BeehiveBlockEntity) {
/* 292 */         BeehiveBlockEntity debug5 = (BeehiveBlockEntity)debug4;
/* 293 */         debug5.emptyAllLivingFromHive(null, debug1, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
/*     */       } 
/*     */     } 
/* 296 */     return super.getDrops(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 301 */     if (debug4.getBlockState(debug6).getBlock() instanceof FireBlock) {
/*     */       
/* 303 */       BlockEntity debug7 = debug4.getBlockEntity(debug5);
/* 304 */       if (debug7 instanceof BeehiveBlockEntity) {
/* 305 */         BeehiveBlockEntity debug8 = (BeehiveBlockEntity)debug7;
/* 306 */         debug8.emptyAllLivingFromHive(null, debug1, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
/*     */       } 
/*     */     } 
/* 309 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */   
/*     */   public static Direction getRandomOffset(Random debug0) {
/* 313 */     return (Direction)Util.getRandom((Object[])SPAWN_DIRECTIONS, debug0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\BeehiveBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */