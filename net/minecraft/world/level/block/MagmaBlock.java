/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class MagmaBlock extends Block {
/*    */   public MagmaBlock(BlockBehaviour.Properties debug1) {
/* 24 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stepOn(Level debug1, BlockPos debug2, Entity debug3) {
/* 29 */     if (!debug3.fireImmune() && debug3 instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)debug3)) {
/* 30 */       debug3.hurt(DamageSource.HOT_FLOOR, 1.0F);
/*    */     }
/*    */     
/* 33 */     super.stepOn(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 38 */     BubbleColumnBlock.growColumn((LevelAccessor)debug2, debug3.above(), true);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 43 */     if (debug2 == Direction.UP && debug3.is(Blocks.WATER)) {
/* 44 */       debug4.getBlockTicks().scheduleTick(debug5, this, 20);
/*    */     }
/*    */     
/* 47 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 52 */     BlockPos debug5 = debug3.above();
/* 53 */     if (debug2.getFluidState(debug3).is((Tag)FluidTags.WATER)) {
/* 54 */       debug2.playSound(null, debug3, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (debug2.random.nextFloat() - debug2.random.nextFloat()) * 0.8F);
/* 55 */       debug2.sendParticles((ParticleOptions)ParticleTypes.LARGE_SMOKE, debug5.getX() + 0.5D, debug5.getY() + 0.25D, debug5.getZ() + 0.5D, 8, 0.5D, 0.25D, 0.5D, 0.0D);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 61 */     debug2.getBlockTicks().scheduleTick(debug3, this, 20);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\MagmaBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */