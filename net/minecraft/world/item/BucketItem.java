/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResultHolder;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.BucketPickup;
/*     */ import net.minecraft.world.level.block.LiquidBlockContainer;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FlowingFluid;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.material.Material;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ 
/*     */ public class BucketItem extends Item {
/*     */   public BucketItem(Fluid debug1, Item.Properties debug2) {
/*  36 */     super(debug2);
/*  37 */     this.content = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/*  42 */     ItemStack debug4 = debug2.getItemInHand(debug3);
/*  43 */     BlockHitResult blockHitResult = getPlayerPOVHitResult(debug1, debug2, (this.content == Fluids.EMPTY) ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
/*  44 */     if (blockHitResult.getType() == HitResult.Type.MISS) {
/*  45 */       return InteractionResultHolder.pass(debug4);
/*     */     }
/*     */     
/*  48 */     if (blockHitResult.getType() == HitResult.Type.BLOCK) {
/*  49 */       BlockHitResult debug6 = blockHitResult;
/*  50 */       BlockPos debug7 = debug6.getBlockPos();
/*  51 */       Direction debug8 = debug6.getDirection();
/*  52 */       BlockPos debug9 = debug7.relative(debug8);
/*     */       
/*  54 */       if (!debug1.mayInteract(debug2, debug7) || !debug2.mayUseItemAt(debug9, debug8, debug4)) {
/*  55 */         return InteractionResultHolder.fail(debug4);
/*     */       }
/*     */       
/*  58 */       if (this.content == Fluids.EMPTY) {
/*  59 */         BlockState blockState = debug1.getBlockState(debug7);
/*     */         
/*  61 */         if (blockState.getBlock() instanceof BucketPickup) {
/*  62 */           Fluid fluid = ((BucketPickup)blockState.getBlock()).takeLiquid((LevelAccessor)debug1, debug7, blockState);
/*  63 */           if (fluid != Fluids.EMPTY) {
/*  64 */             debug2.awardStat(Stats.ITEM_USED.get(this));
/*  65 */             debug2.playSound(fluid.is((Tag)FluidTags.LAVA) ? SoundEvents.BUCKET_FILL_LAVA : SoundEvents.BUCKET_FILL, 1.0F, 1.0F);
/*  66 */             ItemStack debug12 = ItemUtils.createFilledResult(debug4, debug2, new ItemStack(fluid.getBucket()));
/*  67 */             if (!debug1.isClientSide) {
/*  68 */               CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)debug2, new ItemStack(fluid.getBucket()));
/*     */             }
/*  70 */             return InteractionResultHolder.sidedSuccess(debug12, debug1.isClientSide());
/*     */           } 
/*     */         } 
/*     */         
/*  74 */         return InteractionResultHolder.fail(debug4);
/*     */       } 
/*  76 */       BlockState debug10 = debug1.getBlockState(debug7);
/*  77 */       BlockPos debug11 = (debug10.getBlock() instanceof LiquidBlockContainer && this.content == Fluids.WATER) ? debug7 : debug9;
/*     */       
/*  79 */       if (emptyBucket(debug2, debug1, debug11, debug6)) {
/*  80 */         checkExtraContent(debug1, debug4, debug11);
/*  81 */         if (debug2 instanceof ServerPlayer) {
/*  82 */           CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)debug2, debug11, debug4);
/*     */         }
/*  84 */         debug2.awardStat(Stats.ITEM_USED.get(this));
/*  85 */         return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(debug4, debug2), debug1.isClientSide());
/*     */       } 
/*  87 */       return InteractionResultHolder.fail(debug4);
/*     */     } 
/*     */ 
/*     */     
/*  91 */     return InteractionResultHolder.pass(debug4);
/*     */   }
/*     */   private final Fluid content;
/*     */   protected ItemStack getEmptySuccessItem(ItemStack debug1, Player debug2) {
/*  95 */     if (!debug2.abilities.instabuild) {
/*  96 */       return new ItemStack(Items.BUCKET);
/*     */     }
/*  98 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkExtraContent(Level debug1, ItemStack debug2, BlockPos debug3) {}
/*     */   
/*     */   public boolean emptyBucket(@Nullable Player debug1, Level debug2, BlockPos debug3, @Nullable BlockHitResult debug4) {
/* 105 */     if (!(this.content instanceof FlowingFluid)) {
/* 106 */       return false;
/*     */     }
/*     */     
/* 109 */     BlockState debug5 = debug2.getBlockState(debug3);
/* 110 */     Block debug6 = debug5.getBlock();
/* 111 */     Material debug7 = debug5.getMaterial();
/* 112 */     boolean debug8 = debug5.canBeReplaced(this.content);
/*     */ 
/*     */     
/* 115 */     boolean debug9 = (debug5.isAir() || debug8 || (debug6 instanceof LiquidBlockContainer && ((LiquidBlockContainer)debug6).canPlaceLiquid((BlockGetter)debug2, debug3, debug5, this.content)));
/*     */     
/* 117 */     if (!debug9)
/*     */     {
/*     */       
/* 120 */       return (debug4 != null && emptyBucket(debug1, debug2, debug4.getBlockPos().relative(debug4.getDirection()), (BlockHitResult)null));
/*     */     }
/*     */     
/* 123 */     if (debug2.dimensionType().ultraWarm() && this.content.is((Tag)FluidTags.WATER)) {
/* 124 */       int debug10 = debug3.getX();
/* 125 */       int debug11 = debug3.getY();
/* 126 */       int debug12 = debug3.getZ();
/*     */       
/* 128 */       debug2.playSound(debug1, debug3, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (debug2.random.nextFloat() - debug2.random.nextFloat()) * 0.8F);
/*     */       
/* 130 */       for (int debug13 = 0; debug13 < 8; debug13++) {
/* 131 */         debug2.addParticle((ParticleOptions)ParticleTypes.LARGE_SMOKE, debug10 + Math.random(), debug11 + Math.random(), debug12 + Math.random(), 0.0D, 0.0D, 0.0D);
/*     */       }
/* 133 */       return true;
/*     */     } 
/*     */     
/* 136 */     if (debug6 instanceof LiquidBlockContainer && this.content == Fluids.WATER) {
/* 137 */       ((LiquidBlockContainer)debug6).placeLiquid((LevelAccessor)debug2, debug3, debug5, ((FlowingFluid)this.content).getSource(false));
/* 138 */       playEmptySound(debug1, (LevelAccessor)debug2, debug3);
/* 139 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 143 */     if (!debug2.isClientSide && debug8 && !debug7.isLiquid()) {
/* 144 */       debug2.destroyBlock(debug3, true);
/*     */     }
/*     */ 
/*     */     
/* 148 */     if (debug2.setBlock(debug3, this.content.defaultFluidState().createLegacyBlock(), 11) || debug5.getFluidState().isSource()) {
/* 149 */       playEmptySound(debug1, (LevelAccessor)debug2, debug3);
/* 150 */       return true;
/*     */     } 
/*     */     
/* 153 */     return false;
/*     */   }
/*     */   
/*     */   protected void playEmptySound(@Nullable Player debug1, LevelAccessor debug2, BlockPos debug3) {
/* 157 */     SoundEvent debug4 = this.content.is((Tag)FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
/* 158 */     debug2.playSound(debug1, debug3, debug4, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\BucketItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */