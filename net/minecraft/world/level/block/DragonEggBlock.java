/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class DragonEggBlock extends FallingBlock {
/* 18 */   protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
/*    */   
/*    */   public DragonEggBlock(BlockBehaviour.Properties debug1) {
/* 21 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 26 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 31 */     teleport(debug1, debug2, debug3);
/* 32 */     return InteractionResult.sidedSuccess(debug2.isClientSide);
/*    */   }
/*    */ 
/*    */   
/*    */   public void attack(BlockState debug1, Level debug2, BlockPos debug3, Player debug4) {
/* 37 */     teleport(debug1, debug2, debug3);
/*    */   }
/*    */   
/*    */   private void teleport(BlockState debug1, Level debug2, BlockPos debug3) {
/* 41 */     for (int debug4 = 0; debug4 < 1000; debug4++) {
/* 42 */       BlockPos debug5 = debug3.offset(debug2.random.nextInt(16) - debug2.random.nextInt(16), debug2.random.nextInt(8) - debug2.random.nextInt(8), debug2.random.nextInt(16) - debug2.random.nextInt(16));
/* 43 */       if (debug2.getBlockState(debug5).isAir()) {
/* 44 */         if (debug2.isClientSide) {
/* 45 */           for (int debug6 = 0; debug6 < 128; debug6++) {
/* 46 */             double debug7 = debug2.random.nextDouble();
/* 47 */             float debug9 = (debug2.random.nextFloat() - 0.5F) * 0.2F;
/* 48 */             float debug10 = (debug2.random.nextFloat() - 0.5F) * 0.2F;
/* 49 */             float debug11 = (debug2.random.nextFloat() - 0.5F) * 0.2F;
/*    */             
/* 51 */             double debug12 = Mth.lerp(debug7, debug5.getX(), debug3.getX()) + debug2.random.nextDouble() - 0.5D + 0.5D;
/* 52 */             double debug14 = Mth.lerp(debug7, debug5.getY(), debug3.getY()) + debug2.random.nextDouble() - 0.5D;
/* 53 */             double debug16 = Mth.lerp(debug7, debug5.getZ(), debug3.getZ()) + debug2.random.nextDouble() - 0.5D + 0.5D;
/* 54 */             debug2.addParticle((ParticleOptions)ParticleTypes.PORTAL, debug12, debug14, debug16, debug9, debug10, debug11);
/*    */           } 
/*    */         } else {
/* 57 */           debug2.setBlock(debug5, debug1, 2);
/* 58 */           debug2.removeBlock(debug3, false);
/*    */         } 
/*    */         return;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getDelayAfterPlace() {
/* 67 */     return 5;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 72 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\DragonEggBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */