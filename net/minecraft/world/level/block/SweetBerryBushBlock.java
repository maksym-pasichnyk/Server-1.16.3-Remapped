/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SweetBerryBushBlock
/*     */   extends BushBlock implements BonemealableBlock {
/*  32 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
/*     */   
/*  34 */   private static final VoxelShape SAPLING_SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D);
/*  35 */   private static final VoxelShape MID_GROWTH_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
/*     */   
/*     */   public SweetBerryBushBlock(BlockBehaviour.Properties debug1) {
/*  38 */     super(debug1);
/*  39 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  49 */     if (((Integer)debug1.getValue((Property)AGE)).intValue() == 0)
/*  50 */       return SAPLING_SHAPE; 
/*  51 */     if (((Integer)debug1.getValue((Property)AGE)).intValue() < 3) {
/*  52 */       return MID_GROWTH_SHAPE;
/*     */     }
/*     */     
/*  55 */     return super.getShape(debug1, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRandomlyTicking(BlockState debug1) {
/*  60 */     return (((Integer)debug1.getValue((Property)AGE)).intValue() < 3);
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  65 */     int debug5 = ((Integer)debug1.getValue((Property)AGE)).intValue();
/*  66 */     if (debug5 < 3 && debug4.nextInt(5) == 0 && debug2.getRawBrightness(debug3.above(), 0) >= 9) {
/*  67 */       debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)AGE, Integer.valueOf(debug5 + 1)), 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void entityInside(BlockState debug1, Level debug2, BlockPos debug3, Entity debug4) {
/*  73 */     if (!(debug4 instanceof net.minecraft.world.entity.LivingEntity) || debug4.getType() == EntityType.FOX || debug4.getType() == EntityType.BEE) {
/*     */       return;
/*     */     }
/*  76 */     debug4.makeStuckInBlock(debug1, new Vec3(0.800000011920929D, 0.75D, 0.800000011920929D));
/*     */ 
/*     */     
/*  79 */     if (!debug2.isClientSide && ((Integer)debug1.getValue((Property)AGE)).intValue() > 0 && (debug4.xOld != debug4.getX() || debug4.zOld != debug4.getZ())) {
/*  80 */       double debug5 = Math.abs(debug4.getX() - debug4.xOld);
/*  81 */       double debug7 = Math.abs(debug4.getZ() - debug4.zOld);
/*     */       
/*  83 */       if (debug5 >= 0.003000000026077032D || debug7 >= 0.003000000026077032D) {
/*  84 */         debug4.hurt(DamageSource.SWEET_BERRY_BUSH, 1.0F);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  91 */     int debug7 = ((Integer)debug1.getValue((Property)AGE)).intValue();
/*  92 */     boolean debug8 = (debug7 == 3);
/*     */     
/*  94 */     if (!debug8 && debug4.getItemInHand(debug5).getItem() == Items.BONE_MEAL) {
/*  95 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/*  98 */     if (debug7 > 1) {
/*  99 */       int debug9 = 1 + debug2.random.nextInt(2);
/* 100 */       popResource(debug2, debug3, new ItemStack((ItemLike)Items.SWEET_BERRIES, debug9 + (debug8 ? 1 : 0)));
/* 101 */       debug2.playSound(null, debug3, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + debug2.random.nextFloat() * 0.4F);
/* 102 */       debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)AGE, Integer.valueOf(1)), 2);
/* 103 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */     } 
/*     */     
/* 106 */     return super.use(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 111 */     debug1.add(new Property[] { (Property)AGE });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(BlockGetter debug1, BlockPos debug2, BlockState debug3, boolean debug4) {
/* 116 */     return (((Integer)debug3.getValue((Property)AGE)).intValue() < 3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 121 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 126 */     int debug5 = Math.min(3, ((Integer)debug4.getValue((Property)AGE)).intValue() + 1);
/* 127 */     debug1.setBlock(debug3, (BlockState)debug4.setValue((Property)AGE, Integer.valueOf(debug5)), 2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\SweetBerryBushBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */