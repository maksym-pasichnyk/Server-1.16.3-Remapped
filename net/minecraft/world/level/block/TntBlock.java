/*     */ package net.minecraft.world.level.block;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.item.PrimedTnt;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class TntBlock extends Block {
/*  27 */   public static final BooleanProperty UNSTABLE = BlockStateProperties.UNSTABLE;
/*     */   
/*     */   public TntBlock(BlockBehaviour.Properties debug1) {
/*  30 */     super(debug1);
/*  31 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)UNSTABLE, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/*  36 */     if (debug4.is(debug1.getBlock())) {
/*     */       return;
/*     */     }
/*  39 */     if (debug2.hasNeighborSignal(debug3)) {
/*  40 */       explode(debug2, debug3);
/*  41 */       debug2.removeBlock(debug3, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void neighborChanged(BlockState debug1, Level debug2, BlockPos debug3, Block debug4, BlockPos debug5, boolean debug6) {
/*  47 */     if (debug2.hasNeighborSignal(debug3)) {
/*  48 */       explode(debug2, debug3);
/*  49 */       debug2.removeBlock(debug3, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerWillDestroy(Level debug1, BlockPos debug2, BlockState debug3, Player debug4) {
/*  55 */     if (!debug1.isClientSide() && !debug4.isCreative() && ((Boolean)debug3.getValue((Property)UNSTABLE)).booleanValue()) {
/*  56 */       explode(debug1, debug2);
/*     */     }
/*     */     
/*  59 */     super.playerWillDestroy(debug1, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public void wasExploded(Level debug1, BlockPos debug2, Explosion debug3) {
/*  64 */     if (debug1.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/*  68 */     PrimedTnt debug4 = new PrimedTnt(debug1, debug2.getX() + 0.5D, debug2.getY(), debug2.getZ() + 0.5D, debug3.getSourceMob());
/*  69 */     debug4.setFuse((short)(debug1.random.nextInt(debug4.getLife() / 4) + debug4.getLife() / 8));
/*  70 */     debug1.addFreshEntity((Entity)debug4);
/*     */   }
/*     */   
/*     */   public static void explode(Level debug0, BlockPos debug1) {
/*  74 */     explode(debug0, debug1, (LivingEntity)null);
/*     */   }
/*     */   
/*     */   private static void explode(Level debug0, BlockPos debug1, @Nullable LivingEntity debug2) {
/*  78 */     if (debug0.isClientSide) {
/*     */       return;
/*     */     }
/*  81 */     PrimedTnt debug3 = new PrimedTnt(debug0, debug1.getX() + 0.5D, debug1.getY(), debug1.getZ() + 0.5D, debug2);
/*  82 */     debug0.addFreshEntity((Entity)debug3);
/*  83 */     debug0.playSound(null, debug3.getX(), debug3.getY(), debug3.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  88 */     ItemStack debug7 = debug4.getItemInHand(debug5);
/*  89 */     Item debug8 = debug7.getItem();
/*  90 */     if (debug8 == Items.FLINT_AND_STEEL || debug8 == Items.FIRE_CHARGE) {
/*  91 */       explode(debug2, debug3, (LivingEntity)debug4);
/*  92 */       debug2.setBlock(debug3, Blocks.AIR.defaultBlockState(), 11);
/*     */       
/*  94 */       if (!debug4.isCreative()) {
/*  95 */         if (debug8 == Items.FLINT_AND_STEEL) {
/*  96 */           debug7.hurtAndBreak(1, (LivingEntity)debug4, debug1 -> debug1.broadcastBreakEvent(debug0));
/*     */         } else {
/*  98 */           debug7.shrink(1);
/*     */         } 
/*     */       }
/* 101 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */     } 
/* 103 */     return super.use(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onProjectileHit(Level debug1, BlockState debug2, BlockHitResult debug3, Projectile debug4) {
/* 108 */     if (!debug1.isClientSide) {
/* 109 */       Entity debug5 = debug4.getOwner();
/* 110 */       if (debug4.isOnFire()) {
/* 111 */         BlockPos debug6 = debug3.getBlockPos();
/* 112 */         explode(debug1, debug6, (debug5 instanceof LivingEntity) ? (LivingEntity)debug5 : null);
/* 113 */         debug1.removeBlock(debug6, false);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean dropFromExplosion(Explosion debug1) {
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 125 */     debug1.add(new Property[] { (Property)UNSTABLE });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\TntBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */