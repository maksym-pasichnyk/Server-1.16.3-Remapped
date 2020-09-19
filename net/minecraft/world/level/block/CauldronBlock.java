/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.BlockItem;
/*     */ import net.minecraft.world.item.DyeableLeatherItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.PotionUtils;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BannerBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CauldronBlock extends Block {
/*  39 */   public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_CAULDRON;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   private static final VoxelShape INSIDE = box(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
/*     */   
/*  50 */   protected static final VoxelShape SHAPE = Shapes.join(
/*  51 */       Shapes.block(), 
/*  52 */       Shapes.or(
/*  53 */         box(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), new VoxelShape[] {
/*  54 */           box(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), 
/*  55 */           box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE
/*     */         }), BooleanOp.ONLY_FIRST);
/*     */ 
/*     */ 
/*     */   
/*     */   public CauldronBlock(BlockBehaviour.Properties debug1) {
/*  61 */     super(debug1);
/*  62 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)LEVEL, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  67 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getInteractionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/*  72 */     return INSIDE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void entityInside(BlockState debug1, Level debug2, BlockPos debug3, Entity debug4) {
/*  77 */     int debug5 = ((Integer)debug1.getValue((Property)LEVEL)).intValue();
/*  78 */     float debug6 = debug3.getY() + (6.0F + (3 * debug5)) / 16.0F;
/*     */     
/*  80 */     if (!debug2.isClientSide && debug4.isOnFire() && debug5 > 0 && debug4.getY() <= debug6) {
/*  81 */       debug4.clearFire();
/*     */       
/*  83 */       setWaterLevel(debug2, debug3, debug1, debug5 - 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  89 */     ItemStack debug7 = debug4.getItemInHand(debug5);
/*  90 */     if (debug7.isEmpty()) {
/*  91 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/*  94 */     int debug8 = ((Integer)debug1.getValue((Property)LEVEL)).intValue();
/*  95 */     Item debug9 = debug7.getItem();
/*  96 */     if (debug9 == Items.WATER_BUCKET) {
/*  97 */       if (debug8 < 3 && !debug2.isClientSide) {
/*  98 */         if (!debug4.abilities.instabuild) {
/*  99 */           debug4.setItemInHand(debug5, new ItemStack((ItemLike)Items.BUCKET));
/*     */         }
/* 101 */         debug4.awardStat(Stats.FILL_CAULDRON);
/*     */         
/* 103 */         setWaterLevel(debug2, debug3, debug1, 3);
/* 104 */         debug2.playSound(null, debug3, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */       } 
/* 106 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */     } 
/*     */     
/* 109 */     if (debug9 == Items.BUCKET) {
/* 110 */       if (debug8 == 3 && !debug2.isClientSide) {
/* 111 */         if (!debug4.abilities.instabuild) {
/* 112 */           debug7.shrink(1);
/* 113 */           if (debug7.isEmpty()) {
/* 114 */             debug4.setItemInHand(debug5, new ItemStack((ItemLike)Items.WATER_BUCKET));
/* 115 */           } else if (!debug4.inventory.add(new ItemStack((ItemLike)Items.WATER_BUCKET))) {
/* 116 */             debug4.drop(new ItemStack((ItemLike)Items.WATER_BUCKET), false);
/*     */           } 
/*     */         } 
/* 119 */         debug4.awardStat(Stats.USE_CAULDRON);
/*     */         
/* 121 */         setWaterLevel(debug2, debug3, debug1, 0);
/* 122 */         debug2.playSound(null, debug3, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */       } 
/* 124 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */     } 
/*     */     
/* 127 */     if (debug9 == Items.GLASS_BOTTLE) {
/* 128 */       if (debug8 > 0 && !debug2.isClientSide) {
/* 129 */         if (!debug4.abilities.instabuild) {
/* 130 */           ItemStack debug10 = PotionUtils.setPotion(new ItemStack((ItemLike)Items.POTION), Potions.WATER);
/* 131 */           debug4.awardStat(Stats.USE_CAULDRON);
/*     */           
/* 133 */           debug7.shrink(1);
/* 134 */           if (debug7.isEmpty()) {
/* 135 */             debug4.setItemInHand(debug5, debug10);
/* 136 */           } else if (!debug4.inventory.add(debug10)) {
/* 137 */             debug4.drop(debug10, false);
/* 138 */           } else if (debug4 instanceof ServerPlayer) {
/* 139 */             ((ServerPlayer)debug4).refreshContainer((AbstractContainerMenu)debug4.inventoryMenu);
/*     */           } 
/*     */         } 
/*     */         
/* 143 */         debug2.playSound(null, debug3, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 144 */         setWaterLevel(debug2, debug3, debug1, debug8 - 1);
/*     */       } 
/*     */       
/* 147 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */     } 
/*     */     
/* 150 */     if (debug9 == Items.POTION && PotionUtils.getPotion(debug7) == Potions.WATER) {
/* 151 */       if (debug8 < 3 && !debug2.isClientSide) {
/* 152 */         if (!debug4.abilities.instabuild) {
/* 153 */           ItemStack debug10 = new ItemStack((ItemLike)Items.GLASS_BOTTLE);
/* 154 */           debug4.awardStat(Stats.USE_CAULDRON);
/*     */           
/* 156 */           debug4.setItemInHand(debug5, debug10);
/*     */           
/* 158 */           if (debug4 instanceof ServerPlayer) {
/* 159 */             ((ServerPlayer)debug4).refreshContainer((AbstractContainerMenu)debug4.inventoryMenu);
/*     */           }
/*     */         } 
/*     */         
/* 163 */         debug2.playSound(null, debug3, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 164 */         setWaterLevel(debug2, debug3, debug1, debug8 + 1);
/*     */       } 
/*     */       
/* 167 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */     } 
/*     */     
/* 170 */     if (debug8 > 0 && debug9 instanceof DyeableLeatherItem) {
/* 171 */       DyeableLeatherItem debug10 = (DyeableLeatherItem)debug9;
/*     */       
/* 173 */       if (debug10.hasCustomColor(debug7) && !debug2.isClientSide) {
/* 174 */         debug10.clearColor(debug7);
/* 175 */         setWaterLevel(debug2, debug3, debug1, debug8 - 1);
/* 176 */         debug4.awardStat(Stats.CLEAN_ARMOR);
/* 177 */         return InteractionResult.SUCCESS;
/*     */       } 
/*     */     } 
/*     */     
/* 181 */     if (debug8 > 0 && debug9 instanceof net.minecraft.world.item.BannerItem) {
/* 182 */       if (BannerBlockEntity.getPatternCount(debug7) > 0 && !debug2.isClientSide) {
/* 183 */         ItemStack debug10 = debug7.copy();
/* 184 */         debug10.setCount(1);
/* 185 */         BannerBlockEntity.removeLastPattern(debug10);
/* 186 */         debug4.awardStat(Stats.CLEAN_BANNER);
/*     */ 
/*     */         
/* 189 */         if (!debug4.abilities.instabuild) {
/* 190 */           debug7.shrink(1);
/* 191 */           setWaterLevel(debug2, debug3, debug1, debug8 - 1);
/*     */         } 
/* 193 */         if (debug7.isEmpty()) {
/* 194 */           debug4.setItemInHand(debug5, debug10);
/* 195 */         } else if (!debug4.inventory.add(debug10)) {
/* 196 */           debug4.drop(debug10, false);
/* 197 */         } else if (debug4 instanceof ServerPlayer) {
/* 198 */           ((ServerPlayer)debug4).refreshContainer((AbstractContainerMenu)debug4.inventoryMenu);
/*     */         } 
/*     */       } 
/* 201 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */     } 
/*     */     
/* 204 */     if (debug8 > 0 && debug9 instanceof BlockItem) {
/* 205 */       Block debug10 = ((BlockItem)debug9).getBlock();
/* 206 */       if (debug10 instanceof ShulkerBoxBlock && !debug2.isClientSide()) {
/* 207 */         ItemStack debug11 = new ItemStack(Blocks.SHULKER_BOX, 1);
/* 208 */         if (debug7.hasTag()) {
/* 209 */           debug11.setTag(debug7.getTag().copy());
/*     */         }
/* 211 */         debug4.setItemInHand(debug5, debug11);
/* 212 */         setWaterLevel(debug2, debug3, debug1, debug8 - 1);
/* 213 */         debug4.awardStat(Stats.CLEAN_SHULKER_BOX);
/* 214 */         return InteractionResult.SUCCESS;
/*     */       } 
/* 216 */       return InteractionResult.CONSUME;
/*     */     } 
/*     */     
/* 219 */     return InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public void setWaterLevel(Level debug1, BlockPos debug2, BlockState debug3, int debug4) {
/* 223 */     debug1.setBlock(debug2, (BlockState)debug3.setValue((Property)LEVEL, Integer.valueOf(Mth.clamp(debug4, 0, 3))), 2);
/* 224 */     debug1.updateNeighbourForOutputSignal(debug2, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleRain(Level debug1, BlockPos debug2) {
/* 229 */     if (debug1.random.nextInt(20) != 1) {
/*     */       return;
/*     */     }
/*     */     
/* 233 */     float debug3 = debug1.getBiome(debug2).getTemperature(debug2);
/* 234 */     if (debug3 < 0.15F) {
/*     */       return;
/*     */     }
/*     */     
/* 238 */     BlockState debug4 = debug1.getBlockState(debug2);
/* 239 */     if (((Integer)debug4.getValue((Property)LEVEL)).intValue() < 3) {
/* 240 */       debug1.setBlock(debug2, (BlockState)debug4.cycle((Property)LEVEL), 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnalogOutputSignal(BlockState debug1) {
/* 246 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAnalogOutputSignal(BlockState debug1, Level debug2, BlockPos debug3) {
/* 251 */     return ((Integer)debug1.getValue((Property)LEVEL)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 256 */     debug1.add(new Property[] { (Property)LEVEL });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 261 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\CauldronBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */