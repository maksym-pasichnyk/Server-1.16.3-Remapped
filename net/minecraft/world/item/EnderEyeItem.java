/*     */ package net.minecraft.world.item;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.InteractionResultHolder;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.EyeOfEnder;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.EndPortalFrameBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPattern;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class EnderEyeItem extends Item {
/*     */   public EnderEyeItem(Item.Properties debug1) {
/*  30 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext debug1) {
/*  35 */     Level debug2 = debug1.getLevel();
/*  36 */     BlockPos debug3 = debug1.getClickedPos();
/*     */     
/*  38 */     BlockState debug4 = debug2.getBlockState(debug3);
/*     */     
/*  40 */     if (!debug4.is(Blocks.END_PORTAL_FRAME) || ((Boolean)debug4.getValue((Property)EndPortalFrameBlock.HAS_EYE)).booleanValue()) {
/*  41 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/*  44 */     if (debug2.isClientSide) {
/*  45 */       return InteractionResult.SUCCESS;
/*     */     }
/*     */     
/*  48 */     BlockState debug5 = (BlockState)debug4.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(true));
/*  49 */     Block.pushEntitiesUp(debug4, debug5, debug2, debug3);
/*  50 */     debug2.setBlock(debug3, debug5, 2);
/*  51 */     debug2.updateNeighbourForOutputSignal(debug3, Blocks.END_PORTAL_FRAME);
/*  52 */     debug1.getItemInHand().shrink(1);
/*     */     
/*  54 */     debug2.levelEvent(1503, debug3, 0);
/*     */ 
/*     */     
/*  57 */     BlockPattern.BlockPatternMatch debug6 = EndPortalFrameBlock.getOrCreatePortalShape().find((LevelReader)debug2, debug3);
/*  58 */     if (debug6 != null) {
/*  59 */       BlockPos debug7 = debug6.getFrontTopLeft().offset(-3, 0, -3);
/*  60 */       for (int debug8 = 0; debug8 < 3; debug8++) {
/*  61 */         for (int debug9 = 0; debug9 < 3; debug9++) {
/*  62 */           debug2.setBlock(debug7.offset(debug8, 0, debug9), Blocks.END_PORTAL.defaultBlockState(), 2);
/*     */         }
/*     */       } 
/*  65 */       debug2.globalLevelEvent(1038, debug7.offset(1, 0, 1), 0);
/*     */     } 
/*     */     
/*  68 */     return InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/*  73 */     ItemStack debug4 = debug2.getItemInHand(debug3);
/*  74 */     BlockHitResult blockHitResult = getPlayerPOVHitResult(debug1, debug2, ClipContext.Fluid.NONE);
/*  75 */     if (blockHitResult.getType() == HitResult.Type.BLOCK && 
/*  76 */       debug1.getBlockState(blockHitResult.getBlockPos()).is(Blocks.END_PORTAL_FRAME)) {
/*  77 */       return InteractionResultHolder.pass(debug4);
/*     */     }
/*     */ 
/*     */     
/*  81 */     debug2.startUsingItem(debug3);
/*  82 */     if (debug1 instanceof ServerLevel) {
/*  83 */       BlockPos debug6 = ((ServerLevel)debug1).getChunkSource().getGenerator().findNearestMapFeature((ServerLevel)debug1, StructureFeature.STRONGHOLD, debug2.blockPosition(), 100, false);
/*  84 */       if (debug6 != null) {
/*  85 */         EyeOfEnder debug7 = new EyeOfEnder(debug1, debug2.getX(), debug2.getY(0.5D), debug2.getZ());
/*  86 */         debug7.setItem(debug4);
/*  87 */         debug7.signalTo(debug6);
/*  88 */         debug1.addFreshEntity((Entity)debug7);
/*     */         
/*  90 */         if (debug2 instanceof ServerPlayer) {
/*  91 */           CriteriaTriggers.USED_ENDER_EYE.trigger((ServerPlayer)debug2, debug6);
/*     */         }
/*     */         
/*  94 */         debug1.playSound(null, debug2.getX(), debug2.getY(), debug2.getZ(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
/*  95 */         debug1.levelEvent(null, 1003, debug2.blockPosition(), 0);
/*  96 */         if (!debug2.abilities.instabuild) {
/*  97 */           debug4.shrink(1);
/*     */         }
/*  99 */         debug2.awardStat(Stats.ITEM_USED.get(this));
/* 100 */         debug2.swing(debug3, true);
/* 101 */         return InteractionResultHolder.success(debug4);
/*     */       } 
/*     */     } 
/* 104 */     return InteractionResultHolder.consume(debug4);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\EnderEyeItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */