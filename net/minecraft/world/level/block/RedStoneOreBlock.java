/*     */ package net.minecraft.world.level.block;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.DustParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class RedStoneOreBlock extends Block {
/*  25 */   public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
/*     */   
/*     */   public RedStoneOreBlock(BlockBehaviour.Properties debug1) {
/*  28 */     super(debug1);
/*  29 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)LIT, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void attack(BlockState debug1, Level debug2, BlockPos debug3, Player debug4) {
/*  34 */     interact(debug1, debug2, debug3);
/*  35 */     super.attack(debug1, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stepOn(Level debug1, BlockPos debug2, Entity debug3) {
/*  40 */     interact(debug1.getBlockState(debug2), debug1, debug2);
/*  41 */     super.stepOn(debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  46 */     if (debug2.isClientSide) {
/*  47 */       spawnParticles(debug2, debug3);
/*     */     } else {
/*  49 */       interact(debug1, debug2, debug3);
/*     */     } 
/*     */ 
/*     */     
/*  53 */     ItemStack debug7 = debug4.getItemInHand(debug5);
/*  54 */     if (debug7.getItem() instanceof net.minecraft.world.item.BlockItem && (new BlockPlaceContext(debug4, debug5, debug7, debug6)).canPlace()) {
/*  55 */       return InteractionResult.PASS;
/*     */     }
/*  57 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private static void interact(BlockState debug0, Level debug1, BlockPos debug2) {
/*  61 */     spawnParticles(debug1, debug2);
/*  62 */     if (!((Boolean)debug0.getValue((Property)LIT)).booleanValue()) {
/*  63 */       debug1.setBlock(debug2, (BlockState)debug0.setValue((Property)LIT, Boolean.valueOf(true)), 3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRandomlyTicking(BlockState debug1) {
/*  69 */     return ((Boolean)debug1.getValue((Property)LIT)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  74 */     if (((Boolean)debug1.getValue((Property)LIT)).booleanValue()) {
/*  75 */       debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)LIT, Boolean.valueOf(false)), 3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnAfterBreak(BlockState debug1, ServerLevel debug2, BlockPos debug3, ItemStack debug4) {
/*  81 */     super.spawnAfterBreak(debug1, debug2, debug3, debug4);
/*     */     
/*  83 */     if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, debug4) == 0) {
/*     */       
/*  85 */       int debug5 = 1 + debug2.random.nextInt(5);
/*  86 */       popExperience(debug2, debug3, debug5);
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
/*     */   private static void spawnParticles(Level debug0, BlockPos debug1) {
/*  98 */     double debug2 = 0.5625D;
/*  99 */     Random debug4 = debug0.random;
/* 100 */     for (Direction debug8 : Direction.values()) {
/* 101 */       BlockPos debug9 = debug1.relative(debug8);
/* 102 */       if (!debug0.getBlockState(debug9).isSolidRender((BlockGetter)debug0, debug9)) {
/*     */ 
/*     */ 
/*     */         
/* 106 */         Direction.Axis debug10 = debug8.getAxis();
/* 107 */         double debug11 = (debug10 == Direction.Axis.X) ? (0.5D + 0.5625D * debug8.getStepX()) : debug4.nextFloat();
/* 108 */         double debug13 = (debug10 == Direction.Axis.Y) ? (0.5D + 0.5625D * debug8.getStepY()) : debug4.nextFloat();
/* 109 */         double debug15 = (debug10 == Direction.Axis.Z) ? (0.5D + 0.5625D * debug8.getStepZ()) : debug4.nextFloat();
/*     */         
/* 111 */         debug0.addParticle((ParticleOptions)DustParticleOptions.REDSTONE, debug1.getX() + debug11, debug1.getY() + debug13, debug1.getZ() + debug15, 0.0D, 0.0D, 0.0D);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 117 */     debug1.add(new Property[] { (Property)LIT });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\RedStoneOreBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */